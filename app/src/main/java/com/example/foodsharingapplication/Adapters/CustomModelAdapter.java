package com.example.foodsharingapplication.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.model.BidModel;
import com.example.foodsharingapplication.model.CustomModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomModelAdapter extends RecyclerView.Adapter<CustomModelAdapter.ViewHolder> {

    private ArrayList<BidModel> customModels;
    private View.OnClickListener listener;

    public CustomModelAdapter(ArrayList<BidModel> customModels) {
        this.customModels = customModels;
    }

    @NonNull
    @Override
    public CustomModelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomModelAdapter.ViewHolder holder, int position) {
        BidModel customModel = customModels.get(holder.getAdapterPosition());

        holder.username.setText("Sender Name :" + " " + customModel.getSender().getUserName());
        holder.productName.setText("Product Name :" + " " + customModel.getName());

        if (customModel.isAccepted()) {
            holder.isAccepted.setText("Request Status :" + " " + "Bid Accepted");
            holder.isAccepted.setVisibility(View.VISIBLE);
        } else {
            holder.isAccepted.setText("Request Status :" + " " + "Bid Not Accepted");
            holder.isAccepted.setVisibility(View.VISIBLE);
        }

        if (customModel.getSender().getUserProfilePicUrl() != null)
            Picasso.get().load(customModel.getSender().getUserProfilePicUrl()).fit().centerCrop().into(holder.imageView);
        holder.itemView.setTag(customModel);
        holder.itemView.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return customModels != null ? customModels.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public TextView productName;
        public TextView isAccepted;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            productName = itemView.findViewById(R.id.product_title);
            isAccepted = itemView.findViewById(R.id.isAccepted);
            imageView = itemView.findViewById(R.id.ad_image);
        }
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}
