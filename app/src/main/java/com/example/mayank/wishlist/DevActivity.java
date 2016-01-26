package com.example.mayank.wishlist;

import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DevActivity extends AppCompatActivity {
    Button testNotification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev);
        testNotification = (Button) findViewById(R.id.notification_test);
        testNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notification.Builder builder = new Notification.Builder(getApplicationContext());
                builder.setSmallIcon(R.drawable.logo);
                builder.setContentTitle("New Wishlist Deal Available");
                builder.setContentText("An item from your cart, now has a reduced price.");
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.notify(1, builder.build());
            }
        });
    }
}
