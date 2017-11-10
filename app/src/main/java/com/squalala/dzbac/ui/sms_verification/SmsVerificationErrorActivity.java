package com.squalala.dzbac.ui.sms_verification;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.squalala.dzbac.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Back Packer
 * Date : 06/01/16
 */
public class SmsVerificationErrorActivity extends AppCompatActivity {

    @InjectView(R.id.tool_bar)
    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_verification_error);

        ButterKnife.inject(this);

        setSupportActionBar(mToolbar);

    }
}
