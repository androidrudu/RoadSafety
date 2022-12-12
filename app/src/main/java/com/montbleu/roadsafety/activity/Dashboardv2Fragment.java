package com.montbleu.roadsafety.activity;

import static org.chromium.base.ContextUtils.getApplicationContext;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.montbleu.Retrofit.RestClient;
import com.montbleu.Retrofit.RestMethods;
import com.montbleu.Utils.Constants;
import com.montbleu.Utils.Preference_Details;
import com.montbleu.Utils.Util_functions;
import com.montbleu.adapter.RideListAdapter;
import com.montbleu.adapter.RiskAlertAdapter;
import com.montbleu.model.DriveSummaryPOJO;
import com.montbleu.model.DrivingScorePOJO;
import com.montbleu.model.RideLeaderboardPOJO;
import com.montbleu.model.RiskAlertDataModel;
import com.montbleu.model.RiskAlertPOJO;
import com.montbleu.model.rideListItem;
import com.montbleu.roadsafety.BeautifulProgressDialog;
import com.montbleu.roadsafety.R;
import com.montbleu.widget.GridSpacingItemDecoration;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Dashboardv2Fragment extends Fragment {

    public TextView tvTotalRides, tvDrivenKms, tvDrivenHours, tvLatestRide, tvName;
    RecyclerView rvRidesLeaderboard;
    private RideListAdapter rideListAdapter;
    private ArrayList<rideListItem> rideList = new ArrayList<>();
    public Spinner spinYear, spinMonth;
    public RecyclerView rvRiskAlerts;
    private static List<RiskAlertDataModel> riskAlertDataList;
    //    RiskAlertDataModel riskData;
    ArrayList<Entry> lineEntries = new ArrayList<Entry>();

    RestMethods restMethods;
    LineChart lineChart;
    ImageView profile_image;
    RiskAlertAdapter myAdapter;
    BeautifulProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboardv2, container, false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        tvName = view.findViewById(R.id.tvName);
        tvTotalRides = view.findViewById(R.id.tvTotalRides);
        tvDrivenKms = view.findViewById(R.id.tvDrivenKms);
        tvDrivenHours = view.findViewById(R.id.tvDrivenHours);
        tvLatestRide = view.findViewById(R.id.tvLatestRide);
        rvRidesLeaderboard = view.findViewById(R.id.rvRidesLeaderboard);
        rvRiskAlerts = view.findViewById(R.id.rvRiskAlert);
        profile_image = view.findViewById(R.id.profile_image);
        lineChart = view.findViewById(R.id.reportingChart);

        setWeeklyDriveSummary();
        RidesLeaderboard();

        spinYear = view.findViewById(R.id.spinYear);
        spinMonth = view.findViewById(R.id.spinMonth);
        String[] str = Constants.frst_name.split(" ");
        Constants.frst_name = str[0];
        tvName.setText(Constants.frst_name);
        List<String> monthList = new ArrayList<String>();
        monthList.add("JAN");
        monthList.add("FEB");
        monthList.add("MAR");
        monthList.add("APR");
        monthList.add("MAY");
        monthList.add("JUN");
        monthList.add("JUL");
        monthList.add("AUG");
        monthList.add("SEPT");
        monthList.add("OCT");
        monthList.add("NOV");
        monthList.add("DEC");
        int indexofmonth = Calendar.getInstance().get(Calendar.MONTH);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, monthList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinMonth.setAdapter(dataAdapter);
        spinMonth.setSelection(indexofmonth);
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 2020; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinYear.setAdapter(adapter);
        spinYear.setSelection(adapter.getCount() - 1);
//        int i = spinYear.getSelectedItemPosition() + 1;

        progressDialog = new BeautifulProgressDialog(getActivity(), BeautifulProgressDialog.withLottie, null);
        progressDialog.setLottieLocation(Constants.lottiePath);
        progressDialog.setLottieLoop(true);
        progressDialog.setLayoutColor(Color.TRANSPARENT);
        progressDialog.setLayoutElevation(0);
        progressDialog.setCancelable(true);
        progressDialog.show();
        riskAlert();
        spinMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (Constants.img_profile == true) {
            try {
                Picasso.get()
                        .load(Constants.BASE_URL + Constants.file + "?referredBy=" + Constants.referredBy + "&sessionId=" + Preference_Details.getLoginSessID() + "&companyId=" + Constants.companyID + "&userId=" + Preference_Details.getUserID() + "&type=" + Constants.type_profile)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(profile_image);
                Constants.img_profile = false;
                //profile_image.setImageURI(Uri.parse(Constants.BASE_URL+Constants.file+"?referredBy="+Constants.referredBy+"&sessionId="+Preference_Details.getLoginSessID()+"&companyId="+Constants.companyID+"&userId="+Preference_Details.getUserID()+"&type="+Constants.type_profile));
            } catch (Exception e) {
                profile_image.setBackground(getActivity().getDrawable(R.drawable.profile_man));
                e.printStackTrace();
            }
        } else {
            try {
                Picasso.get()
                        .load(Constants.BASE_URL + Constants.file + "?referredBy=" + Constants.referredBy + "&sessionId=" + Preference_Details.getLoginSessID() + "&companyId=" + Constants.companyID + "&userId=" + Preference_Details.getUserID() + "&type=" + Constants.type_profile)
                        .into(profile_image);
                //profile_image.setImageURI(Uri.parse(Constants.BASE_URL+Constants.file+"?referredBy="+Constants.referredBy+"&sessionId="+Preference_Details.getLoginSessID()+"&companyId="+Constants.companyID+"&userId="+Preference_Details.getUserID()+"&type="+Constants.type_profile));
            } catch (Exception e) {
                profile_image.setBackground(getActivity().getDrawable(R.drawable.profile_man));
                e.printStackTrace();
            }
        }

        return view;
    }

    private void loadChart() {
        //////////////////LOAD CHART//////////
        restMethods = RestClient.buildHTTPClient();
        int year = 2020 + spinYear.getSelectedItemPosition();
        int month = spinMonth.getSelectedItemPosition() + 1;
        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("companyId", Preference_Details.getCompanyID());
        parameters2.put("divisionId", Preference_Details.getDivisionID());
        parameters2.put("userId", Preference_Details.getUserID());
        parameters2.put("type", Constants.type_GPS_Device_GET);
        parameters2.put("dashboardType", Constants.USER_MONTHLY_DRIVING_SCORE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            parameters2.put("startDateTime", YearMonth.of(year, month).atDay(1) + " " + "00:00:00");
            parameters2.put("endDateTime", YearMonth.of(year, month).atEndOfMonth() + " " + "23:59:59");
        }
        //String ymax, ymin;
        ArrayList<String> xAxisLabel = new ArrayList<>();
        Call<List<DrivingScorePOJO>> call2 = restMethods.getDashboard("ANDROID",
                Preference_Details.getLoginSessID(), parameters2);
        call2.enqueue(new Callback<List<DrivingScorePOJO>>() {
            @Override
            public void onResponse(Call<List<DrivingScorePOJO>> call, Response<List<DrivingScorePOJO>> response) {

                if (response.isSuccessful()) {
                    List<DrivingScorePOJO> resbody = response.body();
                    LineDataSet lineDataSet = new LineDataSet(getLineEntries(0, 0), "");
                    lineDataSet.setDrawFilled(true);
                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(lineDataSet);
                    LineData lineData = new LineData(dataSets);

                    lineChart.invalidate();
                    lineChart.setData(lineData);
                    lineDataSet.setDrawCircles(false);
                    lineDataSet.setDrawFilled(false);
                    lineDataSet.setFillAlpha(85);
                    lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
                    lineDataSet.setDrawValues(false);
                    int light_blue = ContextCompat.getColor(getActivity(), R.color.graphblue);
                    lineDataSet.setColor(light_blue);
                    lineChart.setDrawGridBackground(false);
                    lineChart.animateY(2000);
                    lineChart.setDragEnabled(false);
                    lineChart.setScaleEnabled(false);
                    lineChart.setPinchZoom(true);
                    lineChart.getDescription().setEnabled(false);
                    lineChart.getLegend().setEnabled(true);
                    Legend l = lineChart.getLegend();
                    l.setForm(Legend.LegendForm.EMPTY);
                    lineChart.getAxisRight().setEnabled(false);

                    lineChart.getXAxis().setDrawGridLines(false);
                    lineChart.getAxisRight().setDrawGridLines(false);
                    lineChart.getAxisLeft().setDrawGridLines(true);
                    lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    lineChart.setDrawGridBackground(false);
                    lineChart.getAxisLeft().setAxisLineColor(Color.GRAY);
                    lineChart.getXAxis().setAxisLineColor(Color.WHITE);
                    lineChart.getData().setHighlightEnabled(false);
                    lineChart.invalidate();
                    if (!resbody.isEmpty()) {
                        for (int i = 0; i < resbody.get(0).getDrivingScoreList().size(); i++) {
                            getLineEntries(Math.round(Float.valueOf(String.valueOf(Float.parseFloat(resbody.get(0).getDrivingScoreList().get(i).getRideName().replaceAll("\\D+", ""))))), resbody.get(0).getDrivingScoreList().get(i).getDrivingScore());
                            xAxisLabel.add(String.valueOf(resbody.get(0).getDrivingScoreList().get(i).getRideName()));
                        }


                        int size = resbody.get(0).getDrivingScoreList().size();
                        lineChart.getXAxis().setAxisMinimum(Float.parseFloat(resbody.get(0).getDrivingScoreList().get(0).getRideName().replaceAll("\\D+", "")));
                        lineChart.getXAxis().setAxisMaximum(Float.parseFloat(resbody.get(0).getDrivingScoreList().get(size - 1).getRideName().replaceAll("\\D+", "")));
                        if (resbody.get(0).getDrivingScoreList().size() <= 6) {
                            lineChart.getXAxis().setLabelCount(resbody.get(0).getDrivingScoreList().size());
                        } else {
                            lineChart.getXAxis().setLabelCount(7);
                        }
                        YAxis yAxis = lineChart.getAxisLeft();
                        yAxis.setAxisMaximum(100);
                        yAxis.setAxisMinimum(0);
                        yAxis.setLabelCount(10);
                        yAxis.setTextSize(15);
                        lineChart.getAxisRight().setEnabled(false);
                    } else {
                        lineEntries.clear();
                    }
                    lineChart.invalidate();
                }
            }

            @Override
            public void onFailure(Call<List<DrivingScorePOJO>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Service Unavailable", Toast.LENGTH_LONG).show();
            }
        });

    }

    private ArrayList<Entry> getLineEntries(int x, int y) {
        lineEntries.add(new Entry(x, y));
        Collections.sort(lineEntries, new EntryXComparator());
        lineChart.setEnabled(true);
        return lineEntries;
    }


    private void setWeeklyDriveSummary() {
        restMethods = RestClient.buildHTTPClient();

        Map<String, String> parameters = new HashMap<>();
        parameters.put("companyId", Preference_Details.getCompanyID());
        parameters.put("divisionId", Preference_Details.getDivisionID());
        parameters.put("userId", Preference_Details.getUserID());
        parameters.put("type", Constants.type_GPS_Device_GET);
        parameters.put("dashboardType", Constants.USER_DASHBOARD_TOP_LINE_ITEM);

        Call<List<DriveSummaryPOJO>> call = restMethods.getDashboardValues("ANDROID",
                Preference_Details.getLoginSessID(), parameters);
        call.enqueue(new Callback<List<DriveSummaryPOJO>>() {
            @Override
            public void onResponse(Call<List<DriveSummaryPOJO>> call, Response<List<DriveSummaryPOJO>> response) {
                if (response.isSuccessful()) {

                    if (response.body().get(0).getRideCount() != null) {
                        tvTotalRides.setText(response.body().get(0).getRideCount());
                        tvDrivenKms.setText(response.body().get(0).getDrivenKiloMeter());
                        tvDrivenHours.setText(response.body().get(0).getDrivenHour());
                        tvLatestRide.setText(response.body().get(0).getLastRideDateTime());
                    } else {
                        tvTotalRides.setText("0");
                        tvDrivenKms.setText("0");
                        tvDrivenHours.setText("0");
                        tvLatestRide.setText(response.body().get(0).getLastRideDateTime());
                    }

                }
            }

            @Override
            public void onFailure(Call<List<DriveSummaryPOJO>> call, Throwable t) {
                tvTotalRides.setText("0");
                tvDrivenKms.setText("0");
                tvDrivenHours.setText("0");
                tvLatestRide.setText("0");
                Toast.makeText(getApplicationContext(), "Service Unavailable", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void RidesLeaderboard() {
        restMethods = RestClient.buildHTTPClient();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvRidesLeaderboard.setLayoutManager(layoutManager);
        rvRidesLeaderboard.setNestedScrollingEnabled(false);

        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("companyId", Preference_Details.getCompanyID());
        parameters1.put("userId", Preference_Details.getUserID());
        parameters1.put("type", "SPEEDO_METER_DEVICE");
        parameters1.put("limit", "5");
        parameters1.put("sortOrder", "DESC");
        parameters1.put("queryType", "SPEEDO_METER_DEVICE_AND_DATA_LIST");
        parameters1.put("queryFields", " {\"deviceDataCategory\":\"END_DATA\"}");

        Call<List<RideLeaderboardPOJO>> call1 = restMethods.getQuery("ANDROID",
                Preference_Details.getLoginSessID(), parameters1);
        call1.enqueue(new Callback<List<RideLeaderboardPOJO>>() {
            @Override
            public void onResponse(Call<List<RideLeaderboardPOJO>> call, Response<List<RideLeaderboardPOJO>> response) {
                String score;
                if (response.isSuccessful()) {
                    rideList.add(new rideListItem("RIDES", "RISK ALERT", "SCORE"));

                    List<RideLeaderboardPOJO> resbody = response.body();
                    for (int i = 0; i < resbody.size(); i++) {
                        if (resbody.get(i).getDeviceDataList().get(0).getDeviceDataField().getDrivingScore().contains("-")) {
                            score = "0";
                        } else {
                            score = String.valueOf(Math.round(Float.parseFloat(resbody.get(i).getDeviceDataList().get(0).getDeviceDataField().getDrivingScore())));
                        }
                        rideList.add(new rideListItem(resbody.get(i).getDeviceName(), resbody.get(i).getAlertDataCount(), score));

                    }

                    rideListAdapter = new RideListAdapter(rideList, getActivity());
                    rvRidesLeaderboard.setAdapter(rideListAdapter);

                }
            }

            @Override
            public void onFailure(Call<List<RideLeaderboardPOJO>> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "Service Unavailable", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void riskAlert() {
        List<Integer> graph = new ArrayList<>();
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
        myAdapter = new RiskAlertAdapter(getActivity(), riskAlertDataList);
        rvRiskAlerts.setHasFixedSize(true);
        rvRiskAlerts.setAdapter(myAdapter);
        getAlertListCount();

    }

    private void getAlertListCount() {
        Map<String, String> parameters4 = new HashMap<>();
        parameters4.put("companyId", Constants.companyID);
        parameters4.put("divisionId", Constants.divisionId);
        parameters4.put("type", Constants.type_GPS_Device_GET);
        parameters4.put("dashboardType", Constants.USER_DASHBOARD_RISK_ALERT);
        parameters4.put("userId", Constants.userID);
        Call<List<RiskAlertPOJO>> call4 = restMethods.getRiskAlert("ANDROID",
                Preference_Details.getLoginSessID(), parameters4);
        call4.enqueue(new Callback<List<RiskAlertPOJO>>() {
            @Override
            public void onResponse(Call<List<RiskAlertPOJO>> call, Response<List<RiskAlertPOJO>> response) {

                if (response.isSuccessful()) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    List<RiskAlertPOJO> resbody = response.body();
                    for (int i = 0; i < resbody.size(); i++) {
                        for (int k = 0; k < riskAlertDataList.size(); k++) {
                            if (resbody.get(i).getSubCategory().equals(Util_functions.getSubCategoryValueUpper(Integer.valueOf(riskAlertDataList.get(k).getAlertId())))) {
                                riskAlertDataList.set(k, new RiskAlertDataModel(riskAlertDataList.get(k).getTitle(), resbody.get(i).getRiskTotalValue(), riskAlertDataList.get(k).getImage(), riskAlertDataList.get(i).getAlertId(), resbody.get(i).getRiskValueList()));
                            }
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<RiskAlertPOJO>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Service Unavailable", Toast.LENGTH_LONG).show();
            }
        });
    }

}