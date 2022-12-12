package com.montbleu.OBD.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.pires.obd.commands.ObdCommand;
import com.github.pires.obd.commands.SpeedCommand;
import com.github.pires.obd.commands.engine.RPMCommand;
import com.github.pires.obd.commands.engine.RuntimeCommand;
import com.github.pires.obd.enums.AvailableCommandNames;
import com.montbleu.OBD.adapter.OBDAdapter;
import com.montbleu.OBD.adapter.Uncheck_Adapter;
import com.montbleu.OBD.changeStatus;
import com.montbleu.OBD.config.ObdConfig;
import com.montbleu.OBD.io.AbstractGatewayService;
import com.montbleu.OBD.io.LogCSVWriter;
import com.montbleu.OBD.io.ObdCommandJob;
import com.montbleu.OBD.io.ObdGatewayService;
import com.montbleu.OBD.io.ObdProgressListener;
import com.montbleu.OBD.model.OBDModel;
import com.montbleu.OBD.net.ObdReading;
import com.montbleu.OBD.net.ObdService;
import com.montbleu.OBD.trips.TripLog;
import com.montbleu.OBD.trips.TripRecord;
import com.montbleu.Utils.Constants;
import com.montbleu.Utils.ProgressDialog;
import com.montbleu.Utils.Util_functions;
import com.montbleu.roadsafety.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.montbleu.OBD.activity.ConfigActivity.getGpsDistanceUpdatePeriod;
import static com.montbleu.OBD.activity.ConfigActivity.getGpsUpdatePeriod;


// Some code taken from https://github.com/barbeau/gpstest

public class MainActivity extends Activity implements ObdProgressListener, LocationListener, GpsStatus.Listener, View.OnClickListener, changeStatus {

    private static final String TAG = MainActivity.class.getName();
    private static final int NO_BLUETOOTH_ID = 0;
    private static final int BLUETOOTH_DISABLED = 1;
    private static final int START_LIVE_DATA = 2;
    private static final int STOP_LIVE_DATA = 3;
    private static final int SETTINGS = 4;
    private static final int GET_DTC = 5;
    private static final int TABLE_ROW_MARGIN = 7;
    private static final int NO_ORIENTATION_SENSOR = 8;
    private static final int NO_GPS_SUPPORT = 9;
    private static final int TRIPS_LIST = 10;
    private static final int SAVE_TRIP_NOT_AVAILABLE = 11;
    private static final int REQUEST_ENABLE_BT = 1234;
    private static boolean bluetoothDefaultIsEnable = false;
    private int MY_PERMISSION_LOCATION =999;
    TextView textNoData;
    NestedScrollView data_scroll;
    List<OBDModel> obdModelList = new ArrayList<>();
    List<OBDModel> obdModelListClone = new ArrayList<>();
    List<OBDModel> obdModelListClone2 = new ArrayList<>();
    List<OBDModel> obdModelListFilter = new ArrayList<>();

    Parcelable recyclerViewState;
    Uncheck_Adapter uncheckAdapter;
  /*  static {
        RoboGuice.setUseAnnotationDatabases(false);
    }*/

    public Map<String, String> commandResult = new HashMap<String, String>();
    boolean mGpsIsStarted = false;
    private LocationManager mLocService;
    private LocationProvider mLocProvider;
    private LogCSVWriter myCSVWriter;
    private Location mLastLocation;
    /// the trip log
    private TripLog triplog;
    private TripRecord currentTrip;
    private TextView compass;
    OBDAdapter obdAdapter;
    ImageView imgAdd;
   /* private static final String[] NAMES_COLUMNS = {"EQUIV_RATIO","DISTANCE_TRAVELED_MIL_ON",
            "TIMING_ADVANCE","ENGINE_LOAD","THROTTLE_POS","Long Term Fuel Trim Bank 1","Long Term Fuel Trim Bank 2",
            "Short Term Fuel Trim Bank 1","Short Term Fuel Trim Bank 2","AIR_FUEL_RATIO","ENGINE_OIL_TEMP",
            "BAROMETRIC_PRESSURE","FUEL_CONSUMPTION_RATE","FUEL_LEVEL","FUEL_PRESSURE","FUEL_RAIL_PRESSURE",
            "INTAKE_MANIFOLD_PRESSURE","AIR_INTAKE_TEMP","AMBIENT_AIR_TEMP","ENGINE_COOLANT_TEMP",
            "SPEED","AMBIENT_AIR_TEMP","ENGINE_RUNTIME","CONTROL_MODULE_VOLTAGE"};*/

