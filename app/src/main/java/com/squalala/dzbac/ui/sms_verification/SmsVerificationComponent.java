package com.squalala.dzbac.ui.sms_verification;

import com.squalala.dzbac.ActivityScope;
import com.squalala.dzbac.AppComponent;

import dagger.Component;

/**
 * Created by Back Packer
 * Date : 04/12/15
 */
@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = SmsVerificationModule.class
)
public interface SmsVerificationComponent {

    void inject(SmsVerificationActivity activity);

    SmsVerificationPresenter getSmsVerificationPresenter();


}
