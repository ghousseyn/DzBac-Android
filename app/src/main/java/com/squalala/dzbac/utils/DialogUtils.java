package com.squalala.dzbac.utils;

import android.content.Context;

import com.squalala.dzbac.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Back Packer
 * Date : 26/01/16
 */
public class DialogUtils {

    public static void showPopup(Context context, String title, String message) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setContentText(message)
                .setTitleText(title)
                .setConfirmText(context.getString(R.string.ok))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        sweetAlertDialog.dismissWithAnimation();

                    }
                });

        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
    }
}
