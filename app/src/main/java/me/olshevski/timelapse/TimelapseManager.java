package me.olshevski.timelapse;

import hugo.weaving.DebugLog;

class TimelapseManager {

    private static final int SECOND_IN_MILLIS = 1000;
    private static final int DEFAULT_TIMELAPSE_TIME = 5;

    private boolean started;
    private boolean onHold;
    private CountDownTimerCompat timer;
    private Action action;

    boolean isStarted() {
        return started;
    }

    void start() {
        started = true;
        changeTimerState();
    }

    private void changeTimerState() {
        if (started && !onHold) {
            timer = new TimelapseCountDownTimer(DEFAULT_TIMELAPSE_TIME);
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

    interface Action {
        void run();
    }

    private class TimelapseCountDownTimer extends CountDownTimerCompat {

        TimelapseCountDownTimer(int seconds) {
            super(seconds * SECOND_IN_MILLIS, SECOND_IN_MILLIS);
        }

        @DebugLog
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @DebugLog
        @Override
        public void onFinish() {
            if (action != null) {
                action.run();
            }
            timer.start();
        }
    }

}
