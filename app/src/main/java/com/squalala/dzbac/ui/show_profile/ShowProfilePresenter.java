package com.squalala.dzbac.ui.show_profile;

import com.squalala.dzbac.data.UserInformations;

import okhttp3.RequestBody;
import retrofit.mime.TypedFile;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ShowProfilPresenter.java
 * Date : 31 juil. 2014
 * 
 */
public interface ShowProfilePresenter {
	
	void getDataUser(String idMembre);

	void blockUser(String idMembre);

	void bannUser(String idMembre);
	
	void updateDataUser(UserInformations userInformations);
	
	void updateAvatarUser(RequestBody typedFile);
	
	void updateBackgroundUser(RequestBody typedFile);

	void onFollowUser(String idMembre);

	void onUnFollowUser(String idMembre);

}
