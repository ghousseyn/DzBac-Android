package com.squalala.dzbac.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squalala.dzbac.R;
import com.squalala.dzbac.common.OnUserClickListener;
import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.ui.viewholders.UserViewHolder;

import java.util.List;

import hugo.weaving.DebugLog;

/**
 * Created by Fayçal KADDOURI on 13/10/16.
 */
public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {

    List<ApiResponse.User> list;
    OnUserClickListener mListener;

    //ajouter un constructeur prenant en entrée une liste
    public UserAdapter(List<ApiResponse.User> list, OnUserClickListener mListener) {
        this.list = list;
        this.mListener = mListener;
    }

    //cette fonction permet de créer les viewHolder
    //et par la même indiquer la vue à inflater (à partir des layout xml)
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_user, viewGroup,false);
        return new UserViewHolder(view);
    }

    //c'est ici que nous allons remplir notre cellule avec le texte/image de chaque MyObjects
    @DebugLog
    @Override
    public void onBindViewHolder(UserViewHolder myViewHolder, int position) {
        ApiResponse.User user = list.get(position);
        myViewHolder.bind(user, mListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}