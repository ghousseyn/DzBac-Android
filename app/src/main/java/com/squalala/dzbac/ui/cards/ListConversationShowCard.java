package com.squalala.dzbac.ui.cards;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.squalala.dzbac.R;
import com.squalala.dzbac.DjihtiConstant;
import com.squalala.dzbac.data.api.ApiResponse.ListConversation;
import com.squalala.dzbac.ui.list_conversation.ListConversationActivity;
import com.squalala.dzbac.ui.list_conversation.SelectCardToRemoveListener;
import com.squalala.dzbac.ui.show_conversation.ShowConversationActivity;
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
public class ListConversationShowCard extends Card {
	
	@InjectView(R.id.avatar_user) CircleImageView avatar_user;
	@InjectView(R.id.pseudo_user) TextView pseudo_user;
	@InjectView(R.id.date_conversation) TextView date_conversation;
	@InjectView(R.id.titre_conversation)
	EmojiconTextView sujet_conversation;
	@InjectView(R.id.message_lu) FontAwesomeText message_lu;
	
	private ListConversation conversation;
	private Activity activity;
	private SelectCardToRemoveListener mListener;

	public ListConversationShowCard(Activity activity) {
		super(activity, R.layout.card_list_conversation_show_inner_main);
		this.activity = activity;
	}
	
	public void setListConversation(ListConversation conversations) {
		this.conversation = conversations; 
	}
	
	
	@Override
	public void setupInnerViewElements(ViewGroup parent, final View view) {

//		setSwipeable(true);

		ButterKnife.inject(this, parent);

		FontAwesomeText fontContribution = (FontAwesomeText) view.findViewById(R.id.font_contribution);
		fontContribution.setVisibility(View.VISIBLE);



		switch (conversation.levelContribution) {
			case 0 : fontContribution.setVisibility(View.GONE);break;
			case 1 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.vert_smargadin)); break;
			case 2 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.violet)); break;
			case 3 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.bleu)); break;
			case 4 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.red_end_call)); break;
			case 5 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.bleu)); break;
			case 6 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.violet)); break;
		}
		
		pseudo_user.setText(conversation.pseudo);

		if (conversation.level >= DjihtiConstant.ADMIN_GRADUATION)
			pseudo_user.setTextColor(getContext().getResources().getColor(android.R.color.holo_red_dark));
		else if (conversation.level == DjihtiConstant.BANNED_GRADUATION)
			pseudo_user.setTextColor(getContext().getResources().getColor(android.R.color.darker_gray));
		else
			pseudo_user.setTextColor(getContext().getResources().getColor(android.R.color.black));

		if (conversation.level >= DjihtiConstant.HEIGHT_ADMIN_GRADUATION)
			pseudo_user.setTextColor(getContext().getResources().getColor(R.color.color3));
		else if (conversation.level >= DjihtiConstant.MEDIUM_ADMIN_GRADUATION)
			pseudo_user.setTextColor(getContext().getResources().getColor(R.color.rouge_nacarat));

		Date date = DateUtils.strToDate(conversation.date_creation);

		date_conversation.setText(DateUtils.getRelativeTime(date));


		sujet_conversation.setText(StringUtils.toEmoji(conversation.sujet));

		Picasso.with(getContext())
				.load(conversation.url_avatar)
				.placeholder(R.drawable.djihti_photo)
				.into(avatar_user);

		if (conversation.message_lu == 1)
			message_lu.setVisibility(View.GONE);
		else
			message_lu.setVisibility(View.VISIBLE);
		
		avatar_user.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShowProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id_membre", conversation.id_membre);
                getContext().startActivity(intent);
            }
        });


		setOnClickListener(new OnCardClickListener() {

            @Override
            public void onClick(Card arg0, View arg1) {
                Intent intent = new Intent(activity, ShowConversationActivity.class);
                intent.putExtra("id_header", conversation.id);
                intent.putExtra("sujet", conversation.sujet);
                intent.putExtra("id_membre_2", conversation.id_membre_2);
                activity.startActivityForResult(intent,
                        DjihtiConstant.REQUEST_CODE_CONVERSATION_ACTIVITY);
            }
        });

        setOnUndoHideSwipeListListener(new OnUndoHideSwipeListListener() {
            @Override
            public void onUndoHideSwipe(Card card) {
                mListener = (ListConversationActivity) getContext();
                mListener.onSelectedCard(conversation.id);
            }
        });

		super.setupInnerViewElements(parent, view);
	}

}
