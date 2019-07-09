package com.example.speedcapitalltd.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.speedcapitalltd.R;
import com.example.speedcapitalltd.models.PaymentModeProvider;
import com.example.speedcapitalltd.models.ProviderBranch;
import com.example.speedcapitalltd.utilities.DeletePaymentModeApiservice;
import com.example.speedcapitalltd.utilities.GetBankListApiService;
import com.example.speedcapitalltd.utilities.GetBranchListApiService;
import com.example.speedcapitalltd.utilities.GetModeProviderApiService;
import com.example.speedcapitalltd.utilities.GetPaymentAccountsApiService;
import com.example.speedcapitalltd.utilities.GetProviderBranchApiService;
import com.example.speedcapitalltd.utilities.SavePaymentModesApiService;
import com.example.speedcapitalltd.utilities.ShowNoNetworkError;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import cz.msebera.android.httpclient.Header;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class PaymentOptionsActivity extends AppCompatActivity {
    /**
     * Layout to display bank details
     */
    private RelativeLayout rlBankLayout;

    private RelativeLayout
            rlMobileMoneyLayout;
    private RelativeLayout
            rlExistingBankAccount;
    private RelativeLayout
            rlSafaricomInput;
    private RelativeLayout
            rlAirtelInput;
    private RelativeLayout
            rlEquitelInput;
    private RelativeLayout
            rlTKashInput;

    private CardView cardAirtelMoney;
    private	CardView cardEquitel;
    private	CardView cardSafaricom;
    private CardView cardTKash;
    private Button btnBank;
    private Button btnRemoveAccount;
    private Button btnMobileMoney;

    private final String payment_mode_id="payment_mode_id";
    /**
     * The Mobile payment mode.
     */
    private final int mobile_payment_mode = 2;
    private final int bank_payment_mode = 1;
    /**
     * The Safaricom provider.
     */




    private final String data="data";
    /**
     * The Tv add t kash phone number.
     */
    private TextView tvAddTKashPhoneNumber;
    private TextView
            tvAddEquitelPhoneNumber;
    private TextView
            tvAddAirtelPhoneNumber;
    private TextView
            tvAddSafaricomPhoneNumber;
    /**
     * The Tv t kash phone number.
     */
    private TextView tvTKashPhoneNumber;
    private TextView
            tvAirtelPhoneNumber;
    private TextView
            tvSafaricomPhoneNumber;
    private TextView
            tvEquitelPhoneNumber;
    /**
     * The Bank list.
     */
    private final List<PaymentModeProvider> bankList = new ArrayList<>();
    /**
     * The Branch list.
     */
    private final List<ProviderBranch> branchList = new ArrayList<>();
    /**
     * The Tv account number text.
     */
    private TextView tvAccountNumberText;
    private TextView
            tvBankNameText;
    private TextView
            tvBankBranchText;
    private TextView
            tvAccountNameText;
    /**
     * The Exists bank account.
     */
    private boolean existsBankAccount = false;
    /**
     * The Edt bank account number.
     */
    private EditText edtBankAccountNumber;
    private EditText
            edtBankAccountName;
    /**
     * The Tl bank account number.
     */
    private TextInputLayout tlBankAccountNumber;
    private TextInputLayout
            tlBankAccountName;

    /**
     * The Spn select bank.
     */
    private Spinner spnSelectBank;
    /**
     * The Spn select bank branch.
     */
    private Spinner spnSelectBankBranch;

    /**
     * The Mobile provider.
     */
    private int mobileProvider = 0;
    /**
     * The Request.
     */
    private int request = 0;
    /**
     * The Btn bank control.
     */
    private Button btnBankControl;
    /**
     * The Payment mode providers.
     */
    private final List<PaymentModeProvider> paymentModeProviders = new ArrayList<>();
    /**
     * The Provider branches.
     */
    private final List<ProviderBranch> providerBranches = new ArrayList<>();
    /**
     * The Spn mode provider.
     */
    private ArrayAdapter<String> spnModeProvider;
    private ArrayAdapter<String> spnProviderBranch;


    private final String id="id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_options);

        getSupportActionBar().setTitle(R.string.add_payment_methods_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        spnModeProvider = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item);
        spnModeProvider.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnProviderBranch = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item);
        spnProviderBranch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnModeProvider.add(getString(R.string.select_bank));
        spnProviderBranch.add(getString(R.string.select_bank_branch));

        loadBankList();

              initUi();
              handleClick();

              spnSelectBank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                  @Override
                  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                      if (position > 0){
                          getProviderBranches(paymentModeProviders.get(position - 1).getId());
                      }
                  }
                  public void onNothingSelected(AdapterView<?> parent){

                  }
              });
    }

    private void initUi() {
        btnBankControl = findViewById(R.id.btnBankControl);

        cardAirtelMoney = findViewById(R.id.cardAirtleMoney);
        cardEquitel = findViewById(R.id.cardEquitel);
        cardSafaricom = findViewById(R.id.cardSafaricom);
        cardTKash = findViewById(R.id.cardTKash);

        rlBankLayout = findViewById(R.id.rlBankLayout);
        // RelativeLayout rlButtonLayout = findViewById(R.id.rlButton);
        rlMobileMoneyLayout = findViewById(R.id.rlMobileMoneyLayout);
        rlExistingBankAccount = findViewById(R.id.rlExistingBankAccount);
        rlBankLayout.setVisibility(View.VISIBLE);
        rlMobileMoneyLayout.setVisibility(View.GONE);
        rlExistingBankAccount.setVisibility(View.GONE);


        request = getIntent().getIntExtra("request", 0);

        rlAirtelInput = findViewById(R.id.rlAirtelInput);
        rlTKashInput = findViewById(R.id.rlTKashInput);
        rlEquitelInput = findViewById(R.id.rlEquitelInput);
        rlSafaricomInput = findViewById(R.id.rlSafaricomInput);

        rlAirtelInput.setVisibility(View.GONE);
        rlSafaricomInput.setVisibility(View.GONE);
        rlEquitelInput.setVisibility(View.GONE);
        rlTKashInput.setVisibility(View.GONE);

        tvAddAirtelPhoneNumber = findViewById(R.id.tvAddAirtelPhoneNumber);
        tvAddEquitelPhoneNumber = findViewById(R.id.tvAddEquitelPhoneNumber);
        tvAddTKashPhoneNumber = findViewById(R.id.tvAddTKashPhoneNumber);
        tvAddSafaricomPhoneNumber = findViewById(R.id.tvAddMpesaPhoneNumber);


        tvAccountNameText = findViewById(R.id.tvAccountNameText);
        tvAccountNumberText = findViewById(R.id.tvAccountNumberText);

        tvBankBranchText = findViewById(R.id.tvBankBranchText);
        tvBankNameText = findViewById(R.id.tvBankText);

        tvSafaricomPhoneNumber = findViewById(R.id.tvMpesaPhoneNumber);
        tvAirtelPhoneNumber = findViewById(R.id.tvAirtlePhoneNumber);
        tvEquitelPhoneNumber = findViewById(R.id.tvEquitelPhoneNumber);
        tvTKashPhoneNumber = findViewById(R.id.tvTKashPhoneNUmber);


        spnSelectBank = findViewById(R.id.spnSelectBank);
        spnSelectBankBranch = findViewById(R.id.spnSelectBankBranch);

        tlBankAccountName = findViewById(R.id.tlAccountName);
        tlBankAccountNumber = findViewById(R.id.tlBankAccountNumber);

        edtBankAccountName = findViewById(R.id.edtAccountName);
        edtBankAccountNumber = findViewById(R.id.edtBankAccountNumber);
        btnRemoveAccount = findViewById(R.id.btnRemoveAccount);
    }
    /**
     * intializes click handlers
     */
    private void handleClick() {
        btnRemoveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePaymentOption(String.valueOf(bank_payment_mode));
            }
        });


        cardTKash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobileProvider == 0) {
                    rlTKashInput.setVisibility(View.VISIBLE);
                    rlSafaricomInput.setVisibility(View.GONE);
                    rlAirtelInput.setVisibility(View.GONE);
                    rlEquitelInput.setVisibility(View.GONE);
                }
            }
        });

        cardSafaricom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobileProvider == 0) {
                    rlTKashInput.setVisibility(View.GONE);
                    rlSafaricomInput.setVisibility(View.VISIBLE);
                    rlAirtelInput.setVisibility(View.GONE);
                    rlEquitelInput.setVisibility(View.GONE);
                }
            }
        });

        cardAirtelMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobileProvider == 0) {
                    rlTKashInput.setVisibility(View.GONE);
                    rlSafaricomInput.setVisibility(View.GONE);
                    rlAirtelInput.setVisibility(View.VISIBLE);
                    rlEquitelInput.setVisibility(View.GONE);
                }
            }
        });
        cardEquitel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobileProvider == 0) {
                    rlTKashInput.setVisibility(View.GONE);
                    rlSafaricomInput.setVisibility(View.GONE);
                    rlAirtelInput.setVisibility(View.GONE);
                    rlEquitelInput.setVisibility(View.VISIBLE);
                }
            }
        });

        buttonControl();

        addSafaricomNumber();
        addTKashNumber();
        addEquitelNumber();
        addAirtelNumber();




        btnBank = findViewById(R.id.btnBank);
        btnMobileMoney = findViewById(R.id.btnMobileMoney);

        bankClick();

        mobileMoneyClick();
    }
    /**
     * Handles method to add airtel number
     */
    private void addAirtelNumber() {
        tvAddAirtelPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tvAddAirtelPhoneNumber.getText().toString().equals(getString(R.string.delete_phone_number))) {
                    removePaymentOption(String.valueOf(mobile_payment_mode));
                } else
                {
                    int  airtel_provider = 2;

                    addPhoneNumberDialog(airtel_provider);
                }
            }
        });
    }
    /**
     * Handles method to add equitel number
     */
    private void addEquitelNumber() {
        tvAddEquitelPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tvAddEquitelPhoneNumber.getText().toString().equals(getString(R.string.delete_phone_number))) {
                    removePaymentOption(String.valueOf(mobile_payment_mode));
                } else {
                    int  equitel_provider = 4;
                    addPhoneNumberDialog(equitel_provider);
                }
            }
        });
    }
    /**
     * Handles method to add TKash number
     */
    private void addTKashNumber() {
        tvAddTKashPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvAddTKashPhoneNumber.getText().toString().equals(getString(R.string.delete_phone_number))) {
                    removePaymentOption(String.valueOf(mobile_payment_mode));
                } else
                {
                    int telkom_provider = 3;
                    addPhoneNumberDialog(telkom_provider);
                }
            }
        });
    }
    /**
     * Handles method to add safaricom number
     */
    private void addSafaricomNumber() {
        tvAddSafaricomPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tvAddSafaricomPhoneNumber.getText().toString().equals(getString(R.string.delete_phone_number))) {
                    removePaymentOption(String.valueOf(mobile_payment_mode));


                } else {
                    int safaricom_provider = 1;
                    addPhoneNumberDialog(safaricom_provider);
                }

            }
        });
    }
    /**
     * Handles method to implement bank button click event
     */
    private void buttonControl()
    {
        btnBankControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btnBankControl.getText().toString().equals(getString(R.string.add_account))) {

                    if (validateBankDetails()) {
                        PaymentModeProvider modeProvider = paymentModeProviders.get(spnSelectBank.getSelectedItemPosition() - 1);
                        ProviderBranch providerBranch = providerBranches.get(spnSelectBankBranch.getSelectedItemPosition() - 1);
                        String accountNumber = edtBankAccountNumber.getText().toString().trim();
                        String accountName = edtBankAccountName.getText().toString().trim();
                        addPaymentMode(bank_payment_mode, modeProvider.getId(), providerBranch.getBranchId(), accountNumber, accountName);
                    }
                }
            }
        });

    }

        /**
         * Handles click event for bank button
         */
         private void bankClick() {
        btnBank=findViewById(R.id.btnBank);
        btnBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (existsBankAccount) {
                    rlBankLayout.setVisibility(GONE);
                    rlMobileMoneyLayout.setVisibility(GONE);
                    rlExistingBankAccount.setVisibility(VISIBLE);
                } else {
                    rlBankLayout.setVisibility(VISIBLE);
                    rlMobileMoneyLayout.setVisibility(GONE);
                    rlExistingBankAccount.setVisibility(GONE);
                }
                btnBank.setBackgroundColor(ContextCompat.getColor(PaymentOptionsActivity.this, R.color.red));
                btnBank.setTextColor(ContextCompat.getColor(PaymentOptionsActivity.this, R.color.white));
                btnMobileMoney.setBackgroundColor(ContextCompat.getColor(PaymentOptionsActivity.this, R.color.red));
                btnMobileMoney.setTextColor(ContextCompat.getColor(PaymentOptionsActivity.this, R.color.colorAccent));
            }
        });
        }


        /**
         *
         * Handles event for mobile money button click
         */
        private void mobileMoneyClick(){
        btnMobileMoney= findViewById(R.id.btnMobileMoney);
            btnMobileMoney.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rlBankLayout.setVisibility(GONE);
                    rlExistingBankAccount.setVisibility(GONE);
                    rlMobileMoneyLayout.setVisibility(VISIBLE);
                    btnMobileMoney.setBackgroundColor(ContextCompat.getColor(PaymentOptionsActivity.this, R.color.red));
                    btnMobileMoney.setTextColor(ContextCompat.getColor(PaymentOptionsActivity.this, R.color.white));
                    btnBank.setBackgroundColor(ContextCompat.getColor(PaymentOptionsActivity.this, R.color.red));
                    btnBank.setTextColor(ContextCompat.getColor(PaymentOptionsActivity.this, R.color.colorAccent));

                }
            });
        }

