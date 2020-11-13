package com.example.customview.customview;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

public class App extends Application {
    private static Handler mHandler=null;
    @Override
    public void onCreate() {
        super.onCreate();
        mHandler=new Handler();
        sContext=getApplicationContext();
    }
    public static Handler getmHandler(){
        return mHandler;
    }
    private static Context sContext=null;
    public static Context getApplContext(){
        return sContext;
    }
}
