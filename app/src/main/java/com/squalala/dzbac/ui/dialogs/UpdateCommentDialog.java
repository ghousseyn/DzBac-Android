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
import com.squalala.dzbac.ui.cards.CommentShowCard;
import com.squalala.dzbac.ui.comment.CommentaryListener;
import com.squalala.dzbac.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : updateCommentDialog.java
 * Date : 7 sept. 2014
 * 
 */
public class UpdateCommentDialog extends Dialog 
	implements OnClickListener {
	

	@InjectView(R.id.edit_comment_dialog)
    EmojiconEditText edit_comment;
	@InjectView(R.id.btn_send_comment_dialog) BootstrapButton btn_send_comment;
	@InjectView(R.id.progressBar_send_comment_dialog) ProgressBar progressBar;
	
	private CommentaryListener listener;
	private String idComment, comment;
    private CommentShowCard card;

	public UpdateCommentDialog(Context context, String comment,
			String idComment,
            CommentShowCard card,
			CommentaryListener listener) {
		super(context);
		this.listener = listener;
		this.idComment = idComment;
		this.comment = comment;
        this.card = card;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.dialog_comment);
		setTitle(getContext().getString(R.string.dialog_commenter));
		
		ButterKnife.inject(this);

        String commentStr;

		commentStr = StringUtils.toEmoji(comment);

				// Correction pour éviter le crash à cause des emoji
		/*if (comment.contains("\\\\u")) {
			try{
				commentStr = StringEscapeUtils.unescapeJava(StringEscapeUtils.unescapeJava(comment));
			} catch (NestableRuntimeException e) {
				e.printStackTrace();
				commentStr = comment;
			}
		}
		else
			commentStr = comment;*/

				edit_comment.setText(commentStr);
		
		btn_send_comment.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		updateComment();
	}
	
	private void updateComment() {
		
		boolean error = false;
		
		if (getComment().equals("")) {
			Toast.makeText(getContext(), getContext().getString(R.string.error_message_vide),
					Toast.LENGTH_LONG).show();
			error = true;
		}
		
		if (!error) {
			listener.onSelectUpdateComment(idComment, getComment(), card);
			dismiss();
		}
	}
	
	private String getComment() {
		return edit_comment.getText().toString().trim();
	}
	
	
	
	

}
