package com.squalala.dzbac.ui.ranking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.google.android.gms.analytics.Tracker;
import com.squalala.dzbac.ActivityModule;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.R;
import com.squalala.dzbac.common.BaseFragment;

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
public class RankingFragment extends BaseFragment
    implements RankingView {

    @InjectView(R.id.progressBar_list_conversation)
    ProgressBar progress;

    @InjectView(R.id.aucune_conversation)
    TextView aucune_conversation;

    @Inject
    Tracker tracker;

    @Inject RankingPresenter rankingPresenter;

    private CardArrayAdapter mCardArrayAdapter;
    private CardListView listView;

    private static final String TAG = RankingFragment.class.getSimpleName();


    public static RankingFragment newInstance() {
        RankingFragment fragment = new RankingFragment();

        return fragment;
    }

    @DebugLog
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Answers.getInstance().logCustom(new CustomEvent("Menu Drawer")
                .putCustomAttribute("Click", "Ranking"));

        View root = inflater.inflate(R.layout.fragment_ranking, container, false);

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

        rankingPresenter.getListUsers();
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

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerRankComponent.builder()
                .appComponent(appComponent)
                .rankingModule(new RankingModule(this))
                .activityModule(new ActivityModule(getActivity()))
                .build()
                .inject(this);
    }
}
