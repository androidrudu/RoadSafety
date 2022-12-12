package com.montbleu.roadsafety.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import com.montbleu.roadsafety.R;
import com.montbleu.roadsafety.fragment.forgot_screen_one;

public class ForgotPassword extends AppCompatActivity {
    public static final String FRAGMENT_TAG = "Forgot Screen One";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_password);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setFragment(new forgot_screen_one(), FRAGMENT_TAG);
    }

    public void setFragment(Fragment fragment, String fragmentTag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment, fragmentTag);
        fragmentTransaction.commit();
    }

}