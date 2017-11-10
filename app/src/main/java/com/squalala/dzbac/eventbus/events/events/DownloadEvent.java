package com.squalala.dzbac.eventbus.events.events;

/**
 * Created by Back Packer
 * Date : 23/04/15
 */
public class DownloadEvent {

    private final boolean finish;

    public DownloadEvent(boolean finish) {
        this.finish = finish;
    }

    public boolean isFinish() {
        return finish;
    }
}
