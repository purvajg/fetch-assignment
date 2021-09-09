package db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dao.ItemsDao;
import dao.MetadataDao;
import model.Item;
import model.Metadata;

@Database(entities = {Item.class, Metadata.class}, version = 1, exportSchema = false)
public abstract class ItemDatabase  extends RoomDatabase {
    public abstract ItemsDao itemsDao();

    public abstract MetadataDao metadataDao();

    private static volatile ItemDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ItemDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (ItemDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ItemDatabase.class, "item_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
