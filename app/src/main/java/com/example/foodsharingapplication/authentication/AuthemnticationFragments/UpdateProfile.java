package com.example.foodsharingapplication.authentication.AuthemnticationFragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.authentication.SignIn;
import com.example.foodsharingapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.facebook.AccessTokenManager.TAG;

public class UpdateProfile extends Fragment implements DialogeConfirmCredentials.ConfirmUserCredentials {

    private TextInputEditText editUserName, addUserNewPassword, confirmUserNewPassword;
    private Button btnSubmitChanges;
    private TextView editProfile;
    private ImageView userProfilePic, logoImg;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private String email, password, userName;
    private DialogeConfirmCredentials dialogeConfirmCredentials;
    private User userData;
    private SignIn signIn;
    FirebaseAuth firebaseAuth;

    public UpdateProfile() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_update_profile, container, false);

        editUserName = view.findViewById(R.id.editUserName);
        addUserNewPassword = view.findViewById(R.id.editUserNewPassword);
        confirmUserNewPassword = view.findViewById(R.id.editUserConfirmPassword);
        editProfile = view.findViewById(R.id.editProfile);
        btnSubmitChanges = view.findViewById(R.id.btnSubmitUpdates);
        userProfilePic = view.findViewById(R.id.userProfilePic);
        logoImg = view.findViewById(R.id.logoImg);

        userProfilePic.setVisibility(View.VISIBLE);
        logoImg.setVisibility(View.GONE);

        userData = new User();
        firebaseAuth = FirebaseAuth.getInstance();
        dialogeConfirmCredentials = new DialogeConfirmCredentials(getContext());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
        dialogeConfirmCredentials.setDatabaseReference(databaseReference);

        editProfile.setText(firebaseUser.getEmail());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userData = dataSnapshot.getValue(User.class);
                editUserName.setText(userData.getUserName());
                //Picasso.get().load(userData.getUserProfilePicUrl()).centerCrop().fit().into(userProfilePic);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btnSubmitChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        User userData = dataSnapshot.getValue(User.class);
                        if (!TextUtils.isEmpty(editUserName.getText().toString()) && !editUserName.getText().toString().equals(userData.getUserName())){

                            Log.i("editUserName : ",editUserName.getText().toString());
                            userName = editUserName.getText().toString();
                            userData.setUserName(userName);
                            databaseReference.setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        editUserName.setEnabled(false);
                                        toastMessage("User Name Updated");

                                    }
                                }
                            });
                        }


                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                try {
                    if (!TextUtils.isEmpty(addUserNewPassword.getText().toString())) {
                        if (addUserNewPassword.getText().toString().length() >= 6) {
                            if (!TextUtils.isEmpty(confirmUserNewPassword.getText().toString())) {
                                if (addUserNewPassword.getText().toString().equals(confirmUserNewPassword.getText().toString())) {
                                    //intent.putExtra("uPassword", pass.getText().toString().trim());
                                    openLoginDialog();
                                }
                                else{
                                    toastMessage("Password does not match");
                                }
                            }
                            else{
                                toastMessage("Confirm Password can't be empty");
                            }
                        }
                        else{
                            toastMessage("password must include at least 6 characters");
                        }
                    }
                }
                catch (NullPointerException e){
                    Log.e(TAG,"Catch: on Password nullException" +e.getMessage());
                }
            }
        });

        return view;
    }



    /*public void insertUpdatedData(){

        firebaseUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                toastMessage("Email Updated1");
            }
        });
        firebaseUser.updatePassword(password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            toastMessage("Password Update1");
                        }
                    }
                });
        userData.setUserName(userName);
        databaseReference.child(firebaseUser.getUid()).setValue(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //startActivity(new Intent(getContext(), Profile.class));
                        toastMessage("Profile1 Updated successfully");
                    }
                });
    }*/
    public void openLoginDialog(){
        dialogeConfirmCredentials.show(getFragmentManager(),"Before Updating Confirm Credentials");
        dialogeConfirmCredentials.setTargetFragment(UpdateProfile.this,1);
    }
    private boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            toastMessage("Email is not Valid");
        }
        return matcher.matches();
    }
    private void toastMessage(String message) {

        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void confirmUser(final String Email, String confirmedPassword) {
        final String userEmail = Email;
        final String mPassword = confirmedPassword;
        Log.i("Pass,email 4m DCC",confirmedPassword +"  " +Email);
        AuthCredential authCredential=EmailAuthProvider
                .getCredential(firebaseUser.getEmail(),confirmedPassword);
        firebaseUser.reauthenticate(authCredential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG," User Re_Authenticated");
                            /*try {
                                if (!editUserEmail.getText().toString().isEmpty()){
                                    // check if new email is not already in use

                                    firebaseAuth.fetchSignInMethodsForEmail(editUserEmail.getText().toString())
                                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                                                    boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                                                    if (isNewUser) {
                                                        Log.e("TAG", "Email is Available!");
                                                        firebaseUser.updateEmail(editUserEmail.getText().toString())
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        Toast.makeText(getContext(),"Email Updated",Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                    } else {
                                                        Log.e("TAG", "Email already exists!");
                                                    }

                                                }
                                            });
                                }
                                else {
                                    Toast.makeText(getContext(),"Email is not Updated",Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch (NullPointerException e){
                                Log.e(TAG,"Catch: on Email NullException" +e.getMessage());
                            }*/

                            try {
                                password = addUserNewPassword.getText().toString();
                                if (mPassword != password){

                                    firebaseUser.updatePassword(addUserNewPassword.getText().toString())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(getContext(),"Password Updated",Toast.LENGTH_SHORT).show();

                                                        firebaseAuth.signOut();
                                                        startActivity(new Intent(getContext(), SignIn.class));
                                                    }
                                                    else {
                                                        toastMessage("Can't Update Password");
                                                    }
                                                }
                                            });
                                }
                                else{
                                    toastMessage("Must enter new Password");
                                }
                            }
                            catch (NullPointerException e){
                                Log.e(TAG,"Catch: on Password nullException" +e.getMessage());
                            }

                        }
                        else {
                            Log.d(TAG," User Re_Authentication failed");
                        }
                    }
                });

    }

}
