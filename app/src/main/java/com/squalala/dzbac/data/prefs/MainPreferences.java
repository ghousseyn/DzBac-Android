package com.squalala.dzbac.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.digits.sdk.android.Digits;
import com.squalala.dzbac.DjihtiConstant;
import com.squalala.dzbac.utils.SystemUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import hugo.weaving.DebugLog;

import static android.content.Context.MODE_PRIVATE;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : AnnoncePreferences.java
 * Date : 20 juil. 2014
 * Description : Cette classe sert à sauvegarder les préférences de l'utilisateur avec sa position
 * 
 */
public class MainPreferences {
	
	private SharedPreferences preferences;
	private Context context;

	private static final String KEY_SPLASH = "splash";

	public MainPreferences(Context context) {
		preferences = context.getSharedPreferences("dzbac", MODE_PRIVATE);
		this.context = context;
	}
	
	public MainPreferences(SharedPreferences sharedPreferences) {
		preferences = sharedPreferences;
	}

	public boolean counterForAdInter() {

		String key = "count_ad_inter";
		int count = preferences.getInt(key, 0);

		if (count++ >= DjihtiConstant.LIMIT_NUMBER_SHOW_AD) {

			count = 0;
			preferences.edit().putInt(key, count).apply();
			return true;
		}

		Log.e("Prefs", "counter : "+ count);
		preferences.edit().putInt(key, count).apply();

		return false;
	}
	
	/** 
	 *   SETTER
	 */
	
	public void setShowCaseMain(boolean isView) {
		preferences.edit().putBoolean("show_main_activity", isView).commit();
	}

	public void setBanned(boolean isView) {
		preferences.edit().putBoolean("banned", isView).commit();
	}

    public void setShowCaseShowItem(boolean isView) {
        preferences.edit().putBoolean("show_item_activity", isView).commit();
    }

	public void setLevelUser(int value) {
		preferences.edit().putInt("level_user", value).apply();
	}

	public int getLevelUser() {
		return preferences.getInt("level_user", 0);
	}

	@DebugLog
	public void setLevelContribution(int value) {
		preferences.edit().putInt("level_contribution", value).apply();
	}

	public int getLevelContribution() {
		return preferences.getInt("level_contribution", 0);
	}
	
	public void setEmailUser(String value) {
		preferences.edit().putString("email", value).apply();
	}

	public void setCurrentCommentaireId(String id) {
		preferences.edit().putString("id_content_commentaire", id).commit();
	}

	public String getCurrentCommentaireId() {
		return preferences.getString("id_content_commentaire", null);
	}

	public void setPasswordUser(String value) {
		preferences.edit().putString("password", value).apply();
	}
	
	public void setSubject(int subject) {
		preferences.edit().putInt("matière", subject).apply();
	}

    @DebugLog
	public void setFormat(int format) {
		preferences.edit().putInt("format", format).apply();
	}

	public int getFormat() {
		return preferences.getInt("format", 0);
	}
	
	public void setMotCle(String subject) {
		preferences.edit().putString("mot_cle", subject).apply();
	}
	
	public void setType(int type) {
		preferences.edit().putInt("type", type).apply();
	}
	
	public void setPseudo(String value) {
		preferences.edit().putString("pseudo", value).apply();
	}

	public void setPathVideo(String value) {
		preferences.edit().putString("path_video", value).apply();
	}

	public void setPathImage(String value) {
		preferences.edit().putString("path_image", value).apply();
	}

	public String getPathVideo() {
		return preferences.getString("path_video", "");
	}

	public String getPathImage() {
		return preferences.getString("path_image", "");
	}

	
	public void setApiKey(String value) {
		preferences.edit().putString("api_key", value).apply();
	}
	
	public void setGCMId(String gcmId) {
		preferences.edit().putString("gcm_id", gcmId).commit();
	}
	
	public void setIdUser(String value) {
		preferences.edit().putString("id_membre", value).apply();
	}

    public void setUrlPhoto(String value) {
        preferences.edit().putString("url_avatar", value).apply();
    }

	public void setPhoneNumber(String value) {
		preferences.edit().putString("phone_number", value).apply();
	}

