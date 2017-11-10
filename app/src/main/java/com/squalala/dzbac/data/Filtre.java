package com.squalala.dzbac.data;

import com.google.gson.annotations.SerializedName;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : Filtre.java
 * Date : 9 juil. 2014
 * 
 */
public class Filtre {
	
	@SerializedName("categorie")
	private String categorie;
	@SerializedName("sous_categorie")
	private String sous_categorie;
	@SerializedName("mot_cle")
	private String mot_cle;
	@SerializedName("type_ordre")
	private String type_ordre;
	

	@SerializedName("latitude")
	private String latitude;
	@SerializedName("longitude")
	private String longitude;
	
	/**
	 * @return the categorie
	 */
	public String getCategorie() {
		return categorie;
	}
	/**
	 * @param categorie the categorie to set
	 */
	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}
	/**
	 * @return the sous_categorie
	 */
	public String getSous_categorie() {
		return sous_categorie;
	}
	/**
	 * @param sous_categorie the sous_categorie to set
	 */
	public void setSousCategorie(String sous_categorie) {
		this.sous_categorie = sous_categorie;
	}

	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	/**
	 * @return the mot_cle
	 */
	public String getMotCle() {
		return mot_cle;
	}
	/**
	 * @param mot_cle the mot_cle to set
	 */
	public void setMotCle(String mot_cle) {
		this.mot_cle = mot_cle;
	}
	/**
	 * @return the type_ordre
	 */
	public String getTypeOrdre() {
		return type_ordre;
	}
	/**
	 * @param type_ordre the type_ordre to set
	 */
	public void setTypeOrdre(String type_ordre) {
		this.type_ordre = type_ordre;
	}

	
	/**
	 * @param type_annonce the type_annonce to set
	 */
	public void setTypeAnnonce(String type_annonce) {
	/*	if (type_annonce.equals(TypeAnnonce.OFFRE.toString()))
			this.type_annonce = 0;
		else if (type_annonce.equals(TypeAnnonce.DEMANDE.toString()))
			this.type_annonce = 1;
		else
			this.type_annonce = 2;*/
	}
	
	/**
	 * @param type_service the type_annonce to set
	 */
	public void setTypeService(String type_service) {
	/*	if (type_service.equals(TypeService.PROPOSE.toString()))
			this.type_service = 0;
		else if (type_service.equals(TypeService.CHERCHE.toString()))
			this.type_service = 1;
		else
			this.type_service = 2;*/
	}
	

}
