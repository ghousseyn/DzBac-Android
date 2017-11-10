package com.squalala.dzbac.data.api;

import com.squalala.dzbac.data.api.ApiResponse.BasicResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : LikeService.java
 * Date : 28 juil. 2014
 * 
 */
public interface LikeService {
	
	
	@FormUrlEncoded
	@POST("like/update")
	Call<BasicResponse> like(
			  @Field("id_item") String id_item);

}
