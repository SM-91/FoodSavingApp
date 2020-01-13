package com.example.foodsharingapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.request.RequestOptions;
import com.example.foodsharingapplication.authentication.Register;
import com.example.foodsharingapplication.authentication.SignIn;
import com.example.foodsharingapplication.extras.showproducts;
import com.example.foodsharingapplication.model.User;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {
    private ViewFlipper viewFlipper;
    private Button btnSignUp,loginBtn;
    private TextView txtViewGuest;
    private LoginButton facebookBtn;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mFirebaseAuthListener;
    private CallbackManager callbackManager;



/*    private Button btnCountry_picker;
    private ImageView countryFlag;
    private CountryPicker countryPicker;
    private String strCountry_isoCode,strCountry_DialCode,strCountry_Name,strCountry_Currency;*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        loginBtn = (Button) findViewById(R.id.btnLogin);
        txtViewGuest = (TextView) findViewById(R.id.browse);
        facebookBtn = (LoginButton) findViewById(R.id.fb_login_button);

        //Country with Flag Picker starts
        /*btnCountry_picker=(Button) findViewById(R.id.countryPicker);
        countryFlag=(ImageView) findViewById(R.id.selected_country_flag_image_view);
        btnCountry_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryPicker.showDialog(getSupportFragmentManager());
            }
        });
        countryPicker = new CountryPicker.Builder().with(this).listener(this).build();*/

        // Country with flag Picker ends



        viewFlipper = findViewById(R.id.slider);
        int sliderImagesList[] = {R.drawable.slider1,
                                    R.drawable.slider2,R.drawable.slider3,
                                    R.drawable.slider4,R.drawable.slider5,
                                    R.drawable.slider6};

        btnSignUp.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        //facebookBtn.setOnClickListener(this);



        for (int image:sliderImagesList){
            imagesFlipper(image);
        }
        firebaseAuth = FirebaseAuth.getInstance();
        //firebaseStorageReference=firebasestorage.getReference("User");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("User");
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        txtViewGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Dashboard.this, showproducts.class);
                startActivity(intent);
            }
        });
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth==null){

                   // loginBtn.setVisibility(View.VISIBLE);
                   // btnSignUp.setVisibility(View.VISIBLE);
                }
                else{
                   // loginBtn.setVisibility(View.GONE);
                   // btnSignUp.setVisibility(View.GONE);
                   // Intent intent= new Intent(Dashboard.this, ShowData.class);
                   // startActivity(intent);

                }
            }
        });
        callbackManager = CallbackManager.Factory.create();
        facebookBtn.setReadPermissions(Arrays.asList("email","public_profile"));
        checkLoginStatus();

        facebookBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }
    //Country picker implementation
   /* @Override
    public void onSelectCountry(Country country){
        countryFlag.setImageResource(country.getFlag());
        strCountry_Currency= country.getCurrency();
        strCountry_DialCode=country.getDialCode();
        strCountry_isoCode = country.getCode();
        strCountry_Name=country.getName();
        Log.i("Country Currency:",strCountry_Currency);
        Log.i("Country DialCode:",strCountry_DialCode);
        Log.i("Country ISO Code:",strCountry_isoCode);
        Log.i("Country Name:",strCountry_Name);
    }*/


    //Country picker implementation

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken)
        {
            if(currentAccessToken==null)
            {
                //txtName.setText("");
                //txtEmail.setText("");
                //circleImageView.setImageResource(0);
                Toast.makeText(Dashboard.this,"User Logged out from facebook",Toast.LENGTH_LONG).show();
            }
            else
                loadUserProfile(currentAccessToken);
        }
    };

    private void loadUserProfile(final AccessToken newAccessToken)
    {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response)
            {
                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");
                    String image_url = "https://graph.facebook.com/"+id+ "/picture?type=normal";
                    AuthCredential authCredential = FacebookAuthProvider.getCredential(newAccessToken.getToken());
                    firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
                            getUserFbInformation(firebaseUser);
                        }
                    });

                    //txtEmail.setText(email);
                   // txtName.setText(first_name +" "+last_name);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();

                    //Glide.with(MainActivity.this).load(image_url).into(circleImageView);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields","first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }
    private void getUserFbInformation(FirebaseUser firebaseUser) {

        Log.i("", firebaseUser.getEmail());
        Log.i("", firebaseUser.getDisplayName());
        Log.i("", firebaseUser.getPhotoUrl().toString());
        User userData=new User();
        userData.setUserName(firebaseUser.getDisplayName());
        userData.setUserEmail(firebaseUser.getEmail());
        userData.setUserProfilePicUrl(firebaseUser.getPhotoUrl().toString());
        userData.setUserWithFb(Boolean.TRUE);
        String userDbKey=mDatabaseReference.push().getKey();
        mDatabaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(userData).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext()," Successfully Registered with FaceBook ",Toast.LENGTH_SHORT).show();
                        //Intent intent=new Intent(Dashboard.this,ShowData.class);
                        //startActivity(intent);
                    }
                });
/*        intent.putExtra("fbUserName",firebaseUser.getDisplayName());
        intent.putExtra("fbUserEmail",firebaseUser.getEmail());
        intent.putExtra("fbUserPrfileImgUrl",firebaseUser.getPhotoUrl().toString());*/
        //Log.i("", firebaseUser.getPhoneNumber());
        //Log.i("",firebaseUser.getMetadata());

    }
    private void checkLoginStatus()
    {
        if(AccessToken.getCurrentAccessToken()!=null)
        {
            loadUserProfile(AccessToken.getCurrentAccessToken());
            Intent intent = new Intent(Dashboard.this, showproducts.class);
           /* intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);*/
            startActivity(intent);
        }
    }
    public void imagesFlipper(int image){
        ImageView imageView=new ImageView(this);
        imageView.setBackgroundResource(image);
        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(2000);
        viewFlipper.setAutoStart(true);

        //Animation
        viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSignUp:
                startActivity(new Intent(this, Register.class));
                break;
        }
        switch (v.getId()){
            case R.id.btnLogin:
                startActivity(new Intent(this, SignIn.class));
                break;
        }
        switch (v.getId()){
            case R.id.fb_login_button:
                Intent intent = new Intent(Dashboard.this, SignIn.class);
                startActivity(intent);
                break;
        }


    }
}
