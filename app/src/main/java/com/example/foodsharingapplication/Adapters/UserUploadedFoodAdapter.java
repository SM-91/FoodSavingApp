package com.example.foodsharingapplication.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsharingapplication.HomeActivity;
import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.model.UserUploadFoodModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserUploadedFoodAdapter extends RecyclerView.Adapter<UserUploadedFoodAdapter.ViewHolder> {

    private List<UserUploadFoodModel> userUploadFoodModelList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    private Context context;
    private ArrayList<String> imageList = null;

    // data is passed into the constructor
    public UserUploadedFoodAdapter(Context context, List<UserUploadFoodModel> userUploadFoodModelList) {
        this.mInflater = LayoutInflater.from(context);
        this.userUploadFoodModelList = userUploadFoodModelList;
    }


    @NonNull
    @Override
    public UserUploadedFoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.user_uploaded_food_adapter, parent, false);
        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull UserUploadedFoodAdapter.ViewHolder holder, int position) {

        UserUploadFoodModel userUploadFoodModel = userUploadFoodModelList.get(position);
        holder.food_title.setText(userUploadFoodModel.getFoodTitle());
        holder.food_type.setText("Food Type: " + userUploadFoodModel.getFoodType());
        holder.food_price.setText("Food Price: " + userUploadFoodModel.getFoodPrice());

        try {
            if (userUploadFoodModel.getmImageUri() != null) {
                Picasso.get().load(userUploadFoodModel.getmImageUri())
                        .fit()
                        .centerCrop()
                        .into(holder.imageView);

            } else {

                imageList = userUploadFoodModel.getmArrayString();
                if (imageList != null) {
                    Uri uri = Uri.parse(imageList.get(0));
                    Picasso.get().load(uri)
                            .fit()
                            .centerCrop()
                            .into(holder.imageView);
                }
            }

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            //Toast.makeText(this.context,"Error in multiple images" + e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return userUploadFoodModelList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView food_title;
        TextView food_type;
        TextView food_price;
        ImageView imageView;
        ArrayList<String> multipleImagesList;
        Uri uri;

        ViewHolder(View itemView) {
            super(itemView);
            food_title = itemView.findViewById(R.id.ad_title);
            food_type = itemView.findViewById(R.id.ad_type);
            food_price = itemView.findViewById(R.id.ad_estimation);
            imageView = itemView.findViewById(R.id.ad_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public UserUploadFoodModel getItem(int id) {
        return userUploadFoodModelList.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
