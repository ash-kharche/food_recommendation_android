package com.done.recommendation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.done.recommendation.Constants;
import com.done.recommendation.R;
import com.done.recommendation.database.DatabaseHelper;
import com.done.recommendation.database.DbOperations;
import com.done.recommendation.network.ApiClient;
import com.done.recommendation.network.ServerApi;
import com.done.recommendation.network.response.User;
import com.done.recommendation.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Questions extends AppCompatActivity {
    @BindView(R.id.rb_veg)
    RadioButton rbVeg;
    @BindView(R.id.rb_nonveg)
    RadioButton rbNoVeg;

    @BindView(R.id.cb_diabetes)
    CheckBox cbDiabetes;
    @BindView(R.id.cb_cholestrol)
    CheckBox cbCholestrol;
    @BindView(R.id.rb_kid)
    RadioButton rbKid;
    @BindView(R.id.rb_senior)
    RadioButton rbSenior;
    @BindView(R.id.rb_none)
    RadioButton rbNone;


    @OnClick(R.id.bt_submit)
    public void onSubmitClicked(View v) {
        getUserData();
        webservice_submitAnswers();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ButterKnife.bind(this);
        setUserData();
    }

    private void getUserData() {
        //VEG OR NON VEG
        if (rbVeg.isChecked() && !rbNoVeg.isChecked()) {
            DbOperations.insertOrUpdateKeys(Activity_Questions.this, DatabaseHelper.Keys.IS_VEG, "1");

        } else if (!rbVeg.isChecked() && rbNoVeg.isChecked()) {
            DbOperations.insertOrUpdateKeys(Activity_Questions.this, DatabaseHelper.Keys.IS_VEG, "0");
        }

        //DIABETES
        if (cbDiabetes.isChecked()) {
            DbOperations.insertOrUpdateKeys(Activity_Questions.this, DatabaseHelper.Keys.IS_DIABETES, "1");

        } else {
            DbOperations.insertOrUpdateKeys(Activity_Questions.this, DatabaseHelper.Keys.IS_DIABETES, "0");
        }

        //CHOLESTROL
        if (cbCholestrol.isChecked()) {
            DbOperations.insertOrUpdateKeys(Activity_Questions.this, DatabaseHelper.Keys.IS_CHOLESTROL, "1");
        } else {
            DbOperations.insertOrUpdateKeys(Activity_Questions.this, DatabaseHelper.Keys.IS_CHOLESTROL, "0");
        }

        //KID
        if (rbKid.isChecked()) {
            DbOperations.insertOrUpdateKeys(Activity_Questions.this, DatabaseHelper.Keys.IS_KID, "1");
        } else {
            DbOperations.insertOrUpdateKeys(Activity_Questions.this, DatabaseHelper.Keys.IS_KID, "0");
        }

        //SENIOR
        if (rbSenior.isChecked()) {
            DbOperations.insertOrUpdateKeys(Activity_Questions.this, DatabaseHelper.Keys.IS_SENIOR, "1");
        } else {
            DbOperations.insertOrUpdateKeys(Activity_Questions.this, DatabaseHelper.Keys.IS_SENIOR, "0");
        }
    }

    private void setUserData() {
        int isQuestionsDone = DbOperations.getKeyInt(Activity_Questions.this, DatabaseHelper.Keys.QUESTIONS_DONE);
        if (isQuestionsDone == 1) {
            //VEG OR NON VEG
            int isVeg = DbOperations.getKeyInt(Activity_Questions.this, DatabaseHelper.Keys.IS_VEG);
            if (isVeg == 1) {
                rbVeg.setChecked(true);
                rbNoVeg.setChecked(false);
            } else {
                rbVeg.setChecked(false);
                rbNoVeg.setChecked(true);
            }

            //DIABETES
            int isDiabetes = DbOperations.getKeyInt(Activity_Questions.this, DatabaseHelper.Keys.IS_DIABETES);
            if (isDiabetes == 1) {
                cbDiabetes.setChecked(true);
            } else {
                cbDiabetes.setChecked(false);
            }

            //CHOLESTROL
            int isCholestrol = DbOperations.getKeyInt(Activity_Questions.this, DatabaseHelper.Keys.IS_CHOLESTROL);
            if (isCholestrol == 1) {
                cbCholestrol.setChecked(true);
            } else {
                cbCholestrol.setChecked(false);
            }

            //KID
            int isKid = DbOperations.getKeyInt(Activity_Questions.this, DatabaseHelper.Keys.IS_KID);
            if (isKid == 1) {
                rbKid.setChecked(true);
            } else {
                rbKid.setChecked(false);
            }

            //SENIOR
            int isSenior = DbOperations.getKeyInt(Activity_Questions.this, DatabaseHelper.Keys.IS_SENIOR);
            if (isSenior == 1) {
                rbSenior.setChecked(true);
            } else {
                rbSenior.setChecked(false);
            }
        }
    }

    private void webservice_submitAnswers() {
        Util.showProgressDialog(Activity_Questions.this, false);
        try {
            User user = new User();
            user.setUserId(DbOperations.getKeyInt(Activity_Questions.this, DatabaseHelper.Keys.USER_ID));

            if (rbVeg.isChecked()) {
                user.setIsVeg(1);
            } else if (rbNoVeg.isChecked()) {
                user.setIsVeg(0);
            }
            if (cbDiabetes.isChecked()) {
                user.setIsDiabetes(1);
            } else {
                user.setIsDiabetes(0);
            }
            if (cbCholestrol.isChecked()) {
                user.setIsCholestrol(1);
            } else {
                user.setIsCholestrol(0);
            }
            if (rbKid.isChecked()) {
                user.setIsKid(1);
            } else {
                user.setIsKid(0);
            }
            if (rbSenior.isChecked()) {
                user.setIsSenior(1);
            } else {
                user.setIsSenior(0);
            }
            user.setIsQuestionDone(1);

            ServerApi serverApi = ApiClient.getApiClient();
            Call<User> call = serverApi.submitAnswers(user);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        Util.hideProgressDialog();

                        DbOperations.insertOrUpdateKeys(Activity_Questions.this, DatabaseHelper.Keys.QUESTIONS_DONE, "1");

                        Intent i = new Intent(Activity_Questions.this, Activity_Main.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                        finish();

                        DbOperations.clearCart(Activity_Questions.this);
                        DbOperations.deleteData(Activity_Questions.this);

                    } else {
                        Log.d(Constants.TAG, "onResponse error:  ");
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e(Constants.TAG, "onFailure  " + t);
                    Util.hideProgressDialog();
                }
            });


        } catch (Exception e) {
            Log.e(Constants.TAG, "webservice_submitAnswers", e);
            Util.hideProgressDialog();
        }
    }
}
