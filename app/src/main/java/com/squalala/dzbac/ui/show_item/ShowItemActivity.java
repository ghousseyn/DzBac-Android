package com.squalala.dzbac.ui.show_item;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.bumptech.glide.Glide;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.RatingEvent;
import com.crashlytics.android.answers.ShareEvent;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.share.widget.ShareButton;
import com.google.android.gms.analytics.Tracker;
import com.squalala.dzbac.ActivityModule;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.DjihtiConstant;
import com.squalala.dzbac.R;
import com.squalala.dzbac.common.DjihtiBaseActivity;
import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.data.api.ApiResponse.Item;
import com.squalala.dzbac.data.api.LikeService;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.eventbus.events.FilterEvent;
import com.squalala.dzbac.ui.cards.CardModule;
import com.squalala.dzbac.ui.cards.show_info.AnnonceShowInfoCard;
import com.squalala.dzbac.ui.comment.CommentaryActivity;
import com.squalala.dzbac.ui.dialogs.DialogModule;
import com.squalala.dzbac.ui.dialogs.SendCommentDialog;
import com.squalala.dzbac.ui.dialogs.SendMessageDialog;
import com.squalala.dzbac.ui.dialogs.SignaleDialog;
import com.squalala.dzbac.ui.full_imageview.ViewPagerActivity;
import com.squalala.dzbac.ui.main.MainActivity;
import com.squalala.dzbac.ui.player.PlayerActivity2;
import com.squalala.dzbac.ui.show_profile.ShowProfileActivity;
import com.squalala.dzbac.utils.DateUtils;
import com.squalala.dzbac.utils.StringUtils;
import com.squalala.dzbac.utils.UrlToSplit;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import hugo.weaving.DebugLog;
import it.gmariotti.cardslib.library.view.CardView;
import retrofit2.Call;
import xyz.hanks.library.SmallBang;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ShowItemActivity.java
 * Date : 24 juil. 2014
 *
 */
