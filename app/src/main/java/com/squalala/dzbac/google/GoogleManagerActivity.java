package com.squalala.dzbac.google;

import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LoginEvent;
import com.crashlytics.android.answers.SignUpEvent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.R;
import com.squalala.dzbac.common.DjihtiBaseActivity;
import com.squalala.dzbac.ui.edit_profile.EditProfileActivity;
import com.squalala.dzbac.ui.main.MainActivity;

import javax.inject.Inject;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : GooglePlusSignInActivity.java
 * Date : 21 sept. 2014
 * 
 */
public class GoogleManagerActivity extends DjihtiBaseActivity 
	implements ConnectionCallbacks, OnConnectionFailedListener, GoogleView {
	
	// Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;
    private ConnectionResult mConnectionResult;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private boolean isRegister = true;
    private static final int RC_SIGN_IN = 0;
    
    // Google client to interact with Google API
    @Inject GoogleApiClient mGoogleApiClient;
    @Inject GooglePresenter googlePresenter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_google);
		
		isRegister = getIntent().getBooleanExtra("is_register", false);
		
		signInWithGplus();
	}
	
    /**
     * Sign-in into google
     * */
    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            Log.e("FAIl", "TRUE");
          //  resolveSignInError();
        }
    }

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		 if (!result.hasResolution()) {
	            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
	                    0).show();
	            return;
	        }
		 
	        if (!mIntentInProgress) {
	            // Store the ConnectionResult for later usage
	            mConnectionResult = result;
	 
	            if (mSignInClicked) {
	                // The user has already clicked 'sign-in' so we attempt to
	                // resolve all
	                // errors until the user is signed in, or they cancel.
	                resolveSignInError();
	            }
	        }
	        Log.e("COOC" , "FAIL");
	}

	@Override
	public void onConnected(Bundle connectionHint) {
	        // Get user's information
	        getProfileInformation();
	}

	@Override
	public void onConnectionSuspended(int cause) {
		mGoogleApiClient.connect();
	}
	
	 /**
     * Fetching user's information name, email, profile pic
     * */
    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                
                String pseudo = currentPerson.getDisplayName();
                String urlAvatar = currentPerson.getImage().getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                String idGoogle = currentPerson.getId();
                urlAvatar = urlAvatar.substring(0,
                        urlAvatar.length() - 2)
                        + PROFILE_PIC_SIZE;
                
                if (isRegister)
                	googlePresenter.register(pseudo,
                			email,
                			idGoogle,
                			urlAvatar);
                else 
                	googlePresenter.login(email, idGoogle);
 
                Log.e("dze", "Name: " + pseudo + ", plusProfile: "
                        + ", email: " + email
                        + ", Image: " + urlAvatar + " about " + currentPerson.getAboutMe() +
                        " test " + currentPerson.getBirthday() + 
                        " rrr "  + currentPerson.getId());
 
            } else {
                Toast.makeText(getApplicationContext(),
                        "ERROR 11", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	 @Override
    protected void onActivityResult(int requestCode, int responseCode,
            Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }
 
            mIntentInProgress = false;
 
            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }
	
	@Override
	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		 if (mGoogleApiClient.isConnected()) {
	            mGoogleApiClient.disconnect();
	        }
	}
	
	 /**
     * Method to resolve any signin errors
     * */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    /*@Override
	protected List<Object> getModules() {
		return Arrays.<Object>asList(new GoogleModule(this, this, this, this)); 
	}*/

	@Override
	protected void setupComponent(AppComponent appComponent) {
		DaggerGoogleComponent.builder()
				.appComponent(appComponent)
				.googleModule(new GoogleModule(this, this, this, this))
				.build()
				.inject(this);
	}

	@Override
	public void navigateToEditProfile(String idUser) {

		// La personne vient de s'inscrire sur le serveur
		Answers.getInstance().logSignUp(new SignUpEvent()
				.putMethod("Google")
				.putSuccess(true));

		Intent intent = new Intent(this, EditProfileActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("is_register", true);
		intent.putExtra("id_membre", idUser);
		startActivity(intent);
		finish();
	}

	@Override
	public void navigateToHome() {

		Answers.getInstance().logLogin(new LoginEvent()
				.putMethod("Google")
				.putSuccess(true));

		Intent intent = new Intent(this , MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	@Override
	public void setError(String message) {
		Crouton.makeText(this, message, Style.ALERT).show();
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				finish();
			}
		}, 2500);
	}

}
