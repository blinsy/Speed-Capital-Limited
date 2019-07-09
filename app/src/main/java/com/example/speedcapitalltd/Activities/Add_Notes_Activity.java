package com.example.speedcapitalltd.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.speedcapitalltd.R;

public class Add_Notes_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__notes_);

        getSupportActionBar().hide();
    }

    public void backpress(View view) {
        Intent intent = new Intent(Add_Notes_Activity.this,My_Invoice_Activity.class);
        startActivity(intent);

    }


}
