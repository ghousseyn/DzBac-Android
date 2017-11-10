package com.squalala.dzbac.ui.splash;

import android.app.Activity;
import android.app.Dialog;
import android.app.backup.BackupManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.squalala.dzbac.ActivityModule;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.R;
import com.squalala.dzbac.common.DjihtiBaseActivity;
import com.squalala.dzbac.data.GCM;
import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.data.api.ApiResponse.BasicResponse;
import com.squalala.dzbac.data.api.FacebookService;
import com.squalala.dzbac.data.api.UserService;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.ui.dialogs.LoginChoiceDialog;
import com.squalala.dzbac.ui.dialogs.SignChoiceDialog;
import com.squalala.dzbac.ui.edit_profile.EditProfileActivity;
import com.squalala.dzbac.ui.main.MainActivity;
import com.squalala.dzbac.utils.GradientBackgroundPainter;
import com.squalala.dzbac.utils.Sha1;
import com.squalala.dzbac.utils.SystemUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hugo.weaving.DebugLog;
import retrofit2.Call;

import static com.squalala.dzbac.DjihtiConstant.SENDER_ID;
import static com.squalala.dzbac.utils.GCMUtils.getRegistrationId;
import static com.squalala.dzbac.utils.GCMUtils.storeRegistrationId;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : SplashActivity.java
 * Date : 20 juin 2014
 * 
 */
