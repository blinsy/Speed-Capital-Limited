package com.example.speedcapitalltd.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.speedcapitalltd.Adapters.InvoiceAdapter;
import com.example.speedcapitalltd.Fragments.FragmentActivity;
import com.example.speedcapitalltd.R;
import com.example.speedcapitalltd.models.ValidInvoice;
import com.example.speedcapitalltd.utilities.GetInvoiceApplicationApiService;
import com.example.speedcapitalltd.utilities.GetpaymentAccountApiService;
import com.example.speedcapitalltd.utilities.ShowNoNetworkError;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
//import com.loopj.android.http.JsonHttpResponseHandler;
//import com.loopj.android.http.RequestParams;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rcvInvoices;
    private TextView tvNotFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        rcvInvoices = findViewById(R.id.rcvInvoices);
        ImageView ivManageClient = findViewById(R.id.ivManageClient);
        ImageView ivMoreInvoiceActivity = findViewById(R.id.ivMoreInvoiceActivity);
        TextView tvViewAll = findViewById(R.id.tvViewAll);
        RelativeLayout rlRequestInvoicePayments = findViewById(R.id.rlRequestInvoicePayments);
        ImageView ivInvoiceStatus = findViewById(R.id.ivInvoiceStatus);
        ImageView ivSettings = findViewById(R.id.ivSettings);
        tvNotFound = findViewById(R.id.tvNotFound);
        rlRequestInvoicePayments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent= new Intent(MainActivity.this, Select_ClientActivity.class);
//                startActivity(intent);
                getPaymentAccounts();
            }
        });
        tvViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTransactions();
            }
        });

        ivMoreInvoiceActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTransactions();
            }
        });
        ivSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);

            }
        });
        ivInvoiceStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FragmentActivity.class);
                startActivity(intent);

            }
        });
               getApplications();
LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
       rcvInvoices.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rcvInvoices.setLayoutManager(linearLayoutManager);
