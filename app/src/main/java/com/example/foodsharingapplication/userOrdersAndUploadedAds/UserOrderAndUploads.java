package com.example.foodsharingapplication.userOrdersAndUploadedAds;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.model.UploadModel;
import com.example.foodsharingapplication.products.ProductsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;

public class UserOrderAndUploads extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView tag1,tag2;
    private ProductsAdapter allProductsAdapter;
    private List<UploadModel> productsList;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser mFirebaseCurrentUser;
    private RatingBar ratingBar;
    private String userId, productTitle, productPrice;
    private float barRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        recyclerView = findViewById(R.id.slider_recycler_view);
        ratingBar =(RatingBar) findViewById(R.id.ratingBar);
        tag1 = findViewById(R.id.tag1);
        tag2 = findViewById(R.id.tag2);
        recyclerView.setHasFixedSize(true);
        //txtAddProduct=(Button) findViewById(R.id.txtAddProduct);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        //Changing Tags according to own requirement start
        tag1.setText("My Orders");
        tag2.setText("My Ads");

        //Changing Tags according to own requirement end

        productsList = new ArrayList<>();
        mFirebaseCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance()
                .getReference("Orders")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            if (mFirebaseCurrentUser.getUid().equals(ds.getKey())) {

                                Log.i("current Order: ", mFirebaseCurrentUser.getUid());
                                mDatabaseRef = FirebaseDatabase.getInstance()
                                        .getReference("Orders").child(mFirebaseCurrentUser.getUid());
                                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot productsDataSnapshot : dataSnapshot.getChildren()) {
                                            Log.i("Current UID: ", productsDataSnapshot.toString());
                                            UploadModel orders = productsDataSnapshot.getValue(UploadModel.class);
                                            if (orders.getRating()==0){
                                                Toast.makeText(UserOrderAndUploads.this,"please rate the cosine type",Toast.LENGTH_SHORT).show();

                                                //send a code to user on payment completion in PostDetailsActivity
                                                //if seller has also inserted this code into app by asking from buyer then
                                                //show rating bar as ratingbar.setvisibility(visible)
                                                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                                    @Override
                                                    public void onRatingChanged(RatingBar ratingBar1, float rating, boolean fromUser) {
                                                        Toast.makeText(UserOrderAndUploads.this,""+rating,Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                //saveRating();
                                            }
                                            productsList.add(orders);
                                            allProductsAdapter = new ProductsAdapter(UserOrderAndUploads.this, productsList);
                                            recyclerView.setAdapter(allProductsAdapter);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(UserOrderAndUploads.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });


                            } else {
                                Toast.makeText(UserOrderAndUploads.this, "please order first", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

  /*  private void saveRating() {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(final RatingBar ratingBar, float rating, boolean fromUser) {
                //barRating = rating;
                Toast.makeText(UserOrderAndUploads.this, "rating"+rating, Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance()
                        .getReference("Orders").child(mFirebaseCurrentUser.getUid()).child("Product").child("Rating").setValue(rating)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(UserOrderAndUploads.this, "Thanks for rating", Toast.LENGTH_SHORT).show();
                                //ratingBar.setIsIndicator(false);
                            }
                        });
            }
        });
    }*/
}
