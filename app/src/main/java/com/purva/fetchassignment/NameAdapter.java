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

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class NameAdapter extends ListAdapter<String, ItemViewHolder> {


    protected NameAdapter(@NonNull DiffUtil.ItemCallback<String> diffCallback, NamesActivity namesActivity) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ItemViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        String name = getItem(position);
        holder.bind(null, name);
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

