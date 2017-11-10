package com.squalala.dzbac.ui.show_item;

import com.squalala.dzbac.interactors.ItemInteractor;

import dagger.Module;
import dagger.Provides;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ShowItemModule.java
 * Date : 25 juil. 2014
 * 
 */
@Module
public class ShowItemModule {
	
	private ShowItemView showItemView;
	
	public ShowItemModule(ShowItemView showItemView) {
		this.showItemView = showItemView;
	}
	
	@Provides public ShowItemView provideShowItemView() {
		return showItemView;
	}
	
	@Provides
	public ShowItemPresenter provideShowItemPresenter(ShowItemView showItemView ,
			ItemInteractor showItemInteractor) {
		return new ShowItemPresenterImpl(showItemView, showItemInteractor);
	}
	

}
