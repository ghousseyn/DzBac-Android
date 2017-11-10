package com.squalala.dzbac.ui.list_conversation;

import android.app.Activity;

import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.interactors.MessagingInteractor;

import dagger.Module;
import dagger.Provides;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ListConversationModule.java
 * Date : 1 août 2014
 * 
 */
@Module
public class ListConversationModule {
	
	private ListConversationView view;
	
	public ListConversationModule(ListConversationView view) {
		this.view = view;
	}
	
	@Provides
	public ListConversationView provideListConversationView() {
		return view;
	}
	
	@Provides
	public ListConversationPresenter provideListConversationPresenter(
			ListConversationView view, MessagingInteractor interactor,
			Activity activity, MainPreferences preferences) {
		return new ListConversationPresenterImpl(interactor, view, activity, preferences);
	}

}
