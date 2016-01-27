package com.example.mayank.wishlist;

/**
 * Created by Mayank on 27-01-2016.
 */
public class Deal {

    String product_name;
    String website;
    String price;
    String discount;
    String delivery_date;

    Deal(String product_name, String website, String price, String discount, String delivery_date) {
        this.product_name = product_name;
        this.website = website;
        this.price = price;
        this.discount = discount;
        this.delivery_date = delivery_date;
    }
}

