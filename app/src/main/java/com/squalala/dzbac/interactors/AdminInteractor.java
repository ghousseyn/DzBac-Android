package com.squalala.dzbac.interactors;

import com.squalala.dzbac.ui.show_profile.OnUserBannedListener;

/**
 * Created by Back Packer
 * Date : 19/05/15
 */
public interface AdminInteractor {

    void bannUser(String UserId, OnUserBannedListener listener);
}
