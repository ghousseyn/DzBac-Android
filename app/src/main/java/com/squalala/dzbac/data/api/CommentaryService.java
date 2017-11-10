package com.squalala.dzbac.data.api;

import com.squalala.dzbac.data.api.ApiResponse.BasicResponse;
import com.squalala.dzbac.data.api.ApiResponse.Commentaires;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;


/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : CommentaryService.java
 * Date : 30 juil. 2014
 * 
 */
public interface CommentaryService {


    /**
     *  Pour initialiser un commentaire
     */
    @FormUrlEncoded
    @PUT("commentaire/init")
	Call<ApiResponse.Commentaire> initItem(
                  @Field("id_content") String idContent);


    /**
     *  Pour Mettre un commentaire avec des images
     */
    @FormUrlEncoded
    @POST("commentaire/insert_images")
    Call<BasicResponse> insertCommentWithImages(
                       @Field("id_comment") String id_comment,
                       @Field("id_content") String id_content,
                       @Field("message") String comment);

	/**
	 *  Pour Mettre un commentaire avec un audio
	 */
	@FormUrlEncoded
	@POST("commentaire/insert_audio")
	Call<BasicResponse> insertCommentWithAudio(
			@Field("id_content") String id_content,
			@Field("filename") String filename);

    /**
     *  Obtenir les commentaires d'un poste spécifique
     */
	@GET("commentaire/list")
	Call<Commentaires> getCommentaires(
			   @Query("page") int page,
			   @Query("id_content") String id_item);
	
	/**
	 *  Mettre un commentraire
	 */
	@FormUrlEncoded
	@PUT("commentaire/insert")
	Call<BasicResponse> comment(
			  @Field("id_content") String id_item,
			  @Field("message") String comment);
	
	/**
	 *  Modifier un commentaire
	 */
	@FormUrlEncoded
	@POST("commentaire/update")
	Call<BasicResponse> updateComment(
			  @Field("id") String idComment,
			  @Field("message") String comment);
	
	/**
	 *  Supprimer un commentaire
	 */
	@FormUrlEncoded
	@POST("commentaire/delete")
	Call<BasicResponse> deleteComment(
					@Field("id") String idComment,
					@Field("id_content") String idContent);
}
