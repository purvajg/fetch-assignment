package repository;

import android.app.Application;

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

public class ItemRepository {
    private static final String PRESIGNED_URL = "https://fetch-hiring.s3.amazonaws.com/hiring.json";

    private ItemsDao itemsDao;

    private MetadataDao metadataDao;

    private S3BasedPayloadDao s3Dao;

    private ExecutorService executor;

    public ItemRepository(Application application) {
        ItemDatabase db = ItemDatabase.getDatabase(application);
        itemsDao = db.itemsDao();
        metadataDao = db.metadataDao();
        s3Dao = new S3BasedPayloadDao();
        executor  = Executors.newFixedThreadPool(5);
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
            e.printStackTrace();
            System.out.println("ExecutionException "+e);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("InterruptedException "+e);
        }

        Long lastModified = null;
        try {
            lastModified = executor.submit(() -> s3Dao.readFileMetadata(PRESIGNED_URL)).get();
        } catch (ExecutionException e) {
            e.printStackTrace(); //todo -add logging
        } catch (InterruptedException e) {
            e.printStackTrace(); // todo - add logging
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

    public void insertItemObjects(){
        try {
            List<Item> items = executor.submit(() -> s3Dao.readData(PRESIGNED_URL)).get();
            for(int i=0; i<items.size(); i++){
                final Item item = items.get(i);
                ItemDatabase.databaseWriteExecutor.execute(() -> itemsDao.insert(item));
            }
        } catch (InterruptedException e) {
            //TODO - add logging
        } catch (ExecutionException e) {
            //TODO - add logging
            System.out.println("ExecutionException "+e);
        }
    }
}