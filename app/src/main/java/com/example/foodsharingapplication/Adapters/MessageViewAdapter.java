package com.example.foodsharingapplication.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.model.CustomModel;
import com.example.foodsharingapplication.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MessageViewAdapter extends RecyclerView.Adapter<MessageViewAdapter.ViewHolder> {

    private Context mContext;
    private List<User> users;
    private ArrayList<CustomModel> customModelArrayList;
    private View.OnClickListener listener;

    public MessageViewAdapter(Context mContext, ArrayList<CustomModel> customModelArrayList) {
        this.mContext = mContext;
        this.customModelArrayList = customModelArrayList;
    }

    @NonNull
    @Override
    public MessageViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new MessageViewAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MessageViewAdapter.ViewHolder holder, int position) {

        CustomModel customModel = customModelArrayList.get(holder.getAdapterPosition());
        holder.username.setText("User Name :" + " " + customModel.getUser().getUserName());
        holder.productName.setText("Product Title :" + " " + customModel.getUserUploadFoodModel().getFoodTitle());
        Picasso.get().load(customModel.getUser().getUserProfilePicUrl()).fit().centerCrop().into(holder.imageView);
        holder.itemView.setTag(customModel);
        holder.itemView.setOnClickListener(listener);

    }

    @Override
    public int getItemCount(){
        return customModelArrayList != null ? customModelArrayList.size() : 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public TextView productName;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            productName = itemView.findViewById(R.id.product_title);
            imageView = itemView.findViewById(R.id.ad_image);
        }
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}
