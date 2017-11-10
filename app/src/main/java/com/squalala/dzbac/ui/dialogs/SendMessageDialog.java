package com.squalala.dzbac.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.squalala.dzbac.R;
import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.data.api.ApiResponse.BasicResponse;
import com.squalala.dzbac.data.api.MessagingService;
import com.squalala.dzbac.data.prefs.MainPreferences;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : SendMessageDialog.java
 * Date : 27 juil. 2014
 * 
 */
public class SendMessageDialog extends Dialog implements OnClickListener {
	
	@InjectView(R.id.edit_message_dialog) BootstrapEditText edit_message;
	@InjectView(R.id.edit_sujet_message_dialog) BootstrapEditText edit_sujet;
	@InjectView(R.id.btn_send_message_dialog) BootstrapButton btn_send_message;
	@InjectView(R.id.progressBar_send_message_dialog) ProgressBar progressBar;
	
	private MessagingService messagingService;
	private String subject, idReceveur;
	private MainPreferences mainPreferences;

	public SendMessageDialog(Context context,
			MessagingService messaging, MainPreferences mainPreferences) {
		super(context);
		this.messagingService = messaging;
		this.mainPreferences = mainPreferences;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.dialog_send_message);
		setTitle(getContext().getString(R.string.dialog_envoyer_message));
		
		ButterKnife.inject(this);
		
		btn_send_message.setOnClickListener(this);
	}
	
	public void setSubject(String s) {
		subject = s;
		if (s != null)
			edit_sujet.setVisibility(View.GONE);
	}
	
	public void setIdReceveur(String id) {
		idReceveur = id;
	}
	
	private void hideElement() {
		btn_send_message.setVisibility(View.GONE);
		edit_message.setVisibility(View.GONE);
		edit_sujet.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		setCancelable(false);
	}
	
	private void showElement() {
		btn_send_message.setVisibility(View.VISIBLE);
		edit_message.setVisibility(View.VISIBLE);
		edit_sujet.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);
		setCancelable(true);
	}
	
	private void clearEdit() {
		edit_message.setText("");
		edit_sujet.setText("");
	}

	@Override
	public void onClick(View v) {
		sendMessage();
	}
	
	private void sendMessage() {
		
		boolean error = false;
		
		if (getSubject().equals("")) {
			Toast.makeText(getContext(), getContext().getString(R.string.error_sujet_conversation),
					Toast.LENGTH_LONG).show();
			error = true;
		}
		
		if (getMessage().equals("")) {
			Toast.makeText(getContext(), getContext().getString(R.string.error_message_vide),
					Toast.LENGTH_LONG).show();
			error = true;
		}
		
		if (!error) {
			
			hideElement();
			
			String pseudoExpediteur = mainPreferences.getPseudo();
			
			messagingService.startConversation(
									getMessage(),
									idReceveur,
									pseudoExpediteur,
									getSubject())
									.enqueue(new retrofit2.Callback<BasicResponse>() {
										@Override
										public void onResponse(Call<BasicResponse> call, retrofit2.Response<BasicResponse> response) {


											if (response.isSuccessful())
											{
												Toast.makeText(getContext(), response.body().message,
														Toast.LENGTH_LONG).show();
												showElement();

											}
											else
											{
												String message = response.body().message;
												Toast.makeText(getContext(), message,
														Toast.LENGTH_LONG).show();
											}

											showElement();
											clearEdit();
											dismiss();

										}

										@Override
										public void onFailure(Call<BasicResponse> call, Throwable t) {

										}
									});
		}
		
		
	}
	
	private String getMessage() {
		return edit_message.getText().toString().trim();
	}
	
	private String getSubject() {
		if(subject != null)
			return subject;
		else
			return edit_sujet.getText().toString().trim();
	}


	
	

}
