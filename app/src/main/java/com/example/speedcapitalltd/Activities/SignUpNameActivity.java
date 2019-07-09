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

public class SignUpNameActivity extends AppCompatActivity {
    private EditText edtFullName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_name);
        getSupportActionBar().hide();

        edtFullName=findViewById(R.id.edtFullName);

        edtFullName.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                boolean keyFound=false;
                if(actionId== EditorInfo.IME_ACTION_DONE&&validateInput())
                {
                    Intent intent=new Intent(SignUpNameActivity.this,Sign_Up_Id_Activity.class);
                    intent.putExtra("name",edtFullName.getText().toString());
                    startActivity(intent);
                   // Bungee.slideLeft(SignUpFullnameActivity.this);
                    keyFound = true;
                }

                return keyFound;
            }
        });
        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //Bungee.shrink(SignUpFullnameActivity.this);
            }
        });
    }
    /**
     * validates full name
     * @return boolean
     */

    private boolean validateInput()
    {

        boolean validInput=true;
        if(edtFullName.getText().toString().trim().length()==0)
        {
            edtFullName.setError("Provide Full Name");
            edtFullName.requestFocus();
            validInput = false;
        }
        return validInput;
    }
}
