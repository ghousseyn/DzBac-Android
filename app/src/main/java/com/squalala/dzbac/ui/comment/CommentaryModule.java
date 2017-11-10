package com.squalala.dzbac.ui.comment;

import android.app.Activity;
import android.support.v4.content.ContextCompat;

import com.squalala.dzbac.R;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.interactors.CommentaryInteractor;
import com.squalala.dzbac.interactors.NotificationInteractor;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dagger.Module;
import dagger.Provides;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : CommentaryModule.java
 * Date : 30 juil. 2014
 * 
 */
@Module
public class CommentaryModule {
	
	private CommentaryView commentaryView;

	public CommentaryModule(CommentaryView commentaryView) {
		this.commentaryView = commentaryView;
	}
	
	@Provides
	public CommentaryView provideView() {
		return commentaryView;
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
     public CommentaryPresenter providePresenter(CommentaryView view, 
		  CommentaryInteractor interactor,
		  NotificationInteractor notificationInteractor, Activity activity, MainPreferences preferences) {
	    return new CommentaryPresenterImpl(view, interactor, notificationInteractor, activity, preferences);
     }

}
