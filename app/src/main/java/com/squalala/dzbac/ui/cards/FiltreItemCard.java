package com.squalala.dzbac.ui.cards;

import java.util.List;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : FiltreItemCard.java
 * Date : 24 juil. 2014
 * 
 */
public interface FiltreItemCard {

	void onSelectedTags(List<String> tags);
	
	public void initComposants();
	
	public void saveParams(String mot_cle);
	
	public void setDefaultParams();

}
