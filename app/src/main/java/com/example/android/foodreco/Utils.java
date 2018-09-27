package com.example.android.foodreco;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.android.foodreco.widgets.CustomProgressDialog;

public class Utils {

    public static CustomProgressDialog sProgressDialog;
    public final static String TAG = "Utils";

    public static void hideProgressDialog() {
        if (sProgressDialog != null && sProgressDialog.isShowing()) {
            try {
                sProgressDialog.dismiss();
                sProgressDialog = null;
            } catch (Exception e) {
                sProgressDialog = null;
            }
        }
    }

    public static void showProgressDialog(Activity context, boolean isCancelable) {
        hideProgressDialog();
        if (context != null) {
            try {
                sProgressDialog = new CustomProgressDialog(context);
                sProgressDialog.setCanceledOnTouchOutside(true);
                sProgressDialog.setCancelable(isCancelable);
                sProgressDialog.show();
            } catch (Exception e) {
            }
        }
    }

    public static boolean isInternetConnected(Context ctx) {
        if (ctx != null) {
            ConnectivityManager connectivityMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityMgr != null) {
                NetworkInfo networkInfo = connectivityMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }
}