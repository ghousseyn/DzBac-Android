package com.squalala.dzbac.common;


/**
 * Created by faycal on 01/05/16.
 */

import android.os.Bundle;

import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.DjihtiApp;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import javax.inject.Inject;

import pl.tajchert.nammu.Nammu;

public abstract class BaseRxActivity extends RxAppCompatActivity {

    private final static String TAG = BaseRxActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     setupComponent((AppComponent) DjihtiApp.get(this).component());
    }

    protected abstract void setupComponent(AppComponent appComponent);

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
