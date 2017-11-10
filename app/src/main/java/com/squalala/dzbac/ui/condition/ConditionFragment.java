package com.squalala.dzbac.ui.condition;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.squalala.dzbac.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Back Packer
 * Date : 03/04/15
 */
public class ConditionFragment extends Fragment {

    @InjectView(R.id.webView)
    WebView webView;


    private final static String KEY_URL = "url";

    public static ConditionFragment newInstance(String url) {

        ConditionFragment fragment = new ConditionFragment();

        Bundle args = new Bundle();

        args.putString(KEY_URL, url);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Answers.getInstance().logCustom(new CustomEvent("Menu Drawer")
                .putCustomAttribute("Click", "Réglement"));

        View root = null;

        root = inflater.inflate(R.layout.fragment_condition, container, false);

        ButterKnife.inject(this, root);

        webView.loadUrl("http://squalala.xyz/reglement.php");

        return root;
    }


}
