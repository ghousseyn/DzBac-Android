package com.squalala.dzbac.interactors;

import com.squalala.dzbac.common.listener.GroupListener;
import com.squalala.dzbac.ui.notifications.NotificationListener;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : NotificationInteractor.java
 * Date : 2 sept. 2014
 * 
 */
public interface NotificationInteractor {
	
	/**
	 *  Sert à collecter les notifications du membre
	 */
	void getNotifications(int page, NotificationListener listener);
	

	/**
	 *  Marquer toutes les notifications en lus
	 */
	void setAllNotificationRead(NotificationListener listener);
	
	/**
	 * Elle sert à avoir les nouvelles nouvelles notifications
	 * par le biais de notifictions intents
	 */
	void getNewNotifications();
	
	/**
	 * Afin de rejoidre un groupe
	 */
	void joinGroup(String idItem, final GroupListener listener);
	
	/**
	 * Afin de quitter un groupe
	 */
	void removeFromGroup(String idItem, final GroupListener listener);


	void destroyCaller();

}
