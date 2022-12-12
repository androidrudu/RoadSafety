package com.montbleu.OBD.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.montbleu.OBD.adapter.DeviceListAdapter;
import com.montbleu.OBD.adapter.OBDAdapter;
import com.montbleu.OBD.adapter.Uncheck_Adapter;
import com.montbleu.OBD.changeStatus;
import com.montbleu.OBD.model.OBDModel;
import com.montbleu.OBD.model.OBDRespResult;
import com.montbleu.Retrofit.RestClient;
import com.montbleu.Retrofit.RestMethods;
import com.montbleu.Utils.Constants;
import com.montbleu.Utils.PreferenceManager;
import com.montbleu.Utils.Preference_Details;
import com.montbleu.Utils.ProgressDialog;
import com.montbleu.Utils.Util_functions;
import com.montbleu.interfaces.OnDialogButtonClickListener;
import com.montbleu.interfaces.Preference_Keys;
import com.montbleu.interfaces.onWifiCredListener;
import com.montbleu.model.BlueDataResponse;
import com.montbleu.model.Column;
import com.montbleu.model.UserDeviceDataModalResp;
import com.montbleu.model.UserDeviceResponse;
import com.montbleu.roadsafety.BeautifulProgressDialog;
import com.montbleu.roadsafety.R;
import com.montbleu.roadsafety.activity.LoginPageActivity;
import com.montbleu.roadsafety.activity.MorepageActvity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static android.content.ContentValues.TAG;