public class ShowItemActivity extends DjihtiBaseActivity
	implements ShowItemView, OnClickListener {

	@InjectView(R.id.pseudo_user) TextView pseudoUser;
	@InjectView(R.id.like_item) FontAwesomeText fontLike;
	@InjectView(R.id.play_video) FontAwesomeText fontPlayVideo;
	@InjectView(R.id.comment_item) FontAwesomeText fontComment;
	@InjectView(R.id.share_item) FontAwesomeText fontShare;
	@InjectView(R.id.avatar_user) CircleImageView avatarUser;

	@InjectView(R.id.date_creation_item) TextView dateCreation;
	@InjectView(R.id.titre_annonce_show)
	EmojiconTextView titre;
	//@InjectView(R.id.signale_item) TextView signale;
	@InjectView(R.id.card_show_item_information) CardView cardView_info;

	@InjectView(R.id.carouselView)
    CarouselView mCarouselView;
	@InjectView(R.id.fb_share_button)
	ShareButton shareButton;

    @InjectView(R.id.tool_bar)
    Toolbar mToolbar;


	@InjectView(R.id.txt_signale)
	TextView txtSignale;

//	private int counter = 0;


	@Inject ShowItemPresenter showItemPresenter;
	@Inject SendMessageDialog sendMessageDialog;
	@Inject
	AnnonceShowInfoCard annonceShowCard;
	@Inject SignaleDialog signaleDialog;
	@Inject SendCommentDialog sendCommentDialog;
	@Inject LikeService likeService;
	@Inject Tracker tracker;
	@Inject MainPreferences mainPreferences;

    private static final String TAG = ShowItemActivity.class.getSimpleName();
	protected Item item;

    private int currentPositionSlide;

	private String pathImages;

	//private SharePhotoContent photoContent;
	//CallbackManager callbackManager;


	private boolean stateLike, stateFavoris;

	protected boolean isDeleteFavoris = false;

	protected String id_item, urlWithToSplit = "", urlVideo;

	private Activity activity = this;

	protected boolean isTopPost;

	protected boolean isNotification;
	private SmallBang mSmallBang;

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		if (isNotification) {

			startActivity(new Intent(this, MainActivity.class));
			finish();

		}

	}

	@Override
	public void cleanView() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_item);


		mSmallBang = SmallBang.attach2Window(this);
	//	callbackManager = CallbackManager.Factory.create();

		pathImages = getIntent().getExtras().getString("path_images");

		id_item = getIntent().getExtras().getString("id_item");

		isNotification = getIntent().getBooleanExtra("is_notification", false);

		String typeNotification = getIntent().getStringExtra("type_notification");

		if (typeNotification != null && typeNotification.equals("top_post")) {
			Answers.getInstance().logCustom(new CustomEvent("Notification")
					.putCustomAttribute("Click", "Top Post"));
			isTopPost = true;
		}
		else {
			isTopPost = false;
		}

		ButterKnife.inject(this);

        setSupportActionBar(mToolbar);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);


		//fontFavoris.setVisibility(View.GONE);

		//fontFavoris.setOnClickListener(this);
		fontLike.setOnClickListener(this);
		fontComment.setOnClickListener(this);
		fontPlayVideo.setOnClickListener(this);
		fontShare.setOnClickListener(this);
		txtSignale.setOnClickListener(this);
		//signale.setOnClickListener(this);

		pseudoUser.setOnClickListener(this);
		avatarUser.setOnClickListener(this);

		//TODO à corriger
	//	shareButton.setOnClickListener(this);
		shareButton.setVisibility(View.GONE);

		showItemPresenter.getDataItem(id_item);

        txtSignale.setPaintFlags(txtSignale.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	//	callbackManager.onActivityResult(requestCode, resultCode, data);

	}


	@Override
	public void showErrorLoading() {

	}

	@DebugLog
    @Override
	public void onDestroy() {
        mCarouselView.stopCarousel();
        mCarouselView = null;
		Crouton.cancelAllCroutons();
		showItemPresenter.onDestroy();
	    super.onDestroy();
	}

	@Override
	public void finish() {
		Intent i = new Intent();
		i.putExtra("is_delete_favoris", isDeleteFavoris);
		setResult(RESULT_OK, i);
		super.finish();
	}


	@Override
	protected void setupComponent(AppComponent appComponent) {
		DaggerShowItemComponent.builder()
				.appComponent(appComponent)
				.showItemModule(new ShowItemModule(this))
				.cardModule(new CardModule(this))
				.dialogModule(new DialogModule())
				.activityModule(new ActivityModule(this))
				.build()
				.inject(this);
	}


	@DebugLog
	@Override
	public void displayItem(Item item) {
        Log.e(TAG, "displayItem");


		if (mCarouselView == null)
			return;
       // if (sliderLayout == null)
      //      return;

		this.item = item;

		if (item != null) {
			Answers.getInstance().logContentView(new ContentViewEvent()
					.putContentName(item.titre)
					.putContentType("Post View " + item.type)
					.putContentId(id_item));
		}

		// Aime et favoris state
		if (item.valeur_aime == 1) {
			fontLike.setIcon("fa-heart");
			stateLike = true;
		}
		else {
			fontLike.setIcon("fa-heart-o");
			stateLike = false;
		}

		Picasso.with(getApplicationContext())
			.load(item.url_avatar)
                .placeholder(R.drawable.djihti_photo)
			.into(avatarUser);

		Date date = DateUtils.strToDate(item.date_creation);

		dateCreation.setText(DateUtils.getRelativeTime(date));

		titre.setText(StringUtils.toEmoji(item.titre));

		pseudoUser.setText(item.pseudo);

        boolean isImages = false;


		if (mCarouselView != null)
        	mCarouselView.setVisibility(View.INVISIBLE);

		// On vérifie qu'il y a d'abord des images
		if (item.url_images != null && item.url_images.size() != 0) {
            System.out.println(item.url_images.get(0).toString());

			if (mCarouselView != null) {
				mCarouselView.setImageListener(imageListener);
				mCarouselView.setPageCount(item.url_images.size());
				mCarouselView.setSlideInterval(4000);
			}

            isImages = true;

			/*for(int i = 0; i < item.url_images.size(); i++){

				System.out.println(item.url_images.get(i).toString());

				ImageSliderView myViewSliderView = new ImageSliderView(getApplicationContext(), "");
				myViewSliderView
					.setOnSliderClickListener(this)
                    //.setScaleType(BaseSliderView.ScaleType.FitCenterCrop)
					.image(item.url_images.get(i).toString());

				myViewSliderView.bundle(new Bundle());
				myViewSliderView.getBundle().putInt("position", i);
	           sliderLayout.addSlider(myViewSliderView);
	        }

			if (item != null && item.url_images != null && item.url_images.size() == 1)
				sliderLayout.stopAutoCycle();*/

			// Chaine à passer dans l'intent
			urlWithToSplit = UrlToSplit.strWithComma(item.url_images);

            int screenSize = getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK;

            if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE ||
                    screenSize == 4) {
                // nothing

            }
            else
                mToolbar.setBackgroundColor(Color.TRANSPARENT);

		}
		else // Il n'y a pas d'images
		{
            int screenSize = getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK;

            if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE ||
                    screenSize == 4 && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // nothing
                Log.e("ShowItemActivity", "LARGE");
            }
            else {
				if (mCarouselView != null)
               	 	mCarouselView.setVisibility(View.GONE);
            }


			urlWithToSplit = DjihtiConstant.NO_IMAGE_DJIHTI_URL;
           // On arrête le cycle
       //    sliderLayout.stopAutoCycle();
		}

        if (isImages) {
			if (mCarouselView != null)
				mCarouselView.setVisibility(View.VISIBLE);
		}



		if (item.urlVideo != null && !TextUtils.isEmpty(item.urlVideo)) {
			fontPlayVideo.setVisibility(View.VISIBLE);
			urlVideo = item.urlVideo;
		}
		else if (item.urlAudio != null && !TextUtils.isEmpty(item.urlAudio)) {
			fontPlayVideo.setVisibility(View.VISIBLE);

			if (item.url_images == null || item.url_images.size() == 0)
			{
				if (mCarouselView != null) {
					mCarouselView.setVisibility(View.VISIBLE);
					mCarouselView.setImageListener(imageListener);
					mCarouselView.setPageCount(1);
					mCarouselView.setSlideInterval(4000);
				}
			}


		}
		else {
			fontPlayVideo.setVisibility(View.GONE);
		}
	//	relativeContainer.setVisibility(View.VISIBLE);
	}

	@Override
	public void showMessage(String message) {
		Snackbar.make(this.findViewById(android.R.id.content),
				message, Snackbar.LENGTH_LONG).show();
	}

	@Override
	public void exit() {
		finish();
		EventBus.getDefault().post(new FilterEvent(""));
	}

    private ImageListener imageListener = new ImageListener() {

        @DebugLog
        @Override
        public void setImageForPosition(final int position, ImageView imageView) {
            currentPositionSlide = position;
            ///Toast.makeText(ShowItemActivity.this, "Pos " + position, Toast.LENGTH_LONG).show();

			String urlImage = null;

			if(item.url_images != null && item.url_images.size() > 0){
				urlImage = item.url_images.get(position).toString();

				imageView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(ShowItemActivity.this, ViewPagerActivity.class);
						intent.putExtra("position", position);
						intent.putExtra("path_images", pathImages);
						intent.putExtra("url_to_split", urlWithToSplit);
						startActivity(intent);
					}
				});
			}

			else
				urlImage = item.url_avatar;

            Glide.with(ShowItemActivity.this)
                    .load(urlImage)
					.fitCenter()
                    .into(imageView);

        }
    };



    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	    	MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.menu_show_item, menu);

	        return super.onCreateOptionsMenu(menu);
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {

	    	switch (item.getItemId())
	    	{
				case R.id.menu_send_message:

					sendMessageDialog.show();
					try {
						sendMessageDialog.setSubject(this.item.titre);
						sendMessageDialog.setIdReceveur(this.item.id_membre);
					}
					catch (NullPointerException e) {
						Crouton.makeText(this, getString(R.string.patientez),
								Style.ALERT).show();
					}


					break;

				case android.R.id.home:

					finish();

					return true;

				default:
					break;
			}


	    	return super.onOptionsItemSelected(item);
	    }

