package dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import model.Item;



 /*
    Direct queries to database can be made with the help of Room DAO
    annotations like:
    @Query and @Insert
     */
@Dao
public interface ItemsDao {

    /*
    Using 'Replace' to avoid duplication, as the insert operation would
    be carried everytime an update is found in S3
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Item item);


    @Query("SELECT DISTINCT listId FROM items ORDER BY listId ASC")
    LiveData<List<Integer>> getListIds();// this data should be populated to cache

     //LiveData class is used to observe changes in data
    @Query("SELECT name FROM items WHERE listId=:listId AND name IS NOT NULL AND length(name) > 0 ORDER BY id ASC")
    LiveData<List<String>> getNames(int listId);
}
