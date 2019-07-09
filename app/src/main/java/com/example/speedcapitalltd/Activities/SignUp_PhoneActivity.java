package com.example.speedcapitalltd.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.speedcapitalltd.R;
import com.example.speedcapitalltd.utilities.RegisterApiService;
import com.example.speedcapitalltd.utilities.RequestResetApiService;
import com.example.speedcapitalltd.utilities.ShowNoNetworkError;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SignUp_PhoneActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_CALL = 300;
    private EditText edtPhoneNumber;
    private String type = null;
    private String idNumber;
    private String fullName;
    private String email;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up__phone);
        getSupportActionBar().hide();
        idNumber = getIntent().getStringExtra("id_number");
        email = getIntent().getStringExtra("email");
        type = getIntent().getStringExtra("type");
        fullName = getIntent().getStringExtra("name");
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);

        if (!checkPermisions()) {
            requestPermision();
        }
        edtPhoneNumber.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE && validateInput() ) {

               if (type != null){
                   requestRequestReset();
               }else register();
                }
                return false;
            }
        });


        ImageView ivBack = findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }
    private boolean validateInput() {

        boolean validInput=true;
        if (edtPhoneNumber.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(),"provide Phone Number",Toast.LENGTH_LONG).show();
            edtPhoneNumber.requestFocus();
            validInput = false;
        }
        return validInput;
    }

   private void register() {

      RequestParams requestParams = new RequestParams();
        requestParams.put("supplier_type", "individual");
       requestParams.put("name", fullName);
        requestParams.put("email", email);
        requestParams.put("phone_number", edtPhoneNumber.getText().toString());
        requestParams.put("id_number", idNumber);
        RegisterApiService.register("supplier/supplier/create-new", requestParams, new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (response.toString() != null) {
                            Log.d("REGISTER_RESPONSE", response.toString());
                            try {
                                id = response.getJSONObject("data").getString("id");
                                //openNewActivity();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }


                        } else {
                            Log.d("REGISTER_RESPONSE", "null response");
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        new ShowNoNetworkError(getApplicationContext());
                        if (errorResponse != null) {
                            Log.d("REGISTER_RESPONSE error", errorResponse.toString());}
                        else {
                            Log.d("REGISTER_RESPONSE error", throwable.toString()); }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        requestRequestReset();
                    }
                }


        );


  }
    /**
     * opens new activity
     */

    private void openNewActivity() {
        Intent intent = new Intent(SignUp_PhoneActivity.this, Start_VerificationActivity.class);
        //intent.putExtra(getString(R.string.supplier_id_identifier),supplier_id);
        intent.putExtra("phone_number", id);
        intent.putExtra("phone", edtPhoneNumber.getText().toString());
        intent.putExtra("type", type);
        startActivity(intent);
        finish();

    }
    /**
     * Enables user to request reset
     */

    private  void requestRequestReset(){
        final LoadingClass loadingClass = new LoadingClass(SignUp_PhoneActivity.this);
        loadingClass.showLoading();
        RequestParams requestParams = new RequestParams();
        requestParams.put("phone_number", edtPhoneNumber.getText().toString());

        RequestResetApiService.requestReset("auth/default/password-reset-request", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("RESET_RESPONSE success", response.toString());
                try {
                    id = response.getJSONObject("data").getString("id");
                    openNewActivity();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (loadingClass.isDialogShowing()) {
                    loadingClass.hideLoading();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                new ShowNoNetworkError(getApplicationContext());
                try {
                    if (errorResponse != null) {

                        if ("false".equals(errorResponse.getString("success"))) {
                            Toast.makeText(SignUp_PhoneActivity.this, "Phone number does not match our records", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }


                if (loadingClass.isDialogShowing()) {
                    loadingClass.hideLoading();}
            }
        });



    }
    /**
     * Requests for permissions
     */
    private void requestPermision() {
        // Permission is not granted

        if (ActivityCompat.shouldShowRequestPermissionRationale(SignUp_PhoneActivity.this,
                Manifest.permission.RECEIVE_SMS)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            permisionDialog();

        } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(SignUp_PhoneActivity.this,
                    new String[]{Manifest.permission.RECEIVE_SMS},
                    MY_PERMISSIONS_CALL);


            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }



    /**
     * Checks if permissions have been given
     * granted
     * @return boolean
     */
    private boolean checkPermisions() {
        // Permission is not granted
        return ContextCompat.checkSelfPermission(SignUp_PhoneActivity.this, Manifest.permission.RECEIVE_SMS)
                == PackageManager.PERMISSION_GRANTED;
    }



    /**
     * Displays dialog to explain to user why it is important to grant a certain permission
     */
    private void permisionDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(SignUp_PhoneActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("Read SMS");
        dialog.setMessage("We need permission to verify phone number");
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                ActivityCompat.requestPermissions(SignUp_PhoneActivity.this,
                        new String[]{Manifest.permission.RECEIVE_SMS},
                        MY_PERMISSIONS_CALL);

            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
