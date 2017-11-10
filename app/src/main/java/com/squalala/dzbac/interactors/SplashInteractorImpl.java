/**
 * 
 */
package com.squalala.dzbac.interactors;

import android.content.Context;
import android.os.Handler;

import com.squalala.dzbac.DjihtiConstant;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.ui.splash.OnFinishedListener;
import com.squalala.dzbac.utils.ConnectionDetector;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : SplashInteractorImpl.java
 * Date : 20 juin 2014
 * 
 */
public class SplashInteractorImpl implements SplashInteractor {
	
	private MainPreferences mainPreferences;
	private Context context;
	
	public SplashInteractorImpl(MainPreferences mainPreferences,
			Context context) {
		this.mainPreferences = mainPreferences;
		this.context = context;
	}
	
	@Override
	public void stateUser(final OnFinishedListener listener) {
		
		if (ConnectionDetector.isConnectingToInternet(context)) {
			new Handler().postDelayed(new Runnable() {
	            @Override public void run() {
	            	if (mainPreferences.getApiKey() != null)
	            		listener.onUserConnected();
	            	else
	            		listener.onUserNoConnected();
	            }
	        }, DjihtiConstant.SPLASH_TIME_OUT);
		}
		else
			listener.onNoConnection();
		
	}

}
