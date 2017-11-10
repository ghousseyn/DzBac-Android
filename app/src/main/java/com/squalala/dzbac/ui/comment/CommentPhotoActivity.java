package com.squalala.dzbac.ui.comment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squalala.dzbac.ActivityModule;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.R;
import com.squalala.dzbac.common.DjihtiBaseActivity;
import com.squalala.dzbac.DjihtiConstant;
import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.ui.alert_dialog.AstuceMessage;
import com.squalala.dzbac.ui.alert_dialog.PhotoManagerAlert;
import com.squalala.dzbac.ui.cards.CardModule;
import com.squalala.dzbac.ui.cards.CommentAddInfoCard;
import com.squalala.dzbac.ui.cards.CommentShowCard;
import com.squalala.dzbac.ui.show_item.ShowItemView;
import com.squalala.dzbac.utils.DpToPixel;
import com.squalala.dzbac.utils.FileUtils;
import com.squalala.dzbac.utils.UploadHelper;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import info.hoang8f.widget.FButton;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardView;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

/**
 * Created by brio on 26/01/15.
 */
public class CommentPhotoActivity extends DjihtiBaseActivity
    implements CommentaryView, View.OnClickListener, ShowItemView
    {
        @InjectView(R.id.card_info_add_item) CardView card_info;
        @InjectView(R.id.linear_container_photos) LinearLayout containerPhotos;
        @InjectView(R.id.photo_item_add)
        FontAwesomeText photoItemAdd;
        @InjectView(R.id.camera_item_add)
        FontAwesomeText cameraItemAdd;
        @InjectView(R.id.file_item_add)
        FontAwesomeText fileAdd;
        @InjectView(R.id.audio_item_add)
        FontAwesomeText audioItemAdd;
        @InjectView(R.id.photo_item_1) ImageView photoItem1;
        @InjectView(R.id.photo_item_2) ImageView photoItem2;
        @InjectView(R.id.photo_item_3) ImageView photoItem3;
        @InjectView(R.id.photo_item_4) ImageView photoItem4;
        @InjectView(R.id.photo_item_5) ImageView photoItem5;
        @InjectView(R.id.photo_item_6) ImageView photoItem6;
        @InjectView(R.id.photo_item_7) ImageView photoItem7;
        @InjectView(R.id.photo_item_8) ImageView photoItem8;
        @InjectView(R.id.photo_item_9) ImageView photoItem9;
        @InjectView(R.id.photo_item_10) ImageView photoItem10;
        @InjectView(R.id.photo_item_11) ImageView photoItem11;
        @InjectView(R.id.photo_item_12) ImageView photoItem12;
        @InjectView(R.id.photo_item_13) ImageView photoItem13;
        @InjectView(R.id.photo_item_14) ImageView photoItem14;
        @InjectView(R.id.photo_item_15) ImageView photoItem15;
        @InjectView(R.id.photo_item_16) ImageView photoItem16;
        @InjectView(R.id.photo_item_17) ImageView photoItem17;
        @InjectView(R.id.photo_item_18) ImageView photoItem18;
        @InjectView(R.id.photo_item_19) ImageView photoItem19;
        @InjectView(R.id.photo_item_20) ImageView photoItem20;
        @InjectView(R.id.photo_item_21) ImageView photoItem21;
        @InjectView(R.id.photo_item_22) ImageView photoItem22;
        @InjectView(R.id.photo_item_23) ImageView photoItem23;
        @InjectView(R.id.photo_item_24) ImageView photoItem24;
        @InjectView(R.id.photo_item_25) ImageView photoItem25;
        @InjectView(R.id.photo_item_26) ImageView photoItem26;
        @InjectView(R.id.photo_item_27) ImageView photoItem27;
        @InjectView(R.id.photo_item_28) ImageView photoItem28;
        @InjectView(R.id.photo_item_29) ImageView photoItem29;
        @InjectView(R.id.photo_item_30) ImageView photoItem30;

        @InjectView(R.id.main_layout)
        View mLayout;


        @InjectView(R.id.btn_depose_item) FButton btnDeposeItem;

        @InjectView(R.id.smooth_add_item)
        SmoothProgressBar smoothProgressBarUpload;

    //    @InjectView(R.id.smooth_progress_upload)
       // NumberProgressBar smoothProgressBarUpload;

        @InjectView(R.id.horizontal_scroll_container_photos) HorizontalScrollView horizontalScrollPhoto;

        @Inject
        CommentAddInfoCard commentAddInfoCard;
        @Inject
        CommentaryPresenter presenter;
        @Inject PhotoManagerAlert alManager;
        @Inject
        SweetAlertDialog pDialog;
    //    @Inject Tracker tracker;

        protected boolean imagesDisplay [], uploading [], isSubmited = false;

        protected String filenames[], urlImages[];

        protected String idComment, idContent;

        protected ImageView [] photoitems;
        protected int numberImages, limit_image = 30;
        private Uri[] uriImages;
        private static final int INTENT_REQUEST_GET_IMAGES = 13;
        private static final int INTENT_REQUEST_GET_N_IMAGES = 14;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_item);

        ButterKnife.inject(this);

        cameraItemAdd.setVisibility(View.GONE);
        audioItemAdd.setVisibility(View.GONE);
        fileAdd.setVisibility(View.GONE);
