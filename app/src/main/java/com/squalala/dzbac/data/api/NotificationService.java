package com.squalala.dzbac.data.api;

import com.squalala.dzbac.data.api.ApiResponse.BasicResponse;
import com.squalala.dzbac.data.api.ApiResponse.NotificationResponse;
import com.squalala.dzbac.data.api.ApiResponse.NotificationsPosts;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : NotificationService.java
 * Date : 2 sept. 2014
 * 
 */
public interface NotificationService {
	
	/**
	 *  Avoir les notifications dans l'activity
	 */
	@GET("notifications/list")
	Call<NotificationsPosts> getNotifications(
			   @Query("page") int page);
	
	/**
	 *  Avoir les notifications news en background
	 */
	@GET("notifications/notifications_news")
	Call<NotificationsPosts> getNotificationsNews();
	
	/**
	 *  Avoir le nombre de notifications non lus
	 */
	@GET("notifications/get_nomber_notification")
	Call<NotificationResponse> getNumberNotifications();
	
	/**
	 * Mettre toutes les notifications en lus
	 */
	 @FormUrlEncoded
	 @POST("notifications/all_notification_read")
	 Call<BasicResponse> allNotificationReaded(@Field("none") String none);
	 
	 /**
	 * Quitter un groupe
	 */
	 @FormUrlEncoded
	 @POST("notifications/remove_from_group")
	 Call<NotificationResponse> removeFromGroup(
			  @Field("id_item") String idItem);
	 
	 /**
	 * Rejoindre un groupe
	 */
	 @FormUrlEncoded
	 @POST("notifications/join_group")
	 Call<NotificationResponse> joinGroup(
			  @Field("id_item") String idItem);
	

}
