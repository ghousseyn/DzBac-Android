package com.squalala.dzbac.ui.show_conversation;

import android.content.Context;
import android.util.Log;

import com.squalala.dzbac.R;
import com.squalala.dzbac.common.listener.MessagingListener;
import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.interactors.MessagingInteractor;
import com.squalala.dzbac.ui.cards.MessageShowCard;

import java.util.ArrayList;

import hugo.weaving.DebugLog;
import it.gmariotti.cardslib.library.internal.Card;
import okhttp3.RequestBody;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ShowConversationPresenterImpl.java
 * Date : 2 août 2014
 * 
 */
public class ShowConversationPresenterImpl
	implements ShowConversationPresenter, MessagingListener {
	
	private ShowConversationView view;
	private MessagingInteractor interactor;
	private MainPreferences mainPreferences;
	private Context context;
	private boolean backpressed, destroyed;

	private String idReceveur, idHeader;
	
	public ShowConversationPresenterImpl(ShowConversationView view ,
			MessagingInteractor interactor, MainPreferences mainPreferences, Context context) {
		this.view = view;
		this.interactor = interactor;
		this.mainPreferences = mainPreferences;
		this.context = context;
	}

	@Override
	public void getConversation(String id_header, int page) {
		interactor.getConversation(id_header, page, this);
	}

	@Override
	public void onUploadAudioFile(String filename) {
		interactor.sendAudioMessage(idHeader, idReceveur, filename, this);
	}

	@Override
	public void onUploadAudioFileError(String messageError) {
		view.hideProgressDialog();
		view.showMessage(messageError);
	}

	@Override
	public void onAudioMessageSended(String message) {
		view.hideProgressDialog();
		view.showMessage(message);
		getConversation(idHeader, 1);
	}

	@Override
	public void uploadAudio(String idHeader,String idReceveur, RequestBody file) {
		this.idHeader = idHeader;
		this.idReceveur = idReceveur;
		view.showProgressDialog();
		interactor.sendAudioFile(file, this);
	}

	@Override
	public void onBackPressed() {
		backpressed = true;
	}

	@DebugLog
	@Override
	public void onDestroy() {
		destroyed = true;
	}

	@Override
	public void OnUserBlocked(String message) {

	}

	@Override
	public void onConversationNotFound() {
		view.exit();
	}

	@Override
	public void onDeleteConversation(String message) {

	}

	@Override
	public void onDeleteConversationError(String message) {

	}

	@Override
	public void onLoadListConversation(ApiResponse.ListConversationPosts arg0, int page) {

	}

	@DebugLog
	@Override
	public void onLoadConversation(ApiResponse.Messages arg0) {

        Log.v("ShowConv", "destroyed : " + destroyed + "backpress : " + backpressed);

        if (destroyed || backpressed)
            return;

        Log.v("ShowConv", "after retrun");

        ArrayList<Card> cards = new ArrayList<Card>();

        int taille = arg0.conversation.size();
        int count = 0;

        MessageShowCard card;

        for (ApiResponse.Message message : arg0.conversation) {

            count++;

            boolean isMe = message.id_membre.equals(mainPreferences.getIdUser());
            int idLayout;

            if (!isMe)
                idLayout = R.layout.card_message_left_show_inner_main;
            else
                idLayout = R.layout.card_message_right_show_inner_main;

            // Pour connaître le dernier message
            if (count == taille)
                card = new MessageShowCard(context, mainPreferences, true, idLayout);
            else
                card = new MessageShowCard(context, mainPreferences, false, idLayout);

            card.setMessage(message);
            cards.add(card);
        }

        view.displayConversation(cards);

	}



	@Override
	public void sendMessage(String idHeader,String idReceveur, String message) {
		interactor.sendMessage(idHeader, idReceveur, message, this);
	}

	@Override
	public void onMessageSended(String idHeader) {
        view.setAnimationMessageSended();
		getConversation(idHeader, 1);
	}


}
