package com.squalala.dzbac.interactors;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.squalala.dzbac.R;
import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.data.api.ApiResponse.PostsItems;
import com.squalala.dzbac.data.api.PostsService;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.ui.list_items.ListItemListener;
import com.squalala.dzbac.utils.Translater;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ListItemInteractorImpl.java
 * Date : 8 juil. 2014
 *
 */
public final class ListItemInteractorImpl implements ListItemInteractor {

	private final PostsService itemsService;
	private Context context;
	private MainPreferences mainPreferences;
    private Translater translater;

	private static final String TAG = "ListItemInteractorImpl";

	private Call<PostsItems> loadPostsSignaledCall, loadItemsCall;
	
	@Inject
	public ListItemInteractorImpl(PostsService itemsService,
			MainPreferences mainPreferences, Translater translater, Context context) {
		this.itemsService = itemsService;
		this.mainPreferences = mainPreferences;
        this.translater = translater;
		this.context = context;
	}


	@Override
	public void loadPostsSignaled(final int page, final ListItemListener listener) {


		loadPostsSignaledCall = itemsService.getPostsSignaled(page);

		loadPostsSignaledCall.enqueue(new retrofit2.Callback<PostsItems>() {
			@Override
			public void onResponse(Call<PostsItems> call, retrofit2.Response<PostsItems> response) {
				if (response.body() != null)
					listener.onLoadDataItems(response.body(), page, itemsService);
			}

			@Override
			public void onFailure(Call<PostsItems> call, Throwable t) {

			}
		});


	}

	@Override
	public void loadItems(final boolean isMyPosts, final boolean isLikes, final int page,
                          String membreId, final ListItemListener listener) {
		
		Log.e(TAG, "");

			String typeArray [] = context.getResources().getStringArray(R.array.filtre_type);
			String type = null;
			int positionType = mainPreferences.getType();
			
			String subjectArray [] = context.getResources().getStringArray(R.array.filtre_subject);
			String subject = null;
			int positionSubject = mainPreferences.getSubject();
			
			if (positionType != 0) {
				type = typeArray[positionType];
			}
			
			if (positionSubject != 0) {
				subject = subjectArray[positionSubject];
			}
			
			String motCle = mainPreferences.getMotCle();
			int formatPosition = mainPreferences.getFormat();
			String formatStr = null;

			switch (formatPosition) {

				case 1:

					formatStr = "i";

					break;

				case 2:

					formatStr = "a";

					break;

				case 3:

					formatStr = "v";

					break;

			}



			String jsonTagsId = new Gson().toJson(mainPreferences.getTagsId());

			System.out.println("tags  : " + jsonTagsId);

		
			boolean isOrderByViews = false;
			boolean isOrderByLikes = false;


			switch (mainPreferences.getTypeOrder()) {

				case 0:

					isOrderByLikes = false;
					isOrderByViews = false;

					break;

				case 1:

					isOrderByLikes = true;
					isOrderByViews = false;

					break;


				case 2:

					isOrderByLikes = false;
					isOrderByViews = true;

					break;


				case 3:

					isOrderByLikes = true;
					isOrderByViews = true;

					break;

			}

			loadItemsCall = itemsService.getPosts(
					membreId,
					subject,
					type,
					motCle,
					formatStr,
					jsonTagsId,
					isOrderByViews ? 1 : 0,
					isOrderByLikes ? 1 : 0,
					isMyPosts ? 1 : 0,
					isLikes ? 1 : 0,
					page);

			loadItemsCall.enqueue(new retrofit2.Callback<PostsItems>() {
				@Override
				public void onResponse(Call<PostsItems> call, retrofit2.Response<PostsItems> response) {
					if (response.body() != null)
						listener.onLoadDataItems(response.body(), page, itemsService);
				}

				@Override
				public void onFailure(Call<PostsItems> call, Throwable t) {

				}
			});

	}


}
