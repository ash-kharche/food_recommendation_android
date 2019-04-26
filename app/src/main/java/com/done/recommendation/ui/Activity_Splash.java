package com.done.recommendation.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.done.recommendation.Constants;
import com.done.recommendation.R;
import com.done.recommendation.database.DatabaseHelper;
import com.done.recommendation.database.DbOperations;

public class Activity_Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String isLogdedIn = DbOperations.getKeyValue(Activity_Splash.this, DatabaseHelper.Keys.USER_ID);
                String questionsDone = DbOperations.getKeyValue(Activity_Splash.this, DatabaseHelper.Keys.QUESTIONS_DONE);

                Log.d(Constants.TAG, "isLogdedIn:  " + isLogdedIn + "    questionsDone:  " + questionsDone);
                if (isLogdedIn == null) {
                    Intent i = new Intent(Activity_Splash.this, Activity_Login.class);
                    startActivity(i);
                    finish();

                } else if (questionsDone == null) {
                    Intent i = new Intent(Activity_Splash.this, Activity_Questions.class);
                    startActivity(i);
                    finish();

                } else {
                    Intent i = new Intent(Activity_Splash.this, Activity_Main.class);
                    startActivity(i);
                    finish();
                }

            }
        }, 2000);
    }
}