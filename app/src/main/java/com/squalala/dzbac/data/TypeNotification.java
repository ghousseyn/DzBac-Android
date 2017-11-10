package com.squalala.dzbac.data;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : TypeNotification.java
 * Date : 2 sept. 2014
 * 
 */
public enum TypeNotification {
	COMMENTED("commented");
	
	private String typeNotification = "";
	
	TypeNotification(String typeTransport) {
		this.typeNotification = typeTransport;
	}
	
	public String toString() {
		return typeNotification;
	}

}
