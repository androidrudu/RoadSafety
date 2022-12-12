package com.montbleu.roadsafety.activity;

import static com.montbleu.Utils.Constants.scoreListModelArrayList;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.anastr.speedviewlib.Speedometer;
import com.github.anastr.speedviewlib.components.Section;
import com.github.anastr.speedviewlib.components.indicators.Indicator;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.github.mikephil.charting.utils.Utils;
import com.here.android.mpa.common.GeoBoundingBox;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.ftcr.FTCRRoute;
import com.here.android.mpa.ftcr.FTCRRouteOptions;
import com.here.android.mpa.ftcr.FTCRRoutePlan;
import com.here.android.mpa.ftcr.FTCRRouter;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.FTCRMapRoute;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.routing.RouteWaypoint;
import com.here.android.mpa.routing.RoutingError;
import com.montbleu.Retrofit.RestClient;
import com.montbleu.Retrofit.RestMethods;
import com.montbleu.Utils.Constants;
import com.montbleu.Utils.Preference_Details;
import com.montbleu.Utils.ProgressDialog;
import com.montbleu.Utils.Util_functions;
import com.montbleu.adapter.RiskAlertAdapter;
import com.montbleu.adapter.RiskAlertListAdapter;
import com.montbleu.interfaces.OnDialogButtonClickListener;
import com.montbleu.model.GPSGETDeviceResp;
import com.montbleu.model.GPSGetAlertListResp;
import com.montbleu.model.RiskAlertDataModel;
import com.montbleu.model.Risk_alert_model;
import com.montbleu.roadsafety.BeautifulProgressDialog;
import com.montbleu.roadsafety.R;
import com.montbleu.widget.GridSpacingItemDecoration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import eu.amirs.JSON;
import kotlin.Unit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LastDriveSummary extends AppCompatActivity implements OnDialogButtonClickListener, MediaPlayer.OnCompletionListener {
    PieChart pieChart;
    int[] tracks = new int[3];
    int currentTrack = 0;
    MediaPlayer mediaPlayer = null, ring = null;
    PieData pieData;
    PieDataSet pieDataSet;
    ArrayList pieEntries;
    //TextView tvName;
    private Map map = null;
    ProgressBar anticipate_gauge, self_conf_gauge, drive_skill_gauge;
    TextView antici_gauge_text_val, self_conf_text_val, drive_skill_text_val;
    public TextView tvTotalRides, tvDrivenKms, tvDrivenHours, tvLatestRide;
    TextView tvtime, tvkm, tvwait;
    private FTCRRouter.CancellableTask m_routeTask;
    String rideID;
    DecimalFormat REAL_FORMATTER = new DecimalFormat("00.00");

    Speedometer speedView;
    Drawable progressDrawable;
    LineChart lineChart, riskChart, driverStateChart, drivingStyleChart;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    RecyclerView risk_list;
    XAxis xAxis;
    ArrayList<Entry> lineEntries;
    ArrayList<Entry> lineEntries1;
    ArrayList<Entry> lineEntries2;
    ArrayList<Entry> risklineEntries, driverStateEntrises, drivingStyleEntries;
    LineDataSet lineDataSet, lineDataSet1, lineDataSet2;
    LineDataSet risklineDataSet, driverStateDataSet, drivingStyleDateSet;
    BeautifulProgressDialog progressDialog;
    RestMethods restMethods;
    ArrayList<GPSGETDeviceResp.DeviceDataList> graphArrayList = new ArrayList<>();
    double totdiff = 0f;
    Double diffKm = 0.0;
    Integer totKm = 0;
    int light_blue, yellow, red;
    public RecyclerView rvRiskAlerts;
    private static List<RiskAlertDataModel> riskAlertDataList;
    RiskAlertAdapter myAdapter;
    private AndroidXMapFragment mapFragment = null;
    TextView day_percent, night_percent, no_record, drivescore;
    List<Integer> graph;
    List<Risk_alert_model> risk_arrayList = new ArrayList<>();
    private GeoCoordinate m_startPoint;
    private GeoCoordinate m_endPoint;
    private FTCRMapRoute m_mapRoute;
    private FTCRRouter m_router;
    ImageView back_profile_btn;
    List<RouteWaypoint> routePoints = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lastdrivesummary);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        init();
    }

    public void init() {
        lineChart = findViewById(R.id.graph);
        riskChart = findViewById(R.id.risk_graph);
        drivescore = findViewById(R.id.drive_score);
        mapFragment = (AndroidXMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        driverStateChart = findViewById(R.id.driver_state_graph);
        drivingStyleChart = findViewById(R.id.driving_style_graph);
        pieChart = findViewById(R.id.piechart);
        back_profile_btn = findViewById(R.id.back_profile_btn);
        anticipate_gauge = findViewById(R.id.anticipate_gauge);
        self_conf_gauge = findViewById(R.id.self_conf_gauge);
        drive_skill_gauge = findViewById(R.id.drive_skill_gauge);
        antici_gauge_text_val = findViewById(R.id.antici_gauge_text_val);
        self_conf_text_val = findViewById(R.id.self_conf_text_val);
        drive_skill_text_val = findViewById(R.id.drive_skill_text_val);
        tvTotalRides = findViewById(R.id.tvTotalRides);
        tvDrivenKms = findViewById(R.id.tvDrivenKms);
        tvDrivenHours = findViewById(R.id.tvDrivenHours);
        no_record = findViewById(R.id.no_record);
        tvLatestRide = findViewById(R.id.tvLatestRide);
        tvtime = findViewById(R.id.tvtime);
        tvkm = findViewById(R.id.tvkm);
        tvwait = findViewById(R.id.tvwait);
        risk_list = findViewById(R.id.risk_list);
        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        risk_list.setLayoutManager(RecyclerViewLayoutManager);
        speedView = findViewById(R.id.speedometer_detail);
        rvRiskAlerts = findViewById(R.id.rvRiskAlert_lastDrive);
        //animatedPieView=findViewById(R.id.piechart);
        day_percent = findViewById(R.id.day_percent);
        night_percent = findViewById(R.id.night_percent);
        //profile_image=findViewById(R.id.profile_image);
        rideID = getIntent().getStringExtra("RideID");
        xAxis = lineChart.getXAxis();
        showDrivScoreDetails();
        showPieChart();
        showRiskChart();
        showRiskStyleChart();
        showDriverStateChart();
        showDrivingStyleChart();
        getAlertList();
        riskAlert();
        showAnimatePie();
        back_profile_btn.setOnClickListener(view -> finish());
        //showProfileImg();
        mapFragment.init(error -> {
            if (error == OnEngineInitListener.Error.NONE) {
                m_router = new FTCRRouter();
                map = mapFragment.getMap();
                map.setCenter(new GeoCoordinate(11.0168, 76.9558, 0.0), Map.Animation.NONE);
                map.setZoomLevel(11);

                //map.setMapScheme(Map.Scheme.HYBRID_DAY);
            } else {
                //Toast.makeText(LastDriveSummary.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDriverStateChart() {
        xAxis = driverStateChart.getXAxis();
        xAxis.setTextSize(13);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        light_blue = ContextCompat.getColor(LastDriveSummary.this, R.color.blue_grp);
        yellow = ContextCompat.getColor(LastDriveSummary.this, R.color.ride_amber);
        red = ContextCompat.getColor(LastDriveSummary.this, R.color.red);
        driverStateEntrises = new ArrayList<Entry>();
        driverStateEntrises.clear();
        driverStateDataSet = new LineDataSet(getDriverStateEntries(0f, 0f), "");
        driverStateDataSet.setColor(light_blue);
        //Linechart
        YAxis yAxis = driverStateChart.getAxisLeft();
        yAxis.setAxisMaximum(3);
        yAxis.setAxisMinimum(0);
        yAxis.setLabelCount(3);
        yAxis.setTextSize(13);
        if (Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(LastDriveSummary.this, R.drawable.fade_lightblue);
            // lineDataSet.setFillDrawable(drawable);
        } else {
            //lineDataSet.setFillColor(Color.BLACK);
        }
        driverStateDataSet.setDrawFilled(true);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(driverStateDataSet);
        LineData lineData = new LineData(dataSets);
        driverStateChart.invalidate();
        driverStateChart.setData(lineData);
        driverStateDataSet.setDrawCircles(false);
        driverStateDataSet.setDrawFilled(false);
        driverStateDataSet.setFillAlpha(85);
        driverStateDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        driverStateDataSet.setDrawValues(false);
        driverStateDataSet.setCircleColor(getResources().getColor(R.color.blue_grp));
        driverStateChart.setDrawGridBackground(false);
        driverStateChart.animateY(2000);
        driverStateChart.setDragEnabled(false);
        driverStateChart.setScaleEnabled(false);
        driverStateChart.setPinchZoom(true);
        driverStateChart.getXAxis().setAxisMinimum(0);
        driverStateChart.getXAxis().setAxisMaximum(Float.parseFloat(scoreListModelArrayList.get(0).getTotkms()));
        driverStateChart.getDescription().setEnabled(false);
        driverStateChart.getLegend().setEnabled(true);
        Legend l = driverStateChart.getLegend();
        l.setForm(Legend.LegendForm.EMPTY);
        driverStateChart.getAxisRight().setEnabled(false);
        driverStateChart.getXAxis().setDrawGridLines(false);
        driverStateChart.getAxisRight().setDrawGridLines(false);
        driverStateChart.getAxisLeft().setDrawGridLines(true);
        driverStateChart.setDrawGridBackground(false);
        driverStateChart.getAxisLeft().setAxisLineColor(Color.GRAY);
        driverStateChart.getXAxis().setAxisLineColor(Color.WHITE);
        driverStateChart.getData().setHighlightEnabled(false);
        driverStateChart.invalidate();
    }

    private void showRiskStyleChart() {
        xAxis = riskChart.getXAxis();
        xAxis.setTextSize(13);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        light_blue = ContextCompat.getColor(LastDriveSummary.this, R.color.blue_grp);
        yellow = ContextCompat.getColor(LastDriveSummary.this, R.color.ride_amber);
        red = ContextCompat.getColor(LastDriveSummary.this, R.color.red);
        risklineEntries = new ArrayList<Entry>();
        risklineEntries.clear();
        risklineDataSet = new LineDataSet(getRiskEntries(0f, 0f), "");
        risklineDataSet.setColor(light_blue);
        //Linechart
        YAxis yAxis = riskChart.getAxisLeft();
        yAxis.setAxisMaximum(3);
        yAxis.setAxisMinimum(0);
        yAxis.setLabelCount(3);
        yAxis.setTextSize(13);
        if (Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(LastDriveSummary.this, R.drawable.fade_lightblue);
            // lineDataSet.setFillDrawable(drawable);
        } else {
            //lineDataSet.setFillColor(Color.BLACK);
        }
        risklineDataSet.setDrawFilled(true);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(risklineDataSet);
        LineData lineData = new LineData(dataSets);
        riskChart.invalidate();
        riskChart.setData(lineData);
        risklineDataSet.setDrawCircles(false);
        risklineDataSet.setDrawFilled(false);
        risklineDataSet.setFillAlpha(85);
        risklineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        risklineDataSet.setDrawValues(false);
        risklineDataSet.setCircleColor(getResources().getColor(R.color.blue_grp));
        riskChart.setDrawGridBackground(false);
        riskChart.animateY(2000);
        riskChart.setDragEnabled(false);
        riskChart.setScaleEnabled(false);
        riskChart.setPinchZoom(true);
        riskChart.getXAxis().setAxisMinimum(0);
        riskChart.getXAxis().setAxisMaximum(Float.parseFloat(scoreListModelArrayList.get(0).getTotkms()));
        riskChart.getDescription().setEnabled(false);
        riskChart.getLegend().setEnabled(true);
        Legend l = riskChart.getLegend();
        l.setForm(Legend.LegendForm.EMPTY);
        riskChart.getAxisRight().setEnabled(false);
        riskChart.getXAxis().setDrawGridLines(false);
        riskChart.getAxisRight().setDrawGridLines(false);
        riskChart.getAxisLeft().setDrawGridLines(true);
        riskChart.setDrawGridBackground(false);
        riskChart.getAxisLeft().setAxisLineColor(Color.GRAY);
        riskChart.getXAxis().setAxisLineColor(Color.WHITE);
        riskChart.getData().setHighlightEnabled(false);
        riskChart.invalidate();
    }

    private void showDrivingStyleChart() {
        xAxis = drivingStyleChart.getXAxis();
        xAxis.setTextSize(13);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        light_blue = ContextCompat.getColor(LastDriveSummary.this, R.color.blue_grp);
        yellow = ContextCompat.getColor(LastDriveSummary.this, R.color.ride_amber);
        red = ContextCompat.getColor(LastDriveSummary.this, R.color.red);
        drivingStyleEntries = new ArrayList<Entry>();
        drivingStyleEntries.clear();
        drivingStyleDateSet = new LineDataSet(getDrivingStyleEntries(0f, 0f), "");
        drivingStyleDateSet.setColor(light_blue);
        //Linechart
        YAxis yAxis = drivingStyleChart.getAxisLeft();
        yAxis.setAxisMaximum(5);
        yAxis.setAxisMinimum(0);
        yAxis.setLabelCount(5);
        yAxis.setTextSize(13);
        if (Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(LastDriveSummary.this, R.drawable.fade_lightblue);
            // lineDataSet.setFillDrawable(drawable);
        } else {
            //lineDataSet.setFillColor(Color.BLACK);
        }
        drivingStyleDateSet.setDrawFilled(true);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(drivingStyleDateSet);
        LineData lineData = new LineData(dataSets);
        drivingStyleChart.invalidate();
        drivingStyleChart.setData(lineData);
        drivingStyleDateSet.setDrawCircles(false);
        drivingStyleDateSet.setDrawFilled(false);
        drivingStyleDateSet.setFillAlpha(85);
        drivingStyleDateSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        drivingStyleDateSet.setDrawValues(false);
        drivingStyleDateSet.setCircleColor(getResources().getColor(R.color.blue_grp));
        drivingStyleChart.setDrawGridBackground(false);
        drivingStyleChart.animateY(2000);
        drivingStyleChart.setDragEnabled(false);
        drivingStyleChart.setScaleEnabled(false);
        drivingStyleChart.setPinchZoom(true);
        drivingStyleChart.getXAxis().setAxisMinimum(0);
        drivingStyleChart.getXAxis().setAxisMaximum(Float.parseFloat(scoreListModelArrayList.get(0).getTotkms()));
        drivingStyleChart.getDescription().setEnabled(false);
        drivingStyleChart.getLegend().setEnabled(true);
        Legend l = drivingStyleChart.getLegend();
        l.setForm(Legend.LegendForm.EMPTY);
        drivingStyleChart.getAxisRight().setEnabled(false);
        drivingStyleChart.getXAxis().setDrawGridLines(false);
        drivingStyleChart.getAxisRight().setDrawGridLines(false);
        drivingStyleChart.getAxisLeft().setDrawGridLines(true);
        drivingStyleChart.setDrawGridBackground(false);
        drivingStyleChart.getAxisLeft().setAxisLineColor(Color.GRAY);
        drivingStyleChart.getXAxis().setAxisLineColor(Color.WHITE);
        drivingStyleChart.getData().setHighlightEnabled(false);
        drivingStyleChart.invalidate();
    }


    private void showAnimatePie() {
        pieEntries = new ArrayList<>();
        // int[] colorslist = new int[3];
        int[] colorslist = new int[]{Color.parseColor("#FA7D7C"),
                Color.parseColor("#9A79C4"), Color.parseColor("#78CAE3")};
        pieEntries.add(new PieEntry(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getUrbanPercent())), 0));
        pieEntries.add(new PieEntry(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getRuralPercent())), 1));
        pieEntries.add(new PieEntry(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getHighwayPercent())), 2));
        pieDataSet = new PieDataSet(pieEntries, "");
        pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormat(pieChart));
        pieChart.setData(pieData);
        pieChart.setDrawEntryLabels(true);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.setTouchEnabled(false);
        pieDataSet.setColors(ColorTemplate.createColors(colorslist));
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(10f);
        pieDataSet.setSliceSpace(0);
        pieChart.setHoleRadius(0);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        Legend l = pieChart.getLegend();
        l.setForm(Legend.LegendForm.EMPTY);
        day_percent.setText(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDayPercentage())) + "%");
        night_percent.setText(100 - Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDayPercentage())) + "%");
    }

    public class PercentFormat extends ValueFormatter {

        public DecimalFormat mFormat;
        private PieChart pieChart;

        public PercentFormat() {
            mFormat = new DecimalFormat("###,###,##0");
        }

        // Can be used to remove percent signs if the chart isn't in percent mode
        public PercentFormat(PieChart pieChart) {
            this();
            this.pieChart = pieChart;
        }

        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(value) + " %";
        }

        @Override
        public String getPieLabel(float value, PieEntry pieEntry) {
            if (pieChart != null && pieChart.isUsePercentValuesEnabled()) {
                // Converted to percent
                return getFormattedValue(value);
            } else {
                // raw value, skip percent sign
                return mFormat.format(value);
            }
        }
    }

    private void riskAlert() {
        graph = new ArrayList<>();
        rvRiskAlerts.setHasFixedSize(true);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        rvRiskAlerts.addItemDecoration(new GridSpacingItemDecoration(3, 0, true, 0));
        rvRiskAlerts.setLayoutManager(mGridLayoutManager);
        rvRiskAlerts.setNestedScrollingEnabled(false);
        riskAlertDataList = new ArrayList<>();
        riskAlertDataList.add(new RiskAlertDataModel("Animal Crossing", "0", R.drawable.animal_cross, "2", graph));
        riskAlertDataList.add(new RiskAlertDataModel("Caution", "0", R.drawable.caution, "3", graph));
        riskAlertDataList.add(new RiskAlertDataModel("Curves", "0", R.drawable.curves, "5", graph));
        riskAlertDataList.add(new RiskAlertDataModel("Force Acceleration", "0", R.drawable.force_acceleration, "32", graph));
        riskAlertDataList.add(new RiskAlertDataModel("Hill", "0", R.drawable.hill, "6", graph));
        riskAlertDataList.add(new RiskAlertDataModel("Hill Downwards", "0", R.drawable.hill_downwards, "7", graph));
        riskAlertDataList.add(new RiskAlertDataModel("Hill Upwards", "0", R.drawable.hill_upwards, "8", graph));
        riskAlertDataList.add(new RiskAlertDataModel("Icy Conditions", "0", R.drawable.icy, "22", graph));
        riskAlertDataList.add(new RiskAlertDataModel("Intersection", "0", R.drawable.intersection, "10", graph));
        riskAlertDataList.add(new RiskAlertDataModel("Lane Merge", "0", R.drawable.lane_merge, "11", graph));
        riskAlertDataList.add(new RiskAlertDataModel("Low Gear Area", "0", R.drawable.low_gear, "12", graph));
        riskAlertDataList.add(new RiskAlertDataModel("Mobile Use", "0", R.drawable.mobile_use, "29", graph));
        riskAlertDataList.add(new RiskAlertDataModel("Narrow Road", "0", R.drawable.narrow_road, "13", graph));
        riskAlertDataList.add(new RiskAlertDataModel("No Overtaking", "0", R.drawable.no_overtaking, "14", graph));
        riskAlertDataList.add(new RiskAlertDataModel("Over Speed", "0", R.drawable.over_speed, "30", graph));
        riskAlertDataList.add(new RiskAlertDataModel("Pedestrian Crossing", "0", R.drawable.pedestrian_crossing, "16", graph));
        riskAlertDataList.add(new RiskAlertDataModel("Priority", "0", R.drawable.priority, "17", graph));
        riskAlertDataList.add(new RiskAlertDataModel("Railway Crossing", "0", R.drawable.railway_cross, "19", graph));
        riskAlertDataList.add(new RiskAlertDataModel("Risk Of Grounding", "0", R.drawable.risk_of_grounding, "20", graph));
        riskAlertDataList.add(new RiskAlertDataModel("School Zone", "0", R.drawable.school_zone, "21", graph));
        riskAlertDataList.add(new RiskAlertDataModel("Slippery Roads", "0", R.drawable.slippery, "22", graph));
        riskAlertDataList.add(new RiskAlertDataModel("Stop Sign", "0", R.drawable.stop, "23", graph));
        riskAlertDataList.add(new RiskAlertDataModel("Sudden Braking", "0", R.drawable.sudden_breaking, "31", graph));
        riskAlertDataList.add(new RiskAlertDataModel("Traffic Light", "0", R.drawable.traffic_light, "24", graph));
        riskAlertDataList.add(new RiskAlertDataModel("Wind", "0", R.drawable.wind, "26", graph));
        riskAlertDataList.add(new RiskAlertDataModel("Winding Road", "0", R.drawable.winding_road, "27", graph));
        riskAlertDataList.add(new RiskAlertDataModel("Yield", "0", R.drawable.yield, "28", graph));
        myAdapter = new RiskAlertAdapter(getApplicationContext(), riskAlertDataList);
        rvRiskAlerts.setHasFixedSize(true);
        rvRiskAlerts.setAdapter(myAdapter);
        getAlertListCount();

    }

    private void getAlertListCount() {

        restMethods = RestClient.buildHTTPClient();
        restMethods.GETAlertCountResponse(Constants.referredBy, Preference_Details.getLoginSessID(), Constants.companyID, Preference_Details.getUserID(), rideID, Constants.type_GPS_Device_GET, Constants.gps_ALERT_COUNT).enqueue(new Callback<List<GPSGetAlertListResp>>() {
            @Override
            public void onResponse(@NonNull Call<List<GPSGetAlertListResp>> call, @NonNull Response<List<GPSGetAlertListResp>> response) {
                Log.e("status", "" + response.body());
                try {

                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            //  deviceDataListArrayList.clear();
                            for (int i = 0; i < response.body().size(); i++) {
                                for (int j = 0; j < response.body().get(i).getDeviceDataList().size(); j++) {
                                    if (!response.body().get(i).getDeviceDataList().get(j).getSubCategory().isEmpty()) {
                                        for (int k = 0; k < riskAlertDataList.size(); k++) {
                                            if (response.body().get(i).getDeviceDataList().get(j).getSubCategory().equals(Util_functions.getSubCategoryValueUpper(Integer.parseInt(riskAlertDataList.get(k).getAlertId())))) {
                                                riskAlertDataList.set(k, new RiskAlertDataModel(riskAlertDataList.get(k).getTitle(), response.body().get(i).getDeviceDataList().get(j).getNoOfRecords(), riskAlertDataList.get(k).getImage(), riskAlertDataList.get(i).getAlertId(), graph));
                                            }
                                        }
                                        myAdapter.notifyDataSetChanged();
                                    }
                                }
                            }

                            // getAlertsList();
                        } else if (response.code() == 412) {
                            try {
                                String Error = response.errorBody().string();
                                JSONObject obj = new JSONObject(Error);
                                JSONArray jsonArray1 = obj.getJSONArray("messages");
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    JSONObject object1 = jsonArray1.getJSONObject(0);
                                    Util_functions.ShowValidateAlertError(getApplicationContext(), object1.getString("message"), "OK");
                                }
                                ProgressDialog.dismissProgress();
                            } catch (Exception e) {
                                e.printStackTrace();
                                ProgressDialog.dismissProgress();

                            }

                        } else {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            String text = obj.getString("message");
                            Util_functions.ShowValidateAlertError(getApplicationContext(), text, "OK");
                            //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.txt_error),true);
                            ProgressDialog.dismissProgress();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        ProgressDialog.dismissProgress();
                    } catch (WindowManager.BadTokenException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<GPSGetAlertListResp>> call, @NonNull Throwable t) {
                if (t.toString().contains("Unable to resolve host")) {
                    Util_functions.ShowValidateAlertError(LastDriveSummary.this, getString(R.string.check_internet), "OK");
                }
                Util_functions.ShowAlertError(LastDriveSummary.this, t.getMessage(), "OK", LastDriveSummary.this);
                t.printStackTrace();
                ProgressDialog.dismissProgress();
            }
        });

    }

    private void getAlertList() {
        JSON generatedJsonObject = JSON.create(
                JSON.dic(
                        "deviceDataCategory", getString(R.string.gps_START_DATA) + "," + getString(R.string.gps_ALERT_DATA) + "," + getString(R.string.gps_END_DATA)
                )
        );
        restMethods = RestClient.buildHTTPClient();
        restMethods.GETgraphList(Constants.referredBy, Preference_Details.getLoginSessID(), Constants.companyID, Preference_Details.getUserID(), rideID, Constants.type_GPS_Device_GET, Constants.gps_DEVICE_AND_DATA_LIST, generatedJsonObject.toString()).enqueue(new Callback<List<GPSGetAlertListResp>>() {
            @Override
            public void onResponse(@NonNull Call<List<GPSGetAlertListResp>> call, @NonNull Response<List<GPSGetAlertListResp>> response) {
                try {
                    if (response.code() == 200) {
                        risk_arrayList.clear();
                        for (int i = 0; i < response.body().size(); i++) {
                            for (int j = 0; j < response.body().get(i).getDeviceDataList().size(); j++) {
                                String ps_sl_val = "";
                                if (Double.parseDouble(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLatitude()) != 0.0 && Double.parseDouble(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLongitude()) != 0.0) {
                                    routePoints.add(new RouteWaypoint(new GeoCoordinate(Double.parseDouble(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLatitude()), Double.parseDouble(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLongitude()))));
                                }
                                if (response.body().get(i).getDeviceDataList().get(j).getCategory().contains(getString(R.string.gps_START_DATA))) {
                                    map = mapFragment.getMap();
                                    map.setCenter(new GeoCoordinate(Double.parseDouble(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLatitude()), Double.parseDouble(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLongitude()), 0.0), Map.Animation.NONE);
                                    map.setZoomLevel(6);
                                    m_startPoint = new GeoCoordinate(Double.parseDouble(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLatitude()), Double.parseDouble(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLongitude()));
                                    setAlertImage("img_start", Double.parseDouble(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLatitude()), Double.parseDouble(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLongitude()));
                                    risk_arrayList.add(new Risk_alert_model(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getSpeed(),
                                            response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getZipCode(),
                                            response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getAlertKiloMeter(),
                                            response.body().get(i).getDeviceDataList().get(j).getCreatedAtToTimeZone(),
                                            response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getAlertValue(),
                                            response.body().get(i).getDeviceDataList().get(j).getCategory(),
                                            response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLatitude(),
                                            response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLongitude(), ps_sl_val,
                                            response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getRisk()));
                                    // String speed, String zipcode, String km_distance, String time_hh_mm, String alert_val, String category, String lat, String lng
                                } else if (response.body().get(i).getDeviceDataList().get(j).getCategory().contains(getString(R.string.gps_END_DATA))) {
                                    setAlertImage("img_finish", Double.parseDouble(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLatitude()), Double.parseDouble(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLongitude()));
                                    m_endPoint = new GeoCoordinate(Double.parseDouble(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLatitude()), Double.parseDouble(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLongitude()));
                                    risk_arrayList.add(new Risk_alert_model(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getSpeed(),
                                            response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getZipCode(),
                                            response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getAlertKiloMeter(),
                                            response.body().get(i).getDeviceDataList().get(j).getCreatedAtToTimeZone(),
                                            response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getAlertValue(),
                                            response.body().get(i).getDeviceDataList().get(j).getCategory(),
                                            response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLatitude(),
                                            response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLongitude(), ps_sl_val,
                                            response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getRisk()));
                                } else {
                                    if (response.body().get(i).getDeviceDataList().get(j).getSubCategory().contains("SUDDEN_BRAKING") || response.body().get(i).getDeviceDataList().get(j).getSubCategory().contains("FORCE_ACCELERATION")) {
                                        ps_sl_val = String.valueOf(Integer.parseInt(String.valueOf(Math.round(Float.parseFloat(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getPreviousSpeed())))));
                                    } else if (response.body().get(i).getDeviceDataList().get(j).getSubCategory().contains("OVER_SPEED")) {
                                        ps_sl_val = response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getAlert();
                                    } else {
                                        ps_sl_val = "-";
                                    }
                                    setAlertImage(Util_functions.getAlertImageString(Integer.parseInt(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getAlertValue())), Double.parseDouble(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLatitude()), Double.parseDouble(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLongitude()));
                                    risk_arrayList.add(new Risk_alert_model(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getSpeed(),
                                            response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getZipCode(),
                                            response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getAlertKiloMeter(),
                                            response.body().get(i).getDeviceDataList().get(j).getCreatedAtToTimeZone(),
                                            response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getAlertValue(),
                                            response.body().get(i).getDeviceDataList().get(j).getCategory(),
                                            response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLatitude(),
                                            response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLongitude(), ps_sl_val,
                                            response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getRisk()));
                                }
                            }
                            calculateRoute();
                        }
                        //    Toast.makeText(getApplicationContext(), "Tested", Toast.LENGTH_SHORT).show();
                        if (risk_arrayList.size() > 0) {
                            risk_list.setVisibility(View.VISIBLE);
                            no_record.setVisibility(View.GONE);
                            risk_list.setAdapter(new RiskAlertListAdapter(LastDriveSummary.this, risk_arrayList));
                        } else {
                            risk_list.setVisibility(View.GONE);
                            no_record.setVisibility(View.VISIBLE);
                        }


                    } else if (response.code() == 412) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    } else {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                } catch (Exception e) {

                    getAlertList();

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<GPSGetAlertListResp>> call, @NonNull Throwable t) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });

    }

    private void calculateRoute() {
        /*
         * Initialize a RouteOption. HERE Mobile SDK allow users to define their own parameters
         * for the route calculation, including transport modes, route types and
         * route restrictions etc. Please refer to API doc for full list of APIs
         */
        mapFragment.init(error -> {
            if (error == OnEngineInitListener.Error.NONE) {
                map = mapFragment.getMap();
                m_router = new FTCRRouter();
                map.setCenter(new GeoCoordinate(11.0168, 76.9558, 0.0), Map.Animation.NONE);
                map.setZoomLevel(11);
                //mapFakeRoutePolyline.setPatternStyle(MapPolyline.PatternStyle.DASH_PATTERN);
                FTCRRouteOptions routeOptions = new FTCRRouteOptions();
                /* Other transport modes are also available e.g Pedestrian */
                routeOptions.setTransportMode(FTCRRouteOptions.TransportMode.CAR);
                /* Calculate the shortest route available. */
                routeOptions.setRouteType(FTCRRouteOptions.Type.FASTEST);
                /* Define waypoints for the route */

                /* Initialize a RoutePlan */

                FTCRRoutePlan routePlan = new FTCRRoutePlan(routePoints, routeOptions);
    /*
      Set the name of the map overlay. It has to be the same that is used for uploading
      the custom roads to the fleet telematics server.
     */
                if (m_routeTask != null) {
                    m_routeTask.cancel();
                }

                m_routeTask = m_router.calculateRoute(routePlan, (routeResults, errorResponse) -> {
                    /* Calculation is done. Let's handle the result */
                    if (errorResponse.getErrorCode() == RoutingError.NONE) {
                        if (routeResults.get(0) != null) {
                            /* Create a FTCRMapRoute so that it can be placed on the map */
                            m_mapRoute = new FTCRMapRoute(routeResults.get(0));

                            /* Add the FTCRMapRoute to the map */

                            map.addMapObject(m_mapRoute);
                            //map.addMapObject(mapFakeRoutePolyline);

                            /*
                             * We may also want to make sure the map view is orientated properly
                             * so the entire route can be easily seen.
                             */
                            GeoBoundingBox gbb = routeResults.get(0).getBoundingBox();
                            map.zoomTo(gbb, Map.Animation.NONE,
                                    Map.MOVE_PRESERVE_ORIENTATION);
                        } else {
                            Toast.makeText(LastDriveSummary.this,
                                    "Error:route results returned is not valid",
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(LastDriveSummary.this,
                                "Error:route calculation returned error code: "
                                        + errorResponse.getErrorCode()
                                        + ",\nmessage: " + errorResponse.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
                /*try {`
                    Image image = new Image();
                    image.setImageResource(R.drawable.over_speed);
                    MapMarker customMarker = new MapMarker(new GeoCoordinate(37.7397, -121.4252, 0.0), image);
                    map.addMapObject(customMarker);
                } catch (Exception e) {
                    com.here.odnp.util.Log.e("HERE", e.getMessage());
                }*/

            }
        });

    }

    private void showRiskChart() {
        xAxis = lineChart.getXAxis();
        xAxis.setTextSize(13);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        light_blue = ContextCompat.getColor(LastDriveSummary.this, R.color.blue_grp);
        yellow = ContextCompat.getColor(LastDriveSummary.this, R.color.ride_amber);
        red = ContextCompat.getColor(LastDriveSummary.this, R.color.red);
        lineEntries = new ArrayList<Entry>();
        lineEntries.clear();
        getGraphList();
        //getlocalGraphList();
        lineDataSet = new LineDataSet(getEntries(0f, 0f, 11.0168, 76.9558), "");
        lineDataSet1 = new LineDataSet(getLineEntries1(), "");
        lineDataSet2 = new LineDataSet(getLineEntries2(), "");
        lineDataSet.setColor(light_blue);
        lineDataSet1.setColor(red);
        lineDataSet2.setColor(yellow);
        //Linechart
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMaximum(100);
        yAxis.setAxisMinimum(0);
        yAxis.setLabelCount(10);
        yAxis.setTextSize(15);
        if (Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(LastDriveSummary.this, R.drawable.fade_lightblue);
            // lineDataSet.setFillDrawable(drawable);
        } else {
            //lineDataSet.setFillColor(Color.BLACK);
        }
        lineDataSet.setDrawFilled(true);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        dataSets.add(lineDataSet1);
        dataSets.add(lineDataSet2);
        LineData lineData = new LineData(dataSets);
        lineChart.invalidate();
        lineChart.setData(lineData);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawFilled(false);
        lineDataSet.setFillAlpha(85);
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setDrawValues(false);
        lineDataSet1.setDrawCircles(false);
        lineDataSet1.setDrawValues(false);
        lineDataSet2.setDrawCircles(false);
        lineDataSet2.setDrawValues(false);
        lineDataSet.setCircleColor(getResources().getColor(R.color.blue_grp));
        lineChart.setDrawGridBackground(false);
        lineChart.animateY(2000);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(true);
        lineChart.getXAxis().setAxisMinimum(0);
        lineChart.getXAxis().setAxisMaximum(Float.parseFloat(scoreListModelArrayList.get(0).getTotkms()));
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(true);
        Legend l = lineChart.getLegend();
        l.setForm(Legend.LegendForm.EMPTY);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(true);
        lineChart.setDrawGridBackground(false);
        lineChart.getAxisLeft().setAxisLineColor(Color.GRAY);
        lineChart.getXAxis().setAxisLineColor(Color.WHITE);
        lineChart.getData().setHighlightEnabled(false);
        lineChart.invalidate();
    }


    private void getGraphList() {
        progressDialog = new BeautifulProgressDialog(this, BeautifulProgressDialog.withLottie, null);
        progressDialog.setLottieLocation(Constants.lottiePath);
        progressDialog.setLottieLoop(true);
        progressDialog.setLayoutColor(Color.TRANSPARENT);
        progressDialog.setLayoutElevation(0);
        progressDialog.setCancelable(true);
        progressDialog.show();
        JSON generatedJsonObject = JSON.create(
                JSON.dic(
                        "deviceDataCategory", getString(R.string.gps_DISTANCE_DATA)
                )
        );
        restMethods = RestClient.buildHTTPClient();
        restMethods.GETgraphList(Constants.referredBy, Preference_Details.getLoginSessID(), Constants.companyID, Preference_Details.getUserID(), rideID, Constants.type_GPS_Device_GET, Constants.gps_DEVICE_AND_DATA_LIST, generatedJsonObject.toString()).enqueue(new Callback<List<GPSGetAlertListResp>>() {
            @Override
            public void onResponse(@NonNull Call<List<GPSGetAlertListResp>> call, @NonNull Response<List<GPSGetAlertListResp>> response) {
                Log.e("status", "" + response.body());
                try {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            graphArrayList.clear();
                            for (int i = 0; i < response.body().size(); i++) {
                                for (int j = 0; j < response.body().get(i).getDeviceDataList().size(); j++) {
                                    if (j == 0) {
                                        getEntries(0.0f, 0.0f, new Double(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLatitude()), new Double(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLongitude()));
                                        if (response.body().get(i).getDeviceDataList().get(j).getDeviceDataField1().getDriverState() != null) {
                                            getRiskEntries(0.0f, 0.0f);
                                            getDriverStateEntries(0.0f, 0.0f);
                                            getDrivingStyleEntries(0.0f, 0.0f);
                                        }
                                    } else if (j == response.body().get(i).getDeviceDataList().size() - 1) {
                                        getEntries(Float.valueOf(scoreListModelArrayList.get(0).getTotkms()), 0.0f, new Double(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLatitude()), new Double(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLongitude()));
                                        if (response.body().get(i).getDeviceDataList().get(j).getDeviceDataField1().getDriverState() != null) {
                                            getRiskEntries(Float.valueOf(scoreListModelArrayList.get(0).getTotkms()), 0.0f);
                                            getDriverStateEntries(Float.valueOf(scoreListModelArrayList.get(0).getTotkms()), 0.0f);
                                            getDrivingStyleEntries(Float.valueOf(scoreListModelArrayList.get(0).getTotkms()), 0.0f);
                                        }
                                    } else {
                                        getEntries(Float.parseFloat(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getKiloMeter()), Float.parseFloat(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getRisk()), new Double(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLatitude()), new Double(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLongitude()));
                                        if (response.body().get(i).getDeviceDataList().get(j).getDeviceDataField1().getDriverState() != null) {
                                            getRiskEntries(Float.parseFloat(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getKiloMeter()), Float.parseFloat(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField1().getRiskStyle()));
                                            getDriverStateEntries(Float.parseFloat(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getKiloMeter()), Float.parseFloat(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField1().getDriverState()));
                                            getDrivingStyleEntries(Float.parseFloat(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getKiloMeter()), Float.parseFloat(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField1().getDrivingStyle()));
                                        }
                                    }

                                }
                            }
                        }
                        lineChart.invalidate();
                        riskChart.invalidate();
                        driverStateChart.invalidate();
                        drivingStyleChart.invalidate();
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    } else if (response.code() == 412) {
                        try {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowValidateAlertError(getApplicationContext(), object1.getString("message"), "OK");
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
                        String Error = response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text = obj.getString("message");
                        Util_functions.ShowValidateAlertError(getApplicationContext(), text, "OK");
                        //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.txt_error),true);
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    } catch (WindowManager.BadTokenException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<GPSGetAlertListResp>> call, @NonNull Throwable t) {
                if (t.toString().contains("Unable to resolve host")) {
                    Util_functions.ShowValidateAlertError(LastDriveSummary.this, getString(R.string.check_internet), "OK");
                }
                Util_functions.ShowAlertError(LastDriveSummary.this, t.getMessage(), "OK", LastDriveSummary.this);
                t.printStackTrace();
                ProgressDialog.dismissProgress();
            }
        });


    }


    private ArrayList<Entry> getEntries(Float x, float y, Double lat, Double longi) {
        if (x == 0f && y == 0f) {
            if (Float.parseFloat(scoreListModelArrayList.get(0).getTotkms()) > 0f) {
                lineEntries.add(new Entry(0, 0));
                Collections.sort(lineEntries, new EntryXComparator());
            }
        } else if (x.equals(Float.parseFloat(scoreListModelArrayList.get(0).getTotkms()))) {
            lineEntries.add(new Entry(x, 0));
            Collections.sort(lineEntries, new EntryXComparator());
        } else {
            float xx = x * 10;
            double diffkk = diffKm * 10;
            double diff = xx % diffkk;
            if (diff == 0.0) {
                DecimalFormat REAL_FORMATTER = new DecimalFormat("0.0");
                diffKm = Double.valueOf(REAL_FORMATTER.format(diffKm));
                //routePoints.add(new RouteWaypoint(new GeoCoordinate(lat,longi)));
                if (y == -1.0 || y == -100.0) {
                    y = 0;
                    lineEntries.add(new Entry(x, y));
                    Collections.sort(lineEntries, new EntryXComparator());
                } else {
                    lineEntries.add(new Entry(x, y));
                    Collections.sort(lineEntries, new EntryXComparator());
                }
            } else {
                if (totKm > 50) {
                    if (y != -1.0 && y != -100.0 && y != 0.0) {
                        if (y > 50.0) {
                            lineEntries.add(new Entry(x, y));
                            Collections.sort(lineEntries, new EntryXComparator());
                        }
                    }
                } else {
                    if (y != -1.0 && y != -100.0 && y != 0.0) {
                        lineEntries.add(new Entry(x, y));
                        Collections.sort(lineEntries, new EntryXComparator());
                    }
                }
            }
        }

        lineChart.setEnabled(true);
        // x- km y - risk
        return lineEntries;
    }

    private ArrayList<Entry> getLineEntries1() {

        lineEntries1 = new ArrayList<Entry>();
        lineEntries1.clear();
        lineEntries1.add(new Entry(0, 90));
        if (!scoreListModelArrayList.isEmpty()) {
            lineEntries1.add(new Entry(Float.parseFloat(scoreListModelArrayList.get(0).getTotkms()), 90));
        } else {
            startActivity(new Intent(LastDriveSummary.this, RoadStartPageActivity.class));
            finish();
        }
        return lineEntries1;
    }

    private ArrayList<Entry> getRiskEntries(Float x, float y) {

        risklineEntries.add(new Entry(x, y));
        Collections.sort(risklineEntries, new EntryXComparator());
        return risklineEntries;
    }

    private ArrayList<Entry> getDriverStateEntries(Float x, float y) {
        /*
        return driverStateEntrises;*/
        driverStateEntrises.add(new Entry(x, y));
        Collections.sort(driverStateEntrises, new EntryXComparator());
        return driverStateEntrises;
    }

    private ArrayList<Entry> getDrivingStyleEntries(Float x, float y) {
        drivingStyleEntries.add(new Entry(x, y));
        Collections.sort(drivingStyleEntries, new EntryXComparator());
        return drivingStyleEntries;
    }

    private ArrayList<Entry> getLineEntries2() {  //need to set co -pilot mode
        lineEntries2 = new ArrayList<Entry>();
        lineEntries2.add(new Entry(0, 60));    //value -70 - co -pilot mode need to change
        if (!scoreListModelArrayList.isEmpty()) {
            lineEntries2.add(new Entry(Float.parseFloat(scoreListModelArrayList.get(0).getTotkms()), 60));
        } else {
            startActivity(new Intent(LastDriveSummary.this, RoadStartPageActivity.class));
            finish();
        }
        return lineEntries2;
    }


    private void showDrivScoreDetails() {
        if (!Constants.scoreListModelArrayList.isEmpty()) {
            if (Constants.scoreListModelArrayList.get(0).getDriveScore().equals("0.0") || Constants.scoreListModelArrayList.get(0).getDriveScore().equals("") || Constants.scoreListModelArrayList.get(0).getDriveScore().contains("-1.0") || Constants.scoreListModelArrayList.get(0).getDriveScore().contains("-0.0")) {
                //  holder.tvRide1Time.setText("0");
                ring = MediaPlayer.create(LastDriveSummary.this
                        , R.raw.scorenotavailabe_travelwas_tooshort);
                ring.start();
                tvTotalRides.setText("0");
                drivescore.setText("0");
                tvDrivenHours.setText("Poor");
                anticipate_gauge.setProgress(0);
                self_conf_gauge.setProgress(0);
                drive_skill_gauge.setProgress(0);
            } else {
                if (Math.round(Float.parseFloat(Constants.scoreListModelArrayList.get(0).getDriveScore())) <= 80) {
                    tvDrivenHours.setText("Bronze");
                } else if (Math.round(Float.parseFloat(Constants.scoreListModelArrayList.get(0).getDriveScore())) > 80 && Math.round(Float.parseFloat(Constants.scoreListModelArrayList.get(0).getDriveScore())) <= 90) {
                    tvDrivenHours.setText("Silver");
                } else if (Math.round(Float.parseFloat(Constants.scoreListModelArrayList.get(0).getDriveScore())) > 90) {
                    tvDrivenHours.setText("Gold");
                }
                if (Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDriveScore())) < 95) {
                    String score = "score_" + Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDriveScore()));
                    if (Preference_Details.getVoice()) {
                        tracks[0] = R.raw.your_drivingscore_is;
                        tracks[1] = getApplicationContext().getResources().getIdentifier(score, "raw", getApplicationContext().getPackageName());
                        tracks[2] = R.raw.trytofollow_safealert;
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), tracks[currentTrack]);
                        mediaPlayer.setOnCompletionListener(LastDriveSummary.this);
                        mediaPlayer.start();
                        //txt_to_speech("your driving score is" + String.valueOf(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDriveScore()))) + ". try to follow better road drive it safe alerts");
                    }
                } else if (Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDriveScore())) >= 95) {
                    String score = "score_" + Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDriveScore()));
                    if (Preference_Details.getVoice()) {
                        tracks[0] = R.raw.your_drivingscore_is;
                        tracks[1] = getApplicationContext().getResources().getIdentifier(score, "raw", getApplicationContext().getPackageName());
                        tracks[2] = R.raw.excellent_driving;
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), tracks[currentTrack]);
                        mediaPlayer.setOnCompletionListener(LastDriveSummary.this);
                        mediaPlayer.start();
                        //txt_to_speech("your driving score is" + String.valueOf(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDriveScore()))) + ". excellent driving");
                    }
                }
                anticipate_gauge.setProgress(Math.round(Float.parseFloat(Constants.scoreListModelArrayList.get(0).getAnticipate())));
                self_conf_gauge.setProgress(Math.round(Float.parseFloat(Constants.scoreListModelArrayList.get(0).getSelfConfident())));
                drive_skill_gauge.setProgress(Math.round(Float.parseFloat(Constants.scoreListModelArrayList.get(0).getDrivingSkill())));
                tvTotalRides.setText(String.valueOf(Math.round(Float.parseFloat(Constants.scoreListModelArrayList.get(0).getDriveScore()))));
                drivescore.setText(String.valueOf(Math.round(Float.parseFloat(Constants.scoreListModelArrayList.get(0).getDriveScore()))));
                antici_gauge_text_val.setText(String.valueOf(Math.round(Float.parseFloat(Constants.scoreListModelArrayList.get(0).getAnticipate()))));
                self_conf_text_val.setText(String.valueOf(Math.round(Float.parseFloat(Constants.scoreListModelArrayList.get(0).getSelfConfident()))));
                drive_skill_text_val.setText(String.valueOf(Math.round(Float.parseFloat(Constants.scoreListModelArrayList.get(0).getDrivingSkill()))));


                progressDrawable = anticipate_gauge.getProgressDrawable().mutate();
                if (Math.round(Float.parseFloat(Constants.scoreListModelArrayList.get(0).getAnticipate())) <= 80) {
                    progressDrawable.setColorFilter(
                            getColor(R.color.ride_red), android.graphics.PorterDuff.Mode.SRC_IN);
                } else if (Math.round(Float.parseFloat(Constants.scoreListModelArrayList.get(0).getAnticipate())) > 80 && Math.round(Float.parseFloat(Constants.scoreListModelArrayList.get(0).getAnticipate())) <= 90) {
                    progressDrawable.setColorFilter(getColor(R.color.ride_amber), android.graphics.PorterDuff.Mode.SRC_IN);
                } else if (Math.round(Float.parseFloat(Constants.scoreListModelArrayList.get(0).getAnticipate())) > 90) {
                    progressDrawable.setColorFilter(
                            getColor(R.color.ride_green), android.graphics.PorterDuff.Mode.SRC_IN);
                }
                progressDrawable = self_conf_gauge.getProgressDrawable().mutate();

                if (Math.round(Float.parseFloat(Constants.scoreListModelArrayList.get(0).getSelfConfident())) <= 80) {
                    progressDrawable.setColorFilter(
                            getColor(R.color.ride_red), android.graphics.PorterDuff.Mode.SRC_IN);
                } else if (Math.round(Float.parseFloat(Constants.scoreListModelArrayList.get(0).getSelfConfident())) > 80 && Math.round(Float.parseFloat(Constants.scoreListModelArrayList.get(0).getSelfConfident())) <= 90) {
                    progressDrawable.setColorFilter(
                            getColor(R.color.ride_amber), android.graphics.PorterDuff.Mode.SRC_IN);
                } else if (Math.round(Float.parseFloat(Constants.scoreListModelArrayList.get(0).getSelfConfident())) > 90) {
                    progressDrawable.setColorFilter(
                            getColor(R.color.ride_green), android.graphics.PorterDuff.Mode.SRC_IN);
                }
                progressDrawable = drive_skill_gauge.getProgressDrawable().mutate();
                if (Math.round(Float.parseFloat(Constants.scoreListModelArrayList.get(0).getDrivingSkill())) <= 80) {
                    progressDrawable.setColorFilter(
                            getColor(R.color.ride_red), android.graphics.PorterDuff.Mode.SRC_IN);
                } else if (Math.round(Float.parseFloat(Constants.scoreListModelArrayList.get(0).getDrivingSkill())) > 80 && Math.round(Float.parseFloat(Constants.scoreListModelArrayList.get(0).getDrivingSkill())) <= 90) {
                    progressDrawable.setColorFilter(
                            getColor(R.color.ride_amber), android.graphics.PorterDuff.Mode.SRC_IN);
                } else if (Math.round(Float.parseFloat(Constants.scoreListModelArrayList.get(0).getDrivingSkill())) > 90) {
                    progressDrawable.setColorFilter(
                            getColor(R.color.ride_green), android.graphics.PorterDuff.Mode.SRC_IN);
                }


            }


            //  tvTotalRides.setText(Math.round(Float.parseFloat(Constants.scoreListModelArrayList.get(0).getDriveScore())));

            if (Constants.scoreListModelArrayList.get(0).getName().equals("0") || Constants.scoreListModelArrayList.get(0).getName().equals("")) {
                tvDrivenKms.setText("0");
            } else {
                tvDrivenKms.setText(Constants.scoreListModelArrayList.get(0).getName().substring(4));
            }

            if (Constants.scoreListModelArrayList.get(0).getDate_time().equals("0.0") || Constants.scoreListModelArrayList.get(0).getDate_time().equals("") || Constants.scoreListModelArrayList.get(0).getDate_time().contains("-1.0") || Constants.scoreListModelArrayList.get(0).getDate_time().equals("0")) {
                tvLatestRide.setText("");
            } else {
                //mtotTime = REAL_FORMATTER.format(Double.parseDouble(Constants.scoreListModelArrayList.get(0).getTotTime()));

                try {
                    SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date newDate = spf.parse(Constants.scoreListModelArrayList.get(0).getDate_time());
                    spf = new SimpleDateFormat("dd MMM yyyy hh:mm:ss");
                    tvLatestRide.setText(spf.format(newDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }

            if (Constants.scoreListModelArrayList.get(0).getTotkms().equals("0.0") || Constants.scoreListModelArrayList.get(0).getTotkms().equals("") || Constants.scoreListModelArrayList.get(0).getTotkms().contains("-1.0") || Constants.scoreListModelArrayList.get(0).getTotkms().contains("-0.0")) {
                tvkm.setText("00.00 KM");
            } else {
                tvkm.setText(REAL_FORMATTER.format(Double.parseDouble(Constants.scoreListModelArrayList.get(0).getTotkms())) + " KM");
                xAxis.setAxisMaximum(10000);
                xAxis.setAxisMinimum(0);
                //lineChart.setVisibleXRange(0, Float.parseFloat(scoreListModelArrayList.get(0).getTotkms()));
                DecimalFormat REAL_FORMATTER = new DecimalFormat("0.0");
                totKm = Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getTotkms()));
                totdiff = (float) totKm / 50;
                diffKm = Double.valueOf(REAL_FORMATTER.format(totdiff));
                totdiff = diffKm * 10;
                if (totdiff % 2 == 0) {
                    if (totKm / 10 == 0) {
                        if (totdiff != 0) {
                            diffKm = diffKm + 0;
                            totdiff = diffKm;
                        } else {
                            diffKm = diffKm + 0.2;
                            totdiff = diffKm;
                        }
                    } else {
                        diffKm = diffKm + 0.2;
                        totdiff = diffKm;
                    }

                } else {
                    diffKm = diffKm + 0.1;
                    totdiff = diffKm;
                }
                // xAxis.setGranularityEnabled(true);                                   //maxi km   x axis km
                xAxis.setLabelCount(6);
            }

            if (Constants.scoreListModelArrayList.get(0).getDate_time().equals("0.0") || Constants.scoreListModelArrayList.get(0).getDate_time().equals("") || Constants.scoreListModelArrayList.get(0).getDate_time().contains("-1.0") || Constants.scoreListModelArrayList.get(0).getDate_time().contains("-0.0")) {
                //  holder.tvRide1Time.setText("0");
                tvtime.setText("0");
            } else {
                getDateCurrentTimeZone(Constants.scoreListModelArrayList.get(0).getDate_time());
            }


            if (Constants.scoreListModelArrayList.get(0).getTotTime().equals("0.0") || Constants.scoreListModelArrayList.get(0).getTotTime().equals("") || Constants.scoreListModelArrayList.get(0).getTotTime().contains("-1.0") || Constants.scoreListModelArrayList.get(0).getTotTime().contains("-0.0")) {
                tvwait.setText("00.00");
            } else {
                tvwait.setText(REAL_FORMATTER.format(Double.parseDouble(Constants.scoreListModelArrayList.get(0).getTotTime())) + " Mins");
            }


            // Constants.scoreListModelArrayList.get(0).driveScore;
        }
    }


    private void getDateCurrentTimeZone(String timestamp) {

        try {
            SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date newDate = spf.parse(timestamp);
            spf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
            timestamp = spf.format(newDate);

            // String  datetimeString = DateFormat.format("dd/MM/yyyy hh:mm:ss a", new Date()).toString();
            String timeString = timestamp.substring(11, 16);
            String dateString1 = timestamp.substring(0, 11);
            String t2 = timestamp.substring(20, 22);
            Log.d("data=e :", timeString + " " + dateString1 + " " + t2);
            tvtime.setText(Html.fromHtml("<b>" + timeString + "</b>") + " " + t2.toUpperCase(Locale.getDefault()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showPieChart() {

        speedView.getSections().clear();
        speedView.setIndicator(Indicator.Indicators.NeedleIndicator);
        speedView.setSpeedTextSize(0);

        speedView.setUnit("");
        speedView.addSections(new Section(0f, .8f, getResources().getColor(R.color.ride_red), speedView.dpTOpx(25)),
                new Section(.8f, .9f, getResources().getColor(R.color.ride_amber), speedView.dpTOpx(25)),
                new Section(.9f, 1f, getResources().getColor(R.color.ride_green), speedView.dpTOpx(25)));
        speedView.setOnSectionChangeListener((previousSection, newSection) -> {
            if (previousSection != null) {
                previousSection.setWidth(speedView.dpTOpx(25f));
            }
            if (newSection != null) {
                if (Constants.scoreListModelArrayList.get(0).getDriveScore() != null || Constants.scoreListModelArrayList.get(0).getDriveScore() != "") {
                    speedView.setSpeedAt(Math.round(Float.parseFloat(Constants.scoreListModelArrayList.get(0).getDriveScore())));
                    if (Integer.parseInt(String.valueOf(Math.round(Float.parseFloat(Constants.scoreListModelArrayList.get(0).getDriveScore())))) <= 0) {
                        newSection.setWidth(speedView.dpTOpx(25f));
                    } else {
                        newSection.setWidth(speedView.dpTOpx(35f));
                    }
                }
            }
            return Unit.INSTANCE;
        });


    }

    @Override
    public void onPositiveButtonClicked(AlertDialog alertDialog) {
        finish();

    }

    private void setAlertImage(String img, Double lat, Double longi) {
        try {
            Image image = new Image();
            String uri = "@drawable/" + img;
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            image.setImageResource(imageResource);
            mapFragment.getPositionIndicator().setMarker(image);
            MapMarker customMarker = new MapMarker(new GeoCoordinate(lat, longi, 0.0), image);
            map.addMapObject(customMarker);
        } catch (Exception e) {
            com.here.odnp.util.Log.e("HERE", e.getMessage());
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.release();
        if (currentTrack < tracks.length - 1) {
            currentTrack++;
            mp = MediaPlayer.create(getApplicationContext(), tracks[currentTrack]);
            mp.setOnCompletionListener(this);
            mp.start();
        }
    }

    @Override
    public void onNegativeButtonClicked() {

    }
}