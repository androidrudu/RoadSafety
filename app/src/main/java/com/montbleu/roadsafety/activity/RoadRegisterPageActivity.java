package com.montbleu.roadsafety.activity;

import static com.montbleu.Utils.Constants.category_Password;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.montbleu.Retrofit.RestClient;
import com.montbleu.Retrofit.RestMethods;
import com.montbleu.Utils.Constants;
import com.montbleu.Utils.PreferenceManager;
import com.montbleu.Utils.Preference_Details;
import com.montbleu.Utils.ProgressDialog;
import com.montbleu.Utils.Util_functions;
import com.montbleu.interfaces.OnDialogButtonClickListener;
import com.montbleu.interfaces.Preference_Keys;
import com.montbleu.model.RegisterReq;
import com.montbleu.model.RegisterResponse;
import com.montbleu.roadsafety.R;
import com.nexyad.jndksafetynex.JNDKSafetyNex;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoadRegisterPageActivity extends AppCompatActivity implements View.OnClickListener, OnDialogButtonClickListener {
    private CheckBox checkbox_terms_condition;
    private androidx.appcompat.widget.AppCompatButton btnRoadSubmit;
    TextInputEditText et_name, et_email, et_mobilenumber, et_password;
    Boolean status_name = false, status_country = false, status_mob = false, status_pass = false;
    Boolean status_agree = false;
    RestMethods restMethods;
    Spinner spinner;
    TextView tv_terms;
    String mliceDate = "";

    Boolean mconnectStatus = false;
    TextInputLayout edit_password_layout;
    private PreferenceManager mPrefernce_Manager = PreferenceManager.getInstance();
    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    TextView txt_LoginNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_road_register_page);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        txt_LoginNow = findViewById(R.id.txt_LoginNow);
        txt_LoginNow.setOnClickListener(this);
        et_name = findViewById(R.id.et_name);
