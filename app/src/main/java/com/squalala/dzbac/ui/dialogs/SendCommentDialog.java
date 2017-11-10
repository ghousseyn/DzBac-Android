package com.squalala.dzbac.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.squalala.dzbac.R;
import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.data.api.ApiResponse.BasicResponse;
import com.squalala.dzbac.data.api.CommentaryService;
import com.squalala.dzbac.data.prefs.MainPreferences;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : SendMessageDialog.java
 * Date : 27 juil. 2014
 * 
 */
public class SendCommentDialog extends Dialog implements OnClickListener {
	
	@InjectView(R.id.edit_comment_dialog)
	EmojiconEditText edit_comment;
	@InjectView(R.id.btn_send_comment_dialog) BootstrapButton btn_send_comment;
	@InjectView(R.id.progressBar_send_comment_dialog) ProgressBar progressBar;
	
	private CommentaryService commentaryService;
	private String idItem;
	private MainPreferences mainPreferences;

	public SendCommentDialog(Context context,
			CommentaryService commentaryService, MainPreferences mainPreferences) {
		super(context);
		this.commentaryService = commentaryService;
		this.mainPreferences = mainPreferences;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.dialog_comment);
		setTitle(getContext().getString(R.string.dialog_commenter));
		
		ButterKnife.inject(this);
		
		btn_send_comment.setOnClickListener(this);
	}
	
	
	public void setIdItem(String idItem) {
		this.idItem = idItem;
	}
	
	private void hideElement() {
		btn_send_comment.setVisibility(View.GONE);
		edit_comment.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		setCancelable(false);
	}
	
	private void showElement() {
		btn_send_comment.setVisibility(View.VISIBLE);
		edit_comment.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);
		setCancelable(true);
	}

	@Override
	public void onClick(View v) {
		sendComment();
	}
	
	private void sendComment() {
		
		boolean error = false;
		
		if (getComment().equals("")) {
			Toast.makeText(getContext(), getContext().getString(R.string.error_message_vide),
					Toast.LENGTH_LONG).show();
			error = true;
		}
		
		if (!error) {
			
			hideElement();
			
			/*commentaryService.comment(
									idItem,
									getComment(),
									new Callback<ApiResponse.BasicResponse>() {

										@Override
										public void failure(RetrofitError arg0) {
											if(arg0.toString().contains("java.io.EOFException")){
												sendComment();
										    }
										//	showElement();
										}

										@Override
										public void success(BasicResponse arg0,
												Response arg1) {
											Toast.makeText(getContext(), arg0.message,
													Toast.LENGTH_LONG).show();
											showElement();
											edit_comment.setText("");
											dismiss();
										}
									});
		*/
		}
	}
	
	private String getComment() {
		return edit_comment.getText().toString().trim();
	}
	

}
