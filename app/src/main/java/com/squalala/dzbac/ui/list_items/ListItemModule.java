package com.squalala.dzbac.ui.list_items;

import android.app.Activity;

import com.squalala.dzbac.common.listener.MultiScrollListener;
import com.squalala.dzbac.data.api.PostsService;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.interactors.ListItemInteractor;

import dagger.Module;
import dagger.Provides;


/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ListItemModule.java
 * Date : 23 juil. 2014
 * 
 */
@Module
public class ListItemModule {
	
	private ListItemView listItemView;

	public ListItemModule(ListItemView listItemView) {
		this.listItemView = listItemView;
	}
	
	@Provides
	public MultiScrollListener provideMultiScrollListener() {
		return new MultiScrollListener();
	}
	
	@Provides
	public ListItemView provideListItemView() {
		return listItemView;
	}
	
	@Provides
	ListItemPresenter provideListItemPresenter(ListItemView listItemView,
			ListItemInteractor listItemInteractor, MainPreferences prefs) {
		return new ListItemPresenterImpl(listItemView, listItemInteractor , prefs);
	}
	

}
