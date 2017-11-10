package com.squalala.dzbac.interactors;

import com.squalala.dzbac.common.listener.GroupListener;
import com.squalala.dzbac.ui.cards.CommentShowCard;
import com.squalala.dzbac.ui.comment.CommentaryListener;
import com.squalala.dzbac.ui.show_item.ShowItemListener;

import okhttp3.RequestBody;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : CommentaryInteractor.java
 * Date : 30 juil. 2014
 * 
 */
public interface CommentaryInteractor {

    void initItem(String idContent, ShowItemListener listener);
	
	void getCommentaires(int page, String idItem, final CommentaryListener commentaryListener,
			final GroupListener listener);

    void uploadPhotoItem(String idItem, RequestBody typedFile, int position , ShowItemListener listener);

    void deletePhotoItem(String filename, String idItem, ShowItemListener listener);

    void sendMessage(String comment, String idItem, String idComment, CommentaryListener listener);

	void sendMessage(String comment, String idItem , CommentaryListener listener);
	
	void deleteComment(String idComment, String idContent, CommentShowCard card, CommentaryListener listener);
	
	void updateComment(String idComment, String comment, CommentShowCard card, CommentaryListener listener);

	void destroyCaller();

	void sendAudioFile(RequestBody file, CommentaryListener mListener);

	void sendAudioComment(String idContent, String filename, CommentaryListener mListener);
}
