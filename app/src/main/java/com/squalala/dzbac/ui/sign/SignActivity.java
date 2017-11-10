package com.squalala.dzbac.ui.sign;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.SignUpEvent;
import com.google.android.gms.analytics.Tracker;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.R;
import com.squalala.dzbac.common.DjihtiBaseActivity;
import com.squalala.dzbac.ui.edit_profile.EditProfileActivity;
import com.squalala.dzbac.utils.GCMUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : SignActivity.java
 * Date : 22 juin 2014
 * 
 */
public class SignActivity extends DjihtiBaseActivity 
	implements SignView, OnClickListener{
	
	@Inject SignPresenter signPresenter;

	@Inject
	Tracker tracker;
	
	@InjectView(R.id.btn_sign) Button btn_sign;
	@InjectView(R.id.editEmailSign) BootstrapEditText editEmail; 
	@InjectView(R.id.editMdpSign) BootstrapEditText editMdp;
	@InjectView(R.id.editMdpConfirmeSign) BootstrapEditText editMdpConfirme;
	@InjectView(R.id.editPseudoSign) BootstrapEditText editPseudo;
	@InjectView(R.id.progressSign) ProgressBar progressBar;


    @InjectView(R.id.tool_bar)
    Toolbar toolbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign);

		tracker.setScreenName(getClass().getName());
		
		ButterKnife.inject(this);
		btn_sign.setOnClickListener(this);

        setSupportActionBar(toolbar);
		
	/*	editPseudo.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				editPseudo.setError(null);
				signPresenter.checkPseudoUser(s.toString(), count);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {}
		});
	*/


        editEmail.setText(GCMUtils.getEmail(this));

		editEmail.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			@Override
			public void afterTextChanged(Editable s) {
				editEmail.setError(null);
				signPresenter.checkEmailUser(s.toString());
			}
		});
		
	}

	@Override
	public void showMessage(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	@Override
	protected void setupComponent(AppComponent appComponent) {
		DaggerSignComponent.builder()
				.appComponent(appComponent)
				.signModule(new SignModule(this))
				.build()
				.inject(this);
	}

	@Override
	public void onClick(View v) {
		signPresenter.validateUserdata(getPseudo(), getEmail(), getPassword(), getPasswordConfirme());
		
	}

	@Override
	public void showProgress() {
		btn_sign.setEnabled(false);
		progressBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgress() {
		btn_sign.setEnabled(true);
		progressBar.setVisibility(View.INVISIBLE);
	}

	@Override
	public void setPseudoError() {
		editPseudo.setError(getString(R.string.error_pseudo_empty));
	}


	@Override
	public void setPseudoNotAvailaibleError(String message) {
		editPseudo.setError(message);
	}


	@Override
	public void setEmailError() {
		editEmail.setError(getString(R.string.error_email_empty));
	}


	@Override
	public void setEmailNotAvailaibleError(String message) {
		editEmail.setError(message);
	}


	@Override
	public void setPasswordError() {
		editMdp.setError(getString(R.string.error_password_empty));
	}


	@Override
	public void setPasswordDifferentError() {
		editMdpConfirme.setError(getString(R.string.error_password_confirme));
	}
	
	@Override
	public void setEmailInvalidError() {
		editEmail.setError(getString(R.string.error_email_invalide));
	}

	@Override
	public void navigateToEditProfile(String idUser) {

		// La personne vient de s'inscrire sur le serveur
		Answers.getInstance().logSignUp(new SignUpEvent()
				.putMethod("Email")
				.putSuccess(true));


		Toast.makeText(getApplicationContext(), getString(R.string.inscription_reussi),
				Toast.LENGTH_LONG).show();
		
		Intent intent = new Intent(this, EditProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("is_register", true);
		intent.putExtra("id_membre", idUser);
		startActivity(intent);
		finish();
	}
	
	private String getPseudo() {
		return editPseudo.getText().toString().trim();
	}
	
	private String getEmail() {
		return editEmail.getText().toString().trim();
	}
	
	private String getPassword() {
		return editMdp.getText().toString().trim();
	}
	
	private String getPasswordConfirme() {
		return editMdpConfirme.getText().toString().trim();
	}
	

}
