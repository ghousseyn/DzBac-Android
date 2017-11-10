package com.squalala.dzbac.interactors;

import com.squalala.dzbac.ui.list_items.ListItemListener;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ListItemInteractor.java
 * Date : 8 juil. 2014
 * 
 */
public interface ListItemInteractor {
	
	
  	void loadItems(boolean isMyPosts, boolean isLikes, int page, String membreId, final ListItemListener listener);
  	
	void loadPostsSignaled(int page, ListItemListener listener);
}
