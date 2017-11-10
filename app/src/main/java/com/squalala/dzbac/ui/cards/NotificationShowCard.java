package com.squalala.dzbac.ui.cards;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.squalala.dzbac.R;
import com.squalala.dzbac.DjihtiConstant;
import com.squalala.dzbac.data.TypeNotification;
import com.squalala.dzbac.data.api.ApiResponse.NotificationResponse;
import com.squalala.dzbac.ui.comment.CommentaryActivity;
import com.squalala.dzbac.ui.show_profile.ShowProfileActivity;
import com.squalala.dzbac.utils.DateUtils;
import com.squalala.dzbac.utils.StringUtils;
import com.squareup.picasso.Picasso;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : CommentShowCard.java
 * Date : 29 juil. 2014
 * 
 */
public class NotificationShowCard extends Card {
	
	@InjectView(R.id.avatar_user) CircleImageView avatar_user;
	@InjectView(R.id.pseudo_user) TextView pseudo_user;
	@InjectView(R.id.date_conversation) TextView date_notification;
	@InjectView(R.id.titre_conversation)
	EmojiconTextView sujet_conversation;
	@InjectView(R.id.message_lu) FontAwesomeText message_lu;
	
	private NotificationResponse notification;
	private Activity activity;

	public NotificationShowCard(Activity activity) {
		super(activity, R.layout.card_notification_show_inner_main);
		this.activity = activity;
	}
	
	public void setNotification(NotificationResponse notification) {
		this.notification = notification;
	}
	
	
	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		
		ButterKnife.inject(this, parent);

		FontAwesomeText fontContribution = (FontAwesomeText) view.findViewById(R.id.font_contribution);
		fontContribution.setVisibility(View.VISIBLE);

		switch (notification.levelContribution) {
			case 0 : fontContribution.setVisibility(View.GONE);break;
			case 1 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.vert_smargadin)); break;
			case 2 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.violet)); break;
			case 3 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.bleu)); break;
			case 4 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.red_end_call)); break;
			case 5 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.bleu)); break;
			case 6 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.violet)); break;
		}

		Date date = DateUtils.strToDate(notification.date_creation);
		
		date_notification.setText(DateUtils.getRelativeTime(date));
		
		sujet_conversation.setText(Html.fromHtml(StringUtils.toEmoji(notification.notification)));

		Picasso.with(getContext())
				.load(notification.url_avatar)
				.placeholder(R.drawable.djihti_photo)
				.into(avatar_user);

		pseudo_user.setVisibility(View.INVISIBLE);
		message_lu.setVisibility(View.GONE);
		
		if (!notification.state.equals("read"))
			message_lu.setVisibility(View.VISIBLE);
		else
			message_lu.setVisibility(View.GONE);
		
		avatar_user.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = null;
				
				if (notification.verb.equals(TypeNotification.COMMENTED.toString())) {
					intent = new Intent(getContext(), ShowProfileActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("id_membre", notification.id_membre);
					getContext().startActivity(intent);
				}
				
			}
		});
		
		setOnClickListener(new OnCardClickListener() {
			
			@Override
			public void onClick(Card arg0, View arg1) {
				
				Intent intent = null;
				
				System.out.println("NOT --> " + notification.id_item);
				
				if (notification.verb.equals(TypeNotification.COMMENTED.toString())) {
					intent = new Intent(getContext(), CommentaryActivity.class);
					intent.putExtra("id_item", notification.id_item);
					intent.putExtra("type_item", notification.type);
					intent.putExtra("is_notification", true);
				}
				
				activity.startActivityForResult(intent,
						DjihtiConstant.REQUEST_CODE_NOTIFICATION_ACTIVITY);
			}
		});
		
		super.setupInnerViewElements(parent, view);
	}

}
