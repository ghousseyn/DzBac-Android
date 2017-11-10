package com.squalala.dzbac.utils;

import android.util.Log;

import com.squalala.dzbac.DjihtiConstant;

import java.io.File;

/**
 * Created by Back Packer
 * Date : 01/03/15
 */
public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    public static void cleanFilesTemp() {
        Log.e(TAG, "cleanFilesTemp");

        File parentDir = new File(DjihtiConstant.PATH_TEMP_FILE);

        if (parentDir != null && parentDir.exists() && parentDir.isDirectory()) {
            Log.e(TAG, "directory exist");

            File[] files = parentDir.listFiles();

            for (File file : files) {

                if (file.isFile()) {

                    if (file.delete()){
                        System.out.println("File deleted");
                    }
                    else
                        System.out.println("File NOT deleted");
                }

                System.out.println("FILE :" + file);
            }
        }


    }

    public static String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf("."));
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }


}
