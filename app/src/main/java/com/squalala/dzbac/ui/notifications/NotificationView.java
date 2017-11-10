package com.squalala.dzbac.ui.notifications;

import it.gmariotti.cardslib.library.internal.Card;

import java.util.ArrayList;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : NotificationView.java
 * Date : 1 août 2014
 * 
 */
public interface NotificationView {
	
	public void displayNotifications(ArrayList<Card> cards);
	
	public void refreshNotifications();

}
