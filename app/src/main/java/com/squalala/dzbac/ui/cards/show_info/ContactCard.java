package com.squalala.dzbac.ui.cards.show_info;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squalala.dzbac.R;
import com.squalala.dzbac.DjihtiConstant;
import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.ui.show_profile.ShowProfileActivity;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Back Packer
 * Date : 03/01/16
 */
public class ContactCard extends Card {

    @InjectView(R.id.avatar_user)
    CircleImageView avatar_user;
    @InjectView(R.id.pseudo_user)
    TextView pseudo_user;

    private ApiResponse.RankUser friend;

    public ContactCard(Context context, ApiResponse.RankUser friend) {
        super(context, R.layout.card_contact_show_inner_main);
        this.friend = friend;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        ButterKnife.inject(this, view);

        pseudo_user.setText(friend.username);

        if (friend.level >= DjihtiConstant.ADMIN_GRADUATION)
            pseudo_user.setTextColor(getContext().getResources().getColor(android.R.color.holo_red_dark));
        else if (friend.level == DjihtiConstant.BANNED_GRADUATION)
            pseudo_user.setTextColor(getContext().getResources().getColor(android.R.color.darker_gray));
        else
            pseudo_user.setTextColor(getContext().getResources().getColor(android.R.color.black));

        Picasso.with(getContext())
                .load(friend.urlAvatar)
                .placeholder(R.drawable.djihti_photo)
                .into(avatar_user);

        setOnClickListener(new OnCardClickListener() {

            @Override
            public void onClick(Card arg0, View arg1) {
                Intent intent = new Intent(getContext(), ShowProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id_membre", friend.id_membre);
                getContext().startActivity(intent);
            }
        });

        super.setupInnerViewElements(parent, view);
    }
}
