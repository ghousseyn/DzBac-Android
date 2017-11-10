package com.squalala.dzbac.ui.list_conversation;

import java.util.ArrayList;

import de.keyboardsurfer.android.widget.crouton.Style;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ListConversationView.java
 * Date : 1 août 2014
 * 
 */
public interface ListConversationView {
	
	void displayListConversations(ArrayList<Card> cards);

	void showMessage(String message, Style style);

	void showProgress();

	void hideProgress();

	void initLayout();

	void showProgressBar();

	void hideProgressBar();

	void clearCards();

}
