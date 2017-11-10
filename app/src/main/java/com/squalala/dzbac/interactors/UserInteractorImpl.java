package com.squalala.dzbac.interactors;

import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.squalala.dzbac.data.UserInformations;
import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.data.api.ApiResponse.BasicResponse;
import com.squalala.dzbac.data.api.ApiResponse.FilesResponse;
import com.squalala.dzbac.data.api.ApiResponse.User;
import com.squalala.dzbac.data.api.MessagingService;
import com.squalala.dzbac.data.api.UploadService;
import com.squalala.dzbac.data.api.UserService;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.ui.friends.FriendsListener;
import com.squalala.dzbac.ui.list_followers.ListFollowerListener;
import com.squalala.dzbac.ui.main.MainListener;
import com.squalala.dzbac.ui.ranking.RankingListener;
import com.squalala.dzbac.ui.show_profile.ShowProfileListener;

import java.util.ArrayList;

import hugo.weaving.DebugLog;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ShowProfileInteractorImpl.java
 * Date : 31 juil. 2014
 * 
 */
public class UserInteractorImpl implements UserInteractor {
	
	private UserService userService;
	private MessagingService messagingService;
	private UploadService uploadService;
	private MainPreferences mainPreferences;
	private Context contextApp;

    private static String TAG = UserInteractorImpl.class.getSimpleName();
	
	public UserInteractorImpl(UserService userService,
			UploadService uploadService,
			MessagingService messagingService,
			MainPreferences mainPreferences, Context context) {
		this.userService = userService;
		this.mainPreferences = mainPreferences;
		this.uploadService = uploadService;
		this.messagingService = messagingService;
		this.contextApp = context;
	}

	@Override
	public void getFollowers(int page, String idMembre, final ListFollowerListener listener) {

		userService.getFollowers(page, idMembre).enqueue(new retrofit2.Callback<ApiResponse.Users>() {
			@Override
			public void onResponse(Call<ApiResponse.Users> call, retrofit2.Response<ApiResponse.Users> response) {
				listener.onLoadListUsers(response.body());
			}

			@Override
			public void onFailure(Call<ApiResponse.Users> call, Throwable t) {

			}
		});

	}

	@Override
	public void getMyFollowings(int page, final ListFollowerListener listener) {

		userService.getMyFollowings(page).enqueue(new retrofit2.Callback<ApiResponse.Users>() {
			@Override
			public void onResponse(Call<ApiResponse.Users> call, retrofit2.Response<ApiResponse.Users> response) {
				listener.onLoadListUsers(response.body());
			}

			@Override
			public void onFailure(Call<ApiResponse.Users> call, Throwable t) {

			}
		});

	}

	@Override
	public void getDataUser(final String idMembre, final ShowProfileListener listener) {
		
		userService.getData(idMembre).enqueue(new retrofit2.Callback<User>() {
			@Override
			public void onResponse(Call<User> call, retrofit2.Response<User> response) {
				if (response.body() != null)
					listener.onLoadData(response.body());
			}

			@Override
			public void onFailure(Call<User> call, Throwable t) {

			}
		});
		
	}

	@Override
	public void followUser(String idFollowed, final ShowProfileListener mListener) {

		userService.follow(idFollowed).enqueue(new retrofit2.Callback<BasicResponse>() {
			@Override
			public void onResponse(Call<BasicResponse> call, retrofit2.Response<BasicResponse> response) {
				if (response.body() != null)
					mListener.onUserFollowed(response.body().message);
			}

			@Override
			public void onFailure(Call<BasicResponse> call, Throwable t) {

			}
		});
	}

	@Override
	public void unFollowUser(String idFollowed, final ShowProfileListener mListener) {

		userService.unFollow(idFollowed).enqueue(new retrofit2.Callback<BasicResponse>() {
			@Override
			public void onResponse(Call<BasicResponse> call, retrofit2.Response<BasicResponse> response) {
				if (response.body() != null)
					mListener.onUserUnFollowed(response.body().message);
			}

			@Override
			public void onFailure(Call<BasicResponse> call, Throwable t) {

			}
		});
	}

	@Override
	public void updatePhoneUser(final UserInformations userInformations, final ShowProfileListener listener) {

		Gson gson = new Gson();
		String params = gson.toJson(userInformations);

		userService.updateData(params).enqueue(new retrofit2.Callback<BasicResponse>() {
			@Override
			public void onResponse(Call<BasicResponse> call, retrofit2.Response<BasicResponse> response) {
				if (response.body() != null) {

					boolean isBanned = false;
					boolean isPhoneAlreadyUsed = false;
					String message = response.body().message;

					if (response.body().stateBann != null && response.body().stateBann.equals("1"))
						isBanned = true;

					if (response.body().error != null && response.body().error.equals("1")) {
						isPhoneAlreadyUsed = true;
					}

					listener.onUpdatePhoneUser(mainPreferences.getIdUser(), message, isBanned, isPhoneAlreadyUsed);
				}

			}

			@Override
			public void onFailure(Call<BasicResponse> call, Throwable t) {

			}
		});
	}

