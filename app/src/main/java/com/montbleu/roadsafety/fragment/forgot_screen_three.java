package com.montbleu.roadsafety.fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.montbleu.Retrofit.RestClient;
import com.montbleu.Retrofit.RestMethods;
import com.montbleu.Utils.Constants;
import com.montbleu.Utils.ProgressDialog;
import com.montbleu.Utils.Util_functions;
import com.montbleu.interfaces.OnDialogButtonClickListener;
import com.montbleu.model.OTPReq;
import com.montbleu.model.OTPVerification;
import com.montbleu.roadsafety.R;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link forgot_screen_three#newInstance} factory method to
 * create an instance of this fragment.
 */
public class forgot_screen_three extends Fragment implements View.OnClickListener, OnDialogButtonClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextInputEditText newPassword;
    TextInputEditText confPassword;
    Boolean newPass_status = false,confPassStatus = true,new_conf=false;
    Button btnSendOTP;
    ImageView close_btn;
    RestMethods restMethods;
    public forgot_screen_three() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment forgot_screen_three.
     */
    // TODO: Rename and change types and number of parameters
    public static forgot_screen_three newInstance(String param1, String param2) {
        forgot_screen_three fragment = new forgot_screen_three();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
        View view = inflater.inflate(R.layout.fragment_forgot_screen_three, container, false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        newPassword = view.findViewById(R.id.etNewPass);
        confPassword = view.findViewById(R.id.etConfpass);
        btnSendOTP = view.findViewById(R.id.btnSendOTP);
        btnSendOTP.setOnClickListener(this);

        //newPassword.setOnClickListener(this);mn,
       // confPassword.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSendOTP:
                String pass = "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*+=?-]).{8,20}$";
                if (newPassword.getText().toString().isEmpty() && confPassword.getText().toString().isEmpty()) {
                    Util_functions.ShowValidateAlertError(getActivity(), getString(R.string.enter_pwd), "ok");
                } else {
                    if (newPassword.getText().toString().isEmpty()) {
                        newPass_status = false;
                        Util_functions.ShowValidateAlertError(getActivity(), getString(R.string.new_pwd), "ok");
                        break;
                    } else {
                        if (!(newPassword.length()>7)){
                            Util_functions.ShowValidateAlertError(getActivity(), getString(R.string.valid_password_len), "OK");
                            break;
                        }
                        if (newPassword.getText().toString().trim().matches(pass)) {
                            newPass_status = true;

                        } else {
                            newPass_status = false;
                            Util_functions.ShowValidateAlertError(getActivity(), getString(R.string.val_new_pwd), "ok");
                            break;
                        }
                        if (confPassword.getText().toString().isEmpty()) {
                            Util_functions.ShowValidateAlertError(getActivity(), getString(R.string.conf_pwd), "ok");
                            confPassStatus = false;
                            break;
                        } else {
                            if (!(confPassword.length()>7)){
                                Util_functions.ShowValidateAlertError(getActivity(), getString(R.string.valid_password_len), "OK");
                                break;
                            }
                            if (confPassword.getText().toString().trim().matches(pass)) {
                                confPassStatus = true;

                            } else {
                                Util_functions.ShowValidateAlertError(getActivity(), getString(R.string.val_conf_pwd), "ok");
                                confPassStatus = false;
                                break;
                            }
                            if (newPass_status==true&&confPassStatus==true){
                                if (newPassword.getText().toString().contentEquals(confPassword.getText().toString())){
                                    ProgressDialog.setLottieProgressDialog(getActivity(),Constants.lottiePath);
                                    ProgressDialog.showProgress();
                                    doNewPassChange();

                                }
                                else {
                                    Util_functions.ShowValidateAlertError(getActivity(), getString(R.string.pass_not_match), "ok");
                                    new_conf=false;
                                    break;
                                }
                            }

                        }
                    }

                }


                break;
            case R.id.etNewPass:

                break;
            case R.id.etConfpass:

                break;


        }
    }


    private void doNewPassChange() {
            OTPReq otpReq=new OTPReq();
            otpReq.setId(forgot_screen_two.mID);
            otpReq.setUsername(forgot_screen_two.mMobileNo);
            otpReq.setVerificationCode(forgot_screen_two.otpresponse);
            otpReq.setStatus(forgot_screen_two.mStatus);
            otpReq.setType(forgot_screen_two.mUserType);
            otpReq.setCompanyId(Constants.companyID);
            OTPReq.UserVerificationField userVerificationField = new OTPReq.UserVerificationField();
            userVerificationField.setPassword(confPassword.getText().toString());
            //userVerificationField.setPassword("test123");
            otpReq.setUserVerificationField(userVerificationField);
            restMethods = RestClient.buildHTTPClient();
            restMethods.putOTPVerifyCall(Constants.referredBy,Constants.Static_SessionID,otpReq).enqueue(new Callback<OTPVerification>() {
                @Override
                public void onResponse(Call<OTPVerification> call, Response<OTPVerification> response) {
                    try {
                        if (response.code()==202){
                            Log.e("statusstatus",response.body().toString());
                            Util_functions.ShowAlert(getContext(), getString(R.string.txt_pass_changed),getString(R.string.txt_back_to_login), (OnDialogButtonClickListener) forgot_screen_three.this);
                            new_conf=true;
                           ProgressDialog.dismissProgress();
                        }  else if(response.code()==412){
                            try {
                                String Error=response.errorBody().string();
                                JSONObject obj = new JSONObject(Error);
                                JSONArray jsonArray1 = obj.getJSONArray("messages");
                                for(int i=0;i<jsonArray1.length();i++){
                                    JSONObject object1 = jsonArray1.getJSONObject(0);
                                    Util_functions.ShowAlertError(getActivity(),object1.getString("message"),"OK",forgot_screen_three.this);
                                }
                                //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.resp_empty),true);
                                ProgressDialog.dismissProgress();
                            } catch (Exception e){
                                e.printStackTrace();
                                ProgressDialog.dismissProgress();

                            }

                        }
                        else {
                            String Error=response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            String text=obj.getString("message");
                            Util_functions.ShowValidateAlertError(getContext(),text,"OK");
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
                    Util_functions.ShowAlertError(getContext(),t.getMessage(),"OK",forgot_screen_three.this);
                    ProgressDialog.dismissProgress();

                }
            });

    }



    @Override
    public void onPositiveButtonClicked(AlertDialog alertDialog) {
        //getActivity().finish();
        if (new_conf==true){
            getActivity().finish();
        }
        else
        {

        }
    }

    @Override
    public void onNegativeButtonClicked() {

    }
}