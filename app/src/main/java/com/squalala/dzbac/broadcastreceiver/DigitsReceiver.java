package com.squalala.dzbac.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.digits.sdk.android.ContactsUploadResult;
import com.digits.sdk.android.ContactsUploadService;
import com.squalala.dzbac.eventbus.events.events.ContactsEvent;

import org.greenrobot.eventbus.EventBus;

import hugo.weaving.DebugLog;

/**
 * Created by Back Packer
 * Date : 03/01/16
 */
public class DigitsReceiver extends BroadcastReceiver {

    @DebugLog
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ContactsUploadService.UPLOAD_COMPLETE.equals(intent.getAction())) {
            ContactsUploadResult result = intent
                    .getParcelableExtra(ContactsUploadService.UPLOAD_COMPLETE_EXTRA);


            EventBus.getDefault().post(new ContactsEvent());
            // Post success notification
        } else {
            // Post failure notification
        }
    }
}