package com.example.speedcapitalltd.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.speedcapitalltd.R;
import com.example.speedcapitalltd.models.FragmentAllModel;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class FragmentAllAdapter extends RecyclerView.Adapter<FragmentAllAdapter.ViewHolder> {

    private List<FragmentAllModel> list;

    private Context context;

    public FragmentAllAdapter(List<FragmentAllModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_invoice_all,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {


        viewHolder.imageName.setText(list.get(i).getClientName());

        viewHolder.image.setImageResource(list.get(i).getImage());
        viewHolder.payment.setText(list.get(i).getPayment());
        viewHolder.amount.setText(list.get(i).getAmount());
        viewHolder.date.setText(list.get(i).getDate());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircularImageView image;
        public TextView imageName;
        public  TextView payment;
      public  TextView amount;
      public  TextView date;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.ivClientPic);
            imageName = itemView.findViewById(R.id.tvClientName);
            payment=itemView.findViewById(R.id.paystatus);
            amount=itemView.findViewById(R.id.tvInvoiceAmount);
            date=itemView.findViewById(R.id.tvInvoiceDate);

        }
    }
}
