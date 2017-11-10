package com.squalala.dzbac.ui.cards.show_info;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squalala.dzbac.R;
import com.squalala.dzbac.DjihtiConstant;
import com.squalala.dzbac.data.api.ApiResponse.Item;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.service.DownloadImageService;
import com.squalala.dzbac.service.DownloadService;
import com.squalala.dzbac.ui.full_imageview.ViewPagerActivity;
import com.squalala.dzbac.ui.payement.PayementActivity;
import com.squalala.dzbac.utils.StringUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import hugo.weaving.DebugLog;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Auteur : Fayçal Kaddouri
 * Date : 26 juil. 2014
 * 
 */
public class AnnonceShowInfoCard extends Card {
	
	@InjectView(R.id.post_description)
    EmojiconTextView description;
    @InjectView(R.id.icon_download_post)
    FontAwesomeText iconDownload;

	private Item post;

    private MainPreferences preferences;


    private final static String KEY = "filename";
    private final static String KEY_URL = "url";


	
	public void setPost(Item post) {
		this.post = post;
	}



	public AnnonceShowInfoCard(Context context) {
		super(context, R.layout.card_post_show_info_inner_main);
        preferences = new MainPreferences(context);
	}
	
	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		
		ButterKnife.inject(this, parent);
		
        if (post.filename != null && !TextUtils.isEmpty(post.filename))
            iconDownload.setVisibility(View.VISIBLE);
        else
            iconDownload.setVisibility(View.GONE);

		String descriptionStr = post.description;

		description.setText((StringUtils.toEmoji(descriptionStr)));

        iconDownload.setOnClickListener(new View.OnClickListener() {

            @DebugLog
            @Override
            public void onClick(View v) {

                if (preferences.getPremiumCode() == 4) {
                    Answers.getInstance().logCustom(new CustomEvent("Download")
                            .putCustomAttribute("Click", "File"));

                    Intent i= new Intent(getContext(), DownloadService.class);
                    i.putExtra(KEY, post.filename);
                    i.putExtra(KEY_URL, DjihtiConstant.URL_UPLOADS + post.filename);
                    i.putExtra("path", post.matiere + "/" + post.type + "/Fichiers/");
                    getContext().startService(i);

                    hideDownloadButton();
                }
                else if (preferences.getLevelContribution() < 2) {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(getContext().getString(R.string.oups))
                            .setContentText(getContext().getString(R.string.alert_legend_message_contrib_error))
                            .setCancelText(getContext().getString(R.string.ok))
                            .setConfirmText(getContext().getString(R.string.en_savoir_plus))
                            .showCancelButton(true)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    getContext().startActivity(new Intent(getContext(), PayementActivity.class));
                                }
                            })
                            .show();
                }
                else {
                    Answers.getInstance().logCustom(new CustomEvent("Download")
                            .putCustomAttribute("Click", "File"));

                    Intent i= new Intent(getContext(), DownloadService.class);
                    i.putExtra(KEY, post.filename);
                    i.putExtra(KEY_URL, DjihtiConstant.URL_UPLOADS + post.filename);
                    i.putExtra("path", post.matiere + "/" + post.type + "/Fichiers/");
                    getContext().startService(i);

                    hideDownloadButton();
                }




            }
        });

		
		super.setupInnerViewElements(parent, view);
	}


    public void hideDownloadButton() {
        iconDownload.setVisibility(View.GONE);
    }

    public void showDownloadButton() {

        // Au cas il n'y a pas de fichier
        if (post.filename != null && !TextUtils.isEmpty(post.filename)) {
            iconDownload.setVisibility(View.VISIBLE);

            YoYo.with(Techniques.BounceIn)
                    .duration(700)
                    .playOn(iconDownload);
        }

    }



}
