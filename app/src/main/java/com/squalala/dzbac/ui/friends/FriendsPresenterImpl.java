package com.squalala.dzbac.ui.friends;

import android.app.Activity;

import com.digits.sdk.android.DigitsUser;
import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.interactors.UserInteractor;
import com.squalala.dzbac.ui.cards.show_info.ContactCard;

import java.util.ArrayList;

import hugo.weaving.DebugLog;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Back Packer
 * Date : 04/01/16
 */
public class FriendsPresenterImpl
        implements FriendsPresenter, FriendsListener {

    private UserInteractor interactor;
    private FriendsView mView;
    private Activity activity;

    public FriendsPresenterImpl(UserInteractor interactor, FriendsView mView, Activity activity) {
        this.interactor = interactor;
        this.mView = mView;
        this.activity = activity;
    }

    @Override
    public void onLoadFriendDigits(ArrayList<DigitsUser> digitsUsers) {

        if (digitsUsers != null && digitsUsers.size() > 0)
        {
            ArrayList<Long> digitsIds = new ArrayList<>();

            for(DigitsUser user : digitsUsers)
                digitsIds.add(user.id);

            interactor.getFriends(digitsIds, this);
        }
        else
            mView.displayListUsers(new ArrayList<Card>());

    }

    @DebugLog
    @Override
    public void onLoadFriendList(ApiResponse.Rankings firends) {

        ArrayList<Card> cards = new ArrayList<Card>();

        for (ApiResponse.RankUser rankUser: firends.rankUsers) {

            ContactCard card = new ContactCard(activity, rankUser);
            cards.add(card);

        }

        mView.displayListUsers(cards);
    }
}
