package com.squalala.dzbac.ui.friends;

import com.digits.sdk.android.DigitsUser;

import java.util.ArrayList;

/**
 * Created by Back Packer
 * Date : 04/01/16
 */
public interface FriendsPresenter {

    void onLoadFriendDigits(ArrayList<DigitsUser> digitsUsers);
}