    String name[]={"SPEED","ENGINE_OIL_TEMP","ENGINE_COOLANT_TEMP","AMBIENT_AIR_TEMP",
            "AIR_INTAKE_TEMP","CONTROL_MODULE_VOLTAGE", "FUEL_LEVEL","ENGINE_LOAD" ,
            "FUEL_CONSUMPTION_RATE","FUEL_RAIL_PRESSURE", "Short Term Fuel Trim Bank 1","Short Term Fuel Trim Bank 2",
            "Long Term Fuel Trim Bank 1","Long Term Fuel Trim Bank 2","INTAKE_MANIFOLD_PRESSURE","TIMING_ADVANCE",
            "AIR_FUEL_RATIO","ENGINE_RUNTIME","EQUIV_RATIO","DISTANCE_TRAVELED_MIL_ON",
            "BAROMETRIC_PRESSURE"};

    int image[]={R.mipmap.speed,R.mipmap.oil_temp,R.mipmap.coolant_temp,R.mipmap.ambient_temp,
            R.mipmap.intake_temp, R.mipmap.batt_volt,R.mipmap.fuel_level,R.mipmap.engine_load,
            R.mipmap.fuel_rate,R.mipmap.fuel_rail_pressure, R.mipmap.fuel_short_term,R.mipmap.fuel_short_term,
            R.mipmap.fuel_long_term,R.mipmap.fuel_long_term, R.mipmap.car_manifold,R.mipmap.timing_advance,
            R.mipmap.air_fuel_ratio,R.mipmap.engine_start, R.mipmap.equivalence_ratio,R.mipmap.distance_travel,
            R.mipmap.barometric_pressure};



