package com.squalala.dzbac.utils;

import android.text.TextUtils;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : Check.java
 * Date : 24 juin 2014
 * 
 */
public class Check {
	
	public final static boolean isValidEmail(CharSequence target) {
		  if (TextUtils.isEmpty(target)) {
		    return false;
		  } else {
		    return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		  }
		}
	
	public final static String pseudoValidate(String pseudo) {
		return pseudo.replaceAll("[^a-zA-Z0-9_-]", "");
	}

}
