package com.squalala.dzbac.ui.signalement;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.Tracker;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.FloatingActionButton.FabOnScrollListener;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.DjihtiApp;
import com.squalala.dzbac.R;
import com.squalala.dzbac.common.DjihtiBaseListFragment;
import com.squalala.dzbac.common.listener.MultiScrollListener;
import com.squalala.dzbac.ui.list_items.DaggerPostsComponent;
import com.squalala.dzbac.ui.list_items.ListItemModule;
import com.squalala.dzbac.ui.list_items.ListItemPresenter;
import com.squalala.dzbac.ui.list_items.ListItemView;
import com.squalala.dzbac.ui.main.MainMyItemActivity.Updateable;
import com.squalala.dzbac.utils.EndlessScrollListener;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import it.gmariotti.cardslib.library.extra.staggeredgrid.internal.CardGridStaggeredArrayAdapter;
import it.gmariotti.cardslib.library.extra.staggeredgrid.view.CardGridStaggeredView;
import it.gmariotti.cardslib.library.internal.Card;

public class PostsFragment extends DjihtiBaseListFragment
	implements ListItemView, Updateable, SwipeRefreshLayout.OnRefreshListener {

    private CardGridStaggeredArrayAdapter mCardArrayAdapter;
    private CardGridStaggeredView staggeredView;

    private static final String TAG = "PostsFragment";


    @Inject
    ListItemPresenter listItemPresenter;
    @Inject
    MultiScrollListener multiScrollListener;

    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout swipeLayout;

    @Inject
    Tracker tracker;


    public static PostsFragment newInstance() {
        Log.e(TAG, "newInstance");

        PostsFragment fragment = new PostsFragment();

        return fragment;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    //    RefWatcher refWatcher = DjihtiApp.getRefWatcher(getActivity());
      //  refWatcher.watch(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");

        View root = null;


        root = inflater.inflate(R.layout.posts_fragment_staggeredgrid, container, false);

        setupListFragmentAnnonce(root);

        ButterKnife.inject(this, root);

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                R.color.vert_icon_app,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        tracker.setScreenName(getClass().getName());

        Log.e(TAG, "onActivityCreadted --> savedInstanceState : " + (savedInstanceState == null));


        ArrayList<Card> cards = new ArrayList<Card>();

        mCardArrayAdapter = new CardGridStaggeredArrayAdapter(getActivity(), cards);


        staggeredView = (CardGridStaggeredView) getActivity().findViewById(R.id.annonce_carddemo_extras_grid_stag);
        staggeredView.setEmptyView(getActivity().findViewById(R.id.annonce_empty));

        SwingBottomInAnimationAdapter animCardArrayAdapter = new SwingBottomInAnimationAdapter(mCardArrayAdapter);
        animCardArrayAdapter.setAbsListView(staggeredView);
        staggeredView.setExternalAdapter(animCardArrayAdapter, mCardArrayAdapter);

      /*  if (isMyLikes || isMyPosts)
        {

        }
        else
        {*/


        // }
        if (savedInstanceState != null)
            clearCards();

        // On enlève le filtre

        listItemPresenter.setSignalement(true);
        listItemPresenter.setActivity(getActivity());
        listItemPresenter.getPostsSignaled(1);
    }

    @Override
    public void onRefresh() {
        listItemPresenter.getPostsSignaled(1);
    }


    @Override
    public void initScrollDown() {

        multiScrollListener = new MultiScrollListener();

        multiScrollListener.addScrollListener(new EndlessScrollListener() {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getActivity().setProgressBarIndeterminateVisibility(true);
                Log.e(TAG, "onLoadMoreMyFollowers ---> " + page);
                listItemPresenter.getPostsSignaled(page);
            }

        });

        multiScrollListener.addScrollListener(new FabOnScrollListener());

        FloatingActionButton fab;

        if (getActivity() != null) {

            fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);


            fab.setVisibility(View.GONE);

        }

        staggeredView.setOnScrollListener(multiScrollListener);
    }




	@Override
	public void showItems(ArrayList<Card> cards) {
		
		if (cards != null) {
			mCardArrayAdapter.addAll(cards);
		    mCardArrayAdapter.notifyDataSetChanged();
		}

		displayList();
	}


    @Override
    public void clearCards() {
        mCardArrayAdapter.clear();
    }

    @Override
    public void showProgess() {
        hideList(false);
    }

    @Override
    public void showProgressBar() {
        swipeLayout.setRefreshing(true);
    }

    @Override
    public void hideProgressBar() {
        swipeLayout.setRefreshing(false);
    }

    @Override
	public void removeItem() {
		
		Card cardToRemove; 
		
		for (int i = 0; i < mCardArrayAdapter.getCount(); i++) {
			
			if (!mCardArrayAdapter.getItem(i).isClickable()) {
				System.out.println("ITEM NON CLICKALE !!!");
				cardToRemove = mCardArrayAdapter.getItem(i);
				mCardArrayAdapter.remove(cardToRemove);
				mCardArrayAdapter.notifyDataSetChanged();
				setAllClickable();
				break;
			}
		}
		
		displayList();
	}



	@Override
	public void update() {
		removeItem();
	}

	@Override
	public void setAllClickable() {
		for (int i = 0; i < mCardArrayAdapter.getCount(); i++) 
			mCardArrayAdapter.getItem(i).setClickable(true);
	}

	/*@Override
	protected List<Object> getModules() {
		return Arrays.<Object>asList(new ListItemModule(this, getActivity()));
	}*/

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerPostsComponent.builder()
                .appComponent(appComponent)
                .listItemModule(new ListItemModule(this))
                .build()
                .inject(this);
    }
}
