package com.squalala.dzbac.ui.main;

import android.app.Activity;
import android.app.Application;

import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.interactors.UserInteractor;
import com.squalala.dzbac.ui.list_items.PostsFragment;
import com.squalala.dzbac.utils.NoteAppReminder;

import dagger.Module;
import dagger.Provides;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : MainModule.java
 * Date : 3 juil. 2014
 * 
 */

@Module
public class MainModule {
	
	private MainView view;

    public MainModule(MainView view) {
        this.view = view;
    }

    @Provides public MainView provideView() {
        return view;
    }
    
    @Provides
    public MainPresenter providePresenter(MainView MainView, UserInteractor interactor, Application app, MainPreferences preferences) {
        return new MainPresenterImpl(MainView, interactor, app.getApplicationContext(), preferences);
    }

    @Provides
    public NoteAppReminder provideNoteAppReminder(Activity activity, MainPreferences mainPreferences) {
        return new NoteAppReminder(activity, mainPreferences);
    }


    @Provides
    public PostsFragment providePostsFragment() {
        return new PostsFragment();
    }
    


}
