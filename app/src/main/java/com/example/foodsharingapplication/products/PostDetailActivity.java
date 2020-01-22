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
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.models.Authorization;
import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.extras.Products;
import com.example.foodsharingapplication.model.UserUploadFoodModel;
import com.example.foodsharingapplication.userOrdersAndUploadedAds.UserOrderAndUploads;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
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

    private static final int PAYPAL_REQ_CODE = 5;
    private static PayPalConfiguration payPalConfiguration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId("AfvVv6UoY5ruwEhm1H6ZFOA_mxGnBmP-3KX29jCKlPSqGP6r1-HxL-9QpWx2PIC-8TR4xSO0c-7EFQzD");
    String payment;
    private TextView pTitle, pDescription, pPrice, pTime, pType, pCuisineType, pAvailable, pPayment;
    private ImageView pImage, pImage2;
    private ViewFlipper pFlip;
    //Get Data from Card
    private String title, desc, image, price, time, type, availablity, cuisineType, paymentimage2;
    private String[] imageString;
    //Shehzad Paypal declarations start
    private Button btnPayNow;
    //Shehzad Paypal declarations end
    HashMap<String, String> hashImage;
    private BraintreeFragment mBraintreeFragment;
    private Authorization mAuthorization;
    FirebaseAuth firebaseAuth;

    User foodPostedBy;
    String ad_id;
    String user_id;

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

        //Shehzad Paypal button Casting start
        btnPayNow = (Button) findViewById(R.id.btnPay);
        //Shehzad Paypal button Casting end

        //Get Data from Card
        title = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("description");
        image = getIntent().getStringExtra("image");
        // String image2 = getIntent().getStringExtra("image2");
        price = getIntent().getStringExtra("price");
        time = getIntent().getStringExtra("time");
        type = getIntent().getStringExtra("type");
        availablity = getIntent().getStringExtra("availability");
        cuisineType = getIntent().getStringExtra("cuisineType");
        payment = getIntent().getStringExtra("pay");
        imageString = getIntent().getStringArrayExtra("hashImage");

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
       // Log.i("imagesString[]: ", imageString.length);

        /*if (image == null) {
            for (int i = 0; i < imageString.length; i++) {
=======


        if(image==null) {

            for (int i = 0; i < imageArray.size(); i++) {
>>>>>>> feature/Shayan

                ImageView imageV = new ImageView(this);
                //imageV.setLayoutParams(new android.view.ViewGroup.LayoutParams(80,60));
                //imageV.setMaxHeight(20);
                //imageV.setMaxWidth(20);
                // Adds the view to the layout
                pFlip.addView(imageV);

            }
        } else {
            Picasso.get().load(image).into(pImage);
        }*/
        // Picasso.get().load(image2).into(pImage2);
        Intent intentPaypal=new Intent(PostDetailActivity.this, PayPalService.class);
        intentPaypal.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,payPalConfiguration);
        startService(intentPaypal);

        /*Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);*/


        pFlip.setAutoStart(true);
       // pFlip.setInAnimation(in);
        //pFlip.setOutAnimation(out);
        pFlip.setFlipInterval(3000);
        //Shehzad Paypal Acting functionality called on click start
        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payWithPaypal();
            }
        });
        //Shehzad Paypal Acting functionality called on click end

    }

    private void payWithPaypal() {
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(10),
                "EUR", pTitle.getText().toString(), PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(PostDetailActivity.this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQ_CODE);
        Toast.makeText(this, "RESULT_OK", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Toast.makeText(this, "Successfully...", Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "Successfully", Toast.LENGTH_SHORT).show();
        if (requestCode == PAYPAL_REQ_CODE) {

            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "RESULT_OK", Toast.LENGTH_SHORT).show();

                PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (paymentConfirmation != null) {
                    try {
                        String paymentDetails = paymentConfirmation.toJSONObject().toString(4);
                        Log.i("PAYMENTdETAILS: ", paymentDetails);
                        //JSONObject jsonObject = new JSONObject(paymentDetails);
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                .getReference("Orders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Product");
                        UserUploadFoodModel uploadModel = new UserUploadFoodModel();
                        uploadModel.setFoodTitle(title);
                        uploadModel.setFoodDescription(desc);
                        uploadModel.setFoodPrice(price);
                        uploadModel.setFoodType(type);
                        uploadModel.setFoodTypeCuisine(cuisineType);
                        //uploadModel.setmImageUri(image);
                        //uploadModel.setPaymentDetails(paymentDetails);
                        databaseReference.setValue(uploadModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(PostDetailActivity.this, "Successful Order", Toast.LENGTH_SHORT).show();
                                Intent userOrder = new Intent(PostDetailActivity.this, UserOrderAndUploads.class);
                                startActivity(userOrder);
                            }
                        });

                        //startActivity(new Intent(PostDetailActivity.this, UserOrderAndUploads.class));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Something went wrong JSONException", Toast.LENGTH_SHORT).show();
                    }
                }

            } else {

                Toast.makeText(this, "Something went wrong WITH RESULT_OK", Toast.LENGTH_SHORT).show();

            }
        } else {

            Toast.makeText(this, "Something went wrong WITH PAYPAL_REQ_CODE", Toast.LENGTH_SHORT).show();

        }

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

   /* @Override
    public void onActivityResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Toast.makeText(this, "Successfully Paid", Toast.LENGTH_SHORT).show();
        if (requestCode == PAYPAL_REQ_CODE) {

            if (requestCode == Activity.RESULT_OK) {


                DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                        .getReference("Orders").child("UserID"+" - "+FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Product");
                UploadModel uploadModel= new UploadModel();
                uploadModel.setFoodTitle(title);
                uploadModel.setFoodDescription(desc);
                uploadModel.setFoodPrice(price);
                uploadModel.setFoodType(type);
                uploadModel.setFoodTypeCuisine(cuisineType);
                databaseReference.setValue(uploadModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(PostDetailActivity.this, "Successful Order", Toast.LENGTH_SHORT).show();
                        Intent userOrder = new Intent(PostDetailActivity.this, UserOrderAndUploads.class);
                        startActivity(userOrder);
                    }
                });


            } else {

                Toast.makeText(this, "Something went wrong during Payment", Toast.LENGTH_SHORT).show();

            }


        }
    }*/

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
    // Handle Back press

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


}
