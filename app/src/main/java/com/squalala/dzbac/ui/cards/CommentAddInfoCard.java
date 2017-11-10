package com.squalala.dzbac.ui.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.squalala.dzbac.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : AnnonceAddItemInfoCard.java
 * Date : 6 août 2014
 *
 */
public class CommentAddInfoCard extends Card {


    @InjectView(R.id.edit_commentaire) BootstrapEditText editDescription;


    public void setDescription(String description) {
        editDescription.setText(description);
    }


    public CommentAddInfoCard(Context context) {
        super(context, R.layout.card_info_add_comment_inner_main);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        ButterKnife.inject(this, parent);

        super.setupInnerViewElements(parent, view);
    }


    public String getDescription() {
        return editDescription.getText().toString().trim();
    }

    /**
     * @return the editDescription
     */
    public BootstrapEditText getEditDescription() {
        return editDescription;
    }


}
