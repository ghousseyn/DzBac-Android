
package com.squalala.dzbac.common;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.DjihtiApp;
import com.squalala.dzbac.DjihtiConstant;
import com.squalala.dzbac.data.prefs.MainPreferences;

import javax.inject.Inject;

import pl.tajchert.nammu.Nammu;

public abstract class DjihtiBaseActivity extends AppCompatActivity {

    private final static String TAG = DjihtiBaseActivity.class.getSimpleName();
    @Inject
    MainPreferences mainPreferences;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupComponent((AppComponent) DjihtiApp.get(this).component());
    }

   /* public void onEvent(BannEvent event) {
        Log.e(TAG, "onEvent base");
        Intent intent_facebook = new Intent (this, SplashActivity.class);
        intent_facebook.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent_facebook);
    }*/

    protected abstract void setupComponent(AppComponent appComponent);



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            // Sample was denied WRITE_EXTERNAL_STORAGE permission
            Toast.makeText(this, "Videos will be saved in a cache directory instead of an external storage directory since permission was denied.", Toast.LENGTH_LONG).show();
        }
    }


}
