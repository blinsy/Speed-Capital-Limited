package com.example.speedcapitalltd.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.speedcapitalltd.R;
import com.example.speedcapitalltd.utilities.RequestResetApiService;
import com.example.speedcapitalltd.utilities.ShowNoNetworkError;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class Start_VerificationActivity extends AppCompatActivity implements OTPListener{

    private Dialog dialog;
    private String password = "";
    private String phoneNumber;

    private String phone;
    private ImageView ivFirst;
    private ImageView ivSecond;
    private ImageView ivThird;
    private ImageView ivFourth;
    private Button btnStart;

    private TextView tvOne;
    private TextView tvTwo;
    private TextView tvThree;
    private TextView tvFour;
    private TextView tvFive;
    private TextView tvSix;
    private TextView tvSeven;
    private TextView tvEight;
    private TextView tvNine;
    private TextView tvZero;
    private TextView tvCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start__verification);
getSupportActionBar().hide();

        ImageView ivClose = findViewById(R.id.ivClose);
        phoneNumber = getIntent().getStringExtra("phone_number");
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Start_VerificationActivity.this,SignUp_PhoneActivity.class);
                startActivity(intent);


            }
        });

        initUi();

        phone = getIntent().getStringExtra("phone");


    handleClick();



        //OtpReader.bind(this, "Eclectics");

    }

    /**
     * Method initializes the UI
     */
    private void initUi() {
        btnStart = findViewById(R.id.btnStart);
        tvOne = findViewById(R.id.tvOne);
        tvTwo = findViewById(R.id.tvTwo);
        tvThree = findViewById(R.id.tvThree);
        tvFour = findViewById(R.id.tvFour);
        tvFive = findViewById(R.id.tvFive);
        tvSix = findViewById(R.id.tvSix);
        tvSeven = findViewById(R.id.tvSeven);
        tvEight = findViewById(R.id.tvEight);
        tvNine = findViewById(R.id.tvNine);
        tvZero = findViewById(R.id.tvZero);
        tvCancel = findViewById(R.id.tvCancel);
        ivFirst = findViewById(R.id.ivFirst);
        ivSecond = findViewById(R.id.ivSecond);
        ivThird = findViewById(R.id.ivThird);
        ivFourth = findViewById(R.id.ivFourth);
    }
    /**
     * Handles method for line one of the ui click
     */

    private void lineOneClick() {
        tvOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.length() < 4) {
                    password += 1;
                }
                setProgress();
            }
        });
        tvTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.length() < 4) {
                    password += 2;
                }
                setProgress();
            }
        });
        tvThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.length() < 4) {
                    password += 3;
                }
                setProgress();
            }
        });

    }
    /**
     * Handles line two clicks
     */
    private void lineTwoClick() {
        tvFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.length() < 4) {
                    password += 4;
                }
                setProgress();
            }
        });
        tvFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.length() < 4) {
                    password += 5;
                }
                setProgress();
            }
        });
        tvSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.length() < 4) {
                    password += 6;
                }
                setProgress();
            }
        });
    }
    /**
     * Handles line three clicks
     */
    private void lineThreeClick() {
        tvSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.length() < 4) {
                    password += 7;
                }
                setProgress();
            }
        });

        tvEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.length() < 4) {
                    password += 8;
                }
                setProgress();
            }
        });
        tvNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.length() < 4) {
                    password += 9;
                }
                setProgress();
            }
        });
    }
    /**
     * handles click events
     */
    private void handleClick() {
        lineOneClick();
        lineTwoClick();
        lineThreeClick();

        tvZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.length() < 4) {
                    password += 0;
                }
                setProgress();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.length() > 0) {
                    password = password.substring(0, password.length() - 1);
                }
                setProgress();
            }
        });


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendVerificationCode();

            }
        });
    }
    /**
     * Shows progress of password input
     */
    private void setProgress() {
        switch (password.length()) {
            case 0:
                ivFourth.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.grey_circle));
                ivSecond.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.grey_circle));
                ivThird.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.grey_circle));
                ivFirst.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.grey_circle));
                break;
            case 1:
                ivFourth.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.grey_circle));
                ivSecond.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.grey_circle));
                ivThird.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.grey_circle));
                ivFirst.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.red_circle));
                break;
            case 2:
                ivFourth.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.grey_circle));
                ivSecond.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.red_circle));
                ivThird.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.grey_circle));
                ivFirst.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.red_circle));
                break;
            case 3:
                ivFourth.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.grey_circle));
                ivSecond.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.red_circle));
                ivThird.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.red_circle));
                ivFirst.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.red_circle));
                break;
            case 4:
                ivFourth.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.red_circle));
                ivSecond.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.red_circle));
                ivThird.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.red_circle));
                ivFirst.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.red_circle));

                //showDialog();


                break;
            default:
                break;
        }

    }

    private void showDialog() {
        dialog = new Dialog(Start_VerificationActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirm_dialog_layout);
        TextView tvPhoneNumber = dialog.findViewById(R.id.tvPhoneNumber);

        tvPhoneNumber.setText(phone);
        dialog.setCancelable(false);
        dialog.show();
    }
    private void hideDialog() {
        dialog.dismiss();
    }

    public  void  otpReceived (String messageText){
        Pattern pattern = Pattern.compile("(\\d{4})");
        Matcher matcher = pattern.matcher(messageText);

        if (matcher.find()) {
            password = matcher.group(1);
        }
        setProgress();
        sendVerificationCode();


    }
    /**
     * method requests server to send verification code
     */

    private void sendVerificationCode() {

        showDialog();
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", phoneNumber);
        requestParams.put("token", password);

        RequestResetApiService.requestReset("auth/default/password-reset-token-verification", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("VERIFY_TOKEN ", response.toString() + " id: " + phoneNumber + " token " + password);
                try {
                    if ("true".equals(response.getString("success"))) {
                        openNewActivity();
                    }
                    else {
                        hideDialog();
                        Toast.makeText(Start_VerificationActivity.this, "Invalid verification code", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    hideDialog();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                new ShowNoNetworkError(getApplicationContext());


                if (errorResponse != null) {
                    try {
                        if ("false".equals(errorResponse.getString("success"))) {
                            Toast.makeText(Start_VerificationActivity.this, "Invalid verification code", Toast.LENGTH_LONG).show();
                        }
                        hideDialog();
                    } catch (Exception ex) {
                        hideDialog();

                    }

                }
            }
        });
    }
    /**
     * Method opens new activity
     */

    private void openNewActivity() {


        hideDialog();
        Intent intent = new Intent(Start_VerificationActivity.this, Verification_CompleteActivity.class);
        intent.putExtra("code", password);
        intent.putExtra("id", phoneNumber);
        intent.putExtra("type", getIntent().getStringExtra("type"));
        startActivity(intent);
        finish();

    }
}