/*
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new
                ColorDrawable(Color.parseColor("#FF9955")));
*/
        card_info.setCard(commentAddInfoCard);

        idContent = getIntent().getExtras().getString("id_item");

        pDialog.show();

        photoitems = new ImageView[limit_image];
        photoitems[0] = photoItem1;
        photoitems[1] = photoItem2;
        photoitems[2] = photoItem3;
        photoitems[3] = photoItem4;
        photoitems[4] = photoItem5;
        photoitems[5] = photoItem6;
        photoitems[6] = photoItem7;
        photoitems[7] = photoItem8;
        photoitems[8] = photoItem9;
        photoitems[9] = photoItem10;
        photoitems[10] = photoItem11;
        photoitems[11] = photoItem12;
        photoitems[12] = photoItem13;
        photoitems[13] = photoItem14;
        photoitems[14] = photoItem15;
        photoitems[15] = photoItem16;
        photoitems[16] = photoItem17;
        photoitems[17] = photoItem18;
        photoitems[18] = photoItem19;
        photoitems[19] = photoItem20;
        photoitems[20] = photoItem21;
        photoitems[21] = photoItem22;
        photoitems[22] = photoItem23;
        photoitems[23] = photoItem24;
        photoitems[24] = photoItem25;
        photoitems[25] = photoItem26;
        photoitems[26] = photoItem27;
        photoitems[27] = photoItem28;
        photoitems[28] = photoItem29;
        photoitems[29] = photoItem30;

        photoItemAdd.setOnClickListener(this);
        btnDeposeItem.setOnClickListener(this);
        photoItem1.setOnClickListener(this);
        photoItem2.setOnClickListener(this);
        photoItem3.setOnClickListener(this);
        photoItem4.setOnClickListener(this);
        photoItem5.setOnClickListener(this);
        photoItem6.setOnClickListener(this);
        photoItem7.setOnClickListener(this);
        photoItem8.setOnClickListener(this);
        photoItem9.setOnClickListener(this);
        photoItem10.setOnClickListener(this);
        photoItem11.setOnClickListener(this);
        photoItem12.setOnClickListener(this);
        photoItem13.setOnClickListener(this);
        photoItem14.setOnClickListener(this);
        photoItem15.setOnClickListener(this);
        photoItem16.setOnClickListener(this);
        photoItem17.setOnClickListener(this);
        photoItem18.setOnClickListener(this);
        photoItem19.setOnClickListener(this);
        photoItem20.setOnClickListener(this);
        photoItem21.setOnClickListener(this);
        photoItem22.setOnClickListener(this);
        photoItem23.setOnClickListener(this);
        photoItem24.setOnClickListener(this);
        photoItem25.setOnClickListener(this);
        photoItem26.setOnClickListener(this);
        photoItem27.setOnClickListener(this);
        photoItem28.setOnClickListener(this);
        photoItem29.setOnClickListener(this);
        photoItem30.setOnClickListener(this);

        imagesDisplay = new boolean[limit_image];
        filenames = new String[limit_image];
        uploading = new boolean[limit_image];
        uriImages = new Uri[limit_image];
        urlImages = new String[limit_image];


        btnDeposeItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // On vérifie qu'il n'y a pas d'upload en cours
                boolean isUploading = false;

                for (int i = 0; i < uploading.length; i++) {
                    if (uploading[i]) {
                        isUploading = true;
                        break;
                    }
                }

                if (isUploading)
                    isSubmited = true;
                else
                    submit();
            }
        });


        presenter.initItem(idContent);

    }


        @Override
        public void addComment(Card card) {

        }

        @Override
        public void showProgressDialog() {
            pDialog.show();
        }

        @Override
        public void hideProgressDialog() {
            pDialog.hide();
        }

        @Override
        public void showMessage(String message) {

        }

        @Override
        public void showErrorLoading() {

        }

        @Override
        public void cleanView() {

        }

        @Override
        public void setIdItem(String idItem) {

        this.idComment = idItem;
        alManager.setImagesDisplay(imagesDisplay);
        alManager.setUploading(uploading);
        alManager.setUrlImages(urlImages);
        alManager.setIdItem(idItem);
        alManager.setFilesnames(filenames);
        alManager.setHorizontalScroll(horizontalScrollPhoto);
        alManager.setPresenter(presenter);

        pDialog.hide();
    }

    //protected abstract void submit();

    private void scrollRight() {

        // Afin de scroller jusqu'au bout quand on rajoute un élément
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                horizontalScrollPhoto.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        }, 100L);

    }

        @Override
        public void exit() {

        }

        /* @Override
    protected List<Object> getModules() {
        return  Arrays.<Object>asList(new CardModule(this),
                new CommentaryModule(this, this),
                new ShowItemModule(this));
    }*/

        @Override
        protected void setupComponent(AppComponent appComponent) {
            DaggerCommentaryPhotoComponent.builder()
                    .appComponent(appComponent)
                    .commentaryModule(new CommentaryModule(this))
                 //   .showItemModule(new ShowItemModule(this))
                    .cardModule(new CardModule(this))
                    .activityModule(new ActivityModule(this))
                    .build()
                    .inject(this);
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == INTENT_REQUEST_GET_IMAGES || requestCode == INTENT_REQUEST_GET_N_IMAGES) {
                ArrayList<String> photos;


                if (data != null) {
                    photos =
                            data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);


                    for(int i = 0; i < photos.size(); i++)
                    {
                        for (int j = 0; j < limit_image; j++)
                        {
                            if (!imagesDisplay[j])
                            {
                                Uri uri = Uri.fromFile(new File(photos.get(i)));


                                imagesDisplay[j] = true;
                                uriImages[j] = uri;
                                photoitems[j].setVisibility(View.VISIBLE);

                                int px = DpToPixel.getPixel(100, this);

                                File file = new File(uri.getPath());

                                Glide.with(this)
                                        .load(file)
                                        .centerCrop()
                                        .override(px, px)
                                        .into(photoitems[j]);


                                scrollRight();

                                // smoothProgressBar.setVisibility(View.VISIBLE);
                                smoothProgressBarUpload.setVisibility(View.VISIBLE);

                                new UploadTask().execute(j);

                                displayPhotoItemAdd();

                                break;
                            }
                        }
                    }

                }
            }
        }


    }

    private void displayPhotoItemAdd() {

        boolean isPhotoItemAddCanVisible = false;

        for (int j = 0; j < limit_image; j++) {
            if (!imagesDisplay[j]) {
                isPhotoItemAddCanVisible = true;
                break;
            }
        }

        if (isPhotoItemAddCanVisible)
            photoItemAdd.setVisibility(View.VISIBLE);
        else
            photoItemAdd.setVisibility(View.GONE);

    }

    private class UploadTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {

            //	Log.e("BEGIN" ,"################ UPLOAD CHECK ################## " );
            for (int i = 0; i < filenames.length; i++) {

                // Il y a un fichier à upload
                if (uriImages[i] != null && !uploading[i]) {

                    boolean isOk = true;

                    // On vérifie qu'il n'y a pas d'upload avant d'essayer d'en lancer un autre
                    for(int j = 0; j < i; j++) {

                        //		Log.e("UPLOAD STATE", uploading[j]+"");

                        // Cela veut dire qu'il y a des déjà un upload en cours
                        if (uriImages[j] != null && uploading[j]) {

                            isOk = false;
                            //	Log.e("INTENT NON NULL", " POSITION --> " + j);
                            Log.e("UPLOAD STATE OK", isOk + "");

                            break;
                        }
                    }

                    if (isOk) {
                        Log.e("BEGIN" ,"################ UPLOAD TASK ################## ");
                        uploading[i] = true;
                        presenter.uploadPhotoItem( idComment /*idItem*/,
                                i,
                                UploadHelper.getTypedFileWithId(getApplicationContext(),
                                UploadHelper.getRealPathFromURI(getApplicationContext(), uriImages[i]), idComment));
                    }

                    break;
                }

            }

            return null;
        }

    }

    private void takePicture(int requestCode) {

        PhotoPickerIntent intent = new PhotoPickerIntent(CommentPhotoActivity.this);
        intent.setPhotoCount( getNumberImagesAvalaible());
        intent.setShowCamera(true);
        intent.setShowGif(true);
        startActivityForResult(intent, INTENT_REQUEST_GET_N_IMAGES);
    }

    private int getNumberImagesAvalaible() {

        int count = 0;

        for (int i = 0; i < limit_image; i++)  {
            if (!imagesDisplay[i])
                count++;
        }

        return count;
    }


    private void addImages() {
        for (int i = 0; i < limit_image; i++) {
            if (!imagesDisplay[i]) {
                takePicture(i);
                break;
            }
        }
    }

        final PermissionCallback permissionPhotoCallback = new PermissionCallback() {
            @Override
            public void permissionGranted() {
                //boolean hasAccess = Tools.accessContacts(MainActivity.this);
                //   Toast.makeText(MainActivity.this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();
                addImages();
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

            case R.id.photo_item_add:


                if(Nammu.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    //We have a permission, easy peasy
                    //  boolean hasAccess = Tools.accessContacts(this);
                    // Toast.makeText(this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();
                    addImages();
                } else {
                    //We do not own this permission
                    if (Nammu.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        //User already refused to give us this permission or removed it
                        //Now he/she can mark "never ask again" (sic!)
                        Snackbar.make(mLayout, getString(R.string.permission_photo),
                                Snackbar.LENGTH_INDEFINITE)
                                .setAction(getString(R.string.ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Nammu.askForPermission(CommentPhotoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE, permissionPhotoCallback);
                                    }
                                }).show();
                    } else {
                        //First time asking for permission
                        // or phone doesn't offer permission
                        // or user marked "never ask again"
                        Nammu.askForPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, permissionPhotoCallback);
                    }
                }

                break;

            case R.id.photo_item_1:

                alManager.buildDialog(photoItem1, photoItemAdd, 0,  this).show();

                break;

            case R.id.photo_item_2:

                alManager.buildDialog(photoItem2, photoItemAdd, 1,  this).show();

                break;

            case R.id.photo_item_3:

                alManager.buildDialog(photoItem3, photoItemAdd, 2,  this).show();

                break;

            case R.id.photo_item_4:

                alManager.buildDialog(photoItem4, photoItemAdd, 3,  this).show();

                break;

            case R.id.photo_item_5:

                alManager.buildDialog(photoItem5, photoItemAdd, 4,  this).show();

                break;

            case R.id.photo_item_6:

                alManager.buildDialog(photoItem6, photoItemAdd, 5,  this).show();

                break;

            case R.id.photo_item_7:

                alManager.buildDialog(photoItem7, photoItemAdd, 6,  this).show();

                break;

            case R.id.photo_item_8:

                alManager.buildDialog(photoItem8, photoItemAdd, 7,  this).show();

                break;

            case R.id.photo_item_9:

                alManager.buildDialog(photoItem9, photoItemAdd, 8,  this).show();

                break;

            case R.id.photo_item_10:

                alManager.buildDialog(photoItem10, photoItemAdd, 9,  this).show();

                break;

            case R.id.photo_item_11:

                alManager.buildDialog(photoItem11, photoItemAdd, 10,  this).show();

                break;

            case R.id.photo_item_12:

                alManager.buildDialog(photoItem12, photoItemAdd, 11,  this).show();

                break;

            case R.id.photo_item_13:

                alManager.buildDialog(photoItem13, photoItemAdd, 12,  this).show();

                break;

            case R.id.photo_item_14:

                alManager.buildDialog(photoItem14, photoItemAdd, 13,  this).show();

                break;

            case R.id.photo_item_15:

                alManager.buildDialog(photoItem15, photoItemAdd, 14,  this).show();

                break;

            case R.id.photo_item_16:

                alManager.buildDialog(photoItem16, photoItemAdd, 15,  this).show();

                break;

            case R.id.photo_item_17:

                alManager.buildDialog(photoItem17, photoItemAdd, 16,  this).show();

                break;

            case R.id.photo_item_18:

                alManager.buildDialog(photoItem18, photoItemAdd, 17,  this).show();

                break;

            case R.id.photo_item_19:

                alManager.buildDialog(photoItem19, photoItemAdd, 18,  this).show();

                break;

            case R.id.photo_item_20:

                alManager.buildDialog(photoItem20, photoItemAdd, 19,  this).show();

                break;

            case R.id.photo_item_21:

                alManager.buildDialog(photoItem21, photoItemAdd, 20,  this).show();

                break;

            case R.id.photo_item_22:

                alManager.buildDialog(photoItem22, photoItemAdd, 21,  this).show();

                break;

            case R.id.photo_item_23:

                alManager.buildDialog(photoItem23, photoItemAdd, 22,  this).show();

                break;

            case R.id.photo_item_24:

                alManager.buildDialog(photoItem24, photoItemAdd, 23,  this).show();

                break;

            case R.id.photo_item_25:

                alManager.buildDialog(photoItem25, photoItemAdd, 24,  this).show();

                break;

            case R.id.photo_item_26:

                alManager.buildDialog(photoItem26, photoItemAdd, 25,  this).show();

                break;

            case R.id.photo_item_27:

                alManager.buildDialog(photoItem27, photoItemAdd, 26,  this).show();

                break;

            case R.id.photo_item_28:

                alManager.buildDialog(photoItem28, photoItemAdd, 27,  this).show();

                break;

            case R.id.photo_item_29:

                alManager.buildDialog(photoItem29, photoItemAdd, 28,  this).show();

                break;

            case R.id.photo_item_30:

                alManager.buildDialog(photoItem30, photoItemAdd, 29,  this).show();

                break;

            default:
                break;
        }
    }

    @Override
    public void populateFilenames(int position, String filename) {

        FileUtils.cleanFilesTemp();

        filenames[position] = filename;
        uploading[position] = false;
        uriImages[position] = null;
        urlImages[position] = DjihtiConstant.PATH_IMAGES_COMMENTS + "/" + idComment + "/" +filename;

        YoYo.with(Techniques.Shake)
                .duration(700)
                .playOn(photoitems[position]);

        smoothProgressBarUpload.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);
        new UploadTask().execute();

        // Afin d'éviter le bug de la progressBar
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // Si il y a au moins un upload on laisse la progress bar
                boolean isUploading = true;

                for (int i = 0; i < uploading.length; i++) {
                    if (uploading[i]) {
                        isUploading = true;
                        break;
                    }
                    else
                        isUploading = false;
                }

                if (!isUploading) {
                    smoothProgressBarUpload.setVisibility(View.GONE);
                    setProgressBarIndeterminateVisibility(false);
                    // Aucune upload en cours et aucun fichier à upload, on vérifie si la personne a terminé
                    if (isSubmited)
                        submit();
                }
                else {
                    smoothProgressBarUpload.setVisibility(View.VISIBLE);
                    setProgressBarIndeterminateVisibility(true);
                }


               /* presenter.updateDataImages(idItem,
                        typeItem.toString(),
                        getUrImagePresentation());*/
            }
        }, 500);



    }


    private void submit() {
        presenter.sendComment(commentAddInfoCard.getDescription(), idContent, idComment);
    }

    @Override
    public void showMessageDelete(String message) {
        Crouton.makeText(this, message, Style.INFO).show();
        displayPhotoItemAdd();
       /* presenter.updateDataImages(idItem,
                typeItem.toString(),
                urlImagePresentation);*/
    }

    @Override
    protected void onDestroy() {
        Crouton.cancelAllCroutons();
        super.onDestroy();
    }

    protected void hydrateItem() {


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.menu_astuce:
                AstuceMessage.message(this, getString(R.string.alert_titre_astuce),
                        getString(R.string.alert_astuce_message)).show();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void navigateToBack(String message) {
        Crouton.makeText(this, message, Style.INFO).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (pDialog != null && pDialog.isShowing())
                    pDialog.hide();
                setResult(RESULT_OK);
                finish();
            }
        }, 1100);
    }

    @Override
    public void displayItem(ApiResponse.Item itemResponse) {

        setIdItem(idComment);

        for (int i = 0; i < itemResponse.url_images.size(); i++) {

            int index = itemResponse.url_images.get(i).toString().indexOf(idComment) + idComment.length();
            filenames[i] = itemResponse.url_images.get(i).toString().substring(index + 1);
            urlImages[i] = itemResponse.url_images.get(i).toString();

            System.out.println("---> " + filenames[i]);
            photoitems[i].setVisibility(View.VISIBLE);

            Glide.with(this)
                    .load(itemResponse.url_images.get(i).toString())
                    .fitCenter()
                    .placeholder(R.drawable.no_image_small)
                    .into(photoitems[i]);

            imagesDisplay[i] = true;
        }


        if (limit_image == itemResponse.url_images.size())
            photoItemAdd.setVisibility(View.GONE);

        pDialog.dismiss();
    }


    @Override
    public void setUploadError(int position, String message) {

        imagesDisplay[position] = false;
        uploading[position] = false;
        uriImages[position] = null;

        photoitems[position].setVisibility(View.GONE);

        Crouton.makeText(this, getString(R.string.error_upload_fail), Style.INFO).show();

        stateProgressBarCircle();
    }

    private void stateProgressBarCircle() {
        boolean isUploading = false;

        for (int i = 0; i < uploading.length; i++) {
            if (uploading[i]) {
                isUploading = true;
                break;
            }
            else
                isUploading = false;
        }

        if (!isUploading) {
            smoothProgressBarUpload.setVisibility(View.GONE);
        }
    }


    protected int getNumbersImages() {
        numberImages = 0;

        for (int i = 0; i < imagesDisplay.length; i++) {
            if (imagesDisplay[i])
                numberImages++;
        }

        return numberImages;
    }

    @Override
    public void showCommentaires(ArrayList<Card> cards) {

    }

    @Override
    public void showStateInGroup(boolean stateInGroup) {
    }


    @Override
    public void setErrorComment() {
        Crouton.makeText(this, getString(R.string.error_message_vide), Style.ALERT).show();
    }

        @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void setCommentDeleted(String message, CommentShowCard card) {

    }

    @Override
    public void setCommentUpdated(String message) {

    }

        @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void initLayout() {

    }

    @Override
    public void playAnimationButton() {

    }

    @Override
    public void clearCards() {

    }

}
