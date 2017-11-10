package com.squalala.dzbac.ui.cards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.squalala.dzbac.R;
import com.squalala.dzbac.data.Post;
import com.squalala.dzbac.ui.main.MainActivity;
import com.squalala.dzbac.ui.show_item.ShowAnnonceActivity;
import com.squalala.dzbac.utils.DynamicHeightPicassoCardThumbnailView;
import com.squalala.dzbac.utils.StringUtils;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : AnnonceStaggeredCard.java
 * Date : 11 juil. 2014
 * 
 */
public class PostStaggeredCard extends ItemStaggeredCard {
	
	protected Post post;

	protected boolean signalement;
	
	public PostStaggeredCard(Context context) {
		super(context, R.layout.post_staggered_inner_main);
	}

	public boolean isSignalement() {
		return signalement;
	}

	public void setSignalement(boolean signalement) {
		this.signalement = signalement;
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		super.setupInnerViewElements(parent, view);
		
		 EmojiconTextView titre = (EmojiconTextView) view.findViewById(R.id.post_title);

		CircleImageView avatarUser = (CircleImageView) view.findViewById(R.id.avatar_user);

		FontAwesomeText fontContribution = (FontAwesomeText) view.findViewById(R.id.font_contribution);
		fontContribution.setVisibility(View.VISIBLE);

		switch (post.levelContribution) {
			case 0 : fontContribution.setVisibility(View.GONE);break;
			case 1 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.vert_smargadin)); break;
			case 2 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.violet)); break;
			case 3 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.bleu)); break;
			case 4 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.red_end_call)); break;
			case 5 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.bleu)); break;
			case 6 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.violet)); break;
		}


		Picasso.with(getContext())
				.load(post.urlAvatar)
				.placeholder(R.drawable.djihti_photo)
				.into(avatarUser);

		TextView pseudo = (TextView) view.findViewById(R.id.pseudo_user);

		pseudo.setText(post.pseudo);

	     titre.setText((StringUtils.toEmoji(post.titre)));

	     TextView categorie = (TextView) view.findViewById(R.id.post_type);
         categorie.setText(post.type);


		if (post.type.equals(getContext().getString(R.string.challenge)))
			categorie.setTextColor(ContextCompat.getColor(context, R.color.red_end_call));
		else
			categorie.setTextColor(ContextCompat.getColor(context, R.color.demo_card_staggered_subtitle));

	     TextView prix = (TextView) view.findViewById(R.id.post_matiere);
	     prix.setText(post.matiere);

		FontAwesomeText fontVideo = (FontAwesomeText) view.findViewById(R.id.play_video);

		if (post.isVideo || post.isAudio)
			fontVideo.setVisibility(View.VISIBLE);
		else
			fontVideo.setVisibility(View.GONE);

		 DynamicHeightPicassoCardThumbnailView thumbView = (DynamicHeightPicassoCardThumbnailView) view.findViewById(R.id.card_thumbnail_layout);
		 thumbView.bindTo(post);
	     
	     clearComs(view);
	    
	     super.initBottomCard(view, post);
	}
	
	public Post getPost() {
		return post;
	}
	
	public void setPost(Post annonce) {
		this.post = annonce;
	}
	
	@Override
	public void init() {
		 //Add the thumbnail
      //  StaggeredCardThumb thumbnail = new StaggeredCardThumb(getContext());
      //  thumbnail.item = post;
       // addCardThumbnail(thumbnail);

        //A simple clickListener
        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
            	/*if (getContext().getClass().equals(MainFavorisActivity.class))
            	{
            		setClickable(false);
            		Intent intent = new Intent(getContext(), ShowAnnonceActivity.class);
                	intent.putExtra("id_item", item.id_item);
                	((Activity) getContext()).startActivityForResult(intent, 0);
            	}*/
            	if (getContext().getClass().equals(MainActivity.class))
            	{
            		Intent intent = new Intent(getContext(), ShowAnnonceActivity.class);
                	intent.putExtra("id_item", post.id_item);
					intent.putExtra("path_images", post.matiere + "/" + post.type);
                	getContext().startActivity(intent);
            	}
            	else
            	{
            		setClickable(false);
            		Intent intent = new Intent(getContext(), ShowAnnonceActivity.class);
                	intent.putExtra("id_item", post.id_item);
					intent.putExtra("path_images", post.matiere + "/" + post.type);
                	((Activity) getContext()).startActivityForResult(intent, 0);
        //            ((Activity) getContext()).overridePendingTransition(R.anim.animation_enter, R.anim.test_ok);
            	}
            }
        });
	}


}
