package com.example.speedcapitalltd.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.speedcapitalltd.Activities.ReceiptActivity;
import com.example.speedcapitalltd.R;
import com.example.speedcapitalltd.models.ValidInvoice;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.InvoiceViewHolder> {

    private final Activity activity;
    private final List<ValidInvoice> invoiceList;
    private int source=0;

    /**
     * Instantiates a new Invoice adapter.
     *
     * @param activity    the activity
     * @param invoiceList the invoice list
     */
    public InvoiceAdapter(Activity activity, List<ValidInvoice> invoiceList) {
        this.activity = activity;
        this.invoiceList = invoiceList;
    }

    /**
     * Instantiates a new Invoice adapter.
     *
     * @param activity    the activity
     * @param invoiceList the invoice list
     * @param source      the source
     */
    public InvoiceAdapter(Activity activity, List<ValidInvoice> invoiceList,int source) {
        this(activity,invoiceList);
        this.source=source;
    }
    @NonNull
    @Override
    public InvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        InvoiceViewHolder viewHolder=null;
        if(source==0) {
            viewHolder= new InvoiceViewHolder(LayoutInflater.from(parent.getContext()).inflate
                    (R.layout.invoice_activity_layout,parent,false));
        }
        else
        {
            viewHolder= new InvoiceViewHolder(LayoutInflater.from(parent.getContext()).inflate
                    (R.layout.invoice_status_layout,parent,false));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final InvoiceViewHolder holder, int position) {

        holder.tvInvoiceDate.setText(invoiceList.get(holder.getAdapterPosition()).getInvoiceDate());
        holder.tvInvoiceDescription.setText(invoiceList.get(holder.getAdapterPosition()).getMechSupCode());
        holder.tvLpoNumber.setText(invoiceList.get(holder.getAdapterPosition()).getlPONumber());
        holder.tvClientName.setText(invoiceList.get(holder.getAdapterPosition()).getMaturityDate());
        holder.tvInvoiceAmount.setText(activity.getString(R.string.currency).concat(" ").concat(invoiceList.get(holder.getAdapterPosition()).getAmount()));

        if(invoiceList.get(holder.getAdapterPosition()).getMerchantLogo()!=null)
        {
            Glide.with(activity)
                    .asBitmap()
                    .load(invoiceList.get(holder.getAdapterPosition()).getMerchantLogo())
                    .into(holder.ivClientPic);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, ReceiptActivity.class);
                intent.putExtra("record_id",invoiceList.get(holder.getAdapterPosition()).getRecordId());
                activity.startActivity(intent);

            }
        });

    }
    @Override
    public int getItemCount() {
        return (invoiceList!=null ?invoiceList.size():0);
    }

    /**
     * The type Invoice view holder.
     */
    @SuppressWarnings("CanBeFinal")
    static class InvoiceViewHolder extends RecyclerView.ViewHolder{
        /**
         * The Iv client pic.
         */
        CircularImageView ivClientPic;
        /**
         * The  client name.
         */
        TextView tvClientName;

        /**
         * The  invoice amount.
         */
        TextView tvInvoiceAmount;
        /**
         * The  invoice description.
         */
        TextView tvInvoiceDescription;
        /**
         * The  invoice date.
         */
        TextView tvInvoiceDate;

        RelativeLayout parent;

        TextView tvLpoNumber;

        /**
         * Instantiates a new Invoice view holder.
         *
         * @param itemView the item view
         */
        InvoiceViewHolder(View itemView) {
            super(itemView);
            parent= itemView.findViewById(R.id.parent);
            ivClientPic=itemView.findViewById(R.id.ivClientPic);
            tvClientName=itemView.findViewById(R.id.tvClientName);
            tvInvoiceAmount=itemView.findViewById(R.id.tvInvoiceAmount);
            tvInvoiceDescription=itemView.findViewById(R.id.tvInvoiceDescription);
            tvInvoiceDate=itemView.findViewById(R.id.tvInvoiceDate);
        }
    }

}