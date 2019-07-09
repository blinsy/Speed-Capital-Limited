package com.example.speedcapitalltd.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.speedcapitalltd.Adapters.SelectClientAdapter;
import com.example.speedcapitalltd.Configurations.ApiConstants;
import com.example.speedcapitalltd.R;

public class DashboardActivity extends AppCompatActivity {

    private EditText edtLPo;
    private Button  btnsubmit;
    private  EditText edtbranch;
    private  TextView txtClient;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(ApiConstants.toolbar_header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        String client = intent.getStringExtra("clientName");
        int image = intent.getIntExtra("logo",0);


        txtClient = findViewById(R.id.passclientname);
        imageView=findViewById(R.id.passimage);

        txtClient.setText(client);


        btnsubmit= findViewById(R.id.btnsubmit);

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtLPo= findViewById(R.id.edtLOP);
                edtbranch = findViewById(R.id.edtbranch);

                String LOP = edtLPo.getText().toString();
                String branch= edtbranch.getText().toString();
                String name= txtClient.getText().toString();
                int image = SelectClientAdapter.image;

                Intent intent = new Intent(DashboardActivity.this,My_Invoice_Activity.class);
                intent.putExtra("number",LOP);
                intent.putExtra("branch",branch);
                intent.putExtra("name",name);

                intent.putExtra("logo",image);

                startActivity(intent);
            }
        });
    }

}
