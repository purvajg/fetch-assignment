package com.purva.fetchassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import viewmodel.ItemViewModel;

public class MainActivity extends AppCompatActivity {

    private ItemViewModel itemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ListIdAdapter adapter = new ListIdAdapter(new ListIdAdapter.ItemDiff(),this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        itemViewModel.getListIds().observe(this, ids -> {
            adapter.submitList(ids);
        });
    }
}