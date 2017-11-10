package com.squalala.dzbac.ui.cards;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squalala.dzbac.R;
import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.ui.show_profile.ShowProfileActivity;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Back Packer
 * Date : 24/09/15
 */
public class UserCard extends Card {

    @InjectView(R.id.avatar_user)
    CircleImageView avatar_user;
    @InjectView(R.id.pseudo_user)
    TextView pseudo_user;

    private int count;

    private ApiResponse.User rankUser;

    public UserCard(Activity activity) {
        super(activity, R.layout.card_user_show_inner_main);

    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setUser(ApiResponse.User rankUser) {
        this.rankUser = rankUser;
    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        ButterKnife.inject(this, view);

        pseudo_user.setText(rankUser.userName);

        Picasso.with(getContext())
                .load(rankUser.url_avatar_thumbnail)
                .placeholder(R.drawable.djihti_photo)
                .into(avatar_user);

        setOnClickListener(new OnCardClickListener() {

            @Override
            public void onClick(Card arg0, View arg1) {
                System.out.println("id_membre : " + rankUser.idMembre);
                Intent intent = new Intent(getContext(), ShowProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id_membre", rankUser.idMembre);
                getContext().startActivity(intent);
            }
        });

        super.setupInnerViewElements(parent, view);
    }

}