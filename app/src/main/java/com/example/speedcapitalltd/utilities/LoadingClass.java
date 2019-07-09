package com.example.speedcapitalltd.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.Toast;

import com.example.speedcapitalltd.R;

/**
 * The type Loading class.
 * Shows Loading dialog
 */
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
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.setCancelable(true);
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
        loadingDialog.dismiss();
    }
    public  void hideLoading(String message)
    {
        loadingDialog.dismiss();
        Toast.makeText(loadingDialog.getContext(),message,Toast.LENGTH_SHORT).show();
    }
}
