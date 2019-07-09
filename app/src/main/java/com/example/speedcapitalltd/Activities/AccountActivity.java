package com.example.speedcapitalltd.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.speedcapitalltd.Configurations.ApiConstants;
import com.example.speedcapitalltd.R;
import com.example.speedcapitalltd.utilities.EditAccountApiService;
import com.example.speedcapitalltd.utilities.GetMyAccountApiService;
import com.example.speedcapitalltd.utilities.ShowNoNetworkError;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class AccountActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 3000;
    private static final int REQUEST_IMAGES = 1000;
    private final String email="email";

    private TextView tvProfileName;
    private TextView tvEmail;
    private TextView tvPhoneNumber;
    /**
     * The  profile pic.
     */

    private CircularImageView ivProfilePic;
    /**
     * The New image where encoded image will be stored.
     */
    private String newImage = null;
    private String supplier_id;
    private final String data="data";
    private final String avatar="avatar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        getSupportActionBar().hide();

        tvEmail = findViewById(R.id.tvEmail);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        ImageView ivEditEmail = findViewById(R.id.ivEditEmail);

        ivProfilePic = findViewById(R.id.ivProfilePic);


        ImageView ivEdit = findViewById(R.id.ivEdit);
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermisions()) {
                    getProfilePic();
                    // testToast("Button Clicked!, Permission granted");

                } else {
                    requestPermision();
                    //testToast("Button Clicked!, Permission not granted");
                }
            }
        });
        ivEditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditEmailDialog();
            }
        });
        getAccount();
    }

    public void pressback(View view) {
        Intent intent= new Intent(AccountActivity.this,ProfileActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AccountActivity.this,ProfileActivity.class));
        finish();

    }
    /**
     * Gets account account details from the server.
     */
    private void getAccount() {
        final LoadingClass loadingClass = new LoadingClass(AccountActivity.this);
        loadingClass.showLoading();
        String accessToken = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_access_token_identifier), null);
        RequestParams params = new RequestParams();
        params.put("id", getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_user_id_identifier), null));
        GetMyAccountApiService.getAccount(accessToken, "supplier/supplier/view-one", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                //Log.d("ACCOUNT_RES", response.toString());
                // Toast.makeText(AccountActivity.this,response.toString(),Toast.LENGTH_LONG).show();


                processGetAccountResponse(response,loadingClass);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                new ShowNoNetworkError(AccountActivity.this);
                if (loadingClass.isDialogShowing()) {
                    loadingClass.hideLoading();
                }

            }
        });
    }
    /**
     * Called when a response is recieved from callback to process data
     */
    private void processGetAccountResponse(JSONObject response, LoadingClass loadingClass){
        try {
            tvEmail.setText(response.getJSONObject(data).getString(email));
            tvProfileName.setText(response.getJSONObject(data).getString("name"));
            supplier_id = response.getJSONObject(data).getString("id");
            tvPhoneNumber.setText(response.getJSONObject(data).getString("phone_number"));
            String profile_pic = response.getJSONObject(data).getString(avatar);

            if (profile_pic != null&&profile_pic.length()!=0&&!profile_pic.equals("null")) {
                Glide.with(AccountActivity.this)
                        .asBitmap()
                        .load(profile_pic)
                        .into(ivProfilePic);
            }
            else
            {
                Glide.with(AccountActivity.this)
                        .asDrawable()
                        .load(R.drawable.ic_account)
                        .into(ivProfilePic);
            }
            if (loadingClass.isDialogShowing()) {
                loadingClass.hideLoading();
            }

        } catch (Exception ex) {
            if (loadingClass.isDialogShowing()) {
                loadingClass.hideLoading();
            }

        }
    }
    /**
     * Show edit email dialog.
     */
    private void showEditEmailDialog() {
        final Dialog dialog = new Dialog(AccountActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_email_dialog);
        dialog.show();

        TextView btnSave = dialog.findViewById(R.id.btnSave);
        TextView btnCancel = dialog.findViewById(R.id.btnCancel);

        final EditText edtNewEmail = dialog.findViewById(R.id.edtNewEmail);
        final TextInputLayout tlNewEmail = dialog.findViewById(R.id.tlNewEmail);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });

        handleClick(btnSave,edtNewEmail,tlNewEmail,dialog);
    }
    /**
     * Handles button save click listener
     * @param btnSave
     */

    private void handleClick(TextView btnSave, final EditText edtNewEmail, final TextInputLayout tlNewEmail,final  Dialog dialog) {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Pattern patternMatcher = Pattern.compile(ApiConstants.REGEXP, Pattern.CASE_INSENSITIVE);
                Matcher matcher = patternMatcher.matcher(edtNewEmail.getText().toString().trim());
                if (edtNewEmail.getText().toString().trim().length() > 0 && matcher.matches()) {
                    final LoadingClass loadingClass = new LoadingClass(AccountActivity.this);
                    loadingClass.showLoading();
                    String accessToken = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_access_token_identifier), null);
                    RequestParams params = new RequestParams();
                    params.put(email, edtNewEmail.getText().toString().trim());
                    EditAccountApiService.editAccount(accessToken, "supplier/supplier/" + supplier_id, params, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);

                            loadData(response);
                            if (loadingClass.isDialogShowing())
                            {
                                loadingClass.hideLoading();
                            }
                            dialog.dismiss();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            new ShowNoNetworkError(AccountActivity.this);
                            if (loadingClass.isDialogShowing()) {
                                loadingClass.hideLoading();
                                dialog.dismiss();
                            }
                        }
                    });
                } else if (edtNewEmail.getText().toString().trim().length() == 0) {
                    tlNewEmail.setError("Provide new Email");
                    tlNewEmail.requestFocus();
                } else if (!matcher.matches()) {
                    tlNewEmail.setError("Provide valid Email");
                    tlNewEmail.requestFocus();
                }
            }
        });
    }
    /**
     * The method sets the saved data to the fields of the account activity
     * @param response JSONObject response from the server
     */
    private void loadData(JSONObject response)
    {
        try {
            tvEmail.setText(response.getJSONObject(data).getString(email));
            String profilePic = response.getJSONObject(data).getString(avatar);



            if (profilePic != null && !profilePic.equals("null")) {
                Glide.with(AccountActivity.this)
                        .asBitmap()
                        .load(profilePic)
                        .into(ivProfilePic);
            }


        } catch (Exception ex) {
            ex.printStackTrace();

            //throw exception

        }
    }
    /**
     * Check permissions boolean.
     *
     * @return the boolean
     */
    private boolean checkPermisions() {
        // Permission is not granted
        return ContextCompat.checkSelfPermission(AccountActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Request permission to access external memory i.e SDCARD or Internal Memory.
     */
    private void requestPermision() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(AccountActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            permisionDialog();

        } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(AccountActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);


            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    /**
     * Permission dialog is invoked in the case the user has denied permission to access memory and offers explanation
     * as to why permissions are needed..
     */
    private void permisionDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(AccountActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("Access Storage");
        dialog.setMessage("We need to help you set up your business profile picture. We will read it from your external memory");
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                ActivityCompat.requestPermissions(AccountActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);

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

    /**
     *
     * @param requestCode request code used to request particular permision(s)
     * @param permissions permisions requested
     * @param grantResults results obtained from the request processor
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getProfilePic();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                }

                else
                {
                    //requestPermision();
                    Toast.makeText(AccountActivity.this, "Permission to access external memory denied. We are unable to update profile picture", Toast.LENGTH_LONG).show();
                }
            }
            break;
            default:
                break;
        }

    }
    /**
     *
     * @param requestCode request code from the calling activity
     * @param resultCode result from the activity providing response
     * @param data returned data from the activity sending results
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGES) {
            InputStream imageStream=null;
            try {
                imageStream = this.getContentResolver().openInputStream(data.getData());
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                newImage = encodeTobase64(selectedImage);
                updateProfilePic();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
    }

    /**
     * Gets profile pic from memory.
     */
    private void getProfilePic() {
        //testToast("Picking profile pic!, picking");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGES);

    }
    /**
     * Encode tobase 64 string.
     *
     * @param image the image picked from memory
     * @return the string
     */
    private static String encodeTobase64(Bitmap image) {
        int quality=40;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    /**
     * Update profile pic on the server.
     */
    private void updateProfilePic() {
        //testToast("Updating profile!, Updating profile..");
        final LoadingClass loadingClass = new LoadingClass(AccountActivity.this);
        loadingClass.showLoading();
        String accessToken = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_access_token_identifier), null);
        RequestParams params = new RequestParams();
        params.put(avatar, newImage);
        // String id = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_user_id_identifier), null);
        //params.put("id", getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_user_id_identifier), null));
        EditAccountApiService.editAccount(accessToken, "supplier/supplier/" + supplier_id, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    tvEmail.setText(response.getJSONObject(data).getString(email));
                    String profilePic = response.getJSONObject(data).getString(avatar);

                    if (profilePic != null && !profilePic.equals("null")) {
                        Glide.with(AccountActivity.this)
                                .asBitmap()
                                .load(profilePic)
                                .into(ivProfilePic);
                    }

                    Toast.makeText(AccountActivity.this, "Profile picture updated", Toast.LENGTH_LONG).show();
                    if (loadingClass.isDialogShowing()) {

                        loadingClass.hideLoading();}

                } catch (Exception ex) {
                    if (loadingClass.isDialogShowing()) {
                        loadingClass.hideLoading();}

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                new ShowNoNetworkError(AccountActivity.this);


                if (loadingClass.isDialogShowing()) {
                    loadingClass.hideLoading();
                }

            }
        });
    }

}
