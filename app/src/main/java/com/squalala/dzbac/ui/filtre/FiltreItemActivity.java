package com.squalala.dzbac.ui.filtre;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.SearchEvent;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.squalala.dzbac.DjihtiApp;
import com.squalala.dzbac.R;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.ui.cards.AnnonceAddItemInfoCard;
import com.squalala.dzbac.ui.cards.FiltreItemCard;
import com.squalala.dzbac.ui.cards.FiltrePostCard;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import hugo.weaving.DebugLog;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardView;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : MapActivity.java
 * Date : 18 juil. 2014
 * 
 */
public class FiltreItemActivity extends AppCompatActivity
        implements MaterialTabListener {

    @InjectView(R.id.materialTabHost) MaterialTabHost tabHost;
    @InjectView(R.id.view_pager) ViewPager pager;
    @InjectView(R.id.tool_bar) Toolbar mToolbar;

    private ViewPagerAdapter pagerAdapter;

	private static final String TAG = FiltreItemActivity.class.getSimpleName();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filtre);

        ButterKnife.inject(this);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                tabHost.setSelectedNavigationItem(position);
            }
        });

        // insert all tabs from pagerAdapter data
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setIcon(getIcon(i))
                            .setTabListener(this)
            );
        }

		DjihtiApp.tracker.setScreenName(getClass().getName());
		
	}


    @DebugLog
    @Override
    public void onTabSelected(MaterialTab tab) {
// when the tab is clicked the pager swipe content to the tab position
        pager.setCurrentItem(tab.getPosition());
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @DebugLog
        public Fragment getItem(int num) {
            switch (num) {
                case 0 : return new FiltrePostFragment();
                case 1: return new FiltreUserFragment();
            }

            return new FiltrePostFragment();
        }
        @Override
        public int getCount() {
            return 2;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0: return getString(R.string.filtre_post);
                case 1: return getString(R.string.filtre_user);
                default: return null;
            }
        }
    }
    /*
    * It doesn't matter the color of the icons, but they must have solid colors
    */
    private Drawable getIcon(int position) {
        switch(position) {
            case 0:
                return ContextCompat.getDrawable(this, R.drawable.ic_create_white_24dp);
            case 1:
                return ContextCompat.getDrawable(this, R.drawable.ic_people_white_24dp);
        }
        return null;
    }


    @Override
    public void onTabReselected(MaterialTab tab) {}

    @Override
    public void onTabUnselected(MaterialTab tab) {}

    @Override
	protected void onDestroy() {
		Crouton.cancelAllCroutons();
		super.onDestroy();
	}


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

        case android.R.id.home:

            finish();

            return true;

        default:
            break;
        }


        return super.onOptionsItemSelected(item);
    }


}
