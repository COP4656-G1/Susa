package com.susa.ajayioluwatobi.susa;

import android.app.Application;
import android.os.SystemClock;

public class SplashDuration extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SystemClock.sleep(2000);
    }
}
