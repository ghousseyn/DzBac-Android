package com.squalala.dzbac.ui.about;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.R;
import com.squalala.dzbac.common.BaseFragment;
import com.squalala.dzbac.data.api.ApiResponse.User;
import com.squalala.dzbac.eventbus.events.UserEvent;
import com.squalala.dzbac.ui.list_followers.ListFollowerActivity;
import com.squalala.dzbac.ui.payement.PayementActivity;
import com.squalala.dzbac.utils.DateUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Back Packer
 * Date : 12/04/15
 */
public class AboutFragment extends BaseFragment
    implements AboutView {

    @InjectView(R.id.txt_specialite_about)
    TextView mSpecialite;
    @InjectView(R.id.txt_localisation_about)
    TextView mLocalisation;
    @InjectView(R.id.txt_likes_about)
    TextView mLikes;

    @InjectView(R.id.txt_contribution_about)
    TextView mContribution;

    @InjectView(R.id.txt_followers_about)
    TextView mFollowing;

    @InjectView(R.id.txt_date_creation)
    TextView mDateCretion;

    @InjectView(R.id.txt_date_last_activity)
    TextView mDateLastActivity;

    @InjectView(R.id.icon_contribution_profil)
    ImageView imgContribution;

    @InjectView(R.id.icon_location_profil)
    ImageView icon_location_profil;

    @InjectView(R.id.icon_school_profil)
    ImageView icon_school_profil;

    @InjectView(R.id.icon_followers_profil)
    ImageView icon_follwer_profil;

    @InjectView(R.id.icon_last_activity)
    ImageView icon_last_activity;

    @InjectView(R.id.icon_date_creation)
    ImageView icon_date_creation;

    @InjectView(R.id.icon_likes_profil)
    ImageView icon_likes_profil;


    @InjectView(R.id.layout_follow)
    RelativeLayout mLayout;

    @Inject AboutPresenter presenter;

    private static final String KEY_ID_MEMBRE = "id_membre";

    private String idMembre;


    public static AboutFragment newInstance(String membreId) {

        AboutFragment fragment = new AboutFragment();

        Bundle args = new Bundle();

        args.putString(KEY_ID_MEMBRE, membreId);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = null;

        root = inflater.inflate(R.layout.fragment_about, container, false);

        ButterKnife.inject(this, root);

        icon_date_creation.setColorFilter(ContextCompat.getColor(getContext(), R.color.vert_smargadin));
        icon_follwer_profil.setColorFilter(ContextCompat.getColor(getContext(), R.color.bleu));
        icon_last_activity.setColorFilter(ContextCompat.getColor(getContext(), R.color.orange));
        icon_location_profil.setColorFilter(ContextCompat.getColor(getContext(), R.color.rouge_nacarat));
        icon_school_profil.setColorFilter(ContextCompat.getColor(getContext(), R.color.jaune_paille));
        icon_likes_profil.setColorFilter(ContextCompat.getColor(getContext(), R.color.red_end_call));

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        idMembre = getArguments().getString(KEY_ID_MEMBRE);

        presenter.onResume(idMembre);
    }

    @Override
    public void setUserDetails(User user) {

        try {

            switch (user.levelContribution) {
                case 0: mContribution.setText(getString(R.string.aucune)); break;
                case 1: mContribution.setText(getString(R.string.petit_malin)); mContribution.setTypeface(null, Typeface.BOLD); imgContribution.setColorFilter(ContextCompat.getColor(getContext(), R.color.vert_smargadin)); break;
                case 2: mContribution.setText(getString(R.string.sauveteur)); mContribution.setTypeface(null, Typeface.BOLD); imgContribution.setColorFilter(ContextCompat.getColor(getContext(), R.color.violet));break;
                case 3: mContribution.setText(getString(R.string.hero)); mContribution.setTypeface(null, Typeface.BOLD); imgContribution.setColorFilter(ContextCompat.getColor(getContext(), R.color.bleu));  ;break;
                case 4: mContribution.setText(getString(R.string.legende)); mContribution.setTypeface(null, Typeface.BOLD); imgContribution.setColorFilter(ContextCompat.getColor(getContext(), R.color.red_end_call));break;
            }

            mContribution.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), PayementActivity.class));
                }
            });

            imgContribution.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), PayementActivity.class));
                }
            });

            mLocalisation.setText(user.localisation);
            mSpecialite.setText(user.apropos);

            Date dateLastActivity = DateUtils.strToDate(user.dateLastActivity);
            Date dateCreation = DateUtils.strToDate(user.dateCreation);

            mDateLastActivity.setText(DateUtils.getRelativeTime(dateLastActivity));
            mDateCretion.setText(DateUtils.getRelativeTime(dateCreation));

            mFollowing.setText(user.abonnes + " " + getString(R.string.abonnés));

            EventBus.getDefault().post(new UserEvent(user.isFollowing.equals("1")));

            // Cela peut causer des bugs
            try {
                mLikes.setText(user.likes + " " + getString(R.string.points));
            } catch ( IllegalStateException e) {

            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ListFollowerActivity.class)
                        .putExtra("id_membre", idMembre)
                        .putExtra("title", getString(R.string.followers))
                        .putExtra("is_follower", true)
                );
            }
        });

    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerAboutComponent.builder()
                .appComponent(appComponent)
                .aboutModule(new AboutModule(this))
                .build()
                .inject(this);
    }

    //@Override
   // protected List<Object> getModules() {
    //    return Arrays.<Object>asList(new AboutModule(this));

}
