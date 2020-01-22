package com.example.foodsharingapplication.products;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.model.User;
import com.example.foodsharingapplication.model.UserUploadFoodModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import me.abhinay.input.CurrencyEditText;

public class UpdateUserFood extends AppCompatActivity {

    /*Model Class*/
    UserUploadFoodModel userUploadFoodModel;

    FirebaseAuth firebaseAuth;

    private User currentUser = null;

    /*Layouts*/
    private LinearLayout imageViewLayout, buttonLayout;
    RelativeLayout viewPagerLayout;

    /*Buttons*/
    private Button chooseImage, btnChoose, btnSubmit;

    /*Image View*/
    ImageView imageView, imgView, mainImageView;

    /*EditText*/
    private static CurrencyEditText editTextPrice;
    private static EditText editTextTitle, editTextDescription, editTextPickUpDetails;

    /*Radio UI*/
    private RadioGroup foodTypeRadioGroup, paymentGroup;
    private RadioButton cookedFoodRadioBtn, rawFoodRadioBtn, paypalOption, bankTransferOption, cashOnDeliveryOption;

    /*Spinner UI*/
    private SmartMaterialSpinner cuisineTypeSpinner, listFoodSpinner;

    /*ViewPager*/
    ViewPager viewPager , updateViewPager;
    ViewPageAdapter adapter = null;
    ViewPageAdapter updateImagesAdapter = null;

    /*Other Variable Declaration*/
    private String cuisine_type, days, title, description, pickUpDetails, price;

    /*ArrayList*/
    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
    final ArrayList<String> arrayList = new ArrayList<>();
    Uri mImageUri;
    int uploadCount = 0;

    /*Other Static declarations*/
    private static final int MULTIPLE_IMAGE_REQUEST = 2;
    ProgressDialog progressDialog;

    private String ad_id = " ";
    private String user_id = " ";
    private ArrayList<String> listImages = new ArrayList<>();

    ArrayAdapter<String> cuisineAdapter;
    private String[] cuisines = {"Asian" , "Italian", "French", "FastFood", "German", "Continental",
            "South American", "Arabic", "African"};