import static com.montbleu.OBD.activity.DeviceListActivity.visibleStatus;
import static com.montbleu.Utils.Constants.StartDis;
import static com.montbleu.Utils.Constants.static_porID;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MBTActivity extends AppCompatActivity implements View.OnClickListener,changeStatus, OnDialogButtonClickListener, onWifiCredListener {
    static OBDAdapter obdAdapter;
    RestMethods restMethods;

    private static PreferenceManager mPrefernce_Manager=PreferenceManager.getInstance();
    ArrayList<UserDeviceDataModalResp> userDeviceDataModalResps = new ArrayList<>();
    /*static String[] name_val = {"RPM",
            "SPEED",
            "ENGINE OIL TEMP",
            "ENGINE COOLANT TEMP",
            "Ambient Air Temperature",
            "AIR INTAKE TEMP",
            "CONTROL MODULE Power Supply",
            "FUEL RAIL PRESSURE",
            "ENGINE LOAD",
            "FUEL CONSUMPTION RATE",
            "THROTTLE",
            "INTAKE MANIFOLD PRESSURE",
            "TIMING ADVANCE",
            "Air/Fuel Ratio",
            "Run Time Since Engine Start",
            "DISTANCE TRAVELED With MIL ON",
            "Short Term Fuel Trim Bank 1",
            "Short Term Fuel Trim Bank 2",
            "Long Term Fuel Trim Bank 1",
            "Long Term Fuel Trim Bank 2",
            "FUEL LEVEL",
            "Command Equivalence Ratio",
            "BAROMETRIC PRESSURE",
            "FUEL PRESSURE",
            "Test1",
            "Test2",
            "Test3",
            "Test4",
            "Test5",
            "Test6"
    };
*/
    static String[] name_val = {
    "Fuel System Status value",
    "MAF air flow rate",
    "V FUEL LEVEL",
    "ODOMETER",
    "Distance traveled since codes cleared value",
    "V OIL TEMP",
    "Oxygen Sensor 1 Air Fuel Equivalence Ratio value",
   "Absolute load value value",
    "Relative throttle position value",
    "Absolute throttle position B value",
    "Accelerator pedal position D value",
    "V FUEL RATE",
    "V AIR TEMP",
    "V Engine Load",
    "V COOLANT TEMP",
    "V S Fuel trim Bank 1",
    "V Intake Manifold Absolute Pressure",
    "V RPM",
    "V SPEED",
    "V Timing Advance",
    "V AIR FUEL RATIO",
    "V THROTTLE POS",
    "V Run Time Since Engine Start",
    "V  BATT VOLTAGE",
    "V Distance MIL",
    "V BAROMETRIC PRESSURE",
    "Commanded evaporative purge value",
    "Warm ups since codes cleared value",
    "Accelerator pedal position E value",
    "Commanded throttle actuator value",
    "V L Fuel trim Bank 1",
    "V S Fuel trim Bank 2",
    "V L Fuel trim Bank 2",
    "V FUEL PRESSURE",
    "V INTAKE TEMP",
    "V FUEL RAIL PRESSURE",
    "Oxygen Sensor 1 Air Fuel Ratio",
    "Oxygen Sensor 1 voltage",
    "Cylinder Fuel Rate value",
    "Engine Friction Percent Torque value",
    "Hybrid battery pack remaining life value",
    "Transmission Actual  Gear value",
    "Engine Friction Percent Torque  value",
    "Commanded EGR value",
    "EGR Error value",
    "Fuel Rail  Gauge Pressure value",
    "latitude",
    "longitude",
    "altitude",
    "satellites",
    };
    static String name_id[] = {
            "pm01",
            "pm02",
            "pm03",
            "pm04",
            "pm05",
            "pm06",
            "pm07",
            "pm08",
            "pm09",
            "pm10",
            "pm11",
            "pm12",
            "pm13",
            "pm14",
            "pm15",
            "pm16",
            "pm17",
            "pm18",
            "pm19",
            "pm20",
            "pm21",
            "pm22",
            "pm23",
            "pm24",
            "pm25",
            "pm26",
            "pm27",
            "pm28",
            "pm29",
            "pm30",
            "pm31",
            "pm32",
            "pm33",
            "pm34",
            "pm35",
            "pm36",
            "pm37",
            "pm38",
            "pm39",
            "pm40",
            "pm41",
            "pm42",
            "pm43",
            "pm44",
            "pm45",
            "pm46",
            "pm47",
            "pm48",
            "pm49",
            "pm50",
    };
    static String[] name = {
            "Fuel_System_Status_value",
            "MAF_air_flow_rate",
            "V_FUEL_LEVEL",
            "ODOMETER",
            "Distance_traveled_since_codes_cleared_value",
            "V_OIL_TEMP",
            "Oxygen_Sensor_1_Air_Fuel_Equivalence_Ratio_value",
            "Absolute_load_value_value",
            "Relative_throttle_position_value",
            "Absolute_throttle_position_B_value",
            "Accelerator_pedal_position_D_value",
            "V_FUEL_RATE",
            "V_AIR_TEMP",
            "V_Engine_Load",
            "V_COOLANT_TEMP",
            "V_S_Fuel_trim_Bank_1",
            "V_Intake_Manifold_Absolute_Pressure",
            "V_RPM",
            "V_SPEED",
            "V_Timing_Advance",
            "V_AIR_FUEL_RATIO",
            "V_THROTTLE_POS",
            "V_Run_Time_Since_Engine_Start",
            "V_BATT_VOLTAGE",
            "V_Distance_MIL",
            "V_BAROMETRIC_PRESSURE",
            "Commanded_evaporative_purge_value",
            "Warm_ups_since_codes_cleared_value",
            "Accelerator_pedal_position_E_value",
            "Commanded_throttle_actuator_value",
            "V_L_Fuel_trim_Bank_1",
            "V_S_Fuel_trim_Bank_2",
            "V_L_Fuel_trim_Bank_2",
            "V_FUEL_PRESSURE",
            "V_INTAKE_TEMP",
            "V_FUEL_RAIL_PRESSURE",
            "Oxygen_Sensor_1_Air_Fuel_Ratio",
            "Oxygen_Sensor_1_voltage",
            "Cylinder_Fuel_Rate_value",
            "Engine_Friction_Percent_Torque_value",
            "Hybrid_battery_pack_remaining_life_value",
            "Transmission_Actual_Gear_value",
            "Engine_Friction_Percent_Torque_value",
            "Commanded_EGR_value",
            "EGR_Error_value",
            "Fuel_Rail_Gauge_Pressure_value",
            "latitude",
            "longitude",
            "altitude",
            "satellites",
    };
    /*static String[] name = {"RPM",
            "SPEED",
            "ENGINE_OIL_TEMP",
            "ENGINE_COOLANT_TEMP",
            "AMBIENT_AIR_TEMP",
            "AIR_INTAKE_TEMP",
            "CONTROL_MODULE_VOLTAGE",
            "FUEL_RAIL_PRESSURE",
            "ENGINE_LOAD",
            "FUEL_CONSUMPTION_RATE",
            "THROTTLE",
            "INTAKE_MANIFOLD_PRESSURE",
            "TIMING_ADVANCE",
            "AIR_FUEL_RATIO",
            "Run_Time_Since_Engine_Start",
            "DISTANCE_TRAVELED_MIL_ON",
            "Short Term Fuel Trim Bank 1",
            "Short Term Fuel Trim Bank 2",
            "Long Term Fuel Trim Bank 1",
            "Long Term Fuel Trim Bank_2",
            "FUEL_LEVEL",
            "EQUIV_RATIO",
            "BAROMETRIC_PRESSURE",
            "FUEL_PRESSURE",
            "Test1",
            "Test2",
            "Test3",
            "Test4",
            "Test5",
            "Test6"
    };*/
    static int[] image = {
            R.mipmap.rpm,
            R.mipmap.speed,
            R.mipmap.oil_temp,
            R.mipmap.coolant_temp,
            R.mipmap.air_temp,
            R.mipmap.intake_temp,
            R.mipmap.batt_volt,
            R.mipmap.fuel_rail_pressure,
            R.mipmap.engine_load,
            R.mipmap.fuel_rate,
            R.drawable.chauffer,
            R.mipmap.car_manifold,
            R.mipmap.timing_advance,
            R.mipmap.air_fuel_ratio,
            R.mipmap.engine_start,
            R.mipmap.distance_travel,
            R.mipmap.fuel_short_term,
            R.mipmap.fuel_short_term,
            R.mipmap.fuel_long_term,
            R.mipmap.fuel_long_term,
            R.mipmap.fuel_level,
            R.mipmap.equivalence_ratio,
            R.mipmap.barometric_pressure,
            R.mipmap.fuel_pressure,
            R.mipmap.ambient_temp,
            R.mipmap.ambient_temp,
            R.mipmap.ambient_temp,
            R.mipmap.ambient_temp,
            R.mipmap.ambient_temp,
            R.mipmap.ambient_temp,
            R.mipmap.ambient_temp,
            R.mipmap.ambient_temp,
            R.mipmap.ambient_temp,
            R.mipmap.ambient_temp,
            R.mipmap.ambient_temp,
            R.mipmap.ambient_temp,
            R.mipmap.ambient_temp,
            R.mipmap.ambient_temp,
            R.mipmap.ambient_temp,
            R.mipmap.ambient_temp,
            R.mipmap.ambient_temp,
            R.mipmap.ambient_temp,
            R.mipmap.ambient_temp,
            R.mipmap.ambient_temp,
            R.mipmap.ambient_temp,
            R.mipmap.ambient_temp,
            R.mipmap.ambient_temp,
            R.mipmap.ambient_temp,
            R.mipmap.ambient_temp,
            R.mipmap.ambient_temp,
            R.mipmap.ambient_temp,

    };

    List<OBDModel> obdModelList = new ArrayList<>();
    DeviceListAdapter deviceListAdapter;
    static List<OBDModel> obdModelListClone = new ArrayList<>();
    List<OBDModel> obdModelListClone2 = new ArrayList<>();
    List<OBDModel> obdModelListFilter = new ArrayList<>();
    static List<OBDRespResult> obdSocketResult = new ArrayList<>();
    Boolean Progress_status = true;
    static RecyclerView mrecyclerOBDList;
    static ImageView imgAdd,img_wifi_gprs;
    Uncheck_Adapter uncheckAdapter;
    private static final int NO_BLUETOOTH_ID = 0;
    private static final int BLUETOOTH_DISABLED = 1;
    private static final int REQUEST_ENABLE_BT = 1234;
    private static TextView btStatusTextView, start_discovery, paired_device;
    static BluetoothAdapter mBluetoothAdapter;
    private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
    private ArrayList<BluetoothDevice> mDeviceAvailableList = new ArrayList<BluetoothDevice>();
    private Boolean mEnable = true;
    static Parcelable recyclerViewState;
    private static BluetoothSocket mSocket = null;
    private static BufferedReader mBufferedReader = null;
    static TextView textNoData;
    static NestedScrollView data_scroll;
    static MBTActivity mbtActivity;
    static InputStream aStream = null;
    static InputStreamReader aReader = null;
    static OutputStream aOutputStream = null;
    //private static final String UUID_SERIAL_PORT_PROFILE = "00001101-0000-1000-8000-00805f9b34fb";
    //private static final String UUID_SERIAL_PORT_PROFILE = "00001101-0000-1000-8000-00805f9b34fb";
    private static final String UUID_SERIAL_PORT_PROFILE = "00001101-0000-1000-8000-00805F9B34FB";
    BluetoothAdapter btAdapter = null;
    public static Boolean retry_remt_status = false;
    private String pm01;
    private Handler handler = new Handler();
    private Runnable runnable;
    static TableLayout headerLayout;
    static  TextView OBD_Lat,OBD_Lng,OBD_alt,OBD_sat;
    static BeautifulProgressDialog progressDialog;
    static BluetoothSPP bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mbtactivity);
        mbtActivity = MBTActivity.this;
        progressDialog = new BeautifulProgressDialog(mbtActivity, BeautifulProgressDialog.withLottie, null);
        progressDialog.setLottieLocation(Constants.lottiePath);
        progressDialog.setLottieLoop(true);
        progressDialog.setLayoutColor(Color.TRANSPARENT);
        progressDialog.setLayoutElevation(0);
        progressDialog.setCancelable(true);
        OBD_Lat = findViewById(R.id.OBD_Lat);
        OBD_Lng = findViewById(R.id.OBD_Lng);
        OBD_alt = findViewById(R.id.OBD_alt);
        OBD_sat = findViewById(R.id.OBD_sat);
        headerLayout = findViewById(R.id.headerLayout);
        textNoData = findViewById(R.id.textNoData);
        mrecyclerOBDList = findViewById(R.id.recyclerList);
        imgAdd = findViewById(R.id.imgAdd);
        btStatusTextView = findViewById(R.id.BT_STATUS);
        start_discovery = findViewById(R.id.start_discovery);
        paired_device = findViewById(R.id.paired_device);
        headerLayout = findViewById(R.id.headerLayout);
        img_wifi_gprs=findViewById(R.id.img_wifi_gprs);
        img_wifi_gprs.setOnClickListener(this);
        start_discovery.setOnClickListener(this);
        paired_device.setOnClickListener(this);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        imgAdd.setOnClickListener(this);
        getOBDetails();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        data_scroll = findViewById(R.id.data_scroll);
        Constants.regPin="disconnect";
        //  getLiveData();
        //  showDialog();

        bt = new BluetoothSPP(MBTActivity.this);
        if (!bt.isBluetoothEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if(!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_ANDROID);
            }
        }

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                Log.d("Messagee",message);
                if (message.equals("SIM NOT INSERTED")){
                    showDialog_Connect();
                }else if (message.equals("NO INTERNET CONNECTION")){
                    showDialog_Connect();
                } else {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (message.contains("pm50")||message.contains("pm49")){
                        Gson gson = new Gson();
                        BlueDataResponse dataa = gson.fromJson(String.valueOf(message), BlueDataResponse.class);
                        implementList(dataa.getData());
                    }

                }

                //Toast.makeText(MBTActivity.this, message, Toast.LENGTH_SHORT).show();
                // Do something when data incoming
            }
        });
        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceDisconnected() {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                paired_device.setText("Disconnected");
                Util_functions.AlertDialog(data_scroll.getContext(), mbtActivity.getString(R.string.Connect_fail), mbtActivity.getString(R.string.retry_again), mbtActivity.getString(R.string.remote_access), mbtActivity);
                Log.d("Messagee","Disconnected");
            }

            public void onDeviceConnectionFailed() {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                paired_device.setText("Disconnected");
                Util_functions.AlertDialog(data_scroll.getContext(), mbtActivity.getString(R.string.Connect_fail), mbtActivity.getString(R.string.retry_again), mbtActivity.getString(R.string.remote_access), mbtActivity);
                Log.d("Messagee","Connection Failed");
            }

            public void onDeviceConnected(String name, String address) {
                paired_device.setText("Connected");
                DeviceDataResponse();
                Log.d("Messagee","Connected");
            }
        });
        bt.setAutoConnectionListener(new BluetoothSPP.AutoConnectionListener() {
            public void onNewConnection(String name, String address) {
                if (paired_device.getText().toString().equals("Paired Device")){
                    paired_device.setText("Connected");
                }else if (paired_device.getText().toString().equals("DisConnected")){
                    paired_device.setText("Connected");
                } else {
                    paired_device.setText("DisConnected");
                }
                Log.i("Messagee", "New Connection - " + name + " - " + address);
            }

            public void onAutoConnectionStarted() {
                Log.i("Messagee", "Auto menu_connection started");
            }
        });
        start_discovery.performClick();
        StartDis=true;
    }

    @Override
    public void onPause() {
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("obd_data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private JSONObject getJsonValidate() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(loadJSONFromAsset());
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.show();

        }
        return jsonObject;
    }

    private void getOBDetails() {
        //progressDialog.show();
        if (obdAdapter == null) { // it works second time and later
            // it works first time
            JSONObject jsonObject = getJsonValidate();
            try {
                obdModelList.clear();
                JSONArray jsonArray = jsonObject.getJSONArray("OBD_Data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                    for (int j = 0; j < name.length; j++) {

                        if (name[j].equals(jsonObject1.get("id"))) {
                            obdModelList.add(new OBDModel(jsonObject1.get("id").toString(), jsonObject1.get("value").toString(), "", image[j], true));
                        }
                    }
                }
                obdModelListClone.clear();
                obdModelListClone.addAll(obdModelList);
                if (obdAdapter == null) {
                    obdAdapter = new OBDAdapter(this, obdModelListClone);
                    GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
                    //at last set adapter to recycler view.
                    mrecyclerOBDList.setHasFixedSize(true);
                    mrecyclerOBDList.setLayoutManager(layoutManager);
                    mrecyclerOBDList.setAdapter(obdAdapter);
                    mrecyclerOBDList.setNestedScrollingEnabled(true);
                    // getLiveData();
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    obdModelListClone.clear();
                    obdModelListClone.addAll(obdModelListFilter);
                    obdAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                progressDialog.dismiss();

            }
        } else {
            progressDialog.dismiss();
        }
    }

    private static UUID getSerialPortUUID() {
        //return UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        return UUID.fromString(UUID_SERIAL_PORT_PROFILE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static boolean openDeviceConnection(BluetoothDevice aDevice)
            throws IOException {
        try {

            mSocket = aDevice.createRfcommSocketToServiceRecord(getSerialPortUUID());
            //Toast.makeText(getApplicationContext(), "Socket :" + mSocket, Toast.LENGTH_SHORT).show();

            //mSocket.connect();
            DeviceDataResponse();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Could not connect to device", e);
            if (e.getMessage().contains("read failed, socket might")) {
                Util_functions.AlertDialog(data_scroll.getContext(), mbtActivity.getString(R.string.Connect_fail), mbtActivity.getString(R.string.retry_again), mbtActivity.getString(R.string.remote_access), mbtActivity);
                progressDialog.dismiss();
            }
            // Toast.makeText(MBTActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            close(mBufferedReader);
            close(aReader);
            close(aStream);
            close(mSocket);
            //   throw e;
        }

        return false;
    }

    public static void write(String s) throws IOException {
        if (mSocket != null) {
            if (mSocket.isConnected()) {
                aOutputStream = mSocket.getOutputStream();
                aOutputStream.write(s.getBytes());
            }
        }
    }

    private static void DeviceDataResponse() {
       /* ProgressDialog.setLottieProgressDialog(mbtActivity.getParent(), Constants.lottiePath);
        ProgressDialog.show();*/
        RestMethods restMethods = RestClient.buildHTTPClient();
        restMethods.userDeviceResponse(Constants.referredBy, Preference_Details.getLoginSessID(), Constants.companyID, Preference_Details.getUserID(), Constants.type_live_Data, "id", "ASC", 0, 100).enqueue(new Callback<List<UserDeviceResponse>>() {
            @Override
            public void onResponse(Call<List<UserDeviceResponse>> call, Response<List<UserDeviceResponse>> response) {
                Log.e("status", "" + response.body());
                try {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            progressDialog.dismiss();
                            //ProgressDialog.dismiss();
                            if (response.body().size()>0) { //true
                                // Constants.Bledata = "000000000000000000000000" + "," +"000000000000000000000000";
                                //  Constants.Bledata = "111111111111111111111111" + "," +"111111111111111111111111";
                                Constants.Bledata = Preference_Details.getPortionID() + "," + Constants.companyID;
                                // Util_functions.AlertDialog(mbtActivity, "Do You Want Configure ?", "ok", "cancel", mbtActivity);
                                /*ProgressDialog.setLottieProgressDialog(mbtActivity, Constants.lottiePath);
                                ProgressDialog.show();*/
                                Constants.regPin="connect";
                                ConnectBlu();
                            } else {
                                //Constants.Bledata = static_porID + "," + Constants.companyID;
                                //Constants.Bledata = Preference_Details.getPortionID() + "," + Constants.companyID;
                                // Util_functions.AlertDialog(mbtActivity, "Do You Want Configure ?", "ok", "cancel", mbtActivity);
                                /*ProgressDialog.setLottieProgressDialog(mbtActivity, Constants.lottiePath);
                                ProgressDialog.show();*/
                                Constants.Bledata = Preference_Details.getPortionID() + "," + Constants.companyID;
                                Constants.regPin="disconnect";
                                ConnectBlu();
                            }

                        } else {

                        }
                    } else if (response.code() == 412) {
                        try {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowValidateAlertError(mbtActivity.getApplicationContext(), object1.getString("message"), "OK");
                                // ProgressDialog.dismiss();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            // ProgressDialog.dismiss();

                        }

                    } else {
                        String Error = response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text = obj.getString("message");
                        Util_functions.ShowValidateAlertError(mbtActivity.getApplicationContext(), text, "OK");
                        //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.txt_error),true);
                        // ProgressDialog.dismiss();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // ProgressDialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<List<UserDeviceResponse>> call, Throwable t) {
                t.printStackTrace();
                // ProgressDialog.dismiss();

            }
        });

    }

    private static void close(Closeable aConnectedObject) {
        if (aConnectedObject == null) return;
        try {
            aConnectedObject.close();
        } catch (IOException e) {
        }
        aConnectedObject = null;
    }

    public static void addTableRow(String id, String key, String val) {

        if (obdAdapter != null) { // it works second time and later
            //obdModelList.clear();
            obdModelListClone.size();
            for (int i = 0; i < obdModelListClone.size(); i++) {
                if (obdModelListClone.get(i).getName().toLowerCase().contains(key.toLowerCase())) {
                    for (int j = 0; j < name.length; j++) {
                        if (name[j].equals(id)) {
                            obdModelListClone.set(i, new OBDModel(id, key, val, image[j], true));
                            break;
                        }
                    }
                }
                // restore recycleView state
                //   Toast.makeText(getApplicationContext(),"List--->>"+obdModelList,Toast.LENGTH_SHORT).show();
            }

            mbtActivity.runOnUiThread(new Runnable() {
                public void run() {
                    obdAdapter.notifyDataSetChanged();
                   // mrecyclerOBDList.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                    headerLayout.setVisibility(View.VISIBLE);
                    mrecyclerOBDList.setVisibility(View.VISIBLE);
                    textNoData.setVisibility(View.GONE);
                    data_scroll.setVisibility(View.VISIBLE);
                    imgAdd.setVisibility(View.VISIBLE);
                }
            });

        }
    }

    public static void addRemoteTableRow(String id, String key, String val) {

        if (obdAdapter != null) { // it works second time and later
            //obdModelList.clear();
            obdModelListClone.size();
            for (int i = 0; i < obdModelListClone.size(); i++) {
                if (obdModelListClone.get(i).getName().toLowerCase().contains(key.toLowerCase())) {
                    for (int j = 0; j < name.length; j++) {
                        if (name[j].equals(id)) {
                            obdModelListClone.set(i, new OBDModel(id, key, val, image[j], true));
                            obdAdapter.notifyDataSetChanged();

                          /*  try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }*/
                            break;
                        }
                    }
                }
                // restore recycleView state
                //   Toast.makeText(getApplicationContext(),"List--->>"+obdModelList,Toast.LENGTH_SHORT).show();
            }
            mrecyclerOBDList.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            mrecyclerOBDList.setVisibility(View.VISIBLE);
            headerLayout.setVisibility(View.VISIBLE);
            textNoData.setVisibility(View.GONE);
            data_scroll.setVisibility(View.VISIBLE);
            imgAdd.setVisibility(View.VISIBLE);
            progressDialog.dismiss();
            //mbtActivity.recreate();

        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgAdd:
                addOption();
                break;
            case R.id.start_discovery:
                if (mBluetoothAdapter.isEnabled()) {
                    progressDialog.setMessage(getString(R.string.loading_bluetooth_title));
                    progressDialog.show();
                    StartDis=true;
                    startDiscovvry();
                } else {
                    Util_functions.Show_Toast(getApplicationContext(),getString(R.string.pref_bluetooth_summaryOff),true);
                    btStatusTextView.setText(getString(R.string.status_bluetooth_disabled));
                    // showDialog(BLUETOOTH_DISABLED);
                }
                break;
            case R.id.paired_device:
                if (mBluetoothAdapter.isEnabled()) {
                    pairedDevice();
                } else {
                    Util_functions.Show_Toast(getApplicationContext(), getString(R.string.pref_bluetooth_summaryOff), true);
                    showDialog(BLUETOOTH_DISABLED);
                    btStatusTextView.setText(getString(R.string.status_bluetooth_disabled));
                }
                break;
            case R.id.img_wifi_gprs:
                showDialog_Connect();
        }
    }

    private void pairedDevice() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices == null || pairedDevices.size() == 0) {
            Util_functions.Show_Toast(getApplicationContext(), "No Paired Devices Found", true);
        } else {
            ArrayList<BluetoothDevice> list = new ArrayList<BluetoothDevice>();
            list.addAll(pairedDevices);
            paired_device.setClickable(false);
            Intent intent = new Intent(MBTActivity.this, DeviceListActivity.class);
            //intent.putParcelableArrayListExtra("device.list", list);
            startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
            //startActivity(intent);

        }
    }

    private void startDiscovvry() {
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == 0) {
            if (mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.cancelDiscovery();
                mBluetoothAdapter.startDiscovery();
            }
            mBluetoothAdapter.startDiscovery();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                //Location
                case 1:
                    start_discovery.performClick();
                    StartDis=true;
                    break;
                //Call

            }


        }
    }

    private void addOption() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alertDialog;
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutView = inflater.inflate(R.layout.obd_add_list, null);
        builder.setView(layoutView);
        alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        RecyclerView recycler_unchecked = layoutView.findViewById(R.id.recycler_unchecked);
        RecyclerView.LayoutManager RecyclerViewLayoutManager = new LinearLayoutManager(this);
        recycler_unchecked.setLayoutManager(RecyclerViewLayoutManager);
        TextView mtext = layoutView.findViewById(R.id.text_no_record);
        Button submit = layoutView.findViewById(R.id.submit);
        if (uncheckAdapter == null) {
            uncheckAdapter = new Uncheck_Adapter(this, obdModelListClone, this);
        } else {
            obdModelListClone.clear();
            obdModelListClone.addAll(obdModelListClone2);
            obdModelList.clear();
            obdModelList.addAll(obdModelListClone2);
            uncheckAdapter.notifyDataSetChanged();
        }
        recycler_unchecked.setHasFixedSize(true);
        recycler_unchecked.setAdapter(uncheckAdapter);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obdModelList.clear();
                obdModelListFilter.clear();
                for (int i = 0; i < obdModelListClone.size(); i++) {
                    if (obdModelListClone.get(i).getStatus().booleanValue()) {
                        obdModelListFilter.add(obdModelListClone.get(i));
                    }
                }
                obdModelListClone2.clear();
                obdModelListClone2.addAll(obdModelListClone);
                //  obdModelList.addAll(obdModelListClone);
                //  uncheckAdapter.notifyDataSetChanged();
                obdModelListClone.clear();
                obdModelListClone.addAll(obdModelListFilter);
                obdAdapter.notifyDataSetChanged();
               /* obdModelListClone.clear();
                obdModelListClone.addAll(obdModelList);*/
                alertDialog.hide();
                alertDialog.dismiss();
            }
        });

    }

    @Override
    public void checkStatus(Boolean status, int pos) {
        obdModelListClone.get(pos).setStatus(status);
    }

    @Override
    public void Submit() {

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!Constants.BluetoothName.equals("")){
            if(bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                bt.disconnect();
            }else {
                bt.autoConnect(Constants.BluetoothName);
            }

            Constants.BluetoothName="";
        }
        paired_device.setClickable(true);
        mEnable = true;
        mDeviceAvailableList.clear();
        IntentFilter filter = new IntentFilter();
        //filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        //filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        // filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        // filter.addAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        registerReceiver(mReceiver, filter);

        btAdapter = BluetoothAdapter
                .getDefaultAdapter();

        if (btAdapter != null && btAdapter.isEnabled()) {
//yes
            btStatusTextView.setText(getString(R.string.status_bluetooth_ok));
        } else {
            //no
            showDialog(BLUETOOTH_DISABLED);
            btStatusTextView.setText(getString(R.string.status_bluetooth_disabled));
        }
        if (visibleStatus.booleanValue() && data_scroll.getVisibility()==View.GONE) {
            mbtActivity.runOnUiThread(new Runnable() {
                public void run() {
                    visibleStatus = false;
                    progressDialog = new BeautifulProgressDialog(mbtActivity, BeautifulProgressDialog.withLottie, null);
                    progressDialog.setLottieLocation(Constants.lottiePath);
                    progressDialog.setLottieLoop(true);
                    progressDialog.setLayoutColor(Color.TRANSPARENT);
                    progressDialog.setLayoutElevation(0);
                    progressDialog.setCancelable(true);
                    progressDialog.show();
                }
            });
        }


    }


    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        switch (id) {
            case NO_BLUETOOTH_ID:
                build.setMessage(getString(R.string.text_no_bluetooth_id));
                return build.create();
            case BLUETOOTH_DISABLED:
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                return build.create();

        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                btStatusTextView.setText(getString(R.string.status_bluetooth_connected));
                progressDialog.dismiss();
            } else {
                Toast.makeText(this, R.string.text_bluetooth_disabled, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_ANDROID);
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
       // super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onPositiveButtonClicked(AlertDialog alertDialog) {

        if (alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog.cancel();
        }


    }
    @Override
    public void onNegativeButtonClicked() {

            getLiveData();
            retry_remt_status = false;
            progressDialog.show();



    }

    private static void ConnectBlu(){
        Log.d("MBTActivity", "Testing");

        if (Constants.firstTimeUserStatus.booleanValue()) {
            Constants.firstTimeUserStatus = false;
        }
        /*try {*/



            visibleStatus = false;
            if (Preference_Details.getWifiGprs()==2){
                showDialog_Connect();
            }else if (Preference_Details.getWifiGprs()==0){
                readBlueToothData(Preference_Details.getWifiGprs(),Preference_Details.getWifiSSID(),Preference_Details.getWifiPwd(),1);
            }
            else if (Preference_Details.getWifiSSID().isEmpty()||Preference_Details.getWifiPwd().isEmpty()){
                showDialog_Connect();
            }else {
                readBlueToothData(Preference_Details.getWifiGprs(),Preference_Details.getWifiSSID(),Preference_Details.getWifiPwd(),1);
            }

            //Util_functions.WifiAlertDialog(data_scroll.getContext(),mbtActivity);
        /*} catch (IOException e) {
            visibleStatus = false;
            Log.e(TAG, "Could not connect to device", e);
            if (e.getMessage().contains("socket closed")) {
                Util_functions.AlertDialog(data_scroll.getContext(), mbtActivity.getString(R.string.Connect_fail), mbtActivity.getString(R.string.retry_again), mbtActivity.getString(R.string.remote_access), mbtActivity);
                progressDialog.dismiss();
            }
            // Toast.makeText(MBTActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            close(mBufferedReader);
            close(aReader);
            close(aStream);
            close(mSocket);
            visibleStatus = false;
        }*/

    }
    public static void showDialog_Connect() {
        CharSequence[] items = {"GPRS","WIFI"};
        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(data_scroll.getContext());
        builder.setSingleChoiceItems(items, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==0) {
                            dialog.dismiss();
                            Constants.regPin="disconnect";
                            if (paired_device.getText().toString().equals("Connected")){
                                bt.send(Constants.Bledata + "," + "0" + "," + "" + "," + "" + "," + "0", true);
                                /*readBlueToothData(0,"","",0);*/
                                mPrefernce_Manager.setString(Preference_Keys.LOginKeys.wifi_ssid,"");
                                mPrefernce_Manager.setString(Preference_Keys.LOginKeys.wifi_pwd,"");
                                mPrefernce_Manager.setInt(Preference_Keys.LOginKeys.wifi_gprs,0);
                                progressDialog.setMessage("");
                                progressDialog.show();
                            }
                            /*Intent MBTActivity = new Intent(mbtActivity.getApplicationContext(), com.montbleu.OBD.activity.MBTActivity.class);
                            startActivity(MBTActivity);*/
                        } else {
                            dialog.dismiss();
                            Util_functions.WifiAlertDialog(data_scroll.getContext(),mbtActivity);
                            /*Intent myIntent = new Intent(MBTActivity.this, MainActivity.class);
                            startActivity(myIntent);*/
                        }
                        //Util_functions.Show_Toast(MorepageActvity.this,items[which]+" Selected",true);

                    }

                });
        alert = builder.create();
        alert.setTitle("OBD DEVICE");
        alert.setCancelable(false);
        alert.show();
    }
    private static void readBlueToothData(int status, String name, String pass, Integer config) {
        try {
            if (Constants.regPin.equals("disconnect")) {
                if (config==0) {
                    if (status == 1) {
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.wifi_ssid, name);
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.wifi_pwd, pass);
                        bt.send(Constants.Bledata + "," + "1" + "," + Preference_Details.getWifiSSID() + "," + Preference_Details.getWifiPwd() + "," + config, true);
                        //write("port00000000000000000002" + "," + Constants.companyID);
                        /*mPrefernce_Manager.setString(Preference_Keys.LOginKeys.wifi_ssid, name);
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.wifi_pwd, pass);
                        write(Constants.Bledata + "," + "1" + "," + Preference_Details.getWifiSSID() + "," + Preference_Details.getWifiPwd() + "," + config);*/
                    } else if (status == 0) {
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.wifi_ssid, name);
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.wifi_pwd, pass);
                        bt.send(Constants.Bledata + "," + "0" + "," + "" + "," + "" + "," + config + "," + config, true);
                       /* mPrefernce_Manager.setString(Preference_Keys.LOginKeys.wifi_ssid, "0");
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.wifi_pwd, "0");
                        write(Constants.Bledata + "," + "0" + "," + "" + "," + "" + "," + config);*/

                    }
                }
            }




            aStream = mSocket.getInputStream();
            try {
                // progressDialog.show();
                aStream.read();

            } catch (IOException e) {
                e.printStackTrace();
                mbtActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                    }
                });
            }
            aReader = new InputStreamReader(aStream);
            mBufferedReader = new BufferedReader(aReader);
            ArrayList<String> listArray = new ArrayList<>();
            listArray.clear();
            String line= mBufferedReader.readLine();
            int i=0;
            while (line != null && !line.equals("")){
                // length = length + line.length();
                if(i<2){
                    i++;
                    line= mBufferedReader.readLine();
                    listArray.add(line);
                    //  Toast.makeText(getApplicationContext(), "list array "+listArray, Toast.LENGTH_SHORT).show();
                } else{
                    break;
                }
            }
            if (!listArray.isEmpty()) {
                for (int j=0;j<listArray.size();j++){
                    if (listArray.get(j).contains("action")) {
                        Gson gson = new Gson();
                        try {
                                if (status==0) {
                                    mbtActivity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            //ProgressDialog.show();
                                        }
                                    });
                                } else {
                                    ProgressDialog.setLottieProgressDialog(mbtActivity, Constants.lottiePath);
                                   // ProgressDialog.show();
                                }

                            JSONObject obj = new JSONObject(listArray.get(j));
                            BlueDataResponse data = gson.fromJson(String.valueOf(obj), BlueDataResponse.class);
                            implementList(data.getData());

                        } catch (JSONException e) {
                            listArray.clear();
                            e.printStackTrace();
                        }

                    }
                }
            }else {

            }

        } catch (IOException e) {
            e.printStackTrace();
            mbtActivity.runOnUiThread(new Runnable() {
                public void run() {
                    progressDialog.dismiss();
                }
            });
        }
    }

    private static void implementList(BlueDataResponse.Data data) {
        // String Data ="{\"Data\":[{\"RPM\":\"pm01\",\"id\":\"RPM\",\"name\":\"RPM\"},{\"SPEED\":\"pm02\",\"id\":\"SPEED\",\"name\":\"Vehicle Speed\"},{\"ENGINE_OIL_TEMP\":\"pm03\",\"id\":\"ENGINE_OIL_TEMP\",\"name\":\"ENGINE OIL TEMP\"},{\"ENGINE_COOLANT_TEMP\":\"pm04\",\"id\":\"ENGINE_COOLANT_TEMP\",\"name\":\"ENGINE COOLANT TEMP\"},{\"AMBIENT_AIR_TEMP\":\"pm05\",\"id\":\"AMBIENT_AIR_TEMP\",\"name\":\"AMBIENT AIR TEMP\"},{\"AIR_INTAKE_TEMP\":\"pm06\",\"id\":\"AIR_INTAKE_TEMP\",\"name\":\"AIR INTAKE TEMP\"},{\"CONTROL_MODULE_VOLTAGE\":\"pm07\",\"id\":\"CONTROL_MODULE_VOLTAGE\",\"name\":\"Control Module Power Supply\"},{\"FUEL_RAIL_PRESSURE\":\"pm08\",\"id\":\"FUEL_RAIL_PRESSURE\",\"name\":\"FUEL RAIL PRESSURE\"},{\"ENGINE_LOAD\":\"pm09\",\"id\":\"ENGINE_LOAD\",\"name\":\"ENGINE LOAD\"},{\"FUEL_CONSUMPTION_RATE\":\"pm10\",\"id\":\"FUEL_CONSUMPTION_RATE\",\"name\":\"FUEL CONSUMPTION RATE\"},{\"THROTTLE\":\"pm11\",\"id\":\"THROTTLE\",\"name\":\"THROTTLE\"},{\"INTAKE_MANIFOLD_PRESSURE\":\"pm12\",\"id\":\"INTAKE_MANIFOLD_PRESSURE\",\"name\":\"INTAKE MANIFOLD PRESSURE\"},{\"TIMING_ADVANCE\":\"pm13\",\"id\":\"TIMING_ADVANCE\",\"name\":\"TIMING ADVANCE\"},{\"AIR_FUEL_RATIO\":\"pm14\",\"id\":\"AIR_FUEL_RATIO\",\"name\":\"Air/Fuel Ratio\"},{\"Run_Time_Since_Engine_Start\":\"pm15\",\"id\":\"Run_Time_Since_Engine_Start\",\"name\":\"Run Time Since Engine Start\"},{\"DISTANCE_TRAVELED_MIL_ON\":\"pm16\",\"id\":\"DISTANCE_TRAVELED_MIL_ON\",\"name\":\"DISTANCE TRAVELED MIL ON\"},{\"Short Term Fuel Trim Bank 1\":\"pm17\",\"id\":\"Short Term Fuel Trim Bank 1\",\"name\":\"Short Term Fuel Trim Bank 1\"},{\"Short Term Fuel Trim Bank 2\":\"pm18\",\"id\":\"Short Term Fuel Trim Bank 2\",\"name\":\"Short Term Fuel Trim Bank 2\"},{\"Long Term Fuel Trim Bank 1\":\"pm19\",\"id\":\"Long Term Fuel Trim Bank 1\",\"name\":\"Long Term Fuel Trim Bank 1\"},{\"Long Term Fuel Trim Bank_2\":\"pm20\",\"id\":\"Long Term Fuel Trim Bank_2\",\"name\":\"Long Term Fuel Trim Bank 2\"},{\"FUEL_LEVEL\":\"pm21\",\"id\":\"FUEL_LEVEL\",\"name\":\"FUEL LEVEL\"},{\"EQUIV_RATIO\":\"pm22\",\"id\":\"EQUIV_RATIO\",\"name\":\"Command Equivalence Ratio\"},{\"BAROMETRIC_PRESSURE\":\"pm23\",\"id\":\"BAROMETRIC_PRESSURE\",\"name\":\"BAROMETRIC PRESSURE\"},{\"FUEL_PRESSURE\":\"pm24\",\"id\":\"FUEL_PRESSURE\",\"name\":\"FUEL PRESSURE\"},{\"Test1\":\"pm25\",\"id\":\"Test1\",\"name\":\"Test1\"},{\"Test2\":\"pm26\",\"id\":\"Test2\",\"name\":\"Test2\"},{\"Test3\":\"pm27\",\"id\":\"Test3\",\"name\":\"Test3\"},{\"Test4\":\"pm28\",\"id\":\"Test4\",\"name\":\"Test4\"},{\"Test5\":\"pm29\",\"id\":\"Test5\",\"name\":\"Test5\"},{\"Test6\":\"pm30\",\"id\":\"Test6\",\"name\":\"Test6\"}]}";

        try {
            // BlueDataResponse.Data data = json.("data");
            for(int i=0;i<name.length;i++){
                String key = name_val[i];
                String id = name[i];
                String val = null;
                if(i==0){
                    val =  data.getPm01();

                } else if(i==1){
                    val = data.getPm02();

                } else if(i==2){
                    val =    data.getPm03();

                } else if(i==3){
                    val =  data.getPm04();

                } else if(i==4){
                    val =  data.getPm05();

                } else if(i==5){
                    val =   data.getPm06();

                } else if(i==6){
                    val =   data.getPm07();

                } else if(i==7){
                    val =    data.getPm08();

                } else if(i==8){
                    val =   data.getPm09();

                } else if(i==9){
                    val =    data.getPm10();

                } else if(i==10){
                    val =   data.getPm11();

                } else if(i==11){
                    val= data.getPm12();

                } else if(i==12){
                    val =   data.getPm13();

                } else if(i==13){
                    val = data.getPm14();

                } else if(i==14){
                    val =  data.getPm15();

                } else if(i==15){
                    val =  data.getPm16();

                } else if(i==16){
                    val =   data.getPm17();

                } else if(i==17){
                    val =    data.getPm18();

                } else if(i==18){
                    val =   data.getPm19();

                } else if(i==19){
                    val =   data.getPm20();

                }else if(i==20){
                    val =   data.getPm21();

                } else if(i==21){
                    val =  data.getPm22();

                } else if(i==22){
                    val =   data.getPm23();

                } else if(i==23){
                    val =   data.getPm24();

                } else if(i==24){
                    val =   data.getPm25();
                } else if(i==25){
                    val = data.getPm26();

                }  else if(i==26){
                    val =   data.getPm27();

                }  else if(i==27){
                    val =  data.getPm28();

                }  else if(i==28){
                    val = data.getPm29();

                }  else if(i==29){
                    val =  data.getPm30();

                }else if(i==30){
                    val =   data.getPm31();

                } else if(i==31){
                    val =  data.getPm32();

                } else if(i==32){
                    val =   data.getPm33();

                } else if(i==33){
                    val =   data.getPm34();

                } else if(i==34){
                    val =   data.getPm35();
                } else if(i==35){
                    val = data.getPm36();
                }  else if(i==36){
                    val =   data.getPm37();
                }  else if(i==37){
                    val =  data.getPm38();
                }  else if(i==38){
                    val = data.getPm39();
                }  else if(i==39){
                    val =  data.getPm40();
                }else if(i==40){
                    val =   data.getPm41();

                } else if(i==41){
                    val =  data.getPm42();

                } else if(i==42){
                    val =   data.getPm43();

                } else if(i==43){
                    val =   data.getPm44();

                } else if(i==44){
                    val =   data.getPm45();

                } else if(i==45){
                    val = data.getPm46();

                }  else if(i==46){
                    val =   data.getPm47();
                    OBD_Lat.setText(val);
                }  else if(i==47){
                    val =  data.getPm48();
                    OBD_Lng.setText(val);
                }  else if(i==48){
                    val = data.getPm49();
                    OBD_alt.setText(val);
                }  else if(i==49){
                    val =  data.getPm50();
                    OBD_sat.setText(val);

                }
                addTableRow(id, key,val);
            }

        } catch (Exception e){
            e.printStackTrace();
            mbtActivity.runOnUiThread(new Runnable() {
                public void run() {
                    progressDialog.dismiss();
                }
            });

        }

    }

    @Override
    public void onWifiOk(String name, String pass) {
        if (visibleStatus.booleanValue()) {
            mbtActivity.runOnUiThread(new Runnable() {
                public void run() {
                    visibleStatus = false;
                    progressDialog = new BeautifulProgressDialog(mbtActivity, BeautifulProgressDialog.withLottie, null);
                    progressDialog.setLottieLocation(Constants.lottiePath);
                    progressDialog.setLottieLoop(true);
                    progressDialog.setLayoutColor(Color.TRANSPARENT);
                    progressDialog.setLayoutElevation(0);
                    progressDialog.setCancelable(true);
                    progressDialog.show();
                }
            });
        }
        mPrefernce_Manager.setInt(Preference_Keys.LOginKeys.wifi_gprs,1);
        Constants.regPin="disconnect";
        if (Constants.regPin.equals("disconnect")) {
            if (Preference_Details.getWifiGprs() == 1) {
                mPrefernce_Manager.setString(Preference_Keys.LOginKeys.wifi_ssid, name);
                mPrefernce_Manager.setString(Preference_Keys.LOginKeys.wifi_pwd, pass);
                bt.send(Constants.Bledata + "," + "1" + "," + Preference_Details.getWifiSSID() + "," + Preference_Details.getWifiPwd() + "," + 0, true);
                //write("port00000000000000000002" + "," + Constants.companyID);
                        /*mPrefernce_Manager.setString(Preference_Keys.LOginKeys.wifi_ssid, name);
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.wifi_pwd, pass);
                        write(Constants.Bledata + "," + "1" + "," + Preference_Details.getWifiSSID() + "," + Preference_Details.getWifiPwd() + "," + config);*/
            } else if (Preference_Details.getWifiGprs() == 0) {
                mPrefernce_Manager.setString(Preference_Keys.LOginKeys.wifi_ssid, name);
                mPrefernce_Manager.setString(Preference_Keys.LOginKeys.wifi_pwd, pass);
                bt.send(Constants.Bledata + "," + "0" + "," + "" + "," + "" + "," + Preference_Details.getWifiGprs() + "," + 0, true);
                       /* mPrefernce_Manager.setString(Preference_Keys.LOginKeys.wifi_ssid, "0");
                        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.wifi_pwd, "0");
                        write(Constants.Bledata + "," + "0" + "," + "" + "," + "" + "," + config);*/

            }
        }

        //readBlueToothData(Preference_Details.getWifiGprs(),name,pass,0);
    }

    @Override
    public void onWifiCancel() {
        if (visibleStatus.booleanValue()) {
            mbtActivity.runOnUiThread(new Runnable() {
                public void run() {
                    visibleStatus = false;
                    progressDialog = new BeautifulProgressDialog(mbtActivity, BeautifulProgressDialog.withLottie, null);
                    progressDialog.setLottieLocation(Constants.lottiePath);
                    progressDialog.setLottieLoop(true);
                    progressDialog.setLayoutColor(Color.TRANSPARENT);
                    progressDialog.setLayoutElevation(0);
                    progressDialog.setCancelable(true);
                    progressDialog.show();
                }
            });
        }
      //  readBlueToothData(1,Preference_Details.getWifiSSID(),Preference_Details.getWifiPwd(),1);
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;

        @Override
        protected String doInBackground(String... params) {
            //publishProgress("Scanning...");
            //  show();
            try {
                int time = Integer.parseInt(params[0])*1000;
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
                progressDialog.dismiss();
                resp = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                resp = e.getMessage();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            mDeviceList.clear();
            unregisterReceiver(mReceiver);
            bt.setDeviceTarget(BluetoothState.DEVICE_OTHER);
            Intent newIntent = new Intent(MBTActivity.this, DeviceListStartDis.class);
            newIntent.putParcelableArrayListExtra("device.list", mDeviceAvailableList);

			/*
			if(bt.getServiceState() == BluetoothState.STATE_CONNECTED)
    			bt.disconnect();*/
            startActivityForResult(newIntent, BluetoothState.REQUEST_CONNECT_DEVICE);
           // startActivity(newIntent);
            mBluetoothAdapter.cancelDiscovery();
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }


        @Override
        protected void onProgressUpdate(String... text) {
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                if (state == BluetoothAdapter.STATE_ON) {
                    Util_functions.Show_Toast(getApplicationContext(),"Enabled",true);
                    //  showEnabled();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                progressDialog.show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                progressDialog.dismiss();
                Intent newIntent = new Intent(MBTActivity.this, DeviceListActivity.class);
                newIntent.putParcelableArrayListExtra("device.list", mDeviceList);
                startActivityForResult(newIntent, BluetoothState.REQUEST_CONNECT_DEVICE);
                startActivity(newIntent);
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //mDeviceList.clear();
                //mDeviceList.add(device);
                if (mDeviceList.size()>0) {
                    for(int i=0;i<mDeviceList.size();i++){
                        if(mDeviceList.get(i).getBluetoothClass().equals(device.getBluetoothClass())){
                            //mDeviceList.add(device);
                            break;
                        } else {
                            mDeviceList.add(device);
                            mDeviceAvailableList.add(device);
                        }
                    }
                } else {
                    mDeviceList.add(device);
                    mDeviceAvailableList.add(device);
                    if (mEnable) {
                        mEnable = false;
                        AsyncTaskRunner runner = new AsyncTaskRunner();
                        runner.execute("10");
                    }
                }
            }
              /* Intent newIntent = new Intent(MainActivity.this, DeviceListActivity.class);
               newIntent.putParcelableArrayListExtra("device.list", mDeviceList);
               startActivity(newIntent);*/
            // showToast("Found device " + device.getName());

        }
    };

    private void getLiveData() {
        restMethods = RestClient.buildHTTPClient();
        restMethods.userDeviceDataGetResp(Constants.referredBy, Preference_Details.getLoginSessID(),Constants.companyID,Constants.moduleID,Constants.divisionId,Preference_Details.getPortionID(),Constants.userID,Constants.type_live_Data,Constants.category, "DESC", 0, 100).enqueue(new Callback<List<UserDeviceDataModalResp>>() {
            @Override
            public void onResponse(Call<List<UserDeviceDataModalResp>> call, Response<List<UserDeviceDataModalResp>> response) {
                Log.e("status", "" + response.body());
                try {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            userDeviceDataModalResps.clear();
                            if (!response.body().isEmpty()) {
                                userDeviceDataModalResps.addAll(response.body());
                                implementRemoteList(userDeviceDataModalResps.get(userDeviceDataModalResps.size()-1));
                                progressDialog.dismiss();
                            }  else if(response.body().isEmpty()) {
                                progressDialog.dismiss();
                                Util_functions.ShowValidateAlertError(mbtActivity,getString(R.string.json_empty), "OK");
                            } else {
                                String Error = response.errorBody().string();
                                JSONObject obj = new JSONObject(Error);
                                String text = obj.getString("message");
                                Util_functions.ShowValidateAlertError(mbtActivity.getApplicationContext(), text, "OK");
                                //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.resp_empty),true);
                                progressDialog.dismiss();
                            }
                        } else {
                            progressDialog.dismiss();
                        }

                    } else if (response.code() == 412) {
                        try {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowValidateAlertError(mbtActivity.getApplicationContext(), object1.getString("message"), "OK");
                            }
                            progressDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.dismiss();

                        }

                    } else {
                        String Error = response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text = obj.getString("message");
                        Util_functions.ShowValidateAlertError(mbtActivity, text, "OK");
                        //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.txt_error),true);
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        progressDialog.dismiss();
                    }
                    catch (WindowManager.BadTokenException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<UserDeviceDataModalResp>> call, Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
            }
        });
    }

    private void implementRemoteList(UserDeviceDataModalResp userDeviceDataModalResp) {
        try{
            for(int i=0;i<name_val.length;i++){
                String key = name_val[i];
                String id = name[i];
                String val = "0";
                if(i==0){
                    val =  userDeviceDataModalResp.getUserDeviceDataLiveField().getPm01();

                } else if(i==1){
                    val = userDeviceDataModalResp.getUserDeviceDataLiveField().getPm02();

                } else if(i==2){
                    val =    userDeviceDataModalResp.getUserDeviceDataLiveField().getPm03();

                } else if(i==3){
                    val =  userDeviceDataModalResp.getUserDeviceDataLiveField().getPm04();

                } else if(i==4){
                    val =  userDeviceDataModalResp.getUserDeviceDataLiveField().getPm05();

                } else if(i==5){
                    val =   userDeviceDataModalResp.getUserDeviceDataLiveField().getPm06();

                } else if(i==6){
                    val =   userDeviceDataModalResp.getUserDeviceDataLiveField().getPm07();

                } else if(i==7){
                    val =    userDeviceDataModalResp.getUserDeviceDataLiveField().getPm08();

                } else if(i==8){
                    val =   userDeviceDataModalResp.getUserDeviceDataLiveField().getPm09();

                } else if(i==9){
                    val =    userDeviceDataModalResp.getUserDeviceDataLiveField().getPm10();

                } else if(i==10){
                    val =   userDeviceDataModalResp.getUserDeviceDataLiveField().getPm11();

                } else if(i==11){
                    val =   userDeviceDataModalResp.getUserDeviceDataLiveField().getPm12();

                } else if(i==12){
                    val =   userDeviceDataModalResp.getUserDeviceDataLiveField().getPm13();

                } else if(i==13){
                    val = userDeviceDataModalResp.getUserDeviceDataLiveField().getPm14();

                } else if(i==14){
                    val =  userDeviceDataModalResp.getUserDeviceDataLiveField().getPm15();

                } else if(i==15){
                    val =  userDeviceDataModalResp.getUserDeviceDataLiveField().getPm16();

                } else if(i==16){
                    val =   userDeviceDataModalResp.getUserDeviceDataLiveField().getPm17();

                } else if(i==17){
                    val =    userDeviceDataModalResp.getUserDeviceDataLiveField().getPm18();

                } else if(i==18){
                    val =   userDeviceDataModalResp.getUserDeviceDataLiveField().getPm19();

                } else if(i==19){
                    val =   userDeviceDataModalResp.getUserDeviceDataLiveField().getPm20();

                }else if(i==20){
                    val =   userDeviceDataModalResp.getUserDeviceDataLiveField().getPm21();

                } else if(i==21){
                    val =  userDeviceDataModalResp.getUserDeviceDataLiveField().getPm22();

                } else if(i==22){
                    val =   userDeviceDataModalResp.getUserDeviceDataLiveField().getPm23();

                } else if(i==23){
                    val =   userDeviceDataModalResp.getUserDeviceDataLiveField().getPm24();

                } else if(i==24){
                    val =   userDeviceDataModalResp.getUserDeviceDataLiveField().getPm25();

                } else if(i==25){
                    val = userDeviceDataModalResp.getUserDeviceDataLiveField().getPm26();

                }  else if(i==26){
                    val =   userDeviceDataModalResp.getUserDeviceDataLiveField().getPm27();

                }  else if(i==27){
                    val =  userDeviceDataModalResp.getUserDeviceDataLiveField().getPm28();

                }  else if(i==28){
                    val = userDeviceDataModalResp.getUserDeviceDataLiveField().getPm29();

                }  else if(i==29){
                    val =  userDeviceDataModalResp.getUserDeviceDataLiveField().getPm30();

                }
                addRemoteTableRow(id, key,val);

            }

        } catch (Exception e){
            e.printStackTrace();
            progressDialog.dismiss();

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(bt.getServiceState() == BluetoothState.STATE_CONNECTED){
            bt.disconnect();

        }
        Constants.alertcount=true;
        obdAdapter=null;
        progressDialog.dismiss();
        //obdModelListClone.clear();
        finish();

    }

    @Override
    protected void onStop() {
        super.onStop();
        //   thread.stop();


    }



/*  if(j==24){
        OBD_Lat.setText(jsonObject1.get("value").toString());
    } else if(j==25){
        OBD_Lng.setText(jsonObject1.get("value").toString());
    }  else if(j==26){
        OBD_alt.setText(jsonObject1.get("value").toString());
    }  else if(j==27){
        OBD_sat.setText(jsonObject1.get("value").toString());
    }*/



    private static class ThreadTask extends TimerTask {

        @Override
        public void run() {
            if (mSocket.isConnected())
            {
                readBlueToothData(Preference_Details.getWifiGprs(), Preference_Details.getWifiSSID(), Preference_Details.getWifiPwd(), 1);
            }else {
               /* try {
                    //mSocket.connect();
                }
                catch (IOException e) {
                    visibleStatus = false;
                    Log.e(TAG, "Could not connect to device", e);
                    if (e.getMessage().contains("socket closed")) {
                        Util_functions.AlertDialog(data_scroll.getContext(), mbtActivity.getString(R.string.Connect_fail), mbtActivity.getString(R.string.retry_again), mbtActivity.getString(R.string.remote_access), mbtActivity);
                        progressDialog.dismiss();
                    }
                    // Toast.makeText(MBTActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    close(mBufferedReader);
                    close(aReader);
                    close(aStream);
                    close(mSocket);
                    visibleStatus = false;
                }*/

            }


        }
    }

}
