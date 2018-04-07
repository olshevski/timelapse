package me.olshevski.timelapse;

import me.olshevski.timelapse.misc.CountDownTimerCompat;

final class TimelapseIntervalTimer {

    private static final int THOUSAND_MS = 1000;

    private final Callback callback;
    private CountDownTimerCompat timer;

    TimelapseIntervalTimer(Callback callback) {
        this.callback = callback;
    }

    /**
     * @param intervalTime in seconds
     */
    void start(int intervalTime) {
        if (timer != null) {
            throw new IllegalStateException("already started");
        }
        timer = new TimelapseCountDownTimer(intervalTime);
        timer.start();
    }

    boolean isStarted() {
        return timer != null;
    }

    void stop() {
        if (timer == null) {
            throw new IllegalStateException("already stopped");
        }
        timer.cancel();
        timer = null;
    }

    interface Callback {
        void onSecondsLeftBeforeTick(int seconds);

        void onIntervalTick();
    }

    private class TimelapseCountDownTimer extends CountDownTimerCompat {

        TimelapseCountDownTimer(int seconds) {
            super(seconds * THOUSAND_MS, THOUSAND_MS);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int seconds = (int) Math.ceil((double) millisUntilFinished / THOUSAND_MS);
            callback.onSecondsLeftBeforeTick(seconds);
        }

        @Override
        public void onFinish() {
            callback.onIntervalTick();
            timer.start();
        }

    }

}
