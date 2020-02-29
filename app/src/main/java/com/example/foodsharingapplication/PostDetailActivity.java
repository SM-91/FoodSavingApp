package com.example.foodsharingapplication;

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
import com.braintreepayments.api.models.Authorization;
import com.example.foodsharingapplication.model.BidModel;
import com.example.foodsharingapplication.model.User;
import com.example.foodsharingapplication.model.UserUploadFoodModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;


public class PostDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = PostDetailActivity.class.getSimpleName();

    private static final int PAYPAL_REQ_CODE = 5;
    private static PayPalConfiguration payPalConfiguration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId("AfvVv6UoY5ruwEhm1H6ZFOA_mxGnBmP-3KX29jCKlPSqGP6r1-HxL-9QpWx2PIC-8TR4xSO0c-7EFQzD");
    String payment;
    //Shehzad Paypal declarations end

    FirebaseAuth firebaseAuth;
    private User sender, receiver;
    private boolean accepted;
    private String ad_id,user_id;
    LinearLayout linearLayout;

    private TextView pTitle, pDescription, pPrice, pTime, pType, pCuisineType, pAvailable, pPayment;
    private ImageView pImage, pImage2;
    private ViewFlipper pFlip;
    //Get Data from Card
    private String title, desc, imageUri, price, time, type, availablity, cuisineType, imageString, paymentimage2;
    ArrayList<String> imageArray = new ArrayList<>();

    //Shehzad Paypal declarations start
    private Button btnPayNow, chatBtn, btnBuyNow;
    private BraintreeFragment mBraintreeFragment;
    private Authorization mAuthorization;
    private UserUploadFoodModel orders;

    private GoogleMap mMap;
    private String lat;
    private String lang;
    private float lat1;
    private float lang1;
    private static LatLng loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        firebaseAuth = FirebaseAuth.getInstance();

        //Flipper View
        pFlip = findViewById(R.id.viewFlipper);
        linearLayout = findViewById(R.id.btn_layout);

        pTitle = findViewById(R.id.titlePost);
        pDescription = findViewById(R.id.descPost);
        pPrice = findViewById(R.id.pricePost);
        pTime = findViewById(R.id.timePost);
        pType = findViewById(R.id.typePost);
        pAvailable = findViewById(R.id.availability);
        pCuisineType = findViewById(R.id.cuisineTypePost);
        pPayment = findViewById(R.id.paymentPost);
        chatBtn = findViewById(R.id.chat_button);
        btnBuyNow = findViewById(R.id.bid_button);

        //Shehzad Paypal button Casting start
        btnPayNow = (Button) findViewById(R.id.btnPay);
        //Shehzad Paypal button Casting end


        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);


        //Get Data from Card
        ad_id = getIntent().getStringExtra("ad_id");
        title = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("description");
        price = getIntent().getStringExtra("price");
        time = getIntent().getStringExtra("time");
        type = getIntent().getStringExtra("type");
        availablity = getIntent().getStringExtra("availability");
        cuisineType = getIntent().getStringExtra("cuisineType");
        payment = getIntent().getStringExtra("pay");
        receiver = getIntent().getParcelableExtra("foodPostedBy");
        imageUri = getIntent().getStringExtra("imageUri");
        imageArray = getIntent().getStringArrayListExtra("imageArray");

        lat=getIntent().getStringExtra("lat");
        lang=getIntent().getStringExtra("lang");

        Log.e(TAG,"dd"+lat+lang);
        if (lat!=null && lang!=null){
            Log.e(TAG, "hii:"+lat+lang);
            lat1=Float.parseFloat(lat);
            lang1=Float.parseFloat(lang);
            loc=new LatLng(lat1,lang1);

        }

        //Set Data in Views
        pTitle.setText(title);
        pDescription.setText(desc);
        pPrice.setText(price);
        pTime.setText(time);
        pType.setText(type);
        pAvailable.setText(availablity);
        pCuisineType.setText(cuisineType);
        pPayment.setText(payment);

        if(payment.equals("Free")){
            btnPayNow.setVisibility(View.GONE);
        }


        if (imageUri != null) {
            ImageView imageV = new ImageView(PostDetailActivity.this);
            Picasso.get().load(imageUri).fit().into(imageV);
            pFlip.addView(imageV);
        } else if (!imageArray.isEmpty()) {
            for (int i = 0; i < imageArray.size(); i++) {
                String uri = imageArray.get(i);
                ImageView imageV = new ImageView(PostDetailActivity.this);
                Picasso.get().load(uri).fit().into(imageV);
                pFlip.addView(imageV);
            }
            Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
            Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);

            pFlip.setInAnimation(in);
            pFlip.setOutAnimation(out);
            pFlip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pFlip.showNext();
                }
            });
        }

        // Picasso.get().load(image2).into(pImage2);
        Intent intentPaypal = new Intent(PostDetailActivity.this, PayPalService.class);
        intentPaypal.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        startService(intentPaypal);

        DatabaseReference recieverDatabaseReference;
        recieverDatabaseReference = FirebaseDatabase.getInstance().getReference("User");
        recieverDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User userModel = snapshot.getValue(User.class);

                    if (userModel.getUserId().equals(firebaseAuth.getUid())) {
                        sender = userModel;
                        /*Hide Layout if user_id and auth_id are same*/
                        hideLayout();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*Check the value of accepted*/
        getAccepted();


        //Shehzad Paypal Acting functionality called on click start
        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payWithPaypal();
            }
        });
        //Shehzad Paypal Acting functionality called on click end

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chat_intent = new Intent(PostDetailActivity.this, MessageActivity.class);
                chat_intent.putExtra("ad_id", ad_id);
                chat_intent.putExtra("foodTitle", title);
                chat_intent.putExtra("foodPostedBy", receiver);
                startActivity(chat_intent);
            }
        });

        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference requestReference;
                BidModel bidModel = new BidModel();
                bidModel.setAdId(ad_id);
                bidModel.setReciever(receiver);
                bidModel.setSender(sender);
                bidModel.setName(title);
                bidModel.setAccepted(false);

                requestReference = FirebaseDatabase.getInstance().getReference("Requests").child(ad_id);
                String requestId = requestReference.push().getKey();
                bidModel.setRequestId(requestId);

                requestReference.child(requestId).setValue(bidModel);

                Toast.makeText(PostDetailActivity.this, "Buy Request Send", Toast.LENGTH_LONG).show();
            }
        });

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
                                .getReference("Orders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Product")
                                .child(ad_id);
                        UserUploadFoodModel uploadModel = new UserUploadFoodModel();
                        uploadModel.setAdId(ad_id);
                        //FirebaseDatabase.getInstance().getReference("Orders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Product");

                        uploadModel.setFoodTitle(title);
                        uploadModel.setFoodDescription(desc);
                        uploadModel.setFoodPrice(price);
                        uploadModel.setFoodType(type);
                        uploadModel.setFoodTypeCuisine(cuisineType);
                        uploadModel.setmImageUri(imageUri);
                        //uploadModel.setPaymentDetails(paymentDetails);
                        databaseReference.setValue(uploadModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(PostDetailActivity.this, "Successful Order", Toast.LENGTH_SHORT).show();
                                Intent userOrder = new Intent(PostDetailActivity.this, UserOrderedFood.class);
                                userOrder.putExtra("ad_id", ad_id);
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

    }

    private void getAccepted() {
        DatabaseReference allRequestsReference = FirebaseDatabase.getInstance().getReference("Requests").child(ad_id);
        //Show Progress Dialog
        allRequestsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //otherUserList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BidModel bidModel = snapshot.getValue(BidModel.class);
                    accepted = bidModel.isAccepted();

                    if (firebaseAuth.getUid().equals(bidModel.getSender().getUserId())) {
                        if (bidModel.isAccepted()) {
                            btnBuyNow.setVisibility(View.GONE);
                            chatBtn.setVisibility(View.VISIBLE);
                            btnPayNow.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void hideLayout() {
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng sydney = new LatLng(-33.867, 151.206);
        mMap=googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
        mMap.setMyLocationEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        if (loc!=null) {
            mMap.addMarker(new MarkerOptions()
                    .title("Sydney")
                    .snippet("The most populous city in Australia.")
                    .position(loc));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));

        }

    }


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
