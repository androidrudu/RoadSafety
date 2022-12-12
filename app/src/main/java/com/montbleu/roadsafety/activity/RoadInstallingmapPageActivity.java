package com.montbleu.roadsafety.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.montbleu.Retrofit.RestClient;
import com.montbleu.Retrofit.RestMethods;
import com.montbleu.Utils.Constants;
import com.montbleu.Utils.PreferenceManager;
import com.montbleu.Utils.Preference_Details;
import com.montbleu.Utils.ProgressDialog;
import com.montbleu.Utils.Util_functions;
import com.montbleu.adapter.SpinnerMapAdapter;
import com.montbleu.adapter.SpinnerMapCountryAdapter2;
import com.montbleu.interfaces.OnDialogButtonClickListener;
import com.montbleu.interfaces.Preference_Keys;
import com.montbleu.model.ContinentResponse;
import com.montbleu.model.CountryResponse;
import com.montbleu.model.LoginRequest;
import com.montbleu.model.LoginResponse;
import com.montbleu.model.MapSettingResponse;
import com.montbleu.model.UserMapSettingReq;
import com.montbleu.roadsafety.BuildConfig;
import com.montbleu.roadsafety.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoadInstallingmapPageActivity extends AppCompatActivity implements OnDialogButtonClickListener {

    private ArrayList<ContinentResponse> continentItem;
    private ArrayList<CountryResponse> countrytItem;
    private ArrayList<ContinentResponse> continentItemClone;
    private ArrayList<CountryResponse> countrytItemClone;

    Button btnRoadMapSubmit;
    RestMethods restMethods;
    Spinner spinner_continent;
    Spinner spinner_country;
    SpinnerMapAdapter spinnerMapAdapter;
    SpinnerMapCountryAdapter2 spinnerMapCountryAdapter2;
    String ContinentID = "";
    String CountryID = "";
    String mCompanyID = "";
    static String mregSess = "";
    String mregId = "";
    private PreferenceManager mPrefernce_Manager = PreferenceManager.getInstance();
    Boolean mconnectStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_road_installingmap_page);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        ProgressDialog.setLottieProgressDialog(RoadInstallingmapPageActivity.this, Constants.lottiePath);
        ProgressDialog.showProgress();
        restMethods = RestClient.buildHTTPClient();
        continentItem = new ArrayList<>();
        countrytItem = new ArrayList<>();
        continentItemClone = new ArrayList<>();
        countrytItemClone = new ArrayList<>();
        btnRoadMapSubmit = findViewById(R.id.btnRoadMapSubmit);
        spinner_country = findViewById(R.id.spinner_country);
        spinner_continent = findViewById(R.id.spinner_continent);
        spinner_continent.setSelected(false);  // must
        spinner_continent.setSelection(0, true);
        // spinner_continent.setOnItemSelectedListener(this);
        spinner_country.setSelected(false);  // must
        spinner_country.setSelection(1, true);
        //spinner_country.setOnItemSelectedListener(this);
        spinner_continent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == -1) {
                } else {
                    ContinentID = continentItem.get(position).getId();
                    continentItemClone.clear();
                    continentItemClone.add(continentItem.get(position));
                    if (!ContinentID.isEmpty()) {
                        ProgressDialog.showProgress();
                        if (Util_functions.ConnectivityCheck(RoadInstallingmapPageActivity.this, RoadInstallingmapPageActivity.this).booleanValue()) {
                            //mconnectStatus = true;
                            getCountry();
                        }
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == -1) {
                } else {
                    CountryID = countrytItem.get(position).getId();
                    countrytItemClone.clear();
                    countrytItemClone.add(countrytItem.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        if (Util_functions.ConnectivityCheck(RoadInstallingmapPageActivity.this, RoadInstallingmapPageActivity.this).booleanValue()) {
            // mconnectStatus = true;
            getContinent();
        }


        btnRoadMapSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner_continent.getSelectedItemPosition() >= 0 && spinner_country.getSelectedItemPosition() >= 0) {
                    mPrefernce_Manager.setString(Preference_Keys.CommonKeys.ContinentID, ContinentID);
                    mPrefernce_Manager.setString(Preference_Keys.CommonKeys.CountryID, CountryID);
                    mPrefernce_Manager.setString(Preference_Keys.CommonKeys.ContinentArray, new Gson().toJson(continentItemClone));
                    mPrefernce_Manager.setString(Preference_Keys.CommonKeys.ContinentID, new Gson().toJson(countrytItemClone));
                    if (Util_functions.ConnectivityCheck(RoadInstallingmapPageActivity.this, RoadInstallingmapPageActivity.this).booleanValue()) {
                        mconnectStatus = true;
                        ProgressDialog.showProgress();
                        getLoginSession();
                    }

                }
            }
        });

        spinner_country.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onGlobalLayout() {
                if (spinner_country.isEnabled()) {
//                    Toast.makeText(getApplicationContext(),"Spinner country",Toast.LENGTH_LONG);
                }
                //  ((TextView) spinner_country.getSelectedView()).setTextColor(R.color.color_title); //change to your color
            }
        });

        spinner_continent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onGlobalLayout() {
                //     ((TextView) spinner_continent.getSelectedView()).setTextColor(R.color.color_title); //change to your color
            }
        });

    }

    public void startLocationButtonClick() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
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

    private void getLoginSession() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setId("");
        loginRequest.setCategory(Constants.category_Password);
        loginRequest.setCompanyId(Constants.companyID);
        loginRequest.setUsername(Constants.regUsername);
        loginRequest.setPassword(Constants.regPass);
        LoginRequest.UserSessionField userSessionField = new LoginRequest.UserSessionField();
        userSessionField.setDeviceType("ANDROID");
        userSessionField.setDeviceStatus("REGISTERED");
        userSessionField.setDeviceOrderId("3523");
        userSessionField.setDeviceUniqueId(Constants.M_ANDROID_ID);
        userSessionField.setDeviceVersionNumber(String.valueOf(Build.VERSION.RELEASE));
        DecimalFormat REAL_FORMATTER = new DecimalFormat("0");
        userSessionField.setDeviceModelName(String.valueOf(Build.MANUFACTURER) + " " + String.valueOf(Build.MODEL) + " " + REAL_FORMATTER.format(Double.parseDouble(Build.VERSION.RELEASE)));
        loginRequest.setUserSessionField(userSessionField);
        restMethods = RestClient.buildHTTPClient();
        restMethods.getLoginResponse(Constants.referredBy, Constants.Static_SessionID, loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                try {
                    if (response.code() == 201) {
                        Log.e("status", "" + response.body());
                        mregSess = response.body().getUserSession().getId();
                        mregId = response.body().getUser().getId();
                        mCompanyID = response.body().getUser().getCompanyId();
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.userID, response.body().getUser().getId());
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.LoginSessID, response.body().getUserSession().getId());
                        SaveMapSettings();

                    } else if (response.code() == 412) {

                        try {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowValidateAlertError(RoadInstallingmapPageActivity.this, object1.getString("message"), "OK");
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
                        Util_functions.ShowValidateAlertError(RoadInstallingmapPageActivity.this, text, "OK");
                        // Util_functions.Show_Toast(getApplicationContext(),getString(R.string.txt_error),true);
                        ProgressDialog.dismissProgress();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ProgressDialog.dismissProgress();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                if (t.toString().contains("Unable to resolve host")) {
                    Util_functions.ShowValidateAlertError(RoadInstallingmapPageActivity.this, getString(R.string.check_internet), "OK");
                }
                Util_functions.ShowAlertError(RoadInstallingmapPageActivity.this, t.getMessage(), "OK", RoadInstallingmapPageActivity.this);
                t.printStackTrace();
                ProgressDialog.dismissProgress();
            }
        });
    }

    public void SaveMapSettings() {
        UserMapSettingReq userSettingRequest = new UserMapSettingReq();
        userSettingRequest.setCompanyId(mCompanyID);
        userSettingRequest.setDivisionId(Constants.divisionId);
        userSettingRequest.setModuleId(Constants.moduleID);
        userSettingRequest.setName("user map settings"); // need to change
        userSettingRequest.setStatus("REGISTERED");
        userSettingRequest.setUserId(Constants.regUserID);
        UserMapSettingReq.UserPreferenceField userPreferenceField = new UserMapSettingReq.UserPreferenceField();
        userPreferenceField.setContinentId(ContinentID);
        userPreferenceField.setCountryId(CountryID);
        userSettingRequest.setUserPreferenceField(userPreferenceField);
        userSettingRequest.setType(Constants.mapSettingsType);
        restMethods = RestClient.buildHTTPClient();
        restMethods.userMapSettingPostResp(Constants.referredBy, Preference_Details.getLoginSessID(), userSettingRequest).enqueue(new Callback<MapSettingResponse>() {
            @Override
            public void onResponse(@NonNull Call<MapSettingResponse> call, @NonNull Response<MapSettingResponse> response) {
                Log.e("status", "" + response.body());
                try {
                    if (response.code() == 201) {
                        ProgressDialog.dismissProgress();
                        Util_functions.ShowAlertError(RoadInstallingmapPageActivity.this, getString(R.string.text_Map_setting_add), "OK", RoadInstallingmapPageActivity.this);
                        Log.e("value", response.message().toString());
                    } else if (response.code() == 412) {
                        try {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowValidateAlertError(RoadInstallingmapPageActivity.this, object1.getString("message"), "OK");
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
                        Util_functions.ShowValidateAlertError(RoadInstallingmapPageActivity.this, text, "OK");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ProgressDialog.dismissProgress();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MapSettingResponse> call, @NonNull Throwable t) {
                if (t.toString().contains("Unable to resolve host")) {
                    Util_functions.ShowValidateAlertError(RoadInstallingmapPageActivity.this, getString(R.string.check_internet), "OK");
                }
                t.printStackTrace();
                ProgressDialog.dismissProgress();
                Util_functions.ShowAlertError(RoadInstallingmapPageActivity.this, t.getMessage(), "OK", RoadInstallingmapPageActivity.this);
            }
        });
    }

    private void getContinent() {
        //Preference_Details.getLoginSessID()
        restMethods.getContinentResponse("ANDROID", Constants.Static_SessionID, Constants.companyID, "road").enqueue(new Callback<List<ContinentResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<ContinentResponse>> call, @NonNull Response<List<ContinentResponse>> response) {
                Log.e("status", "" + response.body());
                try {
                    if (response.code() == 200) {
                        continentItem.clear();
                        for (int i = 0; i < response.body().size(); i++) {
                            continentItem.add(response.body().get(i));
                        }
                        spinnerMapAdapter = new SpinnerMapAdapter(RoadInstallingmapPageActivity.this,
                                R.layout.listitems_layout, R.id.title, continentItem);
                        spinner_continent.setAdapter(spinnerMapAdapter);
                        ProgressDialog.dismissProgress();
                    } else if (response.code() == 412) {
                        try {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowValidateAlertError(RoadInstallingmapPageActivity.this, object1.getString("message"), "OK");
                            }
                            //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.resp_empty),true);
                            ProgressDialog.dismissProgress();
                        } catch (Exception e) {
                            e.printStackTrace();
                            ProgressDialog.dismissProgress();

                        }

                    } else {
                        String Error = "";
                        JSONObject obj = new JSONObject(Error);
                        String text = obj.getString("message");
                        Util_functions.ShowValidateAlertError(RoadInstallingmapPageActivity.this, text, "OK");
                        //  Util_functions.Show_Toast(getApplicationContext(),getString(R.string.txt_error),true);
                        ProgressDialog.dismissProgress();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ProgressDialog.dismissProgress();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ContinentResponse>> call, @NonNull Throwable t) {
                if (t.toString().contains("Unable to resolve host")) {
                    Util_functions.ShowValidateAlertError(RoadInstallingmapPageActivity.this, getString(R.string.check_internet), "OK");
                }
                Util_functions.ShowAlertError(RoadInstallingmapPageActivity.this, t.getMessage(), "OK", RoadInstallingmapPageActivity.this);
                t.printStackTrace();
                ProgressDialog.dismissProgress();
            }
        });

    }

    private void getCountry() {
        //Preference_Details.getLoginSessID()
        restMethods.getCountryResponse(Constants.referredBy, Constants.Static_SessionID, Constants.companyID, ContinentID, "road").enqueue(new Callback<List<CountryResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<CountryResponse>> call, @NonNull Response<List<CountryResponse>> response) {
                Log.e("status", "" + response.body());
                try {
                    if (response.code() == 200) {
                        countrytItem.clear();
                        for (int i = 0; i < response.body().size(); i++) {
                            countrytItem.add(response.body().get(i));
                        }
                        spinnerMapCountryAdapter2 = new SpinnerMapCountryAdapter2(RoadInstallingmapPageActivity.this,
                                R.layout.listitems_layout, R.id.title, countrytItem);
                        spinner_country.setAdapter(spinnerMapCountryAdapter2);
                        ProgressDialog.dismissProgress();
                    } else if (response.code() == 412) {
                        try {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowValidateAlertError(RoadInstallingmapPageActivity.this, object1.getString("message"), "OK");
                            }
                            //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.resp_empty),true);
                            ProgressDialog.dismissProgress();
                        } catch (Exception e) {
                            e.printStackTrace();
                            ProgressDialog.dismissProgress();

                        }

                    } else {
                        String Error = "";
                        JSONObject obj = new JSONObject(Error);
                        String text = obj.getString("message");
                        Util_functions.ShowAlertError(RoadInstallingmapPageActivity.this, text, "OK", RoadInstallingmapPageActivity.this);
                        //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.txt_error),true);
                        ProgressDialog.dismissProgress();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ProgressDialog.dismissProgress();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CountryResponse>> call, @NonNull Throwable t) {
                if (t.toString().contains("Unable to resolve host")) {
                    Util_functions.ShowValidateAlertError(RoadInstallingmapPageActivity.this, getString(R.string.check_internet), "OK");
                }
                Util_functions.ShowAlertError(RoadInstallingmapPageActivity.this, t.getMessage(), "OK", RoadInstallingmapPageActivity.this);
                t.printStackTrace();
                ProgressDialog.dismissProgress();
            }
        });

    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
    }

    @Override
    public void onPositiveButtonClicked(AlertDialog alertDialog) {

        if (!mconnectStatus.booleanValue()) {
            ProgressDialog.dismissProgress();
        } else {
            startActivity(new Intent(RoadInstallingmapPageActivity.this, RoadInstalingMapViewPageActivity.class));
            finish();
        }

    }

    @Override
    public void onNegativeButtonClicked() {

    }

}
