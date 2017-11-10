package com.squalala.dzbac.ui.show_conversation;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.content.ContextCompat;

import com.squalala.dzbac.R;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.interactors.MessagingInteractor;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dagger.Module;
import dagger.Provides;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ShowConversationModule.java
 * Date : 2 août 2014
 * 
 */
@Module
public class ShowConversationModule {
	
	private ShowConversationView view;
	
	public ShowConversationModule(ShowConversationView view) {
		this.view = view;
	}
	
	@Provides
	public ShowConversationView provideConversationView() {
		return view;
	}
	
	@Provides
	public Handler provideHandler() {
		return new Handler();
	}

	@Provides
	public SweetAlertDialog provideSweetAlertDialog(Activity activity) {

		SweetAlertDialog pDialog = new SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE);
		pDialog.getProgressHelper().setBarColor(ContextCompat.getColor(activity, R.color.vert_icon_app));
		pDialog.setTitleText(activity.getString(R.string.patientez));
		pDialog.setCancelable(false);

		return pDialog;
	}


	@Provides
	public ShowConversationPresenter provideShowConversationPresenter(
			ShowConversationView view, MessagingInteractor interactor, MainPreferences preferences, Activity activity) {
		return new ShowConversationPresenterImpl(view, interactor, preferences, activity);
	}
	
}
