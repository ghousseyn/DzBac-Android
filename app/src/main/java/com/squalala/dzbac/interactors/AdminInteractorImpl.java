package com.squalala.dzbac.interactors;

import com.squalala.dzbac.data.api.AdminService;
import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.ui.show_profile.OnUserBannedListener;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;

/**
 * Created by Back Packer
 * Date : 19/05/15
 */
public class AdminInteractorImpl implements AdminInteractor {

    private AdminService service;

    public AdminInteractorImpl(AdminService service) {
        this.service = service;
    }

    @Override
    public void bannUser(String userId, final OnUserBannedListener listener) {

        Call<ApiResponse.BasicResponse> call = service.bannUsere(userId);

        call.enqueue(new retrofit2.Callback<ApiResponse.BasicResponse>() {
            @Override
            public void onResponse(Call<ApiResponse.BasicResponse> call, retrofit2.Response<ApiResponse.BasicResponse> response) {
                listener.onUserBanned(response.body().message);
            }

            @Override
            public void onFailure(Call<ApiResponse.BasicResponse> call, Throwable t) {

            }
        });
    }
}
