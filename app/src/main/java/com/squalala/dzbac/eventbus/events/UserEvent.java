package com.squalala.dzbac.eventbus.events;

/**
 * Created by Back Packer
 * Date : 21/11/15
 */
public class UserEvent {

    private boolean followed;

    public UserEvent(boolean followed) {
        this.followed = followed;
    }

    public boolean isFollowed() {
        return followed;
    }
}
