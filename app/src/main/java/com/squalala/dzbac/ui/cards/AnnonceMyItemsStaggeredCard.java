package com.squalala.dzbac.ui.cards;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squalala.dzbac.R;
import com.squalala.dzbac.DjihtiConstant;
import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.data.api.ApiResponse.BasicResponse;
import com.squalala.dzbac.data.api.PostsService;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.ui.add_item.AddPostActivity;
import com.squalala.dzbac.ui.list_items.ListItemListener;
import com.squalala.dzbac.ui.payement.PayementActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : AnnonceFavorisStaggeredCard.java
 * Date : 3 août 2014
 * 
 */
public class AnnonceMyItemsStaggeredCard extends PostStaggeredCard 
	implements OnClickListener {
	
	private PostsService itemService;
	private ListItemListener listener;
	private MainPreferences mainPreferences;

	public AnnonceMyItemsStaggeredCard(Context context, PostsService service, MainPreferences mainPreferences,
			ListItemListener listener) {
		super(context);
		this.itemService = service;
		this.mainPreferences = mainPreferences;
		this.listener = listener;
	}
	
	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		super.setupInnerViewElements(parent, view);
		
		linearGestionItem.setVisibility(View.VISIBLE);
		trashItemStaggered.setVisibility(View.VISIBLE);
		reditItemStaggered.setVisibility(View.VISIBLE);
		trashFavorisStaggered.setVisibility(View.GONE);
		fontUpPost.setVisibility(View.VISIBLE);

        fontUpPost.setTextColor(ContextCompat.getColor(getContext(), R.color.bleu));
        reditItemStaggered.setTextColor(ContextCompat.getColor(getContext(), R.color.color8));
        trashItemStaggered.setTextColor(ContextCompat.getColor(getContext(), R.color.vert_icon_app));

        Log.e(TAG, "id_membre" + post.idMembre);
        if (post.idMembre.equals(mainPreferences.getIdUser()) ||
			mainPreferences.getLevelUser() >= DjihtiConstant.ADMIN_GRADUATION) {
            linearGestionItem.setVisibility(View.VISIBLE);
            trashItemStaggered.setVisibility(View.VISIBLE);
            reditItemStaggered.setVisibility(View.VISIBLE);

            fontUpPost.setVisibility(View.VISIBLE);
        }
        else {
            linearGestionItem.setVisibility(View.GONE);
            trashItemStaggered.setVisibility(View.GONE);
            reditItemStaggered.setVisibility(View.GONE);
            fontUpPost.setVisibility(View.GONE);
        }



        fontSignalement.setVisibility(View.VISIBLE);

		if (isSignalement() && mainPreferences.getLevelUser() >= DjihtiConstant.ADMIN_GRADUATION)
        {
            fontSignalement.setVisibility(View.VISIBLE);
            viewDeleteSignale.setVisibility(View.VISIBLE);
        }
		else {
            fontSignalement.setVisibility(View.GONE);
            viewDeleteSignale.setVisibility(View.GONE);
        }

		fontSignalement.setOnClickListener(this);
		trashItemStaggered.setOnClickListener(this);
		reditItemStaggered.setOnClickListener(this);
		fontUpPost.setOnClickListener(this);
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {

			case R.id.icon_uppost:

                if (mainPreferences.getLevelContribution() == 4) {

                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText(context.getString(R.string.dzbac_boost))
                            .setContentText(context.getString(R.string.alert_epingler_post))
                            .setCancelText(context.getString(R.string.non))
                            .setConfirmText(context.getString(R.string.oui))
                            .showCancelButton(true)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();
                                    pinPost();
                                }
                            })
                            .show();
                }
                else if (mainPreferences.getPremiumCode() == 6) {

					new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
							.setTitleText(context.getString(R.string.dzbac_boost))
							.setContentText(context.getString(R.string.alert_epingler_post))
							.setCancelText(context.getString(R.string.non))
							.setConfirmText(context.getString(R.string.oui))
							.showCancelButton(true)
							.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
								@Override
								public void onClick(SweetAlertDialog sweetAlertDialog) {
									sweetAlertDialog.cancel();
									pinPost();
								}
							})
							.show();
				}
                else {
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(context.getString(R.string.dzbac_boost))
                            .setContentText(context.getString(R.string.alert_epingler_post_error))
                            .setCancelText(context.getString(R.string.ok))
                            .setConfirmText(context.getString(R.string.en_savoir_plus))
                            .showCancelButton(true)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    getContext().startActivity(new Intent(getContext(), PayementActivity.class));
                                }
                            })
                            .show();
                }

				break;

			case R.id.delete_signale_staggered:

				new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
						.setTitleText(context.getString(R.string.alert_titre_info))
						.setContentText(context.getString(R.string.supp_signalement))
						.setCancelText(context.getString(R.string.non))
						.setConfirmText(context.getString(R.string.oui))
						.showCancelButton(true)
						.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sweetAlertDialog) {
								sweetAlertDialog.cancel();
								deleteSignalement();
							}
						})
						.show();



				break;
		
			case R.id.trash_item_staggered:

				new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
						.setTitleText(context.getString(R.string.alert_titre_info))
						.setContentText(context.getString(R.string.alert_delete_item_message))
						.setCancelText(context.getString(R.string.non))
						.setConfirmText(context.getString(R.string.oui))
						.showCancelButton(true)
						.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sweetAlertDialog) {
								sweetAlertDialog.cancel();
								deleteItem();
							}
						})
						.show();

				/* AlertDialog.Builder builder = new AlertDialog.Builder(context);
				 builder.setMessage(context.getString(R.string.alert_delete_item_message));

				 builder.setNeutralButton(context.getString(R.string.non),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});

				 builder.setNegativeButton(context.getString(R.string.oui),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {

									deleteItem();

								}
					});

				 AlertDialog alertDialog = builder.create();

				 alertDialog.show();*/


				break;

			case R.id.redit_item_staggered:

				System.out.println("ID --> "+ post.id_item);

				Intent i = new Intent(context , AddPostActivity.class);
				i.putExtra("is_modification", true);
				i.putExtra("id_item", post.id_item);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);


				break;

			default:
				break;
		}
	}
	
	
	private void deleteItem() {
		

		listener.onLoad();
		
		itemService.deleteItem(
				post.id_item).enqueue(new retrofit2.Callback<BasicResponse>() {
			@Override
			public void onResponse(Call<BasicResponse> call, retrofit2.Response<BasicResponse> response) {
				setClickable(false);
				listener.onRemoveItem();
				Toast.makeText(getContext(), response.body().message,
						Toast.LENGTH_LONG).show();
			}

			@Override
			public void onFailure(Call<BasicResponse> call, Throwable t) {

			}
		});
	}

    private void pinPost() {

        itemService.pinPost(post.id_item).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                Toast.makeText(getContext(), response.body().message,
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }


	private void deleteSignalement() {


		listener.onLoad();

		itemService.deleteSignalement(
                post.id_item).enqueue(new retrofit2.Callback<BasicResponse>() {
			@Override
			public void onResponse(Call<BasicResponse> call, retrofit2.Response<BasicResponse> response) {
				setClickable(false);
				listener.onRemoveItem();
				Toast.makeText(getContext(), response.body().message,
						Toast.LENGTH_LONG).show();
			}

			@Override
			public void onFailure(Call<BasicResponse> call, Throwable t) {

			}
		});
	}

}
		
		
