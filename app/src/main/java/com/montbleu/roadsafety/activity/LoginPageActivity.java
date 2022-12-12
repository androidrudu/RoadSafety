package com.montbleu.roadsafety.activity;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static com.montbleu.Utils.Constants.DEMO_BIN_LICENSE_FILE;
import static com.montbleu.Utils.Constants.DEMO_LICENSE_FILE;
import static com.montbleu.Utils.Constants.DEMO_LICENSE_FILE_NEXYAD;
import static com.montbleu.Utils.Constants.SUB_FOLD_MAP_NAME;
import static com.montbleu.Utils.Constants.SUB_FOLD_NAME;
import static com.montbleu.Utils.Constants.SUB_PATH;
import static com.montbleu.Utils.Constants.WSDL_URL;

import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.montbleu.Retrofit.RestClient;
import com.montbleu.Retrofit.RestMethods;
import com.montbleu.Utils.Constants;
import com.montbleu.Utils.PreferenceManager;
import com.montbleu.Utils.Preference_Details;
import com.montbleu.Utils.ProgressDialog;
import com.montbleu.Utils.Util_functions;
import com.montbleu.interfaces.OnDialogButtonClickListener;
import com.montbleu.interfaces.Preference_Keys;
import com.montbleu.model.GetModuleResponse;
import com.montbleu.model.GetPortionResponse;
import com.montbleu.model.LoginRequest;
import com.montbleu.model.LoginResponse;
import com.montbleu.roadsafety.R;
import com.nexyad.jndksafetynex.JNDKSafetyNex;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPageActivity extends AppCompatActivity implements View.OnClickListener, OnDialogButtonClickListener {
    private AppCompatButton btnRoadLogin;
    TextInputEditText etPassword,etEmail;
    Boolean email_mob_Status = false,pass_status = false;
    TextView txt_joinNow;
    RestMethods restMethods;
    private PreferenceManager mPrefernce_Manager=PreferenceManager.getInstance();
    TextView forgot_password;
    Boolean mconnectStatus = false;
    public static final int PERMISSION_REQUEST_CODE = 101;
    File DEMO_WORKING_PATH;
    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        DEMO_WORKING_PATH = getFilesDir().getAbsoluteFile();
        //getDeviceID();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        if (SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        //mPrefernce_Manager = new PreferenceManager();
        txt_joinNow = findViewById(R.id.txt_joinNow);
        forgot_password = findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(this);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRoadLogin = findViewById(R.id.btnRoadLogin);
        btnRoadLogin.setOnClickListener(this);
        txt_joinNow.setOnClickListener(this);
        ProgressDialog.setLottieProgressDialog(LoginPageActivity.this,Constants.lottiePath);
        //genCert();

        if (Util_functions.checkPermission(LoginPageActivity.this,permissions)) {
            getDeviceID();
        }

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forgot_password:
                Constants.mob_num = etEmail.getText().toString();
                Intent forgotPass = new Intent(getApplicationContext(),ForgotPassword.class);
                startActivity(forgotPass);
                break;
            case R.id.txt_joinNow:
                startActivity(new Intent(LoginPageActivity.this,RoadRegisterPageActivity.class));
                break;
            case R.id.btnRoadLogin:
                if (etEmail.getText().toString().isEmpty() && etPassword.getText().toString().isEmpty()) {
                    Util_functions.ShowValidateAlertError(LoginPageActivity.this, getString(R.string.enter_mob_pwd), "OK");
                    break;
                } else {
                    if (etEmail.getText().toString().isEmpty()) {
                        email_mob_Status = false;
                        Util_functions.ShowValidateAlertError(LoginPageActivity.this, getString(R.string.enter_phn), "OK");
                        break;
                    } else {
                        if (TextUtils.isDigitsOnly(etEmail.getText().toString())) {
                            if (Patterns.PHONE.matcher(etEmail.getText().toString()).matches()) {
                                if (Util_functions.isValidPhoneNumber(etEmail.getText().toString())) {
                                    email_mob_Status = true;

                                } else {
                                    email_mob_Status = false;
                                    Util_functions.ShowValidateAlertError(LoginPageActivity.this, getString(R.string.valid_phno), "OK");
                                    break;
                                }
                            }
                        }

                    }
                }
                if (etPassword.getText().toString().isEmpty()){
                    pass_status=false;
                    Util_functions.ShowValidateAlertError(LoginPageActivity.this, getString(R.string.enter_pwd), "OK");
                    break;
                }
                else {
                    String pass = "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*+=?-]).{8,20}$";
                    if (!(etPassword.length()>7)){
                        Util_functions.ShowValidateAlertError(LoginPageActivity.this, getString(R.string.valid_password_len), "OK");
                        break;
                    }
                    else {
                        if (etPassword.getText().toString().trim().matches(pass)) {
                            pass_status = true;

                        } else {
                            pass_status = false;
                            Util_functions.ShowValidateAlertError(LoginPageActivity.this, getString(R.string.valid_password), "OK");
                            break;
                        }
                    }
                }
                if (email_mob_Status && pass_status) {
                    FirebaseApp.initializeApp(getApplicationContext());
                    ProgressDialog.setLottieProgressDialog(LoginPageActivity.this,Constants.lottiePath);
                    if(Util_functions.ConnectivityCheck(LoginPageActivity.this,LoginPageActivity.this)) {
                        mconnectStatus = true;
                       // genCert();
                        if (Util_functions.checkPermission(LoginPageActivity.this,permissions)) {
                            btnRoadLogin.setClickable(false);
                            ProgressDialog.showProgress();
                            getDeviceID();
                            callLogin();
                        }
                    }

                } else {
                    Util_functions.ShowValidateAlertError(LoginPageActivity.this, getString(R.string.valid_phn_pass), "OK");
                }

        }

    }
    public static String returnMeFCMtoken() {
        final String[] token = {""};
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if(task.isComplete()){
                token[0] = task.getResult();
                Log.e("AppConstants", "onComplete: new Token got: "+token[0] );
            }
        });
        return token[0];
    }
    private void callLogin() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setId("");
        loginRequest.setCategory(Constants.category_Password);
        loginRequest.setCompanyId(Constants.companyID);
        loginRequest.setUsername(etEmail.getText().toString());
        loginRequest.setPassword(etPassword.getText().toString());
        LoginRequest.UserSessionField userSessionField = new LoginRequest.UserSessionField();
        userSessionField.setDeviceType("ANDROID");
        userSessionField.setDeviceOrderId("3523");
        //userSessionField.setDeviceStatus("DEFAULT");
        userSessionField.setDeviceStatus("AUDIT");
        //userSessionField.setDeviceStatus("REGISTERED");
        userSessionField.setDeviceUniqueId(Preference_Details.getDeviceId());
        userSessionField.setDeviceVersionNumber(String.valueOf(Build.VERSION.RELEASE));

        userSessionField.setDeviceModelName(String.valueOf(Build.MANUFACTURER)+" "+String.valueOf(Build.MODEL)+" "+String.valueOf(Build.VERSION.RELEASE));
        loginRequest.setUserSessionField(userSessionField);
        restMethods = RestClient.buildHTTPClient();
        restMethods.getLoginResponse(Constants.referredBy,Constants.Static_SessionID,loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                try{
                    if (response.code()==201) {
                        Constants.mob_num =response.body().getUserSession().getUsername();
                        Log.e("status",""+response.body());
                        Constants.frst_name=response.body().getUser().getFirstName();
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.pwd,etPassword.getText().toString());
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.LoginSessID,response.body().getUserSession().getId());
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.userName,response.body().getUserSession().getUsername());
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.EmailID,response.body().getUser().getEmail());
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.LoginType,response.body().getUser().getType());
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.companyID,response.body().getUser().getCompanyId());
                        //  mPrefernce_Manager.setString(Preference_Keys.LOginKeys.countryID,response.body().getUser().);
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.userID,response.body().getUser().getId());
                        mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.LoginStatus,true);
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.FrstName,response.body().getUser().getFirstName().trim());
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.userStatus,response.body().getUser().getStatus());
                        mPrefernce_Manager.setLong(Preference_Keys.LOginKeys.sessionTime,getUniTime());
                        //  Util_functions.Show_Toast(getApplicationContext(),getString(R.string.txt_login_success),false);
                        Constants.firstTimeUserStatus = false;
                        getRoleModule();
                     //  ProgressDialog.dismissProgress();
                        btnRoadLogin.setClickable(true);
                    }  else if(response.code()==412){
                        try {
                            String Error=response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for(int i=0;i<jsonArray1.length();i++){
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowAlertError(LoginPageActivity.this,object1.getString("message"),"OK",LoginPageActivity.this);
                            }
                            //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.resp_empty),true);
                            ProgressDialog.dismissProgress();
                        } catch (Exception e){
                            e.printStackTrace();
                            ProgressDialog.dismissProgress();
                        }

                    } else {
                        String Error=response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text=obj.getString("message");
                       /* if (text.contains("not matched")){
                            Util_functions.AlertDialog(LoginPageActivity.this,"Your mobile number is already login in another mobile.\n Do you want login ?","OK","CANCEL",LoginPageActivity.this);
                        }else if (text.contains("Unsupported")){

                        }else {*/
                        if (text.contains("Verification")){
                            ProgressDialog.dismissProgress();
                            startActivity(new Intent(getApplicationContext(),VerificationCodePageActivity.class).putExtra("mob_number",etEmail.getText().toString()));
                        }else {
                            Util_functions.ShowValidateAlertError(LoginPageActivity.this,text,"OK");
                        }

                        // Util_functions.Show_Toast(getApplicationContext(),getString(R.string.txt_error),true);
                        ProgressDialog.dismissProgress();
                        btnRoadLogin.setClickable(true);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    ProgressDialog.dismissProgress();
                    btnRoadLogin.setClickable(true);
                }
            }
            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                if (t.toString().contains("Unable to resolve host")){
                    Util_functions.ShowValidateAlertError(LoginPageActivity.this,getString(R.string.check_internet),"OK");
                }
                t.printStackTrace();
                Util_functions.ShowAlertError(LoginPageActivity.this,t.getMessage(),"OK",LoginPageActivity.this);
                ProgressDialog.dismissProgress();
                btnRoadLogin.setClickable(true);
            }
        });

    }
    private Long getUniTime() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        long timeInMili = calendar .getTimeInMillis();
        return timeInMili;
    }
    private void getRoleModule() {
        RestMethods restMethods = RestClient.buildHTTPClient();
        restMethods.moduleGetResp(Constants.referredBy, Preference_Details.getLoginSessID(),Preference_Details.getUserID(),Constants.companyID,Constants.type_role_module).enqueue(new Callback<List<GetModuleResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<GetModuleResponse>> call, @NonNull Response<List<GetModuleResponse>> response) {
                Log.e("get Role Modue", String.valueOf(response.body()));
                try {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                           // ProgressDialog.dismissProgress();
                            for (int i=0;i<response.body().size();i++){
                                mPrefernce_Manager.setString(Preference_Keys.LOginKeys.divisionID,response.body().get(i).getDivisionId());
                                mPrefernce_Manager.setString(Preference_Keys.LOginKeys.moduleID,response.body().get(i).getModuleId());
                            }
                            getPortion();
                        }
                    } else if (response.code() == 412) {
                        try {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowValidateAlertError(getApplicationContext(), object1.getString("message"), "OK");
                                ProgressDialog.dismissProgress();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        String Error = response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text = obj.getString("message");
                        Util_functions.ShowValidateAlertError(getApplicationContext(), text, "OK");
                        //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.txt_error),true);
                        ProgressDialog.dismissProgress();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ProgressDialog.dismissProgress();
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<GetModuleResponse>> call, @NonNull Throwable t) {
                if (t.toString().contains("Unable to resolve host")){
                    Util_functions.ShowValidateAlertError(LoginPageActivity.this,getString(R.string.check_internet),"OK");
                }
                Util_functions.ShowAlertError(LoginPageActivity.this,t.getMessage(),"OK",LoginPageActivity.this);
                t.printStackTrace();
                ProgressDialog.dismissProgress();

            }
        });

    }

    private void getPortion() {
        RestMethods restMethods = RestClient.buildHTTPClient();
        restMethods.portionGetResp(Constants.referredBy, Preference_Details.getLoginSessID(),Preference_Details.getUserID(),Constants.companyID,Constants.type_portion).enqueue(new Callback<List<GetPortionResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<GetPortionResponse>> call, @NonNull Response<List<GetPortionResponse>> response) {
                Log.e("status", "" + response.body());
                try {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            for (int i=0;i<response.body().size();i++){
                                mPrefernce_Manager.setString(Preference_Keys.LOginKeys.portionID,response.body().get(i).getId());
                                mPrefernce_Manager.setString(Preference_Keys.LOginKeys.propertyID,response.body().get(i).getPropertyId());
                                mPrefernce_Manager.setString(Preference_Keys.LOginKeys.sectID,response.body().get(i).getSectionId());
                            }
                          //  verifyLocalUserName();
                            gotoDashBoard();
                        }
                    } else if (response.code() == 412) {
                        try {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowValidateAlertError(getApplicationContext(), object1.getString("message"), "OK");
                                ProgressDialog.dismissProgress();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        String Error = response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text = obj.getString("message");
                        Util_functions.ShowValidateAlertError(getApplicationContext(), text, "OK");
                        //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.txt_error),true);
                        ProgressDialog.dismissProgress();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ProgressDialog.dismissProgress();

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<GetPortionResponse>> call, @NonNull Throwable t) {
                if (t.toString().contains("Unable to resolve host")){
                    Util_functions.ShowValidateAlertError(LoginPageActivity.this,getString(R.string.check_internet),"OK");
                }
                Util_functions.ShowAlertError(LoginPageActivity.this,t.getMessage(),"OK",LoginPageActivity.this);
                t.printStackTrace();
                ProgressDialog.dismissProgress();

            }
        });
    }


    public void gotoDashBoard(){
        startActivity(new Intent(LoginPageActivity.this,RoadStartPageActivity.class));
        finish();
        ProgressDialog.dismissProgress();
    }

    @Override
    public void onPositiveButtonClicked(AlertDialog alertDialog) {
        if (!mconnectStatus.booleanValue()) {
            ProgressDialog.dismissProgress();
        }
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    //Map License

    private void genCert() {

        if(!checkPermission()){
            requestPermission();
        } else {
            callService();
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
                    //callService();
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
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
                  // callService();
                } else
                {
                    Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
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
        request.addProperty("orderId","3523");
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
                System.out.println(e);
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
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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



    public static String convertStringToBinary(String input) {
        StringBuilder result = new StringBuilder();
        char[] chars = input.toCharArray();
        for (char aChar : chars) {
            result.append(
                    String.format("%8s", Integer.toBinaryString(aChar))   // char -> int, auto-cast
                            .replaceAll(" ", "0")                         // zero pads
            );
        }
        return result.toString();
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
            return;
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
        File file = new File(DEMO_WORKING_PATH+SUB_FOLD_NAME+SUB_PATH);
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            }
        }
        Uri uri = Uri.parse(yourUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS,SUB_FOLD_NAME+SUB_PATH);
        //request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); // to notify when download is complete
        request.allowScanningByMediaScanner();// if you want to be available from media players
        DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        manager.enqueue(request);
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

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
    private void getDeviceID() {
        try {
            JNDKSafetyNex mJniFunction = JNDKSafetyNex.GetInstance(this);
            mPrefernce_Manager.setString(Preference_Keys.LOginKeys.devicveID,mJniFunction.GetDeviceId());
            Constants.M_ANDROID_ID = mJniFunction.GetDeviceId();
        }catch (Exception e){
            e.printStackTrace();
        }
        // Toast.makeText(getApplicationContext(), "DeviceID"+android_id, Toast.LENGTH_SHORT).show();
    }
}