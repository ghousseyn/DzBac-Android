package com.squalala.dzbac.ui.comment;

import android.Manifest;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.piasy.rxandroidaudio.AudioRecorder;
import com.github.piasy.rxandroidaudio.PlayConfig;
import com.github.piasy.rxandroidaudio.RxAmplitude;
import com.github.piasy.rxandroidaudio.RxAudioPlayer;
import com.google.android.gms.analytics.Tracker;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.squalala.dzbac.ActivityModule;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.DjihtiApp;
import com.squalala.dzbac.R;
import com.squalala.dzbac.common.BaseRxActivity;
import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.eventbus.events.AudioEvent;
import com.squalala.dzbac.model.Message;
import com.squalala.dzbac.ui.cards.CommentShowCard;
import com.squalala.dzbac.ui.show_item.ShowAnnonceActivity;
import com.squalala.dzbac.ui.showcaseview.ToolbarActionItemTarget;
import com.squalala.dzbac.utils.DateUtils;
import com.squalala.dzbac.utils.EndlessScrollListener;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hugo.weaving.DebugLog;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : CommentActivity.java
 * Date : 29 juil. 2014
 * 
 */
public class CommentaryActivity extends BaseRxActivity
 implements CommentaryView, SwipeRefreshLayout.OnRefreshListener, OnClickListener, AudioRecorder.OnErrorListener {

	private static final String TAG = CommentaryActivity.class.getSimpleName();

	public static final int MIN_AUDIO_LENGTH_SECONDS = 2;

	public static final int SHOW_INDICATOR_DELAY_MILLIS = 300;

	List<ImageView> mIvVoiceIndicators;

	private AudioRecorder mAudioRecorder;

	private RxAudioPlayer mRxAudioPlayer;

	private File mAudioFile;

	private Subscription mRecordSubscription;

	private Queue<File> mAudioFiles = new LinkedList<>();

    private ArrayList<Card> cards = new ArrayList<>();


    @Inject
    SweetAlertDialog pDialog;


	@Inject CommentaryPresenter commentaryPresenter;

	@Inject
	Tracker tracker;

	@InjectView(R.id.progressBar_comment)
	CircularProgressBar progress;

	@InjectView(R.id.mFlIndicator)
	FrameLayout mFlIndicator;

	@InjectView(R.id.mTvRecordingHint)
	TextView mTvRecordingHint;

	@InjectView(R.id.edit_commentaire)
	EmojiconEditText editComment;
	@InjectView(R.id.aucun_commentaire) TextView aucun_commentaire;
    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout swipeLayout;
    @InjectView(R.id.tool_bar)
    Toolbar mToolbar;
	@InjectView(R.id.img_send_message_comment)
	ImageView send_comment;
	@InjectView(R.id.edit_and_send)
	LinearLayout linearLayoutComment;
	@InjectView(R.id.txt_suivre_commentaires) TextView txt_stateInGroup;

    @InjectView(R.id.txt_new_comment_indicator)
    TextView txtIndicatorNumberComment;

    private int numberNewComment;

	@InjectView(R.id.emoji_btn)
	ImageView emojiBtn;

	@InjectView(R.id.root_view)
	View rootView;
	
	private String idItem;
	private CardArrayAdapter mCardArrayAdapter;
	private CardListView listView;
	private boolean stateInGroup, isFromNotification;

    private MenuItem favorisMenuItem;

	private EmojIconActions emojIcon;

    private boolean updateFavoris;

    private MediaPlayer mediaPlayer;
    private String urlAudio;
    private boolean audioReady;

    private ArrayList<Card> listCommentShowCards;

    private AudioListener mListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		tracker.setScreenName(getClass().getName());

		setContentView(R.layout.activity_commentary);
		
		ButterKnife.inject(this);

        mAudioRecorder = AudioRecorder.getInstance();
        mRxAudioPlayer = RxAudioPlayer.getInstance();
        mAudioRecorder.setOnErrorListener(this);

        mIvVoiceIndicators = new ArrayList<>();
        mIvVoiceIndicators.add(ButterKnife.<ImageView>findById(this, R.id.mIvVoiceIndicator1));
        mIvVoiceIndicators.add(ButterKnife.<ImageView>findById(this, R.id.mIvVoiceIndicator2));
        mIvVoiceIndicators.add(ButterKnife.<ImageView>findById(this, R.id.mIvVoiceIndicator3));
        mIvVoiceIndicators.add(ButterKnife.<ImageView>findById(this, R.id.mIvVoiceIndicator4));
        mIvVoiceIndicators.add(ButterKnife.<ImageView>findById(this, R.id.mIvVoiceIndicator5));
        mIvVoiceIndicators.add(ButterKnife.<ImageView>findById(this, R.id.mIvVoiceIndicator6));
        mIvVoiceIndicators.add(ButterKnife.<ImageView>findById(this, R.id.mIvVoiceIndicator7));

		emojIcon = new EmojIconActions(this, rootView, editComment, emojiBtn);
		emojIcon.setIconsIds(R.drawable.ic_keyboard_white_48dp, R.drawable.ic_tag_faces_white_48dp);
		emojIcon.ShowEmojIcon();


		if (savedInstanceState != null) {
			editComment.setText(savedInstanceState.getString("edit_text"));
		}

        setSupportActionBar(mToolbar);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                R.color.vert_icon_app,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
		
		isFromNotification = getIntent().getBooleanExtra("is_notification", true);
		
		send_comment.setOnClickListener(this);
		txt_stateInGroup.setOnClickListener(this);
		
		idItem = getIntent().getExtras().getString("id_item");
		
		mCardArrayAdapter = new CardArrayAdapter(this ,cards);
	    listView = (CardListView) this.findViewById(R.id.myList);
	    
        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }

        txtIndicatorNumberComment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                numberNewComment = 0;
                txtIndicatorNumberComment.setVisibility(View.GONE);
                listView.setSelection(0);
            }
        });

		editComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0)
                {
                    send_comment.setImageResource(R.drawable.ic_send_white_24dp);
                    linearLayoutComment.setBackgroundResource(R.color.bleu);
                }

                else {
                    send_comment.setImageResource(R.drawable.ic_mic_white_36dp);
                    linearLayoutComment.setBackgroundResource(R.color.colorPrimary);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

		send_comment.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (getComment().isEmpty()) {

					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							press2Record();
							break;
						case MotionEvent.ACTION_UP:
							release2Send();
							break;
						case MotionEvent.ACTION_CANCEL:
							release2Send();
							break;
						default:
							break;
					}

					return true;
				}

				return false;
			}
		});

        commentaryPresenter.loadCommentaires(idItem);


	}

	


	@Override
	public void onError(@AudioRecorder.Error int error) {

	}

	private void press2Record() {
	//	mTvPressToSay.setBackgroundResource(R.drawable.button_press_to_say_pressed_bg);
		mTvRecordingHint.setText(R.string.voice_msg_input_hint_speaking);

		boolean isPermissionsGranted = RxPermissions.getInstance(getApplicationContext())
				.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
				RxPermissions.getInstance(getApplicationContext())
						.isGranted(Manifest.permission.RECORD_AUDIO);
		if (!isPermissionsGranted) {
			RxPermissions.getInstance(getApplicationContext())
					.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
							Manifest.permission.RECORD_AUDIO)
					.subscribe(new Action1<Boolean>() {
						@Override
						public void call(Boolean granted) {
							// not record first time to request permission
							if (granted) {
								Toast.makeText(getApplicationContext(), "Permission granted",
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(getApplicationContext(), "Permission not granted",
										Toast.LENGTH_SHORT).show();
							}
						}
					}, new Action1<Throwable>() {
						@Override
						public void call(Throwable throwable) {
							throwable.printStackTrace();
						}
					});
		} else {
			recordAfterPermissionGranted();
		}
	}

	private void recordAfterPermissionGranted() {
		mRecordSubscription = Single.just(true)
				.subscribeOn(Schedulers.io())
				.flatMap(new Func1<Boolean, Single<Boolean>>() {
					@Override
					public Single<Boolean> call(Boolean aBoolean) {
						Log.d(TAG, "to play audio_record_start: " + R.raw.audio_record_start);
						return mRxAudioPlayer.play(
								PlayConfig.res(getApplicationContext(), R.raw.audio_record_start)
										.build());
					}
				})
				.doOnSuccess(new Action1<Boolean>() {
					@Override
					public void call(Boolean aBoolean) {
						Log.d(TAG, "audio_record_start play finished");
					}
				})
				.map(new Func1<Boolean, Boolean>() {
					@Override
					public Boolean call(Boolean aBoolean) {
						mAudioFile = new File(
								Environment.getExternalStorageDirectory().getAbsolutePath() +
										File.separator + System.nanoTime() + ".file.m4a");
						Log.d(TAG, "to prepare record");
						return mAudioRecorder.prepareRecord(MediaRecorder.AudioSource.MIC,
								MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.AudioEncoder.AAC,
								192000, 192000, mAudioFile);
					}
				})
				.doOnSuccess(new Action1<Boolean>() {
					@Override
					public void call(Boolean aBoolean) {
						Log.d(TAG, "prepareRecord success");
					}
				})
				.flatMap(new Func1<Boolean, Single<Boolean>>() {
					@Override
					public Single<Boolean> call(Boolean aBoolean) {
						Log.d(TAG, "to play audio_record_ready: " + R.raw.audio_record_ready);
						return mRxAudioPlayer.play(
								PlayConfig.res(getApplicationContext(), R.raw.audio_record_ready)
										.build());
					}
				})
				.doOnSuccess(new Action1<Boolean>() {
					@Override
					public void call(Boolean aBoolean) {
						Log.d(TAG, "audio_record_ready play finished");
					}
				})
				.map(new Func1<Boolean, Boolean>() {
					@Override
					public Boolean call(Boolean aBoolean) {
						// TODO why need delay?
						mFlIndicator.postDelayed(new Runnable() {
							@Override
							public void run() {
								mFlIndicator.setVisibility(View.VISIBLE);
							}
						}, SHOW_INDICATOR_DELAY_MILLIS);
						return mAudioRecorder.startRecord();
					}
				})
				.doOnSuccess(new Action1<Boolean>() {
					@Override
					public void call(Boolean aBoolean) {
						Log.d(TAG, "startRecord success");
					}
				})
				.toObservable()
				.flatMap(new Func1<Boolean, Observable<Integer>>() {
					@Override
					public Observable<Integer> call(Boolean aBoolean) {
						return RxAmplitude.from(mAudioRecorder);
					}
				})
				.observeOn(AndroidSchedulers.mainThread())
				.compose(this.<Integer>bindToLifecycle())
				.subscribe(new Action1<Integer>() {
					@Override
					public void call(Integer level) {
						int progress = mAudioRecorder.progress();
						Log.d(TAG, "amplitude: " + level + ", progress: " + progress);

						refreshAudioAmplitudeView(level);

						if (progress >= 12) {
							mTvRecordingHint.setText(String.format(
									getString(R.string.voice_msg_input_hint_time_limited_formatter),
									15 - progress));
							if (progress == 15) {
								release2Send();
							}
						}
					}
				}, new Action1<Throwable>() {
					@Override
					public void call(Throwable throwable) {
						throwable.printStackTrace();
					}
				});
	}

    @Override
    public void showProgressDialog() {
        if (pDialog != null && !pDialog.isShowing())
            pDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        pDialog.hide();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void release2Send() {
	///	mTvPressToSay.setBackgroundResource(R.drawable.button_press_to_say_bg);
		mFlIndicator.setVisibility(View.GONE);

		if (mRecordSubscription != null && !mRecordSubscription.isUnsubscribed()) {
			mRecordSubscription.unsubscribe();
			mRecordSubscription = null;
		}

		Log.d(TAG, "to play audio_record_end: " + R.raw.audio_record_end);
		mRxAudioPlayer.play(PlayConfig.res(getApplicationContext(), R.raw.audio_record_end).build())
				.doOnSuccess(new Action1<Boolean>() {
					@Override
					public void call(Boolean aBoolean) {
						Log.d(TAG, "audio_record_end play finished");
					}
				})
				.subscribeOn(Schedulers.io())
				.map(new Func1<Boolean, Boolean>() {
					@Override
					public Boolean call(Boolean aBoolean) {
						int seconds = mAudioRecorder.stopRecord();
						if (seconds >= MIN_AUDIO_LENGTH_SECONDS) {
							mAudioFiles.offer(mAudioFile);
							return true;
						}
                        else {

                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    if (getComment().isEmpty()) {
                                        Toast.makeText(CommentaryActivity.this, getString(R.string.rester_appuyer), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                        }
						return false;
					}
				})
				.toObservable()
				.observeOn(AndroidSchedulers.mainThread())
				.compose(this.<Boolean>bindToLifecycle())
				.subscribe(new Action1<Boolean>() {
					@Override
					public void call(Boolean added) {
						if (added) {

                            new SweetAlertDialog(CommentaryActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setContentText(getString(R.string.message_vocal))
                                    .setTitleText(getString(R.string.audio))
                                    .setCancelText(getString(R.string.non))
                                    .setConfirmText(getString(R.string.oui))
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                            sweetAlertDialog.dismissWithAnimation();

                                            RequestBody typedFile = new MultipartBody.Builder()
                                                    .setType(MultipartBody.FORM)
                                                    .addFormDataPart("file", mAudioFile.getName(),
                                                            RequestBody.create(MediaType.parse("application/octet-stream"), mAudioFile))
                                                    .build();
                                            System.out.println( "audio 2 file " + mAudioFile.getName() + " added");

                                            Answers.getInstance().logCustom(new CustomEvent("Audio")
                                                    .putCustomAttribute("Commentaire", "Send"));

                                          commentaryPresenter.uploadAudio(typedFile);

                                        }
                                    })
                                    .show();
                            Log.d(TAG,  "audio file " + mAudioFile.getName() + " added");
						//	mTvLog.setText(
						//			mTvLog.getText() + "\n" + "audio file " + mAudioFile.getName() +
						//					" added");
						}
					}
				}, new Action1<Throwable>() {
					@Override
					public void call(Throwable throwable) {
						throwable.printStackTrace();
					}
				});
	}

	private static final ButterKnife.Action<View> INVISIBLE = new ButterKnife.Action<View>() {
		@Override
		public void apply(View view, int index) {
			view.setVisibility(View.INVISIBLE);
		}
	};

	private static final ButterKnife.Action<View> VISIBLE = new ButterKnife.Action<View>() {
		@Override
		public void apply(View view, int index) {
			view.setVisibility(View.VISIBLE);
		}
	};

    @DebugLog
	private void refreshAudioAmplitudeView(int level) {
		int end = level < mIvVoiceIndicators.size() ? level : mIvVoiceIndicators.size();
		ButterKnife.apply(mIvVoiceIndicators.subList(0, end), VISIBLE);
		ButterKnife.apply(mIvVoiceIndicators.subList(end, mIvVoiceIndicators.size()), INVISIBLE);
	}


	@Override
    public void onRefresh() {
        numberNewComment = 0;
        txtIndicatorNumberComment.setVisibility(View.GONE);
        commentaryPresenter.onRefresh(idItem);
    }


    @Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		isFromNotification = true;
		idItem = intent.getExtras().getString("id_item");
        commentaryPresenter.onNewIntent(idItem);
	}

	private String getComment() {
		return  editComment.getText().toString().trim();
	}


	@Override
	protected void setupComponent(AppComponent appComponent) {
		DaggerCommentaryComponent.builder()
				.appComponent(appComponent)
				.commentaryModule(new CommentaryModule(this))
				.activityModule(new ActivityModule(this))
				.build()
				.inject(this);
	}


    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewCommentaire(ApiResponse.Commentaire commentaire) {
        commentaryPresenter.onNewComment(commentaire);
    }

    private boolean contains(ApiResponse.Commentaire commentaire) {

        for (Card card_ :cards) {

            CommentShowCard commentShowCard = (CommentShowCard) card_;

            if (commentShowCard.getCommentaire().id.equals(commentaire.id))
                return true;
        }

        return false;
    }



    @Override
	public void onBackPressed() {
        commentaryPresenter.onBackPressed();
		if (isFromNotification) {
			Intent intent = new Intent(this, ShowAnnonceActivity.class);
            intent.putExtra("id_item", idItem);
			startActivity(intent);
			finish();
		}
		super.onBackPressed();
	}



	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("edit_text", getComment());
	}


    @DebugLog
    @Override
    public void addComment(Card card) {

        CommentShowCard commentShowCard = (CommentShowCard) card;

		System.out.println("");

        if (!contains(commentShowCard.getCommentaire()) &&
				DateUtils.differenceDateMinuteFromNow(commentShowCard.getCommentaire().date_creation) < 3)
        {
			cards.add(0, card);
			mCardArrayAdapter.notifyDataSetChanged();

			if (listView.getFirstVisiblePosition() == 0) {
				listView.setSelection(0);
				txtIndicatorNumberComment.setVisibility(View.GONE);
			}
			else {
				numberNewComment++;
				txtIndicatorNumberComment.setVisibility(View.VISIBLE);
				txtIndicatorNumberComment.setText(getString(R.string.new_comments, numberNewComment));
			}
        }
    }

    @DebugLog
	@Override
	public void showCommentaires(ArrayList<Card> cards_) {


		if (cards_ != null) {

            cards.addAll(cards_);
		//	mCardArrayAdapter.addAll(cards);
		    mCardArrayAdapter.notifyDataSetChanged();
		}
		
		if (mCardArrayAdapter.isEmpty())
			aucun_commentaire.setVisibility(View.VISIBLE);
		else
			aucun_commentaire.setVisibility(View.GONE);
	}

    @Override
    public void clearCards() {
        mCardArrayAdapter.clear();
    }

    @Override
    public void initLayout() {

		if (listView != null)
			listView.setOnScrollListener(new EndlessScrollListener() {

				@Override
				public void onLoadMore(int page, int totalItemsCount) {

					try {
						commentaryPresenter.onLoadMore(page, idItem);
					} catch (NullPointerException e) {
						e.printStackTrace();
					}


				}
			});
	}

    public interface AudioListener {

        void onLoading();

        void onPlaying();

        void onStop();

    }

   private void play() throws IOException {
        // Todo check what happens after second time loading
        if (!audioReady) {

            mediaPlayer = new MediaPlayer();

            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(urlAudio);

            prepareAsync();

            mediaPlayer.setOnPreparedListener(onPreparedListener);
            mediaPlayer.setOnCompletionListener(onCompletionListener);

        } else
            playAudio();
    }

    private void prepareAsync() {
        mediaPlayer.prepareAsync();
        mListener.onLoading();
     //   setTextLoading();
     //   sendCallbackAudioPreparing();
    }

    private void playAudio()  {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            mListener.onPlaying();
       //     setText(stopText);
        }
        else {
            audioReady = false;
            try {
                play();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            mListener.onStop();
          //  setText(playText);
        }
    }

    @DebugLog
    public void toggleAudio() throws IOException {
        if (mediaPlayer != null && mediaPlayer.isPlaying())
            stop();
        else
            play();
    }


    private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            playAudio();
            audioReady = true;
        }
    };

    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mListener.onStop();
        }
    };


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    @DebugLog
    public void onEvent(AudioEvent event) {
        Log.e(TAG, "onEvent audio : " + event.getUrlAudio());

        if (!event.isPlaying())
        {
            // pour arrêter le précédent
            if (mListener != null) {
                stop();
                audioReady = false;
                mediaPlayer.release();
                mediaPlayer = null;
                mListener.onStop();
            }

            mListener = event.getAudioListener();
            urlAudio = event.getUrlAudio();

            try {
                toggleAudio();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                toggleAudio();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void playAnimationButton() {
        YoYo.with(Techniques.RubberBand)
                .duration(700)
                .playOn(send_comment);
    }

    @DebugLog
    @Override
	protected void onDestroy() {

        commentaryPresenter.onDestroy();
        commentaryPresenter = null;

		Crouton.cancelAllCroutons();
		if (mRxAudioPlayer != null) {
			mRxAudioPlayer.stopPlay();
		}

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

		if (mAudioRecorder != null) {
			mAudioRecorder.setOnErrorListener(null);
			mAudioRecorder = null;
		}

		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {

		case R.id.img_send_message_comment:


			if (!getComment().isEmpty() && !getComment().contains("facebook.com")) {
                commentaryPresenter.sendComment(getComment(), idItem);
                editComment.setText("");
            }



			break;
			
		case R.id.txt_suivre_commentaires:
			
			if (stateInGroup) 
				commentaryPresenter.removeFromGroup(idItem);
			else
				commentaryPresenter.joinGroup(idItem);
			
			break;

		default:
			break;
		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_comment, menu);

        favorisMenuItem = menu.findItem(R.id.action_favoris);

		MainPreferences preferences = new MainPreferences(this);

		if (!preferences.isTutoGuide(TAG)) {

			new ShowcaseView.Builder(this)
					.withMaterialShowcase()
					.setTarget(new ToolbarActionItemTarget(mToolbar, R.id.menu_comment_image))
					.setStyle(R.style.CustomShowcaseTheme4)
					.setContentTitle("Poste des photos !")
					.setContentText("Tu peux ajouter des photos à tes commentaires pour mieux expliquer !")
					.singleShot(5)
					.build();

			preferences.setTutoGuide(TAG, true);
		}

        ActionItemBadge.update(this, menu.findItem(R.id.menu_comment_image),
                FontAwesome.Icon.faw_camera, ActionItemBadge.BadgeStyles.YELLOW, Integer.MIN_VALUE);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {

            case R.id.action_favoris:

                updateFavoris = true;

                if (stateInGroup)
                    commentaryPresenter.removeFromGroup(idItem);
                else
                    commentaryPresenter.joinGroup(idItem);


                break;


            case R.id.menu_comment_image:

                Intent intent = new Intent(this, CommentPhotoActivity.class);
                intent.putExtra("id_item", idItem);
                startActivityForResult(intent, 0);

                break;


			case android.R.id.home:

				finish();
				return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }



	@Override
	public void showStateInGroup(boolean stateInGroup) {



		if (stateInGroup) {
            favorisMenuItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_star_white_36dp));
            if (updateFavoris)
                Snackbar.make(this.findViewById(android.R.id.content),
                        getString(R.string.suivre_conversation), Snackbar.LENGTH_LONG).show();
			this.txt_stateInGroup.setText(getString(R.string.ne_plus_suivre));
			this.stateInGroup = true;
		}
		else {
            favorisMenuItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_star_border_white_36dp));

            if (updateFavoris)
                Snackbar.make(this.findViewById(android.R.id.content),
                        getString(R.string.ne_plus_suivre_conversation), Snackbar.LENGTH_LONG).show();
			this.stateInGroup = false;
			this.txt_stateInGroup.setText(getString(R.string.suivre));
		}


        updateFavoris = false;

	}

    @Override
    public void showProgressBar() {
        swipeLayout.setRefreshing(true);
    }

    @Override
    public void hideProgressBar() {
        Log.e("CommentaryActivity", "hideProgressBar");
        swipeLayout.setRefreshing(false);
    }



    @Override
	public void showProgress() {
        progress.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgress() {
        progress.setVisibility(View.GONE);
	}

	@Override
	public void setCommentDeleted(String message, CommentShowCard card) {
		Crouton.makeText(this,
				message,
				Style.INFO);
        mCardArrayAdapter.remove(card);
        mCardArrayAdapter.notifyDataSetChanged();
	}

	@Override
	public void setCommentUpdated(String message) {
		Crouton.makeText(this,
				message,
				Style.INFO);
	}

    @Override
    public void navigateToBack(String message) {}

    @Override
    public void populateFilenames(int position, String filename) {}

    @Override
    public void showMessageDelete(String message) {}

    @Override
    public void setIdItem(String idItem) {}

    @Override
    public void setUploadError(int position, String message) {}

    @Override
    public void setErrorComment() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        commentaryPresenter.onActivityResult(idItem);
    }


    
}
