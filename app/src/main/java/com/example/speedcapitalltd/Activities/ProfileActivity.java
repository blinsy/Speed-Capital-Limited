package com.example.speedcapitalltd.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.speedcapitalltd.R;
import com.example.speedcapitalltd.utilities.GetCustomerCareContactsApiService;
import com.example.speedcapitalltd.utilities.GetMyAccountApiService;
import com.example.speedcapitalltd.utilities.ShowNoNetworkError;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_CALL = 200;
    private TextView tvEmail;
    private String email;
    private String phone;
    private String id;
    private  CircularImageView ivProfilePic;
    private final String data="data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();
        ImageView ivBack = findViewById(R.id.ivBack);
        RelativeLayout rlPaymentOptions = findViewById(R.id.rlPaymentOptions);
        RelativeLayout rlManageClient = findViewById(R.id.rlManageClient);
        TextView tvLogout = findViewById(R.id.tvLogOut);
        RelativeLayout rlCustomerCareService = findViewById(R.id.rlCustomerCareService);
        RelativeLayout rlPersonalInformation = findViewById(R.id.rlPersonalInformation);
        RelativeLayout rlLoginAndSecurity = findViewById(R.id.rlLoginAndSecurity);
       ivProfilePic = findViewById(R.id.ivProfilePic);
        id = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_user_id_identifier), null);

        tvEmail = findViewById(R.id.tvEmail);
        rlPersonalInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, AccountActivity.class));

            }
        });
        rlCustomerCareService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContactDialog();
            }
        });
        rlLoginAndSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, Verification_CompleteActivity.class);
               // intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).edit();
                editor.clear();
                editor.apply();
                startActivity(new Intent(ProfileActivity.this, SplashActivity.class));

            }
        });


        rlManageClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(ProfileActivity.this, ManageClientActivity.class);
               startActivity(intent);
            }
        });

        rlPaymentOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, PaymentOptionsActivity.class));

            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
               // Bungee.shrink(ProfileActivity.this);
            }
        });
                 getAccount();
    }
    /**
     * Method gets the customer care contacts from the server
     */

    private void getContactCustomerCareContacts() {
        final LoadingClass loadingClass = new LoadingClass(ProfileActivity.this);
        loadingClass.showLoading();
        String accessToken = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_access_token_identifier), null);
        RequestParams params = new RequestParams();
        //params.put("id",getSharedPreferences(getString(R.string.string_authentication_identifier),MODE_PRIVATE).getString(getString(R.string.string_user_id_identifier),null));
        GetCustomerCareContactsApiService.getContacts(accessToken, "v1/setting/customer-care-contacts", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    email = response.getJSONObject(data).getString("email");
                    phone = response.getJSONObject(data).getString("phone");
                    if (loadingClass.isDialogShowing())
                    {
                        loadingClass.hideLoading();
                    }
                } catch (Exception ex) {
                    if (loadingClass.isDialogShowing())
                    {
                        loadingClass.hideLoading();
                    }
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


    public void backpresssed(View view) {
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
    }
    /**
     * This method shows the contact us dialog and handles operations related to contact function
     */
    private void showContactDialog() {

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.contact_dialog);
        RelativeLayout rlCall = dialog.findViewById(R.id.rlCall);
        RelativeLayout rlText = dialog.findViewById(R.id.rlText);
        RelativeLayout rlEmail = dialog.findViewById(R.id.rlEmail);

        rlEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailSelectorIntent = new Intent(Intent.ACTION_SENDTO);
                emailSelectorIntent.setData(Uri.parse("mailto:"));

                final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                emailIntent.setSelector(emailSelectorIntent);
                if (emailIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(emailIntent);
                }
            }
        });
        rlText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phone));
                intent.putExtra("sms_body", "");
                startActivity(intent);
            }
        });


        rlCall.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                if (checkPermisions()) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + phone));
                    startActivity(intent);
                } else {
                    requestPermision();
                }
            }
        });
        dialog.show();

    }

    /**
     * The method requests for user account from the server
     */

    private void getAccount() {
        final LoadingClass loadingClass = new LoadingClass(ProfileActivity.this);
        loadingClass.showLoading();
        String accessToken = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_access_token_identifier), null);
        RequestParams params = new RequestParams();
        params.put("id", getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_user_id_identifier), null));
        GetMyAccountApiService.getAccount(accessToken, "supplier/supplier/view-one", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d("SUPPLIER_RES",response.toString());
                super.onSuccess(statusCode, headers, response);
                //Log.d("ACCOUNT_RES", response.toString());

                try {
                    tvEmail.setText(response.getJSONObject(data).getString("email"));
                    //id=response.getJSONObject(data).getString("id");
                   /// tvProfileName.setText(response.getJSONObject(data).getString("name"));
                    String profile_pic = response.getJSONObject(data).getString("avatar");


                    if (profile_pic != null&&profile_pic.length()!=0&&!profile_pic.equals("null")){
                        Glide.with(ProfileActivity.this)
                                .asBitmap()
                                .load(profile_pic)
                                .into(ivProfilePic);

                    }
                    else
                    {
                        Glide.with(ProfileActivity.this)
                                .asDrawable()
                                .load(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_account))
                                .into(ivProfilePic);

                    }
                    if (loadingClass.isDialogShowing()) {
                        loadingClass.hideLoading();
                    }

                } catch (Exception ex) {
                    if (loadingClass.isDialogShowing()) {
                        loadingClass.hideLoading();
                    }
                    Glide.with(ProfileActivity.this)
                            .asDrawable()
                            .load(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_account))
                            .into(ivProfilePic);
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

            @Override
            public void onFinish() {
                super.onFinish();
                getContactCustomerCareContacts();
            }
        });
    }
    /**
     * Method checks for permissions if they have been granted
     * @return boolean
     */
    private boolean checkPermisions() {
        // Permission is not granted
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CALL_PHONE);
    }

    /**
     * Method initiates dialog to request for permissions
     */

    private void requestPermision() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this,
                Manifest.permission.CALL_PHONE)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            permisionDialog();

        } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(ProfileActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_CALL);


            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    /**
     *  Method shows dialog as to why a particular permission they have denied the app from using is necessary
     */

    private void permisionDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(ProfileActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("Make Calls");
        dialog.setMessage("We need permission to help you contact customer care");
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                ActivityCompat.requestPermissions(ProfileActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
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

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_CALL: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + phone));
                    startActivity(intent);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    //requestPermision();
                    Toast.makeText(ProfileActivity.this, "Permission to make calls denied", Toast.LENGTH_LONG).show();
                }
            }
            break;
            default:
                break;
        }


    }
    @Override
    protected void onResume() {
        super.onResume();
        getAccount();

    }
}