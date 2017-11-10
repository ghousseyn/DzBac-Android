package com.squalala.dzbac.utils;

import android.util.Log;

/**
 * Created by Back Packer
 * Date : 14/01/16
 */
public class LogUtils {

    public static void log(String result) {
        if (result.length() > 4000) {
            Log.e("Activit", "sb.length = " + result.length());
            int chunkCount = result.length() / 4000;     // integer division
            for (int i = 0; i <= chunkCount; i++) {
                int max = 4000 * (i + 1);
                if (max >= result.length()) {
                    Log.e("Activit", "chunk " + i + " of " + chunkCount + ":" + result.substring(4000 * i));
                } else {
                    Log.e("Activit", "chunk " + i + " of " + chunkCount + ":" + result.substring(4000 * i, max));
                }
            }
        } else {
            Log.e("Activit", result.toString());
        }
    }
}
