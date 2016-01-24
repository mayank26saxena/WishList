package com.example.mayank.wishlist;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {

    public static final String YOUR_APPLICATION_ID = "E2KRUls5hSP7EmT9ObSH25ppiZfaiTfoB3RBEqKr";
    public static final String YOUR_CLIENT_KEY = "xwt47XV83lzuyE4VigQjiqm7R68WfA5D2ktqT644";

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Existing initialization happens after all classes are registered
        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);

    }

}
