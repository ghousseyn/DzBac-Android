package com.squalala.dzbac.ui.comment;

import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.ui.cards.CommentShowCard;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : CommentaryListener.java
 * Date : 30 juil. 2014
 * 
 */
public interface CommentaryListener {
	
	void onLoadCommentaires(ApiResponse.Commentaires arg0, int page);
	
	void onCommentSended(String idItem, String message);

    void onCommentSendedWithPhoto(String message);
	
	void onSelectDeleteComment(String idComment, String idContent, CommentShowCard card);
	
	void onSelectUpdateComment(String idComment, String comment, CommentShowCard card);
	
	void onDeleteComment(String message, CommentShowCard card);
	
	void onUpdateComment(String message, CommentShowCard card);

    void onCommentEmpty();

	void onUploadAudioFile(String filename);

	void onUploadAudioFileError(String messageError);

	void onAudioCommentSended(String message);

}
