package com.example.foodsharingapplication.authentication;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.foodsharingapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

public class Authentication_Firebase {

    private StorageTask uploadImageTask;
    private FirebaseStorage firebasestorage;
    private StorageReference firebaseStorageReference;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference firebaseDatabaseRef;
    private FirebaseAuth firebaseAuth;
    private String currentUserid;
    private Uri firebaseUri;
    private Context context;
    private FirebaseUser firebaseUser;
    private User userData;
    private Boolean notifyRegistration = false;
    private FirebaseAuth.AuthStateListener mFirebaseAuthListener;
    private String currentUserId;
    private Intent intent;
    private int onSuccessRegisterCode = 1;

    public Authentication_Firebase(Context context) {

        this.context = context;
        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabaseRef = mFirebaseDatabase.getReference("User");

    }

    public Authentication_Firebase() {

        //empty constructor

        //setCurrentUserid(firebaseAuth.getCurrentUser());

    }

    public String getCurrentUserid() {
        return currentUserid = currentUserid;
    }

    public void setCurrentUserid(String currentUserid) {
        this.currentUserid = currentUserid;
    }

    public void setCurrentUserid(FirebaseUser currentUser) {
        this.currentUserid = firebaseAuth.getCurrentUser().getUid();
    }

    public Boolean registerToFirebase(User user, String userPassword) {
        String Password = userPassword;
        userData = new User();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabaseRef = FirebaseDatabase.getInstance().getReference("User");
        userData = user;
        try {
            firebaseAuth.createUserWithEmailAndPassword(user.getUserEmail(), Password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                setCurrentUserid(firebaseAuth.getCurrentUser());
                                //System.out.println("current user id in registerToFirebase "+currentUserid);
                                //if (!currentUserid.isEmpty()) {
                                //intent = new Intent(getContext(), HomeActivity.class);
                                firebaseDatabaseRef.child(firebaseAuth.getCurrentUser().getUid()).setValue(userData)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    notifyRegistration = true;
                                                    System.out.println("Register status in adding data to db " + notifyRegistration);
                                                }
                                            }
                                        });
                            /*} else {
                                //do on failure
                                Toast.makeText(getContext(), "Some Thing went wrong with DB", Toast.LENGTH_SHORT).show();
                            }*/
                            } else {
                                //do on failure
                                Toast.makeText(getContext(), "Email Already Exists", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (NullPointerException e) {
            Toast.makeText(context, "Email already exists!", Toast.LENGTH_SHORT).show();
        }
        System.out.println("Register status in registerToFirebase " + notifyRegistration);
        return notifyRegistration;

    }


    public Boolean insertIntoDatabase(User user) {

        firebaseDatabaseRef.child(firebaseAuth.getCurrentUser().getUid()).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            return;
                        }
                    }
                });

        return false;
    }

    public User storeImagesToDB(Uri filePath, Context applicationContext) {
        firebasestorage = FirebaseStorage.getInstance();
        firebaseStorageReference = firebasestorage.getReference("User");
        //if (filePath != null ) {
        System.out.println("file path entering into imageToDb " + filePath.toString());
        firebaseUri = filePath;
        context = applicationContext;
        final StorageReference ref = firebaseStorageReference.child(System.currentTimeMillis()
                + "." + getFileExtension(firebaseUri));


        uploadImageTask = ref.putFile(firebaseUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> downloadUriTask = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        downloadUriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                userData.setUserProfilePicUrl(uri.toString());
                            }
                        });
                        //imageUrl = Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl().toString();
                        //userData.setUserProfilePicName(name+"_"+email);
                        //userData.setUserProfilePicUrl(imageUrl);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //progressBar.dismiss();
                        Toast.makeText(context, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NotNull UploadTask.TaskSnapshot taskSnapshot) {
                            /*double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressBar.setProgress((int)progress);*/
                    }
                });
        //}
        System.out.println("Returning file path from imageToDb " + filePath.toString());
        return userData;
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

   /* public FirebaseUser getFirebaseCurrentUser(){
        mFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null){
                    Log.i(" AuthStateChanged login", firebaseUser.getUid());
                }
                else{
                    firebaseAuth.signOut();
                    Toast.makeText(getContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                    Authentication_Firebase.this.context.startActivity(new Intent(Authentication_Firebase.this.context,SignIn.class));
                    //   Log.i(" logOut", firebaseUser.getUid());
                }

            }
        };

        return firebaseUser;
    }*/

    //Profile start

    public User getCurrentUserProfile() {
        if (firebaseAuth.getCurrentUser() != null) {
            firebaseDatabaseRef.child(firebaseAuth.getCurrentUser().getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            userData = dataSnapshot.getValue(User.class);
                            Log.i("getCurrentUserProfile", userData.getUserEmail());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
        return userData;
    }

    //Profile end
}
