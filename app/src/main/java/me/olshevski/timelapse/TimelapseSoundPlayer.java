package me.olshevski.timelapse;

import android.content.Context;

import me.olshevski.timelapse.util.SoundPlayer;

/**
 * Simple wrapper around {@link me.olshevski.timelapse.util.SoundPlayer} with specific sounds
 * preloaded.
 */
final class TimelapseSoundPlayer {

    /**
     * Same volume for sounds as in Google Camera.
     */
    private static final float DEFAULT_VOLUME = 0.6f;

    private final SoundPlayer soundPlayer;

    TimelapseSoundPlayer(Context appContext) {
        soundPlayer = new SoundPlayer(appContext);
        for (AudioClip audioClip : AudioClip.values()) {
            soundPlayer.loadSound(audioClip.resId);
        }
    }

    void play(AudioClip audioClip) {
        soundPlayer.play(audioClip.resId, DEFAULT_VOLUME);
    }

    void release() {
        soundPlayer.release();
    }

}
