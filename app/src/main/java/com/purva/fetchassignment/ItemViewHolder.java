package com.purva.fetchassignment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemViewHolder extends RecyclerView.ViewHolder{

    private final TextView listIdItemView;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        listIdItemView = itemView.findViewById(R.id.textView);
    }

    public void bind(Integer id, String text) {
        if(id!=null) {
            listIdItemView.setText(String.valueOf(id));
        } else {
            listIdItemView.setText(text);
        }

    }

    static ItemViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new ItemViewHolder(view);
    }
}
