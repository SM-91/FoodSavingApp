package com.example.foodsharingapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodsharingapplication.authentication.SignIn;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeDefinition extends AppCompatActivity {

    Intent intent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {

            intent = new Intent(this, HomeActivity.class);
            Log.i("to Home ",intent.toString());

        } else {

            intent = new Intent(this, SignIn.class);
            Log.i("to signIn ",intent.toString());

        }
        startActivity(intent);

    }

/*@Override
    public void onCreate() {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_definition);
        super.onCreate();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){


            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (isBackPressed)
                fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.,attachedfragmentHere);
            fragmentTransaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out);
            fragmentTransaction.commit();

            Intent intent=new Intent(HomeDefinition.this, ProductGridView.class);
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
    }*/

}
