package com.squalala.dzbac.ui.sms_verification;

/**
 * Created by Back Packer
 * Date : 04/12/15
 */
public interface SmsVerificationView {

    void exit();

    void navigateToSplash();

    void showDialog(String message);

    void showProgress();

    void hideProgress();

    void hideButton();

    void showButton();
}
