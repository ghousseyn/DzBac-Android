package com.squalala.dzbac.ui.add_item;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.DjihtiApp;
import com.squalala.dzbac.R;
import com.squalala.dzbac.data.api.ApiResponse.Item;
import com.squalala.dzbac.utils.PostUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import hugo.weaving.DebugLog;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : AddAnnonceActivity.java
 * Date : 8 août 2014
 * 
 */
public class AddPostActivity extends AddItemActivity {
	

	
	private boolean isModification = false;

    private static final String TAG = AddPostActivity.class.getSimpleName();


	@Override
	protected void onCreate(Bundle savedInstanceState) {

        DjihtiApp.tracker.setScreenName(getClass().getName());
		
		limit_image = 30;

		super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("");
		
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

		card_info.setCard(addItemInfoCard);
		
		btnDeposeItem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

                if (!addItemInfoCard.getDescription().contains("facebook.com/groups/"))
                {
                    pDialog.show();

                    // On vérifie qu'il n'y a pas d'upload en cours
                    boolean isUploading = false;

                    for (int i = 0; i < uploading.length; i++) {
                        if (uploading[i]) {
                            isUploading = true;
                            break;
                        }
                    }

                    if (isUploading || isUploadinFile)
                        isSubmited = true;
                    else
                        submit();
                }
				

			}
		});
		
		try {
			isModification = getIntent().getExtras().getBoolean("is_modification");
			idItem = getIntent().getExtras().getString("id_item");
		} catch (NullPointerException e) {
			isModification = false;
			//e.printStackTrace();
		}
		
		if (isModification)
			presenterShowItem.getDataItem(idItem);
		else
			presenter.initItem();

       // Crouton.makeText(this, "zrrzer", Style.INFO).show();
	}

    @Override
    public void showErrorLoading() {
        Toast.makeText(this, getString(R.string.error_loading), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void cleanView() {

    }

    @DebugLog
    @Override
    public void showMessage(String message) {
        Snackbar.make(this.findViewById(android.R.id.content),
               message, Snackbar.LENGTH_LONG).show();
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
    protected void setupComponent(AppComponent appComponent) {
        super.setupComponent(appComponent);
    }

    @Override
	public void setIdItem(String idItem) {
		this.idItem = idItem;
		super.setIdItem(idItem);
	}

    @Override
    public void exit() {

    }

    @Override
	public void setTitleError(String message) {
		addItemInfoCard.getEditTitre().setError(message);
		Crouton.makeText(this, message, Style.ALERT).show();
	}


    @Override
    public void updateUIPhotos() {
        displayPhotoItemAdd();
        presenter.updateDataImages(idItem,
                "",
                urlImagePresentation);
    }

    @Override
	public void setDescriptionError(String message) {
		addItemInfoCard.getEditDescription().setError(message);
		Crouton.makeText(this, message, Style.ALERT).show();
	}

    @Override
    public void showMessageError(String message) {
        Crouton.makeText(this, message, Style.ALERT).show();
    }

    @Override
    public void showMessageSuccesFul(String message) {
        Log.e(TAG, "showMessageSuccesFul");
        Crouton.makeText(this, message, Style.INFO).show();

    }

    @Override
    public void hideButtonAddCamera() {
        cameraItemAdd.setVisibility(View.GONE);
    }

    @Override
    public void showButtonAddCamera() {
        cameraItemAdd.setVisibility(View.VISIBLE);
    }

    @Override
	protected void hydrateItem() {

		postAdd.setMatiere(addItemInfoCard.getMatiere());
		postAdd.setType(PostUtils.TypeStrToTypeCode(this, addItemInfoCard.getTypePost()));
		postAdd.setTitle(addItemInfoCard.getTitre());
        postAdd.setSecteur(addItemInfoCard.getFiliere());
		postAdd.setDescription(addItemInfoCard.getDescription());
        postAdd.setFilename(filenameUpload);
        postAdd.setAudio(audioNameUpload);
        postAdd.setUrlVideo(urlVideo);
        postAdd.setTagsId(addItemInfoCard.getJsonTagsId());
		
		super.hydrateItem();
	}

	@Override
	protected void submit() {
		hydrateItem();

        if (postAdd.getMatiere().equals("Sélectionne une matière")
                ||
                postAdd.getType().equals("Sélectionne un type") ||
                postAdd.getTagsId() == null) {
            Crouton.makeText(this, getString(R.string.error_fields_add_item), Style.ALERT).show();
            pDialog.dismiss();
        }
        else
            presenter.submitItem(postAdd, idItem, isModification);

	}
	

	@Override
	public void hideProgressBar() {
        smoothProgressBarUpload.setVisibility(View.GONE);
		pDialog.dismiss();
	}


    @Override
    public void hideButtonAddAudio() {
        audioItemAdd.setVisibility(View.GONE);
    }

    @Override
    public void showButtonAddAudio() {
        audioItemAdd.setVisibility(View.VISIBLE);
    }

    @Override
	public void displayItem(Item itemResponse) {
		super.displayItem(itemResponse);
		
		addItemInfoCard.setTitre(itemResponse.titre);
		addItemInfoCard.setDescription(itemResponse.description);
		addItemInfoCard.setMatiere(itemResponse.matiere);
		addItemInfoCard.setTypePost(PostUtils.TypeCodeToTypeStr(this, itemResponse.type));
        addItemInfoCard.setTagsView(itemResponse.tags);



        System.out.println("filiere : " + itemResponse.filiere);
       // addItemInfoCard.setFiliere(itemResponse.filiere);



        if (itemResponse.filename != null && itemResponse.filename.length() > 0)
            filenameUpload = itemResponse.filename;
        else
            filenameUpload = null;

        if (filenameUpload != null && filenameUpload.length() > 0) {

            photoItemAdd.setVisibility(View.GONE);
            audioItemAdd.setVisibility(View.GONE);

            String typeIcon = null;

            hideButtonPhoto();
            int i = filenameUpload.lastIndexOf('.');

            if (i >= 0) {

                String extension = filenameUpload.substring(i+1).toLowerCase();

                switch (extension) {

                    case "pdf":

                        typeIcon = "fa-file-pdf-o";

                        break;

                    case "txt":

                        typeIcon = "fa-file-text-o";

                        break;

                    case "zip":

                        typeIcon = "fa-file-zip-o";

                        break;

                    case "rar":

                        typeIcon = "fa-file-zip-o";

                        break;

                    default:

                        typeIcon = "fa-file-text";

                        break;
                }

                fileItemAdd.setIcon(typeIcon);

            }


            if (itemResponse.urlAudio != null && itemResponse.urlAudio.length() > 0)
                audioNameUpload = itemResponse.urlAudio;
            else
                audioNameUpload = null;

            if (audioNameUpload != null) {
                audioItemAdd.setVisibility(View.VISIBLE);
                photoItemAdd.setVisibility(View.GONE);
                fileItemAdd.setVisibility(View.GONE);
            }

        }



        /*
        addItemInfoCard.setMatiere(translater.translate(R.array.subject, itemResponse.matiere));
		addItemInfoCard.setTypePost(translater.translate(R.array.type, itemResponse.type));
         */


        this.urlVideo = itemResponse.urlVideo;

        if (urlVideo != null) {
            cameraItemAdd.setVisibility(View.GONE);
            fileItemAdd.setVisibility(View.GONE);
            photoItemAdd.setVisibility(View.GONE);
            photoItem1.setVisibility(View.GONE);
        }

	}




    @Override
    public void setFileNameUpload(String filename) {
        filenameUpload = filename;

        isUploadinFile = false;

        if (isSubmited)
            submit();
    }


    @Override
    public void setAudioNameUpload(String filename) {
        audioNameUpload = filename;

        isUploadinFile = false;

        if (isSubmited)
            submit();
    }

    @Override
    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    @Override
    public void onBackPressed() {

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setContentText(this.getString(R.string.quitter_activity))
                .setTitleText(this.getString(R.string.attention))
                .setCancelText(this.getString(R.string.non))
                .setConfirmText(this.getString(R.string.oui))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        sweetAlertDialog.dismiss();
                        AddPostActivity.super.onBackPressed();

                    }
                })
                .show();
    }

    @Override
    public void showProgressBar() {
        smoothProgressBarUpload.setVisibility(View.VISIBLE);
    }

    @Override
    public void setIconFile(String iconType) {
        fileItemAdd.setIcon(iconType);

        YoYo.with(Techniques.Bounce)
                .duration(700)
                .playOn(fileItemAdd);
    }



    @Override
    public void hideButtonPhoto() {
        photoItemAdd.setVisibility(View.GONE);
    }

    @Override
    public void showButtonAddPhoto() {
        photoItemAdd.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideButtonAddFile() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fileItemAdd.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void showButtonAddFile() {
        fileItemAdd.setVisibility(View.VISIBLE);
    }
}


