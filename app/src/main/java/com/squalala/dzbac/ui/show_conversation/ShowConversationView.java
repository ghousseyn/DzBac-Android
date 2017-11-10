package com.squalala.dzbac.ui.show_conversation;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ShowConversationView.java
 * Date : 2 août 2014
 * 
 */
public interface ShowConversationView {
	
	void displayConversation(ArrayList<Card> cards);
	
	void showProgress();

    void setAnimationMessageSended();

	void exit();

	void showProgressDialog();

	void hideProgressDialog();

	void showMessage(String message);


}
