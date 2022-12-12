package com.montbleu.roadsafety;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRatingBar;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.montbleu.Retrofit.RestClient;
import com.montbleu.Retrofit.RestMethods;
import com.montbleu.Utils.Constants;
import com.montbleu.Utils.Preference_Details;
import com.montbleu.Utils.ProgressDialog;
import com.montbleu.Utils.Util_functions;
import com.montbleu.interfaces.OnDialogButtonClickListener;
import com.montbleu.model.UserFeedbackPOJO;
import com.montbleu.roadsafety.activity.LoginPageActivity;
import com.montbleu.roadsafety.activity.ReportProblemPageActivity;
import com.montbleu.roadsafety.activity.RoadRegisterPageActivity;
import com.montbleu.roadsafety.activity.VerificationCodePageActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener, OnDialogButtonClickListener {

    RatingBar ratingBar;
    AppCompatButton btnSubmit,btnCancel;
    EditText description;
    ImageView back_feed_btn;
    RestMethods restMethods;
    BeautifulProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        back_feed_btn = findViewById(R.id.back_feed_btn);
        back_feed_btn.setOnClickListener(this);
        description = findViewById(R.id.description);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
             ratingBar.setRating(rating);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_feed_btn:
                finish();
                break;
            case R.id.btnSubmit:
                progressDialog = new BeautifulProgressDialog(this, BeautifulProgressDialog.withLottie, null);
                progressDialog.setLottieLocation(Constants.lottiePath);
                progressDialog.setLottieLoop(true);
                progressDialog.setLayoutColor(Color.TRANSPARENT);
                progressDialog.setLayoutElevation(0);
                progressDialog.setCancelable(true);
                progressDialog.show();
                if (description.getText().toString().isEmpty()) {
                    Util_functions.ShowValidateAlertError(this, getString(R.string.text_desc_empty), "OK");
                    progressDialog.dismiss();
                } else {
                    if(ratingBar.getRating() == 0.0){
                        Util_functions.ShowValidateAlertError(this, getString(R.string.text_desc), "OK");
                        progressDialog.dismiss();
                    }else{
                           gotoFeedback();
                    }

                }


                break;
            case R.id.btnCancel:
                ratingBar.setRating(0f);
                description.setText("");
                break;
        }
    }

    private void gotoFeedback() {
        restMethods = RestClient.buildHTTPClient();

        UserFeedbackPOJO mod = new UserFeedbackPOJO();
        mod.setCompanyId(Preference_Details.getCompanyID());
        mod.setDivisionId(Preference_Details.getDivisionID());
        mod.setModuleId(Preference_Details.getModuleId());
        mod.setDescription(description.getText().toString());
        mod.setStatus("REGISTERED");
        mod.setType("FEEDBACK");
        mod.setUserId(Preference_Details.getUserID());
        UserFeedbackPOJO.UserFeedbackField val = new UserFeedbackPOJO.UserFeedbackField();
        val.setDescription(description.getText().toString());
        val.setRateValue(String.valueOf(ratingBar.getRating()));
        mod.setUserFeedbackField(val);


        Call<UserFeedbackPOJO> call = restMethods.postUserFeedback("ANDROID",
                Preference_Details.getLoginSessID(), mod);
        call.enqueue(new Callback<UserFeedbackPOJO>() {
            @Override
            public void onResponse(Call<UserFeedbackPOJO> call, Response<UserFeedbackPOJO> response) {

                try {
                    if (response.code() == 201) {
                        if (response.isSuccessful()) {
                            progressDialog.dismiss();
                            Util_functions.Show_Toast(getApplicationContext(),"Feedback sent successfully!",true);
                            //Util_functions.ShowAlertError(getApplicationContext(),"Feedback sent successfully!","OK",FeedbackActivity.this);
                            finish();

                        }
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<UserFeedbackPOJO> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Service Unavailable", Toast.LENGTH_LONG).show();
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
}