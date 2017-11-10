package com.squalala.dzbac.ui.login;

public interface LoginView {

    void showMessage(String message);

    void showProgress();

    void hideProgress();

    void setEmailError();
    
    void setEmailInvalidError();

    void setPasswordError();
    
    void setLoginError();

    void setBannedError();

    void navigateToMainScreen();
}
