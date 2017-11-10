package com.squalala.dzbac.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import com.squalala.dzbac.DjihtiApp;
import com.squalala.dzbac.R;
import com.squalala.dzbac.data.prefs.MainPreferences;

import javax.inject.Inject;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : SettingsActivity.java
 * Date : 15 août 2014
 * 
 */
public class SettingsActivity extends PreferenceActivity {
	
	@Inject MainPreferences mainPreferences;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	((DjihtiApp) getApplication()).inject(this);
		 DjihtiApp.get(this).component().inject(this);
		 addPreferencesFromResource(R.xml.settings);
		// Display the fragment as the main content.
      /*  FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager
                                .beginTransaction();
        PrefsFragment mPrefsFragment = new PrefsFragment();
        mFragmentTransaction.replace(android.R.id.content, mPrefsFragment);
        mFragmentTransaction.commit();*/
	}
	
	@Override
	protected void onStop() {
		Log.e("ON STOP", "SETTINGS");
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		System.out.println("SOUND ---> " + preferences.getBoolean("sound", true));
		
		mainPreferences.setEnabledSound(preferences.getBoolean("sound", true));
		mainPreferences.setEnabledVibrate(preferences.getBoolean("vibrate", true));
		mainPreferences.setFollowing(preferences.getBoolean("follower_notification", true));
		mainPreferences.setEnableDailyNotification(preferences.getBoolean("daily_notification", true));
		mainPreferences.setEnableCall(preferences.getBoolean("enable_call", true));
		
		
		super.onStop();
	}
	
	/*@SuppressLint("NewApi")
	public static class PrefsFragment extends PreferenceFragment {
		 
        @Override
        public void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);

                    // Load the preferences from an XML resource
                    addPreferencesFromResource(R.xml.settings);
        }
}
*/
}
