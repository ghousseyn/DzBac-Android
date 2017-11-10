package com.squalala.dzbac.ui.notifications;

import com.squalala.dzbac.ActivityModule;
import com.squalala.dzbac.ActivityScope;
import com.squalala.dzbac.AppComponent;

import dagger.Component;

/**
 * Created by Back Packer
 * Date : 30/04/15
 */
@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = {
                NotificationModule.class,
                ActivityModule.class
        }
)
public interface NotificationComponent {
    void inject(NotificationActivity activity);

    NotificationPresenter getNotificationPresenter();
}
