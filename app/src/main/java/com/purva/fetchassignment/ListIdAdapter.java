//package com.purva.fetchassignment;
//
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.DiffUtil;
//import androidx.recyclerview.widget.ListAdapter;
//import androidx.recyclerview.widget.RecyclerView;
//
//import model.Item;
//
//public class ListIdAdapter extends RecyclerView.Adapter<ListIdAdapter.MyViewHolder> {
//
//    public ListAdapter(){
//
//    }
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }
//}


package com.purva.fetchassignment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import model.Item;

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




//package com.purva.fetchassignment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.DiffUtil;
//import androidx.recyclerview.widget.ListAdapter;
//import androidx.recyclerview.widget.RecyclerView;
//
//import model.Item;
//
//public class ListIdAdapter extends ListAdapter<Integer, ItemViewHolder> {
//
//    public ListIdAdapter(@NonNull DiffUtil.ItemCallback<Integer> diffCallback) {
//        super(diffCallback);
//    }
//
//    @NonNull
//    @Override
//    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        return ItemViewHolder.create(parent);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
//        Integer item = getItem(position);
//        holder.bind(item);
//    }
//
//    static class ItemDiff extends DiffUtil.ItemCallback<Integer> {
//
//        @Override
//        public boolean areItemsTheSame(@NonNull Integer oldItem, @NonNull Integer newItem) {
//            return oldItem.equals(newItem);
//        }
//
//        @Override
//        public boolean areContentsTheSame(@NonNull Integer oldItem, @NonNull Integer newItem) {
//            return oldItem.equals(newItem);
//        }
//    }
//}
