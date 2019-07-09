package com.example.speedcapitalltd.utilities;

import android.content.Context;
import android.content.Intent;

import com.example.speedcapitalltd.Activities.No_Internet_Activity;

public class ShowNoNetworkError {
    private final Context context;

    /**
     * Instantiates a new Show no network error.
     *
     * @param context the context
     */
    public ShowNoNetworkError(Context context) {
        this.context = context;
        showErrorActivity();
    }
    /**
     * method displays no internet activity
     */
    private void showErrorActivity()
    {
        context.startActivity(new Intent(context, No_Internet_Activity.class));
    }

}
