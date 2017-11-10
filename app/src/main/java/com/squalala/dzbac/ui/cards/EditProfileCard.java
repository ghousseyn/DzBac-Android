package com.squalala.dzbac.ui.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.squalala.dzbac.R;
import com.squalala.dzbac.data.UserInformations;
import com.squalala.dzbac.data.api.ApiResponse.User;

import butterknife.ButterKnife;
import butterknife.InjectView;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : EditProfileCard.java
 * Date : 4 août 2014
 * 
 */
public class EditProfileCard extends Card {
	
	@InjectView(R.id.edit_nom_prenom)
    EditText edtiUsername;
	@InjectView(R.id.spinner_localisation) Spinner spinnerLocalisation;
	@InjectView(R.id.spinner_about) Spinner spinnerAbout;
	
	private User user;
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public void setErrorUserName(String message) {
		edtiUsername.setError(message);
	}
	
	public EditProfileCard(Context context) {
		super(context , R.layout.card_edit_profile_inner_main);
	}
	
	/**
	 * Pour obtenir le json de UserInfomations
	 */
	public UserInformations getUserInfomations() {
		
		UserInformations userInfo = new UserInformations();
		userInfo.setLocalisation(getLocalisation());
		userInfo.setNom(edtiUsername.getText().toString().trim());
		userInfo.setApropos(getAbout());
		
		return userInfo;
	}
	
	private String getLocalisation() {
		if (spinnerLocalisation.getSelectedItemPosition() == -1)
			return "no localisation";
		else
			return spinnerLocalisation.getSelectedItem().toString();
	}
	
	private String getAbout() {
		return spinnerAbout.getSelectedItem().toString();
	}
	
	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		super.setupInnerViewElements(parent, view);
		ButterKnife.inject(this , parent);
		
		String [] wilayas = getContext().getResources().getStringArray(R.array.wilaya);
		int positionWilaya = 0;
		
		for (int i = 0; i < wilayas.length; i++) {
			if (wilayas[i].equals(user.localisation)) {
				positionWilaya = i;
				break;
			}
		}
		
		spinnerLocalisation.setSelection(positionWilaya);
		
		String [] abouts = getContext().getResources().getStringArray(R.array.about);
		int positionAbout = 0;
		
		for (int i = 0; i < abouts.length; i++) {
			if (abouts[i].equals(user.apropos)) {
				positionAbout = i;
				break;
			}
		}
		
		spinnerAbout.setSelection(positionAbout);
		
		edtiUsername.setText(user.userName);
	}

}
