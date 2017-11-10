package com.squalala.dzbac.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Arrays;
import java.util.Locale;

/**
 * Created by brio on 11/02/15.
 */
public class Translater {

    private Context context;
    private Resources defaultResources;

    private static final String TAG = Translater.class.getSimpleName();


    public Translater(Context context) {
        this.context = context;
    }



    private String getStringInDefaultLocale(int resId) {
        Resources currentResources = context.getResources();
        AssetManager assets = currentResources.getAssets();
        DisplayMetrics metrics = currentResources.getDisplayMetrics();
        Configuration config = new Configuration(
                currentResources.getConfiguration());
        config.locale = Locale.FRANCE;
    /*
     * Note: This (temporiarily) changes the devices locale! TODO find a
     * better way to get the string in the specific locale
     */
        Resources defaultLocaleResources = new Resources(assets, metrics,
                config);
        String string = defaultLocaleResources.getString(resId);
        // Restore device-specific locale
        new Resources(assets, metrics, currentResources.getConfiguration());
        return string;
    }

    private String [] getStringArrayInDefaultLocale(int resId) {
        Resources currentResources = context.getResources();
        AssetManager assets = currentResources.getAssets();
        DisplayMetrics metrics = currentResources.getDisplayMetrics();
        Configuration config = new Configuration(
                currentResources.getConfiguration());
        config.locale = Locale.FRANCE;
    /*
     * Note: This (temporiarily) changes the devices locale! TODO find a
     * better way to get the string in the specific locale
     */
        Resources defaultLocaleResources = new Resources(assets, metrics,
                config);
        String array [] = defaultLocaleResources.getStringArray(resId);
        // Restore device-specific locale
        new Resources(assets, metrics, currentResources.getConfiguration());
        return array;
    }

    /*
     * Dans le cas ou l'utilisateur poste quelque chose
     * Donc on doit la traduire en français
     */
    public String translateToFrench(int res, int position, String defaultStr) {

        if (!Locale.getDefault().getLanguage().equals(Locale.FRANCE.getLanguage()))
        {

            String [] array = getStringArrayInDefaultLocale(res);

            return array[position];
        }
        else
            return defaultStr;
    }

    /*
    * Dans le cas où on récpuère l'information du serveur et
    * l'utilisateur n'a pas le français
    */
    public String translate(int res, String word) {

        if (Locale.getDefault().getLanguage().equals(Locale.FRANCE))
            return word;
        else
        {
            String defaultArray [] = context.getResources().getStringArray(res);

            String [] array= getStringArrayInDefaultLocale(res);
            int position = Arrays.asList(array).indexOf(word);

            if (position == -1)
                return word;

            Log.e(TAG, array[0] + " " + position + word) ;

            Log.e(TAG, defaultArray[0] + " *****" + position) ;

            return defaultArray[position];
        }

    }


}
