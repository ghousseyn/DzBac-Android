package com.squalala.dzbac.ui.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squalala.dzbac.R;
import com.squalala.dzbac.common.OnUserClickListener;
import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.utils.DateUtils;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Fayçal KADDOURI on 13/10/16.
 */
public class UserViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.imageUser) ImageView imageUser;
    @InjectView(R.id.about) TextView txtAbout;
    @InjectView(R.id.localisation) TextView txtLocalisation;
    @InjectView(R.id.dateLastActivity) TextView txtDateLastActivity;
    @InjectView(R.id.username) TextView txtUsername;

    private View view;

    //itemView est la vue correspondante à 1 cellule
    public UserViewHolder(View itemView) {
        super(itemView);
        this.view = itemView;

        ButterKnife.inject(this, itemView);
    }

    //puis ajouter une fonction pour remplir la cellule en fonction d'un MyObject
    public void bind(final ApiResponse.User user, final OnUserClickListener mListener){
        txtAbout.setText(user.apropos);
        txtLocalisation.setText(user.localisation);
        txtDateLastActivity.setText(txtDateLastActivity.getContext().getString(R.string.actif,
                DateUtils.getRelativeTime(DateUtils.strToDate(user.dateLastActivity)).toLowerCase()));
        txtUsername.setText(user.userName);
        Picasso.with(imageUser.getContext()).load(user.url_avatar_thumbnail).fit().centerCrop().into(imageUser);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onUserclick(user.idMembre);
            }
        });
    }
}

