package com.example.mayank.wishlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    HashMap<String, Integer> items;
    ArrayList<String> itemNames;
    Context context;
    CustomLongClickListener listener;
    public ListAdapter(HashMap<String, Integer> items, Context context) {
        this.items = items;
        itemNames = new ArrayList<>(items.keySet());
        this.context = context;
        Collections.sort(itemNames);
    }
    public void delete(int position, String name) {
        items.remove(name);
        itemNames.remove(name);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());
    }
    public void add(int position, String itemName, int quantity) {
        items.put(itemName, quantity);
        itemNames.add(itemName);
        notifyItemInserted(position);
    }
    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, null);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        holder.itemName.setText(itemNames.get(position));
        String name = itemNames.get(position);
        int value = items.get(name);
        holder.quantity.setText(value + "");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView itemName, quantity;

        public ListViewHolder(View itemView) {
            super(itemView);
            itemName = (TextView) itemView.findViewById(R.id.item_name);
            quantity = (TextView) itemView.findViewById(R.id.quantity);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            listener.onItemLongClick(getAdapterPosition(), v);
            return true;
        }
    }
    public void setOnItemLongClickListener(CustomLongClickListener listener) {
        this.listener = listener;
    }
    public interface CustomLongClickListener {
        void onItemLongClick(int position, View v);
    }
}
