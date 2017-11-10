package com.squalala.dzbac.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : DipToPixel.java
 * Date : 30 juil. 2014
 * 
 */
public class DpToPixel {
	
	public static int getPixel(int dip, Context context) {
		Resources r = context.getResources();
		int px = (int) TypedValue.applyDimension(
		        TypedValue.COMPLEX_UNIT_DIP,
		        dip, 
		        r.getDisplayMetrics()
		);
		
		return px;
	}

}
