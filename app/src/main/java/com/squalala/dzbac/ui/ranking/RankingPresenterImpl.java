package com.squalala.dzbac.ui.ranking;

import android.app.Activity;

import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.interactors.UserInteractor;
import com.squalala.dzbac.ui.cards.RankCard;

import java.util.ArrayList;

import hugo.weaving.DebugLog;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Back Packer
 * Date : 24/09/15
 */
public class RankingPresenterImpl
        implements RankingPresenter, RankingListener {

    private UserInteractor interactor;
    private RankingView mView;
    private Activity activity;

    public RankingPresenterImpl(UserInteractor interactor, RankingView mView, Activity activity) {
        this.interactor = interactor;
        this.mView = mView;
        this.activity = activity;
    }

    @DebugLog
    @Override
    public void getListUsers() {
        interactor.getRankingUser(this);
    }

    @DebugLog
    @Override
    public void onLoadListUsers(ApiResponse.Rankings rankings) {

        ArrayList<Card> cards = new ArrayList<Card>();

        int count = 1;

        for (ApiResponse.RankUser rankUser: rankings.rankUsers) {
            RankCard rankCard = new RankCard(activity);
            rankCard.setRankUser(rankUser);
            rankCard.setCount(count++);
            cards.add(rankCard);

        }

        mView.displayListUsers(cards);
    }


}
