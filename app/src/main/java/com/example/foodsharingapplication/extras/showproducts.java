package com.example.foodsharingapplication.extras;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsharingapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class showproducts extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AllProducts allProductsAdapter;
    private List<Products> productsList;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mFirebaseAuth;
    private Button btnLogin;
    private Button txtAddProduct,btnSetting;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productslist);
        /*        Toolbar toolbar1=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar1);*/
        recyclerView=findViewById(R.id.productsListRecyclerView);
        recyclerView.setHasFixedSize(true);
        //txtAddProduct=(Button) findViewById(R.id.txtAddProduct);
        layoutManager=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);

        productsList=new ArrayList<>();
        //getSupportActionBar().hide();//Ocultar ActivityBar anterior
        //Toolbar mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(mTopToolbar);
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("Products");
        mFirebaseAuth=FirebaseAuth.getInstance();
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot productsDataSnapshot:dataSnapshot.getChildren()){
                    Products products=productsDataSnapshot.getValue(Products.class);
                    productsList.add(products);
                    allProductsAdapter=new AllProducts(showproducts.this,productsList);
                    recyclerView.setAdapter(allProductsAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(showproducts.this, databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        /*btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(showproducts.this,SignIn.class);
                startActivity(intent);
            }
        });*/
        /*txtAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),uploadproduct.class));
            }
        });*/
    }


    public void addNewProduct(View view) {
        Intent intent = new Intent(showproducts.this, uploadproduct.class);
        startActivity(intent);
    }
    public void settings(View view) {
//        Intent intent = new Intent(showproducts.this, Settings.class);
//        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer_menue, menu);
        menu.getItem(0).getSubMenu().getItem(3).setVisible(false);
        menu.getItem(0).getSubMenu().getItem(4).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }
}
