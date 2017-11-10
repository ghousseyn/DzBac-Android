package com.squalala.dzbac.google;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : GooglePresenter.java
 * Date : 21 sept. 2014
 * 
 */
public interface GooglePresenter {
	
	public void register(String pseudo, String email,
			String idGoogle, String urlAvatar);
	
	public void login(String email, String idGoogle);
	

}
