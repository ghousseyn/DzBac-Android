package com.squalala.dzbac.ui.list_items;

import android.app.Activity;

import com.squalala.dzbac.data.Post;
import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.data.api.PostsService;
import com.squalala.dzbac.data.mock.MockPostLoader;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.interactors.ListItemInteractor;
import com.squalala.dzbac.ui.cards.AnnonceMyItemsStaggeredCard;
import com.squalala.dzbac.ui.cards.PostStaggeredCard;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ListItemPresenterImpl.java
 * Date : 8 juil. 2014
 * 
 */
public class ListItemPresenterImpl implements ListItemPresenter, ListItemListener {
	
	private ListItemView listItemView;
	private ListItemInteractor listItemInteractor;
    private String membreId;
    private Activity activity;
    private MainPreferences mainPreferences;

    private boolean signalement;
	
	public ListItemPresenterImpl(ListItemView listItemView, ListItemInteractor listItemInteractor, MainPreferences mainPreferences) {
		this.listItemInteractor = listItemInteractor;
		this.listItemView = listItemView;
        this.mainPreferences = mainPreferences;
	}

    public boolean isSignalement() {
        return signalement;
    }

    @Override
    public void setSignalement(boolean signalement) {
        this.signalement = signalement;
    }

    @Override
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
	public void getDataItems(boolean isMyItem, boolean isLikes, int page, String membreId) {
        this.membreId = membreId;
        listItemView.showProgess();
		listItemInteractor.loadItems(isMyItem, isLikes, page, membreId, this);
	}

    @Override
    public void getPostsSignaled(int page) {
        listItemView.showProgess();
        listItemInteractor.loadPostsSignaled(page, this);
    }

    @Override
    public void onResume(boolean isMyItem, boolean isLikes) {
        listItemView.showProgressBar();
        listItemInteractor.loadItems(isMyItem, isLikes, 1, membreId, this);
    }

    @Override
	public void onLoadDataItems(ApiResponse.PostsItems items, int page, PostsService postsService) {
        listItemView.hideProgressBar();
     //   listItemView.hideProgress();

        ArrayList<Card> cards = new ArrayList<>();

        MockPostLoader mockPostLoader = new MockPostLoader();

        for (ApiResponse.Items item : items.posts)
        {
            PostStaggeredCard postStaggeredCard = null;

//	if (isMyPosts)
            postStaggeredCard = new AnnonceMyItemsStaggeredCard(activity, postsService,
                    mainPreferences,  this);

            postStaggeredCard.setPost((Post) mockPostLoader.newItem()
                    .idItem(item.id)
                    .urlPresentation(item.url_presentation)
                    .width(item.width_url_image_presentation)
                    .height(item.height_url_image_presentation)
                    .titre(item.title)
                    .idMembre(item.id_membre)
                    .type(item.type)
                    .matiere(item.matiere)
                    .isVideo(item.isVideo == 1)
                    .isAudio(item.isAudio == 1)
                    .urlAvatar(item.url_avatar)
                    .pseudo(item.pseudo)
                    .levelContribution(item.levelContribution)
                            //	.type(translater.translate(R.array.type ,item.type))
                            //.matiere(translater.translate(R.array.subject, item.matiere))
                    .hearts(item.like)
                    .views(item.vues)
                    .comments(item.nombre_commentaire)
                    .build());

            postStaggeredCard.init();
            postStaggeredCard.setSignalement(isSignalement());

            cards.add(postStaggeredCard);
        }

        if (page == 1) {
            listItemView.clearCards();
            listItemView.initScrollDown();
        }


		listItemView.showItems(cards);
	}

    @Override
    public void onLoadMore(boolean isMyItem, boolean isLikes, int page) {
        listItemView.showProgressBar();
        listItemInteractor.loadItems(isMyItem, isLikes, page, membreId, this);
    }

    @Override
    public void onRefresh(boolean isMyItem, boolean isLikes) {
        listItemView.showProgressBar();
        listItemInteractor.loadItems(isMyItem, isLikes, 1, membreId, this);
    }

    @Override
	public void onRemoveItem() {
		listItemView.removeItem();
	}

	@Override
	public void onLoad() {
		listItemView.showProgess();
	}
	


}
