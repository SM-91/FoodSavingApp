package com.example.foodsharingapplication.authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.foodsharingapplication.Dashboard;
import com.example.foodsharingapplication.extras.PhoneVerificationFragment;
import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.extras.ShowData_Fragment;
import com.example.foodsharingapplication.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.OnCountryPickerListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity implements View.OnClickListener, OnCountryPickerListener {
    private static final int PICK_IMAGE_REQUEST = 1;
    EditText fName, lName, email, pass, cPass;
    Button btnRegister, btnCancel;
    User userData;
    FirebaseAuth firebaseAuth;
    ShowData_Fragment showDataInFragment = new ShowData_Fragment();
    PhoneVerificationFragment phoneVerificationFragment = new PhoneVerificationFragment();
    Bundle dataBundle = new Bundle();
    FragmentManager fragManger = getSupportFragmentManager();
    final FragmentTransaction fragTrans = fragManger.beginTransaction();
    ProgressDialog progress;
    ProgressBar progressBar;
    FirebaseStorage firebasestorage;
    StorageReference firebaseStorageReference;
    DatabaseReference firebaseDatabaseRef;
    EditText imagename;
    private EditText phoneNumber;
    private Button btnChoose, btnUpload;
    private ImageView profileImageView;
    private Uri filePath;
    private String imageUploadId;
    private StorageTask uploadImageTask;
    private String radioCheckedValues;
    private String datePicker;
    private RadioGroup sexSelectorGroup;
    private RadioButton radioMale, radioFemale, radioDiverse;
    private Button btnCountry_picker;
    private ImageView countryFlag;
    private CountryPicker countryPicker;
    private TextView txtCountryName,txtPhoneCode,txtCountryISO,txtVerificationField;
    private String strCountry_isoCode,strCountry_DialCode,strCountry_Name,strCountry_Currency;

    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private  Button btnvarifyCode;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toast.makeText(Register.this, "FireBase Connected successfully ", Toast.LENGTH_SHORT).show();

        fName = findViewById(R.id.firstName);
        lName = findViewById(R.id.lastName);
        email = findViewById(R.id.enterEmail);
        pass = findViewById(R.id.enterPassword);
        cPass = findViewById(R.id.confirmPassword);
        phoneNumber = findViewById(R.id.phoneMobile);
        btnRegister = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);
        btnChoose = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        progressBar = findViewById(R.id.progress_bar);
        profileImageView = findViewById(R.id.profileImgView);
        final ImageView logoImage = findViewById(R.id.logoImg);
        radioMale = findViewById(R.id.radio_male);
        radioFemale = findViewById(R.id.radio_female);
        radioDiverse = findViewById(R.id.radio_diverse);
        sexSelectorGroup = findViewById(R.id.sexBtnGroup);
        //txtVerificationField = (TextView) findViewById(R.id.mVerificationField);
        //btnvarifyCode = (Button) findViewById(R.id.varifyCode);


        //Country with Flag Picker starts

        btnCountry_picker=(Button) findViewById(R.id.countryPicker);
        countryFlag=(ImageView) findViewById(R.id.selected_country_flag_image_view);
        txtCountryName = (TextView) findViewById(R.id.countryName);
        txtPhoneCode = (TextView) findViewById(R.id.phoneCode);
        txtCountryISO = (TextView) findViewById(R.id.countryISO);
        btnCountry_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryPicker.showDialog(getSupportFragmentManager());
            }
        });
        countryPicker = new CountryPicker.Builder().with(this).listener(this).build();

        // Country with flag Picker ends
        /*mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                mVerificationInProgress = false;
                Toast.makeText(Register.this,"Varifies",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                mVerificationInProgress = false;

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    phoneNumber.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                }
                else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                mVerificationId = verificationId;
                mResendToken = token;
            }
        };*/
        //imagename.setVisibility(View.GONE);
        userData = new User();
        firebaseAuth = FirebaseAuth.getInstance();
        firebasestorage = FirebaseStorage.getInstance();
        firebaseStorageReference = firebasestorage.getReference("User");
        firebaseDatabaseRef = FirebaseDatabase.getInstance().getReference("User");
        btnCancel.setOnClickListener(this);
        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait!!");
        progress.setMessage("Wait!!");
        progress.setCancelable(true);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                progress.cancel();
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 1000);

        sexSelectorGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (radioMale.isChecked()) {
                    radioCheckedValues = "Male";
                } else if (radioFemale.isChecked()) {
                    radioCheckedValues = "Female";
                } else if (radioDiverse.isChecked()) {
                    radioCheckedValues = "Diverse";
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(Register.this, FinalSignUp.class);
                if (TextUtils.isEmpty(fName.getText().toString())) {

                    fName.setError("First name Can't be empty");
                    fName.requestFocus();
                } else if (TextUtils.isEmpty(lName.getText().toString())) {

                    lName.setError("Last Name name Can't be empty");
                    lName.requestFocus();

                } else {

                    intent.putExtra("name", fName.getText().toString() + lName.getText().toString());

                }
                if (!TextUtils.isEmpty(email.getText().toString())) {

                    if (isEmailValid(email.getText().toString())) {

                        intent.putExtra("uEmail", email.getText().toString().trim());

                    } else {

                        email.setError("Enter a valid email address");
                        email.requestFocus();

                    }

                } else {

                    email.setError("Email Can't be empty");
                    email.requestFocus();

                }
                if (!TextUtils.isEmpty(phoneNumber.getText().toString())) {
                    //startPhoneNumberVerification(phoneNumber+strCountry_DialCode);
                    intent.putExtra("uPhoneNumber", phoneNumber.getText().toString().trim());

                } else {

                    phoneNumber.setError("Phone Number Can't be empty");
                    phoneNumber.requestFocus();

                }
                if (!TextUtils.isEmpty(pass.getText().toString())) {
                    if (pass.getText().toString().length() >= 6) {

                        if (!TextUtils.isEmpty(cPass.getText().toString())) {

                            if (pass.getText().toString().equals(cPass.getText().toString())) {

                                intent.putExtra("uPassword", pass.getText().toString().trim());

                            } else {

                                cPass.setError("Password Does not match");
                                cPass.requestFocus();
                                pass.setError("Password Does not match");
                                pass.requestFocus();

                            }

                        } else {

                            cPass.setError("Confirm Password Can't be empty");
                            cPass.requestFocus();

                        }
                    } else {

                        pass.setError("Password must contain at least 6 characters");
                        pass.requestFocus();

                    }

                } else {

                    pass.setError("Password Can't be empty");
                    pass.requestFocus();

                }
                if (radioDiverse.isChecked() || radioFemale.isChecked() || radioMale.isChecked()) {

                    intent.putExtra("uGender", radioCheckedValues.trim());

                } else {

                    radioMale.setError("Gender Can't be empty");
                    radioDiverse.requestFocus();
                    radioFemale.requestFocus();
                    radioMale.requestFocus();

                }
                if (!strCountry_Currency.isEmpty() && !strCountry_DialCode.isEmpty()
                        && !strCountry_isoCode.isEmpty() && !strCountry_Name.isEmpty()){

                    intent.putExtra("country_Currency", strCountry_Currency.trim());
                    intent.putExtra("country_DialCode", strCountry_DialCode.trim());
                    intent.putExtra("country_isoCode", strCountry_isoCode.trim());
                    intent.putExtra("country_Name", strCountry_Name.trim());

                }
                else{

                    btnCountry_picker.setError("select a country");
                    btnCountry_picker.requestFocus();

                }
                if (intent.getExtras() != null && intent.getExtras().containsKey("name")
                        && intent.getExtras().containsKey("uEmail") && intent.getExtras().containsKey("uPhoneNumber")
                        && intent.getExtras().containsKey("uPassword") && intent.getExtras().containsKey("uGender")) {

                    startActivity(intent);

                }
            }
        });
        /*btnvarifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code =txtPhoneCode.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    txtVerificationField.setError("Cannot be empty.");
                    return;
                }

                verifyPhoneNumberWithCode(mVerificationId, code);
            }
        });*/

    }
    /*private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }
    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }
*/
    //Country picker implementation
    @Override
    public void onSelectCountry(Country country){
        countryFlag.setImageResource(country.getFlag());
        txtCountryName.setVisibility(View.VISIBLE);
        countryFlag.setVisibility(View.VISIBLE);
        txtPhoneCode.setVisibility(View.VISIBLE);
        txtCountryISO.setVisibility(View.VISIBLE);
        txtCountryName.setText(country.getName());
        userData.setUserCountry(txtCountryName.toString());
        txtPhoneCode.setText(country.getDialCode());
        txtCountryISO.setText(country.getCode());
        userData.setUserCountryCurrency(country.getCurrency());
        userData.setUserCountryDialCode(country.getDialCode());
        userData.setUserCountryISO(country.getCode());

        strCountry_Currency= country.getCurrency();
        strCountry_DialCode=country.getDialCode();
        strCountry_isoCode = country.getCode();
        strCountry_Name=country.getName();
        Log.i("Country Currency:",strCountry_Currency);
        Log.i("Country DialCode:",strCountry_DialCode);
        Log.i("Country ISO Code:",strCountry_isoCode);
        Log.i("Country Name:",strCountry_Name);
    }


    //Country picker implementation
    private boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            toastMessage("please enter valid Email ");

        }
        return matcher.matches();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                startActivity(new Intent(this, Dashboard.class));
                break;
        }
    }

    private void toastMessage(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
