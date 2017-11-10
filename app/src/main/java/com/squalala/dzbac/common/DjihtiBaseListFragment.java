package com.squalala.dzbac.common;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.DjihtiApp;
import com.squalala.dzbac.R;

import pl.tajchert.nammu.Nammu;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : DjihtiBaseListFragment.java
 * Date : 26 juin 2014
 * 
 */
public abstract class DjihtiBaseListFragment extends DjihtiBaseFragment {
	
	protected boolean mListShown;
    protected View mProgressContainer;
    protected View mListContainer;




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * Setup the list fragment
     *
     * @param root
     */
    protected void setupListFragmentAnnonce(View root) {

    	if (root != null) {
		  mListContainer = root.findViewById(R.id.annonce_listContainer);
	      mProgressContainer = root.findViewById(R.id.annonce_progressContainer);
	      mListShown = true;
    	}
     
    }
    
    @Override public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
        setupComponent((AppComponent) DjihtiApp.get(getActivity()).component());
      /*  try {
            ((DjihtiBaseMainActivity) getActivity()).addModules(getModules());
            ((DjihtiBaseMainActivity) getActivity()).inject(this);
        }
        catch (ClassCastException e) {
            ((DjihtiBaseActivity) getActivity()).addModules(getModules());
            ((DjihtiBaseActivity) getActivity()).inject(this);
        }*/

	}

    protected abstract void setupComponent(AppComponent appComponent);
    
    
    protected void displayList(){
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }

    protected void hideList(boolean animate){
        setListShown(false,animate);
    }

	@Override
	public int getTitleResourceId() {
		return 0;
	}
	
    public void setListShown(boolean shown) {
        setListShown(shown, true);
	}

    public void setListShownNoAnimation(boolean shown) {
        setListShown(shown, false);
    }
    
    /**
     * @param shown
     * @param animate
     */
    protected void setListShown(boolean shown, boolean animate) {
        if (mListShown == shown) {
            return;
        }
        mListShown = shown;
        if (shown) {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_out));
                mListContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_in));
            }
            mProgressContainer.setVisibility(View.GONE);
            mListContainer.setVisibility(View.VISIBLE);
        } else {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_in));
                mListContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_out));
            }
            mProgressContainer.setVisibility(View.VISIBLE);
            mListContainer.setVisibility(View.INVISIBLE);
        }
    }


}
