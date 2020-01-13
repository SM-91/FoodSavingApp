package com.example.foodsharingapplication;

import android.app.Application;
import android.content.Intent;

import com.example.foodsharingapplication.products.ProductListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeDefinition extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){

            Intent intent=new Intent(HomeDefinition.this, ProductListView.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }
        else{

            Intent intent=new  Intent(HomeDefinition.this, Dashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }
    }

}
