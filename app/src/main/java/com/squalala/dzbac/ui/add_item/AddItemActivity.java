package com.squalala.dzbac.ui.add_item;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;
import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.bumptech.glide.Glide;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.squalala.dzbac.ActivityModule;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.R;
import com.squalala.dzbac.common.DjihtiBaseActivity;
import com.squalala.dzbac.DjihtiConstant;
import com.squalala.dzbac.common.ProgressRequestBody;
import com.squalala.dzbac.data.PostAdd;
import com.squalala.dzbac.data.api.ApiResponse.Item;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.ui.alert_dialog.PhotoManagerAlert;
import com.squalala.dzbac.ui.cards.AnnonceAddItemInfoCard;
import com.squalala.dzbac.ui.cards.CardModule;
import com.squalala.dzbac.ui.filepicker.MultimediaPickerActivity;
import com.squalala.dzbac.ui.show_item.ShowItemModule;
import com.squalala.dzbac.ui.show_item.ShowItemPresenter;
import com.squalala.dzbac.ui.show_item.ShowItemView;
import com.squalala.dzbac.utils.DpToPixel;
import com.squalala.dzbac.utils.FileUtils;
import com.squalala.dzbac.utils.Translater;
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
import hugo.weaving.DebugLog;
import info.hoang8f.widget.FButton;
import it.gmariotti.cardslib.library.view.CardView;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : AddItemActivity.java
 * Date : 6 août 2014
 *
 */