	@DebugLog
	@Override
	public void updateDataUser(final UserInformations userInformations, 
			final ShowProfileListener listener) {
		
		boolean error = false;
		
		if (TextUtils.isEmpty(userInformations.getNom())) {
			error = true;
			listener.onNameUserError();
		}
		
		if (!error) {
			
			Gson gson = new Gson();
			String params = gson.toJson(userInformations);

			userService.updateData(params).enqueue(new retrofit2.Callback<BasicResponse>() {
				@Override
				public void onResponse(Call<BasicResponse> call, retrofit2.Response<BasicResponse> response) {
					if (response.body() != null)
						listener.onUpdateDataUser(mainPreferences.getIdUser(),
								response.body().message);
				}

				@Override
				public void onFailure(Call<BasicResponse> call, Throwable t) {

				}
			});
		}
		
	}

	@Override
	public void updateAvatarUser(final RequestBody typedFile,
			final ShowProfileListener listener) {
		
		uploadService.uploadAvatar(typedFile).enqueue(new retrofit2.Callback<FilesResponse>() {
			@Override
			public void onResponse(Call<FilesResponse> call, retrofit2.Response<FilesResponse> response) {
				if (response.body() != null)
					listener.onAvatarUploaded(response.body().files.get(0).thumbnailUrl);
			}

			@Override
			public void onFailure(Call<FilesResponse> call, Throwable t) {

			}
		});
	}

	@Override
	public void updateBackgroundUser(final RequestBody typedFile,
			final ShowProfileListener listener) {
		
		uploadService.uploadBackground(typedFile).enqueue(new retrofit2.Callback<FilesResponse>() {
			@Override
			public void onResponse(Call<FilesResponse> call, retrofit2.Response<FilesResponse> response) {
				if (response.body() != null)
					listener.onBackgroundUploaded(response.body().files.get(0).url);
			}

			@Override
			public void onFailure(Call<FilesResponse> call, Throwable t) {

			}
		});
	}


	@Override
	public void getStateUser(final MainListener listener) {

		int versionApp = 0;

		try {
			versionApp = contextApp.getPackageManager().getPackageInfo( (contextApp.getPackageName()), 0).versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		userService.getState(Settings.Secure.getString(contextApp.getContentResolver(),
				Settings.Secure.ANDROID_ID),
				versionApp).enqueue(new retrofit2.Callback<ApiResponse.StateUser>() {
			@Override
			public void onResponse(Call<ApiResponse.StateUser> call, retrofit2.Response<ApiResponse.StateUser> response) {

				if (response.body() != null)
				{
					ApiResponse.StateUser stateUser = response.body();

					listener.onStateUser(Integer.valueOf(stateUser.nbreMessageNonLu), Integer.valueOf(stateUser.nbreNotification),
							Integer.valueOf(stateUser.stateBann), stateUser.minVersionApp, stateUser.codePrenium, Integer.valueOf(stateUser.levelContribution));
				}
			}

			@Override
			public void onFailure(Call<ApiResponse.StateUser> call, Throwable t) {

			}
		});

	}

	@DebugLog
	@Override
	public void getRankingUser(final RankingListener listener) {

		userService.getRanking().enqueue(new retrofit2.Callback<ApiResponse.Rankings>() {
			@Override
			public void onResponse(Call<ApiResponse.Rankings> call, retrofit2.Response<ApiResponse.Rankings> response) {
				if (response.body() != null)
					listener.onLoadListUsers(response.body());
			}

			@Override
			public void onFailure(Call<ApiResponse.Rankings> call, Throwable t) {

			}
		});
	}

	@Override
	public void getFriends(ArrayList<Long> listUsers, final FriendsListener listener) {

		String jsonDigistIds = new Gson().toJson(listUsers);

		userService.getFriends(jsonDigistIds).enqueue(new retrofit2.Callback<ApiResponse.Rankings>() {
			@Override
			public void onResponse(Call<ApiResponse.Rankings> call, retrofit2.Response<ApiResponse.Rankings> response) {
				//LogUtils.log(new String(((TypedByteArray) response.getBody()).getBytes()));
				if (response.body() != null)
					listener.onLoadFriendList(response.body());
			}

			@Override
			public void onFailure(Call<ApiResponse.Rankings> call, Throwable t) {

			}
		});

	}
}
