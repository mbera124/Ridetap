package com.example.ridetap.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ridetap.R;
import com.example.ridetap.model.Cars;
import com.example.ridetap.viewholder.AddcarViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AddcarAdapter extends RecyclerView.Adapter<AddcarViewHolder>implements Filterable {
    private List<Cars> carsList;
//    private List<Cars> carsListFiltered;
    private Context mContext;
    private String TAG = "CarsAdapter";
    public AddcarAdapter(Context context, List<Cars> carsList) {
        this.mContext = context;
        this.carsList = carsList;
//        this.carsListFiltered = carsListFiltered;


    }

    @NonNull
    @Override
    public AddcarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_cars_items, parent, false);
        return new AddcarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddcarViewHolder holder, int position) {

        Cars cars =carsList.get(position);
        holder.model.setText(cars.getModel());
        holder.location.setText(cars.getLocation());
        holder.price.setText(cars.getPrice());
        holder.contact.setText(cars.getContact());
//     StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images");
        Picasso.get().load(cars.getImageURL()).placeholder(R.drawable.background).into(holder.imgcar);
//Glide.with(mContext).load(pigs.getImageURL()).into(holder.imgpig);
        holder.cvcarsitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick: clicked on: " + carsList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return carsList==null?0:carsList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    carsList = carsList;
                } else {
                    List<Cars> filteredList = new ArrayList<>();
                    for (Cars row : carsList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getLocation().toLowerCase().contains(charString.toLowerCase()) || row.getContact().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    carsList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = carsList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                carsList = (ArrayList<Cars>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface CarsAdapterListener {
        void onCarsSelected(Cars cars);
    }
}
