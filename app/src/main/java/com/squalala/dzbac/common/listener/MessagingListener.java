package com.squalala.dzbac.common.listener;

import com.squalala.dzbac.data.api.ApiResponse;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ListConversationListener.java
 * Date : 1 août 2014
 * 
 */
public interface MessagingListener {
	
	void onLoadListConversation(ApiResponse.ListConversationPosts arg0, int page);

	void onDeleteConversation(String message);

	void onDeleteConversationError(String message);
	
	void onLoadConversation(ApiResponse.Messages arg0);

	void onConversationNotFound();
	
	void onMessageSended(String id_header);

	void onUploadAudioFile(String filename);

	void onUploadAudioFileError(String messageError);

	void onAudioMessageSended(String message);

	void OnUserBlocked(String message);
}