public abstract class AddItemActivity extends DjihtiBaseActivity
        implements AddItemView, OnClickListener, OnLongClickListener,
        ShowItemView, ProgressRequestBody.UploadCallbacks {

    private static final int RESULT_CAMERA = 9999;

    @InjectView(R.id.tool_bar)
    Toolbar toolbar;

    private final static int PERMISSION_RQ = 84;

    @InjectView(R.id.card_info_add_item) CardView card_info;
    //@InjectView(R.id.card_contact_add_item) CardView card_contact;
    //@InjectView(R.id.card_localisation_add_item) CardView card_localisation;
    @InjectView(R.id.linear_container_photos) LinearLayout containerPhotos;
    @InjectView(R.id.photo_item_add)
    FontAwesomeText photoItemAdd;
    @InjectView(R.id.file_item_add)
    FontAwesomeText fileItemAdd;
    @InjectView(R.id.camera_item_add)
    FontAwesomeText cameraItemAdd;
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

    @InjectView(R.id.btn_depose_item) FButton btnDeposeItem;

    @InjectView(R.id.smooth_add_item)
    SmoothProgressBar smoothProgressBarUpload;

    @InjectView(R.id.main_layout)
    View mLayout;

    protected boolean isUploadinFile;

    private File saveDir;
    private RequestBody typedFileVideo;

  //  @InjectView(R.id.smooth_progress_upload)
  //  NumberProgressBar smoothProgressBarUpload;

    @InjectView(R.id.horizontal_scroll_container_photos) HorizontalScrollView horizontalScrollPhoto;


    @Inject AddItemPresenter presenter;
    @Inject PostAdd postAdd;
    @Inject PhotoManagerAlert alManager;
    @Inject SweetAlertDialog pDialog;
    @Inject
    AnnonceAddItemInfoCard addItemInfoCard;
    @Inject
    ShowItemPresenter presenterShowItem;
    @Inject
    Translater translater;

    @Inject
    MainPreferences preferences;

    protected boolean imagesDisplay [], uploading [], isSubmited = false;

    protected String filenames[], urlImages[];

    protected String idItem, urlImagePresentation, urlVideo;

    protected ImageView [] photoitems;
    protected int limit_image = 4 , positionUrlImagePresentation;
    private Uri [] uriImages;

    protected String filenameUpload, audioNameUpload;


    private static final int INTENT_REQUEST_GET_N_IMAGES = 14;

    private static final int FILE_CODE = 4994;
    private static final int AUDI_CODE = 4995;
    private final static int VIDEO_CODE = 6969;
    private final static int IMAGE_PREVIEW_CODE = 6900;

    private static final String TAG = AddItemActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_add_item);

        ButterKnife.inject(this);


        setSupportActionBar(toolbar);
   /*     getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new
                ColorDrawable(Color.parseColor("#FF9955")));*/

        pDialog.show();

        //card_contact.setCard(addConctactCard);

        photoItemAdd.setOnClickListener(this);
        fileItemAdd.setOnClickListener(this);
        cameraItemAdd.setOnClickListener(this);
        audioItemAdd.setOnClickListener(this);
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

        photoItem1.setOnLongClickListener(this);
        photoItem2.setOnLongClickListener(this);
        photoItem3.setOnLongClickListener(this);
        photoItem4.setOnLongClickListener(this);
        photoItem5.setOnLongClickListener(this);
        photoItem6.setOnLongClickListener(this);
        photoItem7.setOnLongClickListener(this);
        photoItem8.setOnLongClickListener(this);
        photoItem9.setOnLongClickListener(this);
        photoItem10.setOnLongClickListener(this);
        photoItem11.setOnLongClickListener(this);
        photoItem12.setOnLongClickListener(this);
        photoItem13.setOnLongClickListener(this);
        photoItem14.setOnLongClickListener(this);
        photoItem15.setOnLongClickListener(this);
        photoItem16.setOnLongClickListener(this);
        photoItem17.setOnLongClickListener(this);
        photoItem18.setOnLongClickListener(this);
        photoItem19.setOnLongClickListener(this);
        photoItem20.setOnLongClickListener(this);
        photoItem21.setOnLongClickListener(this);
        photoItem22.setOnLongClickListener(this);
        photoItem23.setOnLongClickListener(this);
        photoItem24.setOnLongClickListener(this);
        photoItem25.setOnLongClickListener(this);
        photoItem26.setOnLongClickListener(this);
        photoItem27.setOnLongClickListener(this);
        photoItem28.setOnLongClickListener(this);
        photoItem29.setOnLongClickListener(this);
        photoItem30.setOnLongClickListener(this);

        imagesDisplay = new boolean[limit_image];
        filenames = new String[limit_image];
        uploading = new boolean[limit_image];
        uriImages = new Uri[limit_image];
        urlImages = new String[limit_image];

    }




    @Override
    public void setIdItem(String idItem) {

        alManager.setImagesDisplay(imagesDisplay);
        alManager.setUploading(uploading);
        alManager.setIdItem(idItem);
        alManager.setFilesnames(filenames);
        alManager.setUrlImages(urlImages);
        alManager.setHorizontalScroll(horizontalScrollPhoto);
        alManager.setPresenter(presenter);
        //TODO à vérifier
     //   pDialog.dismiss();
    }

    @Override
    public void onProgressUpdate(int percentage) {
        System.out.println("Pourcentage : " + percentage + " %");
    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {

    }

    protected abstract void submit();

    private void scrollRight() {

        // Afin de scroller jusqu'au bout quand on rajoute un élément
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                horizontalScrollPhoto.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        }, 100L);

    }

   /* @Override
    protected List<Object> getModules() {
        return  Arrays.<Object>asList(new CardModule(this),
                new AddItemModule(this, this),
                new ShowItemModule(this));
    }*/

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerAddItemComponent.builder()
                .appComponent(appComponent)
                .addItemModule(new AddItemModule(this))
                .showItemModule(new ShowItemModule(this))
                .activityModule(new ActivityModule(this))
                .cardModule(new CardModule(this))
                .build()
                .inject(this);
    }

    @DebugLog
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == AnnonceAddItemInfoCard.CODE_TAG) {

                addItemInfoCard.setTags(data.getStringArrayListExtra("tags"));

            }

            if (requestCode == RESULT_CAMERA )
            {
                File fileVideo, fileImage;

                System.out.println("path : " + data.getStringExtra("path"));
                System.out.println("path image : " + data.getStringExtra("imagePath"));

                fileVideo = new File(preferences.getPathVideo());
                fileImage = new File(preferences.getPathImage());

                if (!fileVideo.exists() || !fileImage.exists())
                    return;

                RequestBody typedFileVideo = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file", fileVideo.getName(),
                                RequestBody.create(MediaType.parse("application/octet-stream"), fileVideo))
                        .build();

               // RequestBody typedFileVideo = RequestBody.create(MediaType.parse("application/octet-stream"), fileVideo);
                Uri uriImage = Uri.fromFile(fileImage);

                    presenter.uploadPhotoAndVideo(idItem /*idItem*/, "",
                            0,
                            UploadHelper.getTypedFileWithId(getApplicationContext(),
                                    UploadHelper.getRealPathFromURI(getApplicationContext(), uriImage), idItem), typedFileVideo);
            }

            if (requestCode == INTENT_REQUEST_GET_N_IMAGES) {

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
                                        .override(px, px)
                                        .centerCrop()
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

            if (requestCode == FILE_CODE ) {

                Uri uri = data.getData();

                File file = new File(uri.getPath());

                String extension = "";

                int i = file.getName().lastIndexOf('.');

                if (i >= 0) {

                    boolean error = false;

                    extension = file.getName().substring(i+1).toLowerCase();

                    if (file.length() > DjihtiConstant.MAX_LENGTH_FILE) {
                        error = true;
                        Crouton.makeText(this, getString(R.string.error_file_too_large), Style.ALERT).show();
                    }

                    if (!(extension.equals("pdf") || extension.equals("txt") || extension.equals("doc")
                            || extension.equals("docx") || extension.equals("rar")
                            || extension.equals("zip"))) {
                        error = true;
                        Crouton.makeText(this, getString(R.string.error_file), Style.ALERT).show();
                    }

                    if (!error) {

                        RequestBody typedFile = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("file", file.getName(),
                                        RequestBody.create(MediaType.parse("application/octet-stream"), file))
                                .build();

                   //     RequestBody typedFile = RequestBody.create(MediaType.parse("application/octet-stream"), file);

                        isUploadinFile = true;
                        presenter.uploadFile(typedFile);
                    }

                }
                else {
                    Crouton.makeText(this, getString(R.string.error_file), Style.ALERT).show();
                }

                Log.e(TAG, "Uri : " + uri.toString());
            }
            else if (requestCode == AUDI_CODE) {

                Uri uri = data.getData();

                File file = new File(uri.getPath());

            //    RequestBody e = null;
                RequestBody typedFile = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file", file.getName(),
                                RequestBody.create(MediaType.parse("application/octet-stream"), file))
                        .build();

           /*     RequestBody typedFile = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file", file.getName(),
                                RequestBody.create(MediaType.parse("application/octet-stream"), file))
                        .build();  */

             //   RequestBody typedFile = RequestBody.create(MediaType.parse("application/octet-stream"), file);

                isUploadinFile = true;
                presenter.uploadAudioFile(typedFile);

            }
            else if (requestCode == VIDEO_CODE) {

                final File file = new File(data.getData().getPath());

                typedFileVideo = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file", file.getName(),
                                RequestBody.create(MediaType.parse("application/octet-stream"), file))
                        .build();


                addPhotoPreviewForVideo();
            }
            else if (requestCode == IMAGE_PREVIEW_CODE) {

                final File file = new File(data.getData().getPath());

                Uri uriImage = Uri.fromFile(file);

                presenter.uploadPhotoAndVideo(idItem /*idItem*/, "",
                        0,
                        UploadHelper.getTypedFileWithId(getApplicationContext(),
                                UploadHelper.getRealPathFromURI(getApplicationContext(), uriImage), idItem), typedFileVideo);
            }

        }



    }

    private void addPhotoPreviewForVideo() {

        Toast.makeText(this, getString(R.string.photo_preview_video), Toast.LENGTH_LONG).show();
        MaterialCamera materialCamera = new MaterialCamera(this)
                .saveDir(saveDir)
                .showPortraitWarning(false)
                .autoSubmit(false)
                .allowRetry(true)
                .defaultToFrontFacing(true);

                materialCamera.stillShot();
                materialCamera.start(IMAGE_PREVIEW_CODE); // launches the Camera in stillshot mode
    }

    protected void displayPhotoItemAdd() {

        boolean isPhotoItemAddCanVisible = false;

        for (int j = 0; j < limit_image; j++) {

            if (!imagesDisplay[j]) {
                isPhotoItemAddCanVisible = true;


                break;
            }

        }

        // Pour savoir si on permet l'upload de fichier ou pas
        for (int i = 0; i < filenames.length; i++) {
            if (imagesDisplay[i]) {
                fileItemAdd.setVisibility(View.GONE);
                break;
            }
            else
                fileItemAdd.setVisibility(View.VISIBLE);
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
            for (int i = 0; i < limit_image; i++) {

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
                            //		Log.e("UPLOAD STATE OK", isOk+"");

                            break;
                        }
                    }

                    if (isOk) {
                        //	Log.e("BEGIN" ,"################ UPLOAD TASK ################## ");
                        uploading[i] = true;
                        presenter.uploadPhotoItem( idItem /*idItem*/, "",
                                i,
                                UploadHelper.getTypedFileWithId(getApplicationContext(),
                                        UploadHelper.getRealPathFromURI(getApplicationContext(), uriImages[i]), idItem));
                    }

                    break;
                }

            }

            return null;
        }

    }

    private void takePicture() {

        PhotoPickerIntent intent = new PhotoPickerIntent(AddItemActivity.this);
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


    /**
     * Used to handle result of askForPermission for Contacts Permission, in better way than onRequestPermissionsResult() and handling with big switch statement
     */
    final PermissionCallback permissionCameraCallback = new PermissionCallback() {
        @Override
        public void permissionGranted() {
            //boolean hasAccess = Tools.accessContacts(MainActivity.this);
         //   Toast.makeText(MainActivity.this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();
            addVideo();
        }

        @Override
        public void permissionRefused() {
          //  boolean hasAccess = Tools.accessContacts(MainActivity.this);
           // Toast.makeText(MainActivity.this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();
        }
    };

    final PermissionCallback permissionFileCallback = new PermissionCallback() {
        @Override
        public void permissionGranted() {
            //boolean hasAccess = Tools.accessContacts(MainActivity.this);
            //   Toast.makeText(MainActivity.this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();
            addFile();
        }

        @Override
        public void permissionRefused() {
            //  boolean hasAccess = Tools.accessContacts(MainActivity.this);
            // Toast.makeText(MainActivity.this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();
        }
    };


    final PermissionCallback permissionAudioCallback = new PermissionCallback() {
        @Override
        public void permissionGranted() {
            //boolean hasAccess = Tools.accessContacts(MainActivity.this);
            //   Toast.makeText(MainActivity.this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();
            addFileAudio();
        }

        @Override
        public void permissionRefused() {
            //  boolean hasAccess = Tools.accessContacts(MainActivity.this);
            // Toast.makeText(MainActivity.this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();
        }
    };

    final PermissionCallback permissionPhotoCallback = new PermissionCallback() {
        @Override
        public void permissionGranted() {
            //boolean hasAccess = Tools.accessContacts(MainActivity.this);
            //   Toast.makeText(MainActivity.this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();
            addPhoto();
        }

        @Override
        public void permissionRefused() {
            //  boolean hasAccess = Tools.accessContacts(MainActivity.this);
            // Toast.makeText(MainActivity.this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();
        }
    };

    private void addVideo() {
        Answers.getInstance().logCustom(new CustomEvent("Record Video")
                .putCustomAttribute("Click", "Record Video"));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request permission to save videos in external storage
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_RQ);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Only use external storage directory if permission is granted, otherwise cache directory is used by default
            saveDir = new File(Environment.getExternalStorageDirectory(), "DzBac");
            if (!saveDir.exists())
                saveDir.mkdirs();
        }

        MaterialCamera materialCamera = new MaterialCamera(this)
                .saveDir(saveDir)
                .showPortraitWarning(true)
                .maxAllowedFileSize(DjihtiConstant.MAX_LENGTH_VIDEO)
                .qualityProfile(MaterialCamera.QUALITY_LOW)
                .autoSubmit(false)
                .allowRetry(true)
                .defaultToFrontFacing(true);

        materialCamera.start(VIDEO_CODE);
    }

    private void addPhoto(){

        for (int i = 0; i < limit_image; i++) {
            if (!imagesDisplay[i]) {
                takePicture();
                break;
            }
        }
    }

    private void addFile() {
        if (filenameUpload == null) {
            Intent intent = new Intent(this, FilePickerActivity.class);
            intent.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
            //  intent.putExtra(FilePickerActivity.EXTRA_MODE, AbstractFilePickerFragment.MODE_FILE_AND_DIR);
            startActivityForResult(intent, FILE_CODE);
        }
        else {

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setContentText(this.getString(R.string.supprimer_fichier))
                    .setTitleText(this.getString(R.string.alerte))
                    .setCancelText(this.getString(R.string.non))
                    .setConfirmText(this.getString(R.string.oui))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            sweetAlertDialog.dismissWithAnimation();
                            presenter.deleteFile(idItem, filenameUpload);

                        }
                    })
                    .show();



        }
    }

    private void addFileAudio() {
        if (audioNameUpload == null) {
            Toast.makeText(this, getString(R.string.audio_info), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MultimediaPickerActivity.class);
            intent.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
            //  intent.putExtra(FilePickerActivity.EXTRA_MODE, AbstractFilePickerFragment.MODE_FILE_AND_DIR);
            startActivityForResult(intent, AUDI_CODE);
        }
        else {

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setContentText(this.getString(R.string.supprimer_fichier))
                    .setTitleText(this.getString(R.string.alerte))
                    .setCancelText(this.getString(R.string.non))
                    .setConfirmText(this.getString(R.string.oui))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            sweetAlertDialog.dismissWithAnimation();
                            presenter.deleteAudio(idItem, audioNameUpload);

                        }
                    })
                    .show();



        }
    }

    @Override
    public void onClick(View v) {

        getUrImagePresentation();

        switch (v.getId()) {

            case R.id.camera_item_add:

                if(Nammu.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //We have a permission, easy peasy
                  //  boolean hasAccess = Tools.accessContacts(this);
                   // Toast.makeText(this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();
                    addVideo();
                } else {
                    //We do not own this permission
                    if (Nammu.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        //User already refused to give us this permission or removed it
                        //Now he/she can mark "never ask again" (sic!)
                        Snackbar.make(mLayout, getString(R.string.permission_camera),
                                Snackbar.LENGTH_INDEFINITE)
                                .setAction(getString(R.string.ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Nammu.askForPermission(AddItemActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, permissionCameraCallback);
                                    }
                                }).show();
                    } else {
                        //First time asking for permission
                        // or phone doesn't offer permission
                        // or user marked "never ask again"
                        Nammu.askForPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, permissionCameraCallback);
                    }
                }


                break;

            case R.id.file_item_add:


                if(Nammu.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    //We have a permission, easy peasy
                    //  boolean hasAccess = Tools.accessContacts(this);
                    // Toast.makeText(this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();
                    addFile();
                } else {
                    //We do not own this permission
                    if (Nammu.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        //User already refused to give us this permission or removed it
                        //Now he/she can mark "never ask again" (sic!)
                        Snackbar.make(mLayout, getString(R.string.permission_file),
                                Snackbar.LENGTH_INDEFINITE)
                                .setAction(getString(R.string.ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Nammu.askForPermission(AddItemActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE, permissionFileCallback);
                                    }
                                }).show();
                    } else {
                        //First time asking for permission
                        // or phone doesn't offer permission
                        // or user marked "never ask again"
                        Nammu.askForPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, permissionFileCallback);
                    }
                }




                break;


            case R.id.audio_item_add:



                if(Nammu.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    //We have a permission, easy peasy
                    //  boolean hasAccess = Tools.accessContacts(this);
                    // Toast.makeText(this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();
                    addFileAudio();
                } else {
                    //We do not own this permission
                    if (Nammu.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        //User already refused to give us this permission or removed it
                        //Now he/she can mark "never ask again" (sic!)
                        Snackbar.make(mLayout, getString(R.string.permission_file),
                                Snackbar.LENGTH_INDEFINITE)
                                .setAction(getString(R.string.ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Nammu.askForPermission(AddItemActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE, permissionAudioCallback);
                                    }
                                }).show();
                    } else {
                        //First time asking for permission
                        // or phone doesn't offer permission
                        // or user marked "never ask again"
                        Nammu.askForPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, permissionAudioCallback);
                    }
                }





                break;

            case R.id.photo_item_add:

                if(Nammu.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    //We have a permission, easy peasy
                    //  boolean hasAccess = Tools.accessContacts(this);
                    // Toast.makeText(this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();
                    addPhoto();
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
                                        Nammu.askForPermission(AddItemActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE, permissionPhotoCallback);
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
        urlImages[position] = DjihtiConstant.PATH_IMAGES_POSTS + "/" + idItem + "/" +filename;
        uploading[position] = false;
        uriImages[position] = null;

        YoYo.with(Techniques.Shake)
                .duration(700)
                .playOn(photoitems[position]);


        smoothProgressBarUpload.setVisibility(View.VISIBLE);
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
                    // Aucune upload en cours et aucun fichier à upload, on vérifie si la personne a terminé
                    if (isSubmited)
                        submit();
                }
                else {
                    smoothProgressBarUpload.setVisibility(View.VISIBLE);
                }


                presenter.updateDataImages(idItem,
                        "",
                        getUrImagePresentation());
            }
        }, 500);



    }



    protected void hydrateItem() {


        postAdd.setUrlPresentation(filenames[positionUrlImagePresentation]);

        if (filenames[positionUrlImagePresentation] == null) {
            for (int i = 0; i < filenames.length; i++) {
                if (filenames[i] != null) {
                    postAdd.setUrlPresentation(getUrImagePresentation());
                    break;
                }
            }
        }

    }



    private String getUrImagePresentation() {

        if (filenames[positionUrlImagePresentation] == null)
            for (int i = 0; i < filenames.length; i++) {
                if (filenames[i] != null) {
                    this.urlImagePresentation = filenames[i];
                    return filenames[i];
                }
            }
        else {
            this.urlImagePresentation = filenames[positionUrlImagePresentation];
            return filenames[positionUrlImagePresentation];
        }

        return null;
    }


    @Override
    public boolean onLongClick(View v) {

        Crouton.makeText(this, getString(R.string.image_selectione), Style.INFO).show();

        switch(v.getId())
        {
            case R.id.photo_item_1:

                positionUrlImagePresentation = 0;

                break;

            case R.id.photo_item_2:

                positionUrlImagePresentation = 1;

                break;

            case R.id.photo_item_3:

                positionUrlImagePresentation = 2;

                break;

            case R.id.photo_item_4:

                positionUrlImagePresentation = 3;

                break;

            case R.id.photo_item_5:

                positionUrlImagePresentation = 4;

                break;

            case R.id.photo_item_6:

                positionUrlImagePresentation = 5;

                break;

            case R.id.photo_item_7:

                positionUrlImagePresentation = 6;

                break;

            case R.id.photo_item_8:

                positionUrlImagePresentation = 7;

                break;

            case R.id.photo_item_9:

                positionUrlImagePresentation = 8;

                break;

            case R.id.photo_item_10:

                positionUrlImagePresentation = 9;

                break;

            case R.id.photo_item_11:

                positionUrlImagePresentation = 10;

                break;


            case R.id.photo_item_12:

                positionUrlImagePresentation = 11;

                break;

            case R.id.photo_item_13:

                positionUrlImagePresentation = 12;

                break;

            case R.id.photo_item_14:

                positionUrlImagePresentation = 13;

                break;

            case R.id.photo_item_15:

                positionUrlImagePresentation = 14;

                break;

            case R.id.photo_item_16:

                positionUrlImagePresentation = 15;

                break;

            case R.id.photo_item_17:

                positionUrlImagePresentation = 16;

                break;

            case R.id.photo_item_18:

                positionUrlImagePresentation = 17;

                break;

            case R.id.photo_item_19:

                positionUrlImagePresentation = 18;

                break;


            case R.id.photo_item_20:

                positionUrlImagePresentation = 19;

                break;

            case R.id.photo_item_21:

                positionUrlImagePresentation = 20;

                break;
            case R.id.photo_item_22:

                positionUrlImagePresentation = 21;

                break;

            case R.id.photo_item_23:

                positionUrlImagePresentation = 22;

                break;
            case R.id.photo_item_24:

                positionUrlImagePresentation = 23;

                break;
            case R.id.photo_item_25:

                positionUrlImagePresentation = 24;

                break;

            case R.id.photo_item_26:

                positionUrlImagePresentation = 25;

                break;
            case R.id.photo_item_27:

                positionUrlImagePresentation = 26;

                break;

            case R.id.photo_item_28:

                positionUrlImagePresentation = 27;

                break;
            case R.id.photo_item_29:

                positionUrlImagePresentation = 28;

                break;

            case R.id.photo_item_30:

                positionUrlImagePresentation = 29;

                break;

        }

        return false;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_astuce, menu);

        ActionItemBadge.update(this, menu.findItem(R.id.menu_astuce),
                FontAwesome.Icon.faw_question_circle, ActionItemBadge.BadgeStyles.YELLOW, Integer.MIN_VALUE);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.menu_astuce:
                //z	AstuceMessage.message(this, getString(R.string.alert_titre_astuce),
                //	getString(R.string.alert_astuce_message)).show();
                new SweetAlertDialog(this)
                        .setTitleText( getString(R.string.alert_titre_astuce))
                        .setContentText(getString(R.string.alert_astuce_message))
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void navigateToUserItems(String message) {
        Crouton.makeText(this, message, Style.INFO).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                setResult(RESULT_OK);
                finish();
            }
        }, 1100);
    }




    @Override
    public void displayItem(Item itemResponse) {
        Log.e(TAG, "displayItem");

        setIdItem(idItem);

        for (int i = 0; i < itemResponse.url_images.size(); i++) {

            int index = itemResponse.url_images.get(i).toString().indexOf(idItem) + idItem.length();
            filenames[i] = itemResponse.url_images.get(i).toString().substring(index + 1);
            urlImages[i] = itemResponse.url_images.get(i).toString();

            //    System.out.println( i + " --> " + filenames[i]);

            if (filenames[i].equals(itemResponse.image_name))
                positionUrlImagePresentation = i;

            //	System.out.println("---> " +filenames[i] );
            photoitems[i].setVisibility(View.VISIBLE);


            Glide.with(this)
                    .load(itemResponse.url_images.get(i).toString())
                    .fitCenter()
                    .placeholder(R.drawable.no_image_small)
                    .into(photoitems[i]);

            imagesDisplay[i] = true;
        }

        displayPhotoItemAdd();

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
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        // card_info.set

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





}
