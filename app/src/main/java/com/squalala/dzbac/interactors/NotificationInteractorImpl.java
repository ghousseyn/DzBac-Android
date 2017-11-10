package com.squalala.dzbac.interactors;

import com.squalala.dzbac.common.listener.GroupListener;
import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.data.api.ApiResponse.BasicResponse;
import com.squalala.dzbac.data.api.ApiResponse.NotificationResponse;
import com.squalala.dzbac.data.api.ApiResponse.NotificationsPosts;
import com.squalala.dzbac.data.api.NotificationService;
import com.squalala.dzbac.ui.notifications.NotificationListener;

import hugo.weaving.DebugLog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : NotificationInteractorImpl.java
 * Date : 2 sept. 2014
 * 
 */
public class NotificationInteractorImpl implements NotificationInteractor {
	
	private NotificationService notificationService;

	private Call<NotificationsPosts> getNotificationsCall;
	private Call<NotificationResponse> joinGroupCall, removeFromGroupCall;

	public NotificationInteractorImpl(NotificationService service) {
		this.notificationService = service;
	}
	
	@Override
	public void getNotifications(int page, final NotificationListener listener) {

		getNotificationsCall = notificationService.getNotifications(page);

		getNotificationsCall.enqueue(new retrofit2.Callback<NotificationsPosts>() {
			@Override
			public void onResponse(Call<NotificationsPosts> call, retrofit2.Response<NotificationsPosts> response) {
				if (response.body() != null)
					listener.onLoadNotifications(response.body());
			}

			@Override
			public void onFailure(Call<NotificationsPosts> call, Throwable t) {

			}
		});

	}

	@Override
	public void getNewNotifications() {
	}

	@Override
	public void setAllNotificationRead(final NotificationListener listener) {

		notificationService.allNotificationReaded(null)
				.enqueue(new retrofit2.Callback<BasicResponse>() {
					@Override
					public void onResponse(Call<BasicResponse> call, retrofit2.Response<BasicResponse> response) {
						if (response.body() != null)
							listener.onSetAllNotificationsReaded();
					}

					@Override
					public void onFailure(Call<BasicResponse> call, Throwable t) {

					}
				});
	}

	@Override
	public void joinGroup(String idItem, final GroupListener listener) {

		joinGroupCall = notificationService.joinGroup(idItem);

		joinGroupCall.enqueue(new retrofit2.Callback<NotificationResponse>() {
			@Override
			public void onResponse(Call<NotificationResponse> call, retrofit2.Response<NotificationResponse> response) {
				if (response.body() != null)
					listener.onLoadStateInGroup(response.body().state_group);
			}

			@Override
			public void onFailure(Call<NotificationResponse> call, Throwable t) {

			}
		});
	}

	@Override
	public void removeFromGroup(String idItem, final GroupListener listener) {

		removeFromGroupCall = notificationService.removeFromGroup(idItem);

		removeFromGroupCall.enqueue(new retrofit2.Callback<NotificationResponse>() {
			@Override
			public void onResponse(Call<NotificationResponse> call, retrofit2.Response<NotificationResponse> response) {
				if (response.body() != null)
					listener.onLoadStateInGroup(response.body().state_group);
			}

			@Override
			public void onFailure(Call<NotificationResponse> call, Throwable t) {

			}
		});

	}

	@Override
	public void destroyCaller() {
        if (getNotificationsCall != null && getNotificationsCall.isExecuted())
		    getNotificationsCall.cancel();

        if (removeFromGroupCall != null && removeFromGroupCall.isExecuted())
		    removeFromGroupCall.cancel();

        if (joinGroupCall != null && joinGroupCall.isExecuted())
		    joinGroupCall.cancel();
	}
}
