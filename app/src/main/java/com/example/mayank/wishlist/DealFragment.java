package com.example.mayank.wishlist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class DealFragment extends Fragment {

    List<Deal> deals;
    RecyclerView rv;

    String[] website_list = new String[40];
    String[] price_list = new String[40];
    String[] discount_list = new String[40];
    String[] delivery_date_list = new String[40];


    public DealFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.deal_fragment, container, false);

        rv = (RecyclerView) view.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        deals = new ArrayList<>();
        //initializeData();
        //initializeAdapter();

        getAllItemsInWishlist();

        return view;
    }

    private void initializeData() {
        deals = new ArrayList<>();
        deals.add(new Deal("Product Name : Shoes", "Website : Amazon", "Price : 1500", "Discount : 10%", "Delivery Date : 3 days"));
        deals.add(new Deal("Product Name : Bag", "Website : Flipkart", "Price : 1500", "Discount : 8%", "Delivery Date : 33 days"));
        deals.add(new Deal("Product Name : House", "Website : Snapdeal", "Price : 150", "Discount : 12%", "Delivery Date : 13 days"));

    }

    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(deals);
        rv.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    public void getAllItemsInWishlist() {
        String username = ParseUser.getCurrentUser().getUsername();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ListItem");
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> items, ParseException e) {
                if (e == null) {
                    Log.d("Items : ", "Retrieved " + items.size() + " items.");
                    int n = items.size();

                    String[] product_name_list = new String[n];

                    for (int i = 0; i < n; i++) {
                        product_name_list[i] = (String) items.get(i).get("item_name");
                    }

                    findDealforItem(product_name_list, n);

                } else {
                    Log.d("Items", "Error : " + e.getLocalizedMessage());
                }
            }
        });
    }

    public void findDealforItem(final String[] product_name_list, int n) {

       /* website_list = new String[n];
        price_list = new String[n];
        discount_list = new String[n];
        delivery_date_list = new String[n]; */

        int pos;

        for (int i = 0; i < n; i++) {
            pos = i;
            ParseQuery<ParseObject> query = ParseQuery.getQuery("deal");
            query.whereEqualTo("product_name", product_name_list[i]);
            final int finalPos = pos;
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null && objects.size() > 0) {
                        website_list[finalPos] = (String) objects.get(finalPos).get("website");
                        price_list[finalPos] = (String) objects.get(finalPos).get("price");
                        discount_list[finalPos] = (String) objects.get(finalPos).get("discount");
                        delivery_date_list[finalPos] = (String) objects.get(finalPos).get("delivery_date");

                        deals.add(new Deal("Product Name : " + product_name_list[finalPos], "Website : " + website_list[finalPos],
                                "Price : " + price_list[finalPos], "Discount : " + discount_list[finalPos],
                                "Delivery Date : " + delivery_date_list[finalPos]));

                        initializeAdapter();
                    }

                }
            });
        }
    }
}
