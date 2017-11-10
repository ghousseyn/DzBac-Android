package com.squalala.dzbac.ui.list_conversation;

import android.app.Activity;

import com.squalala.dzbac.common.listener.MessagingListener;
import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.interactors.MessagingInteractor;
import com.squalala.dzbac.ui.cards.ListConversationShowCard;

import java.util.ArrayList;

import de.keyboardsurfer.android.widget.crouton.Style;
import hugo.weaving.DebugLog;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ListConversationPresenterImpl.java
 * Date : 1 août 2014
 * 
 */
public class ListConversationPresenterImpl 
	implements ListConversationPresenter, MessagingListener {
	
	private MessagingInteractor interactor;
	private ListConversationView view;
	private Activity activity;
	private MainPreferences mainPreferences;
	private boolean backpressed, destroyed;
	
	 public ListConversationPresenterImpl(MessagingInteractor interactor,
			 ListConversationView view, Activity activity, MainPreferences mainPreferences) {
		 this.interactor = interactor;
		 this.view = view;
		 this.activity = activity;
		 this.mainPreferences = mainPreferences;
	 }


	@Override
	public void onBackPressed() {
		backpressed = true;
	}

	@Override
	public void onDestroy() {
		destroyed = true;
		interactor.destroyCaller();
	}


	@Override
	public void onUploadAudioFile(String filename) {

	}

	@Override
	public void onUploadAudioFileError(String messageError) {

	}

	@Override
	public void onAudioMessageSended(String message) {

	}

	@Override
	public void OnUserBlocked(String message) {

	}

	@Override
	public void onConversationNotFound() {

	}

	@DebugLog
	@Override
	public void loadListConversation() {
		view.showProgress();
		interactor.getListConversation(1, this);
	}

	@Override
	public void onActivityResult() {
		view.showProgress();
		interactor.getListConversation(1, this);
	}

	@Override
	public void onRefresh() {
		interactor.getListConversation(1, this);
	}

	@Override
	public void onLoadMore(int page) {
		view.showProgressBar();
		interactor.getListConversation(page, this);
	}

	@Override
	public void deleteConversation(String headerId) {
		interactor.deleteConversation(headerId, this);
	}

	@Override
	public void onDeleteConversation(String message) {
		view.showMessage(message, Style.INFO);
	}

	@Override
	public void onDeleteConversationError(String message) {
		view.showMessage(message, Style.ALERT);
	}


	@DebugLog
	@Override
	public void onLoadListConversation(ApiResponse.ListConversationPosts arg0, int page) {

		if( !destroyed && !backpressed)
		{
			view.hideProgress();
			view.hideProgressBar();

			if (page == 1) {
				view.clearCards();
				view.initLayout();
			}


			ArrayList<Card> cards = new ArrayList<Card>();

			for (ApiResponse.ListConversation conversation: arg0.posts) {
				ListConversationShowCard card = new ListConversationShowCard(activity);
				card.setListConversation(conversation);
				card.setSwipeable(true);
				card.setId(conversation.id);
				cards.add(card);
			}

			view.displayListConversations(cards);
		}

	}

	@Override
	public void onLoadConversation(ApiResponse.Messages arg0) {
	}

	@Override
	public void onMessageSended(String idHeader) {}

}
