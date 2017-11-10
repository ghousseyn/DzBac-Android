package com.squalala.dzbac.utils;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

import hugo.weaving.DebugLog;

/**
 * Created by Back Packer
 * Date : 25/11/15
 */
public class SystemUtils {

    /**
     *  Le fichier de bannisssement s'apppelle "dzbac.b"
     */

   // private static final String PATH_FILE_BAN = "/DzBac/dzbac.b";
    private static final String PATH_FILE_BAN = "/.yemak/";

    private static final String TAG = SystemUtils.class.getSimpleName();

    @DebugLog
    public static boolean isFileBanExist() {
        File file = new File(Environment.getExternalStorageDirectory() + PATH_FILE_BAN);

        // Cela veut dire que la personne a déjà été banni
        if (file != null && file.exists())
            return true;
        else
            return false;
    }


    @DebugLog
    private static void createFileBan() {

        File file = new File(Environment.getExternalStorageDirectory() + PATH_FILE_BAN);

        if (!file.exists())
            System.out.println(file.mkdirs());
    }

    @DebugLog
    public static void checkMe() {
        File file = new File(Environment.getExternalStorageDirectory() + PATH_FILE_BAN);
        System.out.println(file.getAbsolutePath());
        System.out.println(file.exists());
        System.out.println(file.isDirectory());
        System.out.println(file.isHidden());
    }

    @DebugLog
    public static void cleanDzBacFolder() {

        createFileBan();

        File dir = new File(Environment.getExternalStorageDirectory()+ "/Dzbac");

        try {
            if (dir.isDirectory())
            {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++)
                {
                    //   Log.e(TAG, children[i]);
                    new File(dir, children[i]).delete();
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }



}
