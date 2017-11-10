package com.squalala.dzbac.ui.list_followers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.analytics.Tracker;
import com.squalala.dzbac.ActivityModule;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.R;
import com.squalala.dzbac.common.BaseFragment;
import com.squalala.dzbac.utils.EndlessScrollListener;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hugo.weaving.DebugLog;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by Back Packer
 * Date : 24/09/15
 */
public class ListFollowerFragment extends BaseFragment
    implements ListFollowerView {

    @InjectView(R.id.progressBar_list_conversation)
    ProgressBar progress;

    @InjectView(R.id.aucune_conversation)
    TextView aucune_conversation;

    @Inject
    Tracker tracker;

    @Inject
    ListFollowerPresenter rankingPresenter;

    private CardArrayAdapter mCardArrayAdapter;
    private CardListView listView;

    private static final String TAG = ListFollowerFragment.class.getSimpleName();

    private static final String KEY_FOLLOW = "followers", KEY_ID_MEMBRE = "id_membre";

    private boolean isFollowers;
    private String idMembre;

    @DebugLog
    public static ListFollowerFragment newInstance(boolean isFollowers, String idMembre) {

        ListFollowerFragment fragment = new ListFollowerFragment();

        Bundle args = new Bundle();
        args.putBoolean(KEY_FOLLOW, isFollowers);
        args.putString(KEY_ID_MEMBRE, idMembre);

        fragment.setArguments(args);

        return fragment;
    }

    @DebugLog
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_list_users, container, false);

        ButterKnife.inject(this, root);

        return root;
    }


    @DebugLog
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<Card> cards = new ArrayList<Card>();
        mCardArrayAdapter = new CardArrayAdapter(getActivity() ,cards);
        listView = (CardListView) view.findViewById(R.id.myList);

        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tracker.setScreenName(getClass().getName());

        this.isFollowers = getArguments().getBoolean(KEY_FOLLOW);
        this.idMembre = getArguments().getString(KEY_ID_MEMBRE);

        if (isFollowers)
            rankingPresenter.getListFollowers(1, idMembre);
        else
            rankingPresenter.getListMyFollowings(1);
    }

    @DebugLog
    @Override
    public void displayListUsers(ArrayList<Card> cards) {

        progress.setVisibility(View.GONE);

        if (cards != null) {
            mCardArrayAdapter.addAll(cards);
            mCardArrayAdapter.notifyDataSetChanged();
        }

        if (mCardArrayAdapter.isEmpty())
            aucune_conversation.setVisibility(View.VISIBLE);
        else
            aucune_conversation.setVisibility(View.GONE);

    }

    @DebugLog
    @Override
    public void initScrollDown() {
        listView.setOnScrollListener(new EndlessScrollListener() {
            @DebugLog
            @Override
            public void onLoadMore(int page, int totalItemsCount) {

                if (isFollowers)
                    rankingPresenter.onLoadMoreListFollowers(page);
                else
                    rankingPresenter.onLoadMoreMyFollowers(page);
            }

        });
    }

    @Override
    public void clearCards() {
        mCardArrayAdapter.clear();

        if (mCardArrayAdapter != null)
            mCardArrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerListFollowerComponent.builder()
                .appComponent(appComponent)
                .rankingModule(new RankingModule(this))
                .activityModule(new ActivityModule(getActivity()))
                .build()
                .inject(this);
    }
}
