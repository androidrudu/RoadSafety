package com.montbleu.roadsafety.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.montbleu.Retrofit.RestClient;
import com.montbleu.Retrofit.RestMethods;
import com.montbleu.Utils.Preference_Details;
import com.montbleu.model.FAQPOJO;
import com.montbleu.roadsafety.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FaqActivity extends AppCompatActivity {

    RestMethods restMethods;
    ImageView back_report_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        restMethods = RestClient.buildHTTPClient();
        back_report_btn = findViewById(R.id.back_profile_btn);
        back_report_btn.setOnClickListener(view -> finish());

        gotoFAQ();

    }

    private void gotoFAQ() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("companyId", Preference_Details.getCompanyID());
        parameters.put("userId", Preference_Details.getUserID());
        parameters.put("type", "FAQ");
        parameters.put("sortBy", "createdAt");
        parameters.put("sortOrder", "ASC");
        parameters.put("offset", "0");
        parameters.put("limit", "100");

        Call<List<FAQPOJO>> call = restMethods.getFAQ("ANDROID",
                "202120212021202120212021", parameters);
        call.enqueue(new Callback<List<FAQPOJO>>() {
            @Override
            public void onResponse(@NonNull Call<List<FAQPOJO>> call, @NonNull Response<List<FAQPOJO>> response) {
                ArrayList<String> name = new ArrayList<>();
                ArrayList<List<Object>> lines = new ArrayList<>();
                if (response.isSuccessful()) {
                    List<FAQPOJO> resbody = response.body();

                    if(resbody!=null) {
                        for (int i = 0; i < resbody.size(); i++) {
                            name.add(resbody.get(i).getName());
                            lines.add(resbody.get(i).getCompanyPreferenceField().getLines());
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<FAQPOJO>> call, @NonNull Throwable t) {

                Toast.makeText(getApplicationContext(), "Service Unavailable", Toast.LENGTH_LONG).show();
            }
        });
    }
}