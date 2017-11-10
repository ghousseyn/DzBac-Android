package com.squalala.dzbac.ui.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.squalala.dzbac.R;
import com.squalala.dzbac.google.GoogleManagerActivity;
import com.squalala.dzbac.ui.sign.SignActivity;
import com.squalala.dzbac.ui.splash.OnLoginChoiceListener;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : SignChoiceDialog.java
 * Date : 24 juil. 2014
 * 
 */
public class SignChoiceDialog extends Dialog implements OnClickListener {
	
	@InjectView(R.id.btn_sign_dialog_facebook) BootstrapButton btn_facebook_sign;
	@InjectView(R.id.btn_sign_dialog_email) BootstrapButton btn_email_sign;
	@InjectView(R.id.btn_sign_dialog_google) BootstrapButton btn_google_sign;
	@InjectView(R.id.progress) ProgressBar progressBar;
	
	private Dialog dialog = this;
	private Activity activity;

    private static final String TAG = SignChoiceDialog.class.getSimpleName();

	public SignChoiceDialog(Activity activity) {
		super(activity);
		this.activity = activity;
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_sign_facebook_or_email);
		setTitle(R.string.dialog_inscription);
		
		ButterKnife.inject(this);
		btn_email_sign.setOnClickListener(this);
		btn_facebook_sign.setOnClickListener(this);
		btn_google_sign.setOnClickListener(this);
		
	//	btn_facebook_sign.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btn_sign_dialog_facebook:
			
			Log.e("FACEBOOK", "INSCRIPTION");

			OnLoginChoiceListener mListener = (OnLoginChoiceListener) activity;

			mListener.onChooseSignUpFacebook();


			break;
			
		case R.id.btn_sign_dialog_email:

			activity.startActivity(new Intent(activity, SignActivity.class));

			break;
			
		case R.id.btn_sign_dialog_google:

			Intent intent = new Intent(activity, GoogleManagerActivity.class);
			intent.putExtra("is_register", true);
			activity.startActivity(intent);
			cancel();

			break;

		default:
			break;
		}
	}

}
