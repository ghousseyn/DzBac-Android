package com.squalala.dzbac.ui.show_item;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.DjihtiApp;
import com.squalala.dzbac.R;
import com.squalala.dzbac.data.api.ApiResponse.Item;
import com.squalala.dzbac.eventbus.events.DownloadEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import bolts.AppLinks;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import hugo.weaving.DebugLog;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Sequence;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ShowItemActivity.java
 * Date : 24 juil. 2014
 * 
 */
public class ShowAnnonceActivity extends ShowItemActivity {
	

    private final static String TAG = ShowAnnonceActivity.class.getSimpleName();
    private Animation mEnterAnimation, mExitAnimation;
    public TourGuide mTutorialHandler;
    private AdView mAdView;

    private InterstitialAd mInterstitialAd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        DjihtiApp.tracker.setScreenName(getClass().getName());

        //TODO des test ici

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();

        if (data != null)
            System.out.println(data.toString());

        Uri targetUrl =
                AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        if (targetUrl != null) {
            Log.e("Activity", "App Link Target URL: " + targetUrl.toString());
        }

        mAdView = (AdView) findViewById(R.id.adView);
        mAdView.setVisibility(View.GONE);
	}

    private void loadInterstitial() {

        if (isTopPost || mainPreferences.counterForAdInter())
        {
            if (!mainPreferences.isPrenium() || mainPreferences.isInTestPeriode())
            {
                // Create the InterstitialAd and set the adUnitId.
                mInterstitialAd = new InterstitialAd(this);
                // Defined in res/values/strings.xml
                mInterstitialAd.setAdUnitId(getString(R.string.intersial_ad_unit_id_show_image));

                if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
                    AdRequest adRequestInt = new AdRequest.Builder()
                            .addTestDevice("56D45AC8398DAB57396A0DE171C72AD4")
                            .addTestDevice("9CBA49B81F3038A5C9153862B9E76EC3")
                            .addTestDevice("28642802442164DDBD2B384C3A86AEA9")
                            .addTestDevice("C55C7CD1895D81774E227F184E6DD310")
                            .build();
                    mInterstitialAd.loadAd(adRequestInt);

                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            super.onAdLoaded();
                            showInterstitial();
                        }
                    });
                }
            }
        }

    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            // Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showErrorLoading() {
        Toast.makeText(ShowAnnonceActivity.this, getString(R.string.error_loading), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.enter, R.anim.exit);
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        Crouton.cancelAllCroutons();
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }


    @DebugLog
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        id_item = intent.getExtras().getString("id_item");

        isNotification = intent.getBooleanExtra("is_notification", false);

        Intent i = new Intent(this, ShowAnnonceActivity.class);
        i.putExtra("id_item", id_item);
       // i.putExtra("is_notification",  isNotification);

        startActivity(i);

  //      showItemPresenter.onNewIntent(id_item);
    }

    @Override
    public void cleanView() {

    }

    @Override
	public void displayItem(Item item) {
		
		annonceShowCard.setPost(item);
		cardView_info.setCard(annonceShowCard);

        annonceShowCard.notifyDataSetChanged();


        if (item != null) {

            if (!mainPreferences.isTutoGuide(TAG))
            {
                mEnterAnimation = new AlphaAnimation(0f, 1f);
                mEnterAnimation.setDuration(600);
                mEnterAnimation.setFillAfter(true);

                mExitAnimation = new AlphaAnimation(1f, 0f);
                mExitAnimation.setDuration(600);
                mExitAnimation.setFillAfter(true);

                TourGuide tourGuide1 = TourGuide.init(this)
                            /*.setOverlay(new Overlay()
                                .setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mTutorialHandler.cleanUp();
                                        mTutorialHandler.next();
                                    }
                                }))*/
                        .setToolTip(new ToolTip()
                                        .setTitle("Donne ton avis !")
                                        .setDescription("Tu peux aimer un post, cela aidera les autres à savoir si il est intéressant !")
                                        .setGravity(Gravity.TOP)


                        )
                                // note that there is not Overlay here, so the default one will be used
                        .playLater(fontLike);

                TourGuide tourGuide2 = TourGuide.init(this)
                   /* .setOverlay(new Overlay()
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                  //  mTutorialHandler.next();
                                    mTutorialHandler.cleanUp();

                                }
                            }))*/
                        .setToolTip(new ToolTip()
                                        .setTitle("Commente !")
                                        .setDescription("Tu peux aussi remercier la personne ou l'aider !")
                                        .setGravity(Gravity.TOP)

                        )
                                // note that there is not Overlay here, so the default one will be used
                        .playLater(fontComment);


                Sequence sequence = new Sequence.SequenceBuilder()
                        .add(tourGuide1, tourGuide2)
                        .setDefaultOverlay(new Overlay()
                                        .setEnterAnimation(mEnterAnimation)
                                        .setExitAnimation(mExitAnimation)
                                        .setBackgroundColor(Color.parseColor("#EE2c3e50"))
                        )
                                //       .setDefaultPointer(new Pointer())
                        .setDefaultPointer(null)
                        .setContinueMethod(Sequence.ContinueMethod.Overlay)
                        .build();


                mTutorialHandler = TourGuide.init(this).playInSequence(sequence);

                mainPreferences.setTutoGuide(TAG, true);
            }


        }

		super.displayItem(item);


        loadInterstitial();
	}

    @Override
    protected void setupComponent(AppComponent appComponent) {
        super.setupComponent(appComponent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onEvent(DownloadEvent event) {
        Log.e(TAG, "onEvent Download");

        if (event.isFinish())
            annonceShowCard.showDownloadButton();

    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
}
