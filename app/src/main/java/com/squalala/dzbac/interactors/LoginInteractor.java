package com.squalala.dzbac.interactors;

import com.squalala.dzbac.google.GoogleManagerListener;
import com.squalala.dzbac.ui.login.OnLoginFinishedListener;

public interface LoginInteractor {
	
    public void login(String username, String password, OnLoginFinishedListener listener);
    
    public void loginByGoogle(String email, String idGoogle, GoogleManagerListener listener);
    
   // public void loginByFacebook()
}
