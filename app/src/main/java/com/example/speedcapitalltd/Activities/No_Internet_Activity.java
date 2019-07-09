package com.example.speedcapitalltd.Activities;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.speedcapitalltd.Configurations.ApiConstants;
import com.example.speedcapitalltd.R;

import java.net.InetAddress;

public class No_Internet_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no__internet_);
        getSupportActionBar().hide();
        Button btnRetry = findViewById(R.id.btnRetry);


        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInternetAvailable())
                {
                    startActivity(new Intent(getApplicationContext(),SplashActivity.class));
                }
            }
        });

    }

    /**
     * method checks if there is internet connectivity
     * @return  boolean
     */
    private boolean isInternetAvailable() {
        boolean internetAvailable=false;
        try {
            InetAddress ipAddr = InetAddress.getByName(ApiConstants.BASE_URL);
            //You can replace it with your name
            //noinspection EqualsBetweenInconvertibleTypes
            internetAvailable= ipAddr.toString().length()!=0;

        } catch (Exception e) {
            internetAvailable = false;
        }


        return internetAvailable;

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {

        finishAffinity();
    }
    }

