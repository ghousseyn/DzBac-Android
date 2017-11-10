package com.squalala.dzbac.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.squalala.dzbac.R;
import com.squalala.dzbac.data.api.ApiResponse.Item;
import com.squalala.dzbac.data.api.SignalisationService;
import com.squalala.dzbac.data.prefs.MainPreferences;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : SignaleDialog.java
 * Date : 28 juil. 2014
 * 
 */
public class SignaleDialog extends Dialog implements OnClickListener {
	
	@InjectView(R.id.spinner_signalisation_dialog) Spinner spin_signalisation;
	@InjectView(R.id.progressBar_signalisation_dialog) ProgressBar progressBar;
	@InjectView(R.id.btn_send_signalisation_dialog) BootstrapButton btn_send_signalisation;
	
	private SignalisationService signalisationService;
	private MainPreferences mainPreferences;
	private Item item;
	private String id_item;
	
	public SignaleDialog(Context context,
			SignalisationService signalisationService, 
			MainPreferences mainPreferences) {
		super(context);
		this.signalisationService = signalisationService;
		this.mainPreferences = mainPreferences;
	}
	

	public void setItem(Item item) {
		this.item = item;
	}
	
	public void setIdItem(String id_item) {
		this.id_item = id_item;
	}
	
	private void hideElement() {
		spin_signalisation.setVisibility(View.GONE);
		btn_send_signalisation.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		setCancelable(false);
	}
	
	private void showElement() {
		spin_signalisation.setVisibility(View.VISIBLE);
		btn_send_signalisation.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);
		setCancelable(true);
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_signalisation);
		
		setTitle(getContext().getString(R.string.motif_signalisation));
		
		ButterKnife.inject(this);
		
		btn_send_signalisation.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		
		String motif = spin_signalisation.getSelectedItem().toString();
		hideElement();

	/*	signalisationService.signale(mainPreferences.getIdUser(),
				id_item,
				item.id_membre,
				motif, 
				authRequest.getSignature(),
				new Callback<ApiResponse.BasicResponse>() {
			
					@Override
					public void success(BasicResponse arg0, Response arg1) {
						Toast.makeText(getContext(), arg0.message,
								Toast.LENGTH_LONG).show();
						showElement();
						dismiss();
					}
					
					@Override
					public void failure(RetrofitError arg0) {
						Log.e("FAIL", arg0.toString());
						showElement();
					}
				});*/
	}

}
