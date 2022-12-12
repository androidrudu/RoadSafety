package com.montbleu.roadsafety.fragment;

import static org.chromium.base.ContextUtils.getApplicationContext;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.montbleu.Retrofit.RestMethods;
import com.montbleu.Utils.Constants;
import com.montbleu.Utils.Util_functions;
import com.montbleu.interfaces.OnDialogButtonClickListener;
import com.montbleu.model.VerifyGetResponse;
import com.montbleu.roadsafety.BeautifulProgressDialog;
import com.montbleu.roadsafety.R;
import com.montbleu.roadsafety.activity.ForgotPassword;
import com.montbleu.roadsafety.activity.LoginPageActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link forgot_screen_one#newInstance} factory method to
 * create an instance of this fragment.
 */
public class forgot_screen_one extends Fragment implements View.OnClickListener, OnDialogButtonClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static final String FRAGMENT_TAG = "Forgot Screen One";
    Boolean mob_status = false;
    TextInputEditText etForgotMob;
    androidx.appcompat.widget.AppCompatButton btnSendOTP;
    RestMethods restMethods;
    ArrayList<VerifyGetResponse> verifyGetResponses = new ArrayList<>();
    BeautifulProgressDialog progressDialog;
    TextInputLayout layout_etForgotMob;
    TextView txt_login;


    public forgot_screen_one() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment forgot_screen_one.
     */
    // TODO: Rename and change types and number of parameters
    public forgot_screen_one newInstance(String param1, String param2) {
        forgot_screen_one fragment = new forgot_screen_one();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

/*
        String Item =getArguments().getInt("friendsID",etForgotMob);
        args.putInt("mob_no", Integer.parseInt(Item));
        forgot_screen_two mFragment_B = new forgot_screen_two();
        mFragment_B.setArguments(args);*/
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
        View view = inflater.inflate(R.layout.fragment_forgot_screen_one, container, false);
        init(view);

        if (!TextUtils.isEmpty(Constants.mob_num))
        {
            etForgotMob.setText(Constants.mob_num);
            etForgotMob.setEnabled(true);
            layout_etForgotMob.setEndIconVisible(true);
            layout_etForgotMob.setEnabled(true);
            layout_etForgotMob.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
            layout_etForgotMob.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
            layout_etForgotMob.setEndIconDrawable(R.drawable.close_image);

        }
        return view;

    }

    private void init(View view) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        etForgotMob =view.findViewById(R.id.etForgotMob);
        btnSendOTP = view.findViewById(R.id.btnSendOTP);
        btnSendOTP.setOnClickListener(this);
        layout_etForgotMob=view.findViewById(R.id.layout_etForgotMob);
        txt_login = view.findViewById(R.id.txt_login);
        txt_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.txt_LoginNow:
                startActivity(new Intent(getApplicationContext(), LoginPageActivity.class));
                getActivity().finish();
                break;
            case R.id.btnSendOTP:
                 if(doValidate()){
                   //  showProgress();
                   //  generateOTP();
                     String s=etForgotMob.getText().toString();
                     getActivity().getIntent().putExtra("mob_num",s);
                     ((ForgotPassword) getActivity()).setFragment(new forgot_screen_two(),FRAGMENT_TAG);
                 }

                break;



        }

    }

    private void showProgress() {
        progressDialog = new BeautifulProgressDialog(getActivity(), BeautifulProgressDialog.withLottie, null);
        progressDialog.setLottieLocation("lottie_1.json");
        progressDialog.setLottieLoop(true);
        progressDialog.setLayoutColor(Color.TRANSPARENT);
        progressDialog.setLayoutElevation(0);
        // progressDialog.setLayoutColor(getResources().getColor(R.color.white));
        if (progressDialog!=null) {
            progressDialog.setCancelable(true);
            progressDialog.show();
        }
    }

   /* private void generateOTP() {
        restMethods = RestClient.buildHTTPClient();
        restMethods.verifyGetResponse(Constants.referredBy,Constants.regSessionID,"8838771578","1234",Constants.verifyType,"ASC",0,100).enqueue(new Callback<List<VerifyGetResponse>>() {
            @Override
            public void onResponse(Call<List<VerifyGetResponse>> call, Response<List<VerifyGetResponse>> response) {
                Log.e("status",""+response.body());
                try{
                    if (response.code()==200) {
                        if (response.isSuccessful()) {
                            verifyGetResponses.clear();
                            if (!response.body().isEmpty()) {
                                verifyGetResponses = (ArrayList<VerifyGetResponse>) response.body();
                                StringBuilder termsVal = new StringBuilder();
                              *//*  for (int i = 0; i < verifyGetResponses.size(); i++) {
                                    etEmail.setText(verifyGetResponses.get(i).getEmail());
                                    emer_cont_number.setText(verifyGetResponses.get(i).getUserField().getEmergencyContactNumber());
                                    license_date.setText(verifyGetResponses.get(i).getUserField().getDrivingLicenseIssueDate());

                                }*//*
                                progressDialog.dismiss();
                            } else {
                                String Error=response.errorBody().string();
                                JSONObject obj = new JSONObject(Error);
                                String text=obj.getString("message");
                                Util_functions.ShowAlertError(getContext(),text,"OK", (OnDialogButtonClickListener) getContext());
                                //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.resp_empty),true);
                                progressDialog.dismiss();
                            }
                        } else {
                            progressDialog.dismiss();
                        }

                    } else {
                        String Error=response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text=obj.getString("message");
                        Util_functions.ShowAlertError(getContext(),text,"OK", (OnDialogButtonClickListener) getActivity());
                        //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.txt_error),true);
                        progressDialog.dismiss();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onFailure(Call<List<VerifyGetResponse>> call, Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
            }
        });







    }*/

    private Boolean doValidate() {
        if (Util_functions.isValidPhoneNumber(etForgotMob.getText().toString())) {
            mob_status= true;
        } else {
           Util_functions.Show_Toast(getContext(),getString(R.string.valid_phno),true);
            mob_status = false;
        }
        return mob_status;
    }


    @Override
    public void onPositiveButtonClicked(AlertDialog alertDialog) {

    }

    @Override
    public void onNegativeButtonClicked() {
    }
}