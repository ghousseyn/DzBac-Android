package com.squalala.dzbac.ui.sms_verification;

import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.interactors.UserInteractor;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Back Packer
 * Date : 04/12/15
 */
@Module
public class SmsVerificationModule {

    private SmsVerificationView view;

    public SmsVerificationModule(SmsVerificationView view) {
        this.view = view;
    }

    @Provides
    public SmsVerificationView provideSmsVerificationView() {
        return view;
    }

    @Provides
    public SmsVerificationPresenter provideSmsVerificationPresenter(
            SmsVerificationView view, UserInteractor interactor, MainPreferences mainPreferences
    ) {
        return new SmsVerificationPresenterImpl(view, interactor, mainPreferences);
    }


}
