package com.example.speedcapitalltd.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.speedcapitalltd.Adapters.ClientAdapter;
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

public class ManageClientActivity extends AppCompatActivity {

//    private static final String TAG = "ManageClientActivity";
//    private ArrayList<Boolean> mCheckbox = new ArrayList<>();
//    private ArrayList<String> mNames = new ArrayList<>();
//    private ArrayList<Integer> mImages= new ArrayList<>();

    private RecyclerView rcvManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_client);

        getSupportActionBar().hide();

        rcvManage=findViewById(R.id.rcvClientTomanage);
//        ImageView imageView= findViewById(R.id.ClientPic);
//        TextView textView= findViewById(R.id.ClientName);
//        CheckBox checkBox= findViewById(R.id.checkBox);
//


        rcvManage.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        getMerchants();
//        List<ManageClient> myList=new ArrayList<>();
//
//        ManageClient v1= new ManageClient(R.drawable.cleanshelf,"1","CleanSelf Supermarket",true);
//        ManageClient v2= new ManageClient(R.drawable.naivas,"2","Naivas Supermarket",false);
//        ManageClient v3= new ManageClient(R.drawable.nakumatt,"3","nakumatt Supermarket",false);
//        ManageClient v4= new ManageClient(R.drawable.uchumi,"5","Uchumi Supermarket",false);
//        ManageClient v5= new ManageClient(R.drawable.ukwala,"6","Ukwala Supermarket",false);
//        ManageClient v6= new ManageClient(R.drawable.tuskys,"7","Tuskys Supermarket",false);
//        ManageClient v7= new ManageClient(R.drawable.ukwala,"8","Ukwala  Supermarket",false);
//        ManageClient v8= new ManageClient(R.drawable.cleanshelf,"9","CleanSelf Supermarket",false);
//
//     myList.add(v1);
//        myList.add(v2);
//        myList.add(v3);
//        myList.add(v4);
//        myList.add(v5);
//        myList.add(v6);
//        myList.add(v7);
//        myList.add(v8);
//
//        Log.d("list size ",String.valueOf(myList.size()));
//        ClientAdapter adapter = new ClientAdapter(myList,getApplicationContext());
  //     rcvManage.setAdapter(adapter);



                    }

                    public void back(View view) {
                        Intent intent= new Intent(ManageClientActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                    @Override
                    public boolean onOptionsItemSelected(MenuItem item) {
                        if (item.getItemId() == android.R.id.home) {
                            finish();
                           // Bungee.shrink(ManageClientActivity.this);
                        }

                        if (item.getItemId() == R.id.add_item) {
                            startActivity(new Intent(ManageClientActivity.this, AddClientActivity.class));
                       //     Bungee.slideLeft(ManageClientActivity.this);
                            finish();
                        }
                        return super.onOptionsItemSelected(item);
                    }
                    /**
                     * The method gets merchants linked to a particular user
                     */

                    private void getMerchants() {

                        final LoadingClass loadingClass = new LoadingClass(ManageClientActivity.this);
                        loadingClass.showLoading();
                        String accessToken = getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_access_token_identifier), null);
                        RequestParams params = new RequestParams();
                        params.put("user_id", getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_user_id_identifier), null));
                        GetLinkedMerchants.getLinkedMerchants(accessToken, "merchant/suppliers/linked-merchants", params, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);

                                List<MerchantClient> merchantClients = new ArrayList<>();

                                try {
                                    merchantClients.clear();
                                    JSONArray jsonArray = response.getJSONArray("data");

                                    int length=jsonArray.length();
                                    for (int i = 0; i <length; i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        MerchantClient merchantClient = new MerchantClient();
                                        merchantClient.setRecordId(jsonObject.getInt("id"));
                                        merchantClient.setMerchantId(jsonObject.getInt("merchant_id"));
                                        merchantClient.setChecked(false);
                                        merchantClients.add(merchantClient);

                                    }

                                    if(jsonArray.length()>0) {
                                        rcvManage.setAdapter(new ClientAdapter(merchantClients, ManageClientActivity.this));
                    }else
                    {
                        rcvManage.setVisibility(View.GONE);
                        TextView tvNotFound=findViewById(R.id.tvNotFound);
                        tvNotFound.setVisibility(View.VISIBLE);
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

                Toast.makeText(ManageClientActivity.this, "We experienced an error while processing your request", Toast.LENGTH_LONG).show();
                if (loadingClass.isDialogShowing()) {
                    loadingClass.hideLoading();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_client_menu, menu);
        return true;
    }
}
