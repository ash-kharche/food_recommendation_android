package com.example.android.foodreco.widgets;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.android.foodreco.R;

public class CustomProgressDialog extends Dialog {

    public CustomProgressDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_progress_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }
}

/*public class CustomProgressDialog extends ProgressDialog {
    private AnimationDrawable mAnimationDrawable;

    public CustomProgressDialog(Context context) {
        super(context);
    }

    public static CustomProgressDialog getInstance(Context context) {
        CustomProgressDialog dialog = new CustomProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_progress_dialog);
        ImageView mProgressDialogImageView = (ImageView) findViewById(R.id.custom_progress_dialog_image);
        if (mProgressDialogImageView != null) {
            mAnimationDrawable = (AnimationDrawable) mProgressDialogImageView.getBackground();
        }
    }

    @Override
    public void show() {
        super.show();
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mAnimationDrawable != null) {
                    mAnimationDrawable.start();
                }
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mAnimationDrawable != null) {
            mAnimationDrawable.stop();
        }
    }
}*/
