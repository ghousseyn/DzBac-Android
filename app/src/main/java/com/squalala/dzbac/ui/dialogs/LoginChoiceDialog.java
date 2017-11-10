package com.squalala.dzbac.ui.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.squalala.dzbac.R;
import com.squalala.dzbac.google.GoogleManagerActivity;
import com.squalala.dzbac.ui.login.LoginActivity;
import com.squalala.dzbac.ui.splash.OnLoginChoiceListener;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : SignChoiceDialog.java
 * Date : 24 juil. 2014
 * 
 */
public class LoginChoiceDialog extends Dialog implements OnClickListener {
	
	@InjectView(R.id.btn_login_dialog_facebook) BootstrapButton btn_facebook_sign;
	@InjectView(R.id.btn_login_dialog_email) BootstrapButton btn_email_sign;
	@InjectView(R.id.btn_login_dialog_google) BootstrapButton btn_google_sign;
	@InjectView(R.id.progress) ProgressBar progressBar;
	
	private Dialog dialog = this;
	private Activity activity;

	public LoginChoiceDialog(Activity activity) {
		super(activity);
		this.activity = activity;
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_login_facebook_or_email);
		setTitle(R.string.dialog_connection);
		
		ButterKnife.inject(this);
		btn_email_sign.setOnClickListener(this);
		btn_facebook_sign.setOnClickListener(this);
		btn_google_sign.setOnClickListener(this);
		
	//	btn_facebook_sign.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login_dialog_facebook:

			OnLoginChoiceListener mListener = (OnLoginChoiceListener) activity;

			mListener.onChooseLoginFacebook();

			break;
			
		case R.id.btn_login_dialog_email:
			activity.startActivity(new Intent(activity, LoginActivity.class));
			break;
			
		case R.id.btn_login_dialog_google:
			Intent intent = new Intent(activity, GoogleManagerActivity.class);
			intent.putExtra("is_register", false);
			activity.startActivity(intent);
			cancel();
		default:
			break;
		}
	}

}
