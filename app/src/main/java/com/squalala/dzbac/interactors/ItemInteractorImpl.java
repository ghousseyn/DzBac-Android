package com.squalala.dzbac.interactors;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.squalala.dzbac.R;
import com.squalala.dzbac.common.listener.OnDeleteStateListener;
import com.squalala.dzbac.common.listener.OnFinishedAddItemListener;
import com.squalala.dzbac.common.listener.OnUploadFileListener;
import com.squalala.dzbac.data.PostAdd;
import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.data.api.ApiResponse.BasicResponse;
import com.squalala.dzbac.data.api.ApiResponse.ContentItem;
import com.squalala.dzbac.data.api.ApiResponse.FilesResponse;
import com.squalala.dzbac.data.api.ApiResponse.Items;
import com.squalala.dzbac.data.api.ItemService;
import com.squalala.dzbac.data.api.UploadService;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.ui.show_item.ShowItemListener;
import com.squalala.dzbac.utils.StringUtils;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ShowItemInteractorImpl.java
 * Date : 24 juil. 2014
 * 
 */
public class ItemInteractorImpl implements ItemInteractor {
	
	private ItemService itemService;
	private UploadService uploadService;
	private MainPreferences mainPreferences;
    private Context context;
    private int MAX_ATTEMPS = 2;
    private int countUploadsTry = 1;


	private Call<ContentItem> getDataCall;
	private Call<BasicResponse> signalePostCall, deleteAudioCall, deleteFileCall;
	private Call<ApiResponse.FileUploadResponse> uploadFileCall, uploadAudioCall, uploadVideoCall;
	private Call<FilesResponse> uploadPhotoItemCall;

    private static final String TAG = ItemInteractorImpl.class.getSimpleName();
	
	public ItemInteractorImpl(ItemService itemService, UploadService uploadService,
			MainPreferences mainPreferences, Context context) {
		this.itemService = itemService;
		this.uploadService = uploadService;
		this.mainPreferences = mainPreferences;
        this.context = context;
	}

	@Override
	public void getData(final String idItem ,final ShowItemListener listener) {
		
		Log.e("id_item", ""+idItem);

		getDataCall = itemService.getItem(idItem);

		getDataCall.enqueue(new retrofit2.Callback<ContentItem>() {
			@Override
			public void onResponse(Call<ContentItem> call, retrofit2.Response<ContentItem> response) {

				if (!call.isCanceled() && response.body() != null)
				{
					if (response.body().posts != null && response.body().posts.size() > 0)
						listener.onLoadData(response.body().posts.get(0));
					else
						listener.onNoDataLoaded();
				}

			}

			@Override
			public void onFailure(Call<ContentItem> call, Throwable t) {
				if (!call.isCanceled())
					listener.onErrorLoading();
			}
		});

	}

	@Override
	public void signalePost(String idContent, final ShowItemListener mListener) {

		signalePostCall = itemService.signalePost(idContent);

		signalePostCall.enqueue(new retrofit2.Callback<BasicResponse>() {
			@Override
			public void onResponse(Call<BasicResponse> call, retrofit2.Response<BasicResponse> response) {
				if (response.body() != null)
					mListener.onPostSignaled(response.body().message);
			}

			@Override
			public void onFailure(Call<BasicResponse> call, Throwable t) {

			}
		});

	}

	@Override
    public void uploadFile(RequestBody typedFile, final OnUploadFileListener listener) {

		uploadFileCall = uploadService.uploadFile(typedFile);

		uploadFileCall.enqueue(new retrofit2.Callback<ApiResponse.FileUploadResponse>() {
			@Override
			public void onResponse(Call<ApiResponse.FileUploadResponse> call, retrofit2.Response<ApiResponse.FileUploadResponse> response) {
				if (response.body() != null)
				{
					if (response.body().filename != null)
						listener.onUploadFileFinished(response.body().filename);
					else
						listener.onUploadFileError(response.body().message);
				}

			}

			@Override
			public void onFailure(Call<ApiResponse.FileUploadResponse> call, Throwable t) {

			}
		});

    }


