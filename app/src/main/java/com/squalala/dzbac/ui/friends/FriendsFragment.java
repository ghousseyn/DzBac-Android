package com.squalala.dzbac.ui.friends;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.digits.sdk.android.Contacts;
import com.digits.sdk.android.ContactsCallback;
import com.digits.sdk.android.Digits;
import com.squalala.dzbac.ActivityModule;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.R;
import com.squalala.dzbac.common.BaseFragment;
import com.squalala.dzbac.eventbus.events.events.ContactsEvent;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hugo.weaving.DebugLog;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by Back Packer
 * Date : 03/01/16
 */
public class FriendsFragment extends BaseFragment
    implements FriendsView {

    private CardArrayAdapter mCardArrayAdapter;

    @InjectView(R.id.progressBar_list_conversation)
    ProgressBar progress;

    @InjectView(R.id.no_contact)
    TextView noContact;

    @InjectView(R.id.myList)
    CardListView listView;

    @Inject
    FriendsPresenter presenter;

    @DebugLog
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        Answers.getInstance().logCustom(new CustomEvent("Menu Drawer")
                .putCustomAttribute("Click", "FriendsFound"));

        View root = inflater.inflate(R.layout.fragment_found_friends, container, false);

        ButterKnife.inject(this, root);

        Digits.getInstance().getContactsClient().startContactsUpload();

        return root;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @DebugLog
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayList<Card> cards = new ArrayList<>();
        mCardArrayAdapter = new CardArrayAdapter(getActivity() ,cards);

        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }


        initContacts();

    }


    private void initContacts() {


        Digits.getInstance().getContactsClient().lookupContactMatches(null, null,
                new ContactsCallback<Contacts>() {

                    @DebugLog
                    @Override
                    public void success(Result<Contacts> result) {
                        System.out.println(result.response.toString());
                        System.out.println(result.data.users);
                        if (result.data.users != null)
                            presenter.onLoadFriendDigits(result.data.users);
                        else
                            presenter.onLoadFriendDigits(null);
                    }

                    @DebugLog
                    @Override
                    public void failure(TwitterException exception) {
                        // Show error
                        exception.printStackTrace();

                    }
                });
    }

    @Subscribe
    @DebugLog
    public void onEvent(ContactsEvent event) {
        initContacts();
    }

    @DebugLog
    @Override
    public void displayListUsers(ArrayList<Card> cards) {

        progress.setVisibility(View.GONE);
        mCardArrayAdapter.clear();

        if (cards != null) {


            mCardArrayAdapter.addAll(cards);
            mCardArrayAdapter.notifyDataSetChanged();
        }

        System.out.println("is empty " + mCardArrayAdapter.isEmpty());

        if (mCardArrayAdapter.isEmpty())
            noContact.setVisibility(View.VISIBLE);
        else
            noContact.setVisibility(View.GONE);
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerFriendsComponent.builder()
                .appComponent(appComponent)
                .friendsModule(new FriendsModule(this))
                .activityModule(new ActivityModule(getActivity()))
                .build()
                .inject(this);
    }






}
