package com.squalala.dzbac.ui.show_profile;

import com.squalala.dzbac.common.listener.MessagingListener;
import com.squalala.dzbac.data.UserInformations;
import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.data.api.ApiResponse.User;
import com.squalala.dzbac.interactors.AdminInteractor;
import com.squalala.dzbac.interactors.MessagingInteractor;
import com.squalala.dzbac.interactors.UserInteractor;

import okhttp3.RequestBody;


/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ShowProfilePresenterImpl.java
 * Date : 31 juil. 2014
 *
 */
public class ShowProfilePresenterImpl implements ShowProfilePresenter, ShowProfileListener,
	OnUserBannedListener, MessagingListener {

	private ShowProfileView showProfileView;
	private UserInteractor showProfileInteractor;
	private AdminInteractor adminInteractor;
	private MessagingInteractor messagingInteractor;

	public ShowProfilePresenterImpl(ShowProfileView showProfileView,
									UserInteractor showProfileInteractor,
									AdminInteractor adminInteractor,
									MessagingInteractor messagingInteractor) {
		this.showProfileView = showProfileView;
		this.showProfileInteractor = showProfileInteractor;
		this.adminInteractor = adminInteractor;
		this.messagingInteractor = messagingInteractor;
	}

	@Override
	public void onUpdatePhoneUser(String idMembre, String message, boolean isBanned, boolean isPhoneAlreadyUsed) {

	}

	@Override
	public void onConversationNotFound() {

	}

	@Override
	public void onFollowUser(String idMembre) {
		showProfileInteractor.followUser(idMembre, this);
	}

	@Override
	public void onUserFollowed(String message) {
		showProfileView.showCrouton(message);
		showProfileView.updateFollow(true);
	}

	@Override
	public void onUserUnFollowed(String message) {
		showProfileView.showCrouton(message);
		showProfileView.updateFollow(false);
	}

	@Override
	public void onUploadAudioFile(String filename) {

	}

	@Override
	public void onUploadAudioFileError(String messageError) {

	}

	@Override
	public void onAudioMessageSended(String message) {

	}

	@Override
	public void onUnFollowUser(String idMembre) {
		showProfileInteractor.unFollowUser(idMembre, this);
	}

	@Override
	public void onUserBanned(String message) {
		showProfileView.showCrouton(message);
	}

	@Override
	public void OnUserBlocked(String message) {
		showProfileView.showCrouton(message);
	}

	@Override
	public void blockUser(String idMembre) {
		messagingInteractor.blockUser(idMembre, this);
	}

	@Override
	public void bannUser(String idMembre) {
		adminInteractor.bannUser(idMembre, this);
	}

	@Override
	public void onLoadData(User user) {
		showProfileView.displayDataUser(user);
	}

	@Override
	public void getDataUser(String idMembre) {
		showProfileInteractor.getDataUser(idMembre, this);
	}

	@Override
	public void updateDataUser(UserInformations userInformations) {
		showProfileInteractor.updateDataUser(userInformations, this);
	}

	@Override
	public void onUpdateDataUser(String idMembre, String message) {
		showProfileView.showCrouton(message);
		showProfileView.navigateToHome();
	}

	@Override
	public void updateAvatarUser(RequestBody typedFile) {
		showProfileInteractor.updateAvatarUser(typedFile, this);
	}

	@Override
	public void updateBackgroundUser(RequestBody typedFile) {
		showProfileInteractor.updateBackgroundUser(typedFile, this);
	}

	@Override
	public void onAvatarUploaded(String urlAvatar) {
		showProfileView.finishUploadAvatar(urlAvatar);
	}

	@Override
	public void onBackgroundUploaded(String urlBackground) {
		showProfileView.finishUploadBackground(urlBackground);
	}

	@Override
	public void onNameUserError() {
		showProfileView.setErrorUserName();
	}



	@Override
	public void onDeleteConversation(String message) {
	}

	@Override
	public void onDeleteConversationError(String message) {
	}

	@Override
	public void onLoadListConversation(ApiResponse.ListConversationPosts arg0, int page) {

	}

	@Override
	public void onLoadConversation(ApiResponse.Messages arg0) {

	}

	@Override
	public void onMessageSended(String id_header) {
	}

}
