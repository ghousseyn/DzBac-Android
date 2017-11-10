package com.squalala.dzbac.ui.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squalala.dzbac.R;
import com.squalala.dzbac.data.api.ApiResponse;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.prototypes.CardWithList;
import it.gmariotti.cardslib.library.prototypes.LinearListView;

/**
 * Created by Back Packer
 * Date : 03/03/15
 */
public class ProfileCard extends CardWithList {

    private ApiResponse.User user;


    public ProfileCard(Context context, ApiResponse.User user) {
        super(context);
        this.user = user;
    }

    @Override
    protected CardHeader initCardHeader() {



        return null;
    }

    @Override
    protected void initCard() {

    }

    public void init(String lol) {
        super.init();
    }

    @Override
    protected List<ListObject> initChildren() {

        List<ListObject> mObjects = new ArrayList<ListObject>();

        UserObject u1 = new UserObject(this);
        u1.about = getContext().getString(R.string.apropos);
        u1.aboutDetails = user.apropos;

        mObjects.add(u1);

        UserObject u2 = new UserObject(this);
        u2.about = getContext().getString(R.string.localisation);
        u2.aboutDetails = user.localisation;

        mObjects.add(u2);

        UserObject u3 = new UserObject(this);
        u3.about = getContext().getString(R.string.commentaires);
        u3.aboutDetails = user.commentaires;

        mObjects.add(u3);

        UserObject u4 = new UserObject(this);
        u4.about = getContext().getString(R.string.posts_publie);
        u4.aboutDetails = user.posts;

        mObjects.add(u4);

        return mObjects;
    }

    @Override
    public View setupChildView(int i, ListObject listObject, View view, ViewGroup viewGroup) {


        TextView about = (TextView) view.findViewById(R.id.txt_profile_a_propos);
        TextView aboutDetails = (TextView) view.findViewById(R.id.txt_profile_a_propos_details);

        UserObject userObject = (UserObject) listObject;

        about.setText(userObject.about);
        aboutDetails.setText(userObject.aboutDetails);


        return view;
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.card_profile_membre_inner_main;
    }





    public class UserObject extends DefaultListObject {

        public String about;
        public String aboutDetails;

        public UserObject(Card parentCard) {
            super(parentCard);
            init();
        }


        private void init() {
            setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(LinearListView parent, View view, int position, ListObject object) {
                    Toast.makeText(getContext(), "Click on " + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



}
