package com.squalala.dzbac.ui.list_items;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ListItemView.java
 * Date : 8 juil. 2014
 * 
 */
public interface ListItemView {
	
	public void showItems(ArrayList<Card> cards);
	
	public void removeItem();
	
	public void showProgess();

    public void showProgressBar();

    public void hideProgressBar();

    public void initScrollDown();

    public void clearCards();
	
}
