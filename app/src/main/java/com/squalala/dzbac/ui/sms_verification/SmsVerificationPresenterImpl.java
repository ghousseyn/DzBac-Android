package com.squalala.dzbac.ui.sms_verification;

import com.squalala.dzbac.data.UserInformations;
import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.interactors.UserInteractor;
import com.squalala.dzbac.ui.show_profile.ShowProfileListener;

import hugo.weaving.DebugLog;

/**
 * Created by Back Packer
 * Date : 04/12/15
 */
public class SmsVerificationPresenterImpl
        implements SmsVerificationPresenter, ShowProfileListener {

    private SmsVerificationView view;
    private UserInteractor interactor;
    private MainPreferences mainPreferences;

    private String phoneNumber;

    public SmsVerificationPresenterImpl(SmsVerificationView view, UserInteractor interactor,
                                        MainPreferences mainPreferences) {
        this.view = view;
        this.interactor = interactor;
        this.mainPreferences = mainPreferences;
    }

    @DebugLog
    @Override
    public void onSuccess(String phoneNumber, String digitsId) {
        this.phoneNumber = phoneNumber;

        view.hideButton();
        view.showProgress();

        UserInformations userInfo = new UserInformations();
        userInfo.setPhone(phoneNumber);
        userInfo.setDigits_id(digitsId);

        interactor.updatePhoneUser(userInfo, this);
    }

    @Override
    public void onLoadData(ApiResponse.User user) {}

    @Override
    public void onUserFollowed(String message) {}

    @Override
    public void onUserUnFollowed(String message) {}


    @DebugLog
    @Override
    public void onUpdateDataUser(String idMembre, String message) {}

    @Override
    public void onUpdatePhoneUser(String idMembre, String message, boolean isBanned, boolean isPhoneAlreadyUsed) {

        view.hideProgress();

        if (isBanned) {
            mainPreferences.setBanned(true);
            mainPreferences.deconnexion();
            view.navigateToSplash();
        }
        else if (isPhoneAlreadyUsed) {
            view.showDialog(message);
            view.showButton();
        }
        else {
            mainPreferences.setPhoneNumber(phoneNumber);
            view.exit();
        }



    }

    @Override
    public void onAvatarUploaded(String urlAvatar) {}

    @Override
    public void onBackgroundUploaded(String urlBackground) {}

    @Override
    public void onNameUserError() {}
}
