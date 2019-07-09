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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.speedcapitalltd.Activities.ManageClientActivity;
import com.example.speedcapitalltd.R;
import com.example.speedcapitalltd.models.MerchantClient;
import com.example.speedcapitalltd.utilities.GetMerchantApiService;
import com.example.speedcapitalltd.utilities.LoadingClass;
import com.example.speedcapitalltd.utilities.ShowNoNetworkError;
import com.example.speedcapitalltd.utilities.UnlinkMerchantApiService;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.MODE_PRIVATE;


public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ViewHolder> {

    //private static final String TAG = "ClientAdapter";

    private List<MerchantClient> merchantClients;

     private final Activity activity;

    private  int lastSelectedPosition=-1;
    private final  String data="data";


    public ClientAdapter(List<MerchantClient> merchantClients, Activity activity) {
        this.merchantClients = merchantClients;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_manage_client,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        getMerchant(viewHolder.imageName, viewHolder.image, viewHolder.getAdapterPosition(), merchantClients.get(viewHolder.getAdapterPosition()).getMerchantId());
        viewHolder.imageName.setText(merchantClients.get(i).getMerchantName());

       // viewHolder.image.setImageResource(merchantClients.get(i).getMerchantLogo());

       viewHolder.check.setChecked(lastSelectedPosition==i);
       if (viewHolder.check.isChecked())merchantClients.get(i) ;
            showUnlinkDialog(merchantClients.get(viewHolder.getAdapterPosition()).getRecordId(), merchantClients.get(viewHolder.getAdapterPosition()).getMerchantName());

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showUnlinkDialog(merchantClients.get(viewHolder.getAdapterPosition()).getRecordId(), merchantClients.get(viewHolder.getAdapterPosition()).getMerchantName());
                }
            });
    }
    @Override
    public int getItemCount() {
        return merchantClients.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircularImageView image;
        public TextView imageName;
        public CheckBox check;
        public RelativeLayout parentlayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.ClientPic);
            imageName = itemView.findViewById(R.id.ClientName);
             check=itemView.findViewById(R.id.checkBox);
            parentlayout = itemView.findViewById(R.id.parent);

        }
    }

    /**
     * method that sends a request to remove merchant attached to supplier
     * @param recordId
     * @param supplier_id
     * @param dialog
     */
    private void removeClient(int recordId, String supplier_id, final Dialog dialog) {
        final LoadingClass loadingClass = new LoadingClass(activity);
        loadingClass.showLoading();
        String accessToken = activity.getSharedPreferences(activity.getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(activity.getString(R.string.string_access_token_identifier), null);
        RequestParams params = new RequestParams();
        //params.put("user_id", activity.getSharedPreferences(activity.getString(R.string.string_authentication_identifier), MODE_PRIVATE).getString(activity.getString(R.string.string_user_id_identifier), null));
        params.put("id", recordId);
        params.put("merchant_supplier_code", supplier_id);

        UnlinkMerchantApiService.unlinkMerchant(accessToken, "merchant/suppliers/unlink", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("ADD_CLIENT",response.toString());
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    //Log.d("LINK_RES", response.toString());

                    processRemoveClient(response,loadingClass);
                }

                loadingClass.hideLoading();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                //ShowNoNetworkError errorFound = new ShowNoNetworkError(activity.getApplicationContext());

                if (errorResponse != null) {

                    try {
                        Toast.makeText(activity, errorResponse.getJSONObject(data).getString("message"), Toast.LENGTH_LONG).show();
                        if (loadingClass.isDialogShowing())
                        {
                            loadingClass.hideLoading();
                        }

                        if (dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                    } catch (Exception ex) {
                        if (loadingClass.isDialogShowing())
                        {
                            loadingClass.hideLoading();
                        }

                        if (dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                    }
                } else {

                    Toast.makeText(activity, throwable.toString(), Toast.LENGTH_LONG).show();
                }


            }
        });
    }
    /**
     * method requests server to remove a linked merchant from supplier's merchants
     * @param response
     * @param loadingClass
     */

    private void processRemoveClient(JSONObject response, LoadingClass loadingClass){
        try {
            Toast.makeText(activity, response.getJSONObject(data).getString("message"), Toast.LENGTH_LONG).show();

            activity.startActivity(new Intent(activity, ManageClientActivity.class));
         activity.finish();


            if (loadingClass.isDialogShowing()) {
                loadingClass.hideLoading();}


        } catch (Exception ex) {
            if (loadingClass.isDialogShowing()) {
                loadingClass.hideLoading();}
        }


    }

    /**
     * This shows the dialog that asks the user to provide supplier-merchant code to unlink supplier from merchant
     * @param recordId
     * @param clientName
     */

    private void showUnlinkDialog(final int recordId, final String clientName) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.unlink_dialog_layout);
        dialog.show();

        TextView btnCancel = dialog.findViewById(R.id.btnCancel);
        TextView btnSave = dialog.findViewById(R.id.btnSave);


        TextView tvClientName = dialog.findViewById(R.id.tvClientName);
        final EditText edtSupplierId = dialog.findViewById(R.id.edtSupplierId);


        edtSupplierId.setEnabled(false);
        edtSupplierId.setEnabled(true);
        tvClientName.setText("Unlink ".concat(clientName));

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                removeClient(recordId, edtSupplierId.getText().toString(), dialog);
                //Toast.makeText(activity, "Unlinked from " + clientName + " Successfully", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * method returns merchant details and updates UI
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
                processGetMerchant(textView,roundedImageView,position,response,loadingClass);
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
    /**
     * process results from get merchant method
     * @param textView
     * @param roundedImageView
     * @param position
     * @param response
     * @param loadingClass
     */

    private void processGetMerchant(TextView textView, ImageView roundedImageView, int position, JSONObject response, LoadingClass loadingClass){
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
                loadingClass.hideLoading();}
        } catch (Exception ex) {
            if (loadingClass.isDialogShowing()) {
                loadingClass.hideLoading();}
        }

    }
}
