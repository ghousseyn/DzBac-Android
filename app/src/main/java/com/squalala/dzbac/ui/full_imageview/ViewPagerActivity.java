package com.squalala.dzbac.ui.full_imageview;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.DjihtiApp;
import com.squalala.dzbac.R;
import com.squalala.dzbac.common.DjihtiBaseActivity;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.hacks.HackyViewPager;
import com.squalala.dzbac.service.DownloadImageService;
import com.squalala.dzbac.ui.payement.PayementActivity;
import com.squalala.dzbac.ui.showcaseview.ToolbarActionItemTarget;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import hugo.weaving.DebugLog;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by Back Packer
 * Date : 25/04/15
 */
public class ViewPagerActivity extends DjihtiBaseActivity {


    private static final String ISLOCKED_ARG = "isLocked";

    @InjectView(R.id.view_pager) HackyViewPager mViewPager;
    @InjectView(R.id.main_layout)
    View mLayout;

    private List<String> urlImages;

    private MenuItem menuLock;

    @InjectView(R.id.tool_bar)
    Toolbar mToolbar;

    MainPreferences preferences;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    private boolean isDestroyed;

    private String pathImages;

    private static final String TAG = ViewPagerActivity.class.getSimpleName();

    private boolean isAvatar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        DjihtiApp.tracker.setScreenName(getClass().getName());
        setContentView(R.layout.activity_view_pager_images);

        // mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
    //    setContentView(mViewPager);

        ButterKnife.inject(this);

        pathImages = getIntent().getStringExtra("path_images");

        if (pathImages == null)
            pathImages = "others";

        preferences = new MainPreferences(this);


     //   if (!preferences.isPrenium())
       //     getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setBackgroundColor(Color.TRANSPARENT);

        // Position sur laquelle l'utilisateur a cliqué.
        int position = getIntent().getExtras().getInt("position");


        isAvatar = getIntent().getExtras().getBoolean("avatar", false);
       // Log.e(TAG, urlImages + "");

        try {
            urlImages = Arrays.asList(getIntent().getExtras()
                    .getString("url_to_split")
                    .replaceAll("medium/", "")
                    .replaceAll("thumbnail/", "")
                    .replaceAll("thumbnail", "")
                    .replaceAll("medium", "")
                    .split(","));

            System.out.println(urlImages);
        } catch (NullPointerException e) {
            finish();
        }

        if (urlImages.size() > 1)
            getSupportActionBar().setTitle( (position + 1) + "/" + urlImages.size());

