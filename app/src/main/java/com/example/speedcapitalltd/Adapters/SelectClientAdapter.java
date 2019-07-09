package com.example.speedcapitalltd.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.speedcapitalltd.Activities.Select_ClientActivity;
import com.example.speedcapitalltd.Configurations.ApiConstants;
import com.example.speedcapitalltd.R;
import com.example.speedcapitalltd.models.MerchantClient;
import com.example.speedcapitalltd.models.SelectClientModel;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;



public class SelectClientAdapter extends RecyclerView.Adapter<SelectClientAdapter.ViewHolder> {

    private List<SelectClientModel> list;

    private Context context;
    public static int image;
    public static String name;

    private  int lastSelectedPosition=-1;

    public SelectClientAdapter(Select_ClientActivity list, List<MerchantClient> context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_select_client,viewGroup,false);
        SelectClientAdapter.ViewHolder holder = new SelectClientAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        viewHolder.imageName.setText(list.get(i).getClientName());

        viewHolder.image.setImageResource(list.get(i).getImage());
         viewHolder.checked.setChecked(lastSelectedPosition==i);

         if (viewHolder.checked.isChecked()) {
             ApiConstants.toolbar_header =list.get(i).getClientName();
             image = list.get(i).getImage();
             name=list.get(i).getClientName();
         }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircularImageView image;
        public TextView imageName;
        public RadioButton checked;
        public RelativeLayout parentlayout;


        public ViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.ClientPic);
            imageName = itemView.findViewById(R.id.tvClientName);
              checked=itemView.findViewById(R.id.radio);
            parentlayout = itemView.findViewById(R.id.parent);

            checked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition =getAdapterPosition();
                    notifyDataSetChanged();

                }
            });
            
        }
    }
}
