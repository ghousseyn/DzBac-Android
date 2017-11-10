package com.squalala.dzbac.data.api;



import com.squalala.dzbac.data.api.ApiResponse.BasicResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PUT;


/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : RegisterService.java
 * Date : 24 juin 2014
 * 
 */
public interface RegisterService {
	
	@FormUrlEncoded
	@PUT("user/register")
	Call<BasicResponse> register(@Field("username") String pseudo,
								 @Field("password") String mot_de_passe,
								 @Field("email") String email,
								 @Field("gcm_id") String gcmId);
}
