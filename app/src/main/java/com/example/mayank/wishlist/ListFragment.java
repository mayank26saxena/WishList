package com.example.mayank.wishlist;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ListFragment extends Fragment {
    RecyclerView itemList;
    FloatingActionButton addButton;
    ListAdapter listAdapter;
    HashMap<String, Integer> items;
    Dialog dialog;
    public static final String TAG = ListFragment.class.getSimpleName();

    String[] product_name_list = new String[40];
    String[] quantity_list = new String[40];
    String username;

    ProgressDialog myProgressDialog;

    public ListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);

        myProgressDialog = new ProgressDialog(getContext());
        myProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myProgressDialog.setMessage(getResources().getString(R.string.pls_wait_txt));
        myProgressDialog.setCancelable(false);
        myProgressDialog.show();

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            Log.v(TAG,"No User logged in");
            navigateToLogin();
        } else {
            Log.i(TAG, currentUser.getUsername());
        }

        itemList = (RecyclerView) view.findViewById(R.id.list);
        addButton = (FloatingActionButton) view.findViewById(R.id.add_button);
        items = new HashMap<>();
        itemList.setHasFixedSize(true);
        itemList.setLayoutManager(new LinearLayoutManager(getContext()));
        listAdapter = new ListAdapter(items, getContext());
        listAdapter.setOnItemLongClickListener(new ListAdapter.CustomLongClickListener() {
            @Override
            public void onItemLongClick(final int position, View v) {
                final String name = ((TextView) v.findViewById(R.id.item_name)).getText().toString();

                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete Item");
                builder.setMessage("Are you sure you want to delete this item from your WishList?");
                builder.setCancelable(true);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listAdapter.delete(position, name);
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
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

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
                        if (!TextUtils.isEmpty(quantity.getText().toString()) && !TextUtils.isEmpty(name.getText().toString())) {
                            listAdapter.add(listAdapter.itemNames.size(),
                                    name.getText().toString(), Integer.parseInt(quantity.getText().toString()));
                            Snackbar.make(itemList, "Inserted new Element", Snackbar.LENGTH_LONG).show();
                            dialog.dismiss();
                            ParseObject o = new ParseObject("ListItem");
                            o.put("username", ParseUser.getCurrentUser().getUsername());
                            o.put("item_name", name.getText().toString());
                            o.put("quantity", quantity.getText().toString());
                            o.put("added_on", new Date().toString());
                            o.saveInBackground();

                            ((MainActivity) getActivity()).replaceFragment();

                        } else
                            Snackbar.make(addButton, "No Field can be empty", Snackbar.LENGTH_LONG).show();


                    }
                });
                dialog.show();
            }
        });

        getAllItemsInWishlist();

        return view;
    }


    private void navigateToLogin() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(this.getClass().getSimpleName(), "onResume Called in List Fragment");
        // getAllItemsInWishlist();
    }

    public void getAllItemsInWishlist() {
        if(ParseUser.getCurrentUser() != null) {
            String username = ParseUser.getCurrentUser().getUsername();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("ListItem");
            query.whereEqualTo("username", username);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> items, ParseException e) {
                    if (e == null) {
                        Log.d("Items : ", "Retrieved " + items.size() + " items.");

                        int n = items.size();

                        for (int i = 0; i < n; i++) {
                            product_name_list[i] = (String) items.get(i).get("item_name");
                            quantity_list[i] = (String) items.get(i).get("quantity");
                            listAdapter.add(listAdapter.itemNames.size(),
                                    product_name_list[i], Integer.parseInt(quantity_list[i]));
                        }

                        myProgressDialog.dismiss();

                    } else {
                        Log.d("Items", "Error : " + e.getLocalizedMessage());
                    }


                }
            });

        } else {
            navigateToLogin();
        }

    }

}