        mViewPager.setAdapter(new SamplePagerAdapter());
        mViewPager.setCurrentItem(position);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);

        if (savedInstanceState != null) {
            boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
            ((HackyViewPager) mViewPager).setLocked(isLocked);
        }


        mAdView = (AdView) findViewById(R.id.adView);
    //    mAdView.setVisibility(View.GONE);

        if (!preferences.isPrenium() || preferences.isInTestPeriode())
        {

         //   Appodeal.show(this, Appodeal.BANNER_BOTTOM);

            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice("56D45AC8398DAB57396A0DE171C72AD4")
                    .addTestDevice("9CBA49B81F3038A5C9153862B9E76EC3")
                    .addTestDevice("28642802442164DDBD2B384C3A86AEA9")
                    .build();
            mAdView.loadAd(adRequest);

          /*  // Create the InterstitialAd and set the adUnitId.
            mInterstitialAd = new InterstitialAd(this);
            // Defined in res/values/strings.xml
            mInterstitialAd.setAdUnitId(getString(R.string.intersial_ad_unit_id_show_image));

            if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
                AdRequest adRequestInt = new AdRequest.Builder()
                        .addTestDevice("56D45AC8398DAB57396A0DE171C72AD4")
                        .addTestDevice("9CBA49B81F3038A5C9153862B9E76EC3")
                        .addTestDevice("28642802442164DDBD2B384C3A86AEA9")
                        .build();
                mInterstitialAd.loadAd(adRequestInt);

                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        showInterstitial();
                    }
                });
            }*/
        }
        else
            mAdView.setVisibility(View.GONE);

    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            getSupportActionBar().setTitle((position + 1) + "/" + urlImages.size());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
           // Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
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

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }



    /** Called before the activity is destroyed */
    @DebugLog
    @Override
    public void onDestroy() {

        isDestroyed = true;

        if (mAdView != null) {
            mAdView.destroy();
        }

     //   mViewPager.removeOnPageChangeListener(mOnPageChangeListener);
      //  mViewPager.removeAllViews();
      //  Picasso.with(this).shutdown();

        super.onDestroy();

    }

    @Override
    protected void setupComponent(AppComponent appComponent) {

    }

    class SamplePagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return urlImages.size();
        }

        @DebugLog
        @Override
        public View instantiateItem(ViewGroup container, int position) {
            Log.e(TAG, "instantiateItem");

            PhotoView photoView = new PhotoView(container.getContext());
            // Now just add PhotoView to ViewPager and return it
           container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

           /* if (urlImages.get(position).toString().endsWith(".gif")) {
                Glide.with(ViewPagerActivity.this)
                        .load(urlImages.get(position).toString())
                        .asGif()
                        .into(photoView);
            }
            else {
                Glide.with(ViewPagerActivity.this)
                        .load(urlImages.get(position).toString())
                        .fitCenter()
                        .into(photoView);
            }*/
        /*    Glide.with(ViewPagerActivity.this)
                    .load(urlImages.get(position).toString())
                    .fitCenter()
                    .into(photoView);
*/
            Picasso.with(container.getContext())
                    .load(urlImages.get(position).toString())
                    .fit().centerInside()
                    .into(photoView);


            return photoView;
        }

        @DebugLog
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
     //   getMenuInflater().inflate(R.menu.viewpager_menu, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_full_imageview, menu);

        if (isAvatar) {
            menu.findItem(R.id.menu_save_all_images).setVisible(false);
            menu.findItem(R.id.menu_save_image).setVisible(false);
        }


        MainPreferences preferences = new MainPreferences(this);

        if (!preferences.isTutoGuide(TAG)) {
            new ShowcaseView.Builder(this)
                    .withMaterialShowcase()
                    .setTarget(new ToolbarActionItemTarget(mToolbar, R.id.menu_save_image))
                    .setStyle(R.style.CustomShowcaseTheme4)
                    .setContentTitle("Télécharge !")
                    .setContentText("Tu peux sauvegarder les images qui te plaisent, et pour les avoir d'un seul coup clique sur l'icône à droite !")
                    .build()
                    .show();


            preferences.setTutoGuide(TAG, true);
        }



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menuLock = menu.findItem(R.id.menu_image_operation);
       /* menuLockItem = menu.findItem(R.id.menu_lock);
        toggleLockBtnTitle();
        menuLockItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                toggleViewPagerScrolling();
                toggleLockBtnTitle();
                return true;
            }
        });
*/

        return super.onPrepareOptionsMenu(menu);
    }


    private void downloadImage() {
    //    if (preferences.isPrenium())
    //    {
        if (preferences.getPremiumCode() == 4 ||
                preferences.getPremiumCode() == 5 ||
                preferences.getPremiumCode() == 6) {
            Answers.getInstance().logCustom(new CustomEvent("Download")
                    .putCustomAttribute("Click", "One image"));

            Picasso.with(this)
                    // .load(urlImages.get(sliderLayout.getCurrentPosition()))
                    .load(urlImages.get(mViewPager.getCurrentItem()))
                    .into(target);

            Toast.makeText(this, getString(R.string.save_image_load),
                    Toast.LENGTH_SHORT).show();
        }
        else if (preferences.getLevelContribution() < 2) {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(this.getString(R.string.oups))
                    .setContentText(this.getString(R.string.alert_legend_message_contrib_error))
                    .setCancelText(getString(R.string.ok))
                    .setConfirmText(getString(R.string.en_savoir_plus))
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            startActivity(new Intent(ViewPagerActivity.this, PayementActivity.class));
                        }
                    })
                    .show();
        }
        else {
            Answers.getInstance().logCustom(new CustomEvent("Download")
                    .putCustomAttribute("Click", "One image"));

            Picasso.with(this)
                    // .load(urlImages.get(sliderLayout.getCurrentPosition()))
                    .load(urlImages.get(mViewPager.getCurrentItem()))
                    .into(target);

            Toast.makeText(this, getString(R.string.save_image_load),
                    Toast.LENGTH_SHORT).show();
        }

    //    }
   //     else
     //       startActivity(new Intent(this, PayementActivity.class));
        // DialogUtils.showPopup(this, getString(R.string.compte_prenium), getString(R.string.error_prenimum));
    }


    private void downloadAllaImages() {
    //    if (preferences.isPrenium())
      //  {
        if (preferences.getPremiumCode() == 4 ||
            preferences.getPremiumCode() == 5 ||
            preferences.getPremiumCode() == 6) {
            Answers.getInstance().logCustom(new CustomEvent("Download")
                    .putCustomAttribute("Click", "All images"));

            ArrayList<String> urls = new ArrayList<String>();
            urls.addAll(urlImages);

            Intent i= new Intent(this, DownloadImageService.class);
            i.putExtra("url_images_download", urls);
            i.putExtra("path", pathImages);
            startService(i);
        }
        else if (preferences.getLevelContribution() < 2) {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(this.getString(R.string.oups))
                    .setContentText(this.getString(R.string.alert_legend_message_contrib_error))
                    .setCancelText(getString(R.string.ok))
                    .setConfirmText(getString(R.string.en_savoir_plus))
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            startActivity(new Intent(ViewPagerActivity.this, PayementActivity.class));
                        }
                    })
                    .show();
        }
        else {
            Answers.getInstance().logCustom(new CustomEvent("Download")
                    .putCustomAttribute("Click", "All images"));

            ArrayList<String> urls = new ArrayList<String>();
            urls.addAll(urlImages);

            Intent i= new Intent(this, DownloadImageService.class);
            i.putExtra("url_images_download", urls);
            i.putExtra("path", pathImages);
            startService(i);
        }

     //   }
     /*   else {
            startActivity(new Intent(this, PayementActivity.class));
        }*/
        // DialogUtils.showPopup(this, getString(R.string.compte_prenium), getString(R.string.error_prenimum));
    }

    final PermissionCallback permissionImageCallback = new PermissionCallback() {
        @Override
        public void permissionGranted() {
            //boolean hasAccess = Tools.accessContacts(MainActivity.this);
            //   Toast.makeText(MainActivity.this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();
            downloadImage();
        }

        @Override
        public void permissionRefused() {
            //  boolean hasAccess = Tools.accessContacts(MainActivity.this);
            // Toast.makeText(MainActivity.this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();
        }
    };

    final PermissionCallback permissionAllIamgesCallback = new PermissionCallback() {
        @Override
        public void permissionGranted() {
            //boolean hasAccess = Tools.accessContacts(MainActivity.this);
            //   Toast.makeText(MainActivity.this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();
            downloadAllaImages();
        }

        @Override
        public void permissionRefused() {
            //  boolean hasAccess = Tools.accessContacts(MainActivity.this);
            // Toast.makeText(MainActivity.this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();
        }
    };
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {

            case R.id.menu_image_operation:

                toggleViewPagerScrolling();
                toggleLockBtnTitle();


                break;

            case R.id.menu_save_all_images:

                if(Nammu.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //We have a permission, easy peasy
                    //  boolean hasAccess = Tools.accessContacts(this);
                    // Toast.makeText(this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();
                   downloadAllaImages();
                } else {
                    //We do not own this permission
                    if (Nammu.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        //User already refused to give us this permission or removed it
                        //Now he/she can mark "never ask again" (sic!)
                        Snackbar.make(mLayout, getString(R.string.permission_photo_enregistrement),
                                Snackbar.LENGTH_INDEFINITE)
                                .setAction(getString(R.string.ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Nammu.askForPermission(ViewPagerActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, permissionAllIamgesCallback);
                                    }
                                }).show();
                    } else {
                        //First time asking for permission
                        // or phone doesn't offer permission
                        // or user marked "never ask again"
                        Nammu.askForPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, permissionAllIamgesCallback);
                    }
                }



                break;

            case android.R.id.home:

                finish();

                return true;

            case R.id.menu_cancel:

                finish();

                break;

            case R.id.menu_save_image:

                if(Nammu.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //We have a permission, easy peasy
                    //  boolean hasAccess = Tools.accessContacts(this);
                    // Toast.makeText(this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();
                    downloadImage();
                } else {
                    //We do not own this permission
                    if (Nammu.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        //User already refused to give us this permission or removed it
                        //Now he/she can mark "never ask again" (sic!)
                        Snackbar.make(mLayout, getString(R.string.permission_photo_enregistrement),
                                Snackbar.LENGTH_INDEFINITE)
                                .setAction(getString(R.string.ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Nammu.askForPermission(ViewPagerActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, permissionImageCallback);
                                    }
                                }).show();
                    } else {
                        //First time asking for permission
                        // or phone doesn't offer permission
                        // or user marked "never ask again"
                        Nammu.askForPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, permissionImageCallback);
                    }
                }

                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private Target target = new Target() {

        @Override
        public void onPrepareLoad(Drawable arg0) {
        }

        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom arg1) {

            Log.e(TAG, "onBitmapLoaded");

            new Thread(new Runnable() {
                @Override
                public void run() {

                    Date date = new Date();

                    String path = Environment.getExternalStorageDirectory() + "/DzBac/";

                    if (pathImages != null) {
                        path += pathImages + "/";
                    }

                    /*
                     * Pour vérifier que le dossier "DzBac" existe bien
                     */
                    File f = new File(path);

                    if (!f.exists()) {
                        f.mkdirs();
                    }

                    File file = new File(path + date.getTime() + ".jpg");

                    Log.e(TAG, "Path --> " + file.getAbsolutePath());
                    try
                    {
                        file.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                        ostream.close();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            Uri contentUri = Uri.fromFile(file);
                            mediaScanIntent.setData(contentUri);
                            sendBroadcast(mediaScanIntent);
                        } else {
                            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/" + "DzBac")));
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }).start();

            Crouton.makeText(ViewPagerActivity.this, getString(R.string.save_image_load_success),
                    Style.CONFIRM).show();
        }

        @Override
        public void onBitmapFailed(Drawable arg0) {
            Log.e(TAG, "onBitmapFailed");
            Crouton.makeText(ViewPagerActivity.this, getString(R.string.save_image_load_fail),
                    Style.ALERT).show();
        }
    };

    private void toggleViewPagerScrolling() {
        if (isViewPagerActive()) {
            ((HackyViewPager) mViewPager).toggleLock();
        }
    }

    private void toggleLockBtnTitle() {
        boolean isLocked = false;
        if (isViewPagerActive()) {
            isLocked = ((HackyViewPager) mViewPager).isLocked();
        }

        String title;

        if (isLocked)
            title = getString(R.string.debloquer_image);
        else
            title = getString(R.string.bloquer_image);


        if (menuLock != null) {
            menuLock.setTitle(title);
        }
    }


    private boolean isViewPagerActive() {
        return (mViewPager != null && mViewPager instanceof HackyViewPager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (isViewPagerActive()) {
            outState.putBoolean(ISLOCKED_ARG, ((HackyViewPager) mViewPager).isLocked());
        }
        super.onSaveInstanceState(outState);
    }

}