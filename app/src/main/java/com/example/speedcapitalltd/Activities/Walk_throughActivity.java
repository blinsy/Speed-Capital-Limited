package com.example.speedcapitalltd.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.speedcapitalltd.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

public class Walk_throughActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_through);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        CarouselView cvTuitotials = findViewById(R.id.carosel);

        int pagecount= 3;
        cvTuitotials.setPageCount(pagecount);

        cvTuitotials.setViewListener(new ViewListener() {
            @Override
            public View setViewForPosition(int position) {
                View view = null;
                if (position == 0) {
                    view = getLayoutInflater().inflate(R.layout.walk_through_1, null);

                    handleClickEvents(view);

                }
                if (position == 1) {
                    view = getLayoutInflater().inflate(R.layout.walk_through_2, null);

                    handleClickEvents(view);

                }

                if (position == 2) {
                    view = getLayoutInflater().inflate(R.layout.walk_through_3, null);

                    handleClickEvents(view);
                }
                return view;
            }

                });

    }

    public void handleClickEvents(View view){
        TextView tvLogin = view.findViewById(R.id.tvLogin);

        Button btnGetStarted = view.findViewById(R.id.btnGetStarted);

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(Walk_throughActivity.this, SignUpNameActivity.class);
                startActivity(intent);
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(Walk_throughActivity.this, LogIn_EmailActivity.class);
                startActivity(intent);

            }
        });
    }
}
