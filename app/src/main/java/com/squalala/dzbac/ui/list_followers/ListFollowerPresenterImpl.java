package com.squalala.dzbac.ui.list_followers;

import android.app.Activity;


import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.interactors.UserInteractor;
import com.squalala.dzbac.ui.cards.UserCard;

import java.util.ArrayList;

import hugo.weaving.DebugLog;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Back Packer
 * Date : 24/09/15
 */
public class ListFollowerPresenterImpl
        implements ListFollowerPresenter, ListFollowerListener {

    private UserInteractor interactor;
    private ListFollowerView mView;
    private Activity activity;

    private String idMembre;

    public ListFollowerPresenterImpl(UserInteractor interactor, ListFollowerView mView, Activity activity) {
        this.interactor = interactor;
        this.mView = mView;
        this.activity = activity;
    }

    @Override
    public void getListFollowers(int page, String idMembre) {

        this.idMembre = idMembre;

        if (page == 1) {
            mView.clearCards();
            mView.initScrollDown();
        }

        interactor.getFollowers(page, idMembre, this);
    }

    @Override
    public void onLoadMoreMyFollowers(int page) {
        interactor.getMyFollowings(page, this);
    }

    @DebugLog
    @Override
    public void onLoadMoreListFollowers(int page) {
        interactor.getFollowers(page, idMembre, this);
    }

    @Override
    public void getListMyFollowings(int page) {

        if (page == 1) {
            mView.clearCards();
            mView.initScrollDown();
        }

        interactor.getMyFollowings(page, this);
    }

    @DebugLog
    @Override
    public void onLoadListUsers(ApiResponse.Users rankings) {

        ArrayList<Card> cards = new ArrayList<Card>();

        for (ApiResponse.User rankUser: rankings.rankUsers) {
            UserCard rankCard = new UserCard(activity);
            rankCard.setUser(rankUser);
            cards.add(rankCard);
        }

        mView.displayListUsers(cards);
    }


}
