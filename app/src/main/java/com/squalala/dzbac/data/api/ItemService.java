package com.squalala.dzbac.data.api;

import com.squalala.dzbac.data.api.ApiResponse.BasicResponse;
import com.squalala.dzbac.data.api.ApiResponse.ContentItem;
import com.squalala.dzbac.data.api.ApiResponse.Items;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;


/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ItemService.java
 * Date : 25 juil. 2014
 * 
 */
public interface ItemService {
	
	@GET("post/content")
	Call<ContentItem> getItem(@Query("id_content") String idItem);
	
	/**
	 *  Pour initialiser un nouvel item
	 */
	@FormUrlEncoded
	@PUT("post/init")
	Call<Items> initItem(@Field("none") String e);


	/**
	 * Signaler un post
	 */
	@FormUrlEncoded
	@PUT("signalement/")
	Call<BasicResponse> signalePost(@Field("id_content") String idContent);
	
	/**
	 *  Pour Ajouter une annonce
	 */
	@FormUrlEncoded
	@POST("post/insert")
	Call<BasicResponse> deposeAnnonce(
			  @Field("id_content") String idContent,
			  @Field("data") String data,
			  @Field("is_modification") int isModification);
	
}
