package com.squalala.dzbac.ui.cards;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hugomatilla.audioplayerview.AudioPlayerView;
import com.squalala.dzbac.R;
import com.squalala.dzbac.DjihtiConstant;
import com.squalala.dzbac.data.api.ApiResponse.Message;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.eventbus.events.AudioEvent;
import com.squalala.dzbac.ui.comment.CommentaryActivity;
import com.squalala.dzbac.ui.show_profile.ShowProfileActivity;
import com.squalala.dzbac.utils.DateUtils;
import com.squalala.dzbac.utils.StringUtils;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import hugo.weaving.DebugLog;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : CommentShowCard.java
 * Date : 29 juil. 2014
 * 
 */
public class MessageShowCard extends Card
		implements CommentaryActivity.AudioListener {
	
	@InjectView(R.id.avatar_user) CircleImageView avatar_user;
	@InjectView(R.id.pseudo_user) TextView pseudo_user;
	@InjectView(R.id.date_message) TextView date_message;
	@InjectView(R.id.message_message)
	EmojiconTextView message_message;
	@InjectView(R.id.date_last_view) TextView date_last_view;

	@InjectView(R.id.play)
	AudioPlayerView audioPlayerView;

	private boolean isPlaying;


	private Message message;
	private MainPreferences mainPreferences;
	private boolean isLastMessage; 

	public MessageShowCard(Context context, MainPreferences mainPreferences,
			boolean isLastMessage, int idLayout) {
		super(context, idLayout);
		this.mainPreferences = mainPreferences;
		this.isLastMessage = isLastMessage;
	}
	
	public void setMessage(Message message) {
		this.message = message;
	}
	
	
	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		
		ButterKnife.inject(this, parent);

		if (message.urlAudio != null) {
			System.out.println("audio " + message.urlAudio);
			message_message.setVisibility(View.INVISIBLE);
			audioPlayerView.setVisibility(View.VISIBLE);
			audioPlayerView.setTextPlay();

			if (isPlaying)
				audioPlayerView.setTextStop();

			audioPlayerView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					EventBus.getDefault().post(new AudioEvent(MessageShowCard.this, message.urlAudio, isPlaying));
				}
			});
		}
		else {
			message_message.setVisibility(View.VISIBLE);
			audioPlayerView.setVisibility(View.GONE);
		}
		
		pseudo_user.setText(message.pseudo);

        if (message.level >= DjihtiConstant.ADMIN_GRADUATION)
            pseudo_user.setTextColor(getContext().getResources().getColor(android.R.color.holo_red_dark));
		else if (message.level == DjihtiConstant.BANNED_GRADUATION)
			pseudo_user.setTextColor(getContext().getResources().getColor(android.R.color.darker_gray));
        else
            pseudo_user.setTextColor(getContext().getResources().getColor(android.R.color.black));

		if (message.level >= DjihtiConstant.HEIGHT_ADMIN_GRADUATION)
			pseudo_user.setTextColor(getContext().getResources().getColor(R.color.color3));
		else if (message.level >= DjihtiConstant.MEDIUM_ADMIN_GRADUATION)
			pseudo_user.setTextColor(getContext().getResources().getColor(R.color.rouge_nacarat));
		
		//String messageStr = message.commentaire.replaceAll("(\r\n|\n)", "<br/>");
        String messageStr = message.commentaire;
		message_message.setText(StringUtils.toEmoji(messageStr));

		Date dateMessage = DateUtils.strToDate(message.date_creation);
		
		date_message.setText(DateUtils.getRelativeTime(dateMessage));
		
		if (isLastMessage &&
				message.id_membre.equals(mainPreferences.getIdUser()) && 
				Integer.valueOf(message.message_lu) == 1) {
			date_last_view.setVisibility(View.VISIBLE);

			Date dateLastView = DateUtils.strToDate(message.date_last_view);

			date_last_view.setText("Vu " + DateUtils.getRelativeTime(dateLastView));
		}
			
		else {
			date_last_view.setVisibility(View.GONE);
		}

		Picasso.with(getContext())
				.load(message.url_avatar)
				.placeholder(R.drawable.djihti_photo)
				.into(avatar_user);

		avatar_user.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getContext(), ShowProfileActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("id_membre", message.id_membre);
				getContext().startActivity(intent);
			}
		});
		
		super.setupInnerViewElements(parent, view);
	}

	public Date getDateMessage() {
		return DateUtils.strToDate(message.date_creation);
	}


    @DebugLog
    @Override
    public void onLoading() {
        audioPlayerView.setTextLoading();
    }

    @DebugLog
    @Override
    public void onPlaying() {
        isPlaying = true;
        audioPlayerView.setTextStop();
    }

    @DebugLog
    @Override
    public void onStop() {
        isPlaying = false;
        audioPlayerView.setTextPlay();
    }

}
