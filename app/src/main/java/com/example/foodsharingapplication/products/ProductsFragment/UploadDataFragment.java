package com.example.foodsharingapplication.products.ProductsFragment;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.TextView;
import android.widget.Toast;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.model.UploadModel;
import com.example.foodsharingapplication.products.UploadData;
import com.example.foodsharingapplication.products.ViewPageAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

import me.abhinay.input.CurrencyEditText;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadDataFragment extends Fragment {

    /*Model Class*/
    UploadModel uploadModel;

    /*Firebase*/
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    /*Layouts*/
    private LinearLayout imageViewLayout, buttonLayout;
    RelativeLayout viewPagerLayout;

    /*Buttons*/
    private Button chooseImage, btnChoose, btnSubmit;

    /*Image View*/
    ImageView imageView,imgView, mainImageView;

    /*EditText*/
    private static CurrencyEditText editTextPrice;
    private static EditText editTextTitle, editTextDescription, editTextPickUpDetails;

    /*Radio UI*/
    private RadioGroup foodTypeRadioGroup,paymentGroup;
    private RadioButton cookedFoodRadioBtn, rawFoodRadioBtn,paypalOption,bankTransferOption,cashOnDeliveryOption;

    /*Spinner UI*/
    private SmartMaterialSpinner cuisineTypeSpinner, listFoodSpinner;

    /*ViewPager*/
    ViewPager viewPager;
    ViewPageAdapter adapter = null;

    /*Other Variable Declaration*/
    private String cuisine_type,days,title,description,pickUpDetails,price;

    /*ArrayList*/
    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
    Uri mImageUri;
    int uploadCount = 0;

    /*Other Static declarations*/
    private static final int MULTIPLE_IMAGE_REQUEST = 2;
    ProgressDialog progressDialog;
    public UploadDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_upload_data,container,false);


        /*Model Class*/
        uploadModel = new UploadModel();

        /*Layouts*/
        viewPagerLayout = (RelativeLayout)view.findViewById(R.id.test1);
        imageViewLayout = (LinearLayout) view.findViewById(R.id.imageViewLayout);
        buttonLayout = (LinearLayout) view.findViewById(R.id.layout_buttons);

        /*ViewPager*/
        viewPager = view.findViewById(R.id.flipperView);
        adapter = new ViewPageAdapter(getContext(), mArrayUri);
        viewPager.setAdapter(adapter);

        /*ImageView*/
        imageView = (ImageView)view.findViewById(R.id.mainImage);

        /*EditTexts*/
        editTextTitle = (EditText) view.findViewById(R.id.editTextTitle);
        editTextDescription = (EditText) view.findViewById(R.id.editTextDescription);
        editTextPickUpDetails = (EditText) view.findViewById(R.id.editTextPickUpDetails);
        editTextPrice = (CurrencyEditText)view.findViewById(R.id.etPrice);
        editTextPrice.setCurrency("€");
        editTextPrice.setDelimiter(false);
        editTextPrice.setSpacing(false);
        editTextPrice.setDecimals(true);
        //Make sure that Decimals is set as false if a custom Separator is used
        editTextPrice.setSeparator(".");


        /*Buttons*/
        btnChoose = (Button) view.findViewById(R.id.btnChoosePhoto);
        btnSubmit = (Button) view.findViewById(R.id.submitData);

        /*RadioGroups*/
        /*FoodType*/
        foodTypeRadioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        cookedFoodRadioBtn = (RadioButton) view.findViewById(R.id.radioCookedFood);
        rawFoodRadioBtn = (RadioButton) view.findViewById(R.id.radioRawFood);
        /*Payment*/
        paymentGroup = (RadioGroup) view.findViewById(R.id.radioGroupPaymentMethods);
        paypalOption = (RadioButton) view.findViewById(R.id.paypal);
        bankTransferOption = (RadioButton) view.findViewById(R.id.bankTransfer);
        cashOnDeliveryOption = (RadioButton) view.findViewById(R.id.cashOnDelivery);

        /*Spinners*/
        cuisineTypeSpinner = (SmartMaterialSpinner) view.findViewById(R.id.cuisine_spinner);
        listFoodSpinner = (SmartMaterialSpinner) view.findViewById(R.id.days_spinner);
        initCuisineSpinner();
        initDaysSpinner();

        /*ProgressDialog*/
        progressDialog = new ProgressDialog(getContext());

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
                postingData();
                //dataToDB();
            }
        });

        foodTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (cookedFoodRadioBtn.isChecked()) {
                    /*sData.setCookedFood("Cooked Food");*/
                    uploadModel.setFoodType("CookedFood");
                } else {
                    /*sData.setCookedFood("Raw Food");*/
                    uploadModel.setFoodType("RawFood");
                }
            }
        });

        paymentGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (paypalOption.isChecked()){
                    uploadModel.setPayment("Paypal");
                }else if(bankTransferOption.isChecked()){
                    uploadModel.setPayment("Bank Transfer");
                }else if(cashOnDeliveryOption.isChecked()){
                    uploadModel.setPayment("Cash On Delivery");
                }
            }
        });

        return view;
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
                imageViewLayout.setVisibility(View.GONE);
                buttonLayout.setVisibility(View.GONE);

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) viewPagerLayout.getLayoutParams();
                lp.height = (int) convertDpToPixelInt(300,getContext()); //convert pixel to dp
                viewPagerLayout.setLayoutParams(lp);

                adapter.notifyDataSetChanged();

            }
            else if (data.getData() != null) {
                mImageUri = data.getData();
                Bitmap new_bitmap = null;
                try {

                    new_bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),mImageUri);
                    imageView.setImageBitmap(new_bitmap);
                    imageViewLayout.setVisibility(View.GONE);
                    viewPager.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    buttonLayout.setVisibility(View.GONE);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                lp.height = (int) convertDpToPixelInt(300,getContext()); //convert pixel to dp
                viewPagerLayout.setLayoutParams(lp);


                String imagePath = data.getData().getPath();
                Log.i("count", "imagePath:" + imagePath);

            }

        }
    }

    /*EditText init()*/
    private void initEditText(){

        uploadModel.setFoodTitle(editTextTitle.getText().toString().trim());

        Log.i("description of food", editTextDescription.getText().toString());
        uploadModel.setFoodDescription(editTextDescription.getText().toString().trim());

        Log.i("pickUpDetails of food", editTextPickUpDetails.getText().toString());
        uploadModel.setFoodPickUpDetail(editTextPickUpDetails.getText().toString().trim());

        Log.i("price of food", editTextPrice.getText().toString());
        uploadModel.setFoodPrice(editTextPrice.getText().toString());
    }

    /*Spinner init()*/
    private void initCuisineSpinner() {

        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        String country;
        for (Locale loc : locale) {
            country = loc.getDisplayCountry();
            if (country.length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        ArrayAdapter<String> countries_adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, countries);
        cuisineTypeSpinner.setAdapter(countries_adapter);

        cuisineTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                uploadModel.setFoodTypeCuisine(cuisineTypeSpinner.getSelectedItem().toString());
                /*cuisine_type = cuisineTypeSpinner.getSelectedItem().toString();*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, arrayList);
        //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listFoodSpinner.setAdapter(arrayAdapter);

        listFoodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                uploadModel.setAvailabilityDays(listFoodSpinner.getSelectedItem().toString());
                /*days = listFoodSpinner.getSelectedItem().toString();*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /*Posting Data*/
    private void postingData(){

        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        StorageReference ImageFolder = FirebaseStorage.getInstance().getReference("SellerImageFolder").child("Images");

        Log.i("Checking Storage", String.valueOf(ImageFolder));

        if (!mArrayUri.isEmpty()){
            for (uploadCount = 0; uploadCount < mArrayUri.size(); uploadCount++){
                final Uri individualImage =  mArrayUri.get(uploadCount);
                Log.i("Individual Image Uri:", String.valueOf(individualImage));
                final StorageReference imageName = ImageFolder.child("Image"+ individualImage.getLastPathSegment());

                imageName.putFile(individualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = String.valueOf(uri);
                                Log.i("images Url", url);
                                //sData.setUri(mArrayUri);
                                //uploadModel.setmArrayUri(mArrayUri.get());

                                HashMap<String,String> hashMap = new HashMap<>();
                                hashMap.put("ImgLink",url);
                                uploadModel.setHashMap(hashMap);
                                storeLink();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        Log.i("Progress","checking progress" + progress);
                        progressDialog.setMessage("Uploaded" + (int) progress + "%");
                    }
                });

            }
        }
        else if(mImageUri != null){

            final StorageReference singleImageName = ImageFolder.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));

            singleImageName.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    singleImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String url = String.valueOf(uri);
                            Log.i("image Url",url);
                            uploadModel.setmImageUri(url);
                            storeLink();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    Log.i("Progress","checking progress" + progress);
                    progressDialog.setMessage("Uploaded" + (int) progress + "%");
                }
            });
        }
        else{
            Toast.makeText(getContext(),"No image selected",Toast.LENGTH_SHORT).show();
        }
    }

    /*Getting File Extension for single Image Uri*/
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    /*Sending Data to DB*/
    private void storeLink(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Seller").child("User");

        initEditText();
        databaseReference.push().setValue(uploadModel);

        progressDialog.dismiss();
        Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
    }



}
