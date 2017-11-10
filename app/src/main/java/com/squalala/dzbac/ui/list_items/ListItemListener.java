package com.squalala.dzbac.ui.list_items;

import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.data.api.PostsService;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ListItemListener.java
 * Date : 8 juil. 2014
 * 
 */
public interface ListItemListener {
	
	void onLoadDataItems(ApiResponse.PostsItems items, int page, PostsService itemsService);
	
	/**
	 * On cherche la carte qui n'est pas cliquable et on la supprime
	 */
	void onRemoveItem();
	
	/**
	 *  Pour afficher la progressBar quand on fait une requête depuis une carte
	 */
	void onLoad();
	
}
