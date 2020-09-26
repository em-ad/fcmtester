package com.emad.fcmtester;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;

public class MyApplication extends Application {

    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
