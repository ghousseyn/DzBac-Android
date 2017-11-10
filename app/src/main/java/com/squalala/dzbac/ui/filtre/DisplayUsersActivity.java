package com.squalala.dzbac.ui.filtre;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.squalala.dzbac.DjihtiConstant;
import com.squalala.dzbac.R;
import com.squalala.dzbac.adapters.UserAdapter;
import com.squalala.dzbac.common.OnUserClickListener;
import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.data.api.LoggingInterceptor;
import com.squalala.dzbac.data.api.UserService;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.ui.show_profile.ShowProfileActivity;
import com.squalala.dzbac.utils.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import hugo.weaving.DebugLog;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Fayçal KADDOURI on 13/10/16.
 */
public class DisplayUsersActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener, OnUserClickListener {

    private UserService service;

    public static final String KEY_LOCALISATION = "localisation";
    public static final String KEY_ABOUT = "about";
    public static final String KEY_TRIER_PAR = "trier_par";

    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;

    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout swipeLayout;

    @InjectView(R.id.tool_bar)
    Toolbar mToolbar;

    @InjectView(R.id.progressBar_user)
    CircularProgressBar progressBar;

    private List<ApiResponse.User> users = new ArrayList<>();

    private String data;
    private UserAdapter adapter;

    private MainPreferences preferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_user);
        ButterKnife.inject(this);

        swipeLayout.setOnRefreshListener(this);


        preferences = new MainPreferences(this);

        setSupportActionBar(mToolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        service = getRetrofit().create(UserService.class);

        ApiResponse.User user = new ApiResponse.User();
        user.apropos = getIntent().getStringExtra(KEY_ABOUT);
        user.localisation = getIntent().getStringExtra(KEY_LOCALISATION);
        user.dateLastActivity = getIntent().getStringExtra(KEY_TRIER_PAR);

        data = new Gson().toJson(user);
        System.out.println(data);

        adapter = new UserAdapter(users, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @DebugLog
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                getUsers(page);
            }
        });
        getUsers(1);
    }

    @Override
    public void onUserclick(String membreId) {

        Intent intent = new Intent(this, ShowProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id_membre", membreId);
        startActivity(intent);

     /*   if (preferences.getPremiumCode() == 4) {
            Intent intent = new Intent(this, ShowProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("id_membre", membreId);
            startActivity(intent);
        }
        else if (preferences.getLevelContribution() < 3) {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(this.getString(R.string.oups))
                    .setContentText(this.getString(R.string.alert_profil_consult_contrib_error))
                    .setCancelText(this.getString(R.string.ok))
                    .setConfirmText(this.getString(R.string.en_savoir_plus))
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            startActivity(new Intent(DisplayUsersActivity.this, PayementActivity.class));
                        }
                    })
                    .show();
        }
        else {
            Intent intent = new Intent(this, ShowProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("id_membre", membreId);
            startActivity(intent);
        }

        */

    }

    @Override
    public void onRefresh() {
        progressBar.setVisibility(View.VISIBLE);
        users.clear();
        adapter.notifyDataSetChanged();
        getUsers(1);
    }

    private void getUsers(int page) {


        service.getSearchedUsers(page, data).enqueue(new Callback<ApiResponse.Users>() {
            @DebugLog
            @Override
            public void onResponse(Call<ApiResponse.Users> call, Response<ApiResponse.Users> response) {

                if(response.body().rankUsers != null) {
                    System.out.println(response.body().rankUsers);
                    users.addAll(response.body().rankUsers);
                    adapter.notifyDataSetChanged();
                    System.out.println(adapter.getItemCount());

                    progressBar.setVisibility(View.GONE);
                    swipeLayout.setRefreshing(false);
                }

            }

            @Override
            public void onFailure(Call<ApiResponse.Users> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                finish();

                return true;

            default:
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    private Retrofit getRetrofit() {
        MainPreferences mainPreferences = new MainPreferences(this);
        LoggingInterceptor interceptor = new LoggingInterceptor(mainPreferences);
        HttpLoggingInterceptor interceptorHttp = new HttpLoggingInterceptor();
        interceptorHttp.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(6, TimeUnit.SECONDS); // connect timeout
        builder.readTimeout(60 * 2, TimeUnit.SECONDS);    // socket timeout
        builder.writeTimeout(60 * 5, TimeUnit.SECONDS);

        builder.interceptors().add(interceptor);

        builder.interceptors().add(interceptorHttp);
        OkHttpClient client = builder.build();

        return new Retrofit.Builder()
                .baseUrl(DjihtiConstant.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }
}
