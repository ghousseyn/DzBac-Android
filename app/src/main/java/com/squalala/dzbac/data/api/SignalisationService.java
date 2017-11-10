package com.squalala.dzbac.data.api;

import com.squalala.dzbac.data.api.ApiResponse.BasicResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PUT;


/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : SignalisationService.java
 * Date : 28 juil. 2014
 * 
 */
public interface SignalisationService {
	
	@FormUrlEncoded
	@PUT("signalisation_djihti/signalisation")
	Call<BasicResponse> signale(
						   @Field("id_item") String id_item,
						   @Field("id_suspect") String id_suspect,
						   @Field("motif") String motif,
						   @Field("type_item") String type_item);

}
