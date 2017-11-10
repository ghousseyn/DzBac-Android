package com.squalala.dzbac.ui.comment;

import android.content.Context;

import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.interactors.CommentaryInteractor;
import com.squalala.dzbac.interactors.NotificationInteractor;
import com.squalala.dzbac.ui.cards.CommentShowCard;
import com.squalala.dzbac.ui.show_item.ShowItemListener;

import java.util.ArrayList;

import hugo.weaving.DebugLog;
import it.gmariotti.cardslib.library.internal.Card;
import okhttp3.RequestBody;
import retrofit.mime.TypedFile;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : CommentaryPresenterImpl.java
 * Date : 30 juil. 2014
 * 
 */
public class CommentaryPresenterImpl 
	implements CommentaryPresenter, CommentaryListener, ShowItemListener {
	
	private CommentaryView view;
	private CommentaryInteractor commentaryInteractor;
	private NotificationInteractor notificationInteractor;
	private Context context;
    private MainPreferences preferences;
    private boolean backpressed, destroyed;

    private String idItem;

    private static final String TAG = CommentaryPresenterImpl.class.getSimpleName();
	
	public CommentaryPresenterImpl(CommentaryView view,
			CommentaryInteractor commentaryInteractor,
			NotificationInteractor notificationInteractor,
			Context context,
            MainPreferences preferences) {
		this.commentaryInteractor = commentaryInteractor;
		this.view = view;
		this.notificationInteractor = notificationInteractor;
		this.context = context;
        this.preferences = preferences;
	}


    @Override
    public void onPostSignaled(String message) {

    }

    @Override
    public void sendComment(String comment, String idItem, String idComment) {
        view.showProgressBar();
        view.showProgressDialog();
        commentaryInteractor.sendMessage(comment, idItem, idComment, this);
    }

    @Override
    public void onNoDataLoaded() {

    }

    @Override
    public void onErrorLoading() {

    }

    @Override
    public void uploadPhotoItem(String idItem, int position, RequestBody typedFile) {
        commentaryInteractor.uploadPhotoItem(idItem, typedFile, position, this);
    }

    @Override
    public void onUpdateDataImages() {

    }

    @Override
    public void deletePhoto(String filename, String idItem) {
        commentaryInteractor.deletePhotoItem(filename, idItem, this);
    }

    @Override
    public void initItem(String idContent) {
        commentaryInteractor.initItem(idContent, this);
    }

    @DebugLog
    @Override
	public void loadCommentaires(String idItem) {
        this.idItem = idItem;
        view.showProgress();
        preferences.setCurrentCommentaireId(idItem);
		commentaryInteractor.getCommentaires(1, idItem, this, this);
	}

    @Override
    public void onLoadMore(int page, String idItem) {
        view.showProgressBar();
        commentaryInteractor.getCommentaires(page, idItem, this, this);
    }

    @Override
    public void onRefresh(String idItem) {
        view.showProgressBar();
        commentaryInteractor.getCommentaires(1, idItem, this, this);
    }


    @Override
    public void onBackPressed() {
        backpressed = true;
    }

    @DebugLog
    @Override
    public void onDestroy() {
        destroyed = true;
        preferences.setCurrentCommentaireId(null);
        commentaryInteractor.destroyCaller();
    }

    @Override
    public void uploadAudio(RequestBody file) {
        view.showProgressDialog();
        commentaryInteractor.sendAudioFile(file, this);
    }

    @Override
    public void onUploadAudioFile(String filename) {
        commentaryInteractor.sendAudioComment(idItem, filename, this);
    }

    @Override
    public void onAudioCommentSended(String message) {
        view.hideProgressDialog();
        view.showMessage(message);
        commentaryInteractor.getCommentaires(1, idItem, this, this);
    }

    @Override
    public void onUploadAudioFileError(String messageError) {
        view.hideProgressDialog();
        view.showMessage(messageError);
    }

    @DebugLog
    @Override
    public void onNewComment(ApiResponse.Commentaire commentaire) {

        CommentShowCard commentShowCard = new CommentShowCard(context, preferences);
        commentShowCard.setCommentaire(commentaire);
        commentShowCard.setIdUser(preferences.getIdUser());
        commentShowCard.setListener(this);
        commentShowCard.setIdContent(idItem);

        view.addComment(commentShowCard);
    }

    @DebugLog
    @Override
	public void onLoadCommentaires(ApiResponse.Commentaires arg0, int page) {

        if (destroyed || backpressed)
            return;

        view.hideProgress();
        view.hideProgressBar();

        if (page == 1) {
            view.clearCards();
            view.initLayout();
        }

        ArrayList<Card> cards = new ArrayList<Card>();

        for (ApiResponse.Commentaire commentaire : arg0.posts) {
            CommentShowCard commentShowCard = new CommentShowCard(context, preferences);
            commentShowCard.setCommentaire(commentaire);
            commentShowCard.setIdUser(preferences.getIdUser());
            commentShowCard.setListener(this);
            commentShowCard.setIdContent(idItem);
            cards.add(commentShowCard);
        }

        view.showCommentaires(cards);

	}
	
	@Override
	public void onLoadStateInGroup(boolean stateInGroup) {
        if (destroyed || backpressed)
            return;

        view.hideProgressBar();
        view.showStateInGroup(stateInGroup);
	}

	@Override
	public void sendComment(String comment, String idItem) {
		view.showProgressBar();
		commentaryInteractor.sendMessage(comment, idItem, this);
	}

	@Override
	public void onCommentSended(String idItem, String message) {
        view.playAnimationButton();
        commentaryInteractor.getCommentaires(1, idItem, this, this);
	}

    @Override
    public void onCommentSendedWithPhoto(String message) {
        view.navigateToBack(message);
    }

    @Override
	public void removeFromGroup(String idItem) {
		view.showProgressBar();
		notificationInteractor.removeFromGroup(idItem, this);
	}

	@Override
	public void joinGroup(String idItem) {
		view.showProgressBar();
		notificationInteractor.joinGroup(idItem, this);
	}

    @Override
    public void onActivityResult(String idItem) {
        view.showProgress();
        commentaryInteractor.getCommentaires(1, idItem, this, this);
    }

    @Override
	public void onSelectDeleteComment(String idComment, String idContent, CommentShowCard card) {
		view.showProgressBar();
		commentaryInteractor.deleteComment(idComment, idContent, card, this);
	}

	@Override
	public void onSelectUpdateComment(String idComment, String comment, CommentShowCard card) {
		view.showProgressBar();
		commentaryInteractor.updateComment(idComment, comment, card, this);
	}

	@Override
	public void onDeleteComment(String message, CommentShowCard card) {
		view.hideProgressBar();
		view.setCommentDeleted(message, card);
	}

	@Override
	public void onUpdateComment(String message, CommentShowCard card) {
		view.hideProgressBar();
		view.setCommentUpdated(message);
	}

    @Override
    public void onNewIntent(String idItem) {
        commentaryInteractor.destroyCaller();
        view.clearCards();
        view.showProgress();
        preferences.setCurrentCommentaireId(idItem);
        commentaryInteractor.getCommentaires(1, idItem, this, this);
    }

    @Override
    public void onLoadData(ApiResponse.Item item) {}

    @Override
    public void onPhotoUploaded(int position, String filename) {
        view.populateFilenames(position, filename);
    }

    @Override
    public void onPhotoDeleted(String message) {
        view.showMessageDelete(message);
    }

    @Override
    public void onPhotoUploadFailed(int position, String message) {
        view.setUploadError(position, message);
        view.hideProgress();
        view.hideProgressDialog();
    }

    @Override
    public void onInitItem(String idItem) {
        view.hideProgress();
        view.setIdItem(idItem);
    }

    @Override
    public void onCommentEmpty() {
        view.hideProgressBar();
        view.hideProgressDialog();
        view.setErrorComment();
    }
}
