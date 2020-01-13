package com.example.foodsharingapplication.authentication.AuthemnticationFragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import static com.facebook.AccessTokenManager.TAG;

public class DialogeConfirmCredentials extends AppCompatDialogFragment {
    private EditText txtLoginEmail, txtLoginPassword;
    private String userEmail,userNewName,userPassword;
    private ImageView loginLogoImage;
    private TextView txtLogin, goToRegister;
    private Button btnLogin;
    DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ConfirmUserCredentials confirmUserCredentials;
    private Context context;

    public DialogeConfirmCredentials(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        final View view = inflater.inflate(R.layout.activity_sign_in, null);
        builder.setView(view)
                .setTitle("Confirm Credentials")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                    //startActivity( new Intent(getContext(), Profile.class));
                    Toast.makeText(getContext(), "Please check Email or Password", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        if (!txtLoginEmail.getText().toString().equals("") && !txtLoginPassword.getText().toString().equals("")) {

                            firebaseAuth.signInWithEmailAndPassword(txtLoginEmail.getText().toString(), txtLoginPassword.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                String email = txtLoginEmail.getText().toString();
                                                String password= txtLoginPassword.getText().toString();
                                                confirmUserCredentials.confirmUser(email, password);

                                                //insertUpdatedData();
                                                //againLogin();
                                                Log.i("DbOX",txtLoginEmail.getText().toString() +" ");

                                            } else {
                                                showToast("Something went wrong with your email or password");

                                            }
                                        }
                                    });
                    /*fragTrans.add(R.id.fragment_container,showData_fragment);
                    fragTrans.commit();*/

                        } else {
                            Toast.makeText(context, "Please enter valid Credentials", Toast.LENGTH_SHORT).show();
                            getDialog().show();
                        }
                    }
                });

        txtLoginEmail = view.findViewById(R.id.enterEmail);
        txtLoginPassword = view.findViewById(R.id.enterLoginPassword);
        loginLogoImage = view.findViewById(R.id.logoImg);
        txtLogin = view.findViewById(R.id.loginText);
        goToRegister = view.findViewById(R.id.txtRegister);
        btnLogin = view.findViewById(R.id.btnLogin);

        loginLogoImage.setVisibility(View.GONE);
        txtLogin.setVisibility(View.GONE);
        goToRegister.setVisibility(View.GONE);
        btnLogin.setVisibility(View.GONE);


        return builder.create();
    }
    public void showToast(String message){

        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            confirmUserCredentials = (ConfirmUserCredentials) getTargetFragment();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach ClassCastException" + e.getMessage());
        }
    }

    public void insertUpdatedData(){

        firebaseUser.updateEmail(getUserEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                message("Email Updated");
            }
        });
        firebaseUser.updatePassword(getUserPassword())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    message("Password Update");
                }
            }
        });
        User userData = new User();
        userData.setUserName(getUserNewName());
        getDatabaseReference().child(firebaseAuth.getCurrentUser().getUid()).setValue(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //startActivity(new Intent(getContext(), Profile.class));
                        message("Profile Updated successfully");
                    }
                });
    }
    /*public void againLogin(){



        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        startActivity(new Intent(getContext(), SignIn.class));
        message("Login with New Credentials");

    }*/
    /*public void reAuthenticateUser(){
        AuthCredential authCredential = EmailAuthProvider
                .getCredential(txtLoginEmail.getText().toString(),txtLoginPassword.getText().toString());
        this.show(getFragmentManager(),"Login with New Credentials");
        firebaseAuth.getCurrentUser().reauthenticate(authCredential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        message("User Re_authenticated");
                    }
                });

    }*/
    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userNewEmail) {

        this.userEmail = userNewEmail;
    }

    public String getUserNewName() {
        return userNewName;
    }

    public void setUserNewName(String userNewName) {

        message("New Password");
        message(userNewName);
        this.userNewName = userNewName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        message("New password");
        message(userPassword);
        this.userPassword = userPassword;
    }
    public void message(String message){

        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();

    }
    public interface ConfirmUserCredentials{
        void confirmUser(String Email,String password);
    }
}
