package com.squalala.dzbac.data;

import com.google.gson.annotations.SerializedName;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : User.java
 * Date : 4 août 2014
 * 
 */
public class UserInformations {
	
	@SerializedName("username")
	private String userName;
	@SerializedName("localisation")
	private String localisation;
	@SerializedName("about")
	private String apropos;

	@SerializedName("phone")
	private String phone;

	@SerializedName("digits_id")
	private String digits_id;


	public String getDigits_id() {
		return digits_id;
	}

	public void setDigits_id(String digits_id) {
		this.digits_id = digits_id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}



	/**
	 * @return the nom
	 */
	public String getNom() {
		return userName;
	}
	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.userName = nom;
	}
	/**
	 * @return the localisation
	 */
	public String getLocalisation() {
		return localisation;
	}
	/**
	 * @param localisation the localisation to set
	 */
	public void setLocalisation(String localisation) {
		this.localisation = localisation;
	}
	/**
	 * @return the apropos
	 */
	public String getApropos() {
		return apropos;
	}
	/**
	 * @param apropos the apropos to set
	 */
	public void setApropos(String apropos) {
		this.apropos = apropos;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