	@Override
	public void deleteAudio(String contentId, String filename, final OnDeleteStateListener listener) {

		deleteAudioCall = uploadService.deleteAudio(contentId, filename);

		deleteAudioCall.enqueue(new retrofit2.Callback<BasicResponse>() {
			@Override
			public void onResponse(Call<BasicResponse> call, retrofit2.Response<BasicResponse> response) {
				if (response.body() != null && response.body().message != null)
					listener.onDeleteAudio(response.body().message);
				else
					listener.onDeleteAudioError(response.body().error);
			}

			@Override
			public void onFailure(Call<BasicResponse> call, Throwable t) {

			}
		});

	}

	@Override
	public void uploadAudio(RequestBody typedFile, final OnUploadFileListener listener) {

		uploadAudioCall = uploadService.uploadAudio(typedFile);

		uploadAudioCall.enqueue(new retrofit2.Callback<ApiResponse.FileUploadResponse>() {
			@Override
			public void onResponse(Call<ApiResponse.FileUploadResponse> call, retrofit2.Response<ApiResponse.FileUploadResponse> response) {
				if (response.body() != null && response.body().filename != null)
					listener.onUploadAudioFinished(response.body().filename);
				else
					listener.onUploadAudioFailed(response.body().message);
			}

			@Override
			public void onFailure(Call<ApiResponse.FileUploadResponse> call, Throwable t) {

			}
		});

	}

	@Override
    public void deleteFile(String contentId, String filename, final OnDeleteStateListener listener) {

		deleteFileCall = uploadService.deleteFile(contentId, filename);

		deleteFileCall.enqueue(new retrofit2.Callback<BasicResponse>() {
			@Override
			public void onResponse(Call<BasicResponse> call, retrofit2.Response<BasicResponse> response) {
				if (response.body() != null && response.body().message != null)
					listener.onDeleteFile(response.body().message);
				else
					listener.onDeleteFileError(response.body().error);
			}

			@Override
			public void onFailure(Call<BasicResponse> call, Throwable t) {

			}
		});
    }

	@Override
	public void uploadVideo(RequestBody file, final OnUploadFileListener listener) {

		uploadVideoCall = uploadService.uploadVideo(file);

		uploadVideoCall.enqueue(new retrofit2.Callback<ApiResponse.FileUploadResponse>() {
			@Override
			public void onResponse(Call<ApiResponse.FileUploadResponse> call, retrofit2.Response<ApiResponse.FileUploadResponse> response) {
				if (response.body() != null)
					listener.onUploadVideoFinished(response.body().filename, context.getString(R.string.video_uploaded));
			}

			@Override
			public void onFailure(Call<ApiResponse.FileUploadResponse> call, Throwable t) {
				listener.onUploadVideoFailed(context.getString(R.string.error_video_uploaded));
			}
		});

	}

	@Override
	public void uploadPhotoItem(final String idItem,final String typeItem,
			final RequestBody typedFile,final int position,final ShowItemListener listener) {
		
        final long startTime = System.currentTimeMillis();

        String fileType = "image/jpeg";

		uploadPhotoItemCall = uploadService.uploadPhotoItem(typedFile) ;//, typedFile);

		uploadPhotoItemCall.enqueue(new retrofit2.Callback<FilesResponse>() {
			@Override
			public void onResponse(Call<FilesResponse> call, retrofit2.Response<FilesResponse> response) {
				Log.e("ItemIpml ", response.body().files.get(0).name);

				long endTime = System.currentTimeMillis();
				System.out.println("That took " + (endTime - startTime) + " milliseconds");
				countUploadsTry = 1;

				if (response.body() != null)
					listener.onPhotoUploaded(position, response.body().files.get(0).name);
			}

			@Override
			public void onFailure(Call<FilesResponse> call, Throwable t) {
				listener.onPhotoUploadFailed(position, t.getMessage());
			}
		});

	}

