package com.squalala.dzbac.ui.add_item;

import android.app.Activity;
import android.app.Application;

import com.squalala.dzbac.R;
import com.squalala.dzbac.data.PostAdd;
import com.squalala.dzbac.interactors.ItemInteractor;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dagger.Module;
import dagger.Provides;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : AddItemModule.java
 * Date : 6 août 2014
 * 
 */
@Module
public class AddItemModule {
	
	private AddItemView addItemView;

	public AddItemModule(AddItemView view) {
		addItemView = view;
	}
	
	@Provides
	public AddItemView provideAddItemView() {
		return addItemView;
	}
	
	@Provides
	public PostAdd provideItemAdd() {
		return new PostAdd();
	}
	
   @Provides
   public SweetAlertDialog provideSweetAlertDialog(Activity activity) {

        SweetAlertDialog pDialog = new SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(activity.getResources().getColor(R.color.vert_icon_app));
        pDialog.setTitleText(activity.getString(R.string.patientez));
        pDialog.setCancelable(false);

        return pDialog;
    }

	
	@Provides
	public AddItemPresenter provideAddItemPresenter(AddItemView view,
			ItemInteractor interactor, Application app) {
		return new AddItemPresenterImpl(interactor, view, app.getApplicationContext());
	}

}
