package com.squalala.dzbac.ui.friends;

import com.squalala.dzbac.data.api.ApiResponse;

/**
 * Created by Back Packer
 * Date : 04/01/16
 */
public interface FriendsListener {

    void onLoadFriendList(ApiResponse.Rankings rankings);
}
