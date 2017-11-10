package com.squalala.dzbac.data.api;

import android.app.Application;

import com.squalala.dzbac.DjihtiConstant;
import com.squalala.dzbac.data.prefs.MainPreferences;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ApiModule.java
 * Date : 21 juin 2014
 * 
 */
@Module
public final class ApiModule {


	@Provides @Singleton
	  Retrofit provideRestAdapter(final Application app, MainPreferences mainPreferences) {

	/*	  int cacheSize = 10 * 1024 * 1024; // 10 MiB
		  File cacheDirectory = new File(app.getCacheDir().getAbsolutePath(), "HttpCache");
		  Cache cache;
		  cache = new Cache(cacheDirectory, cacheSize);
		  OkHttpClient client = new OkHttpClient();
		  client.setCache(cache);
		  client.interceptors().add(new LoggingInterceptor(mainPreferences));
		  client.setConnectTimeout(20, TimeUnit.SECONDS); // connect timeout
		  client.setReadTimeout(60 * 2, TimeUnit.SECONDS);    // socket timeout
		  client.setWriteTimeout(60 * 5, TimeUnit.SECONDS); */

        //.setLogLevel(RestAdapter.LogLevel.FULL).setLog(new AndroidLog("DzBacL"))
				/*  .setRequestInterceptor(new RequestInterceptor() {
					  @Override
					  public void intercept(RequestFacade request) {

						  request.addHeader("Accept", "application/json;versions=1");

						  if (ConnectionDetector.isConnectingToInternet(app)) {
							  int maxAge = 60; // read from cache for 1 minute
							  request.addHeader("Cache-Control", "public, max-age=" + maxAge);
						  } else {
							  int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
							  request.addHeader("Cache-Control",
									  "public, only-if-cached, max-stale=" + maxStale);
						  }
					  }
				  })*/ //		.setClient(new OkClient(client))
       //   OkHttpClient client = new OkHttpClient();

        LoggingInterceptor interceptor = new LoggingInterceptor(mainPreferences);
		HttpLoggingInterceptor interceptorHttp = new HttpLoggingInterceptor();
		interceptorHttp.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.connectTimeout(6, TimeUnit.SECONDS); // connect timeout
		builder.readTimeout(60 * 5, TimeUnit.SECONDS);    // socket timeout // 2
		builder.writeTimeout(60 * 10, TimeUnit.SECONDS); //5

		builder.interceptors().add(interceptor);

		builder.interceptors().add(interceptorHttp);
        OkHttpClient client = builder.build();

		  return new Retrofit.Builder()
                  .baseUrl(DjihtiConstant.API_URL)
                  .addConverterFactory(GsonConverterFactory.create())
                  .client(client)
                  .build();
	  }

	  @Provides
	  @Singleton
	  FacebookService provideRegisterFacebookService(Retrofit restAdapter) {
		return restAdapter.create(FacebookService.class);
	  }

	  @Provides
	  @Singleton
	  RegisterService provideRegisterService(Retrofit restAdapter) {
		return restAdapter.create(RegisterService.class);
	  }

	  @Provides
	  @Singleton
	  LoginService provideLoginService(Retrofit restAdapter) {
		return restAdapter.create(LoginService.class);
	  }

	  @Provides
	  @Singleton
	  CheckService provideCheckService(Retrofit restAdapter) {
		return restAdapter.create(CheckService.class);
	  }

	  @Provides
	  @Singleton
	  PostsService provideItemsService(Retrofit restAdapter) {
		  return restAdapter.create(PostsService.class);
	  }

	  @Provides
	  @Singleton
	  ItemService provideItemService(Retrofit restAdapter) {
		  return restAdapter.create(ItemService.class);
	  }

	  @Provides
	  @Singleton
	  MessagingService provideMessagingService(Retrofit restAdapter) {
		  return restAdapter.create(MessagingService.class);
	  }

	  @Provides
	  @Singleton
	  SignalisationService provideSignalisationService(Retrofit restAdapter) {
		  return restAdapter.create(SignalisationService.class);
	  }

	  @Provides
	  @Singleton
	  LikeService provideLikeService(Retrofit restAdapter) {
		  return restAdapter.create(LikeService.class);
	  }

	  @Provides
	  @Singleton
	  CommentaryService provideCommentaryService(Retrofit restAdapter) {
		  return restAdapter.create(CommentaryService.class);
	  }

	  @Provides
	  @Singleton
	  UserService provideUserService(Retrofit restAdapter) {
		  return restAdapter.create(UserService.class);
	  }

	  @Provides
	  @Singleton
	  UploadService provideUploadService(Retrofit restAdapter) {
		  return restAdapter.create(UploadService.class);
	  }

	  @Provides
	  @Singleton
	  NotificationService provideNotificationService(Retrofit restAdapter) {
		  return restAdapter.create(NotificationService.class);
	  }

	  @Provides
	  @Singleton
	  AdminService provideAdminService(Retrofit restAdapter) {
		return restAdapter.create(AdminService.class);
	  }

	  @Provides
	  @Singleton
	  GoogleService provideGoogleService(Retrofit restAdapter) {
		  return restAdapter.create(GoogleService.class);
	  }

}
