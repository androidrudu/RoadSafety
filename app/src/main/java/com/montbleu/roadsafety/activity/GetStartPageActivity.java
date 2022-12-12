package com.montbleu.roadsafety.activity;

import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

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
import com.montbleu.model.GetStartEditResponse;
import com.montbleu.model.GetStartPersEditResp;
import com.montbleu.model.RegisterReq;
import com.montbleu.model.UserSettingRequest;
import com.montbleu.model.UserSettingResponse;
import com.montbleu.roadsafety.BuildConfig;
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

import static com.montbleu.Utils.Constants.Gps_Loc;
import static com.montbleu.Utils.Constants.REQUEST_CODE_PERMISSIONS;
import static com.montbleu.roadsafety.activity.RoadInstallingmapPageActivity.mregSess;

public class GetStartPageActivity extends AppCompatActivity implements View.OnClickListener, OnDialogButtonClickListener {
    private Button btnStartSave, btnSavePersonal;
    Boolean status_name = false, staus_age = false, status_mob = false, license_date_status = false, email_mob_Status = false;
    EditText mName, et_email, et_mobilenumber, et_license_date, et_age;
    static final int DATE_DIALOG_ID = 999;
    private int year, month, day;
    private Calendar calendar;
    Switch switchNoti, switchStBg, switchSLBeep, switchBeep, switchVcAlert;
    SeekBar seekBar;
    TextInputLayout edit_name_layout;
    RestMethods restMethods;
    int seekBarValue;
    TextView distance_mid;
    ArrayList<GetStartPersEditResp> profileGetResponses = new ArrayList<>();
    String mliceDate = "", mAge = "";
    private PreferenceManager mPrefernce_Manager = PreferenceManager.getInstance();
    boolean alert_status, alert_start;
    RegisterReq registerReq = new RegisterReq();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_road_get_start_page);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        btnStartSave = findViewById(R.id.btnSave);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        edit_name_layout = findViewById(R.id.edit_name_layout);
        mName = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        distance_mid = findViewById(R.id.distance_mid);
        et_age = findViewById(R.id.et_age);
        et_mobilenumber = findViewById(R.id.et_mobilenumber);
        et_license_date = findViewById(R.id.et_license_date);
        switchNoti = findViewById(R.id.switchNoti);
        switchStBg = findViewById(R.id.switchStBg);
        switchSLBeep = findViewById(R.id.switchSLBeep);
        switchBeep = findViewById(R.id.switchBeep);
        switchVcAlert = findViewById(R.id.switchVcAlert);
        switchSLBeep.setEnabled(false);
        switchVcAlert.setEnabled(false);
        btnSavePersonal = findViewById(R.id.btnSavePersonal);
        btnSavePersonal.setOnClickListener(this);
        seekBar = findViewById(R.id.seekBar);
        btnStartSave.setOnClickListener(this);
        ProgressDialog.setLottieProgressDialog(GetStartPageActivity.this, Constants.lottiePath);
        ProgressDialog.showProgress();
        getPersonalDetails();
        Constants.mapCount = 0;
        distance_mid.setText(String.valueOf(50));
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
        switchStBg.setOnClickListener(view -> {
            if (switchStBg.isChecked()) {
                if (!(SDK_INT >= Build.VERSION_CODES.Q)) {
                    if ((ActivityCompat.checkSelfPermission(GetStartPageActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) == false) {
                        //ShowAlertError(RoadSettingsPageActivity.this,"Please click\n\"Allow all the time\"","OK",RoadSettingsPageActivity.this);
                    }
                } else {
                    if (!(ActivityCompat.checkSelfPermission(GetStartPageActivity.this,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                        ShowAlertError(GetStartPageActivity.this, "Please click\n\"Allow all the time\"", "OK", GetStartPageActivity.this);
                    }
                }
            }
        });
    }

    private void getPersonalDetails() {
        restMethods = RestClient.buildHTTPClient();
        restMethods.registerGetStartResponse(Constants.referredBy, mregSess, Constants.regCompID, "USER", Constants.regUserID, "ASC", 0, 100).enqueue(new Callback<List<GetStartPersEditResp>>() {
            @Override
            public void onResponse(Call<List<GetStartPersEditResp>> call, Response<List<GetStartPersEditResp>> response) {
                Log.e("status", "" + response.body());
                try {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            profileGetResponses.clear();
                            if (!response.body().isEmpty()) {
                                profileGetResponses = (ArrayList<GetStartPersEditResp>) response.body();
                                StringBuilder termsVal = new StringBuilder();
                                for (int i = 0; i < profileGetResponses.size(); i++) {
                                    mName.setText(profileGetResponses.get(i).getFirstName() + "" + profileGetResponses.get(i).getLastName());
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

                                edit_name_layout.setEndIconVisible(true);
                                edit_name_layout.setEnabled(true);
                                edit_name_layout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                                edit_name_layout.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
                                edit_name_layout.setEndIconDrawable(R.drawable.close_image);
                                ProgressDialog.dismissProgress();
                            } else {
                                String Error = response.errorBody().string();
                                JSONObject obj = new JSONObject(Error);
                                String text = obj.getString("message");
                                Util_functions.ShowValidateAlertError(getApplicationContext(), text, "OK");
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
                                Util_functions.ShowValidateAlertError(GetStartPageActivity.this, object1.getString("message"), "OK");
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
                        Util_functions.ShowValidateAlertError(getApplicationContext(), text, "OK");
                        //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.txt_error),true);
                        ProgressDialog.dismissProgress();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ProgressDialog.dismissProgress();
                }
            }

            @Override
            public void onFailure(Call<List<GetStartPersEditResp>> call, Throwable t) {
                if (t.toString().contains("Unable to resolve host")) {
                    Util_functions.ShowValidateAlertError(GetStartPageActivity.this, getString(R.string.check_internet), "OK");
                }
                Util_functions.ShowAlertError(GetStartPageActivity.this, t.getMessage(), "OK", GetStartPageActivity.this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                if (alert_start) {
                    ProgressDialog.showProgress();
                    saveStartSettings();
                } else {
                    Util_functions.ShowValidateAlertError(GetStartPageActivity.this, getString(R.string.fill_personal), "ok");
                }

                break;
            case R.id.btnSavePersonal:
                if (doValidate()) {
                    alert_status = true;
                    Util_functions.AlertDialog(GetStartPageActivity.this, "Do you want to save ?", "OK", "Cancel", GetStartPageActivity.this);
                }
                break;
        }
    }

    private void saveGeneralSettings() {
        ProgressDialog.showProgress();
        registerReq.setCategory(Constants.category_Password);
        registerReq.setCompanyId(Constants.regCompID);
        registerReq.setCountryId(Constants.countryID);
        registerReq.setEmail(et_email.getText().toString());
        registerReq.setEmailVerified("NO");
        registerReq.setFirstName(mName.getText().toString());
        registerReq.setId(Constants.regUserID);
        registerReq.setLastName("");
        registerReq.setStatus("REGISTERED");
        registerReq.setType("USER");
        registerReq.setTimeZoneId("tizo00000000000000000001");
        registerReq.setUsernameVerified("YES");

        registerReq.setUsername(Constants.regUsername);
        RegisterReq.UserField userField = new RegisterReq.UserField();
        mAge = et_age.getText().toString();
        userField.setAge(mAge);
        userField.setEmergencyContactNumber(et_mobilenumber.getText().toString());
        if (!TextUtils.isEmpty(mliceDate)) {
            userField.setDrivingLicenseIssueDate(getFormatDate(mliceDate));
        } else {
            userField.setDrivingLicenseIssueDate(mliceDate);
        }
        registerReq.setUserField(userField);
        RegisterReq.UserPrimaryDetail userPrimaryDetail = new RegisterReq.UserPrimaryDetail();
        userPrimaryDetail.setPrimaryDivisionId("divi00000000000000000001");
        userPrimaryDetail.setPrimaryModuleId("modu00000000000000000001");
        registerReq.setUserPrimaryDetail(userPrimaryDetail);
        restMethods = RestClient.buildHTTPClient();
        restMethods.EditGetStartResponse(Constants.referredBy, Preference_Details.getLoginSessID(), registerReq).enqueue(new Callback<GetStartEditResponse>() {
            @Override
            public void onResponse(Call<GetStartEditResponse> call, Response<GetStartEditResponse> response) {
                Log.e("status", "" + response.body());
                try {
                    if (response.code() == 202) {
                        ProgressDialog.dismissProgress();
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.FrstName, registerReq.getFirstName().trim());
                        //Constants.regUserID= response.body().getId();
                        //Constants.regCompID = response.body().getCompanyId();
                        ///    Util_functions.ShowAlertError(GetStartPageActivity.this, getString(R.string.text_pers_info_added),"OK",GetStartPageActivity.this);
                        Util_functions.ShowValidateAlertError(GetStartPageActivity.this, getString(R.string.text_pers_info_added), "OK");
                        alert_start = true;
                        Log.e("value", response.message().toString());
                    } else if (response.code() == 412) {
                        try {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowValidateAlertError(GetStartPageActivity.this, object1.getString("message"), "OK");
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
                        Util_functions.ShowValidateAlertError(GetStartPageActivity.this, text, "OK");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ProgressDialog.dismissProgress();
                }
            }

            @Override
            public void onFailure(Call<GetStartEditResponse> call, Throwable t) {
                if (t.toString().contains("Unable to resolve host")) {
                    Util_functions.ShowValidateAlertError(GetStartPageActivity.this, getString(R.string.check_internet), "OK");
                }
                t.printStackTrace();
                ProgressDialog.dismissProgress();
                Util_functions.ShowAlertError(GetStartPageActivity.this, t.getMessage(), "OK", GetStartPageActivity.this);
            }
        });
    }

    private void saveStartSettings() {
        UserSettingRequest userSettingRequest = new UserSettingRequest();
        userSettingRequest.setCompanyId(Constants.regCompID);
        userSettingRequest.setName("test"); // need to change
        userSettingRequest.setStatus("REGISTERED");
        userSettingRequest.setType(Constants.SettingType);
        userSettingRequest.setUserId(Constants.regUserID);
        userSettingRequest.setDivisionId(Constants.divisionId);
        userSettingRequest.setModuleID(Constants.moduleID);
        UserSettingRequest.UserPreferenceField userPreferenceField = new UserSettingRequest.UserPreferenceField();
        userPreferenceField.setAppNotification(checkSwitch(switchNoti));
        userPreferenceField.setBackgroundStartup(checkSwitch(switchStBg));
        userPreferenceField.setBeep(checkSwitch(switchBeep));
        seekBarValue = seekBar.getProgress(); // get progress value from the Seek bar
        userPreferenceField.setCoPliotMode(String.valueOf(seekBarValue));
        userPreferenceField.setVoiceAlert(checkSwitch(switchVcAlert));
        userPreferenceField.setSpeedLimitBeep(checkSwitch(switchSLBeep));
        userSettingRequest.setUserPreferenceField(userPreferenceField);
        restMethods = RestClient.buildHTTPClient();
        restMethods.userSettingPostResp(Constants.referredBy, Preference_Details.getLoginSessID(), userSettingRequest).enqueue(new Callback<UserSettingResponse>() {
            @Override
            public void onResponse(Call<UserSettingResponse> call, Response<UserSettingResponse> response) {
                Log.e("status", "" + response.body());
                try {
                    if (response.code() == 201) {

                        ProgressDialog.dismissProgress();
                        Constants.frst_name = mName.getText().toString();
                        Util_functions.ShowAlertError(GetStartPageActivity.this, getString(R.string.text_App_setting_add), "OK", (OnDialogButtonClickListener) GetStartPageActivity.this);
                        Log.e("value", response.message().toString());
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.portionID, "");
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.propertyID, "");
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.sectID, "");
                        if (response.body().getUserPreferenceField() != null) {
                            //Constants.response.body().getUserPreferenceField().getAppNotification();
                            mPrefernce_Manager.setInt(Preference_Keys.LOginKeys.coipilot, Integer.parseInt(response.body().getUserPreferenceField().getCoPliotMode()));
                            mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.voice, response.body().getUserPreferenceField().getVoiceAlert().contains("YES"));
                            mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.startonBg, response.body().getUserPreferenceField().getBackgroundStartup().contains("YES"));
                            mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.speedlimit, response.body().getUserPreferenceField().getSpeedLimitBeep().contains("YES"));
                            mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.beep, response.body().getUserPreferenceField().getBeep().contains("YES"));
                            mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.Appnotif, response.body().getUserPreferenceField().getAppNotification().contains("YES"));

                        }

                    } else if (response.code() == 412) {
                        try {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowValidateAlertError(GetStartPageActivity.this, object1.getString("message"), "OK");
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
            public void onFailure(Call<UserSettingResponse> call, Throwable t) {
                if (t.toString().contains("Unable to resolve host")) {
                    Util_functions.ShowValidateAlertError(GetStartPageActivity.this, getString(R.string.check_internet), "OK");
                }
                t.printStackTrace();
                ProgressDialog.dismissProgress();
                Util_functions.ShowAlertError(GetStartPageActivity.this, t.getMessage(), "OK", GetStartPageActivity.this);
            }
        });


    }

    private String checkSwitch(Switch mswitch) {
        Boolean switchState = mswitch.isChecked();
        if (switchState) {
            return "YES";
        } else {
            return "NO";
        }

    }

    private Boolean doValidate() {
        Boolean status = false;
        if (mName.getText().toString().isEmpty() || mName.getText().toString().equals("")) {
            status_name = false;
            Util_functions.ShowValidateAlertError(GetStartPageActivity.this, getString(R.string.enter_name), "OK");
        } else {
            status_name = true;
            email_mob_Status = true;
            if (et_age.getText().toString().isEmpty()) {
                Util_functions.ShowValidateAlertError(GetStartPageActivity.this, getString(R.string.valid_Age_empty), "OK");
                staus_age = false;
            } else {
                staus_age = true;
                if (!Util_functions.isValidPhoneNumber(et_mobilenumber.getText().toString())) {
                    Util_functions.ShowValidateAlertError(GetStartPageActivity.this, getString(R.string.valid_emgcont), "OK");
                    status_mob = false;
                } else {
                    status_mob = true;
                    if (et_license_date.getText().toString().isEmpty()) {
                        Util_functions.ShowValidateAlertError(GetStartPageActivity.this, getString(R.string.valid_date), "OK");
                        license_date_status = false;
                    } else {
                        license_date_status = true;

                        if (status_name == true && email_mob_Status && status_mob && staus_age && license_date_status) {
                            status = true;
                        }
                    }

                }
            }
        }
        return status;
    }

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
            = (arg0, year, month, day) -> {
        Calendar cal = Calendar.getInstance();
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
    };


    @Override
    public void onPositiveButtonClicked(AlertDialog alertDialog) {
        if (alert_status) {
            ProgressDialog.showProgress();
            saveGeneralSettings();
            alert_status = false;

        } else if (!alert_status) {
            setLoginPreference();
            Constants.mob_num = Constants.regUsername;
            startActivity(new Intent(GetStartPageActivity.this, RoadStartPageActivity.class).putExtra("fragPosition", "0"));
            finish();
        }
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    private void setLoginPreference() {
        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.LoginSessID, mregSess);
        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.userName, Constants.regUsername);
        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.EmailID, Constants.regEmailID);
        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.LoginType, Constants.regType);
        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.companyID, Constants.regCompID);
        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.userID, Constants.regUserID);
        mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.LoginStatus, true);
        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.userStatus, Constants.regStatus);
        Constants.firstTimeUserStatus = true;
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

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
    }

    private void requestLocationPermission() {

        boolean foreground = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (foreground) {
            boolean background = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;

            if (background) {
                handleLocationUpdates();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_CODE_PERMISSIONS);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_CODE_PERMISSIONS);
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
                        AlertDialog(GetStartPageActivity.this, "App in background functionality will be disabled", "OK", "CANCEL", GetStartPageActivity.this);
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
        if (Constants.alertcount) {
            alertDialog.show();
            Constants.alertcount = false;
        }
        alertDialog.setCancelable(false);
        TextView alert_title = layoutView.findViewById(R.id.title_error);
        alert_title.setText(title);
        Button alert_ok = layoutView.findViewById(R.id.alert_ok);
        alert_ok.setText(BtnText);

        alert_ok.setOnClickListener(v -> {
            requestLocationPermission();
            alertDialog.hide();
            alertDialog.cancel();
            alertDialog.dismiss();
            Constants.alertcount = true;
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
        if (Constants.alertcount) {
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
        alert_yes.setOnClickListener(v -> {
            alertDialog.hide();
            alertDialog.cancel();
            alertDialog.dismiss();
            Constants.alertcount = true;
            switchStBg.setChecked(false);
        });

        alert_no.setOnClickListener(v -> {
            requestLocationPermission();
            alertDialog.hide();
            alertDialog.cancel();
            alertDialog.dismiss();
            Constants.alertcount = true;
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
}
