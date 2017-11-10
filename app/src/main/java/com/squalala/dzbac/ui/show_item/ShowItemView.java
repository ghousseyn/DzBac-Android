package com.squalala.dzbac.ui.show_item;

import com.squalala.dzbac.data.api.ApiResponse.Item;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ShowItemView.java
 * Date : 24 juil. 2014
 * 
 */
public interface ShowItemView {
	
	void displayItem(Item item);

	void cleanView();

	void exit();

	void showMessage(String message);

	void showErrorLoading();

}