	public void setCalling(boolean value) {
		preferences.edit().putBoolean("calling", value).apply();
	}

	public void setEnableCall(boolean value) {
		preferences.edit().putBoolean("enable_call", value).apply();
	}

	@DebugLog
	public void setPrenium(int value) {
		Log.e("Prefs", ""+value);
		preferences.edit().putInt("prenium", value).apply();
	}

	public boolean isPrenium() {
		Log.e("Prefs", "" + preferences.getInt("prenium", 0));
		return preferences.getInt("prenium", 0) > 0;
	}

	public boolean isInTestPeriode() {
		return preferences.getInt("prenium", 0) == 4;
	}

	public int getPremiumCode() {
		return preferences.getInt("prenium", 0);
	}

	public boolean isCallEnabled() {
		return preferences.getBoolean("enable_call", true);
	}

	public boolean isCalling() {
		return preferences.getBoolean("calling", false);
	}

	public boolean isPhoneNumberVerified() {

		if (preferences.getString("phone_number", null) != null)
			return true;
		else
			return false;
	}

    public void incrementValue() {
        preferences.edit().putInt("note_app", getNoteAppValue() + 1).commit();
    }

    public void setValueNoteApp(int value) {
        preferences.edit().putInt("note_app", value).commit();
    }

	public void setTutoGuide(String key, boolean value) {
		preferences.edit().putBoolean(key, value).commit();
	}

	public boolean isTutoGuide(String key) {
		return preferences.getBoolean(key, false);
	}

    public int getNoteAppValue() {
        return preferences.getInt("note_app", 0);
    }

    public void setUrlBackground(String value) {
        preferences.edit().putString("url_background", value).apply();
    }

    public void setUserName(String value) {
        preferences.edit().putString("username", value).apply();
    }
	
	public void setAvatarUser(String value) {
		preferences.edit().putString("url_avatar", value).apply();
	}

	
	public void setEnabledSound(boolean value) {
		preferences.edit().putBoolean("sound", value).apply();
	}

	public void setEnableDailyNotification(boolean value) {
		preferences.edit().putBoolean("daily_notification", value).apply();
	}

	public void setFollowing(boolean value){
		preferences.edit().putBoolean("follower_notification", value).apply();
	}

	public boolean isFollowing() {
		return preferences.getBoolean("follower_notification", true);
	}

	public void setTypeOrder(int value) {
		preferences.edit().putInt("order_by", value).apply();
	}

	public int getTypeOrder() {
		return preferences.getInt("order_by", 0);
	}
	
	public void setEnabledVibrate(boolean value) {
		preferences.edit().putBoolean("vibrate", value).apply();
	}
	
	/**
	 *  Remettre les parametres à zéro pour les filtres
	 */
	
	public void setDefaultPreferences() {
		preferences.edit().putInt("matière", 0).apply();
		preferences.edit().putInt("type", 0).apply();
        preferences.edit().putInt("format", 0).apply();
		preferences.edit().putString("mot_cle", null).apply();
		preferences.edit().putStringSet("tags_id", null).apply();
		setTypeOrder(0);
	}
	 
	
	public void deconnexion() {
		preferences.edit().putString("api_key", "").apply();
		preferences.edit().putString("email", null).apply();
		preferences.edit().putString("id_membre", "").apply();
        preferences.edit().putString("url_background", DjihtiConstant.URL_DEFAULT_BACKGROUND).apply();
        preferences.edit().putString("url_avatar", DjihtiConstant.URL_DEFAULT_AVATAR).apply();
        preferences.edit().putString("username", "CLIQUER SUR PROFILE").apply();
        setPhoneNumber(null);
		Digits.getSessionManager().clearActiveSession();
	}
	
	/**
	 *  GETTER 
	 */

    public String getUrlPhoto() {
        return preferences.getString("url_avatar", DjihtiConstant.URL_DEFAULT_AVATAR);
    }

	public void setSpashScreen(boolean value) {
		preferences.edit().putBoolean(KEY_SPLASH, value).commit();
	}

