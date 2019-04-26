package com.done.recommendation.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.done.recommendation.Constants;
import com.done.recommendation.R;
import com.done.recommendation.database.DatabaseHelper;
import com.done.recommendation.database.DbOperations;
import com.done.recommendation.network.ApiClient;
import com.done.recommendation.network.ServerApi;
import com.done.recommendation.network.request.SignUpUserBody;
import com.done.recommendation.network.response.User;
import com.done.recommendation.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_SignUp extends Activity {
    @BindView(R.id.tv_name)
    EditText tvCustomerName;
    @BindView(R.id.tv_email)
    EditText tvEmail;
    @BindView(R.id.tv_mobile)
    EditText tvMobileNo;
    @BindView(R.id.tv_password)
    EditText tvPassword;

    @OnClick(R.id.bt_save)
    public void saveProfile(View v) {
        webservice_signUpUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        webservice_getUser();
    }

    private void webservice_getUser() {
        int userId = DbOperations.getKeyInt(Activity_SignUp.this, DatabaseHelper.Keys.USER_ID);

        Log.e(Constants.TAG, "Profile: userId is " + userId);
        if (userId != 0) {
            Util.showProgressDialog(Activity_SignUp.this, false);
            try {
                ServerApi serverApi = ApiClient.getApiClient();
                Call<User> call = serverApi.getUser(userId);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Util.hideProgressDialog();
                        if (response.isSuccessful()) {
                            User user = response.body();
                            DbOperations.insertLoggedInUser(Activity_SignUp.this, user);

                            loadProfileUi(user);
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e(Constants.TAG, "onFailure  " + t);
                        Util.hideProgressDialog();
                    }
                });
            } catch (Exception e) {
                Log.e(Constants.TAG, "webservice_getUser", e);
                Util.hideProgressDialog();
                showSnackBar();
            }
        }
    }

    private void loadProfileUi(User user) {
        if (user != null) {
            tvCustomerName.setText(user.getName());
            tvEmail.setText(user.getEmail());
            tvMobileNo.setText(user.getMobile());
        }
    }

    private void webservice_signUpUser() {
        int userId = 0;
        try {
            userId = Integer.parseInt(DbOperations.getKeyValue(Activity_SignUp.this, DatabaseHelper.Keys.USER_ID));
        } catch (NumberFormatException e) {
            Log.e(Constants.TAG, "Profile: userId is null", e);
        }

        Log.e(Constants.TAG, "Profile: userId is " + userId);
        if (userId == 0) {
            //signUp User
            Util.showProgressDialog(Activity_SignUp.this, false);
            try {
                ServerApi serverApi = ApiClient.getApiClient();
                Call<User> call = serverApi.signUpUser(getSignUpBody());
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Util.hideProgressDialog();

                        User user = response.body();
                        DbOperations.insertLoggedInUser(Activity_SignUp.this, user);

                        if (response.isSuccessful()) {
                            showAlertDialog();
                            if (getCallingActivity() == null) {
                                Intent i = new Intent(Activity_SignUp.this, Activity_Main.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(i);
                                finish();
                            } else {
                                setResult(Activity.RESULT_OK);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e(Constants.TAG, "onFailure  " + t);
                        Util.hideProgressDialog();
                    }
                });
            } catch (Exception e) {
                Util.hideProgressDialog();
                showSnackBar();
            }
        }
    }

    private SignUpUserBody getSignUpBody() {
        SignUpUserBody body = new SignUpUserBody();
        body.setUserName(tvCustomerName.getText().toString());
        body.setMobileNumber(tvMobileNo.getText().toString());
        body.setPassword(tvPassword.getText().toString());
        body.setEmail(tvEmail.getText().toString());
        return body;
    }

    public void showSnackBar() {
        if (!isDestroyed() || !isFinishing()) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), null, Snackbar.LENGTH_SHORT);
            snackbar.setText(getString(R.string.error_no_response));
            snackbar.setAction(R.string.dismiss, view -> snackbar.dismiss());
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.snackbar_alert));
            snackbar.show();
        }
    }

    private void showAlertDialog() {
        if (!isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
            builder.setTitle(getResources().getString(R.string.profile_saved_title));
            builder.setMessage(getResources().getString(R.string.profile_saved_message));

            builder.setPositiveButton(getString(R.string.cool), (dialog, which) -> {
                dialog.dismiss();

            });
            AlertDialog dialog1 = builder.create();
            dialog1.show();

        }
    }

}
