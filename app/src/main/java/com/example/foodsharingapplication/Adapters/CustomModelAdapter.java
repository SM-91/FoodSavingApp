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
import com.example.foodsharingapplication.model.CustomModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomModelAdapter extends RecyclerView.Adapter<CustomModelAdapter.ViewHolder> {

    private ArrayList<CustomModel> customModels;
    private View.OnClickListener listener;

    public CustomModelAdapter( ArrayList<CustomModel> customModels) {
        this.customModels = customModels;
    }

    @NonNull
    @Override
    public CustomModelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CustomModelAdapter.ViewHolder holder, int position) {
        CustomModel customModel = customModels.get(holder.getAdapterPosition());

        holder.username.setText("Sender Name :" + " " + customModel.getUser().getUserName());
        holder.productName.setText("Product Name :" + " " + customModel.getUserUploadFoodModel().getFoodTitle());

        if(customModel.getBidModel().isAccepted() == true){
            holder.isAccepted.setText("Request Status :" + " " + "Bid Accepted");
            holder.isAccepted.setVisibility(View.VISIBLE);
        } else {
            holder.isAccepted.setText("Request Status :" + " " + "Bid Not Accepted");
            holder.isAccepted.setVisibility(View.VISIBLE);
        }

        Picasso.get().load(customModel.getUser().getUserProfilePicUrl()).fit().centerCrop().into(holder.imageView);
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
