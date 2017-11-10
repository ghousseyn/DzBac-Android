package com.squalala.dzbac.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by Back Packer
 * Date : 23/05/15
 */
public class FacebookUtils {


    public static boolean isExist(Context context) {
        try{
            ApplicationInfo info = context.getPackageManager().
                    getApplicationInfo("com.facebook.android", 0 );
            return true;
        } catch( PackageManager.NameNotFoundException e ){
            return false;
        }
    }

}
