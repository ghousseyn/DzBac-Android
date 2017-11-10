/**
 * 
 */
package com.squalala.dzbac.ui.splash;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : SplashView.java
 * Date : 20 juin 2014
 * 
 */
public interface SplashView {
	
	// On affiche les boutons login et sign-up si l'utilisateur n'est pas connecté
	public void showBtnLoginSignUp();
	  
	//On mène l'utilisateur à l'écran principale où il y a les annonces, récupérations...etc
	public void navigateToMainScreen();
	
	// On informe l'utilisateur qu'une connexion internet est requise pour continuer
	public void setConnectionError();

}
