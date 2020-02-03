package com.example.foodsharingapplication.Adapters;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.model.User;
import com.example.foodsharingapplication.model.UserUploadFoodModel;
import com.example.foodsharingapplication.Fragments.ProductGridView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NearestAdapter extends RecyclerView.Adapter<NearestAdapter.ViewHolder> {

    private List<UserUploadFoodModel> userlist;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private FirebaseAuth firebaseAuth;


    private Context context;

    private ArrayList<String> imageList;

    // data is passed into the constructor
    public NearestAdapter(Context context, List<UserUploadFoodModel> userlist) {
        this.mInflater = LayoutInflater.from(context);
        this.userlist = userlist;
    }

    // inflates the itemlist layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_grid, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        UserUploadFoodModel list = userlist.get(position);

        holder.title.setText(list.getFoodTitle());
        holder.price.setText(list.getFoodPrice());
        holder.time.setText(list.getFoodPickUpDetail());
        holder.profImage.setVisibility(View.GONE);

        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ad_id = getItem(position).getAdId();
                String myTitle = getItem(position).getFoodTitle();
                String myDesc = getItem(position).getFoodDescription();
                String myImage = getItem(position).getmImageUri();
                String myPrice = getItem(position).getFoodPrice();
                String myTime = getItem(position).getFoodPickUpDetail();
                String myType = getItem(position).getFoodType();
                String myCuisineType = getItem(position).getFoodTypeCuisine();
                String pay = getItem(position).getPayment();
                User foodPostedBy = getItem(position).getFoodPostedBy();
                String available = getItem(position).getAvailabilityDays();
                String mImageUri = getItem(position).getmImageUri();

                ArrayList<String> imageArray = getItem(position).getmArrayString();

                ProductGridView.getInstance().favourites_check(ad_id, myTitle, myDesc, myImage, myPrice, myTime, myType, myCuisineType, pay,
                        foodPostedBy, available, mImageUri, imageArray, v);
            }
        });
        try {
            if (list.getmImageUri() != null) {

                Picasso.get().load(list.getmImageUri())
                        .into(holder.image);

            } else {

                imageList = list.getmArrayString();
                if (imageList != null) {
                    Uri uri = Uri.parse(imageList.get(0));
                    Picasso.get().load(uri)
                            .into(holder.image);
                }
            }

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            //Toast.makeText(this.context,"Error in multiple images" + e.getMessage(),Toast.LENGTH_SHORT).show();
        }


    }

    // total number of rows
    @Override
    public int getItemCount() {
        return userlist.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView price, title, time;
        ImageView image, profImage;
        ImageButton fav;
        Uri uri;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textTitle);
            time = itemView.findViewById(R.id.textTime);
            price = itemView.findViewById(R.id.textPrice);
            image = itemView.findViewById(R.id.imageView1);
            fav = itemView.findViewById(R.id.fav);
            profImage = itemView.findViewById(R.id.imageView2);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onNearestItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public UserUploadFoodModel getItem(int id) {
        return userlist.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onNearestItemClick(View view, int position);
    }
}