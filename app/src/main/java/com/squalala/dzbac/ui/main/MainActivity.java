package com.squalala.dzbac.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.squalala.dzbac.ActivityModule;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.R;
import com.squalala.dzbac.common.DjihtiBaseMainActivity;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.eventbus.events.FilterEvent;
import com.squalala.dzbac.settings.SettingsActivity;
import com.squalala.dzbac.ui.condition.ConditionFragment;
import com.squalala.dzbac.ui.dialogs.DialogModule;
import com.squalala.dzbac.ui.filtre.FiltreItemActivity;
import com.squalala.dzbac.ui.full_imageview.ViewPagerActivity;
import com.squalala.dzbac.ui.list_conversation.ListConversationActivity;
import com.squalala.dzbac.ui.list_followers.ListFollowerFragment;
import com.squalala.dzbac.ui.list_items.PostsFragment;
import com.squalala.dzbac.ui.notifications.NotificationActivity;
import com.squalala.dzbac.ui.payement.PayementActivity;
import com.squalala.dzbac.ui.payement.PayementFragment;
import com.squalala.dzbac.ui.ranking.RankingFragment;
import com.squalala.dzbac.ui.show_profile.ShowProfileActivity;
import com.squalala.dzbac.ui.showcaseview.ToolbarActionItemTarget;
import com.squalala.dzbac.ui.sms_verification.SmsVerificationActivity;
import com.squalala.dzbac.ui.splash.SplashActivity;
import com.squalala.dzbac.utils.ConnectionDetector;
import com.squalala.dzbac.utils.NoteAppReminder;
import com.squalala.dzbac.utils.SystemUtils;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import bolts.AppLinks;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import hugo.weaving.DebugLog;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialSectionListener;

