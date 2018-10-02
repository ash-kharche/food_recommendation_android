package com.example.android.foodreco.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.foodreco.R;
import com.example.android.foodreco.Utils;
import com.example.android.foodreco.network.ApiClient;
import com.example.android.foodreco.network.ServerApi;
import com.example.android.foodreco.network.request.LoginBody;
import com.example.android.foodreco.network.request.SignUpUserBody;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.tv_mobile_number)
    AutoCompleteTextView mMobileNumberView;

    @BindView(R.id.tv_password)
    AutoCompleteTextView mPasswordView;

    @BindView(R.id.bt_sign_up)
    Button mBtSignUp;

    @OnClick(R.id.bt_login_in)
    public void onLoginClicked(View v) {
        attemptLogin();
    }

    @OnClick(R.id.bt_sign_up)
    public void onSignUpClicked(View v) {
        Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }

    private void attemptLogin() {
        if(validFields()) {
            loginApi();
        }
    }

    private boolean validFields() {
        boolean isValid = true;
        if(mMobileNumberView.getText() == null || !isMobileValid(mMobileNumberView.getText().toString())) {
            isValid = false;
            mMobileNumberView.setError(getString(R.string.signup_error_mobile));
        }

        if(mPasswordView.getText() == null || !isPasswordValid(mPasswordView.getText().toString())) {
            isValid = false;
            mPasswordView.setError(getString(R.string.signup_error_password));
        }
        return isValid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    private boolean isMobileValid(String mobile) {
        return mobile.length() == 10;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private void loginApi() {
        Utils.showProgressDialog(LoginActivity.this, false);

        LoginBody body = new LoginBody();
        body.setMobileNumber(mMobileNumberView.getText().toString().trim());
        body.setPassword(mPasswordView.getText().toString().trim());

        ServerApi serverApi = ApiClient.getApiClient();
        Call<Void> call = serverApi.login(body);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Utils.hideProgressDialog();
                if(response.isSuccessful()) {
                    Intent i = new Intent(LoginActivity.this, QuestionActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Please signup", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("poo","onFailure  " + t);
                Utils.hideProgressDialog();
            }
        });
    }

}

