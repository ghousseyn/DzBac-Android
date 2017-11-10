package com.squalala.dzbac.utils;

import android.content.Context;

import com.squalala.dzbac.R;

import hugo.weaving.DebugLog;

/**
 * Created by Back Packer
 * Date : 29/03/16
 */
public class TagsUtils {

    @DebugLog
    public static String getIdTag(Context context, String tag) {

        String [] tagsName = context.getResources().getStringArray(R.array.filiere_entries);
        System.out.println(tagsName);
        System.out.println(tag);

        for (int i = 0; i < tagsName.length; i++) {
            System.out.println("tagsName : " + tagsName[i] + " tag : " + tag);
            if (tagsName[i].equals(tag))
                return  String.valueOf(i + 1);
        }


        return null;
    }


    @DebugLog
    public static String getTagName(Context context, String tagId) {

        String [] tagsName = context.getResources().getStringArray(R.array.filiere_entries);
        System.out.println(tagsName);

        for (int i = 0; i < tagsName.length; i++)
            if ((Integer.valueOf(tagId) - 1) == i)
                return  tagsName[i];

        return null;

    }


    public static String getFiliere(int tagId) {

        switch (tagId) {

            case 1: return "sc";
            case 2: return "mat";
            case 3: return "matech";
            case 4: return "let";
            case 5: return "ges";

        }

        return "sc";
    }


}
