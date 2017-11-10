package com.squalala.dzbac.ui.comment;

import com.squalala.dzbac.ui.cards.CommentShowCard;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : CommentaryView.java
 * Date : 30 juil. 2014
 * 
 */
public interface CommentaryView {

    void showProgressDialog();

    void hideProgressDialog();

    void showMessage(String message);
	
	void showCommentaires(ArrayList<Card> cards);

    void addComment(Card card);
	
	void showStateInGroup(boolean stateInGroup);
	
	void showProgress();
	
	void hideProgress();

    void showProgressBar();

    void hideProgressBar();

    void initLayout();

    void playAnimationButton();

    void clearCards();

	void setCommentDeleted(String message, CommentShowCard card);
	
	void setCommentUpdated(String message);

    /**
     * Cette fonction sert à remplir le tableau filenames des noms des images afin
     * de pouvoir les suppprimer après dans la class AlertPhotoManager
     */
    void populateFilenames(int position, String filename);


    /**
     *  On montre un message quand une image est supprimée
     */
    void showMessageDelete(String message);

    /**
     *  On initialise notre item et on récupère l'id
     */
    void setIdItem(String idItem);

    void setUploadError(int position, String message);

    /**
     * Afficher un message et quitter l'activity
     * @param message
     */
    void navigateToBack(String message);


    void setErrorComment();
	
}
