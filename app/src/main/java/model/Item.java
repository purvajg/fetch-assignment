package model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.sql.Timestamp;

/*
Annotations are required to assocaite the variables with the Room database.
Room uses this information to generate code.
 */

@Entity(tableName = "items")
@JsonDeserialize
public class Item {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "listId")
    private int listId;

    @ColumnInfo(name = "name")
    private String name;


    Item() {
        // Unused constructor , required for Jackson de-serialization
    }

    Item(@NonNull int id, @NonNull int listId, String name){
        this.id = id;
        this.name = name;
        this.listId = listId;
    }

    public int getId() {
        return id;
    }

    public int getListId() {
        return listId;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
