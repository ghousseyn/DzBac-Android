package com.squalala.dzbac.ui.add_item;


/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : AddItemView.java
 * Date : 6 août 2014
 * 
 */
public interface AddItemView {

	
	/** 
	 * Cette fonction sert à remplir le tableau filenames des noms des images afin	
	 * de pouvoir les suppprimer après dans la class AlertPhotoManager
	 */
	void populateFilenames(int position, String filename);



	/**
	 *  On montre un message successful
	 */
	void showMessageSuccesFul(String message);

    void updateUIPhotos();

    void hideButtonPhoto();

    void showButtonAddPhoto();

    void hideButtonAddFile();

    void hideButtonAddAudio();

    void showButtonAddAudio();

    void showButtonAddFile();

    void hideButtonAddCamera();

    void showButtonAddCamera();

    void showProgressDialog();

    void hideProgressDialog();

    void setIdItem(String idItem);

    void setFileNameUpload(String filename);

    void setAudioNameUpload(String filename);

    void setIconFile(String iconType);

    void setUrlVideo(String urlVideo);

    /**
     *  On montre un message d'erreur
     */
    void showMessageError(String message);

    void showMessage(String message);
	

	void setTitleError(String message);
	
	void setDescriptionError(String message);

	void setUploadError(int position, String message);

	void hideProgressBar();

    void showProgressBar();
	
	void navigateToUserItems(String message);

}
