package com.montbleu.roadsafety.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.montbleu.Retrofit.RestClient;
import com.montbleu.Retrofit.RestMethods;
import com.montbleu.Utils.Constants;
import com.montbleu.Utils.PreferenceManager;
import com.montbleu.Utils.Preference_Details;
import com.montbleu.Utils.ProgressDialog;
import com.montbleu.Utils.Util_functions;
import com.montbleu.interfaces.OnDialogButtonClickListener;
import com.montbleu.model.ReportScreenResponse;
import com.montbleu.model.UserFeedbackReq;
import com.montbleu.roadsafety.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportProblemPageActivity extends AppCompatActivity implements View.OnClickListener, OnDialogButtonClickListener {

    ConstraintLayout image_gallery, screenshot, screen_record;
    int PICK_IMAGE = 101;
    EditText description, mailId;
    RestMethods restMethods;
    MultipartBody.Builder multi_builder;
    Boolean mconnectStatus = false;
    AppCompatButton btnCancel, btnSubmit;
    ImageView back_report_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_report_problem);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        back_report_btn = findViewById(R.id.back_profile_btn);
        back_report_btn.setOnClickListener(this);
        mailId = findViewById(R.id.mailId);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
        description = findViewById(R.id.description);
        screen_record = findViewById(R.id.linear_first);
        screenshot = findViewById(R.id.linear_second);
        image_gallery = findViewById(R.id.linear_three);
        image_gallery.setOnClickListener(this);

        ProgressDialog.setLottieProgressDialog(ReportProblemPageActivity.this, Constants.lottiePath);

        description.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (description.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_profile_btn:
                finish();
                break;
            case R.id.btnCancel:
                description.setText("");
                mailId.setText("");
                break;
            case R.id.linear_three:
                pickImage();
                break;
            case R.id.back_feed_btn:
                finish();
                break;
            case R.id.btnSubmit:
                if (mailId.getText().toString().isEmpty()) {
                    Util_functions.ShowValidateAlertError(ReportProblemPageActivity.this, getString(R.string.valid_email_address), "OK");
                } else {
                    if (!Util_functions.validateEmail(mailId.getText().toString())) {
                        Util_functions.ShowValidateAlertError(ReportProblemPageActivity.this, getString(R.string.valid_email_address), "OK");
                    } else if (!description.getText().toString().isEmpty()) {
                        if (Util_functions.ConnectivityCheck(ReportProblemPageActivity.this, ReportProblemPageActivity.this).booleanValue()) {
                            ProgressDialog.showProgress();
                            mconnectStatus = true;
                            doSubmit();
                        }
                    } else {
                        Util_functions.ShowValidateAlertError(ReportProblemPageActivity.this, getString(R.string.text_desc_empty), "OK");
                    }
                }
                break;
        }
    }

    private void doSubmit() {
        UserFeedbackReq userFeedbackReq = new UserFeedbackReq();
        userFeedbackReq.setCompanyId(Preference_Details.getCompanyID());
        userFeedbackReq.setDescription("Testing");
        userFeedbackReq.setId(Preference_Details.getUserID());
        userFeedbackReq.setStatus("REGISTERED");
        userFeedbackReq.setDivisionId(Constants.divisionId);
        userFeedbackReq.setModuleID(Constants.moduleID);
        userFeedbackReq.setType("REPORT_PROBLEM");
        UserFeedbackReq.UserFeedbackField userFeedbackField = new UserFeedbackReq.UserFeedbackField();
        userFeedbackField.setDescription(description.getText().toString());
        userFeedbackReq.setUserFeedbackField(userFeedbackField);
        userFeedbackReq.setUserId(Preference_Details.getUserID());

        restMethods = RestClient.buildHTTPClient();
        //Preference_Details.getLoginSessID()
        restMethods.ReportScreenCall(Constants.referredBy, Preference_Details.getLoginSessID(), userFeedbackReq).enqueue(new Callback<ReportScreenResponse>() {
            @Override
            public void onResponse(@NonNull Call<ReportScreenResponse> call, @NonNull Response<ReportScreenResponse> response) {
                Log.e("status", "" + response.body());
                try {
                    if (response.code() == 201) {
                        ProgressDialog.dismissProgress();
                        //  Util_functions.Show_Toast(getApplicationContext(),getString(R.string.txt_report_sent),true);
                        Util_functions.ShowAlertError(ReportProblemPageActivity.this, "Report sent successfully!", "OK", ReportProblemPageActivity.this);

                    } else if (response.code() == 412) {
                        try {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowValidateAlertError(ReportProblemPageActivity.this, object1.getString("message"), "OK");
                            }
                            //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.resp_empty),true);
                            ProgressDialog.dismissProgress();
                        } catch (Exception e) {
                            e.printStackTrace();
                            ProgressDialog.dismissProgress();
                        }

                    } else {
                        // Util_functions.Show_Toast(getApplicationContext(),getString(R.string.txt_error),true);
                        ProgressDialog.dismissProgress();
                        String Error = response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text = obj.getString("message");
                        if (text.toLowerCase().contains("please login again")) {
                        }
                        Util_functions.ShowValidateAlertError(ReportProblemPageActivity.this, text, "OK");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ProgressDialog.dismissProgress();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReportScreenResponse> call, @NonNull Throwable t) {
                if (t.toString().contains("Unable to resolve host")) {
                    Util_functions.ShowValidateAlertError(ReportProblemPageActivity.this, getString(R.string.check_internet), "OK");
                }
                Util_functions.ShowAlertError(ReportProblemPageActivity.this, t.getMessage(), "OK", ReportProblemPageActivity.this);
                t.printStackTrace();
                ProgressDialog.dismissProgress();
//                Util_functions.ShowAlertError(ReportProblemPageActivity.this,"Report sent successfully!","OK",ReportProblemPageActivity.this);
            }
        });

    }

    public void pickImage() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
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
                multi_builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                multi_builder.addFormDataPart("featured_photo", file.getName(), RequestBody.create(MultipartBody.FORM, bos.toByteArray()));
                //multi_builder.addFormDataPart("featured_photo", picturePath., RequestBody.create(MultipartBody.FORM, bos.toByteArray()));
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file.getAbsoluteFile());
// MultipartBody.Part is used to send also the actual file name
                // MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

                MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
            } catch (Exception e) {
                e.printStackTrace();
            }
            //getImageProfile(filePart);


            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", "no");
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String thePath = "no-path-found";
        String[] filePathColumn = {MediaStore.Images.Media.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            thePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return thePath;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onPositiveButtonClicked(AlertDialog alertDialog) {
        finish();

    }

    @Override
    public void onNegativeButtonClicked() {


    }
}
