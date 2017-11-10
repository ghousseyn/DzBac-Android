package com.squalala.dzbac.interactors;

import com.squalala.dzbac.common.listener.OnDeleteStateListener;
import com.squalala.dzbac.common.listener.OnFinishedAddItemListener;
import com.squalala.dzbac.common.listener.OnUploadFileListener;
import com.squalala.dzbac.data.PostAdd;
import com.squalala.dzbac.ui.show_item.ShowItemListener;

import okhttp3.RequestBody;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ShowItemInteractor.java
 * Date : 24 juil. 2014
 * 
 */
public interface ItemInteractor {

	void signalePost(String idContent, ShowItemListener mListener);
	
	void initItem(ShowItemListener listener);
	
	void getData(String idItem , ShowItemListener showItemListener);

 //   void downloadFile(String filename//);

    void deleteFile(String contentId, String filename, OnDeleteStateListener listener);

	void deleteAudio(String contentId, String filename, OnDeleteStateListener listener);

    void uploadFile(RequestBody typedFile, OnUploadFileListener listener);

	void uploadAudio(RequestBody typedFile, OnUploadFileListener listener);

	void uploadVideo(RequestBody file, OnUploadFileListener listener);
	
	void uploadPhotoItem(String idItem, String typeItem,
			RequestBody typedFile, int position , ShowItemListener listener);
	
	void deletePhotoItem(String filename, String idItem, String typeItem,
			ShowItemListener listener);
	
	void updateDataPhoto(String idItem, String typeItem, String urlImagePresentation, ShowItemListener mListener);
	
	void addPost(String idItem, PostAdd itemAdd, boolean isModification, OnFinishedAddItemListener listener);

	void destroyCaller();
	
}
