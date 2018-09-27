package com.example.android.foodreco.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.android.foodreco.R;
import com.example.android.foodreco.Utils;
import com.example.android.foodreco.network.ApiClient;
import com.example.android.foodreco.network.ServerApi;
import com.example.android.foodreco.network.request.SignUpUserBody;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    @BindView(R.id.tv_name)
    AutoCompleteTextView mNameView;

    @BindView(R.id.tv_email)
    AutoCompleteTextView mEmailView;

    @BindView(R.id.tv_mobile_number)
    AutoCompleteTextView mMobileNumberView;

    @BindView(R.id.tv_password)
    AutoCompleteTextView mPasswordView;

    @BindView(R.id.bt_sign_up)
    Button mBtSignUp;

    @OnClick(R.id.bt_sign_up)
    public void onSignUpClicked(View v) {
        attemptLogin();
    }

    private void attemptLogin() {
        if(validFields()) {
            saveUserApi();
        }
    }

    private boolean validFields() {
        boolean isValid = true;
        if(mNameView.getText() == null || mNameView.length() <= 0) {
            isValid = false;
            mNameView.setError(getString(R.string.signup_error_name));
        }

        if(mEmailView.getText() == null || !isEmailValid(mEmailView.getText().toString())) {
            isValid = false;
            mEmailView.setError(getString(R.string.signup_error_email));
        }

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
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isMobileValid(String mobile) {
        return mobile.length() > 0 && mobile.length() < 11;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private void saveUserApi() {
        Utils.showProgressDialog(SignUpActivity.this, false);

        SignUpUserBody body = new SignUpUserBody();
        body.setUserName(mNameView.getText().toString().trim());
        body.setEmail(mEmailView.getText().toString().trim());
        body.setMobileNumber(mMobileNumberView.getText().toString().trim());
        body.setPassword(mPasswordView.getText().toString().trim());

        ServerApi serverApi = ApiClient.getApiClient();
        Call<Void> call = serverApi.signUpUser(body);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    Log.d("poo","onResponse success:  " + response.toString());
                    Utils.hideProgressDialog();

                } else {
                    Log.d("poo","onResponse error:  ");
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

