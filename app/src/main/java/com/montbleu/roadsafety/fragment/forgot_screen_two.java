package com.montbleu.roadsafety.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.montbleu.roadsafety.activity.ForgotPassword;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link forgot_screen_two#newInstance} factory method to
 * create an instance of this fragment.
 */
public class forgot_screen_two extends Fragment implements View.OnClickListener, OnDialogButtonClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static final String FRAGMENT_TAG = "Forgot Screen Two";
    public static final String FRAGMENT_TAG2 = "Forgot Screen Three";
    com.google.android.material.textfield.TextInputLayout inputlayout_otpMob;
    com.google.android.material.textfield.TextInputEditText tv_phonenumber_edit;
    PinView pinview;
    androidx.appcompat.widget.AppCompatButton btnSubmit;
    TextView mob_num;
    String TAG = getClass().getName();
    RestMethods restMethods;
    static String otpresponse ="";
    static String mMobileNo ="";
    static String mID ="";
    static String mUserType ="";
    static  String mStatus ="";
    Boolean mconnectStatus = false;
    TextView resend_btn;
    ArrayList<VerifyGetResponse> verifyGetResponses = new ArrayList<>();
    public forgot_screen_two() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment forgot_screen_two.
     */
    // TODO: Rename and change types and number of parameters
    public forgot_screen_two newInstance(String param1, String param2) {
        forgot_screen_two fragment = new forgot_screen_two();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
/*
        Bundle bundle = getArguments();
        int value = bundle.getInt("friendsID");

       Log.e("mobile_number", String.valueOf(value));*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_forgot_screen_two, container, false);
        init(view);
        ProgressDialog.setLottieProgressDialog(getActivity(),Constants.lottiePath);
        ProgressDialog.showProgress();
        OTPVerify();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter("otp"));
        otpresponse = "";
    }

    private void init(View view) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        hideKeyboard(getActivity());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        tv_phonenumber_edit = view.findViewById(R.id.tv_phonenumber_edit);
        resend_btn = view.findViewById(R.id.resend_btn);
        inputlayout_otpMob = view.findViewById(R.id.inputlayout_otpMob);
        tv_phonenumber_edit.setText(getActivity().getIntent().getExtras().getString("mob_num"));
        inputlayout_otpMob.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s1=tv_phonenumber_edit.getText().toString();
                Constants.mob_num=s1;
                ((ForgotPassword) getActivity()).setFragment(new forgot_screen_one(),FRAGMENT_TAG);
                Log.e("mobile",s1);
            }
        });
        pinview = view.findViewById(R.id.pinview);
        pinview.setItemCount(4);
        pinview.setItemHeight(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_size));
        pinview.setItemWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_size));
        pinview.setItemRadius(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_radius));
        pinview.setItemSpacing(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_spacing));
        pinview.setLineWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_line_width));
        pinview.setAnimationEnable(true);// start animation when adding text
        pinview.setCursorVisible(false);
        pinview.requestFocusFromTouch();
        pinview.setItemBackground(getResources().getDrawable(R.drawable.otp_border));

        //mob_num=view.findViewById(R.id.tv_phonenumber_edit);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnSubmit.setClickable(true);
        btnSubmit.setOnClickListener(this);
        //mob_num.setText(getActivity().getIntent().getExtras().getString("mob_num"));
        checkAndRequestPermissions();
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


        pinview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged() called with: s = [" + s + "], start = [" + start + "], before = [" + before + "], count = [" + count + "]");

              /*  if (count == 4) {

                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(),0);
    }

    private void OTPVerify() {
       /* ProgressDialog.setLottieProgressDialog(getActivity(),Constants.lottiePath);
        ProgressDialog.showProgress();*/
        OTPReq otpReq=new OTPReq();
        otpReq.setId(Constants.Static_SessionID);
        otpReq.setUsername(getActivity().getIntent().getExtras().getString("mob_num"));
        //otpReq.setUsername("8012121212");
        otpReq.setStatus("REGISTERED");
        otpReq.setCompanyId(Constants.companyID);
        otpReq.setType(Constants.newForgotPassType);
        restMethods = RestClient.buildHTTPClient();
        restMethods.getOTPVerifyCall(Constants.referredBy,Constants.Static_SessionID,otpReq).enqueue(new Callback<OTPVerification>() {
            @Override
            public void onResponse(Call<OTPVerification> call, Response<OTPVerification> response) {
                try {
                    if (response.code()==201){
                        Log.e("statusstatus",response.body().getVerificationCode());
                        //otpresponse=response.body().getVerificationCode();
                        mID = response.body().getId();
                        mMobileNo =response.body().getUsername();
                        mUserType = response.body().getType();
                        mStatus = response.body().getStatus();
                        //pinview.setText(otpresponse);
                        ProgressDialog.dismissProgress();
                    }  else if(response.code()==412){
                        try {
                            String Error=response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for(int i=0;i<jsonArray1.length();i++){
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowAlertError(getActivity(),object1.getString("message"),"OK",forgot_screen_two.this);
                            }
                            //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.resp_empty),true);
                            ProgressDialog.dismissProgress();
                        } catch (Exception e){
                            e.printStackTrace();
                            ProgressDialog.dismissProgress();

                        }

                    }
                    else {
                        btnSubmit.setClickable(false);
                        String Error=response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text=obj.getString("message");
                        Util_functions.ShowAlertError(getActivity(),text,"OK",forgot_screen_two.this);
                        // Util_functions.ShowValidateAlertError(getContext(),text,"OK");
                        ProgressDialog.dismissProgress();

                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    ProgressDialog.dismissProgress();

                }
            }

            @Override
            public void onFailure(Call<OTPVerification> call, Throwable t) {
                Util_functions.ShowAlertError(getActivity(),t.getMessage(),"OK",forgot_screen_two.this);
                // Util_functions.ShowValidateAlertError(getActivity(),t.getMessage(),"OK");
                ProgressDialog.dismissProgress();

            }
        });
    }

    private void getVerifyPass() {
        ProgressDialog.setLottieProgressDialog(getActivity(),Constants.lottiePath);
        ProgressDialog.showProgress();
        restMethods = RestClient.buildHTTPClient();
        restMethods.verifyGetResponse(Constants.referredBy,Constants.Static_SessionID,Constants.companyID,forgot_screen_two.mID,forgot_screen_two.mMobileNo,pinview.getText().toString(),forgot_screen_two.mStatus,forgot_screen_two.mUserType,"ASC",0,100).enqueue(new Callback<List<VerifyGetResponse>>() {
            @Override
            public void onResponse(Call<List<VerifyGetResponse>> call, Response<List<VerifyGetResponse>> response) {
                Log.e("status",""+response.body());
                try{
                    if (response.code()==200) {
                        if (response.isSuccessful()) {
                            verifyGetResponses.clear();
                            if (!response.body().isEmpty()) {
                                verifyGetResponses = (ArrayList<VerifyGetResponse>) response.body();
                                for (int i = 0; i < verifyGetResponses.size(); i++) {
                                    mID = verifyGetResponses.get(i).getId();
                                    otpresponse=verifyGetResponses.get(i).getVerificationCode();
                                    mMobileNo = verifyGetResponses.get(i).getUsername();
                                    mStatus = verifyGetResponses.get(i).getStatus();
                                    mUserType = verifyGetResponses.get(i).getType();
                                }
                                ((ForgotPassword) getActivity()).setFragment(new forgot_screen_three(),FRAGMENT_TAG2);
                                ProgressDialog.dismissProgress();
                            }  else {
                                ProgressDialog.dismissProgress();
                                Util_functions.ShowAlertError(getActivity(),"OTP is incorrect","OK",forgot_screen_two.this);
                            }
                    }
                        else if(response.code()==412){
                            try {
                                String Error=response.errorBody().string();
                                JSONObject obj = new JSONObject(Error);
                                JSONArray jsonArray1 = obj.getJSONArray("messages");
                                for(int i=0;i<jsonArray1.length();i++){
                                    JSONObject object1 = jsonArray1.getJSONObject(0);
                                    Util_functions.ShowAlertError(getActivity(),object1.getString("message"),"OK",forgot_screen_two.this);
                                    // Util_functions.ShowAlertError(getContext(),object1.getString("message"),"OK",forgot_screen_two.this);
                                }
                                //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.resp_empty),true);
                                ProgressDialog.dismissProgress();
                            } catch (Exception e){
                                e.printStackTrace();
                                ProgressDialog.dismissProgress();

                            }

                        } else {
                            String Error=response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            String text=obj.getString("message");
                            Util_functions.ShowAlertError(getActivity(),text,"OK",forgot_screen_two.this);
                            //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.resp_empty),true);
                            ProgressDialog.dismissProgress();
                        }
                    } else {
                        ProgressDialog.dismissProgress();
                        String Error=response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text=obj.getString("message");
                        Util_functions.ShowAlertError(getActivity(),text,"OK",forgot_screen_two.this);
                        //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.txt_error),true);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    ProgressDialog.dismissProgress();
                }
            }
            @Override
            public void onFailure(Call<List<VerifyGetResponse>> call, Throwable t) {
                Util_functions.ShowAlertError(getActivity(),t.getMessage(),"OK",forgot_screen_two.this);
                t.printStackTrace();
                ProgressDialog.dismissProgress();
            }
        });


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnSubmit:
                if (Util_functions.ConnectivityCheck(getContext(),this).booleanValue()) {
                    mconnectStatus = true;
                    if (pinview.getText().equals("")||pinview.getText().toString().isEmpty()){
                        Util_functions.ShowValidateAlertError(getActivity(),"OTP is Empty","OK");
                    }else{
                        getVerifyPass();
                    }
                    //otpCheck();

                }
                break;
            case R.id.resend_btn:
                OTPVerify();
                break;
        }

    }

    private void otpCheck() {
        String pin_otp=pinview.getText().toString();
        if (pin_otp.equals(otpresponse)){
            if(doValidate()){
                ((ForgotPassword) getActivity()).setFragment(new forgot_screen_three(),FRAGMENT_TAG2);
            }
        }
        else {
            Util_functions.ShowAlertError(getActivity(),"OTP is incorrect","OK",this);
        }
    }

    private Boolean doValidate() {
        if(!pinview.getText().equals("") || pinview.getText().length()==4){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onPositiveButtonClicked(AlertDialog alertDialog) {
        if (!mconnectStatus.booleanValue()) {
            ProgressDialog.dismissProgress();
        } /*else {
            String s1=mob_num.getText().toString();
            Constants.mob_num=s1;
            ((ForgotPassword) getActivity()).setFragment(new forgot_screen_one(),FRAGMENT_TAG2);
            mconnectStatus = false;
        }*/
    }

    @Override
    public void onNegativeButtonClicked() {
        //getActivity().finish();

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
                /*otpOnlyTextView.setText(message.replaceAll("\\D+", ""));
                fullmessageTextView.setText(sender + " : " + message);*/
                //Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                Log.e("OTP MESSSGE", message);
            }
        }
    };

    private boolean checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            int receiveSMS = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.RECEIVE_SMS);
            int readSMS = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_SMS);
            List<String> listPermissionsNeeded = new ArrayList<>();
            if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
            }
            if (readSMS != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.READ_SMS);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(getActivity(),
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
                return false;
            }
            return true;
        }
        return true;
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
        super.onPause();
    }
}