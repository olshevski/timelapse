package me.olshevski.timelapse;

class TimelapseManager {

    private boolean running;

    public boolean isRunning() {
        return running;
    }

    public void toggleRunning() {
        running = !running;
    }



}
