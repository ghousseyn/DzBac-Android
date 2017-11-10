package com.squalala.dzbac.data.api;

import com.squalala.dzbac.data.api.ApiResponse.BasicResponse;
import com.squalala.dzbac.data.api.ApiResponse.PostsItems;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ItemsService.java
 * Date : 8 juil. 2014
 * 
 */
public interface PostsService {
	
	@GET("post/list")
	Call<PostsItems> getPosts(@Query("key") String key,
							  @Query("subject") String subject,
							  @Query("type") String type,
							  @Query("mot_cle") String motCle,
							  @Query("format") String format,
							  @Query("tags") String tagsId,
							  @Query("order_by_views") int order_by_views,
							  @Query("order_by_likes") int order_by_likes,
							  @Query("is_me") int isMyPost,
							  @Query("is_likes") int isLikes,
							  @Query("page") int page);


    /**
     * Obtenir la liste des posts signalé
     */
	@GET("signalement/list")
	Call<PostsItems> getPostsSignaled(@Query("page") int page);

	/**
	 *  Pour supprimer un signalement d'un post
	 */
	@FormUrlEncoded
	@POST("signalement/delete_signal")
	Call<BasicResponse> deleteSignalement(
			@Field("id_content") String idContent);

	/**
	 *  Pour supprimer un item
	 */
	@FormUrlEncoded
	@POST("post/delete")
	Call<BasicResponse> deleteItem(
					@Field("id_content") String idContent);

	/*
	 * Mettre en avant un post
	 */
	@FormUrlEncoded
	@POST("uppost/pin_content")
	Call<BasicResponse> pinPost(
			@Field("id_content") String idContent);
}