//        edit_country_layout = findViewById(R.id.et_country);
        et_email = findViewById(R.id.et_email);
        et_mobilenumber = findViewById(R.id.et_mobilenumber);
        et_password = findViewById(R.id.et_password);
        btnRoadSubmit = findViewById(R.id.btnRoadSubmit);
        btnRoadSubmit.setOnClickListener(this);
        tv_terms = findViewById(R.id.tv_terms);
        tv_terms.setOnClickListener(this);
        edit_password_layout = findViewById(R.id.edit_password_layout);
        ProgressDialog.setLottieProgressDialog(RoadRegisterPageActivity.this, Constants.lottiePath);
        checkbox_terms_condition = findViewById(R.id.checkbox_terms_condition);
        SpannableString spantext = new SpannableString("I agree to ");
        spantext.setSpan(new ForegroundColorSpan(Color.GRAY), 0, 10, 0);
        //This Will Print Nitesh In Blue Color
        //spantext.setSpan(new RelativeSizeSpan(1.0f), 7, 12, 0);
        // This will print "Tiwari" 1x  bigger than the remaining Figure
        checkbox_terms_condition.setText(spantext, TextView.BufferType.SPANNABLE);
        //country spinner


        spinner = findViewById(R.id.et_country);
        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries);
        for (String country : countries) {
            System.out.println(country);
        }
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(RoadRegisterPageActivity.this,
                android.R.layout.simple_spinner_item, countries);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the your spinner
        spinner.setAdapter(countryAdapter);
        //
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView)parentView.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        checkbox_terms_condition.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                status_agree = true;
            } else {
                status_agree = false;
            }
        });

        if (Util_functions.checkPermission(RoadRegisterPageActivity.this, permissions)) {
            getDeviceID();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.txt_LoginNow:
                startActivity(new Intent(getApplicationContext(), LoginPageActivity.class));
                finish();
                break;
            case R.id.tv_terms:
                startActivity(new Intent(RoadRegisterPageActivity.this, RoadTermsPageActivity.class));
                break;

            case R.id.btnRoadSubmit:
                if (et_name.getText().toString().isEmpty()) {
                    status_name = false;
                    Util_functions.ShowValidateAlertError(RoadRegisterPageActivity.this, getString(R.string.enter_name), "ok");
                    break;
                } else {
                    status_name = true;
                }
                if (spinner.getSelectedItemPosition() == 0) {
                    Util_functions.ShowValidateAlertError(RoadRegisterPageActivity.this, getString(R.string.select_country), "OK");
                    status_country = false;
                    break;
                } else {
                    status_country = true;
                }
                if (et_mobilenumber.getText().toString().isEmpty()) {
                    status_mob = false;
                    Util_functions.ShowValidateAlertError(RoadRegisterPageActivity.this, getString(R.string.enter_phn), "ok");
                    break;
                } else {
                    if (Util_functions.isValidPhoneNumber(et_mobilenumber.getText().toString())) {
                        status_mob = true;
                    } else {
                        status_mob = false;
                        Util_functions.ShowValidateAlertError(RoadRegisterPageActivity.this, getString(R.string.valid_phno), "ok");
                        break;
                    }
                }
                if (et_password.getText().toString().isEmpty()) {
                    status_pass = false;
                    Util_functions.ShowValidateAlertError(RoadRegisterPageActivity.this, getString(R.string.enter_pwd), "ok");
                    break;
                } else {
                    if (!(et_password.length() > 7)) {
                        Util_functions.ShowValidateAlertError(RoadRegisterPageActivity.this, getString(R.string.valid_password_len), "OK");
                        break;
                    } else {
                        if (Util_functions.isValidPassword(et_password.getText().toString())) {
                            status_pass = true;
                            edit_password_layout.setError("");
                        } else {
                            //Util_functions.ShowValidateAlertError(RoadRegisterPageActivity.this,getString(R.string.valid_password),"ok");
                            edit_password_layout.setErrorIconDrawable(0);
                            edit_password_layout.setError("Password must contain at least \none special character,one number and alphabet");
                            status_pass = false;
                            break;
                        }
                    }

                }


               /* if (et_name.getText().toString().isEmpty() || et_name.getText().toString().equals("")) {
                    Util_functions.ShowValidateAlertError(RoadRegisterPageActivity.this,getString(R.string.enter_name),"OK");
               *//*
                    status_name = false;
                    break;
                } else {
                    status_name = true;
                    if(spinner.getSelectedItemPosition()==0){
                        Util_functions.ShowValidateAlertError(RoadRegisterPageActivity.this,getString(R.string.enter_country),"OK");
                        status_country = false;
                        break;
                    } else {
                        status_country = true;
                    *//*if (!et_email.getText().toString().isEmpty() || !et_email.getText().toString().isEmpty()) {
                        if (!validateEmail(et_email.getText().toString())) {
                            status_email = false;
                            Toast toast = Toast.makeText(this, getString(R.string.valid_email_address), Toast.LENGTH_SHORT);
                            ViewGroup group = (ViewGroup) toast.getView();
                            TextView messageTextView = (TextView) group.getChildAt(0);
                            messageTextView.setTextSize(25);
                            toast.show();
                        } else {
                            status_email = true;*//*
                    }
                        if (et_mobilenumber.getText().toString().isEmpty()) {
                            Util_functions.ShowValidateAlertError(RoadRegisterPageActivity.this,getString(R.string.enter_phn),"ok");
                            status_mob = false;
                        }
                        else {
                            if (Util_functions.isValidPhoneNumber(et_mobilenumber.getText().toString())){
                                status_mob=true;
                                break;
                            }
                            else {
                                Util_functions.ShowValidateAlertError(RoadRegisterPageActivity.this,getString(R.string.valid_phno),"ok");
                                status_mob=false;
                     *//*   if (!et_mobilenumber.getText().toString().isEmpty() || !et_mobilenumber.getText().toString().equals("")) {
                            if (TextUtils.isDigitsOnly(et_mobilenumber.getText().toString().trim())) {
                                if (Patterns.PHONE.matcher(et_mobilenumber.getText().toString()).matches()) {
                                    if (isValidPhoneNumber(et_mobilenumber.getText().toString())) {
                                        status_mob = true;*//*
                                break;
                            }
                                        if (!et_password.getText().toString().isEmpty() || !et_password.getText().toString().isEmpty()) {
                                            if (!isValidPassword(et_password.getText().toString())) {
                                                Util_functions.ShowValidateAlertError(RoadRegisterPageActivity.this,getString(R.string.valid_password),"OK");
                                                    *//*
                                                status_pass = false;
                                             //   et_password.setError("^(?=.*[0-9])(?=.*[a-z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$");

                                            } else {
                                                status_pass = true;
                                            }
                                        } else {

                                            Util_functions.ShowValidateAlertError(RoadRegisterPageActivity.this,getString(R.string.valid_password),"OK");
                                                *//*
                                            status_pass = false;
                                        }

                                    else {

                                        Util_functions.ShowValidateAlertError(RoadRegisterPageActivity.this,getString(R.string.valid_phno),"OK");
                                           *//*
                                        status_mob = false;




                        } else {

                            Util_functions.ShowValidateAlertError(RoadRegisterPageActivity.this,getString(R.string.valid_phno),"OK");
                               *//*
                            status_mob = false;
                        }



                  *//*  } else {
                        status_email = true;
                    }*//*
                }*/

                if (status_name && status_mob && status_country && status_pass) {

                    if (status_agree) {
                        if (Util_functions.ConnectivityCheck(this, this).booleanValue()) {
                            mconnectStatus = true;
                            if (Util_functions.checkPermission(RoadRegisterPageActivity.this, permissions)) {
                                getDeviceID();
                                // ProgressDialog.showProgress();
                                RegisterNow();
                            }

                        }

                    } else {

                        Util_functions.ShowValidateAlertError(RoadRegisterPageActivity.this, getString(R.string.agree_terms), "OK");
                        status_mob = false;
                    }

                }
                break;
        }

    }

    private void getDeviceID() {
        try {
            JNDKSafetyNex mJniFunction = JNDKSafetyNex.GetInstance(this);
            mPrefernce_Manager.setString(Preference_Keys.LOginKeys.devicveID, mJniFunction.GetDeviceId());
            Constants.M_ANDROID_ID = mJniFunction.GetDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Toast.makeText(getApplicationContext(), "DeviceID"+android_id, Toast.LENGTH_SHORT).show();
    }

    private void RegisterNow() {
        ProgressDialog.setLottieProgressDialog(this, Constants.lottiePath);
        ProgressDialog.showProgress();
        RegisterReq registerReq = new RegisterReq();
        RegisterReq.UserSessionField userSessionField = new RegisterReq.UserSessionField();
        registerReq.setCategory(category_Password);
        registerReq.setCompanyId("comp00000000000000000001");
        registerReq.setCountryId("coun00000000000000000004");
        registerReq.setEmail(et_email.getText().toString());
        registerReq.setEmailVerified("NO");
        registerReq.setFirstName(et_name.getText().toString());
        registerReq.setId("");
        registerReq.setTimeZoneId("tizo00000000000000000001");
        registerReq.setLastName("");
        registerReq.setStatus("REGISTERED");
        registerReq.setType("USER");
        userSessionField.setDeviceType("ANDROID");
        userSessionField.setDeviceOrderId("3523");
        userSessionField.setDeviceStatus("REGISTERED");
        registerReq.setUsernameVerified("NO");
        userSessionField.setDeviceUniqueId(Preference_Details.getDeviceId());
        userSessionField.setDeviceVersionNumber(String.valueOf(Build.VERSION.RELEASE));
        DecimalFormat REAL_FORMATTER = new DecimalFormat("0");
        userSessionField.setDeviceModelName(String.valueOf(Build.MANUFACTURER) + " " + String.valueOf(Build.MODEL) + " " + REAL_FORMATTER.format(Double.parseDouble(Build.VERSION.RELEASE)));
        registerReq.setUserSessionField(userSessionField);
        // registerReq.setPassword("test123");
        registerReq.setPassword(et_password.getText().toString());
        registerReq.setUsername(et_mobilenumber.getText().toString());
        RegisterReq.UserField userField = new RegisterReq.UserField();
        userField.setAge("");
        if (!TextUtils.isEmpty(mliceDate)) {
            userField.setDrivingLicenseIssueDate(getFormatDate(mliceDate));
        } else {
            userField.setDrivingLicenseIssueDate(mliceDate);
        }
        userField.setEmergencyContactNumber("");
        registerReq.setUserField(userField);
        RegisterReq.UserPrimaryDetail userPrimaryDetail = new RegisterReq.UserPrimaryDetail();
        userPrimaryDetail.setPrimaryDivisionId("divi00000000000000000001");
        userPrimaryDetail.setPrimaryModuleId("modu00000000000000000001");
        registerReq.setUserPrimaryDetail(userPrimaryDetail);
        restMethods = RestClient.buildHTTPClient();
        restMethods.registerCall(Constants.referredBy, Constants.Static_SessionID, registerReq).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegisterResponse> call, @NonNull Response<RegisterResponse> response) {
                Log.e("status", "" + response.body());
                try {
                    if (response.code() == 201) {
                        ProgressDialog.dismissProgress();
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.pwd, et_password.getText().toString());
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.userName, response.body().getUsername());
                        Constants.regUserID = response.body().getId();
                        Constants.regCompID = response.body().getCompanyId();
                        Constants.regCountrID = response.body().getCountryId();
                        Constants.regUsername = response.body().getUsername();
                        Constants.regEmailID = response.body().getEmail();
                        Constants.regType = response.body().getType();
                        Constants.regStatus = response.body().getStatus();
                        //Constants.regPass = "test123";
                        Constants.regPass = et_password.getText().toString();
                        Util_functions.ShowAlertError(RoadRegisterPageActivity.this, getString(R.string.text_register), "OK", RoadRegisterPageActivity.this);
                        Log.e("value", response.message().toString());
                    } else if (response.code() == 412) {
                        try {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowValidateAlertError(RoadRegisterPageActivity.this, object1.getString("message"), "OK");
                            }
                            //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.resp_empty),true);
                            ProgressDialog.dismissProgress();
                        } catch (Exception e) {
                            e.printStackTrace();
                            ProgressDialog.dismissProgress();
                        }
                    } else {
                        ProgressDialog.dismissProgress();
                        String Error = response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text = obj.getString("message");
                        Util_functions.ShowValidateAlertError(RoadRegisterPageActivity.this, text, "OK");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ProgressDialog.dismissProgress();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable t) {
                if (t.toString().contains("Unable to resolve host")) {
                    Util_functions.ShowValidateAlertError(RoadRegisterPageActivity.this, getString(R.string.check_internet), "OK");
                }
                Util_functions.ShowAlertError(RoadRegisterPageActivity.this, t.getMessage(), "OK", RoadRegisterPageActivity.this);
                t.printStackTrace();
                ProgressDialog.dismissProgress();
                //   Util_functions.ShowValidateAlertError(RoadRegisterPageActivity.this,t.getMessage(),"OK");
            }
        });


    }

    @Override
    public void onPositiveButtonClicked(AlertDialog alertDialog) {
        if (!mconnectStatus.booleanValue()) {
            ProgressDialog.dismissProgress();
        } else {
            startActivity(new Intent(getApplicationContext(), VerificationCodePageActivity.class).putExtra("mob_number", et_mobilenumber.getText().toString()));
            finish();
            mconnectStatus = false;
        }
    }

    @Override
    public void onNegativeButtonClicked() {


    }

    private String getFormatDate(String DateNow) {
        String newDateString = "";
        try {
            // mon+" "+date+", "+year
            //current date format
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
            Date objDate = dateFormat.parse(DateNow);
            //Expected date format
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
            newDateString = dateFormat2.format(objDate);
            Log.d("Date Format:", "Final Date:" + newDateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newDateString;
    }

}