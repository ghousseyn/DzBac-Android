package com.squalala.dzbac.ui.notifications;

import android.app.Activity;

import com.squalala.dzbac.interactors.NotificationInteractor;

import dagger.Module;
import dagger.Provides;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ListConversationModule.java
 * Date : 1 août 2014
 * 
 */
@Module
public class NotificationModule {
	
	private NotificationView view;

	public NotificationModule(NotificationView view) {
		this.view = view;
	}

	@Provides
	public NotificationView provideNotificationView() {
		return view;
	}
	
	@Provides
	public NotificationPresenter provideNotificationPresenter(
			NotificationView view, NotificationInteractor interactor, Activity activity) {
		return new NotificationPresenterImpl(interactor, view, activity);
	}

}
