package com.squalala.dzbac.ui.payement;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.squalala.dzbac.R;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.ui.full_imageview.ViewPagerActivity;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by Back Packer
 * Date : 26/01/16
 */
public class PayementFragment extends Fragment {

    private ArrayList<String> urlImages;


    @InjectView(R.id.webView)
    WebView webView;

    private MainPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        preferences = new MainPreferences(getActivity());

        final String urlsSplited = "http://www.squalala.xyz/dz_bac/images/example.jpg,http://www.squalala.xyz/dz_bac/images/recu_ccp.jpg";

        Answers.getInstance().logCustom(new CustomEvent("Menu Drawer")
                .putCustomAttribute("Click", "Soutiens"));

        View root = null;

        root = inflater.inflate(R.layout.fragment_condition, container, false);

        ButterKnife.inject(this, root);

     //   if(preferences.isPrenium())
        webView.loadUrl("http://squalala.xyz/soutien.php");
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.addJavascriptInterface(new Object() {

            @JavascriptInterface           // For API 17+
            public void onClickImage(final String id) {

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        //Code that interact with UI
                        System.out.println(id);

                        try {
                            Intent intent = new Intent(getActivity(), ViewPagerActivity.class);
                            intent.putExtra("url_to_split", urlsSplited);
                            intent.putExtra("position", Integer.valueOf(id));
                            getActivity().startActivity(intent);
                            System.out.println(id);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }


        }, "Image");

        String message = null;

        switch (preferences.getPremiumCode()) {
            case 0:

                message = getString(R.string.premium_0);

                break;

            case 1:

                message = getString(R.string.premium_1);

                break;

            case 2:

                message = getString(R.string.premium_2);

                break;

            case 3:

                message = getString(R.string.premium_3);

                break;

            case 4:

                message = getString(R.string.premium_4);

                break;

            case 5:

                message = getString(R.string.premium_5);

                break;

            case 6:

                message = getString(R.string.premium_6);

                break;
        }

        Crouton.makeText(getActivity(), message, Style.INFO).show();


        return root;
    }


}
