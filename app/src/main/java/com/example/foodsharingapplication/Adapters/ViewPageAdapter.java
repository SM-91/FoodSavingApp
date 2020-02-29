package com.example.foodsharingapplication.Adapters;


import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewPageAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<Uri> imageUrls;
    private String[] images;

    private boolean isUri = true;

    public ViewPageAdapter(Context context, ArrayList<Uri> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    public ViewPageAdapter(Context context, String[] images) {
        this.context = context;
        this.images = images;

        this.isUri = false;
    }

    @Override
    public int getCount() {
        return !isUri? images.length:imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
       // Uri uri = imageUrls.get(position);
        ImageView imageView = new ImageView(context);

        if(isUri) {
            Picasso.get()
                    .load(imageUrls.get(position))
                    .fit()
                    .centerCrop()
                    .into(imageView);
        } else {
            Picasso.get()
                    .load(images[position])
                    .fit()
                    .centerCrop()
                    .into(imageView);
        }


//        try {
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
//            imageView.setImageBitmap(bitmap);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        container.addView(imageView);

        return imageView;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

}
