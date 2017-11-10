package com.squalala.dzbac.data.api;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Back Packer
 * Date : 20/01/16
 */
public interface TelephonyService {


    @FormUrlEncoded
    @POST("telephony/call")
    Call<ApiResponse.BasicResponse> call(@Field("id_receveur") String id_membre,
                                         @Field("link_call") String linkCall,
                                         @Field("id_header") String idHeader);


}
