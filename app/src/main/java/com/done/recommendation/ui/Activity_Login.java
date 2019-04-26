package com.done.recommendation.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.done.recommendation.Constants;
import com.done.recommendation.R;
import com.done.recommendation.database.DatabaseHelper;
import com.done.recommendation.database.DbOperations;
import com.done.recommendation.network.ApiClient;
import com.done.recommendation.network.ServerApi;
import com.done.recommendation.network.request.LoginBody;
import com.done.recommendation.network.response.User;
import com.done.recommendation.utils.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Login extends AppCompatActivity {
    @BindView(R.id.tv_email)
    EditText mEmailView;

    @BindView(R.id.tv_password)
    EditText mPasswordView;

    @BindView(R.id.bt_sign_up)
    Button mBtSignUp;

    @OnClick(R.id.bt_login_in)
    public void onLoginClicked(View v) {
        attemptLogin();
    }

    @OnClick(R.id.tv_skip)
    public void onSkipClicked(View v) {
        Intent i = new Intent(Activity_Login.this, Activity_Main.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.bt_sign_up)
    public void onSignUpClicked(View v) {
        Intent i = new Intent(Activity_Login.this, Activity_SignUp.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }

    private void attemptLogin() {
        if (validFields()) {
            webservice_login();
        }
    }

    private boolean validFields() {
        boolean isValid = true;
        if (mEmailView.getText() == null || !isEmailValid(mEmailView.getText().toString())) {
            isValid = false;
            mEmailView.setError(getString(R.string.signup_error_email));
        }

        if (mPasswordView.getText() == null || !isPasswordValid(mPasswordView.getText().toString())) {
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

    public static boolean isEmailValid(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
    }

    //Password must contain minimum 8 characters at least 1 Alphabet, 1 Number and 1 Special Character, for this I tried the code below, but it's not working.
    private boolean isPasswordValid(String password) {
        /*if(TextUtils.isEmpty(password)) {
            return false;
        } else {
            final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
            Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
            Matcher matcher = pattern.matcher(password);

            return matcher.matches();
        }*/
        return true;
    }

    private void webservice_login() {
        Util.showProgressDialog(Activity_Login.this, false);

        LoginBody body = new LoginBody();
        body.setEmail(mEmailView.getText().toString().trim());
        body.setPassword(mPasswordView.getText().toString().trim());

        ServerApi serverApi = ApiClient.getApiClient();
        Call<User> call = serverApi.login(body);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Util.hideProgressDialog();

                if (response.isSuccessful()) {
                    User user = response.body();
                    DbOperations.insertLoggedInUser(Activity_Login.this, user);

                    Log.d(Constants.TAG, "getCallingActivity  " + getCallingActivity());
                    if (getCallingActivity() != null && getCallingActivity().getClassName() == Activity_Orders.class.getName()) {
                        Log.d(Constants.TAG, "setResult Activity.RESULT_OK");
                        setResult(Activity.RESULT_OK);

                    } else {

                        int questionsDone = DbOperations.getKeyInt(Activity_Login.this, DatabaseHelper.Keys.QUESTIONS_DONE);
                        if (questionsDone == 1) {
                            Intent i = new Intent(Activity_Login.this, Activity_Main.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(i);
                            finish();

                        } else {
                            Intent i = new Intent(Activity_Login.this, Activity_Questions.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(i);
                            finish();
                        }
                    }
                } else if (response.errorBody() != null) {
                    try {
                        showSnackBar(response.errorBody().string());
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(Constants.TAG, "onFailure  " + t);
                Util.hideProgressDialog();
            }
        });
    }

    public void showSnackBar(String message) {
        if (!isDestroyed() || !isFinishing()) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), null, Snackbar.LENGTH_SHORT);
            if (TextUtils.isEmpty(message)) {
                snackbar.setText("Please SignUp!!!");
            } else {
                snackbar.setText(message);
            }
            snackbar.setAction(R.string.dismiss, view -> snackbar.dismiss());
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.snackbar_alert));
            snackbar.show();
        }
    }
}

