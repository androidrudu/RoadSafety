package com.montbleu.roadsafety.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.montbleu.Retrofit.RestClient;
import com.montbleu.Retrofit.RestMethods;
import com.montbleu.Utils.Constants;
import com.montbleu.Utils.Preference_Details;
import com.montbleu.Utils.Util_functions;
import com.montbleu.adapter.ScoreListAdapter;
import com.montbleu.interfaces.OnDialogButtonClickListener;
import com.montbleu.model.GPSGETDeviceResp;
import com.montbleu.model.ScoreListModel;
import com.montbleu.roadsafety.BeautifulProgressDialog;
import com.montbleu.roadsafety.R;
import com.rahimlis.badgedtablayout.BadgedTabLayout;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import eu.amirs.JSON;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyRidesPageActivity extends AppCompatActivity implements View.OnClickListener, OnDialogButtonClickListener {
    private Unbinder viewBinder;
    static RecyclerView recyclerViewMyRides;
    @BindView(R.id.speedometer)
    View drivingView;
    @BindView(R.id.ricks)
    View ridesView;
    @BindView(R.id.dashboard)
    View dashboardView;
    @BindView(R.id.more)
    View moreView;
    ScoreListAdapter scoreListAdapter1;
    public static ArrayList<ScoreListModel> myRidesArrayList = new ArrayList<>();
    public List<ScoreListModel> myRidesArrayListClone = new ArrayList<>();
    LinearLayoutManager RecyclerViewLayoutManager;

    //EditText searchText;
    int k = 0;
    ImageView profile;
    int hours=0;
    BadgedTabLayout tabLayout;
    ArrayList<GPSGETDeviceResp> gpsgetDeviceRespArrayList = new ArrayList<>();
    com.facebook.shimmer.ShimmerFrameLayout shimmerFrameLayout;
    Boolean rideListstat=true;
    RestMethods restMethods;
    TextView no_ride1;
    BeautifulProgressDialog progressDialog;

    static int offset = 0, limit = 20, limitLong = 30;
    Boolean isloading = true;
    private ProgressBar loadingPB;

    InputMethodManager imm;

    int ride_num=0;
    ImageView back_myRide_btn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_road_myrides_page);
        initial();
    }

    private void initial() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        viewBinder = ButterKnife.bind(MyRidesPageActivity.this);
        recyclerViewMyRides = findViewById(R.id.my_rides_recycler_View);
        shimmerFrameLayout = findViewById(R.id.shimmerFrameLayout);
        RecyclerViewLayoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerViewLayoutManager.setAutoMeasureEnabled(false);
        recyclerViewMyRides.setLayoutManager(RecyclerViewLayoutManager);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            Picasso.get()
                    .load(Constants.BASE_URL+Constants.file+"?referredBy="+Constants.referredBy+"&sessionId="+Preference_Details.getLoginSessID()+"&companyId="+Constants.companyID+"&userId="+Preference_Details.getUserID()+"&type="+Constants.type_profile)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(profile);
        }catch (Exception e){

            e.printStackTrace();
        }

        back_myRide_btn = findViewById(R.id.back_profile_btn);
        back_myRide_btn.setOnClickListener(this);
      //  searchText = findViewById(R.id.searchText);
        tabLayout = findViewById(R.id.tabs);
        no_ride1 = findViewById(R.id.no_ride1);
        no_ride1.setVisibility(View.GONE);
        loadingPB = findViewById(R.id.idPBLoading);
        loadingPB.setVisibility(View.GONE);
        //  searchText.onActionViewExpanded();
       getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getDuration(String.valueOf(Preference_Details.getsessTime()),String.valueOf(Util_functions.getUniTime()));
        Constants.mob_num =Preference_Details.getUserName().trim();
        Constants.regUsername = Preference_Details.getUserName().trim();
        Constants.pwd = Preference_Details.getPWD().trim();
        if (!Constants.regUsername.isEmpty()||!Constants.pwd.isEmpty()) {
            if (hours >= 8 || hours <= -8) {
                Util_functions.getMapUpdate(getApplicationContext(), MyRidesPageActivity.this);
                Util_functions.callLogin(getApplicationContext(),MyRidesPageActivity.this);
            }
        }
        if (rideListstat) {
            offset = 0;
            limit = 20;
            limitLong = 30;
            myRidesArrayList.clear();
            getMyRidesList(offset, limit);
            rideListstat = false;
        }
        recyclerViewMyRides.addOnScrollListener(new EndlessRecyclerOnScrollListener(RecyclerViewLayoutManager) {
            @Override
            public void onScrolledToEnd() {
             /*   if (bgStatus) {*/
                    if (!myRidesArrayList.isEmpty()) {
                        int totalItemCount = myRidesArrayList.size();
                        int lastVisibleItem = RecyclerViewLayoutManager.findLastCompletelyVisibleItemPosition();
                        int numItems = recyclerViewMyRides.getAdapter().getItemCount();
                        if (totalItemCount > 1) {
                            if (lastVisibleItem == totalItemCount - 1) {
                                if (isloading) {
                                    if (offset == 0) {
                                        offset = limit;
                                    } else {
                                        offset += limitLong;
                                    }
                                    isloading = false;
                                    loadingPB.setVisibility(View.GONE);

                                    new HTTPAsyncTask().execute("");
                                    // getMyRidesList(offset,limit);
                                }

                            } else {
                                loadingPB.setVisibility(View.GONE);

                            }
                        }
                    }
                }

        });

        /*searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
             *//*
               *//* if (scoreListAdapter1 != null) {
                    if (scoreListAdapter1.getItemCount() > 0) {*//* }
                }*//*
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable text) {
                if (text.length() == 2) {
                    text.append('/');
                } else if(text.length() == 5){
                    text.append('/');
                }
            }
        });

        searchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                    searchText.setText("");
                }
                return false;
            }
        });*/





     //   getLocalData();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }




    @Override
    public void onPositiveButtonClicked(AlertDialog alertDialog) {
        startActivity(new Intent(getApplicationContext(),MorepageActvity.class));
        finish();
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

       // public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

        // use your LayoutManager instead
        private LinearLayoutManager llm;

        public EndlessRecyclerOnScrollListener(LinearLayoutManager sglm) {
            this.llm = llm;
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (!recyclerView.canScrollVertically(1)) {
                onScrolledToEnd();
            }
        }

        public abstract void onScrolledToEnd();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    class HTTPAsyncTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            getMyRidesList(offset, limitLong);
            return null;
        }

        protected void onPostExecute(Boolean result) {
           // bgStatus = result;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }





    @Override
    public void onResume() {
        super.onResume();
        ride_num=0;
        if (rideListstat) {
            offset = 0;
            limit = 20;
            limitLong = 30;
            myRidesArrayList.clear();
            getMyRidesList(offset, limit);
            rideListstat = false;
        }

       // getLocalData();


    }


/*    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (Constants.page_change_pos==0) {
            Constants.page_change_pos=-1;
            if (isVisibleToUser) {
                if (isloading) {
                    if (scoreArrayList.size()>0) {
                        isloading = false;
                        handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getExistList();
                            }
                        }, 250);

                    } else {
                        offset = 0
                      getMyRidesList(offset,limit);
                    }
                }
            }
        }
    }*/

    private void getMyRidesList(int offset, int limit) {

        if (offset == 0) {
            loadingPB.setVisibility(View.GONE);
            if (!(no_ride1.getVisibility() == View.VISIBLE)) {
                progressDialog = new BeautifulProgressDialog(this, BeautifulProgressDialog.withLottie, null);
                progressDialog.setLottieLocation(Constants.lottiePath);
                progressDialog.setLottieLoop(true);
                progressDialog.setLayoutColor(Color.TRANSPARENT);
                progressDialog.setLayoutElevation(0);
                progressDialog.setCancelable(true);
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                    shimmerFrameLayout.startShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);
                }
            }

        }
        JSON generatedJsonObject = JSON.create(
                JSON.dic(
                        "deviceDataCategory",  getString(R.string.gps_END_DATA)
                )
        );
        restMethods = RestClient.buildHTTPClient();
        restMethods.GETGPSDeviceResponse(Constants.referredBy, Preference_Details.getLoginSessID(),Constants.companyID,Preference_Details.getUserID(),Constants.type_GPS_Device_GET,"createdAt","DESC",offset,limit,Constants.gps_DEVICE_AND_DATA_LIST,generatedJsonObject.toString()).enqueue(new Callback<List<GPSGETDeviceResp>>() {
            @Override
            public void onResponse(@NonNull Call<List<GPSGETDeviceResp>> call, @NonNull Response<List<GPSGETDeviceResp>> response) {
                Log.e("status", "" + response.body());
                try {
                    if (response.code() == 200) {
                        Constants.no_ride_vis = false;
                        if (response.isSuccessful()) {
                            no_ride1.setVisibility(View.GONE);
                            loadingPB.setVisibility(View.GONE);
                            gpsgetDeviceRespArrayList.clear();
                            if (!response.body().isEmpty()) {
                                try {
                                    isloading = true;
                                    gpsgetDeviceRespArrayList.addAll(response.body());
                                    Log.e("gpsgetDeviceRespArrayList", String.valueOf(gpsgetDeviceRespArrayList.size()) + " " + offset + " " + limit);
                                    if (!myRidesArrayList.isEmpty()) {
                                        for (int index = 0; index < gpsgetDeviceRespArrayList.size(); index++) {
                                            if (gpsgetDeviceRespArrayList.size() != limitLong) {
                                                isloading = false;
                                            }
                                            if (gpsgetDeviceRespArrayList.get(index).getDeviceDataList().size() > 0) {
                                                for (int j = 0; j < gpsgetDeviceRespArrayList.get(index).getDeviceDataList().size(); j++) {
                                                    String TotTime = gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getTravelTime();
                                                    String totkms = gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getTotalKiloMeter();
                                                    String driveScore = gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingScore();
                                                    String date_time = gpsgetDeviceRespArrayList.get(index).getCreatedAtToTimeZone();
                                                    String id = gpsgetDeviceRespArrayList.get(index).getDeviceId();
                                                    String anticipate = gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getAnticipation();
                                                    String selfConfid = gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getSelfConfidence();
                                                    String driveskill = gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingSkill();
                                                    String deviceMode = gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDeviceMode();

                                                    String highwayPercent,urbanPercent,ruralPercent,dayPercentage;

                                                    if (gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getHighwayPercent()==null){
                                                        highwayPercent = "0";
                                                    }else {
                                                        highwayPercent = gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getHighwayPercent();
                                                    }
                                                    if (gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getUrbanPercent()==null){
                                                        urbanPercent = "0";
                                                    }else {
                                                        urbanPercent = gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getUrbanPercent();
                                                    }
                                                    if (gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getRuralPercent()==null){
                                                        ruralPercent = "0";
                                                    }else {
                                                        ruralPercent = gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getRuralPercent();
                                                    }
                                                    String name=gpsgetDeviceRespArrayList.get(index).getDeviceName();
                                                    dayPercentage = gpsgetDeviceRespArrayList.get(index).getDayPercentage();
                                                    if (k<100){
                                                        myRidesArrayList.add(new ScoreListModel(TotTime, totkms, driveScore, date_time, id, anticipate, selfConfid, driveskill, name,"0",highwayPercent,urbanPercent,ruralPercent,dayPercentage,deviceMode));
                                                        k++;
                                                    }else {
                                                        isloading=false;
                                                    }

                                                }
                                            }
                                        }
                                        myRidesArrayListClone.clear();
                                        myRidesArrayListClone.addAll(myRidesArrayList);
                                        //scoreListAdapter1.notifyDataSetChanged();
                                        if (scoreListAdapter1!=null) {
                                            scoreListAdapter1.notifyItemRangeChanged(0, scoreListAdapter1.getItemCount());
                                        } else {
                                            scoreListAdapter1 = new ScoreListAdapter(myRidesArrayList,MyRidesPageActivity.this);
                                            recyclerViewMyRides.setHasFixedSize(true);
                                            recyclerViewMyRides.setAdapter(scoreListAdapter1);
                                        }
                                        if (progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                        }
                                    } else {
                                        for (int index = 0; index < gpsgetDeviceRespArrayList.size(); index++) {
                                            if (gpsgetDeviceRespArrayList.size() != limit) {
                                                isloading = false;
                                            }
                                            if (gpsgetDeviceRespArrayList.get(index).getDeviceDataList().size() > 0) {
                                                for (int j = 0; j < gpsgetDeviceRespArrayList.get(index).getDeviceDataList().size(); j++) {
                                                    String TotTime = gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getTravelTime();
                                                    String totkms = gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getTotalKiloMeter();
                                                    String driveScore = gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingScore();
                                                    String date_time = gpsgetDeviceRespArrayList.get(index).getCreatedAtToTimeZone();
                                                    String id = gpsgetDeviceRespArrayList.get(index).getDeviceId();
                                                    String anticipate = gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getAnticipation();
                                                    String selfConfid = gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getSelfConfidence();
                                                    String driveskill = gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingSkill();
                                                    String deviceMode = gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDeviceMode();

                                                    String highwayPercent,urbanPercent,ruralPercent,dayPercentage;
                                                    if (gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getHighwayPercent()==null){
                                                        highwayPercent = "0";
                                                    }else {
                                                        highwayPercent = gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getHighwayPercent();
                                                    }
                                                    if (gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getUrbanPercent()==null){
                                                        urbanPercent = "0";
                                                    }else {
                                                        urbanPercent = gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getUrbanPercent();
                                                    }
                                                    if (gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getRuralPercent()==null){
                                                        ruralPercent = "0";
                                                    }else {
                                                        ruralPercent = gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getRuralPercent();
                                                    }
                                                    String name=gpsgetDeviceRespArrayList.get(index).getDeviceName();
                                                    dayPercentage = gpsgetDeviceRespArrayList.get(index).getDayPercentage();

                                                    if (k<100){
                                                        myRidesArrayList.add(new ScoreListModel(TotTime, totkms, driveScore, date_time, id, anticipate, selfConfid, driveskill, name,"0",highwayPercent,urbanPercent,ruralPercent,dayPercentage,deviceMode));
                                                        k++;
                                                    }else {
                                                        isloading = false;
                                                    }
                                                }
                                            }
                                        }
                                        myRidesArrayListClone.clear();
                                        myRidesArrayListClone.addAll(myRidesArrayList);
                                        scoreListAdapter1 = new ScoreListAdapter( myRidesArrayList,MyRidesPageActivity.this);
                                        recyclerViewMyRides.setHasFixedSize(true);
                                        recyclerViewMyRides.setAdapter(scoreListAdapter1);
                                        Log.i("**", "myRidesArrayList: " + myRidesArrayList);
                                      //  bgStatus = true;
                                    }
                                    shimmerFrameLayout.setVisibility(View.GONE);
                                    shimmerFrameLayout.stopShimmerAnimation();
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }


                                } catch (Exception jsonException) {
                                    Log.e(("Warning"), jsonException.getMessage());
                                    shimmerFrameLayout.setVisibility(View.GONE);
                                    shimmerFrameLayout.stopShimmerAnimation();
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }

                                }
                                shimmerFrameLayout.setVisibility(View.GONE);
                                shimmerFrameLayout.stopShimmerAnimation();
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            } else if (response.body().isEmpty()) {
                                if (offset > 0) {
                                    no_ride1.setVisibility(View.GONE);
                                } else {
                                    no_ride1.setVisibility(View.VISIBLE);
                                //    filter.setVisibility(View.GONE);
                                    Constants.no_ride_vis = true;
                                   // loadingPB.setVisibility(View.GONE);
                                }
                                shimmerFrameLayout.setVisibility(View.GONE);
                                shimmerFrameLayout.stopShimmerAnimation();
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                // Util_functions.ShowValidateAlertError(getContext(),getString(R.string.json_empty), "OK");
                            } else {
                                String Error = response.errorBody().string();
                                JSONObject obj = new JSONObject(Error);
                                String text = obj.getString("message");
                                Util_functions.ShowValidateAlertError(getApplicationContext(), text, "OK");
                                //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.resp_empty),true);
                                shimmerFrameLayout.setVisibility(View.GONE);
                                shimmerFrameLayout.stopShimmerAnimation();
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            }
                        } else {
                            shimmerFrameLayout.setVisibility(View.GONE);
                            shimmerFrameLayout.stopShimmerAnimation();
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }

                    } else if (response.code() == 412) {
                        try {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowValidateAlertError(getApplicationContext(), object1.getString("message"), "OK");
                            }
                            shimmerFrameLayout.setVisibility(View.GONE);
                            shimmerFrameLayout.stopShimmerAnimation();
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            shimmerFrameLayout.stopShimmerAnimation();
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                        }
                    } else {
                        String Error = response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text = obj.getString("message");
                        Util_functions.ShowValidateAlertError(getApplicationContext(), text, "OK");
                        //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.txt_error),true);
                        shimmerFrameLayout.setVisibility(View.GONE);
                        shimmerFrameLayout.stopShimmerAnimation();
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        shimmerFrameLayout.setVisibility(View.GONE);
                        shimmerFrameLayout.stopShimmerAnimation();
                        if (progressDialog != null) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }

                    } catch (WindowManager.BadTokenException e1) {
                        e1.printStackTrace();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        shimmerFrameLayout.stopShimmerAnimation();
                        if (progressDialog != null) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<GPSGETDeviceResp>> call, @NonNull Throwable t) {
                if (t.toString().contains("Unable to resolve host")) {
                    Util_functions.ShowAlertError(getApplicationContext(),getString(R.string.check_internet),"OK",MyRidesPageActivity.this);
                }
                Util_functions.ShowAlertError(getApplicationContext(),t.getMessage(),"OK",MyRidesPageActivity.this);
                t.printStackTrace();
                shimmerFrameLayout.setVisibility(View.GONE);
                shimmerFrameLayout.stopShimmerAnimation();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
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
        //super.onBackPressed();
    }

    private void getDuration(String call_starttime,String call_endtime) {
        try
        {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
            call_endtime = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm:ss aa", new Date(Long.parseLong(String.valueOf(call_endtime)))).toString();
            call_starttime=android.text.format.DateFormat.format("dd/MM/yyyy hh:mm:ss aa", new Date(Long.parseLong(String.valueOf(call_starttime)))).toString();
            Date date1 = format.parse(call_starttime);
            Date date2 = format.parse(call_endtime);
            long mills = date2.getTime() - date1.getTime();
            Log.v("Data1", ""+date1.getTime());
            Log.v("Data2", ""+date2.getTime());
            hours = (int) (mills/(1000 * 60 * 60));
            int mins = (int) (mills/(1000*60)) % 60;
            int sec = (int) (mills/1000) % 60;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
