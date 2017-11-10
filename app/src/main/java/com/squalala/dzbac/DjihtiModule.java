package com.squalala.dzbac;

import android.app.Application;
import android.content.Context;
import android.net.Uri;

import com.squareup.picasso.Cache;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DjihtiModule {

    private DjihtiApp app;

    public DjihtiModule(DjihtiApp app) {
        this.app = app;
    }

    @Provides @Singleton
    public Application provideApplication() {
        return app;
    }

    @Provides @Singleton
    public Context provideContext() {
        return app.getApplicationContext();
    }



}
