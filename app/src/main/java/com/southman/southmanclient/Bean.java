package com.southman.southmanclient;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class Bean extends Application {
    private static Context context;

    public String baseurl = "http://ec2-13-126-246-74.ap-south-1.compute.amazonaws.com/";


    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();


        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));

    }
}
