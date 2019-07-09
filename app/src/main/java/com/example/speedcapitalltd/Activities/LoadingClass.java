package com.example.speedcapitalltd.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

public class LoadingClass {

    private Dialog loadingDialog;

    private final Activity activity;

    /**
     * Instantiates a new Loading class.
     *
     * @param activity the activity
     */
    public LoadingClass(Activity activity) {
        this.activity = activity;
        initializeLoader();
    }

    /**
     * initializes the loading class
     */

    private void initializeLoader()
    {
        loadingDialog=new Dialog(activity);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
     //   loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.setCancelable(false);
    }

    /**
     * Show loading.
     */
    public  void showLoading()
    {
        loadingDialog.show();
    }

    /**
     * Is dialog showing boolean.
     *
     * @return the boolean
     */
    public boolean isDialogShowing()
    {
        return null != loadingDialog;

    }

    /**
     * Hide loading.
     */
    public  void hideLoading()
    {
        loadingDialog.hide();
    }
}
