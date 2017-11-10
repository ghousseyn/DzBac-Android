package com.squalala.dzbac.data.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PUT;

/**
 * Created by Back Packer
 * Date : 19/05/15
 */
public interface AdminService {

    /**
     *  Pour bannir un membre
     */
    @FormUrlEncoded
    @PUT("admin/bann")
    Call<ApiResponse.BasicResponse> bannUsere(
            @Field("id_banned") String idBanned);
}
