package com.squalala.dzbac.data.api;


import com.squalala.dzbac.data.api.ApiResponse.Login;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : LoginService.java
 * Date : 26 juin 2014
 * 
 */
public interface LoginService {


	
	@GET("user/login")
	Call<Login> login(@Query("email") String email,
					  @Query("password") String password,
					  @Query("gcm_id") String gcmId);

}
