package com.squalala.dzbac.ui.list_items;


import android.app.Activity;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ListItemPresenter.java
 * Date : 8 juil. 2014
 * 
 */
public interface ListItemPresenter {
	
	void getDataItems(boolean isMyItem, boolean isLikes, int page, String membreId);

    void onLoadMore(boolean isMyItem, boolean isLikes, int page);

    void onResume(boolean isMyItem, boolean isLikes);

    void onRefresh(boolean isMyItem, boolean isLikes);

    void getPostsSignaled(int page);

    void setActivity(Activity a);

    void setSignalement(boolean a);
}
