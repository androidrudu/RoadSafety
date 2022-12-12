package com.montbleu.roadsafety.activity;

import static android.os.Build.VERSION.SDK_INT;
import static com.montbleu.Utils.Constants.Gps_Loc;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

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
import com.montbleu.model.SettingEditResponse;
import com.montbleu.model.SettingGetGenResponse;
import com.montbleu.model.UserSettingGETResponse;
import com.montbleu.model.UserSettingRequest;
import com.montbleu.model.UserSettingResponse;
import com.montbleu.roadsafety.BuildConfig;
import com.montbleu.roadsafety.FeedbackActivity;
import com.montbleu.roadsafety.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoadSettingsPageActivity extends AppCompatActivity implements View.OnClickListener, OnDialogButtonClickListener {
    SeekBar seekBar;
    TextView distance_mid;
    Switch switchNoti, switchStBg, switchSLBeep, switchBeep, switchVcAlert;
    ConstraintLayout personal_info, app_setting;
    RelativeLayout carSettings, gen_settings, app_layout, faq_layout, feed_layout, report_layout;
    Boolean status_name = false, staus_age = false, status_mob = false, license_date_status = false, email_mob_Status = false;
    EditText et_name, et_email, et_mobilenumber, et_license_date, et_age;
    //TextInputLayout edit_name_layout,edit_email_layout,edit_age_layout,edit_mobile_layout;
    static final int DATE_DIALOG_ID = 999;
    private int year, month, day;
    private Calendar calendar;
    RestMethods restMethods;
    ArrayList<SettingGetGenResponse> profileGetResponses = new ArrayList<>();
    ArrayList<UserSettingGETResponse> userSettingGETResponses = new ArrayList<>();
    int seekBarValue;
    String mliceDate = "";
    String mAge = "";
    String mSID = "";
    String mName = "";
    Boolean mstatusRedirect = false;
    Boolean mconnectStatus = false;
    private PreferenceManager mPrefernce_Manager = PreferenceManager.getInstance();
    public static final int REQUEST_CODE_PERMISSIONS = 101;

    ImageView img_gen_setting, img_app_setting;
    AppCompatButton btnSubmit, btnCancel;
    ImageView back_terms_btn;
    ImageView img_faq_setting, img_feed_setting, img_report_setting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings_page);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        img_gen_setting = findViewById(R.id.img_gen_setting);
        img_app_setting = findViewById(R.id.img_app_setting);
        app_setting = findViewById(R.id.app_setting);
        carSettings = findViewById(R.id.car_layout);
        gen_settings = findViewById(R.id.gen_layout);
        app_layout = findViewById(R.id.app_layout);
        app_layout.setOnClickListener(this);
        carSettings.setOnClickListener(this);
        gen_settings.setOnClickListener(this);
        personal_info = findViewById(R.id.personal_info);
        distance_mid = findViewById(R.id.distance_mid);
        faq_layout = findViewById(R.id.faq_layout);
        feed_layout = findViewById(R.id.feed_layout);
        report_layout = findViewById(R.id.report_layout);
        faq_layout.setOnClickListener(this);
        report_layout.setOnClickListener(this);
        feed_layout.setOnClickListener(this);
        switchNoti = findViewById(R.id.switchNoti);
        switchStBg = findViewById(R.id.switchStBg);
        switchSLBeep = findViewById(R.id.switchSLBeep);
        switchBeep = findViewById(R.id.switchBeep);
        switchVcAlert = findViewById(R.id.switchVcAlert);
        switchSLBeep.setEnabled(false);
        switchVcAlert.setEnabled(false);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_age = findViewById(R.id.et_age);
        et_mobilenumber = findViewById(R.id.et_mobilenumber);
        et_license_date = findViewById(R.id.et_license_date);
        seekBar = findViewById(R.id.seekBar);
        img_faq_setting = findViewById(R.id.img_faq_setting);
        img_feed_setting = findViewById(R.id.img_feed_setting);
        img_report_setting = findViewById(R.id.img_report_setting);

        distance_mid.setText(String.valueOf(60));
        back_terms_btn = findViewById(R.id.back_profile_btn);
        back_terms_btn.setOnClickListener(this);
        ProgressDialog.setLottieProgressDialog(RoadSettingsPageActivity.this, Constants.lottiePath);
        seekBar.setEnabled(false);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                distance_mid.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });
        switchStBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switchStBg.isChecked() == true) {
                    if (!(SDK_INT >= Build.VERSION_CODES.Q)) {
                        if ((ActivityCompat.checkSelfPermission(RoadSettingsPageActivity.this,
                                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) == false) {
                            //ShowAlertError(RoadSettingsPageActivity.this,"Please click\n\"Allow all the time\"","OK",RoadSettingsPageActivity.this);
                        }
                    } else {
                        if ((ActivityCompat.checkSelfPermission(RoadSettingsPageActivity.this,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) == false) {
                            ShowAlertError(RoadSettingsPageActivity.this, "Please click\n\"Allow all the time\"", "OK", RoadSettingsPageActivity.this);
                        }
                    }

                }

            }
        });
        if (Util_functions.ConnectivityCheck(RoadSettingsPageActivity.this, RoadSettingsPageActivity.this).booleanValue()) {
            // mconnectStatus = true;
            ProgressDialog.showProgress();
            getPersonalDetails();
            getAppSettingDetails();
        }
    }

    private void getAppSettingDetails() {
        restMethods = RestClient.buildHTTPClient();
        UserSettingGETResponse userSettingGETResponse = new UserSettingGETResponse();
        userSettingGETResponse.setCompanyId(Constants.companyID);
        userSettingGETResponse.setDivisionId(Constants.divisionId);
        userSettingGETResponse.setModuleID(Constants.moduleID);
        restMethods.userSettingGetResponse(Constants.referredBy, Preference_Details.getLoginSessID(), Preference_Details.getCompanyID(), Preference_Details.getUserID(), Constants.AppSettingGetType, "ASC", 0, 100).enqueue(new Callback<List<UserSettingGETResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<UserSettingGETResponse>> call, @NonNull Response<List<UserSettingGETResponse>> response) {
                Log.e("status", "" + response.body());
                try {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            userSettingGETResponses.clear();
                            if (!response.body().isEmpty()) {
                                userSettingGETResponses = (ArrayList<UserSettingGETResponse>) response.body();

                                for (int i = 0; i < userSettingGETResponses.size(); i++) {
                                    mSID = userSettingGETResponses.get(i).getId();
                                    if (!TextUtils.isEmpty(userSettingGETResponses.get(i).getUserPreferenceField().getAppNotification())) {
                                        if (userSettingGETResponses.get(i).getUserPreferenceField().getAppNotification().equals("YES")) {
                                            switchNoti.setChecked(true);
                                            mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.Appnotif, true);
                                        } else {
                                            switchNoti.setChecked(false);
                                            mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.Appnotif, false);
                                        }
                                    }
                                    if (!TextUtils.isEmpty(userSettingGETResponses.get(i).getUserPreferenceField().getBackgroundStartup())) {
                                        if (userSettingGETResponses.get(i).getUserPreferenceField().getBackgroundStartup().equals("YES")) {
                                            switchStBg.setChecked(true);
                                            if (switchStBg.isChecked() == true) {
                                                if (!(SDK_INT >= Build.VERSION_CODES.Q)) {
                                                    if ((ActivityCompat.checkSelfPermission(RoadSettingsPageActivity.this,
                                                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) == false) {
                                                        //ShowAlertError(RoadSettingsPageActivity.this,"Please click\n\"Allow all the time\"","OK",RoadSettingsPageActivity.this);
                                                    }
                                                } else {
                                                    if ((ActivityCompat.checkSelfPermission(RoadSettingsPageActivity.this,
                                                            Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) == false) {
                                                        ShowAlertError(RoadSettingsPageActivity.this, "Please click\n\"Allow all the time\"", "OK", RoadSettingsPageActivity.this);
                                                    }
                                                }
                                            }
                                            mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.startonBg, true);
                                        } else {
                                            switchStBg.setChecked(false);
                                            mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.startonBg, false);
                                        }
                                    }
                                    if (!TextUtils.isEmpty(userSettingGETResponses.get(i).getUserPreferenceField().getSpeedLimitBeep())) {
                                        if (userSettingGETResponses.get(i).getUserPreferenceField().getSpeedLimitBeep().equals("YES")) {
                                            switchSLBeep.setChecked(true);
                                            mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.speedlimit, true);
                                        } else {
                                            switchSLBeep.setChecked(false);
                                            mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.speedlimit, false);
                                        }
                                    }
                                    if (!TextUtils.isEmpty(userSettingGETResponses.get(i).getUserPreferenceField().getBeep())) {
                                        if (userSettingGETResponses.get(i).getUserPreferenceField().getBeep().equals("YES")) {
                                            switchBeep.setChecked(true);
                                            mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.beep, true);
                                        } else {
                                            switchBeep.setChecked(false);
                                            mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.beep, true);
                                        }
                                    }
                                    if (!TextUtils.isEmpty(userSettingGETResponses.get(i).getUserPreferenceField().getVoiceAlert())) {
                                        if (userSettingGETResponses.get(i).getUserPreferenceField().getVoiceAlert().equals("YES")) {
                                            switchVcAlert.setChecked(true);
                                            mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.voice, true);
                                        } else {
                                            switchVcAlert.setChecked(false);
                                            mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.voice, false);
                                        }
                                    }
                                    if (!TextUtils.isEmpty(userSettingGETResponses.get(i).getUserPreferenceField().getCoPliotMode())) {
                                        /* seekBar.setProgress(Integer.parseInt(userSettingGETResponses.get(i).getUserPreferenceField().getCoPliotMode())); mPrefernce_Manager.setInt(Preference_Keys.LOginKeys.coipilot,Integer.parseInt(userSettingGETResponses.get(i).getUserPreferenceField().getCoPliotMode()));*/
                                        seekBar.setProgress(60);
                                        mPrefernce_Manager.setInt(Preference_Keys.LOginKeys.coipilot, Integer.parseInt(userSettingGETResponses.get(i).getUserPreferenceField().getCoPliotMode()));
                                    }
                                }
                                //  progressDialog.dismiss();
                            } else {
                                saveStartSettings();
                            }
                        } else {
                            ProgressDialog.dismissProgress();
                        }
                    } else if (response.code() == 412) {
                        try {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowAlertError(RoadSettingsPageActivity.this, object1.getString("message"), "OK", RoadSettingsPageActivity.this);
                            }
                            //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.resp_empty),true);
                            ProgressDialog.dismissProgress();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        String Error = response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text = obj.getString("message");
                        Util_functions.ShowAlertError(RoadSettingsPageActivity.this, text, "OK", RoadSettingsPageActivity.this);
                        //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.txt_error),true);
                        ProgressDialog.dismissProgress();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ProgressDialog.dismissProgress();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UserSettingGETResponse>> call, @NonNull Throwable t) {
                if (t.toString().contains("Unable to resolve host")) {
                    Util_functions.ShowAlertError(RoadSettingsPageActivity.this, getString(R.string.check_internet), "OK", RoadSettingsPageActivity.this);
                }
                Util_functions.ShowAlertError(RoadSettingsPageActivity.this, t.getMessage(), "OK", RoadSettingsPageActivity.this);
                t.printStackTrace();
                ProgressDialog.dismissProgress();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_profile_btn:
                finish();
                break;
            case R.id.faq_layout:
                Intent faq_acitivity = new Intent(getApplicationContext(), FaqActivity.class);
                startActivity(faq_acitivity);
                break;
            case R.id.feed_layout:
                Intent feed_acitivity = new Intent(getApplicationContext(), FeedbackActivity.class);
                startActivity(feed_acitivity);
                break;
            case R.id.report_layout:
                Intent reportActivity = new Intent(getApplicationContext(), ReportProblemPageActivity.class);
                startActivity(reportActivity);
                break;
            case R.id.btnSubmit:
                //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.setting_update),true);
                if (Util_functions.ConnectivityCheck(RoadSettingsPageActivity.this, RoadSettingsPageActivity.this).booleanValue()) {
                    mconnectStatus = true;
                    ProgressDialog.showProgress();
                    updateGeneralSettings();
                }

                // finish();
                break;
            case R.id.btnCancel:
                finish();
                break;
            case R.id.app_layout:
                if (app_setting.getVisibility() == View.VISIBLE) {
                    app_setting.setVisibility(View.GONE);
                    img_app_setting.setImageDrawable(getDrawable(R.drawable.down_arrow_ic));
                    // setting_ok.setVisibility(View.GONE);
                } else {
                    app_setting.setVisibility(View.VISIBLE);
                    img_app_setting.setImageDrawable(getDrawable(R.drawable.up_arrow_ic));
                    // setting_ok.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.gen_layout:
                if (personal_info.getVisibility() == View.VISIBLE) {
                    personal_info.setVisibility(View.GONE);
                    img_gen_setting.setImageDrawable(getDrawable(R.drawable.down_arrow_ic));
                    // setting_ok.setVisibility(View.GONE);
                } else {
                    personal_info.setVisibility(View.VISIBLE);
                    img_gen_setting.setImageDrawable(getDrawable(R.drawable.up_arrow_ic));
                    // setting_ok.setVisibility(View.VISIBLE);
                }
                break;


        }
    }


    private void updateGeneralSettings() {
        RegisterReq registerReq = new RegisterReq();
        registerReq.setCompanyId(Preference_Details.getCompanyID());
        registerReq.setCountryId(Constants.countryID);
        registerReq.setCategory(Constants.category_Password);
        registerReq.setTimeZoneId("tizo00000000000000000001");
        registerReq.setEmail(et_email.getText().toString());
        if (!TextUtils.isEmpty(profileGetResponses.get(0).getEmailVerified())) {
            registerReq.setEmailVerified(profileGetResponses.get(0).getEmailVerified());
        } else {
            registerReq.setEmailVerified("");
        }
        if (!TextUtils.isEmpty(profileGetResponses.get(0).getFirstName())) {
            registerReq.setFirstName(profileGetResponses.get(0).getFirstName());
        } else {
            registerReq.setFirstName("");
        }
        if (!TextUtils.isEmpty(profileGetResponses.get(0).getLastName())) {
            registerReq.setLastName(profileGetResponses.get(0).getLastName());
        } else {
            registerReq.setLastName("");
        }
        registerReq.setId(Preference_Details.getUserID());
        registerReq.setStatus("REGISTERED");
        registerReq.setType("USER");
        registerReq.setUsernameVerified("YES");
        registerReq.setPassword("");
        registerReq.setUsername(Preference_Details.getUserName());
        RegisterReq.UserField userField = new RegisterReq.UserField();
        //userField.setAge(profileGetResponses.get(0).getUserField().getAge());
        if (!TextUtils.isEmpty(mliceDate)) {
            userField.setDrivingLicenseIssueDate(getFormatDate(mliceDate));
        } else {
            userField.setDrivingLicenseIssueDate(getFormatDate(et_license_date.getText().toString()));
        }
        mAge = et_age.getText().toString();
        mName = et_name.getText().toString();
        userField.setAge(mAge);
        userField.setEmergencyContactNumber(et_mobilenumber.getText().toString());
        registerReq.setFirstName(mName);
        registerReq.setUserField(userField);
        RegisterReq.UserPrimaryDetail userPrimaryDetail = new RegisterReq.UserPrimaryDetail();
        userPrimaryDetail.setPrimaryDivisionId("divi00000000000000000001");
        userPrimaryDetail.setPrimaryModuleId("modu00000000000000000001");
        registerReq.setUserPrimaryDetail(userPrimaryDetail);
        restMethods = RestClient.buildHTTPClient();
        restMethods.EditSettingResponse(Constants.referredBy, Preference_Details.getLoginSessID(), registerReq).enqueue(new Callback<SettingEditResponse>() {
            @Override
            public void onResponse(@NonNull Call<SettingEditResponse> call, @NonNull Response<SettingEditResponse> response) {
                Log.e("status", "" + response.body());
                try {
                    if (response.code() == 202) {
                        //  Log.e("value",response.message().toString());
                        // Constants.frst_name=et_name.getText().toString();
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.FrstName, registerReq.getFirstName().trim());
                        Constants.frst_name = registerReq.getFirstName().trim();
                        updateAppPersonalSettings();
                    } else if (response.code() == 412) {
                        try {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowValidateAlertError(RoadSettingsPageActivity.this, object1.getString("message"), "OK");
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
                        if (text.toLowerCase().contains("please login again")) {
                            mstatusRedirect = true;
                        }
                        Util_functions.ShowValidateAlertError(RoadSettingsPageActivity.this, text, "OK");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ProgressDialog.dismissProgress();

                }
            }

            @Override
            public void onFailure(@NonNull Call<SettingEditResponse> call, @NonNull Throwable t) {
                if (t.toString().contains("Unable to resolve host")) {
                    Util_functions.ShowAlertError(RoadSettingsPageActivity.this, getString(R.string.check_internet), "OK", RoadSettingsPageActivity.this);
                }
                t.printStackTrace();
                ProgressDialog.dismissProgress();
                Util_functions.ShowAlertError(RoadSettingsPageActivity.this, t.getMessage(), "OK", RoadSettingsPageActivity.this);
            }
        });

    }

    private String getFormatDate(String DateNow) {
        String newDateString = "";
        try {
            // mon+" "+date+", "+year
            //current date format
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
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

    private void updateAppPersonalSettings() {
        UserSettingRequest userSettingRequest = new UserSettingRequest();
        userSettingRequest.setCompanyId(Preference_Details.getCompanyID());
        userSettingRequest.setName(Constants.SettingUpdateType); // need to change
        userSettingRequest.setStatus("REGISTERED");
        userSettingRequest.setType(Constants.SettingType);
        userSettingRequest.setId(mSID);
        userSettingRequest.setUserId(Preference_Details.getUserID());
        UserSettingRequest.UserPreferenceField userPreferenceField = new UserSettingRequest.UserPreferenceField();
        if (switchNoti.isChecked()) {
            userPreferenceField.setAppNotification("YES");
            mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.Appnotif, true);
        } else {
            userPreferenceField.setAppNotification("NO");
            mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.Appnotif, false);
        }
        if (switchStBg.isChecked()) {
            userPreferenceField.setBackgroundStartup("YES");
            mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.startonBg, true);
        } else {
            userPreferenceField.setBackgroundStartup("NO");
            mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.startonBg, false);
        }
        if (switchBeep.isChecked()) {
            userPreferenceField.setBeep("YES");
            mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.beep, true);
        } else {
            userPreferenceField.setBeep("NO");
            mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.beep, false);
        }
        if (switchSLBeep.isChecked()) {
            userPreferenceField.setSpeedLimitBeep("YES");
            mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.speedlimit, true);
        } else {
            userPreferenceField.setSpeedLimitBeep("NO");
            mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.speedlimit, false);
        }
        if (switchVcAlert.isChecked()) {
            userPreferenceField.setVoiceAlert("YES");
            mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.voice, true);
        } else {
            userPreferenceField.setVoiceAlert("NO");
            mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.voice, false);
        }
        seekBarValue = seekBar.getProgress(); // get progress value from the Seek bar
        userPreferenceField.setCoPliotMode(String.valueOf(seekBarValue));
        mPrefernce_Manager.setInt(Preference_Keys.LOginKeys.coipilot, seekBarValue);
        userSettingRequest.setModuleID(Constants.moduleID);
        userSettingRequest.setDivisionId(Constants.divisionId);
        userSettingRequest.setUserPreferenceField(userPreferenceField);
        restMethods = RestClient.buildHTTPClient();
        restMethods.userSettingPUTResp(Constants.referredBy, Preference_Details.getLoginSessID(), userSettingRequest).enqueue(new Callback<UserSettingResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserSettingResponse> call, @NonNull Response<UserSettingResponse> response) {
                Log.e("status", "" + response.body());
                try {
                    if (response.code() == 202) {
                        ProgressDialog.dismissProgress();
                        // Constants.frst_name=et_name.getText().toString();
                        Util_functions.ShowAlertError(RoadSettingsPageActivity.this, getString(R.string.setting_update), "OK", (OnDialogButtonClickListener) RoadSettingsPageActivity.this);
                        Log.e("value", response.message());

                    } else if (response.code() == 412) {
                        try {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowValidateAlertError(RoadSettingsPageActivity.this, object1.getString("message"), "OK");
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
                        if (text.toLowerCase().contains("please login again")) {
                            mstatusRedirect = true;
                        }
                        Util_functions.ShowValidateAlertError(RoadSettingsPageActivity.this, text, "OK");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ProgressDialog.dismissProgress();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserSettingResponse> call, @NonNull Throwable t) {
                if (t.toString().contains("Unable to resolve host")) {
                    Util_functions.ShowAlertError(RoadSettingsPageActivity.this, getString(R.string.check_internet), "OK", RoadSettingsPageActivity.this);
                }
                t.printStackTrace();
                ProgressDialog.dismissProgress();
                Util_functions.ShowAlertError(RoadSettingsPageActivity.this, t.getMessage(), "OK", RoadSettingsPageActivity.this);
            }
        });


    }

    private void getPersonalDetails() {
        restMethods = RestClient.buildHTTPClient();
        restMethods.settingGetResponse(Constants.referredBy, Preference_Details.getLoginSessID(), Preference_Details.getCompanyID(), Preference_Details.getLoginType(), Preference_Details.getUserID(), "ASC", 0, 100).enqueue(new Callback<List<SettingGetGenResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<SettingGetGenResponse>> call, @NonNull Response<List<SettingGetGenResponse>> response) {
                Log.e("status", "" + response.body());
                try {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            profileGetResponses.clear();
                            if (!response.body().isEmpty()) {
                                profileGetResponses = (ArrayList<SettingGetGenResponse>) response.body();
                                for (int i = 0; i < profileGetResponses.size(); i++) {
                                    et_name.append(profileGetResponses.get(i).getFirstName().trim() + "" + profileGetResponses.get(i).getLastName().trim());
                                    et_email.setText(profileGetResponses.get(i).getEmail());
                                    et_mobilenumber.setText(profileGetResponses.get(i).getUserField().getEmergencyContactNumber());
                                    if (!TextUtils.isEmpty(profileGetResponses.get(i).getUserField().getDrivingLicenseIssueDate())) {
                                        et_license_date.setText(getSplittedDate(profileGetResponses.get(i).getUserField().getDrivingLicenseIssueDate()));
                                        mliceDate = getSplittedDate(profileGetResponses.get(i).getUserField().getDrivingLicenseIssueDate());
                                    } else {
                                        et_license_date.setText("");
                                    }
                                    if (!TextUtils.isEmpty(profileGetResponses.get(i).getUserField().getAge())) {
                                        et_age.setText(profileGetResponses.get(i).getUserField().getAge());
                                    } else {
                                        et_age.setText("");
                                    }

                                }
                                ProgressDialog.dismissProgress();

                            } else {
                                String Error = response.errorBody().string();
                                JSONObject obj = new JSONObject(Error);
                                String text = obj.getString("message");
                                if (text.toLowerCase().contains("please login again")) {
                                    mstatusRedirect = true;
                                }
                                Util_functions.ShowAlertError(getApplicationContext(), text, "OK", RoadSettingsPageActivity.this);
                                //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.resp_empty),true);
                                ProgressDialog.dismissProgress();
                            }
                        } else {
                            ProgressDialog.dismissProgress();
                        }
                    } else if (response.code() == 412) {
                        try {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowAlertError(RoadSettingsPageActivity.this, object1.getString("message"), "OK", RoadSettingsPageActivity.this);
                            }
                            //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.resp_empty),true);
                            ProgressDialog.dismissProgress();
                        } catch (Exception e) {
                            e.printStackTrace();
                            ProgressDialog.dismissProgress();

                        }

                    } else {
                        String Error = response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text = obj.getString("message");
                        if (text.toLowerCase().contains("please login again")) {
                            mstatusRedirect = true;
                        }
                        Util_functions.ShowAlertError(RoadSettingsPageActivity.this, text, "OK", RoadSettingsPageActivity.this);
                        //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.txt_error),true);
                        ProgressDialog.dismissProgress();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ProgressDialog.dismissProgress();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SettingGetGenResponse>> call, @NonNull Throwable t) {
                if (t.toString().contains("Unable to resolve host")) {
                    Util_functions.ShowAlertError(RoadSettingsPageActivity.this, getString(R.string.check_internet), "OK", RoadSettingsPageActivity.this);
                }
                Util_functions.ShowAlertError(RoadSettingsPageActivity.this, t.getMessage(), "OK", RoadSettingsPageActivity.this);
                t.printStackTrace();
                ProgressDialog.dismissProgress();
            }
        });
    }

    public String getSplittedDate(String dateNow) {
        String dateInputString = dateNow;
        String stringDate = null;
        try {
            // obtain date and time from initial string
            Date date1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).parse(dateInputString);
            // set date string
            stringDate = new SimpleDateFormat("dd MMM yyyy", Locale.US).format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return stringDate;
    }


    /*private Boolean doValidate() {
        Boolean status =false;
        if (et_name.getText().toString().isEmpty() || et_name.getText().toString().equals("")) {
            status_name = false;
            Util_functions.Show_Toast(getApplicationContext(), getString(R.string.enter_name),true);
        } else {
            status_name = true;
            if (!Util_functions.validateEmail(et_email.getText().toString())) {
                Util_functions.Show_Toast(getApplicationContext(), getString(R.string.valid_email_address),true);
                email_mob_Status = false;
            } else {
                email_mob_Status = true;
                if(et_age.getText().toString().isEmpty()){
                    Util_functions.Show_Toast(getApplicationContext(), getString(R.string.valid_Age_empty),true);
                    staus_age = false;
                } else {
                    staus_age =true;
                    if(!Util_functions.isValidPhoneNumber(et_mobilenumber.getText().toString())){
                        Util_functions.Show_Toast(getApplicationContext(), getString(R.string.valid_phno),true);
                        status_mob = false;
                    } else {
                        status_mob = true;
                        if(et_license_date.getText().toString().isEmpty()){
                            Util_functions.Show_Toast(getApplicationContext(), getString(R.string.valid_date),true);
                            license_date_status = false;
                        } else {
                            license_date_status = true;

                            if(status_name==true && email_mob_Status==true && status_mob ==true &&staus_age == true && license_date_status==true){
                                status = true;
                            }

                        }

                    }
                }
            }
        }
        return  status;
    }*/

    public void renderDatePicker(View view) {
        showDialog(DATE_DIALOG_ID);
    }

    private void showDate(int year, String month, int day) {
        et_license_date.setText(new StringBuilder().append(day).append(" ")
                .append(month).append(" ").append(year));
        mliceDate = day + " " + month + " " + year;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DATE_DIALOG_ID) {
            DatePickerDialog dialog = new DatePickerDialog(this, R.style.my_dialog_theme, myDateListener, year, month, day);
            dialog.getDatePicker().setMaxDate(new Date().getTime());
            return dialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {

            int tempmonth = month + 1;
            SimpleDateFormat newformat = new SimpleDateFormat("MMM");
            SimpleDateFormat oldformat = new SimpleDateFormat("MM");
            String monthName = null;
            Date myDate;
            try {
                myDate = oldformat.parse(String.valueOf(tempmonth));
                monthName = newformat.format(myDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.d("MMM", monthName);
            showDate(year, monthName, day);
        }


    };


    @Override
    public void onPositiveButtonClicked(AlertDialog alertDialog) {
        if (!mconnectStatus.booleanValue()) {
            ProgressDialog.dismissProgress();
            finish();
        } else {
            if (mstatusRedirect == true) {
                mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.LoginStatus, false);
                Intent loginScreen = new Intent(getApplicationContext(), LoginPageActivity.class);
                startActivity(loginScreen);
                ProgressDialog.dismissProgress();
                finish();
            } else {
                ProgressDialog.dismissProgress();
                finish();
            }
        }
    }

    @Override
    public void onNegativeButtonClicked() {
        requestLocationPermission();
    }

    private void saveStartSettings() {
        UserSettingRequest userSettingRequest = new UserSettingRequest();
        userSettingRequest.setCompanyId(Preference_Details.getCompanyID());
        userSettingRequest.setName("test"); // need to change
        userSettingRequest.setStatus("REGISTERED");
        userSettingRequest.setType(Constants.SettingType);
        userSettingRequest.setUserId(Preference_Details.getUserID());
        userSettingRequest.setDivisionId(Constants.divisionId);
        userSettingRequest.setModuleID(Constants.moduleID);
        UserSettingRequest.UserPreferenceField userPreferenceField = new UserSettingRequest.UserPreferenceField();
        userPreferenceField.setAppNotification("NO");
        userPreferenceField.setBackgroundStartup("NO");
        userPreferenceField.setBeep("NO");
        seekBarValue = seekBar.getProgress(); // get progress value from the Seek bar
        userPreferenceField.setCoPliotMode(String.valueOf(seekBarValue));
        userPreferenceField.setVoiceAlert("NO");
        userPreferenceField.setSpeedLimitBeep("NO");
        userSettingRequest.setUserPreferenceField(userPreferenceField);
        restMethods = RestClient.buildHTTPClient();
        restMethods.userSettingPostResp(Constants.referredBy, Preference_Details.getLoginSessID(), userSettingRequest).enqueue(new Callback<UserSettingResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserSettingResponse> call, @NonNull Response<UserSettingResponse> response) {
                Log.e("status", "" + response.body());
                try {
                    if (response.code() == 201) {
                        ProgressDialog.dismissProgress();
                        //Util_functions.ShowAlertError(RoadSettingsPageActivity.this, getString(R.string.text_App_setting_add),"OK", (OnDialogButtonClickListener) RoadSettingsPageActivity.this);
                        Log.e("value", response.message().toString());
                        mSID = response.body().getId();
                        if (response.body().getUserPreferenceField() != null) {
                            //Constants.response.body().getUserPreferenceField().getAppNotification();
                            mPrefernce_Manager.setInt(Preference_Keys.LOginKeys.coipilot, Integer.parseInt(response.body().getUserPreferenceField().getCoPliotMode()));
                            if (response.body().getUserPreferenceField().getVoiceAlert().contains("YES")) {
                                mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.voice, true);
                            } else {
                                mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.voice, false);
                            }
                            if (response.body().getUserPreferenceField().getBackgroundStartup().contains("YES")) {
                                mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.startonBg, true);
                            } else {
                                mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.startonBg, false);
                            }
                            if (response.body().getUserPreferenceField().getSpeedLimitBeep().contains("YES")) {
                                mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.speedlimit, true);
                            } else {
                                mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.speedlimit, false);
                            }
                            if (response.body().getUserPreferenceField().getBeep().contains("YES")) {
                                mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.beep, true);
                            } else {
                                mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.beep, false);
                            }
                            if (response.body().getUserPreferenceField().getAppNotification().contains("YES")) {
                                mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.Appnotif, true);
                            } else {
                                mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.Appnotif, false);
                            }

                        }

                    } else if (response.code() == 412) {
                        try {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowValidateAlertError(RoadSettingsPageActivity.this, object1.getString("message"), "OK");
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
                        Util_functions.ShowValidateAlertError(getApplicationContext(), text, "OK");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ProgressDialog.dismissProgress();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserSettingResponse> call, @NonNull Throwable t) {
                if (t.toString().contains("Unable to resolve host")) {
                    Util_functions.ShowValidateAlertError(RoadSettingsPageActivity.this, getString(R.string.check_internet), "OK");
                }
                t.printStackTrace();
                ProgressDialog.dismissProgress();
                Util_functions.ShowAlertError(RoadSettingsPageActivity.this, t.getMessage(), "OK", RoadSettingsPageActivity.this);
            }
        });
    }

    private void requestLocationPermission() {

        boolean foreground = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (foreground) {
            boolean background = false;
            if (SDK_INT >= Build.VERSION_CODES.Q) {
                background = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
            }

            if (background) {
                handleLocationUpdates();
            } else {
                if (SDK_INT >= Build.VERSION_CODES.Q) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_CODE_PERMISSIONS);
                }
            }
        } else {
            if (SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_CODE_PERMISSIONS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {

            boolean foreground = false, background = false;

            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    //foreground permission allowed
                    if (grantResults[i] >= 0) {
                        foreground = true;
                        //Toast.makeText(getApplicationContext(), "Foreground location permission allowed", Toast.LENGTH_SHORT).show();
                        continue;
                    } else {
                        openSettings();
                        switchStBg.setChecked(false);
                        //Toast.makeText(getApplicationContext(), "Location Permission denied", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                if (permissions[i].equalsIgnoreCase(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                    if (grantResults[i] >= 0) {
                        foreground = true;
                        background = true;
                        mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.gpsLoc, true);
                        //Toast.makeText(getApplicationContext(), "Background location location permission allowed", Toast.LENGTH_SHORT).show();
                    } else {
                        AlertDialog(RoadSettingsPageActivity.this, "App in background functionality will be disabled", "OK", "CANCEL", RoadSettingsPageActivity.this);
                        // Toast.makeText(getApplicationContext(), "Background location location permission denied", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            if (foreground) {
                if (background) {
                    handleLocationUpdates();
                } else {
                    handleForegroundLocationUpdates();
                }
            }
        }
    }

    private void handleLocationUpdates() {
        //foreground and background
        Gps_Loc = true;
        mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.gpsLoc, true);
        //Toast.makeText(getApplicationContext(),"Start Foreground and Background Location Updates",Toast.LENGTH_SHORT).show();
    }

    private void handleForegroundLocationUpdates() {
        //handleForeground Location Updates
        //Toast.makeText(getApplicationContext(),"Start foreground location updates",Toast.LENGTH_SHORT).show();
    }


    public void ShowAlertError(Context context, String title, String BtnText, final OnDialogButtonClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutView = inflater.inflate(R.layout.alert_error, null);
        builder.setView(layoutView);
        alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (Constants.alertcount == true) {
            alertDialog.show();
            Constants.alertcount = false;
        }
        alertDialog.setCancelable(false);
        TextView alert_title = layoutView.findViewById(R.id.title_error);
        alert_title.setText(title);
        Button alert_ok = layoutView.findViewById(R.id.alert_ok);
        alert_ok.setText(BtnText);

        alert_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLocationPermission();
                alertDialog.hide();
                alertDialog.cancel();
                alertDialog.dismiss();
                Constants.alertcount = true;
            }
        });
    }

    public void AlertDialog(Context context, String title, String positiveBtnText, String negativeBtnText,
                            final OnDialogButtonClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutView = inflater.inflate(R.layout.alert, null);
        builder.setView(layoutView);
        alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (Constants.alertcount == true) {
            alertDialog.show();
            Constants.alertcount = false;
        }
        TextView alert_title = layoutView.findViewById(R.id.alert_title);
        Button alert_yes = layoutView.findViewById(R.id.alert_yes);
        Button alert_no = layoutView.findViewById(R.id.alert_no);
        alert_title.setText(title);
        alertDialog.setCancelable(false);
        alert_yes.setText(positiveBtnText);
        alert_no.setText(negativeBtnText);
        alert_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.hide();
                alertDialog.cancel();
                alertDialog.dismiss();
                Constants.alertcount = true;
                switchStBg.setChecked(false);
            }
        });

        alert_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLocationPermission();
                alertDialog.hide();
                alertDialog.cancel();
                alertDialog.dismiss();
                Constants.alertcount = true;
            }
        });
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //requestLocationPermission();
    }
}