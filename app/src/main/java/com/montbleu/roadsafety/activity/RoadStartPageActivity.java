package com.montbleu.roadsafety.activity;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static com.montbleu.Utils.Constants.BASE_URL;
import static com.montbleu.Utils.Constants.DEMO_BIN_LICENSE_FILE;
import static com.montbleu.Utils.Constants.DEMO_LICENSE_FILE;
import static com.montbleu.Utils.Constants.DEMO_LICENSE_FILE_NEXYAD;
import static com.montbleu.Utils.Constants.DEMO_MAP_SUB_PATH;
import static com.montbleu.Utils.Constants.Gps_Loc;
import static com.montbleu.Utils.Constants.SUB_FOLD_NAME;
import static com.montbleu.Utils.Constants.SUB_PATH;
import static com.montbleu.Utils.Constants.StartScrVisibility;
import static com.montbleu.Utils.Constants.WSDL_URL;
import static com.montbleu.Utils.Constants.alertId;
import static com.montbleu.Utils.Constants.alert_show;
import static com.montbleu.Utils.Constants.apicount;
import static com.montbleu.Utils.Constants.autoStart;
import static com.montbleu.Utils.Constants.batteryStatus;
import static com.montbleu.Utils.Constants.gps_connectivity;
import static com.montbleu.Utils.Constants.gps_second;
import static com.montbleu.Utils.Constants.handlercount;
import static com.montbleu.Utils.Constants.mapCount;
import static com.montbleu.Utils.Constants.mapStatus;
import static com.montbleu.Utils.Constants.net_connectivity;
import static com.montbleu.Utils.Constants.net_end;
import static com.montbleu.Utils.Constants.ride_ongoing;
import static com.montbleu.Utils.Constants.ring_count;
import static com.montbleu.Utils.Constants.ring_status;
import static com.montbleu.Utils.Constants.ring_status_miss;
import static com.montbleu.Utils.Constants.runnable;
import static com.montbleu.Utils.Constants.sCount;
import static com.montbleu.Utils.Constants.scoreListModelArrayList;
import static com.montbleu.Utils.Constants.speedLimit_count;
import static com.montbleu.Utils.Constants.speedcount;
import static com.montbleu.Utils.Constants.stateId;
import static com.montbleu.Utils.Constants.stopCount;
import static com.montbleu.Utils.Constants.stopDuration;
import static com.montbleu.Utils.Constants.stopStatus;
import static com.montbleu.Utils.Constants.tripStatus;

import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import static com.montbleu.Utils.Util_functions.getAlertImageString;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beardedhen.androidbootstrap.BootstrapProgressBar;
import com.codersroute.flexiblewidgets.FlexibleSwitch;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import com.montbleu.Retrofit.FileDownloadClient;
import com.montbleu.Retrofit.RestClient;
import com.montbleu.Retrofit.RestMethods;
import com.montbleu.Service.GPSTracker;
import com.montbleu.Utils.Constants;
import com.montbleu.Utils.OpenWeatherSingleton;
import com.montbleu.Utils.PreferenceManager;
import com.montbleu.Utils.Preference_Details;
import com.montbleu.Utils.ProgressDialog;
import com.montbleu.Utils.Util_functions;
import com.montbleu.interfaces.OnDialogButtonClickListener;
import com.montbleu.interfaces.Preference_Keys;
import com.montbleu.model.GPSDeviceReq;
import com.montbleu.model.GPSDeviceResp;
import com.montbleu.model.GPSEditDeviceReq;
import com.montbleu.model.GPSEditDeviceResp;
import com.montbleu.model.GPSGETDeviceResp;
import com.montbleu.model.GetPortionResponse;
import com.montbleu.model.LicenseUpdateReq;
import com.montbleu.model.RiskAddDataModel;
import com.montbleu.model.ScoreListModel;
import com.montbleu.model.UserSettingResponse;
import com.montbleu.model.weather.WeatherModel;
import com.montbleu.roadsafety.BuildConfig;
import com.montbleu.roadsafety.R;
import com.montbleu.safetyNexDemo.CNxDemoData;
import com.montbleu.safetyNexDemo.CNxInputAPI;
import com.nexyad.jndksafetynex.CNxFullStat;
import com.nexyad.jndksafetynex.CNxInstantDriverSignature;
import com.nexyad.jndksafetynex.CNxLicenseInfo;
import com.nexyad.jndksafetynex.CNxRisk;
import com.nexyad.jndksafetynex.CNxUserStat;
import com.nexyad.jndksafetynex.JNDKSafetyNex;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RoadStartPageActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, SensorEventListener,LocationListener, OnDialogButtonClickListener, MediaPlayer.OnCompletionListener {
    private ImageView img_road_green,img_view_road_green_center,img_view_road_green_pause;
    private  ImageView img_view_road_start;
    private final int SPLASH_DISPLAY_Image = 2000;
    ImageView closeBtn;
    private View.OnClickListener mRunListener;
    private Handler mTimerHandler;
    private Runnable mTimerRunnable;
    private String mMessage;
    private boolean mIsRunning;
    double latii=0l;
    double longg=0l;
    double useLatNew=0l;
    double useLonNew=0l;
    float accuracy;
    int kk = 0;
    float dist2poi = 0f;
    File nex_cer,nex_rename;
    private String TAG = "RoadStartPageActivity";
    private JNDKSafetyNex mJniFunction;
    private CNxDemoData mData;
    public CNxInputAPI mInpuAPI;
    private int mCount;
    private CNxRisk mNxRisk;
    int risk=0,pre_risk=0;
    double ii=0d;
    Boolean gps_lost=false;
    BootstrapProgressBar speed_progress_animated_1;
    SensorManager sm = null;
    private float currentDegree = 0f;
    Sensor sensrors;
    int hours=0;
    ConstraintLayout card_color;
    SensorEventListener sel;
    List list;
    private SensorManager mSensorManager;
    float xx=0f;
    float yy=0f;
    float zz=0f;
    float gyroX=0f;
    float gyroY=0f;
    float gyroZ=0f;
    float magX=0f;
    float magY=0f;
    float magZ=0f;
    Double lat =0.0;
    Double longi =0.0;
    double speedd = 0.0;
    Float Accx=0f;
    Float Accy=0f;
    Float Accz=0f;
    float totalSnr;
    int i = 0;
    String zipcode = "";
    float Totduration = 0;
    float Totdistance = 0;
    int start_state = 0;
    // location last updated time
    private String mLastUpdateTime;
    String name;
    String alert="alert_3";
    List<Location> locations = new ArrayList<>();
    Location location;
    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    int satelliteCount  =0;
    int usedSatellites = 0;
    Double kmDistance = 0.00;
    Boolean DistanceStatuus=false;
    Double kmDuration = 0.0;
    int limitKmDistance = 0;
    Double startlimitKmDistance = 0.0;
    Boolean isCheckedStop=false;
    Boolean isCheckedStart=false;
    Boolean isCheckedStatus=false;
    boolean isChecked = true;
    int alertValue=-1;
    double current_speed = 0.0f;
    double previous_speed = 0.0f;
    double previous_time = 0.0f;
    double break_speed = 0.0f;
    Handler handlerPause;
    Boolean mapCall=true;
    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private static final int REQUEST_CHECK_SETTINGS = 100;
    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    LocationManager locationManager = null;
    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;
    AnimationDrawable RiskAnim = null;
    AnimationDrawable Anim = null;
    AnimationDrawable gearAnim = null;
    int NxAlert;
    Long NxAlertID,NxAlertTS;
    TextToSpeech textToSpeech;
    String TTS;
    GPSTracker locationTrack;
    Handler handler_ring;
    Thread thread_ring;
    int state = 0;
    int mGPScount = 0;
    int startCount=0;
    ZipFile zip_file;
    TextView text,text1;
    private GnssStatus.Callback mGnssStatusCallback ;
    String switchText,mspeedText;
    RestMethods restMethods;
    ToneGenerator beep;
    int startFirst = 1;
    float alertKm =0;
    float alertDuration = 0;
    String alertZipcode = "";
    AppCompatButton txt_view_road_start,txt_view_road_stop;
    ImageView img_view_road_start_toggle,img_view_road_green_gear;
    static int alter_status = 0;
    ImageView more_start,profile;
    Boolean mEnd_status = true,mStart_status = true,mDist_status = true,mAlert_status = true,mRegist_status = true;
    Long mLastLocationMillis =0l;
    float mTimeDiffGPSSeconds =0f;
    Handler handler_gps;
    Thread thread_gps;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    Boolean mconnectStatus = false;
    Handler handler;
    Handler batteryHandler,speedLimitHandler;
    Double distanceKm =0.0;
    String distanceZip = "";
    boolean isLicOK;
    int autoState = 0;
    Long call_starttime=0L;
    Long call_endtime=0L;
    String diff_dur="";
    String deviceid="";
    BatteryManager bm;
    int k = 0;
    int percentage=0;
    Boolean RegBoolStat = true,MapDownload=false;
    Boolean StartBoolStat = true;
    Boolean EndBoolStat = true;
    CNxLicenseInfo tempLicInfo;
    int speedValue=-1;
    private PreferenceManager mPrefernce_Manager=PreferenceManager.getInstance();
    LicenseUpdateReq licenseUpdateReq=new LicenseUpdateReq();
    Boolean for_acc = true;
    Boolean sudd_break = true;
    Boolean severe_corner = true;
    //Mediaplayer
    int[] tracks = new int[3];
    private MediaPlayer mediaPlayer,ring = null;
    int currentTrack = 0;
    Context context=this;
    private static final String TAGG = "resPMain";
    String DEMO_WORKING_PATH;
    String DEMO_WORKING_PATH_MAP;
    // Used in checking for runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    // The BroadcastReceiver used to listen from broadcasts from the service.
    private MyReceiver myReceiver;
    // A reference to the service used to get location updates.
    private GPSTracker mService = null;

    // Tracks the bound state of the service.
    private boolean mBound = false;
    private boolean cloudOneTime = true;

    // UI elements.
    private Button mRequestLocationUpdatesButton;
    private Button mRemoveLocationUpdatesButton;
    private ConstraintLayout relat_over_switch;
    FlexibleSwitch deviceMode;
    String deviceModeStatus = "WORK";
    ImageView weather_img,alert_img;
    TextView weather_deg_val,lat_val,lng_val,slr_val,speed_val,dist_val,set_speed_val,text_dist2poi;
    ArrayList<WeatherModel> weatherList = new ArrayList<>();
    public static final int REQUEST_CODE_PERMISSIONS = 103;
    Boolean mstopStatus = false;
    ProgressBar riskBarValue;
    TextView risk_val,acc_sped_val,text_eg;
    TextView current_speed_val,txt_start;
    Double pre_dist = 0.00,curr_dist = 0.00;
    //TextView curr_speed_km,curr_speed_km_2;
    CNxInstantDriverSignature mInstantDriverSignature = new CNxInstantDriverSignature();
    float [] Temp = new float[10];
    int SizeArray = 2;
    float [] ParamArray = new float[SizeArray];
    private GPSEditDeviceReq courseModalArrayList;
    private RiskAddDataModel mRiskAddData;
    ImageView img_risk_style,img_severe,img_driver_state,img_driving_style;
    TextView txt_risk_style,txt_severe,txt_driver_state,txt_driving_style;
    String JsonInput = "{\"INTENT\":{\"_value\":1}}";
    public static ArrayList<ScoreListModel> myRidesArrayList = new ArrayList<>();
    ArrayList<GPSEditDeviceResp> gpsgetDeviceRespArrayList = new ArrayList<>();

    // Monitors the state of the connection to the service.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            GPSTracker.LocalBinder binder = (GPSTracker.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        myReceiver = new MyReceiver();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());
        setContentView(R.layout.start_screen_v2);
        net_end=false;

        try {
            SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
            // creating a variable for gson.
            Gson gson = new Gson();
            // below line is to get to string present from our
            // shared prefs if not present setting it as null.
            String json = sharedPreferences.getString("ride", null);
            // below line is to get the type of our array list.
            Type type = new TypeToken<GPSEditDeviceReq>() {
            }.getType();
            // in below line we are getting data from gson
            // and saving it to our array list
            if (!json.contains("null")) {
                courseModalArrayList = gson.fromJson(json, type);
            }
            // checking below if the array list is empty or not
            if (courseModalArrayList != null) {
                // if the array list is empty
                // creating a new array list.
                //courseModalArrayList = new GPSEditDeviceReq();
                if (courseModalArrayList.getDeviceDataField() != null) {
                    net_end = true;
                    setGpsDataAPI(courseModalArrayList);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        Constants.stopStatus=false;
        context=this;
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mapStatus=false;
        File DEMO_WORKING_PATH_DIR = new File(getFilesDir().getAbsolutePath());
        File file = new File(DEMO_WORKING_PATH_DIR,"rudu");
        file.mkdir();
        DEMO_WORKING_PATH_MAP = String.valueOf(getFilesDir().getAbsolutePath());
        DEMO_WORKING_PATH = getFilesDir().getAbsolutePath() + "/rudu";
        //Toast.makeText(RoadStartPageActivity.this, String.valueOf(Build.MANUFACTURER), Toast.LENGTH_SHORT).show();
      /*  if (alarmManager == null) {
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceive.class);
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 30000,
                    pendingIntent);
        }
*/
        //  riskBarValue.setOutlineAmbientShadowColor(getColor(R.color.ride_green));
        riskBarValue=findViewById(R.id.boxed_vertical);
        speed_progress_animated_1 = findViewById(R.id.speed_progress_animated_1);
        //speed_progress_animated_1.setBootstrapBrand(DefaultBootstrapBrand.INFO);
        //riskBarValue.setProgress(Integer.valueOf(50));
        //risk_val.setText(50);

        //setAlertImageAlone(getAlertImageString(31),alert_img);

        //deviceMode.setOnTouchListener(null);
        deviceMode=findViewById(R.id.deviceMode);
        alert_img=findViewById(R.id.alert_img);
        current_speed_val=findViewById(R.id.current_speed_val);
        deviceMode.addOnStatusChangedListener(new FlexibleSwitch.OnStatusChangedListener() {
            @Override
            public void onStatusChanged(boolean b) {
                if (b) {
                    deviceModeStatus = "PERSONAL";
                    //  Log.d("status--1", deviceModeStatus);
                } else {
                    deviceModeStatus = "WORK";
                    //Log.d("status--0",deviceModeStatus);
                }
            }
        });
        relat_over_switch = findViewById(R.id.relat_over_switch);
        relat_over_switch.bringToFront();
        //batteryHandler=new Handler();
        bm = (BatteryManager)getSystemService(BATTERY_SERVICE);
        percentage = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
//        txt_to_speech("hello have a nice trip with road Drive It Safe");
        startCount=0;
        Constants.stateId=0;
        Constants.startid=0;
        Constants.autoStart=true;
        alertId = 0;
        gps_second=false;
        RiskAnim = new AnimationDrawable();
        Anim = new AnimationDrawable();
        gearAnim = new AnimationDrawable();
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        beep = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 200);
        //beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD,100);
        restMethods = RestClient.buildHTTPClient();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mIsRunning = false;
        text = findViewById(R.id.text1);
        text.setVisibility(View.INVISIBLE);
        // text1=findViewById(R.id.text4);
        /*text1=findViewById(R.id.text);*/
        risk_val=findViewById(R.id.risk_val);
        text_dist2poi=findViewById(R.id.text_dist2poi);
        img_driver_state=findViewById(R.id.img_driver_state);
        txt_driver_state=findViewById(R.id.txt_driver_state);
        img_severe=findViewById(R.id.img_severe);
        txt_severe=findViewById(R.id.txt_severe);
        img_driving_style=findViewById(R.id.img_driving_style);
        txt_driving_style=findViewById(R.id.txt_driving_style);
        img_risk_style=findViewById(R.id.img_risk_style);
        txt_risk_style=findViewById(R.id.txt_risk_style);
        txt_start=findViewById(R.id.txt_start);
        txt_view_road_start=findViewById(R.id.txt_view_road_start);
        txt_view_road_start.setClickable(true);
        txt_start.setText("START");
        txt_view_road_start.setVisibility(View.VISIBLE);
        txt_view_road_stop=findViewById(R.id.txt_view_road_stop);
        img_road_green = findViewById(R.id.img_view_road_green);
        img_view_road_green_gear=findViewById(R.id.img_view_road_green_gear);
        //img_view_road_start_toggle=findViewById(R.id.img_view_road_start_toggle);
        img_view_road_green_pause = findViewById(R.id.img_view_road_green_pause);
        img_view_road_green_center = findViewById(R.id.img_view_road_green_center);
        //img_view_road_start = findViewById(R.id.img_view_road_start);
        profile=findViewById(R.id.profile);
        more_start=findViewById(R.id.more_start);
        more_start.getDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP );
        // img_road_green.setBackgroundResource(R.mipmap.img_road_green);

        if (Constants.img_profile==true) {
            try {
                Picasso.get()
                        .load(Constants.BASE_URL + Constants.file + "?referredBy=" + Constants.referredBy + "&sessionId=" + Preference_Details.getLoginSessID() + "&companyId=" + Constants.companyID + "&userId=" + Preference_Details.getUserID() + "&type=" + Constants.type_profile)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(profile);
                Constants.img_profile=false;
                //profile_image.setImageURI(Uri.parse(Constants.BASE_URL+Constants.file+"?referredBy="+Constants.referredBy+"&sessionId="+Preference_Details.getLoginSessID()+"&companyId="+Constants.companyID+"&userId="+Preference_Details.getUserID()+"&type="+Constants.type_profile));
            } catch (Exception e) {
                profile.setBackground(getDrawable(R.drawable.profile_man));
                e.printStackTrace();
            }
        }else {
            try {
                Picasso.get()
                        .load(Constants.BASE_URL + Constants.file + "?referredBy=" + Constants.referredBy + "&sessionId=" + Preference_Details.getLoginSessID() + "&companyId=" + Constants.companyID + "&userId=" + Preference_Details.getUserID() + "&type=" + Constants.type_profile)
                        .into(profile);
                //profile_image.setImageURI(Uri.parse(Constants.BASE_URL+Constants.file+"?referredBy="+Constants.referredBy+"&sessionId="+Preference_Details.getLoginSessID()+"&companyId="+Constants.companyID+"&userId="+Preference_Details.getUserID()+"&type="+Constants.type_profile));
            } catch (Exception e) {
                profile.setBackground(getDrawable(R.drawable.profile_man));
                e.printStackTrace();
            }
        }

        //closeBtn = findViewById(R.id.closeBtn);
        //closeBtn.setOnClickListener(this);

       /* img_view_road_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(RoadStartPageActivity.this,DashboardActivity.class));
                ((ToggleButton)findViewById(R.id.toggleRun)).performClick();
            }
        });*/
        more_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoStart==true){
                    locationManager.removeUpdates(RoadStartPageActivity.this);
                    stopLocationButtonClick();
                }
                startActivity(new Intent(RoadStartPageActivity.this,DashboardActivity.class));
                StartScrVisibility = false;
                finish();
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RoadStartPageActivity.this,ProfileScreenActivity.class));
                StartScrVisibility = false;
            }
        });
        Constants.subType="FOREGROUND";
        if (Preference_Details.getBgStart() == true && Constants.BgStart == true && Constants.GPS_deviceID != ""){
            txt_view_road_start.setVisibility(View.INVISIBLE);
            txt_view_road_stop.setVisibility(View.VISIBLE);
            txt_start.setText("STOP");
            txt_view_road_stop.setClickable(true);
            current_speed_val.setVisibility(View.VISIBLE);
            autoStart=false;
            stateId=1;
            more_start.setClickable(false);
            profile.setClickable(false);
        }

        //animLoader();
        txt_view_road_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initAPI();
                //Toast.makeText(RoadStartPageActivity.this, "start text", Toast.LENGTH_SHORT).show();
                if (isLicOK == true) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txt_view_road_start.setVisibility(View.INVISIBLE);
                            txt_view_road_stop.setVisibility(View.VISIBLE);
                            txt_start.setText("STOP");
                            txt_view_road_stop.setClickable(false);
                            current_speed_val.setVisibility(View.VISIBLE);
                        }
                    });

                    //   curr_speed_km_2.setVisibility(View.VISIBLE);
                    Constants.gps_second = true;
                    //mMessage = "Data reading begins after " + Constants.DEMO_FIRST_DELAY / 1000 + " seconds";
                    // ((TextView)findViewById(R.id.textState)).setText(mMessage);
                    if (Util_functions.ConnectivityCheck(RoadStartPageActivity.this, RoadStartPageActivity.this)) {
                        mconnectStatus = true;
                        if (isCheckedStop == true && isCheckedStatus == true) {
                            if (percentage>=1){
                                // deviceMode.setClickable(false);
                                // deviceMode.setChecked(false);
                                //  deviceMode.setFocusable(false);
                                tripStatus="- Ride Started!";
                                startDrive();
                                setstartDet();
                            }else {
//                                txt_to_speech("Low Battery. Please Charge Your Mobile");
                                ring= MediaPlayer.create(RoadStartPageActivity.this,R.raw.lowbattery_pleasecharge_yourmobile);
                                ring.start();
                            }
                        } else {
                            txt_start.setText("START");
                            txt_view_road_start.setVisibility(View.VISIBLE);
                            txt_view_road_stop.setVisibility(View.INVISIBLE);
                            txt_view_road_start.setClickable(true);
                            startLocationButtonClick();
                        }
                    }
                    //((ToggleButton)findViewById(R.id.toggleRun)).performClick();
                }
                else {
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "Your license is not valid Please communicate the IMEI " + "(" + deviceid + ")" + " to Motiv AI in order to generate a license.", "ok");
                    }
                }
            }

        });
        if (Preference_Details.getPortionID().equals("")||Preference_Details.getPropertyID().equals("")||Preference_Details.getSectID().equals("")||
                Preference_Details.getPortionID().equals(Constants.porID)||Preference_Details.getPortionID().equals(Constants.propID)||Preference_Details.getPortionID().equals(Constants.secID)){
            getPortion();
        }
        txt_view_road_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceMode.setEnabled(true);
                //txt_start.setText("START");
                //txt_view_road_start.setVisibility(View.VISIBLE);
                txt_view_road_stop.setVisibility(View.VISIBLE);
                //Toast.makeText(RoadStartPageActivity.this, "Start text changed", Toast.LENGTH_SHORT).show();
                current_speed_val.setVisibility(View.INVISIBLE);
                stopCount=0;
                state=0;
                if (Util_functions.ConnectivityCheck(RoadStartPageActivity.this, RoadStartPageActivity.this)) {
                    mconnectStatus = true;
                    txt_view_road_start.setClickable(false);
                    autoStart=true;
                    tripStatus="- Ride Stopped!";
                    stopDrive();
                }
            }
        });
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //sensrors = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        Acc_position();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mTimerHandler = new Handler();
        /*mTimerHandler = new Handler();
        mTimerRunnable = new Runnable() {
            @Override
            public void run() {
                try {


                     *//*if (Preference_Details.getBgStart() == true && Constants.GPS_deviceID != "") {
                        //AsyncTask<?, ?, ?> runningTask = new LongOperation();
                        //runningTask.execute();
                        txt_view_road_start.setVisibility(View.INVISIBLE);
                        txt_view_road_stop.setVisibility(View.VISIBLE);
                        txt_start.setText("STOP");
                        txt_view_road_stop.setClickable(true);
                        current_speed_val.setVisibility(View.VISIBLE);
                        curr_speed_km.setVisibility(View.VISIBLE);
                        autoStart=false;

                }*//*

                    if (autoStart==true) {
                        setstartDet();
                        initAPI();
                        mJniFunction.SetGPSData(Double.valueOf(lat), Double.valueOf(longi), satelliteCount, Double.valueOf(currentDegree), Double.valueOf(speedd) * 3.6, mTimeDiffGPSSeconds);
                        mJniFunction.GetAccelDataWithRisk(Accx, Accy, Accz, mNxRisk);
                        long[] CurrEhorizon = mJniFunction.GetCurrEHorizon();
                        if (CurrEhorizon != null && CurrEhorizon.length > 4) {
                            autoState = mNxRisk.m_iSafetyNexEngineState;
                        } else {
                            autoState = mNxRisk.m_iSafetyNexEngineState;
                        }
                        Log.d("ENGINE STATE", String.valueOf(Double.valueOf(lat)) + String.valueOf(Double.valueOf(longi)) + String.valueOf(satelliteCount) + String.valueOf(Double.valueOf(currentDegree)) + String.valueOf(Double.valueOf(speedd) * 3.6) + String.valueOf(mTimeDiffGPSSeconds));


                            if (autoState == 2 || autoState == 0 || mNxRisk.m_bAutoStartDetection == true || Double.valueOf(speedd) * 3.6 >= 15) {
                                i = 0;
                                initAPI();
                                Constants.auto_manuel_status = "AUTO";
                                txt_view_road_start.performClick();
                                animLoader();

                        }
                    }
                    updateRisk();
                    doNextStep();
                }catch (Exception e){
                    e.printStackTrace();
                }

                kk++;

                previous_speed = current_speed;
                current_speed = speedd*3.6;

                CNxFullStat[] FullStattt = mJniFunction.GetCloudStat();
                if (FullStattt.length >= 0) {
                    for (int i = 0; i < FullStattt.length; i++) {
                        distanceKm += FullStattt[i].m_fDistance / 1000;
                        alertDuration += FullStattt[i].m_fDuration / 60;
                        if (!FullStattt[i].m_sZipCode.equals("99999")) {
                            distanceZip = FullStattt[i].m_sZipCode;
                        }
                    }
                    if (kk==1) {
                        break_speed = speedd*3.6;
                        previous_time = alertDuration;
                    }
                    //text1.setVisibility(View.VISIBLE);
                    // text1.setText(previous_speed + " " + current_speed + " " + alertDuration);
                    GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
                    gpsEditDeviceReq.setCategory(getString(R.string.gps_ALERT_DATA));
                    gpsEditDeviceReq.setCompanyId(Constants.companyID);
                    GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
                    deviceField.setAlert("");
                    deviceField.setAnticipation("");
                    deviceField.setDrivingScore("");
                    deviceField.setDrivingSkill("");
                    deviceField.setLatitude(String.valueOf(lat));
                    deviceField.setDrivingSkill("");
                    deviceField.setKiloMeter("");
                    deviceField.setLongitude(String.valueOf(longi));
                    deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                    deviceField.setSpeed(String.valueOf(speedd * 3.6));
                    deviceField.setTotalKiloMeter("");
                    deviceField.setSelfConfidence("");
                    deviceField.setTravelTime(String.valueOf(alertDuration));
                    deviceField.setGpsCount(String.valueOf(totalSnr));
                    deviceField.setAccelerX(String.valueOf(Accx));
                    deviceField.setAccelerY(String.valueOf(Accy));
                    deviceField.setAccelerZ(String.valueOf(Accz));
                    deviceField.setSenRotate(String.valueOf(currentDegree));
                    deviceField.setRideTime(String.valueOf(getUniTime()));
                    deviceField.setAlertId("");
                    deviceField.setAlertKiloMeter(String.valueOf(distanceKm));
                    deviceField.setAlertTime("");
                    deviceField.setZipCode(distanceZip);
                    deviceField.setRideDate("");
                    deviceField.setGyroscopeX(String.valueOf(gyroX));
                    deviceField.setGyroscopeY(String.valueOf(gyroY));
                    deviceField.setGyroscopeZ(String.valueOf(gyroZ));
                    deviceField.setMagnatometerX(String.valueOf(magX));
                    deviceField.setMagnatometerY(String.valueOf(magY));
                    deviceField.setMagnatometerZ(String.valueOf(magZ));
                    deviceField.setEhorizonLength(String.valueOf(FullStattt.length));
                    deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
                    deviceField.setEngineState(String.valueOf(state));
                    gpsEditDeviceReq.setDeviceDataField(deviceField);
                    gpsEditDeviceReq.setDivisionId(Constants.divisionId);
                    gpsEditDeviceReq.setModuleId(Constants.moduleID);
                    gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
                    gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
                    gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
                    gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
                    gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
                    gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
                    gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
                    Log.d("alert_log", gpsEditDeviceReq.toString());
                    if (kk <= 3 ){
                        if((break_speed - current_speed)>=12 && sudd_break == true){
                            //text.setVisibility(View.VISIBLE);
                            //text.setText("BRAKING");
                            if (Util_functions.ConnectivityCheck(getApplicationContext(), RoadStartPageActivity.this)) {
                                setAlertImageAlone(getAlertImageString(31),alert_img);
                                gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(31));
                                deviceField.setAlertValue("31");
                                deviceField.setPreviousSpeed(String.valueOf(break_speed));
                                deviceField.setPreviousTravelTime(String.valueOf(previous_time));
                                deviceField.setAlertName(Util_functions.getSubCategoryValueLowerr(31));
                                mconnectStatus = true;
                                sudd_break = false;
                                setGpsDataAPI(gpsEditDeviceReq);
                                kk = 0;
                                //Toast.makeText(RoadStartPageActivity.this, "BRAKING" + speedd * 3.6, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else {
                        sudd_break=true;
                        kk=0;
                    }
                    if (current_speed > previous_speed) {
                        k++;
                        if (k <= 10 && current_speed >= 90 && for_acc==true) {
                            //text.setVisibility(View.VISIBLE);
                            // text.setText("ACCELERATION");
                            if (Util_functions.ConnectivityCheck(getApplicationContext(), RoadStartPageActivity.this)) {
                                gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(32));
                                deviceField.setAlertValue("32");
                                deviceField.setPreviousSpeed(String.valueOf(previous_speed));
                                deviceField.setPreviousTravelTime(String.valueOf(previous_time));
                                deviceField.setAlertName(Util_functions.getSubCategoryValueLowerr(32));
                                mconnectStatus = true;
                                for_acc=false;
                                setGpsDataAPI(gpsEditDeviceReq);
                                //Toast.makeText(RoadStartPageActivity.this, "ACCELERATION"+speedd*3.6, Toast.LENGTH_SHORT).show();
                            }
                        }else if (current_speed <=70){
                            for_acc = true;
                        }
                    } else {
                        k = 0;
                    }
                    distanceKm = 0.0;
                    distanceZip = "";
                    alertDuration = 0;

                }
            }
        };*/
        mRunListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
        //  ((ToggleButton)findViewById(R.id.toggleRun)).setOnClickListener(mRunListener);
        // ((ToggleButton)findViewById(R.id.toggleRun)).setChecked(mIsRunning);
        init();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        stopLocationButtonClick();
        startLocationButtonClick();
        getBackgroundPermission();
        getGps();
        restoreValuesFromBundle(savedInstanceState);
        handler_ring = new Handler();
        thread_ring = new Thread(runnable_ring);
        thread_ring.start();
        if (!checkPermission()){
            requestPermission();
        }else {
            try {
                nex_cer=new File(DEMO_WORKING_PATH+SUB_FOLD_NAME,SUB_PATH+".crt");
                nex_rename=new File(DEMO_WORKING_PATH+SUB_FOLD_NAME,SUB_PATH);
                if (nex_cer.exists()){
                    nex_cer.renameTo(nex_rename);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            if (EndBoolStat==true) {
                EndBoolStat = false;
                File mfile;
                mfile = new File(DEMO_WORKING_PATH_MAP, DEMO_MAP_SUB_PATH);
                if (!mfile.exists()) {
                    mfile.mkdirs();
                }
                try {
                    zip_file = new ZipFile(DEMO_WORKING_PATH+"/Road_LIC_CERT1.zip");
                    zip_file.size();
                } catch (IOException e) {
                    downloadMapFile();
                    e.printStackTrace();
                }
                initAPI();
                if (isLicOK == false) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                    getLiscenseUpdate();
                } else if (tempLicInfo.m_lDaysToExpiration <= 2) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RENEWED");
                    getLiscenseUpdate();
                }
                File mfilee;
                mfilee = new File(DEMO_WORKING_PATH+SUB_FOLD_NAME, DEMO_BIN_LICENSE_FILE);
                if (!mfilee.exists()){
                    callService();
                }

                if (!mfile.exists() || mfile.list().length == 0 ) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    mfile.delete();
                    downloadMapFile();
                }else {
                    try {
                        if (!(mfile.listFiles().length>=zip_file.size()-1)){
                            MapDownload = true;
                            txt_view_road_start.setClickable(false);
                            profile.setClickable(false);
                            more_start.setClickable(false);
                            mfile.delete();
                            downloadMapFile();
                        }
                    }catch (Exception e){
                        MapDownload = true;
                        txt_view_road_start.setClickable(false);
                        profile.setClickable(false);
                        more_start.setClickable(false);
                        mfile.delete();
                        downloadMapFile();
                    }

                }
                if (!Preference_Details.getMapCode().equals(Preference_Details.getPreviousMapCode())) {
                    if (mfile.exists()){
                        if (deleteDir(mfile)) {
                            MapDownload = true;
                            txt_view_road_start.setClickable(false);
                            profile.setClickable(false);
                            more_start.setClickable(false);
                            mfile.delete();
                            downloadMapFile();
                        }
                    }

                }


            }

        }
        getDuration(String.valueOf(Preference_Details.getsessTime()),String.valueOf(Util_functions.getUniTime()));
        Constants.mob_num =Preference_Details.getUserName().trim();
        Constants.regUsername = Preference_Details.getUserName().trim();
        Constants.pwd = Preference_Details.getPWD().trim();
        if (!Constants.regUsername.isEmpty()||!Constants.pwd.isEmpty()) {
            if (hours >= 8 || hours <= -8) {
                Util_functions.getMapUpdate(RoadStartPageActivity.this,RoadStartPageActivity.this);
                Util_functions.callLogin(RoadStartPageActivity.this,RoadStartPageActivity.this);
            }
        }


        /*batteryHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                batteryHandler.postDelayed(this,150000);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    percentage = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                    if (percentage<=15){
//                        txt_to_speech("Low Battery. Please Charge Your Mobile");
                        ring= MediaPlayer.create(RoadStartPageActivity.this,R.raw.lowbattery_pleasecharge_yourmobile);
                        ring.start();

                    }else if (percentage==1){
                        if (autoStart==false){
                            txt_view_road_start.setVisibility(View.VISIBLE);
                            txt_start.setText("START");
                            txt_view_road_stop.setVisibility(View.INVISIBLE);
                            tripStatus="- Ride Stopped!";
                            stopDrive();
                        }

                    }
                }
            }
        },0000);*/
        //handler.removeCallbacksAndMessages(null);

//if (handlerStatus==false) {
        //handlerStatus=true;
        handler = new Handler();
        //handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handlercount++;
                if (autoStart == false) {
                    handlerAsync handlerAsync = new handlerAsync();
                    handlerAsync.execute();
                    if (handlercount>21) {
                        handlercount=0;
                        getUpdateHandler();
                    }
                }
                Log.e("UpdateHandler","UpdateHandler "+xx+" y "+yy+" z "+zz+" lat "+lat+" long "+longi);
                handler.postDelayed(this, 50);
                /*if (autoStart==true) {
                   handler.removeCallbacksAndMessages(null);
                }*/
            }
        }, 50);
//}
        //  weatherData(lat,longi);


    }

    private void getUpdateHandler(){
        try {
            if (autoStart==true) {
                setstartDet();
                //initAPI();
                mJniFunction.SetGPSData(Double.valueOf(lat), Double.valueOf(longi), satelliteCount, Double.valueOf(currentDegree), Double.valueOf(speedd) * 3.6, mTimeDiffGPSSeconds);
                if (Accx != 0.0) {
                    mJniFunction.GetAccelDataWithRisk(Accx, Accy, Accz, mNxRisk);
                }
                long[] CurrEhorizon = mJniFunction.GetCurrEHorizon();
                if (CurrEhorizon != null && CurrEhorizon.length > 4) {
                    autoState = mNxRisk.m_iSafetyNexEngineState;
                } else {
                    autoState = mNxRisk.m_iSafetyNexEngineState;
                }
                Log.d("ENGINE STATE", String.valueOf(Double.valueOf(lat)) + String.valueOf(Double.valueOf(longi)) + String.valueOf(satelliteCount) + String.valueOf(Double.valueOf(currentDegree)) + String.valueOf(Double.valueOf(speedd) * 3.6) + String.valueOf(mTimeDiffGPSSeconds));


                if (autoState == 1 || mNxRisk.m_bAutoStartDetection == true || Double.valueOf(speedd) * 3.6 >= 20) {
                    i = 0;
                    //initAPI();
                    Constants.auto_manuel_status = "AUTO";
                    txt_view_road_start.performClick();
                    //animLoader();
                }
            }else if(txt_view_road_start.getVisibility() == View.VISIBLE){
                txt_view_road_start.setVisibility(View.INVISIBLE);
                txt_view_road_stop.setVisibility(View.VISIBLE);
                txt_start.setText("STOP");
                tripStatus = "- Ride Started!";
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                percentage = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                if (percentage <= 15 && batteryStatus == false && percentage > 1) {
                    if (percentage % 2 != 0){
                        batteryStatus = true;
                        ring = MediaPlayer.create(RoadStartPageActivity.this, R.raw.lowbattery_pleasecharge_yourmobile);
                        ring.start();
                    }else {
                        batteryStatus = false;
                    }
//                        txt_to_speech("Low Battery. Please Charge Your Mobile");
                } else if (percentage == 1) {
                    if (autoStart == false) {
                        txt_view_road_start.setVisibility(View.VISIBLE);
                        txt_start.setText("START");
                        txt_view_road_stop.setVisibility(View.INVISIBLE);
                        tripStatus = "- Ride Stopped!";
                        stopDrive();
                    }

                }
            }
            // text_eg.setVisibility(View.VISIBLE);
            // text_eg.setText(Accx+" "+Accy+" "+Accz);
            //text_eg.setText(String.valueOf(state)+" "+String.valueOf(autoStart)+" start"+String.valueOf(txt_view_road_start.getVisibility())+" stop"+String.valueOf(txt_view_road_stop.getVisibility()));
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)==true){
                Constants.gps_connectivity="YES";
            }else {
                Constants.gps_connectivity="NO";
            }
            //updateRisk();
            //doNextStep();
        }catch (Exception e){
            e.printStackTrace();
        }

        kk++;

        previous_speed = current_speed;
        current_speed = speedd*3.6;

        CNxFullStat[] FullStattt = mJniFunction.GetCloudStat();
        if (FullStattt.length >= 0) {
            for (int i = 0; i < FullStattt.length; i++) {
                distanceKm += FullStattt[i].m_fDistance / 1000;
                alertDuration += FullStattt[i].m_fDuration / 60;
                if (!FullStattt[i].m_sZipCode.equals("99999")) {
                    distanceZip = FullStattt[i].m_sZipCode;
                }
            }
            if (kk==1) {
                break_speed = speedd*3.6;
                previous_time = alertDuration;
            }
            //text1.setVisibility(View.VISIBLE);
            // text1.setText(previous_speed + " " + current_speed + " " + alertDuration);
            GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
            gpsEditDeviceReq.setCategory(getString(R.string.gps_ALERT_DATA));
            gpsEditDeviceReq.setCompanyId(Constants.companyID);
            GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
            deviceField.setAlert("");
            deviceField.setAnticipation("");
            deviceField.setDrivingScore("");
            deviceField.setDrivingSkill("");
            deviceField.setLatitude(String.valueOf(lat));
            deviceField.setDrivingSkill("");
            deviceField.setKiloMeter("");
            deviceField.setLongitude(String.valueOf(longi));
            deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
            deviceField.setSpeed(String.valueOf(speedd * 3.6));
            deviceField.setTotalKiloMeter("");
            deviceField.setSelfConfidence("");
            deviceField.setTravelTime(String.valueOf(alertDuration));
            deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
            //deviceField.setGpsCount(String.valueOf(totalSnr));
            deviceField.setAccelerX(String.valueOf(Accx));
            deviceField.setAccelerY(String.valueOf(Accy));
            deviceField.setAccelerZ(String.valueOf(Accz));
            deviceField.setSenRotate(String.valueOf(currentDegree));
            deviceField.setRideTime(String.valueOf(getUniTime()));
            deviceField.setAlertId("");
            deviceField.setAlertKiloMeter(String.valueOf(distanceKm));
            deviceField.setAlertTime("");
            deviceField.setZipCode(distanceZip);
            deviceField.setRideDate("");
            deviceField.setGyroscopeX(String.valueOf(gyroX));
            deviceField.setGyroscopeY(String.valueOf(gyroY));
            deviceField.setGyroscopeZ(String.valueOf(gyroZ));
            deviceField.setMagnatometerX(String.valueOf(magX));
            deviceField.setMagnatometerY(String.valueOf(magY));
            deviceField.setMagnatometerZ(String.valueOf(magZ));
            deviceField.setEhorizonLength(String.valueOf(FullStattt.length));
            deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
            deviceField.setEngineState(String.valueOf(state));
            gpsEditDeviceReq.setDeviceDataField(deviceField);
            gpsEditDeviceReq.setDivisionId(Constants.divisionId);
            gpsEditDeviceReq.setModuleId(Constants.moduleID);
            gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
            gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
            gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
            gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
            gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
            gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
            gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
            GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
            deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
            deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
            deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
            deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
            deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
            gpsEditDeviceReq.setDeviceDataField1(deviceField1);
            if(Constants.subType.contains("BACKGROUND")) {
                gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
            }else {
                gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
            }
            Log.d("alert_log", gpsEditDeviceReq.toString());
            //if (kk <= 3 ){
            //if((break_speed - current_speed)>=12 && sudd_break == true){
            if(mInstantDriverSignature.m_iSevereBraking == 2 && sudd_break == true){
                //text.setVisibility(View.VISIBLE);
                //text.setText("BRAKING");
                if (Util_functions.ConnectivityCheck(getApplicationContext(), RoadStartPageActivity.this)) {
                    setAlertImageAlone(getAlertImageString(31),alert_img);
                    gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(31));
                    deviceField.setAlertValue("31");
                    deviceField.setPreviousSpeed(String.valueOf(break_speed));
                    deviceField.setPreviousTravelTime(String.valueOf(previous_time));
                    deviceField.setAlertName(Util_functions.getSubCategoryValueLowerr(31));
                    mconnectStatus = true;
                    sudd_break = false;
                    setGpsDataAPI(gpsEditDeviceReq);
                    kk = 0;
                    //Toast.makeText(RoadStartPageActivity.this, "BRAKING" + speedd * 3.6, Toast.LENGTH_SHORT).show();
                }
                //}
            }else if (mInstantDriverSignature.m_iSevereBraking != 2){
                sudd_break=true;
                kk=0;
            }
            //if (current_speed > previous_speed) {
            // k++;
            //if (k <= 10 && current_speed >= 90 && for_acc==true) {
            //text.setVisibility(View.VISIBLE);
            // text.setText("ACCELERATION");
            if (mInstantDriverSignature.m_iSevereAcceleration == 2 && for_acc==true){
                if (Util_functions.ConnectivityCheck(getApplicationContext(), RoadStartPageActivity.this)) {
                    gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(32));
                    deviceField.setAlertValue("32");
                    deviceField.setPreviousSpeed(String.valueOf(previous_speed));
                    deviceField.setPreviousTravelTime(String.valueOf(previous_time));
                    deviceField.setAlertName(Util_functions.getSubCategoryValueLowerr(32));
                    mconnectStatus = true;
                    for_acc=false;
                    setGpsDataAPI(gpsEditDeviceReq);
                    //Toast.makeText(RoadStartPageActivity.this, "ACCELERATION"+speedd*3.6, Toast.LENGTH_SHORT).show();
                }
            }//else if (current_speed <=70){
            else if(mInstantDriverSignature.m_iSevereAcceleration != 2){
                for_acc = true;
            }
            //}
            else {
                k = 0;
            }

            /*if (mInstantDriverSignature.m_iSevereCornering == 2 && severe_corner==true){
                severe_corner=false;
                if (Util_functions.ConnectivityCheck(getApplicationContext(), RoadStartPageActivity.this)) {
                    gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(33));
                    deviceField.setAlertValue("33");
                    deviceField.setPreviousSpeed(String.valueOf(previous_speed));
                    deviceField.setPreviousTravelTime(String.valueOf(previous_time));
                    deviceField.setAlertName(Util_functions.getSubCategoryValueLowerr(33));
                    mconnectStatus = true;
                    for_acc=false;
                    setGpsDataAPI(gpsEditDeviceReq);
                    //Toast.makeText(RoadStartPageActivity.this, "ACCELERATION"+speedd*3.6, Toast.LENGTH_SHORT).show();
                }

            }else {
                severe_corner=true;
            }*/

            distanceKm = 0.0;
            distanceZip = "";
            alertDuration = 0;

        }
        long [] CurrEhorizon = mJniFunction.GetCurrEHorizon();
        if (CurrEhorizon != null) {
            try {
                mMessage = getMessageCustomer(CurrEhorizon);
                mData.WriteData(mMessage);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    private void getBackgroundPermission() {
        if (!(SDK_INT >= Build.VERSION_CODES.Q)) {
            if ((ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)==false){
                //ShowAlertError(RoadSettingsPageActivity.this,"Please click\n\"Allow all the time\"","OK",RoadSettingsPageActivity.this);
            }
        }else {
            if ((ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED)==false){
                ShowAlertError(this,"Please click\n\"Allow all the time\"","OK",this);
            }
        }
    }


    public void ShowAlertError(Context context, String title, String BtnText, final OnDialogButtonClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutView = inflater.inflate(R.layout.alert_error, null);
        builder.setView(layoutView);
        alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (Constants.alertcount==true) {
            alertDialog.show();
            //   Constants.alertcount=false;
        }
        alertDialog.setCancelable(false);
        TextView alert_title = layoutView.findViewById(R.id.title_error);
        alert_title.setText(title);
        Button alert_ok = layoutView.findViewById(R.id.alert_ok);
        alert_ok.setText(BtnText);

        alert_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLocationPermission();
                alertDialog.hide();
                alertDialog.cancel();
                alertDialog.dismiss();
                //  Constants.alertcount=true;
            }
        });
    }

    private void requestLocationPermission() {

        boolean foreground = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (foreground) {
            boolean background = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;

            if (background) {
                handleLocationUpdates();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_CODE_PERMISSIONS);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION},REQUEST_CODE_PERMISSIONS);
        }
    }

    private void handleLocationUpdates() {
        //foreground and background
        Gps_Loc=true;
        mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.gpsLoc,true);
        //Toast.makeText(getApplicationContext(),"Start Foreground and Background Location Updates",Toast.LENGTH_SHORT).show();
    }


    private void setstartDet() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DecimalFormat df = new DecimalFormat("0.000");
                //lat_val.setText(String.valueOf(df.format(lat)));
                //lng_val.setText(String.valueOf(df.format(longi)));
                //slr_val.setText("0 km/h");
                if (autoStart==false) {
                    if (speedd == 0.0) {
                        current_speed_val.setText("0");
                        //dist_val.setText("0 m");
                    } else {
                        Double val = speedd * 3.6;
                        current_speed_val.setText(String.valueOf(Math.round(Float.parseFloat(String.valueOf(speedd * 3.6))) ));
                        //dist_val.setText("0 m");
                    }
                }
            }
        });
    }

    Runnable runnable_ring = new Runnable() {
        public void run() {
            handler_ring.postDelayed(this, 0);
            if (ContextCompat.checkSelfPermission(RoadStartPageActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(RoadStartPageActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE},1);
//requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
            } else {
                if (mBound == true) {
                    if (Util_functions.requestingLocationUpdates(RoadStartPageActivity.this)) {
                        if (!checkPermissions_loc()) {
                            requestPermissions();
                        } else {
                            if (mService != null) {
                                runnable=true;
                                if (Preference_Details.getStartOnbg()==true&& StartScrVisibility == true){
                                    mService.requestLocationUpdates(RoadStartPageActivity.this);
                                }
                                handler_ring.removeCallbacksAndMessages(null);
                            }

                        }
                    }
                }
            }

        }
    };

    private void registerGps() {
      /*  ProgressDialog.setLottieProgressDialog(RoadStartPageActivity.this,Constants.lottiePath);
        ProgressDialog.showProgress();*/
        GPSDeviceReq gpsDeviceReq = new GPSDeviceReq();
        gpsDeviceReq.setName("RIDE");
        gpsDeviceReq.setCompanyId(Constants.companyID);
        gpsDeviceReq.setDivisionId(Constants.divisionId);
        gpsDeviceReq.setModuleId(Constants.moduleID);
        //gpsDeviceReq.setCategory(Constants.auto_manuel_status);
        gpsDeviceReq.setCategory("");
        gpsDeviceReq.setUserId(Preference_Details.getUserID());
        /*gpsDeviceReq.setPropertyId(Constants.propID);
        gpsDeviceReq.setPortionId(Constants.porID);
        gpsDeviceReq.setSectionId(Constants.secID);*/
        GPSDeviceReq.DeviceField deviceField = new GPSDeviceReq.DeviceField();
        gpsDeviceReq.setDeviceField(deviceField);
        gpsDeviceReq.setPropertyId(Preference_Details.getPropertyID());
        gpsDeviceReq.setPortionId(Preference_Details.getPortionID());
        gpsDeviceReq.setSectionId(Preference_Details.getSectID());
        gpsDeviceReq.setStatus(getString(R.string.gps_status));
        gpsDeviceReq.setType(getString(R.string.gps_type));
        if(Constants.subType.contains("BACKGROUND")) {
            gpsDeviceReq.setSubType(Constants.subType + "_AUTO");
        }else {
            gpsDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
        }
        //gpsDeviceReq.setSubType(Constants.subType+"_"+Constants.auto_manuel_status);
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(lat,longi,1);
            if (addressList != null && addressList.size() > 0) {
                gpsDeviceReq.setLocationName(String.valueOf(addressList.get(0).getFeatureName()+","+addressList.get(0).getSubLocality()));
                deviceField.setCity(String.valueOf(addressList.get(0).getLocality()));
                deviceField.setState(String.valueOf(addressList.get(0).getAdminArea()));
                deviceField.setCountry(String.valueOf(addressList.get(0).getCountryName()));
                deviceField.setZipCode(String.valueOf(addressList.get(0).getPostalCode()));
            }
            gpsDeviceReq.setTimezoneCode(String.valueOf(TimeZone.getDefault().getID()));
            Log.d("time_ID",TimeZone.getDefault().getID());
        } catch (IOException e) {
            e.printStackTrace();
        }
        restMethods = RestClient.buildHTTPClient();
        restMethods.GPSDeviceResponse(Constants.referredBy,Preference_Details.getLoginSessID(),gpsDeviceReq).enqueue(new Callback<GPSDeviceResp>() {
            @Override
            public void onResponse(Call<GPSDeviceResp> call, Response<GPSDeviceResp> response) {
                Log.e("Register Gps",""+response.body());
                if (Constants.auto_manuel_status.contains("AUTO")) {
                    Constants.auto_manuel_status="MANUAL";
                }
                mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.BgStart,true);
                try{
                    //  ProgressDialog.dismissProgress();
                    if (response.code()==201) {
                        //  Toast.makeText(getApplicationContext(), "Register GPS", Toast.LENGTH_SHORT).show();
                        name=response.body().getName();
                        Constants.GPS_deviceID = response.body().getId();
                        more_start.setClickable(false);
                        profile.setClickable(false);
                        PreferenceManager mPrefernce_Manager=PreferenceManager.getInstance();
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.userID,response.body().getUserId());
                        Constants.startid=1;
                        if (StartBoolStat.booleanValue()) {
                            StartBoolStat = false;
                            startData();
                        }

                    } else if(response.code()==412){
                        RegBoolStat = true;
                        if (RegBoolStat.booleanValue()) {
                            registerGps();
                        }

                        try {
                            String Error=response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for(int i=0;i<jsonArray1.length();i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                if (Constants.subType.contains("FOREGROUND")) {
                                    Util_functions.ShowValidateAlertError(getApplicationContext(), object1.getString("message"), "OK");
                                }
                            }
                            txt_view_road_stop.setClickable(true);
                            //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.resp_empty),true);
                            // ProgressDialog.dismissProgress();
                        } catch (Exception e){
                            e.printStackTrace();
                            txt_view_road_stop.setClickable(true);
                            //ProgressDialog.dismissProgress();
                        }
                    } else {
                        RegBoolStat = true;
                        if (RegBoolStat.booleanValue()) {
                            registerGps();
                        }
                        ProgressDialog.dismissProgress();
                        String Error=response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text=obj.getString("message");
                        if (Constants.subType.contains("FOREGROUND")) {
                            Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, text, "OK");
                        }
                        txt_view_road_stop.setClickable(true);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    txt_view_road_stop.setClickable(true);
                    if (Constants.auto_manuel_status.contains("AUTO")) {
                        Constants.auto_manuel_status="MANUAL";
                    }
                    // ProgressDialog.dismissProgress();
                }
            }
            @Override
            public void onFailure(Call<GPSDeviceResp> call, Throwable t) {
                RegBoolStat = true;
                if (Constants.auto_manuel_status.contains("AUTO")) {
                    Constants.auto_manuel_status="MANUAL";
                }
                if (RegBoolStat.booleanValue()) {
                    registerGps();
                }
                if (t.getMessage().contains("Failed to connect")){
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, getString(R.string.check_internet), "OK");
                    }
                    txt_view_road_stop.setClickable(true);
                }else {
                    t.printStackTrace();
                    //  ProgressDialog.dismissProgress();
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, t.getMessage(), "OK");
                    }
                }

                txt_view_road_stop.setClickable(true);
            }
        });
    }

    private void startData() {
        /*Ride Start 1st Time*/
        Log.d("LOOCATIONS",String.valueOf(lat)+String.valueOf(longi));
        GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
        gpsEditDeviceReq.setCategory(getString(R.string.gps_START_DATA));
        GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
        deviceField.setDeviceMode(deviceModeStatus);
        deviceField.setAlert("");
        deviceField.setAnticipation("");
        deviceField.setDrivingScore("");
        deviceField.setDrivingSkill("");
        deviceField.setLatitude(String.valueOf(lat));
        deviceField.setDrivingSkill("");
        deviceField.setKiloMeter("");
        deviceField.setLongitude(String.valueOf(longi));
        deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
        deviceField.setSpeed(String.valueOf(speedd * 3.6));
        deviceField.setTotalKiloMeter("");
        deviceField.setSelfConfidence("");
        deviceField.setTravelTime("");
        deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
        //deviceField.setGpsCount(String.valueOf(totalSnr));
        deviceField.setAccelerX(String.valueOf(Accx));
        deviceField.setAccelerY(String.valueOf(Accy));
        deviceField.setAccelerZ(String.valueOf(Accz));
        deviceField.setSenRotate(String.valueOf(currentDegree));
        deviceField.setAlertId("");
        deviceField.setAlertValue("");
        deviceField.setAlertTime("");
        deviceField.setZipCode("");
        deviceField.setRideDate("");
        deviceField.setAlertKiloMeter(String.valueOf(Totdistance));
        deviceField.setRideTime(String.valueOf(getUniTime()));
        deviceField.setGyroscopeX(String.valueOf(gyroX));
        deviceField.setGyroscopeY(String.valueOf(gyroY));
        deviceField.setGyroscopeZ(String.valueOf(gyroZ));
        deviceField.setMagnatometerX(String.valueOf(magX));
        deviceField.setMagnatometerY(String.valueOf(magY));
        deviceField.setMagnatometerZ(String.valueOf(magZ));
        deviceField.setAlertName("");
        deviceField.setEhorizonLength("0");
        deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
        deviceField.setEngineState(String.valueOf(state));
        gpsEditDeviceReq.setDeviceDataField(deviceField);
        gpsEditDeviceReq.setCompanyId(Constants.companyID);
        gpsEditDeviceReq.setDivisionId(Constants.divisionId);
        gpsEditDeviceReq.setModuleId(Constants.moduleID);
        /*gpsEditDeviceReq.setPropertyId(Constants.propID);
        gpsEditDeviceReq.setPortionId(Constants.porID);
        gpsEditDeviceReq.setSectionId(Constants.secID);*/
        gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
        gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
        gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
        gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
        gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
        gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
        gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
        GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
        deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
        deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
        deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
        deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
        deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
        gpsEditDeviceReq.setDeviceDataField1(deviceField1);
        if(Constants.subType.contains("BACKGROUND")) {
            gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
        }else {
            gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
        }
        if (mStart_status) {
            mStart_status = false;
            if(Util_functions.ConnectivityCheck(getApplicationContext(),RoadStartPageActivity.this)) {
                mconnectStatus = true;
                setGpsDataAPI(gpsEditDeviceReq);
            }
        }
        Log.d("start_log",gpsEditDeviceReq.toString());
    }

    private void startDrive() {
        Constants.BgStart=true;
        sCount=1;
        limitKmDistance=1;
        ring = MediaPlayer.create(RoadStartPageActivity.this, R.raw.hello_nice_trip);
        ring.start();

        //txt_view_road_start.setText("STOP");
        txt_view_road_start.setClickable(false);
        //Toast.makeText(RoadStartPageActivity.this, "start drive", Toast.LENGTH_SHORT).show();
        Constants.gps_second=false;
        autoStart=false;
        img_road_green.setVisibility(View.VISIBLE);
        isCheckedStart=true;

        // startLocationButtonClick();
        //initAPI();
        if(Util_functions.ConnectivityCheck(getApplicationContext(),RoadStartPageActivity.this)) {
            mconnectStatus= true;
            if (RegBoolStat.booleanValue()) {
                RegBoolStat = false;
                registerGps();
            }
        }
        mIsRunning = true;
        /*img_view_road_start.setImageResource(R.drawable.stop_button);*/
        mTimerHandler.postDelayed(mTimerRunnable, Constants.DEMO_FIRST_DELAY);

    }

    private void LowRiskAnimLoader(){
        try {
            BitmapDrawable frame1 = (BitmapDrawable) getResources().getDrawable(
                    R.drawable.image_phone_rond_orange_oeil_a01);
            RiskAnim.addFrame(frame1, 50);
            RiskAnim.setOneShot(false);
            img_road_green.setBackgroundDrawable(RiskAnim);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (RiskAnim.isRunning()) {
                    } else {
                        RiskAnim.start();
                    }
                }
            }, 0000);

        } catch (Exception e) {
// TODO: handle exception
        }
    }


    private void RiskAnimLoader(){
        try {
            BitmapDrawable frame1 = (BitmapDrawable) getResources().getDrawable(
                    R.drawable.image_phone_rond_rouge_alerte_a01);
            BitmapDrawable frame2 = (BitmapDrawable) getResources().getDrawable(
                    R.drawable.image_phone_rond_rouge_alerte_a02);
            RiskAnim.addFrame(frame1, 50);
            RiskAnim.addFrame(frame2, 50);
            RiskAnim.setOneShot(false);
            img_road_green.setBackgroundDrawable(RiskAnim);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (RiskAnim.isRunning()) {
                    } else {
                        RiskAnim.start();
                    }
                }
            }, 0000);

        } catch (Exception e) {
// TODO: handle exception
        }
    }

    private void animLoader() {
        try {
            img_road_green.setVisibility(View.VISIBLE);
            img_view_road_green_pause.setVisibility(View.INVISIBLE);
            img_view_road_green_gear.setVisibility(View.INVISIBLE);
            BitmapDrawable frame1 = (BitmapDrawable) getResources().getDrawable(
                    R.drawable.image_phone_rond_vert_route_d01);
            BitmapDrawable frame2 = (BitmapDrawable) getResources().getDrawable(
                    R.drawable.image_phone_rond_vert_route_d02);
            BitmapDrawable frame3 = (BitmapDrawable) getResources().getDrawable(
                    R.drawable.image_phone_rond_vert_route_d03);
            BitmapDrawable frame4 = (BitmapDrawable) getResources().getDrawable(
                    R.drawable.image_phone_rond_vert_route_d04);
            BitmapDrawable frame5 = (BitmapDrawable) getResources().getDrawable(
                    R.drawable.image_phone_rond_dert_doute_d05);
            BitmapDrawable frame6 = (BitmapDrawable) getResources().getDrawable(
                    R.drawable.image_phone_rond_vert_route_d06);
            BitmapDrawable frame7 = (BitmapDrawable) getResources().getDrawable(
                    R.drawable.image_phone_rond_vert_route_d07);
            BitmapDrawable frame8 = (BitmapDrawable) getResources().getDrawable(
                    R.drawable.image_phone_rond_vert_route_d08);
            Anim.addFrame(frame1, 32);
            Anim.addFrame(frame2, 32);
            Anim.addFrame(frame3, 32);
            Anim.addFrame(frame4, 32);
            Anim.addFrame(frame5, 32);
            Anim.addFrame(frame6, 32);
            Anim.addFrame(frame7, 32);
            Anim.addFrame(frame8, 32);
            Anim.setOneShot(false);
            img_road_green.setBackgroundDrawable(Anim);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {

                    if (Anim!=null) {
                        if (Anim.isRunning()) {
                        } else {
                            Anim.start();
                        }

                    }
                }
            }, 0000);

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    protected void onStart() {
        //mTimerHandler.removeCallbacksAndMessages(mTimerRunnable);
        //mTimerHandler.removeCallbacks(mTimerRunnable);
        //mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.BgStart,false);
        //autoStart=false;
        //img_view_road_start.setImageResource(R.drawable.stop_button);
        //animLoader();
        //mTimerHandler.postDelayed(mTimerRunnable, Constants.DEMO_FIRST_DELAY);

        android.preference.PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        bindService(new Intent(this, GPSTracker.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);
        super.onStart();
        StartScrVisibility = true;

        if (Preference_Details.getBgStart() == true && Constants.BgStart == true){
            mTimerHandler.removeCallbacksAndMessages(null);
            mTimerHandler.removeCallbacks(mTimerRunnable);
            mIsRunning = true;
            //img_view_road_start.setImageResource(R.drawable.stop_button);
            mTimerHandler.postDelayed(mTimerRunnable, Constants.DEMO_FIRST_DELAY);
        }
        //  Toast.makeText(getApplicationContext(), "started", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        isChecked = true;
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection);
            mBound = false;
        }
        android.preference.PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
        // mTimerHandler.removeCallbacksAndMessages(mTimerRunnable);
        //   super.onStop();



        //Toast.makeText(RoadStartPageActivity.this, "stopp", Toast.LENGTH_SHORT).show();
        //   StartScrVisibility = false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        StartScrVisibility = true;

        //  Toast.makeText(getApplicationContext(), "restarted", Toast.LENGTH_SHORT).show();
    }



    private void gearAnimLoader() {
        try {
            img_road_green.setVisibility(View.INVISIBLE);
            img_view_road_green_gear.setVisibility(View.VISIBLE);
            img_view_road_green_center.setVisibility(View.INVISIBLE);
            img_view_road_green_pause.setVisibility(View.INVISIBLE);
            BitmapDrawable frame1 = (BitmapDrawable) getResources().getDrawable(
                    R.drawable.image_phone_rond_gris_engrenages_a01);
            BitmapDrawable frame2 = (BitmapDrawable) getResources().getDrawable(
                    R.drawable.image_phone_rond_gris_engrenages_a02);
            BitmapDrawable frame3 = (BitmapDrawable) getResources().getDrawable(
                    R.drawable.image_phone_rond_gris_engrenages_a03);
            BitmapDrawable frame4 = (BitmapDrawable) getResources().getDrawable(
                    R.drawable.image_phone_rond_gris_engrenages_a04);
            BitmapDrawable frame5 = (BitmapDrawable) getResources().getDrawable(
                    R.drawable.image_phone_rond_gris_engrenages_a05);
            gearAnim.addFrame(frame1, 50);
            gearAnim.addFrame(frame2, 50);
            gearAnim.addFrame(frame3, 50);
            gearAnim.addFrame(frame4, 50);
            gearAnim.addFrame(frame5, 50);
            gearAnim.setOneShot(false);
            img_view_road_green_gear.setBackgroundDrawable(gearAnim);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (gearAnim.isRunning()) {
                    } else {
                        gearAnim.start();
                    }
                }
            }, 0000);

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void Acc_position() {
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        list = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (list.size() > 0) {
            sm.registerListener(sel, (Sensor) list.get(0), SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            // Toast.makeText(getBaseContext(), "Error: No Accelerometer.", Toast.LENGTH_LONG).show();
        }
    }

    private void initAPI() {
        Log.v(TAG, "initAPI");
        String WorkingPath = DEMO_WORKING_PATH;
        String InputFile = WorkingPath +SUB_FOLD_NAME+ Constants.DEMO_IN_FILE_NAME;
        String OutputFile = WorkingPath +SUB_FOLD_NAME+ Constants.DEMO_OUT_FILE_NAME ;
        String LicenseFileBnd = WorkingPath +SUB_FOLD_NAME+Constants.DEMO_BIN_LICENSE_FILE;
        String LicenseFileNx = WorkingPath +SUB_FOLD_NAME+Constants.DEMO_LICENSE_FILE_NEXYAD;
        String MapSubPath = DEMO_WORKING_PATH_MAP+ DEMO_MAP_SUB_PATH;
        String UnlockKey = Constants.DEMO_UNLOCK_KEY;
        int Language = 0;
        try {
            Language = Constants.DEMO_LANGUAGE;
        }catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        mCount = 0;
        File tempPath[] = this.getExternalFilesDirs(null); //A call to this function seems needed to grant access to externals directories
        //mData = new CNxDemoData(null, OutputFile);
        mData = new CNxDemoData(InputFile, OutputFile);
        mJniFunction = JNDKSafetyNex.GetInstance(this);
        ParamArray[0] = SizeArray;
        ParamArray[1] = 0.3f;
        mJniFunction.GetInstantDriverSignature(mInstantDriverSignature);
        //boolean isLicOK = mJniFunction.Birth(LicenseFileBnd, MapSubPath, UnlockKey, Language, LicenseFileNx);
        tempLicInfo = new CNxLicenseInfo();
        isLicOK = mJniFunction.Birth(LicenseFileBnd, MapSubPath, UnlockKey, Language, LicenseFileNx, tempLicInfo);
        // Toast.makeText(RoadStartPageActivity.this, "License valid"+tempLicInfo.m_lDaysToExpiration, Toast.LENGTH_SHORT).show();

        if(isLicOK==false) {
            //TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            //String deviceid = manager.getDeviceId();
            String deviceidBirth = tempLicInfo.m_sRunningDeviceId;
            try {
                deviceid = mJniFunction.GetDeviceId();
            }catch (Exception e){
                e.printStackTrace();
            }

            if (autoStart==false)
            {
                try {
                    EndBoolStat = true;
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "Your license is not valid Please communicate the IMEI " + "(" + deviceid + ")" + " to Motiv AI in order to generate a license.", "ok");
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        mData=new CNxDemoData(String.valueOf(Accx),String.valueOf(Accy),String.valueOf(Accz),String.valueOf(lat),String.valueOf(longi),String.valueOf(speedd),String.valueOf(mTimeDiffGPSSeconds),String.valueOf(currentDegree));

        mInpuAPI = new CNxInputAPI();
        mNxRisk=new CNxRisk();

        //mJniFunction.SetTreshMin(20);
        try {
            //mJniFunction.UserStart();
            if (limitKmDistance==1){
                limitKmDistance=2;
                mJniFunction.UserStart();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("MissingPermission")
    private void getGps() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, RoadStartPageActivity.this);

        mGnssStatusCallback = new GnssStatus.Callback() {

            @Override
            public void onSatelliteStatusChanged(GnssStatus status) {

                satelliteCount = status.getSatelliteCount();

                totalSnr = 0;
                for (int i = 0; i < satelliteCount; i++){
                    if (status.usedInFix(i)) {
                        usedSatellites++;
                        totalSnr += status.getCn0DbHz(i); //this method obtains the signal from each satellite
                    }
                }
                // we calculate the average of the power of the GPS signal
                float avgSnr = (usedSatellites > 0) ? totalSnr / usedSatellites: 0.0f;
                // mGPScount = (int) totalSnr;
                mLastLocationMillis = System.currentTimeMillis();
                Log.d(TAG, "Number used satelites: " + usedSatellites + " SNR: " + totalSnr+"avg SNR: "+avgSnr);
            }
        };
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locationManager.registerGnssStatusCallback(mGnssStatusCallback);
            }
        } catch (SecurityException e) {
            Log.e(TAG,"Error "+e);
        }
    }

    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                lat= mCurrentLocation.getLatitude();
                longi = mCurrentLocation.getLongitude();
                if (cloudOneTime) {
                    //weatherData(lat,longi);
                    cloudOneTime = false;
                }

                updateLocationUI();
            }
        };
        mRequestingLocationUpdates = false;
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Restoring values from saved instance state
     */
    private void restoreValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates");
            }

            if (savedInstanceState.containsKey("last_known_location")) {
                mCurrentLocation = savedInstanceState.getParcelable("last_known_location");
            }

            if (savedInstanceState.containsKey("last_updated_on")) {
                mLastUpdateTime = savedInstanceState.getString("last_updated_on");
            }
        }
        updateLocationUI();
    }


    /**
     * Update the UI displaying the location data
     * and toggling the buttons
     */
    private void updateLocationUI() {
        if (mCurrentLocation != null) {

        }
        toggleButtons();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates);
        outState.putParcelable("last_known_location", mCurrentLocation);
        outState.putString("last_updated_on", mLastUpdateTime);

    }
    private void toggleButtons() {
        if (mRequestingLocationUpdates==false) {
            isCheckedStop=false;
        }
        else {
            isCheckedStop=true;
            isCheckedStatus=true;
        }
    }



    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");
                        // Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();
                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
                        updateLocationUI();
                        getGps();
                        isCheckedStop=true;
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(RoadStartPageActivity.this, REQUEST_CHECK_SETTINGS);
                                    isCheckedStatus=false;
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                // Toast.makeText(RoadStartPageActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }

    public void startLocationButtonClick() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    public void stopLocationButtonClick() {
        mRequestingLocationUpdates = false;
        stopLocationUpdates();
    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
                        toggleButtons();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("true",String.valueOf(resultCode));
        if (requestCode == 101) {
            startFirst=1;
            if (EndBoolStat==true) {
                EndBoolStat = false;
                File mfile;
                mfile = new File(DEMO_WORKING_PATH, DEMO_MAP_SUB_PATH);
                if (!mfile.exists()) {
                    mfile.mkdirs();
                }
                try {
                    zip_file = new ZipFile(DEMO_WORKING_PATH+"/Road_LIC_CERT1.zip");
                    zip_file.size();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                initAPI();
                if (isLicOK == false) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                    getLiscenseUpdate();
                } else if (tempLicInfo.m_lDaysToExpiration <= 2) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RENEWED");
                    getLiscenseUpdate();
                }
                if (!mfile.exists() || mfile.list().length == 0 ) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    mfile.delete();
                    downloadMapFile();
                }else {
                    try {
                        if (!(mfile.listFiles().length>=zip_file.size()-1)){
                            MapDownload = true;
                            txt_view_road_start.setClickable(false);
                            profile.setClickable(false);
                            more_start.setClickable(false);
                            mfile.delete();
                            downloadMapFile();
                        }
                    }catch (Exception e){
                        MapDownload = true;
                        txt_view_road_start.setClickable(false);
                        profile.setClickable(false);
                        more_start.setClickable(false);
                        mfile.delete();
                        downloadMapFile();
                    }

                }
                if (!Preference_Details.getMapCode().equals(Preference_Details.getPreviousMapCode())) {
                    if (mfile.exists()){
                        if (deleteDir(mfile)) {
                            MapDownload = true;
                            txt_view_road_start.setClickable(false);
                            profile.setClickable(false);
                            more_start.setClickable(false);
                            mfile.delete();
                            downloadMapFile();
                        }
                    }

                }

            }

        }


        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e(TAG, "User agreed to make required location settings changes.");
                        mRequestingLocationUpdates=true;
                        isCheckedStop=true;
                        gps_lost=true;
                        if (gps_second==true){
                            // Toast.makeText(RoadStartPageActivity.this, "started by ok", Toast.LENGTH_SHORT).show();
                            txt_view_road_start.setVisibility(View.INVISIBLE);
                            txt_view_road_stop.setVisibility(View.VISIBLE);
                            txt_start.setText("STOP");
                            txt_view_road_stop.setClickable(false);
                            tripStatus="- Ride Started!";
                            startDrive();
                            updateLocationUI();
                        }
                        img_view_road_green_pause.setVisibility(View.INVISIBLE);
                        img_road_green.setVisibility(View.VISIBLE);
                        img_road_green.setBackgroundDrawable(getDrawable(R.drawable.image_phone_rond_vert_route_d01));

                        break;
                    case Activity.RESULT_CANCELED:
                        gps_lost=false;
                        Log.e(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        isCheckedStop=false;
                        if (autoStart==true){
                            txt_view_road_start.setVisibility(View.VISIBLE);
                            txt_start.setText("START");
                            txt_view_road_stop.setVisibility(View.INVISIBLE);
                            txt_view_road_stop.setClickable(false);
                        }
                        img_view_road_green_pause.setVisibility(View.VISIBLE);
                        img_view_road_green_pause.setImageResource(R.drawable.image_phone_rond_rouge_pas_de_gps_e01);
                        img_road_green.setVisibility(View.INVISIBLE);
                        break;
                }
                break;
        }
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    @Override
    protected void onDestroy () {
        super.onDestroy();
        isChecked = true;
        Log.v(TAG, "onDestroy");
        stopLocationButtonClick();
        mIsRunning = false;

        //((ToggleButton)findViewById(R.id.toggleRun)).setChecked(mIsRunning);

        if (txt_view_road_stop.getVisibility()== View.VISIBLE) {
            ride_ongoing = true;
        }


    }

    private void doNextStep() {
        upDateHMI();
        if(mIsRunning) {
            //mIsRunning = false;
            mTimerHandler.postDelayed(mTimerRunnable, Constants.DEMO_RUN_DELAY);
        } else {
            //closeAPI();
            //upDateHMI();
        }
    }

    private void upDateHMI() {
        //Log.v(TAG, mMessage);
        // ((TextView)findViewById(R.id.textState)).setText(mMessage);
        //((ToggleButton)findViewById(R.id.toggleRun)).setChecked(mIsRunning);
    }

    void alertMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNeutralButton("OK", null);
        builder.create().show();
    }

    private void updateRisk() {
        Log.v(TAG, "upDateRisk");
        //Read data
        String lineFull = mData.ReadNextData();

        mTimeDiffGPSSeconds = (float) ((System.currentTimeMillis() - mLastLocationMillis )/ 1000.0);
        mJniFunction.SetGPSData(Double.valueOf(lat),Double.valueOf(longi),satelliteCount,Double.valueOf(currentDegree),Double.valueOf(speedd)*3.6,mTimeDiffGPSSeconds);
        text.setVisibility(View.INVISIBLE);
        text.setTextColor(getColor(R.color.white));
        //text.setText("Lat "+Double.valueOf(lat)+" Long "+Double.valueOf(longi)+" Satt count"+satelliteCount+" Degree "+Double.valueOf(currentDegree)+" Speed "+Double.valueOf(speedd)*3.6+" Diff "+mTimeDiffGPSSeconds + " State "+state);
        //  mData=new CNxDemoData(Accx,Accy,Accz,lat,longi,speedd,String.valueOf(mGPScount),String.valueOf(currentDegree));
        // setGpsDataAPI("YES",mJniFunction.GetUserAnticipationCapacity(),mJniFunction.GetUserSelfConfidance() );
        //Set Accel and get Risk
        if (Accx != 0.0) {
            mJniFunction.GetAccelDataWithRisk(Accx, Accy, Accz, mNxRisk);
        }

        //auto-ends

        long [] CurrEhorizon = mJniFunction.GetCurrEHorizon();

       /*  mJniFunction.GetMatchedGPSpos(new CNxMatchedPos());
         CNxMatchedPos c1 = new CNxMatchedPos();*/

        //Update Output
        /*if (CurrEhorizon != null) {
            try {
                mMessage = getMessageCustomer(CurrEhorizon);
                mData.WriteData(mMessage);
            }catch (Exception e){
                e.printStackTrace();
            }

        } else {
            mMessage = "Count " + (mCount+1)
                    + "; No e-Horizon";
        }*/
        /*} else {
            mMessage = "Count " + (mCount+1)
                    + "; Invalid line of data";
        }*/
        //mCount++;
    }

    private Long getUniTime() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        long timeInMili = calendar .getTimeInMillis();
        return timeInMili;
    }

    private void setGpsDataAPI(GPSEditDeviceReq gpsEditDeviceReq) {
       /* GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
        gpsEditDeviceReq.setCategory(getString(R.string.gps_category));
        gpsEditDeviceReq.setCompanyId(Constants.companyID);
        GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
        deviceField.setAlert("");
        deviceField.setAnticipation(String.valueOf(mJniFunction.GetUserAnticipationCapacity()));
        deviceField.setDrivingScore("");
        deviceField.setDrivingSkill("");
        deviceField.setLatitude(String.valueOf(lat));
        deviceField.setDrivingSkill("");
        deviceField.setKiloMeter(String.valueOf(kmDistance));
        deviceField.setLongitude(String.valueOf(longi));
        deviceField.setRisk(String.valueOf(mNxRisk.m_fRisk));
        deviceField.setSpeed(String.valueOf(speedd));
        deviceField.setTotalKiloMeter(String.valueOf(distance));
        deviceField.setSelfConfidence("");
        deviceField.setTravelTime(String.valueOf(duration));
        deviceField.setGpsCount(String.valueOf(totalSnr));
        deviceField.setAccelerX(String.valueOf(Accx));
        deviceField.setAccelerY(String.valueOf(Accy));
        deviceField.setAccelerZ(String.valueOf(Accz));
        deviceField.setSenRotate(String.valueOf(currentDegree));
        deviceField.setZipCode(zipcode);
        deviceField.setAlertId(String.valueOf(mNxRisk.m_lAlertId));
        deviceField.setAlertValue(String.valueOf(mNxRisk.m_TAlert));
        deviceField.setAlertTime("");
        deviceField.setRideDate("");
        deviceField.setRideTime("");
        gpsEditDeviceReq.setDeviceDataField(deviceField);
        gpsEditDeviceReq.setDivisionId(Constants.divisionId);
        gpsEditDeviceReq.setModuleId(Constants.moduleID);
        gpsEditDeviceReq.setPropertyId(Constants.propID);
        gpsEditDeviceReq.setPortionId(Constants.porID);
        gpsEditDeviceReq.setSectionId(Constants.secID);
        gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
        gpsEditDeviceReq.setType(getString(R.string.gps_type));
        gpsEditDeviceReq.setUserId(Constants.userID);*/
        // ProgressDialog.setLottieProgressDialog(this,Constants.lottiePath);
        //  ProgressDialog.showProgress();
        restMethods = RestClient.buildHTTPClient();
        restMethods.GPSPostDeviceDataResponse(Constants.referredBy,Preference_Details.getLoginSessID(),gpsEditDeviceReq).enqueue(new Callback<GPSEditDeviceResp>() {
            @Override
            public void onResponse(Call<GPSEditDeviceResp> call, Response<GPSEditDeviceResp> response) {
                Log.e("status",""+response.body());
                try{
                    //   ProgressDialog.dismissProgress();
                    if (response.code()==201) {
                        RegBoolStat = true;
                        mAlert_status = true;
                        mDist_status = true;
                        mEnd_status = true;
                        mStart_status = true;
                        Constants.score_frag=1;
                        if (gpsEditDeviceReq.getCategory().equals(getString(R.string.gps_START_DATA))) {
                            StartBoolStat = true;
                            Constants.startid=2;
                            txt_view_road_stop.setClickable(true);
                            //  Toast.makeText(getApplicationContext(), "START DATA SUCCESS", Toast.LENGTH_SHORT).show();
                        }
                        if (gpsEditDeviceReq.getCategory().equals(getString(R.string.gps_END_DATA))){

                            mTimerHandler.removeCallbacksAndMessages(null);
                            mTimerHandler.removeCallbacks(mTimerRunnable);
                            locationManager.removeUpdates(RoadStartPageActivity.this);
                            stopLocationButtonClick();
                            Constants.GPS_deviceID = "";
                            if(Constants.subType.contains("BACKGROUND")) {
                                if (gpsEditDeviceReq.getDeviceDataField().getDrivingScore().contains("-1") || gpsEditDeviceReq.getDeviceDataField().getDrivingScore().equals("") || gpsEditDeviceReq.getDeviceDataField().getDrivingScore().equals("0.0")) {
                                    if (Constants.score_frag==1) {
//                                                            txt_to_speech("Score not available.Travel was too short");
                                        ring= MediaPlayer.create(getApplicationContext(),R.raw.scorenotavailabe_travelwas_tooshort);
                                        ring.start();
                                        Constants.score_frag=0;
                                    }
                                }else {
                                    if(Math.round(Float.parseFloat(gpsEditDeviceReq.getDeviceDataField().getDrivingScore()))<95){
                                        String score="score_"+Math.round(Float.parseFloat(gpsEditDeviceReq.getDeviceDataField().getDrivingScore()));
                                        Constants.tracks[0] = R.raw.your_drivingscore_is;
                                        Constants.tracks[1] = getApplicationContext().getResources().getIdentifier(score, "raw", getApplicationContext().getPackageName());
                                        Constants.tracks[2] = R.raw.trytofollow_safealert;
                                        mediaPlayer = MediaPlayer.create(getApplicationContext(), Constants.tracks[currentTrack]);
                                        mediaPlayer.setOnCompletionListener(RoadStartPageActivity.this);
                                        mediaPlayer.start();
                                        //txt_to_speech("your driving score is"+String.valueOf(Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingScore())))+". try to follow better road drive it safe alerts");
                                        Constants.score_frag=0;
                                    }
                                    else if(Math.round(Float.parseFloat(gpsEditDeviceReq.getDeviceDataField().getDrivingScore()))>=95) {
                                        String score="score_"+Math.round(Float.parseFloat(gpsEditDeviceReq.getDeviceDataField().getDrivingScore()));
                                        Constants.tracks[0] = R.raw.your_drivingscore_is;
                                        Constants.tracks[1] = getApplicationContext().getResources().getIdentifier(score, "raw", getApplicationContext().getPackageName());
                                        Constants.tracks[2] = R.raw.excellent_driving;
                                        mediaPlayer = MediaPlayer.create(getApplicationContext(), Constants.tracks[currentTrack]);
                                        mediaPlayer.setOnCompletionListener(RoadStartPageActivity.this);
                                        mediaPlayer.start();
                                        //txt_to_speech("your driving score is"+String.valueOf(Math.round(Float.parseFloat(gpsgetDeviceRespArrayList.get(index).getDeviceDataList().get(j).getDeviceDataField().getDrivingScore())))+". excellent driving");
                                        Constants.score_frag=0;
                                    }
                                }
                            }
                            autoStart = true;
                            callEndData(response);
                        } if(gpsEditDeviceReq.getCategory().equals(getString(R.string.gps_ALERT_DATA))){
                            Constants.apicount=0;
                            //addLocalDATA(gpsEditDeviceReq);
                        } if(gpsEditDeviceReq.getCategory().equals(getString(R.string.gps_DISTANCE_DATA))){
                            //  addLocalDATA(gpsEditDeviceReq);
                        }

                        //  Toast.makeText(getApplicationContext(),"status_data :"+gpsEditDeviceReq.getCategory(),Toast.LENGTH_SHORT).show();
                    } else if(response.code()==412){
                        mAlert_status = true;
                        mDist_status = true;
                        mEnd_status = true;
                        mStart_status = true;
                        StartBoolStat = true;
                        RegBoolStat = true;
                        //setGpsDataAPI(gpsEditDeviceReq);
                        try {
                            String Error=response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for(int i=0;i<jsonArray1.length();i++){
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                if (gpsEditDeviceReq.getCategory().equals(getString(R.string.gps_START_DATA))||gpsEditDeviceReq.getCategory().equals(getString(R.string.gps_END_DATA))) {
                                    if (Constants.subType.contains("FOREGROUND")) {
                                        Util_functions.ShowValidateAlertError(getApplicationContext(), object1.getString("message"), "OK");
                                    }
                                }
                            }
                            txt_view_road_stop.setClickable(true);
                            //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.resp_empty),true);
                            //   ProgressDialog.dismissProgress();
                        } catch (Exception e){
                            e.printStackTrace();
                            txt_view_road_stop.setClickable(true);
                            //    ProgressDialog.dismissProgress();
                        }
                    } else {
                        mAlert_status = true;
                        mDist_status = true;
                        mEnd_status = true;
                        mStart_status = true;
                        StartBoolStat = true;
                        RegBoolStat = true;
                        /*if (gpsEditDeviceReq.getCategory().equals(getString(R.string.gps_END_DATA))) {
                            setGpsDataAPI(gpsEditDeviceReq);
                        }*/
                        //  ProgressDialog.dismissProgress();
                        String Error=response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text=obj.getString("message");
                        if (gpsEditDeviceReq.getCategory().equals(getString(R.string.gps_START_DATA))||gpsEditDeviceReq.getCategory().equals(getString(R.string.gps_END_DATA))) {
                            if (Constants.subType.contains("FOREGROUND")) {
                                Util_functions.ShowValidateAlertError(getApplicationContext(), text, "OK");
                            }
                        }
                        txt_view_road_stop.setClickable(true);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    txt_view_road_stop.setClickable(true);
                }
            }

            private void callEndData(Response<GPSEditDeviceResp> response) {
                LocalBroadcastManager.getInstance(RoadStartPageActivity.this).unregisterReceiver(receiver);
                Constants.horiz_count=0;
                if (net_end==false) {
                    if (mstopStatus == true) {
                        //finishAffinity();
                        startActivity(new Intent(RoadStartPageActivity.this, RoadStartPageActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.BgStart, false);
                        Constants.BgStart = false;
                        more_start.setClickable(true);
                        profile.setClickable(true);
                        StartScrVisibility = true;
                        autoStart = true;
                    } else {
                        //finishAffinity();

                        startActivity(new Intent(RoadStartPageActivity.this, MyRidesPageActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("fragPosition", "0").putExtra("activ_status", "2"));
                        mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.BgStart, false);
                        Constants.BgStart = false;
                        StartScrVisibility = false;
                        autoStart = true;
                        finish();

                    }

                }else {
                    SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    // creating a new variable for gson.
                    Gson gson = new Gson();
                    // getting data from gson and storing it in a string.
                    String json = gson.toJson(null);
                    // below line is to save data in shared
                    // prefs in the form of string.
                    editor.putString("ride", json);
                    // below line is to apply changes
                    // and save data in shared prefs.
                    editor.apply();
                    net_end=false;
                    courseModalArrayList=new GPSEditDeviceReq();
                }
                mstopStatus = false;
                MyRidesPageActivity.myRidesArrayList.clear();
                MyRidesPageActivity.offset = 0;MyRidesPageActivity.limit = 20;MyRidesPageActivity.limitLong = 30;
                if (percentage==1){
                    //Dont have audio file
                    //txt_to_speech("you are not allowed to start ride since battery is low");
                    ring= MediaPlayer.create(RoadStartPageActivity.this,R.raw.not_allow_start);
                    ring.start();

                }
            }

            @Override
            public void onFailure(Call<GPSEditDeviceResp> call, Throwable t) {
                mAlert_status = true;
                mDist_status = true;
                mEnd_status = true;
                mStart_status = true;
                StartBoolStat = true;

                //setGpsDataAPI(gpsEditDeviceReq);
                if (t.toString().contains("Unable to resolve host")||t.toString().contains("Failed to connect to")){
                    if (gpsEditDeviceReq.getCategory().equals(getString(R.string.gps_END_DATA))){
                        net_connectivity="NO";
                        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        // creating a new variable for gson.
                        Gson gson = new Gson();
                        // getting data from gson and storing it in a string.
                        String json = gson.toJson(gpsEditDeviceReq);
                        // below line is to save data in shared
                        // prefs in the form of string.
                        editor.putString("ride", json);
                        // below line is to apply changes
                        // and save data in shared prefs.
                        editor.apply();

                        startActivity(new Intent(RoadStartPageActivity.this,DashboardActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("fragPosition","0").putExtra("activ_status","2"));
                        mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.BgStart,false);
                        Constants.BgStart=false;
                        StartScrVisibility=false;
                        autoStart=true;

                    }else if (gpsEditDeviceReq.getCategory().equals(getString(R.string.gps_START_DATA))) {
                        if (Constants.subType.contains("FOREGROUND")) {
                            Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, getString(R.string.check_internet), "OK");
                        }

                        txt_view_road_stop.setClickable(true);
                    }
                }
                else {
                    if (!RoadStartPageActivity.this.isFinishing()){
                        if (gpsEditDeviceReq.getCategory().equals(getString(R.string.gps_START_DATA))||gpsEditDeviceReq.getCategory().equals(getString(R.string.gps_END_DATA))) {
                            if (Constants.subType.contains("FOREGROUND")) {
                                ShowAlertError(RoadStartPageActivity.this, t.getMessage(), "OK", RoadStartPageActivity.this);
                            }
                        }
                    }
                    t.printStackTrace();
                }

                txt_view_road_stop.setClickable(true);
                //  Util_functions.ShowValidateAlertError(getApplicationContext(),t.getMessage(),"OK");
            }
        });
    }



    private void closeAPI() {
       /*{
            "companyId": "comp00000000000000000001",
                "divisionId": "divi00000000000000000001",
                "moduleId": "modu00000000000000000001",
                "userId": "user00000000000000000001",
                "propertyId": "prop00000000000000000001",
                "sectionId": "sect00000000000000000001",
                "portionId": "port00000000000000000001",
                "deviceId": "port00000000000000000001",
                "status": "REGISTERED",
                "type": "SPEEDO_METER_DEVICE_DATA"
        }*/
        //stop drive - start

        Log.v(TAG, "closeAPI");
        mJniFunction.UserStop();
        float grade = mJniFunction.GetUserGrade();
        CNxUserStat InputUserStat = new CNxUserStat();
        CNxUserStat OutUserStat = new CNxUserStat();
        mJniFunction.GetSIUserStat(InputUserStat);
        mJniFunction.GetLocalUserStat(OutUserStat, InputUserStat);
        Totduration = 0;
        Totdistance = 0;
        zipcode = "";
        Boolean status = false;
        CNxFullStat[] FullStat = mJniFunction.GetCloudStat();
        // Toast.makeText(RoadStartPageActivity.this, "City Per "+OutUserStat.m_fTimeCityPer+"Highway Per "+OutUserStat.m_fTimeHighwayPer+"Road Per "+OutUserStat.m_fTimeRoadPer,Toast.LENGTH_LONG).show();
        if (FullStat.length>0) {
            //FullStat[0].m_sZipCode
            for (int i = 0; i < FullStat.length; i++){
                Totduration += FullStat[i].m_fDuration;
                Totdistance += FullStat[i].m_fDistance;
                //Toast.makeText(RoadStartPageActivity.this,"Occurence "+FullStat[i].m_iOccurrence + "City Per "+OutUserStat.m_fTimeCityPer+"Highway Per "+OutUserStat.m_fTimeHighwayPer+"Road Per "+OutUserStat.m_fTimeRoadPer,Toast.LENGTH_LONG).show();
                //  FullStat[i].
                // Toast.makeText(getApplicationContext(),"duration : distance : length :"+Totduration+" "+Totdistance+" "+FullStat.length,Toast.LENGTH_SHORT).show();
            }

            status = true;
        } else {
            status = false;
        }
        //mJniFunction.StoreCloudStatToMemory(DEMO_WORKING_PATH+SUB_FOLD_NAME);

        if (status) {
            Totduration = Totduration/60;
            Totdistance = Totdistance/1000;

            mMessage = "Grade : " + grade
                    + "; Duration : " + Totduration
                    + "; Distance : " + Totdistance
                    + "; Anticipation : " + mJniFunction.GetUserAnticipationCapacity()
                    + "; Self Confidence : " + mJniFunction.GetUserSelfConfidance()
                    + "; Expertise level : " + mJniFunction.GetUserExpertiseLevel()
                    + "; Car position : " + mJniFunction.GetCarPosition()
                    + "; Version :" + mJniFunction.GetVersion()
                    + "; Risk " + Math.round(mNxRisk.m_fRisk * 100)
                    + "; altid" + mNxRisk.m_lAlertId
                    + "; speedalrt " + mNxRisk.m_SpeedAlert
                    + "; talert" + mNxRisk.m_TAlert
            //+switchText +mspeedText
            ;

        } else {

            Totduration =  OutUserStat.m_fTotaltimeSec/60;
            Totdistance = OutUserStat.m_fTotalDistance/1000;
            mMessage = "Grade : " + grade
                    + "; Duration : " +Totduration
                    + "; Distance : " + Totdistance
                    + "; Anticipation : " + mJniFunction.GetUserAnticipationCapacity()
                    + "; Self Confidence : " + mJniFunction.GetUserSelfConfidance()
                    + "; Expertise level : " + mJniFunction.GetUserExpertiseLevel()
                    + "; Car position : " + mJniFunction.GetCarPosition()
                    + "; Version :" + mJniFunction.GetVersion()
                    + "; Risk " + Math.round(mNxRisk.m_fRisk * 100)
                    + "; altid" + mNxRisk.m_lAlertId
                    + "; speedalrt " + mNxRisk.m_SpeedAlert
                    + "; talert" + mNxRisk.m_TAlert
            //+switchText +mspeedText
            //+switchText +mspeedText
            ;
        }



        //Toast.makeText(RoadStartPageActivity.this, ""+OutUserStat.m_lAbsStartTime+"  "+OutUserStat.m_sStartTime, Toast.LENGTH_LONG).show();


      /*  int i=0;
        for(i=0;i<5;i++){

           if(i<=3){
                 GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
            gpsEditDeviceReq.setCategory(getString(R.string.gps_ALERT_DATA));
            gpsEditDeviceReq.setCompanyId(Constants.companyID);
            gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(3));
               GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
               deviceField.setAlert(diff_dur);
               deviceField.setAnticipation("");
               deviceField.setDrivingScore("");
               deviceField.setDrivingSkill("");
               deviceField.setLatitude("");
               deviceField.setDrivingSkill("");
               deviceField.setKiloMeter("");
               deviceField.setLongitude("");
               deviceField.setRisk("");
               deviceField.setSpeed("");
               deviceField.setTotalKiloMeter("");
               deviceField.setSelfConfidence("");
               deviceField.setTravelTime("");
               deviceField.setGpsCount("");
               deviceField.setAccelerX("");
               deviceField.setAccelerY("");
               deviceField.setAccelerZ("");
               deviceField.setSenRotate("");
               deviceField.setRideTime(String.valueOf(getUniTime()));
               deviceField.setAlertId("");
               deviceField.setAlertValue("3");
               deviceField.setAlertKiloMeter("1000");
               deviceField.setAlertTime("");
               deviceField.setZipCode("1000");
               deviceField.setRideDate("");
            gpsEditDeviceReq.setDeviceDataField(deviceField);
            gpsEditDeviceReq.setDivisionId(Constants.divisionId);
            gpsEditDeviceReq.setModuleId(Constants.moduleID);
            gpsEditDeviceReq.setPropertyId(Constants.propID);
            gpsEditDeviceReq.setPortionId(Constants.porID);
            gpsEditDeviceReq.setSectionId(Constants.secID);
            gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
            gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
            gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
            gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
            GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
            deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
            deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
            deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
            deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
            deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
            gpsEditDeviceReq.setDeviceDataField1(deviceField1);
            setGpsDataAPI(gpsEditDeviceReq);

           } else {
*/


        CNxFullStat[] FullStatt = mJniFunction.GetCloudStat();
        if (FullStatt.length > 0) {
            for (int i = 0; i < FullStatt.length; i++) {
                kmDistance += FullStatt[i].m_fDistance / 1000;
                            /*text.setVisibility(View.VISIBLE);
                            text.setTextColor(getColor(R.color.white));
                            text.setText(String.valueOf(kmDistance));*/
                if (!FullStatt[i].m_sZipCode.equals("99999")) {
                    zipcode = FullStatt[i].m_sZipCode;
                }
            }
            zipcode = "";
        }
        GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
        gpsEditDeviceReq.setCategory(getString(R.string.gps_END_DATA));
        gpsEditDeviceReq.setName("Ride");
        GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
        deviceField.setDeviceMode(deviceModeStatus);
        deviceField.setAnticipation(String.valueOf(mJniFunction.GetUserAnticipationCapacity()));
        deviceField.setDrivingScore(String.valueOf(mJniFunction.GetUserGrade()));
        //deviceField.setDrivingScore(String.valueOf("90"));
        deviceField.setDrivingSkill(String.valueOf(mJniFunction.GetUserExpertiseLevel()));
        deviceField.setSelfConfidence(String.valueOf(mJniFunction.GetUserSelfConfidance()));
        /*deviceField.setAnticipation(String.valueOf("100"));
        deviceField.setDrivingScore(String.valueOf("90"));
        deviceField.setDrivingSkill(String.valueOf("70"));
        deviceField.setSelfConfidence(String.valueOf("80"));*/
        deviceField.setTotalKiloMeter(String.valueOf(Totdistance));
        deviceField.setTravelTime(String.valueOf(Totduration));
               /* deviceField.setAnticipation("100");
                deviceField.setDrivingScore("100");
                deviceField.setDrivingSkill("100");
                deviceField.setSelfConfidence("100");
                deviceField.setTotalKiloMeter("10.00");
                deviceField.setTravelTime("10.00");*/
        //deviceField.setRideTime(String.valueOf(getUniTime()));
        deviceField.setRideTime(String.valueOf(getUniTime()));
        deviceField.setKiloMeter("");
        /*deviceField.setLatitude("");
        deviceField.setLongitude("");
        deviceField.setRisk("");
        deviceField.setSpeed("");
        deviceField.setAlert("");
        deviceField.setGpsCount("");
        deviceField.setAccelerX("");
        deviceField.setAccelerY("");
        deviceField.setAccelerZ("");
        deviceField.setSenRotate("");*/
        deviceField.setUrbanPercent(String.valueOf(OutUserStat.m_fTimeCityPer));
        deviceField.setRuralPercent(String.valueOf(OutUserStat.m_fTimeRoadPer));
        deviceField.setHighwayPercent(String.valueOf(OutUserStat.m_fTimeHighwayPer));
        deviceField.setLatitude(String.valueOf(lat));
        deviceField.setLongitude(String.valueOf(longi));
        deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
        deviceField.setSpeed(String.valueOf(speedd * 3.6));
        deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
        //deviceField.setGpsCount(String.valueOf(totalSnr));
        deviceField.setAccelerX(String.valueOf(Accx));
        deviceField.setAccelerY(String.valueOf(Accy));
        deviceField.setAccelerZ(String.valueOf(Accz));
        deviceField.setSenRotate(String.valueOf(currentDegree));
        deviceField.setAlertId("");
        deviceField.setAlertValue("");
        deviceField.setAlertTime("");
        deviceField.setRideDate("");
        deviceField.setZipCode(zipcode);
        deviceField.setAlertKiloMeter(String.valueOf(Totdistance));
        deviceField.setRideTime(String.valueOf(getUniTime()));
        deviceField.setGyroscopeX(String.valueOf(gyroX));
        deviceField.setGyroscopeY(String.valueOf(gyroY));
        deviceField.setGyroscopeZ(String.valueOf(gyroZ));
        deviceField.setMagnatometerX(String.valueOf(magX));
        deviceField.setMagnatometerY(String.valueOf(magY));
        deviceField.setMagnatometerZ(String.valueOf(magZ));
        deviceField.setAlertName("");
        deviceField.setEhorizonLength(String.valueOf(FullStatt.length));
        deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
        deviceField.setEngineState(String.valueOf(state));
        gpsEditDeviceReq.setDeviceDataField(deviceField);
        gpsEditDeviceReq.setCompanyId(Constants.companyID);
        gpsEditDeviceReq.setDivisionId(Constants.divisionId);
        gpsEditDeviceReq.setModuleId(Constants.moduleID);
        gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
        gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
        gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
        /*gpsEditDeviceReq.setPropertyId(Constants.propID);
        gpsEditDeviceReq.setPortionId(Constants.porID);
        gpsEditDeviceReq.setSectionId(Constants.secID);*/
        gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
        gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
        gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
        gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
        GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
        deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
        deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
        deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
        deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
        deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
        gpsEditDeviceReq.setDeviceDataField1(deviceField1);
        if(Constants.subType.contains("BACKGROUND")) {
            gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
        }else {
            gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
        }
        Log.d("end_log",gpsEditDeviceReq.toString());
        //addLocalDATA(gpsEditDeviceReq);
        if (mEnd_status) {

            mEnd_status = false;
            //handler_gps.removeCallbacksAndMessages(null);
            //runnable=false;
            setGpsDataAPI(gpsEditDeviceReq);
        }
        mJniFunction.Death();
        //Stop-drive-ends
          /* }


        }
*/


    }


    private String getMessageCustomer(long [] prmCurrEhorizon) {
        String TempMessage;
        if (prmCurrEhorizon != null && prmCurrEhorizon.length > 4) {
            NxAlert=mNxRisk.m_TAlert.m_iNxAlertValue;
            NxAlertID = mNxRisk.m_lAlertId;
            NxAlertTS = mNxRisk.m_lAlertTS;
            TTS=mNxRisk.m_TAlert.m_sTextToSpeech;
            state = mNxRisk.m_iSafetyNexEngineState;
            Constants.apicount_speed = state;
            mJniFunction.GetInstantDriverSignature(mInstantDriverSignature);
            String JsonString = mJniFunction.GetRiskAddData(Temp,10);
            Gson gson = new Gson();
            Type type = new TypeToken<RiskAddDataModel>() {}.getType();
            mRiskAddData = gson.fromJson(JsonString, type);
            if (!String.valueOf(Math.round(mNxRisk.m_fRisk * 100)).contains("-")) {
                risk_val.setText(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
            }

            /*text_eg.setText(mRiskAddData.getDistPoi().getString().substring(0,mRiskAddData.getDistPoi().getString().length()-2)+" "+String.valueOf(JsonString)+"  "+String.valueOf(mInstantDriverSignature.m_iDrivingStyle)+
                    "  "+String.valueOf(mInstantDriverSignature.m_iDriverState)+"  "+String.valueOf(mInstantDriverSignature.m_iRiskStyle)+"  "+String.valueOf(mInstantDriverSignature.m_iDriverState)+"  "+String.valueOf(mInstantDriverSignature.m_iTrafficScore)+"  "+String.valueOf(mInstantDriverSignature.m_iTransportId)+
                    "  "+String.valueOf(mInstantDriverSignature.m_iSevereAcceleration)+"  "+String.valueOf(mInstantDriverSignature.m_iSevereBraking)+"  "+String.valueOf(mInstantDriverSignature.m_iSevereCornering));*/
            /*text_eg.setText(String.valueOf(JsonString)+"  "+String.valueOf(mInstantDriverSignature.m_iDrivingStyle)+
                    "  "+String.valueOf(mInstantDriverSignature.m_iDriverState)+"  "+String.valueOf(mInstantDriverSignature.m_iRiskStyle)+"  "+String.valueOf(mInstantDriverSignature.m_iDriverState)+"  "+String.valueOf(mInstantDriverSignature.m_iTrafficScore)+"  "+String.valueOf(mInstantDriverSignature.m_iTransportId)+
                    "  "+String.valueOf(mInstantDriverSignature.m_iSevereAcceleration)+"  "+String.valueOf(mInstantDriverSignature.m_iSevereBraking)+"  "+String.valueOf(mInstantDriverSignature.m_iSevereCornering)+" Alert "+mNxRisk.m_TAlert.m_iNxAlertValue+" "+mNxRisk.m_bAutoStartDetection);*/
            //text_eg.setText(String.valueOf(state)+" "+String.valueOf(autoStart)+" start"+String.valueOf(txt_view_road_start.getVisibility())+" stop"+String.valueOf(txt_view_road_stop.getVisibility()));
            setstartDet();
            /*text.setVisibility(View.VISIBLE);
            text.setTextColor(getColor(R.color.white));
            text.setText(String.valueOf(mNxRisk.m_TAlert.m_iNxAlertValue)+" "+String.valueOf(mNxRisk.m_TAlert.m_sTextToSpeech));*/
            TempMessage = "Count " + mCount
                    + "; Speed :" + Math.round(Double.valueOf(speedd)*3.6) + "km/h"
                    + "; State :" + state
                    + "; Risk :" + Math.round(mNxRisk.m_fRisk * 100) + "%"
                    + "; TTS :" +TTS
                    + "; NxAlert :" + NxAlert
                    + "; len :" +  prmCurrEhorizon[1]+" "+switchText +mspeedText
            ;
            Log.d("ENGINE STATE",String.valueOf(state));
            //state = 1;
            if (state != 2 && state != 3 && state != 4){
                stopCount=0;
            }
            switch_state();
            if (autoStart==false) {
                driver_behaviour();
                alert_state();
            }


        } else {
            NxAlert=mNxRisk.m_TAlert.m_iNxAlertValue;
            NxAlertID = mNxRisk.m_lAlertId;
            NxAlertTS = mNxRisk.m_lAlertTS;
            TTS=mNxRisk.m_TAlert.m_sTextToSpeech;
            state = mNxRisk.m_iSafetyNexEngineState;
            Constants.apicount_speed = state;
            mJniFunction.GetInstantDriverSignature(mInstantDriverSignature);
            String JsonString = mJniFunction.GetRiskAddData(Temp,10);
            Gson gson = new Gson();
            Type type = new TypeToken<RiskAddDataModel>() {}.getType();
            mRiskAddData = gson.fromJson(JsonString, type);
            if (!String.valueOf(Math.round(mNxRisk.m_fRisk * 100)).contains("-")) {
                risk_val.setText(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
            }
            /*text_eg.setText(mRiskAddData.getDistPoi().getString().substring(0,mRiskAddData.getDistPoi().getString().length()-2)+" "+String.valueOf(JsonString)+"  "+String.valueOf(mInstantDriverSignature.m_iDrivingStyle)+
                    "  "+String.valueOf(mInstantDriverSignature.m_iDriverState)+"  "+String.valueOf(mInstantDriverSignature.m_iRiskStyle)+"  "+String.valueOf(mInstantDriverSignature.m_iDriverState)+"  "+String.valueOf(mInstantDriverSignature.m_iTrafficScore)+"  "+String.valueOf(mInstantDriverSignature.m_iTransportId)+
                    "  "+String.valueOf(mInstantDriverSignature.m_iSevereAcceleration)+"  "+String.valueOf(mInstantDriverSignature.m_iSevereBraking)+"  "+String.valueOf(mInstantDriverSignature.m_iSevereCornering));*/
            /*text_eg.setText(String.valueOf(JsonString)+"  "+String.valueOf(mInstantDriverSignature.m_iDrivingStyle)+
                    "  "+String.valueOf(mInstantDriverSignature.m_iDriverState)+"  "+String.valueOf(mInstantDriverSignature.m_iRiskStyle)+"  "+String.valueOf(mInstantDriverSignature.m_iDriverState)+"  "+String.valueOf(mInstantDriverSignature.m_iTrafficScore)+"  "+String.valueOf(mInstantDriverSignature.m_iTransportId)+
                    "  "+String.valueOf(mInstantDriverSignature.m_iSevereAcceleration)+"  "+String.valueOf(mInstantDriverSignature.m_iSevereBraking)+"  "+String.valueOf(mInstantDriverSignature.m_iSevereCornering)+" Alert "+mNxRisk.m_TAlert.m_iNxAlertValue+" "+mNxRisk.m_bAutoStartDetection);*/
            Log.d("ENGINE STATE",String.valueOf(state));
            /*text.setVisibility(View.VISIBLE);
            text.setTextColor(getColor(R.color.white));
            text.setText(String.valueOf(mNxRisk.m_TAlert.m_iNxAlertValue)+" "+String.valueOf(mNxRisk.m_TAlert.m_sTextToSpeech));*/
            TempMessage = "Count " + mCount
                    + "; Speed :" + Math.round(Double.valueOf(speedd)*3.6) + "km/h"
                    + "; State :" + state
                    + "; Risk :" + Math.round(mNxRisk.m_fRisk * 100) + "%"
                    + "; TTS :" +TTS
                    + "; NxAlert :" + NxAlert+" "+switchText +mspeedText
            ;
            //text_eg.setText(String.valueOf(state)+" "+String.valueOf(autoStart)+" start"+String.valueOf(txt_view_road_start.getVisibility())+" stop"+String.valueOf(txt_view_road_stop.getVisibility()));
            //state = 1;
            if (state != 2 && state != 3 && state != 4){
                stopCount=0;
            }
            switch_state();
            if (autoStart==false) {
                driver_behaviour();
                alert_state();
            }

        }
        if (sCount>0){
            // text.setText("Message "+TempMessage);
            mData=new CNxDemoData(String.valueOf(Accx),String.valueOf(Accy),String.valueOf(Accz),String.valueOf(lat),String.valueOf(longi),String.valueOf(speedd),String.valueOf(mTimeDiffGPSSeconds),String.valueOf(currentDegree));
            mInpuAPI = new CNxInputAPI();
            // Toast.makeText(getApplicationContext(), "input speed 1 | sample 2 curr"+mInpuAPI.mSpeed+" "+speedd, Toast.LENGTH_SHORT).show();
            mspeedText = "input speed 1 |"+Math.round(Double.valueOf(speedd)*3.6)+" sample 2 curr"+speedd;
            mNxRisk = new CNxRisk();
            //mJniFunction.SetTreshMin(20);
            //mJniFunction.UserStart();
        }

        return TempMessage;
    }
    static int getRandomNumber(int max, int min)
    {
        return (int)((Math.random()
                * (max - min)) + min);
    }
    public void txt_to_speech(String str) {
//speeech for text
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) { if (Preference_Details.getVoice()==true){
                    textToSpeech.setLanguage(Locale.UK);
                    textToSpeech.setPitch(0.7f);
                    textToSpeech.setSpeechRate(1f);
                    textToSpeech.speak(str,TextToSpeech.QUEUE_FLUSH, null,null);
                }
                }
            }
        });
        /* animation wit red image */
/*        try {
            BitmapDrawable frame1 = (BitmapDrawable) getResources().getDrawable(
                    R.drawable.image_phone_rond_rouge_alerte_a01);
            BitmapDrawable frame2 = (BitmapDrawable) getResources().getDrawable(
                    R.drawable.image_phone_rond_rouge_alerte_a02);
            Anim = new AnimationDrawable();
            Anim.addFrame(frame1, 100);
            Anim.addFrame(frame2, 100);
            Anim.setOneShot(false);
            img_road_green.setBackgroundDrawable(Anim);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (Anim!=null) {
                        Anim.start();
                    }
                }
            }, 0000);

        } catch (Exception e) {
// TODO: handle exception
        }
//beep sound
        ToneGenerator beep = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 200);
        beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD,200);*/
    }
    private void driver_behaviour(){
        if (mInstantDriverSignature.m_iDriverState == 1){
            img_driver_state.setBackground(getDrawable(R.drawable.normal));
            txt_driver_state.setText("NORMAL");
        }else if (mInstantDriverSignature.m_iDriverState == 2){
            img_driver_state.setBackground(getDrawable(R.drawable.tired));
            txt_driver_state.setText("TIRED");
        }else if (mInstantDriverSignature.m_iDriverState == 3){
            img_driver_state.setBackground(getDrawable(R.drawable.angry));
            txt_driver_state.setText("ANGRY");
        }else {
            img_driver_state.setBackground(getDrawable(R.drawable.start_unknown));
            txt_driver_state.setText("UNKNOWN");
        }

        if (mInstantDriverSignature.m_iRiskStyle == 1){
            img_risk_style.setBackground(getDrawable(R.drawable.anticipative));
            txt_risk_style.setText("ANTICIPATIVE");
        }else if (mInstantDriverSignature.m_iRiskStyle == 2){
            img_risk_style.setBackground(getDrawable(R.drawable.expert));
            txt_risk_style.setText("EXPERT");
        }else if (mInstantDriverSignature.m_iRiskStyle == 3){
            img_risk_style.setBackground(getDrawable(R.drawable.risky));
            txt_risk_style.setText("RISKY");
        }else {
            img_risk_style.setBackground(getDrawable(R.drawable.start_unknown));
            txt_risk_style.setText("UNKNOWN");
        }

        if (mInstantDriverSignature.m_iDrivingStyle == 1){
            img_driving_style.setBackground(getDrawable(R.drawable.sportive));
            txt_driving_style.setText("SPORTIVE");
        }else if (mInstantDriverSignature.m_iDrivingStyle == 2){
            img_driving_style.setBackground(getDrawable(R.drawable.agressive));
            txt_driving_style.setText("AGREESIVE");
        }else if (mInstantDriverSignature.m_iDrivingStyle == 3){
            img_driving_style.setBackground(getDrawable(R.drawable.chaotic));
            txt_driving_style.setText("CHAOTIC");
        }else if (mInstantDriverSignature.m_iDrivingStyle == 4){
            img_driving_style.setBackground(getDrawable(R.drawable.normal));
            txt_driving_style.setText("NORMAL");
        }else if (mInstantDriverSignature.m_iDrivingStyle == 5){
            img_driving_style.setBackground(getDrawable(R.drawable.eco));
            txt_driving_style.setText("ECO");
        }else {
            img_driving_style.setBackground(getDrawable(R.drawable.start_unknown));
            txt_driving_style.setText("UNKNOWN");
        }

        if (mInstantDriverSignature.m_iSevereAcceleration!=0){
            if (mInstantDriverSignature.m_iSevereAcceleration==1){
                img_severe.setBackground(getDrawable(R.drawable.force_acc_green));
                txt_severe.setText("S.ACCELERATION\n(Low)");
            }else if (mInstantDriverSignature.m_iSevereAcceleration==2){
                img_severe.setBackground(getDrawable(R.drawable.force_acc_amber));
                txt_severe.setText("S.ACCELERATION\n(Medium)");
            }else {
                img_severe.setBackground(getDrawable(R.drawable.force_acc_red));
                txt_severe.setText("S.ACCELERATION\n(High)");
            }
        }else if (mInstantDriverSignature.m_iSevereBraking!=0){
            if (mInstantDriverSignature.m_iSevereBraking==1){
                img_severe.setBackground(getDrawable(R.drawable.severe_brake_green));
                txt_severe.setText("S.BRAKING\n(Low)");
            }else if (mInstantDriverSignature.m_iSevereBraking==2){
                img_severe.setBackground(getDrawable(R.drawable.severe_brake_amber));
                txt_severe.setText("S.BRAKING\n(Medium)");
            }else {
                img_severe.setBackground(getDrawable(R.drawable.severe_brake_red));
                txt_severe.setText("S.BRAKING\n(High)");
            }
        }else if (mInstantDriverSignature.m_iSevereCornering!=0){
            if (mInstantDriverSignature.m_iSevereCornering==1){
                img_severe.setBackground(getDrawable(R.drawable.severe_corner_green));
                txt_severe.setText("S.CORNERING\n(Low)");
            }else if (mInstantDriverSignature.m_iSevereCornering==2){
                img_severe.setBackground(getDrawable(R.drawable.severe_corner_amber));
                txt_severe.setText("S.CORNERING\n(Medium)");
            }else {
                img_severe.setBackground(getDrawable(R.drawable.severe_corner_red));
                txt_severe.setText("S.CORNERING\n(High)");
            }
        }else {
            img_severe.setBackground(getDrawable(R.drawable.start_unknown));
            txt_severe.setText("UNKNOWN");
        }

    }
    private void alert_state(){
        if(Math.round(mNxRisk.m_fRisk * 100)>50){
        /*if (alert_show==0) {
            if (Math.round(mNxRisk.m_fRisk * 100) > 90) {
                currentTrack = 0;
                Constants.tracks[0] = R.raw.danger;
                Constants.tracks[1] = getApplicationContext().getResources().getIdentifier(alert, "raw", getApplicationContext().getPackageName());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), Constants.tracks[0]);
                mediaPlayer.setOnCompletionListener(RoadStartPageActivity.this);
                mediaPlayer.start();
//                                        txt_to_speech("Caution" + Util_functions.getSubCategoryValueLower(mNxRisk.m_TAlert.m_iNxAlertValue) + ". Slow Down");
            } else if (Math.round(mNxRisk.m_fRisk * 100) > 5 && Math.round(mNxRisk.m_fRisk * 100) < 90) {
                currentTrack = 0;
                Constants.tracks[0] = getApplicationContext().getResources().getIdentifier(alert, "raw", getApplicationContext().getPackageName());
                Constants.tracks[1] = R.raw.slow_down;
                mediaPlayer = MediaPlayer.create(getApplicationContext(), Constants.tracks[0]);
                mediaPlayer.setOnCompletionListener(RoadStartPageActivity.this);
                mediaPlayer.start();
//                                        txt_to_speech(Util_functions.getSubCategoryValueLower(mNxRisk.m_TAlert.m_iNxAlertValue) + ". Slow Down");
            }
        }*/
            if (alert_show!=mNxRisk.m_TAlert.m_iNxAlertValue||mNxRisk.m_TAlert.m_iNxAlertValue==-1){
                Constants.alert_count_v2=0;
            }
            alert_show=mNxRisk.m_TAlert.m_iNxAlertValue;
        }


        if (mNxRisk.m_TAlert.m_iNxAlertValue != -1) {
            Constants.horiz_count++;
            if (mNxRisk.m_TAlert.m_iNxAlertValue!=alertValue){
                alertId=0;
            }
            if (mNxRisk.m_TAlert.m_iNxAlertValue<29){
                alert="alert_"+mNxRisk.m_TAlert.m_iNxAlertValue;
            }else {
                alert="alert_"+3;
            }
            CNxFullStat[] FullStat = mJniFunction.GetCloudStat();
            if (FullStat.length > 0) {
                for (int i = 0; i < FullStat.length; i++) {
                    alertDuration += FullStat[i].m_fDuration / 60;
                    alertKm += FullStat[i].m_fDistance / 1000;
                    if (!FullStat[i].m_sZipCode.equals("99999")) {
                        alertZipcode = FullStat[i].m_sZipCode;
                    }
                }
                alertValue=mNxRisk.m_TAlert.m_iNxAlertValue;
                //  Toast.makeText(RoadStartPageActivity.this, ""+alertKm, Toast.LENGTH_SHORT).show();
                //  FullStat[i].
                // Toast.makeText(getApplicationContext(),"duration : distance : length :"+Totduration+" "+Totdistance+" "+FullStat.length,Toast.LENGTH_SHORT).show();
                if (Constants.alert_show!=0) {
                    //setAlertImageAlone(getAlertImageString(mNxRisk.m_TAlert.m_iNxAlertValue), alert_img);
                    setAlertImageAlone(getAlertImageString(alert_show), alert_img);

                }
                GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
                gpsEditDeviceReq.setCategory(getString(R.string.gps_ALERT_DATA));
                gpsEditDeviceReq.setCompanyId(Constants.companyID);
                gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(mNxRisk.m_TAlert.m_iNxAlertValue));
                GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
                deviceField.setAlert("");
                deviceField.setAnticipation("");
                deviceField.setDrivingScore("");
                deviceField.setDrivingSkill("");
                deviceField.setLatitude(String.valueOf(lat));
                deviceField.setDrivingSkill("");
                deviceField.setKiloMeter("");
                deviceField.setLongitude(String.valueOf(longi));
                deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                deviceField.setSpeed(String.valueOf(speedd * 3.6));
                deviceField.setTotalKiloMeter("");
                deviceField.setSelfConfidence("");
                deviceField.setTravelTime("");
                deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
                //deviceField.setGpsCount(String.valueOf(totalSnr));
                deviceField.setAccelerX(String.valueOf(Accx));
                deviceField.setAccelerY(String.valueOf(Accy));
                deviceField.setAccelerZ(String.valueOf(Accz));
                deviceField.setSenRotate(String.valueOf(currentDegree));
                deviceField.setRideTime(String.valueOf(getUniTime()));
                deviceField.setAlertId(String.valueOf(mNxRisk.m_lAlertId));
                deviceField.setAlertValue(String.valueOf(mNxRisk.m_TAlert.m_iNxAlertValue));
                deviceField.setAlertKiloMeter(String.valueOf(alertKm));
                deviceField.setAlertTime(String.valueOf(alertDuration));
                deviceField.setZipCode(alertZipcode);
                deviceField.setRideDate("");
                deviceField.setGyroscopeX(String.valueOf(gyroX));
                deviceField.setGyroscopeY(String.valueOf(gyroY));
                deviceField.setGyroscopeZ(String.valueOf(gyroZ));
                deviceField.setMagnatometerX(String.valueOf(magX));
                deviceField.setMagnatometerY(String.valueOf(magY));
                deviceField.setMagnatometerZ(String.valueOf(magZ));
                deviceField.setAlertName(Util_functions.getSubCategoryValueLowerr(mNxRisk.m_TAlert.m_iNxAlertValue));
                deviceField.setEhorizonLength(String.valueOf(FullStat.length));
                deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
                deviceField.setEngineState(String.valueOf(state));
                gpsEditDeviceReq.setDeviceDataField(deviceField);
                gpsEditDeviceReq.setDivisionId(Constants.divisionId);
                gpsEditDeviceReq.setModuleId(Constants.moduleID);
                            /*gpsEditDeviceReq.setPropertyId(Constants.propID);
                            gpsEditDeviceReq.setPortionId(Constants.porID);
                            gpsEditDeviceReq.setSectionId(Constants.secID);*/
                gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
                gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
                gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
                gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
                gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
                gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
                gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
                GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
                deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
                deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
                deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
                deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
                deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
                gpsEditDeviceReq.setDeviceDataField1(deviceField1);
                if(Constants.subType.contains("BACKGROUND")) {
                    gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
                }else {
                    gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
                }
                Log.d("alert_log", gpsEditDeviceReq.toString());
                //if (Constants.alertId == 0&&apicount==0) {
                if (Constants.alertId == 0 && Constants.alert_count_v2 == 0) {
                    Constants.alert_count_v2++;
                    apicount++;
                    //Toast.makeText(RoadStartPageActivity.this,String.valueOf(apicount),Toast.LENGTH_SHORT).show();
                    if (mAlert_status) {
                        mAlert_status = false;
                        if (Math.round(mNxRisk.m_fRisk * 100) > 90) {
                            currentTrack=0;
                            Constants.tracks[0] = R.raw.danger;
                            Constants.tracks[1] = getApplicationContext().getResources().getIdentifier(alert, "raw", getApplicationContext().getPackageName());
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), Constants.tracks[0]);
                            mediaPlayer.setOnCompletionListener(RoadStartPageActivity.this);
                            mediaPlayer.start();
//                                        txt_to_speech("Caution" + Util_functions.getSubCategoryValueLower(mNxRisk.m_TAlert.m_iNxAlertValue) + ". Slow Down");
                        } else if (Math.round(mNxRisk.m_fRisk * 100)>50&&Math.round(mNxRisk.m_fRisk * 100)<90){
                            currentTrack=0;
                            Constants.tracks[0] =getApplicationContext().getResources().getIdentifier(alert, "raw", getApplicationContext().getPackageName());
                            Constants.tracks[1] = R.raw.slow_down;
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), Constants.tracks[0]);
                            mediaPlayer.setOnCompletionListener(RoadStartPageActivity.this);
                            mediaPlayer.start();
//                                        txt_to_speech(Util_functions.getSubCategoryValueLower(mNxRisk.m_TAlert.m_iNxAlertValue) + ". Slow Down");
                        }
                        // txt_to_speech(Util_functions.getSubCategoryValueLower(mNxRisk.m_TAlert.m_iNxAlertValue)+". Slow Down");
                        setGpsDataAPI(gpsEditDeviceReq);
                    }
                    alertId++;
                }
            }
            alertDuration = 0;
            alertKm = 0;
            alertZipcode = "";


            //
        } else {
            //Constants.alert_show=0;
            Constants.alertId = 0;
            alertValue = 0;
            //setAlertImageAlone(getAlertImageString(0),alert_img);
            alert_img.setImageResource(0);

        }
        if (alert_show!=0){
            if (mRiskAddData!=null) {
                if (!mRiskAddData.getDIST2POI().getString().contains("-") && !mRiskAddData.getDIST2POI().getString().equals("m") && !mRiskAddData.getDIST2POI().getString().contains("nan")) {
                    if (Math.round(mRiskAddData.getDIST2POI().get_value()) <= 250) {
                        speed_progress_animated_1.setProgress(Math.round(mRiskAddData.getDIST2POI().get_value()));
                        text_dist2poi.setText(mRiskAddData.getDIST2POI().getString());
                    }else if(Math.round(mRiskAddData.getDIST2POI().get_value()) == 250){
                        alert_show=mNxRisk.m_TAlert.m_iNxAlertValue;
                        if (Math.round(mNxRisk.m_fRisk * 100) > 90) {
                            currentTrack=0;
                            Constants.tracks[0] = R.raw.danger;
                            Constants.tracks[1] = getApplicationContext().getResources().getIdentifier(alert, "raw", getApplicationContext().getPackageName());
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), Constants.tracks[0]);
                            mediaPlayer.setOnCompletionListener(RoadStartPageActivity.this);
                            mediaPlayer.start();
//                                        txt_to_speech("Caution" + Util_functions.getSubCategoryValueLower(mNxRisk.m_TAlert.m_iNxAlertValue) + ". Slow Down");
                        } else if (Math.round(mNxRisk.m_fRisk * 100)>50&&Math.round(mNxRisk.m_fRisk * 100)<90){
                            currentTrack=0;
                            Constants.tracks[0] =getApplicationContext().getResources().getIdentifier(alert, "raw", getApplicationContext().getPackageName());
                            Constants.tracks[1] = R.raw.slow_down;
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), Constants.tracks[0]);
                            mediaPlayer.setOnCompletionListener(RoadStartPageActivity.this);
                            mediaPlayer.start();
//                                        txt_to_speech(Util_functions.getSubCategoryValueLower(mNxRisk.m_TAlert.m_iNxAlertValue) + ". Slow Down");
                        }
                    }
                }else {
                    speed_progress_animated_1.setProgress(0);
                    text_dist2poi.setText("0 m");
                    alert_show=0;
                }
            }else {
                speed_progress_animated_1.setProgress(0);
                text_dist2poi.setText("0 m");
                alert_show=0;
            }
        }else if (alert_show!=mNxRisk.m_TAlert.m_iNxAlertValue){
            alert_show=0;
        }else {
            speed_progress_animated_1.setProgress(0);
            text_dist2poi.setText("0 m");
            alert_show=0;
        }





    }


    private void switch_state() {
        switch(state){
            case CNxRisk.RISK_STOPPED:
                if (autoStart==false) {

                    if (autoStart == false){
                        if (stopCount == 0){
                            stopCount++;
                            stopDuration = getUniTime();
                        }

                        if (stopCount>0){
                            try
                            {
                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
                                String call_endtime = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm:ss aa", new Date(Long.parseLong(String.valueOf(getUniTime())))).toString();
                                String call_starttime=android.text.format.DateFormat.format("dd/MM/yyyy hh:mm:ss aa", new Date(Long.parseLong(String.valueOf(stopDuration)))).toString();
                                Date date1 = format.parse(call_starttime);
                                Date date2 = format.parse(call_endtime);
                                long mills = date2.getTime() - date1.getTime();
                                Log.v("Data1", ""+date1.getTime());
                                Log.v("Data2", ""+date2.getTime());
                                hours = (int) (mills/(1000 * 60 * 60));
                                int mins = (int) (mills/(1000*60)) % 60;
                                int sec = (int) (mills/1000) % 60;
                                diff_dur = mins+"."+sec; // updated value every1 second

                                if (Double.valueOf(diff_dur) >= 5.0&&sCount>=1&&Constants.horiz_count!=0){
                                    sCount=0;
                                    mconnectStatus = true;
                                    //autoStart=true;
                                    tripStatus="- Ride Stopped!";
                                    txt_view_road_stop.performClick();
                                    txt_view_road_stop.setClickable(false);
                                /*img_road_green.setVisibility(View.VISIBLE);
                                //Toast.makeText(RoadStartPageActivity.this, "STOP_PROLOG " + mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                                txt_view_road_start.setVisibility(View.VISIBLE);
                                txt_start.setText("START");
                                txt_view_road_stop.setVisibility(View.INVISIBLE);
                                //Toast.makeText(RoadStartPageActivity.this, "Start text changed", Toast.LENGTH_SHORT).show();
                                txt_view_road_start.setClickable(false);
                                if (Util_functions.ConnectivityCheck(RoadStartPageActivity.this, RoadStartPageActivity.this) && GPS_deviceID != "") {
                                    mconnectStatus = true;
                                    sCount=0;
                                    mstopStatus = true;
                                    stopDrive();
                                }*/
                                }else if (Double.valueOf(diff_dur) >= 5.0&&sCount>=1&&Constants.horiz_count==0){
                                    sCount=0;
                                    mconnectStatus = true;
                                    //autoStart=true;
                                    tripStatus="- Ride Stopped!";
                                    txt_view_road_stop.performClick();
                                    txt_view_road_stop.setClickable(false);
                                /*img_road_green.setVisibility(View.VISIBLE);
                                //Toast.makeText(RoadStartPageActivity.this, "STOP_PROLOG " + mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                                txt_view_road_start.setVisibility(View.VISIBLE);
                                txt_start.setText("START");
                                txt_view_road_stop.setVisibility(View.INVISIBLE);
                                //Toast.makeText(RoadStartPageActivity.this, "Start text changed", Toast.LENGTH_SHORT).show();
                                txt_view_road_start.setClickable(false);
                                if (Util_functions.ConnectivityCheck(RoadStartPageActivity.this, RoadStartPageActivity.this) && GPS_deviceID != "") {
                                    mconnectStatus = true;
                                    sCount=0;
                                    mstopStatus = true;
                                    stopDrive();
                                }*/
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }

                    img_road_green.setVisibility(View.VISIBLE);
                    img_view_road_green_pause.setVisibility(View.INVISIBLE);
                }
                //   Toast.makeText(RoadStartPageActivity.this, "Risk is Stopped", Toast.LENGTH_SHORT).show();
                break;
            case CNxRisk.RISK_AVAILABLE:
                if (autoStart==false) {


                    Constants.stateId = 1;
                    // animLoader();
                   /* mJniFunction.SetTreshMin(10);
                    mJniFunction.SetTreshMax(50);
                    mJniFunction.SetSpeedAlertSensitivity(1);*/
                    if (Preference_Details.getCoPilotVal() < 90) {
                        //mJniFunction.SetTreshMin(Preference_Details.getCoPilotVal());
                        mJniFunction.SetTreshMin(0);
                        mJniFunction.SetTreshMax(90);
                    } else {
                        mJniFunction.SetTreshMin(90);
                        mJniFunction.SetTreshMax(90);
                    }
                    /*img_road_green.setVisibility(View.VISIBLE);
                    img_view_road_green_gear.setVisibility(View.INVISIBLE);
                    img_view_road_green_pause.setVisibility(View.INVISIBLE);*/
                    // img_road_green.setVisibility(View.VISIBLE);
                    /*text.setVisibility(View.VISIBLE);
                    text.setTextColor(getColor(R.color.white));
                    text.setText(String.valueOf(mNxRisk.m_TAlert.m_iNxAlertValue)+" "+String.valueOf(mNxRisk.m_TAlert.m_sTextToSpeech)+"\n"+ speedLimit_count);
                    text1.setVisibility(View.VISIBLE);
                    text1.setTextColor(getColor(R.color.white));
                    text1.setText(String.valueOf(Math.round(mNxRisk.m_fRisk * 100))+" "+String.valueOf(mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel)+" "+String.valueOf(Math.round(Double.valueOf(speedd)*3.6)));*/
                    //previous_speed = current_speed;
                    //current_speed = speedd*3.6;
                    //mNxRisk.m_TAlert.m_iNxAlertValue =  getRandomNumber(35,26);
                    // text_eg.setText(String.valueOf(mNxRisk.m_TAlert.m_iNxAlertValue)+"  "+String.valueOf(alertValue)+"  "+alertId+"  "+Constants.apicount);
                    CNxFullStat[] FullStatt = mJniFunction.GetCloudStat();
                    if (FullStatt.length > 0) {
                        for (int i = 0; i < FullStatt.length; i++) {
                            kmDistance += FullStatt[i].m_fDistance / 1000;
                            /*text.setVisibility(View.VISIBLE);
                            text.setTextColor(getColor(R.color.white));
                            text.setText(String.valueOf(kmDistance));*/
                            if (!FullStatt[i].m_sZipCode.equals("99999")) {
                                zipcode = FullStatt[i].m_sZipCode;
                            }
                        }
                        // text.setVisibility(View.VISIBLE);
                        DecimalFormat REAL_FORMATTER = new DecimalFormat("0.0");
                        kmDistance = Double.valueOf(REAL_FORMATTER.format(kmDistance));
                        double remain = kmDistance % 0.2;
                        remain = Double.parseDouble(REAL_FORMATTER.format(remain));
                        curr_dist = kmDistance;
                        if (Math.round(mNxRisk.m_fRisk * 100)>50){
                            risk++;
                            //Toast.makeText(RoadStartPageActivity.this, String.valueOf(risk), Toast.LENGTH_SHORT).show();
                            if (risk<=3) {
                                if (Math.round(mNxRisk.m_fRisk * 100)==pre_risk){
                                    // Toast.makeText(RoadStartPageActivity.this, String.valueOf(Math.round(mNxRisk.m_fRisk * 100)), Toast.LENGTH_SHORT).show();
                                    if (kmDistance>=ii) {
                                        //  Toast.makeText(RoadStartPageActivity.this, String.valueOf(risk)+String.valueOf(ii), Toast.LENGTH_SHORT).show();
                                        pre_risk = Math.round(mNxRisk.m_fRisk * 100);
                                        GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
                                        gpsEditDeviceReq.setCategory(getString(R.string.gps_DISTANCE_DATA));
                                        GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
                                        deviceField.setAlert("");
                                        deviceField.setAnticipation("");
                                        deviceField.setDrivingScore("");
                                        deviceField.setDrivingSkill("");
                                        deviceField.setLatitude(String.valueOf(lat));
                                        deviceField.setDrivingSkill("");
                                        deviceField.setKiloMeter(String.valueOf(kmDistance));
                                        deviceField.setLongitude(String.valueOf(longi));
                                        deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                                        deviceField.setSpeed(String.valueOf(speedd * 3.6));
                                        deviceField.setTotalKiloMeter("");
                                        deviceField.setSelfConfidence("");
                                        deviceField.setTravelTime("");
                                        deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
                                        //deviceField.setGpsCount(String.valueOf(totalSnr));
                                        deviceField.setAccelerX(String.valueOf(Accx));
                                        deviceField.setAccelerY(String.valueOf(Accy));
                                        deviceField.setAccelerZ(String.valueOf(Accz));
                                        deviceField.setSenRotate(String.valueOf(currentDegree));
                                        deviceField.setZipCode(zipcode);
                                        deviceField.setAlertId("");
                                        deviceField.setAlertValue("");
                                        deviceField.setAlertTime("");
                                        deviceField.setRideDate("");
                                        deviceField.setRideTime(String.valueOf(getUniTime()));
                                        deviceField.setGyroscopeX(String.valueOf(gyroX));
                                        deviceField.setGyroscopeY(String.valueOf(gyroY));
                                        deviceField.setGyroscopeZ(String.valueOf(gyroZ));
                                        deviceField.setMagnatometerX(String.valueOf(magX));
                                        deviceField.setMagnatometerY(String.valueOf(magY));
                                        deviceField.setMagnatometerZ(String.valueOf(magZ));
                                        deviceField.setAlertName("");
                                        deviceField.setEhorizonLength(String.valueOf(FullStatt.length));
                                        deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
                                        deviceField.setEngineState(String.valueOf(state));
                                        gpsEditDeviceReq.setDeviceDataField(deviceField);
                                        gpsEditDeviceReq.setCompanyId(Constants.companyID);
                                        gpsEditDeviceReq.setDivisionId(Constants.divisionId);
                                        gpsEditDeviceReq.setModuleId(Constants.moduleID);
                                       /* gpsEditDeviceReq.setPropertyId(Constants.propID);
                                        gpsEditDeviceReq.setPortionId(Constants.porID);
                                        gpsEditDeviceReq.setSectionId(Constants.secID);*/
                                        gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
                                        gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
                                        gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
                                        gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
                                        gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
                                        gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
                                        gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
                                        GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
                                        deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
                                        deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
                                        deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
                                        deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
                                        deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
                                        gpsEditDeviceReq.setDeviceDataField1(deviceField1);
                                        if(Constants.subType.contains("BACKGROUND")) {
                                            gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
                                        }else {
                                            gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
                                        }
                                        Log.d("distance_log", gpsEditDeviceReq.toString());
                                        ii=ii+0.1;
                                        if (!Constants.GPS_deviceID.isEmpty() && !Constants.GPS_deviceID.equals("")) {
                                            setGpsDataAPI(gpsEditDeviceReq);
                                        }
                                        setstartDet();

                                    }
                                }
                                else{
                                    if (risk==1){
                                        ii=kmDistance;
                                    }
                                    if (kmDistance>=ii) {
                                        pre_risk = Math.round(mNxRisk.m_fRisk * 100);
                                        GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
                                        gpsEditDeviceReq.setCategory(getString(R.string.gps_DISTANCE_DATA));
                                        GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
                                        deviceField.setAlert("");
                                        deviceField.setAnticipation("");
                                        deviceField.setDrivingScore("");
                                        deviceField.setDrivingSkill("");
                                        deviceField.setLatitude(String.valueOf(lat));
                                        deviceField.setDrivingSkill("");
                                        deviceField.setKiloMeter(String.valueOf(kmDistance));
                                        deviceField.setLongitude(String.valueOf(longi));
                                        deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                                        deviceField.setSpeed(String.valueOf(speedd * 3.6));
                                        deviceField.setTotalKiloMeter("");
                                        deviceField.setSelfConfidence("");
                                        deviceField.setTravelTime("");
                                        deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
                                        //deviceField.setGpsCount(String.valueOf(totalSnr));
                                        deviceField.setAccelerX(String.valueOf(Accx));
                                        deviceField.setAccelerY(String.valueOf(Accy));
                                        deviceField.setAccelerZ(String.valueOf(Accz));
                                        deviceField.setSenRotate(String.valueOf(currentDegree));
                                        deviceField.setZipCode(zipcode);
                                        deviceField.setAlertId("");
                                        deviceField.setAlertValue("");
                                        deviceField.setAlertTime("");
                                        deviceField.setRideDate("");
                                        deviceField.setRideTime(String.valueOf(getUniTime()));
                                        deviceField.setGyroscopeX(String.valueOf(gyroX));
                                        deviceField.setGyroscopeY(String.valueOf(gyroY));
                                        deviceField.setGyroscopeZ(String.valueOf(gyroZ));
                                        deviceField.setMagnatometerX(String.valueOf(magX));
                                        deviceField.setMagnatometerY(String.valueOf(magY));
                                        deviceField.setMagnatometerZ(String.valueOf(magZ));
                                        deviceField.setAlertName("");
                                        deviceField.setEhorizonLength(String.valueOf(FullStatt.length));
                                        deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
                                        deviceField.setEngineState(String.valueOf(state));
                                        gpsEditDeviceReq.setDeviceDataField(deviceField);
                                        gpsEditDeviceReq.setCompanyId(Constants.companyID);
                                        gpsEditDeviceReq.setDivisionId(Constants.divisionId);
                                        gpsEditDeviceReq.setModuleId(Constants.moduleID);
                                       /* gpsEditDeviceReq.setPropertyId(Constants.propID);
                                        gpsEditDeviceReq.setPortionId(Constants.porID);
                                        gpsEditDeviceReq.setSectionId(Constants.secID);*/
                                        gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
                                        gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
                                        gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
                                        gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
                                        gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
                                        gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
                                        gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
                                        GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
                                        deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
                                        deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
                                        deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
                                        deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
                                        deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
                                        gpsEditDeviceReq.setDeviceDataField1(deviceField1);
                                        if(Constants.subType.contains("BACKGROUND")) {
                                            gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
                                        }else {
                                            gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
                                        }
                                        Log.d("distance_log", gpsEditDeviceReq.toString());
                                        ii=ii+0.1;
                                        if (!Constants.GPS_deviceID.isEmpty() && !Constants.GPS_deviceID.equals("")) {
                                            setGpsDataAPI(gpsEditDeviceReq);
                                        }
                                        setstartDet();
                                    }
                                    // Toast.makeText(RoadStartPageActivity.this, String.valueOf(risk)+String.valueOf(ii), Toast.LENGTH_SHORT).show();
                                }
                            }

                        }else{
                            risk=0;
                            pre_risk=0;
                            ii=0;

                            if (remain == 0.0 || remain == 0.2) {
                                // if (curr_dist - pre_dist == 0.0){
                                if (DistanceStatuus == false) {
                                    setstartDet();
                                    //weatherData(lat,longi);
                                    GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
                                    gpsEditDeviceReq.setCategory(getString(R.string.gps_DISTANCE_DATA));
                                    GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
                                    deviceField.setAlert("");
                                    deviceField.setAnticipation("");
                                    deviceField.setDrivingScore("");
                                    deviceField.setDrivingSkill("");
                                    deviceField.setLatitude(String.valueOf(lat));
                                    deviceField.setDrivingSkill("");
                                    deviceField.setKiloMeter(String.valueOf(kmDistance));
                                    deviceField.setLongitude(String.valueOf(longi));
                                    deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                                    deviceField.setSpeed(String.valueOf(speedd * 3.6));
                                    deviceField.setTotalKiloMeter("");
                                    deviceField.setSelfConfidence("");
                                    deviceField.setTravelTime("");
                                    deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
                                    //deviceField.setGpsCount(String.valueOf(totalSnr));
                                    deviceField.setAccelerX(String.valueOf(Accx));
                                    deviceField.setAccelerY(String.valueOf(Accy));
                                    deviceField.setAccelerZ(String.valueOf(Accz));
                                    deviceField.setSenRotate(String.valueOf(currentDegree));
                                    deviceField.setZipCode(zipcode);
                                    deviceField.setAlertId("");
                                    deviceField.setAlertValue("");
                                    deviceField.setAlertTime("");
                                    deviceField.setRideDate("");
                                    deviceField.setRideTime(String.valueOf(getUniTime()));
                                    deviceField.setGyroscopeX(String.valueOf(gyroX));
                                    deviceField.setGyroscopeY(String.valueOf(gyroY));
                                    deviceField.setGyroscopeZ(String.valueOf(gyroZ));
                                    deviceField.setMagnatometerX(String.valueOf(magX));
                                    deviceField.setMagnatometerY(String.valueOf(magY));
                                    deviceField.setMagnatometerZ(String.valueOf(magZ));
                                    deviceField.setAlertName("");
                                    deviceField.setEhorizonLength(String.valueOf(FullStatt.length));
                                    deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
                                    deviceField.setEngineState(String.valueOf(state));
                                    gpsEditDeviceReq.setDeviceDataField(deviceField);
                                    gpsEditDeviceReq.setCompanyId(Constants.companyID);
                                    gpsEditDeviceReq.setDivisionId(Constants.divisionId);
                                    gpsEditDeviceReq.setModuleId(Constants.moduleID);
                                /*gpsEditDeviceReq.setPropertyId(Constants.propID);
                                gpsEditDeviceReq.setPortionId(Constants.porID);
                                gpsEditDeviceReq.setSectionId(Constants.secID);*/
                                    gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
                                    gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
                                    gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
                                    gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
                                    gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
                                    gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
                                    gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
                                    GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
                                    deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
                                    deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
                                    deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
                                    deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
                                    deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
                                    gpsEditDeviceReq.setDeviceDataField1(deviceField1);
                                    DistanceStatuus = true;
                                    if(Constants.subType.contains("BACKGROUND")) {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
                                    }else {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
                                    }
                                    Log.d("distance_log", gpsEditDeviceReq.toString());
                                    if (!Constants.GPS_deviceID.isEmpty()&&!Constants.GPS_deviceID.equals("")){
                                        setGpsDataAPI(gpsEditDeviceReq);
                                    }

                                }

                            } else {
                                pre_dist = curr_dist;
                                DistanceStatuus = false;
                            }
                        }
                        kmDuration = 0.0;
                        kmDistance = 0.00;
                        zipcode = "";

                    }



                    /*text.setVisibility(View.VISIBLE);
                    text.setTextColor(getColor(R.color.white));
                    text.setText(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                    risk=Math.round(mNxRisk.m_fRisk * 100);
                    if (risk==pre_risk){
                        text1.setVisibility(View.VISIBLE);
                        text1.setTextColor(getColor(R.color.white));
                        text1.setText(String.valueOf(ii));
                        pre_risk=Math.round(mNxRisk.m_fRisk * 100);
                    }else {
                        text1.setVisibility(View.VISIBLE);
                        text1.setTextColor(getColor(R.color.white));
                        ii++;
                        text1.setText(String.valueOf(ii));
                        pre_risk=Math.round(mNxRisk.m_fRisk * 100);
                    }*/

                    if (Math.round(mNxRisk.m_fRisk * 100) <= 60) {   //Risk 70 - 90
                        // Toast.makeText(RoadStartPageActivity.this, "loader runing", Toast.LENGTH_SHORT).show();
                        animLoader();
                        //riskBarValue.setProgressDrawable(getDrawable(R.drawable.progress_green));
                        //card_color.setBackground(getDrawable(R.drawable.start_bg_corner_green));

                    } else if (Math.round(mNxRisk.m_fRisk * 100) > 60 && Math.round(mNxRisk.m_fRisk * 100) <= 90) {
                        // Toast.makeText(RoadStartPageActivity.this, ""+Math.round(mNxRisk.m_fRisk * 100), Toast.LENGTH_SHORT).show();
                        LowRiskAnimLoader();
                        //riskBarValue.setProgressDrawable(getDrawable(R.drawable.progress_amber));
                        //card_color.setBackground(getDrawable(R.drawable.start_bg_corner_amber));
                    } else if (Math.round(mNxRisk.m_fRisk * 100) > 90) {
                        RiskAnimLoader();
                        //riskBarValue.setProgressDrawable(getDrawable(R.drawable.progress_red));
                        //card_color.setBackground(getDrawable(R.drawable.start_bg_corner_red));
                    }
                    if (mNxRisk.m_TAlert.m_iVisualAlert == CNxRisk.CNxAlert.VISUAL_ALERT_1) {
                        //Toast.makeText(RoadStartPageActivity.this, "Risk is Low <70 speed", Toast.LENGTH_SHORT).show();
                    } else if (mNxRisk.m_TAlert.m_iVisualAlert == CNxRisk.CNxAlert.VISUAL_ALERT_2) {
                        if (Preference_Details.getBeep() == true) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD,100);
                                }
                            }, 500);
                        }

                        //Toast.makeText(RoadStartPageActivity.this, "Risk is Medium 70 - 90 speed", Toast.LENGTH_SHORT).show();
                    } else if (mNxRisk.m_TAlert.m_iVisualAlert == CNxRisk.CNxAlert.VISUAL_ALERT_3) {
                        if (Preference_Details.getBeep() == true) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //  beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD,100);
                                }
                            }, 500);

                        }
                        //Toast.makeText(RoadStartPageActivity.this, "Risk is High >90 speed", Toast.LENGTH_SHORT).show();
                    }

                    /* if (mNxRisk.m_TAlert.m_iTonesRiskAlert == 0) {
                     *//**Do SomeThing*//*
                }*/
                    // mNxRisk.m_iJourneyState  = 3;

                    if (mNxRisk.m_TAlert.m_iTonesRiskAlert == 1) {

                        if (Preference_Details.getBeep() == true) {
                            // beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD,200);
                        }
                    }

                    if (mNxRisk.m_TAlert.m_sTextToSpeech != "" || mNxRisk.m_TAlert.m_sTextToSpeech != null) {
                        if (alter_status == 0) {
                            // NxAlert_speech();
                        }
                    } else {
                        alter_status = 0;
                        //Toast.makeText(RoadStartPageActivity.this, "TTS null", Toast.LENGTH_SHORT).show();
                    }
                    if (mNxRisk.m_iJourneyState == 0) { //working
                        //Toast.makeText(RoadStartPageActivity.this, "state signaling that the journey started automatically"+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                        // startDrive();
        /*              if (Anim!=null) {
                        Anim.run();
                    }
                    mIsRunning = true;*/
                        img_view_road_green_pause.setImageResource(R.drawable.image_phone_rond_gris_pause_b01);
                        img_view_road_green_pause.setVisibility(View.INVISIBLE);
                        txt_view_road_start.setText("STOP");
                        /*img_view_road_start.setImageResource(R.drawable.stop_button);*/
                        img_road_green.setVisibility(View.VISIBLE);
                        upDateHMI();
                        //  mTimerHandler.postDelayed(mTimerRunnable, Constants.DEMO_FIRST_DELAY);
                    } else if (mNxRisk.m_iJourneyState == 1) {
                        img_view_road_green_pause.setImageResource(R.drawable.image_phone_rond_gris_pause_b01);
                        img_view_road_green_pause.setVisibility(View.INVISIBLE);
                        img_road_green.setVisibility(View.VISIBLE);
                        //Toast.makeText(RoadStartPageActivity.this, "state signaling that the journey started manually"+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                    } else if (mNxRisk.m_iJourneyState == 2) { //
                        //Toast.makeText(RoadStartPageActivity.this, "default state Journey stop"+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                        // stopDrive();
                        //   closeAPI();
                        mIsRunning = false;
                        upDateHMI();
                        if (Anim != null) {
                            if (Anim.isRunning()) {
                                Anim.stop();
                            }
                        }
                    } else if (mNxRisk.m_iJourneyState == 3) {
                        //   stopDrive();

                    }
                    if (mNxRisk.m_SpeedAlert.m_iSpeedLimitTone == 1) {
                        //Toast.makeText(RoadStartPageActivity.this, "" + mNxRisk.m_SpeedAlert.m_iSpeedLimitTone, Toast.LENGTH_SHORT).show();
                    }
                    switchText = "1.speed limit |" + mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel + " Start Detection | " + mNxRisk.m_bAutoStartDetection + " stop detection |" + mNxRisk.m_bAutoStopDetection + " Journey state |" + mNxRisk.m_iJourneyState + " Engine State " + mNxRisk.m_iSafetyNexEngineState;
                    //  Toast.makeText(getApplicationContext(), "1.speed limit | Start Detection | stop detection | Journey state | Engine State"+mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel+" "+mNxRisk.m_bAutoStartDetection+" "+mNxRisk.m_bAutoStopDetection+" "+mNxRisk.m_iJourneyState+" "+mNxRisk.m_iSafetyNexEngineState, Toast.LENGTH_SHORT).show();
                    if (mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel != -1 && speedd * 3.6 > mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel) {
                        Constants.horiz_count++;
                        if (mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel!=speedValue){
                            speedLimit_count=1;
                        }

                        if (Constants.alert_show==0) {
                            setAlertImageAlone(getAlertImageString(30), alert_img);
                        }

                        //    progress_km.setProgress(mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel);

                        if (mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel >= 10 && mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel < 15) {
                            img_view_road_green_center.setVisibility(View.VISIBLE);
                            img_view_road_green_center.setImageResource(R.drawable.v10b);
                            if (Constants.speedLimit_count <= 3) {
                                speedValue=mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel;
                                Constants.speedLimit_count++;
                                if (Preference_Details.getSpeedLimit() == true) {
                                    beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100);
                                }
                            }else if (speedLimit_count == 4) {
                                Constants.speedLimit_count++;
                                CNxFullStat[] FullStat = mJniFunction.GetCloudStat();
                                if (FullStat.length > 0) {
                                    for (int i = 0; i < FullStat.length; i++) {
                                        distanceKm += FullStat[i].m_fDistance / 1000;
                                        alertDuration += FullStat[i].m_fDuration /60;
                                        if (!FullStat[i].m_sZipCode.equals("99999")) {
                                            distanceZip = FullStat[i].m_sZipCode;
                                        }
                                    }
                                    if (Constants.alert_show==0) {
                                        setAlertImageAlone(getAlertImageString(30), alert_img);
                                    }


                                    GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
                                    gpsEditDeviceReq.setCategory(getString(R.string.gps_ALERT_DATA));
                                    gpsEditDeviceReq.setCompanyId(Constants.companyID);
                                    gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(30));
                                    GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
                                    deviceField.setAlert("10");
                                    deviceField.setAnticipation("");
                                    deviceField.setDrivingScore("");
                                    deviceField.setDrivingSkill("");
                                    deviceField.setLatitude(String.valueOf(lat));
                                    deviceField.setDrivingSkill("");
                                    deviceField.setKiloMeter("");
                                    deviceField.setLongitude(String.valueOf(longi));
                                    deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                                    deviceField.setSpeed(String.valueOf(speedd * 3.6));
                                    deviceField.setTotalKiloMeter("");
                                    deviceField.setSelfConfidence("");
                                    deviceField.setTravelTime(String.valueOf(alertDuration));
                                    deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
                                    //deviceField.setGpsCount(String.valueOf(totalSnr));
                                    deviceField.setAccelerX(String.valueOf(Accx));
                                    deviceField.setAccelerY(String.valueOf(Accy));
                                    deviceField.setAccelerZ(String.valueOf(Accz));
                                    deviceField.setSenRotate(String.valueOf(currentDegree));
                                    deviceField.setZipCode(distanceZip);
                                    deviceField.setRideTime(String.valueOf(getUniTime()));
                                    deviceField.setAlertId("");
                                    deviceField.setAlertValue("30");
                                    deviceField.setAlertKiloMeter(String.valueOf(distanceKm));
                                    deviceField.setAlertTime("");
                                    deviceField.setRideDate("");
                                    deviceField.setGyroscopeX(String.valueOf(gyroX));
                                    deviceField.setGyroscopeY(String.valueOf(gyroY));
                                    deviceField.setGyroscopeZ(String.valueOf(gyroZ));
                                    deviceField.setMagnatometerX(String.valueOf(magX));
                                    deviceField.setMagnatometerY(String.valueOf(magY));
                                    deviceField.setMagnatometerZ(String.valueOf(magZ));
                                    deviceField.setAlertName(Util_functions.getSubCategoryValueLowerr(30));
                                    deviceField.setEhorizonLength(String.valueOf(FullStatt.length));
                                    deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
                                    deviceField.setEngineState(String.valueOf(state));
                                    gpsEditDeviceReq.setDeviceDataField(deviceField);
                                    gpsEditDeviceReq.setDivisionId(Constants.divisionId);
                                    gpsEditDeviceReq.setModuleId(Constants.moduleID);
                                    /*gpsEditDeviceReq.setPropertyId(Constants.propID);
                                    gpsEditDeviceReq.setPortionId(Constants.porID);
                                    gpsEditDeviceReq.setSectionId(Constants.secID);*/
                                    gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
                                    gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
                                    gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
                                    gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
                                    gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
                                    gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
                                    gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
                                    GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
                                    deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
                                    deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
                                    deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
                                    deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
                                    deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
                                    gpsEditDeviceReq.setDeviceDataField1(deviceField1);
                                    if(Constants.subType.contains("BACKGROUND")) {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
                                    }else {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
                                    }
                                    Log.d("alert_log", gpsEditDeviceReq.toString());
                                    if (Util_functions.ConnectivityCheck(getApplicationContext(), RoadStartPageActivity.this)&&speedcount==0) {
                                        speedcount++;
                                        mconnectStatus = true;
                                        setGpsDataAPI(gpsEditDeviceReq);
                                    }
                                }
                                alertDuration = 0;
                                distanceKm = 0.0;
                                distanceZip = "";
                                 /*else if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count > 4) {
                                    speedLimitHandler = new Handler();
                                    speedLimitHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            speedLimitHandler.postDelayed(this, 2000);
                                            Constants.speedLimit_count++;
                                            if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count > 4) {
                                                txt_to_speech("Slow down");
                                            } else {
                                                speedLimitHandler.removeCallbacksAndMessages(null);
                                            }

                                        }
                                    }, 0000);


                                }*/
                            }
                        } else if (mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel >= 15 && mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel < 20) {
                            img_view_road_green_center.setVisibility(View.VISIBLE);
                            img_view_road_green_center.setImageResource(R.drawable.v15b);
                            if (Constants.speedLimit_count <= 3) {
                                Constants.speedLimit_count++;
                                speedValue=mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel;
                                if (Preference_Details.getSpeedLimit() == true) {
                                    beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100);
                                }
                            }else if (speedLimit_count == 4) {
                                Constants.speedLimit_count++;
                                CNxFullStat[] FullStat = mJniFunction.GetCloudStat();
                                if (FullStat.length > 0) {
                                    for (int i = 0; i < FullStat.length; i++) {
                                        distanceKm += FullStat[i].m_fDistance / 1000;
                                        alertDuration += FullStat[i].m_fDuration /60;
                                        if (!FullStat[i].m_sZipCode.equals("99999")) {
                                            distanceZip = FullStat[i].m_sZipCode;
                                        }
                                    }
                                    if (Constants.alert_show==0) {
                                        setAlertImageAlone(getAlertImageString(30), alert_img);
                                    }

                                    // text1.setText("Risk Value "+mNxRisk.m_fRisk   mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel);

                                    GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
                                    gpsEditDeviceReq.setCategory(getString(R.string.gps_ALERT_DATA));
                                    gpsEditDeviceReq.setCompanyId(Constants.companyID);
                                    gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(30));
                                    GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
                                    deviceField.setAlert("15");
                                    deviceField.setAnticipation("");
                                    deviceField.setDrivingScore("");
                                    deviceField.setDrivingSkill("");
                                    deviceField.setLatitude(String.valueOf(lat));
                                    deviceField.setDrivingSkill("");
                                    deviceField.setKiloMeter("");
                                    deviceField.setLongitude(String.valueOf(longi));
                                    deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                                    deviceField.setSpeed(String.valueOf(speedd * 3.6));
                                    deviceField.setTotalKiloMeter("");
                                    deviceField.setSelfConfidence("");
                                    deviceField.setTravelTime(String.valueOf(alertDuration));
                                    deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
                                    //deviceField.setGpsCount(String.valueOf(totalSnr));
                                    deviceField.setAccelerX(String.valueOf(Accx));
                                    deviceField.setAccelerY(String.valueOf(Accy));
                                    deviceField.setAccelerZ(String.valueOf(Accz));
                                    deviceField.setSenRotate(String.valueOf(currentDegree));
                                    deviceField.setZipCode(distanceZip);
                                    deviceField.setRideTime(String.valueOf(getUniTime()));
                                    deviceField.setAlertId("");
                                    deviceField.setAlertValue("30");
                                    deviceField.setAlertKiloMeter(String.valueOf(distanceKm));
                                    deviceField.setAlertTime("");
                                    deviceField.setRideDate("");
                                    deviceField.setGyroscopeX(String.valueOf(gyroX));
                                    deviceField.setGyroscopeY(String.valueOf(gyroY));
                                    deviceField.setGyroscopeZ(String.valueOf(gyroZ));
                                    deviceField.setMagnatometerX(String.valueOf(magX));
                                    deviceField.setMagnatometerY(String.valueOf(magY));
                                    deviceField.setMagnatometerZ(String.valueOf(magZ));
                                    deviceField.setAlertName(Util_functions.getSubCategoryValueLowerr(30));
                                    deviceField.setEhorizonLength(String.valueOf(FullStatt.length));
                                    deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
                                    deviceField.setEngineState(String.valueOf(state));
                                    gpsEditDeviceReq.setDeviceDataField(deviceField);
                                    gpsEditDeviceReq.setDivisionId(Constants.divisionId);
                                    gpsEditDeviceReq.setModuleId(Constants.moduleID);
                                    /*gpsEditDeviceReq.setPropertyId(Constants.propID);
                                    gpsEditDeviceReq.setPortionId(Constants.porID);
                                    gpsEditDeviceReq.setSectionId(Constants.secID);*/
                                    gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
                                    gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
                                    gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
                                    gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
                                    gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
                                    gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
                                    gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
                                    GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
                                    deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
                                    deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
                                    deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
                                    deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
                                    deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
                                    gpsEditDeviceReq.setDeviceDataField1(deviceField1);
                                    if(Constants.subType.contains("BACKGROUND")) {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
                                    }else {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
                                    }
                                    Log.d("alert_log", gpsEditDeviceReq.toString());
                                    if (Util_functions.ConnectivityCheck(getApplicationContext(), RoadStartPageActivity.this)&&speedcount==0) {
                                        speedcount++;
                                        mconnectStatus = true;
                                        setGpsDataAPI(gpsEditDeviceReq);
                                    }
                                }
                                alertDuration = 0;
                                distanceZip = "";
                                distanceKm = 0.0;
                                 /*else if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count > 4) {
                                    speedLimitHandler = new Handler();
                                    speedLimitHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            speedLimitHandler.postDelayed(this, 2000);
                                            Constants.speedLimit_count++;
                                            if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count > 4) {
                                                txt_to_speech("Slow down");
                                            } else {
                                                speedLimitHandler.removeCallbacksAndMessages(null);
                                            }

                                        }
                                    }, 0000);

                                }*/

                            }

                        } else if (mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel >= 20 && mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel < 25) {
                            img_view_road_green_center.setVisibility(View.VISIBLE);
                            img_view_road_green_center.setImageResource(R.drawable.v20b);

                            if (Constants.speedLimit_count <= 3) {
                                Constants.speedLimit_count++;
                                speedValue=mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel;
                                if (Preference_Details.getSpeedLimit() == true) {
                                    beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100);
                                }
                            }else if (speedLimit_count == 4) {
                                Constants.speedLimit_count++;
                                CNxFullStat[] FullStat = mJniFunction.GetCloudStat();
                                if (FullStat.length > 0) {
                                    for (int i = 0; i < FullStat.length; i++)
                                    {
                                        distanceKm += FullStat[i].m_fDistance / 1000;
                                        alertDuration += FullStat[i].m_fDuration /60;
                                        if (!FullStat[i].m_sZipCode.equals("99999")) {
                                            distanceZip = FullStat[i].m_sZipCode;
                                        }
                                    }
                                    if (Constants.alert_show==0) {
                                        setAlertImageAlone(getAlertImageString(30), alert_img);
                                    }


                                    GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
                                    gpsEditDeviceReq.setCategory(getString(R.string.gps_ALERT_DATA));
                                    gpsEditDeviceReq.setCompanyId(Constants.companyID);
                                    gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(30));
                                    GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
                                    deviceField.setAlert("20");
                                    deviceField.setAnticipation("");
                                    deviceField.setDrivingScore("");
                                    deviceField.setDrivingSkill("");
                                    deviceField.setLatitude(String.valueOf(lat));
                                    deviceField.setDrivingSkill("");
                                    deviceField.setKiloMeter("");
                                    deviceField.setLongitude(String.valueOf(longi));
                                    deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                                    deviceField.setSpeed(String.valueOf(speedd * 3.6));
                                    deviceField.setTotalKiloMeter("");
                                    deviceField.setSelfConfidence("");
                                    deviceField.setTravelTime(String.valueOf(alertDuration));
                                    deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
                                    //deviceField.setGpsCount(String.valueOf(totalSnr));
                                    deviceField.setAccelerX(String.valueOf(Accx));
                                    deviceField.setAccelerY(String.valueOf(Accy));
                                    deviceField.setAccelerZ(String.valueOf(Accz));
                                    deviceField.setSenRotate(String.valueOf(currentDegree));
                                    deviceField.setZipCode(distanceZip);
                                    deviceField.setRideTime(String.valueOf(getUniTime()));
                                    deviceField.setAlertId("");
                                    deviceField.setAlertValue("30");
                                    deviceField.setAlertKiloMeter(String.valueOf(distanceKm));
                                    deviceField.setAlertTime("");
                                    deviceField.setRideDate("");
                                    deviceField.setGyroscopeX(String.valueOf(gyroX));
                                    deviceField.setGyroscopeY(String.valueOf(gyroY));
                                    deviceField.setGyroscopeZ(String.valueOf(gyroZ));
                                    deviceField.setMagnatometerX(String.valueOf(magX));
                                    deviceField.setMagnatometerY(String.valueOf(magY));
                                    deviceField.setMagnatometerZ(String.valueOf(magZ));
                                    deviceField.setAlertName(Util_functions.getSubCategoryValueLowerr(30));
                                    deviceField.setEhorizonLength(String.valueOf(FullStatt.length));
                                    deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
                                    deviceField.setEngineState(String.valueOf(state));
                                    gpsEditDeviceReq.setDeviceDataField(deviceField);
                                    gpsEditDeviceReq.setDivisionId(Constants.divisionId);
                                    gpsEditDeviceReq.setModuleId(Constants.moduleID);
                                    /*gpsEditDeviceReq.setPropertyId(Constants.propID);
                                    gpsEditDeviceReq.setPortionId(Constants.porID);
                                    gpsEditDeviceReq.setSectionId(Constants.secID);*/
                                    gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
                                    gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
                                    gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
                                    gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
                                    gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
                                    gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
                                    gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
                                    GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
                                    deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
                                    deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
                                    deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
                                    deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
                                    deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
                                    gpsEditDeviceReq.setDeviceDataField1(deviceField1);
                                    if(Constants.subType.contains("BACKGROUND")) {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
                                    }else {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
                                    }
                                    Log.d("alert_log", gpsEditDeviceReq.toString());
                                    if (Util_functions.ConnectivityCheck(getApplicationContext(), RoadStartPageActivity.this)&&speedcount==0) {
                                        speedcount++;
                                        mconnectStatus = true;
                                        setGpsDataAPI(gpsEditDeviceReq);
                                    }
                                }
                                alertDuration = 0;
                                distanceZip = "";
                                distanceKm = 0.0;
                                 /*else if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count > 4) {
                                    speedLimitHandler = new Handler();
                                    speedLimitHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            speedLimitHandler.postDelayed(this, 2000);
                                            Constants.speedLimit_count++;
                                            if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count > 4) {
                                                txt_to_speech("Slow down");
                                            } else {
                                                speedLimitHandler.removeCallbacksAndMessages(null);
                                            }

                                        }
                                    }, 0000);

                                }*/
                            }

                        } else if (mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel >= 25 && mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel < 30) {
                            img_view_road_green_center.setVisibility(View.VISIBLE);
                            img_view_road_green_center.setImageResource(R.drawable.v25b);
                            if (Constants.speedLimit_count <= 3) {
                                Constants.speedLimit_count++;
                                speedValue=mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel;
                                if (Preference_Details.getSpeedLimit() == true) {
                                    beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100);
                                }
                            }else if (speedLimit_count == 4) {
                                Constants.speedLimit_count++;
                                CNxFullStat[] FullStat = mJniFunction.GetCloudStat();
                                if (FullStat.length > 0) {
                                    for (int i = 0; i < FullStat.length; i++) {
                                        distanceKm += FullStat[i].m_fDistance / 1000;
                                        alertDuration += FullStat[i].m_fDuration /60;
                                        if (!FullStat[i].m_sZipCode.equals("99999")) {
                                            distanceZip = FullStat[i].m_sZipCode;
                                        }
                                    }

                                    if (Constants.alert_show==0) {
                                        setAlertImageAlone(getAlertImageString(30), alert_img);
                                    }


                                    GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
                                    gpsEditDeviceReq.setCategory(getString(R.string.gps_ALERT_DATA));
                                    gpsEditDeviceReq.setCompanyId(Constants.companyID);
                                    gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(30));
                                    GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
                                    deviceField.setAlert("25");
                                    deviceField.setAnticipation("");
                                    deviceField.setDrivingScore("");
                                    deviceField.setDrivingSkill("");
                                    deviceField.setLatitude(String.valueOf(lat));
                                    deviceField.setDrivingSkill("");
                                    deviceField.setKiloMeter("");
                                    deviceField.setLongitude(String.valueOf(longi));
                                    deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                                    deviceField.setSpeed(String.valueOf(speedd * 3.6));
                                    deviceField.setTotalKiloMeter("");
                                    deviceField.setSelfConfidence("");
                                    deviceField.setTravelTime(String.valueOf(alertDuration));
                                    deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
                                    //deviceField.setGpsCount(String.valueOf(totalSnr));
                                    deviceField.setAccelerX(String.valueOf(Accx));
                                    deviceField.setAccelerY(String.valueOf(Accy));
                                    deviceField.setAccelerZ(String.valueOf(Accz));
                                    deviceField.setSenRotate(String.valueOf(currentDegree));
                                    deviceField.setZipCode(distanceZip);
                                    deviceField.setRideTime(String.valueOf(getUniTime()));
                                    deviceField.setAlertId("");
                                    deviceField.setAlertValue("30");
                                    deviceField.setAlertKiloMeter(String.valueOf(distanceKm));
                                    deviceField.setAlertTime("");
                                    deviceField.setRideDate("");
                                    deviceField.setGyroscopeX(String.valueOf(gyroX));
                                    deviceField.setGyroscopeY(String.valueOf(gyroY));
                                    deviceField.setGyroscopeZ(String.valueOf(gyroZ));
                                    deviceField.setMagnatometerX(String.valueOf(magX));
                                    deviceField.setMagnatometerY(String.valueOf(magY));
                                    deviceField.setMagnatometerZ(String.valueOf(magZ));
                                    deviceField.setAlertName(Util_functions.getSubCategoryValueLowerr(30));
                                    deviceField.setEhorizonLength(String.valueOf(FullStatt.length));
                                    deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
                                    deviceField.setEngineState(String.valueOf(state));
                                    gpsEditDeviceReq.setDeviceDataField(deviceField);
                                    gpsEditDeviceReq.setDivisionId(Constants.divisionId);
                                    gpsEditDeviceReq.setModuleId(Constants.moduleID);
                                    /*gpsEditDeviceReq.setPropertyId(Constants.propID);
                                    gpsEditDeviceReq.setPortionId(Constants.porID);
                                    gpsEditDeviceReq.setSectionId(Constants.secID);*/
                                    gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
                                    gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
                                    gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
                                    gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
                                    gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
                                    gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
                                    gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
                                    GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
                                    deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
                                    deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
                                    deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
                                    deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
                                    deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
                                    gpsEditDeviceReq.setDeviceDataField1(deviceField1);
                                    if(Constants.subType.contains("BACKGROUND")) {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
                                    }else {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
                                    }
                                    Log.d("alert_log", gpsEditDeviceReq.toString());
                                    if (Util_functions.ConnectivityCheck(getApplicationContext(), RoadStartPageActivity.this)&&speedcount==0) {
                                        speedcount++;
                                        mconnectStatus = true;
                                        setGpsDataAPI(gpsEditDeviceReq);
                                    }
                                }
                                alertDuration=0;
                                distanceZip = "";
                                distanceKm = 0.0;
                                 /*else if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count > 4) {
                                    speedLimitHandler = new Handler();
                                    speedLimitHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            speedLimitHandler.postDelayed(this, 2000);
                                            Constants.speedLimit_count++;
                                            if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count > 4) {
                                                txt_to_speech("Slow down");
                                            } else {
                                                speedLimitHandler.removeCallbacksAndMessages(null);
                                            }

                                        }
                                    }, 0000);

                                }*/

                            }

                        } else if (mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel >= 30 && mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel < 35) {
                            img_view_road_green_center.setVisibility(View.VISIBLE);
                            img_view_road_green_center.setImageResource(R.drawable.v30b);

                            if (Constants.speedLimit_count <= 3) {
                                Constants.speedLimit_count++;
                                speedValue=mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel;
                                if (Preference_Details.getSpeedLimit() == true) {
                                    beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100);
                                }
                            }else if (speedLimit_count == 4) {
                                Constants.speedLimit_count++;
                                CNxFullStat[] FullStat = mJniFunction.GetCloudStat();
                                if (FullStat.length > 0) {
                                    for (int i = 0; i < FullStat.length; i++) {
                                        distanceKm += FullStat[i].m_fDistance / 1000;
                                        alertDuration += FullStat[i].m_fDuration /60;
                                        if (!FullStat[i].m_sZipCode.equals("99999")) {
                                            distanceZip = FullStat[i].m_sZipCode;
                                        }
                                    }
                                    if (Constants.alert_show==0) {
                                        setAlertImageAlone(getAlertImageString(30), alert_img);
                                    }

                                    GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
                                    gpsEditDeviceReq.setCategory(getString(R.string.gps_ALERT_DATA));
                                    gpsEditDeviceReq.setCompanyId(Constants.companyID);
                                    gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(30));
                                    GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
                                    deviceField.setAlert("30");
                                    deviceField.setAnticipation("");
                                    deviceField.setDrivingScore("");
                                    deviceField.setDrivingSkill("");
                                    deviceField.setLatitude(String.valueOf(lat));
                                    deviceField.setDrivingSkill("");
                                    deviceField.setKiloMeter("");
                                    deviceField.setLongitude(String.valueOf(longi));
                                    deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                                    deviceField.setSpeed(String.valueOf(speedd * 3.6));
                                    deviceField.setTotalKiloMeter("");
                                    deviceField.setSelfConfidence("");
                                    deviceField.setTravelTime(String.valueOf(alertDuration));
                                    deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
                                    //deviceField.setGpsCount(String.valueOf(totalSnr));
                                    deviceField.setAccelerX(String.valueOf(Accx));
                                    deviceField.setAccelerY(String.valueOf(Accy));
                                    deviceField.setAccelerZ(String.valueOf(Accz));
                                    deviceField.setSenRotate(String.valueOf(currentDegree));
                                    deviceField.setZipCode(distanceZip);
                                    deviceField.setRideTime(String.valueOf(getUniTime()));
                                    deviceField.setAlertId("");
                                    deviceField.setAlertValue("30");
                                    deviceField.setAlertKiloMeter(String.valueOf(distanceKm));
                                    deviceField.setAlertTime("");
                                    deviceField.setRideDate("");
                                    deviceField.setGyroscopeX(String.valueOf(gyroX));
                                    deviceField.setGyroscopeY(String.valueOf(gyroY));
                                    deviceField.setGyroscopeZ(String.valueOf(gyroZ));
                                    deviceField.setMagnatometerX(String.valueOf(magX));
                                    deviceField.setMagnatometerY(String.valueOf(magY));
                                    deviceField.setMagnatometerZ(String.valueOf(magZ));
                                    deviceField.setAlertName(Util_functions.getSubCategoryValueLowerr(30));
                                    deviceField.setEhorizonLength(String.valueOf(FullStatt.length));
                                    deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
                                    deviceField.setEngineState(String.valueOf(state));
                                    gpsEditDeviceReq.setDeviceDataField(deviceField);
                                    gpsEditDeviceReq.setDivisionId(Constants.divisionId);
                                    gpsEditDeviceReq.setModuleId(Constants.moduleID);
                                    /*gpsEditDeviceReq.setPropertyId(Constants.propID);
                                    gpsEditDeviceReq.setPortionId(Constants.porID);
                                    gpsEditDeviceReq.setSectionId(Constants.secID);*/
                                    gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
                                    gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
                                    gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
                                    gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
                                    gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
                                    gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
                                    gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
                                    GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
                                    deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
                                    deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
                                    deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
                                    deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
                                    deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
                                    gpsEditDeviceReq.setDeviceDataField1(deviceField1);
                                    if(Constants.subType.contains("BACKGROUND")) {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
                                    }else {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
                                    }
                                    Log.d("alert_log", gpsEditDeviceReq.toString());
                                    if (Util_functions.ConnectivityCheck(getApplicationContext(), RoadStartPageActivity.this)&&speedcount==0) {
                                        speedcount++;
                                        mconnectStatus = true;
                                        setGpsDataAPI(gpsEditDeviceReq);
                                    }
                                }
                                distanceZip = "";
                                distanceKm = 0.0;
                                alertDuration=0;
                                 /*else if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count >= 4) {
                                    speedLimitHandler = new Handler();
                                    speedLimitHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            speedLimitHandler.postDelayed(this, 2000);
                                            Constants.speedLimit_count++;
                                            if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count > 4) {
                                                txt_to_speech("Slow down");
                                            } else {
                                                speedLimitHandler.removeCallbacksAndMessages(null);
                                            }

                                        }
                                    }, 0000);

                                }*/

                            }

                        } else if (mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel >= 35 && mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel < 40) {
                            img_view_road_green_center.setVisibility(View.VISIBLE);
                            img_view_road_green_center.setImageResource(R.drawable.v35b);
                            if (Constants.speedLimit_count <= 3) {
                                Constants.speedLimit_count++;
                                speedValue=mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel;
                                if (Preference_Details.getSpeedLimit() == true) {
                                    beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100);
                                }
                            }else if (speedLimit_count == 4) {
                                Constants.speedLimit_count++;
                                CNxFullStat[] FullStat = mJniFunction.GetCloudStat();
                                if (FullStat.length > 0) {
                                    for (int i = 0; i < FullStat.length; i++) {
                                        distanceKm += FullStat[i].m_fDistance / 1000;
                                        alertDuration += FullStat[i].m_fDuration /60;
                                        if (!FullStat[i].m_sZipCode.equals("99999")) {
                                            distanceZip = FullStat[i].m_sZipCode;
                                        }
                                    }

                                    if (Constants.alert_show==0) {
                                        setAlertImageAlone(getAlertImageString(30), alert_img);
                                    }

                                    GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
                                    gpsEditDeviceReq.setCategory(getString(R.string.gps_ALERT_DATA));
                                    gpsEditDeviceReq.setCompanyId(Constants.companyID);
                                    gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(30));
                                    GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
                                    deviceField.setAlert("35");
                                    deviceField.setAnticipation("");
                                    deviceField.setDrivingScore("");
                                    deviceField.setDrivingSkill("");
                                    deviceField.setLatitude(String.valueOf(lat));
                                    deviceField.setDrivingSkill("");
                                    deviceField.setKiloMeter("");
                                    deviceField.setLongitude(String.valueOf(longi));
                                    deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                                    deviceField.setSpeed(String.valueOf(speedd * 3.6));
                                    deviceField.setTotalKiloMeter("");
                                    deviceField.setSelfConfidence("");
                                    deviceField.setTravelTime(String.valueOf(alertDuration));
                                    deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
                                    //deviceField.setGpsCount(String.valueOf(totalSnr));
                                    deviceField.setAccelerX(String.valueOf(Accx));
                                    deviceField.setAccelerY(String.valueOf(Accy));
                                    deviceField.setAccelerZ(String.valueOf(Accz));
                                    deviceField.setSenRotate(String.valueOf(currentDegree));
                                    deviceField.setZipCode(distanceZip);
                                    deviceField.setRideTime(String.valueOf(getUniTime()));
                                    deviceField.setAlertId("");
                                    deviceField.setAlertValue("30");
                                    deviceField.setAlertKiloMeter(String.valueOf(distanceKm));
                                    deviceField.setAlertTime("");
                                    deviceField.setRideDate("");
                                    deviceField.setGyroscopeX(String.valueOf(gyroX));
                                    deviceField.setGyroscopeY(String.valueOf(gyroY));
                                    deviceField.setGyroscopeZ(String.valueOf(gyroZ));
                                    deviceField.setMagnatometerX(String.valueOf(magX));
                                    deviceField.setMagnatometerY(String.valueOf(magY));
                                    deviceField.setMagnatometerZ(String.valueOf(magZ));
                                    deviceField.setAlertName(Util_functions.getSubCategoryValueLowerr(30));
                                    deviceField.setEhorizonLength(String.valueOf(FullStatt.length));
                                    deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
                                    deviceField.setEngineState(String.valueOf(state));
                                    gpsEditDeviceReq.setDeviceDataField(deviceField);
                                    gpsEditDeviceReq.setDivisionId(Constants.divisionId);
                                    gpsEditDeviceReq.setModuleId(Constants.moduleID);
                                   /* gpsEditDeviceReq.setPropertyId(Constants.propID);
                                    gpsEditDeviceReq.setPortionId(Constants.porID);
                                    gpsEditDeviceReq.setSectionId(Constants.secID);*/
                                    gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
                                    gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
                                    gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
                                    gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
                                    gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
                                    gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
                                    gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
                                    GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
                                    deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
                                    deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
                                    deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
                                    deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
                                    deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
                                    gpsEditDeviceReq.setDeviceDataField1(deviceField1);
                                    if(Constants.subType.contains("BACKGROUND")) {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
                                    }else {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
                                    }
                                    Log.d("alert_log", gpsEditDeviceReq.toString());
                                    if (Util_functions.ConnectivityCheck(getApplicationContext(), RoadStartPageActivity.this)&&speedcount==0) {
                                        speedcount++;
                                        mconnectStatus = true;
                                        setGpsDataAPI(gpsEditDeviceReq);
                                    }
                                }
                                distanceZip = "";
                                distanceKm = 0.0;
                                alertDuration=0;
                                 /*else if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count >= 4) {
                                    speedLimitHandler = new Handler();
                                    speedLimitHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            speedLimitHandler.postDelayed(this, 2000);
                                            Constants.speedLimit_count++;
                                            if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count > 4) {
                                                txt_to_speech("Slow down");
                                            } else {
                                                speedLimitHandler.removeCallbacksAndMessages(null);
                                            }

                                        }
                                    }, 0000);

                                }*/
                            }

                        } else if (mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel >= 40 && mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel < 45) {
                            img_view_road_green_center.setVisibility(View.VISIBLE);
                            img_view_road_green_center.setImageResource(R.drawable.v40b);

                            if (Constants.speedLimit_count <= 3) {
                                Constants.speedLimit_count++;
                                speedValue=mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel;
                                if (Preference_Details.getSpeedLimit() == true) {
                                    beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100);
                                }
                            }else if (speedLimit_count == 4) {
                                Constants.speedLimit_count++;
                                CNxFullStat[] FullStat = mJniFunction.GetCloudStat();
                                if (FullStat.length > 0) {
                                    for (int i = 0; i < FullStat.length; i++) {
                                        distanceKm += FullStat[i].m_fDistance / 1000;
                                        alertDuration += FullStat[i].m_fDuration /60;
                                        if (!FullStat[i].m_sZipCode.equals("99999")) {
                                            distanceZip = FullStat[i].m_sZipCode;
                                        }
                                    }

                                    if (Constants.alert_show==0) {
                                        setAlertImageAlone(getAlertImageString(30), alert_img);
                                    }

                                    GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
                                    gpsEditDeviceReq.setCategory(getString(R.string.gps_ALERT_DATA));
                                    gpsEditDeviceReq.setCompanyId(Constants.companyID);
                                    gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(30));
                                    GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
                                    deviceField.setAlert("40");
                                    deviceField.setAnticipation("");
                                    deviceField.setDrivingScore("");
                                    deviceField.setDrivingSkill("");
                                    deviceField.setLatitude(String.valueOf(lat));
                                    deviceField.setDrivingSkill("");
                                    deviceField.setKiloMeter("");
                                    deviceField.setLongitude(String.valueOf(longi));
                                    deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                                    deviceField.setSpeed(String.valueOf(speedd * 3.6));
                                    deviceField.setTotalKiloMeter("");
                                    deviceField.setSelfConfidence("");
                                    deviceField.setTravelTime(String.valueOf(alertDuration));
                                    deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
                                    //deviceField.setGpsCount(String.valueOf(totalSnr));
                                    deviceField.setAccelerX(String.valueOf(Accx));
                                    deviceField.setAccelerY(String.valueOf(Accy));
                                    deviceField.setAccelerZ(String.valueOf(Accz));
                                    deviceField.setSenRotate(String.valueOf(currentDegree));
                                    deviceField.setZipCode(distanceZip);
                                    deviceField.setRideTime(String.valueOf(getUniTime()));
                                    deviceField.setAlertId("");
                                    deviceField.setAlertValue("30");
                                    deviceField.setAlertKiloMeter(String.valueOf(distanceKm));
                                    deviceField.setAlertTime("");
                                    deviceField.setRideDate("");
                                    deviceField.setGyroscopeX(String.valueOf(gyroX));
                                    deviceField.setGyroscopeY(String.valueOf(gyroY));
                                    deviceField.setGyroscopeZ(String.valueOf(gyroZ));
                                    deviceField.setMagnatometerX(String.valueOf(magX));
                                    deviceField.setMagnatometerY(String.valueOf(magY));
                                    deviceField.setMagnatometerZ(String.valueOf(magZ));
                                    deviceField.setAlertName(Util_functions.getSubCategoryValueLowerr(30));
                                    deviceField.setEhorizonLength(String.valueOf(FullStatt.length));
                                    deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
                                    deviceField.setEngineState(String.valueOf(state));
                                    gpsEditDeviceReq.setDeviceDataField(deviceField);
                                    gpsEditDeviceReq.setDivisionId(Constants.divisionId);
                                    gpsEditDeviceReq.setModuleId(Constants.moduleID);
                                    /*gpsEditDeviceReq.setPropertyId(Constants.propID);
                                    gpsEditDeviceReq.setPortionId(Constants.porID);
                                    gpsEditDeviceReq.setSectionId(Constants.secID);*/
                                    gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
                                    gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
                                    gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
                                    gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
                                    gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
                                    gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
                                    gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
                                    GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
                                    deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
                                    deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
                                    deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
                                    deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
                                    deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
                                    gpsEditDeviceReq.setDeviceDataField1(deviceField1);
                                    if(Constants.subType.contains("BACKGROUND")) {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
                                    }else {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
                                    }
                                    Log.d("alert_log", gpsEditDeviceReq.toString());
                                    if (Util_functions.ConnectivityCheck(getApplicationContext(), RoadStartPageActivity.this)&&speedcount==0) {
                                        speedcount++;
                                        mconnectStatus = true;
                                        setGpsDataAPI(gpsEditDeviceReq);
                                    }
                                }
                                distanceZip = "";
                                distanceKm = 0.0;
                                alertDuration=0;
                                 /*else if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count >= 4) {
                                    speedLimitHandler = new Handler();
                                    speedLimitHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            speedLimitHandler.postDelayed(this, 2000);
                                            Constants.speedLimit_count++;
                                            if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count > 4) {
                                                txt_to_speech("Slow down");
                                            } else {
                                                speedLimitHandler.removeCallbacksAndMessages(null);
                                            }

                                        }
                                    }, 0000);

                                }*/
                            }

                        } else if (mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel >= 45 && mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel < 50) {
                            img_view_road_green_center.setVisibility(View.VISIBLE);
                            img_view_road_green_center.setImageResource(R.drawable.v45b);


                            if (Constants.speedLimit_count <= 3) {
                                Constants.speedLimit_count++;
                                speedValue=mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel;
                                if (Preference_Details.getSpeedLimit() == true) {
                                    beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100);
                                }
                            }
                            else if (speedLimit_count == 4) {
                                Constants.speedLimit_count++;
                                CNxFullStat[] FullStat = mJniFunction.GetCloudStat();
                                if (FullStat.length > 0) {
                                    for (int i = 0; i < FullStat.length; i++) {
                                        distanceKm += FullStat[i].m_fDistance / 1000;
                                        alertDuration += FullStat[i].m_fDuration /60;
                                        if (!FullStat[i].m_sZipCode.equals("99999")) {
                                            distanceZip = FullStat[i].m_sZipCode;
                                        }
                                    }

                                    if (Constants.alert_show==0) {
                                        setAlertImageAlone(getAlertImageString(30), alert_img);
                                    }


                                    GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
                                    gpsEditDeviceReq.setCategory(getString(R.string.gps_ALERT_DATA));
                                    gpsEditDeviceReq.setCompanyId(Constants.companyID);
                                    gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(30));
                                    GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
                                    deviceField.setAlert("45");
                                    deviceField.setAnticipation("");
                                    deviceField.setDrivingScore("");
                                    deviceField.setDrivingSkill("");
                                    deviceField.setLatitude(String.valueOf(lat));
                                    deviceField.setDrivingSkill("");
                                    deviceField.setKiloMeter("");
                                    deviceField.setLongitude(String.valueOf(longi));
                                    deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                                    deviceField.setSpeed(String.valueOf(speedd * 3.6));
                                    deviceField.setTotalKiloMeter("");
                                    deviceField.setSelfConfidence("");
                                    deviceField.setTravelTime(String.valueOf(alertDuration));
                                    deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
                                    //deviceField.setGpsCount(String.valueOf(totalSnr));
                                    deviceField.setAccelerX(String.valueOf(Accx));
                                    deviceField.setAccelerY(String.valueOf(Accy));
                                    deviceField.setAccelerZ(String.valueOf(Accz));
                                    deviceField.setSenRotate(String.valueOf(currentDegree));
                                    deviceField.setZipCode(distanceZip);
                                    deviceField.setRideTime(String.valueOf(getUniTime()));
                                    deviceField.setAlertId("");
                                    deviceField.setAlertValue("30");
                                    deviceField.setAlertKiloMeter(String.valueOf(distanceKm));
                                    deviceField.setAlertTime("");
                                    deviceField.setRideDate("");
                                    deviceField.setGyroscopeX(String.valueOf(gyroX));
                                    deviceField.setGyroscopeY(String.valueOf(gyroY));
                                    deviceField.setGyroscopeZ(String.valueOf(gyroZ));
                                    deviceField.setMagnatometerX(String.valueOf(magX));
                                    deviceField.setMagnatometerY(String.valueOf(magY));
                                    deviceField.setMagnatometerZ(String.valueOf(magZ));
                                    deviceField.setAlertName(Util_functions.getSubCategoryValueLowerr(30));
                                    deviceField.setEhorizonLength(String.valueOf(FullStatt.length));
                                    deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
                                    deviceField.setEngineState(String.valueOf(state));
                                    gpsEditDeviceReq.setDeviceDataField(deviceField);
                                    gpsEditDeviceReq.setDivisionId(Constants.divisionId);
                                    gpsEditDeviceReq.setModuleId(Constants.moduleID);
                                    /*gpsEditDeviceReq.setPropertyId(Constants.propID);
                                    gpsEditDeviceReq.setPortionId(Constants.porID);
                                    gpsEditDeviceReq.setSectionId(Constants.secID);*/
                                    gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
                                    gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
                                    gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
                                    gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
                                    gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
                                    gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
                                    gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
                                    GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
                                    deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
                                    deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
                                    deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
                                    deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
                                    deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
                                    gpsEditDeviceReq.setDeviceDataField1(deviceField1);
                                    if(Constants.subType.contains("BACKGROUND")) {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
                                    }else {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
                                    }
                                    Log.d("alert_log", gpsEditDeviceReq.toString());
                                    if (Util_functions.ConnectivityCheck(getApplicationContext(), RoadStartPageActivity.this)&&speedcount==0) {
                                        speedcount++;
                                        mconnectStatus = true;
                                        setGpsDataAPI(gpsEditDeviceReq);
                                    }
                                }
                                distanceZip = "";
                                distanceKm = 0.0;
                                alertDuration=0;
                                 /*else if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count >= 4) {
                                    speedLimitHandler = new Handler();
                                    speedLimitHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            speedLimitHandler.postDelayed(this, 2000);
                                            Constants.speedLimit_count++;
                                            if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count > 4) {
                                                txt_to_speech("Slow down");
                                            } else {
                                                speedLimitHandler.removeCallbacksAndMessages(null);
                                            }

                                        }
                                    }, 0000);

                                }*/
                            }

                        } else if (mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel >= 50 && mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel < 55) {
                            img_view_road_green_center.setVisibility(View.VISIBLE);
                            img_view_road_green_center.setImageResource(R.drawable.v50b);
                            if (Constants.speedLimit_count <= 3) {
                                Constants.speedLimit_count++;
                                speedValue=mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel;
                                if (Preference_Details.getSpeedLimit() == true) {
                                    beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100);
                                }
                            }else if (speedLimit_count == 4) {
                                Constants.speedLimit_count++;
                                CNxFullStat[] FullStat = mJniFunction.GetCloudStat();
                                if (FullStat.length > 0) {
                                    for (int i = 0; i < FullStat.length; i++) {
                                        distanceKm += FullStat[i].m_fDistance / 1000;
                                        alertDuration += FullStat[i].m_fDuration /60;
                                        if (!FullStat[i].m_sZipCode.equals("99999")) {
                                            distanceZip = FullStat[i].m_sZipCode;
                                        }
                                    }
                                    if (Constants.alert_show==0) {
                                        setAlertImageAlone(getAlertImageString(30), alert_img);
                                    }


                                    GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
                                    gpsEditDeviceReq.setCategory(getString(R.string.gps_ALERT_DATA));
                                    gpsEditDeviceReq.setCompanyId(Constants.companyID);
                                    gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(30));
                                    GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
                                    deviceField.setAlert("50");
                                    deviceField.setAnticipation("");
                                    deviceField.setDrivingScore("");
                                    deviceField.setDrivingSkill("");
                                    deviceField.setLatitude(String.valueOf(lat));
                                    deviceField.setDrivingSkill("");
                                    deviceField.setKiloMeter("");
                                    deviceField.setLongitude(String.valueOf(longi));
                                    deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                                    deviceField.setSpeed(String.valueOf(speedd * 3.6));
                                    deviceField.setTotalKiloMeter("");
                                    deviceField.setSelfConfidence("");
                                    deviceField.setTravelTime(String.valueOf(alertDuration));
                                    deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
                                    //deviceField.setGpsCount(String.valueOf(totalSnr));
                                    deviceField.setAccelerX(String.valueOf(Accx));
                                    deviceField.setAccelerY(String.valueOf(Accy));
                                    deviceField.setAccelerZ(String.valueOf(Accz));
                                    deviceField.setSenRotate(String.valueOf(currentDegree));
                                    deviceField.setZipCode(distanceZip);
                                    deviceField.setRideTime(String.valueOf(getUniTime()));
                                    deviceField.setAlertId("");
                                    deviceField.setAlertValue("30");
                                    deviceField.setAlertKiloMeter(String.valueOf(distanceKm));
                                    deviceField.setAlertTime("");
                                    deviceField.setRideDate("");
                                    deviceField.setGyroscopeX(String.valueOf(gyroX));
                                    deviceField.setGyroscopeY(String.valueOf(gyroY));
                                    deviceField.setGyroscopeZ(String.valueOf(gyroZ));
                                    deviceField.setMagnatometerX(String.valueOf(magX));
                                    deviceField.setMagnatometerY(String.valueOf(magY));
                                    deviceField.setMagnatometerZ(String.valueOf(magZ));
                                    deviceField.setAlertName(Util_functions.getSubCategoryValueLowerr(30));
                                    deviceField.setEhorizonLength(String.valueOf(FullStatt.length));
                                    deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
                                    deviceField.setEngineState(String.valueOf(state));
                                    gpsEditDeviceReq.setDeviceDataField(deviceField);
                                    gpsEditDeviceReq.setDivisionId(Constants.divisionId);
                                    gpsEditDeviceReq.setModuleId(Constants.moduleID);
                                    /*gpsEditDeviceReq.setPropertyId(Constants.propID);
                                    gpsEditDeviceReq.setPortionId(Constants.porID);
                                    gpsEditDeviceReq.setSectionId(Constants.secID);*/
                                    gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
                                    gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
                                    gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
                                    gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
                                    gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
                                    gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
                                    gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
                                    GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
                                    deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
                                    deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
                                    deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
                                    deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
                                    deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
                                    gpsEditDeviceReq.setDeviceDataField1(deviceField1);
                                    if(Constants.subType.contains("BACKGROUND")) {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
                                    }else {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
                                    }
                                    Log.d("alert_log", gpsEditDeviceReq.toString());
                                    if (Util_functions.ConnectivityCheck(getApplicationContext(), RoadStartPageActivity.this)&&speedcount==0) {
                                        speedcount++;
                                        mconnectStatus = true;
                                        setGpsDataAPI(gpsEditDeviceReq);
                                    }
                                }
                                distanceZip = "";
                                distanceKm = 0.0;
                                alertDuration=0;
                                 /*else if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count >= 4) {
                                    speedLimitHandler = new Handler();
                                    speedLimitHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            speedLimitHandler.postDelayed(this, 2000);
                                            Constants.speedLimit_count++;
                                            if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count > 4) {
                                                txt_to_speech("Slow down");
                                            } else {
                                                speedLimitHandler.removeCallbacksAndMessages(null);
                                            }

                                        }
                                    }, 0000);

                                }*/
                            }

                        } else if (mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel >= 55 && mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel < 60) {
                            img_view_road_green_center.setVisibility(View.VISIBLE);
                            img_view_road_green_center.setImageResource(R.drawable.v55b);

                            if (Constants.speedLimit_count <= 3) {
                                Constants.speedLimit_count++;
                                speedValue=mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel;
                                if (Preference_Details.getSpeedLimit() == true) {
                                    beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100);
                                }
                            }else if (speedLimit_count == 4) {
                                Constants.speedLimit_count++;
                                CNxFullStat[] FullStat = mJniFunction.GetCloudStat();
                                if (FullStat.length > 0) {
                                    for (int i = 0; i < FullStat.length; i++) {
                                        distanceKm += FullStat[i].m_fDistance / 1000;
                                        alertDuration += FullStat[i].m_fDuration /60;
                                        if (!FullStat[i].m_sZipCode.equals("99999")) {
                                            distanceZip = FullStat[i].m_sZipCode;
                                        }
                                    }

                                    if (Constants.alert_show==0) {
                                        setAlertImageAlone(getAlertImageString(30), alert_img);
                                    }

                                    GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
                                    gpsEditDeviceReq.setCategory(getString(R.string.gps_ALERT_DATA));
                                    gpsEditDeviceReq.setCompanyId(Constants.companyID);
                                    gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(30));
                                    GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
                                    deviceField.setAlert("55");
                                    deviceField.setAnticipation("");
                                    deviceField.setDrivingScore("");
                                    deviceField.setDrivingSkill("");
                                    deviceField.setLatitude(String.valueOf(lat));
                                    deviceField.setDrivingSkill("");
                                    deviceField.setKiloMeter("");
                                    deviceField.setLongitude(String.valueOf(longi));
                                    deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                                    deviceField.setSpeed(String.valueOf(speedd * 3.6));
                                    deviceField.setTotalKiloMeter("");
                                    deviceField.setSelfConfidence("");
                                    deviceField.setTravelTime(String.valueOf(alertDuration));
                                    deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
                                    //deviceField.setGpsCount(String.valueOf(totalSnr));
                                    deviceField.setAccelerX(String.valueOf(Accx));
                                    deviceField.setAccelerY(String.valueOf(Accy));
                                    deviceField.setAccelerZ(String.valueOf(Accz));
                                    deviceField.setSenRotate(String.valueOf(currentDegree));
                                    deviceField.setZipCode(distanceZip);
                                    deviceField.setRideTime(String.valueOf(getUniTime()));
                                    deviceField.setAlertId("");
                                    deviceField.setAlertValue("30");
                                    deviceField.setAlertKiloMeter(String.valueOf(distanceKm));
                                    deviceField.setAlertTime("");
                                    deviceField.setRideDate("");
                                    deviceField.setGyroscopeX(String.valueOf(gyroX));
                                    deviceField.setGyroscopeY(String.valueOf(gyroY));
                                    deviceField.setGyroscopeZ(String.valueOf(gyroZ));
                                    deviceField.setMagnatometerX(String.valueOf(magX));
                                    deviceField.setMagnatometerY(String.valueOf(magY));
                                    deviceField.setMagnatometerZ(String.valueOf(magZ));
                                    deviceField.setAlertName(Util_functions.getSubCategoryValueLowerr(30));
                                    deviceField.setEhorizonLength(String.valueOf(FullStatt.length));
                                    deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
                                    deviceField.setEngineState(String.valueOf(state));
                                    gpsEditDeviceReq.setDeviceDataField(deviceField);
                                    gpsEditDeviceReq.setDivisionId(Constants.divisionId);
                                    gpsEditDeviceReq.setModuleId(Constants.moduleID);
                                    /*gpsEditDeviceReq.setPropertyId(Constants.propID);
                                    gpsEditDeviceReq.setPortionId(Constants.porID);
                                    gpsEditDeviceReq.setSectionId(Constants.secID);*/
                                    gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
                                    gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
                                    gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
                                    gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
                                    gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
                                    gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
                                    gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
                                    GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
                                    deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
                                    deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
                                    deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
                                    deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
                                    deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
                                    gpsEditDeviceReq.setDeviceDataField1(deviceField1);
                                    if(Constants.subType.contains("BACKGROUND")) {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
                                    }else {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
                                    }
                                    Log.d("alert_log", gpsEditDeviceReq.toString());
                                    if (Util_functions.ConnectivityCheck(getApplicationContext(), RoadStartPageActivity.this)&&speedcount==0) {
                                        speedcount++;
                                        mconnectStatus = true;
                                        setGpsDataAPI(gpsEditDeviceReq);
                                    }
                                }
                                distanceZip = "";
                                distanceKm = 0.0;
                                alertDuration=0;
                                 /*else if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count >= 4) {
                                    speedLimitHandler = new Handler();
                                    speedLimitHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            speedLimitHandler.postDelayed(this, 2000);
                                            Constants.speedLimit_count++;
                                            if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count > 4) {
                                                txt_to_speech("Slow down");
                                            } else {
                                                speedLimitHandler.removeCallbacksAndMessages(null);
                                            }

                                        }
                                    }, 0000);

                                }*/
                            }

                        } else if (mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel >= 60 && mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel < 65) {
                            img_view_road_green_center.setVisibility(View.VISIBLE);
                            img_view_road_green_center.setImageResource(R.drawable.v60b);


                            if (Constants.speedLimit_count <= 3) {
                                Constants.speedLimit_count++;
                                speedValue=mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel;
                                if (Preference_Details.getSpeedLimit() == true) {
                                    beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100);
                                }
                            }else if (speedLimit_count == 4) {
                                Constants.speedLimit_count++;
                                CNxFullStat[] FullStat = mJniFunction.GetCloudStat();
                                if (FullStat.length > 0) {
                                    for (int i = 0; i < FullStat.length; i++) {
                                        distanceKm += FullStat[i].m_fDistance / 1000;
                                        alertDuration += FullStat[i].m_fDuration /60;
                                        if (!FullStat[i].m_sZipCode.equals("99999")) {
                                            distanceZip = FullStat[i].m_sZipCode;
                                        }
                                    }
                                    if (Constants.alert_show==0) {
                                        setAlertImageAlone(getAlertImageString(30), alert_img);
                                    }


                                    GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
                                    gpsEditDeviceReq.setCategory(getString(R.string.gps_ALERT_DATA));
                                    gpsEditDeviceReq.setCompanyId(Constants.companyID);
                                    gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(30));
                                    GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
                                    deviceField.setAlert("60");
                                    deviceField.setAnticipation("");
                                    deviceField.setDrivingScore("");
                                    deviceField.setDrivingSkill("");
                                    deviceField.setLatitude(String.valueOf(lat));
                                    deviceField.setDrivingSkill("");
                                    deviceField.setKiloMeter("");
                                    deviceField.setLongitude(String.valueOf(longi));
                                    deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                                    deviceField.setSpeed(String.valueOf(speedd * 3.6));
                                    deviceField.setTotalKiloMeter("");
                                    deviceField.setSelfConfidence("");
                                    deviceField.setTravelTime(String.valueOf(alertDuration));
                                    deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
                                    //deviceField.setGpsCount(String.valueOf(totalSnr));
                                    deviceField.setAccelerX(String.valueOf(Accx));
                                    deviceField.setAccelerY(String.valueOf(Accy));
                                    deviceField.setAccelerZ(String.valueOf(Accz));
                                    deviceField.setSenRotate(String.valueOf(currentDegree));
                                    deviceField.setZipCode(distanceZip);
                                    deviceField.setRideTime(String.valueOf(getUniTime()));
                                    deviceField.setAlertId("");
                                    deviceField.setAlertValue("30");
                                    deviceField.setAlertKiloMeter(String.valueOf(distanceKm));
                                    deviceField.setAlertTime("");
                                    deviceField.setRideDate("");
                                    deviceField.setGyroscopeX(String.valueOf(gyroX));
                                    deviceField.setGyroscopeY(String.valueOf(gyroY));
                                    deviceField.setGyroscopeZ(String.valueOf(gyroZ));
                                    deviceField.setMagnatometerX(String.valueOf(magX));
                                    deviceField.setMagnatometerY(String.valueOf(magY));
                                    deviceField.setMagnatometerZ(String.valueOf(magZ));
                                    deviceField.setAlertName(Util_functions.getSubCategoryValueLowerr(30));
                                    deviceField.setEhorizonLength(String.valueOf(FullStatt.length));
                                    deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
                                    deviceField.setEngineState(String.valueOf(state));
                                    gpsEditDeviceReq.setDeviceDataField(deviceField);
                                    gpsEditDeviceReq.setDivisionId(Constants.divisionId);
                                    gpsEditDeviceReq.setModuleId(Constants.moduleID);
                                    /*gpsEditDeviceReq.setPropertyId(Constants.propID);
                                    gpsEditDeviceReq.setPortionId(Constants.porID);
                                    gpsEditDeviceReq.setSectionId(Constants.secID);*/
                                    gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
                                    gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
                                    gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
                                    gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
                                    gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
                                    gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
                                    gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
                                    GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
                                    deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
                                    deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
                                    deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
                                    deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
                                    deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
                                    gpsEditDeviceReq.setDeviceDataField1(deviceField1);
                                    if(Constants.subType.contains("BACKGROUND")) {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
                                    }else {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
                                    }
                                    Log.d("alert_log", gpsEditDeviceReq.toString());
                                    if (Util_functions.ConnectivityCheck(getApplicationContext(), RoadStartPageActivity.this)&&speedcount==0) {
                                        speedcount++;
                                        mconnectStatus = true;
                                        setGpsDataAPI(gpsEditDeviceReq);
                                    }
                                }
                                distanceZip = "";
                                distanceKm = 0.0;
                                alertDuration=0;
                                 /*else if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count >= 4) {
                                    speedLimitHandler = new Handler();
                                    speedLimitHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            speedLimitHandler.postDelayed(this, 2000);
                                            Constants.speedLimit_count++;
                                            if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count > 4) {
                                                txt_to_speech("Slow down");
                                            } else {
                                                speedLimitHandler.removeCallbacksAndMessages(null);
                                            }

                                        }
                                    }, 0000);

                                }*/
                            }

                        } else if (mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel >= 65 && mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel < 70) {
                            img_view_road_green_center.setVisibility(View.VISIBLE);
                            img_view_road_green_center.setImageResource(R.drawable.v65b);


                            if (Constants.speedLimit_count <= 3) {
                                Constants.speedLimit_count++;
                                speedValue=mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel;
                                if (Preference_Details.getSpeedLimit() == true) {
                                    beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100);
                                } }
                            else if (speedLimit_count == 4) {
                                Constants.speedLimit_count++;
                                CNxFullStat[] FullStat = mJniFunction.GetCloudStat();
                                if (FullStat.length > 0) {
                                    for (int i = 0; i < FullStat.length; i++) {
                                        distanceKm += FullStat[i].m_fDistance / 1000;
                                        alertDuration += FullStat[i].m_fDuration /60;
                                        if (!FullStat[i].m_sZipCode.equals("99999")) {
                                            distanceZip = FullStat[i].m_sZipCode;
                                        }
                                    }

                                    if (Constants.alert_show==0) {
                                        setAlertImageAlone(getAlertImageString(30), alert_img);
                                    }


                                    GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
                                    gpsEditDeviceReq.setCategory(getString(R.string.gps_ALERT_DATA));
                                    gpsEditDeviceReq.setCompanyId(Constants.companyID);
                                    gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(30));
                                    GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
                                    deviceField.setAlert("65");
                                    deviceField.setAnticipation("");
                                    deviceField.setDrivingScore("");
                                    deviceField.setDrivingSkill("");
                                    deviceField.setLatitude(String.valueOf(lat));
                                    deviceField.setDrivingSkill("");
                                    deviceField.setKiloMeter("");
                                    deviceField.setLongitude(String.valueOf(longi));
                                    deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                                    deviceField.setSpeed(String.valueOf(speedd * 3.6));
                                    deviceField.setTotalKiloMeter("");
                                    deviceField.setSelfConfidence("");
                                    deviceField.setTravelTime(String.valueOf(alertDuration));
                                    deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
                                    //deviceField.setGpsCount(String.valueOf(totalSnr));
                                    deviceField.setAccelerX(String.valueOf(Accx));
                                    deviceField.setAccelerY(String.valueOf(Accy));
                                    deviceField.setAccelerZ(String.valueOf(Accz));
                                    deviceField.setSenRotate(String.valueOf(currentDegree));
                                    deviceField.setZipCode(distanceZip);
                                    deviceField.setRideTime(String.valueOf(getUniTime()));
                                    deviceField.setAlertId("");
                                    deviceField.setAlertValue("30");
                                    deviceField.setAlertKiloMeter(String.valueOf(distanceKm));
                                    deviceField.setAlertTime("");
                                    deviceField.setRideDate("");
                                    deviceField.setGyroscopeX(String.valueOf(gyroX));
                                    deviceField.setGyroscopeY(String.valueOf(gyroY));
                                    deviceField.setGyroscopeZ(String.valueOf(gyroZ));
                                    deviceField.setMagnatometerX(String.valueOf(magX));
                                    deviceField.setMagnatometerY(String.valueOf(magY));
                                    deviceField.setMagnatometerZ(String.valueOf(magZ));
                                    deviceField.setAlertName(Util_functions.getSubCategoryValueLowerr(30));
                                    deviceField.setEhorizonLength(String.valueOf(FullStatt.length));
                                    deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
                                    deviceField.setEngineState(String.valueOf(state));
                                    gpsEditDeviceReq.setDeviceDataField(deviceField);
                                    gpsEditDeviceReq.setDivisionId(Constants.divisionId);
                                    gpsEditDeviceReq.setModuleId(Constants.moduleID);
                                   /* gpsEditDeviceReq.setPropertyId(Constants.propID);
                                    gpsEditDeviceReq.setPortionId(Constants.porID);
                                    gpsEditDeviceReq.setSectionId(Constants.secID);*/
                                    gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
                                    gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
                                    gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
                                    gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
                                    gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
                                    gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
                                    gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
                                    GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
                                    deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
                                    deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
                                    deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
                                    deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
                                    deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
                                    gpsEditDeviceReq.setDeviceDataField1(deviceField1);
                                    if(Constants.subType.contains("BACKGROUND")) {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
                                    }else {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
                                    }
                                    Log.d("alert_log", gpsEditDeviceReq.toString());
                                    if (Util_functions.ConnectivityCheck(getApplicationContext(), RoadStartPageActivity.this)&&speedcount==0) {
                                        speedcount++;
                                        mconnectStatus = true;
                                        setGpsDataAPI(gpsEditDeviceReq);
                                    }
                                }
                                distanceZip = "";
                                distanceKm = 0.0;
                                alertDuration=0;
                                    /*else if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count >= 4) {
                                    speedLimitHandler = new Handler();
                                    speedLimitHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            speedLimitHandler.postDelayed(this, 2000);
                                            Constants.speedLimit_count++;
                                            if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count > 4) {
                                                txt_to_speech("Slow down");
                                            } else {
                                                speedLimitHandler.removeCallbacksAndMessages(null);
                                            }

                                        }
                                    }, 0000);

                                }*/
                            }

                        } else if (mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel >= 70 && mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel < 75) {
                            img_view_road_green_center.setVisibility(View.VISIBLE);
                            img_view_road_green_center.setImageResource(R.drawable.v70b);


                            if (Constants.speedLimit_count <= 3) {
                                Constants.speedLimit_count++;
                                speedValue=mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel;
                                if (Preference_Details.getSpeedLimit() == true) {
                                    beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100);
                                }
                            }else if (speedLimit_count == 4) {
                                Constants.speedLimit_count++;
                                CNxFullStat[] FullStat = mJniFunction.GetCloudStat();
                                if (FullStat.length > 0) {
                                    for (int i = 0; i < FullStat.length; i++) {
                                        distanceKm += FullStat[i].m_fDistance / 1000;
                                        alertDuration += FullStat[i].m_fDuration /60;
                                        if (!FullStat[i].m_sZipCode.equals("99999")) {
                                            distanceZip = FullStat[i].m_sZipCode;
                                        }
                                    }

                                    if (Constants.alert_show==0) {
                                        setAlertImageAlone(getAlertImageString(30), alert_img);
                                    }

                                    GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
                                    gpsEditDeviceReq.setCategory(getString(R.string.gps_ALERT_DATA));
                                    gpsEditDeviceReq.setCompanyId(Constants.companyID);
                                    gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(30));
                                    GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
                                    deviceField.setAlert("70");
                                    deviceField.setAnticipation("");
                                    deviceField.setDrivingScore("");
                                    deviceField.setDrivingSkill("");
                                    deviceField.setLatitude(String.valueOf(lat));
                                    deviceField.setDrivingSkill("");
                                    deviceField.setKiloMeter("");
                                    deviceField.setLongitude(String.valueOf(longi));
                                    deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                                    deviceField.setSpeed(String.valueOf(speedd * 3.6));
                                    deviceField.setTotalKiloMeter("");
                                    deviceField.setSelfConfidence("");
                                    deviceField.setTravelTime(String.valueOf(alertDuration));
                                    deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
                                    //deviceField.setGpsCount(String.valueOf(totalSnr));
                                    deviceField.setAccelerX(String.valueOf(Accx));
                                    deviceField.setAccelerY(String.valueOf(Accy));
                                    deviceField.setAccelerZ(String.valueOf(Accz));
                                    deviceField.setSenRotate(String.valueOf(currentDegree));
                                    deviceField.setZipCode(distanceZip);
                                    deviceField.setRideTime(String.valueOf(getUniTime()));
                                    deviceField.setAlertId("");
                                    deviceField.setAlertValue("30");
                                    deviceField.setAlertKiloMeter(String.valueOf(distanceKm));
                                    deviceField.setAlertTime("");
                                    deviceField.setRideDate("");
                                    deviceField.setGyroscopeX(String.valueOf(gyroX));
                                    deviceField.setGyroscopeY(String.valueOf(gyroY));
                                    deviceField.setGyroscopeZ(String.valueOf(gyroZ));
                                    deviceField.setMagnatometerX(String.valueOf(magX));
                                    deviceField.setMagnatometerY(String.valueOf(magY));
                                    deviceField.setMagnatometerZ(String.valueOf(magZ));
                                    deviceField.setAlertName(Util_functions.getSubCategoryValueLowerr(30));
                                    deviceField.setEhorizonLength(String.valueOf(FullStatt.length));
                                    deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
                                    deviceField.setEngineState(String.valueOf(state));
                                    gpsEditDeviceReq.setDeviceDataField(deviceField);
                                    gpsEditDeviceReq.setDivisionId(Constants.divisionId);
                                    gpsEditDeviceReq.setModuleId(Constants.moduleID);
                                    /*gpsEditDeviceReq.setPropertyId(Constants.propID);
                                    gpsEditDeviceReq.setPortionId(Constants.porID);
                                    gpsEditDeviceReq.setSectionId(Constants.secID);*/
                                    gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
                                    gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
                                    gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
                                    gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
                                    gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
                                    gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
                                    gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
                                    GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
                                    deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
                                    deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
                                    deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
                                    deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
                                    deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
                                    gpsEditDeviceReq.setDeviceDataField1(deviceField1);
                                    if(Constants.subType.contains("BACKGROUND")) {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
                                    }else {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
                                    }
                                    Log.d("alert_log", gpsEditDeviceReq.toString());
                                    if (Util_functions.ConnectivityCheck(getApplicationContext(), RoadStartPageActivity.this)&&speedcount==0) {
                                        speedcount++;
                                        mconnectStatus = true;
                                        setGpsDataAPI(gpsEditDeviceReq);
                                    }
                                }
                                distanceZip = "";
                                distanceKm = 0.0;
                                alertDuration=0;
                                 /*else if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count >= 4) {
                                    speedLimitHandler = new Handler();
                                    speedLimitHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            speedLimitHandler.postDelayed(this, 2000);
                                            Constants.speedLimit_count++;
                                            if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count > 4) {
                                                txt_to_speech("Slow down");
                                            } else {
                                                speedLimitHandler.removeCallbacksAndMessages(null);
                                            }

                                        }
                                    }, 0000);

                                }*/
                            }

                        } else if (mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel >= 75 && mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel < 80) {
                            img_view_road_green_center.setVisibility(View.VISIBLE);
                            img_view_road_green_center.setImageResource(R.drawable.v75b);


                            if (Constants.speedLimit_count <= 3) {
                                Constants.speedLimit_count++;
                                speedValue=mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel;
                                if (Preference_Details.getSpeedLimit() == true) {
                                    beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100);
                                }
                            }else if (speedLimit_count == 4) {
                                Constants.speedLimit_count++;
                                CNxFullStat[] FullStat = mJniFunction.GetCloudStat();
                                if (FullStat.length > 0) {
                                    for (int i = 0; i < FullStat.length; i++) {
                                        distanceKm += FullStat[i].m_fDistance / 1000;
                                        alertDuration += FullStat[i].m_fDuration /60;
                                        if (!FullStat[i].m_sZipCode.equals("99999")) {
                                            distanceZip = FullStat[i].m_sZipCode;
                                        }
                                    }

                                    if (Constants.alert_show==0) {
                                        setAlertImageAlone(getAlertImageString(30), alert_img);
                                    }


                                    GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
                                    gpsEditDeviceReq.setCategory(getString(R.string.gps_ALERT_DATA));
                                    gpsEditDeviceReq.setCompanyId(Constants.companyID);
                                    gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(30));
                                    GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
                                    deviceField.setAlert("75");
                                    deviceField.setAnticipation("");
                                    deviceField.setDrivingScore("");
                                    deviceField.setDrivingSkill("");
                                    deviceField.setLatitude(String.valueOf(lat));
                                    deviceField.setDrivingSkill("");
                                    deviceField.setKiloMeter("");
                                    deviceField.setLongitude(String.valueOf(longi));
                                    deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                                    deviceField.setSpeed(String.valueOf(speedd * 3.6));
                                    deviceField.setTotalKiloMeter("");
                                    deviceField.setSelfConfidence("");
                                    deviceField.setTravelTime(String.valueOf(alertDuration));
                                    deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
                                    //deviceField.setGpsCount(String.valueOf(totalSnr));
                                    deviceField.setAccelerX(String.valueOf(Accx));
                                    deviceField.setAccelerY(String.valueOf(Accy));
                                    deviceField.setAccelerZ(String.valueOf(Accz));
                                    deviceField.setSenRotate(String.valueOf(currentDegree));
                                    deviceField.setZipCode(distanceZip);
                                    deviceField.setRideTime(String.valueOf(getUniTime()));
                                    deviceField.setAlertId("");
                                    deviceField.setAlertValue("30");
                                    deviceField.setAlertKiloMeter(String.valueOf(distanceKm));
                                    deviceField.setAlertTime("");
                                    deviceField.setRideDate("");
                                    deviceField.setGyroscopeX(String.valueOf(gyroX));
                                    deviceField.setGyroscopeY(String.valueOf(gyroY));
                                    deviceField.setGyroscopeZ(String.valueOf(gyroZ));
                                    deviceField.setMagnatometerX(String.valueOf(magX));
                                    deviceField.setMagnatometerY(String.valueOf(magY));
                                    deviceField.setMagnatometerZ(String.valueOf(magZ));
                                    deviceField.setAlertName(Util_functions.getSubCategoryValueLowerr(30));
                                    deviceField.setEhorizonLength(String.valueOf(FullStatt.length));
                                    deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
                                    deviceField.setEngineState(String.valueOf(state));
                                    gpsEditDeviceReq.setDeviceDataField(deviceField);
                                    gpsEditDeviceReq.setDivisionId(Constants.divisionId);
                                    gpsEditDeviceReq.setModuleId(Constants.moduleID);
                                    /*gpsEditDeviceReq.setPropertyId(Constants.propID);
                                    gpsEditDeviceReq.setPortionId(Constants.porID);
                                    gpsEditDeviceReq.setSectionId(Constants.secID);*/
                                    gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
                                    gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
                                    gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
                                    gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
                                    gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
                                    gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
                                    gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
                                    GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
                                    deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
                                    deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
                                    deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
                                    deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
                                    deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
                                    gpsEditDeviceReq.setDeviceDataField1(deviceField1);
                                    if(Constants.subType.contains("BACKGROUND")) {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
                                    }else {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
                                    }
                                    Log.d("alert_log", gpsEditDeviceReq.toString());
                                    if (Util_functions.ConnectivityCheck(getApplicationContext(), RoadStartPageActivity.this)&&speedcount==0) {
                                        speedcount++;
                                        mconnectStatus = true;
                                        setGpsDataAPI(gpsEditDeviceReq);
                                    }
                                }
                                distanceZip = "";
                                distanceKm = 0.0;
                                alertDuration=0;
                                /*else if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count >= 4) {
                                    speedLimitHandler = new Handler();
                                    speedLimitHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            speedLimitHandler.postDelayed(this, 2000);
                                            Constants.speedLimit_count++;
                                            if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count > 4) {
                                                txt_to_speech("Slow down");
                                            } else {
                                                speedLimitHandler.removeCallbacksAndMessages(null);
                                            }

                                        }
                                    }, 0000);

                                }*/
                            }

                        } else if (mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel >= 80 && mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel < 90) {
                            img_view_road_green_center.setVisibility(View.VISIBLE);
                            img_view_road_green_center.setImageResource(R.drawable.v80b);
                            if (Constants.speedLimit_count <= 3) {
                                Constants.speedLimit_count++;
                                speedValue=mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel;
                                if (Preference_Details.getSpeedLimit() == true) {
                                    beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100);
                                }
                            }else if (speedLimit_count == 4) {
                                Constants.speedLimit_count++;
                                CNxFullStat[] FullStat = mJniFunction.GetCloudStat();
                                if (FullStat.length > 0) {
                                    for (int i = 0; i < FullStat.length; i++) {
                                        distanceKm += FullStat[i].m_fDistance / 1000;
                                        alertDuration += FullStat[i].m_fDuration /60;
                                        if (!FullStat[i].m_sZipCode.equals("99999")) {
                                            distanceZip = FullStat[i].m_sZipCode;
                                        }
                                    }

                                    if (Constants.alert_show==0) {
                                        setAlertImageAlone(getAlertImageString(30), alert_img);
                                    }


                                    GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
                                    gpsEditDeviceReq.setCategory(getString(R.string.gps_ALERT_DATA));
                                    gpsEditDeviceReq.setCompanyId(Constants.companyID);
                                    gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(30));
                                    GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
                                    deviceField.setAlert("80");
                                    deviceField.setAnticipation("");
                                    deviceField.setDrivingScore("");
                                    deviceField.setDrivingSkill("");
                                    deviceField.setLatitude(String.valueOf(lat));
                                    deviceField.setDrivingSkill("");
                                    deviceField.setKiloMeter("");
                                    deviceField.setLongitude(String.valueOf(longi));
                                    deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                                    deviceField.setSpeed(String.valueOf(speedd * 3.6));
                                    deviceField.setTotalKiloMeter("");
                                    deviceField.setSelfConfidence("");
                                    deviceField.setTravelTime(String.valueOf(alertDuration));
                                    deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
                                    //deviceField.setGpsCount(String.valueOf(totalSnr));
                                    deviceField.setAccelerX(String.valueOf(Accx));
                                    deviceField.setAccelerY(String.valueOf(Accy));
                                    deviceField.setAccelerZ(String.valueOf(Accz));
                                    deviceField.setSenRotate(String.valueOf(currentDegree));
                                    deviceField.setZipCode(distanceZip);
                                    deviceField.setRideTime(String.valueOf(getUniTime()));
                                    deviceField.setAlertId("");
                                    deviceField.setAlertValue("30");
                                    deviceField.setAlertKiloMeter(String.valueOf(distanceKm));
                                    deviceField.setAlertTime("");
                                    deviceField.setRideDate("");
                                    deviceField.setGyroscopeX(String.valueOf(gyroX));
                                    deviceField.setGyroscopeY(String.valueOf(gyroY));
                                    deviceField.setGyroscopeZ(String.valueOf(gyroZ));
                                    deviceField.setMagnatometerX(String.valueOf(magX));
                                    deviceField.setMagnatometerY(String.valueOf(magY));
                                    deviceField.setMagnatometerZ(String.valueOf(magZ));
                                    deviceField.setAlertName(Util_functions.getSubCategoryValueLowerr(30));
                                    deviceField.setEhorizonLength(String.valueOf(FullStatt.length));
                                    deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
                                    deviceField.setEngineState(String.valueOf(state));
                                    gpsEditDeviceReq.setDeviceDataField(deviceField);
                                    gpsEditDeviceReq.setDivisionId(Constants.divisionId);
                                    gpsEditDeviceReq.setModuleId(Constants.moduleID);
                                    /*gpsEditDeviceReq.setPropertyId(Constants.propID);
                                    gpsEditDeviceReq.setPortionId(Constants.porID);
                                    gpsEditDeviceReq.setSectionId(Constants.secID);*/
                                    gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
                                    gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
                                    gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
                                    gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
                                    gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
                                    gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
                                    gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
                                    GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
                                    deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
                                    deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
                                    deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
                                    deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
                                    deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
                                    gpsEditDeviceReq.setDeviceDataField1(deviceField1);
                                    if(Constants.subType.contains("BACKGROUND")) {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
                                    }else {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
                                    }
                                    Log.d("alert_log", gpsEditDeviceReq.toString());
                                    if (Util_functions.ConnectivityCheck(getApplicationContext(), RoadStartPageActivity.this)&&speedcount==0) {
                                        speedcount++;
                                        mconnectStatus = true;
                                        setGpsDataAPI(gpsEditDeviceReq);
                                    }
                                }
                                distanceZip = "";
                                distanceKm = 0.0;
                                alertDuration=0;
                                 /*else if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count >= 4) {
                                    speedLimitHandler = new Handler();
                                    speedLimitHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            speedLimitHandler.postDelayed(this, 2000);
                                            Constants.speedLimit_count++;
                                            if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count > 4) {
                                                txt_to_speech("Slow down");
                                            } else {
                                                speedLimitHandler.removeCallbacksAndMessages(null);
                                            }

                                        }
                                    }, 0000);

                                }*/
                            }

                        } else if (mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel >= 90 && mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel < 100) {
                            img_view_road_green_center.setVisibility(View.VISIBLE);
                            img_view_road_green_center.setImageResource(R.drawable.v90b);
                            if (Constants.speedLimit_count <= 3) {
                                Constants.speedLimit_count++;
                                speedValue=mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel;
                                if (Preference_Details.getSpeedLimit() == true) {
                                    beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100);
                                }
                            }else if (speedLimit_count == 4) {
                                Constants.speedLimit_count++;
                                CNxFullStat[] FullStat = mJniFunction.GetCloudStat();
                                if (FullStat.length > 0) {
                                    for (int i = 0; i < FullStat.length; i++) {
                                        distanceKm += FullStat[i].m_fDistance / 1000;
                                        alertDuration += FullStat[i].m_fDuration /60;
                                        if (!FullStat[i].m_sZipCode.equals("99999")) {
                                            distanceZip = FullStat[i].m_sZipCode;
                                        }
                                    }

                                    if (Constants.alert_show==0) {
                                        setAlertImageAlone(getAlertImageString(30), alert_img);
                                    }


                                    GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
                                    gpsEditDeviceReq.setCategory(getString(R.string.gps_ALERT_DATA));
                                    gpsEditDeviceReq.setCompanyId(Constants.companyID);
                                    gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(30));
                                    GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
                                    deviceField.setAlert("90");
                                    deviceField.setAnticipation("");
                                    deviceField.setDrivingScore("");
                                    deviceField.setDrivingSkill("");
                                    deviceField.setLatitude(String.valueOf(lat));
                                    deviceField.setDrivingSkill("");
                                    deviceField.setKiloMeter("");
                                    deviceField.setLongitude(String.valueOf(longi));
                                    deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                                    deviceField.setSpeed(String.valueOf(speedd * 3.6));
                                    deviceField.setTotalKiloMeter("");
                                    deviceField.setSelfConfidence("");
                                    deviceField.setTravelTime(String.valueOf(alertDuration));
                                    deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
                                    //deviceField.setGpsCount(String.valueOf(totalSnr));
                                    deviceField.setAccelerX(String.valueOf(Accx));
                                    deviceField.setAccelerY(String.valueOf(Accy));
                                    deviceField.setAccelerZ(String.valueOf(Accz));
                                    deviceField.setSenRotate(String.valueOf(currentDegree));
                                    deviceField.setZipCode(distanceZip);
                                    deviceField.setRideTime(String.valueOf(getUniTime()));
                                    deviceField.setAlertId("");
                                    deviceField.setAlertValue("30");
                                    deviceField.setAlertKiloMeter(String.valueOf(distanceKm));
                                    deviceField.setAlertTime("");
                                    deviceField.setRideDate("");
                                    deviceField.setGyroscopeX(String.valueOf(gyroX));
                                    deviceField.setGyroscopeY(String.valueOf(gyroY));
                                    deviceField.setGyroscopeZ(String.valueOf(gyroZ));
                                    deviceField.setMagnatometerX(String.valueOf(magX));
                                    deviceField.setMagnatometerY(String.valueOf(magY));
                                    deviceField.setMagnatometerZ(String.valueOf(magZ));
                                    deviceField.setAlertName(Util_functions.getSubCategoryValueLowerr(30));
                                    deviceField.setEhorizonLength(String.valueOf(FullStatt.length));
                                    deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
                                    deviceField.setEngineState(String.valueOf(state));
                                    gpsEditDeviceReq.setDeviceDataField(deviceField);
                                    gpsEditDeviceReq.setDivisionId(Constants.divisionId);
                                    gpsEditDeviceReq.setModuleId(Constants.moduleID);
                                    /*gpsEditDeviceReq.setPropertyId(Constants.propID);
                                    gpsEditDeviceReq.setPortionId(Constants.porID);
                                    gpsEditDeviceReq.setSectionId(Constants.secID);*/
                                    gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
                                    gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
                                    gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
                                    gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
                                    gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
                                    gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
                                    gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
                                    GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
                                    deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
                                    deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
                                    deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
                                    deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
                                    deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
                                    gpsEditDeviceReq.setDeviceDataField1(deviceField1);
                                    if(Constants.subType.contains("BACKGROUND")) {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
                                    }else {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
                                    }
                                    Log.d("alert_log", gpsEditDeviceReq.toString());
                                    if (Util_functions.ConnectivityCheck(getApplicationContext(), RoadStartPageActivity.this)&&speedcount==0) {
                                        speedcount++;
                                        mconnectStatus = true;
                                        setGpsDataAPI(gpsEditDeviceReq);
                                    }
                                }
                                distanceZip = "";
                                distanceKm = 0.0;
                                alertDuration=0;
                                 /*else if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count >= 4) {
                                    speedLimitHandler = new Handler();
                                    speedLimitHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            speedLimitHandler.postDelayed(this, 2000);
                                            Constants.speedLimit_count++;
                                            if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count > 4) {
                                                txt_to_speech("Slow down");
                                            } else {
                                                speedLimitHandler.removeCallbacksAndMessages(null);
                                            }

                                        }
                                    }, 0000);

                                }*/
                            }

                        } else if (mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel >= 100 && mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel < 110) {
                            img_view_road_green_center.setVisibility(View.VISIBLE);
                            img_view_road_green_center.setImageResource(R.drawable.v100b);
                            if (Constants.speedLimit_count <= 3) {
                                Constants.speedLimit_count++;
                                speedValue=mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel;
                                if (Preference_Details.getSpeedLimit() == true) {
                                    beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100);
                                }
                            }else if (speedLimit_count == 4) {
                                Constants.speedLimit_count++;
                                CNxFullStat[] FullStat = mJniFunction.GetCloudStat();
                                if (FullStat.length > 0) {
                                    for (int i = 0; i < FullStat.length; i++) {
                                        distanceKm += FullStat[i].m_fDistance / 1000;
                                        alertDuration += FullStat[i].m_fDuration /60;
                                        if (!FullStat[i].m_sZipCode.equals("99999")) {
                                            distanceZip = FullStat[i].m_sZipCode;
                                        }
                                    }
                                    if (Constants.alert_show==0) {
                                        setAlertImageAlone(getAlertImageString(30), alert_img);
                                    }


                                    GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
                                    gpsEditDeviceReq.setCategory(getString(R.string.gps_ALERT_DATA));
                                    gpsEditDeviceReq.setCompanyId(Constants.companyID);
                                    gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(30));
                                    GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
                                    deviceField.setAlert("100");
                                    deviceField.setAnticipation("");
                                    deviceField.setDrivingScore("");
                                    deviceField.setDrivingSkill("");
                                    deviceField.setLatitude(String.valueOf(lat));
                                    deviceField.setDrivingSkill("");
                                    deviceField.setKiloMeter("");
                                    deviceField.setLongitude(String.valueOf(longi));
                                    deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                                    deviceField.setSpeed(String.valueOf(speedd * 3.6));
                                    deviceField.setTotalKiloMeter("");
                                    deviceField.setSelfConfidence("");
                                    deviceField.setTravelTime(String.valueOf(alertDuration));
                                    deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
                                    //deviceField.setGpsCount(String.valueOf(totalSnr));
                                    deviceField.setAccelerX(String.valueOf(Accx));
                                    deviceField.setAccelerY(String.valueOf(Accy));
                                    deviceField.setAccelerZ(String.valueOf(Accz));
                                    deviceField.setSenRotate(String.valueOf(currentDegree));
                                    deviceField.setZipCode(distanceZip);
                                    deviceField.setRideTime(String.valueOf(getUniTime()));
                                    deviceField.setAlertId("");
                                    deviceField.setAlertValue("30");
                                    deviceField.setAlertKiloMeter(String.valueOf(distanceKm));
                                    deviceField.setAlertTime("");
                                    deviceField.setRideDate("");
                                    deviceField.setGyroscopeX(String.valueOf(gyroX));
                                    deviceField.setGyroscopeY(String.valueOf(gyroY));
                                    deviceField.setGyroscopeZ(String.valueOf(gyroZ));
                                    deviceField.setMagnatometerX(String.valueOf(magX));
                                    deviceField.setMagnatometerY(String.valueOf(magY));
                                    deviceField.setMagnatometerZ(String.valueOf(magZ));
                                    deviceField.setAlertName(Util_functions.getSubCategoryValueLowerr(30));
                                    deviceField.setEhorizonLength(String.valueOf(FullStatt.length));
                                    deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
                                    deviceField.setEngineState(String.valueOf(state));
                                    gpsEditDeviceReq.setDeviceDataField(deviceField);
                                    gpsEditDeviceReq.setDivisionId(Constants.divisionId);
                                    gpsEditDeviceReq.setModuleId(Constants.moduleID);
                                    /*gpsEditDeviceReq.setPropertyId(Constants.propID);
                                    gpsEditDeviceReq.setPortionId(Constants.porID);
                                    gpsEditDeviceReq.setSectionId(Constants.secID);*/
                                    gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
                                    gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
                                    gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
                                    gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
                                    gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
                                    gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
                                    gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
                                    GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
                                    deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
                                    deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
                                    deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
                                    deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
                                    deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
                                    gpsEditDeviceReq.setDeviceDataField1(deviceField1);
                                    if(Constants.subType.contains("BACKGROUND")) {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
                                    }else {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
                                    }
                                    Log.d("alert_log", gpsEditDeviceReq.toString());
                                    if (Util_functions.ConnectivityCheck(getApplicationContext(), RoadStartPageActivity.this)&&speedcount==0) {
                                        speedcount++;
                                        mconnectStatus = true;
                                        setGpsDataAPI(gpsEditDeviceReq);
                                    }
                                }
                                distanceZip = "";
                                distanceKm = 0.0;
                                alertDuration=0;
                                 /*else if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count >= 4) {
                                    speedLimitHandler = new Handler();
                                    speedLimitHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            speedLimitHandler.postDelayed(this, 2000);
                                            Constants.speedLimit_count++;
                                            if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count > 4) {
                                                txt_to_speech("Slow down");
                                            } else {
                                                speedLimitHandler.removeCallbacksAndMessages(null);
                                            }

                                        }
                                    }, 0000);

                                }*/
                            }

                        } else if (mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel >= 110 && mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel < 120) {
                            img_view_road_green_center.setVisibility(View.VISIBLE);
                            img_view_road_green_center.setImageResource(R.drawable.v110b);


                            if (Constants.speedLimit_count <= 3) {
                                Constants.speedLimit_count++;
                                speedValue=mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel;
                                if (Preference_Details.getSpeedLimit() == true) {
                                    beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100);
                                }
                            }else if (speedLimit_count == 4) {
                                Constants.speedLimit_count++;
                                CNxFullStat[] FullStat = mJniFunction.GetCloudStat();
                                if (FullStat.length > 0) {
                                    for (int i = 0; i < FullStat.length; i++) {
                                        distanceKm += FullStat[i].m_fDistance / 1000;
                                        alertDuration += FullStat[i].m_fDuration /60;
                                        if (!FullStat[i].m_sZipCode.equals("99999")) {
                                            distanceZip = FullStat[i].m_sZipCode;
                                        }
                                    }
                                    if (Constants.alert_show==0) {
                                        setAlertImageAlone(getAlertImageString(30), alert_img);
                                    }
                                    GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
                                    gpsEditDeviceReq.setCategory(getString(R.string.gps_ALERT_DATA));
                                    gpsEditDeviceReq.setCompanyId(Constants.companyID);
                                    gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(30));
                                    GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
                                    deviceField.setAlert("110");
                                    deviceField.setAnticipation("");
                                    deviceField.setDrivingScore("");
                                    deviceField.setDrivingSkill("");
                                    deviceField.setLatitude(String.valueOf(lat));
                                    deviceField.setDrivingSkill("");
                                    deviceField.setKiloMeter("");
                                    deviceField.setLongitude(String.valueOf(longi));
                                    deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                                    deviceField.setSpeed(String.valueOf(speedd * 3.6));
                                    deviceField.setTotalKiloMeter("");
                                    deviceField.setSelfConfidence("");
                                    deviceField.setTravelTime(String.valueOf(alertDuration));
                                    deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
                                    //deviceField.setGpsCount(String.valueOf(totalSnr));
                                    deviceField.setAccelerX(String.valueOf(Accx));
                                    deviceField.setAccelerY(String.valueOf(Accy));
                                    deviceField.setAccelerZ(String.valueOf(Accz));
                                    deviceField.setSenRotate(String.valueOf(currentDegree));
                                    deviceField.setZipCode(distanceZip);
                                    deviceField.setRideTime(String.valueOf(getUniTime()));
                                    deviceField.setAlertId("");
                                    deviceField.setAlertValue("30");
                                    deviceField.setAlertKiloMeter(String.valueOf(distanceKm));
                                    deviceField.setAlertTime("");
                                    deviceField.setRideDate("");
                                    deviceField.setGyroscopeX(String.valueOf(gyroX));
                                    deviceField.setGyroscopeY(String.valueOf(gyroY));
                                    deviceField.setGyroscopeZ(String.valueOf(gyroZ));
                                    deviceField.setMagnatometerX(String.valueOf(magX));
                                    deviceField.setMagnatometerY(String.valueOf(magY));
                                    deviceField.setMagnatometerZ(String.valueOf(magZ));
                                    deviceField.setAlertName(Util_functions.getSubCategoryValueLowerr(30));
                                    deviceField.setEhorizonLength(String.valueOf(FullStatt.length));
                                    deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
                                    deviceField.setEngineState(String.valueOf(state));
                                    gpsEditDeviceReq.setDeviceDataField(deviceField);
                                    gpsEditDeviceReq.setDivisionId(Constants.divisionId);
                                    gpsEditDeviceReq.setModuleId(Constants.moduleID);
                                    /*gpsEditDeviceReq.setPropertyId(Constants.propID);
                                    gpsEditDeviceReq.setPortionId(Constants.porID);
                                    gpsEditDeviceReq.setSectionId(Constants.secID);*/
                                    gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
                                    gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
                                    gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
                                    gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
                                    gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
                                    gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
                                    gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
                                    GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
                                    deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
                                    deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
                                    deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
                                    deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
                                    deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
                                    gpsEditDeviceReq.setDeviceDataField1(deviceField1);
                                    if(Constants.subType.contains("BACKGROUND")) {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
                                    }else {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
                                    }
                                    Log.d("alert_log", gpsEditDeviceReq.toString());
                                    if (Util_functions.ConnectivityCheck(getApplicationContext(), RoadStartPageActivity.this)&&speedcount==0) {
                                        speedcount++;
                                        mconnectStatus = true;
                                        setGpsDataAPI(gpsEditDeviceReq);
                                    }
                                }
                                distanceKm = 0.0;
                                distanceZip = "";
                                alertDuration=0;
                                 /*else if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count >= 4) {
                                    speedLimitHandler = new Handler();
                                    speedLimitHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            speedLimitHandler.postDelayed(this, 2000);
                                            Constants.speedLimit_count++;
                                            if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count > 4) {
                                                txt_to_speech("Slow down");
                                            } else {
                                                speedLimitHandler.removeCallbacksAndMessages(null);
                                            }

                                        }
                                    }, 0000);

                                }*/
                            }


                        } else if (mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel >= 120 && mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel < 130) {
                            img_view_road_green_center.setVisibility(View.VISIBLE);
                            img_view_road_green_center.setImageResource(R.drawable.v120b);
                            if (Constants.speedLimit_count <= 3) {
                                Constants.speedLimit_count++;
                                speedValue=mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel;
                                if (Preference_Details.getSpeedLimit() == true) {
                                    beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100);
                                }
                            }else if (speedLimit_count == 4) {
                                Constants.speedLimit_count++;
                                CNxFullStat[] FullStat = mJniFunction.GetCloudStat();
                                if (FullStat.length > 0) {
                                    for (int i = 0; i < FullStat.length; i++) {
                                        distanceKm += FullStat[i].m_fDistance / 1000;
                                        alertDuration += FullStat[i].m_fDuration /60;
                                        if (!FullStat[i].m_sZipCode.equals("99999")) {
                                            distanceZip = FullStat[i].m_sZipCode;
                                        }
                                    }
                                    if (Constants.alert_show==0) {
                                        setAlertImageAlone(getAlertImageString(30), alert_img);
                                    }


                                    GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
                                    gpsEditDeviceReq.setCategory(getString(R.string.gps_ALERT_DATA));
                                    gpsEditDeviceReq.setCompanyId(Constants.companyID);
                                    gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(30));
                                    GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
                                    deviceField.setAlert("120");
                                    deviceField.setAnticipation("");
                                    deviceField.setDrivingScore("");
                                    deviceField.setDrivingSkill("");
                                    deviceField.setLatitude(String.valueOf(lat));
                                    deviceField.setDrivingSkill("");
                                    deviceField.setKiloMeter("");
                                    deviceField.setLongitude(String.valueOf(longi));
                                    deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                                    deviceField.setSpeed(String.valueOf(speedd * 3.6));
                                    deviceField.setTotalKiloMeter("");
                                    deviceField.setSelfConfidence("");
                                    deviceField.setTravelTime(String.valueOf(alertDuration));
                                    deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
                                    //deviceField.setGpsCount(String.valueOf(totalSnr));
                                    deviceField.setAccelerX(String.valueOf(Accx));
                                    deviceField.setAccelerY(String.valueOf(Accy));
                                    deviceField.setAccelerZ(String.valueOf(Accz));
                                    deviceField.setSenRotate(String.valueOf(currentDegree));
                                    deviceField.setZipCode(distanceZip);
                                    deviceField.setRideTime(String.valueOf(getUniTime()));
                                    deviceField.setAlertId("");
                                    deviceField.setAlertValue("30");
                                    deviceField.setAlertKiloMeter(String.valueOf(distanceKm));
                                    deviceField.setAlertTime("");
                                    deviceField.setRideDate("");
                                    deviceField.setGyroscopeX(String.valueOf(gyroX));
                                    deviceField.setGyroscopeY(String.valueOf(gyroY));
                                    deviceField.setGyroscopeZ(String.valueOf(gyroZ));
                                    deviceField.setMagnatometerX(String.valueOf(magX));
                                    deviceField.setMagnatometerY(String.valueOf(magY));
                                    deviceField.setMagnatometerZ(String.valueOf(magZ));
                                    deviceField.setAlertName(Util_functions.getSubCategoryValueLowerr(30));
                                    deviceField.setEhorizonLength(String.valueOf(FullStatt.length));
                                    deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
                                    deviceField.setEngineState(String.valueOf(state));
                                    gpsEditDeviceReq.setDeviceDataField(deviceField);
                                    gpsEditDeviceReq.setDivisionId(Constants.divisionId);
                                    gpsEditDeviceReq.setModuleId(Constants.moduleID);
                                    /*gpsEditDeviceReq.setPropertyId(Constants.propID);
                                    gpsEditDeviceReq.setPortionId(Constants.porID);
                                    gpsEditDeviceReq.setSectionId(Constants.secID);*/
                                    gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
                                    gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
                                    gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
                                    gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
                                    gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
                                    gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
                                    gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
                                    GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
                                    deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
                                    deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
                                    deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
                                    deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
                                    deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
                                    gpsEditDeviceReq.setDeviceDataField1(deviceField1);
                                    if(Constants.subType.contains("BACKGROUND")) {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
                                    }else {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
                                    }
                                    Log.d("alert_log", gpsEditDeviceReq.toString());
                                    if (Util_functions.ConnectivityCheck(getApplicationContext(), RoadStartPageActivity.this)&&speedcount==0) {
                                        speedcount++;
                                        mconnectStatus = true;
                                        setGpsDataAPI(gpsEditDeviceReq);
                                    }
                                }
                                distanceZip = "";
                                distanceKm = 0.0;
                                alertDuration=0;
                                 /*else if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count >= 4) {
                                    speedLimitHandler = new Handler();
                                    speedLimitHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            speedLimitHandler.postDelayed(this, 2000);
                                            Constants.speedLimit_count++;
                                            if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count > 4) {
                                                txt_to_speech("Slow down");
                                            } else {
                                                speedLimitHandler.removeCallbacksAndMessages(null);
                                            }

                                        }
                                    }, 0000);

                                }*/
                            }


                        } else if (mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel >= 130 && mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel < 140) {
                            img_view_road_green_center.setVisibility(View.VISIBLE);
                            img_view_road_green_center.setImageResource(R.drawable.v130b);


                            if (Constants.speedLimit_count <= 3) {
                                Constants.speedLimit_count++;
                                speedValue=mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel;
                                if (Preference_Details.getSpeedLimit() == true) {
                                    beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100);
                                }
                            }else if (speedLimit_count == 4) {
                                Constants.speedLimit_count++;
                                CNxFullStat[] FullStat = mJniFunction.GetCloudStat();
                                if (FullStat.length > 0) {
                                    for (int i = 0; i < FullStat.length; i++) {
                                        distanceKm += FullStat[i].m_fDistance / 1000;
                                        alertDuration += FullStat[i].m_fDuration /60;
                                        if (!FullStat[i].m_sZipCode.equals("99999")) {
                                            distanceZip = FullStat[i].m_sZipCode;
                                        }
                                    }
                                    if (Constants.alert_show==0) {
                                        setAlertImageAlone(getAlertImageString(30), alert_img);
                                    }


                                    GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
                                    gpsEditDeviceReq.setCategory(getString(R.string.gps_ALERT_DATA));
                                    gpsEditDeviceReq.setCompanyId(Constants.companyID);
                                    gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(30));
                                    GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
                                    deviceField.setAlert("130");
                                    deviceField.setAnticipation("");
                                    deviceField.setDrivingScore("");
                                    deviceField.setDrivingSkill("");
                                    deviceField.setLatitude(String.valueOf(lat));
                                    deviceField.setDrivingSkill("");
                                    deviceField.setKiloMeter("");
                                    deviceField.setLongitude(String.valueOf(longi));
                                    deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                                    deviceField.setSpeed(String.valueOf(speedd * 3.6));
                                    deviceField.setTotalKiloMeter("");
                                    deviceField.setSelfConfidence("");
                                    deviceField.setTravelTime(String.valueOf(alertDuration));
                                    deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
                                    //deviceField.setGpsCount(String.valueOf(totalSnr));
                                    deviceField.setAccelerX(String.valueOf(Accx));
                                    deviceField.setAccelerY(String.valueOf(Accy));
                                    deviceField.setAccelerZ(String.valueOf(Accz));
                                    deviceField.setSenRotate(String.valueOf(currentDegree));
                                    deviceField.setZipCode(distanceZip);
                                    deviceField.setRideTime(String.valueOf(getUniTime()));
                                    deviceField.setAlertId("");
                                    deviceField.setAlertValue("30");
                                    deviceField.setAlertKiloMeter(String.valueOf(distanceKm));
                                    deviceField.setAlertTime("");
                                    deviceField.setRideDate("");
                                    deviceField.setGyroscopeX(String.valueOf(gyroX));
                                    deviceField.setGyroscopeY(String.valueOf(gyroY));
                                    deviceField.setGyroscopeZ(String.valueOf(gyroZ));
                                    deviceField.setMagnatometerX(String.valueOf(magX));
                                    deviceField.setMagnatometerY(String.valueOf(magY));
                                    deviceField.setMagnatometerZ(String.valueOf(magZ));
                                    deviceField.setAlertName(Util_functions.getSubCategoryValueLowerr(30));
                                    deviceField.setEhorizonLength(String.valueOf(FullStatt.length));
                                    deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
                                    deviceField.setEngineState(String.valueOf(state));
                                    gpsEditDeviceReq.setDeviceDataField(deviceField);
                                    gpsEditDeviceReq.setDivisionId(Constants.divisionId);
                                    gpsEditDeviceReq.setModuleId(Constants.moduleID);
                                    /*gpsEditDeviceReq.setPropertyId(Constants.propID);
                                    gpsEditDeviceReq.setPortionId(Constants.porID);
                                    gpsEditDeviceReq.setSectionId(Constants.secID);*/
                                    gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
                                    gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
                                    gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
                                    gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
                                    gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
                                    gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
                                    gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
                                    GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
                                    deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
                                    deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
                                    deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
                                    deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
                                    deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
                                    gpsEditDeviceReq.setDeviceDataField1(deviceField1);
                                    if(Constants.subType.contains("BACKGROUND")) {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
                                    }else {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
                                    }
                                    Log.d("alert_log", gpsEditDeviceReq.toString());
                                    if (Util_functions.ConnectivityCheck(getApplicationContext(), RoadStartPageActivity.this)&&speedcount==0) {
                                        speedcount++;
                                        mconnectStatus = true;
                                        setGpsDataAPI(gpsEditDeviceReq);
                                    }
                                }
                                distanceKm = 0.0;
                                distanceZip = "";
                                alertDuration=0;
                                /* else if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count >= 4) {
                                    speedLimitHandler = new Handler();
                                    speedLimitHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            speedLimitHandler.postDelayed(this, 2000);
                                            Constants.speedLimit_count++;
                                            if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count > 4) {
                                                txt_to_speech("Slow down");
                                            } else {
                                                speedLimitHandler.removeCallbacksAndMessages(null);
                                            }

                                        }
                                    }, 0000);

                                }*/
                            }

                        } else if (mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel >= 140 && mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel < 150) {
                            img_view_road_green_center.setVisibility(View.VISIBLE);
                            img_view_road_green_center.setImageResource(R.drawable.v140b);
                            if (Constants.speedLimit_count <= 3) {
                                Constants.speedLimit_count++;
                                speedValue=mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel;
                                if (Preference_Details.getSpeedLimit() == true) {
                                    beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100);
                                }
                            }else if (speedLimit_count == 4) {
                                Constants.speedLimit_count++;
                                CNxFullStat[] FullStat = mJniFunction.GetCloudStat();
                                if (FullStat.length > 0) {
                                    for (int i = 0; i < FullStat.length; i++) {
                                        distanceKm += FullStat[i].m_fDistance / 1000;
                                        alertDuration += FullStat[i].m_fDuration /60;
                                        if (!FullStat[i].m_sZipCode.equals("99999")) {
                                            distanceZip = FullStat[i].m_sZipCode;
                                        }
                                    }

                                    if (Constants.alert_show==0) {
                                        setAlertImageAlone(getAlertImageString(30), alert_img);
                                    }


                                    GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
                                    gpsEditDeviceReq.setCategory(getString(R.string.gps_ALERT_DATA));
                                    gpsEditDeviceReq.setCompanyId(Constants.companyID);
                                    gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(30));
                                    GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
                                    deviceField.setAlert("140");
                                    deviceField.setAnticipation("");
                                    deviceField.setDrivingScore("");
                                    deviceField.setDrivingSkill("");
                                    deviceField.setLatitude(String.valueOf(lat));
                                    deviceField.setDrivingSkill("");
                                    deviceField.setKiloMeter("");
                                    deviceField.setLongitude(String.valueOf(longi));
                                    deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                                    deviceField.setSpeed(String.valueOf(speedd * 3.6));
                                    deviceField.setTotalKiloMeter("");
                                    deviceField.setSelfConfidence("");
                                    deviceField.setTravelTime(String.valueOf(alertDuration));
                                    deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
                                    //deviceField.setGpsCount(String.valueOf(totalSnr));
                                    deviceField.setAccelerX(String.valueOf(Accx));
                                    deviceField.setAccelerY(String.valueOf(Accy));
                                    deviceField.setAccelerZ(String.valueOf(Accz));
                                    deviceField.setSenRotate(String.valueOf(currentDegree));
                                    deviceField.setZipCode(distanceZip);
                                    deviceField.setRideTime(String.valueOf(getUniTime()));
                                    deviceField.setAlertId("");
                                    deviceField.setAlertValue("30");
                                    deviceField.setAlertKiloMeter(String.valueOf(distanceKm));
                                    deviceField.setAlertTime("");
                                    deviceField.setRideDate("");
                                    deviceField.setGyroscopeX(String.valueOf(gyroX));
                                    deviceField.setGyroscopeY(String.valueOf(gyroY));
                                    deviceField.setGyroscopeZ(String.valueOf(gyroZ));
                                    deviceField.setMagnatometerX(String.valueOf(magX));
                                    deviceField.setMagnatometerY(String.valueOf(magY));
                                    deviceField.setMagnatometerZ(String.valueOf(magZ));
                                    deviceField.setAlertName(Util_functions.getSubCategoryValueLowerr(30));
                                    deviceField.setEhorizonLength(String.valueOf(FullStatt.length));
                                    deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
                                    deviceField.setEngineState(String.valueOf(state));
                                    gpsEditDeviceReq.setDeviceDataField(deviceField);
                                    gpsEditDeviceReq.setDivisionId(Constants.divisionId);
                                    gpsEditDeviceReq.setModuleId(Constants.moduleID);
                                    /*gpsEditDeviceReq.setPropertyId(Constants.propID);
                                    gpsEditDeviceReq.setPortionId(Constants.porID);
                                    gpsEditDeviceReq.setSectionId(Constants.secID);*/
                                    gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
                                    gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
                                    gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
                                    gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
                                    gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
                                    gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
                                    gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
                                    GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
                                    deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
                                    deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
                                    deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
                                    deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
                                    deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
                                    gpsEditDeviceReq.setDeviceDataField1(deviceField1);
                                    if(Constants.subType.contains("BACKGROUND")) {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
                                    }else {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
                                    }
                                    Log.d("alert_log", gpsEditDeviceReq.toString());
                                    if (Util_functions.ConnectivityCheck(getApplicationContext(), RoadStartPageActivity.this)&&speedcount==0) {
                                        speedcount++;
                                        mconnectStatus = true;
                                        setGpsDataAPI(gpsEditDeviceReq);
                                    }
                                }
                                distanceZip = "";
                                distanceKm = 0.0;
                                alertDuration=0;
                                /*else if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count > 4) {
                                    speedLimitHandler = new Handler();
                                    speedLimitHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            speedLimitHandler.postDelayed(this, 2000);
                                            Constants.speedLimit_count++;
                                            if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count > 4) {
                                                txt_to_speech("Slow down");
                                            } else {
                                                speedLimitHandler.removeCallbacksAndMessages(null);
                                            }

                                        }
                                    }, 0000);
                                }*/
                            }

                        } else if (mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel > 150) {
                            img_view_road_green_center.setImageResource(R.drawable.v150b);
                            if (Constants.speedLimit_count <= 3) {
                                Constants.speedLimit_count++;
                                speedValue=mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel;
                                if (Preference_Details.getSpeedLimit() == true) {
                                    beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100);
                                }
                            }else if (speedLimit_count == 4) {
                                Constants.speedLimit_count++;
                                CNxFullStat[] FullStat = mJniFunction.GetCloudStat();
                                if (FullStat.length > 0) {
                                    for (int i = 0; i < FullStat.length; i++) {
                                        distanceKm += FullStat[i].m_fDistance / 1000;
                                        alertDuration += FullStat[i].m_fDuration /60;
                                        if (!FullStat[i].m_sZipCode.equals("99999")) {
                                            distanceZip = FullStat[i].m_sZipCode;
                                        }
                                    }

                                    if (Constants.alert_show==0) {
                                        setAlertImageAlone(getAlertImageString(30), alert_img);
                                    }


                                    GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
                                    gpsEditDeviceReq.setCategory(getString(R.string.gps_ALERT_DATA));
                                    gpsEditDeviceReq.setCompanyId(Constants.companyID);
                                    gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(30));
                                    GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
                                    deviceField.setAlert("150");
                                    deviceField.setAnticipation("");
                                    deviceField.setDrivingScore("");
                                    deviceField.setDrivingSkill("");
                                    deviceField.setLatitude(String.valueOf(lat));
                                    deviceField.setDrivingSkill("");
                                    deviceField.setKiloMeter("");
                                    deviceField.setLongitude(String.valueOf(longi));
                                    deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                                    deviceField.setSpeed(String.valueOf(speedd * 3.6));
                                    deviceField.setTotalKiloMeter("");
                                    deviceField.setSelfConfidence("");
                                    deviceField.setTravelTime(String.valueOf(alertDuration));
                                    deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
                                    //deviceField.setGpsCount(String.valueOf(totalSnr));
                                    deviceField.setAccelerX(String.valueOf(Accx));
                                    deviceField.setAccelerY(String.valueOf(Accy));
                                    deviceField.setAccelerZ(String.valueOf(Accz));
                                    deviceField.setSenRotate(String.valueOf(currentDegree));
                                    deviceField.setZipCode(distanceZip);
                                    deviceField.setRideTime(String.valueOf(getUniTime()));
                                    deviceField.setAlertId("");
                                    deviceField.setAlertValue("30");
                                    deviceField.setAlertKiloMeter(String.valueOf(distanceKm));
                                    deviceField.setAlertTime("");
                                    deviceField.setRideDate("");
                                    deviceField.setGyroscopeX(String.valueOf(gyroX));
                                    deviceField.setGyroscopeY(String.valueOf(gyroY));
                                    deviceField.setGyroscopeZ(String.valueOf(gyroZ));
                                    deviceField.setMagnatometerX(String.valueOf(magX));
                                    deviceField.setMagnatometerY(String.valueOf(magY));
                                    deviceField.setMagnatometerZ(String.valueOf(magZ));
                                    deviceField.setAlertName(Util_functions.getSubCategoryValueLowerr(30));
                                    deviceField.setEhorizonLength(String.valueOf(FullStatt.length));
                                    deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
                                    deviceField.setEngineState(String.valueOf(state));
                                    gpsEditDeviceReq.setDeviceDataField(deviceField);
                                    gpsEditDeviceReq.setDivisionId(Constants.divisionId);
                                    gpsEditDeviceReq.setModuleId(Constants.moduleID);
                                    /*gpsEditDeviceReq.setPropertyId(Constants.propID);
                                    gpsEditDeviceReq.setPortionId(Constants.porID);
                                    gpsEditDeviceReq.setSectionId(Constants.secID);*/
                                    gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
                                    gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
                                    gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
                                    gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
                                    gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
                                    gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
                                    gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
                                    GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
                                    deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
                                    deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
                                    deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
                                    deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
                                    deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
                                    gpsEditDeviceReq.setDeviceDataField1(deviceField1);
                                    if(Constants.subType.contains("BACKGROUND")) {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
                                    }else {
                                        gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
                                    }
                                    Log.d("alert_log", gpsEditDeviceReq.toString());
                                    if (Util_functions.ConnectivityCheck(getApplicationContext(), RoadStartPageActivity.this)&&speedcount==0) {
                                        speedcount++;
                                        mconnectStatus = true;
                                        setGpsDataAPI(gpsEditDeviceReq);
                                    }
                                }
                                distanceKm = 0.0;
                                distanceZip = "";
                                alertDuration=0;
                                 /*else {
                                    speedLimitHandler = new Handler();
                                    speedLimitHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            speedLimitHandler.postDelayed(this, 2000);
                                            Constants.speedLimit_count++;
                                            if (Constants.speedLimit_count <= 11 && Constants.speedLimit_count > 4) {
                                                txt_to_speech("Slow down");
                                            } else {
                                                speedLimitHandler.removeCallbacksAndMessages(null);
                                            }

                                        }
                                    }, 0000);

                                }*/
                            }

                        }

                    } else if (mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel == -1) {
                        img_view_road_green_center.setVisibility(View.INVISIBLE);
                        Constants.speedLimit_count = 1;
                        //txt_to_speech("");
                    }

                    /*if (mNxRisk.m_SpeedAlert.m_iSpeedLimitPanel <=20) {
                     *//**Do SomeThing*//*
                }
                if (mNxRisk.m_SpeedAlert.m_iSpeedLimitTone == 0) {
                    *//**Do SomeThing*//*
                }*/
            /*    else if (mNxRisk.m_SpeedAlert.m_iSpeedLimitTone == 1) {
                    ToneGenerator beep = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                    beep.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD,100);
                    *//**Do SomeThing*//*
                }*/
                    /*text.setVisibility(View.VISIBLE);
                    text.setTextColor(getColor(R.color.white));
                    text.setText("Running");*/

                }
                break;
            case CNxRisk.UPDATING_HORIZ:
                // img_road_green.setVisibility(View.VISIBLE);
                img_road_green.setVisibility(View.VISIBLE);
                img_view_road_green_center.setVisibility(View.INVISIBLE);
                img_view_road_green_pause.setVisibility(View.INVISIBLE);
                alert_img.setImageResource(0);
                speed_progress_animated_1.setProgress(0);
                text_dist2poi.setText("0 m");
                if (Constants.stateId==1){
                    gearAnimLoader();
                    /*txt_to_speech("gear");
                    text.setVisibility(View.VISIBLE);
                    text.setTextColor(getColor(R.color.white));
                    text.setText("gear");*/
                }
                /*if (stateId==2){
                    img_view_road_green_pause.setImageResource(R.drawable.image_phone_rond_gris_pause_b01);
                    img_view_road_green_pause.setVisibility(View.VISIBLE);
                    img_view_road_green_gear.setVisibility(View.INVISIBLE);
                    img_road_green.setVisibility(View.INVISIBLE);
                    img_view_road_green_center.setVisibility(View.INVISIBLE);

                }*/

                if (autoStart == false){
                    if (stopCount == 0){
                        stopCount++;
                        stopDuration = getUniTime();
                    }

                    if (stopCount>0){
                        try
                        {
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
                            String call_endtime = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm:ss aa", new Date(Long.parseLong(String.valueOf(getUniTime())))).toString();
                            String call_starttime=android.text.format.DateFormat.format("dd/MM/yyyy hh:mm:ss aa", new Date(Long.parseLong(String.valueOf(stopDuration)))).toString();
                            Date date1 = format.parse(call_starttime);
                            Date date2 = format.parse(call_endtime);
                            long mills = date2.getTime() - date1.getTime();
                            Log.v("Data1", ""+date1.getTime());
                            Log.v("Data2", ""+date2.getTime());
                            hours = (int) (mills/(1000 * 60 * 60));
                            int mins = (int) (mills/(1000*60)) % 60;
                            int sec = (int) (mills/1000) % 60;
                            diff_dur = mins+"."+sec; // updated value every1 second

                            if (Double.valueOf(diff_dur) >= 5.0&&sCount>=1&&Constants.horiz_count!=0){
                                sCount=0;
                                mconnectStatus = true;
                                //autoStart=true;
                                tripStatus="- Ride Stopped!";
                                txt_view_road_stop.performClick();
                                txt_view_road_stop.setClickable(false);
                                /*img_road_green.setVisibility(View.VISIBLE);
                                //Toast.makeText(RoadStartPageActivity.this, "STOP_PROLOG " + mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                                txt_view_road_start.setVisibility(View.VISIBLE);
                                txt_start.setText("START");
                                txt_view_road_stop.setVisibility(View.INVISIBLE);
                                //Toast.makeText(RoadStartPageActivity.this, "Start text changed", Toast.LENGTH_SHORT).show();
                                txt_view_road_start.setClickable(false);
                                if (Util_functions.ConnectivityCheck(RoadStartPageActivity.this, RoadStartPageActivity.this) && GPS_deviceID != "") {
                                    mconnectStatus = true;
                                    sCount=0;
                                    mstopStatus = true;
                                    stopDrive();
                                }*/
                            }else if (Double.valueOf(diff_dur) >= 5.0&&sCount>=1&&Constants.horiz_count==0){
                                sCount=0;
                                mconnectStatus = true;
                                //autoStart=true;
                                tripStatus="- Ride Stopped!";
                                txt_view_road_stop.performClick();
                                txt_view_road_stop.setClickable(false);
                                /*img_road_green.setVisibility(View.VISIBLE);
                                //Toast.makeText(RoadStartPageActivity.this, "STOP_PROLOG " + mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                                txt_view_road_start.setVisibility(View.VISIBLE);
                                txt_start.setText("START");
                                txt_view_road_stop.setVisibility(View.INVISIBLE);
                                //Toast.makeText(RoadStartPageActivity.this, "Start text changed", Toast.LENGTH_SHORT).show();
                                txt_view_road_start.setClickable(false);
                                if (Util_functions.ConnectivityCheck(RoadStartPageActivity.this, RoadStartPageActivity.this) && GPS_deviceID != "") {
                                    mconnectStatus = true;
                                    sCount=0;
                                    mstopStatus = true;
                                    stopDrive();
                                }*/
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }

                CNxFullStat[] FullStatt = mJniFunction.GetCloudStat();
                if (FullStatt.length > 0) {
                    for (int i = 0; i < FullStatt.length; i++) {
                        kmDistance += FullStatt[i].m_fDistance / 1000;
                            /*text.setVisibility(View.VISIBLE);
                            text.setText(FullStat[i].m_sZipCode);*/
                        if (!FullStatt[i].m_sZipCode.equals("99999")) {
                            zipcode = FullStatt[i].m_sZipCode;
                        }
                    }
                    DecimalFormat REAL_FORMATTER = new DecimalFormat("0.0");
                    kmDistance = Double.valueOf(REAL_FORMATTER.format(kmDistance));
                    double remain = kmDistance % 0.2;
                    remain = Double.parseDouble(REAL_FORMATTER.format(remain));
                    if (remain == 0.0 || remain == 0.2) {
                        if (DistanceStatuus == false) {
                            setstartDet();
                            //weatherData(lat,longi);
                            GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
                            gpsEditDeviceReq.setCategory(getString(R.string.gps_DISTANCE_DATA));
                            GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
                            deviceField.setAlert("");
                            deviceField.setAnticipation("");
                            deviceField.setDrivingScore("");
                            deviceField.setDrivingSkill("");
                            deviceField.setLatitude(String.valueOf(lat));
                            deviceField.setDrivingSkill("");
                            deviceField.setKiloMeter(String.valueOf(kmDistance));
                            deviceField.setLongitude(String.valueOf(longi));
                            deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                            deviceField.setSpeed(String.valueOf(speedd * 3.6));
                            deviceField.setTotalKiloMeter("");
                            deviceField.setSelfConfidence("");
                            deviceField.setTravelTime("");
                            deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
                            //deviceField.setGpsCount(String.valueOf(totalSnr));
                            deviceField.setAccelerX(String.valueOf(Accx));
                            deviceField.setAccelerY(String.valueOf(Accy));
                            deviceField.setAccelerZ(String.valueOf(Accz));
                            deviceField.setSenRotate(String.valueOf(currentDegree));
                            deviceField.setZipCode(zipcode);
                            deviceField.setAlertId("");
                            deviceField.setAlertValue("");
                            deviceField.setAlertTime("");
                            deviceField.setRideDate("");
                            deviceField.setRideTime(String.valueOf(getUniTime()));
                            deviceField.setGyroscopeX(String.valueOf(gyroX));
                            deviceField.setGyroscopeY(String.valueOf(gyroY));
                            deviceField.setGyroscopeZ(String.valueOf(gyroZ));
                            deviceField.setMagnatometerX(String.valueOf(magX));
                            deviceField.setMagnatometerY(String.valueOf(magY));
                            deviceField.setMagnatometerZ(String.valueOf(magZ));
                            deviceField.setAlertName("");
                            deviceField.setEhorizonLength(String.valueOf(FullStatt.length));
                            deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
                            deviceField.setEngineState(String.valueOf(state));
                            gpsEditDeviceReq.setDeviceDataField(deviceField);
                            gpsEditDeviceReq.setCompanyId(Constants.companyID);
                            gpsEditDeviceReq.setDivisionId(Constants.divisionId);
                            gpsEditDeviceReq.setModuleId(Constants.moduleID);
                            /*gpsEditDeviceReq.setPropertyId(Constants.propID);
                            gpsEditDeviceReq.setPortionId(Constants.porID);
                            gpsEditDeviceReq.setSectionId(Constants.secID);*/
                            gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
                            gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
                            gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
                            gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
                            gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
                            gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
                            gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
                            GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
                            deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
                            deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
                            deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
                            deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
                            deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
                            gpsEditDeviceReq.setDeviceDataField1(deviceField1);
                            DistanceStatuus = true;
                            if(Constants.subType.contains("BACKGROUND")) {
                                gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
                            }else {
                                gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
                            }
                            Log.d("distance_log", gpsEditDeviceReq.toString());
                            if (!Constants.GPS_deviceID.isEmpty()&&!Constants.GPS_deviceID.equals("")){
                                setGpsDataAPI(gpsEditDeviceReq);
                            }

                        }
                    } else {
                        DistanceStatuus = false;
                    }

                }
                kmDuration = 0.0;
                kmDistance = 0.00;
                zipcode = "";
                //img_road_green.setImageResource(R.drawable.image_phone_rond_gris_engrenages_a01);

                //text.setText("Updating Horizon" +CNxRisk.UPDATING_HORIZ);
                //img_view_road_green_pause.setImageResource(R.drawable.image_phone_rond_vert_route_d01);
                // img_view_road_green_pause.setVisibility(View.VISIBLE);
                // img_road_green.setVisibility(View.INVISIBLE);
                // Toast.makeText(RoadStartPageActivity.this, "Updating Horizon ", Toast.LENGTH_SHORT).show();
                break;
            case CNxRisk.CAR_STOPPED:
                speed_progress_animated_1.setProgress(0);
                text_dist2poi.setText("0 m");
               /* if (autoStart == false && StartScrVisibility == true){
                        img_road_green.setVisibility(View.VISIBLE);
                        Toast.makeText(RoadStartPageActivity.this, "STOP_PROLOG " + mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                        txt_view_road_start.setVisibility(View.VISIBLE);
                        txt_view_road_stop.setVisibility(View.INVISIBLE);
                        //Toast.makeText(RoadStartPageActivity.this, "Start text changed", Toast.LENGTH_SHORT).show();
                        txt_view_road_start.setClickable(false);
                        if (Util_functions.ConnectivityCheck(RoadStartPageActivity.this, RoadStartPageActivity.this)) {
                            mconnectStatus = true;
                            mstopStatus = true;
                            stopDrive();
                        }
                }*/


                /*txt_to_speech("car stopped");
                text.setVisibility(View.VISIBLE);
                text.setTextColor(getColor(R.color.white));
                text.setText("car stopped");*/
                // Toast.makeText(RoadStartPageActivity.this, "CAR_STOPPED"+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                if(autoStart == false) {
                    if (stopCount == 0){
                        stopCount++;
                        stopDuration = getUniTime();
                    }
                    if (stopCount>0){
                        try
                        {
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
                            String call_endtime = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm:ss aa", new Date(Long.parseLong(String.valueOf(getUniTime())))).toString();
                            String call_starttime=android.text.format.DateFormat.format("dd/MM/yyyy hh:mm:ss aa", new Date(Long.parseLong(String.valueOf(stopDuration)))).toString();
                            Date date1 = format.parse(call_starttime);
                            Date date2 = format.parse(call_endtime);
                            long mills = date2.getTime() - date1.getTime();
                            Log.v("Data1", ""+date1.getTime());
                            Log.v("Data2", ""+date2.getTime());
                            hours = (int) (mills/(1000 * 60 * 60));
                            int mins = (int) (mills/(1000*60)) % 60;
                            int sec = (int) (mills/1000) % 60;
                            diff_dur = mins+"."+sec; // updated value every1 second

                            if (Double.valueOf(diff_dur) >= 5.0 && sCount>=1){
                                sCount=0;
                                mconnectStatus = true;
                                //autoStart=true;
                                tripStatus="- Ride Stopped!";
                                txt_view_road_stop.performClick();
                                txt_view_road_stop.setClickable(false);
                                /*img_road_green.setVisibility(View.VISIBLE);
                                //Toast.makeText(RoadStartPageActivity.this, "STOP_PROLOG " + mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                                txt_view_road_start.setVisibility(View.VISIBLE);
                                txt_view_road_stop.setVisibility(View.INVISIBLE);
                                //Toast.makeText(RoadStartPageActivity.this, "Start text changed", Toast.LENGTH_SHORT).show();
                                txt_view_road_start.setClickable(false);
                                if (Util_functions.ConnectivityCheck(RoadStartPageActivity.this, RoadStartPageActivity.this)&&GPS_deviceID!="") {
                                    sCount=0;
                                    mconnectStatus = true;
                                    mstopStatus = true;
                                    stopDrive();
                                }*/
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                    alert_img.setImageResource(0);
                    img_view_road_green_pause.setImageResource(R.drawable.image_phone_rond_gris_pause_b01);
                    img_view_road_green_pause.setVisibility(View.VISIBLE);
                    img_view_road_green_gear.setVisibility(View.INVISIBLE);
                    img_road_green.setVisibility(View.INVISIBLE);
                    img_view_road_green_center.setVisibility(View.INVISIBLE);
                    stateId = 2;
                    current_speed_val.setText("0");
                    riskBarValue.setProgress(0);
                    risk_val.setText("0");
                }

                // img_view_road_start.setImageResource(R.drawable.start_button);
                // text.setText("State :"+mNxRisk.m_iJourneyState);
                break;
            case CNxRisk.GPS_LOST:
               /* txt_to_speech("GPS LOST");
                text.setVisibility(View.VISIBLE);
                text.setTextColor(getColor(R.color.white));
                text.setText("gps lost");*/
                //   Toast.makeText(RoadStartPageActivity.this, "GPS_LOST "+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                img_view_road_green_pause.setImageResource(R.drawable.image_phone_rond_rouge_pas_de_gps_e01);
                img_view_road_green_pause.setVisibility(View.VISIBLE);
                img_road_green.setVisibility(View.INVISIBLE);
                img_view_road_green_gear.setVisibility(View.INVISIBLE);
                alert_img.setImageResource(0);

                if (gps_lost==false){
                    gps_lost=true;
                    startLocationButtonClick();
                }
                //img_view_road_start.setImageResource(R.drawable.start_button);
                break;
            case CNxRisk.RISK_BUG:
                if (autoStart==false) {
                    img_road_green.setVisibility(View.VISIBLE);
                    Toast.makeText(RoadStartPageActivity.this, "RISK_BUG " + mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                }
                break;
            case CNxRisk.STOP_PROLOG:

                if (autoStart==false) {
                    img_road_green.setVisibility(View.VISIBLE);
                    //Toast.makeText(RoadStartPageActivity.this, "STOP_PROLOG " + mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                    txt_view_road_start.setVisibility(View.VISIBLE);
                    txt_start.setText("START");
                    txt_view_road_stop.setVisibility(View.INVISIBLE);
                    //Toast.makeText(RoadStartPageActivity.this, "Start text changed", Toast.LENGTH_SHORT).show();
                    txt_view_road_start.setClickable(false);
                    if (Util_functions.ConnectivityCheck(RoadStartPageActivity.this, RoadStartPageActivity.this) && sCount>0) {
                        mconnectStatus = true;
                        mstopStatus = true;
                        tripStatus="- Ride Stopped!";
                        txt_view_road_stop.setClickable(false);
                        stopDrive();
                    }
                }
                break;
            case CNxRisk.DOWNLOAD_IN_PROGRESS:
                if (autoStart==false) {
                    //   Toast.makeText(RoadStartPageActivity.this, "DOWNLOAD_IN_PROGRESS "+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                    img_view_road_green_pause.setImageResource(R.drawable.image_phone_rond_gris_download_b01);
                    img_view_road_green_pause.setVisibility(View.VISIBLE);
                    img_road_green.setVisibility(View.INVISIBLE);
                    img_view_road_green_center.setVisibility(View.INVISIBLE);
                    img_view_road_green_gear.setVisibility(View.INVISIBLE);
                    alert_img.setImageResource(0);

                }
                break;
            case CNxRisk.HSUID_INACCESSIBLE:
                if (autoStart==false) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                    getLiscenseUpdate();
                    img_road_green.setVisibility(View.VISIBLE);
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "The machine’s HSUID could not be extracted \n please report the problem", "ok");
                    }
                }
                //Toast.makeText(RoadStartPageActivity.this, "The machine’s HSUID could not be extracted"+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                break;
            case CNxRisk.INVALID_LICENSE:
                if (autoStart==false) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                    getLiscenseUpdate();
                    img_road_green.setVisibility(View.VISIBLE);
                    //Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "The license key or license file is not valid for this machine \n please report the problem", "ok");
                }
                //  Toast.makeText(RoadStartPageActivity.this, " the license key or license file is not valid for this machine"+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                break;
            case CNxRisk.EXPIRED_LICENSE:
                if (autoStart==false) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                    getLiscenseUpdate();
                    img_road_green.setVisibility(View.VISIBLE);
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "The license key or license file has expired \n please report the problem", "ok");
                    }
                }
                // Toast.makeText(RoadStartPageActivity.this, " the license key or license file has expired"+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                break;
            case CNxRisk.INVALID_LIC_FILE:
                MapDownload = true;
                txt_view_road_start.setClickable(false);
                profile.setClickable(false);
                more_start.setClickable(false);
                genCert();
                licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                getLiscenseUpdate();
                img_road_green.setVisibility(View.VISIBLE);
                if (Constants.subType.contains("FOREGROUND")) {
                    Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "an invalid license file was provided \n please report the problem", "ok");
                }
                //Toast.makeText(RoadStartPageActivity.this, "an invalid license file was provided"+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                break;
            case CNxRisk.INVALID_LIC_KEY:
                if (autoStart==false) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                    getLiscenseUpdate();
                    img_road_green.setVisibility(View.VISIBLE);
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "license key is not in the correct format \n please report the problem", "ok");
                    }
                }
                //Toast.makeText(RoadStartPageActivity.this, "license key is not in the correct format"+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                break;
            case CNxRisk.LIC_NO_EXPIRATION_DAY:
                if (autoStart==false) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                    getLiscenseUpdate();
                    img_road_green.setVisibility(View.VISIBLE);
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "There is no license expiration date \n please report the problem", "ok");
                    }
                }
                // Toast.makeText(RoadStartPageActivity.this, "There is no license expiration date"+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                break;
            case CNxRisk.LIC_PARAM_ERROR:
                if (autoStart==false) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                    getLiscenseUpdate();
                    img_road_green.setVisibility(View.VISIBLE);
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "an invalid parameter was passed \n please report the problem", "ok");
                    }
                }
                // Toast.makeText(RoadStartPageActivity.this, " an invalid parameter was passed"+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                break;
            case CNxRisk.LIC_NO_MACHINE_ID:
                if (autoStart==false) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                    getLiscenseUpdate();
                    img_road_green.setVisibility(View.VISIBLE);
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "The machine's unique ID could not be gathered \n please report the problem", "ok");
                    }
                }
                //Toast.makeText(RoadStartPageActivity.this, "The machine's unique ID could not be gathered"+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                break;
            case CNxRisk.LIC_UNSUPPORTED_VER:
                if (autoStart==false) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                    getLiscenseUpdate();
                    img_road_green.setVisibility(View.VISIBLE);
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "This licensing file is not supported by this version of the SDK \n please report the problem", "ok");
                        // Toast.makeText(RoadStartPageActivity.this, "This licensing file is not supported by this version of the SDK"+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case CNxRisk.LIC_INTERNAL_ERROR:
                if (autoStart==false) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                    getLiscenseUpdate();
                    img_road_green.setVisibility(View.VISIBLE);
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "an internal licensing error occurred \n please report the problem", "ok");
                        //Toast.makeText(RoadStartPageActivity.this, " an internal licensing error occurred"+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case CNxRisk.LIC_NO_MAP_AUTHZ:
                if (autoStart==false) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                    getLiscenseUpdate();
                    img_road_green.setVisibility(View.VISIBLE);
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "The SDK does not have the necessary map authorizations \n please report the problem", "ok");
                        //  Toast.makeText(RoadStartPageActivity.this, "The SDK does not have the necessary map authorizations"+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case CNxRisk.MAPDIR_NOT_FOUND:
                if (autoStart==false) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                    getLiscenseUpdate();
                    img_road_green.setVisibility(View.VISIBLE);
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "The map’s directory could not be found or is empty \n please report the problem", "ok");
                        //Toast.makeText(RoadStartPageActivity.this, "The map’s directory could not be found or is empty"+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case CNxRisk.MAPDIR_NOT_OPENING:
                if (autoStart==false) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                    getLiscenseUpdate();
                    img_road_green.setVisibility(View.VISIBLE);
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "The map’s directory could not be opened \n please report the problem", "ok");
                        // Toast.makeText(RoadStartPageActivity.this, "The map’s directory could not be opened"+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case CNxRisk.SVS_INVALID:
                if (autoStart==false) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                    getLiscenseUpdate();
                    img_road_green.setVisibility(View.VISIBLE);
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "The SVS files names are invalid \n please report the problem", "ok");
                        //Toast.makeText(RoadStartPageActivity.this, "The SVS files names are invalid "+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case CNxRisk.SVS_NOT_OPENING:
                if (autoStart==false) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                    getLiscenseUpdate();
                    img_road_green.setVisibility(View.VISIBLE);
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "The SVS files could not be open \n please report the problem", "ok");
                        // Toast.makeText(RoadStartPageActivity.this, "The SVS files could not be open"+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case CNxRisk.SVS_ALREADY_EXISTS:
                if (autoStart==false) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                    getLiscenseUpdate();
                    img_road_green.setVisibility(View.VISIBLE);
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "The SVS files are already loaded to memory \n please report the problem", "ok");
                        //Toast.makeText(RoadStartPageActivity.this, "The SVS files are already loaded to memory"+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case CNxRisk.INVALID_SVS:
                if (autoStart==false) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                    getLiscenseUpdate();
                    img_road_green.setVisibility(View.VISIBLE);
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "The files are not in a correct SVS format \n please report the problem", "ok");
                        //Toast.makeText(RoadStartPageActivity.this, "The files are not in a correct SVS format"+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case CNxRisk.INVALID_SVS_VER:
                if (autoStart==false) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                    getLiscenseUpdate();
                    img_road_green.setVisibility(View.VISIBLE);
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "The files are in a deprecated SVS format \n please report the problem", "ok");
                        // Toast.makeText(RoadStartPageActivity.this, "The files are in a deprecated SVS format"+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case CNxRisk.INVALID_UNLOCK_KEY:
                if (autoStart==false) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                    getLiscenseUpdate();
                    img_road_green.setVisibility(View.VISIBLE);
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "The SVS files key does not match the given unlock key \n please report the problem", "ok");
                        // Toast.makeText(RoadStartPageActivity.this, "The SVS files key does not match the given unlock key "+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case CNxRisk.VALIDATION_FAILED:
                if (autoStart==false) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                    getLiscenseUpdate();
                    img_road_green.setVisibility(View.VISIBLE);
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "The SVS file %s failed on validation \n please report the problem", "ok");
                        //Toast.makeText(RoadStartPageActivity.this, "The SVS file %s failed on validation "+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case CNxRisk.SVS_NOT_FOUND:
                if (autoStart==false) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                    getLiscenseUpdate();
                    img_road_green.setVisibility(View.VISIBLE);
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "The SVS files could not be found \n please report the problem", "ok");
                        //Toast.makeText(RoadStartPageActivity.this, "The SVS files could not be found"+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case CNxRisk.NOT_ENOUGH_MEM:
                if (autoStart==false) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                    getLiscenseUpdate();
                    img_road_green.setVisibility(View.VISIBLE);
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "not enough memory to open the SVS files \n please report the problem", "ok");
                        // Toast.makeText(RoadStartPageActivity.this, "not enough memory to open the SVS files"+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case CNxRisk.EXPIRED_SVS_FILE:
                if (autoStart==false) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                    getLiscenseUpdate();
                    img_road_green.setVisibility(View.VISIBLE);
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "The SVS files are expired \n please report the problem", "ok");
                        //Toast.makeText(RoadStartPageActivity.this, "The SVS files are expired"+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case CNxRisk.LIC_ERROR:
                if (autoStart==false) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                    getLiscenseUpdate();
                    img_road_green.setVisibility(View.VISIBLE);
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "Invalid or expired BeNomad license. Failed to load SVS file \n please report the problem", "ok");
                        // Toast.makeText(RoadStartPageActivity.this, "Invalid or expired BeNomad license. Failed to load SVS file"+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case CNxRisk.DOWNLOAD_ERROR:
                if (mapStatus==false) {
                    text.setVisibility(View.VISIBLE);
                    text.setText("Please wait until your map file is updating.....");
                    txt_view_road_start.setClickable(false);
                    File mfile;
                    mfile = new File(DEMO_WORKING_PATH,DEMO_MAP_SUB_PATH);
                    if (!mfile.exists()) {
                        mfile.mkdirs();
                    }
                    File file2 = new File(DEMO_WORKING_PATH,DEMO_MAP_SUB_PATH);
                    if (file2.list().length>0) {
                        if (deleteDir(file2)) {
                            downloadMapFile();
                        }
                    } else {
                        downloadMapFile();
                    }

                }else{
                    if (autoStart==false) {
                        MapDownload = true;
                        txt_view_road_start.setClickable(false);
                        profile.setClickable(false);
                        more_start.setClickable(false);
                        genCert();
                        licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                        getLiscenseUpdate();
                        img_road_green.setVisibility(View.VISIBLE);
                        if (Constants.subType.contains("FOREGROUND")) {
                            Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "Map download failed \n please report the problem", "ok");
                            //Toast.makeText(RoadStartPageActivity.this, "download failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                break;
            case CNxRisk.GRAPHIC_CHART_ERROR:
                if (autoStart==false) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                    getLiscenseUpdate();
                    img_road_green.setVisibility(View.VISIBLE);
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "Invalid graphic chart to display the map \n please report the problem", "ok");
                        // Toast.makeText(RoadStartPageActivity.this, "Invalid graphic chart to display the map"+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case CNxRisk.UNKNOWN_LICENSING_ERROR:
                if (autoStart==false) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                    getLiscenseUpdate();
                    img_road_green.setVisibility(View.VISIBLE);
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "Unknown error during BeNomad license checking \n please report the problem", "ok");
                        // Toast.makeText(RoadStartPageActivity.this, "unknown error during BeNomad license checking"+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case CNxRisk.UNKNOWN_OPENING_SVS_ERROR:
                if (autoStart==false) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                    getLiscenseUpdate();
                    img_road_green.setVisibility(View.VISIBLE);
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "Unknown error during map file loading \n please report the problem", "ok");
                        // Toast.makeText(RoadStartPageActivity.this, "unknown error during map file loading"+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case CNxRisk.NO_TRUCK_ATTRIBUTS:
                if (autoStart==false) {
                    MapDownload = true;
                    txt_view_road_start.setClickable(false);
                    profile.setClickable(false);
                    more_start.setClickable(false);
                    genCert();
                    licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                    getLiscenseUpdate();
                    img_road_green.setVisibility(View.VISIBLE);
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "No file has specific truck attribute definition, the map matching will not operate, the user must ask for a truck compatible maps or only specify a passenger car transportation mode \n please report the problem", "ok");
                        //Toast.makeText(RoadStartPageActivity.this, "no file has specific truck attribute definition, the map matching will not operate, the user must ask for a truck compatible maps or only specify a passenger car transportation mode"+mNxRisk.m_iJourneyState, Toast.LENGTH_SHORT).show();
                    }
                }
                break;

        }
    }
    public static  synchronized boolean deleteDir(File dir) {
        if (dir.isDirectory()&&dir.list().length>0) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty so delete it
        return dir.delete();
    }
    private void downloadMapFile() {
        img_view_road_green_pause.setVisibility(View.VISIBLE);
        img_view_road_green_pause.setImageResource(R.drawable.image_phone_rond_gris_download_b01);
        img_road_green.setVisibility(View.INVISIBLE);
        img_view_road_green_center.setVisibility(View.INVISIBLE);
        img_view_road_green_gear.setVisibility(View.INVISIBLE);
        text.setVisibility(View.VISIBLE);
        text.setTextColor(getColor(R.color.white));
        text.setText("Please wait until your map file is updating.....");
        Retrofit.Builder builder=new Retrofit.Builder().baseUrl(BASE_URL);
        Retrofit retrofit=builder.build();
        FileDownloadClient fileDownloadClient=retrofit.create(FileDownloadClient.class);
        Call<ResponseBody> call=fileDownloadClient.downloadFile(Constants.referredBy, Preference_Details.getLoginSessID(), Constants.companyID, Preference_Details.getUserID(), Constants.type_map_lib);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            boolean success = writeResponseBodyToDisk(response.body());
                            mapStatus=success;
                            //img_road_green.setImageResource(R.drawable.image_phone_rond_gris_download_b01);
                            img_road_green.setVisibility(View.VISIBLE);
                            img_view_road_green_pause.setVisibility(View.INVISIBLE);
                            img_view_road_green_center.setVisibility(View.INVISIBLE);
                            img_view_road_green_gear.setVisibility(View.INVISIBLE);
                            text.setVisibility(View.INVISIBLE);
                            txt_view_road_start.setClickable(true);
                            profile.setClickable(true);
                            more_start.setClickable(true);
                            MapDownload=true;
                            if (success==true) {
                                mPrefernce_Manager.setString(Preference_Keys.LOginKeys.previousmapCode,Preference_Details.getMapCode());
                                mapCall=true;
                            }else {
                                downloadMapFile();
                            }
                            // Toast.makeText(getApplicationContext(), "yes" + success, Toast.LENGTH_SHORT).show();
                            //unzipmapfile
                            String zipFile = String.valueOf(DEMO_WORKING_PATH)+"/Road_LIC_CERT1.zip";
                            String unzipLocation =String.valueOf(DEMO_WORKING_PATH);
                            Decompress d = new Decompress(zipFile, unzipLocation);
                            d.unzip();
                            txt_view_road_stop.setClickable(true);
                            // ProgressDialog.dismissProgress();
                            //startActivity(new Intent(RoadInstalingMapViewPageActivity.this, GetStartPageActivity.class));
                        }
                    } else if (response.code() == 412) {
                        downloadMapFile();
                        try {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                if (Constants.subType.contains("FOREGROUND")) {
                                    Util_functions.ShowValidateAlertError(getApplicationContext(), object1.getString("message"), "OK");
                                    //  ProgressDialog.dismissProgress();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        downloadMapFile();
                        String Error = response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text = obj.getString("message");
                        if (Constants.subType.contains("FOREGROUND")) {
                            Util_functions.ShowValidateAlertError(getApplicationContext(), text, "OK");
                            //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.txt_error),true);
                            //ProgressDialog.dismissProgress();
                        }
                    }
                } catch (Exception e) {
                    downloadMapFile();
                    e.printStackTrace();
                    //ProgressDialog.dismissProgress();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t.toString().contains("Unable to resolve host")) {
                    mapCount = 0;
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, getString(R.string.check_internet), "OK");
                    }
                }else {
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, getString(R.string.check_internet), "OK");
                    }
                }
                //ShowAlertError(RoadStartPageActivity.this,t.getMessage(),"OK",RoadStartPageActivity.this);
                downloadMapFile();
                // Toast.makeText(getApplicationContext(), "no", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    public class Decompress {
        private String _zipFile;
        private String _location;
        public Decompress(String zipFile, String location) {
            _zipFile = zipFile;
            _location = location;
            _dirChecker("");
        }
        public void unzip() {
            try  {
                FileInputStream fin = new FileInputStream(_zipFile);
                ZipInputStream zin = new ZipInputStream(fin);
                ZipEntry ze = null;
                byte b[] = new byte[1024];
                while ((ze = zin.getNextEntry()) != null) {
                    Log.v("Decompress", "Unzipping " + ze.getName());

                    if(ze.isDirectory()) {
                        _dirChecker(ze.getName());
                    } else {
                        FileOutputStream fout = new FileOutputStream(_location + ze.getName());

                        BufferedInputStream in = new BufferedInputStream(zin);
                        BufferedOutputStream out = new BufferedOutputStream(fout);

                        int n;
                        while ((n = in.read(b,0,1024)) >= 0) {
                            out.write(b,0,n);

                        }

                        zin.closeEntry();
                        out.close();
                    }

                }

                zin.close();


            } catch(Exception e) {
                Log.e("Decompress", "unzip", e);
            }
        }
        private void _dirChecker(String dir) {
            File f = new File(_location + dir);
            if(!f.isDirectory()) {
                f.mkdirs();
            }
        }
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(DEMO_WORKING_PATH,"Road_LIC_CERT1.zip");
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    //Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
    private void NxAlert_speech() {
        if (NxAlert==1){
            if(alter_status==0){
                txt_to_speech(TTS);
                alter_status++;
            }

            // Toast.makeText(RoadStartPageActivity.this,TTS, Toast.LENGTH_SHORT).show();
        }
        else if (NxAlert==2){

            if(alter_status==0){
                txt_to_speech(TTS);
                alter_status++;
            }
            //  Toast.makeText(RoadStartPageActivity.this,TTS, Toast.LENGTH_SHORT).show();
        }
        else if (NxAlert==3){
            if(alter_status==0){
                txt_to_speech(TTS);
                alter_status++;
            }
            // Toast.makeText(RoadStartPageActivity.this,TTS, Toast.LENGTH_SHORT).show();
        }
        else if (NxAlert==4) {
            if(alter_status==0){
                txt_to_speech(TTS);
                alter_status++;
            }
            // Toast.makeText(RoadStartPageActivity.this,TTS, Toast.LENGTH_SHORT).show();
        }
        else if (NxAlert==5){
            if(alter_status==0){
                txt_to_speech(TTS);
                alter_status++;
            }
            // Toast.makeText(RoadStartPageActivity.this,TTS, Toast.LENGTH_SHORT).show();
        }
        else if (NxAlert==6){
            if(alter_status==0){
                txt_to_speech(TTS);
                alter_status++;
            }
            // Toast.makeText(RoadStartPageActivity.this,TTS, Toast.LENGTH_SHORT).show();
        }
        else if (NxAlert==7){
            if(alter_status==0){
                txt_to_speech(TTS);
                alter_status++;
            }
            //  Toast.makeText(RoadStartPageActivity.this,TTS, Toast.LENGTH_SHORT).show();
        }
        else if (NxAlert==8){
            if(alter_status==0){
                txt_to_speech(TTS);
                alter_status++;
            }
            //Toast.makeText(RoadStartPageActivity.this,TTS, Toast.LENGTH_SHORT).show();
        }
        else if (NxAlert==9){
            if(alter_status==0){
                txt_to_speech(TTS);
                alter_status++;
            }
            // Toast.makeText(RoadStartPageActivity.this,TTS, Toast.LENGTH_SHORT).show();
        }
        else if (NxAlert==10){
            if(alter_status==0){
                txt_to_speech(TTS);
                alter_status++;
            }
            //  Toast.makeText(RoadStartPageActivity.this,TTS, Toast.LENGTH_SHORT).show();
        }
        else if (NxAlert==11){
            if(alter_status==0){
                txt_to_speech(TTS);
                alter_status++;
            }
            // Toast.makeText(RoadStartPageActivity.this,TTS, Toast.LENGTH_SHORT).show();
        }
        else if (NxAlert==12){
            if(alter_status==0){
                txt_to_speech(TTS);
                alter_status++;
            }
            //  Toast.makeText(RoadStartPageActivity.this,TTS, Toast.LENGTH_SHORT).show();
        }
        else if (NxAlert==13){
            if(alter_status==0){
                txt_to_speech(TTS);
                alter_status++;
            }
            // Toast.makeText(RoadStartPageActivity.this,TTS, Toast.LENGTH_SHORT).show();
        }
        else if (NxAlert==14){
            if(alter_status==0){
                txt_to_speech(TTS);
                alter_status++;
            }
            //Toast.makeText(RoadStartPageActivity.this,TTS, Toast.LENGTH_SHORT).show();
        }
        else if (NxAlert==15){
            if(alter_status==0){
                txt_to_speech(TTS);
                alter_status++;
            }
            //  Toast.makeText(RoadStartPageActivity.this,TTS, Toast.LENGTH_SHORT).show();
        }
        else if (NxAlert==16){
            if(alter_status==0){
                txt_to_speech(TTS);
                alter_status++;
            }
            // Toast.makeText(RoadStartPageActivity.this,TTS, Toast.LENGTH_SHORT).show();
        }
        else if (NxAlert==17){
            if(alter_status==0){
                txt_to_speech(TTS);
                alter_status++;
            }
            //Toast.makeText(RoadStartPageActivity.this,TTS, Toast.LENGTH_SHORT).show();
        }
        else if (NxAlert==18){
            if(alter_status==0){
                txt_to_speech(TTS);
                alter_status++;
            }
            //   Toast.makeText(RoadStartPageActivity.this,TTS, Toast.LENGTH_SHORT).show();
        }

        else if (NxAlert==19){
            if(alter_status==0){
                txt_to_speech(TTS);
                alter_status++;
            }
            // Toast.makeText(RoadStartPageActivity.this,TTS, Toast.LENGTH_SHORT).show();
        }
        else if (NxAlert==20){
            if(alter_status==0){
                txt_to_speech(TTS);
                alter_status++;
            }
            // Toast.makeText(RoadStartPageActivity.this,TTS, Toast.LENGTH_SHORT).show();
        }
        else if (NxAlert==21){
            if(alter_status==0){
                txt_to_speech(TTS);
                alter_status++;
            }
            // Toast.makeText(RoadStartPageActivity.this,TTS, Toast.LENGTH_SHORT).show();
        }
        else if (NxAlert==22){
            if(alter_status==0){
                txt_to_speech(TTS);
                alter_status++;
            }
            // Toast.makeText(RoadStartPageActivity.this,TTS, Toast.LENGTH_SHORT).show();
        }
        else if (NxAlert==23){
            if(alter_status==0){
                txt_to_speech(TTS);
                alter_status++;
            }
            // Toast.makeText(RoadStartPageActivity.this,TTS, Toast.LENGTH_SHORT).show();
        }
        else if (NxAlert==24){
            if(alter_status==0){
                txt_to_speech(TTS);
                alter_status++;
            }
            // Toast.makeText(RoadStartPageActivity.this,TTS, Toast.LENGTH_SHORT).show();
        }
        else if (NxAlert==25){
            if(alter_status==0){
                txt_to_speech(TTS);
                alter_status++;
            }
            // Toast.makeText(RoadStartPageActivity.this,TTS, Toast.LENGTH_SHORT).show();
        }
        else if (NxAlert==26){
            if(alter_status==0){
                txt_to_speech(TTS);
                alter_status++;
            }
            // Toast.makeText(RoadStartPageActivity.this,TTS, Toast.LENGTH_SHORT).show();
        }
        else if (NxAlert==27){
            if(alter_status==0){
                txt_to_speech(TTS);
                alter_status++;
            }
            // Toast.makeText(RoadStartPageActivity.this,TTS, Toast.LENGTH_SHORT).show();
        }
        else if (NxAlert==28){
            if(alter_status==0){
                txt_to_speech(TTS);
                alter_status++;
            }
            // Toast.makeText(RoadStartPageActivity.this,TTS, Toast.LENGTH_SHORT).show();
        }
        else {
            alter_status=0;
            // Toast.makeText(RoadStartPageActivity.this, "No Alert Detected" + TTS, Toast.LENGTH_SHORT).show();
        }

    }


    private void stopDrive() {
        // txt_view_road_start.setText("START");
        locationManager.removeUpdates(RoadStartPageActivity.this);
        txt_view_road_start.setClickable(false);
        autoStart=false;
        mIsRunning = false;
        /*img_view_road_start.setImageResource(R.drawable.start_button);*/
        //((ToggleButton)findViewById(R.id.toggleRun)).setChecked(mIsRunning);
        // mTimerHandler.removeCallbacks(mTimerRunnable);
        if (Anim!=null) {
            if (Anim.isRunning()) {
                Anim.stop();
            }
        }
        sCount = 0;
        closeAPI();
        stopLocationButtonClick();
        text.setVisibility(View.VISIBLE);
        text.setTextColor(getColor(R.color.white));
        //text.setText("Please wait until your score is calculated . . . .");
        mTimerHandler.removeCallbacksAndMessages(null);
        mTimerHandler.removeCallbacks(mTimerRunnable);
        txt_view_road_start.setClickable(false);
        //batteryHandler.removeCallbacksAndMessages(null);
        // handler_gps.removeCallbacksAndMessages(null);


    }



    @Override
    public void onBackPressed() {
        if (MapDownload==true){

        }else {

        }

    }

    @Override
    protected void onPause() {
        // LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        super.onPause();

    }

    private void getStartActivity() {
        startActivity(new Intent(this,RoadStartPageActivity.class));
        autoState=0;
        handlerPause.removeCallbacksAndMessages(null);

    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("phone")) {
                if (ContextCompat.checkSelfPermission(RoadStartPageActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RoadStartPageActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE},1);
//requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
                }
                else {
                    if (startFirst==1){
                        if (!checkPermission()) {
                            requestPermission();
                        } else {
                            if (autoStart==false) {
                                if (isCheckedStop == true && isCheckedStatus == true) {
                                    if (ring_count == 12) {
                                        ring_count = 11;
//                                    txt_to_speech("Please Avoid Using Mobile");
                                        ring= MediaPlayer.create(RoadStartPageActivity.this,R.raw.pleaseavoid_usingmobile);
                                        ring.start();
                                    } else if (ring_count == 13) {
                                        if (ring_status == true) {
                                            call_starttime = getUniTime();
                                            ring_count = 11;
//                                        txt_to_speech("Please Avoid Using Mobile");
                                            ring= MediaPlayer.create(RoadStartPageActivity.this,R.raw.pleaseavoid_usingmobile);
                                            ring.start();
                                        }
                                    } else if (ring_count == 14) {
                                        if (ring_status == true && ring_status_miss == true && speedd*3.6 > 12) {
                                            ring_count = 11;
                                            ring_status = false;
                                            ring_status_miss = false;
                                            CNxFullStat[] FullStat = mJniFunction.GetCloudStat();
                                            if (FullStat.length >= 0) {
                                                for (int i = 0; i < FullStat.length; i++) {
                                                    distanceKm += FullStat[i].m_fDistance / 1000;
                                                    alertDuration += FullStat[i].m_fDuration /60;
                                                    if (!FullStat[i].m_sZipCode.equals("99999")) {
                                                        distanceZip = FullStat[i].m_sZipCode;
                                                    }
                                                }
                                                call_endtime = getUniTime();
                                                getDuration(String.valueOf(call_starttime), String.valueOf(call_endtime));
                                                ring_count = 11;
                                                setAlertImageAlone(getAlertImageString(29),alert_img);

                                                GPSEditDeviceReq gpsEditDeviceReq = new GPSEditDeviceReq();
                                                gpsEditDeviceReq.setCategory(getString(R.string.gps_ALERT_DATA));
                                                gpsEditDeviceReq.setCompanyId(Constants.companyID);
                                                gpsEditDeviceReq.setSubCategory(getSubCategoryValueUpper(29));
                                                GPSEditDeviceReq.DeviceDataField deviceField = new GPSEditDeviceReq.DeviceDataField();
                                                deviceField.setAlert(diff_dur);
                                                deviceField.setAnticipation("");
                                                deviceField.setDrivingScore("");
                                                deviceField.setDrivingSkill("");
                                                deviceField.setLatitude(String.valueOf(lat));
                                                deviceField.setDrivingSkill("");
                                                deviceField.setKiloMeter("");
                                                deviceField.setLongitude(String.valueOf(longi));
                                                deviceField.setRisk(String.valueOf(Math.round(mNxRisk.m_fRisk * 100)));
                                                deviceField.setSpeed(String.valueOf(speedd * 3.6));
                                                deviceField.setTotalKiloMeter("");
                                                deviceField.setSelfConfidence("");
                                                deviceField.setTravelTime(String.valueOf(alertDuration));
                                                deviceField.setGpsCount("NET "+net_connectivity+" GPS "+gps_connectivity);
                                                //deviceField.setGpsCount(String.valueOf(totalSnr));
                                                deviceField.setAccelerX(String.valueOf(Accx));
                                                deviceField.setAccelerY(String.valueOf(Accy));
                                                deviceField.setAccelerZ(String.valueOf(Accz));
                                                deviceField.setSenRotate(String.valueOf(currentDegree));
                                                deviceField.setRideTime(String.valueOf(getUniTime()));
                                                deviceField.setAlertId("Attended");
                                                deviceField.setAlertValue("29");
                                                deviceField.setAlertKiloMeter(String.valueOf(distanceKm));
                                                deviceField.setAlertTime("");
                                                deviceField.setZipCode(distanceZip);
                                                deviceField.setRideDate("");
                                                deviceField.setGyroscopeX(String.valueOf(gyroX));
                                                deviceField.setGyroscopeY(String.valueOf(gyroY));
                                                deviceField.setGyroscopeZ(String.valueOf(gyroZ));
                                                deviceField.setMagnatometerX(String.valueOf(magX));
                                                deviceField.setMagnatometerY(String.valueOf(magY));
                                                deviceField.setMagnatometerZ(String.valueOf(magZ));
                                                deviceField.setAlertName(Util_functions.getSubCategoryValueLowerr(29));
                                                deviceField.setEhorizonLength(String.valueOf(FullStat.length));
                                                deviceField.setGpsTimeDiff(String.valueOf(mTimeDiffGPSSeconds));
                                                deviceField.setEngineState(String.valueOf(state));
                                                gpsEditDeviceReq.setDeviceDataField(deviceField);
                                                gpsEditDeviceReq.setDivisionId(Constants.divisionId);
                                                gpsEditDeviceReq.setModuleId(Constants.moduleID);
                                                gpsEditDeviceReq.setPropertyId(Preference_Details.getPropertyID());
                                                gpsEditDeviceReq.setPortionId(Preference_Details.getPortionID());
                                                gpsEditDeviceReq.setSectionId(Preference_Details.getSectID());
                                                gpsEditDeviceReq.setStatus(getString(R.string.gps_status));
                                                gpsEditDeviceReq.setType(getString(R.string.gps_type_data));
                                                gpsEditDeviceReq.setUserId(Preference_Details.getUserID());
                                                gpsEditDeviceReq.setDeviceId(Constants.GPS_deviceID);
                                                GPSEditDeviceReq.DeviceDataField1 deviceField1 = new GPSEditDeviceReq.DeviceDataField1();
                                                deviceField1.setDriverState(String.valueOf(mInstantDriverSignature.m_iDriverState));
                                                deviceField1.setDrivingStyle(String.valueOf(mInstantDriverSignature.m_iDrivingStyle));
                                                deviceField1.setRiskStyle(String.valueOf(mInstantDriverSignature.m_iRiskStyle));
                                                deviceField1.setTrafficScore(String.valueOf(mInstantDriverSignature.m_iTrafficScore));
                                                deviceField1.setTransportMode(String.valueOf(mInstantDriverSignature.m_iTransportId));
                                                gpsEditDeviceReq.setDeviceDataField1(deviceField1);
                                                if(Constants.subType.contains("BACKGROUND")) {
                                                    gpsEditDeviceReq.setSubType(Constants.subType + "_AUTO");
                                                }else {
                                                    gpsEditDeviceReq.setSubType(Constants.subType + "_"+Constants.auto_manuel_status);
                                                }
                                                Log.d("alert_log", gpsEditDeviceReq.toString());
//                                            txt_to_speech("Please Avoid Using Mobile");
                                                ring= MediaPlayer.create(RoadStartPageActivity.this,R.raw.pleaseavoid_usingmobile);
                                                ring.start();
                                                if (Util_functions.ConnectivityCheck(getApplicationContext(), RoadStartPageActivity.this)) {
                                                    mconnectStatus = true;
                                                    setGpsDataAPI(gpsEditDeviceReq);
                                                }
                                            }
                                            distanceKm = 0.0;
                                            distanceZip = "";
                                            alertDuration=0;

                                        } else if (ring_status == true && ring_status_miss == false) {
                                            ring_status = false;
                                            ring_status_miss = false;


                                        }
                                    }
                                } else if (isCheckedStop == true && isCheckedStart == true) {
                                    handler_ring.removeCallbacks(runnable_ring);
                                }
                            }


                        }
                    }

                }
                //Toast.makeText(RoadStartPageActivity.this, String.valueOf(ring_count)+ String.valueOf(ring_status)+String.valueOf(ring_status_miss), Toast.LENGTH_SHORT).show();
            }
            /*if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                final String sender = intent.getStringExtra("Sender");
                String otp = message.substring(message.lastIndexOf(",")+2);
                otp = otp.substring(0,4);
                *//*otpOnlyTextView.setText(message.replaceAll("\\D+", ""));
                fullmessageTextView.setText(sender + " : " + message);*//*
                //Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                Log.e("OTP MESSSGE", message);
            }*/
        }
    };



    public void onResume() {
        super.onResume();

        if (ride_ongoing) {
            // Toast.makeText(getApplicationContext(),"running"+Preference_Details.getUserID(),Toast.LENGTH_SHORT).show();
        }

        StartScrVisibility = true;

        if (Constants.img_profile==true) {
            try {
                Picasso.get()
                        .load(Constants.BASE_URL + Constants.file + "?referredBy=" + Constants.referredBy + "&sessionId=" + Preference_Details.getLoginSessID() + "&companyId=" + Constants.companyID + "&userId=" + Preference_Details.getUserID() + "&type=" + Constants.type_profile)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(profile);
                Constants.img_profile=false;
                //profile_image.setImageURI(Uri.parse(Constants.BASE_URL+Constants.file+"?referredBy="+Constants.referredBy+"&sessionId="+Preference_Details.getLoginSessID()+"&companyId="+Constants.companyID+"&userId="+Preference_Details.getUserID()+"&type="+Constants.type_profile));
            } catch (Exception e) {
                profile.setBackground(getDrawable(R.drawable.profile_man));
                e.printStackTrace();
            }
        }else {
            try {
                Picasso.get()
                        .load(Constants.BASE_URL + Constants.file + "?referredBy=" + Constants.referredBy + "&sessionId=" + Preference_Details.getLoginSessID() + "&companyId=" + Constants.companyID + "&userId=" + Preference_Details.getUserID() + "&type=" + Constants.type_profile)
                        .into(profile);
                //profile_image.setImageURI(Uri.parse(Constants.BASE_URL+Constants.file+"?referredBy="+Constants.referredBy+"&sessionId="+Preference_Details.getLoginSessID()+"&companyId="+Constants.companyID+"&userId="+Preference_Details.getUserID()+"&type="+Constants.type_profile));
            } catch (Exception e) {
                profile.setBackground(getDrawable(R.drawable.profile_man));
                e.printStackTrace();
            }
        }
        /*if (!checkPermission()){
            requestPermission();
        }*/
        if (runnable==false){
            LocalBroadcastManager.getInstance(RoadStartPageActivity.this).registerReceiver(myReceiver,new IntentFilter(GPSTracker.ACTION_BROADCAST));
        }

        LocalBroadcastManager.getInstance(RoadStartPageActivity.this).registerReceiver(receiver, new IntentFilter("phone"));
        // Toast.makeText(getApplicationContext(), "resume", Toast.LENGTH_SHORT).show();
        // txt_to_speech("hello have a nice trip with road Drive It Safe");
        sm.registerListener(gyroListener, sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(gyroListener, sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(gyroListener, sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL);
        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(RoadStartPageActivity.this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
        // Resuming location updates depending on button state and
        // allowed permissions
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);
        // currentDegree = degree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public SensorEventListener gyroListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }
        public void onSensorChanged(SensorEvent event) {
            try {
                if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
                    float[] values = event.values;
                    xx = values[0];
                    yy = values[1];
                    zz = values[2];
                }
                if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                    float[] values = event.values;
                    gyroX = values[0];
                    gyroY = values[1];
                    gyroZ = values[2];
                }
                if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                    float[] values = event.values;
                    magX = values[0];
                    magY = values[1];
                    magZ = values[2];
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (location != null) {
            i++;
            // Toast.makeText(RoadStartPageActivity.this, "location was changed", Toast.LENGTH_SHORT).show();
            lat= location.getLatitude();
            longi = location.getLongitude();
            speedd = location.getSpeed();
            currentDegree = location.getBearing();
            Log.e("Location latitude longitude","lat "+lat+" long "+longi);
            Accx=xx;
            Accy=yy;
            Accz=zz;
            if (autoStart==true) {
                setstartDet();
                //initAPI();
                mJniFunction.SetGPSData(Double.valueOf(lat), Double.valueOf(longi), satelliteCount, Double.valueOf(currentDegree), Double.valueOf(speedd) * 3.6, mTimeDiffGPSSeconds);
                if (Accx != 0.0) {
                    mJniFunction.GetAccelDataWithRisk(Accx, Accy, Accz, mNxRisk);
                }
                long[] CurrEhorizon = mJniFunction.GetCurrEHorizon();
                if (CurrEhorizon != null && CurrEhorizon.length > 4) {
                    autoState = mNxRisk.m_iSafetyNexEngineState;
                } else {
                    autoState = mNxRisk.m_iSafetyNexEngineState;
                }
                Log.d("ENGINE STATE", String.valueOf(Double.valueOf(lat)) + String.valueOf(Double.valueOf(longi)) + String.valueOf(satelliteCount) + String.valueOf(Double.valueOf(currentDegree)) + String.valueOf(Double.valueOf(speedd) * 3.6) + String.valueOf(mTimeDiffGPSSeconds));


                //if (autoState == 1 || mNxRisk.m_bAutoStartDetection == true || Double.valueOf(speedd) * 3.6 >= 20) {
                if (autoState == 1 || mNxRisk.m_bAutoStartDetection == true || Double.valueOf(speedd) * 3.6 >= 20) {
                    i = 0;
                    //initAPI();
                    Constants.auto_manuel_status = "AUTO";
                    txt_view_road_start.performClick();
                    // animLoader();
                }

            }
            mLastLocationMillis = System.currentTimeMillis();
            if (autoStart == false && StartScrVisibility == true){
                //getUpdateHandler();
            }

            // text_eg.setText(String.valueOf(state)+" "+String.valueOf(autoStart)+" start"+String.valueOf(txt_view_road_start.getVisibility())+" stop"+String.valueOf(txt_view_road_stop.getVisibility()));
            /*else{
                txt_view_road_start.setVisibility(View.INVISIBLE);
                txt_view_road_stop.setVisibility(View.VISIBLE);
                txt_start.setText("STOP");
                txt_view_road_stop.setClickable(true);
                current_speed_val.setVisibility(View.VISIBLE);
                curr_speed_km.setVisibility(View.VISIBLE);
                text_eg.setText(String.valueOf(state)+" "+String.valueOf(autoStart)+" start"+String.valueOf(txt_view_road_start.getVisibility())+" stop"+String.valueOf(txt_view_road_stop.getVisibility()));
                //autoStart=false;
               // switch_state();
            }*/

        }

    }
    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {

        //Toast.makeText(getApplicationContext(), "loc 1:"+locations.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFlushComplete(int requestCode) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    private String getSubCategoryValueUpper(int m_iNxAlertValue) {
        String data ="";
        if (m_iNxAlertValue>0) {
            if ( m_iNxAlertValue==1) {
                data = "ACCIDENT";
            } else if ( m_iNxAlertValue==2) {
                data = "ANIMAL_CROSSING";
            } else if ( m_iNxAlertValue==3) {
                data = "CAUTION";
            } else if ( m_iNxAlertValue==4) {
                data = "CONGESTION";
            } else if ( m_iNxAlertValue==5) {
                data = "CURVE";
            } else if ( m_iNxAlertValue==6) {
                data = "HILL";
            } else if ( m_iNxAlertValue==7) {
                data = "HILL_DOWNWARDS";
            } else if ( m_iNxAlertValue==8) {
                data = "HILL_UPWARDS";
            } else if ( m_iNxAlertValue==9) {
                data = "ICY_CONDITIONS";
            } else if ( m_iNxAlertValue==10) {
                data = "INTERSECTION";
            } else if ( m_iNxAlertValue==11) {
                data = "LANE_MERGE";
            } else if ( m_iNxAlertValue==12) {
                data = "LOW_GEAR_AREA";
            } else if ( m_iNxAlertValue==13) {
                data = "NARROW_ROAD";
            } else if ( m_iNxAlertValue==14) {
                data = "NO_OVERTAKING";
            } else if ( m_iNxAlertValue==15) {
                data = "NO_OVERTAKING_TRUCKS";
            } else if ( m_iNxAlertValue==16) {
                data = "PEDESTRIAN_CROSSING";
            } else if ( m_iNxAlertValue==17) {
                data = "PRIORITY";
            } else if ( m_iNxAlertValue==18) {
                data = "PRIORITY_TO_ONCOMING_TRAFFIC";
            } else if ( m_iNxAlertValue==19) {
                data = "RAILWAY_CROSSING";
            } else if ( m_iNxAlertValue==20) {
                data = "RISK_OF_GROUNDING";
            } else if ( m_iNxAlertValue==21) {
                data = "SCHOOL_ZONE";
            } else if ( m_iNxAlertValue==22) {
                data = "SLIPPERY_ROADS";
            } else if ( m_iNxAlertValue==23) {
                data = "STOP_SIGN";
            } else if ( m_iNxAlertValue==24) {
                data = "TRAFFIC_LIGHT";
            } else if ( m_iNxAlertValue==25) {
                data = "TRAMWAY_CROSSING";
            } else if ( m_iNxAlertValue==26) {
                data = "WIND";
            } else if ( m_iNxAlertValue==27) {
                data = "WINDING_ROAD";
            } else if ( m_iNxAlertValue==28) {
                data = "YIELD";
            }else if ( m_iNxAlertValue==29) {
                data = "MOBILE_USE";
            }else if ( m_iNxAlertValue==30) {
                data = "OVER_SPEED";
            }else if(m_iNxAlertValue == 31){
                data = "SUDDEN_BRAKING";
            }else if(m_iNxAlertValue == 32){
                data = "FORCE_ACCELERATION";
            }else if(m_iNxAlertValue == 33){
                data = "SEVERE_CORNERING";
            }
        }

        return data;
    }
    public void onCompletion(MediaPlayer arg0) {
        try {
            if (currentTrack < Constants.tracks.length - 1 && sCount == 0) {
                currentTrack++;
                arg0 = MediaPlayer.create(getApplicationContext(), Constants.tracks[currentTrack]);
                arg0.setOnCompletionListener(this);
                arg0.start();
            } else if (currentTrack>0){

            }else {
                arg0.release();
                arg0 = MediaPlayer.create(getApplicationContext(), Constants.tracks[1]);
                //arg0.setOnCompletionListener(this);
                arg0.start();
                //FadeOut(6000);
                //FadeIn(3000);
                //arg0.release();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onPositiveButtonClicked(AlertDialog alertDialog) {
        if (!mconnectStatus.booleanValue()) {
            ProgressDialog.dismissProgress();
        } else {

        }
    }
    private boolean checkPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            // int result = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
            return /*result == PackageManager.PERMISSION_GRANTED &&*/ result1 == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                startActivityForResult(intent, 101);
                startFirst=0;
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 101);
            }
        } else {
            //below android 11
            boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            if (!hasPermission) {
                ActivityCompat.requestPermissions(RoadStartPageActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        101);
            }
        }
    }
    @Override
    public void onNegativeButtonClicked() {

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
                        } else
                        {
                        } } else if (response.code() == 412) {
                        try {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                if (Constants.subType.contains("FOREGROUND")) {
                                    Util_functions.ShowValidateAlertError(getApplicationContext(), object1.getString("message"), "OK");
                                    ProgressDialog.dismissProgress();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        String Error = response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text = obj.getString("message");
                        if (Constants.subType.contains("FOREGROUND")) {
                            Util_functions.ShowValidateAlertError(getApplicationContext(), text, "OK");
                        }
                        //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.txt_error),true);
                        ProgressDialog.dismissProgress();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ProgressDialog.dismissProgress();

                }
            }

            @Override
            public void onFailure(Call<List<GetPortionResponse>> call, Throwable t) {
                if (Constants.subType.contains("FOREGROUND")) {
                    ShowAlertError(RoadStartPageActivity.this, t.getMessage(), "OK", RoadStartPageActivity.this);
                    t.printStackTrace();
                    ProgressDialog.dismissProgress();
                }

            }
        });
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
            diff_dur = hours+":"+mins+":"+sec; // updated value every1 second
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //    private void changeRedImage() {
//        new Handler().postDelayed(new Runnable(){
//            @Override
//            public void run() {
//                /* Create an Intent that will start the Menu-Activity. */
//                img_road_green.setBackgroundResource(R.mipmap.img_road_red);
////                startActivity(new Intent(RoadStartPageActivity.this,DashboardActivity.class));
//            }
//        }, SPLASH_DISPLAY_Image);
//    }
    private void getLiscenseUpdate() {
        RestMethods restMethods = RestClient.buildHTTPClient();
        try {
            licenseUpdateReq.setCode(mJniFunction.GetDeviceId());
        }catch (Exception e){
            e.printStackTrace();
        }
        licenseUpdateReq.setCompanyId(Constants.companyID);
        licenseUpdateReq.setDivisionId(Constants.divisionId);
        licenseUpdateReq.setModuleId(Constants.moduleID);
        licenseUpdateReq.setStatus("REGISTERED");
        licenseUpdateReq.setName("License");
        licenseUpdateReq.setUserId(Preference_Details.getUserID());
        licenseUpdateReq.setType(Constants.type_lic);
        licenseUpdateReq.setCategory(Constants.referredBy);
        restMethods.LicenseUpdateReq(Constants.referredBy,Preference_Details.getLoginSessID(),licenseUpdateReq).enqueue(new Callback<UserSettingResponse>() {
            @Override
            public void onResponse(Call<UserSettingResponse> call, Response<UserSettingResponse> response) {
                try{
                    if (response.code()==201) {
                        txt_view_road_start.setClickable(true);
                        profile.setClickable(true);
                        more_start.setClickable(true);
                    }  else if(response.code()==412){
                        try {
                            String Error=response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for(int i=0;i<jsonArray1.length();i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                if (Constants.subType.contains("FOREGROUND")) {
                                    Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, object1.getString("message"), "OK");
                                }
                            }
                            //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.resp_empty),true);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    } else {
                        String Error = response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text = obj.getString("message");
                        if (Constants.subType.contains("FOREGROUND")) {
                            Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, text, "OK");
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<UserSettingResponse> call, Throwable t) {
                if (t.toString().contains("Unable to resolve host")) {
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, getString(R.string.check_internet), "OK");
                    }
                }if (Constants.subType.contains("FOREGROUND")) {
                    ShowAlertError(RoadStartPageActivity.this,t.getMessage(),"OK",RoadStartPageActivity.this);
                }

                t.printStackTrace();
            }
        });
    }

    //download License
    private void genCert() {

        if(!checkPermission()){
            requestPermission();
        } else {
            //delete files in folder
            File mfile;
            mfile = new File(DEMO_WORKING_PATH, SUB_FOLD_NAME);
            if (!mfile.exists()) {
                mfile.mkdirs();
                mfile.setReadable(true);
            }

            try {
                File file2 = new File(DEMO_WORKING_PATH, SUB_FOLD_NAME);
                if (file2.list().length > 0) {
                    if (deleteDir(file2)) {
                        callService();
                    }
                } else {
                    callService();
                }
            }catch (Exception e){
                e.printStackTrace();
            }


        }

    }


    private void callService() {
        try {
            InputStream prm_InputStream;
            String key_Content ="Content";
            String key_StreamUrl ="KeyStoreFileUri";
            JSONObject jsonRespObj= null;
            File mfile,mfile2;
            String imei ="";
            mfile = new File(DEMO_WORKING_PATH, SUB_FOLD_NAME);
            if (!mfile.exists()) {
                mfile.mkdirs();
                mfile.setReadable(true);
            }

            /*mfile2 = new File(mfile.getAbsoluteFile(),SUB_FOLD_MAP_NAME);
            if (!mfile2.exists()) {
                mfile2.mkdirs();
            }*/
            String NAMESPACE = "http://schemas.xmlsoap.org/wsdl/";
            String METHOD_NAME = "NewLicenseEx";
            String SOAP_ACTION = WSDL_URL+"#"+METHOD_NAME;
            String TAG = "soap";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("orderId","3523");
            request.addProperty("HSUID",mJniFunction.GetDeviceId());//c29376c5bc64018a 944aa576a404e4b6
            request.addProperty("purchaseId",mJniFunction.GetDeviceId());//869956053990902 CWGV610M45AFW
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(WSDL_URL);
            androidHttpTransport.debug = true;
            List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
            headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode("montbleu:sEhBMQu58Cs".getBytes())));
            androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
            String responseDump = androidHttpTransport.responseDump;
            String requestDump = androidHttpTransport.requestDump;
            Log.e(TAG, requestDump);
            Log.e(TAG, responseDump);
            Log.d("Response ---->> :", String.valueOf(envelope.getResponse()));
            String[] split1 = envelope.getResponse().toString().split("licenseContent=");
            String[] split2  = split1[1].split(";");
            Log.d("License :",split2[0]);
            try {
                // Create new file
                String content = split2[0];
                if (!checkPermission()) {
                    requestPermission();
                } else {
                    //License File
                    String path = SUB_FOLD_NAME + DEMO_LICENSE_FILE;
                    File file = new File(DEMO_WORKING_PATH + path);
                    // If file doesn't exists, then create it
                    if (!file.exists()) {
                        file.createNewFile();
                        file.setWritable(true);
                        file.setReadable(true);
                    }
                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    // Write in file
                    bw.flush();
                    bw.write(content);
                    // Close connection
                    bw.close();

                    //bin_lic_file
                    String path1 = SUB_FOLD_NAME + DEMO_BIN_LICENSE_FILE;
                    File file1 = new File(DEMO_WORKING_PATH + path1);
                    // If file doesn't exists, then create it
                    if (!file1.exists()) {
                        file1.createNewFile();
                        file1.setWritable(true);
                        file1.setReadable(true);
                    }
                    BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file1), "Cp1252"));
                    // Write in file
                    bw1.flush();
                    bw1.write(decodeBase64(content));
                    bw1.close();
                }
            }
            catch(Exception e){
                System.out.println(e);
                if (e.getMessage().contains("Failed to connect")){
                    mapCount=0;
                    if (Constants.subType.contains("FOREGROUND")) {
                        Util_functions.ShowValidateAlertError(RoadStartPageActivity.this, "Check your Internet Connection", "ok");
                    }
                }
                // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            prm_InputStream = getResponseStream(Constants.prm_RequestURL+"imei="+mJniFunction.GetDeviceId()+"&orderId=3523_006");
            jsonRespObj = parseResponse(prm_InputStream);
            try {
                String nexyxml =jsonRespObj.getString(key_Content);
                String url =	jsonRespObj.getString(key_StreamUrl);
                genNexyLicense(nexyxml);
                genNexyCertificate(url);
                //  downloadMap();
            } catch (Exception e){
                e.printStackTrace();
            }
            //	String  jsonRespObj
            //putOnDeviceContentFromJSON(jsonRespObj,key_Content);
            //putOnDeviceURLStreamFromJSON(jsonRespObj,key_StreamUrl);
        } catch (Exception e) {
            System.out.println("Error"+e);
            //Toast.makeText(RoadInstalingMapViewPageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String decodeBase64(String coded){
        byte[] dataa = Base64.decode(coded, Base64.DEFAULT);
        String decoded = null;
        try {
            // decoded = new String(dataa, "WINDOWS-1252");
            decoded = new String(dataa, "WINDOWS-1252");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return decoded;
    }

    public  InputStream getResponseStream(String prm_RequestURL) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(prm_RequestURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            String tempToken = getEncode("montbleu:qmjRAE541sdZ");
            conn.setRequestProperty("Authorization", "Basic " + tempToken);
            int ResponseCode = conn.getResponseCode();
            if (ResponseCode != HttpURLConnection.HTTP_OK/*200*/) {
                //   Log.e(TAG, "Error HTTP : " + ResponseCode);
                //   Log.e(TAG, "Error HTTP : " + conn.getResponseMessage());
                throw new RuntimeException("Failed : HTTP error code : " +
                        ResponseCode);
            }
            InputStream tempStream = conn.getInputStream();
            return tempStream;
        } catch (Exception e) {
            //    Log.e(TAG, "GET ERROR " + e.getMessage());
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public JSONObject parseResponse(InputStream prm_InputStream) {
        String tempString = "";
        int currentSize = 0, cursor;
        JSONObject tempObj = null;
        byte[] tempBuffer = new byte[1048576];
        while (true) {
            try {
                if (!((cursor = prm_InputStream.read(tempBuffer)) != -1)) break;
                tempString += new String(tempBuffer);
                currentSize += cursor;
                tempObj = new JSONObject(tempString);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tempObj;
    }

    public String getEncode(String prm_input) {
        byte[] data;
        try {
            data = prm_input.getBytes("ASCII");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            data = new byte[] {0};
        }
        byte[] dataEnc = Base64.encode(data, Base64.URL_SAFE);
        String encodedBody = new String(dataEnc);
        return encodedBody;
    }
    private void genNexyCertificate(String yourUrl) {
        addCertificate(yourUrl);
    }
    private void addCertificate(String yourUrl) {
        try{
            try {
                URL u = new URL(yourUrl);
                InputStream is = u.openStream();

                DataInputStream dis = new DataInputStream(is);

                byte[] buffer = new byte[1024];
                int length;

                FileOutputStream fos = new FileOutputStream(new File(DEMO_WORKING_PATH+SUB_FOLD_NAME+SUB_PATH));
                while ((length = dis.read(buffer))>0) {
                    fos.write(buffer, 0, length);
                }

            } catch (MalformedURLException mue) {
                Log.e("SYNC getUpdate", "malformed url error", mue);
            } catch (IOException ioe) {
                Log.e("SYNC getUpdate", "io error", ioe);
            } catch (SecurityException se) {
                Log.e("SYNC getUpdate", "security error", se);
            }

        } catch(Exception e){
            e.printStackTrace();
        }

    }


    private void genNexyLicense(String nexyxml) {
        try{
            File file = new File(DEMO_WORKING_PATH+SUB_FOLD_NAME+DEMO_LICENSE_FILE_NEXYAD);
            // If file doesn't exists, then create it
            //  checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 101);
            if (!file.exists()) {
                file.createNewFile();
                file.setWritable(true);
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            // Write in file
            bw.flush();
            bw.write(nexyxml);
            // Close connection
            bw.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean checkPermissions_loc() {
        return  PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(RoadStartPageActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startFirst=1;
                    if (EndBoolStat==true) {
                        EndBoolStat = false;
                        File mfile;
                        mfile = new File(DEMO_WORKING_PATH, DEMO_MAP_SUB_PATH);
                        if (!mfile.exists()) {
                            mfile.mkdirs();
                        }
                        try {
                            zip_file = new ZipFile(DEMO_WORKING_PATH+"/Road_LIC_CERT1.zip");
                            zip_file.size();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        initAPI();
                        if (isLicOK == false) {
                            MapDownload = true;
                            txt_view_road_start.setClickable(false);
                            profile.setClickable(false);
                            more_start.setClickable(false);
                            genCert();
                            licenseUpdateReq.setSubCategory("LICENCE_RECREATED");
                            getLiscenseUpdate();
                        } else if (tempLicInfo.m_lDaysToExpiration <= 2) {
                            MapDownload = true;
                            txt_view_road_start.setClickable(false);
                            profile.setClickable(false);
                            more_start.setClickable(false);
                            genCert();
                            licenseUpdateReq.setSubCategory("LICENCE_RENEWED");
                            getLiscenseUpdate();
                        }
                        if (!mfile.exists() || mfile.list().length == 0 ) {
                            MapDownload = true;
                            txt_view_road_start.setClickable(false);
                            profile.setClickable(false);
                            more_start.setClickable(false);
                            mfile.delete();
                            downloadMapFile();
                        }else {
                            try {
                                if (!(mfile.listFiles().length>=zip_file.size()-1)){
                                    MapDownload = true;
                                    txt_view_road_start.setClickable(false);
                                    profile.setClickable(false);
                                    more_start.setClickable(false);
                                    mfile.delete();
                                    downloadMapFile();
                                }
                            }catch (Exception e){
                                MapDownload = true;
                                txt_view_road_start.setClickable(false);
                                profile.setClickable(false);
                                more_start.setClickable(false);
                                mfile.delete();
                                downloadMapFile();
                            }

                        }
                        if (!Preference_Details.getMapCode().equals(Preference_Details.getPreviousMapCode())) {
                            if (mfile.exists()){
                                if (deleteDir(mfile)) {
                                    MapDownload = true;
                                    txt_view_road_start.setClickable(false);
                                    profile.setClickable(false);
                                    more_start.setClickable(false);
                                    mfile.delete();
                                    downloadMapFile();
                                }
                            }
                        }


                    }
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                }else {
                    requestPermission();
                }
                return;
            }
            case 103 : {
                boolean foreground = false, background = false;

                for (int i = 0; i < permissions.length; i++) {
                    if (permissions[i].equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        //foreground permission allowed
                        if (grantResults[i] >= 0) {
                            foreground = true;
                            //Toast.makeText(getApplicationContext(), "Foreground location permission allowed", Toast.LENGTH_SHORT).show();
                            continue;
                        } else {
                            openSettings();
                            // switchStBg.setChecked(false);
                            //Toast.makeText(getApplicationContext(), "Location Permission denied", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                    if (permissions[i].equalsIgnoreCase(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                        if (grantResults[i] >= 0) {
                            foreground = true;
                            background = true;
                            mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.gpsLoc,true);
                            //Toast.makeText(getApplicationContext(), "Background location location permission allowed", Toast.LENGTH_SHORT).show();
                        }  else{
                            AlertDialog(this,"App in background functionality will be disabled","OK","CANCEL",RoadStartPageActivity.this);
                            // Toast.makeText(getApplicationContext(), "Background location location permission denied", Toast.LENGTH_SHORT).show();
                        }

                    }
                }




            }

            case 34:{
                if (requestCode == 34) {
                    if (grantResults.length <= 0) {
                        // If user interaction was interrupted, the permission request is cancelled and you
                        // receive empty arrays.
                        Log.i(TAG, "User interaction was cancelled.");
                    } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // Permission was granted.
                        if (mService!=null){
                            if (Preference_Details.getStartOnbg()==true && StartScrVisibility == true){
                                mService.requestLocationUpdates(RoadStartPageActivity.this);
                            }

                        }

                    } else {
                        // Permission denied.
                        // setButtonsState(false);
                        /*Snackbar.make(
                                        findViewById(R.id.activity_road_start),
                                        R.string.permission_denied_explanation,
                                        Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.settings, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // Build intent that displays the App settings screen.
                                        Intent intent = new Intent();
                                        intent.setAction(
                                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package",
                                                getApplicationContext().getPackageName(), null);
                                        intent.setData(uri);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                })
                                .show();*/
                    }
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void AlertDialog(Context context, String title, String positiveBtnText, String negativeBtnText,
                            final OnDialogButtonClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutView = inflater.inflate(R.layout.alert, null);
        builder.setView(layoutView);
        alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (Constants.alertcount==true) {
            alertDialog.show();
            Constants.alertcount=false;
        }
        TextView alert_title = layoutView.findViewById(R.id.alert_title);
        Button alert_yes = layoutView.findViewById(R.id.alert_yes);
        Button alert_no = layoutView.findViewById(R.id.alert_no);
        alert_title.setText(title);
        alertDialog.setCancelable(false);
        alert_yes.setText(positiveBtnText);
        alert_no.setText(negativeBtnText);
        alert_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.hide();
                alertDialog.cancel();
                alertDialog.dismiss();
                Constants.alertcount=true;
                //   switchStBg.setChecked(false);
            }
        });

        alert_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLocationPermission();
                alertDialog.hide();
                alertDialog.cancel();
                alertDialog.dismiss();
                Constants.alertcount=true;
            }
        });
    }


    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(GPSTracker.EXTRA_LOCATION);
            if (location != null) {

                if (autoStart == false && stopStatus == false){
                    Constants.sendCount++;
                    //getUpdateHandler();
                }

                //Toast.makeText(MainActivity.this, Utils.getLocationText(location),Toast.LENGTH_SHORT).show();
                //if (Preference_Details.getStartOnbg() == true && StartScrVisibility == true) {
                if (Preference_Details.getStartOnbg() == true){
                    Log.d("LOOCATIONS", String.valueOf(Util_functions.getLocationText(location)));
                    mLastLocationMillis = System.currentTimeMillis();
                    lat = location.getLatitude();
                    longi = location.getLongitude();
                    speedd = location.getSpeed();
                    currentDegree = location.getBearing();
                    //Log.d("LOOCATIONS",String.valueOf(lat)+String.valueOf(longi));
                    Accx = xx;
                    Accy = yy;
                    Accz = zz;
                    if (Preference_Details.getBgStart() == false && Constants.BgStart == false ) {
                        if (mNxRisk.m_bAutoStartDetection == true || Double.valueOf(speedd) * 3.6 >= 20) {
                            i = 0;
                            //initAPI();
                            Constants.auto_manuel_status = "AUTO";
                            txt_view_road_start.performClick();
                        }
                    }else if(Preference_Details.getBgStart() == true && Constants.BgStart == false && Constants.GPS_deviceID == "" && autoStart == true){
                        mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.BgStart,false);
                        Constants.BgStart=false;
                    }

                    if (Preference_Details.getBgStart() == true && Constants.BgStart == true && isChecked == true && Constants.GPS_deviceID != ""){
                        isChecked = false;
                        mTimerHandler.removeCallbacksAndMessages(null);
                        mTimerHandler.removeCallbacks(mTimerRunnable);
                        mIsRunning = true;
                        autoStart=false;
                        //img_view_road_start.setImageResource(R.drawable.stop_button);
                        //mTimerHandler.postDelayed(mTimerRunnable, Constants.DEMO_FIRST_DELAY);
                    }
                }
            }
        }
    }

    private void setAlertImageAlone(String imgName,ImageView imageView){
        try {
            String uri = "@drawable/"+imgName;
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            imageView.setImageResource(imageResource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void weatherData(Double lat,Double lngi){
        okhttp3.Call apiCall = OpenWeatherSingleton.getInstance().getForecastByCity(String.valueOf(lat),String.valueOf(lngi));
        apiCall.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e("SimpleWeather", Log.getStackTraceString(e));
            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                final String result = response.body().string();
                Log.d("SimpleWeather[Request]", result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if(obj.getInt("cod") == 401){
                        Log.e("SimpleWeather", obj.toString());
                    }else{

                        // obj.getJSONObject("weather");

                        // String weather = new Gson().toJson(obj.getString("weather"));
                        try{
                            JSONObject jsonObject = new JSONObject(obj.toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("weather");
                            JSONObject jsonObject1 = jsonObject.getJSONObject("main");
                            String weatVal = String.valueOf(Math.round(Double.valueOf(jsonObject1.getString("temp"))));

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    weather_deg_val.setText(weatVal+"°C");
                                    try {
                                        getIconFromWeather(Integer.parseInt(jsonArray.getJSONObject(0).getString("id")),jsonObject1);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        } catch(Exception e){
                            e.printStackTrace();
                        }


                    }
                } catch (JSONException e) {
                    Log.e("SimpleWeather", Log.getStackTraceString(e));
                }
            }
        });
    }

    public void getIconFromWeather(int id, JSONObject jsonObject1){
        try{
            if(id >= 200 && id < 300){
                weather_img.setImageDrawable(getDrawable(R.drawable.thunder));
            }else if(id >= 300 && id < 500){
                weather_img.setImageDrawable(getDrawable(R.drawable.drizzle));
            }else if(id >= 500 && id < 600){
                weather_img.setImageDrawable(getDrawable(R.drawable.rain));
            }else if(id >= 600 && id < 700){
                weather_img.setImageDrawable(getDrawable(R.drawable.snow));
            }else if(id >= 700 && id < 800){
                weather_img.setImageDrawable(getDrawable(R.drawable.atmosphere));
            }else if (id == 800) {
                weather_img.setImageDrawable(getDrawable(R.drawable.clear));
            }else if(id >= 800){
                weather_img.setImageDrawable(getDrawable(R.drawable.clouds));
            }else{
                weather_img.setImageDrawable(getDrawable(R.drawable.def_cloud));
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    private final class LongOperation extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            // txt.setText(result);
            txt_view_road_start.setVisibility(View.INVISIBLE);
            txt_view_road_stop.setVisibility(View.VISIBLE);
            txt_start.setText("STOP");
            txt_view_road_stop.setClickable(true);
            current_speed_val.setVisibility(View.VISIBLE);
            autoStart=false;
            // You might want to change "executed" for the returned string
            // passed into onPostExecute(), but that is up to you
        }
    }

    public class handlerAsync extends AsyncTask<Boolean,Boolean,Boolean>{

        @Override
        protected Boolean doInBackground(Boolean... booleans) {
            updateRisk();
            doNextStep();
            return true;
        }
    }
}