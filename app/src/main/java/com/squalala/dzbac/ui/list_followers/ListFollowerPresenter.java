package com.squalala.dzbac.ui.list_followers;

/**
 * Created by Back Packer
 * Date : 24/09/15
 */
public interface ListFollowerPresenter {

    void getListFollowers(int page, String idMembre);

    void getListMyFollowings(int page);

    void onLoadMoreMyFollowers(int page);

    void onLoadMoreListFollowers(int page);
}
