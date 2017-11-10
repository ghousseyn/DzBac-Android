package com.squalala.dzbac.ui.notifications;


/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ListConversationPresenter.java
 * Date : 1 août 2014
 * 
 */
public interface NotificationPresenter {
	
	void getNotifications(int page);
	
	void setAllNotificationsRead();

	void onBackPressed();

	void onDestroy();

}
