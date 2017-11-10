package com.squalala.dzbac.ui.about;

import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.interactors.UserInteractor;
import com.squalala.dzbac.ui.show_profile.ShowProfileListener;

/**
 * Created by Back Packer
 * Date : 15/04/15
 */
public class AboutPresenterImpl implements AboutPresenter, ShowProfileListener {

    private AboutView view;
    private UserInteractor interactor;

    public AboutPresenterImpl(AboutView view, UserInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void onUpdatePhoneUser(String idMembre, String message, boolean isBanned, boolean isPhoneAlreadyUsed) {

    }

    @Override
    public void onResume(String membreId) {
        interactor.getDataUser(membreId, this);
    }

    @Override
    public void onLoadData(ApiResponse.User user) {
        view.setUserDetails(user);
    }

    @Override
    public void onUpdateDataUser(String idMembre, String message) {

    }

    @Override
    public void onAvatarUploaded(String urlAvatar) {

    }

    @Override
    public void onUserFollowed(String message) {

    }

    @Override
    public void onUserUnFollowed(String message) {

    }

    @Override
    public void onBackgroundUploaded(String urlBackground) {

    }

    @Override
    public void onNameUserError() {

    }
}
