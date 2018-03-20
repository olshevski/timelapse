package me.olshevski.timelapse;

import android.view.accessibility.AccessibilityNodeInfo;

class TimelapseManager {

    private static final int SECOND_IN_MILLIS = 1000;
    private static final int DEFAULT_TIMELAPSE_TIME = 5;

    private boolean started;
    private CountDownTimerCompat countDownTimer;
    private AccessibilityNodeInfo cameraButton;

    boolean isStarted() {
        return started;
    }

    void start() {
        started = true;
        countDownTimer = new TimelapseCountDownTimer(DEFAULT_TIMELAPSE_TIME);
        countDownTimer.start();
    }

    void stop() {
        started = false;
        countDownTimer.cancel();
        countDownTimer = null;
    }

    void setCameraButton(AccessibilityNodeInfo cameraButton) {
        this.cameraButton = cameraButton;
    }

    private class TimelapseCountDownTimer extends CountDownTimerCompat {

        TimelapseCountDownTimer(int seconds) {
            super(seconds * SECOND_IN_MILLIS, SECOND_IN_MILLIS);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            if (cameraButton != null) {
                cameraButton.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
            countDownTimer.start();
        }
    }



}
