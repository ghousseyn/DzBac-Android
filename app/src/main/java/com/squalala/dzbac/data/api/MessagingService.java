package com.squalala.dzbac.data.api;

import com.squalala.dzbac.data.api.ApiResponse.BasicResponse;
import com.squalala.dzbac.data.api.ApiResponse.ListConversationPosts;
import com.squalala.dzbac.data.api.ApiResponse.Messages;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;


/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : MessagingService.java
 * Date : 27 juil. 2014
 * 
 */

public interface MessagingService {

	/**
	 *  Débuter une conversation avec quelqu'un.
	 */
	@FormUrlEncoded
	@PUT("messagerie/conversation")
	Call<BasicResponse> startConversation(
						   @Field("message") String message,
						   @Field("id_receveur") String id_receveur,
						   @Field("pseudo_expediteur") String pseudo_expediteur,
						   @Field("sujet") String sujet);
	
	/**
	 *  Avoir la liste des conversations.
	 */
	@GET("messagerie/list_conversations")
	Call<ListConversationPosts> getListConversations(
			   @Query("page") int page);
	

	/**
	 *  Avoir une conversation précise
	 */
	@GET("messagerie/conversation")
	Call<Messages> getconversation(
			   @Query("id_header") String id_header,
			   @Query("page") int page);


	/**
	 *  Bloquer un utilisateur
	 */
	@FormUrlEncoded
	@POST("messagerie/block")
	Call<BasicResponse> blockUser(
			@Field("id_blocked") String idBlocked);
	
	/**
	 *  Répondre dans une conversation
	 */
	@FormUrlEncoded
	@PUT("messagerie/send")
	Call<BasicResponse> sendMessage(
					   @Field("id_header") String idHeader,
					   @Field("id_receveur") String idReceveur,
					   @Field("message") String message);

	/**
	 *  Pour un message vocal dans la conversation
	 */
	@FormUrlEncoded
	@POST("messagerie/insert_audio")
	Call<BasicResponse> insertMessageWithAudio(
			@Field("id_header") String idHeader,
			@Field("id_receveur") String idReceveur,
			@Field("filename") String filename);


	//	@FormUrlEncoded

	@FormUrlEncoded
	@HTTP(method = "DELETE", path = "messagerie/conversation", hasBody = true)
	Call<BasicResponse> deleteConversation(
			@Field("id_header") String headerId);
	
	
	/**
	 *  Obtenir nombre de message non lus et non notifié
	 */
	@GET("messagerie/message_non_lu_new")
	Call<Messages> getMessageNews();
	
	/**
	 *  Obtenir nombre de message non lus c'est tout
	 */
	@GET("messagerie/message_non_lu")
	Call<Messages> getMessageNoLus();


}