//        btnBankControl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (btnBankControl.getText().toString().equals(getString(R.string.add_account))) {
//
//                    if (validateBankDetails()) {
//                        PaymentModeProvider modeProvider = paymentModeProviders.get(spnSelectBank.getSelectedItemPosition() - 1);
//                        ProviderBranch providerBranch = providerBranches.get(spnSelectBankBranch.getSelectedItemPosition() - 1);
//                        String accountNumber = edtBankAccountNumber.getText().toString().trim();
//                        String accountName = edtBankAccountName.getText().toString().trim();
//                     addPaymentMode(bank_payment_mode, modeProvider.getId(), providerBranch.getBranchId(), accountNumber, accountName);
//                    }
//                }
//            }
//
//            private void addPaymentMode(int bank_payment_mode, int id, int branchId, String accountNumber, String accountName) {
//            }
//        });
//    }


    private boolean validateBankDetails() {
        boolean inputs=true;
        if (edtBankAccountName.getText().toString().trim().length() == 0) {
            tlBankAccountName.setError("Provide Account Name");
            edtBankAccountName.requestFocus();
            inputs= false;
        }
        else if (spnSelectBank.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Select Bank", Toast.LENGTH_LONG).show();
            inputs= false;
        }
        else if (spnSelectBankBranch.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Select Bank Branch", Toast.LENGTH_LONG).show();
            inputs= false;
        }
        else if (edtBankAccountNumber.getText().toString().trim().length() == 0) {
            tlBankAccountNumber.setError("Provide Account Name");
            edtBankAccountNumber.requestFocus();
            inputs= false;
        }

        return inputs;
    }
    /**
     * Gets banks.
     *
     */
    private void getBanks() {
        final LoadingClass loadingClass = new LoadingClass(PaymentOptionsActivity.this);
        loadingClass.showLoading();
        String accessToken = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_access_token_identifier), null);
        RequestParams requestParams = new RequestParams();

        int paymentMode=1;
        requestParams.put(payment_mode_id, paymentMode);
        GetModeProviderApiService.getModeProvider(accessToken, "settings/payment-methods/payment-options", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {

                    int length=response.getJSONArray(data).length();

                    for (int i = 0; i < length; i++) {
                        JSONObject jsonObject = response.getJSONArray(data).getJSONObject(i);
                        PaymentModeProvider paymentMode = new PaymentModeProvider();
                        paymentMode.setId(jsonObject.getInt(id));
                        paymentMode.setName(jsonObject.getString("name"));
                        spnModeProvider.add(paymentMode.getName());
                        paymentModeProviders.add(paymentMode);
                    }
                    spnSelectBank.setAdapter(spnModeProvider);

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
                if (loadingClass.isDialogShowing()) {
                    loadingClass.hideLoading();
                }
                Toast.makeText(PaymentOptionsActivity.this, "Encountered an error when processing your request", Toast.LENGTH_LONG).show();


            }

            @Override
            public void onFinish() {
                super.onFinish();
                getPaymentAccounts();
                if (loadingClass.isDialogShowing()) {
                    loadingClass.hideLoading();
                }
            }
        });
    }

    /**
     * Remove payment option.
     *
     * @param payment_mode the payment mode
     */
    private void removePaymentOption(final String payment_mode) {
        final LoadingClass loadingClass = new LoadingClass(PaymentOptionsActivity.this);
        loadingClass.showLoading();
        String userId = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_user_id_identifier), null);
        String accessToken = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_access_token_identifier), null);
        RequestParams requestParams = new RequestParams();
        requestParams.put(payment_mode_id, payment_mode);
        requestParams.put("user_id", userId);

        DeletePaymentModeApiservice.deleteMode(accessToken, "supplier/supplier/del", requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);


                if (loadingClass.isDialogShowing()) {
                    loadingClass.hideLoading();
                }

                startActivity(new Intent(PaymentOptionsActivity.this, PaymentOptionsActivity.class));
               // Bungee.slideLeft(AddPaymentOptions.this);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                new ShowNoNetworkError(getApplicationContext());
                if (loadingClass.isDialogShowing()) {
                    loadingClass.hideLoading();}
            }
        });
    }


    /**
     * Gets provider branches.
     *
     * @param providerId the bank id
     */
    private void getProviderBranches(int providerId) {
        final LoadingClass loadingClass = new LoadingClass(PaymentOptionsActivity.this);
        loadingClass.showLoading();
        String accessToken = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_access_token_identifier), null);
        RequestParams requestParams = new RequestParams();
        requestParams.put("bank_id", providerId);
        GetProviderBranchApiService.getProviderBranch(accessToken, "settings/payment-methods/bank-branches", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    spnProviderBranch.clear();
                    spnProviderBranch.add(getString(R.string.select_bank_branch));
                    int length=response.getJSONArray(data).length();
                    for (int i = 0; i <length ; i++) {
                        ProviderBranch providerBranch = new ProviderBranch();
                        providerBranch.setBranchId(response.getJSONArray(data).getJSONObject(i).getInt(id));
                        providerBranch.setProviderId(response.getJSONArray(data).getJSONObject(i).getInt("bank_id"));
                        providerBranch.setBranchName(response.getJSONArray(data).getJSONObject(i).getString("branch_name"));
                        providerBranches.add(providerBranch);
                        spnProviderBranch.add(providerBranch.getBranchName());
                    }
                    spnSelectBankBranch.setAdapter(spnProviderBranch);
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
                if (loadingClass.isDialogShowing()) {
                    loadingClass.hideLoading();
                }


            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result=false;
        if (item.getItemId() == android.R.id.home) {
            finish();
           // Bungee.shrink(AddPaymentOptions.this);
            result= true;
        }
        return result;
    }


    /**
     * Add payment mode.
     *
     * @param paymentModeId the payment mode id
     * @param providerId    the provider id (bank, or mobile money)
     * @param branch_id     the branch id
     * @param accountNumber the account number
     * @param accountName   the account name
     */
    private void addPaymentMode(int paymentModeId, int providerId, final int branch_id, String accountNumber, String accountName) {
        final LoadingClass loadingClass = new LoadingClass(PaymentOptionsActivity.this);
        loadingClass.showLoading();

        String accessToken = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_access_token_identifier), null);
        String userId = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_user_id_identifier), null);

        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put(payment_mode_id, paymentModeId);
        params.put("provider_id", providerId);
        params.put("account_number", accountNumber);
        if (branch_id != -1) {
            params.put(" provider_branch_id", branch_id);
        }
        if (accountName != null) {
            params.put("account_name", accountName);
        }

        SavePaymentModesApiService.savePaymentMode(accessToken, "supplier/supplier/add-payment-method", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);


                getPaymentAccounts();
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

    /**
     * Add phone number dialog.
     *
     * @param providerId the provider id
     */
    private void addPhoneNumberDialog(final int providerId) {
        final Dialog dialog = new Dialog(PaymentOptionsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_phone_number_dialog);
        dialog.setCancelable(false);

        final EditText edtPhoneNumber = dialog.findViewById(R.id.edtPhoneNumber);
        final TextInputLayout tlPhoneNumber = dialog.findViewById(R.id.tlPhoneNumber);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEditText(edtPhoneNumber, tlPhoneNumber)) {
                    addPaymentMode(mobile_payment_mode, providerId, -1, edtPhoneNumber.getText().toString().trim(), null);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    /**
     * The method validates the edittext if it has required data and then sets focus on textinput layout if otherwise
     * @param editText widget where you type phone number
     * @param textInputLayout widget that holds edittext
     * @return boolean
     */
    private boolean validateEditText(EditText editText, TextInputLayout textInputLayout) {
        boolean result=true;
        if (editText.getText().toString().trim().length() == 0) {
            textInputLayout.setError("Provide Phone Number");
            editText.requestFocus();
            result=false;
        }
        return result;
    }

    /**
     * Gets payment accounts.
     */
    private void getPaymentAccounts() {
        final LoadingClass loadingClass = new LoadingClass(PaymentOptionsActivity.this);
        loadingClass.showLoading();
        String accessToken = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_access_token_identifier), null);
        String userId = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_user_id_identifier), null);

        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        GetPaymentAccountsApiService.getPaymentAccounts(accessToken, "supplier/supplier/supplier-payment-methods", params, new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                processGetPaymentAccounts(response,loadingClass);
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

    /**
     * Method will process data from the calling callback's onSuccess() method
     * @param response JSONObject received from the response
     * @param loadingClass the class that shows progress dialog
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void processGetPaymentAccounts(JSONObject response, LoadingClass loadingClass) {
        try {

            int length= response.getJSONArray(data).length();
            for (int i = 0; i <length; i++) {
                JSONObject jsonObject = response.getJSONArray(data).getJSONObject(i);

                if ("1".equals(jsonObject.getString(payment_mode_id))) {
                    int bank_id = jsonObject.getInt("provider_id");

                    int branch_id = jsonObject.getInt("provider_branch_id");
                    String bankName = "";
                    String branchName = "";
                    int size= bankList.size();
                    for (int k = 0; k <size; k++) {

                        PaymentModeProvider provider = bankList.get(k);

                        if (provider.getId() == bank_id) {
                            bankName = provider.getName();
                        }

                    }

                    int branchSize=branchList.size();
                    for (int branchPosition = 0; branchPosition <branchSize ; branchPosition++) {
                        ProviderBranch branch = branchList.get(branchPosition);
                        if (branch.getBranchId() == branch_id) {
                            branchName = branch.getBranchName();
                        }
                    }
                    String accountName = jsonObject.getString("account_name");
                    String accountNumber = jsonObject.getString("account_number");
                    getProviderBranches(branch_id);
                    getBankAccount(branchName, bankName, accountName, accountNumber);
                    existsBankAccount = true;

                } else {
                    String providerId = jsonObject.getString("provider_id");
                    String phoneNumber = jsonObject.getString("account_number");
                    mobileProvider = Integer.parseInt(providerId);
                    getMobileAccounts(providerId, phoneNumber);

                }
            }
            if (loadingClass.isDialogShowing()) {
                loadingClass.hideLoading();
            }
        } catch (Exception ex) {
            if (loadingClass.isDialogShowing()){
                loadingClass.hideLoading();}
        }


    }

    /**
     * Gets mobile accounts.
     *
     * @param provider_id  the provider id
     * @param phone_number the phone number
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void getMobileAccounts(String provider_id, String phone_number) {

        if ("1".equals(provider_id)) {
            tvSafaricomPhoneNumber.setText(phone_number);
            tvSafaricomPhoneNumber.setTextColor(ContextCompat.getColor(this, R.color.red));
            tvAddSafaricomPhoneNumber.setBackground(ContextCompat.getDrawable(this, R.drawable.red_rectangle_circular));
            tvAddSafaricomPhoneNumber.setText(getString(R.string.delete_phone_number));
            rlSafaricomInput.setVisibility(View.VISIBLE);
            rlTKashInput.setVisibility(View.GONE);
            rlAirtelInput.setVisibility(View.GONE);
            rlEquitelInput.setVisibility(View.GONE);
        }
        if ("2".equals(provider_id)) {
            tvAirtelPhoneNumber.setText(phone_number);
            tvAirtelPhoneNumber.setTextColor(ContextCompat.getColor(this, R.color.red));
            tvAddAirtelPhoneNumber.setBackground(ContextCompat.getDrawable(this, R.drawable.red_rectangle_circular));
            tvAddAirtelPhoneNumber.setText(getString(R.string.delete_phone_number));
            rlSafaricomInput.setVisibility(View.GONE);
            rlTKashInput.setVisibility(View.GONE);
            rlAirtelInput.setVisibility(View.VISIBLE);
            rlEquitelInput.setVisibility(View.GONE);
        }
        if ("4".equals(provider_id)) {
            tvEquitelPhoneNumber.setText(phone_number);
            tvEquitelPhoneNumber.setTextColor(ContextCompat.getColor(this, R.color.red));
            tvAddEquitelPhoneNumber.setBackground(ContextCompat.getDrawable(this, R.drawable.red_rectangle_circular));
            tvAddEquitelPhoneNumber.setText(getString(R.string.delete_phone_number));
            rlSafaricomInput.setVisibility(View.GONE);
            rlTKashInput.setVisibility(View.GONE);
            rlAirtelInput.setVisibility(View.GONE);
            rlEquitelInput.setVisibility(View.VISIBLE);
        }
        if ("3".equals(provider_id)) {
            tvTKashPhoneNumber.setText(phone_number);
            tvTKashPhoneNumber.setTextColor(ContextCompat.getColor(this, R.color.red));
            tvAddTKashPhoneNumber.setBackground(ContextCompat.getDrawable(this, R.drawable.red_rectangle_circular));
            tvAddTKashPhoneNumber.setText(getString(R.string.delete_phone_number));
            rlSafaricomInput.setVisibility(View.GONE);
            rlTKashInput.setVisibility(View.VISIBLE);
            rlAirtelInput.setVisibility(View.GONE);
            rlEquitelInput.setVisibility(View.GONE);
        }

    }

    /**
     * Gets bank account.
     *
     * @param branchName    the branch name
     * @param bankName      the bank name
     * @param accountName   the account name
     * @param accountNumber the account number
     */
    private void getBankAccount(String branchName, String bankName, String accountName, String accountNumber) {
        rlExistingBankAccount.setVisibility(View.VISIBLE);
        btnBank.setBackgroundColor(ContextCompat.getColor(PaymentOptionsActivity.this, R.color.red));
        btnBank.setTextColor(ContextCompat.getColor(PaymentOptionsActivity.this, R.color.white));
        btnMobileMoney.setBackgroundColor(ContextCompat.getColor(PaymentOptionsActivity.this, R.color.red));
        btnMobileMoney.setTextColor(ContextCompat.getColor(PaymentOptionsActivity.this, R.color.colorAccent));
        rlBankLayout.setVisibility(View.GONE);
        tvAccountNameText.setText(accountName);
        tvAccountNumberText.setText(accountNumber);
        tvBankNameText.setText(bankName);
        tvBankBranchText.setText(branchName);


    }

    /**
     * Gets bank list.
     */
    private void loadBankList() {
        final LoadingClass loadingClass = new LoadingClass(PaymentOptionsActivity.this);
        loadingClass.showLoading();
        String accessToken = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_access_token_identifier), null);
        //String userId = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_user_id_identifier), null);

        RequestParams params = new RequestParams();
        //params.put("user_id",userId);

        GetBankListApiService.getBankList(accessToken, "settings/banks/list", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("BANK_LIST", response.toString());

                processGetBankList(response,loadingClass);

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
                getBranchesList();
                if (loadingClass.isDialogShowing()) {
                    loadingClass.hideLoading();
                }
            }
        });

    }

    /**
     * Method is used to process data obtained from the callback
     * requesting for bank list
     * @param response JSONObject received from the callback
     * @param loadingClass The class that shows loading dialog
     */

    private void processGetBankList(JSONObject response, LoadingClass loadingClass){
        try {
            int length=response.getJSONArray(data).length();
            for (int i = 0; i <length;  i++) {
                JSONObject object = response.getJSONArray(data).getJSONObject(i);
                PaymentModeProvider provider = new PaymentModeProvider();
                provider.setId(object.getInt(id));
                provider.setName(object.getString("name"));
                bankList.add(provider);

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
     * Gets branches list.
     */
    private void getBranchesList() {
        final LoadingClass loadingClass = new LoadingClass(PaymentOptionsActivity.this);
        loadingClass.showLoading();
        String accessToken = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_access_token_identifier), null);
        // String userId = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_user_id_identifier), null);

        RequestParams params = new RequestParams();
        //params.put("user_id",userId);

        GetBranchListApiService.getBranchList(accessToken, "settings/bank-branches/list", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                processGetBranchesList(response,loadingClass);


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                new ShowNoNetworkError(getApplicationContext());

                if (loadingClass.isDialogShowing()) {
                    loadingClass.hideLoading();
                }


                if (throwable instanceof ConnectException){
                    startActivity(new Intent(getApplicationContext(), No_Internet_Activity.class));
                }

                else if( throwable instanceof TimeoutException){
                    startActivity(new Intent(getApplicationContext(), No_Internet_Activity.class));
                }
                else {
                    startActivity(new Intent(getApplicationContext(), No_Internet_Activity.class));
                }

            }

            @Override
            public void onFinish() {
                super.onFinish();
                getBanks();
                if (loadingClass.isDialogShowing()) {
                    loadingClass.hideLoading();
                }
            }
        });
    }

    /**
     * Method used to process response from the callback that requests for bank branches
     * @param response obtained from callback onSuccess
     * @param loadingClass shows loading dialog
     */
    private void processGetBranchesList(JSONObject response, LoadingClass loadingClass){
        try {
            int length=response.getJSONArray(data).length();

            for (int i = 0; i <length;  i++) {
                JSONObject object = response.getJSONArray(data).getJSONObject(i);
                ProviderBranch provider = new ProviderBranch();
                provider.setBranchName(object.getString("branch_name"));
                provider.setProviderId(object.getInt("bank_id"));
                provider.setBranchId(object.getInt(id));
                branchList.add(provider);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (request == 1) {
            startActivity(new Intent(PaymentOptionsActivity.this, Select_ClientActivity.class));
            finish();
           // Bungee.shrink(AddPaymentOptions.this);
        } else {
           // Bungee.shrink(AddPaymentOptions.this);
        }
    }
}
