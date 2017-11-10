package com.squalala.dzbac.ui.cards;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squalala.dzbac.R;
import com.squalala.dzbac.data.api.ApiResponse.User;

import butterknife.ButterKnife;
import butterknife.InjectView;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ProfileInfoCard.java
 * Date : 1 août 2014
 * 
 */
public class ProfileInfoCard extends Card {
	
	@InjectView(R.id.profile_nom_complet_user) TextView nom;
	@InjectView(R.id.profile_localisation) TextView localisation;
	@InjectView(R.id.profile_profession) TextView apropos;
	
	private User user;

	public ProfileInfoCard(Context context) {
		super(context, R.layout.card_profile_info_inner_main);
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		
		ButterKnife.inject(this, parent);
		
		String aproposStr = user.apropos.replaceAll("(\r\n|\n)", "<br/>");
		apropos.setText(Html.fromHtml(getContext().getString(R.string.profile_apropos) + 
				" " + aproposStr));
		
		localisation.setText(Html.fromHtml(getContext().getString(R.string.profile_localisation) +
                " " + user.localisation));
		
		nom.setText(user.userName);
		
		super.setupInnerViewElements(parent, view);
	}

}
