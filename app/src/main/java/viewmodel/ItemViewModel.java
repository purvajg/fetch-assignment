package viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import repository.ItemRepository;

public class ItemViewModel extends AndroidViewModel {

    private ItemRepository itemRepository;

    public ItemViewModel(@NonNull Application application) {
        super(application);
        itemRepository = new ItemRepository(application);
    }

    public LiveData<List<Integer>> getListIds() {
        return itemRepository.getListIds();
    }

    LiveData<List<String>> getNames(int listId) {
        return itemRepository.getItemNames(listId);
    }
}
