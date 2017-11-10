package com.squalala.dzbac.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.analytics.Tracker;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.R;
import com.squalala.dzbac.common.DjihtiBaseActivity;
import com.squalala.dzbac.data.prefs.MainPreferences;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class MainMyItemActivity extends DjihtiBaseActivity 
	implements MainView {
	
	private static final String TAG = "MainMYItemActivity";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
        
        setContentView(R.layout.activity_main);
        
        ButterKnife.inject(this);
    }

	@Override
	public void showPopupMultipleDevice() {

	}

	@Override
    protected void onSaveInstanceState(Bundle outState) {
    	// Pour éviter le crash de l'application quand on change d'orientation
    	//super.onSaveInstanceState(outState);
    }

	@Override
	public void setErrorPremium() {

	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getSupportActionBar().setDisplayShowTitleEnabled(true);
    	setTitle("ok");
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    protected void onActivityResult(int arg0, int resultCode, Intent arg2) {
    	
    	if (resultCode == RESULT_OK) {
    		finish();
    		startActivity(getIntent());
    	}
    	
    	super.onActivityResult(arg0, resultCode, arg2);
    }
    

/*	@Override
	protected List<Object> getModules() {
		return Arrays.<Object>asList(new MainModule(this, this),
									 new DialogModule(this),
									 new ActivityModule(this));
	}*/

	@Override
	protected void setupComponent(AppComponent appComponent) {

	}

	@Override
	public void showPopupVersionOutdated() {

	}

	@Override
	public void setCountMessages(int countMessage) {
	}

	@Override
	public void setCountNotifications(int countNotification) {
	}

	@Override
	public void showPopupBan() {

	}

	public interface Updateable {
		
		/**
		 * Dans le cas où la personne consulte un item et le supprime des favoris,
		 * on appelle update() qui appelle removeItem() 
		 * 
		 */
		public void update();
		
		/**
		 *  Dans le cas où la personne ne retire pas l'item de ses favoris, alors on remet toutes
		 *  les cartes en état cliquables, car la carte qu'aura consulté l'utilisateur, son clique sera 
		 *  désactivé
		 */
		public void setAllClickable();
		
	}

   
}
