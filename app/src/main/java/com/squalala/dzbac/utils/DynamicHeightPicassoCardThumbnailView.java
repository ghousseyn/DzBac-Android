package com.squalala.dzbac.utils;

import android.content.Context;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;
import com.squalala.dzbac.data.Post;

import hugo.weaving.DebugLog;
import it.gmariotti.cardslib.library.view.component.CardThumbnailView;

/**
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class DynamicHeightPicassoCardThumbnailView extends CardThumbnailView {


    protected float mHeightRatio = 1;
    protected Post item;

    public DynamicHeightPicassoCardThumbnailView(Context context) {
        super(context);
    }

    public DynamicHeightPicassoCardThumbnailView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicHeightPicassoCardThumbnailView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void bindTo(Post item) {
        this.item = item;

        mHeightRatio = 1f * item.width / item.height;
        requestLayout();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        if (mode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException("layout_width must be match_parent");
        }

        int width = MeasureSpec.getSize(widthMeasureSpec);
        // Honor aspect ratio for height but no larger than 2x width.
        int height = Math.min((int) (width / mHeightRatio), width * 2);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        
        String resource = "rando_color_" + RandomInt.randInt(0, 399);
        int path = getResources().getIdentifier(resource, "color", getContext().getPackageName());

        if (height <= 0) // Pour éviter le problème de crash du au place quand
            height = 200; // il n'y a pas d'image

        Glide.with(getContext())
                .load(item.url_presentation)
                .override(width, height)
                .crossFade()
                .centerCrop()
                .placeholder(path)
                .into(mImageView);

    }
}