//TODO DELETE TEST
//        List<ValidInvoice> myList=new ArrayList<>();
//        ValidInvoice v1=new ValidInvoice("-5,000.00Ksh","20 Nov",R.drawable.niaivas_super,"Naivas ","Preapproved Payment-Complete");
//        ValidInvoice v2=new ValidInvoice("-100,000.00Ksh","20 Nov",R.drawable.tuskys,"Tuskys ","Preapproved Payment-Complete");
//        ValidInvoice v3=new ValidInvoice("-50,000.00Ksh","20 Nov",R.drawable.niaivas_super,"Naivas ","Preapproved Payment-Complete");
//        ValidInvoice v4=new ValidInvoice("-45,700.00Ksh","20 Nov",R.drawable.cleanshelf,"CleanShelf ","Preapproved Payment-Complete");
//        ValidInvoice v5=new ValidInvoice("-10,000.00Ksh","20 Nov",R.drawable.tuskys,"Tuskys ","Preapproved Payment-Complete");
//        ValidInvoice v6=new ValidInvoice("-100,000.00Ksh","20 Nov",R.drawable.tuskys,"Tuskys ","Preapproved Payment-Complete");
//        ValidInvoice v7=new ValidInvoice("-50,000.00Ksh","20 Nov",R.drawable.niaivas_super,"Naivas ","Preapproved Payment-Complete");
//        ValidInvoice v8=new ValidInvoice("-45,700.00Ksh","20 Nov",R.drawable.cleanshelf,"CleanShelf ","Preapproved Payment-Complete");
//        ValidInvoice v9=new ValidInvoice("-10,000.00Ksh","20 Nov",R.drawable.tuskys,"Tuskys ","Preapproved Payment-Complete");
//
//
//
//        myList.add(v1);
//        myList.add(v2);
//        myList.add(v3);
//        myList.add(v4);
//        myList.add(v5);
//        myList.add(v6);
//        myList.add(v7);
//        myList.add(v8);
//        myList.add(v9);
//
//
//        Log.d("list size ",String.valueOf(myList.size()));
//        InvoiceAdapter adapter = new InvoiceAdapter(myList,getApplicationContext());
//        rcvInvoices.setAdapter(adapter);


        ivManageClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ManageClientActivity.class);
                startActivity(intent);
            }
        });
    }
    /**
     * Method to show status of the transactions
     */
    private void showTransactions() {
        startActivity(new Intent(MainActivity.this, InvoiceStatusActivity.class));

    }
    /**
     * This method obtains latest invoice discounting applications from the server
     */

    private void getApplications() {

        final LoadingClass loadingClass = new LoadingClass(MainActivity.this);
        loadingClass.showLoading();
        String accessToken = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_access_token_identifier), null);
        String userId = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_user_id_identifier), null);

        RequestParams params = new RequestParams();
        params.put("user_id", userId);

        GetInvoiceApplicationApiService.GetInvoiceApplication(accessToken, "merchant/applications/applications-list", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Log.d("RESPONSE_INVOICES", response.toString());

               /* "data": [
                {
                    "id": 1,
                        "invoice_id": 17,
                        "merchant_id": 1,
                        "application_date": "2018-06-08 15:12:00",
                        "applied_amount": 60000,
                        "payment_mode_id": 1,
                        "applicant_id": 1,
                        "status": "pending",
                        "narration": null,
                        "created_by": null,
                        "created_on": "2018-06-08 15:12:00",
                        "updated_by": null,
                        "updated_on": null,
                        "invoice_amount": 580000,
                        "invoice_number": "INV0002",
                        "maturity_date": "2018-06-27",
                        "lpo_number": "LPO-0001",
                        "supplier_name": "Speed Movers"
                }*/

                try {

                    List<ValidInvoice> invoices = new ArrayList<>();

                    JSONArray jsonArray = response.getJSONArray("data");

                    if (jsonArray.length() == 0) {
                        rcvInvoices.setVisibility(View.GONE);
                        tvNotFound.setVisibility(View.VISIBLE);
                    }

                    int length=jsonArray.length();
                    for (int k = 0; k < length; k++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(k);
                        ValidInvoice validInvoice = new ValidInvoice();
                        validInvoice.setRecordId(jsonObject.getString("id"));
                        validInvoice.setAmount(jsonObject.getString("applied_amount"));
                        validInvoice.setMerchantId(jsonObject.getString("merchant_id"));
                        validInvoice.setlPONumber(jsonObject.getString("lpo_number"));
                        validInvoice.setInvoiceDate(jsonObject.getString("application_date"));
                        validInvoice.setMechSupCode(jsonObject.getString("status"));
                        validInvoice.setMaturityDate(jsonObject.getString("merchant_name"));
                        validInvoice.setMerchantLogo(jsonObject.getString("merchant_logo"));


                        invoices.add(validInvoice);
                    }
                    InvoiceAdapter adapter = new InvoiceAdapter(MainActivity.this,invoices);
                        rcvInvoices.setAdapter(adapter);
                } catch (Exception ex) {
                    Log.d("RESPONSE_INVOICES ex", ex.toString());
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

    /**
     * This method gets payment accounts of a user
     */

    private void getPaymentAccounts() {
        final LoadingClass loadingClass = new LoadingClass(MainActivity.this);
        loadingClass.showLoading();
        String accessToken = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_access_token_identifier), null);
        String userId = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_user_id_identifier), null);

        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        GetpaymentAccountApiService.getPaymentAccounts(accessToken, "supplier/supplier/supplier-payment-methods", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("PAYMENT_ACCOUNTS", response.toString());
                try {

                    if(response.getJSONArray("data").length()==0)
                    {
                        Toast.makeText(getApplicationContext(),"Add Payment Accounts First",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, PaymentOptionsActivity.class);
                        intent.putExtra("request",1);
                        startActivity(intent);


                    }
                    else {
                        Intent intent = new Intent(MainActivity.this, Select_ClientActivity.class);
                        startActivity(intent);
                      //  Bungee.slideLeft(MainActivity.this);
                    }
                    if (loadingClass.isDialogShowing()) {
                        loadingClass.hideLoading();}
                }
                catch (Exception ex) {
                    if (loadingClass.isDialogShowing()) {
                        loadingClass.hideLoading();}
                }


            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("PAYMENT_ACCOUNTS", response.toString());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                new ShowNoNetworkError(getApplicationContext());


                if (loadingClass.isDialogShowing()) {
                    loadingClass.hideLoading();
                }
                Toast.makeText(MainActivity.this, "Encountered (accounts) an error when processing your request", Toast.LENGTH_LONG).show();


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getApplications();
    }
}