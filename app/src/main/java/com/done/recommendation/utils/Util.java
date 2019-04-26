package com.done.recommendation.utils;

import android.app.Activity;
import android.util.Log;

import com.done.recommendation.Constants;
import com.done.recommendation.utils.widgets.LoadingDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {
    public static LoadingDialog progressDialog;

    public static void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            try {
                progressDialog.dismiss();
                progressDialog = null;
            } catch (Exception e) {
                progressDialog = null;
            }
        }
    }

    public static void showProgressDialog(Activity context, boolean isCancelable) {
        hideProgressDialog();
        if (context != null) {
            try {
                progressDialog = new LoadingDialog(context);
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.setCancelable(isCancelable);
                progressDialog.show();
            } catch (Exception e) {
            }
        }
    }

    public static String formatDate(String s) {
        String formattedDate = "";
        try {
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date date = dt.parse(s);
            SimpleDateFormat dt1 = new SimpleDateFormat("dd-MMM-YY hh:mm a");
            formattedDate = dt1.format(date);
        } catch (Exception e) {
            Log.e(Constants.TAG, "formatDate", e);
        }
        return formattedDate;
    }
}