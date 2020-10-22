package com.example.ridetap.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ridetap.ItemClickListener;
import com.example.ridetap.R;

public class AddcarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView contact,price,pricetxt,contacttxt,model,modeltxt,location,locationtxt;
    public ImageView imgcar;
    public CardView cvcarsitem;
    private ItemClickListener itemClickListener;

    public AddcarViewHolder(@NonNull View itemView) {
        super(itemView);
        cvcarsitem=itemView.findViewById(R.id.cvcarsitem);
        price=itemView.findViewById(R.id.price);
        model=itemView.findViewById(R.id.model);
        modeltxt=itemView.findViewById(R.id.modeltxt);
        location=itemView.findViewById(R.id.location);
        locationtxt=itemView.findViewById(R.id.locationtxt);
        pricetxt=itemView.findViewById(R.id.pricetxt);
        contacttxt=itemView.findViewById(R.id.contacttxt);
        contact=itemView.findViewById(R.id.contact);
        imgcar=itemView.findViewById(R.id.imgcar);
    }
    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }


    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
