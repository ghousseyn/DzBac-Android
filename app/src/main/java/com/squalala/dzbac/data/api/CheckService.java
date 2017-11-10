package com.squalala.dzbac.data.api;


import com.squalala.dzbac.data.api.ApiResponse.BasicResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : CheckService.java
 * Date : 24 juin 2014
 * 
 */
public interface CheckService {
	
	@GET("check/is_username_availaible")
	Call<BasicResponse> checkPseudo(@Query("pseudo") String pseudo);
	
	@GET("check/is_email_availaible")
	Call<BasicResponse> checkEmail(@Query("email") String email);
	

}
