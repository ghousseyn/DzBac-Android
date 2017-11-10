package com.squalala.dzbac.ui.sms_verification;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.R;
import com.squalala.dzbac.common.DjihtiBaseActivity;
import com.squalala.dzbac.ui.splash.SplashActivity;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import hugo.weaving.DebugLog;

/**
 * Created by Back Packer
 * Date : 04/12/15
 */
public class SmsVerificationActivity extends DjihtiBaseActivity
    implements SmsVerificationView {

    private static final int CODE_REQ = 1111;

    @InjectView(R.id.progressBar_sms)
    CircularProgressBar circularProgressBar;

    @InjectView(R.id.txt_sms_verification)
    TextView txtSMS;

    @InjectView(R.id.tool_bar)
    Toolbar mToolbar;

    @InjectView(R.id.txt_sms_error)
    TextView txtSmsError;

    @InjectView(R.id.btn_sms_verification)
    Button btnSmsVerification;

    @Inject SmsVerificationPresenter presenter;


    @OnClick(R.id.btn_sms_verification)
    void checkPhoneNumber() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setProviders(Arrays.asList(
                                new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
                                // Other providers you want to support
                                // ...
                        ))
                        .build(),
                CODE_REQ);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_verification);

        ButterKnife.inject(this);

        txtSmsError.setPaintFlags(txtSmsError.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        txtSmsError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SmsVerificationActivity.this, SmsVerificationErrorActivity.class));
            }
        });

        setSupportActionBar(mToolbar);
        txtSMS.setText(Html.fromHtml(getString(R.string.sms_verification_1)));

        if (FirebaseAuth.getInstance().getCurrentUser() != null &&
                FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() != null) {
            String phoneNumber =  FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
            String userID =  FirebaseAuth.getInstance().getCurrentUser().getUid();
            presenter.onSuccess(phoneNumber, userID);
        }
    }

    @DebugLog
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE_REQ) {

            if (FirebaseAuth.getInstance().getCurrentUser() != null &&
                    FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() != null) {
                String phoneNumber =  FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                String userID =  FirebaseAuth.getInstance().getCurrentUser().getUid();
                presenter.onSuccess(phoneNumber, userID);
            }

        }
    }

    @Override
    public void showDialog(String message) {

        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            FirebaseAuth.getInstance().getCurrentUser().delete();

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setContentText(message)
                .setTitleText(this.getString(R.string.alert_titre_info))
                .setConfirmText(this.getString(R.string.ok))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        sweetAlertDialog.dismiss();
                    }
                })
                .show();
    }


    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerSmsVerificationComponent.builder()
                .appComponent(appComponent)
                .smsVerificationModule(new SmsVerificationModule(this))
                .build()
                .inject(this);
    }

  /*  @Override
    protected void onResume() {
        super.onResume();

        final DigitsSession session = Digits.getSessionManager().getActiveSession();
        if (session != null && session.isValidUser() && session.getPhoneNumber() != null && !isUpdateSended) {
            isUpdateSended = true;
            presenter.onSuccess(session.getPhoneNumber(), session.getId());
        }

    }
    */

    @Override
    public void navigateToSplash() {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void exit() {
        finish();
    }

    @Override
    public void showProgress() {
        circularProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        circularProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void hideButton() {
        btnSmsVerification.setVisibility(View.GONE);
    }

    @Override
    public void showButton() {
        btnSmsVerification.setVisibility(View.VISIBLE);
    }
}
