package com.squalala.dzbac.interactors;

import com.squalala.dzbac.google.GoogleManagerListener;
import com.squalala.dzbac.ui.sign.OnSignListener;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : SignInteractor.java
 * Date : 22 juin 2014
 * 
 */
public interface SignInteractor {
	
	public void register(String pseudo, String password,String confirme_password,
			String email, OnSignListener listener);
	
	public void afterEmailChanged(CharSequence email, OnSignListener listener);
	
	public void onPseudoChanged(CharSequence pseudo, int count, OnSignListener listener);
	
	public void registerByGoogle(String pseudo, String email, 
			String idGoogle, String urlAvatar, GoogleManagerListener listener);

}
