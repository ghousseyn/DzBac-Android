package com.squalala.dzbac.ui.list_items;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.Tracker;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.FloatingActionButton.FabOnScrollListener;
import com.mopub.mobileads.MoPubConversionTracker;
import com.mopub.nativeads.MoPubAdAdapter;
import com.mopub.nativeads.MoPubNativeAdPositioning;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.RequestParameters;
import com.mopub.nativeads.ViewBinder;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.DjihtiApp;
import com.squalala.dzbac.R;
import com.squalala.dzbac.common.DjihtiBaseListFragment;
import com.squalala.dzbac.common.listener.MultiScrollListener;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.eventbus.events.FilterEvent;
import com.squalala.dzbac.ui.add_item.AddPostActivity;
import com.squalala.dzbac.ui.main.MainMyItemActivity.Updateable;
import com.squalala.dzbac.utils.EndlessScrollListener;
import com.squareup.leakcanary.RefWatcher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.EnumSet;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hugo.weaving.DebugLog;
import it.gmariotti.cardslib.library.extra.staggeredgrid.internal.CardGridStaggeredArrayAdapter;
import it.gmariotti.cardslib.library.extra.staggeredgrid.view.CardGridStaggeredView;
import it.gmariotti.cardslib.library.internal.Card;

public class PostsFragment extends DjihtiBaseListFragment
	implements ListItemView, Updateable, SwipeRefreshLayout.OnRefreshListener {

    private CardGridStaggeredArrayAdapter mCardArrayAdapter;
    private CardGridStaggeredView staggeredView;

    private static final String TAG = "PostsFragment";

    private static final String KEY_MY_POSTS = "my_posts";
    private static final String KEY_MY_LIKES = "my_likes";
    private static final String KEY_MY_PROFILE = "fab_button";
    private static final String KEY_MEMBRE_ID = "id_membre";

    private boolean isMyPosts, isMyLikes, hideFabButton;

    private String membreId;

    private EnumSet<RequestParameters.NativeAdAsset> desiredAssets;

    private MoPubAdAdapter mAdAdapter;

    private RequestParameters mRequestParameters;
    
    @Inject ListItemPresenter listItemPresenter;
    @Inject MultiScrollListener multiScrollListener;

    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout swipeLayout;

    @Inject MainPreferences preferences;

    @Inject
    Tracker tracker;


    public static PostsFragment newInstance(boolean isMyItem, boolean isMyLikes, boolean hideFabButton, String membreId) {
        Log.e(TAG, "newInstance");


        PostsFragment fragment = new PostsFragment();

        Bundle args = new Bundle();

        args.putBoolean(KEY_MY_LIKES, isMyLikes);
        args.putBoolean(KEY_MY_POSTS, isMyItem);
        args.putBoolean(KEY_MY_PROFILE, hideFabButton);
        args.putString(KEY_MEMBRE_ID, membreId);

        fragment.setArguments(args);

        return  fragment;
    }


/*
    public void onEventMainThread(FilterEvent event) {
        Log.e(TAG, "onEventMainThread");
        listItemPresenter.getDataItems(isMyPosts, isMyLikes, 1, membreId);
    }

    public void onEventBackgroundThread(FilterEvent event) {
        Log.e(TAG, "onEventtBackgroundMainThread");
        listItemPresenter.getDataItems(isMyPosts, isMyLikes, 1, membreId);
    }*/


    @Override
    public void onResume() {
        super.onResume();

        //if (!(isMyLikes || isMyPosts)) {
            mRequestParameters = new RequestParameters.Builder()
                    .desiredAssets(desiredAssets)
                    .build();

            mAdAdapter.loadAds(getString(R.string.ad_unit_native), mRequestParameters);
       // }
    }

    @Subscribe
    @DebugLog
    public void onEvent(FilterEvent event) {
        listItemPresenter.getDataItems(isMyPosts, isMyLikes, 1, membreId);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
      //  RefWatcher refWatcher = DjihtiApp.getRefWatcher(getActivity());
        //refWatcher.watch(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	Log.e(TAG, "onCreateView");

        EventBus.getDefault().register(this);

        new MoPubConversionTracker().reportAppOpen(getActivity());



    	View root = null;

        if (getArguments().getBoolean(KEY_MY_LIKES))
    	    root = inflater.inflate(R.layout.posts_fragment_staggeredgrid_likes, container, false);
        else
            root = inflater.inflate(R.layout.posts_fragment_staggeredgrid, container, false);

		setupListFragmentAnnonce(root);

        ButterKnife.inject(this, root);

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                R.color.vert_icon_app,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

         desiredAssets = EnumSet.of(
                RequestParameters.NativeAdAsset.TITLE,
                RequestParameters.NativeAdAsset.TEXT,
                RequestParameters.NativeAdAsset.ICON_IMAGE,
                RequestParameters.NativeAdAsset.MAIN_IMAGE,
                RequestParameters.NativeAdAsset.CALL_TO_ACTION_TEXT);

        return root;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        tracker.setScreenName(getClass().getName());

        Log.e(TAG, "onActivityCreadted --> savedInstanceState : " + (savedInstanceState == null));

        isMyPosts = getArguments().getBoolean(KEY_MY_POSTS);
        isMyLikes = getArguments().getBoolean(KEY_MY_LIKES);
        hideFabButton = getArguments().getBoolean(KEY_MY_PROFILE);
        membreId = getArguments().getString(KEY_MEMBRE_ID);

        Log.e(TAG, "onActivituCreadted --> hidebutton : " + hideFabButton);

        ArrayList<Card> cards = new ArrayList<Card>();

        mCardArrayAdapter = new CardGridStaggeredArrayAdapter(getActivity(), cards);

        if (isMyLikes) {
            staggeredView = (CardGridStaggeredView) getActivity().findViewById(R.id.posts_carddemo_extras_grid_stag_likes);
            staggeredView.setEmptyView(getActivity().findViewById(R.id.annonce_empty_likes));
        }
        else {
            staggeredView = (CardGridStaggeredView) getActivity().findViewById(R.id.annonce_carddemo_extras_grid_stag);
            staggeredView.setEmptyView(getActivity().findViewById(R.id.annonce_empty));
        }

        SwingBottomInAnimationAdapter animCardArrayAdapter = new SwingBottomInAnimationAdapter (mCardArrayAdapter);
        animCardArrayAdapter.setAbsListView(staggeredView);


      /*  if (isMyLikes || isMyPosts)
        {
            staggeredView.setExternalAdapter(animCardArrayAdapter, mCardArrayAdapter);
        }
        else
        {*/
            mAdAdapter = new MoPubAdAdapter(getActivity(), mCardArrayAdapter, new MoPubNativeAdPositioning.MoPubServerPositioning());


            // Set up a renderer that knows how to put ad data in your custom native view.
            final MoPubStaticNativeAdRenderer staticAdRender = new MoPubStaticNativeAdRenderer(
                    new ViewBinder.Builder(R.layout.native_ad_layout)
                            .titleId(R.id.native_title)
                            .textId(R.id.native_text)
                          //  .mainImageId(R.id.native_main_image)
                            .iconImageId(R.id.native_icon_image)
                            .callToActionId(R.id.native_cta)
                            .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                            .build());

            mAdAdapter.registerAdRenderer(staticAdRender);




            staggeredView.setAdapter(mAdAdapter);
       // }

      //


        if (savedInstanceState != null)
            clearCards();

        // On enlève le filtre

        listItemPresenter.setActivity(getActivity());
        listItemPresenter.getDataItems(isMyPosts, isMyLikes, 1, membreId);
    }

    @Override
    public void onRefresh() {
        listItemPresenter.onRefresh(isMyPosts, isMyLikes);
    }

    @Override
    public void onDestroyView() {
        // You must call this or the ad adapter may cause a memory leak.
        if (mAdAdapter != null)
            mAdAdapter.destroy();
        super.onDestroyView();
    }



    @Override
    public void initScrollDown() {
    	
    	multiScrollListener = new MultiScrollListener();
    	
    	multiScrollListener.addScrollListener(new EndlessScrollListener() {

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				getActivity().setProgressBarIndeterminateVisibility(true);
				Log.e(TAG, "onLoadMoreMyFollowers ---> " + page);
				listItemPresenter.onLoadMore(isMyPosts, isMyLikes, page);
			}

		});

        multiScrollListener.addScrollListener(new FabOnScrollListener());

        FloatingActionButton fab;

        if (getActivity() != null) {

            if(isMyLikes)
                fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_likes);
            else
                fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);

            if (hideFabButton)
                fab.setVisibility(View.GONE);
            else {
                fab.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        getActivity().startActivityForResult(new Intent(getActivity(), AddPostActivity.class), 0);
                    }
                });

                fab.attachToListView(staggeredView, multiScrollListener.getFabOnScrollListener());
            }

            staggeredView.setOnScrollListener(multiScrollListener);
        }

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
