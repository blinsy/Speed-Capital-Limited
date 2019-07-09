package com.example.speedcapitalltd.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.speedcapitalltd.Adapters.AddClientAdapter;
import com.example.speedcapitalltd.R;
import com.example.speedcapitalltd.models.MerchantClient;
import com.example.speedcapitalltd.utilities.GetNotLinkedMerchantApiService;
import com.example.speedcapitalltd.utilities.ShowNoNetworkError;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AddClientActivity extends AppCompatActivity {
    /**
     * The Mode is used to trace the origin of the request 0 means it comes from the profile while 1 means is from login.
     */
    private int mode = 0;
    /**
     * The recyclerview to populate clients to choose from.
     */
    private RecyclerView rcvAddClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

        getSupportActionBar().setTitle(R.string.add_client_activity_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rcvAddClient = findViewById(R.id.rcvClients);
        mode = getIntent().getIntExtra("mode", 0);
        rcvAddClient.setLayoutManager(new LinearLayoutManager(this));
        getMerchants();
    }

    private void getMerchants() {
        final LoadingClass loadingClass = new LoadingClass(AddClientActivity.this);
        loadingClass.showLoading();
        String accessToken = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_access_token_identifier), null);
        RequestParams params = new RequestParams();
        params.put("user_id", getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_user_id_identifier), null));
        GetNotLinkedMerchantApiService.getNotLinkedMerchants(accessToken, "merchant/suppliers/notlinked-merchants", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                processMerchantsResponse(response,loadingClass);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                new ShowNoNetworkError(AddClientActivity.this);

                Toast.makeText(AddClientActivity.this, "We experienced an error while processing your request", Toast.LENGTH_LONG).show();
                if (loadingClass.isDialogShowing()) {
                    loadingClass.hideLoading();}

            }
        });
    }
    /**
     * This method picks the response from the callback and processes it
     * @param response the response obtained from the callback
     * @param loadingClass the class that shows a progress dialog
     */
    private void processMerchantsResponse(JSONObject response, LoadingClass loadingClass){
        try {
            JSONArray jsonArray = response.getJSONArray("data");
            List<MerchantClient> merchantClients = new ArrayList<>();
            int length=jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                MerchantClient merchantClient = new MerchantClient();
                //merchantClient.setMerchantName(jsonObject.getString("company_name"));
                merchantClient.setMerchantId(jsonObject.getInt("id"));
                // merchantClient.setMerchantLogo(jsonObject.getString("company_logo"));
                merchantClient.setChecked(false);
                merchantClients.add(merchantClient);

            }


            if(jsonArray.length()>0) {
                rcvAddClient.setAdapter(new AddClientAdapter(AddClientActivity.this, merchantClients, mode));
            }else
            {
                TextView tvNotFound=findViewById(R.id.tvNotFound);
                tvNotFound.setVisibility(View.VISIBLE);
                rcvAddClient.setVisibility(View.GONE);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }


        if (loadingClass.isDialogShowing()) {
            loadingClass.hideLoading();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mode == 1) {
                startActivity(new Intent(AddClientActivity.this,SplashActivity.class));
                finish();
            }
            else {
                startActivity(new Intent(AddClientActivity.this,ManageClientActivity.class));

                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
