package me.olshevski.timelapse;

import android.content.Context;

import me.olshevski.timelapse.pref.TimePreference;
import me.olshevski.timelapse.util.CountDownTimerCompat;

class TimelapseManager {

    private static final int MILLIS_IN_SECOND = 1000;

    private final TimePreference timePreference;
    private boolean started;
    private boolean onHold;
    private CountDownTimerCompat timer;
    private Action action;

    TimelapseManager(Context context) {
        timePreference = new TimePreference(context);
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
            int time = timePreference.getTime();
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
        return timePreference.getTime();
    }

    void increaseTime() {
        int time = timePreference.getTime();
        timePreference.setTime(time + 1);
    }

    void decreaseTime() {
        int time = timePreference.getTime();
        int newTime = time - 1;
        if (newTime < 1) {
            newTime = 1;
        }
        timePreference.setTime(newTime);
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
