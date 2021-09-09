package com.purva.fetchassignment;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import model.Item;

public class ListIdAdapter extends ListAdapter<Integer, ItemViewHolder> {

    public ListIdAdapter(@NonNull DiffUtil.ItemCallback<Integer> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ItemViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Integer item = getItem(position);
        holder.bind(item);
    }

    static class ItemDiff extends DiffUtil.ItemCallback<Integer> {

        @Override
        public boolean areItemsTheSame(@NonNull Integer oldItem, @NonNull Integer newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Integer oldItem, @NonNull Integer newItem) {
            return oldItem.equals(newItem);
        }
    }
}
