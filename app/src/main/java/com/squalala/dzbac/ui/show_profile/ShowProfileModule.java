package com.squalala.dzbac.ui.show_profile;

import com.squalala.dzbac.interactors.AdminInteractor;
import com.squalala.dzbac.interactors.MessagingInteractor;
import com.squalala.dzbac.interactors.UserInteractor;

import dagger.Module;
import dagger.Provides;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ShowProfileModule.java
 * Date : 31 juil. 2014
 * 
 */
@Module
public class ShowProfileModule {
	
	private ShowProfileView showProfileView;
	
	public ShowProfileModule(ShowProfileView view) {
		showProfileView = view;
	}
	
	@Provides
	ShowProfileView provideProfileView() {
		return showProfileView;
	}
	
	@Provides
	ShowProfilePresenter provideShowProfilePresenter(UserInteractor interactor,
													 MessagingInteractor messagingInteractor,
													 AdminInteractor adminInteractor,
			ShowProfileView view) {
		return new ShowProfilePresenterImpl(view, interactor, adminInteractor, messagingInteractor);
	}

}
