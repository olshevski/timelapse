package me.olshevski.timelapse;

import android.content.Context;

import me.olshevski.timelapse.misc.SoundPlayer;
import me.olshevski.timelapse.util.Utils;

/**
 * Simple wrapper around {@link SoundPlayer} with specific sounds
 * preloaded.
 */
final class TimelapseSoundPlayer {

    /**
     * Same volume for sounds as in Google Camera.
     */
    private static final float DEFAULT_VOLUME = 0.6f;

    private final Context appContext;

    private SoundPlayer soundPlayer;

    TimelapseSoundPlayer(Context appContext) {
        Utils.assertAppContext(appContext);
        this.appContext = appContext;
    }

    void init() {
        if (soundPlayer != null) {
            throw new IllegalStateException("already initialized");
        }
        soundPlayer = new SoundPlayer(appContext);
        for (AudioClip audioClip : AudioClip.values()) {
            soundPlayer.loadSound(audioClip.resId);
        }
    }

    boolean isInitialized() {
        return soundPlayer != null;
    }

    void play(AudioClip audioClip) {
        soundPlayer.play(audioClip.resId, DEFAULT_VOLUME);
    }

    void release() {
        if (soundPlayer == null) {
            throw new IllegalStateException("already released");
        }
        soundPlayer.release();
        soundPlayer = null;
    }

}
