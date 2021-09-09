package dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import model.Item;

@Dao
public interface ItemsDao {

//    @Query("REPLACE INTO items VALUES (:item)")
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Item item);

    @Query("SELECT DISTINCT listId FROM items ORDER BY listId ASC")
    LiveData<List<Integer>> getListIds();// this data should be populated to cache

    @Query("SELECT name FROM items WHERE listId=:listId ORDER BY name ASC")
    LiveData<List<String>> getNames(int listId);
}
