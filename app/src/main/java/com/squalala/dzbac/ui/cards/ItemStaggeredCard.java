package com.squalala.dzbac.ui.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.squalala.dzbac.R;
import com.squalala.dzbac.data.Post;
import com.squalala.dzbac.utils.DynamicHeightPicassoCardThumbnailView;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardThumbnail;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ItemStaggeredCardThumb.java
 * Date : 10 juil. 2014
 * 
 */
public abstract class ItemStaggeredCard extends Card {


    protected Post item;
    protected Context context;
    protected Picasso picasso;
    
    @InjectView(R.id.gestion_item_staggered) LinearLayout linearGestionItem;
    @InjectView(R.id.trash_item_staggered) FontAwesomeText trashItemStaggered;
    @InjectView(R.id.redit_item_staggered) FontAwesomeText reditItemStaggered;
    @InjectView(R.id.trash_favoris_staggered) FontAwesomeText trashFavorisStaggered;
    @InjectView(R.id.delete_signale_staggered) FontAwesomeText fontSignalement;
    @InjectView(R.id.icon_uppost) FontAwesomeText fontUpPost;
    @InjectView(R.id.relative_delete_signale_staggered) View viewDeleteSignale;

    public ItemStaggeredCard(Context context, int layout_inner_main) {
        super(context, layout_inner_main);
        this.context = context;
    }

    public abstract void init();
    

    protected void initBottomCard(View view, Post item) {
    	TextView likes = (TextView) view.findViewById(R.id.item_number_heart);
    	likes.setText(String.valueOf(item.hearts));
    	
    	TextView comments = (TextView) view.findViewById(R.id.item_number_comment);
    	comments.setText(String.valueOf(item.comments));
    	
    	TextView views = (TextView) view.findViewById(R.id.item_number_view);
    	views.setText(String.valueOf(item.views));
    }
    
    protected void clearComs(View view) {
    	LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearlayout_comments);
    	linearLayout.removeAllViewsInLayout();
    	linearLayout.setVisibility(View.GONE);
    }
    
    protected void initLayoutComment(View view) {
    	
    	  LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearlayout_comments);
      	  linearLayout.removeAllViewsInLayout();

    }
   
    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
    	ButterKnife.inject(this, parent);
    }

    /**
     * A StaggeredCardThumbnail.
     * It uses a DynamicHeightPicassoCardThumbnailView which  maintains its own width to height ratio.
     */
    class StaggeredCardThumb extends CardThumbnail {

        Post item;

        public StaggeredCardThumb(Context context) {
            super(context);
            setExternalUsage(true);
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View viewImage) {
            //Use a DynamicHeightPicassoCardThumbnailView to maintain width/height ratio
            DynamicHeightPicassoCardThumbnailView thumbView = (DynamicHeightPicassoCardThumbnailView) getCardThumbnailView();
            thumbView.bindTo(item);
        }
    }

}
