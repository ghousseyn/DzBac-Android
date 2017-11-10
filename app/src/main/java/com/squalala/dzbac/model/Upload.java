package com.squalala.dzbac.model;

import android.net.Uri;

import com.squalala.dzbac.DjihtiConstant;

/**
 * Created by Back Packer
 * Date : 17/04/15
 */
public class Upload {

    private String postId;

    private Uri uri;

    // Chemin du fichier
    private String urlImage;

    // Nom du fichier
    private String filename;

    // L'était de l'uplaod
    private boolean upload;


    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage() {
        this.urlImage = DjihtiConstant.PATH_IMAGES_POSTS + "/" + postId + "/" + filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;

        setUrlImage();
    }

    public boolean isUpload() {
        return upload;
    }

    public void setUpload(boolean upload) {
        this.upload = upload;
    }
}
