package com.squalala.dzbac.interactors;

import android.app.Application;
import android.content.Context;

import com.squalala.dzbac.data.api.AdminService;
import com.squalala.dzbac.data.api.CheckService;
import com.squalala.dzbac.data.api.CommentaryService;
import com.squalala.dzbac.data.api.GoogleService;
import com.squalala.dzbac.data.api.ItemService;
import com.squalala.dzbac.data.api.LoginService;
import com.squalala.dzbac.data.api.MessagingService;
import com.squalala.dzbac.data.api.NotificationService;
import com.squalala.dzbac.data.api.PostsService;
import com.squalala.dzbac.data.api.RegisterService;
import com.squalala.dzbac.data.api.UploadService;
import com.squalala.dzbac.data.api.UserService;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.utils.Translater;

import dagger.Module;
import dagger.Provides;

@Module
public class InteractorsModule {

	@Provides
	public SplashInteractor provideSplashInteractor(MainPreferences mainPreferences,
			Context context) {
		return new SplashInteractorImpl(mainPreferences, context);
	}

	@Provides
	public AdminInteractor provideAdminInteractor(AdminService service) {
		return new AdminInteractorImpl(service);
	}
	
	@Provides
	public SignInteractor provideSignInteractor(CheckService checkService, 
			RegisterService registerService, MainPreferences mainPreferences,
			GoogleService googleService) {
		return new SignInteractorImpl(checkService, registerService, googleService, mainPreferences);
	}
	
	@Provides
	public LoginInteractor provideLoginInteractor(LoginService loginService, 
			GoogleService googleService, MainPreferences mainPreferences) {
		return new LoginInteractorImpl(loginService, googleService, mainPreferences);
	}
	
	@Provides
	public MainInteractor provideMainInteractor() {
		return new MainInteractorImpl();
	}
	
	@Provides
	public ItemInteractor provideShowItemInteractor(ItemService itemService, 
			UploadService uploadService,
			MainPreferences mainPreferences,
            Context context) {
		return new ItemInteractorImpl(itemService,
				uploadService,
				mainPreferences,
                context);
	}
	
	@Provides
	public CommentaryInteractor provideCommentaryInteractor(CommentaryService service,
                                                            UploadService uploadService) {
		return new CommentaryInteractorImpl(service,
                uploadService);
	}
	
	@Provides 
	public MessagingInteractor provideMessagingInteractor(MessagingService service, UploadService uploadService) {
		return new MessagingInteractorImpl(service, uploadService);
	}
	
	@Provides 
	public NotificationInteractor provideNotificationInteractor(NotificationService service) {
		return new NotificationInteractorImpl(service);
	}
	
	@Provides
	public UserInteractor provideShowProfileInteractor(UserService userService, 
			UploadService uploadService,
			MessagingService messagingService,
			MainPreferences mainPreferences,
			Application app) {
		return new UserInteractorImpl(userService,
				uploadService,
				messagingService,
				mainPreferences,
				app.getApplicationContext());
	}
	
	@Provides
	public ListItemInteractor provideListItemInteractor(PostsService itemsService
			, MainPreferences mainPreferences, Translater translater, Context context) {
		return new ListItemInteractorImpl(itemsService, mainPreferences, translater, context);
	}
	
				
}
