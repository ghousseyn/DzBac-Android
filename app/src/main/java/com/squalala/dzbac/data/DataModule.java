/**
 * 
 */
package com.squalala.dzbac.data;

import android.app.Application;

import com.squalala.dzbac.ui.alert_dialog.PhotoManagerAlert;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.utils.Translater;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : DataModule.java
 * Date : 21 juin 2014
 * 
 */
@Module
public final class DataModule {

   /* @Provides @Singleton SharedPreferences provideSharedPreferences(Application app) {
	  return app.getSharedPreferences("dzbac", MODE_PRIVATE);
    }*/

    @Provides @Singleton
    Translater provideTranslater(Application app) {
        return new Translater(app.getApplicationContext());
    }

    @Provides
    public PhotoManagerAlert providePhotoManagerAlert() {
        return new PhotoManagerAlert();
    }

    @Provides @Singleton
    MainPreferences provideMainPreferences(Application application) {
        return new MainPreferences(application);
    }


}
