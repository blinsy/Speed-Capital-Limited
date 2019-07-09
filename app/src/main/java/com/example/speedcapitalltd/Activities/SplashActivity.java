package com.example.speedcapitalltd.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.speedcapitalltd.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Handler handler = new Handler();

        int delay = 2000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPreferences= getSharedPreferences(getString(R.string.string_user_id_identifier),MODE_PRIVATE);
                //String phone_number = sharedPreferences.getString(getString(R.string.phone_number_identifier),null);

                Intent intent = new Intent(SplashActivity.this, Walk_throughActivity.class);

                startActivity(intent);
                finish();

//                if (phone_number!=null){
//                    intent = new Intent(SplashActivity.this,SignInPasswordActivity.class);
//                }

            }
        },delay);

    }

}
