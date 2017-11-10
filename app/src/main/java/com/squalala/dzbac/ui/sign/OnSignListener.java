package com.squalala.dzbac.ui.sign;
/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : OnSignFinishedListener.java
 * Date : 22 juin 2014
 * 
 */
public interface OnSignListener {
	
	void onPseudoError();
	
	void onPseudoNotAvailaibleError(String message);
	
	void onPasswordError();
	
	void onPasswordDifferentError();
	
	void onEmailError();
	
	void onEmailInvalid();
	
	void onEmailNotAvailaible(String message);
	
	void onRegistred(String idMembre, String pseudo, String email, String passwordEncripted);

	void onRegisterError(String message);

}
