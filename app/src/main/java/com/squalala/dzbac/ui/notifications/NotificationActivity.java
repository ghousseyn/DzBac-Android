package com.squalala.dzbac.ui.notifications;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import hugo.weaving.DebugLog;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ListConversationActivity.java
 * Date : 1 août 2014
 * 
 */
public class NotificationActivity extends DjihtiBaseActivity
 	implements NotificationView, SwipeRefreshLayout.OnRefreshListener {
	
	private CardArrayAdapter mCardArrayAdapter;
	private CardListView listView;

	@Inject
	Tracker tracker;
	
	@Inject NotificationPresenter presenter;

    @InjectView(R.id.tool_bar)
    Toolbar mToolbar;
	
	@InjectView(R.id.progressBar_list_conversation)
    ProgressBar progress;
    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout swipeLayout;
	@InjectView(R.id.aucune_notification) TextView aucune_notification;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notifications);

		tracker.setScreenName(getClass().getName());
		
		ButterKnife.inject(this);
        setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                R.color.vert_icon_app,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
		
		ArrayList<Card> cards = new ArrayList<Card>();
		mCardArrayAdapter = new CardArrayAdapter(this ,cards);
	    listView = (CardListView) this.findViewById(R.id.myList);
	    
        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }


        progress.setVisibility(View.VISIBLE);
        initLayout();
	}


	@Override
	protected void setupComponent(AppComponent appComponent) {
		DaggerNotificationComponent.builder()
				.appComponent(appComponent)
				.notificationModule(new NotificationModule(this))
				.activityModule(new ActivityModule(this))
				.build()
				.inject(this);
	}

	@DebugLog
	@Override
	public void onBackPressed() {
		presenter.onBackPressed();
		super.onBackPressed();
	}

	@DebugLog
	@Override
	protected void onDestroy() {
        presenter.onDestroy();
		presenter = null;
		super.onDestroy();
	}

	@Override
    public void onRefresh() {
        initLayout();
    }

	@DebugLog
    @Override
	public void displayNotifications(ArrayList<Card> cards) {
		
		progress.setVisibility(View.GONE);

        if (swipeLayout.isRefreshing())
            mCardArrayAdapter.clear();
		
		if (cards != null) {
			mCardArrayAdapter.addAll(cards);
		    mCardArrayAdapter.notifyDataSetChanged();
		}
		
		if (mCardArrayAdapter.isEmpty())
			aucune_notification.setVisibility(View.VISIBLE);
		else
			aucune_notification.setVisibility(View.GONE);
		
		swipeLayout.setRefreshing(false);
	}
	
	private void initLayout() {

		if (presenter != null)
		{
			mCardArrayAdapter.clear();

			presenter.getNotifications(1);

			listView.setOnScrollListener(new EndlessScrollListener() {

				@Override
				public void onLoadMore(int page, int totalItemsCount) {
					presenter.getNotifications(page);
				}
			});
		}

	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.notification_menu, menu);

        ActionItemBadge.update(this, menu.findItem(R.id.menu_all_notification_read),
                FontAwesome.Icon.faw_eye, ActionItemBadge.BadgeStyles.YELLOW, Integer.MIN_VALUE);


        return super.onCreateOptionsMenu(menu);
    }
	
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	    	
	    	switch (item.getItemId())
	    	{
				case R.id.menu_all_notification_read:
					
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					 builder.setMessage(getString(R.string.alert_marquer_notification_lu));
					 
					 // ici on inverser 
					 builder.setPositiveButton(getString(R.string.non), new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});
					 
					 builder.setNegativeButton(getString(R.string.oui),
					            new DialogInterface.OnClickListener() {
					                public void onClick(DialogInterface dialog, int id) {
					                    presenter.setAllNotificationsRead();
					                }
					            });
					 
					 AlertDialog alertDialog = builder.create(); 
					 
					 alertDialog.show();
					
					break;

                case android.R.id.home:

                    finish();

                    return true;
		
				default:
					break;
			}
	    	return super.onOptionsItemSelected(item);
	    }
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        swipeLayout.setRefreshing(true);
		initLayout();
		super.onActivityResult(arg0, arg1, arg2);
	}

	@Override
	public void refreshNotifications() {
		initLayout();
	}

}
