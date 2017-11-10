package com.squalala.dzbac.ui.login;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LoginEvent;
import com.google.android.gms.analytics.Tracker;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.R;
import com.squalala.dzbac.common.DjihtiBaseActivity;
import com.squalala.dzbac.DjihtiConstant;
import com.squalala.dzbac.ui.main.MainActivity;
import com.squalala.dzbac.utils.GCMUtils;
import com.squalala.dzbac.utils.Sha1;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends DjihtiBaseActivity
        implements LoginView, View.OnClickListener {

    @Inject LoginPresenter presenter;

    @Inject
    Tracker tracker;

    @InjectView(R.id.editEmailLogin) BootstrapEditText editEmail;
    @InjectView(R.id.editMdpLogin) BootstrapEditText editMdp;
    @InjectView(R.id.progressLogin) ProgressBar progressBar;
    @InjectView(R.id.btn_login) Button btn_login;

    @InjectView(R.id.txt_recover_password)
    TextView txtPassword;

    @InjectView(R.id.tool_bar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tracker.setScreenName(getClass().getName());
        
        ButterKnife.inject(this);

        txtPassword.setPaintFlags(txtPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        setSupportActionBar(toolbar);

        editEmail.setText(GCMUtils.getEmail(this));

        btn_login.setOnClickListener(this);
        txtPassword.setOnClickListener(this);
    }


    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerLoginComponent.builder()
                .appComponent(appComponent)
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);
    }

    @Override public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override public void setEmailError() {
        editEmail.setError(getString(R.string.error_email_empty));
    }

    @Override
    public void setBannedError() {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(getString(R.string.alerte))
                .setContentText(getString(R.string.error_account_banned))
                .show();
    }

    @Override public void setPasswordError() {
        editMdp.setError(getString(R.string.error_password_empty));
    }

    @Override public void navigateToMainScreen() {

        Answers.getInstance().logLogin(new LoginEvent()
                .putMethod("Email")
                .putSuccess(true));

    	Intent intent = new Intent(this , MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
    }

    @Override public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_login:

                presenter.validateCredentials(editEmail.getText().toString().trim(),
                        Sha1.encrypt(editMdp.getText().toString().trim()));

                break;

            case R.id.txt_recover_password:

                String url = DjihtiConstant.PASSWORD_FORGOT;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

                break;
        }


    }

	@Override
	public void setLoginError() {
		Toast.makeText(getApplicationContext(), getString(R.string.error_login),
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void setEmailInvalidError() {
		editEmail.setError(getString(R.string.error_email_invalide));
	}
}
