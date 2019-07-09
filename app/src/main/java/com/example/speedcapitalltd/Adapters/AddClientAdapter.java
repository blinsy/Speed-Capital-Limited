package com.example.speedcapitalltd.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.speedcapitalltd.Activities.AddClientActivity;
import com.example.speedcapitalltd.Activities.MainActivity;
import com.example.speedcapitalltd.R;
import com.example.speedcapitalltd.models.MerchantClient;
import com.example.speedcapitalltd.utilities.AddMerchantClient;
import com.example.speedcapitalltd.utilities.GetMerchantApiService;
import com.example.speedcapitalltd.utilities.LoadingClass;
import com.example.speedcapitalltd.utilities.ShowNoNetworkError;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.MODE_PRIVATE;

/**
 * The type Add client adapter.
 * The adapter populates recyclerview with merchants who are not linked to the supplier and provides support
 * for the supplier to be able to add merchant
 */
public class AddClientAdapter extends RecyclerView.Adapter<AddClientAdapter.AddClientViewHolder> {

    private final List<MerchantClient> merchantClients;
    private final Activity activity;
    private final int mode;
    private  final String data ="data";

    /**
     * Instantiates a new Add client adapter.
     *
     * @param activity        the activity
     * @param merchantClients the merchant clients
     * @param mode            the mode
     */
    public AddClientAdapter(Activity activity, List<MerchantClient> merchantClients, int mode) {
        this.merchantClients = merchantClients;
        this.activity = activity;
        this.mode = mode;
    }

