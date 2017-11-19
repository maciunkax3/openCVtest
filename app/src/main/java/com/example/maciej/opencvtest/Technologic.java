package com.example.maciej.opencvtest;

import android.app.Application;
import android.content.Context;

/**
 * Created by Trollo on 2017-11-16.
 */

public class Technologic extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext(){
        return context;
    }
}
