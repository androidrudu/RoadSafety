package com.montbleu.roadsafety.activity;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static com.montbleu.Utils.Constants.DEMO_BIN_LICENSE_FILE;
import static com.montbleu.Utils.Constants.DEMO_LICENSE_FILE;
import static com.montbleu.Utils.Constants.DEMO_LICENSE_FILE_NEXYAD;
import static com.montbleu.Utils.Constants.DEMO_MAP_SUB_PATH;
import static com.montbleu.Utils.Constants.SUB_FOLD_MAP_NAME;
import static com.montbleu.Utils.Constants.SUB_FOLD_NAME;
import static com.montbleu.Utils.Constants.SUB_PATH;
import static com.montbleu.Utils.Constants.WSDL_URL;
import static com.montbleu.Utils.Constants.mapCount;
import static com.montbleu.Utils.Constants.mapStatus;
import static com.montbleu.roadsafety.activity.LoginPageActivity.PERMISSION_REQUEST_CODE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.montbleu.Retrofit.FileDownloadClient;
import com.montbleu.Retrofit.RestClient;
import com.montbleu.Retrofit.RestMethods;
import com.montbleu.Utils.Constants;
import com.montbleu.Utils.PreferenceManager;
import com.montbleu.Utils.Preference_Details;
import com.montbleu.Utils.Util_functions;
import com.montbleu.interfaces.OnDialogButtonClickListener;
import com.montbleu.model.ContinentResponse;
import com.montbleu.model.CountryResponse;
import com.montbleu.model.LicenseUpdateReq;
import com.montbleu.model.UserSettingResponse;
import com.montbleu.roadsafety.R;
import com.montbleu.safetyNexDemo.CNxDemoData;
import com.nexyad.jndksafetynex.CNxLicenseInfo;
import com.nexyad.jndksafetynex.JNDKSafetyNex;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
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
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RoadInstalingMapViewPageActivity extends AppCompatActivity implements OnDialogButtonClickListener {

    private ImageView mapView;
    String mContinentArray, mCountryArray;
    List<CountryResponse> countryRespItems;
    List<ContinentResponse> continentRespItems;

    TextView tv_percentage;
    Handler handler ,handlerNet;
    Thread thread,threadNet;
    LicenseUpdateReq licenseUpdateReq=new LicenseUpdateReq();
    private PreferenceManager mPrefernce_Manager=PreferenceManager.getInstance();
    String DEMO_WORKING_PATH;
    String DEMO_WORKING_PATH_MAP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());
        setContentView(R.layout.activity_road_instaling_map_view_page);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        File DEMO_WORKING_PATH_DIR = new File(getFilesDir().getAbsolutePath());
        File file = new File(DEMO_WORKING_PATH_DIR,"rudu");
        file.mkdir();
        DEMO_WORKING_PATH_MAP = getFilesDir().getAbsolutePath();
        DEMO_WORKING_PATH = getFilesDir().getAbsolutePath() + "/rudu";
        countryRespItems = new ArrayList<>();
        continentRespItems = new ArrayList<>();
        mapView = findViewById(R.id.map_view);
        tv_percentage=findViewById(R.id.tv_percentage);
        mContinentArray = Preference_Details.getContinentArray();
        mCountryArray = Preference_Details.getCountryArray();
        handler=new Handler();
        thread = new Thread(runnable);
        handlerNet=new Handler();
        threadNet = new Thread(runnableNet);
        if (Util_functions.ConnectivityCheck(RoadInstalingMapViewPageActivity.this,RoadInstalingMapViewPageActivity.this).booleanValue()) {
            thread.start();
        }
        else{

        }
        if (mContinentArray != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<ContinentResponse>>() {
            }.getType();
            continentRespItems = gson.fromJson(mContinentArray, type);
        }

        if (mCountryArray != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<CountryResponse>>() {
            }.getType();
            countryRespItems = gson.fromJson(mCountryArray, type);
        }


    }

    Runnable runnableNet=new Runnable() {
        @Override
        public void run() {
            handlerNet.postDelayed(this,1000);
            if (Util_functions.ConnectivityCheck(RoadInstalingMapViewPageActivity.this,RoadInstalingMapViewPageActivity.this).booleanValue()) {
                thread.start();
            }
        }
    };


    Runnable runnable = new Runnable(){
        public void run() {
            if (mapStatus==false) {
                handler.postDelayed(this, 500);
                if (mapCount==0){
                    if(!checkPermission()){
                        mapCount++;
                        tv_percentage.setText(mapCount +"%");
                        requestPermission();
                    } else {
                        mapCount=2;
                        tv_percentage.setText(mapCount +"%");
                    }
                }else if (mapCount>=2){
                    mapCount++;
                    if (mapCount==5){
                        initAPI();
                    }
                    tv_percentage.setText(mapCount +"%");
                }
            }
            if (mapStatus == true) {
                handler.postDelayed(this,250);
                mapCount++;
                tv_percentage.setText(mapCount +"%");
                if (mapCount == 100) {
                    mapCount=100;
                    handler.removeCallbacks(runnable);
                    startActivity(new Intent(RoadInstalingMapViewPageActivity.this, GetStartPageActivity.class));
                    finish();
                }
            }else if(mapCount>80){
                if (mapStatus==false) {
                    mapCount=0;
                    tv_percentage.setText(mapCount +"%");
                    handler.removeCallbacks(runnable);
                    if (Util_functions.ConnectivityCheck(RoadInstalingMapViewPageActivity.this,RoadInstalingMapViewPageActivity.this).booleanValue()) {
                        Util_functions.ShowAlertError(RoadInstalingMapViewPageActivity.this, "Something Went Wrong \nInternet is too slow", "ok", RoadInstalingMapViewPageActivity.this);
                    }
                }
            }


        }
    };

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {

    }



    private void initAPI() {
        String WorkingPath = DEMO_WORKING_PATH;
        String InputFile = WorkingPath +SUB_FOLD_NAME+ Constants.DEMO_IN_FILE_NAME;
        String OutputFile = WorkingPath +SUB_FOLD_NAME+ Constants.DEMO_OUT_FILE_NAME ;
        String LicenseFileBnd = WorkingPath +SUB_FOLD_NAME+Constants.DEMO_BIN_LICENSE_FILE;
        String LicenseFileNx = WorkingPath +SUB_FOLD_NAME+Constants.DEMO_LICENSE_FILE_NEXYAD;
        String MapSubPath = DEMO_WORKING_PATH_MAP + DEMO_MAP_SUB_PATH;
        String UnlockKey = Constants.DEMO_UNLOCK_KEY;
        int Language = 0;
        try {
            Language = Constants.DEMO_LANGUAGE;
        }catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        File tempPath[] = this.getExternalFilesDirs(null); //A call to this function seems needed to grant access to externals directories
        //mData = new CNxDemoData(null, OutputFile);
        CNxDemoData mData = new CNxDemoData(InputFile, OutputFile);
        JNDKSafetyNex mJniFunction = JNDKSafetyNex.GetInstance(this);
        //boolean isLicOK = mJniFunction.Birth(LicenseFileBnd, MapSubPath, UnlockKey, Language, LicenseFileNx);
        CNxLicenseInfo tempLicInfo = new CNxLicenseInfo();
        Boolean isLicOK = mJniFunction.Birth(LicenseFileBnd, MapSubPath, UnlockKey, Language, LicenseFileNx, tempLicInfo);
        if(isLicOK==false) {
            genCert();
        }
        if (mapStatus==false){
            downloadMapFile();
        }
    }

    //Map License

    private void genCert() {

        if(!checkPermission()){
            requestPermission();
        } else {
            //delete files in folder
            File mfile;
            mfile = new File(DEMO_WORKING_PATH, SUB_FOLD_NAME);
            if (!mfile.exists()) {
                mfile.mkdirs();
            }
            File file2 = new File(DEMO_WORKING_PATH, SUB_FOLD_NAME);
            if (file2.list().length>0) {
                if (deleteDir(file2)) {
                    callService();
                }
            } else {
                callService();
            }


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
                startActivityForResult(intent, 2296);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2296);
            }
        } else {
            //below android 11
            boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            if (!hasPermission) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);
            }        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2296) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    mapCount=2;
                    tv_percentage.setText(mapCount +"%");
                    //delete files in folder
                    File mfile;
                    mfile = new File(DEMO_WORKING_PATH, SUB_FOLD_NAME);
                    if (!mfile.exists()) {
                        mfile.mkdirs();
                    }
                    File file2 = new File(DEMO_WORKING_PATH, SUB_FOLD_NAME);
                    if (file2.list().length>0) {
                        if (deleteDir(file2)) {
                           // callService();
                        }
                    } else {
                       // callService();
                    }
                } else {
                    mapCount=0;
                    tv_percentage.setText(mapCount +"%");
                    //Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    mapCount=2;
                    //delete files in folder
                    File mfile;
                    mfile = new File(DEMO_WORKING_PATH, SUB_FOLD_NAME);
                    if (!mfile.exists()) {
                        mfile.mkdirs();
                    }
                    File file2 = new File(DEMO_WORKING_PATH, SUB_FOLD_NAME);
                    if (file2.list().length>0) {
                        if (deleteDir(file2)) {
                          //  callService();
                        }
                    } else {
                       // callService();
                    }
                } else
                {
                    mapCount=0;
                   // Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    private void callService() {
        InputStream prm_InputStream;
        String key_Content ="Content";
        String key_StreamUrl ="KeyStoreFileUri";
        JSONObject jsonRespObj= null;
        File mfile,mfile2;
        mfile = new File(DEMO_WORKING_PATH, SUB_FOLD_NAME);
        if (!mfile.exists()) {
            mfile.mkdirs();
        }
        mfile2 = new File(mfile.getAbsoluteFile(),SUB_FOLD_MAP_NAME);
        if (!mfile2.exists()) {
            mfile2.mkdirs();
        }
        String NAMESPACE = "http://schemas.xmlsoap.org/wsdl/";
        String METHOD_NAME = "NewLicenseEx";
        String SOAP_ACTION = WSDL_URL+"#"+METHOD_NAME;
        String TAG = "soap";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("orderId","3523");//3523 3988
        request.addProperty("HSUID",Constants.M_ANDROID_ID);//c29376c5bc64018a 944aa576a404e4b6
        request.addProperty("purchaseId",Constants.M_ANDROID_ID);//869956053990902 CWGV610M45AFW
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(WSDL_URL);
        androidHttpTransport.debug = true;
        try {
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

                if (e.getMessage().contains("Failed to connect")){
                    mapCount=0;
                    Util_functions.ShowValidateAlertError(RoadInstalingMapViewPageActivity.this,"Check your Internet Connection","ok");

                }
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            prm_InputStream = getResponseStream(Constants.prm_RequestURL+"imei="+Constants.M_ANDROID_ID+"&orderId=3523");
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
        } catch (Exception e) {
            System.out.println("Error"+e);
            //Toast.makeText(RoadInstalingMapViewPageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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


    public  void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(getApplicationContext(), "Permission already granted", Toast.LENGTH_SHORT).show();

        }
    }

    private void genNexyCertificate(String yourUrl) {
        //checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 101);.

/*		File file = new File(Environment.DIRECTORY_DCIM+SUB_FOLD_NAME);
		if (!file.exists()) {
			addCertificate(yourUrl);
		} else {*/

        addCertificate(yourUrl);
        /*	}*/
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


    //mapfiledownload
    private void downloadMapFile() {
        Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL);
        Retrofit retrofit=builder.build();
        FileDownloadClient fileDownloadClient=retrofit.create(FileDownloadClient.class);
        Call<ResponseBody> call=fileDownloadClient.downloadFile(Constants.referredBy, Preference_Details.getLoginSessID(), Constants.companyID, Preference_Details.getUserID(), Constants.type_map_lib);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            boolean success = writeResponseBodyToDisk(response.body());
                            getLiscenseUpdate();
                            mapStatus=success;
                            if (success==true) {
                                Util_functions.getMapUpdate(RoadInstalingMapViewPageActivity.this,RoadInstalingMapViewPageActivity.this);
                                licenseUpdateReq.setSubCategory("LICENCE_CREATED");
                            }
                            // Toast.makeText(getApplicationContext(), "yes" + success, Toast.LENGTH_SHORT).show();
                            //unzipmapfile
                            String zipFile = DEMO_WORKING_PATH +"/Road_LIC_CERT1.zip";
                            String unzipLocation =String.valueOf(DEMO_WORKING_PATH);
                            Decompress d = new Decompress(zipFile, unzipLocation);
                            d.unzip();

                        }
                    } else if (response.code() == 412) {
                        downloadMapFile();
                        try {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowValidateAlertError(getApplicationContext(), object1.getString("message"), "OK");
                                //  ProgressDialog.dismissProgress();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        downloadMapFile();
                        String Error = response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text = obj.getString("message");
                        Util_functions.ShowValidateAlertError(getApplicationContext(), text, "OK");
                    }
                } catch (Exception e) {
                    downloadMapFile();
                    e.printStackTrace();
                    //ProgressDialog.dismissProgress();

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                downloadMapFile();
                if (t.toString().contains("Unable to resolve host")){
                    mapCount=0;
                    Util_functions.ShowValidateAlertError(RoadInstalingMapViewPageActivity.this,getString(R.string.check_internet),"OK");
                }
                //Toast.makeText(getApplicationContext(), "no", Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    public void onPositiveButtonClicked(AlertDialog alertDialog) {

        if (Util_functions.ConnectivityCheck(RoadInstalingMapViewPageActivity.this,RoadInstalingMapViewPageActivity.this).booleanValue()) {
            final Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mapStatus==false) {
                        handler.postDelayed(this, 500);
                        if (mapCount==0){
                            if(!checkPermission()){
                                mapCount++;
                                tv_percentage.setText(mapCount + "%");
                                requestPermission();
                            } else {
                                mapCount=2;
                                tv_percentage.setText(mapCount + "%");

                            }
                        }else if (mapCount>=2){
                            mapCount++;
                            if (mapCount==5){
                                initAPI();
                            }
                            tv_percentage.setText(mapCount + "%");
                        }
                    }

                    if (mapStatus == true) {
                        handler1.postDelayed(this, 250);
                        mapCount++;
                        tv_percentage.setText(mapCount + "%");
                        if (mapCount == 100) {
                            mapCount = 100;
                            handler1.removeCallbacksAndMessages(null);
                            startActivity(new Intent(RoadInstalingMapViewPageActivity.this, GetStartPageActivity.class));
                            finish();

                        }
                    } else if (mapCount > 80) {
                        if (mapStatus == false) {
                            handler1.removeCallbacksAndMessages(null);
                            mapCount = 0;
                            tv_percentage.setText(mapCount + "%");
                            if (Util_functions.ConnectivityCheck(RoadInstalingMapViewPageActivity.this,RoadInstalingMapViewPageActivity.this).booleanValue()) {
                                Util_functions.ShowAlertError(RoadInstalingMapViewPageActivity.this, "Something Went Wrong \nInternet is too slow", "ok", RoadInstalingMapViewPageActivity.this);
                            }
                        }


                    }

                }
            }, 0000);
        }
    }

    @Override
    public void onNegativeButtonClicked() {

    }


    //unzipfile
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
    private void getLiscenseUpdate() {
        RestMethods restMethods = RestClient.buildHTTPClient();
        licenseUpdateReq.setCode(Constants.M_ANDROID_ID);
        licenseUpdateReq.setCompanyId(Constants.companyID);
        licenseUpdateReq.setDivisionId(Constants.divisionId);
        licenseUpdateReq.setModuleId(Constants.moduleID);
        licenseUpdateReq.setStatus("REGISTERED");
        licenseUpdateReq.setName("License");
        licenseUpdateReq.setUserId(Preference_Details.getUserID());
        licenseUpdateReq.setType(Constants.type_lic);
        licenseUpdateReq.setCategory(Constants.referredBy);
        licenseUpdateReq.setSubCategory("LICENCE_CREATED");
        restMethods.LicenseUpdateReq(Constants.referredBy,Preference_Details.getLoginSessID(),licenseUpdateReq).enqueue(new Callback<UserSettingResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserSettingResponse> call, @NonNull Response<UserSettingResponse> response) {
                try{
                    if (response.code()==201) {

                    }  else if(response.code()==412){
                        getLiscenseUpdate();
                        try {
                            String Error=response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for(int i=0;i<jsonArray1.length();i++){
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowValidateAlertError(RoadInstalingMapViewPageActivity.this,object1.getString("message"),"OK");
                            }
                            //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.resp_empty),true);
                        } catch (Exception e){
                            e.printStackTrace();
                            getLiscenseUpdate();
                        }
                    } else {
                        getLiscenseUpdate();
                        String Error=response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text=obj.getString("message");
                        Util_functions.ShowValidateAlertError(RoadInstalingMapViewPageActivity.this,text,"OK");
                    }
                } catch (Exception e){
                    getLiscenseUpdate();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(@NonNull Call<UserSettingResponse> call, @NonNull Throwable t) {
                getLiscenseUpdate();
                if (t.toString().contains("Unable to resolve host")){
                    Util_functions.ShowValidateAlertError(RoadInstalingMapViewPageActivity.this,getString(R.string.check_internet),"OK");
                }
                Util_functions.ShowAlertError(RoadInstalingMapViewPageActivity.this,t.getMessage(),"OK",RoadInstalingMapViewPageActivity.this);
                t.printStackTrace();
            }
        });
    }

}