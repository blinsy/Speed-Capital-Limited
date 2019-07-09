package com.example.speedcapitalltd.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.speedcapitalltd.R;

public class LogIn_EmailActivity extends AppCompatActivity {

    private EditText phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in__email);
        getSupportActionBar().hide();
        phone=findViewById(R.id.edtPhone);
        ImageView ivBack = findViewById(R.id.ivBack);
        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LogIn_EmailActivity.this,SignUp_PhoneActivity.class);
                intent.putExtra("type","reset");
                startActivity(intent);

            }
        });
        phone.setText("0737702144");

        phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId== EditorInfo.IME_ACTION_DONE && validateInput())
                {
                    openNewActivity();

                }

                return false;
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LogIn_EmailActivity.this,Verification_CompleteActivity.class);

                startActivity(intent);

                finish();
            }
        });
    }
    private  boolean validateInput() {
        boolean valid = true;
        if (phone.getText().toString().trim().length() == 0) {
            phone.setError("Provide Phone Number");
            phone.requestFocus();
            valid = false;
        }
return valid;
    }


    /**
     * Method used to navigate to next activity
     */
    private void openNewActivity()
    {
        Intent intent=new Intent(LogIn_EmailActivity.this,LogIn_PasswordActivity.class);
        intent.putExtra("phone",phone.getText().toString().trim());
        startActivity(intent);


    }

}

