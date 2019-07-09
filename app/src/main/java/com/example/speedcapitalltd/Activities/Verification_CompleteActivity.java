package com.example.speedcapitalltd.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.speedcapitalltd.R;
import com.example.speedcapitalltd.utilities.CreatePasswordApiService;
import com.example.speedcapitalltd.utilities.ShowNoNetworkError;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Verification_CompleteActivity extends AppCompatActivity {
    private static final String TAG = "SETPASSWORD";
    private String userId = "";
    private TextInputEditText edtPassword;
    private TextInputEditText edtConfirmPassword;
    private Button btncontinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification__complete);
        getSupportActionBar().hide();
        btncontinue= findViewById(R.id.btnContinue);
        ImageView ivClose = findViewById(R.id.ivClose);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        userId = getIntent().getStringExtra("id");
        edtPassword = findViewById(R.id.edtpassword);


        btncontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidateInput()){

                    finishReset();
                }
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(Verification_CompleteActivity.this,Start_VerificationActivity.class);
                startActivity(intent);
            }
        });
    }
public  boolean ValidateInput(){
    boolean validInput=true;
    if (edtPassword.getText().toString().trim().length() == 0) {
        edtPassword.setError("Provide password");
        edtPassword.requestFocus();
        validInput = false;
    }
    else if (edtPassword.getText().toString().trim().length() < 6) {
        edtPassword.setError("Password length must be more than 6 ");
        edtPassword.requestFocus();
        validInput =  false;

    }

    else if (edtConfirmPassword.getText().toString().trim().length() == 0) {
        edtConfirmPassword.setError("Confirm password ");
        edtConfirmPassword.requestFocus();
        validInput = false;
    }
    else if (!edtConfirmPassword.getText().toString().trim().equals(edtPassword.getText().toString().trim())) {
        edtConfirmPassword.setError("The two passwords do not match ");
        edtConfirmPassword.requestFocus();
        validInput=  false;
    }
    return validInput;
}
    /**
     * Method used to open new activity
     */
    private void openNewActivity() {
        Intent intent = new Intent(Verification_CompleteActivity.this,LogIn_EmailActivity .class);
        startActivity(intent);
  finish();
    }
    /**
     * Performs password reset or password creation
     */

    private void finishReset() {
        final LoadingClass loadingClass = new LoadingClass(Verification_CompleteActivity.this);
        loadingClass.showLoading();
        RequestParams params = new RequestParams();
        params.put("password", edtPassword.getText().toString().trim());
        params.put("id", userId);
        CreatePasswordApiService.createPassword("auth/default/password-reset", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    Log.d(TAG,response.toString());
                    openNewActivity();
                }
                if (loadingClass.isDialogShowing()) {
                    loadingClass.hideLoading();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                new ShowNoNetworkError(getApplicationContext());

                if (loadingClass.isDialogShowing()) {
                    loadingClass.hideLoading();
                }
            }
        });
    }
}
