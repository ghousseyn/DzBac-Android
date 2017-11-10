package com.squalala.dzbac.ui.list_conversation;


/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ListConversationPresenter.java
 * Date : 1 août 2014
 * 
 */
public interface ListConversationPresenter {
	
	void loadListConversation();

	void deleteConversation(String headerId);

	void onActivityResult();

	void onRefresh();

	void onLoadMore(int page);

	void onBackPressed();

	void onDestroy();
	

}
