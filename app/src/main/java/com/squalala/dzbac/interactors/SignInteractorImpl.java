package com.squalala.dzbac.interactors;

import android.text.TextUtils;

import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.data.api.ApiResponse.BasicResponse;
import com.squalala.dzbac.data.api.CheckService;
import com.squalala.dzbac.data.api.GoogleService;
import com.squalala.dzbac.data.api.RegisterService;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.google.GoogleManagerListener;
import com.squalala.dzbac.ui.sign.OnSignListener;
import com.squalala.dzbac.utils.Check;
import com.squalala.dzbac.utils.Sha1;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : SignInteractorImpl.java
 * Date : 22 juin 2014
 * 
 */
@Singleton
public class SignInteractorImpl implements SignInteractor {
	
	private CheckService checkService;
	private boolean error;
	private RegisterService registerService;
	private GoogleService googleService;
	private MainPreferences mainPreferences;
	
	@Inject
	public SignInteractorImpl(CheckService checkService, RegisterService registerService ,
			GoogleService googleService, MainPreferences mainPreferences) {
		this.checkService = checkService;
		this.registerService = registerService;
		this.googleService = googleService;
		this.mainPreferences = mainPreferences;
	}

	@Override
	public void register(final String pseudo, final String password, String confirme_password, 
			final String email,
			final OnSignListener listener) {
		
		error = false;
		
		if (TextUtils.isEmpty(pseudo)) {
			listener.onPseudoError();
			error = true;
		}
		
		if (TextUtils.isEmpty(email)) {
			listener.onEmailError();
			error = true;
		}
		
		if (!Check.isValidEmail(email)) {
			listener.onEmailInvalid();
			error = true;
		}
		
		if (TextUtils.isEmpty(password)) {
			listener.onPasswordError();
			error = true;
		}
		/*
		if (!(password.equals(confirme_password))) {
			listener.onPasswordDifferentError();
			error = true;
		}
		*/
		if (Check.isValidEmail(email))
		{
			checkService.checkEmail(email).enqueue(new retrofit2.Callback<BasicResponse>() {
				@Override
				public void onResponse(Call<BasicResponse> call, retrofit2.Response<BasicResponse> response) {
					if (response.body() != null) {
						error = true;
						listener.onEmailNotAvailaible(response.body().error);
					}
				}

				@Override
				public void onFailure(Call<BasicResponse> call, Throwable t) {

				}
			});
		}
		
	/*	if (pseudo.length() >= 3)
		{
			checkService.checkPseudo(Check.pseudoValidate(pseudo), new Callback<BasicResponse>() {

				@Override
				public void failure(RetrofitError arg0) {
					error = true;
					listener.onPseudoNotAvailaibleError(((BasicResponse) arg0.getBodyAs(BasicResponse.class)).error);
				}

				@Override
				public void success(BasicResponse arg0, Response arg1) {}
			});
		}
		
	*/	
		
		if (!error) {
			registerService.register(pseudo, Sha1.encrypt(password), email, mainPreferences.getGCMId())
					.enqueue(new retrofit2.Callback<BasicResponse>() {
						@Override
						public void onResponse(Call<BasicResponse> call, retrofit2.Response<BasicResponse> response) {

							if (response.body() != null && response.body().error == null) {
								mainPreferences.setUserName(pseudo);
								listener.onRegistred(response.body().id_membre, pseudo, email, Sha1.encrypt(password));
							}
							else
								listener.onRegisterError(response.body().error);
						}

						@Override
						public void onFailure(Call<BasicResponse> call, Throwable t) {
							listener.onRegisterError("Le serveur est actuellement indisponible réessayer plus tard :D");
						}
					});

		}
		
	}

	@Override
	public void afterEmailChanged(CharSequence email,final OnSignListener listener) {
		
		if (Check.isValidEmail(email.toString()))
		{
			checkService.checkEmail(email.toString())
					.enqueue(new retrofit2.Callback<BasicResponse>() {
						@Override
						public void onResponse(Call<BasicResponse> call, retrofit2.Response<BasicResponse> response) {
							if (response.body() != null)
								listener.onEmailNotAvailaible(response.body().error);
						}

						@Override
						public void onFailure(Call<BasicResponse> call, Throwable t) {

						}
					});
		}
	}

	@Override
	public void onPseudoChanged(CharSequence pseudo, int count, final OnSignListener listener) {
		
		if (count >= 3)
		{
			checkService.checkPseudo(Check.pseudoValidate(pseudo.toString()))
					.enqueue(new retrofit2.Callback<BasicResponse>() {
						@Override
						public void onResponse(Call<BasicResponse> call, retrofit2.Response<BasicResponse> response) {
							if (response.body() != null)
								listener.onPseudoNotAvailaibleError(response.body().error);
						}

						@Override
						public void onFailure(Call<BasicResponse> call, Throwable t) {

						}
					});
		}
		
	}

	@Override
	public void registerByGoogle(final String pseudo,final String email,
			final String idGoogle,final String urlAvatar,final GoogleManagerListener listener) {

		boolean error = false;

		if (idGoogle == null || email == null || urlAvatar == null
				|| pseudo == null)
			error = true;

		if (!error) {

			googleService.registerGoogle(pseudo, idGoogle, email, urlAvatar, mainPreferences.getGCMId())
					.enqueue(new retrofit2.Callback<BasicResponse>() {
						@Override
						public void onResponse(Call<BasicResponse> call, retrofit2.Response<BasicResponse> response) {
							if (response.body() != null && response.body().error == null) {
								mainPreferences.setUserName(pseudo);
								listener.onRegister(response.body().id_membre, pseudo, email, idGoogle);
							}
							else {
								listener.onFailRegisterOrLogin(response.body().error);
							}
						}

						@Override
						public void onFailure(Call<BasicResponse> call, Throwable t) {
							listener.onFailRegisterOrLogin("Le serveur est actuellement indisponible réessayer plus tard :D");
						}
					});
		}
		else
		{
			// TODO à vérifier si l'erreur persiste
			listener.onFailRegisterOrLogin("Une erreur s'est produite, réessayez avec l'inscription par email");
		}
		

		
	}

}
