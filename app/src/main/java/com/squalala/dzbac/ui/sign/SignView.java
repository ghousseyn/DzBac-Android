package com.squalala.dzbac.ui.sign;


/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : SignView.java
 * Date : 22 juin 2014
 * 
 */
public interface SignView {

	 void showMessage(String message);
	
	 void showProgress();

	 void hideProgress();
	 
	 void setPseudoError();
	 
	 void setPseudoNotAvailaibleError(String message);
	 
	 void setEmailError();
	 
	 void setEmailInvalidError();
	 
	 void setEmailNotAvailaibleError(String message);
	 
	 void setPasswordError();
	 
	 void setPasswordDifferentError();
	 
	 void navigateToEditProfile(String idUser);

}
