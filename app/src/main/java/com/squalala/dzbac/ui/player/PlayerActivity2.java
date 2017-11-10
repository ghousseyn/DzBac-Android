package com.squalala.dzbac.ui.player;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Player.EventListener;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer.DecoderInitializationException;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.squalala.dzbac.DjihtiApp;
import com.squalala.dzbac.R;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.service.DownloadService;
import com.squalala.dzbac.ui.payement.PayementActivity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;
import hugo.weaving.DebugLog;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

/**
 * An activity that plays media using {@link SimpleExoPlayer}.
 */
public class PlayerActivity2 extends AppCompatActivity implements OnClickListener, EventListener,
    PlaybackControlView.VisibilityListener {

  public static final String DRM_SCHEME_UUID_EXTRA = "drm_scheme_uuid";
  public static final String DRM_LICENSE_URL = "drm_license_url";
  public static final String DRM_KEY_REQUEST_PROPERTIES = "drm_key_request_properties";
  public static final String PREFER_EXTENSION_DECODERS = "prefer_extension_decoders";

  public static final String ACTION_VIEW = "com.google.android.exoplayer.demo.action.VIEW";
  public static final String EXTENSION_EXTRA = "extension";

  public static final String ACTION_VIEW_LIST =
      "com.google.android.exoplayer.demo.action.VIEW_LIST";
  public static final String URI_LIST_EXTRA = "uri_list";
  public static final String EXTENSION_LIST_EXTRA = "extension_list";
  public static final String AD_TAG_URI_EXTRA = "ad_tag_uri";

  private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
  private static final CookieManager DEFAULT_COOKIE_MANAGER;
  static {
    DEFAULT_COOKIE_MANAGER = new CookieManager();
    DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
  }

  private Handler mainHandler;
  private EventLogger eventLogger;
  private SimpleExoPlayerView simpleExoPlayerView;
  private Button retryButton;

  private DataSource.Factory mediaDataSourceFactory;
  private SimpleExoPlayer player;
  private DefaultTrackSelector trackSelector;
  private TrackSelectionHelper trackSelectionHelper;
  private boolean inErrorState;
  private TrackGroupArray lastSeenTrackGroupArray;

  private boolean shouldAutoPlay;
  private int resumeWindow;
  private long resumePosition;

  public static final String URL_IMAGE = "url_image";
  public static final String FILENAME = "filename";

  // Fields used only for ad playback. The ads loader is loaded via reflection.

  private Object imaAdsLoader; // com.google.android.exoplayer2.ext.ima.ImaAdsLoader
  private Uri loadedAdTagUri;
  private ViewGroup adOverlayViewGroup;

  /**
   *  c'est l'avatr du user à la base
   */
  private String urlImageBackground;

  private String filename;

  private MainPreferences preferences;

  private ImageView imageBackground;
  private String path;
  private Uri contentUri;

  // Activity lifecycle

    @DebugLog
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);



    preferences = new MainPreferences(this);

    shouldAutoPlay = true;
    clearResumePosition();
    mediaDataSourceFactory = buildDataSourceFactory(true);
    mainHandler = new Handler();
    if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
      CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
    }



    setContentView(R.layout.player_activity2);


        Toolbar mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setBackgroundColor(Color.TRANSPARENT);
        getSupportActionBar().setTitle("");

    View rootView = findViewById(R.id.root);
    rootView.setOnClickListener(this);

    imageBackground = (ImageView) findViewById(R.id.image);
    retryButton = (Button) findViewById(R.id.retry_button);
    retryButton.setOnClickListener(this);

    simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
    simpleExoPlayerView.setControllerVisibilityListener(this);
    simpleExoPlayerView.requestFocus();
  }

  @DebugLog
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
      System.out.println("onCreateOptionsMenu");
    //   getMenuInflater().inflate(R.menu.viewpager_menu, menu);
    MenuInflater inflater = getMenuInflater();

    if (path != null)
      inflater.inflate(R.menu.menu_player_audio, menu);

    return super.onCreateOptionsMenu(menu);
  }

  private void downloadAudio() {

    Answers.getInstance().logCustom(new CustomEvent("Download")
            .putCustomAttribute("Click", "Audio"));


    Intent i= new Intent(this, DownloadService.class);
    i.putExtra("filename", filename);
    i.putExtra("url", contentUri.toString());
    i.putExtra("path", path);
    startService(i);
  }

  final PermissionCallback permissionAudioCallback = new PermissionCallback() {
    @Override
    public void permissionGranted() {
      //boolean hasAccess = Tools.accessContacts(MainActivity.this);
      //   Toast.makeText(MainActivity.this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();
      if (preferences.getPremiumCode() == 4 ||
              preferences.getPremiumCode() == 5 ||
              preferences.getPremiumCode() == 6) {
        downloadAudio();
      }
      else if (preferences.getLevelContribution() < 2) {
        new SweetAlertDialog(PlayerActivity2.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.oups))
                .setContentText(getString(R.string.alert_audio_contrib_error))
                .setCancelText(getString(R.string.ok))
                .setConfirmText(getString(R.string.en_savoir_plus))
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                  @Override
                  public void onClick(SweetAlertDialog sweetAlertDialog) {
                    startActivity(new Intent(PlayerActivity2.this, PayementActivity.class));
                  }
                })
                .show();
      }
      else {
        downloadAudio();
      }
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
      case R.id.menu_save_audio:

        if(Nammu.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
          //We have a permission, easy peasy
          //  boolean hasAccess = Tools.accessContacts(this);
          // Toast.makeText(this, "Access granted = " + hasAccess, Toast.LENGTH_SHORT).show();

          if (preferences.getPremiumCode() == 4 ||
                  preferences.getPremiumCode() == 5 ||
                  preferences.getPremiumCode() == 6) {
            downloadAudio();
          }
          else if (preferences.getLevelContribution() < 2) {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(this.getString(R.string.oups))
                    .setContentText(this.getString(R.string.alert_audio_contrib_error))
                    .setCancelText(this.getString(R.string.ok))
                    .setConfirmText(this.getString(R.string.en_savoir_plus))
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                      @Override
                      public void onClick(SweetAlertDialog sweetAlertDialog) {
                        startActivity(new Intent(PlayerActivity2.this, PayementActivity.class));
                      }
                    })
                    .show();
          }
          else {
            downloadAudio();
          }


        } else {
          //We do not own this permission
          if (Nammu.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //User already refused to give us this permission or removed it
            //Now he/she can mark "never ask again" (sic!)
            Snackbar.make(this.findViewById(android.R.id.content), getString(R.string.permission_photo_enregistrement),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.ok), new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                        Nammu.askForPermission(PlayerActivity2.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, permissionAudioCallback);
                      }
                    }).show();
          } else {
            //First time asking for permission
            // or phone doesn't offer permission
            // or user marked "never ask again"
            Nammu.askForPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, permissionAudioCallback);
          }
        }



        break;

      case android.R.id.home:

        finish();

        return true;
    }

    return super.onOptionsItemSelected(item);
  }


  @Override
  public void onNewIntent(Intent intent) {
    releasePlayer();
    shouldAutoPlay = true;
    clearResumePosition();
    setIntent(intent);
  }

  @Override
  public void onStart() {
    super.onStart();
    if (Util.SDK_INT > 23) {
      initializePlayer();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    if ((Util.SDK_INT <= 23 || player == null)) {
      initializePlayer();
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    if (Util.SDK_INT <= 23) {
      releasePlayer();
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    if (Util.SDK_INT > 23) {
      releasePlayer();
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    releaseAdsLoader();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      initializePlayer();
    } else {
      showToast(R.string.storage_permission_denied);
      finish();
    }
  }

  // Activity input

  @Override
  public boolean dispatchKeyEvent(KeyEvent event) {
    // If the event was not handled then see if the player view can handle it.
    return super.dispatchKeyEvent(event) || simpleExoPlayerView.dispatchKeyEvent(event);
  }

  // OnClickListener methods

  @Override
  public void onClick(View view) {
    if (view == retryButton) {
      initializePlayer();
    }
  }

  // PlaybackControlView.VisibilityListener implementation

  @Override
  public void onVisibilityChange(int visibility) {
  }

  // Internal methods

    @DebugLog
  private void initializePlayer() {
    Intent intent = getIntent();
        contentUri = intent.getData();
      urlImageBackground = getIntent().getStringExtra(URL_IMAGE);
      filename = getIntent().getStringExtra(FILENAME);
      path = getIntent().getStringExtra("path");

      if (urlImageBackground != null) {
          imageBackground.setVisibility(View.VISIBLE);
          Glide.with(this).load(urlImageBackground).fitCenter().into(imageBackground);
      }
      else {
          imageBackground.setVisibility(View.GONE);
      }

    boolean needNewPlayer = player == null;
    if (needNewPlayer) {
      TrackSelection.Factory adaptiveTrackSelectionFactory =
          new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
      trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
      trackSelectionHelper = new TrackSelectionHelper(trackSelector, adaptiveTrackSelectionFactory);
      lastSeenTrackGroupArray = null;
      eventLogger = new EventLogger(trackSelector);

      UUID drmSchemeUuid = intent.hasExtra(DRM_SCHEME_UUID_EXTRA)
          ? UUID.fromString(intent.getStringExtra(DRM_SCHEME_UUID_EXTRA)) : null;
      DrmSessionManager<FrameworkMediaCrypto> drmSessionManager = null;
      if (drmSchemeUuid != null) {
        String drmLicenseUrl = intent.getStringExtra(DRM_LICENSE_URL);
        String[] keyRequestPropertiesArray = intent.getStringArrayExtra(DRM_KEY_REQUEST_PROPERTIES);
        int errorStringId = R.string.error_drm_unknown;
        if (Util.SDK_INT < 18) {
          errorStringId = R.string.error_drm_not_supported;
        } else {
          try {
            drmSessionManager = buildDrmSessionManagerV18(drmSchemeUuid, drmLicenseUrl,
                keyRequestPropertiesArray);
          } catch (UnsupportedDrmException e) {
            errorStringId = e.reason == UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME
                ? R.string.error_drm_unsupported_scheme : R.string.error_drm_unknown;
          }
        }
        if (drmSessionManager == null) {
          showToast(errorStringId);
          return;
        }
      }

      boolean preferExtensionDecoders = intent.getBooleanExtra(PREFER_EXTENSION_DECODERS, false);
      @DefaultRenderersFactory.ExtensionRendererMode int extensionRendererMode =
          ((DjihtiApp) getApplication()).useExtensionRenderers()
              ? (preferExtensionDecoders ? DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
              : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
              : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF;
      DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(this,
          drmSessionManager, extensionRendererMode);

      player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector);
      player.addListener(this);
      player.addListener(eventLogger);
      player.setAudioDebugListener(eventLogger);
      player.setVideoDebugListener(eventLogger);
      player.setMetadataOutput(eventLogger);

      simpleExoPlayerView.setPlayer(player);
      player.setPlayWhenReady(shouldAutoPlay);
    }
    String action = intent.getAction();
    Uri[] uris;
    String[] extensions;
    if (ACTION_VIEW.equals(action)) {
      uris = new Uri[]{intent.getData()};
      extensions = new String[]{intent.getStringExtra(EXTENSION_EXTRA)};
    } else if (ACTION_VIEW_LIST.equals(action)) {
      String[] uriStrings = intent.getStringArrayExtra(URI_LIST_EXTRA);
      uris = new Uri[uriStrings.length];
      for (int i = 0; i < uriStrings.length; i++) {
        uris[i] = Uri.parse(uriStrings[i]);
      }
      extensions = intent.getStringArrayExtra(EXTENSION_LIST_EXTRA);
      if (extensions == null) {
        extensions = new String[uriStrings.length];
      }
    } else {
      showToast(getString(R.string.unexpected_intent_action, action));
      return;
    }
    if (Util.maybeRequestReadExternalStoragePermission(this, uris)) {
      // The player will be reinitialized if the permission is granted.
      return;
    }
    MediaSource[] mediaSources = new MediaSource[uris.length];
    for (int i = 0; i < uris.length; i++) {
      mediaSources[i] = buildMediaSource(uris[i], extensions[i]);
    }
    MediaSource mediaSource = mediaSources.length == 1 ? mediaSources[0]
        : new ConcatenatingMediaSource(mediaSources);
    String adTagUriString = intent.getStringExtra(AD_TAG_URI_EXTRA);
    if (adTagUriString != null) {
      Uri adTagUri = Uri.parse(adTagUriString);
      if (!adTagUri.equals(loadedAdTagUri)) {
        releaseAdsLoader();
        loadedAdTagUri = adTagUri;
      }
      try {
        mediaSource = createAdsMediaSource(mediaSource, Uri.parse(adTagUriString));
      } catch (Exception e) {
        showToast(R.string.ima_not_loaded);
      }
    } else {
      releaseAdsLoader();
    }
    boolean haveResumePosition = resumeWindow != C.INDEX_UNSET;
    if (haveResumePosition) {
      player.seekTo(resumeWindow, resumePosition);
    }
    player.prepare(mediaSource, !haveResumePosition, false);
    inErrorState = false;
    updateButtonVisibilities();
  }

  private MediaSource buildMediaSource(Uri uri, String overrideExtension) {
    int type = TextUtils.isEmpty(overrideExtension) ? Util.inferContentType(uri)
        : Util.inferContentType("." + overrideExtension);
    switch (type) {
      case C.TYPE_SS:
        return new SsMediaSource(uri, buildDataSourceFactory(false),
            new DefaultSsChunkSource.Factory(mediaDataSourceFactory), mainHandler, eventLogger);
      case C.TYPE_DASH:
        return new DashMediaSource(uri, buildDataSourceFactory(false),
            new DefaultDashChunkSource.Factory(mediaDataSourceFactory), mainHandler, eventLogger);
      case C.TYPE_HLS:
        return new HlsMediaSource(uri, mediaDataSourceFactory, mainHandler, eventLogger);
      case C.TYPE_OTHER:
        return new ExtractorMediaSource(uri, mediaDataSourceFactory, new DefaultExtractorsFactory(),
            mainHandler, eventLogger);
      default: {
        throw new IllegalStateException("Unsupported type: " + type);
      }
    }
  }

  private DrmSessionManager<FrameworkMediaCrypto> buildDrmSessionManagerV18(UUID uuid,
                                                                            String licenseUrl, String[] keyRequestPropertiesArray) throws UnsupportedDrmException {
    HttpMediaDrmCallback drmCallback = new HttpMediaDrmCallback(licenseUrl,
        buildHttpDataSourceFactory(false));
    if (keyRequestPropertiesArray != null) {
      for (int i = 0; i < keyRequestPropertiesArray.length - 1; i += 2) {
        drmCallback.setKeyRequestProperty(keyRequestPropertiesArray[i],
            keyRequestPropertiesArray[i + 1]);
      }
    }
    return new DefaultDrmSessionManager<>(uuid, FrameworkMediaDrm.newInstance(uuid), drmCallback,
        null, mainHandler, eventLogger);
  }

  private void releasePlayer() {
    if (player != null) {
     // debugViewHelper.stop();
      shouldAutoPlay = player.getPlayWhenReady();
      updateResumePosition();
      player.release();
      player = null;
      trackSelector = null;
      trackSelectionHelper = null;
      eventLogger = null;
    }
  }

  private void updateResumePosition() {
    resumeWindow = player.getCurrentWindowIndex();
    resumePosition = Math.max(0, player.getContentPosition());
  }

  private void clearResumePosition() {
    resumeWindow = C.INDEX_UNSET;
    resumePosition = C.TIME_UNSET;
  }

  /**
   * Returns a new DataSource factory.
   *
   * @param useBandwidthMeter Whether to set {@link #BANDWIDTH_METER} as a listener to the new
   *     DataSource factory.
   * @return A new DataSource factory.
   */
  private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
    return ((DjihtiApp) getApplication())
        .buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
  }

  /**
   * Returns a new HttpDataSource factory.
   *
   * @param useBandwidthMeter Whether to set {@link #BANDWIDTH_METER} as a listener to the new
   *     DataSource factory.
   * @return A new HttpDataSource factory.
   */
  private HttpDataSource.Factory buildHttpDataSourceFactory(boolean useBandwidthMeter) {
    return ((DjihtiApp) getApplication())
        .buildHttpDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
  }

  /**
   * Returns an ads media source, reusing the ads loader if one exists.
   *
   * @throws Exception Thrown if it was not possible to create an ads media source, for example, due
   *     to a missing dependency.
   */
  private MediaSource createAdsMediaSource(MediaSource mediaSource, Uri adTagUri) throws Exception {
    // Load the extension source using reflection so the demo app doesn't have to depend on it.
    // The ads loader is reused for multiple playbacks, so that ad playback can resume.
    Class<?> loaderClass = Class.forName("com.google.android.exoplayer2.ext.ima.ImaAdsLoader");
    if (imaAdsLoader == null) {
      imaAdsLoader = loaderClass.getConstructor(Context.class, Uri.class)
          .newInstance(this, adTagUri);
      adOverlayViewGroup = new FrameLayout(this);
      // The demo app has a non-null overlay frame layout.
      simpleExoPlayerView.getOverlayFrameLayout().addView(adOverlayViewGroup);
    }
    Class<?> sourceClass =
        Class.forName("com.google.android.exoplayer2.ext.ima.ImaAdsMediaSource");
    Constructor<?> constructor = sourceClass.getConstructor(MediaSource.class,
        DataSource.Factory.class, loaderClass, ViewGroup.class);
    return (MediaSource) constructor.newInstance(mediaSource, mediaDataSourceFactory, imaAdsLoader,
        adOverlayViewGroup);
  }

  private void releaseAdsLoader() {
    if (imaAdsLoader != null) {
      try {
        Class<?> loaderClass = Class.forName("com.google.android.exoplayer2.ext.ima.ImaAdsLoader");
        Method releaseMethod = loaderClass.getMethod("release");
        releaseMethod.invoke(imaAdsLoader);
      } catch (Exception e) {
        // Should never happen.
        throw new IllegalStateException(e);
      }
      imaAdsLoader = null;
      loadedAdTagUri = null;
      simpleExoPlayerView.getOverlayFrameLayout().removeAllViews();
    }
  }

  // Player.EventListener implementation

  @Override
  public void onLoadingChanged(boolean isLoading) {
    // Do nothing.
  }

  @Override
  public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
    if (playbackState == Player.STATE_ENDED) {
   //   showControls();
    }
    updateButtonVisibilities();
  }

  @Override
  public void onRepeatModeChanged(int repeatMode) {
    // Do nothing.
  }

  @Override
  public void onPositionDiscontinuity() {
    if (inErrorState) {
      // This will only occur if the user has performed a seek whilst in the error state. Update the
      // resume position so that if the user then retries, playback will resume from the position to
      // which they seeked.
      updateResumePosition();
    }
  }

  @Override
  public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
    // Do nothing.
  }

  @Override
  public void onTimelineChanged(Timeline timeline, Object manifest) {
    // Do nothing.
  }

  @Override
  public void onPlayerError(ExoPlaybackException e) {
    String errorString = null;
    if (e.type == ExoPlaybackException.TYPE_RENDERER) {
      Exception cause = e.getRendererException();
      if (cause instanceof DecoderInitializationException) {
        // Special case for decoder initialization failures.
        DecoderInitializationException decoderInitializationException =
            (DecoderInitializationException) cause;
        if (decoderInitializationException.decoderName == null) {
          if (decoderInitializationException.getCause() instanceof DecoderQueryException) {
            errorString = getString(R.string.error_querying_decoders);
          } else if (decoderInitializationException.secureDecoderRequired) {
            errorString = getString(R.string.error_no_secure_decoder,
                decoderInitializationException.mimeType);
          } else {
            errorString = getString(R.string.error_no_decoder,
                decoderInitializationException.mimeType);
          }
        } else {
          errorString = getString(R.string.error_instantiating_decoder,
              decoderInitializationException.decoderName);
        }
      }
    }
    if (errorString != null) {
      showToast(errorString);
    }
    inErrorState = true;
    if (isBehindLiveWindow(e)) {
      clearResumePosition();
      initializePlayer();
    } else {
      updateResumePosition();
      updateButtonVisibilities();
    }
  }

  @Override
  @SuppressWarnings("ReferenceEquality")
  public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    updateButtonVisibilities();
    if (trackGroups != lastSeenTrackGroupArray) {
      MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
      if (mappedTrackInfo != null) {
        if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_VIDEO)
            == MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
          showToast(R.string.error_unsupported_video);
        }
        if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_AUDIO)
            == MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
          showToast(R.string.error_unsupported_audio);
        }
      }
      lastSeenTrackGroupArray = trackGroups;
    }
  }

  // User controls

  private void updateButtonVisibilities() {

    retryButton.setVisibility(inErrorState ? View.VISIBLE : View.GONE);

    if (player == null) {
      return;
    }

    MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
    if (mappedTrackInfo == null) {
      return;
    }

    for (int i = 0; i < mappedTrackInfo.length; i++) {
      TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
      if (trackGroups.length != 0) {
        Button button = new Button(this);
        int label;
        switch (player.getRendererType(i)) {
          case C.TRACK_TYPE_AUDIO:
            label = R.string.audio;
            break;
          case C.TRACK_TYPE_VIDEO:
            label = R.string.video;
            break;
          case C.TRACK_TYPE_TEXT:
            label = R.string.text;
            break;
          default:
            continue;
        }
        button.setText(label);
        button.setTag(i);
        button.setOnClickListener(this);
      }
    }
  }



  private void showToast(int messageId) {
    showToast(getString(messageId));
  }

  private void showToast(String message) {
    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
  }

  private static boolean isBehindLiveWindow(ExoPlaybackException e) {
    if (e.type != ExoPlaybackException.TYPE_SOURCE) {
      return false;
    }
    Throwable cause = e.getSourceException();
    while (cause != null) {
      if (cause instanceof BehindLiveWindowException) {
        return true;
      }
      cause = cause.getCause();
    }
    return false;
  }

}
