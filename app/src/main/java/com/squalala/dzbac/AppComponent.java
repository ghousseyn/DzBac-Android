package com.squalala.dzbac;

import android.app.Application;

import com.google.android.gms.analytics.Tracker;
import com.squalala.dzbac.analytics.AnalyticsModule;
import com.squalala.dzbac.data.DataModule;
import com.squalala.dzbac.data.api.ApiModule;
import com.squalala.dzbac.data.api.CommentaryService;
import com.squalala.dzbac.data.api.FacebookService;
import com.squalala.dzbac.data.api.LikeService;
import com.squalala.dzbac.data.api.MessagingService;
import com.squalala.dzbac.data.api.SignalisationService;
import com.squalala.dzbac.data.api.UserService;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.interactors.AdminInteractor;
import com.squalala.dzbac.interactors.CommentaryInteractor;
import com.squalala.dzbac.interactors.InteractorsModule;
import com.squalala.dzbac.interactors.ItemInteractor;
import com.squalala.dzbac.interactors.ListItemInteractor;
import com.squalala.dzbac.interactors.LoginInteractor;
import com.squalala.dzbac.interactors.MainInteractor;
import com.squalala.dzbac.interactors.MessagingInteractor;
import com.squalala.dzbac.interactors.NotificationInteractor;
import com.squalala.dzbac.interactors.SignInteractor;
import com.squalala.dzbac.interactors.SplashInteractor;
import com.squalala.dzbac.interactors.UserInteractor;
import com.squalala.dzbac.service.GcmIntentService;
import com.squalala.dzbac.settings.SettingsActivity;
import com.squalala.dzbac.ui.alert_dialog.PhotoManagerAlert;
import com.squalala.dzbac.utils.Translater;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Back Packer
 * Date : 30/04/15
 */
@Singleton
@Component(
        modules = {
                DjihtiModule.class,
                InteractorsModule.class,
                AnalyticsModule.class,
                DataModule.class,
                ApiModule.class
        }
)
public interface AppComponent {
        void inject(DjihtiApp app);
        void inject(GcmIntentService app);
        void inject(SettingsActivity app);

        Application getApplication();
        LoginInteractor getLoginInteractor();
        CommentaryInteractor getCommentaryInteractor();
        ListItemInteractor getListItemInteractor();
        MainInteractor getMainInteractor();
        NotificationInteractor getNotificationInteractor();
        SignInteractor getSignInteractor();
        SplashInteractor getSplashInteractor();
        UserInteractor getUserInteractor();
        MessagingInteractor getMessagingInteractor();
        ItemInteractor getItemInteractor();
        AdminInteractor getAdminInteractor();

        Translater getTranslater();

        MainPreferences getMainPreferences();



        PhotoManagerAlert getPhotoManagerAlert();

        Tracker getTracker();

    FacebookService getFacebookService();
    MessagingService getMessagingService();
    LikeService getLikeService();
    SignalisationService getSignalisationService();
    CommentaryService getCommentaryService();
    UserService getUserService();

    /*

        FacebookService getFacebookService();
        RegisterService getRegisterService();
        LoginService getLoginService();
        CheckService getCheckService();
        PostsService getPostsService();
        ItemService getItemService();




        UploadService getUploadService();
        ContactService getContactService();
        NotificationService getNotificationService();
        GoogleService getGoogleService();*/


}
