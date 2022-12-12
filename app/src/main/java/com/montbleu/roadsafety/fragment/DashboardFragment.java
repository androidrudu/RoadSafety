package com.montbleu.roadsafety.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.anastr.speedviewlib.Speedometer;
import com.github.anastr.speedviewlib.components.Section;
import com.github.anastr.speedviewlib.components.indicators.Indicator;
import com.montbleu.Retrofit.RestClient;
import com.montbleu.Retrofit.RestMethods;
import com.montbleu.Utils.Constants;
import com.montbleu.Utils.PreferenceManager;
import com.montbleu.Utils.Preference_Details;
import com.montbleu.Utils.Util_functions;
import com.montbleu.adapter.ScoreListAdapter;
import com.montbleu.interfaces.OnDialogButtonClickListener;
import com.montbleu.interfaces.Preference_Keys;
import com.montbleu.model.GPSGETDeviceResp;
import com.montbleu.model.GetPortionResponse;
import com.montbleu.model.ScoreListModel;
import com.montbleu.model.SettingGetGenResponse;
import com.montbleu.roadsafety.BeautifulProgressDialog;
import com.montbleu.roadsafety.R;
import com.montbleu.roadsafety.activity.GlobalDashboardActivity;
import com.montbleu.roadsafety.activity.ProfileScreenActivity;
import com.montbleu.roadsafety.activity.RoadStartPageActivity;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import eu.amirs.JSON;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener,OnDialogButtonClickListener,MediaPlayer.OnCompletionListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters

    private String mParam1;
    private String mParam2;

    private Unbinder viewBinder;
    private Activity mContext;

    RecyclerView recyclerViewDashboard;

    @BindView(R.id.speedometer)
    View drivingView;

    @BindView(R.id.ricks)
    View ridesView;

    @BindView(R.id.dashboard)
    View dashboardView;

    @BindView(R.id.more)
    View moreView;

    @BindView(R.id.chauffer)
    View roadView;
    TextView abt_skill_value;
    TextView abt_antic_value;
    TextView abt_self_conf_val;
    TextView driveScore;
    TextView no_ride,txt_trophy_dash;
    ImageView filter,more_top,card_throttle1;
    ImageView profile,img_trophy_dash;
    ScoreListAdapter scoreListAdapter;
    public static ArrayList<ScoreListModel> scoreArrayList = new ArrayList<>();
    public static ArrayList<ScoreListModel> scoreLocalList = new ArrayList<>();
    int hours=0;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    RestMethods restMethods;
    ArrayList<SettingGetGenResponse> profileGetResponses = new ArrayList<>();
    //TextView abt_hello;
    // HalfGauge speedometer;
    Speedometer speedView;
    int counter = 0;
    TextView tv_content;
    ImageView circle1_yellow,circle2_yellow;
    CardView global_dashboard;
    PreferenceManager mPrefernce_Manager=PreferenceManager.getInstance();
    int stringIdList[] = { R.string.tv_driving_tip,R.string.drivingtip_two, R.string.drivingtip_three,R.string.drivingtip_four,R.string.drivingtip_five,R.string.drivingtip_six};
    ArrayList<GPSGETDeviceResp> gpsgetDeviceRespArrayList = new ArrayList<>();
    BeautifulProgressDialog progressDialog;
    Boolean isloading = true;
    TextToSpeech textToSpeech;
    private boolean isVisibleToUser = false;
    private boolean isStart = false;
    static Boolean dashStatus = false;
    Handler handler;
    //Mediaplayer
    int[] tracks = new int[3];
    int currentTrack = 0;
    private MediaPlayer mediaPlayer,ring = null;
    TextView Ride_time_val,km_val,min_val;
    int date=0;
    DecimalFormat REAL_FORMATTER = new DecimalFormat("00.00");
    public DashboardFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance() {
        return new DashboardFragment();
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
    public void onDestroy() {
        super.onDestroy();

    }


    @Override
    public void onResume() {
        super.onResume();
        //getLocalData();
    }

/*    @Override
    public void onStart() {
        super.onStart();
        isStart = true;

    }

    @Override
    public void onStop() {
        super.onStop();
        isStart= false;
    }*/



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initial(view);
    }

    private void initial(View root) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        viewBinder = ButterKnife.bind(getActivity());
        Ride_time_val = root.findViewById(R.id.Ride_time_val);
        km_val = root.findViewById(R.id.km_val);
        min_val = root.findViewById(R.id.min_val);
        abt_skill_value = root.findViewById(R.id.abt_skill_value);
        abt_antic_value = root.findViewById(R.id.abt_antic_value);
        abt_self_conf_val = root.findViewById(R.id.abt_self_conf_val);
        recyclerViewDashboard = root.findViewById(R.id.score_list_recycler_View);
        global_dashboard=getActivity().findViewById(R.id.global_dashboard);
        driveScore = root.findViewById(R.id.driveScore1);
       // filter = getActivity().findViewById(R.id.filter);
        card_throttle1=getActivity().findViewById(R.id.card_throttle1);
        txt_trophy_dash=getActivity().findViewById(R.id.txt_trophy_dash);
        img_trophy_dash=getActivity().findViewById(R.id.img_trophy_dash);
        card_throttle1.setOnClickListener(this);
    //    more_top=getActivity().findViewById(R.id.more_top);
        //    more_top.setVisibility(View.VISIBLE);
        //    filter.setVisibility(View.GONE);
        more_top.getDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP );
        // abt_hello=root.findViewById(R.id.abt_hello);
        // scoreArrayList.clear();
        tv_content=root.findViewById(R.id.tv_content);
        profile = getActivity().findViewById(R.id.profile);
        circle1_yellow=root.findViewById(R.id.circle1_yellow);
        circle1_yellow.setOnClickListener(this);
        circle2_yellow=root.findViewById(R.id.circle2_yellow);
        circle2_yellow.setOnClickListener(this);
        try {
            Picasso.get()
                    .load(Constants.BASE_URL+Constants.file+"?referredBy="+Constants.referredBy+"&sessionId="+Preference_Details.getLoginSessID()+"&companyId="+Constants.companyID+"&userId="+Preference_Details.getUserID()+"&type="+Constants.type_profile)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(profile);
        }catch (Exception e){
            profile.setBackground(getActivity().getDrawable(R.drawable.profile_man));
            e.printStackTrace();
        }
        profile.setOnClickListener(this);
        more_top.setOnClickListener(this);
        no_ride=root.findViewById(R.id.no_ride);
        no_ride.setVisibility(View.GONE);

        getDuration(String.valueOf(Preference_Details.getsessTime()),String.valueOf(Util_functions.getUniTime()));
        Constants.mob_num =Preference_Details.getUserName().trim();
        Constants.regUsername = Preference_Details.getUserName().trim();
        Constants.pwd = Preference_Details.getPWD().trim();
        if (!Constants.regUsername.isEmpty()||!Constants.pwd.isEmpty()) {
            if (hours >= 8 || hours <= -8) {
                Util_functions.getMapUpdate(getActivity(),DashboardFragment.this);
                Util_functions.callLogin(getActivity(),DashboardFragment.this);
            }
        }


        //speedometer
        speedView=root.findViewById(R.id.speedometer_dash);

        speedView.getSections().clear();
        speedView.setIndicator(Indicator.Indicators.NeedleIndicator);
        speedView.setSpeedTextSize(0);
        speedView.setUnit("");
        speedView.addSections(new Section(0f,.8f,getResources().getColor(R.color.ride_red),speedView.dpTOpx(25)),
                new Section(.8f,.9f,getResources().getColor(R.color.ride_amber),speedView.dpTOpx(25)),
                new Section(.9f,1f,getResources().getColor(R.color.ride_green),speedView.dpTOpx(25)));
        speedView.setOnSectionChangeListener(new Function2<Section, Section, Unit>() {
            @Override
            public Unit invoke(Section previousSection, Section newSection) {
                if (previousSection != null) {
                    previousSection.setWidth(speedView.dpTOpx(25f));
                }
                if (newSection != null) {
                    if(Integer.parseInt(driveScore.getText().toString())<=0){
                        newSection.setWidth(speedView.dpTOpx(25f));
                    } else {
                        newSection.setWidth(speedView.dpTOpx(35f));
                    }
                }
                return Unit.INSTANCE;
            }
        });

       /* speedView.setSpeedometerWidth(20);
        speedView.setSpeedTextSize(0);
        speedView.setSpeedometerMode(Speedometer.Mode.TOP);
        speedView.setMarksNumber(0);
        speedView.setSpeedTextPosition(Gauge.Position.CENTER);
        speedView.getSections().clear();
        speedView.setUnit("");
        speedView.addSections(new Section(0f,.8f,getResources().getColor(R.color.ride_red),speedView.dpTOpx(5), Style.ROUND),
                new Section(.8f,.9f,getResources().getColor(R.color.light_amber),speedView.dpTOpx(5), Style.ROUND),
                new Section(.9f,1f,getResources().getColor(R.color.ride_green),speedView.dpTOpx(5), Style.ROUND));
        speedView.setOnSectionChangeListener(new Function2<Section, Section, Unit>() {
            @Override
            public Unit invoke(Section previousSection, Section newSection) {
                if (previousSection != null) {
                    previousSection.setWidth(speedView.dpTOpx(5f));
                }
                if (newSection != null) {
                    newSection.setWidth(speedView.dpTOpx(15f));
                }
                return Unit.INSTANCE;
            }
        });
        ImageIndicator imageIndicator = new ImageIndicator(getContext(), ContextCompat.getDrawable(getContext(), R.drawable.image_indicator));
        speedView.setIndicator(imageIndicator);*/


        RecyclerViewLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewDashboard.setLayoutManager(RecyclerViewLayoutManager);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        global_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileActivity = new Intent(getActivity(), GlobalDashboardActivity.class);
                startActivity(profileActivity);
            }
        });

        if (Preference_Details.getPortionID().equals("")||Preference_Details.getSectID().equals("")||Preference_Details.getPropertyID().equals("")){
            getPortion();
        }
        getScoreList();
        //getLocalData();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return root;
    }

    private void getPortion() {
        RestMethods restMethods = RestClient.buildHTTPClient();
        restMethods.portionGetResp(Constants.referredBy, Preference_Details.getLoginSessID(),Preference_Details.getUserID(),Constants.companyID,Constants.type_portion).enqueue(new Callback<List<GetPortionResponse>>() {
            @Override
            public void onResponse(Call<List<GetPortionResponse>> call, Response<List<GetPortionResponse>> response) {
                Log.e("status", "" + response.body());
                try {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            for (int i=0;i<response.body().size();i++){
                                mPrefernce_Manager.setString(Preference_Keys.LOginKeys.portionID,response.body().get(i).getId());
                                mPrefernce_Manager.setString(Preference_Keys.LOginKeys.propertyID,response.body().get(i).getPropertyId());
                                mPrefernce_Manager.setString(Preference_Keys.LOginKeys.sectID,response.body().get(i).getSectionId());
                            }
                        }
                    } else if (response.code() == 412) {
                        try {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowValidateAlertError(getActivity(), object1.getString("message"), "OK");
                                //ProgressDialog.dismissProgress();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        String Error = response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text = obj.getString("message");
                        Util_functions.ShowValidateAlertError(getActivity(), text, "OK");
                        //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.txt_error),true);
                        //  ProgressDialog.dismissProgress();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //  ProgressDialog.dismissProgress();
                }
            }

            @Override
            public void onFailure(Call<List<GetPortionResponse>> call, Throwable t) {
                Util_functions.ShowAlertError(getContext(),t.getMessage(),"OK",DashboardFragment.this);
                t.printStackTrace();
                //    ProgressDialog.dismissProgress();
            }
        });
    }


  /*  @Override
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
                               // getExistList();
                            }
                        }, 250);

                    } else {
                        getScoreList();
                    }
                }
            }
        }
    }
*/
    private void getScoreList() {
        if (!(no_ride.getVisibility() == View.VISIBLE)) {
            progressDialog = new BeautifulProgressDialog(getActivity(), BeautifulProgressDialog.withLottie, null);
            progressDialog.setLottieLocation(Constants.lottiePath);
            progressDialog.setLottieLoop(true);
            progressDialog.setLayoutColor(Color.TRANSPARENT);
            progressDialog.setLayoutElevation(0);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        JSON generatedJsonObject = JSON.create(
                JSON.dic(
                        "deviceDataCategory",  getString(R.string.gps_END_DATA)
                )
        );
        restMethods = RestClient.buildHTTPClient();
        restMethods.GETGPSDeviceResponse(Constants.referredBy, Preference_Details.getLoginSessID(),Constants.companyID,Preference_Details.getUserID(),Constants.type_GPS_Device_GET,"createdAt","DESC",0,10,Constants.gps_DEVICE_AND_DATA_LIST,generatedJsonObject.toString()).enqueue(new Callback<List<GPSGETDeviceResp>>() {
            @Override
            public void onResponse(Call<List<GPSGETDeviceResp>> call, Response<List<GPSGETDeviceResp>> response) {
                Log.e("status", "" + response.body());
                try {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            no_ride.setVisibility(View.GONE);
                            isloading = true;
                            if (!response.body().isEmpty()) {
                                gpsgetDeviceRespArrayList.clear();
                               scoreArrayList.clear();
                                gpsgetDeviceRespArrayList.addAll(response.body());
                                // String response = "{\"result\": [{\"id\":\"1\",\"ride\":\"1\", \"date\":\"22/03/2021\", \"time\":\"09:55 AM\", \"min\":\"01:24 Mins\", \"dist\":\"11.77 KM\"},{\"id\":\"2\",\"ride\":\"2\", \"date\":\"27/01/2021\", \"time\":\"10:06 AM\", \"min\":\"00:25 Mins\", \"dist\":\"22.38 KM\"},{\"id\":\"3\",\"ride\":\"3\", \"date\":\"02/05/2021\", \"time\":\"11:06 AM\", \"min\":\"22:44 Mins\", \"dist\":\"18.43 KM\"}]}";
                                try {
                                    int k = 0;
                                    for (int index = 0; index < gpsgetDeviceRespArrayList.size(); index++) {
                                        if (gpsgetDeviceRespArrayList.get(index).getDeviceDataList().size() > 0) {
                                            for (int j = 0; j < gpsgetDeviceRespArrayList.get(index).getDeviceDataList().size(); j++) {
                                                if (k==0 &&j == 0) {
                                                    if (gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getAnticipation()==null ||gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getAnticipation().contains("-1")||gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getAnticipation().equals("") || gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getAnticipation().equals("0.0")) {
                                                   //     abt_antic_value.setTextColor(getResources().getColor(R.color.ride_red));
                                                        abt_antic_value.setText("0");
                                                    } else {
                                                        if (Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getAnticipation()))<=50){
                                                  //          abt_antic_value.setTextColor(getResources().getColor(R.color.ride_red));
                                                            abt_antic_value.setText(String.valueOf(Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getAnticipation()))));
                                                        }
                                                        else if (Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getAnticipation()))<=80&&Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getAnticipation()))>=51){
                                                            abt_antic_value.setTextColor(getResources().getColor(R.color.ride_amber));
                                                            abt_antic_value.setText(String.valueOf(Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getAnticipation()))));
                                                        }
                                                        else if (Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getAnticipation()))>=81&&Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getAnticipation()))<=100){
                                                 //           abt_antic_value.setTextColor(getResources().getColor(R.color.ride_green));
                                                            abt_antic_value.setText(String.valueOf(Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getAnticipation()))));
                                                        }
                                                        abt_antic_value.setText(String.valueOf(Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getAnticipation()))));
                                                        //  Toast.makeText(getContext(),"",Toast.LENGTH_SHORT).show();
                                                    }
                                                    if (gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getSelfConfidence().contains("-1") || gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getSelfConfidence().equals("") || gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getSelfConfidence().equals("0.0")) {
                                                 //       abt_self_conf_val.setTextColor(getResources().getColor(R.color.ride_red));
                                                        abt_self_conf_val.setText("0");
                                                    } else {
                                                        if (Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getSelfConfidence()))<=50){
                                                //            abt_self_conf_val.setTextColor(getResources().getColor(R.color.ride_red));
                                                            abt_self_conf_val.setText(String.valueOf(Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getSelfConfidence()))));
                                                        }
                                                        else if (Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getSelfConfidence()))<=80&&Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getSelfConfidence()))>=51){
                                               //             abt_self_conf_val.setTextColor(getResources().getColor(R.color.ride_amber));
                                                            abt_self_conf_val.setText(String.valueOf(Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getSelfConfidence()))));
                                                        }
                                                        else if (Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getSelfConfidence()))>=81&&Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getSelfConfidence()))<=100){
                                              //              abt_self_conf_val.setTextColor(getResources().getColor(R.color.ride_green));
                                                            abt_self_conf_val.setText(String.valueOf(Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getSelfConfidence()))));
                                                        }
                                                        abt_self_conf_val.setText(String.valueOf(Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getSelfConfidence()))));
                                                    }
                                                    if (gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingSkill().contains("-1") || gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingSkill().equals("") || gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingSkill().equals("0.0")) {
                                              //          abt_skill_value.setTextColor(getResources().getColor(R.color.ride_red));
                                                        abt_skill_value.setText("0");
                                                    } else {
                                                        if (Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingSkill()))<=50){
                                              //              abt_skill_value.setTextColor(getResources().getColor(R.color.ride_red));
                                                            abt_skill_value.setText(String.valueOf(Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingSkill()))));
                                                        }
                                                        else if (Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingSkill()))<=80&&Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingSkill()))>=51){
                                              //              abt_skill_value.setTextColor(getResources().getColor(R.color.ride_amber));
                                                            abt_skill_value.setText(String.valueOf(Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingSkill()))));
                                                        }
                                                        else if (Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingSkill()))>=81&&Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingSkill()))<=100){
                                             //               abt_skill_value.setTextColor(getResources().getColor(R.color.ride_green));
                                                            abt_skill_value.setText(String.valueOf(Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingSkill()))));
                                                        }

                                                    }


                                                    if (!gpsgetDeviceRespArrayList.get(index).getCreatedAtToTimeZone().isEmpty()) {
                                                        getDateCurrentTimeZone(gpsgetDeviceRespArrayList.get(index).getCreatedAtToTimeZone());
                                                        Calendar c = Calendar.getInstance();
                                                        int date1 = c.get(Calendar.DATE);
                                                        if (date == date1) {

                                                        } else {

                                                        }
                                                    } else {
                                                       Ride_time_val.setText("00:00");
                                                    }


                                                    if (gpsgetDeviceRespArrayList.get(0).getDeviceDataList().get(0).getDeviceDataField().getTotalKiloMeter().equals("0.0") || gpsgetDeviceRespArrayList.get(0).getDeviceDataList().get(0).getDeviceDataField().getTotalKiloMeter().equals("") || gpsgetDeviceRespArrayList.get(0).getDeviceDataList().get(0).getDeviceDataField().getTotalKiloMeter().contains("-1.0") || gpsgetDeviceRespArrayList.get(0).getDeviceDataList().get(0).getDeviceDataField().getTotalKiloMeter().contains("-0.0")) {
                                                        km_val.setText("00.00 KM");
                                                    } else {
                                                        km_val.setText(REAL_FORMATTER.format(Double.parseDouble(gpsgetDeviceRespArrayList.get(0).getDeviceDataList().get(0).getDeviceDataField().getTotalKiloMeter())) + " " + "KM");
                                                                      //    xAxis.setValueFormatter(new LargeValueFormatter());
                                                    }


                                                    if (gpsgetDeviceRespArrayList.get(0).getDeviceDataList().get(0).getDeviceDataField().getTravelTime().equals("0.0") || gpsgetDeviceRespArrayList.get(0).getDeviceDataList().get(0).getDeviceDataField().getTravelTime().equals("") || gpsgetDeviceRespArrayList.get(0).getDeviceDataList().get(0).getDeviceDataField().getTravelTime().contains("-1.0") || gpsgetDeviceRespArrayList.get(0).getDeviceDataList().get(0).getDeviceDataField().getTravelTime().contains("-0.0")) {
                                                        min_val.setText("00:00 Mins");
                                                    } else {
                                                        min_val.setText(REAL_FORMATTER.format(Double.parseDouble(gpsgetDeviceRespArrayList.get(0).getDeviceDataList().get(0).getDeviceDataField().getTravelTime())) + " " + "Mins");
                                                    }






                                                    if (gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingScore().contains("-1") || gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingScore().equals("") || gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingScore().equals("0.0")) {
                                                        speedView.setSpeedAt(0);
                                                        driveScore.setText("0");
                                                        txt_trophy_dash.setVisibility(View.INVISIBLE);
                                                        img_trophy_dash.setVisibility(View.INVISIBLE);
                                                        if (Constants.score_frag==1) {
//                                                            txt_to_speech("Score not available.Travel was too short");
                                                            ring= MediaPlayer.create(getContext(),R.raw.scorenotavailabe_travelwas_tooshort);
                                                            ring.start();
                                                            Constants.score_frag=0;
                                                        }
                                                    } else {
                                                        speedView.setSpeedAt(Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingScore())));
                                                        driveScore.setText(String.valueOf(Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingScore()))));
                                                        if (Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingScore()))>=93 && Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingScore()))<=95){
                                                            txt_trophy_dash.setVisibility(View.VISIBLE);
                                                            img_trophy_dash.setVisibility(View.VISIBLE);
                                                            txt_trophy_dash.setText("Bronze");
                                                            img_trophy_dash.setBackground(getResources().getDrawable(R.drawable.bronze_trophy_big));
                                                        }else if (Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingScore()))>=96 && Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingScore()))<=98){
                                                            txt_trophy_dash.setVisibility(View.VISIBLE);
                                                            img_trophy_dash.setVisibility(View.VISIBLE);
                                                            txt_trophy_dash.setText("Silver");
                                                            img_trophy_dash.setBackground(getResources().getDrawable(R.drawable.silver_trophy_big));
                                                        }else if (Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingScore()))>=99 && Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingScore()))<=100){
                                                            txt_trophy_dash.setVisibility(View.VISIBLE);
                                                            img_trophy_dash.setVisibility(View.VISIBLE);
                                                            txt_trophy_dash.setText("Gold");
                                                            img_trophy_dash.setBackground(getResources().getDrawable(R.drawable.gold_trophy_big));
                                                        }else {
                                                            txt_trophy_dash.setVisibility(View.INVISIBLE);
                                                            img_trophy_dash.setVisibility(View.INVISIBLE);
                                                        }

                                                        if (Constants.score_frag==1) {
                                                            if(Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingScore()))<95){
                                                                String score="score_"+Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingScore()));
                                                                tracks[0] = R.raw.your_drivingscore_is;
                                                                tracks[1] = getContext().getResources().getIdentifier(score, "raw", getContext().getPackageName());
                                                                tracks[2] = R.raw.trytofollow_safealert;
                                                                mediaPlayer = MediaPlayer.create(getContext(), tracks[currentTrack]);
                                                                mediaPlayer.setOnCompletionListener(DashboardFragment.this);
                                                                mediaPlayer.start();
                                                                //txt_to_speech("your driving score is"+String.valueOf(Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingScore())))+". try to follow better road drive it safe alerts");
                                                                Constants.score_frag=0;
                                                            }
                                                            else if(Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingScore()))>=95) {
                                                                String score="score_"+Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingScore()));
                                                                tracks[0] = R.raw.your_drivingscore_is;
                                                                tracks[1] = getContext().getResources().getIdentifier(score, "raw", getContext().getPackageName());
                                                                tracks[2] = R.raw.excellent_driving;
                                                                mediaPlayer = MediaPlayer.create(getContext(), tracks[currentTrack]);
                                                                mediaPlayer.setOnCompletionListener(DashboardFragment.this);
                                                                mediaPlayer.start();
                                                                //txt_to_speech("your driving score is"+String.valueOf(Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingScore())))+". excellent driving");
                                                                Constants.score_frag=0;
                                                            }
                                                        }
                                                    }
                                                }
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

                                                dayPercentage = gpsgetDeviceRespArrayList.get(index).getDayPercentage();

                                                String name=gpsgetDeviceRespArrayList.get(index).getDeviceName();
                                                if (k<3) {
                                                    scoreArrayList.add(new ScoreListModel(TotTime, totkms, driveScore, date_time, id, anticipate, selfConfid, driveskill,name,"1",highwayPercent,urbanPercent,ruralPercent,dayPercentage,deviceMode));
                                                    k++;
                                                }
                                            }
                                        }
                                    }
                                    scoreListAdapter = new ScoreListAdapter(scoreArrayList,getActivity());
                                    //  int spacingInPixels = 20;
                                    recyclerViewDashboard.setHasFixedSize(true);
                                    recyclerViewDashboard.setAdapter(scoreListAdapter);
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                    //filter.setVisibility(View.VISIBLE);

                                } catch(Exception e) {
                                    e.printStackTrace();
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }

                                }

                            }  else if(response.body().isEmpty()) {
                                //Util_functions.ShowValidateAlertError(getContext(),getString(R.string.json_empty), "OK");
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                no_ride.setVisibility(View.VISIBLE);
                            } else {
                                String Error = response.errorBody().string();
                                JSONObject obj = new JSONObject(Error);
                                String text = obj.getString("message");
                                Util_functions.ShowAlertError(getActivity(), text, "OK", DashboardFragment.this);
                                //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.resp_empty),true);
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            }
                        } else {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }
                    } else if (response.code() == 412) {
                        isloading = true;
                        try {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowValidateAlertError(getContext(), object1.getString("message"), "OK");
                            }
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }
                    } else {
                        isloading = true;
                        String Error = response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text = obj.getString("message");
                        Util_functions.ShowValidateAlertError(getContext(), text, "OK");
                        //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.txt_error),true);
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    isloading = true;
                    try {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                    catch (WindowManager.BadTokenException e1) {
                        e1.printStackTrace();
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GPSGETDeviceResp>> call, Throwable t) {
                isloading = true;
                t.printStackTrace();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Util_functions.ShowAlertError(getContext(),t.getMessage(),"OK",DashboardFragment.this);
            }
        });

    }

    public void getDateCurrentTimeZone(String timestamp) {
        try{
           /* SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
            String dateString = formatter.format(new Date(timestamp));*/

            SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date newDate=spf.parse(timestamp);
            spf= new SimpleDateFormat("dd MMM yyyy hh:mm:ss a");
            timestamp = spf.format(newDate);

            // String  datetimeString = DateFormat.format("dd MMM yyyy hh:mm:ss a", new Date(timestamp)).toString();
            String timeString = timestamp.substring(12,17);
            String dateString1 = timestamp.substring(0,11);
            String t2 = timestamp.substring(20,23);
            Log.d("data=e :",timeString+" "+dateString1+" "+t2);
          //  current_date.setText(dateString1);
            Ride_time_val.setText(Html.fromHtml("<b>" + timeString + "</b>")+t2.toUpperCase(Locale.getDefault()));

        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void txt_to_speech(String str) {
//speeech for text
        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    if (Preference_Details.getVoice() == true) {
                        textToSpeech.setLanguage(Locale.UK);
                        textToSpeech.setPitch(0.7f);
                        textToSpeech.setSpeechRate(1f);
                        textToSpeech.speak(str, TextToSpeech.QUEUE_FLUSH, null, null);
                    }
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.profile:
                Intent profileActivity = new Intent(getActivity(), ProfileScreenActivity.class);
                startActivity(profileActivity);
                break;
            case R.id.circle1_yellow:
                counter++;
                if (counter==stringIdList.length)counter=0;
                tv_content.setText(stringIdList[counter]);
                break;
            /*case R.id.more_top:
                Intent moreActivity = new Intent(getActivity(), MorepageActvity.class);
                startActivity(moreActivity);
                break;*/
            case R.id.circle2_yellow:
                counter++;
                if (counter==stringIdList.length)counter=0;
                tv_content.setText(stringIdList[counter]);
                break;
            case R.id.card_throttle1:
                startActivity(new Intent(getActivity(), RoadStartPageActivity.class));
                break;
        }
    }


    @Override
    public void onPositiveButtonClicked(AlertDialog alertDialog) {
        getActivity().finish();
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.release();
        if (currentTrack < tracks.length-1) {
            currentTrack++;
            mp = MediaPlayer.create(getContext(), tracks[currentTrack]);
            mp.setOnCompletionListener(this);
            mp.start();

        }
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