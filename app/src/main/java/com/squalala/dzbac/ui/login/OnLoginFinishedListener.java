package com.squalala.dzbac.ui.login;

import com.squalala.dzbac.data.api.ApiResponse.Login;

public interface OnLoginFinishedListener {

    void onEmailError();
    
    void onEmailInvalidError();

    void onPasswordError();
    
    void onLoginError();

    void onLogin(Login login, String email, String password);

    void onServerError();
}
