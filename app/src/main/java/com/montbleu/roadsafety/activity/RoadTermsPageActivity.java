package com.montbleu.roadsafety.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.montbleu.Retrofit.RestClient;
import com.montbleu.Retrofit.RestMethods;
import com.montbleu.Utils.Constants;
import com.montbleu.Utils.PreferenceManager;
import com.montbleu.Utils.Preference_Details;
import com.montbleu.Utils.ProgressDialog;
import com.montbleu.Utils.Util_functions;
import com.montbleu.interfaces.OnDialogButtonClickListener;
import com.montbleu.interfaces.Preference_Keys;
import com.montbleu.model.CompanyPrefResponse;
import com.montbleu.roadsafety.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoadTermsPageActivity extends AppCompatActivity implements View.OnClickListener, OnDialogButtonClickListener {

    TextView term_condition;
    RestMethods restMethods;
    ArrayList<CompanyPrefResponse> companyPrefResponses = new ArrayList<>();
    ImageView backButton;
    Boolean mstatusRedirect = false;
    Boolean mconnectStatus = false;
    private PreferenceManager mPrefernce_Manager=PreferenceManager.getInstance();
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_road_terms_page);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        backButton = findViewById(R.id.back_profile_btn);
        backButton.setOnClickListener(this);
        term_condition = findViewById(R.id.termsDEsc);
        ProgressDialog.setLottieProgressDialog(RoadTermsPageActivity.this,Constants.lottiePath);
       ProgressDialog.showProgress();
        if (Util_functions.ConnectivityCheck(RoadTermsPageActivity.this,RoadTermsPageActivity.this).booleanValue()) {
            mconnectStatus = true;
            getTerms();
        }

    }

    private void getTerms() {
        String compId="";
        if (Preference_Details.getCompanyID().isEmpty()){
            compId=Constants.companyID;
        }else {
            compId=Preference_Details.getCompanyID();
        }
        restMethods = RestClient.buildHTTPClient();
        restMethods.getCompanyPreference(Constants.referredBy,Constants.Static_SessionID,compId,"TERMS_AND_CONDITIONS",0,100).enqueue(new Callback<List<CompanyPrefResponse>>() {
            @Override
            public void onResponse(Call<List<CompanyPrefResponse>> call, Response<List<CompanyPrefResponse>> response) {
                Log.e("status",""+response.body());
                try{
                    if (response.code()==200) {
                        if (response.isSuccessful()) {
                            companyPrefResponses.clear();
                            if (!response.body().isEmpty()) {
                                companyPrefResponses = (ArrayList<CompanyPrefResponse>) response.body();
                                StringBuilder termsVal = new StringBuilder();
                                for (int i = 0; i < companyPrefResponses.size(); i++) {
                                    for (int j = 0; j < companyPrefResponses.get(i).getCompanyPreferenceField().getLines().size(); j++) {
                                        termsVal.append(companyPrefResponses.get(i).getCompanyPreferenceField().getLines().get(j) + "\n");
                                    }
                                }
                                term_condition.setText(termsVal);
                               ProgressDialog.dismissProgress();
                            } else if(response.code()==412){
                                try {
                                    String Error=response.errorBody().string();
                                    JSONObject obj = new JSONObject(Error);
                                    JSONArray jsonArray1 = obj.getJSONArray("messages");
                                    for(int i=0;i<jsonArray1.length();i++){
                                        JSONObject object1 = jsonArray1.getJSONObject(0);
                                        Util_functions.ShowAlertError(RoadTermsPageActivity.this,object1.getString("message"),"OK",RoadTermsPageActivity.this);
                                    }
                                    //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.resp_empty),true);
                                    ProgressDialog.dismissProgress();
                                } catch (Exception e){
                                    ProgressDialog.dismissProgress();
                                    e.printStackTrace();
                                }

                            } else {
                                String Error=response.errorBody().string();
                                JSONObject obj = new JSONObject(Error);
                                String text=obj.getString("message");
                                Util_functions.ShowAlertError(RoadTermsPageActivity.this,text,"OK",RoadTermsPageActivity.this);
                                //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.resp_empty),true);
                                ProgressDialog.dismissProgress();

                            }
                        } else {
                            ProgressDialog.dismissProgress();
                        }

                    } else {
                        String Error=response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text=obj.getString("message");
                        if(text.toLowerCase().contains("please login again")){
                            mstatusRedirect = true;
                        }
                        Util_functions.ShowAlertError(RoadTermsPageActivity.this,text,"OK",RoadTermsPageActivity.this);
                        //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.txt_error),true);
                        ProgressDialog.dismissProgress();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    ProgressDialog.dismissProgress();
                }
            }

            @Override
            public void onFailure(Call<List<CompanyPrefResponse>> call, Throwable t) {
                if (t.toString().contains("Unable to resolve host")){
                    Util_functions.ShowAlertError(RoadTermsPageActivity.this,getString(R.string.check_internet),"OK",RoadTermsPageActivity.this);
                }
                Util_functions.ShowAlertError(RoadTermsPageActivity.this,t.getMessage(),"OK",RoadTermsPageActivity.this);
                t.printStackTrace();
                ProgressDialog.dismissProgress();
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.back_profile_btn:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onPositiveButtonClicked(AlertDialog alertDialog) {
        if (!mconnectStatus.booleanValue()) {
            ProgressDialog.dismissProgress();
            finish();
        } else {
            if (mstatusRedirect==true) {
                mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.LoginStatus,false);
                Intent loginScreen = new Intent(getApplicationContext(), LoginPageActivity.class);
                startActivity(loginScreen);
                finish();
            } else {
                finish();
            }
        }
    }

    @Override
    public void onNegativeButtonClicked() {

    }
}