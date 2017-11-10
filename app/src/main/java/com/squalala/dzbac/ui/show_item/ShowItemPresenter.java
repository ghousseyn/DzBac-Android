package com.squalala.dzbac.ui.show_item;

import retrofit.mime.TypedFile;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ShowItemPresenter.java
 * Date : 24 juil. 2014
 * 
 */
public interface ShowItemPresenter {

	void onNewIntent(String idItem);

	void getDataItem(String idItem);
	
	void uploadPhotoItem(TypedFile typedFile);

	void signalePost(String idItem);

	void onDestroy();

}
