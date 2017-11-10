package com.squalala.dzbac.ui.add_item;

import android.content.Context;
import android.util.Log;

import com.squalala.dzbac.R;
import com.squalala.dzbac.common.listener.OnDeleteStateListener;
import com.squalala.dzbac.common.listener.OnFinishedAddItemListener;
import com.squalala.dzbac.common.listener.OnUploadFileListener;
import com.squalala.dzbac.data.PostAdd;
import com.squalala.dzbac.data.api.ApiResponse.Item;
import com.squalala.dzbac.interactors.ItemInteractor;
import com.squalala.dzbac.ui.show_item.ShowItemListener;

import hugo.weaving.DebugLog;
import okhttp3.RequestBody;


/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : AddItemPresenterImpl.java
 * Date : 6 août 2014
 * 
 */
public class AddItemPresenterImpl 
	implements AddItemPresenter, ShowItemListener, OnFinishedAddItemListener,
        OnUploadFileListener, OnDeleteStateListener {
	
	private ItemInteractor itemInteractor;
	private AddItemView addItemView;

    private boolean isPhotoPreview;
    private RequestBody typedFileVideo;

    private Context context;

    private static final String TAG = AddItemPresenterImpl.class.getSimpleName();
	
	public AddItemPresenterImpl(ItemInteractor interactor, AddItemView view, Context context) {
		itemInteractor = interactor;
		addItemView = view;
        this.context = context;
	}

    @Override
    public void uploadPhotoAndVideo(String idItem, String typeItem, int position, RequestBody typedFile, RequestBody typedFileVideo) {
        addItemView.showProgressDialog();
        addItemView.hideButtonAddFile();
        addItemView.hideButtonPhoto();
        addItemView.hideButtonAddCamera();
        addItemView.hideButtonAddAudio();
        isPhotoPreview = true;

        this.typedFileVideo = typedFileVideo;

        itemInteractor.uploadPhotoItem(idItem, typeItem, typedFile, position, this);
    }

    @Override
    public void onItemAddError(String message) {
        addItemView.hideProgressDialog();
        addItemView.showMessage(message);
    }

    @Override
    public void onErrorLoading() {
        addItemView.hideProgressDialog();
        addItemView.showMessage(context.getString(R.string.error_loading));
    }

    @Override
    public void onPostSignaled(String message) {

    }

    @Override
    public void onUploadVideoFailed(String message) {
        typedFileVideo = null;
        addItemView.hideProgressDialog();
        addItemView.showMessageError(message);
    }

    @Override
    public void onUploadVideoFinished(String fileName, String message) {
        typedFileVideo = null;
        addItemView.hideProgressDialog();
        addItemView.showMessageSuccesFul(message);
        addItemView.setUrlVideo(fileName);
    }

    @Override
    public void onNoDataLoaded() {
        addItemView.hideProgressDialog();
        addItemView.showMessage(context.getString(R.string.error_loading));
    }

    @Override
	public void uploadPhotoItem(String idItem, String typeItem, int position ,
			RequestBody typedFile) {
        Log.e(TAG, "uploadPhotoItem");
        addItemView.hideButtonAddFile();
		itemInteractor.uploadPhotoItem(idItem, typeItem, typedFile, position, this);

	}

    @Override
    public void uploadFile(RequestBody typedFile) {
        Log.e(TAG, "uploadFile");

        addItemView.hideButtonPhoto();
        addItemView.hideButtonAddFile();
        addItemView.hideButtonAddAudio();
        addItemView.hideButtonAddCamera();
        addItemView.showProgressBar();
        itemInteractor.uploadFile(typedFile, this);
    }


    @Override
    public void uploadAudioFile(RequestBody typedFile) {
       //  addItemView.hideButtonPhoto();
        addItemView.hideButtonAddFile();
        addItemView.hideButtonAddAudio();
        addItemView.hideButtonAddCamera();
        addItemView.showProgressBar();
        itemInteractor.uploadAudio(typedFile, this);
    }

    @Override
    public void onDeleteAudio(String message) {
        addItemView.showMessageSuccesFul(message);
        addItemView.showButtonAddAudio();
        addItemView.showButtonAddFile();
        addItemView.showButtonAddPhoto();
        addItemView.showButtonAddCamera();
        addItemView.hideProgressBar();
        addItemView.setAudioNameUpload(null);
    }

    @Override
    public void onDeleteAudioError(String message) {
        addItemView.showMessageSuccesFul(message);
        addItemView.showButtonAddAudio();
        addItemView.showButtonAddFile();
        addItemView.showButtonAddPhoto();
        addItemView.showButtonAddCamera();
        addItemView.hideProgressBar();
        addItemView.setAudioNameUpload(null);
    }

    @Override
    public void onUploadAudioFailed(String message) {
        addItemView.showMessageSuccesFul(message);
        addItemView.showButtonAddAudio();
        addItemView.showButtonAddFile();
        addItemView.showButtonAddPhoto();
        addItemView.showButtonAddCamera();
        addItemView.hideProgressBar();
        addItemView.setAudioNameUpload(null);
    }

    @Override
    public void onUploadAudioFinished(String filename) {
        addItemView.setAudioNameUpload(filename);
        addItemView.showButtonAddPhoto();
        addItemView.hideProgressBar();
        addItemView.showButtonAddAudio();
        addItemView.showMessage(context.getString(R.string.audio_upload));
    }

    @Override
    public void deleteFile(String contentId, String filename) {
        Log.e(TAG, "deleteFile");
        addItemView.hideButtonPhoto();
        addItemView.hideButtonAddFile();
        addItemView.hideButtonAddAudio();
        addItemView.hideButtonAddCamera();
        addItemView.showProgressBar();
        itemInteractor.deleteFile(contentId, filename, this);
    }

    @Override
    public void deleteAudio(String contentId, String filename) {
        addItemView.hideButtonPhoto();
        addItemView.hideButtonAddFile();
        addItemView.hideButtonAddAudio();
        addItemView.hideButtonAddCamera();
        addItemView.showProgressBar();
        itemInteractor.deleteAudio(contentId, filename, this);
    }

    @Override
    public void onDeleteFile(String message) {
        addItemView.showMessage(message);
        addItemView.showButtonAddFile();
        addItemView.showButtonAddPhoto();
        addItemView.showButtonAddAudio();
        addItemView.showButtonAddCamera();
        addItemView.hideProgressBar();
        addItemView.setIconFile("fa-file");
        addItemView.setFileNameUpload(null);
    }

    @Override
    public void onDeleteFileError(String message) {
        addItemView.showMessageError(message);
        addItemView.showButtonAddPhoto();
        addItemView.showButtonAddFile();
        addItemView.showButtonAddAudio();
        addItemView.showButtonAddCamera();
        addItemView.hideProgressBar();
        addItemView.setIconFile("fa-file");
        addItemView.setFileNameUpload(null);
    }

    @Override
    public void onUploadFileFinished(String filename) {
        addItemView.setFileNameUpload(filename);
        addItemView.hideProgressBar();
        addItemView.showMessage(context.getString(R.string.fichier_upload));

        String typeIcon = "";

        int i = filename.lastIndexOf('.');

        if (i >= 0) {

            String extension = filename.substring(i+1).toLowerCase();

            switch (extension) {

                case "pdf":

                    typeIcon = "fa-file-pdf-o";

                    break;

                case "txt":

                    typeIcon = "fa-file-text-o";

                    break;

                case "zip":

                    typeIcon = "fa-file-zip-o";

                    break;

                case "rar":

                    typeIcon = "fa-file-zip-o";

                    break;

                default:

                    typeIcon = "fa-file-text";

                    break;
            }

        }

        addItemView.setIconFile(typeIcon);
        addItemView.showButtonAddFile();
    }

    @Override
    public void onUploadFileError(String message) {
        addItemView.showMessageError(message);
        addItemView.showButtonAddFile();
        addItemView.showButtonAddCamera();
        addItemView.showButtonAddAudio();
        addItemView.showButtonAddPhoto();
        addItemView.hideProgressBar();
        addItemView.setIconFile("fa-file");
        addItemView.setFileNameUpload(null);
    }

    @Override
	public void onPhotoUploaded(int position , String filename) {
        Log.e(TAG, "onPhotoUploaded");
		addItemView.populateFilenames(position, filename);

	}

    @DebugLog
	@Override
	public void deletePhoto(String filename, String idItem, String typeItem) {
        addItemView.showProgressBar();
		itemInteractor.deletePhotoItem(filename, idItem, typeItem, this);
	}

	@Override
	public void onPhotoDeleted(String message) {
		addItemView.showMessageSuccesFul(message);
        addItemView.updateUIPhotos();
        addItemView.hideProgressBar();
	}

	@Override
	public void onTitleError(String message) {
		addItemView.hideProgressBar();
		addItemView.setTitleError(message);
	}

	@Override
	public void onDescriptionError(String message) {
		addItemView.hideProgressBar();
		addItemView.setDescriptionError(message);
	}

	@Override
	public void submitItem(PostAdd item, String idItem,boolean isModification) {	
		itemInteractor.addPost(idItem, item, isModification, this);
	}

	@Override
	public void onItemAdded(String message) {
		addItemView.hideProgressBar();
		addItemView.navigateToUserItems(message);
	}

	@Override
	public void onLoadData(Item item) {}

	@Override
	public void updateDataImages(String idItem, String typeItem,
			String urlImagePresentation) {
        Log.e(TAG, "updateDataImages");
		itemInteractor.updateDataPhoto(idItem, typeItem, urlImagePresentation, this);
	}

    @Override
    public void onUpdateDataImages() {
        if (isPhotoPreview) {
            isPhotoPreview = false;
            itemInteractor.uploadVideo(typedFileVideo, this);
        }
    }

    @Override
	public void onPhotoUploadFailed(int position, String message) {
        Log.e(TAG, "onPhotoUploadFailed");
		addItemView.setUploadError(position, message);
		addItemView.hideProgressBar();
	}

	@Override
	public void initItem() {
        addItemView.showProgressBar();
		itemInteractor.initItem(this);
	}

	@Override
	public void onInitItem(String idItem) {
		addItemView.setIdItem(idItem);
		addItemView.hideProgressBar();
	}


}
