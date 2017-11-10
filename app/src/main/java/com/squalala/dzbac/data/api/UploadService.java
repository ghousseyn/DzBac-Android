package com.squalala.dzbac.data.api;

import com.squalala.dzbac.data.api.ApiResponse.BasicResponse;
import com.squalala.dzbac.data.api.ApiResponse.FilesResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;


/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : UploadService.java
 * Date : 5 août 2014
 * 
 */
public interface UploadService {
	
		/**
		 *  Upload de l'avatar de l'utilisateur
		 */
		@Headers({
		    "enctype: application/octet-stream" 
		})
	//	@Multipart
		@POST("upload/avatar")
        Call<FilesResponse> uploadAvatar(
				@Body RequestBody avatar);
		
		/**
		 *  Upload de l'image de fond (background) de l'utilisateur
		 */
		@Headers({
		    "enctype: application/octet-stream" 
		})

		@POST("upload/background")
        Call<FilesResponse> uploadBackground(
				@Body RequestBody avatar);
		
		/**
		 *  Upload des photos d'un item
		 */
		@Headers({
			"Accept:application/json, text/javascript, */*; q=0.01",
			"Connection: keep-alive",
		    "enctype: application/octet-stream" 
		})
		@POST("upload/images_item")
        Call<FilesResponse> uploadPhotoItem(
                @Body RequestBody image);


        /**
         *  Upload des photos d'un commentaire
         */
        @Headers({
                "Accept:application/json, text/javascript, */*; q=0.01",
                "Connection: keep-alive",
                "enctype: application/octet-stream"
        })
        @POST("upload/images_comment")
        Call<FilesResponse> uploadPhotoComment(
               // @Field("id") String id,
                @Body RequestBody image);

        /**
         *  Upload un fichier (pdf, doc, docx)
         */
        @Headers({
                "Accept:application/json, text/javascript, */*; q=0.01",
                "Connection: keep-alive",
                "enctype: application/octet-stream"
        })

        @POST("upload/file")
        Call<ApiResponse.FileUploadResponse> uploadFile(
                @Body RequestBody file);


        /**
         *  Upload d'un fichier audio
         */
        @Headers({
                "Accept:application/json, text/javascript, */*; q=0.01",
                "Connection: keep-alive",
                "enctype: application/octet-stream"
        })

        @POST("upload/audio")
        Call<ApiResponse.FileUploadResponse> uploadAudio(
                @Body RequestBody file);

        /**
         *  Upload un fichier (pdf, doc, docx)
         */
        @Headers({
                "Accept:application/json, text/javascript, */*; q=0.01",
                "Connection: keep-alive",
                "enctype: application/octet-stream"
        })

        @POST("upload/video")
        Call<ApiResponse.FileUploadResponse> uploadVideo(
                @Body RequestBody video);



        /**
         *  Pour supprimer une image
         */
        @FormUrlEncoded
        @POST("upload/delete")
        Call<BasicResponse> deletePhotoItem(@Field("filename") String filename,
                             @Field("id") String idItem);

        /**
         *  Pour télécharger un fichier
         */
        @GET("uploads/{filename}")
        Call<Response> getResults(@Path("filename") String username);

        /**
         *  Pour supprimer un fichier
         */
        @FormUrlEncoded
        @POST("upload/delete_file")
        Call<BasicResponse> deleteFile(@Field("id_content") String contentId,
                        @Field("filename") String filename);

        /**
         *  Pour supprimer un audio
         */
        @FormUrlEncoded
        @POST("upload/delete_audio")
        Call<BasicResponse> deleteAudio(@Field("id_content") String contentId,
                        @Field("filename") String filename);


        /**
         *  Pour supprimer une image
         */
        @FormUrlEncoded
        @POST("upload/delete_image_comment")
        Call<BasicResponse> deletePhotoComment(@Field("filename") String filename,
                             @Field("id") String idItem);

        /**
         *  Modifier les liens des images
         */
        @FormUrlEncoded
        @POST("upload/images_info")
        Call<BasicResponse> updateDataImages(
                  @Field("id") String idItem,
                  @Field("url_image_presentation") String urlImagePresentation);

}
