package dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.sql.Timestamp;
import java.util.Date;

import model.Metadata;

@Dao
public interface MetadataDao {

    //@Query("REPLACE INTO metadata VALUES (:metadata)")
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Metadata metadata);

    @Query("SELECT * FROM metadata LIMIT 1")
    Long getLatestTimestamp();
}
