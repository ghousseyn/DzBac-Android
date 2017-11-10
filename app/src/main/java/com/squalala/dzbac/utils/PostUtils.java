package com.squalala.dzbac.utils;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.squalala.dzbac.R;

/**
 * Created by Back Packer
 * Date : 19/01/16
 */
public class PostUtils {

    public static String TypeStrToTypeCode(Context context, String typePost) {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                context,
                R.array.type_de_post,
                android.R.layout.simple_list_item_1);

        int positionTypePost = adapter.getPosition(typePost);

        adapter = ArrayAdapter.createFromResource(
                context,
                R.array.type,
                android.R.layout.simple_list_item_1);

        return  adapter.getItem(positionTypePost).toString();
    }

    public static String TypeCodeToTypeStr(Context context, String typePost) {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                context,
                R.array.type,
                android.R.layout.simple_list_item_1);

        int positionTypePost = adapter.getPosition(typePost);

        adapter = ArrayAdapter.createFromResource(
                context,
                R.array.type_de_post,
                android.R.layout.simple_list_item_1);

        return  adapter.getItem(positionTypePost).toString();
    }




}
