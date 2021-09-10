package com.purva.fetchassignment;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class ListIdAdapter extends ListAdapter<Integer, ItemViewHolder> {

    private Context context;

    public ListIdAdapter(@NonNull DiffUtil.ItemCallback<Integer> diffCallback, Context context
    ) {
        super(diffCallback);
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ItemViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Integer item = getItem(position);
        System.out.println("item :"+item);
        holder.bind(item, null);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NamesActivity.class);
                intent.putExtra("listId",item);

                context.startActivity(intent);
            }
        });


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