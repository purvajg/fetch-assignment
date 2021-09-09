package model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;
import java.util.Date;

@Entity(tableName = "metadata")
public class Metadata {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="timestamp")
    private Long timestamp;

    public Metadata(@NonNull Long timestamp){
        this.timestamp = timestamp;
    }

    public Long getTimestamp() {
        return timestamp;
    }

}
