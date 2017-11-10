package com.squalala.dzbac.ui.main;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : MainView.java
 * Date : 3 juil. 2014
 * 
 */
public interface MainView {

	void showPopupMultipleDevice();

	void setErrorPremium();

	void showPopupBan();

	void showPopupVersionOutdated();
	
	void setCountMessages(int countMessage);
	
	void setCountNotifications(int countNotification);

}
