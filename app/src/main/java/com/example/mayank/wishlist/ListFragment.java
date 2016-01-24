package com.example.mayank.wishlist;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ListFragment extends Fragment {
    RecyclerView itemList;
    FloatingActionButton addButton;
    ListAdapter listAdapter;
    HashMap<String, Integer> items;
    Dialog dialog;
    public ListFragment() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        itemList = (RecyclerView) view.findViewById(R.id.list);
        addButton = (FloatingActionButton) view.findViewById(R.id.add_button);
        items = new HashMap<>();
        items.put("Item 1", 11);
        items.put("Item 2", 12);
        items.put("Item 3", 13);
        items.put("Item 4", 14);
        items.put("Item 5", 15);
        items.put("Item 6", 16);
        items.put("Item 7", 17);
        items.put("Item 8", 18);
        items.put("Item 9", 19);
        items.put("Item 10", 20);
        items.put("Item 11", 21);
        items.put("Item 12", 22);
        items.put("Item 13", 23);
        items.put("Item 14", 24);
        items.put("Item 15", 25);
        items.put("Item 16", 26);
        itemList.setHasFixedSize(true);
        itemList.setLayoutManager(new LinearLayoutManager(getContext()));
        listAdapter = new ListAdapter(items);
        itemList.setAdapter(listAdapter);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.add_dialog);
                final EditText name = (EditText) dialog.findViewById(R.id.item_name_input);
                final EditText quantity = (EditText) dialog.findViewById(R.id.quantity_input);
                final Button addButton = (Button) dialog.findViewById(R.id.add_button);
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!TextUtils.isEmpty(quantity.getText().toString()) && !TextUtils.isEmpty(name.getText().toString())) {
                            items.put(name.getText().toString(), Integer.parseInt(quantity.getText().toString()));
                            listAdapter = new ListAdapter(items);
                            itemList.setAdapter(listAdapter);
                            Snackbar.make(itemList, "Inserted new Element", Snackbar.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                        else
                            Snackbar.make(addButton, "No Field can be empty", Snackbar.LENGTH_LONG).show();
                    }
                });
                dialog.show();
            }
        });
        return view;
    }
    public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder>{
        HashMap<String, Integer> items;
        ArrayList<String> itemNames;
        public ListAdapter(HashMap<String, Integer> items) {
            this.items = items;
            itemNames = new ArrayList<>(items.keySet());
            Collections.sort(itemNames);
        }
        @Override
        public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null);
            return new ListViewHolder(view);
        }
        @Override
        public void onBindViewHolder(ListViewHolder holder, int position) {
            holder.itemName.setText(itemNames.get(position));
            holder.quantity.setText(Integer.toString(items.get(itemNames.get(position))));
        }
        @Override
        public int getItemCount() {
            return items.size();
        }
        public class ListViewHolder extends RecyclerView.ViewHolder {
            TextView itemName, quantity;
            public ListViewHolder(View itemView) {
                super(itemView);
                itemName = (TextView) itemView.findViewById(R.id.item_name);
                quantity = (TextView) itemView.findViewById(R.id.quantity);
            }
        }
    }
}
