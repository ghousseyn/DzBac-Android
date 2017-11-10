package com.squalala.dzbac.data;

import android.app.backup.BackupAgentHelper;
import android.app.backup.SharedPreferencesBackupHelper;

import hugo.weaving.DebugLog;

/**
 * Created by Back Packer
 * Date : 26/11/15
 */
public class MyBackupAgent extends BackupAgentHelper {

    // The name of the SharedPreferences file
    private static final String PREFS = "dzbac";

    // A key to uniquely identify the set of backup data
    private static final String PREFS_BACKUP_KEY = "dzbac";

    @DebugLog
    @Override
    public void onCreate() {
        SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper(this, PREFS);
        addHelper(PREFS_BACKUP_KEY, helper);
    }

}
