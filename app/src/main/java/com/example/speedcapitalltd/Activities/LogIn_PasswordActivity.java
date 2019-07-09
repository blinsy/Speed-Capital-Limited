package com.example.speedcapitalltd.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.speedcapitalltd.R;
import com.example.speedcapitalltd.models.MerchantClient;
import com.example.speedcapitalltd.utilities.GetLinkedMerchants;
import com.example.speedcapitalltd.utilities.LogInApiService;
import com.example.speedcapitalltd.utilities.ShowNoNetworkError;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class LogIn_PasswordActivity extends AppCompatActivity {

    private EditText edtPassword;
    private int modeFrom;
    private String emailSignIn;
    private final  String data="data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in__password);
        getSupportActionBar().hide();
        ImageView ivBack = findViewById(R.id.ivBack);
        modeFrom=getIntent().getIntExtra(getString(R.string.mode),0);
        edtPassword=findViewById(R.id.edtPassword);
        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);
        edtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE && validateInput()){

                    login();
            }
                return false;
            }
        });
        edtPassword.setText("123456789");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LogIn_PasswordActivity.this,LogIn_EmailActivity.class);
                startActivity(intent);
            }
        });
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogIn_PasswordActivity.this, SignUp_PhoneActivity.class);
                intent.putExtra("type", "reset");
                startActivity(intent);

            }
        });
    }

    private boolean validateInput() {
        boolean valid=true;
        if (edtPassword.getText().toString().trim().length() == 0) {
            edtPassword.setError("Provide Password");
            edtPassword.requestFocus();
            valid = false;
        }

        return valid;
    }
    /**
     * Method to open next activity
     * @param mode tells whether the user has linked merchants or not
     */
    private void openNewActivity(int mode) {
        Intent intent=null;
        if (mode == 0) {
            intent = new Intent(LogIn_PasswordActivity.this, MainActivity.class);
        } else {
            intent = new Intent(LogIn_PasswordActivity.this, AddClientActivity.class);
            intent.putExtra("mode", 1);
        }
        startActivity(intent);


    }
    /**
     * Method used to process reponse from the calling callback's onSuccess() method
     * @param response JSONObject response from the server
     * @param loadingClass class that shows progress dialog
     */

    private void processLogin(JSONObject response, LoadingClass loadingClass) {
        try {
            boolean success = response.getBoolean("success");

            if (success) {
                String accessToken = response.getJSONObject(data).getString("access_token");
                String id = response.getJSONObject(data).getString("id");
                SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).edit();

                if(modeFrom!=0)
                {
                    finish();
                    return;
                }
                if (emailSignIn != null) {
                    editor.putString(getString(R.string.email_identifier), emailSignIn);
                    editor.putString(getString(R.string.string_user_id_identifier), id);
                    editor.putString(getString(R.string.string_access_token_identifier), accessToken);
                    editor.apply();
                    getMerchants();

                } else {
                    editor.putString(getString(R.string.string_user_id_identifier), id);
                    editor.putString(getString(R.string.string_access_token_identifier), accessToken);
                    editor.apply();
                    getMerchants();

                }
                if (loadingClass.isDialogShowing()) {
                    loadingClass.hideLoading();
                }
            } else {
                String error = response.getJSONObject(data).getString("message");
                //Log.d("SERVER_RESPONSE", error);
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (loadingClass.isDialogShowing()) {
                loadingClass.hideLoading();
            }
        }
    }
    /**
     * Method that requests user login
     */
    private void login() {
        final LoadingClass loadingClass = new LoadingClass(this);
        loadingClass.showLoading();

        emailSignIn = getIntent().getStringExtra("email");
        RequestParams requestParams = new RequestParams();
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE);
        String phone_number = sharedPreferences.getString(getString(R.string.email_identifier), null);


        if (emailSignIn != null) {
            requestParams.add("username", emailSignIn);
        } else {
            requestParams.add("username", phone_number);
        }
        requestParams.add("username", emailSignIn);
        requestParams.add("password", edtPassword.getText().toString().trim());
        LogInApiService.post("auth/default/login", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                processLogin(response,loadingClass);
                /// LoginResponseData loginResponseData=new LoginResponseData();


            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                if (errorResponse != null) {
                    //Log.d("SERVER_RESPONSE", error);
                    try {
                        String error = errorResponse.getJSONObject(data).getString("message");
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                        if (loadingClass.isDialogShowing()) {
                            loadingClass.hideLoading();
                        }
                    } catch (Exception ex) {
                        if (loadingClass.isDialogShowing()) {
                            loadingClass.hideLoading();
                        }
                        //ex.printStackTrace();
                    }
                }
                //Log.d("Error", errorResponse.toString());
                if (errorResponse == null) {
                    new ShowNoNetworkError(LogIn_PasswordActivity.this);
                }
                //  Log.d("Error", errorResponse.toString());




            }

            //Log.d("SERVER_RESPONSE", error);


        });
    }
    /**
     * Gets merchants
     */

    private void getMerchants() {
        final LoadingClass loadingClass = new LoadingClass(LogIn_PasswordActivity.this);
        loadingClass.showLoading();
        String accessToken = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_access_token_identifier), null);
        RequestParams params = new RequestParams();
        params.put("user_id", getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_user_id_identifier), null));
        GetLinkedMerchants.getLinkedMerchants(accessToken, "merchant/suppliers/linked-merchants", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    JSONArray jsonArray = response.getJSONArray(data);
                    List<MerchantClient> merchantClients = new ArrayList<>();
                    int length=jsonArray.length();
                    for (int i = 0; i <length; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        MerchantClient merchantClient = new MerchantClient();
                        //merchantClient.setMerchantName(jsonObject.getString("company_name"));
                        merchantClient.setMerchantId(jsonObject.getInt("id"));
                        // merchantClient.setMerchantLogo(jsonObject.getString("company_logo"));
                        merchantClient.setChecked(false);
                        merchantClients.add(merchantClient);

                    }

                    if (merchantClients.size() == 0) {
                        openNewActivity(1);
                    } else {
                        openNewActivity(0);
                    }


                    //rcvAddClient.setAdapter(new AddClientAdapter(AddClientActivity.this,merchantClients,mode));


                } catch (Exception ex) {
                    ex.printStackTrace();
                }


                if (loadingClass.isDialogShowing()) {
                    loadingClass.hideLoading();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                new ShowNoNetworkError(getApplicationContext());

                Toast.makeText(LogIn_PasswordActivity.this, "Merchant: " + getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_user_id_identifier), null), Toast.LENGTH_LONG).show();
                if (loadingClass.isDialogShowing()) {
                    loadingClass.hideLoading();
                }

            }
        });
    }


}
