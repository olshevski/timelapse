package me.olshevski.timelapse;

import android.app.Application;

public class MyApplication extends Application {

    private ClickerServiceManager clickerServiceManager;
    private TimelapseManager timelapseManager;

    @Override
    public void onCreate() {
        super.onCreate();
        clickerServiceManager = new ClickerServiceManager(this);
        timelapseManager = new TimelapseManager();
    }

    public ClickerServiceManager getClickerServiceManager() {
        return clickerServiceManager;
    }

    public TimelapseManager getTimelapseManager() {
        return timelapseManager;
    }

}
