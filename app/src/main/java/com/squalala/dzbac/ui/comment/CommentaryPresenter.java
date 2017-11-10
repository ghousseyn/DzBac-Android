package com.squalala.dzbac.ui.comment;

import com.squalala.dzbac.common.listener.GroupListener;
import com.squalala.dzbac.data.api.ApiResponse;

import okhttp3.RequestBody;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : CommentaryPresenter.java
 * Date : 30 juil. 2014
 * 
 */
public interface CommentaryPresenter extends GroupListener {

	void loadCommentaires(String idItem);
	
	void sendComment(String comment, String idItem);

    /*
     * Envoyer un commentaire avec des photos
     */
    void sendComment(String comment, String idItem, String idComment);

    void uploadPhotoItem(String idItem, int position, RequestBody typedFile);

    void deletePhoto(String filename, String idItem);

    void initItem(String idContent);

    void onActivityResult(String idItem);

    void onRefresh(String idItem);

    void onLoadMore(int page, String idItem);

    void onNewIntent(String idItem);

    void onBackPressed();

    void onNewComment(ApiResponse.Commentaire commentaire);

    void onDestroy();

    void uploadAudio(RequestBody file);

}
