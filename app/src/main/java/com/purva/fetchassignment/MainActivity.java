package com.purva.fetchassignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import viewmodel.ItemViewModel;

public class MainActivity extends AppCompatActivity {

    private ItemViewModel itemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CheckNetwork.isInternetAvailable(this);

        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ListIdAdapter adapter = new ListIdAdapter(new ListIdAdapter.ItemDiff(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        itemViewModel.getListIds().observe(this, ids -> {
            adapter.submitList(ids);

            if (ids.isEmpty()) {
                TextView textView = findViewById(R.id.empty);
                textView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckNetwork.isInternetAvailable(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckNetwork.isInternetAvailable(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        CheckNetwork.isInternetAvailable(this);
    }
}