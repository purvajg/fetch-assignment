package com.purva.fetchassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import viewmodel.ItemViewModel;

public class NamesActivity extends AppCompatActivity {

    private ItemViewModel itemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final NameAdapter adapter = new NameAdapter(new DiffUtil.ItemCallback<String>() {
            @Override
            public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
                return true;
            }

            @Override
            public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
                return false;
            }
        },this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        int listId = 0;

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            listId = extras.getInt("listId");
        }

        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        LiveData<List<String>> itemNames = itemViewModel.getNames(listId);
        List<String> value = itemNames.getValue();
        itemNames.observe(this, names -> {
            adapter.submitList(names);

            if (names.isEmpty()) {
                TextView textView = findViewById(R.id.empty);//To handle empty list view
                textView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });
    }
}
