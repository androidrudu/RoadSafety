package com.montbleu.roadsafety.activity;


import static com.montbleu.Utils.Constants.StartScrVisibility;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.montbleu.Utils.Constants;
import com.montbleu.Utils.Preference_Details;
import com.montbleu.adapter.ScoreListAdapter;
import com.montbleu.model.ScoreListModel;
import com.montbleu.model.SettingGetGenResponse;
import com.montbleu.roadsafety.R;
import com.rahimlis.badgedtablayout.BadgedTabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    ScoreListAdapter scoreListAdapter;
    List<ScoreListModel> scoreArrayList = new ArrayList<>();
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    public BadgedTabLayout tabLayout;
    private ViewPager viewPager;
    TextView title, drivingText, rideText, dashText, moreText;
    CardView RoadStart;
    ImageView drivingView, rideView, dashView, moreView;
    ArrayList<SettingGetGenResponse> profileGetResponses = new ArrayList<>();
    CardView Drive_CardLayout, ride_CardLayout, more_cardLayout;
    ViewPagerAdapter adapter;
    TextView abt_hello;
    ImageView card_throttle1, drive_bg_layout, throttle_bg_layout, more_bg_layout;
    View more_highlight, throttle_highlight, drive_highlight;

    //CardView dash_cardLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_road_dashboard);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        StartScrVisibility = false;

        Constants.frst_name = Preference_Details.getFrstName().trim();
        initialize();

    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void initialize() {
        hideSoftKeyboard();
        scoreArrayList = new ArrayList<>();
        Drive_CardLayout = findViewById(R.id.Drive_CardLayout);
        ride_CardLayout = findViewById(R.id.ride_CardLayout);
        more_cardLayout = findViewById(R.id.more_cardLayout);
        card_throttle1 = findViewById(R.id.img_throttle);
        card_throttle1.setOnClickListener(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //more_top=(ImageView) findViewById(R.id.more_top);
        drivingText = findViewById(R.id.drivingText);
        rideText = findViewById(R.id.rideText);
        drive_bg_layout = findViewById(R.id.drive_bg_layout);
        throttle_bg_layout = findViewById(R.id.throttle_bg_layout);
        more_bg_layout = findViewById(R.id.more_bg_layout);
        more_highlight = findViewById(R.id.more_highlight);
        throttle_highlight = findViewById(R.id.throttle_highlight);
        drive_highlight = findViewById(R.id.drive_highlight);
        dashText = findViewById(R.id.dashText);
        moreText = findViewById(R.id.moreText);
        drivingView = findViewById(R.id.drivingView);
        rideView = findViewById(R.id.rideView);
        dashView = findViewById(R.id.dashView);
        moreView = findViewById(R.id.moreView);
        tabLayout = (BadgedTabLayout) findViewById(R.id.tabs);
        RoadStart = findViewById(R.id.card_throttle);
        title = (TextView) findViewById(R.id.title);

        abt_hello = findViewById(R.id.abt_hello);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        if (!TextUtils.isEmpty(Constants.frst_name)) {
            getCalender();
        }
//        ViewGroup tabs = (ViewGroup) tabLayout.getChildAt(0);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {

                    Constants.page_change_pos = position;

                    ride_CardLayout.setBackgroundResource(R.color.white);

                    more_cardLayout.setBackgroundResource(R.color.white);

                    tabLayout.getTabAt(0).select();
                    drivingText.setTypeface(null, Typeface.BOLD);
                    rideText.setTypeface(null, Typeface.NORMAL);
                    dashText.setTypeface(null, Typeface.NORMAL);
                    moreText.setTypeface(null, Typeface.NORMAL);
                    drive_bg_layout.setVisibility(View.VISIBLE);
                    drive_highlight.setVisibility(View.INVISIBLE);
                    more_bg_layout.setVisibility(View.INVISIBLE);
                    more_highlight.setVisibility(View.INVISIBLE);
                    throttle_bg_layout.setVisibility(View.INVISIBLE);
                    throttle_highlight.setVisibility(View.INVISIBLE);

                    abt_hello.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(Constants.frst_name)) {
                        getCalender();
                    }


                } else if (position == 1) {

                    Constants.page_change_pos = position;
                    Drive_CardLayout.setBackgroundResource(R.color.white);
                    more_cardLayout.setBackgroundResource(R.color.white);
                    tabLayout.getTabAt(1).select();
                    drivingText.setTypeface(null, Typeface.NORMAL);
                    rideText.setTypeface(null, Typeface.BOLD);
                    dashText.setTypeface(null, Typeface.NORMAL);
                    moreText.setTypeface(null, Typeface.NORMAL);
                    drive_bg_layout.setVisibility(View.INVISIBLE);
                    drive_highlight.setVisibility(View.INVISIBLE);
                    more_bg_layout.setVisibility(View.VISIBLE);
                    more_highlight.setVisibility(View.INVISIBLE);
                    throttle_bg_layout.setVisibility(View.INVISIBLE);
                    throttle_highlight.setVisibility(View.INVISIBLE);
                    abt_hello.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ride_CardLayout.setBackgroundResource(R.color.white);

        more_cardLayout.setBackgroundResource(R.color.white);
        drivingText.setTypeface(null, Typeface.BOLD);
        rideText.setTypeface(null, Typeface.NORMAL);
        dashText.setTypeface(null, Typeface.NORMAL);
        moreText.setTypeface(null, Typeface.NORMAL);

        drive_bg_layout.setVisibility(View.VISIBLE);
        drive_highlight.setVisibility(View.INVISIBLE);
        more_bg_layout.setVisibility(View.INVISIBLE);
        more_highlight.setVisibility(View.INVISIBLE);
        throttle_bg_layout.setVisibility(View.INVISIBLE);
        throttle_highlight.setVisibility(View.INVISIBLE);

        Drive_CardLayout.setOnClickListener(this);
        ride_CardLayout.setOnClickListener(this);

        more_cardLayout.setOnClickListener(this);
        RoadStart.setOnClickListener(this);

        abt_hello.setVisibility(View.VISIBLE);

    }

    private void setupTabIcons() {
        tabLayout.setIcon(0, R.drawable.car_speedometer);
        tabLayout.setIcon(1, R.drawable.clock);
        /*tabLayout.setIcon(2, R.drawable.dash_icon);*/
        //  tabLayout.setIcon(2, R.drawable.more);

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Dashboardv2Fragment(), "Driving Score");
        adapter.addFrag(new MorepageActvity(), "Rides");
        /*adapter.addFrag(new DashboardFragment(), "Dashboard");*/
        // adapter.addFrag(new MorePageActivity(), "More");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getCalender();

    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ride_CardLayout:

                Constants.page_change_pos = 1;
                Drive_CardLayout.setBackgroundResource(R.color.white);
                more_cardLayout.setBackgroundResource(R.color.white);
                tabLayout.getTabAt(1).select();
                drivingText.setTypeface(null, Typeface.NORMAL);
                rideText.setTypeface(null, Typeface.BOLD);
                dashText.setTypeface(null, Typeface.NORMAL);
                moreText.setTypeface(null, Typeface.NORMAL);
                drive_bg_layout.setVisibility(View.INVISIBLE);
                drive_highlight.setVisibility(View.INVISIBLE);
                more_bg_layout.setVisibility(View.VISIBLE);
                more_highlight.setVisibility(View.INVISIBLE);
                throttle_bg_layout.setVisibility(View.INVISIBLE);
                throttle_highlight.setVisibility(View.INVISIBLE);
                abt_hello.setVisibility(View.GONE);

                break;
            case R.id.Drive_CardLayout:

                Constants.page_change_pos = 0;
                ride_CardLayout.setBackgroundResource(R.color.white);
                more_cardLayout.setBackgroundResource(R.color.white);
                tabLayout.getTabAt(0).select();
                drivingText.setTypeface(null, Typeface.BOLD);
                rideText.setTypeface(null, Typeface.NORMAL);
                dashText.setTypeface(null, Typeface.NORMAL);
                moreText.setTypeface(null, Typeface.NORMAL);
                drive_bg_layout.setVisibility(View.VISIBLE);
                drive_highlight.setVisibility(View.INVISIBLE);
                more_bg_layout.setVisibility(View.INVISIBLE);
                more_highlight.setVisibility(View.INVISIBLE);
                throttle_bg_layout.setVisibility(View.INVISIBLE);
                throttle_highlight.setVisibility(View.INVISIBLE);

                abt_hello.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(Constants.frst_name)) {
                    getCalender();
                }
                break;
            case R.id.more_cardLayout:

                Drive_CardLayout.setBackgroundResource(R.color.white);
                ride_CardLayout.setBackgroundResource(R.color.white);
                tabLayout.getTabAt(2).select();
                drivingText.setTypeface(null, Typeface.NORMAL);
                rideText.setTypeface(null, Typeface.NORMAL);
                dashText.setTypeface(null, Typeface.NORMAL);
                moreText.setTypeface(null, Typeface.BOLD);
                abt_hello.setVisibility(View.GONE);

                break;

            case R.id.img_throttle:

                Intent myIntent = new Intent(DashboardActivity.this, RoadStartPageActivity.class);
                startActivity(myIntent);
                break;

        }


    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    public void getCalender() {
        Calendar c = Calendar.getInstance();
        String firstLetStr = Constants.frst_name.substring(0, 1);
        firstLetStr = firstLetStr.toUpperCase();
        String remLetStr = Constants.frst_name.substring(1);
        String name = firstLetStr + remLetStr;
        String[] str = name.split(" ");
        Constants.frst_name = str[0];
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if (timeOfDay >= 0 && timeOfDay < 12) {
            abt_hello.setText("Hello" + " " + name + "," + " " + "Good Morning!");
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            abt_hello.setText("Hello" + " " + name + "," + " " + "Good Afternoon!");
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            abt_hello.setText("Hello" + " " + name + "," + " " + "Good Evening!");
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            abt_hello.setText("Hello" + " " + name + "," + " " + "Good Night!");
        }

    }

}
