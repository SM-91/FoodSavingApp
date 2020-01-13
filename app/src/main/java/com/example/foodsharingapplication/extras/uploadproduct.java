package com.example.foodsharingapplication.extras;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.authentication.SignIn;
import com.example.foodsharingapplication.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Maps;
import com.google.firebase.auth.FirebaseAuth;
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
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class uploadproduct extends AppCompatActivity{
    private Maps maps;
    private Button btnchooseImage, btnUploadImage,btnSubmitProduct;
    private EditText txtFoodTitle, txtFoodDescription,txtfoodLocation,txtFoodPrice;
    private ImageView foodImagesView;
    private Spinner foodRegion;
    private FirebaseStorage firebasestorage;
    private StorageReference firebaseStorageReference;
    private DatabaseReference firebaseDatabaseRef;
    private FirebaseAuth firebaseAuth;
    private static final int PICK_IMAGE_REQUEST = 10;
    private Uri filePath;
    private StorageTask uploadImageTask;
    private ProgressBar progressBar;
    private String imageUrl, imageName;
    private Products products;
    private String selectedRegion="";
    private TextView txtViewselectPlace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadproduct);

        btnchooseImage = (Button) findViewById(R.id.chooseProductImg);
        btnUploadImage = (Button) findViewById(R.id.uploadProductImg);
        btnSubmitProduct = (Button) findViewById(R.id.submitProduct);
        txtFoodTitle = (EditText) findViewById(R.id.foodName);
        txtFoodPrice = (EditText) findViewById(R.id.foodPrice);
        txtFoodDescription = (EditText) findViewById(R.id.foodDescription);
        txtfoodLocation = (EditText) findViewById(R.id.foodLocation);
        foodImagesView = (ImageView) findViewById(R.id.viewProductImage);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar );
        foodRegion=(Spinner) findViewById(R.id.foodRegion);
        //txtViewselectPlace = (TextView) findViewById(R.id.selectFoodPlace);

        /*Fragment mapFragment = new com.example.myfirstapplication.Maps();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mapFragment ); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();*/

        //maps = new Maps();
        products=new Products();

        //Firebase Connections
        firebaseAuth = FirebaseAuth.getInstance();
        firebasestorage= FirebaseStorage.getInstance();
        firebaseStorageReference=firebasestorage.getReference("Products");
        firebaseDatabaseRef = FirebaseDatabase.getInstance().getReference("Products");

        // get countries for regional food specification

        Locale[] locales= Locale.getAvailableLocales();
        final ArrayList<String> countries=new ArrayList<String>();
        for (Locale local: locales){
            String country=local.getDisplayCountry();
            if (country.trim().length()>0 && !countries.contains(country)){

                countries.add(country);
            }

        }
        Collections.sort(countries);

        Log.i(" Country ", String.valueOf(countries.size()));
        for (String country: countries){
            Log.i(" Country ", country);
        }
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, countries);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the your spinner
        foodRegion.setAdapter(countryAdapter);

        foodRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRegion= foodRegion.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //submit entry
        btnSubmitProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //assignment of inserted data
                String foodTitle=txtFoodTitle.getText().toString().trim();
                String foodDescription=txtFoodDescription.getText().toString().trim();
                String foodLocation=txtfoodLocation.getText().toString().trim();
                String foodPrice=txtFoodPrice.getText().toString().trim();

           /*     Intent intent=new Intent(uploadproduct.this,productdetails.class);
                intent.putExtra("title",foodTitle);
                intent.putExtra("foodDescription",foodDescription);
                intent.putExtra("foodLocation",foodLocation);
                intent.putExtra("foodPrice",foodPrice);
                intent.putExtra("foodRegion",foodRegion.toString());*/

                if (firebaseAuth.getCurrentUser() != null){

                    String id= firebaseDatabaseRef.push().getKey();
                    products.setProductName(foodTitle);
                    products.setProductDescription(foodDescription);
                    products.setProdcutRegion(selectedRegion);
                    products.setProductLocation(foodLocation);

                    firebaseDatabaseRef.child(id).setValue(products);
                    Toast.makeText(uploadproduct.this,"Product Inserted Successfully",Toast.LENGTH_SHORT).show();
                    Intent intentshowProducts=new Intent(uploadproduct.this,showproducts.class);
                    intentshowProducts.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentshowProducts.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentshowProducts);
                    /*firebaseDatabaseRef.child(firebaseAuth.getCurrentUser().getUid()).setValue(products)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });*/
                }

                else{
                    Intent intentSignIn=new Intent(uploadproduct.this, SignIn.class);
                    startActivity(intentSignIn);
                }
               /* foodImagesView.buildDrawingCache();
                Bitmap bitmap=foodImagesView.getDrawingCache();
                intent.putExtra("foodImageView",bitmap);*/
                //startActivity(intent);

            }
        });

        //get user's data and put into Products as foreign key

        firebaseDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (firebaseAuth.getCurrentUser()!=null){

                    User userData=dataSnapshot.getValue(User.class);
                    String userName=userData.getUserName();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //images stuff
        btnchooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseProdcutImages();
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadImageTask != null && uploadImageTask.isInProgress()){
                    Toast.makeText(uploadproduct.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadProductImages();
                }
            }
        });


    }

    private void chooseProdcutImages() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null ){
            filePath = data.getData();
            Picasso.get().load(filePath).into(foodImagesView);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                foodImagesView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    //For image extension e.g. .jpg or .png
    private  String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadProductImages() {
        if(filePath != null){
            final StorageReference ref = firebaseStorageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(filePath));
            uploadImageTask = ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 2000);

                            //progressDialog.dismiss();

                            //imageName = imagename.getText().toString().trim();
                            Task<Uri> dounloadUriTask= taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            dounloadUriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageUrl = uri.toString();
                                    //Log.e("download url : ", imageUrl);
                                    products.setProductImageUri(imageUrl);
                                }
                            });
                            //imageUrl = Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl().toString();
                            //products.setProductImageName(name+"_"+email);
                            //userData.setUserProfilePicUrl(imageUrl);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //progressBar.dismiss();
                            Toast.makeText(uploadproduct.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NotNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressBar.setProgress((int)progress);
                        }
                    });
        }
        else {
            Toast.makeText(this,"No file selected", Toast.LENGTH_SHORT).show();
        }
    }

}
