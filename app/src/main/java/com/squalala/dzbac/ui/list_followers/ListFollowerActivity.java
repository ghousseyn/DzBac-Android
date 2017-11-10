package com.squalala.dzbac.ui.list_followers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import com.squalala.dzbac.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by faycal on 22/05/16.
 */
public class ListFollowerActivity extends AppCompatActivity {


    @InjectView(R.id.tool_bar)
    Toolbar mToolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_follower);


        ButterKnife.inject(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        boolean isFollowers = getIntent().getBooleanExtra("is_follower", false);
        String idMembre = getIntent().getStringExtra("id_membre");
        String title = getIntent().getStringExtra("title");
        setTitle(title);

        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ListFollowerFragment fragment = ListFollowerFragment.newInstance(isFollowers, idMembre);

        // Replace the contents of the container with the new fragment
        ft.replace(R.id.my_framelayout, fragment);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:

                finish();
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}
