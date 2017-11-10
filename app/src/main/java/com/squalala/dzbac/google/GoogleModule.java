package com.squalala.dzbac.google;

import android.app.Activity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.interactors.LoginInteractor;
import com.squalala.dzbac.interactors.SignInteractor;

import dagger.Module;
import dagger.Provides;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : GoogleModule.java
 * Date : 21 sept. 2014
 * 
 */
@Module
public class GoogleModule {
	
	private GoogleView googleView;
	private Activity activity;
	private ConnectionCallbacks connectionCallbacks;
	private OnConnectionFailedListener connectionFailedListener;
	
	public GoogleModule(GoogleView googleView, Activity activity,
			ConnectionCallbacks callbacks,
			OnConnectionFailedListener connectionFailedListener) {
		this.googleView = googleView;
		this.activity = activity;
		this.connectionCallbacks = callbacks;
		this.connectionFailedListener = connectionFailedListener;
	}
	
	@Provides
	public GoogleView provideGoogleView() {
		return googleView;
	}
	
	@Provides
	public GoogleApiClient provideGoogleApiClient() {
		return  new GoogleApiClient.Builder(activity)
        .addConnectionCallbacks(connectionCallbacks)
        .addOnConnectionFailedListener(connectionFailedListener).addApi(Plus.API)
        .addScope(Plus.SCOPE_PLUS_LOGIN).build();
	}
	
	@Provides
	public GooglePresenter providePresenter(GoogleView view, SignInteractor signInteractor,
			LoginInteractor loginInteractor, MainPreferences mainPreferences) {
		return new GooglePresenterImpl(view, loginInteractor, signInteractor, mainPreferences);
	}
}