public class MainActivity extends DjihtiBaseMainActivity
	implements MainView, View.OnClickListener, DrawerLayout.DrawerListener {

	@Inject MainPresenter mainPresenter;
	
	@Inject MainPreferences mainPreferences;
	@Inject
    Tracker tracker;
    @Inject
    PostsFragment postsFragment;
    @Inject
    PostsFragment myPostsFragment;

    @Inject
    NoteAppReminder noteAppReminder;

    private ImageView button;

    private Animation mEnterAnimation, mExitAnimation;

	
	private Activity activity = this;
    private Intent intent;
	
	private static final String TAG = "MainActivity";

    private TextView userName;
    private ImageView userCover;
    private CircleImageView userPhoto;

	private int numberMessage, numberNotifications;

    MaterialSection sectionPayement;
    MaterialSection sectionSignalement;


    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Log.e(TAG, "init");

        ButterKnife.inject(this);

        Uri targetUrl =
                AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        if (targetUrl != null) {
            Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
        }

        // create and set the header
        View view = LayoutInflater.from(this).inflate(R.layout.custom_drawer,null);
        setDrawerHeaderCustom(view);

        userName = (TextView) findViewById(R.id.user_nome);

        userPhoto = (CircleImageView) findViewById(R.id.user_photo);
        userCover = (ImageView) findViewById(R.id.user_cover);

        MaterialSection sectionNoteApp = newSection(getString(R.string.noter_application),
                R.drawable.ic_play_shopping_bag_grey600_24dp, new Intent());
        MaterialSection sectionFriends = newSection(getString(R.string.mes_amis),
                R.drawable.ic_group_grey600_24dp, new Intent());
     //   MaterialSection sectionRecreation = newSection(getString(R.string.recreation),
       //         R.drawable.ic_forum_grey600_24dp, new RecreationFragment());

        sectionPayement = newSection("Nos offres",
             R.drawable.ic_payment_grey600_24dp, new PayementFragment());

        sectionSignalement = newSection(getString(R.string.signalements), R.drawable.ic_warning_grey600_24dp,
                new com.squalala.dzbac.ui.signalement.PostsFragment());


                MaterialSection sectionMonProfil = newSection(getString(R.string.mon_profil),
                R.drawable.ic_person_grey600_24dp, new Intent(this, ShowProfileActivity.class));
        MaterialSection sectionContact = newSection(getString(R.string.contact),
                R.drawable.ic_email_grey600_24dp, new Intent(this, ShowProfileActivity.class));

        MaterialSection sectionCondition = newSection(getString(R.string.reglement),
                R.drawable.ic_account_balance_grey600_24dp, new ConditionFragment());

        MaterialSection sectionDeconnexion = newSection(getString(R.string.deconnexion),
                R.drawable.ic_settings_power_grey600_24dp, new Fragment());


        userPhoto.setOnClickListener(this);

        setDrawerListener(this);

        sectionNoteApp.setOnClickListener(new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection materialSection) {

                String appPackageName2 = getPackageName();
                Intent marketIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName2));
                marketIntent2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                startActivity(marketIntent2);

                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Play Store")
                        .setAction("Click")
                        .setLabel("Bouton pour noter l'app")
                        .setValue(1)
                        .build());
            }
        });


  /*      sectionShare.setOnClickListener(new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection materialSection) {
                showShareDialogFB();

                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Facebook")
                        .setAction("Click")
                        .setLabel("Bouton pour partager l'app")
                        .setValue(1)
                        .build());
            }
        });*/

        sectionMonProfil.setOnClickListener(new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection materialSection) {

                Answers.getInstance().logCustom(new CustomEvent("Menu Drawer")
                        .putCustomAttribute("Click", "Mon profil"));

                intent = new Intent(activity, ShowProfileActivity.class);
                intent.putExtra("id_membre", mainPreferences.getIdUser()); // getIdUser...
                startActivity(intent);


                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Profile")
                        .setAction("Click")
                        .setLabel("Bouton pour modifier son profil")
                        .setValue(1)
                        .build());
            }
        });

        sectionContact.setOnClickListener(new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection materialSection) {

                Answers.getInstance().logCustom(new CustomEvent("Menu Drawer")
                        .putCustomAttribute("Click", "Contact"));

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"team.dzbac@gmail.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "DZ BAC");
                email.putExtra(Intent.EXTRA_TEXT, "");
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choisissez :"));
            }
        });


        sectionDeconnexion.setOnClickListener(new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection materialSection) {

                Answers.getInstance().logCustom(new CustomEvent("Menu Drawer")
                        .putCustomAttribute("Click", "Deconnexion"));

                new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                        .setContentText(activity.getString(R.string.alert_click_deconnexion))
                        .setTitleText(activity.getString(R.string.deconnexion))
                        .setCancelText(activity.getString(R.string.non))
                        .setConfirmText(activity.getString(R.string.oui))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                sweetAlertDialog.dismiss();
                                mainPreferences.deconnexion();
                                intent = new Intent(activity, SplashActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        })
                        .show();

         /*       AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage(activity.getString(R.string.alert_click_deconnexion));

                builder.setNeutralButton(activity.getString(R.string.oui),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                                mainPreferences.deconnexion();
                                intent = new Intent(activity , SplashActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                builder.setNegativeButton(activity.getString(R.string.non),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();*/

            }
        });


        this.addSection(newSection(getString(R.string.accueil), R.drawable.ic_home_grey600_24dp, PostsFragment.newInstance(false, false, false, null)));
        this.addSection(sectionMonProfil);
        this.addSection(newSection(getString(R.string.mes_posts), R.drawable.ic_create_grey600_24dp, PostsFragment.newInstance(true, false, false, mainPreferences.getIdUser())));
        this.addSection(newSection(getString(R.string.mes_abonnements), R.drawable.ic_account_child_grey600_24dp, ListFollowerFragment.newInstance(false, null)));
        this.addSection(newSection(getString(R.string.classement), R.drawable.ic_trophy_variant_grey600_24dp, RankingFragment.newInstance()));
        this.addSection(newSection(getString(R.string.reglages), R.drawable.ic_settings_grey600_24dp, new Intent(this, SettingsActivity.class)));

        this.addSection(sectionCondition);
      //  this.addSection(sectionFriends);
        this.addSection(sectionPayement);

        if (mainPreferences.getLevelUser() == 2)
            this.addSection(sectionSignalement);

   //     this.addSection(sectionNoteApp);
        this.addSection(sectionContact);

        this.addBottomSection(sectionDeconnexion);

        allowArrowAnimation();

        setBackPattern(MaterialNavigationDrawer.BACKPATTERN_BACK_TO_FIRST);

      //  setDrawer
    }


    /*final PermissionCallback permissionFriendsCallback = new PermissionCallback() {
        @Override
        public void permissionGranted() {
            //boolean hasAccess = Tools.accessContacts(MainActivity.this);
            //   Toast.makeText(MainActivity.this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();

            intent = new Intent(activity, FriendsActivity.class);
            startActivity(intent);
        }

        @Override
        public void permissionRefused() {
            //  boolean hasAccess = Tools.accessContacts(MainActivity.this);
            // Toast.makeText(MainActivity.this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();
        }
    };*/



    private void showShareDialogFB() {

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://dzbac.squalala.com/"))
                .setImageUrl(Uri.parse("http://www.squalala.xyz/dz_bac/images/1.png"))
                .build();

        ShareDialog.show(this, content);
    }

    @Override
    protected void onResume() {

        System.out.println("conne : "
                + ConnectionDetector.isConnectingToInternet(this));
        Log.e(TAG, "onResume");
    	super.onResume();


        if (!mainPreferences.isPhoneNumberVerified()) {
            Intent intent = new Intent(this, SmsVerificationActivity.class);
            intent.putExtra("MainActivity", true);
            startActivity(intent);
        }

     /*   if (!mainPreferences.isPrenium())
        {
            sectionPayement.setTitle("Soutiens-nous !");
        }
*/
        AppEventsLogger.activateApp(this);
        //  noteAppReminder.checkMomentNoteApp(tracker);
        noteAppReminder.checkMomentNoteApp(null);

        userName.setText(mainPreferences.getUserName());

        Picasso.with(this)
                .load(mainPreferences.getUrlPhoto())
             //   .centerCrop()
                .into(userPhoto);

        Picasso.with(this)
                .load(mainPreferences.getUrlBackground())
              //  .centerCrop()
                .into(userCover);

    	mainPresenter.checkStateUser();
    }

    @Override
    public void showPopupBan() {

        mainPreferences.setBanned(true);
        mainPreferences.deconnexion();

        SystemUtils.cleanDzBacFolder();

        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                .setContentText(activity.getString(R.string.ban_user))
                .setTitleText(activity.getString(R.string.bannissment))
                .setConfirmText(activity.getString(R.string.ok))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        sweetAlertDialog.dismissWithAnimation();

                        intent = new Intent(activity, SplashActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });

        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
    }

    @Override
    public void setErrorPremium() {
        if (mainPreferences.getPremiumCode() == 4 ||
                mainPreferences.getPremiumCode() == 5 ||
                mainPreferences.getPremiumCode() == 6) {
           // Continue working
        }
        else if (mainPreferences.getLevelContribution() < 2) {
            SweetAlertDialog dialog =  new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(this.getString(R.string.oups))
                    .setContentText(this.getString(R.string.error_premium))
                    .setCancelText(getString(R.string.quitter))
                    .setConfirmText(getString(R.string.en_savoir_plus))
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            finish();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            startActivity(new Intent(MainActivity.this, PayementActivity.class));
                        }
                    });

            dialog.setCancelable(false);

            dialog.show();
        }
        else {
            // Continue working
        }
    }

    @Override
    public void showPopupMultipleDevice() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                .setContentText(activity.getString(R.string.error_multile_account))
                .setTitleText(activity.getString(R.string.multiple_account))
                .setConfirmText(activity.getString(R.string.ok))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        sweetAlertDialog.dismissWithAnimation();

                        finish();

                    }
                });

        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

    	getMenuInflater().inflate(R.menu.main_menu, menu);


        ActionItemBadge.update(this, menu.findItem(R.id.menu_filtre_recherche),
                ContextCompat.getDrawable(this, R.drawable.ic_search_white_24dp) , ActionItemBadge.BadgeStyles.RED, Integer.MIN_VALUE);

    	ActionItemBadge.update(this, menu.findItem(R.id.menu_notifications),
                ContextCompat.getDrawable(this, R.drawable.ic_notifications_white_24dp), ActionItemBadge.BadgeStyles.PURPLE, numberNotifications);
        
    	ActionItemBadge.update(this, menu.findItem(R.id.menu_messagerie),
                ContextCompat.getDrawable(this, R.drawable.ic_email_white_24dp), ActionItemBadge.BadgeStyles.RED, numberMessage);

    	
    	return super.onCreateOptionsMenu(menu);
    }
    
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	Intent intent = null;
    	
    	switch (item.getItemId())
    	{

			case R.id.menu_notifications:

                Answers.getInstance().logCustom(new CustomEvent("Toolbar")
                        .putCustomAttribute("Click", "Notification"));
				
				intent = new Intent(this, NotificationActivity.class);
				startActivity(intent);
				
				break;
				
			case R.id.menu_messagerie:

                Answers.getInstance().logCustom(new CustomEvent("Toolbar")
                        .putCustomAttribute("Click", "Messagerie"));
				
				intent = new Intent(this, ListConversationActivity.class);
				startActivity(intent);
				
				break;

            case R.id.menu_filtre_recherche:

                Answers.getInstance().logCustom(new CustomEvent("Toolbar")
                        .putCustomAttribute("Click", "Filtre de recherche"));

                intent = new Intent(this, FiltreItemActivity.class);
                startActivityForResult(intent, 1);

/*/                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Filtre de Recherche")
                        .setAction("Click")
                        .setLabel("Bouton pour accéder aux recherche")
                        .setValue(1)
                        .build());*/

                break;
				
		/*	case R.id.menu_favoris:

				intent = new Intent(this , MainFavorisActivity.class);
				startActivity(intent);

				break;
			*/
			default:
				break;
		}
    	
    	return super.onOptionsItemSelected(item);
    }

    @DebugLog
    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        Log.e(TAG, "onDrawerSlide" + slideOffset);
    }

    @DebugLog
    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @DebugLog
    @Override
    public void onDrawerClosed(View drawerView) {
        Log.e(TAG, "onDrawerClosed" + getCurrentSection().getTitle());


        if (getCurrentSection().getTitle().equals(getString(R.string.accueil)) && !mainPreferences.isTutoGuide(TAG)) {

            try {
                new ShowcaseView.Builder(this)
                        .withMaterialShowcase()
                        .setTarget(new ToolbarActionItemTarget(getToolbar(), R.id.menu_filtre_recherche))
                        .setStyle(R.style.CustomShowcaseTheme4)
                        .setContentTitle("Filtres de recherche")
                        .setContentText("Trouve les meilleurs résumés en utilisant les filtres !")
                        .singleShot(1)
                        .build();
            } catch (NullPointerException e) {

            }


            mainPreferences.setTutoGuide(TAG, true);

        }





      /*  if (!mainPreferences.getShowCaseMain()) {
            try {
                new ShowcaseView.Builder(this)
                        .setContentTitle(getString(R.string.titre_showcaseview_main_activity))
                        .setContentText(getString(R.string.content_showcaseview_main_activity))
                        .setStyle(R.style.SampleTheme)
                        .setTarget(target)
                        .hideOnTouchOutside()
                        .build().hideButton();
            } catch (NoClassDefFoundError e) {
                e.printStackTrace();
            }
            catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        mainPreferences.setShowCaseMain(true);*/
    }

    @DebugLog
    @Override
    public void onDrawerStateChanged(int newState) {
        Log.e(TAG, "onDrawerStateChanged" + newState);

    }

    @Override
    public void showPopupVersionOutdated() {

        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                .setContentText(activity.getString(R.string.version_outdated))
                .setTitleText(activity.getString(R.string.app_name))
                .setConfirmText(activity.getString(R.string.ok))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        sweetAlertDialog.dismiss();

                        String appPackageName2 = getPackageName();
                        Intent marketIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName2));
                        marketIntent2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        startActivity(marketIntent2);
                        finish();

                    }
                });

        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.user_cover:

             //   if (!deviceSupportMultiPane())
            //        this.layout.closeDrawer(drawer);

                break;

            case R.id.user_photo:

                Intent intent = new Intent(activity, ShowProfileActivity.class);
                intent.putExtra("id_membre", mainPreferences.getIdUser()); // getIdUser...
                startActivity(intent);

                break;
        }
    }

    @DebugLog
    @Override
    protected void onActivityResult(int arg0, int resultCode, Intent arg2) {
    	super.onActivityResult(arg0, resultCode, arg2);
    	
    	if (resultCode == RESULT_OK) {
            Log.e(TAG, "onActivityResult");
            EventBus.getDefault().post(new FilterEvent(""));
    		//finish();
    		//startActivity(getIntent());
    	}

    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerMainComponent.builder()
                .appComponent(appComponent)
                .mainModule(new MainModule(this))
                .activityModule(new ActivityModule(this))
                .dialogModule(new DialogModule())
                .build()
                .inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
	public void setCountMessages(int countMessage) {
        if (countMessage != 0)
		    this.numberMessage = countMessage;
        else
            this.numberMessage = Integer.MIN_VALUE;
		invalidateOptionsMenu();
	}

	@Override
	public void setCountNotifications(int countNotification) {
        if (countNotification != 0)
	    	this.numberNotifications = countNotification;
        else
            this.numberNotifications = Integer.MIN_VALUE;
		invalidateOptionsMenu();
	}


}
