package com.squalala.dzbac.common.listener;

/**
 * Created by Back Packer
 * Date : 16/04/15
 */
public interface OnUploadFileListener {

    void onUploadVideoFailed(String message);

    void onUploadAudioFailed(String message);

    void onUploadAudioFinished(String filename);

    void onUploadVideoFinished(String urlVideo, String message);

    void onUploadFileFinished(String filename);

    void onUploadFileError(String message);

}
