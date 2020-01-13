package com.example.foodsharingapplication.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodsharingapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class forgetPassword extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private EditText enterEmail;
    private Button btnCancel, btnSendNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        enterEmail = (EditText) findViewById(R.id.enterEmail);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSendNewPassword = (Button) findViewById(R.id.btnSendPassword);

        firebaseAuth = FirebaseAuth.getInstance();

        btnSendNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.sendPasswordResetEmail(enterEmail.getText().toString().trim()).
                        addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(forgetPassword.this, "Please check your Password", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(forgetPassword.this, SignIn.class);
                                    startActivity(intent);

                                } else
                                    Toast.makeText(forgetPassword.this, "Please Re-Enter Email", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
