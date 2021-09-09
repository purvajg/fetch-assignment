package repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;

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
    private static final String BUCKET = "fetch-hiring";
    private static final String KEY = "hiring.json";

    private ItemsDao itemsDao;

    private MetadataDao metadataDao;

    private S3BasedPayloadDao s3Dao;

    private ExecutorService executor;

    public ItemRepository(Application application) {
        ItemDatabase db = ItemDatabase.getDatabase(application);
        itemsDao = db.itemsDao();
        metadataDao = db.metadataDao();
        s3Dao = new S3BasedPayloadDao(new AmazonS3Client(new BasicAWSCredentials("AKIARDZZYLVT6YQ2VPFE", "NInT5WkJvF164PdxVRjCCGSEai+E+XE2VeOF0FWa")), application);
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

        ObjectMetadata objectMetadata = null;
        try {
            objectMetadata = executor.submit(() -> s3Dao.readFileMetadata(BUCKET, KEY)).get();
        } catch (ExecutionException e) {
            e.printStackTrace(); //todo -add logging
        } catch (InterruptedException e) {
            e.printStackTrace(); // todo - add logging
        } finally {
            if(objectMetadata != null && (latestTimestamp == null ||  objectMetadata.getLastModified().getTime() > latestTimestamp)) {
                insertItemObjects();
                ObjectMetadata finalObjectMetadata = objectMetadata;
                ItemDatabase.databaseWriteExecutor.execute(() -> metadataDao.insert(new Metadata((Long) finalObjectMetadata.getLastModified().getTime())));;
            }
        }

    }

    public LiveData<List<String>> getItemNames(int listId) { //todo - change int for overflow
        checkForUpdates();
        return itemsDao.getNames(listId);
    }

    public void insertItemObjects(){
        try {
            List<Item> items = executor.submit(() -> s3Dao.readData(BUCKET, KEY)).get();
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
