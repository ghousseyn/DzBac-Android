package com.squalala.dzbac.ui.show_item;

import com.squalala.dzbac.data.api.ApiResponse.Item;
import com.squalala.dzbac.interactors.ItemInteractor;

import retrofit.mime.TypedFile;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ShowItemPresenterImpl.java
 * Date : 24 juil. 2014
 * 
 */
public class ShowItemPresenterImpl implements ShowItemPresenter, ShowItemListener {
	
	private ShowItemView showItemView;
	private ItemInteractor showItemInteractor;
	
	public ShowItemPresenterImpl(ShowItemView showItemView, ItemInteractor showItemInteractor) {
		this.showItemInteractor = showItemInteractor;
		this.showItemView = showItemView;
	}

	@Override
	public void onNoDataLoaded() {
		showItemView.exit();
	}

	@Override
	public void onNewIntent(String idItem) {
		showItemView.cleanView();
		showItemInteractor.getData(idItem, this);
	}

	@Override
	public void onErrorLoading() {
		showItemView.showErrorLoading();
		showItemView.exit();
	}

	@Override
	public void signalePost(String idItem) {
		showItemInteractor.signalePost(idItem, this);
	}


	@Override
	public void onPostSignaled(String message) {
		showItemView.showMessage(message);
	}

	@Override
	public void onUpdateDataImages() {

	}

	@Override
	public void onLoadData(Item item) {
		showItemView.displayItem(item);
	}

	@Override
	public void getDataItem(String idItem) {
		showItemInteractor.getData(idItem, this);
	}

	@Override
	public void onPhotoUploaded(int position, String filename) {}

	@Override
	public void onPhotoDeleted(String message) {}

	@Override
	public void onPhotoUploadFailed(int position, String message) {}

	@Override
	public void uploadPhotoItem(TypedFile typedFile) {}

	@Override
	public void onInitItem(String idItem) {}


	@Override
	public void onDestroy() {
		showItemInteractor.destroyCaller();
	}
}
