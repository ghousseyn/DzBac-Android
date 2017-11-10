package com.squalala.dzbac.google;

import com.squalala.dzbac.data.api.ApiResponse.Login;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : GoogleManagerListener.java
 * Date : 21 sept. 2014
 * 
 */
public interface GoogleManagerListener {
	
	// L'id google fera office de mot de passe
	void onRegister(String idMembre, String pseudo, String email, String idGoogle);
	
	void onLogin(Login login, String email, String idGoogle);
	
	void onFailRegisterOrLogin(String message);

	void onServerError();

}
