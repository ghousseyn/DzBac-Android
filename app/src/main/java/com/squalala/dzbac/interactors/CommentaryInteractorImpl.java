package com.squalala.dzbac.interactors;

import android.text.TextUtils;
import android.util.Log;

import com.squalala.dzbac.common.listener.GroupListener;
import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.data.api.ApiResponse.BasicResponse;
import com.squalala.dzbac.data.api.ApiResponse.Commentaire;
import com.squalala.dzbac.data.api.ApiResponse.Commentaires;
import com.squalala.dzbac.data.api.CommentaryService;
import com.squalala.dzbac.data.api.UploadService;
import com.squalala.dzbac.ui.cards.CommentShowCard;
import com.squalala.dzbac.ui.comment.CommentaryListener;
import com.squalala.dzbac.ui.show_item.ShowItemListener;
import com.squalala.dzbac.utils.StringUtils;

import hugo.weaving.DebugLog;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : CommentaryInteractorImpl.java
 * Date : 30 juil. 2014
 * 
 */
public class CommentaryInteractorImpl implements CommentaryInteractor {
	
	private CommentaryService commentaryService;
    private UploadService uploadService;

	private Call<Commentaires> getCommentairesCall;
	private Call<BasicResponse> deleteCommentCall,
			updateCommentCall, deletePhotoItemCall, sendMessageCall;
	private Call<Commentaire> initItemCall;
	private Call<ApiResponse.FilesResponse> uploadPhotoItemCall;



	public CommentaryInteractorImpl(CommentaryService commentaryService,
                                    UploadService uploadService) {
		this.commentaryService = commentaryService;
        this.uploadService = uploadService;
	}

	@Override
	public void getCommentaires(final int page, final String idItem,
			final CommentaryListener listener, final GroupListener listenerGroup) {

		getCommentairesCall = commentaryService.getCommentaires(page, idItem);

		getCommentairesCall.enqueue(new retrofit2.Callback<Commentaires>() {
			@Override
			public void onResponse(Call<Commentaires> call, retrofit2.Response<Commentaires> response) {

                if (response.body() != null)
                {
                    listenerGroup.onLoadStateInGroup(response.body().state_group);
                    listener.onLoadCommentaires(response.body(), page);
                }
			}

			@Override
			public void onFailure(Call<Commentaires> call, Throwable t) {

			}
		});
	}

	@Override
	public void sendAudioFile(RequestBody file, final CommentaryListener mListener) {


		uploadService.uploadAudio(file).enqueue(new Callback<ApiResponse.FileUploadResponse>() {
			@Override
			public void onResponse(Call<ApiResponse.FileUploadResponse> call, Response<ApiResponse.FileUploadResponse> response) {
				if (response.body() != null) {
					mListener.onUploadAudioFile(response.body().filename);
				}
			}

			@Override
			public void onFailure(Call<ApiResponse.FileUploadResponse> call, Throwable t) {
				mListener.onUploadAudioFileError(t.getMessage());
			}
		});
	}

	@Override
	public void sendMessage(final String comment, final String idItem,
			final CommentaryListener listener) {


		sendMessageCall = commentaryService.comment(idItem, StringUtils.escapeString(comment));

		sendMessageCall.enqueue(new retrofit2.Callback<BasicResponse>() {
			@Override
			public void onResponse(Call<BasicResponse> call, retrofit2.Response<BasicResponse> response) {
				if (response.body() != null)
					listener.onCommentSended(idItem, response.body().message);
			}

			@Override
			public void onFailure(Call<BasicResponse> call, Throwable t) {

			}
		});
	}

