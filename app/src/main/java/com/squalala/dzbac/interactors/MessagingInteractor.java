package com.squalala.dzbac.interactors;

import com.squalala.dzbac.common.listener.MessagingListener;
import com.squalala.dzbac.ui.comment.CommentaryListener;

import okhttp3.RequestBody;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : MessagingInteractor.java
 * Date : 1 août 2014
 * 
 */
public interface MessagingInteractor {

	void deleteConversation(String headerId, MessagingListener listener);
	
	void getListConversation(int page, MessagingListener listener);
	
	void getConversation(String id_header, int page, MessagingListener listener);

	void blockUser(String userId, MessagingListener listener);
	
	void sendMessage(String idHeader ,String idReceveur,
			String message , MessagingListener listener);

	void destroyCaller();

	void sendAudioFile(RequestBody file, MessagingListener mListener);

	void sendAudioMessage(String idHeader ,String idReceveur, String filename, MessagingListener mListener);
	
}
