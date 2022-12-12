package com.montbleu.roadsafety.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.montbleu.Pinview.PinView;
import com.montbleu.Retrofit.RestClient;
import com.montbleu.Retrofit.RestMethods;
import com.montbleu.Utils.Constants;
import com.montbleu.Utils.ProgressDialog;
import com.montbleu.Utils.Util_functions;
import com.montbleu.interfaces.OnDialogButtonClickListener;
import com.montbleu.model.OTPReq;
import com.montbleu.model.OTPVerification;
import com.montbleu.model.VerifyGetResponse;
import com.montbleu.roadsafety.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationCodePageActivity extends AppCompatActivity implements View.OnClickListener, OnDialogButtonClickListener {
    private Button btnRoadLogin;
    TextView tv_phonenumber_edit;
    ImageView edit_Image_btn, verify_close;
    PinView pinview;
    RestMethods restMethods;

    String otpresponse;
    String mID;
    String mMobileNo;
    String mUserType;
    String mStatus;
    TextView tv_resend_code,timer;
    String TAG = getClass().getName();
    ArrayList<VerifyGetResponse> verifyGetResponses = new ArrayList<>();
    Boolean mSubmit = false;
    Boolean mconnectStatus = false;
    Boolean mVerifyStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code_page);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        pinview = findViewById(R.id.pinview);
        timer = findViewById(R.id.timer);
        pinview.setItemBackground(getResources().getDrawable(R.drawable.edit_round_corner));
        pinview.setItemCount(4);
        pinview.setItemHeight(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_size));
        pinview.setItemWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_size));
        pinview.setItemRadius(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_radius));
        pinview.setItemSpacing(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_spacing));
        pinview.setLineWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_line_width));
        pinview.setAnimationEnable(true);// start animation when adding text
        pinview.setCursorVisible(true);
        pinview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged() called with: s = [" + s + "], start = [" + start + "], before = [" + before + "], count = [" + count + "]");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        checkAndRequestPermissions();
        tv_resend_code = findViewById(R.id.tv_resend_code);
        tv_resend_code.setOnClickListener(this);
        btnRoadLogin = findViewById(R.id.btnRoadLogin);
        verify_close = findViewById(R.id.verify_close);
        verify_close.setOnClickListener(this);
        tv_phonenumber_edit =  findViewById(R.id.tv_phonenumber_edit);
        edit_Image_btn =  findViewById(R.id.edit_Image_btn);
        if (!getIntent().getStringExtra("mob_number").isEmpty()) {
            tv_phonenumber_edit.setText(getIntent().getStringExtra("mob_number"));
        }
        //tv_phonenumber_edit.setText("1234567890");
        btnRoadLogin.setOnClickListener(this);
        edit_Image_btn.setOnClickListener(this);
        if (Util_functions.ConnectivityCheck(this, this).booleanValue()) {
            //mconnectStatus = true;
            ProgressDialog.showProgress();
            tv_resend_code.setClickable(false);
            tv_resend_code.setTextColor(getColor(R.color.gray_txt));
            OTPVerify();
        }
        ProgressDialog.setLottieProgressDialog(this,Constants.lottiePath);
        ProgressDialog.showProgress();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                final String sender = intent.getStringExtra("Sender");
                String otp = message.substring(message.lastIndexOf(",")+2);
                otp = otp.substring(0,4);
                pinview.setText(otp);
                //Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                Log.e("OTP MESSSGE", message);
            }
        }
    };

    private boolean checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            int receiveSMS = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS);
            int readSMS = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS);
            List<String> listPermissionsNeeded = new ArrayList<>();
            if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
            }
            if (readSMS != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.READ_SMS);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
                return false;
            }
            return true;
        }
        return true;

    }


    private void DoActivateUser() {
        OTPReq otpReq = new OTPReq();
        otpReq.setId(mID);
        otpReq.setUsername(mMobileNo);
        otpReq.setVerificationCode(otpresponse);
        otpReq.setStatus(mStatus);
        otpReq.setType(mUserType);
        otpReq.setCompanyId(Constants.companyID);
        restMethods = RestClient.buildHTTPClient();
        restMethods.putOTPVerifyCall(Constants.referredBy, Constants.Static_SessionID, otpReq).enqueue(new Callback<OTPVerification>() {
            @Override
            public void onResponse(@NonNull Call<OTPVerification> call, @NonNull Response<OTPVerification> response) {
                try {
                    if (response.code() == 202) {
                        Log.e("statusstatus", response.body().toString());
                        Util_functions.ShowAlertError(VerificationCodePageActivity.this, getString(R.string.text_activate), "OK", (OnDialogButtonClickListener) VerificationCodePageActivity.this);
                        ProgressDialog.dismissProgress();
                        mSubmit = true;
                    } else if (response.code() == 412) {
                        try {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowValidateAlertError(VerificationCodePageActivity.this, object1.getString("message"), "OK");
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
                        Util_functions.ShowValidateAlertError(VerificationCodePageActivity.this, text, "OK");
                        ProgressDialog.dismissProgress();
                        mSubmit = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ProgressDialog.dismissProgress();
                }

            }

            @Override
            public void onFailure(@NonNull Call<OTPVerification> call, @NonNull Throwable t) {
                if (t.toString().contains("Unable to resolve host")){
                    Util_functions.ShowValidateAlertError(VerificationCodePageActivity.this,getString(R.string.check_internet),"OK");
                }
                Util_functions.ShowAlertError(VerificationCodePageActivity.this,t.getMessage(),"OK",VerificationCodePageActivity.this);
                ProgressDialog.dismissProgress();

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_Image_btn:
                edit_Image_btn.setClickable(false);
                /*finish();*/
                //AlertDialog(VerificationCodePageActivity.this,"Change and get otp for the new number","OK","CANCEL");
                break;
            case R.id.btnRoadLogin:
                mVerifyStatus = true;
                if (Util_functions.ConnectivityCheck(this, this).booleanValue()) {
                    mconnectStatus = true;

                   /* if (pinview.getText().toString().equals(otpval)) {
                        ProgressDialog.showProgress();
                        DoActivateUser();
                    }
                    else {*/
                        /*if (pinview.getText().toString().isEmpty()){
                            Util_functions.ShowValidateAlertError(VerificationCodePageActivity.this, "Enter Your OTP", "OK");
                            break;
                        }
                        else {
                            DoActivateUser();
                            //Util_functions.ShowValidateAlertError(VerificationCodePageActivity.this, "OTP is incorrect", "OK");
                        break;
                        *//*}*//*
                    }*/

                    // need to change
                    if (pinview.getText().toString().isEmpty()||pinview.getText().equals("")){
                        Util_functions.ShowValidateAlertError(VerificationCodePageActivity.this,"OTP is Empty","OK");

                    }else {
                        getVerifyPass();
                    }
                    //getVerifyPass();

                }
                break;
            case R.id.verify_close:
                finish();
                break;
            case R.id.tv_resend_code:
                if (Util_functions.ConnectivityCheck(this, this).booleanValue()) {
                    ProgressDialog.showProgress();
                    tv_resend_code.setClickable(false);
                    tv_resend_code.setTextColor(getColor(R.color.gray_txt));
                    OTPVerify();
                }
                break;
        }
    }

    private void OTPVerify() {
        OTPReq otpReq = new OTPReq();
        otpReq.setId(Constants.Static_SessionID);
        otpReq.setUsername(tv_phonenumber_edit.getText().toString());
        otpReq.setStatus("REGISTERED");
        otpReq.setType("USER_ACTIVATION");
        otpReq.setCompanyId(Constants.companyID);
        restMethods = RestClient.buildHTTPClient();
        restMethods.getOTPVerifyCall(Constants.referredBy, Constants.Static_SessionID, otpReq).enqueue(new Callback<OTPVerification>() {
            @Override
            public void onResponse(@NonNull Call<OTPVerification> call, @NonNull Response<OTPVerification> response) {
                try {
                    if (response.code() == 201) {
                        Log.e("statusstatus", response.body().getVerificationCode());
                        //otpresponse = response.body().getVerificationCode();
                        mID = response.body().getId();
                        mMobileNo = response.body().getUsername();
                        mUserType = response.body().getType();
                        mStatus = response.body().getStatus();
                        startTimer();
                        ProgressDialog.dismissProgress();
                    } else if (response.code() == 412) {
                        try {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowValidateAlertError(VerificationCodePageActivity.this, object1.getString("message"), "OK");
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
                        Util_functions.ShowValidateAlertError(VerificationCodePageActivity.this, text, "OK");
                        ProgressDialog.dismissProgress();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<OTPVerification> call, @NonNull Throwable t) {
                if (t.toString().contains("Unable to resolve host")){
                    Util_functions.ShowValidateAlertError(VerificationCodePageActivity.this,getString(R.string.check_internet),"OK");
                }
                Util_functions.ShowAlertError(VerificationCodePageActivity.this,t.getMessage(),"OK",VerificationCodePageActivity.this);                ProgressDialog.dismissProgress();
            }
        });


    }
private void startTimer(){
    new CountDownTimer(60000, 1000) {
        public void onTick(long millisUntilFinished) {
            // Used for formatting digit to be in 2 digits only
            NumberFormat f = new DecimalFormat("00");
            //long hour = (millisUntilFinished / 3600000) % 24;
            long min = (millisUntilFinished / 60000) % 60;
            long sec = (millisUntilFinished / 1000) % 60;
            timer.setText(f.format(min) + ":" + f.format(sec));
        }
        // When the task is over it will print 00:00:00 there
        public void onFinish() {
            tv_resend_code.setClickable(true);
            tv_resend_code.setTextColor(getColor(R.color.color_resend_code));
            timer.setText("00:00");
        }
    }.start();
}
    private void getVerifyPass() {
        ProgressDialog.setLottieProgressDialog(this,Constants.lottiePath);
        ProgressDialog.showProgress();
        restMethods = RestClient.buildHTTPClient();
        restMethods.verifyGetResponse(Constants.referredBy, Constants.Static_SessionID,Constants.companyID,mID, mMobileNo, pinview.getText().toString(), mStatus, mUserType, "ASC", 0, 100).enqueue(new Callback<List<VerifyGetResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<VerifyGetResponse>> call, @NonNull Response<List<VerifyGetResponse>> response) {
                Log.e("status", "" + response.body());
                try {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            verifyGetResponses.clear();
                            if (!response.body().isEmpty()) {
                                verifyGetResponses = (ArrayList<VerifyGetResponse>) response.body();
                                for (int i = 0; i < verifyGetResponses.size(); i++) {
                                    mID = verifyGetResponses.get(i).getId();
                                    mMobileNo = verifyGetResponses.get(i).getUsername();
                                    mStatus = verifyGetResponses.get(i).getStatus();
                                    mUserType = verifyGetResponses.get(i).getType();
                                    otpresponse=verifyGetResponses.get(i).getVerificationCode();
                                }
                                DoActivateUser();
                                ProgressDialog.dismissProgress();
                            } else {
                                Util_functions.ShowValidateAlertError(VerificationCodePageActivity.this,"OTP is incorrect","OK");
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
                                Util_functions.ShowValidateAlertError(VerificationCodePageActivity.this, object1.getString("message"), "OK");
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
            public void onFailure(@NonNull Call<List<VerifyGetResponse>> call, @NonNull Throwable t) {
                if (t.toString().contains("Unable to resolve host")){
                    Util_functions.ShowValidateAlertError(VerificationCodePageActivity.this,getString(R.string.check_internet),"OK");
                }
                Util_functions.ShowAlertError(VerificationCodePageActivity.this,t.getMessage(),"OK",VerificationCodePageActivity.this);
                t.printStackTrace();
                ProgressDialog.dismissProgress();
            }
        });


    }

    @Override
    public void onPositiveButtonClicked(AlertDialog alertDialog) {
        if (!mconnectStatus.booleanValue()) {
            ProgressDialog.dismissProgress();
            if (!mVerifyStatus.booleanValue()) {
                finish();
                mVerifyStatus = false;
            }


        } else {
            if (mSubmit) {
                startActivity(new Intent(VerificationCodePageActivity.this, RoadInstallingmapPageActivity.class));
                finish();
                mVerifyStatus = false;
                mconnectStatus = false;
            }
        }


    }

    @Override
    public void onNegativeButtonClicked() {
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(VerificationCodePageActivity.this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(VerificationCodePageActivity.this).unregisterReceiver(receiver);
        super.onPause();
    }

}