	public boolean isSplashScreenViewed() {
		return preferences.getBoolean(KEY_SPLASH, false);
	}

	public boolean isBanned() {

		if (preferences.getBoolean("banned", false) || SystemUtils.isFileBanExist())
			return true;
		else
			return false;
	}

    public String getUrlBackground() {
        return preferences.getString("url_background", DjihtiConstant.URL_DEFAULT_BACKGROUND);
    }

	public ArrayList<String> getTagsId() {
		ArrayList tags = new ArrayList<>();

		Set<String> tagsSet = preferences.getStringSet("tags_id", null);

		if (tagsSet != null && tagsSet.size() > 0)
			tags.addAll(tagsSet);
		else
			return tags;

		return tags;
	}

	public void setTagsId(Set<String> value) {
		preferences.edit().putStringSet("tags_id", value).apply();
	}

	@DebugLog
	public ArrayList<String> getFilieres() {

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

		Set<String> languesSet = sharedPrefs.getStringSet("filiere_dzbac", new HashSet<String>());

		//// Si l'utilisateur n'a sélectionné auucne personne, on fait en sorte de lui afficher tout
		//if (languesSet.size() == 0)
		//	languesSet.addAll(new HashSet<>(Arrays.asList(new String [] {"all"})));

		String[] selected = languesSet.toArray(new String[]{});
		System.out.println(selected);

		ArrayList<String> langues = new ArrayList<>();

		Iterator<String> iterator = languesSet.iterator();

		while (iterator.hasNext())
			langues.add(iterator.next());

		// On ajout tous les éléments si il n'y a aucun
		if (langues.size() == 0) {
			langues.add("mat");
			langues.add("sc");
			langues.add("matech");
			langues.add("let");
			langues.add("ges");
		}

		// Toujours par défaut on ajoute ALL
		langues.add("all");

		return langues;
	}

    public String getUserName() {
        return preferences.getString("username", "CLIQUER SUR PROFILE");
    }
	
	public String getEmailUser() {
		return preferences.getString("email", null); 
	}

	public String getPasswordUser() {
		return preferences.getString("password", null);
	}
	
	public int getSubject() {
		return preferences.getInt("matière", 0); 
	}
	
	public String getGCMId() {
		return preferences.getString("gcm_id", null); 
	} 
	
	public int getType() {
		return preferences.getInt("type", 0); 
	}
	
	public String getMotCle() {
		return preferences.getString("mot_cle", null); 
	}
	
	public boolean isEnabledSound() {
		return preferences.getBoolean("sound", true); 
	}

	public boolean isEnabledDailyNotification() {
		return preferences.getBoolean("daily_notification", true);
	}
	
	public boolean isEnabledVibrate() {
		return preferences.getBoolean("vibrate", true); 
	}
	
	public String getApiKey() {
		return preferences.getString("api_key", "");
	}
	
	public boolean isConnected() {
		if (getApiKey() != null && getApiKey().length() > 0)
			return true;
		else
			return false;
	}
	
	public String getPseudo() {
		return preferences.getString("pseudo", null); 
	}
	
	public int getLastIdNotification() {
		
		int idNotification = preferences.getInt("id_notification", 1);
		
		preferences.edit().putInt("id_notification", idNotification + 1).apply();
		
		return idNotification; 
	}

    public int getLastIdPendingIntent() {

        int id_pending = preferences.getInt("id_pending", 1);

        preferences.edit().putInt("id_pending", id_pending + 1).apply();

        return id_pending;

    }

    public int getLastIdNotificationServiceIntent() {

        int id_pending = preferences.getInt("id_notification_service", 1);

        preferences.edit().putInt("id_notification_service", id_pending + 1).apply();

        return id_pending;

    }
	
	public String getIdUser() {
		return preferences.getString("id_membre", "");
	}
	
	public String getAvatarUser() {
		return preferences.getString("url_avatar", null); 
	}
	
	public boolean getShowCaseMain() {
		return preferences.getBoolean("show_main_activity", false);
	}

    public boolean getShowCaseShowItem() {
        return preferences.getBoolean("show_item_activity", false);
    }

}
