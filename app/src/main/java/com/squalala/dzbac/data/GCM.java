package com.squalala.dzbac.data;

import com.google.gson.annotations.SerializedName;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : GCM.java
 * Date : 26 nov. 2014
 * 
 */
public class GCM {

	@SerializedName("gcm_id")
	private String gcmId;

	public String getGcmId() {
		return gcmId;
	}

	public void setGcmId(String gcmId) {
		this.gcmId = gcmId;
	}
	
	
}
