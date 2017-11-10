package com.squalala.dzbac.ui.tags_selection;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.greenfrvr.hashtagview.HashtagView;
import com.squalala.dzbac.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Back Packer
 * Date : 01/03/16
 */
public class TagSelectionActivity extends AppCompatActivity
        implements HashtagView.TagsSelectListener {

    @InjectView(R.id.hashtags5)
    HashtagView hashtagView;

    @InjectView(R.id.main_layout)
    View mLayout;

    @InjectView(R.id.btn_validate_tags)
    BootstrapButton btnValidateTags;

    private List<String> filiereTags;

    private String [] filiereTagSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_tag);

        ButterKnife.inject(this);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(getString(R.string.filiere));

        filiereTags = Arrays.asList(getResources().getStringArray(R.array.filiere_entries));
      //  filiereTagSelected = getIntent().getStringArrayExtra("tags_selected");

        // On supprime les tags sélectionne
      //  filiereTags.removeAll(Arrays.asList(filiereTagSelected));

       // hashtagView.setTransformer();

        hashtagView.setData(filiereTags);
        hashtagView.addOnTagSelectListener(this);

        btnValidateTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hashtagView.getSelectedItems().size() > 0) {
                    Intent intent = new Intent();
                    intent.putExtra("tags", (ArrayList) hashtagView.getSelectedItems());
                    setResult(RESULT_OK, intent);
                }

                finish();

            }
        });
    }

    @Override
    public void onItemSelected(Object item, boolean isSelected) {
        Snackbar.make(mLayout, String.format(getString(R.string.filiere_selectionne),
                Arrays.toString(hashtagView.getSelectedItems().toArray())), Snackbar.LENGTH_SHORT).show();

        if (hashtagView.getSelectedItems().size() > 0)
            btnValidateTags.setVisibility(View.VISIBLE);
        else
            btnValidateTags.setVisibility(View.GONE);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {

            case android.R.id.home:

                finish();

                return true;
        }
        return super.onOptionsItemSelected(item);

    }

}
