package me.olshevski.timelapse;

import android.app.Application;

public class MyApplication extends Application {

    private TimelapseManager timelapseManager;

    @Override
    public void onCreate() {
        super.onCreate();
        timelapseManager = new TimelapseManager(this);
    }

    public TimelapseManager getTimelapseManager() {
        return timelapseManager;
    }

}
