package com.squalala.dzbac.ui.show_conversation;

import android.Manifest;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.piasy.rxandroidaudio.AudioRecorder;
import com.github.piasy.rxandroidaudio.PlayConfig;
import com.github.piasy.rxandroidaudio.RxAmplitude;
import com.github.piasy.rxandroidaudio.RxAudioPlayer;
import com.google.android.gms.analytics.Tracker;
import com.squalala.dzbac.ActivityModule;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.R;
import com.squalala.dzbac.common.BaseRxActivity;
import com.squalala.dzbac.DjihtiConstant;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.eventbus.events.AudioEvent;
import com.squalala.dzbac.ui.cards.MessageShowCard;
import com.squalala.dzbac.ui.comment.CommentaryActivity;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;
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
 * Nom du fichier : ShowConversationActivity.java
 * Date : 2 août 2014
 * 
 */
public class ShowConversationActivity extends BaseRxActivity
	implements ShowConversationView, OnClickListener, AudioRecorder.OnErrorListener {
	
	private CardArrayAdapter mCardArrayAdapter;
	private CardListView listView;
	private String idHeader, idReceveur;

	public static final int MIN_AUDIO_LENGTH_SECONDS = 2;

	public static final int SHOW_INDICATOR_DELAY_MILLIS = 300;

	List<ImageView> mIvVoiceIndicators;

	private AudioRecorder mAudioRecorder;

	private RxAudioPlayer mRxAudioPlayer;

	private File mAudioFile;

	private Subscription mRecordSubscription;

	private Queue<File> mAudioFiles = new LinkedList<>();

    @Inject
    SweetAlertDialog pDialog;


	@Inject
	Tracker tracker;

	@Inject
	MainPreferences preferences;
	
	private static final String TAG = "ShowConversation";

    @InjectView(R.id.tool_bar)
    Toolbar mToolbar;
	@Inject ShowConversationPresenter presenter;
	@Inject Handler handler;
	@InjectView(R.id.edit_and_send)
	LinearLayout linearLayoutComment;
	
	@InjectView(R.id.img_send_message_conversation) ImageView send_message;
	@InjectView(R.id.edit_message_conversation)
	EmojiconEditText edit_message;
	@InjectView(R.id.progressBar_list_conversation) ProgressBar progress;

	@InjectView(R.id.emoji_btn)
	ImageView emojiBtn;

	@InjectView(R.id.mFlIndicator)
	FrameLayout mFlIndicator;

	@InjectView(R.id.mTvRecordingHint)
	TextView mTvRecordingHint;

	@InjectView(R.id.root_view)
	View rootView;

    private Date dateLastMessage;

	private EmojIconActions emojIcon;

	private MediaPlayer mediaPlayer;
	private String urlAudio;
	private boolean audioReady;

	private CommentaryActivity.AudioListener mListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_conversation);

		tracker.setScreenName(getClass().getName());
		
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

		emojIcon = new EmojIconActions(this, rootView, edit_message, emojiBtn);
		emojIcon.setIconsIds(R.drawable.ic_keyboard_white_48dp, R.drawable.ic_tag_faces_white_48dp);
		emojIcon.ShowEmojIcon();


		setSupportActionBar(mToolbar);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		send_message.setOnClickListener(this);
		
		String subjet = getIntent().getExtras().getString("sujet");
		setTitle(subjet);
		
		/*tracker.send(new HitBuilders.EventBuilder()
		    .setCategory("Conversation")
		    .setAction("Item discuté")
		    .setLabel(subjet)
		    .setValue(1)
		    .build());*/
		
		idHeader = getIntent().getExtras().getString("id_header");
		idReceveur = getIntent().getExtras().getString("id_membre_2");


		String typeNotification = getIntent().getStringExtra("type_notification");

		if (typeNotification != null && typeNotification.equals("send_message"))
			Answers.getInstance().logCustom(new CustomEvent("Notification")
					.putCustomAttribute("Click", "Conversation"));

		Answers.getInstance().logContentView(new ContentViewEvent()
				.putContentName(subjet)
				.putContentType("Conversation")
				.putContentId(idHeader));
		
		ArrayList<Card> cards = new ArrayList<Card>();
		mCardArrayAdapter = new CardArrayAdapter(this ,cards);
	    listView = (CardListView) this.findViewById(R.id.myList);
	    
        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }

		edit_message.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				if (charSequence.toString().trim().length() > 0)
				{
					send_message.setImageResource(R.drawable.ic_send_white_24dp);
					linearLayoutComment.setBackgroundResource(R.color.bleu);
				}

				else {
					send_message.setImageResource(R.drawable.ic_mic_white_36dp);
					linearLayoutComment.setBackgroundResource(R.color.colorPrimary);
				}

			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});

		send_message.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (getMessage().isEmpty()) {

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
        
      //  listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        
     //   initLayout();
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
                                    if (getMessage().isEmpty()) {
                                        Toast.makeText(ShowConversationActivity.this, getString(R.string.rester_appuyer), Toast.LENGTH_LONG).show();
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

							new SweetAlertDialog(ShowConversationActivity.this, SweetAlertDialog.WARNING_TYPE)
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
                                                    .putCustomAttribute("Message", "Send"));

											presenter.uploadAudio(idHeader, idReceveur, typedFile);

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
        stopRepeatingTask();

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

            startRepeatingTask();
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
            startRepeatingTask();
		}
	};

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
	public void onStart() {
		super.onStart();
		EventBus.getDefault().register(this);
	}

	@Override
	public void onStop() {
		EventBus.getDefault().unregister(this);
		super.onStop();
	}

	@Override
	public void onError(@AudioRecorder.Error int error) {

	}

	@Override
	public void exit() {
		finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

			case android.R.id.home:

				setResult(RESULT_OK);
                finish();


				return true;


		}

		return super.onOptionsItemSelected(item);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.menu_conversation, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.e("ShowConversation", "onNewIntent");
		System.out.println("LAST ID ---> " + idHeader);
		idHeader = intent.getExtras().getString("id_header");
		idReceveur = intent.getExtras().getString("id_membre_2");
		String subjet = intent.getExtras().getString("sujet");
		
		System.out.println("NEW ID ---> " + idHeader);
		setTitle(subjet);

		String typeNotification = intent.getStringExtra("type_notification");

		if (typeNotification != null && typeNotification.equals("send_message"))
			Answers.getInstance().logCustom(new CustomEvent("Notification")
					.putCustomAttribute("Click", "Conversation"));
	}
	
	 @Override 
	  public void onResume() { 
	     startRepeatingTask();
	     super.onResume();
	  }
	 
	  @Override 
	  public void onPause() { 
	     stopRepeatingTask();
	     super.onPause(); 
	  } 
	
   private Runnable mStatusChecker = new Runnable() {
		    @Override  
		    public void run() { 
		      initLayout();
		      handler.postDelayed(mStatusChecker, DjihtiConstant.INTERVAL_CHECK);
		    } 
		  }; 
		  
    private void startRepeatingTask() {
	    mStatusChecker.run();
    } 
	 
    private void stopRepeatingTask() { 
	    handler.removeCallbacks(mStatusChecker);
    } 	  



	@Override
	protected void setupComponent(AppComponent appComponent) {
		DaggerShowConversationComponent.builder()
				.appComponent(appComponent)
				.showConversationModule(new ShowConversationModule(this))
				.activityModule(new ActivityModule(this))
				.build()
				.inject(this);
	}

	@DebugLog
	@Override
    public void onBackPressed() {
		presenter.onBackPressed();
        super.onBackPressed();
        setResult(RESULT_OK);
    }

    @Override
    public void setAnimationMessageSended() {
        YoYo.with(Techniques.RubberBand)
                .duration(700)
                .playOn(send_message);
    }

    private String getMessage() {
		return edit_message.getText().toString().trim();
	}
	
	private void initLayout() {
		
		progress.setVisibility(View.GONE);
		//mCardArrayAdapter.clear();
		presenter.getConversation(idHeader, 1);
	}


	@Override
	public void displayConversation(ArrayList<Card> cards) {
		
		mCardArrayAdapter.clear();

		if (cards != null) {
			mCardArrayAdapter.addAll(cards);

            if (dateLastMessage == null) {
                dateLastMessage = ((MessageShowCard) cards.get(cards.size() - 1)).getDateMessage();
                mCardArrayAdapter.notifyDataSetChanged();

                Log.e("Conv", "date saved " + dateLastMessage.getTime());
            }

            if (dateLastMessage != null) {
                Date last = ((MessageShowCard) cards.get(cards.size() - 1)).getDateMessage();
                if (last.after(dateLastMessage)) {

                    Log.e("Conv", "if date saved " + dateLastMessage.getTime());
                    Log.e("Conv", "if date last " + last.getTime());

                    dateLastMessage = last;

                  //  listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    mCardArrayAdapter.notifyDataSetChanged();

                    listView.setSelection(mCardArrayAdapter.getCount() - 1);
                  //  listView.setStackFromBottom(true);
                   // listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
                }
            }



		}
	}

	@Override
	public void showProgress() {
		setProgressBarIndeterminateVisibility(true);
	}

	@DebugLog
    @Override
    protected void onDestroy() {

        presenter.onDestroy();
		presenter = null;

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

		case R.id.img_send_message_conversation:

			Answers.getInstance().logCustom(new CustomEvent("Messaging")
					.putCustomAttribute("Click", "Send"));

			if (!getMessage().isEmpty() && !getMessage().contains("facebook.com")) {
				presenter.sendMessage(idHeader, idReceveur, getMessage());
				edit_message.setText("");
			}

			// TODO on retire pour le moment à cause des utilisateurs actifs qui baissent
		/*	if (preferences.isPrenium())
			{
				if (!getMessage().isEmpty()) {
					presenter.sendMessage(idHeader, idReceveur, getMessage());
					edit_message.setText("");
				}
			}
			else
			{
				DialogUtils.showPopup(this, getString(R.string.compte_prenium), getString(R.string.error_prenimum_message));
			}
			*/
			break;

		default:
			break;
		}
	}


    @Override
    public void showProgressDialog() {
        pDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        pDialog.dismiss();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
