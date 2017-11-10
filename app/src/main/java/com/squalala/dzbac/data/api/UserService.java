package com.squalala.dzbac.data.api;

import com.squalala.dzbac.data.api.ApiResponse.BasicResponse;
import com.squalala.dzbac.data.api.ApiResponse.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : UserService.java
 * Date : 31 juil. 2014
 * 
 */
public interface UserService {


	/**
	 *  Pour récupérer les followers d'une certaine personne
	 */
	@GET("follow/list_followers")
	Call<ApiResponse.Users> getFollowers(@Query("page") int page, @Query("id_membre") String id_membre);

	/**
	 *  Pour récupérer les personne qu'on suit
	 */
	@GET("follow/list_my_followings")
	Call<ApiResponse.Users> getMyFollowings(@Query("page") int page);
	
	/**
	 *  Pour récupérer les informations propres à l'utilisateur.
	 */
	@GET("user/show")
	Call<User> getData(@Query("id_membre") String id_membre);

	/**
	 *  Pour s'abonner à un utilisateur
	 */
	@FormUrlEncoded
	@POST("follow/follow")
	Call<BasicResponse> follow(@Field("id_followed") String id_membre);


	/**
	 *  Pour se désabonner d'un utilisateur
	 */
	@FormUrlEncoded
	@POST("follow/un_follow")
	Call<BasicResponse> unFollow(@Field("id_followed") String id_membre);

	/**
	 *  Pour récupérer l'état de l'utilisateur
	 */
	@GET("user/state")
	Call<ApiResponse.StateUser> getState(@Query("android_id") String androidId,
										 @Query("version_code_app") int versioApp);

	/**
	 *  Pour récupérer le classement des utilisteurs
	 */
	@GET("user/ranking")
	Call<ApiResponse.Rankings> getRanking();

	/**
	 *  Pour récupérer le classement des utilisteurs
	 */
	@FormUrlEncoded
	@POST("user/search")
	Call<ApiResponse.Users> getSearchedUsers(@Field("page") int page, @Field("data") String data);

	/**
	 *  Pour récupérer les amis des utilisateurs
	 */
	@GET("user/friends")
	Call<ApiResponse.Rankings> getFriends(@Query("data") String digitsIds);
	
	/**
	 *  Modifier les informations propre à l'utilisateur
	 */
	@FormUrlEncoded
	@POST("user/update")
	Call<BasicResponse> updateData(
			  @Field("params") String params);

}
