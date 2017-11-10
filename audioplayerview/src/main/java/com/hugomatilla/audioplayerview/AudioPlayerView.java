package com.hugomatilla.audioplayerview;

/**
 * Created by hugomatilla on 10/02/16.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.io.IOException;

public class AudioPlayerView extends TextView
    {
    private static final String NULL_PARAMETER_ERROR = "`stopText`, `playText` and `loadingText`" +
            " must have some value, if `useIcons` is set to false. Set `useIcons` to true, or add strings to stopText`, " +
            "`playText` and `loadingText` in the AudioPlayerView.xml";
    private Context context;
    private String playText;
    private String stopText;
    private String loadingText;
    private boolean useIcons;

    //Constructors
    public AudioPlayerView(Context context) {
        super(context);
        System.out.println("Context context");
        this.context = context;
    }

    public AudioPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        System.out.println("Context context, AttributeSet attrs");
        this.context = context;
        getAttributes(attrs);
    }

    public AudioPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        System.out.println("Context context, AttributeSet attrs, int defStyleAttr");
        this.context = context;
        getAttributes(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AudioPlayerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        getAttributes(attrs);
    }


    public void getAttributes(AttributeSet attrs) {
        System.out.println("getAtttr");
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AudioPlayerTextView, 0, 0);

        try {
            stopText = a.getString(R.styleable.AudioPlayerTextView_stopText);
            playText = a.getString(R.styleable.AudioPlayerTextView_playText);
            loadingText = a.getString(R.styleable.AudioPlayerTextView_loadingText);
            useIcons = a.getBoolean(R.styleable.AudioPlayerTextView_useIcons, true);

            if ((stopText == null || playText == null || loadingText == null) && !useIcons)
                throw new UnsupportedOperationException(NULL_PARAMETER_ERROR);

        } finally {
            a.recycle();
        }

        setUpFont();
    }

    private void setUpFont() {
        Typeface iconFont = Typeface.createFromAsset(context.getAssets(), "audio-player-view-font.ttf");
        setTypeface(iconFont);
        playText = getResources().getString(R.string.playIcon);
        stopText = getResources().getString(R.string.stopIcon);
        loadingText = getResources().getString(R.string.loadingIcon);
        setText(playText);
    }

    public void setTextLoading() {
        setText(loadingText);
        if (useIcons)
            startAnimation();
    }


    public void setTextPlay() {
        clearAnimation();
        setText(playText);
    }

    private void startAnimation() {
        final Animation rotation = AnimationUtils.loadAnimation(context, R.anim.rotate_indefinitely);
        this.startAnimation(rotation);
    }

    public void setTextStop() {
        clearAnimation();
        setText(stopText);
    }



}