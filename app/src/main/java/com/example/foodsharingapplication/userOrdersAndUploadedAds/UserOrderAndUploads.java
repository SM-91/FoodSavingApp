package com.example.foodsharingapplication.userOrdersAndUploadedAds;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.model.UploadModel;
import com.example.foodsharingapplication.products.ProductsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserOrderAndUploads extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductsAdapter allProductsAdapter;
    private List<UploadModel> productsList;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mFirebaseAuth;
    private String userId, productTitle, productPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView=findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        //txtAddProduct=(Button) findViewById(R.id.txtAddProduct);
        layoutManager=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);

        userId = getIntent().getStringExtra("userId");
        productTitle = getIntent().getStringExtra("productTitle");
        productPrice = getIntent().getStringExtra("productPrice");

        productsList=new ArrayList<>();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("Seller").child("User");
        mFirebaseAuth= FirebaseAuth.getInstance();

    }
}