/*	@Override
	public void onSliderClick(BaseSliderView arg0) {


		if (item != null)
			Answers.getInstance().logContentView(new ContentViewEvent()
					.putContentName(item.titre)
					.putContentType("Image")
					.putContentId(id_item));

      //  Log.e(TAG, "onSliderClick " +arg0.getBundle().getInt("position"));
		//Intent intent = new Intent(this , FullImageViewSlider.class);
        Intent intent = new Intent(this , ViewPagerActivity.class);
    //    System.out.println(urlWithToSplit);

		intent.putExtra("position", arg0.getBundle().getInt("position"));
		intent.putExtra("url_to_split", urlWithToSplit);
		startActivity(intent);
	}*/


	/*private Target target = new Target() {
		@Override
		public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

			SharePhoto photo = new SharePhoto.Builder()
					.setBitmap(bitmap)
					.setCaption(item.titre)
					.build();

			photos.add(photo);

			Log.e(TAG, " couter : " + counter + " size " + item.url_images.size());

			if (!(counter + 1 == item.url_images.size())) {
				Picasso.with(ShowItemActivity.this)
						.load(item.url_images.get(++counter).toString())
						.into(target);
			}
			else {

				Bitmap bitmapShot = BitmapUtils.takeScreenshot(ShowItemActivity.this);

				photo = new SharePhoto.Builder()
						.setBitmap(bitmapShot)
						.setCaption(item.titre)
						.build();

				photos.add(photo);

				photoContent = new SharePhotoContent.Builder()
						.addPhotos(photos)
						.setRef("DzBac")
						.build();

				ShareDialog.show(ShowItemActivity.this, photoContent);

			}

			Log.e(TAG, "BUILD : " );
		}

		@Override
		public void onBitmapFailed(Drawable errorDrawable) {

		}

		@Override
		public void onPrepareLoad(Drawable placeHolderDrawable) {

		}
	};*/


    @Override
    protected void onPause() {
        super.onPause();
        mCarouselView.pauseCarousel();
    }



    @Override
	public void onClick(View v) {

		Intent intent = null;

		switch (v.getId()) {


			case R.id.txt_signale:

				SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
						.setContentText(activity.getString(R.string.signalement_confirmation))
						.setTitleText(activity.getString(R.string.attention))
						.setConfirmText(activity.getString(R.string.oui))
						.setCancelText(activity.getString(R.string.non))
						.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sweetAlertDialog) {
								sweetAlertDialog.dismissWithAnimation();
								showItemPresenter.signalePost(id_item);

							}
						})
						.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sweetAlertDialog) {
								sweetAlertDialog.dismissWithAnimation();
							}
						});

				sweetAlertDialog.setCancelable(true);
				sweetAlertDialog.show();


				break;

			case R.id.share_item:

				if (item != null) {

					Answers.getInstance().logShare(new ShareEvent()
							.putMethod("DzBac")
							.putContentName(item.titre)
							.putContentType("Post")
							.putContentId(id_item));

					Intent shareIntent = new Intent(Intent.ACTION_SEND);
					shareIntent.setType("text/plain");
					shareIntent.putExtra(Intent.EXTRA_TEXT, "http://squalala.xyz/dz_bac/posts/" + id_item);
					startActivity(Intent.createChooser(shareIntent, "Partager via"));
				}




				break;


			case R.id.play_video:

				Intent mpdIntent = null;


				if (item.urlVideo != null) {

					if (Build.VERSION.SDK_INT > 15)
					{
						Answers.getInstance().logCustom(new CustomEvent("Video")
								.putCustomAttribute("Content Id", id_item)
								.putCustomAttribute("Click", "Play"));

						mpdIntent = new Intent(this, PlayerActivity2.class);
						mpdIntent.setData(Uri.parse(urlVideo));
						mpdIntent.setAction(PlayerActivity2.ACTION_VIEW);
					}
					else
						Toast.makeText(this, getString(R.string.error_version_ancienne), Toast.LENGTH_LONG).show();

				}
				else {

					String urlImage = null;

					if (item.url_images != null && item.url_images.size() > 0)
						urlImage = item.url_images.get(0).toString();
					else
						urlImage = item.url_avatar;

					Answers.getInstance().logCustom(new CustomEvent("Audio")
							.putCustomAttribute("Content Id", id_item)
							.putCustomAttribute("Click", "Play"));

					mpdIntent = new Intent(this, PlayerActivity2.class);
					mpdIntent.putExtra("filename", item.urlAudio);
					mpdIntent.setData(Uri.parse(DjihtiConstant.HOME_URL + "/dz_bac/audio/" + item.urlAudio));
					mpdIntent.putExtra(PlayerActivity2.URL_IMAGE, urlImage);
                    mpdIntent.putExtra("path", pathImages + "/Audios/");
					mpdIntent.setAction(PlayerActivity2.ACTION_VIEW);

				/*	mpdIntent = new Intent(this, PlayerActivity.class)
							.putExtra("filename", item.urlAudio)
							.setData(Uri.parse(DjihtiConstant.HOME_URL + "/dz_bac/audio/" + item.urlAudio))
							.putExtra("path", pathImages + "/Audios/")
							//.setData(Uri.parse("http://squalala.xyz/dz_bac/audio/5b4f00df911eaf594876cdef30389a3ce18ab572.wav"))
									.putExtra(PlayerActivity.URL_IMAGE, urlImage)
							.putExtra(PlayerActivity.CONTENT_TYPE_EXTRA, Util.TYPE_OTHER);  */
				}

						//.putExtra(PlayerActivity.PROVIDER_EXTRA, Util.TYPE_OTHER);
				startActivity(mpdIntent);

				break;

		/*case R.id.fb_share_button:

			 Log.e(TAG, "count " + counter);

			Crouton.makeText(this, getString(R.string.patientez), Style.INFO).show();

				if (item.url_images.size() != 0) {
					Picasso.with(this)
							.load(item.url_images.get(counter).toString())
							.into(target);
				}
				else {

					Bitmap bitmap = BitmapUtils.takeScreenshot(this);

					SharePhoto photo = new SharePhoto.Builder()
							.setBitmap(bitmap)
							.setCaption(item.titre)
							.build();

					photoContent = new SharePhotoContent.Builder()
							.addPhoto(photo)
							.setRef("DzBac")
							.build();


					ShareDialog.show(ShowItemActivity.this, photoContent);
				}


			break;*/

		case R.id.like_item:


			// Create an object
		/*	ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
					.putString("og:type", "article")
					.putString("og:title", "Résume de science cool !")
					.putString("og:description", "Ceci est une description, très intéressante pour vous les amis, et merci beaucoup pour votre attention ! " +
							"In the frozen wastes to the north of Winterfell, sinister and supernatural forces are mustering.")
					.build();

			Log.e(TAG, "url image "+ item.url_images.get(0));

			SharePhoto photo = new SharePhoto.Builder()
					.setImageUrl(Uri.parse(item.url_images.get(0).toString()))
					.setUserGenerated(true)
					.build();

			// Create an action
			ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
					.setActionType("news.publishes")
					.putObject("article", object)
					.putPhoto("image", photo)
					.build();

			// Create the content
			ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
					.setPreviewPropertyName("article")
					.setAction(action)
					.build();

			ShareDialog.show(this, content);*/

			if (item != null)
			{
				if (!item.id_membre.equals(mainPreferences.getIdUser()))
				{
					likeService.like(
							id_item).enqueue(new retrofit2.Callback<ApiResponse.BasicResponse>() {
						@Override
						public void onResponse(Call<ApiResponse.BasicResponse> call, retrofit2.Response<ApiResponse.BasicResponse> response) {
							Crouton.makeText(activity, response.body().message , Style.INFO).show();

							if (!stateLike) { // Item aimé

								mSmallBang.bang(fontLike);

								Answers.getInstance().logRating(new RatingEvent()
										.putRating(1)
										.putContentName(item.titre)
										.putContentType(item.type)
										.putContentId(id_item));

								fontLike.setIcon("fa-heart");
								stateLike = true;

										/*YoYo.with(Techniques.BounceIn)
												.duration(700)
												.playOn(fontLike);*/
							}
							else {

								Answers.getInstance().logRating(new RatingEvent()
										.putRating(0)
										.putContentName(item.titre)
										.putContentType(item.type)
										.putContentId(id_item));


								fontLike.setIcon("fa-heart-o");
								stateLike = false;

								YoYo.with(Techniques.BounceIn)
										.duration(700)
										.playOn(fontLike);
							}
						}

						@Override
						public void onFailure(Call<ApiResponse.BasicResponse> call, Throwable t) {

						}
					});

				}
				else
				{
					Toast.makeText(this, getString(R.string.error_likes), Toast.LENGTH_LONG).show();
				}
			}

			break;

		case R.id.comment_item:

			if (item != null)
				Answers.getInstance().logContentView(new ContentViewEvent()
						.putContentName(item.titre)
						.putContentType("Commentaire")
						.putContentId(id_item));

            intent = new Intent(this, CommentaryActivity.class);
            intent.putExtra("id_item", id_item);
            intent.putExtra("is_notification", false);
            startActivity(intent);

			break;

		case R.id.pseudo_user:

			if (item != null) {
				intent = new Intent(this, ShowProfileActivity.class);
				intent.putExtra("id_membre", item.id_membre);
				startActivity(intent);
			}

			break;

		case R.id.avatar_user:

			if (item != null) {
				intent = new Intent(this, ShowProfileActivity.class);
				intent.putExtra("id_membre", item.id_membre);
				startActivity(intent);
			}

			break;

		default:
			break;
		}
	}

}
