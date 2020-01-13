package com.example.foodsharingapplication.authentication.AuthemnticationFragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodsharingapplication.Dashboard;
import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.authentication.ShowData;
import com.example.foodsharingapplication.model.User;
import com.example.foodsharingapplication.products.ProductsFragment.ProductGridView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class Profile extends Fragment {

    private TextView name,email,pass,phoneNumber,gender,listTextView,userDOB,userCountry;
    private String strName,strEmail,strPass,strPhoneNumber,strGender;
    private Button btnRegister, btnCancel,btnEdit;
    private ImageView profileImage;
    private String userID;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mFirebaseAuthListener;
    private ListView simpleListView;

    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.myprofile, container,false);
        name= (TextView) view.findViewById(R.id.name);
        email= (TextView) view.findViewById(R.id.enterEmail);
        pass= (TextView) view.findViewById(R.id.enterPassword);
        phoneNumber= (TextView) view.findViewById(R.id.phoneNumber);
        userCountry= (TextView) view.findViewById(R.id.userCountry);
        userDOB= (TextView) view.findViewById(R.id.userDOB);
        gender= (TextView) view.findViewById(R.id.gender);
        listTextView= (TextView) view.findViewById(R.id.listTextView);
        profileImage= (ImageView) view.findViewById(R.id.userProfilePic);
        Button cancelBtn= view.findViewById(R.id.btnCancel);
        btnEdit= view.findViewById(R.id.btnEdit);
        CheckBox showPassword= (CheckBox) view.findViewById(R.id.showPass);
        simpleListView= (ListView) view.findViewById(R.id.simpleListView);

        firebaseAuth =FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("User");
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        userID = currentUser.getUid();
        Log.i(" firebaseAuth Id :", firebaseAuth.getUid() +" Current User Id" +currentUser.getDisplayName());

        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                else{
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ProductGridView.class));
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseAuth.getCurrentUser() != null){

                    firebaseAuth.signOut();
                    Intent intent=new Intent(getContext(), Dashboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }
                //firebaseAuth.sendPasswordResetEmail(email);
            }
        });


        mFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
                if (firebaseUser != null){
                    Log.i(" AuthStateChanged login", firebaseUser.getUid());
                }
                else{
                    firebaseAuth.signOut();
                    Toast.makeText(getContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                    /*Intent intent=new Intent(ShowData.this, Dashboard.class);
                    startActivity(intent);*/
                    //   Log.i(" logOut", firebaseUser.getUid());
                }

            }
        };

        mDatabaseReference.child(Objects.requireNonNull(firebaseAuth.getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //for (DataSnapshot ds : dataSnapshot.getChildren()){//snapshot is of overall data and we get its children
                User userData= dataSnapshot.getValue(User.class);
                name.setText(userData.getUserName());
                email.setText(userData.getUserEmail());
                pass.setText(userData.getUserPassword());
                gender.setText(userData.getSex());
                userCountry.setText(userData.getUserCountry());
                userDOB.setText(userData.getUserDateOfBirth());
                phoneNumber.setText(userData.getUserPhoneNumber());
                String profilePicUrl= userData.getUserProfilePicUrl();
                Picasso.get().load(profilePicUrl).fit().centerCrop().into(profileImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mFirebaseAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mFirebaseAuthListener != null){
            firebaseAuth.removeAuthStateListener(mFirebaseAuthListener);
        }
    }

    /*@Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCancel:
                Intent intent = new Intent(getContext(), Dashboard.class);
                startActivity(intent);
                break;
        }
    }*/
}
