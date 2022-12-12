package com.montbleu.roadsafety.activity;

import static com.montbleu.Utils.Constants.StartScrVisibility;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.montbleu.Retrofit.FileDownloadClient;
import com.montbleu.Retrofit.RestClient;
import com.montbleu.Retrofit.RestMethods;
import com.montbleu.Utils.Constants;
import com.montbleu.Utils.PreferenceManager;
import com.montbleu.Utils.Preference_Details;
import com.montbleu.Utils.ProgressDialog;
import com.montbleu.Utils.Util_functions;
import com.montbleu.interfaces.OnDialogButtonClickListener;
import com.montbleu.interfaces.Preference_Keys;
import com.montbleu.model.ProfileEditResponse;
import com.montbleu.model.RegisterReq;
import com.montbleu.roadsafety.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileScreenActivity extends AppCompatActivity implements View.OnClickListener, OnDialogButtonClickListener {
    ImageView backButton,editButton;
    //TextView  license_date;

    EditText etEmail, emer_cont_number,etfullname,etDOB;
   // TextInputLayout layout_fullname,layout_email,layout_dob;

    androidx.appcompat.widget.AppCompatButton btnUpdate;
    static final int DATE_DIALOG_ID = 999;
    private int year, month, day;
    private Calendar calendar;
    RestMethods restMethods;
    ArrayList<ProfileEditResponse> profileGetResponses = new ArrayList<>();
    String mliceDate= "";
    ImageView profile_image,add_remove_icon;
    String mAge ="";
    Boolean mstatusRedirect = false;
    Boolean mconnectStatus = false;
    Boolean status_phn=false;
    TextView title;
    private PreferenceManager mPrefernce_Manager=PreferenceManager.getInstance();
    TextView etLogout;
    private long mLastClickTime = 0;
    private EditText etGender;
    String[] arrayForSpinner = {"Male", "Female"};
    ArrayAdapter adapter;
    TextView tvName;
    Spinner spinGender;
    View feed_seperate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile_screen);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
       // getImageProfile();
        init();
    }

    /*private void getImageProfile(){
        Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL);
        Retrofit retrofit=builder.build();
        FileDownloadClient fileDownloadClient=retrofit.create(FileDownloadClient.class);
        fileDownloadClient.GetImageFile(Constants.referredBy, Preference_Details.getLoginSessID(), Constants.companyID, Preference_Details.getUserID(),Constants.type_profile,).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code()==200){

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
*/
    private void init() {
        StartScrVisibility = false;
        etfullname = findViewById(R.id.etfullname);
        etGender = findViewById(R.id.etGender);
        etLogout = findViewById(R.id.etLogout);
        etLogout.setOnClickListener(this);
        backButton = findViewById(R.id.back_profile_btn);
        editButton = findViewById(R.id.ivedit);
        editButton.setOnClickListener(this);
        etEmail = findViewById(R.id.etEmail);
        title=findViewById(R.id.title);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);
        emer_cont_number = findViewById(R.id.etMobile);
      //  layout_dob = findViewById(R.id.layout_dob);
        tvName = findViewById(R.id.tvName);
      //  layout_license_granddate=(TextInputLayout)findViewById(R.id.);
        etDOB = findViewById(R.id.etDOB);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        profile_image=findViewById(R.id.img);
        add_remove_icon = findViewById(R.id.add_remove_icon);
        spinGender = findViewById(R.id.spinGender);
        adapter = new ArrayAdapter<String>(this,R.layout.listitems_layout, arrayForSpinner);
      //  adapter.setDropDownViewResource(R.layout.listitems_layout);
