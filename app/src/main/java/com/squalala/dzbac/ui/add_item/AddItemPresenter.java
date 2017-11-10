package com.squalala.dzbac.ui.add_item;

import com.squalala.dzbac.data.PostAdd;

import okhttp3.RequestBody;


/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : AddItemPresenter.java
 * Date : 6 août 2014
 * 
 */
public interface AddItemPresenter {


	void initItem();

	void uploadAudioFile(RequestBody typedFile);

    void uploadFile(RequestBody typedFile);

    void deleteFile(String contentId, String filename);

	void deleteAudio(String contentId, String filename);

    void uploadPhotoAndVideo(String idItem, String typeItem, int position,
                                 RequestBody typedFile, RequestBody typedFileVideo);
	
	void uploadPhotoItem(String idItem, String typeItem, int position,
			RequestBody typedFile);
	
	void deletePhoto(String filename, String idItem, String typeItem);
	
	void updateDataImages(String idItem, String typeItem, String urlImagePresentation);
	
	void submitItem(PostAdd item, String idItem,boolean isModification);
	
}
