package com.example.mayank.wishlist;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
        for(int i = 1 ; i <= 20 ; i++)
            items.put("Item " + Integer.toString(i), (i + 10));
        for(int i = 1 ; i <= 20 ; i++) {
            ParseObject object = new ParseObject("ListItem");
            object.put("username", ParseUser.getCurrentUser().getUsername());
            object.put("item_name", "Item " + Integer.toString(i));
            object.put("quantity", (i + 10));
            object.put("added_on", new Date().toString());
            object.saveInBackground();
        }
        itemList.setHasFixedSize(true);
        itemList.setLayoutManager(new LinearLayoutManager(getContext()));
        listAdapter = new ListAdapter(items, getContext());
        listAdapter.setOnItemLongClickListener(new ListAdapter.CustomLongClickListener() {
            @Override
            public void onItemLongClick(int position, View v) {
                final String name = ((TextView) v.findViewById(R.id.item_name)).getText().toString();
                listAdapter.delete(position, name);
                /*listAdapter.setOnItemLongClickListener(this);*/
                Snackbar.make(itemList, "Item Deleted", Snackbar.LENGTH_LONG).show();
                ParseQuery<ParseObject> query = new ParseQuery<>("ListItem");
                query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        for (int i = 0; i < objects.size(); i++) {
                            ParseObject object = objects.get(i);
                            if (object.get("item_name").equals(name))
                                object.deleteInBackground();
                        }
                    }
                });
            }
        });
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
                            listAdapter.add(listAdapter.itemNames.size(),
                                    name.getText().toString(), Integer.parseInt(quantity.getText().toString()));
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
}