spinGender.setAdapter(adapter);
        spinGender.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ((TextView) spinGender.getSelectedView()).setTextColor(getColor(R.color.disabled_color)); //change to your color
            }
        });

        add_remove_icon.setOnClickListener(this);
        backButton.setOnClickListener(this);
        etEmail.setEnabled(false);
        emer_cont_number.setEnabled(false);
        etDOB.setEnabled(false);
        feed_seperate = findViewById(R.id.feed_seperate);
        feed_seperate.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.disabled_color));
        spinGender.setEnabled(false);
        profile_image.setEnabled(false);
        ProgressDialog.setLottieProgressDialog(ProfileScreenActivity.this,Constants.lottiePath);
   //    title.setText(Constants.frst_name);
        if (Util_functions.ConnectivityCheck(ProfileScreenActivity.this,ProfileScreenActivity.this).booleanValue()) {
            ProgressDialog.showProgress();
            getProfileDetails();
        }
        if (Constants.img_profile) {
            try {
                Picasso.get()
                        .load(Constants.BASE_URL + Constants.file + "?referredBy=" + Constants.referredBy + "&sessionId=" + Preference_Details.getLoginSessID() + "&companyId=" + Constants.companyID + "&userId=" + Preference_Details.getUserID() + "&type=" + Constants.type_profile)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(profile_image);
                Constants.img_profile=false;
                //profile_image.setImageURI(Uri.parse(Constants.BASE_URL+Constants.file+"?referredBy="+Constants.referredBy+"&sessionId="+Preference_Details.getLoginSessID()+"&companyId="+Constants.companyID+"&userId="+Preference_Details.getUserID()+"&type="+Constants.type_profile));
            } catch (Exception e) {
                profile_image.setBackground(getDrawable(R.drawable.profile_man));
                e.printStackTrace();
            }
        }else {
            try {
                Picasso.get()
                        .load(Constants.BASE_URL + Constants.file + "?referredBy=" + Constants.referredBy + "&sessionId=" + Preference_Details.getLoginSessID() + "&companyId=" + Constants.companyID + "&userId=" + Preference_Details.getUserID() + "&type=" + Constants.type_profile)
                        .into(profile_image);
                //profile_image.setImageURI(Uri.parse(Constants.BASE_URL+Constants.file+"?referredBy="+Constants.referredBy+"&sessionId="+Preference_Details.getLoginSessID()+"&companyId="+Constants.companyID+"&userId="+Preference_Details.getUserID()+"&type="+Constants.type_profile));
            } catch (Exception e) {
                profile_image.setBackground(getDrawable(R.drawable.profile_man));
                e.printStackTrace();
            }
        }
    }

    private void getProfileDetails() {
        restMethods = RestClient.buildHTTPClient();
        restMethods.profileGetResponse(Constants.referredBy,Preference_Details.getLoginSessID(),Preference_Details.getCompanyID(),Preference_Details.getLoginType(),Preference_Details.getUserID(),"ASC",0,100).enqueue(new Callback<List<ProfileEditResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProfileEditResponse>> call, @NonNull Response<List<ProfileEditResponse>> response) {
                Log.e("status",""+response.body());
                try{
                    if (response.code()==200) {
                        if (response.isSuccessful()) {
                            profileGetResponses.clear();
                            if (!response.body().isEmpty()) {
                                profileGetResponses = (ArrayList<ProfileEditResponse>) response.body();

                                for (int i = 0; i < profileGetResponses.size(); i++) {

                                   // profileGetResponses.get(i).getLastName();
                                    if (!TextUtils.isEmpty(profileGetResponses.get(i).getFirstName())){
                                        tvName.setText(profileGetResponses.get(i).getFirstName());
                                        if (!TextUtils.isEmpty(profileGetResponses.get(i).getLastName())){
                                            etfullname.setText(profileGetResponses.get(i).getFirstName()+" "+profileGetResponses.get(i).getLastName());

                                        } else {
                                            etfullname.setText(profileGetResponses.get(i).getFirstName()+" "+profileGetResponses.get(i).getLastName());
                                        }
                                    } else {
                                        if (!TextUtils.isEmpty(profileGetResponses.get(i).getLastName())){
                                            etfullname.setText(profileGetResponses.get(i).getFirstName()+" "+profileGetResponses.get(i).getLastName());
                                            tvName.setText(profileGetResponses.get(i).getFirstName());
                                        } else {
                                            etfullname.setText(profileGetResponses.get(i).getFirstName()+" "+profileGetResponses.get(i).getLastName());
                                        }
                                    }

                                   // etfullname.setText(profileGetResponses.get(i).getFirstName()+" "+profileGetResponses.get(i).getLastName());
                                    etEmail.setText(profileGetResponses.get(i).getEmail());
                                    emer_cont_number.setText(profileGetResponses.get(i).getUserField().getEmergencyContactNumber());
                                    //license_date.setText(profileGetResponses.get(i).getUserField().getDrivingLicenseIssueDate());
                                    if (!TextUtils.isEmpty(profileGetResponses.get(i).getUserField().getDrivingLicenseIssueDate())) {
                                        etDOB.setText(getSplittedDate(profileGetResponses.get(i).getUserField().getDrivingLicenseIssueDate()));
                                        mliceDate = getSplittedDate(profileGetResponses.get(i).getUserField().getDrivingLicenseIssueDate());
                                    } else {
                                        etDOB.setText("");
                                    }

                                    if(!TextUtils.isEmpty(profileGetResponses.get(i).getUserField().getAge())){
                                        mAge = profileGetResponses.get(i).getUserField().getAge();
                                    } else {
                                        mAge = "";
                                    }

                                }
                                ProgressDialog.dismissProgress();
                            } else {
                                String Error=response.errorBody().string();
                                JSONObject obj = new JSONObject(Error);
                                String text=obj.getString("message");
                                if(text.toLowerCase().contains("please login again")){
                                    mstatusRedirect = true;
                                }
                                Util_functions.ShowAlertError(getApplicationContext(),text,"OK",ProfileScreenActivity.this);
                                //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.resp_empty),true);
                                ProgressDialog.dismissProgress();
                            }
                        } else {
                            ProgressDialog.dismissProgress();
                        }
                    }  else if(response.code()==412){
                        try {
                            String Error=response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for(int i=0;i<jsonArray1.length();i++){
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowAlertError(ProfileScreenActivity.this,object1.getString("message"),"OK",ProfileScreenActivity.this);
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
                        if(text.toLowerCase().contains("please login again")){
                            mstatusRedirect = true;
                        }
                        Util_functions.ShowAlertError(ProfileScreenActivity.this,text,"OK",ProfileScreenActivity.this);
                        //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.txt_error),true);
                        ProgressDialog.dismissProgress();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    ProgressDialog.dismissProgress();
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<ProfileEditResponse>> call, @NonNull Throwable t) {
                t.printStackTrace();
                Util_functions.ShowAlertError(ProfileScreenActivity.this,t.getMessage(),"OK",ProfileScreenActivity.this);
                ProgressDialog.dismissProgress();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.etLogout:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1500){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                mconnectStatus = true;
                Util_functions.AlertDialog(ProfileScreenActivity.this,"Do you want Logout?","Yes","No",ProfileScreenActivity.this);
                break;
            case R.id.back_profile_btn:
               // startActivity(new Intent(ProfileScreenActivity.this,RoadStartPageActivity.class));
                finish();
                break;
            case R.id.add_remove_icon:
                pickImage();
                break;
            case R.id.ivedit:
                btnUpdate.setVisibility(View.VISIBLE);
                editValues();
                break;
            case R.id.btnUpdate:
                /*else {
                    if (Util_functions.validateEmail(etEmail.getText().toString())){
                        status_email = true;
                    }
                    else {
                        status_email =false;
                        Util_functions.ShowValidateAlertError(ProfileScreenActivity.this,getString(R.string.valid_email_address),"ok");
                        break;
                    }
                }*/
                if (emer_cont_number.getText().toString().isEmpty()){
                    status_phn=true;
                }
               else {
                    if (Util_functions.isValidPhoneNumber(emer_cont_number.getText().toString())) {
                        status_phn = true;
                    } else {
                        status_phn = false;
                        Util_functions.ShowValidateAlertError(ProfileScreenActivity.this, getString(R.string.valid_emgcont), "ok");
                        break;
                    }
               }
                if (status_phn){
                    if (Util_functions.ConnectivityCheck(ProfileScreenActivity.this,ProfileScreenActivity.this).booleanValue()) {
                        mconnectStatus = true;
                        ProgressDialog.showProgress();
                        updateProfile();
                    }
                }
        }

    }

    private void pickImage() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i,101);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //ProgressDialog.dismissProgress();
                //Display an error
            } else {
                try{
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                File file = new File(picturePath);
                Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 30, bos);
                MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
                getImageProfile(filePart);
            }catch (Exception e){
                    //ProgressDialog.dismissProgress();
                e.printStackTrace();
            }
            }
        }
    }

    private void getImageProfile(MultipartBody.Part file){
        ProgressDialog.showProgress();
        Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL);
        Retrofit retrofit=builder.build();
        Log.e("o/p response",Constants.type_profile);
        FileDownloadClient fileDownloadClient=retrofit.create(FileDownloadClient.class);
        fileDownloadClient.GetImageFile(Constants.referredBy, Preference_Details.getLoginSessID(), Constants.companyID,Constants.type_profile, Preference_Details.getUserID(),file).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    if (response.code() == 202) {
                        Constants.img_profile=true;
                        ProgressDialog.dismissProgress();
                        try {
                            Picasso.get()
                                    .load(Constants.BASE_URL+Constants.file+"?referredBy="+Constants.referredBy+"&sessionId="+Preference_Details.getLoginSessID()+"&companyId="+Constants.companyID+"&userId="+Preference_Details.getUserID()+"&type="+Constants.type_profile)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                    .networkPolicy(NetworkPolicy.NO_CACHE)
                                    .into(profile_image);
                        }catch (Exception e){
                            profile_image.setBackground(getDrawable(R.drawable.profile_man));
                            e.printStackTrace();
                        }
                        Util_functions.ShowValidateAlertError(ProfileScreenActivity.this, getString(R.string.text_profile_image),"OK");
                    } else if (response.code() == 412) {

                            ProgressDialog.dismissProgress();
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowAlertError(ProfileScreenActivity.this, object1.getString("message"), "OK", ProfileScreenActivity.this);
                            }

                    } else {
                        ProgressDialog.dismissProgress();
                        String Error=response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text=obj.getString("message");
                        if(text.toLowerCase().contains("please login again")){
                            mstatusRedirect = true;
                        }
                        Util_functions.ShowValidateAlertError(ProfileScreenActivity.this,text,"OK");
                    }
                } catch (Exception e){
                e.printStackTrace();
                ProgressDialog.dismissProgress();

            }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                ProgressDialog.dismissProgress();
                Util_functions.ShowAlertError(ProfileScreenActivity.this,t.getMessage(),"OK",ProfileScreenActivity.this);
            }
        });
    }

    public String getSplittedDate(String dateNow){
        String dateInputString = dateNow;
        String stringDate = null;
        try {
            // obtain date and time from initial string
            Date date1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).parse(dateInputString);
            // set date string
            stringDate = new SimpleDateFormat("dd MMM yyyy", Locale.US).format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  stringDate;
    }


    private void updateProfile() {
        RegisterReq registerReq = new RegisterReq();
        registerReq.setCategory(Constants.category_Password);
        registerReq.setCompanyId(Preference_Details.getCompanyID());
        registerReq.setCountryId(Constants.countryID);
        registerReq.setEmail(etEmail.getText().toString());
        registerReq.setEmailVerified(profileGetResponses.get(0).getEmailVerified());
        registerReq.setFirstName(profileGetResponses.get(0).getFirstName());
        registerReq.setId(Preference_Details.getUserID());
        registerReq.setLastName(profileGetResponses.get(0).getLastName());
        registerReq.setStatus("REGISTERED");
        registerReq.setType("USER");
        registerReq.setTimeZoneId("tizo00000000000000000001");
        registerReq.setUsernameVerified("YES");
        //registerReq.setPassword("test123");
        registerReq.setUsername(Preference_Details.getUserName());
        RegisterReq.UserField userField = new RegisterReq.UserField();
        //mAge = et_age.getText().toString();
        userField.setAge(mAge);
        if (!TextUtils.isEmpty(mliceDate)) {
            userField.setDrivingLicenseIssueDate(getFormatDate(mliceDate));
        } else {
            userField.setDrivingLicenseIssueDate(mliceDate);
        }
        userField.setEmergencyContactNumber(emer_cont_number.getText().toString());
        registerReq.setUserField(userField);
        RegisterReq.UserPrimaryDetail userPrimaryDetail = new RegisterReq.UserPrimaryDetail();
        userPrimaryDetail.setPrimaryDivisionId("divi00000000000000000001");
        userPrimaryDetail.setPrimaryModuleId("modu00000000000000000001");
        registerReq.setUserPrimaryDetail(userPrimaryDetail);
        restMethods = RestClient.buildHTTPClient();
        restMethods.profileEditResponse(Constants.referredBy,Preference_Details.getLoginSessID(),registerReq).enqueue(new Callback<ProfileEditResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProfileEditResponse> call, @NonNull Response<ProfileEditResponse> response) {
                Log.e("status",""+response.body());
                try{
                    if (response.code()==202) {
                        ProgressDialog.dismissProgress();
                        Util_functions.ShowAlertError(ProfileScreenActivity.this, getString(R.string.text_profile_update),"OK",ProfileScreenActivity.this);
                        Log.e("value",response.message().toString());
                    }  else if(response.code()==412){
                        try {
                            String Error=response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for(int i=0;i<jsonArray1.length();i++){
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowValidateAlertError(ProfileScreenActivity.this,object1.getString("message"),"OK");
                            }
                            //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.resp_empty),true);
                            ProgressDialog.dismissProgress();
                        } catch (Exception e){
                            e.printStackTrace();
                            ProgressDialog.dismissProgress();

                        }

                    } else {
                        ProgressDialog.dismissProgress();
                        String Error=response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text=obj.getString("message");
                        if(text.toLowerCase().contains("please login again")){
                            mstatusRedirect = true;
                        }
                        Util_functions.ShowValidateAlertError(ProfileScreenActivity.this,text,"OK");
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    ProgressDialog.dismissProgress();

                }
            }
            @Override
            public void onFailure(@NonNull Call<ProfileEditResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
                ProgressDialog.dismissProgress();
                Util_functions.ShowAlertError(ProfileScreenActivity.this,t.getMessage(),"OK",ProfileScreenActivity.this);
            }
        });

    }

    private String getFormatDate(String DateNow){
        String newDateString = "";
        try {
            // mon+" "+date+", "+year
            //current date format
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
            Date objDate = dateFormat.parse(DateNow);
            //Expected date format
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
            newDateString = dateFormat2.format(objDate);
            Log.d("Date Format:", "Final Date:"+newDateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newDateString;
    }
    private void editValues() {
        feed_seperate.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_text));
        etfullname.setEnabled(true);
        etGender.setEnabled(true);
        //  emer_cont_number.setEnabled(true);
        etEmail.setEnabled(true);
   //     layout_email.requestFocus();
        emer_cont_number.setEnabled(true);
        etDOB.setEnabled(true);
        editButton.setClickable(true);
        etDOB.setEnabled(true);
       // layout_dob.setEnabled(true);
        etDOB.setFocusable(true);
        spinGender.setEnabled(true);
        spinGender.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ((TextView) spinGender.getSelectedView()).setTextColor(getColor(R.color.color_title)); //change to your color
            }
        });

        profile_image.setEnabled(true);

    }
    public void renderDatePicker(View view){
        showDialog(DATE_DIALOG_ID);
    }

    private void showDate(int year, String month, int day) {
        etDOB.setText(new StringBuilder().append(day).append(" ")
                .append(month).append(" ").append(year));
        mliceDate = day+" "+month+" "+year;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DATE_DIALOG_ID) {
            DatePickerDialog dialog= new DatePickerDialog(this, R.style.my_dialog_theme, myDateListener, year, month, day);
            dialog.getDatePicker().setMaxDate(new Date().getTime());
            return dialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {

            int tempmonth = month+1;
            SimpleDateFormat newformat = new SimpleDateFormat("MMM");
            SimpleDateFormat oldformat = new SimpleDateFormat("MM");
            String monthName = null;
            Date myDate;
            try {
                myDate = oldformat.parse(String.valueOf(tempmonth));
                monthName = newformat.format(myDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.d("MMM", monthName);
            showDate(year, monthName, day);
        }
    };

    @Override
    public void onPositiveButtonClicked(AlertDialog alertDialog) {
        if(!mconnectStatus.booleanValue()){
            ProgressDialog.dismissProgress();
            finish();
        } else {
            if (mstatusRedirect) {
                mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.LoginStatus,false);
                Intent loginScreen = new Intent(getApplicationContext(), LoginPageActivity.class);
                startActivity(loginScreen);
                finish();
            } else {
                finish();
            }
        }
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onBackPressed() {

    }
}