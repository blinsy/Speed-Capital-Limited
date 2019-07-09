package com.example.speedcapitalltd.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.speedcapitalltd.R;

public class Sign_Up_Id_Activity extends AppCompatActivity {

    private EditText edtPassport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up__id_);
        getSupportActionBar().hide();

        final String fullName=getIntent().getStringExtra("name");
        edtPassport  = findViewById(R.id.edtPassport);

        edtPassport.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                 boolean keyfound =false;
                if (actionId==EditorInfo.IME_ACTION_DONE && validateInput())
                {

                    Intent intent= new Intent(Sign_Up_Id_Activity.this,Signup_EmailActivity.class);

                    intent.putExtra("name",fullName);
                 intent.putExtra("id_number",edtPassport.getText().toString());
                    startActivity(intent);
                     keyfound=true;
                }
                return keyfound;
            }
        });

        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                Intent intent = new Intent(Sign_Up_Id_Activity.this, Walk_throughActivity.class);
//                startActivity(intent);
            }
        });


    }

    private boolean validateInput()
    {
        boolean validateinput = true;
        if (edtPassport.getText().toString().trim().length()==0)
        {
            Toast.makeText(getApplicationContext(),"Provide ID or Passport", Toast.LENGTH_LONG).show();
            edtPassport.requestFocus();
            validateinput= false;
        }

        return validateinput;
    }

}
