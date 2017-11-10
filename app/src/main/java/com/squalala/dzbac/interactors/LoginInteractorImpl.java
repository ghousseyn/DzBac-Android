package com.squalala.dzbac.interactors;

import android.text.TextUtils;

import com.squalala.dzbac.data.api.ApiResponse.Login;
import com.squalala.dzbac.data.api.GoogleService;
import com.squalala.dzbac.data.api.LoginService;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.google.GoogleManagerListener;
import com.squalala.dzbac.ui.login.OnLoginFinishedListener;
import com.squalala.dzbac.utils.Check;

import javax.inject.Inject;
import javax.inject.Singleton;
import retrofit2.Call;

@Singleton
public class LoginInteractorImpl implements LoginInteractor {
	
	private boolean error;
	private LoginService loginService;
	private GoogleService googleService;
	private MainPreferences mainPreferences;
	
	@Inject
	public LoginInteractorImpl(LoginService loginService, GoogleService googleService,
			MainPreferences mainPreferences) {
		this.loginService = loginService;
		this.googleService = googleService;
		this.mainPreferences = mainPreferences;
	}

    @Override
    public void login(final String email, final String password, final OnLoginFinishedListener listener) {
    	
                error = false;
                
                if (TextUtils.isEmpty(email)){
                    listener.onEmailError();
                    error = true;
                }
                if (!Check.isValidEmail(email)){
                    listener.onEmailInvalidError();
                    error = true;
                }
                if (TextUtils.isEmpty(password)){
                    listener.onPasswordError();
                    error = true;
                }
                
                if (!error) 
                {
					loginService.login(email, password, mainPreferences.getGCMId())
							.enqueue(new retrofit2.Callback<Login>() {
								@Override
								public void onResponse(Call<Login> call, retrofit2.Response<Login> response) {
									if (response.body() != null && response.body().error == null)
									{
										mainPreferences.setUrlBackground(response.body().url_background);
										mainPreferences.setUrlPhoto(response.body().url_avatar);
										mainPreferences.setUserName(response.body().pseudo);
										mainPreferences.setLevelUser(response.body().level);

										listener.onLogin(response.body(), email, password);
									}
									else
									{
										listener.onLoginError();
										error = true;
									}
								}

								@Override
								public void onFailure(Call<Login> call, Throwable t) {
									listener.onServerError();
								}
							});
                }
                
                
    }
    

	@Override
	public void loginByGoogle(final String email, final String idGoogle,
			final GoogleManagerListener listener) {
		
		googleService.loginGoogle(idGoogle, mainPreferences.getGCMId())
				.enqueue(new retrofit2.Callback<Login>() {
					@Override
					public void onResponse(Call<Login> call, retrofit2.Response<Login> response) {

						if (response.body() != null && response.body().error == null)
						{
							mainPreferences.setUrlBackground(response.body().url_background);
							mainPreferences.setUrlPhoto(response.body().url_avatar);
							mainPreferences.setUserName(response.body().pseudo);

							listener.onLogin(response.body(), email, idGoogle);
						}
						else
							listener.onFailRegisterOrLogin(response.body().error);
					}

					@Override
					public void onFailure(Call<Login> call, Throwable t) {
						listener.onServerError();
					}
				});
	}

}
