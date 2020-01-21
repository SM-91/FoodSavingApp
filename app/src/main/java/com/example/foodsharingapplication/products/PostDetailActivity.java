package com.example.foodsharingapplication.products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.model.User;
import com.example.foodsharingapplication.model.UserUploadFoodModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class PostDetailActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    TextView pTitle, pDescription, pPrice,pTime,pType, pCuisineType, pAvailable,pPayment;
    ImageView pImage, pImage2;
    ViewFlipper pFlip;
    User foodPostedBy;
    String ad_id;
    String user_id;
    String title;
    LinearLayout linearLayout;

    Button chatBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        firebaseAuth = FirebaseAuth.getInstance();
       /* //Action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Post");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);*/


        //Flipper View
        pFlip = findViewById(R.id.viewFlipper);
        linearLayout = findViewById(R.id.btn_layout);


        pTitle = findViewById(R.id.titlePost);
        pDescription = findViewById(R.id.descPost);
        pImage = findViewById(R.id.imagePost);
        pImage2 = findViewById(R.id.imagePost2);
        pPrice = findViewById(R.id.pricePost);
        pTime = findViewById(R.id.timePost);
        pType = findViewById(R.id.typePost);
        pAvailable = findViewById(R.id.availability);
        pCuisineType = findViewById(R.id.cuisineTypePost);
        pPayment = findViewById(R.id.paymentPost);
        chatBtn = findViewById(R.id.chat_button);

        //Get Data from Card
        ad_id = getIntent().getStringExtra("ad_id");
        title = getIntent().getStringExtra("title");
        String desc = getIntent().getStringExtra("description");
        String image = getIntent().getStringExtra("image");
       // String image2 = getIntent().getStringExtra("image2");
        String price = getIntent().getStringExtra("price");
        String time = getIntent().getStringExtra("time");
        String type = getIntent().getStringExtra("type");
        String availablity = getIntent().getStringExtra("availability");
        String cuisineType = getIntent().getStringExtra("cuisineType");
        String payment = getIntent().getStringExtra("pay");
        foodPostedBy = getIntent().getParcelableExtra("foodPostedBy");

        //getUser();

        ArrayList<String> imageArray = getIntent().getStringArrayListExtra("imageArray");


        //Set Data in Views
        pTitle.setText(title);
        pDescription.setText(desc);
        pPrice.setText(price);
        pTime.setText(time);
        pType.setText(type);
        pAvailable.setText(availablity);
        pCuisineType.setText(cuisineType);
        pPayment.setText(payment);



        if(image==null) {

            for (int i = 0; i < imageArray.size(); i++) {

                ImageView imageV = new ImageView(this);
                //imageV.setLayoutParams(new android.view.ViewGroup.LayoutParams(80,60));
                //imageV.setMaxHeight(20);
                //imageV.setMaxWidth(20);


                // Adds the view to the layout
                pFlip.addView(imageV);

            }
        }
        else{
            Picasso.get().load(image).into(pImage);
        }
       // Picasso.get().load(image2).into(pImage2);


        Animation out = AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right);
        Animation in = AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left);


        pFlip.setAutoStart(true);
        pFlip.setInAnimation(in);
        pFlip.setOutAnimation(out);
        pFlip.setFlipInterval(3000);

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chat_intent = new Intent(PostDetailActivity.this, MessageActivity.class);
                chat_intent.putExtra("ad_id", ad_id);
                chat_intent.putExtra("foodTitle", title);
                chat_intent.putExtra("foodPostedBy",foodPostedBy);
                startActivity(chat_intent);
            }
        });

        DatabaseReference getUserReference;
        getUserReference = FirebaseDatabase.getInstance().getReference("Food")
                .child("FoodByAllUsers").child(ad_id);
        getUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_id = dataSnapshot.child("foodPostedBy").child("userId").getValue(String.class);
                if (firebaseAuth.getUid().equals(user_id)) {
                    linearLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getUser(){


    }

    // Handle Back press

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
