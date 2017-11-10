package com.squalala.dzbac.ui.list_followers;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Back Packer
 * Date : 24/09/15
 */
public interface ListFollowerView {

    void displayListUsers(ArrayList<Card> cards);

    void initScrollDown();

    void clearCards();
}
