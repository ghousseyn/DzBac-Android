package com.squalala.dzbac.common.listener;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : GroupListener.java
 * Date : 3 sept. 2014
 * 
 */
public interface GroupListener {
	
	public void removeFromGroup(String idItem);
	
	public void joinGroup(String idItem);
	
	public void onLoadStateInGroup(boolean stateInGroup);
	
/*	public void onRemoveFromGroup();
	
	public void onJoinGroup();
*/
}
