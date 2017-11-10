package com.squalala.dzbac.common.listener;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : OnFinishedAddItem.java
 * Date : 8 août 2014
 * 
 */
public interface OnFinishedAddItemListener {
	
	void onTitleError(String message);
	
	void onDescriptionError(String message);
	
	void onItemAdded(String message);

	void onItemAddError(String message);

}
