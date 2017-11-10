package com.squalala.dzbac.ui.show_profile;

import com.squalala.dzbac.data.api.ApiResponse.User;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ShowProfilView.java
 * Date : 31 juil. 2014
 * 
 */
public interface ShowProfileView {
	
	void displayDataUser(User user);
	
	/**
	 *  Pour avertir que les informations de l'utilisateur ont été updatés
	 */
	void showCrouton(String message);
	
	void finishUploadAvatar(String message);
	
	void finishUploadBackground(String message);
	
	/**
	 *  Sert à rediriger l'utilisateur vers le home, si il est en phase d'inscription
	 */
	void navigateToHome();
	
	void setErrorUserName();

	void updateFollow(boolean isFollowed);
	
}