    private final SensorEventListener orientListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            String dir = "";
            if (x >= 337.5 || x < 22.5) {
                dir = "N";
            } else if (x >= 22.5 && x < 67.5) {
                dir = "NE";
            } else if (x >= 67.5 && x < 112.5) {
                dir = "E";
            } else if (x >= 112.5 && x < 157.5) {
                dir = "SE";
            } else if (x >= 157.5 && x < 202.5) {
                dir = "S";
            } else if (x >= 202.5 && x < 247.5) {
                dir = "SW";
            } else if (x >= 247.5 && x < 292.5) {
                dir = "W";
            } else if (x >= 292.5 && x < 337.5) {
                dir = "NW";
            }
            updateTextView(compass, dir);
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // do nothing
        }
    };
    private TextView start_Live,Stop_Live,Setting,dtc,tripList;
    private TextView btStatusTextView;
    private TextView obdStatusTextView;
    private TextView gpsStatusTextView;
    private LinearLayout vv;
    RecyclerView mrecyclerOBDList;
    private TableLayout tl;
    private SensorManager sensorManager;
    private PowerManager powerManager;
    @Inject
    private SharedPreferences prefs;
    private boolean isServiceBound;
    private AbstractGatewayService service;
    private final Runnable mQueueCommands = new Runnable() {
        public void run() {
            if (service != null && service.isRunning() && service.queueEmpty()) {
                queueCommands();

                double lat = 0;
                double lon = 0;
                double alt = 0;
                final int posLen = 7;
                if (mGpsIsStarted && mLastLocation != null) {
                    lat = mLastLocation.getLatitude();
                    lon = mLastLocation.getLongitude();
                    alt = mLastLocation.getAltitude();
                    StringBuilder sb = new StringBuilder();
                    sb.append("Lat: ");
                    sb.append(String.valueOf(mLastLocation.getLatitude()).substring(0, posLen));
                    sb.append(" Lon: ");
                    sb.append(String.valueOf(mLastLocation.getLongitude()).substring(0, posLen));
                    sb.append(" Alt: ");
                    sb.append(String.valueOf(mLastLocation.getAltitude()));
                    gpsStatusTextView.setText(sb.toString());
                }
                if (prefs.getBoolean(ConfigActivity.UPLOAD_DATA_KEY, false)) {
                    // Upload the current reading by http
                    final String vin = prefs.getString(ConfigActivity.VEHICLE_ID_KEY, "UNDEFINED_VIN");
                    Map<String, String> temp = new HashMap<String, String>();
                    temp.putAll(commandResult);
                    ObdReading reading = new ObdReading(lat, lon, alt, System.currentTimeMillis(), vin, temp);
                    new UploadAsyncTask().execute(reading);

                } else if (prefs.getBoolean(ConfigActivity.ENABLE_FULL_LOGGING_KEY, false)) {
                    // Write the current reading to CSV

                    final String vin = prefs.getString(ConfigActivity.VEHICLE_ID_KEY, "UNDEFINED_VIN");

                    if(lat!=0 && lon!=0) {
                        try {
                         //   tl.setVisibility(View.VISIBLE);
                            Map<String, String> temp = new HashMap<String, String>();
                            temp.putAll(commandResult);
                            ObdReading reading = new ObdReading(lat, lon, alt, System.currentTimeMillis(), vin, temp);
                            if (reading != null) myCSVWriter.writeLineCSV(reading);
                           ProgressDialog.dismissProgress();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                      //  tl.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"No Data's Available", Toast.LENGTH_LONG).show();
                        ProgressDialog.dismissProgress();
                        return;

                    }
                   }
                commandResult.clear();
            }
            // run again in period defined in preferences
            new Handler().postDelayed(mQueueCommands, ConfigActivity.getObdUpdatePeriod(prefs));
        }
    };
    private Sensor orientSensor = null;
    private PowerManager.WakeLock wakeLock = null;
    private boolean preRequisites = true;
    private ServiceConnection serviceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            Log.d(TAG, className.toString() + " service is bound");
            isServiceBound = true;
            service = ((AbstractGatewayService.AbstractGatewayServiceBinder) binder).getService();
            service.setContext(MainActivity.this);
            Log.d(TAG, "Starting live data");
            try {
                service.startService();
                if (preRequisites)
                    btStatusTextView.setText(getString(R.string.status_bluetooth_connected));
            } catch (IOException ioe) {
                Log.e(TAG, "Failure Starting live data");
                btStatusTextView.setText(getString(R.string.status_bluetooth_error_connecting));
                doUnbindService();
            }
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        // This method is *only* called when the connection to the service is lost unexpectedly
        // and *not* when the client unbinds (http://developer.android.com/guide/components/bound-services.html)
        // So the isServiceBound attribute should also be set to false when we unbind from the service.
        @Override
        public void onServiceDisconnected(ComponentName className) {
            Log.d(TAG, className.toString() + " service is unbound");
            isServiceBound = false;
        }
    };

    public static String LookUpCommand(String txt) {
        for (AvailableCommandNames item : AvailableCommandNames.values()) {
            if (item.getValue().equals(txt)) return item.name();
        }
        return txt;
    }

    public void updateTextView(final TextView view, final String txt) {
        new Handler().post(new Runnable() {
            public void run() {
           //     view.setText(txt);
            }
        });
    }

    public void stateUpdate(final ObdCommandJob job) {
        final String cmdName = job.getCommand().getName();
        String cmdResult = "";
        final String cmdID = LookUpCommand(cmdName);

        if (job.getState().equals(ObdCommandJob.ObdCommandJobState.EXECUTION_ERROR)) {
            cmdResult = job.getCommand().getResult();
            if (cmdResult != null && isServiceBound) {
             //   obdStatusTextView.setText(cmdResult.toLowerCase());
               if(cmdResult.contains("...UNABLETOCONNECT")){
                   data_scroll.setVisibility(View.GONE);
                   textNoData.setVisibility(View.VISIBLE);
                   Util_functions.Show_Toast(getApplicationContext(),"OBD Disconnected",true);
                   ProgressDialog.dismissProgress();
               } else {
                   if (isServiceBound) {
                       data_scroll.setVisibility(View.VISIBLE);
                       textNoData.setVisibility(View.GONE);
                       imgAdd.setVisibility(View.VISIBLE);
                       ProgressDialog.dismissProgress();
                   }

               }

            }
        } else if (job.getState().equals(ObdCommandJob.ObdCommandJobState.BROKEN_PIPE)) {
            if (isServiceBound)
                stopLiveData();
        } else if (job.getState().equals(ObdCommandJob.ObdCommandJobState.NOT_SUPPORTED)) {
            cmdResult = getString(R.string.status_obd_no_support);
        } else {
            cmdResult = job.getCommand().getFormattedResult();
            if (isServiceBound)
                obdStatusTextView.setText(getString(R.string.status_obd_data));
        }

       addTableRow(cmdID,cmdName,cmdResult);

      /*  if (vv.findViewWithTag(cmdID) != null) {

            TextView existingTV = (TextView) vv.findViewWithTag(cmdID);
            existingTV.setText(cmdResult);

        } else {
            addTableRow(cmdID, cmdName, cmdResult);
        }*/ commandResult.put(cmdID, cmdResult);
        updateTripStatistic(job, cmdID);
    }

    private boolean gpsInit() {
        mLocService = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (mLocService != null) {
            mLocProvider = mLocService.getProvider(LocationManager.GPS_PROVIDER);
            if (mLocProvider != null) {
                mLocService.addGpsStatusListener(this);
                if (mLocService.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    gpsStatusTextView.setText(getString(R.string.status_gps_ready));
                    return true;
                }
            }
        }
        gpsStatusTextView.setText(getString(R.string.status_gps_no_support));
        showDialog(NO_GPS_SUPPORT);
        Log.e(TAG, "Unable to get GPS PROVIDER");
        // todo disable gps controls into Preferences
        return false;
    }

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }


    private void updateTripStatistic(final ObdCommandJob job, final String cmdID) {

        if (currentTrip != null) {
            if (cmdID.equals(AvailableCommandNames.SPEED.toString())) {
                SpeedCommand command = (SpeedCommand) job.getCommand();
                currentTrip.setSpeedMax(command.getMetricSpeed());
            } else if (cmdID.equals(AvailableCommandNames.ENGINE_RPM.toString())) {
                RPMCommand command = (RPMCommand) job.getCommand();
                currentTrip.setEngineRpmMax(command.getRPM());
            } else if (cmdID.endsWith(AvailableCommandNames.ENGINE_RUNTIME.toString())) {
                RuntimeCommand command = (RuntimeCommand) job.getCommand();
                currentTrip.setEngineRuntime(command.getFormattedResult());
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        textNoData = findViewById(R.id.textNoData);
        data_scroll = findViewById(R.id.data_scroll);
        btStatusTextView =findViewById(R.id.BT_STATUS);
        obdStatusTextView =findViewById(R.id.OBD_STATUS);
        gpsStatusTextView =findViewById(R.id.GPS_POS);
        start_Live = findViewById(R.id.start_Live);
        Stop_Live = findViewById(R.id.Stop_Live);
        Setting = findViewById(R.id.Setting);
        dtc = findViewById(R.id.dtc);
        tripList = findViewById(R.id.tripList);
        start_Live.setOnClickListener(this);
        Setting.setOnClickListener(this);
        dtc.setOnClickListener(this);
        tripList.setOnClickListener(this);
        ProgressDialog.setLottieProgressDialog(MainActivity.this, Constants.lottiePath);
      //  vv =findViewById(R.id.vehicle_view);
        mrecyclerOBDList = findViewById(R.id.recyclerList);
        imgAdd = findViewById(R.id.imgAdd);
        imgAdd.setOnClickListener(this);
        getOBDetails();
        Stop_Live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopLiveData();
            }
        });
        final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter != null)
            bluetoothDefaultIsEnable = btAdapter.isEnabled();
        // get Orientation sensor
        sensorManager= (SensorManager)getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
        if (sensors.size() > 0)
            orientSensor = sensors.get(0);
        else
            showDialog(NO_ORIENTATION_SENSOR);
        // create a log instance for use by this application
        triplog = TripLog.getInstance(this.getApplicationContext());
        obdStatusTextView.setText(getString(R.string.status_obd_disconnected));
    }


    private void getOBDetails() {

        ProgressDialog.showProgress();
        if (obdAdapter == null) { // it works second time and later
            // it works first time
            JSONObject jsonObject = getJsonValidate();
            try {
                obdModelList.clear();
                JSONArray jsonArray = jsonObject.getJSONArray("OBD_Data");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                    for(int j=0;j<name.length;j++){
                        if (name[j].equals(jsonObject1.get("id"))) {
                            obdModelList.add(new OBDModel(jsonObject1.get("id").toString(),jsonObject1.get("value").toString(),"",image[j],true));
                        }
                    }
                }
                obdModelListClone.clear();
                obdModelListClone.addAll(obdModelList);
                if (obdAdapter==null) {
                    obdAdapter=new OBDAdapter(this,obdModelListClone);
                    GridLayoutManager layoutManager=new GridLayoutManager(this,2);
                    //at last set adapter to recycler view.
                    mrecyclerOBDList.setHasFixedSize(true);
                    mrecyclerOBDList.setLayoutManager(layoutManager);
                    mrecyclerOBDList.setAdapter(obdAdapter);
                    mrecyclerOBDList.setNestedScrollingEnabled(true);
                    ProgressDialog.dismissProgress();
                } else {
                    ProgressDialog.dismissProgress();
                    obdModelListClone.clear();
                    obdModelListClone.addAll(obdModelListFilter);
                    obdAdapter.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
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
        }
        return jsonObject;
    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "Entered onStart...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mLocService != null) {
            mLocService.removeGpsStatusListener(this);
            mLocService.removeUpdates(this);
        }

        releaseWakeLockIfHeld();
        if (isServiceBound) {
            doUnbindService();
        }

        endTrip();

        final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter != null && btAdapter.isEnabled() && !bluetoothDefaultIsEnable)
            btAdapter.disable();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Pausing..");
        releaseWakeLockIfHeld();
    }

    /**
     * If lock is held, release. Lock will be held when the service is running.
     */
    private void releaseWakeLockIfHeld() {
        if (wakeLock.isHeld())
            wakeLock.release();
    }

    @SuppressLint("InvalidWakeLockTag")
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Resuming..");
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        sensorManager= (SensorManager)getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(orientListener, orientSensor,
                SensorManager.SENSOR_DELAY_UI);
        powerManager = (PowerManager)getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
                "ObdReader");

        // get Bluetooth device
        final BluetoothAdapter btAdapter = BluetoothAdapter
                .getDefaultAdapter();

        preRequisites = btAdapter != null && btAdapter.isEnabled();
        if (!preRequisites && prefs.getBoolean(ConfigActivity.ENABLE_BT_KEY, false)) {
            preRequisites = btAdapter != null && btAdapter.enable();
        }

        gpsPremissionCheck();


        if (!preRequisites) {
            ProgressDialog.dismissProgress();
            showDialog(BLUETOOTH_DISABLED);
            btStatusTextView.setText(getString(R.string.status_bluetooth_disabled));
        } else {
            ProgressDialog.dismissProgress();
            btStatusTextView.setText(getString(R.string.status_bluetooth_ok));
        }
    }

    private void updateConfig() {
        startActivity(new Intent(this, ConfigActivity.class));
        ProgressDialog.dismissProgress();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, START_LIVE_DATA, 0, getString(R.string.menu_start_live_data));
        menu.add(0, STOP_LIVE_DATA, 0, getString(R.string.menu_stop_live_data));
        menu.add(0, GET_DTC, 0, getString(R.string.menu_get_dtc));
        menu.add(0, TRIPS_LIST, 0, getString(R.string.menu_trip_list));
        menu.add(0, SETTINGS, 0, getString(R.string.menu_settings));
        return true;
    }

    private void gpsPremissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_LOCATION);
        } else {
            gpsInit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSION_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            gpsInit();
        }
    }


    private void getTroubleCodes() {
        startActivity(new Intent(this, TroubleCodesActivity.class));
        ProgressDialog.dismissProgress();
    }

    private void startLiveData() {
        ProgressDialog.showProgress();
        Log.d(TAG, "Starting live data..");
      //  tl.removeAllViews(); //start fresh
        doBindService();

        currentTrip = triplog.startTrip();
        if (currentTrip == null)
            showDialog(SAVE_TRIP_NOT_AVAILABLE);

        // start command execution
        new Handler().post(mQueueCommands);

        if (prefs.getBoolean(ConfigActivity.ENABLE_GPS_KEY, false))
            gpsStart();
        else
            gpsStatusTextView.setText(getString(R.string.status_gps_not_used));

        // screen won't turn off until wakeLock.release()
        wakeLock.acquire();

        if (prefs.getBoolean(ConfigActivity.ENABLE_FULL_LOGGING_KEY, false)) {

            // Create the CSV Logger
            long mils = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("_dd_MM_yyyy_HH_mm_ss");

            try {
                myCSVWriter = new LogCSVWriter("Log" + sdf.format(new Date(mils)).toString() + ".csv",
                        prefs.getString(ConfigActivity.DIRECTORY_FULL_LOGGING_KEY,
                                getString(R.string.default_dirname_full_logging))
                );
            } catch (FileNotFoundException | RuntimeException e) {
                Log.e(TAG, "Can't enable logging to file.", e);
            }
        }
    }


    private void stopLiveData() {
       ProgressDialog.showProgress();
        Log.d(TAG, "Stopping live data..");
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        gpsStop();
        doUnbindService();
        endTrip();
        releaseWakeLockIfHeld();
       // tl.removeAllViews();
        if (obdAdapter != null) { // it works second time and later
            obdModelList.clear();
            obdAdapter.notifyDataSetChanged();
        }

        data_scroll.setVisibility(View.GONE);
        textNoData.setVisibility(View.VISIBLE);
        imgAdd.setVisibility(View.GONE);

        Toast.makeText(getApplicationContext(),"Live Stopped Successfully",Toast.LENGTH_SHORT).show();

        final String devemail = prefs.getString(ConfigActivity.DEV_EMAIL_KEY, null);
        if (devemail != null && !devemail.isEmpty()) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            ObdGatewayService.saveLogcatToFile(getApplicationContext(), devemail);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Where there issues?\nThen please send us the logs.\nSend Logs?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }

        if (myCSVWriter != null) {
            myCSVWriter.closeLogCSVWriter();
        }

        ProgressDialog.dismissProgress();

    }

    protected void endTrip() {
        if (currentTrip != null) {
            currentTrip.setEndDate(new Date());
            triplog.updateRecord(currentTrip);
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
            case NO_ORIENTATION_SENSOR:
                build.setMessage(getString(R.string.text_no_orientation_sensor));
                return build.create();
            case NO_GPS_SUPPORT:
                build.setMessage(getString(R.string.text_no_gps_support));
                return build.create();
            case SAVE_TRIP_NOT_AVAILABLE:
                build.setMessage(getString(R.string.text_save_trip_not_available));
                return build.create();
        }
        return null;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem startItem = menu.findItem(START_LIVE_DATA);
        MenuItem stopItem = menu.findItem(STOP_LIVE_DATA);
        MenuItem settingsItem = menu.findItem(SETTINGS);
        MenuItem getDTCItem = menu.findItem(GET_DTC);

        if (service != null && service.isRunning()) {
            getDTCItem.setEnabled(false);
            startItem.setEnabled(false);
            stopItem.setEnabled(true);
            settingsItem.setEnabled(false);
        } else {
            getDTCItem.setEnabled(true);
            stopItem.setEnabled(false);
            startItem.setEnabled(true);
            settingsItem.setEnabled(true);
        }

        return true;
    }

    private void addTableRow(String id, String key, String val) {

        if (obdAdapter != null) { // it works second time and later
            //obdModelList.clear();
            obdModelListClone.size();
            for (int i = 0; i < obdModelListClone.size(); i++) {
                if (obdModelListClone.get(i).getName().contains(key)) {
                    for (int j = 0; j < name.length; j++) {
                        if (name[j].equals(id)) {
                            obdModelListClone.set(i, new OBDModel(id, key, val, image[j], true));
                            obdAdapter.notifyDataSetChanged();
                            mrecyclerOBDList.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                        }

                    }
                }
                // restore recycleView state

                //   Toast.makeText(getApplicationContext(),"List--->>"+obdModelList,Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void queueCommands() {
        if (isServiceBound) {
            for (ObdCommand Command : ObdConfig.getCommands()) {
                if (prefs.getBoolean(Command.getName(), true))
                    service.queueJob(new ObdCommandJob(Command));
            }
        }
    }

    private void doBindService() {
        if (!isServiceBound) {
            Log.d(TAG, "Binding OBD service..");
            if (preRequisites) {
                btStatusTextView.setText(getString(R.string.status_bluetooth_connecting));
                ProgressDialog.setProgressDialog(MainActivity.this,Constants.lottiePath,getString(R.string.status_bluetooth_connecting));
                ProgressDialog.showProgress();
                Intent serviceIntent = new Intent(this, ObdGatewayService.class);
                bindService(serviceIntent, serviceConn, Context.BIND_AUTO_CREATE);
            } else {
                ProgressDialog.dismissProgress();
                btStatusTextView.setText(getString(R.string.status_bluetooth_disabled));
              /*  Intent serviceIntent = new Intent(this, MockObdGatewayService.class);
                bindService(serviceIntent, serviceConn, Context.BIND_AUTO_CREATE);*/
            }
        }
    }

    private void doUnbindService() {
        if (isServiceBound) {
            if (service.isRunning()) {
                service.stopService();
                if (preRequisites)
                    btStatusTextView.setText(getString(R.string.status_bluetooth_ok));
                ProgressDialog.dismissProgress();
            }
            Log.d(TAG, "Unbinding OBD service..");
            unbindService(serviceConn);
            isServiceBound = false;
            obdStatusTextView.setText(getString(R.string.status_obd_disconnected));
            ProgressDialog.dismissProgress();
        }
    }

    public void onLocationChanged(Location location) {
        mLastLocation = location;
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onProviderDisabled(String provider) {
    }

    public void onGpsStatusChanged(int event) {

        switch (event) {
            case GpsStatus.GPS_EVENT_STARTED:
                gpsStatusTextView.setText(getString(R.string.status_gps_started));
                break;
            case GpsStatus.GPS_EVENT_STOPPED:
                gpsStatusTextView.setText(getString(R.string.status_gps_stopped));
                break;
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                gpsStatusTextView.setText(getString(R.string.status_gps_fix));
                break;
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                btStatusTextView.setText(getString(R.string.status_bluetooth_connected));
            } else {
                Toast.makeText(this, R.string.text_bluetooth_disabled, Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private synchronized void gpsStart() {
        if (!mGpsIsStarted && mLocProvider != null && mLocService != null && mLocService.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mLocService.requestLocationUpdates(mLocProvider.getName(), getGpsUpdatePeriod(prefs), getGpsDistanceUpdatePeriod(prefs), this);
            mGpsIsStarted = true;
        } else {
            gpsStatusTextView.setText(getString(R.string.status_gps_no_support));
           /*   if(progressDialog!=null){
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }*/
         //   tl.removeAllViews();
        }
    }

    private synchronized void gpsStop() {
        if (mGpsIsStarted) {
            mLocService.removeUpdates(this);
            mGpsIsStarted = false;
            gpsStatusTextView.setText(getString(R.string.status_gps_stopped));
        } else {

        }
        ProgressDialog.dismissProgress();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.imgAdd:
                addOption();
                break;

            case R.id.start_Live:
                startLiveData();
                break;
           /* case R.id.Stop_Live:
                stopLiveData();
                break;*/
            case R.id.Setting:
                updateConfig();
                break;
            case R.id.dtc:
                getTroubleCodes();
                break;
            case R.id.tripList:
                startActivity(new Intent(this, TripListActivity.class));
                break;

        }

    }

    private void addOption() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        AlertDialog alertDialog;
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View layoutView = inflater.inflate( R.layout.obd_add_list, null );
        builder.setView(layoutView);
        alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        RecyclerView recycler_unchecked=layoutView.findViewById(R.id.recycler_unchecked);
        RecyclerView.LayoutManager  RecyclerViewLayoutManager = new LinearLayoutManager(MainActivity.this);
        recycler_unchecked.setLayoutManager(RecyclerViewLayoutManager);
        TextView mtext =layoutView.findViewById(R.id.text_no_record);
        Button submit=layoutView.findViewById(R.id.submit);

        if (uncheckAdapter==null) {
            uncheckAdapter = new Uncheck_Adapter(this,obdModelListClone,this);
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
                for(int i=0;i<obdModelListClone.size();i++){
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




    /**
     * Uploading asynchronous task
     */
    private class UploadAsyncTask extends AsyncTask<ObdReading, Void, Void> {

        @Override
        protected Void doInBackground(ObdReading... readings) {
            Log.d(TAG, "Uploading " + readings.length + " readings..");
            // instantiate reading service client
            final String endpoint = prefs.getString(ConfigActivity.UPLOAD_URL_KEY, "");
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(endpoint)
                    .build();
            ObdService service = restAdapter.create(ObdService.class);
            // upload readings
            for (ObdReading reading : readings) {
                try {
                    Response response = service.uploadReading(reading);
                    assert response.getStatus() == 200;
                } catch (RetrofitError re) {
                    Log.e(TAG, re.toString());
                }

            }
            Log.d(TAG, "Done");
            return null;
        }

    }
}
