package com.example.foodsharingapplication.authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.foodsharingapplication.Dashboard;
import com.example.foodsharingapplication.products.HomeActivity;
import com.example.foodsharingapplication.products.ProductListView;
import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.extras.ShowData_Fragment;
import com.example.foodsharingapplication.model.User;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignIn extends AppCompatActivity implements View.OnClickListener {
    Button btnSignIn, btnsignOut, loginFb, btnProfile;
    EditText email, pass;
    TextView forgetPassword;
    public static final String id = "-LtvYIg-vEh0iY78nmOJ";
    private LoginButton FacebookButton;
    private CircleImageView circleImageView;
    private TextView txtFbName, txtFbEmail;
    private CallbackManager callBackManager;
    private ProgressDialog progressDialog;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference firebaseRef, refToPointNode;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;
    User verifyUser;
    List<User> userList;
    ProgressDialog progress;
    private String loginEmail;
    private String loginPassword;
    private ShowData_Fragment showData_fragment;
    private FragmentManager fragManger;
    private FragmentTransaction fragTrans;
    private String fbName;
    private String fbEmail;
    private String fbid;
    private String fbCountry;
    private String fbAge;
    private String fbphoneNumber;
    private String fbImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        email = (EditText) findViewById(R.id.enterEmail);
        pass = (EditText) findViewById(R.id.enterLoginPassword);
        txtFbEmail = findViewById(R.id.profile_email);
        txtFbName = findViewById(R.id.enterLoginPassword);
        circleImageView = findViewById(R.id.profile_pic);
        forgetPassword = (TextView) findViewById(R.id.forgetLoginPassword);

        btnsignOut = (Button) findViewById(R.id.btnSignOut);
        btnSignIn = (Button) findViewById(R.id.btnLogin);

        showData_fragment = new ShowData_Fragment();
        fragManger = getSupportFragmentManager();
        fragTrans = fragManger.beginTransaction();

        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait!!");
        progress.setMessage("Wait!!");
        progress.setCancelable(true);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, com.example.foodsharingapplication.authentication.forgetPassword.class);
                startActivity(intent);
            }
        });

        userList = new ArrayList<User>();
        verifyUser = new User();
        //database reference pointing to root of database
        firebaseRef = database.getInstance().getReference("User");
        //database reference pointing to User node
        refToPointNode = firebaseRef.child("User").child(id);
        firebaseAuth = FirebaseAuth.getInstance();
        //btnProfile.setVisibility(View.GONE);
        /*if (!firebaseAuth.getCurrentUser().getUid().equals("")){
            btnProfile.setVisibility(View.VISIBLE);
        }*/

        //if (firebaseAuth.getCurrentUser() == null) {

            /*FacebookSdk.sdkInitialize(this.getApplicationContext());
            FacebookButton = (LoginButton) findViewById(R.id.fb_login_button);
            callBackManager = CallbackManager.Factory.create();
            FacebookButton.setReadPermissions(Arrays.asList("email", "public_profile"));
            //FacebookButton.setReadPermissions(permissionNeeds);*/


       // }

        //btnSignIn.setOnClickListener(this);
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Toast.makeText(SignIn.this, "Successfully Signed In with: " + firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(SignIn.this, "Successfully Signed Out: ", Toast.LENGTH_SHORT).show();
            }
        };

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEmail = email.getText().toString().trim();
                loginPassword = pass.getText().toString().trim();

                if (!loginEmail.equals("") && !loginPassword.equals("")) {

                    firebaseAuth.signInWithEmailAndPassword(loginEmail, loginPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignIn.this, "Congrats!", Toast.LENGTH_SHORT).show();
                                        //Intent intent = new Intent(SignIn.this, ShowData.class);
                                        //Intent intent = new Intent(SignIn.this, UserProfile.class);
                                        Intent intent = new Intent(SignIn.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(SignIn.this, "Please check Email or Password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    /*fragTrans.add(R.id.fragment_container,showData_fragment);
                    fragTrans.commit();*/

                } else {
                    Toast.makeText(SignIn.this, "Please enter valid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
       /* btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this,ShowData.class);
                startActivity(intent);
            }
        });*/



        btnsignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.signOut();
                Toast.makeText(SignIn.this, "Signing Out...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignIn.this, Dashboard.class);
                startActivity(intent);


            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (firebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                startActivity(new Intent(this, Dashboard.class));
                break;
        }
        /*switch (v.getId()){
            case R.id.btnLogin:
                logIn();
                break;
        }*/
    }
    /*AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            if (currentAccessToken == null){

                fbAge="";
                fbCountry="";
                fbEmail="";
                fbImageUrl="";
                fbid="";
                fbName="";
                fbphoneNumber="";
                Toast.makeText(SignIn.this,"User successfully logged out from Facebook",Toast.LENGTH_SHORT).show();
            }
            else{

                loadUserProfile(currentAccessToken);

            }

        }
    };*/

    /*private void loadUserProfile(AccessToken newAccessToken){
            final GraphRequest request= GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    try {
                        fbName= object.getString("first_name");
                        fbEmail=object.getString("email");
                        fbid=object.getString("id");
                        fbCountry=object.getString("location");
                        fbAge=object.getString("age");
                        fbphoneNumber=object.getString("phone Number");
                        fbImageUrl="https://graphfacebook.com/"+fbid+"/picture?type=normal";

                        Log.i(" FB Name : ",fbName);
                        Log.i(" FB Email : ",fbEmail);
                        Log.i(" FB ID : ",fbid);
                        Log.i(" FB Country : ",fbCountry);
                        Log.i(" FB Phone Number : ",fbphoneNumber);
                        Log.i(" FB Image Url : ",fbImageUrl);
                        Log.i(" FB Age : ",fbAge);
                        //txtFbEmail.setText(fbEmail);
                        //txtFbName.setText(fbEmail);
                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.dontAnimate();

                        Glide.with(SignIn.this).load(fbImageUrl).into(circleImageView);
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            });
            Bundle parameters= new Bundle();
            parameters.putString("fields","name,email,id,location,phoneNumber,age,country");
            request.setParameters(parameters);
            request.executeAsync();
        }*/

    /*public void buttonClickLoginFb(View view) {
        if (firebaseAuth==null) {
            LoginManager.getInstance().registerCallback(callBackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {

                    checkLoginStatus(loginResult.getAccessToken());

                }

                @Override
                public void onCancel() {

                    Toast.makeText(getApplicationContext(), "User cancelled login plan", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onError(FacebookException error) {

                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
        else {
            LoginManager.getInstance().logOut();
        }

    }

    private void checkLoginStatus(AccessToken accessToken) {
        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    getUserFbInformation(firebaseUser);

                } else {

                    LoginManager.getInstance().logOut();
                    Toast.makeText(getApplicationContext(), "Could not authenticate user", Toast.LENGTH_SHORT).show();

                }
            }
        });
       *//* if(accessToken.getToken())
        {
            loadUserProfile(AccessToken.getCurrentAccessToken());
        }*//*
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callBackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    /*private void getUserFbInformation(FirebaseUser firebaseUser) {

        Log.i("", firebaseUser.getEmail());
        Log.i("", firebaseUser.getDisplayName());
        Log.i("", firebaseUser.getPhotoUrl().toString());
        User userData=new User();
        userData.setUserName(firebaseUser.getDisplayName());
        userData.setUserEmail(firebaseUser.getEmail());
        userData.setUserProfilePicUrl(firebaseUser.getPhotoUrl().toString());
        userData.setUserWithFb(Boolean.TRUE);
        String userDbKey=firebaseRef.push().getKey();
        firebaseRef.child(firebaseAuth.getCurrentUser().getUid()).setValue(userData).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext()," Successfully Registered with FaceBook ",Toast.LENGTH_SHORT).show();
                       // Intent intent=new Intent(SignIn.this,ShowData.class);
                    }
                });
*//*        intent.putExtra("fbUserName",firebaseUser.getDisplayName());
        intent.putExtra("fbUserEmail",firebaseUser.getEmail());
        intent.putExtra("fbUserPrfileImgUrl",firebaseUser.getPhotoUrl().toString());*//*
        //Log.i("", firebaseUser.getPhoneNumber());
        //Log.i("",firebaseUser.getMetadata());

    }*/

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
