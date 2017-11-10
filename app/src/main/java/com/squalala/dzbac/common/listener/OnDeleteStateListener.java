package com.squalala.dzbac.common.listener;

/**
 * Created by Back Packer
 * Date : 16/04/15
 */
public interface OnDeleteStateListener {

    void onDeleteFile(String message);

    void onDeleteFileError(String message);

    void onDeleteAudio(String message);

    void onDeleteAudioError(String message);

}
