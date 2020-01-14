package com.example.foodsharingapplication.extras;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.foodsharingapplication.Dashboard;
import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ShowData_Fragment extends Fragment {
    private  TextView name,email,pass,phoneNumber,gender;
    private  String strName,strEmail,strPass,strPhoneNumber,strGender;
    private Button btnRegister, btnCancel;
    private ImageView profileImage;
    private  String userID;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mFirebaseAuthListener;



    public ShowData_Fragment() {
        // Required empty public constructor
    }
    public static ShowData_Fragment newInstance(String param1, String param2) {
        ShowData_Fragment fragment = new ShowData_Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        firebaseAuth =FirebaseAuth.getInstance();
        //firebaseStorageReference=firebasestorage.getReference("User");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        userID = currentUser.getUid();

        mFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
                if (firebaseUser != null){
                    Log.i(" AuthStateChanged login", firebaseUser.getUid());
                }
                else{

                    Log.i(" logOut", firebaseUser.getUid());
                }

            }
        };

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){//snapshot is of overall data and we get its children
                    User userData=new User();

                    userData.setUserName(ds.child(userID).getValue(User.class).getUserName());
                    userData.setUserPassword(ds.child(userID).getValue(User.class).getUserPassword());
                    userData.setUserEmail(ds.child(userID).getValue(User.class).getUserEmail());
                    userData.setUserPhoneNumber(ds.child(userID).getValue(User.class).getUserPhoneNumber());
                    userData.setSex(ds.child(userID).getValue(User.class).getSex());

                    strName = userData.getUserName();
                    strEmail = userData.getUserName();
                    strPass = userData.getUserName();
                    strPhoneNumber = userData.getUserName();
                    strGender = userData.getUserName();

                    name.setText(strName);
                    email.setText(strEmail);
                    pass.setText(strPass);
                    phoneNumber.setText(strPhoneNumber);
                    gender.setText(strGender);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       /* if (getArguments() != null) {
            *//*mString Param1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*//*
        }*/
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //databaseHelper= new DatabaseHelper(this);
        View view = inflater.inflate(R.layout.fragment_show_data_, container, false);

        name= (TextView) view.findViewById(R.id.name);
        email= (TextView) view.findViewById(R.id.enterEmail);
        pass= (TextView) view.findViewById(R.id.enterPassword);
        //phoneNumber= (TextView) view.findViewById(R.id.phoneMobile);
        //gender= (TextView) view.findViewById(R.id.gender);
        profileImage= (ImageView) view.findViewById(R.id.profileImgView);
        Button cancelBtn= view.findViewById(R.id.btnCancel);
        CheckBox showPassword= (CheckBox) view.findViewById(R.id.showPass);


        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        pass.setTransformationMethod(new PasswordTransformationMethod());

        //Bundle getDataBundle=getArguments();
        //name.setText(userData.getUserName());
        //email.setText(userData.getUserEmail());
        //pass.setText(userData.getUserPassword());
        //phoneNumber.setText(userData.getUserPhoneNumber());
       // gender.setText(userData.getSex());
        //phoneNumber.setText(getDataBundle.getString("phoneNumber"));
        //profileImage.(getDataBundle.getString("imageUrl"));

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btnCancel:
                        Intent intent = new Intent(getActivity(), Dashboard.class);
                        startActivity(intent);
                        break;
                }
            }
        });

        return view;
    }


}
