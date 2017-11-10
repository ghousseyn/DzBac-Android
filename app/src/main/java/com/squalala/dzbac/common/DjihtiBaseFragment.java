
package com.squalala.dzbac.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import pl.tajchert.nammu.Nammu;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : DjihtiBaseFragment.java
 * Date : 20 juin 2014
 * 
 */
public abstract class DjihtiBaseFragment extends Fragment {
	
	
	@Override
	public void onResume() {
		super.onResume();
		setTitle();
	}
	
	public abstract int getTitleResourceId();
	
    protected void setTitle() {
      /*  if (getActivity() != null) {
            ((ActionBarActivity) getActivity()).setTitle(getTitleResourceId());
            ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(getTitleResourceId());
        }*/
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
