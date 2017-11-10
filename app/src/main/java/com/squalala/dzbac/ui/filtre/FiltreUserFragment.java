package com.squalala.dzbac.ui.filtre;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.SearchEvent;
import com.squalala.dzbac.DjihtiConstant;
import com.squalala.dzbac.R;
import com.squalala.dzbac.data.api.LoggingInterceptor;
import com.squalala.dzbac.data.prefs.MainPreferences;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Fayçal KADDOURI on 13/10/16.
 */
public class FiltreUserFragment extends Fragment {

    @InjectView(R.id.spinner_wilaya)
    Spinner spinWilaya;

    @InjectView(R.id.spinner_about)
    Spinner spinAbout;

    @InjectView(R.id.spinner_trier_par_user)
    Spinner spinTrierPar;

    @InjectView(R.id.btn_search_user)
    BootstrapButton btnSearchUser;


    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = null;

        root = inflater.inflate(R.layout.fragment_filtre_user, container, false);

        ButterKnife.inject(this, root);

        btnSearchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Answers.getInstance().logSearch(new SearchEvent()
                        .putQuery("Filtre de recherche User")
                        .putCustomAttribute(DisplayUsersActivity.KEY_TRIER_PAR, getTrierPar())
                        .putCustomAttribute(DisplayUsersActivity.KEY_LOCALISATION, getLocalisation())
                        .putCustomAttribute(DisplayUsersActivity.KEY_ABOUT, getAbout()));

                Intent intent = new Intent(getActivity(), DisplayUsersActivity.class);
                intent.putExtra(DisplayUsersActivity.KEY_TRIER_PAR, getTrierPar());
                intent.putExtra(DisplayUsersActivity.KEY_LOCALISATION, getLocalisation());
                intent.putExtra(DisplayUsersActivity.KEY_ABOUT, getAbout());
                startActivity(intent);
            }
        });

        return root;
    }

    public String getAbout() {
        if (spinAbout.getSelectedItemPosition() != 0)
            return spinAbout.getSelectedItem().toString();
        return null;
    }

    public String getTrierPar() {
      //  if (spinTrierPar.getSelectedItemPosition() != 0)
            return spinTrierPar.getSelectedItem().toString();
    //    return null;
    }

    public String getLocalisation() {
        if (spinWilaya.getSelectedItemPosition() != 0)
            return spinWilaya.getSelectedItem().toString();

        return null;
    }


}
