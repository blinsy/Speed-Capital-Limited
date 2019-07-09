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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup_EmailActivity extends AppCompatActivity {
private EditText edtEmail;

ImageView ivback;
private String idNumber;
    private String fullName;

    @SuppressWarnings("Annotator")
    final String regExpn =
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup__email);
   getSupportActionBar().hide();
        fullName=getIntent().getStringExtra("name");
   idNumber = getIntent().getStringExtra("id_number");
   edtEmail= findViewById(R.id.edtemail);

  edtEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
          boolean keyfound= false;
          if (actionId== EditorInfo.IME_ACTION_DONE && validateInput())
          {

              Intent intent= new Intent(Signup_EmailActivity.this,SignUp_PhoneActivity.class);
             intent.putExtra("id_number",idNumber);
              intent.putExtra("name",fullName);
             intent.putExtra("email",edtEmail.getText().toString());
              startActivity(intent);
              keyfound=true;
          }
          return keyfound;
      }
  });
   ivback= findViewById(R.id.ivBack);

   ivback.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {

           finish();
       }
   });
    }

    private boolean validateInput()
    {
        boolean validEmail=true;
        if(edtEmail.getText().toString().trim().length()==0)
        {
            Toast.makeText(getApplicationContext(),"provide Email Address",Toast.LENGTH_LONG).show();
            edtEmail.requestFocus();
            validEmail =  false;
        } else {
            Pattern pattern=Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
            Matcher matcher=pattern.matcher(edtEmail.getText().toString());
            if(!matcher.matches())
            {
                Toast.makeText(getApplicationContext(),"provide Valid Email Address",Toast.LENGTH_LONG).show();
                edtEmail.requestFocus();
                validEmail =  false;
            }
        }

        return validEmail;
    }


}