public class SplashActivity extends DjihtiBaseActivity 
	implements SplashView, OnClickListener, OnLoginChoiceListener {
	
	@Inject SplashPresenter splashPresenter;

	@Inject
	MainPreferences session;
	@Inject SignChoiceDialog signChoiceDialog;
	@Inject LoginChoiceDialog loginChoiceDialog;
	@Inject UserService userService;
	@Inject
	FacebookService facebookService;

    @InjectView(R.id.txt_intro)
    TextView txtIntro;

    @InjectView(R.id.txt_presentation_dzbac)
    TextView txtPresentation;

	@InjectView(R.id.btn_connection) Button btn_connection;
	@InjectView(R.id.btn_inscription) Button btn_inscription;

	private GoogleCloudMessaging gcm;
	private Context context;
    private String regid = "";
    private static final String TAG = "SplashActivity";
    private static final int MAX_ATTEMPS = 5;
    private int numTest = 0;
    private Activity activity = this;

	@InjectView(R.id.relativeSplash)
	RelativeLayout background;

	private String id, name, picture, email;

	CallbackManager callbackManager;

	private boolean isRegisterFacebook;

    private GradientBackgroundPainter gradientBackgroundPainter;

	@DebugLog
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

        requestBackup();

		getSupportActionBar().hide();

	//	SystemUtils.cleanDzBacFolder();
		SystemUtils.checkMe();

		callbackManager = CallbackManager.Factory.create();
		LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG, "onSuccess");

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

							@DebugLog
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                try {
                                    id = response.getJSONObject().get("id").toString();
                                    if (!response.getJSONObject().isNull("email"))
                                        email = response.getJSONObject().get("email").toString();
                                    else
                                        email = "none";

                                    name = response.getJSONObject().get("name").toString();
                                    picture = "https://graph.facebook.com/" + id + "/picture?type=large";

                                    if (isRegisterFacebook)
                                        registerFacebook();
                                    else
                                        loginFacebook();

                                    Log.e(TAG, "email : " + email + " " + id + " " + name + " ");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "onCancel");
            }

            @Override
            public void onError(FacebookException e) {
                Log.e(TAG, "onError");
            }
        });
		
		ButterKnife.inject(this);



        final int[] drawables = new int[4];
        drawables[0] = R.drawable.gradient_1;
        drawables[1] = R.drawable.gradient_2;
        drawables[2] = R.drawable.gradient_3;
        drawables[3] = R.drawable.gradient_4;

        gradientBackgroundPainter = new GradientBackgroundPainter(background, drawables);
        gradientBackgroundPainter.start();

		//TransitionDrawable trans = (TransitionDrawable) background.getBackground();
		//trans.startTransition(2000);

		btn_connection.setOnClickListener(this);
		btn_inscription.setOnClickListener(this);

        context = getApplicationContext();

		 //TODO à enlever
	//	btn_connection.setVisibility(View.VISIBLE);
	//	btn_inscription.setVisibility(View.VISIBLE);

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/comfortaa.ttf");
        txtIntro.setTypeface(type);

        if (session.isBanned())
            txtPresentation.setText(getString(R.string.ban_user_details));

	}

    public void requestBackup() {
        BackupManager bm = new BackupManager(this);
        bm.dataChanged();
    }

	@Override
	public void onChooseLoginFacebook() {
		isRegisterFacebook = false;
		LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
	}

	@Override
	public void onChooseSignUpFacebook() {
		isRegisterFacebook = true;
		LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
	}

	private void loginFacebook() {

		facebookService.loginFacebook(
				id,
				session.getGCMId()).enqueue(new retrofit2.Callback<ApiResponse.Login>() {
			@Override
			public void onResponse(Call<ApiResponse.Login> call, retrofit2.Response<ApiResponse.Login> response) {

				ApiResponse.Login login = response.body();

				loginChoiceDialog.cancel();

				if (login != null && login.error == null) {
					Toast.makeText(activity, activity.getString(R.string.connexion_reussi),
							Toast.LENGTH_LONG).show();

					session.setApiKey(Sha1.encrypt(email +
							id));
					session.setUserName(login.pseudo);
					session.setIdUser(login.id_membre);
					session.setEmailUser(email);
					session.setUrlBackground(login.url_background);
					session.setUrlPhoto(login.url_avatar);

					navigateToHome();
				} else {
					showMessage(login.error);
				}
			}

			@Override
			public void onFailure(Call<ApiResponse.Login> call, Throwable t) {
				loginChoiceDialog.cancel();
				showMessage(activity.getString(R.string.error_serveur));
			}
		});
	}

	private void showMessage(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	private void registerFacebook() {

		facebookService.registerFacebook(
                name,
                id,
                email,
                picture,
                session.getGCMId()).enqueue(new retrofit2.Callback<BasicResponse>() {
			@Override
			public void onResponse(Call<BasicResponse> call, retrofit2.Response<BasicResponse> response) {

				BasicResponse register = response.body();

				signChoiceDialog.cancel();


				if (register != null && register.error == null)
				{
					Toast.makeText(activity, activity.getString(R.string.inscription_reussi),
							Toast.LENGTH_LONG).show();

					session.setApiKey(Sha1.encrypt(email.toLowerCase() +
							id));
					session.setPseudo(name);
					session.setUrlPhoto(picture);
					session.setUserName(name);
					session.setIdUser(register.id_membre);
					session.setEmailUser(email);
					navigateToEditProfile();
				}
				else {
					showMessage(register.error);
				}
			}

			@Override
			public void onFailure(Call<BasicResponse> call, Throwable t) {
				signChoiceDialog.cancel();
				showMessage(activity.getString(R.string.error_serveur));
			}
		});

	}
    @DebugLog
	private void navigateToHome() {
		destroySlider();
		Intent intent = new Intent(activity, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		activity.startActivity(intent);
		activity.finish();
	}
    @DebugLog
	private void navigateToEditProfile() {
		destroySlider();
		Intent intent = new Intent(activity, EditProfileActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("is_register", true);
		intent.putExtra("id_membre", session.getIdUser());
		activity.startActivity(intent);
		activity.finish();
	}


    @Override
    protected void onResume() {
        super.onResume();

        if (!session.isBanned())
            checkPlayService();
    }
    @DebugLog
    private void checkPlayService() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity.getApplicationContext());
        if (resultCode == ConnectionResult.SUCCESS) {

            //  On vérifie que la personne est connecté
            //avant de faire des vérification sur le gcm_id
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);

            if (regid.isEmpty()) {
                registerInBackground();
            }
            else if (session.isConnected()) {
                //registerInBackground();
                navigateToMainScreen();
            }
            else {
                btn_connection.setVisibility(View.VISIBLE);
                btn_inscription.setVisibility(View.VISIBLE);
            }

        } else if (resultCode == ConnectionResult.SERVICE_MISSING ||
                resultCode == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED ||
                resultCode == ConnectionResult.SERVICE_DISABLED) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, activity, 1);

            if (dialog != null && dialog.isShowing())
                dialog.dismiss();

            dialog.show();
        }
    }


    @Override
	protected void onStop() {
	//	sliderLayout.stopAutoCycle();
		super.onStop();
	}

	private void destroySlider() {
	/*	sliderLayout.stopAutoCycle();
		sliderLayout.removeAllSliders();
		sliderLayout = null;*/
	}


	@Override
	protected void setupComponent(AppComponent appComponent) {
		DaggerSplashComponent.builder()
				.appComponent(appComponent)
				.splashModule(new SplashModule(this))
				.activityModule(new ActivityModule(this))
				.build()
				.inject(this);
	}

    @DebugLog
	private void registerInBackground() {
    	
        new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				 String msg = "";
	                try {
	                    if (gcm == null) {
	                        gcm = GoogleCloudMessaging.getInstance(context);
	                    }
	                    regid = gcm.register(SENDER_ID);
	                    
	                    session.setGCMId(regid);
	                    
	                    msg = "Device registered, registration ID=" + regid;

	                    // You should send the registration ID to your server over HTTP,
	                    // so it can use GCM/HTTP or CCS to send messages to your app.
	                    // The request to your server should be authenticated if your app
	                    // is using accounts.
	                    if (session.isConnected() && regid.length() > 5)
	                    	sendRegistrationIdToBackend();

	                    // For this demo: we don't need to send it because the device
	                    // will send upstream messages to a server that echo back the
	                    // message using the 'from' address in the message.

	                    // Persist the regID - no need to register again.
	                    if (regid.length() > 5)
	                    	storeRegistrationId(context, regid);
	                    
	                } catch (IOException ex) {
	                    msg = "Error :" + ex.getMessage();
	                    // If there is an error, don't just keep trying to register.
	                    // Require the user to click a button again, or perform
	                    // exponential back-off.
	                }
	                
	                Log.e(TAG, msg);
					return null;
	                
			}
			
			protected void onPostExecute(Void result) {
				
				if (regid.length() > 5 && session.isConnected()) {
					navigateToMainScreen();
				}
				else if (regid.length() > 5 && !session.isConnected()) {
					btn_connection.setVisibility(View.VISIBLE);
	            	btn_inscription.setVisibility(View.VISIBLE);
				}
				else if (numTest < MAX_ATTEMPS){
					numTest++;
					registerInBackground();
				}
				else {
					Toast.makeText(activity, "Error, veuillez réssayer après !",
							Toast.LENGTH_LONG).show();
					
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							finish();
						}
					}, 3000);
				}
				
				
				
			};
			
			
        }.execute();
        //...
    }

	@Override
	public void navigateToMainScreen() {
		destroySlider();
		Intent intent = new Intent(this , MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}
	
  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  callbackManager.onActivityResult(requestCode, resultCode, data);



    }
	
	@Override
	public void showBtnLoginSignUp() {
		btn_connection.setVisibility(View.VISIBLE);
		btn_inscription.setVisibility(View.VISIBLE);
	}
	
	 private void sendRegistrationIdToBackend() {
	        // Your implementation here.
	    	GCM gcm = new GCM();
	    	gcm.setGcmId(regid);
	    	
	    	Gson gson = new Gson();
			String json = gson.toJson(gcm);
	    	
	    	userService.updateData(json).enqueue(new retrofit2.Callback<BasicResponse>() {
				@Override
				public void onResponse(Call<BasicResponse> call, retrofit2.Response<BasicResponse> response) {
					Log.e(TAG, "Utilisateurs engristre correctement !");
				}

				@Override
				public void onFailure(Call<BasicResponse> call, Throwable t) {
					Log.e(TAG, "Utilisateurs non engristre !");
				}
			});
	    }

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) 
		{
			case R.id.btn_connection:
				loginChoiceDialog.show();
				break;
	
			case R.id.btn_inscription:
				signChoiceDialog.show();
				break;
		}
		
		
	}

    @Override
    protected void onDestroy() {
        gradientBackgroundPainter.stop();

        if (loginChoiceDialog != null)
            loginChoiceDialog.dismiss();

        if (signChoiceDialog != null)
            signChoiceDialog.dismiss();

        super.onDestroy();
    }

    @Override
	public void setConnectionError() {
		Toast.makeText(this, getString(R.string.error_pas_de_connection_internet),
				Toast.LENGTH_LONG).show();
	}


}
