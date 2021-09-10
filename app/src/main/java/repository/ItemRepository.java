package repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;


import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dao.ItemsDao;
import dao.MetadataDao;
import dao.S3BasedPayloadDao;
import db.ItemDatabase;
import model.Item;
import model.Metadata;

/*
Separate Threads used in the following activities, to avoid blocking
the main(UI) thread:
- Data insertion
- Data retrieval
Note: Usage can be found  in ItemRepository class
Repository decides if the data is to be used from S3 or Room(local database)
 */

public class ItemRepository {
    private static final String PRESIGNED_URL = "https://fetch-hiring.s3.amazonaws.com/hiring.json";
    private static final String TAG = ItemRepository.class.getSimpleName();
    public static final int MAX_THREADS = 5;
    private ItemsDao itemsDao;

    private MetadataDao metadataDao;

    private S3BasedPayloadDao s3Dao;

    private ExecutorService executor; //for spawning separate threads

    public ItemRepository(Application application) {
        ItemDatabase db = ItemDatabase.getDatabase(application);
        itemsDao = db.itemsDao();
        metadataDao = db.metadataDao();
        s3Dao = new S3BasedPayloadDao();
        executor  = Executors.newFixedThreadPool(MAX_THREADS);
    }

    public LiveData<List<Integer>> getListIds() {
        checkForUpdates();
        return itemsDao.getListIds();
    }

    private void checkForUpdates() {
        Long latestTimestamp = null;
        try {
            latestTimestamp = ItemDatabase.databaseWriteExecutor.submit(() -> metadataDao.getLatestTimestamp()).get();
        } catch (ExecutionException e) {
            Log.e(TAG, "Received execution exception "+e.getMessage());
        } catch (InterruptedException e) {
            Log.e(TAG, "Received interrupted exception "+e.getMessage());
        }

        Long lastModified = null;
        try {
            lastModified = executor.submit(() -> s3Dao.readFileMetadata(PRESIGNED_URL)).get();
        } catch (ExecutionException e) {
            Log.e(TAG, "Received execution exception "+e.getMessage());
        } catch (InterruptedException e) {
            Log.e(TAG, "Received interrupted exception "+e.getMessage());
        } finally {
            if(lastModified != null && (latestTimestamp == null ||  lastModified > latestTimestamp)){
                insertItemObjects();
                long finalLastModified = lastModified;
                ItemDatabase.databaseWriteExecutor.execute(() -> metadataDao.insert(new Metadata(finalLastModified)));;
            }
        }

    }

    public LiveData<List<String>> getItemNames(int listId) { //todo - change int for overflow
        checkForUpdates();
        return itemsDao.getNames(listId);
    }

    private void insertItemObjects(){
        try {
            List<Item> items = executor.submit(() -> s3Dao.readData(PRESIGNED_URL)).get();
            for(int i=0; i<items.size(); i++){
                final Item item = items.get(i);
                ItemDatabase.databaseWriteExecutor.execute(() -> itemsDao.insert(item));// separate thread
            }
        } catch (InterruptedException e) {
            Log.e(TAG, "Received interrupted exception "+e.getMessage());
        } catch (ExecutionException e) {
            Log.e(TAG, "Received execution exception "+e.getMessage());
        }
    }
}