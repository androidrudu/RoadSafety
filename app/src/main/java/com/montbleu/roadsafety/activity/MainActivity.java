package com.montbleu.roadsafety.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.montbleu.Retrofit.RestClient;
import com.montbleu.Retrofit.RestMethods;
import com.montbleu.Utils.Constants;
import com.montbleu.Utils.PreferenceManager;
import com.montbleu.Utils.Preference_Details;
import com.montbleu.Utils.Util_functions;
import com.montbleu.interfaces.OnDialogButtonClickListener;
import com.montbleu.interfaces.Preference_Keys;
import com.montbleu.model.LoginRequest;
import com.montbleu.model.LoginResponse;
import com.montbleu.model.MapUpdateResponse;
import com.montbleu.roadsafety.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnDialogButtonClickListener {
    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 800;
    RestMethods restMethods;
    private PreferenceManager mPrefernce_Manager = PreferenceManager.getInstance();
    Boolean sts = false;
    Long strDate = 0L;
    int diff_dur = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    if (Preference_Details.getLoginStatus() == true || !(strDate == 0)) {
                        strDate = getUniTime();
                        getDuration(String.valueOf(Preference_Details.getsessTime()), String.valueOf(strDate));
                        Constants.mob_num = Preference_Details.getUserName().trim();
                        Constants.regUsername = Preference_Details.getUserName().trim();
                        Constants.pwd = Preference_Details.getPWD().trim();
                        getMapUpdate();
                        if (!Constants.regUsername.isEmpty() || !Constants.pwd.isEmpty()) {
                            if (diff_dur >= 8 || diff_dur <= -8) {
                                getMapUpdate();
                                callLogin();
                            } else {

                                Constants.frst_name = Preference_Details.getFrstName().trim();
                                Constants.regUserID = Preference_Details.getUserID().trim();
                                Intent mainIntent = new Intent(MainActivity.this, RoadStartPageActivity.class);
                                startActivity(mainIntent);
                                finish();
                            }
                        }
                    } else {
                        //Create an Intent that will start the Menu-Activity.
                        Intent mainIntent = new Intent(MainActivity.this, LoginPageActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }
                } else {
                    Util_functions.ShowAlertError(MainActivity.this, "Check Your Internet Connection", "ok", MainActivity.this);
                }

            }


        }, SPLASH_DISPLAY_LENGTH);
    }


    private void callLogin() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setId("");
        loginRequest.setCategory(Constants.category_Password);
        loginRequest.setCompanyId(Constants.companyID);
        loginRequest.setUsername(Preference_Details.getUserName());
        loginRequest.setPassword(Preference_Details.getPWD());
        LoginRequest.UserSessionField userSessionField = new LoginRequest.UserSessionField();
        userSessionField.setDeviceType("ANDROID");
        userSessionField.setDeviceStatus("DEFAULT");
        userSessionField.setDeviceOrderId("3523");
        userSessionField.setDeviceUniqueId(Preference_Details.getDeviceId());
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
                        Constants.frst_name = response.body().getUser().getFirstName();
                        Constants.regSessionID = response.body().getUserSession().getId();
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.pwd, Preference_Details.getPWD());
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.LoginSessID, response.body().getUserSession().getId());
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.userName, response.body().getUserSession().getUsername());
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.EmailID, response.body().getUser().getEmail());
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.LoginType, response.body().getUser().getType());
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.companyID, response.body().getUser().getCompanyId());
                        //  mPrefernce_Manager.setString(Preference_Keys.LOginKeys.countryID,response.body().getUser().);
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.userID, response.body().getUser().getId());
                        mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.LoginStatus, true);
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.FrstName, response.body().getUser().getFirstName().trim());
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.userStatus, response.body().getUser().getStatus());
                        mPrefernce_Manager.setLong(Preference_Keys.LOginKeys.sessionTime, getUniTime());
                        //  Util_functions.Show_Toast(getApplicationContext(),getString(R.string.txt_login_success),false);
                        startActivity(new Intent(MainActivity.this, RoadStartPageActivity.class));
                        finish();

                    } else if (response.code() == 412) {
                        try {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                startActivity(new Intent(MainActivity.this, LoginPageActivity.class));
                                finish();
                            }
                            startActivity(new Intent(MainActivity.this, LoginPageActivity.class));
                            //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.resp_empty),true);
                            finish();

                        } catch (Exception e) {
                            e.printStackTrace();
                            startActivity(new Intent(MainActivity.this, LoginPageActivity.class));
                            finish();
                        }

                    } else {
                        String Error = response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text = obj.getString("message");
                        //Util_functions.ShowValidateAlertError(MainActivity.this,text,"OK");
                        startActivity(new Intent(MainActivity.this, LoginPageActivity.class));
                        // Util_functions.Show_Toast(getApplicationContext(),getString(R.string.txt_error),true);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    startActivity(new Intent(MainActivity.this, LoginPageActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                if (t.toString().contains("Unable to resolve host")) {
                    Util_functions.ShowAlertError(MainActivity.this, "Check Your Internet Connection", "ok", MainActivity.this);
                } else {
                    startActivity(new Intent(MainActivity.this, RoadStartPageActivity.class));
                    finish();
                }
            }
        });
    }

    @Override
    public void onPositiveButtonClicked(AlertDialog alertDialog) {
        finish();
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    private Long getUniTime() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        long timeInMili = calendar.getTimeInMillis();
        return timeInMili;
    }

    private void getDuration(String call_starttime, String call_endtime) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
            call_endtime = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm:ss aa", new Date(Long.parseLong(String.valueOf(call_endtime)))).toString();
            call_starttime = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm:ss aa", new Date(Long.parseLong(String.valueOf(call_starttime)))).toString();
            Date date1 = format.parse(call_starttime);
            Date date2 = format.parse(call_endtime);
            long mills = date2.getTime() - date1.getTime();
            Log.v("Data1", "" + date1.getTime());
            Log.v("Data2", "" + date2.getTime());
            int hours = (int) (mills / (1000 * 60 * 60));
            int mins = (int) (mills / (1000 * 60)) % 60;
            int sec = (int) (mills / 1000) % 60;
            int days = hours / 24;
            Log.e("Time Diff Session", days + ":" + hours + ":" + mins + ":" + sec);
            diff_dur = hours; // updated value every1 second
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getMapUpdate() {
        restMethods = RestClient.buildHTTPClient();
        restMethods.MapUpdateReq(Constants.referredBy, Preference_Details.getLoginSessID(), Constants.companyID, Constants.divisionId, Constants.moduleID, Constants.type_map_update, "createdAt", "DESC", "0", "1").enqueue(new Callback<List<MapUpdateResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<MapUpdateResponse>> call, @NonNull Response<List<MapUpdateResponse>> response) {
                try {
                    if (response.code() == 200) {
                        for (int i = 0; i < response.body().size(); i++) {
                            mPrefernce_Manager.setString(Preference_Keys.LOginKeys.mapCode, response.body().get(i).getCode());
                        }
                    } else if (response.code() == 412) {
                        try {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        String Error = response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text = obj.getString("message");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<MapUpdateResponse>> call, @NonNull Throwable t) {
                if (t.toString().contains("Unable to resolve host")) {
                    Util_functions.ShowAlertError(MainActivity.this, "Check Your Internet Connection", "ok", MainActivity.this);
                }
            }
        });

    }
}