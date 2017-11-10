
package com.squalala.dzbac.analytics;

import android.app.Application;

import com.google.android.gms.analytics.Tracker;
import com.squalala.dzbac.analytics.AnalyticsManager.TrackerName;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AnalyticsModule {

    @Provides @Singleton public AnalyticsManager provideAnalyticsManager(Application app){
        return new AnalyticsManager(app);
    }
    
    @Provides @Singleton
    public Tracker provideTracker(AnalyticsManager analyticsManager) {
    	return analyticsManager.getTracker(TrackerName.APP_TRACKER);
    }

}
