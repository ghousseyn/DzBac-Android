package com.squalala.dzbac;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.digits.sdk.android.Digits;
import com.facebook.FacebookSdk;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squalala.dzbac.analytics.AnalyticsManager;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squareup.leakcanary.RefWatcher;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import java.net.URISyntaxException;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;
import pl.tajchert.nammu.Nammu;


public class DjihtiApp extends MultiDexApplication {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "f8fM5OwRiSfTu7uPoK1pz9m7S";
    private static final String TWITTER_SECRET = "eprZAc3ar3zUWDtbIyjJZsv2MyCWyAIekOoChQJFDYalKStoM0";

    private AppComponent component;
    public static GoogleAnalytics analytics;

    //private ObjectGraph objectGraph;
    @Inject AnalyticsManager analyticsManager;
    @Inject public static Tracker tracker;
    @Inject
    MainPreferences mainPreferences;

    protected String userAgent;

    private RefWatcher refWatcher;


 /*   public static RefWatcher getRefWatcher(Context context) {
        DjihtiApp application = (DjihtiApp) context.getApplicationContext();
        return application.refWatcher;
    } */

    @Override public void onCreate() {
        super.onCreate();
        userAgent = Util.getUserAgent(this, "ExoPlayerDemo");

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Crashlytics(), new TwitterCore(authConfig), new Digits());

        setupGraph();
        
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Launch")
                .setAction("Lancement de l'application")
                .setValue(1)
                .build());

        mainPreferences.setDefaultPreferences();

        FacebookSdk.sdkInitialize(getApplicationContext());

//        refWatcher = LeakCanary.install(this);
        Nammu.init(getApplicationContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static DjihtiApp get(Context context) {
        return (DjihtiApp) context.getApplicationContext();
    }

    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(this, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
    }

    public boolean useExtensionRenderers() {
        return BuildConfig.FLAVOR.equals("withExtensions");
    }


    public AppComponent component() {
        return component;
    }

    private void setupGraph() {
        component = DaggerAppComponent.builder()
                .djihtiModule(new DjihtiModule(this))
                .build();
        component.inject(this);
    }


}
