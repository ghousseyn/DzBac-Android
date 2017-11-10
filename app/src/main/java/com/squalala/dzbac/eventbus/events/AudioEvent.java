package com.squalala.dzbac.eventbus.events;

import com.squalala.dzbac.ui.comment.CommentaryActivity;

/**
 * Created by faycal on 02/05/16.
 */
public class AudioEvent {

    private CommentaryActivity.AudioListener mListener;
    private String urlAudio;
    private boolean playing;

    public AudioEvent(CommentaryActivity.AudioListener mListener, String urlAudio, boolean isPlaying) {
        this.mListener = mListener;
        this.urlAudio = urlAudio;
        this.playing = isPlaying;
    }

    public boolean isPlaying() {
        return playing;
    }

    public String getUrlAudio() {
        return urlAudio;
    }

    public CommentaryActivity.AudioListener getAudioListener() {
        return mListener;
    }
}
