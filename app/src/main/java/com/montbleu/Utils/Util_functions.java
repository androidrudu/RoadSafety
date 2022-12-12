package com.montbleu.Utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.montbleu.OBD.activity.MBTActivity;
import com.montbleu.Retrofit.RestClient;
import com.montbleu.Retrofit.RestMethods;
import com.montbleu.adapter.AlertValueAdapter;
import com.montbleu.interfaces.OnDialogButtonClickListener;
import com.montbleu.interfaces.Preference_Keys;
import com.montbleu.interfaces.onWifiCredListener;
import com.montbleu.model.AlertValueModel;
import com.montbleu.model.GPSGetAlertListResp;
import com.montbleu.model.LoginRequest;
import com.montbleu.model.LoginResponse;
import com.montbleu.model.MapUpdateResponse;
import com.montbleu.roadsafety.R;
import com.montbleu.roadsafety.activity.LoginPageActivity;
import com.montbleu.roadsafety.activity.MainActivity;
import com.montbleu.roadsafety.activity.RoadStartPageActivity;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;


import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Util_functions {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static void Show_Toast(Context context, String msg, boolean isShort) {
        Toast toast = Toast.makeText(context, "" + msg, Toast.LENGTH_LONG);
        if (isShort) {
            toast = Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT);
        }
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    public static boolean validateEmail(String email) {
        String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPhoneNumber(String phone) {
        if (!phone.trim().equals("") && phone.length() == 10) {
            return Patterns.PHONE.matcher(phone).matches();
        }
        return false;
    }


    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        String pass = "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*+=?-]).{8,20}$";
        // final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(pass);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static void showAlertDialog(Context context, String title, String msg, DialogInterface.OnClickListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", (DialogInterface.OnClickListener) listener);
        builder.setNegativeButton("Cancel", (DialogInterface.OnClickListener) listener);
        builder.create().show();

    }


    /*permission Starts*/

    public static boolean checkPermission(Context context,String[] strPerm){
        final Boolean[] status = {false};
        Permissions.check(context, strPerm, null, null, new PermissionHandler() {

            @Override
            public void onGranted() {
                status[0] = true;
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                status[0] = false;
                Toast.makeText(context, "Permission denied.", Toast.LENGTH_SHORT).show();
            }


        });
        return status[0];
    }

    /*permission Ends*/

    /*wifi alert start*/

    public static void WifiAlertDialog(Context context,final onWifiCredListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutView = inflater.inflate(R.layout.wifi_credential_data, null);
        builder.setView(layoutView);
        alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView et_uname = layoutView.findViewById(R.id.etUsername);
        TextView et_pass = layoutView.findViewById(R.id.etPassword);
        Button btnOk = layoutView.findViewById(R.id.btnOk);
        Button alert_no = layoutView.findViewById(R.id.btnCancel);
        alertDialog.setCancelable(false);
        alertDialog.show();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_uname.getText().toString())||TextUtils.isEmpty(et_pass.getText().toString())) {
                    Util_functions.ShowValidateAlertError(alertDialog.getContext(), context.getString( R.string.wifi_valid_uname_pass),"OK");

                } else {
                    hideKeyboardFrom(context,layoutView);
                    alertDialog.hide();
                    alertDialog.cancel();
                    alertDialog.dismiss();

                    listener.onWifiOk(et_uname.getText().toString(),et_pass.getText().toString());

                }
            }
        });

        alert_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboardFrom(context,layoutView);
                alertDialog.hide();
                alertDialog.cancel();
                alertDialog.dismiss();
                listener.onWifiCancel();

            }
        });


    }

    /*wifi alert end*/

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void AlertDialog(Context context, String title, String positiveBtnText, String negativeBtnText,
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
                listener.onPositiveButtonClicked(alertDialog);
                Constants.alertcount=true;

            }
        });

        alert_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNegativeButtonClicked();
                alertDialog.hide();
                alertDialog.cancel();
                alertDialog.dismiss();
                Constants.alertcount=true;
                MBTActivity.retry_remt_status = true;
            }
        });


    }


    public static void ShowAlert(Context context, String title, String BtnText, final OnDialogButtonClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutView = inflater.inflate(R.layout.alert_status_success, null);
        builder.setView(layoutView);
        alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (Constants.alertcount==true) {
            alertDialog.show();
            Constants.alertcount=false;
        }
        alertDialog.setCancelable(false);
        TextView alert_title = layoutView.findViewById(R.id.text_desc);
        alert_title.setText(title);
        Button alert_ok = layoutView.findViewById(R.id.alertOk);
        alert_ok.setText(BtnText);

        alert_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPositiveButtonClicked(alertDialog);
                alertDialog.hide();
                alertDialog.cancel();
                alertDialog.dismiss();
                Constants.alertcount=true;
            }
        });


    }




    public static void ShowAlertError(Context context, String title, String BtnText, final OnDialogButtonClickListener listener) {
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
            Constants.alertcount=false;
        }
        alertDialog.setCancelable(false);
        TextView alert_title = layoutView.findViewById(R.id.title_error);
        alert_title.setText(title);
        Button alert_ok = layoutView.findViewById(R.id.alert_ok);
        alert_ok.setText(BtnText);

        alert_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPositiveButtonClicked(alertDialog);
                alertDialog.hide();
                alertDialog.cancel();
                alertDialog.dismiss();
                Constants.alertcount=true;
            }
        });


    }

    public static void ShowValidateAlertError(Context context, String title, String BtnText) {
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
            Constants.alertcount=false;
        }
        alertDialog.setCancelable(false);
        TextView alert_title = layoutView.findViewById(R.id.title_error);
        alert_title.setText(title);
        Button alert_ok = layoutView.findViewById(R.id.alert_ok);
        alert_ok.setText(BtnText);

        alert_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.hide();
                alertDialog.cancel();
                alertDialog.dismiss();
                Constants.alertcount=true;
            }
        });
    }

    private static void doKeepDialog(Dialog dialog) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
    }

    public static Boolean ConnectivityCheck(Context context, OnDialogButtonClickListener listener) {

        Boolean connectivity=false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED){
                connectivity=true;
          /*  try {
                connectivity=true;
                InetAddress ipAddr = InetAddress.getByName("google.com");
                //You can replace it with your name
                return !ipAddr.equals("");

            } catch (Exception e) {
                ShowAlertError(context,"Check Your Internet Connection","ok",listener);
                return false;

            }*/


        }

        else {
            connectivity=false;
            ShowAlertError(context,"Check Your Internet Connection","ok",listener);
        }
        return connectivity;

    }


    public static void ShowGridDetails(Context context,ArrayList<GPSGetAlertListResp.DeviceDataList> gpDeviceDataLists,ArrayList<String> time) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View layoutView = inflater.inflate( R.layout.grid_details, null );
        builder.setView(layoutView);
        alertDialog = builder.create();
        DecimalFormat REAL_FORMATTER = new DecimalFormat("0.00");
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.setCancelable(false);
        TextView textView6=layoutView.findViewById(R.id.textView6);
        TextView txt_speed_limit=layoutView.findViewById(R.id.txt_speed_limit);
        alertDialog.setCanceledOnTouchOutside(false);
        TextView grid_numberss=layoutView.findViewById(R.id.grid_text_number);
        ImageView cls_grid=layoutView.findViewById(R.id.cls_grid);
        TextView txt_speed= layoutView.findViewById(R.id.txt_speed);
        //  grid_numberss.setText(grid_number);
        // TextView grid_textttt=layoutView.findViewById(R.id.grid_text);
        // grid_textttt.setText(grid_text);
        AlertValueAdapter alertValueAdapter ;
        List<AlertValueModel> alertsArrayList ;
        RecyclerView recyclerViewAlertsValue = layoutView.findViewById(R.id.recyclerViewAlertValue);
        alertsArrayList = new ArrayList<>();
        alertsArrayList.clear();
        String alertVal ="",alertTime ="",alertDist ="",alertZip="",alertSpeedLimit="",speed = "";
        for(int i=0;i<gpDeviceDataLists.size();i++){

            if(i==0){
                if (Integer.parseInt(gpDeviceDataLists.get(i).getDeviceDataField().getAlertValue())==29){
                    textView6.setText("Status");
                    grid_numberss.setText(getSubCategoryValueLowerr(Integer.parseInt(gpDeviceDataLists.get(i).getDeviceDataField().getAlertValue())));
                    txt_speed_limit.setVisibility(View.VISIBLE);
                    txt_speed_limit.setText("Duration");
                    txt_speed.setVisibility(View.VISIBLE);
                    txt_speed.setText("Vehicle\nSpeed");
                }else if(Integer.parseInt(gpDeviceDataLists.get(i).getDeviceDataField().getAlertValue())==31){
                    textView6.setText("Distance\n(Km)");
                    grid_numberss.setText(getSubCategoryValueLowerr(Integer.parseInt(gpDeviceDataLists.get(i).getDeviceDataField().getAlertValue())));
                    txt_speed_limit.setVisibility(View.VISIBLE);
                    txt_speed_limit.setText("Before\nBraking");
                    txt_speed.setVisibility(View.VISIBLE);
                    txt_speed.setText("After\nBraking");
                }
                else if (Integer.parseInt(gpDeviceDataLists.get(i).getDeviceDataField().getAlertValue())==30){
                    textView6.setText("Distance\n(Km)");
                    grid_numberss.setText(getSubCategoryValueLowerr(Integer.parseInt(gpDeviceDataLists.get(i).getDeviceDataField().getAlertValue())));
                    txt_speed_limit.setVisibility(View.VISIBLE);
                    txt_speed_limit.setText("Speed\nLimit");
                }
                else{
                    textView6.setText("Distance\n(Km)");
                    grid_numberss.setText(getSubCategoryValueLowerr(Integer.parseInt(gpDeviceDataLists.get(i).getDeviceDataField().getAlertValue())));
                    txt_speed_limit.setVisibility(View.GONE);
                    txt_speed.setVisibility(View.VISIBLE);
                    txt_speed.setText("Vehicle\nSpeed");
                }

            }

            if (gpDeviceDataLists.get(i).getDeviceDataField().getAlertValue().equals("0")||gpDeviceDataLists.get(i).getDeviceDataField().getAlertValue().isEmpty()||gpDeviceDataLists.get(i).getDeviceDataField().getAlertValue().contains("-1")) {
                alertVal ="0";
            } else {

                alertVal = gpDeviceDataLists.get(i).getDeviceDataField().getAlertValue();

            }

            /*if (gpDeviceDataLists.get(i).getDeviceDataField().getRideTime().equals("0")||gpDeviceDataLists.get(i).getDeviceDataField().getRideTime().equals("")||gpDeviceDataLists.get(i).getDeviceDataField().getRideTime().contains("-1")) {
                alertTime = "0";
            } else {
                try {

                    String  datetimeString = DateFormat.format("dd/MM/yyyy HH:mm:ss", new Date(Long.parseLong(gpDeviceDataLists.get(i).getDeviceDataField().getRideTime()))).toString();
                    String timeString = datetimeString.substring(11,16);
                    String dateString1 = datetimeString.substring(0,11);
                    Log.d("data=e :",timeString+" "+dateString1+" ");
                    alertTime = timeString;
                }catch(Exception e){
                    e.printStackTrace();
                }

            }*/
            if (time.get(i).equals("0")||time.get(i).equals("")) {
                alertTime = "0";
            } else {
                try {

                    String  datetimeString = time.get(i);
                    String timeString = datetimeString.substring(11,16);
                    String dateString1 = datetimeString.substring(0,11);
                    Log.d("data=e :",timeString+" "+dateString1+" ");
                    alertTime = timeString;
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
            if (gpDeviceDataLists.get(i).getDeviceDataField().getAlertKiloMeter().equals("0")||gpDeviceDataLists.get(i).getDeviceDataField().getAlertKiloMeter().equals("")||gpDeviceDataLists.get(i).getDeviceDataField().getAlertKiloMeter().contains("-1")) {
                alertDist ="0";
            } else {
                if (Integer.parseInt(gpDeviceDataLists.get(i).getDeviceDataField().getAlertValue())==29){
                    alertDist=gpDeviceDataLists.get(i).getDeviceDataField().getAlertKiloMeter();
                }else{
                    alertDist =REAL_FORMATTER.format(Double.parseDouble(gpDeviceDataLists.get(i).getDeviceDataField().getAlertKiloMeter()));
                }
            }
            if (gpDeviceDataLists.get(i).getDeviceDataField().getZipCode().equals("0")||gpDeviceDataLists.get(i).getDeviceDataField().getZipCode().isEmpty()||gpDeviceDataLists.get(i).getDeviceDataField().getZipCode().contains("-1")) {
                alertZip = "Unknown";
            } else {
                alertZip = gpDeviceDataLists.get(i).getDeviceDataField().getZipCode();
            }
            if (gpDeviceDataLists.get(i).getDeviceDataField().getAlert().equals("0")||gpDeviceDataLists.get(i).getDeviceDataField().getAlert().isEmpty()||gpDeviceDataLists.get(i).getDeviceDataField().getAlert().contains("-1")) {
                alertSpeedLimit = "0";
            } else {
                alertSpeedLimit = gpDeviceDataLists.get(i).getDeviceDataField().getAlert();
            }

            if (gpDeviceDataLists.get(i).getDeviceDataField().getSpeed().equals("0")||gpDeviceDataLists.get(i).getDeviceDataField().getSpeed().isEmpty()||gpDeviceDataLists.get(i).getDeviceDataField().getSpeed().contains("-1")){
                speed = "0";
            }else {
                speed = String.valueOf(Math.round(Float.parseFloat(gpDeviceDataLists.get(i).getDeviceDataField().getSpeed())));
            }
            if (alertVal.equals("31")){
                alertSpeedLimit = String.valueOf(Math.round(Float.parseFloat(gpDeviceDataLists.get(i).getDeviceDataField().getPreviousSpeed())));
                alertsArrayList.add(new AlertValueModel(alertVal,alertTime,alertDist,alertZip,alertSpeedLimit,speed));
            }else {
                alertsArrayList.add(new AlertValueModel(alertVal,alertTime,alertDist,alertZip,alertSpeedLimit,speed));
            }

        }
        // alertsArrayList.add(new AlertValueModel("1","08.50","10.50","IND_641659"));
        alertValueAdapter = new AlertValueAdapter(alertsArrayList);
        recyclerViewAlertsValue.setNestedScrollingEnabled(false);
        recyclerViewAlertsValue.setHasFixedSize(true);
        recyclerViewAlertsValue.setAdapter(alertValueAdapter);
        RecyclerView.LayoutManager RecyclerViewLayoutManager = new LinearLayoutManager(context);
        recyclerViewAlertsValue.setLayoutManager(RecyclerViewLayoutManager);
        AppCompatButton btn_grid=layoutView.findViewById(R.id.grid_button);

        btn_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.hide();
                alertDialog.cancel();
                alertDialog.dismiss();
            }

        });
        cls_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.hide();
                alertDialog.cancel();
                alertDialog.dismiss();
            }
        });
    }


    public static String getSubCategoryValueUpper(int m_iNxAlertValue) {
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
            }
            else if ( m_iNxAlertValue==29) {
                data = "MOBILE_USE";
            }
            else if ( m_iNxAlertValue==30) {
                data = "OVER_SPEED";
            }else if (m_iNxAlertValue == 31){
                data = "SUDDEN_BRAKING";
            }else if (m_iNxAlertValue == 32){
                data = "FORCE_ACCELERATION";
            }
        }

        return data;
    }


    public static String getSubCategoryValueLowerr(int alert_value) {
        String data ="";
        if (alert_value>0) {
            if (alert_value==1) {
                data = "Accident";
            } else if (alert_value==2) {
                data = "Animal Crossing";
            } else if (alert_value==3) {
                data = "Caution";
            } else if (alert_value==4) {
                data = "Congestion";
            }  else if (alert_value==5) {
                data = "Curve";
            } else if (alert_value==6) {
                data = "Hill";
            } else if (alert_value==7) {
                data = "Hill Downwards";
            } else if (alert_value==8) {
                data = "Hill Upwards ";
            } else if (alert_value==9) {
                data = "Icy Conditions";
            } else if (alert_value==10) {
                data = "Intersection";
            } else if (alert_value==11) {
                data = "Lane Merge";
            } else if (alert_value==12) {
                data = "Low Gear Area";
            } else if (alert_value==13) {
                data = "Narrow Road";
            } else if (alert_value==14) {
                data = "No Overtaking";
            } else if (alert_value==15) {
                data = "No Overtaking Trucks";
            } else if (alert_value==16) {
                data = "Pedestrian Crossing";
            } else if (alert_value==17) {
                data = "Priority";
            } else if (alert_value==18) {
                data = "Priority to Oncoming Traffic ";
            } else if (alert_value==19) {
                data = "Railway Crossing";
            } else if (alert_value==20) {
                data = "Risk of Grounding";
            } else if (alert_value==21) {
                data = "School Zone";
            } else if (alert_value==22) {
                data = "Slippery Roads";
            } else if (alert_value==23) {
                data = "Stop Sign";
            } else if (alert_value==24) {
                data = "Traffic Light";
            } else if (alert_value==25) {
                data = "Tramway Crossing";
            } else if (alert_value==26) {
                data = "Wind";
            } else if (alert_value==27) {
                data = "Winding Road";
            } else if (alert_value==28) {
                data = "Yield";
            }else if (alert_value==29) {
                data = "Mobile Use";
            } else if (alert_value==30) {
                data = "Over Speed";
            }else if (alert_value==31) {
                data = "Sudden Braking";
            }else if (alert_value==32) {
                data = "Force Acceleration";
            }
        }
        return data;
    }

    public static String getAlertImageString(int alert_value) {
        String data ="";
        if (alert_value>0) {
            if (alert_value==1) {
                data = "accident";
            } else if (alert_value==2) {
                data = "animal_cross";
            } else if (alert_value==3) {
                data = "caution";
            } else if (alert_value==4) {
                data = "congestion";
            }  else if (alert_value==5) {
                data = "curves";
            } else if (alert_value==6) {
                data = "hill";
            } else if (alert_value==7) {
                data = "hill_downwards";
            } else if (alert_value==8) {
                data = "hill_upwards ";
            } else if (alert_value==9) {
                data = "icy";
            } else if (alert_value==10) {
                data = "intersection";
            } else if (alert_value==11) {
                data = "lane_merge";
            } else if (alert_value==12) {
                data = "low_gear_area";
            } else if (alert_value==13) {
                data = "narrow_road";
            } else if (alert_value==14) {
                data = "no_overtaking";
            } else if (alert_value==15) {
                data = "no_trucks";
            } else if (alert_value==16) {
                data = "pedestrian_crossing";
            } else if (alert_value==17) {
                data = "priority_oncoming";
            } else if (alert_value==18) {
                data = "priority_oncoming";
            } else if (alert_value==19) {
                data = "railway_cross";
            } else if (alert_value==20) {
                data = "risk_of_grounding";
            } else if (alert_value==21) {
                data = "school_zone";
            } else if (alert_value==22) {
                data = "slippery";
            } else if (alert_value==23) {
                data = "stop";
            } else if (alert_value==24) {
                data = "traffic_light";
            } else if (alert_value==25) {
                data = "tramway_crossing";
            } else if (alert_value==26) {
                data = "wind";
            } else if (alert_value==27) {
                data = "winding_road";
            } else if (alert_value==28) {
                data = "yield";
            }else if (alert_value==29) {
                data = "mobile_use";
            } else if (alert_value==30) {
                data = "over_speed";
            }else if (alert_value==31) {
                data = "sudden_breaking";
            } else if (alert_value==32) {
                data = "force_acceleration";
            }else {
                data = "over_speed";
            }
        }
        return data;
    }


    public static String getSubCategoryValueLower(int alert_value) {
        String data ="";
        if (alert_value>0) {
            if (alert_value==1) {
//                data = "Accident";
                data= String.valueOf(R.raw.alert_1);
            } else if (alert_value==2) {
//                data = "Animal Crossing";
                data= String.valueOf(R.raw.alert_2);
            } else if (alert_value==3) {
//                data = "Caution";
                data= String.valueOf(R.raw.alert_3);
            } else if (alert_value==4) {
//                data = "Congestion";
                data= String.valueOf(R.raw.alert_4);
            }  else if (alert_value==5) {
//                data = "Curve";
                data= String.valueOf(R.raw.alert_5);
            } else if (alert_value==6) {
//                data = "Hill";
                data= String.valueOf(R.raw.alert_6);
            } else if (alert_value==7) {
//                data = "Hill Downwards";
                data= String.valueOf(R.raw.alert_7);
            } else if (alert_value==8) {
//                data = "Hill Upwards ";
                data= String.valueOf(R.raw.alert_8);
            } else if (alert_value==9) {
//                data = "Icy Conditions";
                data= String.valueOf(R.raw.alert_9);
            } else if (alert_value==10) {
//                data = "Intersection";
                data= String.valueOf(R.raw.alert_10);
            } else if (alert_value==11) {
//                data = "Lane Merge";
                data= String.valueOf(R.raw.alert_11);
            } else if (alert_value==12) {
//                data = "Low Gear Area";
                data= String.valueOf(R.raw.alert_12);
            } else if (alert_value==13) {
//                data = "Narrow Road";
                data= String.valueOf(R.raw.alert_13);
            } else if (alert_value==14) {
//                data = "No Overtaking";
                data= String.valueOf(R.raw.alert_14);
            } else if (alert_value==15) {
//                data = "No Overtaking Trucks";
                data= String.valueOf(R.raw.alert_15);
            } else if (alert_value==16) {
//                data = "Pedestrian Crossing";
                data= String.valueOf(R.raw.alert_16);
            } else if (alert_value==17) {
//                data = "Priority";
                data= String.valueOf(R.raw.alert_17);
            } else if (alert_value==18) {
//                data = "Priority to Oncoming Traffic ";
                data= String.valueOf(R.raw.alert_18);
            } else if (alert_value==19) {
//                data = "Railway Crossing";
                data= String.valueOf(R.raw.alert_19);
            } else if (alert_value==20) {
//                data = "Risk of Grounding";
                data= String.valueOf(R.raw.alert_20);
            } else if (alert_value==21) {
//                data = "School Zone";
                data= String.valueOf(R.raw.alert_21);
            } else if (alert_value==22) {
//                data = "Slippery Roads";
                data= String.valueOf(R.raw.alert_22);
            } else if (alert_value==23) {
//                data = "Stop Sign";
                data= String.valueOf(R.raw.alert_23);
            } else if (alert_value==24) {
//                data = "Traffic Light";
                data= String.valueOf(R.raw.alert_24);
            } else if (alert_value==25) {
//                data = "Tramway Crossing";
                data= String.valueOf(R.raw.alert_25);
            } else if (alert_value==26) {
//                data = "Wind";
                data= String.valueOf(R.raw.alert_26);
            } else if (alert_value==27) {
//                data = "Winding Road";
                data= String.valueOf(R.raw.alert_27);
            } else if (alert_value==28) {
//                data = "Yield";
                data= String.valueOf(R.raw.alert_28);
            }else if (alert_value==29) {
                data = "Mobile Use";
            }
            else if (alert_value==30) {
                data = "Over Speed";
            }
        }
        return data;
    }

    public static void callLogin(Context context, OnDialogButtonClickListener listener) {
        RestMethods restMethods;
        PreferenceManager mPrefernce_Manager=PreferenceManager.getInstance();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setId("");
        loginRequest.setCategory(Constants.category_Password);
        loginRequest.setCompanyId(Constants.companyID);
        loginRequest.setUsername(Preference_Details.getUserName());
        loginRequest.setPassword(Preference_Details.getPWD());
        LoginRequest.UserSessionField userSessionField = new LoginRequest.UserSessionField();
        userSessionField.setDeviceType("ANDROID");
        userSessionField.setDeviceStatus("DEFAULT");
        userSessionField.setDeviceOrderId("3523");
        userSessionField.setDeviceUniqueId(Preference_Details.getDeviceId());
        userSessionField.setDeviceVersionNumber(String.valueOf(Build.VERSION.RELEASE));
        DecimalFormat REAL_FORMATTER = new DecimalFormat("0");
        userSessionField.setDeviceModelName(String.valueOf(Build.MANUFACTURER)+" "+String.valueOf(Build.MODEL)+" "+REAL_FORMATTER.format(Double.parseDouble(Build.VERSION.RELEASE)));
        loginRequest.setUserSessionField(userSessionField);
        restMethods = RestClient.buildHTTPClient();
        restMethods.getLoginResponse(Constants.referredBy,Constants.Static_SessionID,loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                try{
                    if (response.code()==201) {
                        Log.e("status",""+response.body());
                        Constants.frst_name=response.body().getUser().getFirstName();
                        Constants.regSessionID=response.body().getUserSession().getId();
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.pwd,Preference_Details.getPWD());
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

                    }  else if(response.code()==412){
                        try {
                            String Error=response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for(int i=0;i<jsonArray1.length();i++){
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                            }

                        } catch (Exception e){
                            e.printStackTrace();
                        }

                    } else {
                        String Error=response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text=obj.getString("message");
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                if (t.toString().contains("Unable to resolve host")) {
                    Util_functions.ShowAlertError(context,"Check Your Internet Connection","ok",listener);
                }else {

                }
            }
        });
    }

    public static Long getUniTime() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        long timeInMili = calendar .getTimeInMillis();
        return timeInMili;
    }

    public static void getMapUpdate(Context context, OnDialogButtonClickListener listener) {
        RestMethods restMethods;
        restMethods = RestClient.buildHTTPClient();
        PreferenceManager mPrefernce_Manager=PreferenceManager.getInstance();
        restMethods.MapUpdateReq(Constants.referredBy,Preference_Details.getLoginSessID(),Constants.companyID,Constants.divisionId,Constants.moduleID,Constants.type_map_update,"createdAt","DESC","0","1").enqueue(new Callback<List<MapUpdateResponse>>() {
            @Override
            public void onResponse(Call<List<MapUpdateResponse>> call, Response<List<MapUpdateResponse>> response) {
                try{
                    if (response.code()==200) {
                        for(int i=0;i<response.body().size();i++) {
                            mPrefernce_Manager.setString(Preference_Keys.LOginKeys.mapCode,response.body().get(i).getCode());
                        }
                    }  else if(response.code()==412){
                        try {
                            String Error=response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for(int i=0;i<jsonArray1.length();i++){
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                               /* startActivity(new Intent(MainActivity.this,LoginPageActivity.class));
                                finish();*/
                            }
                            //startActivity(new Intent(MainActivity.this,LoginPageActivity.class));
                            //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.resp_empty),true);
                            //finish();

                        } catch (Exception e){
                            e.printStackTrace();
                            //startActivity(new Intent(MainActivity.this,LoginPageActivity.class));
                            //finish();
                        }

                    } else {
                        String Error=response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text=obj.getString("message");
                        //Util_functions.ShowValidateAlertError(MainActivity.this,text,"OK");
                        //startActivity(new Intent(MainActivity.this,LoginPageActivity.class));
                        // Util_functions.Show_Toast(getApplicationContext(),getString(R.string.txt_error),true);
                        //finish();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    // startActivity(new Intent(MainActivity.this,LoginPageActivity.class));
                    //finish();
                }
            }

            @Override
            public void onFailure(Call<List<MapUpdateResponse>> call, Throwable t) {
                if (t.toString().contains("Unable to resolve host")) {
                    ShowAlertError(context,"Check Your Internet Connection","ok",listener);                }else {
                    //startActivity(new Intent(MainActivity.this,RoadStartPageActivity.class));
                    //  finish();
                }
            }
        });

    }
    /**
     * Returns true if requesting location updates, otherwise returns false.
     *
     * @param context The {@link Context}.
     */
    public static boolean requestingLocationUpdates(Context context) {
        return android.preference.PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(Constants.KEY_REQUESTING_LOCATION_UPDATES, true);
    }

    /**
     * Stores the location updates state in SharedPreferences.
     * @param requestingLocationUpdates The location updates state.
     */
    public static void setRequestingLocationUpdates(Context context, boolean requestingLocationUpdates) {
        android.preference.PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(Constants.KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
                .apply();
    }

    /**
     * Returns the {@code location} object as a human readable string.
     * @param location  The {@link Location}.
     */
    public static String getLocationText(Location location) {
        return location == null ? "Unknown location" :
                "(" + location.getLatitude() + ", " + location.getLongitude() +", "+location.getSpeed()+", "+location.getBearing()+", "+Constants.bg_stop+")";
    }

    public static String getLocationTitle(Context context) {
        return context.getString(R.string.location_updated,
                java.text.DateFormat.getDateTimeInstance().format(new Date()));
    }



}
