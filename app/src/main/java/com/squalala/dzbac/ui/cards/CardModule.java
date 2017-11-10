package com.squalala.dzbac.ui.cards;

import android.app.Activity;

import com.squalala.dzbac.ui.cards.show_info.AnnonceShowInfoCard;
import com.squalala.dzbac.utils.Translater;

import dagger.Module;
import dagger.Provides;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : CardModule.java
 * Date : 12 juil. 2014
 * 
 */
@Module
public class CardModule {
	
    private Activity activity;

	public CardModule(Activity activity) {
		this.activity = activity;
	}

	@Provides
	PostStaggeredCard provideAnnonceStaggeredCard() {
		return new PostStaggeredCard(activity);
	}
	
	@Provides
	AnnonceShowInfoCard provideAnnonceShowInfoCard() {
		return new AnnonceShowInfoCard(activity);
	}
	
	
	@Provides
	ProfileInfoCard provideProfileInfoCard() {
		return new ProfileInfoCard(activity);
	}
	
	@Provides
	EditProfileCard provideEditProfileCard() {
		return new EditProfileCard(activity);
	}
	
	@Provides
	AnnonceAddItemInfoCard provideAnnonceAddItemInfoCard(Translater translater) {
		return new AnnonceAddItemInfoCard(activity, translater);
	}

    @Provides
    CommentAddInfoCard provideCommentAddInfoCard() {
        return new CommentAddInfoCard(activity);
    }


}
