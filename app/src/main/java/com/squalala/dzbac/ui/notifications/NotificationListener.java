package com.squalala.dzbac.ui.notifications;

import com.squalala.dzbac.data.api.ApiResponse;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : NotificationListener.java
 * Date : 2 sept. 2014
 * 
 */
public interface NotificationListener {
	
	void onLoadNotifications(ApiResponse.NotificationsPosts arg0);
	
	void onSetAllNotificationsReaded();

}
