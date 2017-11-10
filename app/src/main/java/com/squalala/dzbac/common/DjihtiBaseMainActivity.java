package com.squalala.dzbac.common;

import android.os.Bundle;
import android.util.Log;

import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.DjihtiApp;
import com.squalala.dzbac.eventbus.events.BannEvent;

import org.greenrobot.eventbus.Subscribe;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import pl.tajchert.nammu.Nammu;

/**
 * Created by brio on 03/02/15.
 */
public abstract class DjihtiBaseMainActivity extends MaterialNavigationDrawer {

    private static final String TAG = DjihtiBaseMainActivity.class.getSimpleName();



    @Override
    public void init(Bundle bundle) {
       setupComponent((AppComponent) DjihtiApp.get(this).component());
    }

    @Subscribe
    public void onEvent(BannEvent event) {
        Log.e(TAG, "onEvent ban");
        finish();
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setBackgroundDrawable(new
                ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        // Pour cacher l'icone et le text en haut à gauche
    //    getSupportActionBar().setDisplayShowHomeEnabled(false);
        return super.onCreateOptionsMenu(menu);
    }*/

    protected abstract void setupComponent(AppComponent appComponent);


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
