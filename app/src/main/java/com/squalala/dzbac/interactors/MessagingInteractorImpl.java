package com.squalala.dzbac.interactors;

import com.squalala.dzbac.common.listener.MessagingListener;
import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.data.api.ApiResponse.BasicResponse;
import com.squalala.dzbac.data.api.ApiResponse.ListConversationPosts;
import com.squalala.dzbac.data.api.ApiResponse.Messages;
import com.squalala.dzbac.data.api.MessagingService;
import com.squalala.dzbac.data.api.UploadService;
import com.squalala.dzbac.utils.StringUtils;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : MessagingInteractorImpl.java
 * Date : 1 août 2014
 * 
 */
public class MessagingInteractorImpl implements MessagingInteractor {
	
	private MessagingService messagingService;
	private UploadService uploadService;

	private Call<ListConversationPosts> getListConversationCall;
	private Call<Messages> getConversationCall;

	public MessagingInteractorImpl(MessagingService messagingService, UploadService uploadService) {
		this.messagingService = messagingService;
		this.uploadService = uploadService;
	}

	@Override
	public void blockUser(String userId, final MessagingListener listener) {

		messagingService.blockUser(userId).enqueue(new retrofit2.Callback<BasicResponse>() {
			@Override
			public void onResponse(Call<BasicResponse> call, retrofit2.Response<BasicResponse> response) {
				if (response.body() != null)
					listener.OnUserBlocked(response.body().message);
			}

			@Override
			public void onFailure(Call<BasicResponse> call, Throwable t) {

			}
		});

	}

	@Override
	public void deleteConversation(String headerId, final MessagingListener listener) {

		messagingService.deleteConversation(headerId).enqueue(new retrofit2.Callback<BasicResponse>() {
			@Override
			public void onResponse(Call<BasicResponse> call, retrofit2.Response<BasicResponse> response) {

				if (response.body() != null)
				{
					if (response.code() == 200)
						listener.onDeleteConversation(response.body().message);
					else if (response.code() == 400)
						listener.onDeleteConversationError(response.body().message);
				}

			}

			@Override
			public void onFailure(Call<BasicResponse> call, Throwable t) {
			}
		});

	}

	@Override
	public void getListConversation(final int page ,final MessagingListener listener) {

		getListConversationCall = messagingService.getListConversations(page);

		getListConversationCall.enqueue(new retrofit2.Callback<ListConversationPosts>() {
			@Override
			public void onResponse(Call<ListConversationPosts> call, retrofit2.Response<ListConversationPosts> response) {
				if (!call.isCanceled() && response.body() != null)
					listener.onLoadListConversation(response.body(), page);
			}

			@Override
			public void onFailure(Call<ListConversationPosts> call, Throwable t) {
                System.out.println(t.getMessage());
			}
		});

	}

	@Override
	public void getConversation(final String id_header, final int page ,final MessagingListener listener) {

		getConversationCall = messagingService.getconversation(id_header, page);

		getConversationCall.enqueue(new retrofit2.Callback<Messages>() {
			@Override
			public void onResponse(Call<Messages> call, retrofit2.Response<Messages> response) {
				if (!call.isCanceled() && response.body() != null)
				{
					if (response.body().conversation.size() > 0)
						listener.onLoadConversation(response.body());
					else
						listener.onConversationNotFound();
				}

			}

			@Override
			public void onFailure(Call<Messages> call, Throwable t) {
                t.printStackTrace();
			}
		});

	}

	@Override
	public void sendMessage(final String idHeader, final String idReceveur, final String message, 
			final MessagingListener listener) {

		messagingService.sendMessage(
				idHeader,
				idReceveur,
				StringUtils.escapeString(message))
				.enqueue(new retrofit2.Callback<BasicResponse>() {
					@Override
					public void onResponse(Call<BasicResponse> call, retrofit2.Response<BasicResponse> response) {
						if (response.body() != null)
							listener.onMessageSended(idHeader);
					}

					@Override
					public void onFailure(Call<BasicResponse> call, Throwable t) {
                        System.out.println(t.getMessage());
					}
				});
		
	}

	@Override
	public void sendAudioFile(RequestBody file, final MessagingListener mListener) {

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
	public void sendAudioMessage(String idHeader, String idReceveur, String filename, final MessagingListener mListener) {

		messagingService.insertMessageWithAudio(idHeader, idReceveur, filename).enqueue(new Callback<BasicResponse>() {
			@Override
			public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
				if (response.body() != null)
					mListener.onAudioMessageSended(response.body().message);
			}

			@Override
			public void onFailure(Call<BasicResponse> call, Throwable t) {

			}
		});
	}

	@Override
	public void destroyCaller() {
		if (getListConversationCall != null && getListConversationCall.isExecuted())
			getListConversationCall.cancel();
		if (getConversationCall != null && getConversationCall.isExecuted())
			getConversationCall.cancel();
	}
}
