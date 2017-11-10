package com.squalala.dzbac.ui.alert_dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.squalala.dzbac.R;
import com.squalala.dzbac.ui.add_item.AddItemPresenter;
import com.squalala.dzbac.ui.comment.CommentaryPresenter;
import com.squalala.dzbac.ui.full_imageview.ViewPagerActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : AlertPhotoManager.java
 * Date : 7 août 2014
 * 
 */
public class PhotoManagerAlert {
	
	private boolean [] imagesDisplay, uploading;
	private HorizontalScrollView horizontalScrollView;
	private String [] filenames;
    private String [] urlImages;
	private String idItem;

	private AddItemPresenter addItemPresenter;
    private CommentaryPresenter commentaryPresenter;
	
	public void setUploading(boolean [] uploading) {
		this.uploading = uploading;
	}
	
	public void setIdItem(String idItem) {
		this.idItem = idItem;
	}

    public void setUrlImages(String [] urlImages) {
        this.urlImages = urlImages;
    }

	public void setImagesDisplay(boolean [] tab) {
		this.imagesDisplay = tab;
	}
	
	public void setFilesnames(String [] filesnames) {
		this.filenames = filesnames;
	}
	
	public void setHorizontalScroll(HorizontalScrollView view) {
		this.horizontalScrollView = view;
	}
	
	public void setPresenter(AddItemPresenter addItemPresenter) {
		this.addItemPresenter = addItemPresenter;
	}

    public void setPresenter(CommentaryPresenter presenter) {
        this.commentaryPresenter = presenter;
    }


	public AlertDialog buildDialog(final ImageView photoItem, final FontAwesomeText addPhoto
			,final int requestCode, final Context context) {
		
		
		    AlertDialog.Builder builder = new AlertDialog.Builder(context);
		    builder.setMessage(context.getString(R.string.alert_click_photo_add_item));
		    
		    builder.setNeutralButton(context.getString(R.string.alert_action_voir_photo_add_item),
		            new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int id) {
		                    dialog.cancel();


                            // Si il y a au moins un upload on laisse la progress bar
                            boolean isUploading = false;

                            for (int i = 0; i < uploading.length; i++) {
                                if (uploading[i]) {
                                    isUploading = true;
                                    break;
                                }
                            }

                            // c'est que l'image est entrain de se faire uploade
                            // et on doit patienter...
                            if  (isUploading) {
                                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText(context.getString(R.string.alert_titre_info))
                                        .setContentText(context.getString(R.string.alert_info_upload_non_fini))
                                        .show();
                                //	AstuceMessage.message(context, context.getString(R.string.alert_titre_info),
                                //			context.getString(R.string.alert_info_upload_non_fini)).show();
                            }

                            else { // Pas d'uploa
                                Intent intent = new Intent(context, ViewPagerActivity.class);
                                Log.e("Alert", " " + requestCode + " " + urlImages[requestCode]);
                                //        intent.putExtra("position", requestCode);
                                intent.putExtra("url_to_split", urlImages[requestCode]);
                                context.startActivity(intent);
                            }
		                }
		            });

            builder.setPositiveButton(context.getString(R.string.alert_action_garder_photo_add_item),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

		    builder.setNegativeButton(context.getString(R.string.alert_action_supp_photo_add_item),
		            new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int id) {
		                	
		                	// Si il y a au moins un upload on laisse la progress bar
		            		boolean isUploading = false;
		            		
		            		for (int i = 0; i < uploading.length; i++) {
		            			if (uploading[i]) {
		            				isUploading = true;
		            				break;
		            			}
		            		}
		            		
		            		// c'est que l'image est entrain de se faire uploade
		                	 // et on doit patienter...
		            		if  (isUploading) {
                                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText(context.getString(R.string.alert_titre_info))
                                        .setContentText(context.getString(R.string.alert_info_upload_non_fini))
                                        .show();
		            		//	AstuceMessage.message(context, context.getString(R.string.alert_titre_info),
		                	//			context.getString(R.string.alert_info_upload_non_fini)).show();
		            		}
		            			
		            		else { // Pas d'upload en cours
		            			imagesDisplay[requestCode] = false;
                                if (addItemPresenter != null)
                                    addItemPresenter.deletePhoto(filenames[requestCode],
                                            idItem, "");
                                else
                                    commentaryPresenter.deletePhoto(filenames[requestCode],
                                            idItem);
		            			
		                		photoItem.setVisibility(View.GONE);
			                    addPhoto.setVisibility(View.VISIBLE);
			                	
			                    scrollRight();
			                    dialog.cancel();
		            		}
		                }
		            });
		    
		    AlertDialog alertDialog = builder.create(); 
		    
		    return alertDialog;
	}
	
	private void scrollRight() {
		
		// Afin de scroller jusqu'au bout quand on rajoute un élément
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
		    public void run() {
		        horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
		    }
		}, 100L);
			
	}

}
