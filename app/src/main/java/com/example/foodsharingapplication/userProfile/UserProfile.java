package com.example.foodsharingapplication.userProfile;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserProfile extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Profile profileAdapter;

    //private DatabaseReference mDatabaseRef;
    private FirebaseDatabase mfirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private List<User> usersList;
    private String userNam;
    private String userEmail;
    private String userPassword;
    private String userCountry;
    private String userDoB;
    private String userGender;
    private String userProfileUrl;
    private String userPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        recyclerView = findViewById(R.id.userProfileRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        usersList = new ArrayList<>();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = mfirebaseDatabase.getReference(Objects.requireNonNull("User"));

        mDatabaseRef.child(mFirebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //if (mFirebaseAuth.getCurrentUser()!= null){
                Toast.makeText(UserProfile.this, " logged in jani " +mFirebaseAuth.getUid(), Toast.LENGTH_SHORT).show();
                    //for (DataSnapshot userDataSnapshot: dataSnapshot.getChildren()){
                User user= dataSnapshot.getValue(User.class);

                assert user != null;
                userNam = user.getUserName();
                userEmail = user.getUserEmail();
                userPassword=user.getUserPassword();
                userCountry=user.getUserCountry();
                userDoB=user.getUserDateOfBirth();
                userGender=user.getSex();
                userProfileUrl=user.getUserProfilePicUrl();
                userPhoneNumber=user.getUserPhoneNumber();
                Log.i(" name= ",userNam);
                Log.i(" userEmail= ",userEmail);
                Log.i(" userPassword= ",userPassword);
                Log.i(" userCountry= ",userCountry);
                Log.i(" userDoB= ",userDoB);
                Log.i(" userGender= ",userGender);
                Log.i(" userProfileUrl= ",userProfileUrl);
                Log.i(" userPhoneNumber= ",userPhoneNumber);
                //usersList.add(user);
                //}
                profileAdapter = new Profile(UserProfile.this,
                        userNam,
                        userEmail,
                        userPassword,
                        userCountry,
                        userDoB,
                        userGender,
                        userProfileUrl,
                        userPhoneNumber);
                //profileAdapter = new Profile(UserProfile.this,user);
                recyclerView.setAdapter(profileAdapter);
                //}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserProfile.this, databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