	@DebugLog
	@Override
	public void sendAudioComment(String idContent, String filename, final CommentaryListener mListener) {

		commentaryService.insertCommentWithAudio(idContent, filename).enqueue(new Callback<BasicResponse>() {
			@Override
			public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
				if (response.body() != null)
					mListener.onAudioCommentSended(response.body().message);
			}

			@Override
			public void onFailure(Call<BasicResponse> call, Throwable t) {

			}
		});
	}

	@Override
	public void deleteComment(final String idComment,final String idContent, final CommentShowCard card,
			final CommentaryListener listener) {

		deleteCommentCall = commentaryService.deleteComment(idComment, idContent);

		deleteCommentCall.enqueue(new retrofit2.Callback<BasicResponse>() {
			@Override
			public void onResponse(Call<BasicResponse> call, retrofit2.Response<BasicResponse> response) {
				if (response.body() != null)
					listener.onDeleteComment(response.body().message, card);
			}

			@Override
			public void onFailure(Call<BasicResponse> call, Throwable t) {

			}
		});
	}

	@Override
	public void updateComment(final String idComment, final String comment, final CommentShowCard card,
			final CommentaryListener listener) {

		updateCommentCall = commentaryService.updateComment(idComment, StringUtils.escapeString(comment));

		updateCommentCall.enqueue(new retrofit2.Callback<BasicResponse>() {
			@Override
			public void onResponse(Call<BasicResponse> call, retrofit2.Response<BasicResponse> response) {
				if (response.body() != null){
					card.setComment(comment);
					listener.onUpdateComment(response.body().message, card);
				}

			}

			@Override
			public void onFailure(Call<BasicResponse> call, Throwable t) {

			}
		});
	}

    @Override
    public void initItem(final String idContent, final ShowItemListener listener) {

		initItemCall = commentaryService.initItem(idContent);

		initItemCall.enqueue(new retrofit2.Callback<Commentaire>() {
			@Override
			public void onResponse(Call<Commentaire> call, retrofit2.Response<Commentaire> response) {
				if (response.body() != null)
					listener.onInitItem(response.body().id);
			}

			@Override
			public void onFailure(Call<Commentaire> call, Throwable t) {

			}
		});
    }

    @Override
    public void uploadPhotoItem(final String idItem, final RequestBody typedFile, final int position, final ShowItemListener listener) {

		uploadPhotoItemCall = uploadService.uploadPhotoComment(typedFile);

		uploadPhotoItemCall.enqueue(new Callback<ApiResponse.FilesResponse>() {
			@Override
			public void onResponse(Call<ApiResponse.FilesResponse> call, Response<ApiResponse.FilesResponse> response) {
				if (response.body() != null)
					listener.onPhotoUploaded(position, response.body().files.get(0).name);
			}

			@Override
			public void onFailure(Call<ApiResponse.FilesResponse> call, Throwable t) {
				//TODO à régler
				listener.onPhotoUploadFailed(position, t.getMessage());
			}
		});
    }

    @Override
    public void deletePhotoItem(String filename, String idItem, final ShowItemListener listener) {

		deletePhotoItemCall = uploadService.deletePhotoComment(filename, idItem);

		deletePhotoItemCall.enqueue(new Callback<BasicResponse>() {
			@Override
			public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
				if (response.body() != null)
					listener.onPhotoDeleted(response.body().message);
			}

			@Override
			public void onFailure(Call<BasicResponse> call, Throwable t) {

			}
		});
    }


    @Override
    public void sendMessage(String comment, final String idItem, String idComment, final CommentaryListener listener) {

        boolean error = false;


        if (TextUtils.isEmpty(comment)) {
            error = true;
            listener.onCommentEmpty();
        }


        if (!error) {

			sendMessageCall = commentaryService.insertCommentWithImages(idComment, idItem, StringUtils.escapeString(comment));

			sendMessageCall.enqueue(new Callback<BasicResponse>() {
				@Override
				public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
					if (response.body() != null)
						listener.onCommentSendedWithPhoto(response.body().message);
				}

				@Override
				public void onFailure(Call<BasicResponse> call, Throwable t) {

				}
			});

        }
    }


	@Override
	public void destroyCaller() {
		if (getCommentairesCall != null && getCommentairesCall.isExecuted())
			getCommentairesCall.cancel();

		if (uploadPhotoItemCall != null && uploadPhotoItemCall.isExecuted())
			uploadPhotoItemCall.cancel();

		if (sendMessageCall != null && sendMessageCall.isExecuted())
			sendMessageCall.cancel();
	}
}
