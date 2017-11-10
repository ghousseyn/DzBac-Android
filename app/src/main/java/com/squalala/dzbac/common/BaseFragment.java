package com.squalala.dzbac.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.DjihtiApp;

/**
 * Created by Back Packer
 * Date : 15/04/15
 */
public abstract class BaseFragment extends Fragment {


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupComponent((AppComponent) DjihtiApp.get(getActivity()).component());
  //      ((DjihtiBaseActivity) getActivity()).addModules(getModules());
   //     ((DjihtiBaseActivity) getActivity()).inject(this);
    }

    protected abstract void setupComponent(AppComponent appComponent);
}
