package com.montbleu.Utils;

import android.content.Context;
import android.os.Environment;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.montbleu.model.ScoreListModel;

import java.util.ArrayList;


public class Constants extends MultiDexApplication {

    public Context context;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setContext(this);
    }

    public void setContext(Context con) {
        context = con;
    }

    public Context getContext() {
        return context;
    }
    //public static final String BASE_URL =   "https://road-api-services.herokuapp.com/api/v1/";
    public static final String BASE_URL =   "http://44.202.40.112:8080/api/v1/";
    //public static final String  DEMO_WORKING_PATH = "/storage/emulated/0/"+Environment.DIRECTORY_DOCUMENTS;
    public static final String  DEMO_WORKING_PATHH = "/storage/emulated/0/"+Environment.DIRECTORY_DOCUMENTS;
    public static final String  DEMO_IN_FILE_NAME = "/datasafety.csv";
    public static final String  DEMO_OUT_FILE_NAME = "/datasafetyOut.csv";
    //public static final String  DEMO_MAP_SUB_PATH = "/road_android_library/";
    public static final String  DEMO_MAP_SUB_PATH = "/ruduroad_android_library/";
    /*public static final String  DEMO_LICENSE_FILE = "/BeNomad.lic";
    public static final String  DEMO_BIN_LICENSE_FILE = "/BeNomad.bin";
    public static final String  DEMO_LICENSE_FILE_NEXYAD = "/nexyad.lic";*/
    public static final String  DEMO_LICENSE_FILE = "/BeNomad.lic";
    public static final String  DEMO_BIN_LICENSE_FILE = "/BeNomad.bin";
    public static final String  DEMO_LICENSE_FILE_NEXYAD = "/nexyad.lic";
    public static final String SUB_PATH ="/nexyad.cer";
    public static final String  DEMO_UNLOCK_KEY = "58KJ1bsd";
    public static final int LOCATION_INTERVAL = 500;
    public static final int FASTEST_LOCATION_INTERVAL = 500;
    public static final int  DEMO_LANGUAGE = 2; //2 for English
    public static final int  DEMO_FIRST_DELAY = 500; //wait 2 seconds to begin read data
    public static final int  DEMO_EXIT_DELAY = 3000; //wait 60 seconds to exit app when license is not valid
    public static final int  DEMO_RUN_DELAY = 500; //wait 50 milliseconds to read next data
    public static final int READ_PHONE_STATE = 102;
    public static String M_ANDROID_ID = "";
    public static final String WSDL_URL ="https://licensing.benomad.com/licensing/licenseService.php?bnd:licenseWSDL";
    public static final String prm_RequestURL = "https://nexyadsafetynexlicensingprod.azurewebsites.net/api/NexyadLicence?";
    public static final String SUB_FOLD_NAME = "/Road_LIC_CERT1";
    public static final String MAP_ZIP = "/Road_LIC_CERT1.zip/";
    public static final String SUB_FOLD_MAP_NAME = "/Map";
    public static final String multipartFile = "multipartFile";
    public static final String file = "file";
    public static final String modulePreference = "modulePreference";
    public static final String type_app_logo = "ROAD_APP_LOGO_PNG";
    public static final String type_map_lib = "ROAD_APP_ANDROID_LIBRARY_ZIP";
    public static final String type_map_update = "ANDROID_LIBRARY_ZIP";
    public static final String Static_SessionID = "202120212021202120212021";
    public static final String moduleID ="modu00000000000000000001";
    public static final String divisionId ="divi00000000000000000001";
    public static final String companyID ="comp00000000000000000001";
    public static final String companyPreference ="companyPreference";
    public static final String dashboard = "dashboard";
    public static final String countryID ="coun00000000000000000004";
    public static final String  userID = "user00000000000000000001";
    public static final String  static_porID = "000000000000000000000000";
    public static final String referredBy ="ANDROID";
    public static final String userSession = "userSession";
    public static final String continent = "continent";
    public static final String country = "country";
    public static final String userFeedback = "userFeedback";
    public static final String register = "user";
    public static final String otpverify = "userVerification";
    public static final String verifyType = "USER_ACTIVATION";
    public static final String newForgotPassType = "USER_FORGOT_PASSWORD";
    public static final String userPreference = "userPreference";
    public static final String SettingType = "APP_SETTINGS";
    public static final String mapSettingsType = "MAP_SETTINGS";
    public static final String SettingUpdateType = "Personal Settings";
    public static final String AppSettingGetType = "APP_SETTINGS";
    public static final String userDeviceData = "deviceData";
    public static final String userdevice="device";
    public static final String userQuery="query";
    public static final String type_live_Data = "OBD_DEVICE_DATA";
    public static final String type_lic = "LICENCE";
    public static final String type_profile = "USER_PROFILE_PICTURE";
    public static final String category = "LIVE_DATA";
    public static final String type_GPS_Device_GET = "SPEEDO_METER_DEVICE";
    public static final String dashboard_type= "DRIVING_BEHAVIOUR_LATEST_SCORE";
    public static final String gps_device_Data_Type ="SPEEDO_METER_DEVICE_DATA";
    public static final String gps_DEVICE_AND_DATA_LIST = "SPEEDO_METER_DEVICE_AND_DATA_LIST";
    public static final String gps_ALERT_COUNT="SPEEDO_METER_DEVICE_AND_DATA_COUNT";
    public static final String USER_MONTHLY_DRIVING_SCORE = "USER_MONTHLY_DRIVING_SCORE";
    public static final String USER_DASHBOARD_TOP_LINE_ITEM = "USER_DASHBOARD_TOP_LINE_ITEM";
    public static final String USER_DASHBOARD_RISK_ALERT ="USER_DASHBOARD_RISK_ALERT";
    public static String lottiePath ="lottie_1.json";
    public static final String type_device_Data = "OBD_DEVICE";
    public static final String type_role_module = "ROLE_MODULE";
    public static String map_code = "001";
    public static final String type_portion = "DRIVER";
    public static final String portion = "portion";
    public static final String module = "roleModule";
    public static final String  category_Password = "USER_PASSWORD";
    public static Boolean firstTimeUserStatus = false;
    public static String Bledata = "";
    public static Boolean beepValue = true;
    public static String regSessionID = "";
    public static String regCompID = "";
    public static String regCountrID = "";
    public static String regUserID = "";
    public static String regUsername = "";
    public static String regEmailID ="";
    public static String regStatus ="";
    public static String regPass = "";
    public static String regPin = "";
    public static String mob_num= "";
    public static String regType= "";
    public static String frst_name= "";
    public static Boolean img_profile= false;
    public static String pwd="";
    public static int sCount = 0;
    public static int mapCount = 0;
    public static String GPS_deviceID = "";
    public static boolean BgStart = false;
    public static boolean mapStatus = false;
    public static ArrayList<ScoreListModel> scoreListModelArrayList = new ArrayList<>();
    public static String subCategory = "subCategory";
    public static int co_pilot = 70;
    public static int stateId = 0;
    public static Boolean speedLimit =true ;
    public static Boolean voice = true ;
    public static Boolean startOnBg =false ;
    public static Boolean StartScrVisibility = false;
    public static Boolean StartbtnClick = false;
    public static final String secID ="sect00000000000000000001";
    public static final String propID ="prop00000000000000000001";
    public static final String porID ="port00000000000000000001";
    public static int alertId = 0;
    public static int startid = 0;
    public static int apicount = 0;
    public static int apicount_speed=0;
    public static Boolean BgState = false;
    public static String subType = "FOREGROUND";
    public static int toneStatus = 0;
    public static int score_frag = 0;
    public static boolean ring_status=false;
    public static boolean ring_status_miss=false;
    public static int ring_count =11;
    public static int speedLimit_count =1;
    public static Boolean autoStart = true ;
    public static Boolean gps_second = false ;
    public static int page_change_pos = -1;
    public static Boolean no_ride_vis = false ;
    public static Boolean alertcount=true;
    public static Boolean runnable = false;
    public static Boolean StartDis = false;
    public static Boolean Gps_Loc = false;
    public static String BluetoothName = "";
    public static final int REQUEST_CODE_PERMISSIONS = 101;
    static final String KEY_REQUESTING_LOCATION_UPDATES = "requesting_locaction_updates";
    public static int bg_stop= 0;
    public static String auto_manuel_status = "MANUAL";
    public static Boolean ride_ongoing = false;
    public static int alert_show = 0;
    public static Boolean batteryStatus = false;
    public static boolean net_end=false;
    public static String net_connectivity = "NO";
    public static String gps_connectivity = "NO";
    public static boolean gps_check=false;
    public static Boolean stopStatus = false;
    public static int stopCount = 0;
    public static Long stopDuration = 0L;
    public static String tripStatus = "";
    public static  int alert_count_v2=0;
    public static int horiz_count = 0;
    public static int[] tracks = new int[3];
    public static int sendCount = 0;
    public static int speedcount=0;
    public static int handlercount=0;
}
