package com.squalala.dzbac.ui.cards;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.hugomatilla.audioplayerview.AudioPlayerView;
import com.squalala.dzbac.R;
import com.squalala.dzbac.DjihtiConstant;
import com.squalala.dzbac.data.api.ApiResponse.Commentaire;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.eventbus.events.AudioEvent;
import com.squalala.dzbac.ui.comment.CommentaryActivity;
import com.squalala.dzbac.ui.comment.CommentaryListener;
import com.squalala.dzbac.ui.dialogs.UpdateCommentDialog;
import com.squalala.dzbac.ui.full_imageview.ViewPagerActivity;
import com.squalala.dzbac.ui.show_profile.ShowProfileActivity;
import com.squalala.dzbac.utils.DateUtils;
import com.squalala.dzbac.utils.StringUtils;
import com.squalala.dzbac.utils.UrlToSplit;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;
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
public class CommentShowCard extends Card
    implements CommentaryActivity.AudioListener {
	
	@InjectView(R.id.avatar_user) CircleImageView avatar_user;
	@InjectView(R.id.pseudo_user) TextView pseudo_user;
	@InjectView(R.id.date_commentaire) TextView date_commentaire;
	@InjectView(R.id.message_commentaire)
    EmojiconTextView message_commentaire;
	@InjectView(R.id.font_edit_comment) FontAwesomeText fontEditComment;
    @InjectView(R.id.imageView)
    ImageView imageView;


    @InjectView(R.id.play)
    AudioPlayerView audioPlayerView;

	private Commentaire commentaire;
	private CommentaryListener listener;
    private String urlWithToSplit;
    private MainPreferences mainPreferences;

    private boolean isPlaying;



	// Afin de savoir si l'on permet la modification du commentaire
	private String idUser;
	
	private String idContent; 

	public CommentShowCard(Context context, MainPreferences mainPreferences) {
		super(context, R.layout.card_comment_show_inner_main);
        this.mainPreferences = mainPreferences;
	}

    public Commentaire getCommentaire() {
        return commentaire;
    }
	
	public void setCommentaire(Commentaire commentaires) {
		this.commentaire = commentaires;
	}
	
	public void setIdContent(String idContent) {
		this.idContent = idContent;
	}
	
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
	
	public void setListener(CommentaryListener listener) {
		this.listener = listener;
	}


    public void setComment(String comment) {
        message_commentaire.setText(comment);
        commentaire.commentaire = comment;
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

    @Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		
		ButterKnife.inject(this, parent);

        FontAwesomeText fontContribution = (FontAwesomeText) view.findViewById(R.id.font_contribution);
        fontContribution.setVisibility(View.VISIBLE);

        switch (commentaire.levelContribution) {
            case 0 : fontContribution.setVisibility(View.GONE);break;
            case 1 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.vert_smargadin)); break;
            case 2 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.violet)); break;
            case 3 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.bleu)); break;
            case 4 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.red_end_call)); break;
            case 5 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.bleu)); break;
            case 6 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.violet)); break;
        }

        if (commentaire.urlAudio != null) {
            System.out.println("audio " + commentaire.urlAudio);
            message_commentaire.setVisibility(View.INVISIBLE);
            audioPlayerView.setVisibility(View.VISIBLE);
            audioPlayerView.setTextPlay();

            if (isPlaying)
                audioPlayerView.setTextStop();

            audioPlayerView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new AudioEvent(CommentShowCard.this, commentaire.urlAudio, isPlaying));
                }
            });
        }
        else {
            message_commentaire.setVisibility(View.VISIBLE);
            audioPlayerView.setVisibility(View.GONE);
        }
		
		pseudo_user.setText(commentaire.pseudo);
		
        String commentaireStr = commentaire.commentaire;
        message_commentaire.setText(StringUtils.toEmoji(commentaireStr));

        if (commentaire.level >= DjihtiConstant.ADMIN_GRADUATION)
            pseudo_user.setTextColor(getContext().getResources().getColor(android.R.color.holo_red_dark));
        else if (commentaire.level == DjihtiConstant.BANNED_GRADUATION)
            pseudo_user.setTextColor(getContext().getResources().getColor(android.R.color.darker_gray));
        else
            pseudo_user.setTextColor(getContext().getResources().getColor(android.R.color.black));

        if (commentaire.level >= DjihtiConstant.HEIGHT_ADMIN_GRADUATION)
            pseudo_user.setTextColor(getContext().getResources().getColor(R.color.color3));
        else if (commentaire.level >= DjihtiConstant.MEDIUM_ADMIN_GRADUATION)
            pseudo_user.setTextColor(getContext().getResources().getColor(R.color.rouge_nacarat));

        Date date = DateUtils.strToDate(commentaire.date_creation);

        date_commentaire.setText(DateUtils.getRelativeTime(date));

		if (idUser.equals(commentaire.id_membre) || mainPreferences.getLevelUser() >= DjihtiConstant.ADMIN_GRADUATION) {

            setOnLongClickListener(new OnLongCardClickListener() {
                @Override
                public boolean onLongClick(Card card, View view) {


                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(getContext().getString(R.string.alert_titre_action_commentaire))
                                    //   .setContentText(getContext().getString(R.string.alert_titre_action_commentaire))
                            .setCancelText(getContext().getString(R.string.alert_action_modifier_photo_add_item))
                            .setConfirmText(getContext().getString(R.string.alert_action_supp_photo_add_item))
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    listener.onSelectDeleteComment(commentaire.id, idContent, CommentShowCard.this);
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {

                                    UpdateCommentDialog	updatedDilaog =
                                            new UpdateCommentDialog(getContext(),
                                                    commentaire.commentaire,
                                                    commentaire.id,
                                                    CommentShowCard.this,
                                                    listener);
                                    updatedDilaog.show();
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .show();


                    return false;
                }
            });
		/*	fontEditComment.setVisibility(View.VISIBLE);
			fontEditComment.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					
				}
			});*/
		}
		else
            setOnLongClickListener(null);
			//fontEditComment.setVisibility(View.GONE);

        Picasso.with(getContext())
                .load(commentaire.url_avatar)
                .placeholder(R.drawable.djihti_photo)
                .into(avatar_user);
		

		avatar_user.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShowProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id_membre", commentaire.id_membre);
                getContext().startActivity(intent);
            }
        });

        Log.e("CommentShowCard", " ---> " + commentaire.url_images.size());
        Log.e("CommentShowCard", " ---> " + commentaire.url_images.toString());
        // On vérifie qu'il y a d'abord des images
        if (commentaire.url_images.size() != 0) {

            imageView.setVisibility(View.VISIBLE);

            Picasso.with(getContext())
                    .load(commentaire.url_images.get(0).toString())
                    .centerCrop().fit()
                    .placeholder(R.drawable.djihti_photo)
                    .into(imageView);

           /* Picasso.with(getContext())
                    .load(commentaire.url_images.get(0).toString())
                    .resize(400, 400)
                    .centerInside()
                    .placeholder(R.drawable.djihti_photo)
                    .into(imageView);*/



            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                 //   Intent intent = new Intent(getContext() , FullImageViewSlider.class);
                    Intent intent = new Intent(getContext() , ViewPagerActivity.class);
                    intent.putExtra("position", 0);
                    intent.putExtra("url_to_split", urlWithToSplit);
                    getContext().startActivity(intent);

                    System.out.println(urlWithToSplit);
                }
            });

            // Chaine à passer dans l'intent
            urlWithToSplit = UrlToSplit.strWithComma(commentaire.url_images);
        }
        else // Il n'y a pas d'images
        {
            imageView.setVisibility(View.GONE);
        }
		
		super.setupInnerViewElements(parent, view);
	}





}
