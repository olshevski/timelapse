package me.olshevski.timelapse;

import android.content.Context;

import me.olshevski.timelapse.pref.GeneralPreference;
import me.olshevski.timelapse.util.CountDownTimerCompat;
import me.olshevski.timelapse.util.Utils;

class TimelapseManager {

    private static final int THOUSAND_MS = 1000;

    private final Context appContext;
    private final GeneralPreference generalPreference;
    private CountDownTimerCompat timer;
    private TimelapseSoundPlayer timelapseSoundPlayer;
    private Action action;

    private boolean started;
    private boolean onHold;

    TimelapseManager(Context appContext) {
        Utils.assertAppContext(appContext);
        this.appContext = appContext;
        generalPreference = new GeneralPreference(appContext);
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

    private void changeSoundPlayerState() {
        if (started) {
            if (generalPreference.isSoundsEnabled()) {
                if (timelapseSoundPlayer != null) {
                    throw new IllegalStateException("SoundPlayer instance already initialized");
                }
                timelapseSoundPlayer = new TimelapseSoundPlayer(appContext);
            }
        } else {
            if (timelapseSoundPlayer != null) {
                timelapseSoundPlayer.release();
                timelapseSoundPlayer = null;
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
            super(seconds * THOUSAND_MS, THOUSAND_MS);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (timelapseSoundPlayer != null) {
                if (millisUntilFinished <= THOUSAND_MS) {
                    timelapseSoundPlayer.play(AudioClip.FINAL_SECOND);
                } else if (millisUntilFinished <= 3 * THOUSAND_MS) {
                    timelapseSoundPlayer.play(AudioClip.INCREMENT);
                }
            }
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
