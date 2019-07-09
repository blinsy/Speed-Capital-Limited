package com.example.speedcapitalltd.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.speedcapitalltd.Adapters.SelectClientAdapter;
import com.example.speedcapitalltd.R;
import com.example.speedcapitalltd.models.MerchantClient;
import com.example.speedcapitalltd.utilities.GetLinkedMerchants;
import com.example.speedcapitalltd.utilities.ShowNoNetworkError;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Select_ClientActivity extends AppCompatActivity  {

    private RecyclerView recyclerView;
    private Button btnContinue;
    private final List<MerchantClient> merchantClients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select__client);

        getSupportActionBar().hide();

        recyclerView= findViewById(R.id.rcvSelectClient);
getMerchants();

        //final RadioButton radioButton= findViewById(R.id.radio);
        btnContinue= findViewById(R.id.btnContinue);
btnContinue.setEnabled(false);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MerchantClient selectedMerchantClient = null;
                for (MerchantClient merchantClients : merchantClients) {
                    if (merchantClients.isChecked()) {
                        selectedMerchantClient = merchantClients;
                    }
                }
                if (selectedMerchantClient != null){
                    Intent intent= new Intent(Select_ClientActivity.this,DashboardActivity.class);
                    intent.putExtra("merchant_id",String.valueOf(selectedMerchantClient.getMerchantId()));
                    intent.putExtra("merchant_name",selectedMerchantClient.getMerchantName());
                    startActivity(intent);
                }else {
                    Toast.makeText(Select_ClientActivity.this,"Select Merchant To Continue",Toast.LENGTH_LONG).show();

                }

//                String client = SelectClientAdapter.name;
//                int image = SelectClientAdapter.image;
//
//                Intent intent = new Intent(Select_ClientActivity.this,DashboardActivity.class);
//
//                intent.putExtra("clientName",client);
//                intent.putExtra("logo",image);
//                startActivity(intent);

            }
        });

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        List<SelectClientModel> myList=new ArrayList<>();
//
//        SelectClientModel v1 = new SelectClientModel(R.drawable.naivas,"","Naivas Limited",true);
//        SelectClientModel v2 = new SelectClientModel(R.drawable.tuskys,"","Tusky Supermarket",false);
//        SelectClientModel v3 = new SelectClientModel(R.drawable.cleanshelf,"","Cleanshelf Supermarket",false);
//
//        SelectClientModel v4 = new SelectClientModel(R.drawable.ukwala,"","Ukwala SuperMarket",false);
//
//        SelectClientModel v5 = new SelectClientModel(R.drawable.uchumi,"","Uchumi Supermarket",false);
//        SelectClientModel v6 = new SelectClientModel(R.drawable.nakumatt,"","Nakumatt Supermartket",false);
//
//myList.add(v1);
//        myList.add(v2);
//        myList.add(v3);
//        myList.add(v4);
//        myList.add(v5);
//        myList.add(v6);
//
//        SelectClientAdapter adapter= new SelectClientAdapter(myList,getApplicationContext());
//        recyclerView.setAdapter(adapter);



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean homeSelected=false;
        if (item.getItemId() == android.R.id.home) {
            finish();
           // Bungee.shrink(SelectClientActivity.this);
            homeSelected= true;
        }
        return homeSelected;
    }

    public void back(View view) {
        Intent intent= new Intent(Select_ClientActivity.this,MainActivity.class);
        startActivity(intent);
    }
    /**
     * Method used to get merchants linked to a particular user
     */
    private void getMerchants() {
        final LoadingClass loadingClass = new LoadingClass(Select_ClientActivity.this);
        loadingClass.showLoading();
        String accessToken = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_access_token_identifier), null);
        RequestParams params = new RequestParams();
        params.put("user_id", getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_user_id_identifier), null));
        GetLinkedMerchants.getLinkedMerchants(accessToken, "merchant/suppliers/linked-merchants", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    int length=jsonArray.length();

                    for (int i = 0; i < length; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        MerchantClient merchantClient = new MerchantClient();
                        //merchantClient.setMerchantName(jsonObject.getString("company_name"));
                        merchantClient.setMerchantId(jsonObject.getInt("merchant_id"));
                        // merchantClient.setMerchantLogo(jsonObject.getString("company_logo"));
                        merchantClient.setChecked(false);
                        merchantClients.add(merchantClient);

                    }

                    if(jsonArray.length()>0) {
                        recyclerView.setAdapter(new SelectClientAdapter(Select_ClientActivity.this,merchantClients));
                    }
                    else
                    {
                        recyclerView.setVisibility(View.GONE);
                        btnContinue.setVisibility(View.GONE);
                        TextView tvNotFound=findViewById(R.id.tvNotFound);
                        tvNotFound.setVisibility(View.VISIBLE);
                    }

                    if (merchantClients.size() > 0) {
                        btnContinue.setEnabled(true);
                    }
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

                Toast.makeText(Select_ClientActivity.this, "We experienced an error while processing your request", Toast.LENGTH_LONG).show();
                if (loadingClass.isDialogShowing()) {
                    loadingClass.hideLoading();
                }

            }
        });
    }

}
