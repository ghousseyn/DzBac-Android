package com.squalala.dzbac.ui.main;

import android.content.Context;
import android.content.pm.PackageManager;

import com.squalala.dzbac.DjihtiConstant;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.interactors.UserInteractor;

import hugo.weaving.DebugLog;


/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : MainPresenterImpl.java
 * Date : 3 juil. 2014
 * 
 */
public class MainPresenterImpl implements MainPresenter, MainListener {
	
	private MainView mainView;
	private UserInteractor interactor;
	private Context context;
	private MainPreferences preferences;

	public MainPresenterImpl(MainView mainView, UserInteractor interactor, Context context, MainPreferences preferences) {
		this.mainView = mainView;
		this.interactor = interactor;
		this.context = context;
		this.preferences = preferences;
	}

	@Override
	public void checkStateUser() {
		interactor.getStateUser(this);
	}


	@DebugLog
	@Override
	public void onStateUser(int countMessage, int countNotification, int stateBan, int minVersionAppCode, int codePrenium, int levelContribution) {
		mainView.setCountMessages(countMessage);
		mainView.setCountNotifications(countNotification);

		preferences.setLevelContribution(levelContribution);

		boolean banned = stateBan == 1;

		if (codePrenium == DjihtiConstant.CODE_ERROR_MULTIPLE_DEVICE)
			mainView.showPopupMultipleDevice();
		else
			preferences.setPrenium(codePrenium);

		if (codePrenium == 0)
			mainView.setErrorPremium();

		if (banned)
			mainView.showPopupBan();

		try {

			int versionCode = context.getPackageManager().getPackageInfo( (context.getPackageName()), 0).versionCode;

			if (versionCode < minVersionAppCode)
				mainView.showPopupVersionOutdated();

		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
	}


}
