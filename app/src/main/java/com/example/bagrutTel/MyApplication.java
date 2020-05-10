package com.example.bagrutTel;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //enable firebase offline
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //to see the data that is loaded will be seen offline
    }
}
