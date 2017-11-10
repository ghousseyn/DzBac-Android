package com.squalala.dzbac.ui.show_item;

import com.squalala.dzbac.data.api.ApiResponse.Item;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ShowItemListener.java
 * Date : 25 juil. 2014
 * 
 */
public interface ShowItemListener {

	void onErrorLoading();
	
	void onLoadData(Item item);

	void onNoDataLoaded();

	void onUpdateDataImages();
	
	void onPhotoUploaded(int position , String filename);
	
	void onPhotoDeleted(String message);
	
	void onPhotoUploadFailed(int position, String message);
	
	void onInitItem(String idItem);

	void onPostSignaled(String message);

}
