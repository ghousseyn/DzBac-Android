package com.squalala.dzbac.ui.show_profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import com.google.android.gms.analytics.Tracker;
import com.squalala.dzbac.ActivityModule;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.DjihtiConstant;
import com.squalala.dzbac.R;
import com.squalala.dzbac.common.DjihtiBaseActivity;
import com.squalala.dzbac.data.api.ApiResponse.User;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.eventbus.events.UserEvent;
import com.squalala.dzbac.ui.about.AboutFragment;
import com.squalala.dzbac.ui.cards.CardModule;
import com.squalala.dzbac.ui.dialogs.DialogModule;
import com.squalala.dzbac.ui.dialogs.SendMessageDialog;
import com.squalala.dzbac.ui.edit_profile.EditProfileActivity;
import com.squalala.dzbac.ui.full_imageview.ViewPagerActivity;
import com.squalala.dzbac.ui.list_items.PostsFragment;
import com.squalala.dzbac.ui.payement.PayementActivity;
import com.squalala.dzbac.utils.DialogUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import github.chenupt.dragtoplayout.DragTopLayout;
import hugo.weaving.DebugLog;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Sequence;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ShowProfileActivity.java
 * Date : 31 juil. 2014
 * 
 */
public class ShowProfileActivity extends DjihtiBaseActivity
	implements ShowProfileView, OnClickListener, MaterialTabListener {
	
	//@InjectView(R.id.relative_show_user) RelativeLayout relativeShowUser;
	@InjectView(R.id.pseudo_user) TextView pseudo;
	@InjectView(R.id.avatar_user) CircleImageView avatarUser;
	@InjectView(R.id.background_user) ImageView backgroundUser;
    @InjectView(R.id.txt_follow) TextView txtFollow;

//	@InjectView(R.id.card_show_info_profile) CardView cardInfoProfile;
	
	@Inject ShowProfilePresenter presenter;
	@Inject SendMessageDialog sendMessageDialog;
	@Inject MainPreferences mainPreferences;

    private Animation mEnterAnimation, mExitAnimation;

    @Inject
    Tracker tracker;

	private MenuItem send_message, edit_profile, blockUser, bannUser;
	
	private User user;
	private String idMembre;

    private static final String TAG = ShowProfileActivity.class.getSimpleName();

    @InjectView(R.id.tabHost)
    MaterialTabHost tabHost;

    @InjectView(R.id.tool_bar)
    Toolbar toolbar;

    @InjectView(R.id.drag_layout)
    DragTopLayout dragLayout;

    @InjectView(R.id.view_pager)
    ViewPager viewPager;

    private static final int NUM_FRAGMENTS = 3;

    private boolean isFollowing;

    // Pour envoyer un MP
    private ImageView button;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
    		super.onCreate(savedInstanceState);

        tracker.setScreenName(getClass().getName());

        Log.e(TAG, "onCreate --> savedInstance : " + (savedInstanceState == null));

		setContentView(R.layout.activity_show_profile);
		
		ButterKnife.inject(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setBackgroundColor(Color.TRANSPARENT);

        dragLayout.setOverDrag(false);
		
		idMembre = getIntent().getExtras().getString("id_membre");

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @DebugLog
            @Override
            public void onPageSelected(int position) {


                if (user != null) {
                    switch (position) {
                        case 0:


                            Answers.getInstance().logContentView(new ContentViewEvent()
                                    .putContentName(user.userName)
                                    .putContentType("Profil About View")
                                    .putContentId(idMembre));


                            break;


                        case 1:

                            Answers.getInstance().logContentView(new ContentViewEvent()
                                    .putContentName(user.userName)
                                    .putContentType("Profil Posts View")
                                    .putContentId(idMembre));



                            break;


                        case 2:

                            Answers.getInstance().logContentView(new ContentViewEvent()
                                    .putContentName(user.userName)
                                    .putContentType("Profil Posts Lile View")
                                    .putContentId(idMembre));



                            break;
                    }
                }


                // when user do a swipe the selected tab change
                tabHost.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < NUM_FRAGMENTS; i++) {

            String [] titlesFragments = getResources().getStringArray(R.array.title_fragment_profil);

            tabHost.addTab(
                    tabHost.newTab()
                            .setText(titlesFragments[i])
                            .setTabListener(this)
            );

        }

	}


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        idMembre = intent.getExtras().getString("id_membre");

        Intent i = new Intent(this, ShowProfileActivity.class);
        i.putExtra("id_membre", idMembre);

        startActivity(i);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onEvent(UserEvent event) {

        if (event.isFollowed())
            txtFollow.setText(getString(R.string.sedésabonner));
        else
            txtFollow.setText(getString(R.string.sabonner));

        isFollowing = event.isFollowed();

        if (mainPreferences.getIdUser().equals(idMembre))
            txtFollow.setVisibility(View.GONE);


    }

    @Override
    public void updateFollow(boolean isFollowed) {

        if (isFollowed)
            txtFollow.setText(getString(R.string.sedésabonner));
        else
            txtFollow.setText(getString(R.string.sabonner));

        isFollowing = isFollowed;
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerShowProfileComponent.builder()
                .appComponent(appComponent)
                .showProfileModule(new ShowProfileModule(this))
                .cardModule(new CardModule(this))
                .dialogModule(new DialogModule())
                .activityModule(new ActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	    	MenuInflater inflater = getMenuInflater();
	    	inflater.inflate(R.menu.menu_show_profile, menu);
	    	
	    	send_message = menu.findItem(R.id.menu_send_message);
	    	edit_profile = menu.findItem(R.id.menu_edit_account);
            blockUser = menu.findItem(R.id.menu_block_user);
            bannUser = menu.findItem(R.id.menu_bann_user);
	    	
	    	// Le profile de l'utilisateur lui même
	    	if (idMembre.equals(mainPreferences.getIdUser())) { 
	    		send_message.setVisible(false);
	    		edit_profile.setVisible(true);
                blockUser.setVisible(false);
	    	}
	    	else {
	    		send_message.setVisible(true);
	    		edit_profile.setVisible(false);
                blockUser.setVisible(true);
	    	}

        button = (ImageView) send_message.getActionView();

        // just adding some padding to look better
        float density = getResources().getDisplayMetrics().density;
        int padding = (int)(5 * density);
        button.setPadding(padding, padding, padding, padding);

        // set an image
        button.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_message_white_24dp));

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mainPreferences.getPremiumCode() == 4 ||
                    mainPreferences.getPremiumCode() == 5 ||
                    mainPreferences.getPremiumCode() == 6) {
                    Answers.getInstance().logCustom(new CustomEvent("Messaging")
                            .putCustomAttribute("Click", "Send From Profile"));

                    sendMessageDialog.setIdReceveur(idMembre);
                    sendMessageDialog.show();
                }
                else if (mainPreferences.getLevelContribution() < 2) {
                    new SweetAlertDialog(ShowProfileActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(ShowProfileActivity.this.getString(R.string.oups))
                            .setContentText(ShowProfileActivity.this.getString(R.string.alert_message_contrib_error))
                            .setCancelText(getString(R.string.ok))
                            .setConfirmText(getString(R.string.en_savoir_plus))
                            .showCancelButton(true)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    startActivity(new Intent(ShowProfileActivity.this, PayementActivity.class));
                                }
                            })
                            .show();
                }
                else {
                    Answers.getInstance().logCustom(new CustomEvent("Messaging")
                            .putCustomAttribute("Click", "Send From Profile"));

                    sendMessageDialog.setIdReceveur(idMembre);
                    sendMessageDialog.show();
                }


                // TODO on retire pour le moment à cause des utilisateurs actifs qui baissent
               /* if (mainPreferences.isPrenium())
                {
                    sendMessageDialog.setIdReceveur(idMembre);
                    sendMessageDialog.show();
                }
                else
                    DialogUtils.showPopup(ShowProfileActivity.this, getString(R.string.compte_prenium), getString(R.string.error_prenimum_message));
                */
            }
        });

	        return super.onCreateOptionsMenu(menu);
	    }
	    
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	    	
	    	switch (item.getItemId())
	    	{
				case R.id.menu_send_message:

                    if (!mainPreferences.isPrenium())
                    {
                        DialogUtils.showPopup(this, getString(R.string.compte_prenium), getString(R.string.error_prenimum_message));
                    }
                    else
                    {
                        Answers.getInstance().logCustom(new CustomEvent("Profil")
                                .putCustomAttribute("Click", "Message privée"));

                        sendMessageDialog.setIdReceveur(idMembre);
                        sendMessageDialog.show();
                    }

					break;

                case android.R.id.home:

                    finish();

                    return true;
					
				case R.id.menu_edit_account:

                    Answers.getInstance().logCustom(new CustomEvent("Profil")
                            .putCustomAttribute("Click", "Edition de compte"));
					
					Intent intent = new Intent(this ,  EditProfileActivity.class);
					intent.putExtra("id_membre", idMembre);
					startActivityForResult(intent, DjihtiConstant.REQUEST_CODE_PROFILE_ACTIVITY);
					
					break;

                case R.id.menu_block_user:

                    Answers.getInstance().logCustom(new CustomEvent("Profil")
                            .putCustomAttribute("Click", "Bloque user"));

                    new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(getString(R.string.alerte))
                            .setContentText(getString(R.string.bloquer_warn))
                            .setCancelText(getString(R.string.non))
                            .setConfirmText(getString(R.string.oui))
                            .showCancelButton(true)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    presenter.blockUser(idMembre);
                                }
                            })
                            .show();



                    break;

                case R.id.menu_bann_user:

                    Answers.getInstance().logCustom(new CustomEvent("Profil")
                            .putCustomAttribute("Click", "Ban User"));

                    new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(getString(R.string.banissement))
                            .setContentText(getString(R.string.banissement_warn))
                            .setCancelText(getString(R.string.non))
                            .setConfirmText(getString(R.string.oui))
                            .showCancelButton(true)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    presenter.bannUser(idMembre);
                                }
                            })
                            .show();


                    break;
		
				default:
					break;
			}
	    	return super.onOptionsItemSelected(item);
	    }
	    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
    	
    	if (resultCode == RESULT_OK &&
    			requestCode == DjihtiConstant.REQUEST_CODE_PROFILE_ACTIVITY) {
    		presenter.getDataUser(idMembre);
    	}
    		
		super.onActivityResult(requestCode, resultCode, arg2);
	}

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");

        presenter.getDataUser(idMembre);
    }

    @Override
	public void displayDataUser(User user) {
        Log.e(TAG, "displayDataUser");
		
		this.user = user;

        if (user != null) {

            Answers.getInstance().logContentView(new ContentViewEvent()
                    .putContentName(user.userName)
                    .putContentType("Profil View")
                    .putContentId(idMembre));

            if (!idMembre.equals(mainPreferences.getIdUser()) && !mainPreferences.isTutoGuide(TAG))
            {
                mEnterAnimation = new AlphaAnimation(0f, 1f);
                mEnterAnimation.setDuration(600);
                mEnterAnimation.setFillAfter(true);

                mExitAnimation = new AlphaAnimation(1f, 0f);
                mExitAnimation.setDuration(600);
                mExitAnimation.setFillAfter(true);

                TourGuide tourGuide1 = TourGuide.init(this)
                        .setToolTip(new ToolTip()
                                        .setTitle("Messagerie privée")
                                        .setDescription("Tu peux contacter un membre à tout moment !")
                                        .setGravity(Gravity.BOTTOM)

                        )
                                // note that there is not Overlay here, so the default one will be used
                        .playLater(button);

                TourGuide tourGuide2 = TourGuide.init(this)
                        .setToolTip(new ToolTip()
                                        .setTitle("Ne rate plus rien !")
                                        .setDescription("Tu peux aussi t'abonner aux autres membres pour recevoir des notifications de leur nouveaux posts !")
                                        .setGravity(Gravity.TOP)
                        )
                                // note that there is not Overlay here, so the default one will be used
                        .playLater(txtFollow);


                Sequence sequence = new Sequence.SequenceBuilder()
                        .add(tourGuide1, tourGuide2)
                        .setDefaultOverlay(new Overlay()
                                        .setEnterAnimation(mEnterAnimation)
                                        .setExitAnimation(mExitAnimation)
                                        .setBackgroundColor(Color.parseColor("#EE2c3e50"))
                        )
                        .setDefaultPointer(null)
                        .setContinueMethod(Sequence.ContinueMethod.Overlay)
                        .build();




                TourGuide.init(this).playInSequence(sequence);

                mainPreferences.setTutoGuide(TAG, true);

            }


        }
          //(ImageView) menuItem.getActionView()

        if (idMembre.equals(mainPreferences.getIdUser())) {
            mainPreferences.setUrlPhoto(user.url_avatar_thumbnail);
            mainPreferences.setUrlBackground(user.url_image_fond);
            mainPreferences.setUserName(user.userName);
        }

        if (!idMembre.equals(mainPreferences.getIdUser()) && mainPreferences.getLevelUser() >= 2) {
            try {
               bannUser.setVisible(true);
            } catch (NullPointerException e) {}
        }

        Picasso.with(this)
                .load(user.url_avatar_thumbnail)
                .placeholder(R.drawable.djihti_photo)
                .into(avatarUser);

		Picasso.with(this)
			.load(user.url_image_fond)
			.fit()
			.into(backgroundUser);

        Picasso.with(this)
                .load(user.url_image_fond)
                .into(target);
		
		pseudo.setText(user.userName);

        avatarUser.setOnClickListener(this);
        backgroundUser.setOnClickListener(this);
        txtFollow.setOnClickListener(this);
	}

    @Override
    protected void onDestroy() {
        Crouton.cancelAllCroutons();
        super.onDestroy();
    }

    @Override
	public void onClick(View v) {
		
		Intent intent;
		
		switch (v.getId()) {

        case R.id.txt_follow:

            txtFollow.setText(getString(R.string.chargement_));

            if (isFollowing) {
                presenter.onUnFollowUser(idMembre);

                Answers.getInstance().logCustom(new CustomEvent("Profil")
                        .putCustomAttribute("Click", "UnFollow User"));
            }
            else {
                presenter.onFollowUser(idMembre);

                Answers.getInstance().logCustom(new CustomEvent("Profil")
                        .putCustomAttribute("Click", "Follow User"));
            }


            break;

		case R.id.avatar_user:
			
			intent = new Intent(getApplicationContext()
					, ViewPagerActivity.class);
            intent.putExtra("avatar", true);
			intent.putExtra("url_to_split", user.url_avatar_large);
            intent.putExtra("path_images", "Profils");
			startActivity(intent);

            if (user != null)
                Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName(user.userName)
                        .putContentType("Profil Avatar View")
                        .putContentId(idMembre));
			
			break;
			
		case R.id.background_user:
			
			intent = new Intent(getApplicationContext()
					, ViewPagerActivity.class);
			intent.putExtra("url_to_split", user.url_image_fond);
            intent.putExtra("path_images", "Backgrounds");
			startActivity(intent);

            if (user != null)
                Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName(user.userName)
                        .putContentType("Profil Background View")
                        .putContentId(idMembre));
			
			break;

		default:
			break;
		}
	}


	@Override
	public void showCrouton(String message) {
        Crouton.makeText(this, message, Style.INFO).show();
    }

	@Override
	public void finishUploadAvatar(String message) {}

	@Override
	public void finishUploadBackground(String message) {}


	@Override
	public void navigateToHome() {
	}


	@Override
	public void setErrorUserName() {}


    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int num) {

            switch (num) {

                case 0:

                    return AboutFragment.newInstance(idMembre);

                case 1:

                    return PostsFragment.newInstance(true, false, true, idMembre);

                case 2:

                    return PostsFragment.newInstance(false, true, true, idMembre);
            }

            return null;
        }


        @Override
        public int getCount() {
            return NUM_FRAGMENTS;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getStringArray(R.array.title_fragment_profil)[position];
        }


    }


    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, final Picasso.LoadedFrom from) {

            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {

                    int defaultColor = ContextCompat.getColor(ShowProfileActivity.this, R.color.black);

                    dragLayout.setBackgroundColor(palette.getDarkVibrantColor(defaultColor));
                    tabHost.setPrimaryColor(palette.getDarkVibrantColor(defaultColor));
                    txtFollow.setBackgroundColor(palette.getDarkVibrantColor(defaultColor));
                    // tabHost.setAccentColor(palette.getDarkVibrantColor(defaultColor));

                }
            });
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    public void onTabSelected(MaterialTab materialTab) {
        viewPager.setCurrentItem(materialTab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }
}
