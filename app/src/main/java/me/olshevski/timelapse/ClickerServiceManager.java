package me.olshevski.timelapse;

import android.content.Context;

class ClickerServiceManager {

    private final Context appContext;

    private boolean clickerServiceStarted = false;

    ClickerServiceManager(Context appContext) {
        this.appContext = appContext;
    }

    void setClickerServiceStarted(boolean started) {
        if (clickerServiceStarted != started) {
            clickerServiceStarted = started;
            if (clickerServiceStarted) {
                ClickerService.startForeground(appContext);
            } else {
                ClickerService.stopForeground(appContext);
            }
        }
    }

}
