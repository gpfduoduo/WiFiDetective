package com.guo.duoduo.wifidetective.ui;


import android.app.Application;


/**
 * Created by 郭攀峰 on 2015/11/4.
 */
public class MainApplication extends Application
{
    private static MainApplication mInstance;

    @Override
    public void onCreate()
    {
        super.onCreate();

        mInstance = this;
    }

    public static MainApplication getInstance()
    {
        return mInstance;
    }
}
