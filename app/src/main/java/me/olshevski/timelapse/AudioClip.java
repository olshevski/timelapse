package me.olshevski.timelapse;

enum AudioClip {
    FINAL_SECOND(R.raw.timer_final_second),
    INCREMENT(R.raw.timer_increment);

    final int resId;

    AudioClip(int resId) {
        this.resId = resId;
    }

}
