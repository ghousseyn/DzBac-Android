package com.squalala.dzbac.ui.cards;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.FontAwesomeText;
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
 * Date : 24/09/15
 */
public class RankCard extends Card {

    @InjectView(R.id.avatar_user)
    CircleImageView avatar_user;
    @InjectView(R.id.pseudo_user)
    TextView pseudo_user;
    @InjectView(R.id.txt_points)
    TextView points;

    @InjectView(R.id.txt_count)
    TextView txtCount;

    private int count;

    private ApiResponse.RankUser rankUser;

    public RankCard(Activity activity) {
        super(activity, R.layout.card_ranking_show_inner_main);

    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setRankUser(ApiResponse.RankUser rankUser) {
        this.rankUser = rankUser;
    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        ButterKnife.inject(this, view);

        pseudo_user.setText(rankUser.username);

        FontAwesomeText fontContribution = (FontAwesomeText) view.findViewById(R.id.font_contribution);
        fontContribution.setVisibility(View.VISIBLE);

        switch (rankUser.levelContribution) {
            case 0 : fontContribution.setVisibility(View.GONE);break;
            case 1 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.vert_smargadin)); break;
            case 2 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.violet)); break;
            case 3 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.bleu)); break;
            case 4 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.red_end_call)); break;
            case 5 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.bleu)); break;
            case 6 : fontContribution.setTextColor(ContextCompat.getColor(getContext(), R.color.violet)); break;
        }

        if (rankUser.level >= DjihtiConstant.ADMIN_GRADUATION)
            pseudo_user.setTextColor(getContext().getResources().getColor(android.R.color.holo_red_dark));
        else if (rankUser.level == DjihtiConstant.BANNED_GRADUATION)
            pseudo_user.setTextColor(getContext().getResources().getColor(android.R.color.darker_gray));
        else
            pseudo_user.setTextColor(getContext().getResources().getColor(android.R.color.black));


        if (rankUser.level >= DjihtiConstant.HEIGHT_ADMIN_GRADUATION)
            pseudo_user.setTextColor(getContext().getResources().getColor(R.color.color3));
        else if (rankUser.level >= DjihtiConstant.MEDIUM_ADMIN_GRADUATION)
            pseudo_user.setTextColor(getContext().getResources().getColor(R.color.rouge_nacarat));

        Picasso.with(getContext())
                .load(rankUser.urlAvatar)
                .placeholder(R.drawable.djihti_photo)
                .into(avatar_user);

        points.setText(rankUser.likes + " " +getContext().getString(R.string.points));

        txtCount.setText(count+"");

        setOnClickListener(new OnCardClickListener() {

            @Override
            public void onClick(Card arg0, View arg1) {
                Intent intent = new Intent(getContext(), ShowProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id_membre", rankUser.id_membre);
                getContext().startActivity(intent);
            }
        });

        super.setupInnerViewElements(parent, view);
    }

}