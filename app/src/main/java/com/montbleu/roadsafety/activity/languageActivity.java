package com.montbleu.roadsafety.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.montbleu.roadsafety.R;

public class languageActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView back_lang_btn;
    String[] arrayForSpinner = {"English", "Hindi"};
    ArrayAdapter adapter;
    Spinner spinLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        back_lang_btn = findViewById(R.id.back_profile_btn);
        spinLang = findViewById(R.id.spinlanguage);
        back_lang_btn.setOnClickListener(this);
        adapter = new ArrayAdapter<String>(this, R.layout.listitems_layout, arrayForSpinner);
        spinLang.setAdapter(adapter);
        spinLang.setSelection(1);
        spinLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String lang = arrayForSpinner[i];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_profile_btn:
                finish();
                break;
        }
    }
}