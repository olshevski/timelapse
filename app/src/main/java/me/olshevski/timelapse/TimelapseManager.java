package me.olshevski.timelapse;

import android.content.Context;

import me.olshevski.timelapse.pref.GeneralPreference;
import me.olshevski.timelapse.util.CountDownTimerCompat;

class TimelapseManager {

    private static final int MILLIS_IN_SECOND = 1000;

    private final GeneralPreference generalPreference;
    private boolean started;
    private boolean onHold;
    private CountDownTimerCompat timer;
    private Action action;

    TimelapseManager(Context context) {
        generalPreference = new GeneralPreference(context);
    }

    boolean isStarted() {
        return started;
    }

    void start() {
        started = true;
        changeTimerState();
    }

    private void changeTimerState() {
        if (started && !onHold) {
            int time = generalPreference.getTime();
            timer = new TimelapseCountDownTimer(time);
            timer.start();
        } else {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        }
    }

    void stop() {
        started = false;
        changeTimerState();
    }

    void setOnHold(boolean onHold) {
        this.onHold = onHold;
        changeTimerState();
    }

    void setAction(Action action) {
        this.action = action;
    }

    int getTime() {
        return generalPreference.getTime();
    }

    void increaseTime() {
        int time = generalPreference.getTime();
        generalPreference.setTime(time + 1);
    }

    void decreaseTime() {
        int time = generalPreference.getTime();
        int newTime = time - 1;
        if (newTime < 1) {
            newTime = 1;
        }
        generalPreference.setTime(newTime);
    }

    interface Action {
        void run();
    }

    private class TimelapseCountDownTimer extends CountDownTimerCompat {

        TimelapseCountDownTimer(int seconds) {
            super(seconds * MILLIS_IN_SECOND, MILLIS_IN_SECOND);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            if (action != null) {
                action.run();
            }
            timer.start();
        }
    }

}
