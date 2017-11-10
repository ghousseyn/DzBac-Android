package com.squalala.dzbac.ui.dialogs;

import android.app.Activity;

import com.squalala.dzbac.data.api.CommentaryService;
import com.squalala.dzbac.data.api.MessagingService;
import com.squalala.dzbac.data.api.SignalisationService;
import com.squalala.dzbac.data.prefs.MainPreferences;

import dagger.Module;
import dagger.Provides;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : DialogModule.java
 * Date : 22 juil. 2014
 * 
 */
@Module
public class DialogModule {
	
	@Provides
	SendMessageDialog provideMessageDialog(
			MessagingService messagingService,
			MainPreferences mainPreferences, Activity activity) {
		return new SendMessageDialog(activity, 
				messagingService,
				mainPreferences);
	}
	
	@Provides
	SendCommentDialog provideCommentDialog(
			CommentaryService commentaryService,
			MainPreferences mainPreferences, Activity activity) {
		return new SendCommentDialog(activity, 
				commentaryService,
				mainPreferences);
	}
	
	@Provides
	SignaleDialog provideSignaleDialog(SignalisationService signalisationService,
			MainPreferences mainPreferences, Activity activity) {
		return new SignaleDialog(activity,
				signalisationService,
				mainPreferences);
	}

}
