package com.example.speedcapitalltd.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.speedcapitalltd.Adapters.InvoiceAdapter;
import com.example.speedcapitalltd.R;
import com.example.speedcapitalltd.models.FragmentAllModel;
import com.example.speedcapitalltd.models.ValidInvoice;
import com.example.speedcapitalltd.utilities.GetInvoiceApplicationApiService;
import com.example.speedcapitalltd.utilities.LoadingClass;
import com.example.speedcapitalltd.utilities.ShowNoNetworkError;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.MODE_PRIVATE;

public class FragmentAll extends Fragment {
  private   View v;
  private RecyclerView myrecyclerview;
  private List<FragmentAllModel> lstAll;
    /**
     * The show when no records are found.
     */
    private TextView tvNotFound;

    public FragmentAll() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.all_fragnment, container, false);
        myrecyclerview=v.findViewById(R.id.rcvInvoiceAll);
        tvNotFound=v.findViewById(R.id.tvNotFound);
       // FragmentAllAdapter adapter= new FragmentAllAdapter(lstAll, getContext());
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        getApplications();
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (myrecyclerview != null){
            myrecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        }


//        lstAll = new ArrayList<>();
//        lstAll.add(new FragmentAllModel(R.drawable.tuskys, "Fully Paid", "Tuskys", "-100,000.00Ksh", "20 Nov"));
//        lstAll.add(new FragmentAllModel(R.drawable.niaivas_super, "Fully Paid", "Naivas", "-50,000.00Ksh", "20 Nov"));
//        lstAll.add(new FragmentAllModel(R.drawable.cleanshelf, "Pending Paymennts", "CleanShelf", "-45,700.00Ksh", "20 Nov"));
//        lstAll.add(new FragmentAllModel(R.drawable.tuskys, "Pending Paymennts", "Tuskys", "-100,000.00Ksh", "20 Nov"));
//        lstAll.add(new FragmentAllModel(R.drawable.tuskys, "Pending Paymennts", "Tuskys", "-100,000.00Ksh", "20 Nov"));
//        lstAll.add(new FragmentAllModel(R.drawable.niaivas_super, "Fully Paid", "Naivas", "-50,000.00Ksh", "20 Nov"));
//        lstAll.add(new FragmentAllModel(R.drawable.cleanshelf, "Fully Paid", "Cleanshelf", "-45,700.00Ksh", "20 Nov"));
//        lstAll.add(new FragmentAllModel(R.drawable.tuskys, "Fully Paid", "Tuskys", "-100,000.00Ksh", "20 Nov"));
    }

    /**
     * Gets applications for this user and their statuses and shows them on the fragment.
     */
    @SuppressWarnings("ConstantConditions")
    private void getApplications() {
        final LoadingClass loadingClass = new LoadingClass(getActivity());
        loadingClass.showLoading();
        //noinspection ConstantConditions
        String accessToken = getActivity().getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_access_token_identifier), null);
        String userId = getActivity().getSharedPreferences(getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(getString(R.string.string_user_id_identifier), null);
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        GetInvoiceApplicationApiService.GetInvoiceApplication(accessToken, "merchant/applications/applications-list", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    List<ValidInvoice> invoices = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("data");
                    int length= jsonArray.length();
                    for (int k = 0; k <length; k++) {
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
                    if(jsonArray.length()>0) {
                        myrecyclerview.setAdapter(new InvoiceAdapter(getActivity(), invoices, 1));}
                    else
                    {
                        tvNotFound.setVisibility(View.VISIBLE);
                        myrecyclerview.setVisibility(View.GONE);
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
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                new ShowNoNetworkError(getContext());

                if (loadingClass.isDialogShowing()) {
                    loadingClass.hideLoading();

                }
            }
        });
    }
}
