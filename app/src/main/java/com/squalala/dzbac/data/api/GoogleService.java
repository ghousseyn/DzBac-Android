package com.squalala.dzbac.data.api;

import com.squalala.dzbac.data.api.ApiResponse.BasicResponse;
import com.squalala.dzbac.data.api.ApiResponse.Login;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : GoogleService.java
 * Date : 21 sept. 2014
 * 
 */
public interface GoogleService {
	
	@FormUrlEncoded
	@PUT("user/register_google")
	Call<BasicResponse> registerGoogle(@Field("username") String pseudo,
									   @Field("id_google") String idGoogle,
									   @Field("email") String email,
									   @Field("url_avatar") String url_avatar,
									   @Field("gcm_id") String gcmId);
	
	@GET("user/login_google")
	Call<Login> loginGoogle(@Query("id_google") String idGoogle,
			         @Query("gcm_id") String gcmId);

}
