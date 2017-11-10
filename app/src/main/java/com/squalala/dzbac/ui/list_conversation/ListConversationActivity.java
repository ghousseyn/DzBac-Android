package com.squalala.dzbac.ui.list_conversation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.analytics.Tracker;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.squalala.dzbac.ActivityModule;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.R;
import com.squalala.dzbac.common.DjihtiBaseActivity;
import com.squalala.dzbac.utils.EndlessScrollListener;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import hugo.weaving.DebugLog;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.listener.UndoBarController;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ListConversationActivity.java
 * Date : 1 août 2014
 * 
 */
public class ListConversationActivity extends DjihtiBaseActivity
 	implements ListConversationView, SwipeRefreshLayout.OnRefreshListener, SelectCardToRemoveListener {
	
	private CardArrayAdapter mCardArrayAdapter;
	private CardListView listView;

	private static final String TAG = ListConversationActivity.class.getSimpleName();
	
	@Inject ListConversationPresenter presenter;

    @InjectView(R.id.tool_bar)
    Toolbar mToolbar;

	@Inject
	Tracker tracker;

	@InjectView(R.id.progressBar_list_conversation)
	ProgressBar progress;
    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout swipeLayout;
	@InjectView(R.id.aucune_conversation) TextView aucune_conversation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_conversation);

		tracker.setScreenName(getClass().getName());
		
		ButterKnife.inject(this);

        setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                R.color.vert_icon_app,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
		
	//	isFromNotification = getIntent().getBooleanExtra("is_notification", true);
		
		ArrayList<Card> cards = new ArrayList<Card>();
		mCardArrayAdapter = new CardArrayAdapter(this ,cards);


		mCardArrayAdapter.setUndoBarUIElements(new UndoBarController.DefaultUndoBarUIElements() {
			@Override
			public int getUndoBarId() {
				return R.id.my_undobar;
			}

			@Override
			public int getUndoBarMessageId() {
				return R.id.my_undobar_message;
			}

			@Override
			public int getUndoBarButtonId() {
				return R.id.my_undobar_button;
			}


			@Override
			public SwipeDirectionEnabled isEnabledUndoBarSwipeAction() {
				return SwipeDirectionEnabled.TOPBOTTOM;
			}


			@Override
			public AnimationType getAnimationType() {
				return AnimationType.TOPBOTTOM;
			}
		});


		mCardArrayAdapter.setEnableUndo(true);


		listView = (CardListView) this.findViewById(R.id.myList);
	    
        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }

        presenter.loadListConversation();
	}


	@Override
	public void showProgress() {
		progress.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgress() {
		progress.setVisibility(View.GONE);
	}

	@Override
	public void showProgressBar() {
		swipeLayout.setRefreshing(true);
	}

	@Override
	public void hideProgressBar() {
		swipeLayout.setRefreshing(false);
	}

    @DebugLog
	@Override
	public void clearCards() {
		mCardArrayAdapter.clear();
	}

	@Override
	public void onSelectedCard(String headerId) {
		if (presenter != null)
			presenter.deleteConversation(headerId);
	}

	@Override
    public void onRefresh() {
       	presenter.onRefresh();
    }

	@Override
	public void showMessage(String message, Style style) {
		Crouton.makeText(this, message, style).show();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_astuce, menu);

		ActionItemBadge.update(this, menu.findItem(R.id.menu_astuce),
				FontAwesome.Icon.faw_question_circle, ActionItemBadge.BadgeStyles.YELLOW, Integer.MIN_VALUE);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId())
		{
			case R.id.menu_astuce:
				//z	AstuceMessage.message(this, getString(R.string.alert_titre_astuce),
				//	getString(R.string.alert_astuce_message)).show();
				new SweetAlertDialog(this)
						.setTitleText( getString(R.string.alert_titre_astuce))
						.setContentText(getString(R.string.alert_astuce_conversation))
						.show();
				break;

			case android.R.id.home:

				finish();

				return true;
		}
		return super.onOptionsItemSelected(item);

	}



	@Override
	protected void setupComponent(AppComponent appComponent) {
		DaggerListConversationComponent.builder()
				.appComponent(appComponent)
				.listConversationModule(new ListConversationModule(this))
				.activityModule(new ActivityModule(this))
				.build()
				.inject(this);
	}

    @DebugLog
	@Override
	public void displayListConversations(ArrayList<Card> cards) {

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
	public void onBackPressed() {
		presenter.onBackPressed();
		super.onBackPressed();
	}


	@DebugLog
	@Override
	public void initLayout() {

		listView.setOnScrollListener(new EndlessScrollListener() {

			@DebugLog
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				if (presenter != null)
					presenter.onLoadMore(page);
			}
		});

	}

	@DebugLog
	@Override
	protected void onStop() {
		super.onStop();
	}

	@DebugLog
	@Override
	protected void onDestroy() {

		presenter.onDestroy();
		presenter = null;

		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		presenter.onActivityResult();
	}




}
