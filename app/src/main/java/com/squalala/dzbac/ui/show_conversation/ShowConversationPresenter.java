package com.squalala.dzbac.ui.show_conversation;

import okhttp3.RequestBody;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ShowConversationPresenter.java
 * Date : 2 août 2014
 * 
 */
public interface ShowConversationPresenter {
	
	void getConversation(String id_header, int page);
	
	void sendMessage(String idHeader, String idReceveur, String message);

	void onBackPressed();

	void onDestroy();

	void uploadAudio(String idHeader,String idReceveur, RequestBody file);

}
