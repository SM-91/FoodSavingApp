package com.example.foodsharingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodsharingapplication.Adapters.ViewPageAdapter;
import com.example.foodsharingapplication.model.User;
import com.example.foodsharingapplication.extras.UploadData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserUploadedFoodDetails extends AppCompatActivity {


    FirebaseAuth firebaseAuth;

    TextView text_food_title;
    TextView text_food_desc;
    TextView text_food_type;
    TextView text_food_price;
    TextView text_food_cuisine_type;
    TextView text_food_pickUpDetails;
    TextView text_food_availablity;

    Button btnUpdate, btnDel;

    ImageView imageView;
    ViewPager viewPager;
    ViewPageAdapter adapter = null;

    RelativeLayout myRelativeLayout;


    private ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
    private ArrayList<String> multipleImagesList = new ArrayList<String>();

    UploadData uploadData;

    private String user_id = " ";
    private String all_products_del_key = " ";
    private User foodPostedBy;
    private String FoodAdID = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_uploaded_food_details);

        firebaseAuth = FirebaseAuth.getInstance();
        uploadData = new UploadData();
        //updateProductFragment = new UpdateProductFragment();

        text_food_title = findViewById(R.id.titlePost);
        text_food_desc = findViewById(R.id.descPost);
        text_food_availablity = findViewById(R.id.availability);
        text_food_price = findViewById(R.id.pricePost);
        text_food_type = findViewById(R.id.typePost);
        text_food_cuisine_type = findViewById(R.id.cuisineTypePost);
        text_food_pickUpDetails = findViewById(R.id.timePost);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnDel = findViewById(R.id.btnDelete);

        imageView = findViewById(R.id.myAdsDetailsImageView);
        /*ViewPager*/
        viewPager = findViewById(R.id.myAdsDetailsViewPager);
        adapter = new ViewPageAdapter(this, mArrayUri);
        viewPager.getOffscreenPageLimit();
        viewPager.setAdapter(adapter);

        Bundle bundle = getIntent().getExtras();

        String myCurrentDataTime = getIntent().getStringExtra("myCurrentDateTime");
        FoodAdID = getIntent().getStringExtra("FoodAdID");
        String FoodTitle = getIntent().getStringExtra("FoodTitle");
        String FoodType = getIntent().getStringExtra("FoodType");
        String FoodCuisineType = getIntent().getStringExtra("FoodCuisineType");
        String FoodDescription = getIntent().getStringExtra("FoodDescription");
        String FoodPrice = getIntent().getStringExtra("FoodPrice");
        String FoodPickUpDetail = getIntent().getStringExtra("FoodPickUpDetail");
        String FoodAvailability = getIntent().getStringExtra("FoodAvailability");
        String FoodPayment = getIntent().getStringExtra("FoodPayment");
        String FoodPostedBy = getIntent().getStringExtra("FoodPostedBy");
        String FoodSingleImage = getIntent().getStringExtra("FoodSingleImage");
        multipleImagesList = getIntent().getStringArrayListExtra("FoodMultipleImages");


        if (bundle != null) {
            if (FoodSingleImage != null) {
                Picasso.get().load(FoodSingleImage)
                        .fit()
                        .centerCrop()
                        .into(imageView);
                imageView.setVisibility(View.VISIBLE);
            } else if (!multipleImagesList.isEmpty()) {
                mArrayUri.clear();
                for (int i = 0; i < multipleImagesList.size(); i++) {
                    Uri tem_uri = Uri.parse(multipleImagesList.get(i));
                    mArrayUri.add(tem_uri);
                }
                viewPager.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            }
            text_food_title.setText(FoodTitle);
            text_food_desc.setText(FoodDescription);
            text_food_availablity.setText("Available For: " + FoodAvailability);
            text_food_price.setText("Food Price: " + FoodPrice);
            text_food_type.setText(FoodType);
            text_food_pickUpDetails.setText("Pick Up Details :" + FoodPickUpDetail);
            text_food_cuisine_type.setText("Cuisine Type :" + FoodCuisineType);
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference userFoodUpdateReference;
                userFoodUpdateReference = FirebaseDatabase.getInstance().getReference("Food")
                        .child("FoodByUser").child(firebaseAuth.getUid()).child(FoodAdID);
                userFoodUpdateReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String foodAdId = dataSnapshot.child("adId").getValue(String.class);
                        String foodAvailability = dataSnapshot.child("availabilityDays").getValue(String.class);
                        String foodDescription = dataSnapshot.child("foodDescription").getValue(String.class);
                        String foodPickUpDetail = dataSnapshot.child("foodPickUpDetail").getValue(String.class);
                        String foodPrice = dataSnapshot.child("foodPrice").getValue(String.class);
                        String foodTitle = dataSnapshot.child("foodTitle").getValue(String.class);
                        String foodType = dataSnapshot.child("foodType").getValue(String.class);
                        String foodTypeCuisine = dataSnapshot.child("foodTypeCuisine").getValue(String.class);
                        String foodUploadDateAndTime = dataSnapshot.child("foodType").getValue(String.class);
                        String foodPayment = dataSnapshot.child("payment").getValue(String.class);
                        String foodSingleImage = dataSnapshot.child("mImageUri").getValue(String.class);

                        ArrayList<String> updateMultipleImagesList = new ArrayList<>();
                        try {
                            if (dataSnapshot.child("mArrayString").getValue(String.class) != null) {

                                for (DataSnapshot snapshot : dataSnapshot.child("mArrayString").getChildren()) {
                                    updateMultipleImagesList.add(snapshot.child("mArrayString").getValue(String.class));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (foodPostedBy != null) {
                            for (DataSnapshot postedBySnapshot : dataSnapshot.child("foodPostedBy").getChildren()) {
                                user_id = postedBySnapshot.child("userId").getValue(String.class);
                            }
                        }

                        Intent intent = new Intent(UserUploadedFoodDetails.this, UpdateUserFood.class);
                        intent.putExtra("foodAdId", foodAdId);
                        intent.putExtra("foodAvailability", foodAvailability);
                        intent.putExtra("foodDescription", foodDescription);
                        intent.putExtra("foodPickUpDetail", foodPickUpDetail);
                        intent.putExtra("foodPrice", foodPrice);
                        intent.putExtra("foodTitle", foodTitle);
                        intent.putExtra("foodType", foodType);
                        intent.putExtra("foodTypeCuisine", foodTypeCuisine);
                        intent.putExtra("foodUploadDateAndTime", foodUploadDateAndTime);
                        intent.putExtra("foodPayment", foodPayment);
                        intent.putExtra("foodPostedBy", foodPostedBy);
                        intent.putExtra("foodSingleImage", foodSingleImage);
                        intent.putExtra("updateMultipleImagesList", updateMultipleImagesList);
                        intent.putExtra("user_id", user_id);
                        startActivity(intent);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference userFoodDelReference;
                userFoodDelReference = FirebaseDatabase.getInstance().getReference("Food")
                        .child("FoodByUser").child(firebaseAuth.getUid());
                userFoodDelReference.child(FoodAdID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        DatabaseReference allFoodsDelReference;
                        allFoodsDelReference = FirebaseDatabase.getInstance().getReference("Food")
                                .child("FoodByAllUsers");
                        allFoodsDelReference.child(FoodAdID).removeValue();
                        Toast.makeText(getApplicationContext(), "Product Deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }

}
