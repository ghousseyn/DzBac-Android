package com.squalala.dzbac.ui.show_profile;

import com.squalala.dzbac.data.api.ApiResponse.User;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ShowProfileListener.java
 * Date : 31 juil. 2014
 * 
 */
public interface ShowProfileListener {
	
	void onLoadData(User user);

	void onUserFollowed(String message);

	void onUserUnFollowed(String message);

	void onUpdatePhoneUser(String idMembre, String message, boolean isBanned, boolean isPhoneAlreadyUsed);
	
	void onUpdateDataUser(String idMembre, String message);
	
	void onAvatarUploaded(String urlAvatar);
	
	void onBackgroundUploaded(String urlBackground);
	
	void onNameUserError();

}
