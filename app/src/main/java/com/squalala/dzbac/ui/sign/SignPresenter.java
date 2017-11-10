package com.squalala.dzbac.ui.sign;


/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : SignPresenter.java
 * Date : 22 juin 2014
 * 
 */
public interface SignPresenter {
	
	void validateUserdata(String pseudo, String email, String password, String confirme_password);
	
	void checkEmailUser(String email);
	
	void checkPseudoUser(String pseudo, int count);

}
