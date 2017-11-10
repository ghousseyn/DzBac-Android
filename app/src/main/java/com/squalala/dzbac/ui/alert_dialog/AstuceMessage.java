package com.squalala.dzbac.ui.alert_dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.squalala.dzbac.R;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : AstuceAddItemAlert.java
 * Date : 9 août 2014
 * 
 */
public class AstuceMessage {


	public static AlertDialog message(Context context, String title, String message) {
		 AlertDialog.Builder builder = new AlertDialog.Builder(context);
		 builder.setTitle(title);
		 builder.setMessage(message);
		 
		 builder.setNeutralButton(context.getString(R.string.ok),
		            new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int id) {
		                    dialog.cancel();
		                }
		            });
		 
		 AlertDialog alertDialog = builder.create(); 
		    
		 return alertDialog;
	}

}
