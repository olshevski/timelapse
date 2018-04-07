package me.olshevski.timelapse;

import android.content.Context;

import me.olshevski.timelapse.pref.GeneralPreference;
import me.olshevski.timelapse.util.Utils;

final class TimelapseManager {

    private final GeneralPreference generalPreference;
    private final TimelapseIntervalTimer intervalTimer;
    private final TimelapseSoundPlayer soundPlayer;
    private Action action;

    private boolean started;
    private boolean onHold;

    TimelapseManager(Context appContext) {
        Utils.assertAppContext(appContext);
        generalPreference = new GeneralPreference(appContext);
        intervalTimer = new TimelapseIntervalTimer(new CallbackImpl());
        soundPlayer = new TimelapseSoundPlayer(appContext);
    }

    boolean isStarted() {
        return started;
    }

    void start() {
        if (started) {
            throw new IllegalStateException("already started");
        }
        started = true;
        changeTimerState();
        changeSoundPlayerState();
    }

    private void changeTimerState() {
        if (started && !onHold) {
            if (!intervalTimer.isStarted()) {
                int time = generalPreference.getIntervalTime();
                intervalTimer.start(time);
            }
        } else {
            if (intervalTimer.isStarted()) {
                intervalTimer.stop();
            }
        }
    }

    private void changeSoundPlayerState() {
        if (started) {
            if (generalPreference.isSoundsEnabled()) {
                soundPlayer.init();
            }
        } else {
            if (soundPlayer.isInitialized()) {
                soundPlayer.release();
            }
        }
    }

    void stop() {
        if (!started) {
            throw new IllegalStateException("already stopped");
        }
        started = false;
        changeTimerState();
        changeSoundPlayerState();
    }

    void setOnHold(boolean onHold) {
        this.onHold = onHold;
        changeTimerState();
    }

    void setAction(Action action) {
        this.action = action;
    }

    int getTime() {
        return generalPreference.getIntervalTime();
    }

    void increaseTime() {
        int time = generalPreference.getIntervalTime();
        generalPreference.setIntervalTime(time + 1);
    }

    void decreaseTime() {
        int time = generalPreference.getIntervalTime();
        int newTime = time - 1;
        if (newTime < 1) {
            newTime = 1;
        }
        generalPreference.setIntervalTime(newTime);
    }

    interface Action {
        void run();
    }

    private class CallbackImpl implements TimelapseIntervalTimer.Callback {

        @Override
        public void onSecondsLeftBeforeTick(int seconds) {
            if (soundPlayer.isInitialized()) {
                if (seconds == 1) {
                    soundPlayer.play(AudioClip.FINAL_SECOND);
                } else if (seconds <= 3) {
                    soundPlayer.play(AudioClip.INCREMENT);
                }
            }
        }

        @Override
        public void onIntervalTick() {
            if (action != null) {
                action.run();
            }
        }

    }

}