    @NonNull
    @Override
    public AddClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new AddClientViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_client, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final AddClientViewHolder holder, int position) {

        getMerchant(holder.tvClientName, holder.ivClientLogo, holder.getAdapterPosition(), merchantClients.get(holder.getAdapterPosition()).getMerchantId());


        holder.radioSelect.setChecked(merchantClients.get(position).isChecked());
        holder.radioSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {


                    for (MerchantClient merchantClient : merchantClients) {
                        merchantClient.setChecked(false);
                    }
                    merchantClients.get(holder.getAdapterPosition()).setChecked(true);
                    showLinkDialog(merchantClients.get(holder.getAdapterPosition()).getMerchantName(), merchantClients.get(holder.getAdapterPosition()).getMerchantId() + "");

                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return merchantClients.size();
    }

    /**
     * The type Add client view holder.
     * This instantiates the viewholder to be used by the adapter and the associated views
     */
    class AddClientViewHolder extends RecyclerView.ViewHolder {
        /**
         * The Radio select.
         */
        final RadioButton radioSelect;
        /**
         * The Tv client name.
         */
        final TextView tvClientName;
        /**
         * The  client logo.
         */
        final ImageView ivClientLogo;

        /**
         * Instantiates a new ivClientPicAdd client view holder.
         *
         * @param itemView the item view
         */
        AddClientViewHolder(View itemView) {
            super(itemView);
            radioSelect = itemView.findViewById(R.id.radio);
            tvClientName = itemView.findViewById(R.id.tvClientName);
            ivClientLogo = itemView.findViewById(R.id.ivClientPic);

            radioSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyDataSetChanged();
                }
            });
        }
    }

    /**
     * method displays dialog requesting user to provide merchant-supplier code
     * @param clientName
     * @param clientId
     */
    private void showLinkDialog(final String clientName, final String clientId) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.link_dialog_layout);
        dialog.show();

        TextView btnCancel = dialog.findViewById(R.id.btnCancel);
        TextView btnSave = dialog.findViewById(R.id.btnSave);


        TextView tvClientName = dialog.findViewById(R.id.tvClientName);
        tvClientName.setText("Link to ".concat(clientName));
        // tvClientName.setText(clientName);
        final EditText edtSupplierId = dialog.findViewById(R.id.edtSupplierId);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtSupplierId.getText().toString().trim().length() > 0) {
                    //dialog.dismiss();
                    addClient(clientId, edtSupplierId.getText().toString().trim(), dialog);

                    //Toast.makeText(activity, "linked to " + clientName + " Successfully", Toast.LENGTH_LONG).show();
                } else {
                    edtSupplierId.setError("Provide supplier id");
                    edtSupplierId.requestFocus();
                }
            }
        });
    }
    /**
     * method links merchant to suppier
     * @param merchantId
     * @param supplier_id
     * @param dialog
     */

    private void addClient(String merchantId, String supplier_id, final Dialog dialog) {
        final LoadingClass loadingClass = new LoadingClass(activity);
        loadingClass.showLoading();
        String accessToken = activity.getSharedPreferences(activity.getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(activity.getString(R.string.string_access_token_identifier), null);
        RequestParams params = new RequestParams();
        params.put("user_id", activity.getSharedPreferences(activity.getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(activity.getString(R.string.string_user_id_identifier), null));
        params.put("merchant_id", merchantId);
        params.put("merch_supplier_code", supplier_id);

        AddMerchantClient.addClient(accessToken, "merchant/suppliers/link", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("ADD_CLIENT",response.toString());
                if(dialog.isShowing()) {
                    // Log.d("LINK_RES", response.toString());
                    processAddClient(response,loadingClass);
                }
                dialog.dismiss();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                //ShowNoNetworkError errorFound = new ShowNoNetworkError(activity.getApplicationContext());

                if (errorResponse != null) {

                    try {
                        if (loadingClass.isDialogShowing()) {
                            loadingClass.hideLoading();
                        }
                        Toast.makeText(activity, errorResponse.getJSONObject(data).getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception ex) {
                        if (loadingClass.isDialogShowing()) {
                            loadingClass.hideLoading();
                        }


                    }
                } else {
                    if (loadingClass.isDialogShowing()) {
                        loadingClass.hideLoading();
                    }
                    Toast.makeText(activity, throwable.toString(), Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    /**
     * request server to add a merchant to a supplier's linked merchants
     * @param response
     * @param loadingClass
     */

    private void processAddClient(JSONObject response, LoadingClass loadingClass){
        if (loadingClass.isDialogShowing()) {
            loadingClass.hideLoading();
        }
        try {
            Toast.makeText(activity, response.getJSONObject(data).getString("message"), Toast.LENGTH_LONG).show();
            if (mode == 1) {
                Intent intent = new Intent(activity, MainActivity.class);
                intent.putExtra("mode", mode);
                activity.startActivity(intent);



            }
            else
            {
                activity.startActivity(new Intent(activity, AddClientActivity.class));

                activity.finish();
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
     * method will get merchant and update ui
     * @param textView
     * @param roundedImageView
     * @param position
     * @param id
     */
    private void getMerchant(final TextView textView, final ImageView roundedImageView, final int position, int id) {
        final LoadingClass loadingClass = new LoadingClass(activity);
        loadingClass.showLoading();
        String accessToken = activity.getSharedPreferences(activity.getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(activity.getString(R.string.string_access_token_identifier), null);
        RequestParams params = new RequestParams();

        params.put("id", id);
        GetMerchantApiService.getMerchant(accessToken, "merchant/merchant/view-one", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                loadingClass.hideLoading();
                try {
                    String company_name = response.getJSONObject(data).getString("company_name");
                    String company_logo = response.getJSONObject(data).getString("company_logo");
                    merchantClients.get(position).setMerchantLogo(company_logo);
                    merchantClients.get(position).setMerchantName(company_name);

                    textView.setText(company_name);
                    if (company_logo != null) {
                        Glide.with(activity)
                                .asBitmap()
                                .load(company_logo)
                                .into(roundedImageView);
                    }
                    if (loadingClass.isDialogShowing()) {
                        loadingClass.hideLoading();
                    }
                } catch (Exception ex) {
                    if (loadingClass.isDialogShowing()) {
                        loadingClass.hideLoading();
                    }

                }
                if (loadingClass.isDialogShowing()) {
                    loadingClass.hideLoading();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                new ShowNoNetworkError(activity.getApplicationContext());
                if (loadingClass.isDialogShowing()) {
                    loadingClass.hideLoading();
                }
            }
        });
    }
}
