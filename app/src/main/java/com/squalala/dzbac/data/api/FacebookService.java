package com.squalala.dzbac.data.api;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

import com.squalala.dzbac.data.api.ApiResponse.BasicResponse;
import com.squalala.dzbac.data.api.ApiResponse.Login;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : RegisterFacebookService.java
 * Date : 23 juin 2014
 * 
 */
public interface FacebookService {
	
	@FormUrlEncoded
	@PUT("user/register_facebook")
	Call<BasicResponse> registerFacebook(@Field("username") String pseudo,
										 @Field("id_facebook") String id_facebook,
										 @Field("email") String email,
										 @Field("url_avatar") String url_avatar,
										 @Field("gcm_id") String gcmId);
	
	@GET("user/login_facebook")
	Call<Login> loginFacebook(@Query("id_facebook") String id_facebook,
										 @Query("gcm_id") String gcmId);

}
