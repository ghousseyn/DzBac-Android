package com.squalala.dzbac.ui.list_followers;


import com.squalala.dzbac.data.api.ApiResponse;

/**
 * Created by Back Packer
 * Date : 24/09/15
 */
public interface ListFollowerListener {

    void onLoadListUsers(ApiResponse.Users rankings);
}
