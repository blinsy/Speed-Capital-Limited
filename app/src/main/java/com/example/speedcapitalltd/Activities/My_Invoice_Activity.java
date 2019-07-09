package com.example.speedcapitalltd.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.speedcapitalltd.R;
import com.mikhaellopez.circularimageview.CircularImageView;

public class My_Invoice_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_invoice);
        getSupportActionBar().hide();


        Intent intent = getIntent();
        String message = intent.getStringExtra("number");
        String branch = intent.getStringExtra("branch");
        String client = intent.getStringExtra("name");
        int image = intent.getIntExtra("logo",0);

        TextView textView= findViewById(R.id.tvLpoNumber);
        TextView textView1 = findViewById(R.id.tvbranchName);
        TextView textView2 = findViewById(R.id.tvClientName);
        CircularImageView img = findViewById(R.id.ivClientLogo);

        textView1.setText(branch);
        textView.setText(message + " LPO ");
        textView2.setText(client);
        img.setImageResource(image);
    }

    public void notes(View view) {
        Intent intent = new Intent(My_Invoice_Activity.this,Add_Notes_Activity.class);

        startActivity(intent);
    }

    public void backpree(View view) {

        Intent intent = new Intent(My_Invoice_Activity.this,DashboardActivity.class);

        startActivity(intent);
    }
}