	@Override
	public void deletePhotoItem(String filename, String idItem,
			String typeItem,final ShowItemListener listener) {

		uploadService.deletePhotoItem(filename, idItem).enqueue(new retrofit2.Callback<BasicResponse>() {
			@Override
			public void onResponse(Call<BasicResponse> call, retrofit2.Response<BasicResponse> response) {
				if (response.body() != null)
					listener.onPhotoDeleted(response.body().message);
			}

			@Override
			public void onFailure(Call<BasicResponse> call, Throwable t) {

			}
		});
	}


    @Override
	public void addPost(final String idItem, final PostAdd itemAdd, final boolean isModification,
			final OnFinishedAddItemListener listener) {
		
		boolean error = false;
		
		if (TextUtils.isEmpty(itemAdd.getTitle())) {
			listener.onTitleError(context.getString(R.string.error_add_item_titre_vide));
			error = true;
		}

        if (itemAdd.getTitle().trim().length() < 8) {
            listener.onTitleError(context.getString(R.string.error_title_lenght));
            error = true;
        }

		if (TextUtils.isEmpty(itemAdd.getDescription())) {
			listener.onDescriptionError(context.getString(R.string.error_add_item_description_vide));
			error = true;
		}

        if (itemAdd.getDescription().trim().length() < 8) {
            listener.onDescriptionError(context.getString(R.string.error_description_lenght));
            error = true;
        }
		
		if (!error) {
			
            Log.e("IntergatocItem", " BEFORE RESULT --> " + itemAdd.getTitle());


            //    Log.e("IntergatocItem", " BEFORE RESULT # --> " + Charset.forName("utf8mb4").encode(itemAdd.getTitle()));
            itemAdd.setDescription(StringUtils.escapeString(itemAdd.getDescription()));
            itemAdd.setTitle(StringUtils.escapeString(itemAdd.getTitle()));

            Log.e("IntergatocItem", "RESULT --> " + itemAdd.getTitle());

			Gson gson = new Gson();
			String jsonData = gson.toJson(itemAdd);

			itemService.deposeAnnonce(idItem,
					jsonData,
					isModification ? 1 : 0).enqueue(new retrofit2.Callback<BasicResponse>() {
				@Override
				public void onResponse(Call<BasicResponse> call, retrofit2.Response<BasicResponse> response) {
					if (response.body() != null)
						listener.onItemAdded(response.body().message);
				}

				@Override
				public void onFailure(Call<BasicResponse> call, Throwable t) {
					listener.onItemAddError(t.getMessage());
				}
			});

		}
		
		
		
	}



	@Override
	public void updateDataPhoto(final String idItem,final String typeItem, 
			final String urlImagePresentation, final ShowItemListener mListener) {


		Log.e("Itemimp", urlImagePresentation + "");

		uploadService.updateDataImages(
				idItem,
				urlImagePresentation).enqueue(new retrofit2.Callback<BasicResponse>() {
			@Override
			public void onResponse(Call<BasicResponse> call, retrofit2.Response<BasicResponse> response) {
				if (response.body() != null)
					mListener.onUpdateDataImages();
			}

			@Override
			public void onFailure(Call<BasicResponse> call, Throwable t) {

			}
		});
	}

	@Override
	public void initItem(final ShowItemListener listener) {


		itemService.initItem(null).enqueue(new retrofit2.Callback<Items>() {
			@Override
			public void onResponse(Call<Items> call, retrofit2.Response<Items> response) {
				if (response.body() != null)
					listener.onInitItem(response.body().id);
			}

			@Override
			public void onFailure(Call<Items> call, Throwable t) {

			}
		});
	}


	@Override
	public void destroyCaller() {
		if (getDataCall != null && getDataCall.isExecuted())
			getDataCall.cancel();
	}
}
