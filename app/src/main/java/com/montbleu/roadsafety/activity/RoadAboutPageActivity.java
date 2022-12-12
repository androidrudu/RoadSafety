package com.montbleu.roadsafety.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.montbleu.Retrofit.RestClient;
import com.montbleu.Retrofit.RestMethods;
import com.montbleu.Utils.Constants;
import com.montbleu.Utils.PreferenceManager;
import com.montbleu.Utils.Preference_Details;
import com.montbleu.Utils.ProgressDialog;
import com.montbleu.Utils.Util_functions;
import com.montbleu.interfaces.OnDialogButtonClickListener;
import com.montbleu.interfaces.Preference_Keys;
import com.montbleu.roadsafety.AboutUsResponse;
import com.montbleu.roadsafety.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoadAboutPageActivity extends AppCompatActivity implements View.OnClickListener, OnDialogButtonClickListener {

    TextView AboutUS;
    RestMethods restMethods;
    ArrayList<AboutUsResponse> companyPrefResponses = new ArrayList<>();
    ImageView back_abt_btn;
    Boolean mstatusRedirect = false;
    Boolean mconnectStatus = false;
    private PreferenceManager mPrefernce_Manager=PreferenceManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_road_about_page);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        AboutUS = findViewById(R.id.aboutUSDEsc);
        back_abt_btn = findViewById(R.id.back_profile_btn);
        back_abt_btn.setOnClickListener(this);
        ProgressDialog.setLottieProgressDialog(RoadAboutPageActivity.this,Constants.lottiePath);
        if (Util_functions.ConnectivityCheck(RoadAboutPageActivity.this,RoadAboutPageActivity.this).booleanValue()) {
           ProgressDialog.showProgress();
            mconnectStatus = true;
            doAboutUs();
        }

    }

    private void doAboutUs() {
        restMethods = RestClient.buildHTTPClient();
        restMethods.getAboutUs(Constants.referredBy,Constants.Static_SessionID,Preference_Details.getCompanyID(),"ABOUT_US",0,100).enqueue(new Callback<List<AboutUsResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<AboutUsResponse>> call, @NonNull Response<List<AboutUsResponse>> response) {

                Log.e("status",""+response.body());
                try{
                    if (response.code()==200) {
                        companyPrefResponses.clear();
                        if (!response.body().isEmpty()) {
                            companyPrefResponses = (ArrayList<AboutUsResponse>) response.body();
                            StringBuilder abtVal = new StringBuilder();
                            for(int i=0;i<companyPrefResponses.size();i++){
                                for(int j=0;j<companyPrefResponses.get(i).getCompanyPreferenceField().getLines().size();j++){
                                    abtVal.append(companyPrefResponses.get(i).getCompanyPreferenceField().getLines().get(j) + "\n");
                                }
                            }
                            AboutUS.setText(abtVal);
                        }

                        ProgressDialog.dismissProgress();

                    }  else if(response.code()==412){
                        try {
                            String Error=response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for(int i=0;i<jsonArray1.length();i++){
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowAlertError(RoadAboutPageActivity.this,object1.getString("message"),"OK",RoadAboutPageActivity.this);
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
                        if(text.toLowerCase().contains("please login again")){
                            mstatusRedirect = true;
                        }
                        Util_functions.ShowAlertError(RoadAboutPageActivity.this,text,"OK",RoadAboutPageActivity.this);
                        // Util_functions.Show_Toast(getApplicationContext(),getString(R.string.txt_error),true);
                        ProgressDialog.dismissProgress();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    ProgressDialog.dismissProgress();
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<AboutUsResponse>> call, @NonNull Throwable t) {
                if (t.toString().contains("Unable to resolve host")){
                    Util_functions.ShowAlertError(RoadAboutPageActivity.this,getString(R.string.check_internet),"OK",RoadAboutPageActivity.this);
                }
                Util_functions.ShowAlertError(RoadAboutPageActivity.this,t.getMessage(),"OK",RoadAboutPageActivity.this);
                ProgressDialog.dismissProgress();
                t.printStackTrace();
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
    public void onPositiveButtonClicked(AlertDialog alertDialog) {
        if (!mconnectStatus.booleanValue()) {
            ProgressDialog.dismissProgress();
            finish();
        }else {
            if (mstatusRedirect) {
                mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.LoginStatus,false);
                Intent loginScreen = new Intent(getApplicationContext(),LoginPageActivity.class);
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