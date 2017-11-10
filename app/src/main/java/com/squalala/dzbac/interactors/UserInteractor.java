package com.squalala.dzbac.interactors;

import com.squalala.dzbac.data.UserInformations;
import com.squalala.dzbac.ui.friends.FriendsListener;
import com.squalala.dzbac.ui.list_followers.ListFollowerListener;
import com.squalala.dzbac.ui.main.MainListener;
import com.squalala.dzbac.ui.ranking.RankingListener;
import com.squalala.dzbac.ui.show_profile.ShowProfileListener;

import java.util.ArrayList;

import okhttp3.RequestBody;


/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ShowProfileInteractor.java
 * Date : 31 juil. 2014
 * 
 */
public interface UserInteractor {
	
	void getDataUser(String idMembre, ShowProfileListener listener);

	void updatePhoneUser(UserInformations userInformations, ShowProfileListener listener);
	
	void updateDataUser(UserInformations userInformations, ShowProfileListener listener);
	
	void updateAvatarUser(RequestBody typedFile, ShowProfileListener listener);
	
	void updateBackgroundUser(RequestBody typedFile, ShowProfileListener listener);

	void followUser(String idFollowed, ShowProfileListener mListener);

	void unFollowUser(String idFollowed, ShowProfileListener mListener);
	
	void getStateUser(MainListener listener);

	void getRankingUser(RankingListener listener);

	void getFriends(ArrayList<Long> listUsers, FriendsListener listener);

	void getFollowers(int page, String idMembre, ListFollowerListener listener);

	void getMyFollowings(int page, ListFollowerListener listener);

}
