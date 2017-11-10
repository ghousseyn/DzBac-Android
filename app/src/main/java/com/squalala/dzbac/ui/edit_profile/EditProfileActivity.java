package com.squalala.dzbac.ui.edit_profile;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.google.android.gms.analytics.Tracker;
import com.squalala.dzbac.ActivityModule;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.R;
import com.squalala.dzbac.common.DjihtiBaseActivity;
import com.squalala.dzbac.DjihtiConstant;
import com.squalala.dzbac.data.api.ApiResponse.User;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.ui.cards.CardModule;
import com.squalala.dzbac.ui.cards.EditProfileCard;
import com.squalala.dzbac.ui.main.MainActivity;
import com.squalala.dzbac.ui.show_profile.DaggerShowProfileComponent;
import com.squalala.dzbac.ui.show_profile.ShowProfileModule;
import com.squalala.dzbac.ui.show_profile.ShowProfilePresenter;
import com.squalala.dzbac.ui.show_profile.ShowProfileView;
import com.squalala.dzbac.ui.showcaseview.ToolbarActionItemTarget;
import com.squalala.dzbac.utils.UploadHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import it.gmariotti.cardslib.library.view.CardView;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : EditProfileActivity.java
 * Date : 4 août 2014
 * 
 */
public class EditProfileActivity extends DjihtiBaseActivity 
 	implements ShowProfileView , OnClickListener  {

	private static final String TAG = EditProfileActivity.class.getSimpleName();
	
	@Inject EditProfileCard editProfileCard;
	@Inject ShowProfilePresenter presenter;

    @Inject
    MainPreferences preferences;

	@Inject
	Tracker tracker;

    @InjectView(R.id.tool_bar)
    Toolbar toolbar;
	
	@InjectView(R.id.avatar_user) CircleImageView avatarUser;
    @InjectView(R.id.smooth_edit_profile)
    SmoothProgressBar smoothBar;
	@InjectView(R.id.background_user) ImageView backgroundUser;
	@InjectView(R.id.card_edit_info_profile) CardView cardEditProfile;
	
	private String idMembre;
	private Intent data;
	
	private boolean isUploadingAvatar = false, isUploadingBackground = false;
	
	private boolean isRegister = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tracker.setScreenName(getClass().getName());

		setContentView(R.layout.activity_edit_profile);
		
		ButterKnife.inject(this);

        setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar.setBackgroundColor(Color.TRANSPARENT);

		
		try {
			isRegister = getIntent().getBooleanExtra("is_register", false);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		avatarUser.setOnClickListener(this);
		backgroundUser.setOnClickListener(this);
		
        smoothBar.setVisibility(View.VISIBLE);
		
		idMembre = getIntent().getExtras().getString("id_membre");
		presenter.getDataUser(idMembre);
	}

	@Override
	public void displayDataUser(User user) {

		if (user != null && !preferences.isTutoGuide(TAG))  {

			new ShowcaseView.Builder(this)
					.withMaterialShowcase()
					.setTarget(new ToolbarActionItemTarget(toolbar, R.id.menu_enregistre_information))
					.setStyle(R.style.CustomShowcaseTheme4)
					.setContentTitle("Ton profil")
					.setContentText("Tu peux modifier ton profil à volonté, mais surtout n'oublie pas d'enregistrer !")
					.singleShot(3)
					.build()
					.show();

			preferences.setTutoGuide(TAG, true);
		}


        preferences.setUrlPhoto(user.url_avatar_thumbnail);
		preferences.setUrlBackground(user.url_image_fond);
        preferences.setUserName(user.userName);

		Picasso.with(this)
		.load(user.url_avatar_thumbnail)
         .placeholder(R.drawable.djihti_photo)
		.into(avatarUser);
	
		Picasso.with(this)
		.load(user.url_image_fond)
		.fit()
		.into(backgroundUser);
		
		editProfileCard.setUser(user);
		cardEditProfile.setCard(editProfileCard);
	//	relativeShowUser.setVisibility(View.VISIBLE);
		hideProgess();


	}
	
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu_edit_profile, menu);

        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch (item.getItemId())
    	{
			case R.id.menu_enregistre_information:
				
                smoothBar.setVisibility(View.VISIBLE);
				presenter.updateDataUser(editProfileCard.getUserInfomations());
				
				break;


			case android.R.id.home:

				if (!isRegister) {
					if (!isUploadingAvatar && !isUploadingBackground)
						finish();
					else
						Crouton.makeText(this, getString(R.string.error_upload_non_termine),
								Style.ALERT).show();
				}

				return true;
	
			default:
				break;
		}
    	
    	return super.onOptionsItemSelected(item);
    }

	/*@Override
	protected List<Object> getModules() {
		return Arrays.<Object>asList(new ShowProfileModule(this),
				                     new CardModule(this));
	}*/

	@Override
	protected void setupComponent(AppComponent appComponent) {
		DaggerShowProfileComponent.builder()
				.appComponent(appComponent)
				.showProfileModule(new ShowProfileModule(this))
				.cardModule(new CardModule(this))
				.activityModule(new ActivityModule(this))
				.build()
				.inject(this);
	}

	@Override
	public void updateFollow(boolean isFollowed) {

	}

	private void intentAvatar() {
		Intent intent = new Intent(
				Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, DjihtiConstant.REQUEST_CODE_LOAD_AVATAR);
	}

	private void intentBackgorund() {
		Intent intent = new Intent(
				Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, DjihtiConstant.REQUEST_CODE_LOAD_BACKGROUND);
	}


	final PermissionCallback permissionAvatarCallback = new PermissionCallback() {
		@Override
		public void permissionGranted() {
			//boolean hasAccess = Tools.accessContacts(MainActivity.this);
			//   Toast.makeText(MainActivity.this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();
			intentAvatar();
		}

		@Override
		public void permissionRefused() {
			//  boolean hasAccess = Tools.accessContacts(MainActivity.this);
			// Toast.makeText(MainActivity.this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();
		}
	};

	final PermissionCallback permissionBakcgroundCallback = new PermissionCallback() {
		@Override
		public void permissionGranted() {
			//boolean hasAccess = Tools.accessContacts(MainActivity.this);
			//   Toast.makeText(MainActivity.this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();
			intentBackgorund();
		}

		@Override
		public void permissionRefused() {
			//  boolean hasAccess = Tools.accessContacts(MainActivity.this);
			// Toast.makeText(MainActivity.this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.avatar_user:
			
			if (!isUploadingAvatar) {

				if(Nammu.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
					//We have a permission, easy peasy
					//  boolean hasAccess = Tools.accessContacts(this);
					// Toast.makeText(this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();
					intentAvatar();
				} else {
					//We do not own this permission
					if (Nammu.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
						//User already refused to give us this permission or removed it
						//Now he/she can mark "never ask again" (sic!)
						Snackbar.make(findViewById(android.R.id.content), getString(R.string.permission_photo),
								Snackbar.LENGTH_INDEFINITE)
								.setAction(getString(R.string.ok), new View.OnClickListener() {
									@Override
									public void onClick(View view) {
										Nammu.askForPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE, permissionAvatarCallback);
									}
								}).show();
					} else {
						//First time asking for permission
						// or phone doesn't offer permission
						// or user marked "never ask again"
						Nammu.askForPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, permissionAvatarCallback);
					}
				}
			}
			else
				Crouton.makeText(this, getString(R.string.error_upload_avatar_en_cours),
						Style.INFO).show();
			
			
			break;
			
		case R.id.background_user:
			
			if (!isUploadingBackground) {

				if(Nammu.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
					//We have a permission, easy peasy
					//  boolean hasAccess = Tools.accessContacts(this);
					// Toast.makeText(this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();
					intentBackgorund();
				} else {
					//We do not own this permission
					if (Nammu.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
						//User already refused to give us this permission or removed it
						//Now he/she can mark "never ask again" (sic!)
						Snackbar.make(findViewById(android.R.id.content), getString(R.string.permission_photo),
								Snackbar.LENGTH_INDEFINITE)
								.setAction(getString(R.string.ok), new View.OnClickListener() {
									@Override
									public void onClick(View view) {
										Nammu.askForPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE, permissionAvatarCallback);
									}
								}).show();
					} else {
						//First time asking for permission
						// or phone doesn't offer permission
						// or user marked "never ask again"
						Nammu.askForPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, permissionBakcgroundCallback);
					}
				}

			}
			else
				Crouton.makeText(this, getString(R.string.error_upload_background_en_cours),
						Style.INFO).show();
			
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		this.data = data;
		
		 if (requestCode == DjihtiConstant.REQUEST_CODE_LOAD_AVATAR && 
				 resultCode == RESULT_OK && null != data) {
			 isUploadingAvatar = true;
			 new UploadTask().execute();
		 }
			      
		 else if (requestCode == DjihtiConstant.REQUEST_CODE_LOAD_BACKGROUND && 
				 resultCode == RESULT_OK && null != data) {
			 isUploadingBackground = true;
			 new UploadTask().execute();
		 }
	}
	
	
	private class UploadTask extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected void onPreExecute() {
            smoothBar.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}
			
		@Override
		protected Void doInBackground(Void... params) {
			 if (isUploadingAvatar)
				 presenter.updateAvatarUser(UploadHelper.getTypedFile(getApplicationContext(),
						 UploadHelper.getPathFromIntent(getApplicationContext(), data)));
			 else
				 presenter.updateBackgroundUser(UploadHelper.getTypedFile(getApplicationContext(),
						 UploadHelper.getPathFromIntent(getApplicationContext(), data)));
			return null;
		}
	}
	
	@Override
	public void onBackPressed() {
		
		if (!isRegister) {
			if (!isUploadingAvatar && !isUploadingBackground)
				super.onBackPressed();
			else
				Crouton.makeText(this, getString(R.string.error_upload_non_termine),
						Style.ALERT).show();
		}
	}
	
	
	@Override
	public void finish() {
		setResult(RESULT_OK);
		super.finish();
	}

	@Override
	public void showCrouton(String message) {
		Crouton.makeText(this, message, Style.INFO).show();
		editProfileCard.setErrorUserName(null);
		hideProgess();
	}
	
	@Override
	protected void onDestroy() {
		Crouton.cancelAllCroutons();
		super.onDestroy();
	}

	@Override
	public void finishUploadAvatar(String urlAvatar) {
		Crouton.makeText(this, getString(R.string.upload_avatar), Style.INFO).show();

        preferences.setUrlPhoto(urlAvatar);


		Picasso.with(this)
			.load(urlAvatar)
			.into(avatarUser, new Callback() {
                @Override
                public void onSuccess() {
                    YoYo.with(Techniques.RubberBand)
                            .duration(700)
                            .playOn(avatarUser);
                }

                @Override
                public void onError() {

                }
            });

		isUploadingAvatar = false;
		hideProgess();


	}

	@Override
	public void finishUploadBackground(String urlBackground) {
		Crouton.makeText(this, getString(R.string.upload_image_fond), Style.INFO).show();

        preferences.setUrlBackground(urlBackground);

		Picasso.with(this)
			.load(urlBackground)
			.fit()
			.into(backgroundUser, new Callback() {
                @Override
                public void onSuccess() {
                    YoYo.with(Techniques.RubberBand)
                            .duration(700)
                            .playOn(backgroundUser);
                }

                @Override
                public void onError() {

                }
            });

		isUploadingBackground = false;
		hideProgess();


	}
	
	private void hideProgess() {
		if (!isUploadingAvatar && !isUploadingBackground) {
            smoothBar.setVisibility(View.GONE);
        }

	}

	@Override
	public void navigateToHome() {
		if(isRegister) {
			Intent intent = new Intent(this , MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}

	@Override
	public void setErrorUserName() {
		String message = getString(R.string.error_nom_prenom);
		Crouton.makeText(this, message, Style.ALERT).show();
		editProfileCard.setErrorUserName(message);
		hideProgess();
	}

}