    ArrayAdapter<String> foodListingAdapter;
    private String foodUploadDateAndTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_food);

        /*Model Class*/
        userUploadFoodModel = new UserUploadFoodModel();
        firebaseAuth = FirebaseAuth.getInstance();

        /*Layouts*/
        viewPagerLayout =  findViewById(R.id.test1);
        imageViewLayout =  findViewById(R.id.imageViewLayout);
        buttonLayout =  findViewById(R.id.layout_buttons);

        /*ViewPager*/
        viewPager = findViewById(R.id.flipperView);
        adapter = new ViewPageAdapter(getApplicationContext(), mArrayUri);
        viewPager.setAdapter(adapter);

        /*ImageView*/
        imageView = findViewById(R.id.mainImage);

        /*EditTexts*/
        editTextTitle =  findViewById(R.id.editTextTitle);
        editTextDescription =  findViewById(R.id.editTextDescription);
        editTextPickUpDetails = findViewById(R.id.editTextPickUpDetails);
        editTextPrice =  findViewById(R.id.etPrice);
        editTextPrice.setCurrency("€");
        editTextPrice.setDelimiter(false);
        editTextPrice.setSpacing(false);
        editTextPrice.setDecimals(true);
        //Make sure that Decimals is set as false if a custom Separator is used
        editTextPrice.setSeparator(".");


        /*Buttons*/
        btnChoose = findViewById(R.id.btnChoosePhoto);
        btnSubmit = findViewById(R.id.submitData);

        /*RadioGroups*/
        /*FoodType*/
        foodTypeRadioGroup = findViewById(R.id.radioGroup);
        cookedFoodRadioBtn =  findViewById(R.id.radioCookedFood);
        rawFoodRadioBtn = findViewById(R.id.radioRawFood);
        /*Payment*/
        paymentGroup = findViewById(R.id.radioGroupPaymentMethods);
        paypalOption = findViewById(R.id.paypal);
        bankTransferOption = findViewById(R.id.bankTransfer);
        cashOnDeliveryOption = findViewById(R.id.cashOnDelivery);

        /*Spinners*/
        cuisineTypeSpinner = findViewById(R.id.cuisine_spinner);
        cuisineAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,
                cuisines);
        cuisineTypeSpinner.setAdapter(cuisineAdapter);
        cuisineTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String cuisineType = parent.getItemAtPosition(position).toString();
                userUploadFoodModel.setFoodTypeCuisine(cuisineType);
                /*cuisine_type = cuisineTypeSpinner.getSelectedItem().toString();*/
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listFoodSpinner = findViewById(R.id.days_spinner);
        initDaysSpinner();

        /*ProgressDialog*/
        progressDialog = new ProgressDialog(getApplicationContext());

        /*Getting Data For Update*/
        getUpdateData();

        /*Listeners*/
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImagesFromGallery();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mArrayUri.isEmpty()) {
                    uploadMultipleImages();
                } else if (mImageUri != null) {
                    uploadSingleImage();
                } else {
                    Toast.makeText(getApplicationContext().getApplicationContext(), "Please Select Image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        foodTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (cookedFoodRadioBtn.isChecked()) {
                    /*sData.setCookedFood("Cooked Food");*/
                    userUploadFoodModel.setFoodType("CookedFood");
                } else {
                    /*sData.setCookedFood("Raw Food");*/
                    userUploadFoodModel.setFoodType("RawFood");
                }
            }
        });

        paymentGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (paypalOption.isChecked()) {
                    userUploadFoodModel.setPayment("Paypal");
                } else if (bankTransferOption.isChecked()) {
                    userUploadFoodModel.setPayment("Bank Transfer");
                } else if (cashOnDeliveryOption.isChecked()) {
                    userUploadFoodModel.setPayment("Cash On Delivery");
                }
            }
        });

        /*Fetching User Model From Firebase*/
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);


                    if (firebaseAuth.getUid().equals(user.getUserId())) {
                        currentUser = user;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getUpdateData(){

        updateImagesAdapter = new ViewPageAdapter(getApplicationContext(), mArrayUri);
        viewPager.getOffscreenPageLimit();


        Bundle bundle = getIntent().getExtras();
        ad_id = getIntent().getStringExtra("foodAdId");
        String foodAvailability = getIntent().getStringExtra("foodAvailability");
        String foodDescription = getIntent().getStringExtra("foodDescription");
        String foodPickUpDetail = getIntent().getStringExtra("foodPickUpDetail");
        String foodPrice = getIntent().getStringExtra("foodPrice");
        String foodTitle = getIntent().getStringExtra("foodTitle");
        String foodType = getIntent().getStringExtra("foodType");
        String foodTypeCuisine = getIntent().getStringExtra("foodTypeCuisine");
        foodUploadDateAndTime = getIntent().getStringExtra("foodUploadDateAndTime");
        String foodPayment = getIntent().getStringExtra("foodPayment");
        String foodSingleImage = getIntent().getStringExtra("foodSingleImage");
        listImages = getIntent().getStringArrayListExtra("updateMultipleImagesList");
        user_id = getIntent().getStringExtra("user_id");

        if(bundle != null){

            if(foodSingleImage != null){
                imageView.setVisibility(View.VISIBLE);
                Picasso.get().load(foodSingleImage)
                        .fit()
                        .centerCrop()
                        .into(imageView);
                userUploadFoodModel.setmImageUri(foodSingleImage);

            } else {

                viewPager.setVisibility(View.VISIBLE);
                mArrayUri.clear();
                try{
                    for (int i = 0; i < listImages.size(); i++) {
                        Uri tem_uri = Uri.parse(listImages.get(i));
                        mArrayUri.add(tem_uri);
                    }

                    userUploadFoodModel.setmArrayString(listImages);
                    viewPager.setAdapter(updateImagesAdapter);
                    updateImagesAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    // This will catch any exception, because they are all descended from Exception
                    System.out.println("Error " + e.getMessage());
                    Toast.makeText(getApplicationContext(),"Error in multiple images" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            editTextTitle.setText(foodTitle);
            editTextDescription.setText(foodDescription);
            editTextPickUpDetails.setText(foodPickUpDetail);
            editTextPrice.setText(foodPrice);


            if(cookedFoodRadioBtn.isChecked()){
                cookedFoodRadioBtn.setChecked(true);
            } else {
                rawFoodRadioBtn.setChecked(true);
            }

            if(paypalOption.isChecked()){
                paypalOption.setChecked(true);
                userUploadFoodModel.setPayment("Paypal");
            } else if(bankTransferOption.isChecked()){
                bankTransferOption.setChecked(true);
                userUploadFoodModel.setPayment("Bank Transfer");
            } else {
                cashOnDeliveryOption.setChecked(true);
                userUploadFoodModel.setPayment(" Cash On Delivery");
            }

            if(foodTypeCuisine != null){
                int cuisinePositionInSpinner = cuisineAdapter.getPosition(foodTypeCuisine);
                cuisineTypeSpinner.setSelection(cuisinePositionInSpinner);
                userUploadFoodModel.setFoodTypeCuisine(foodTypeCuisine);
            }

            if(foodAvailability != null){
                int foodLisitngPositionInSpinner = foodListingAdapter.getPosition(foodAvailability);
                listFoodSpinner.setSelection(foodLisitngPositionInSpinner);
                userUploadFoodModel.setAvailabilityDays(foodAvailability);
            }
        }
    }

    /*Intent Function for Open Gallery*/
    private void selectImagesFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Images"), MULTIPLE_IMAGE_REQUEST);
    }

    /*Function for Converting Dp to Pixel*/
    public int convertDpToPixelInt(float dp, Context context) {
        return (int) (dp * (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f));
    }

    /*Activity Result for Image Intent*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MULTIPLE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                mArrayUri.clear();
                imageView.setImageBitmap(null);
                int count = data.getClipData().getItemCount();
                Log.i("count", String.valueOf(count));
                int currentItem = 0;
                while (currentItem < count) {
                    mImageUri = data.getClipData().getItemAt(currentItem).getUri();
                    Log.i("uri", mImageUri.toString());
                    mArrayUri.add(mImageUri);
                    currentItem = currentItem + 1;

                }
                Log.i("listsize", String.valueOf(mArrayUri.size()));

                imageView.setVisibility(View.GONE);
                viewPager.setVisibility(View.VISIBLE);

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) viewPagerLayout.getLayoutParams();
                lp.height = (int) convertDpToPixelInt(300, getApplicationContext()); //convert pixel to dp
                viewPagerLayout.setLayoutParams(lp);

                adapter.notifyDataSetChanged();

            } else if (data.getData() != null) {
                mImageUri = data.getData();
                Bitmap new_bitmap = null;
                try {

                    new_bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), mImageUri);
                    imageView.setImageBitmap(new_bitmap);
                    viewPager.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                lp.height = (int) convertDpToPixelInt(300, getApplicationContext()); //convert pixel to dp
                viewPagerLayout.setLayoutParams(lp);

                String imagePath = data.getData().getPath();
                Log.i("count", "imagePath:" + imagePath);
            }
        }
    }

    private void initDaysSpinner() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            if (i == 1) {
                arrayList.add(i + " day");
            } else {
                arrayList.add(i + " days");
            }
        }
        listFoodSpinner.setItem(Collections.singletonList(days));
        foodListingAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, arrayList);
        //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listFoodSpinner.setAdapter(foodListingAdapter);

        listFoodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                userUploadFoodModel.setAvailabilityDays(listFoodSpinner.getSelectedItem().toString());
                /*days = listFoodSpinner.getSelectedItem().toString();*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /*Upload Single Image*/
    private void uploadSingleImage() {

        StorageReference ImageFolder = FirebaseStorage.getInstance().getReference("SellerImageFolder").child("Images");

        final StorageReference singleImageName = ImageFolder.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));

        singleImageName.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                singleImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        String url = String.valueOf(uri);
                        Log.i("image Url", url);
                        userUploadFoodModel.setmImageUri(url);
                        updateFood();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                Log.i("Progress", "checking progress" + progress);
                progressDialog.setMessage("Uploaded" + (int) progress + "%");
            }
        });

    }

    /*Getting File Extension for single Image Uri*/
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    /*Upload Multiple Image*/
    private void uploadMultipleImages() {

        progressDialog = ProgressDialog.show(UpdateUserFood.this, "Posting Data",
                "Uploading..", true);

        StorageReference ImageFolder = FirebaseStorage.getInstance().getReference("SellerImageFolder").child("Images");

        arrayList.clear();
        for (uploadCount = 0; uploadCount < mArrayUri.size(); uploadCount++) {
            final Uri individualImage = mArrayUri.get(uploadCount);
            Log.i("Individual Image Uri:", String.valueOf(individualImage));
            final StorageReference imageName = ImageFolder.child("Image" + individualImage.getLastPathSegment());

            imageName.putFile(individualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = String.valueOf(uri);
                            Log.i("images Url", url);
                            //sData.setUri(mArrayUri);
                            //userUploadFoodModel.setmArrayUri(mArrayUri.get());
                            arrayList.add(url);

                            if (arrayList.size() == mArrayUri.size()) {
                                uploadDataWithMultipleImages();
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(UpdateUserFood.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    Log.i("Progress", "checking progress" + progress);
                    progressDialog.setMessage("Uploaded" + (int) progress + "%");
                }
            });

        }


    }

    private void uploadDataWithMultipleImages(){
        userUploadFoodModel.setmArrayString(arrayList);
        progressDialog.cancel();
        updateFood();
    }

    /*Sending Data to DB*/
    private void updateFood() {

        userUploadFoodModel.setFoodTitle(editTextTitle.getText().toString().trim());
        userUploadFoodModel.setFoodDescription(editTextDescription.getText().toString().trim());
        userUploadFoodModel.setFoodPickUpDetail(editTextPickUpDetails.getText().toString().trim());
        userUploadFoodModel.setFoodPrice(editTextPrice.getText().toString());
        userUploadFoodModel.setFoodUploadDateAndTime(foodUploadDateAndTime);
        userUploadFoodModel.setAdId(ad_id);
        userUploadFoodModel.setFoodPostedBy(currentUser);

        DatabaseReference userFoodUpdateReference;
        userFoodUpdateReference = FirebaseDatabase.getInstance().getReference("Food")
                .child("FoodByUser").child(firebaseAuth.getUid());
        userFoodUpdateReference.child(ad_id).setValue(userUploadFoodModel);

        DatabaseReference allFoodUpdateReference;
        allFoodUpdateReference = FirebaseDatabase.getInstance().getReference("Food")
                .child("FoodByAllUsers");
        allFoodUpdateReference.child(ad_id).setValue(userUploadFoodModel);

        Toast.makeText(UpdateUserFood.this, "Food Updated", Toast.LENGTH_SHORT).show();
        finish();

    }

}
