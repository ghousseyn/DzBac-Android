package com.squalala.dzbac.ui.notifications;

import android.app.Activity;

import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.interactors.NotificationInteractor;
import com.squalala.dzbac.ui.cards.NotificationShowCard;

import java.util.ArrayList;

import hugo.weaving.DebugLog;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : NotificationPresenterImpl.java
 * Date : 1 août 2014
 * 
 */
public class NotificationPresenterImpl 
	implements NotificationPresenter, NotificationListener {
	
	private NotificationInteractor notificationInteractor;
	private NotificationView notificationView;
	private Activity activity;

	private boolean backpressed, destroyed;

	 public NotificationPresenterImpl(NotificationInteractor interactor,
			 NotificationView view, Activity activity) {
		 notificationInteractor = interactor;
		 notificationView = view;
		 this.activity = activity;
	 }

	@Override
	public void onBackPressed() {
		backpressed = true;
	}

	@Override
	public void onDestroy() {
		destroyed = true;
		notificationInteractor.destroyCaller();
	}

	@Override
	public void getNotifications(int page) {
		notificationInteractor.getNotifications(page, this);
	}

    @DebugLog
	@Override
	public void onLoadNotifications(ApiResponse.NotificationsPosts arg0) {

		if (!backpressed || !destroyed)
		{
			ArrayList<Card> cards = new ArrayList<>();

			for (ApiResponse.NotificationResponse notification: arg0.posts) {
				NotificationShowCard card = new NotificationShowCard(activity);
				card.setNotification(notification);
				cards.add(card);
			}

			notificationView.displayNotifications(cards);
		}
	}

	@Override
	public void onSetAllNotificationsReaded() {
		notificationView.refreshNotifications();
	}

	@Override
	public void setAllNotificationsRead() {
		notificationInteractor.setAllNotificationRead(this);
	}

}
