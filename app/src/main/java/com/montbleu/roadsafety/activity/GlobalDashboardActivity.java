package com.montbleu.roadsafety.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.montbleu.Retrofit.RestClient;
import com.montbleu.Retrofit.RestMethods;
import com.montbleu.Utils.Constants;
import com.montbleu.Utils.Preference_Details;
import com.montbleu.Utils.ProgressDialog;
import com.montbleu.Utils.Util_functions;
import com.montbleu.interfaces.OnDialogButtonClickListener;
import com.montbleu.model.DashboardResModel;
import com.montbleu.roadsafety.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GlobalDashboardActivity extends AppCompatActivity implements OnDialogButtonClickListener {
    ArrayList<DashboardResModel> gpsGlobalArrayList = new ArrayList<>();
    Float total_rides = 0f, total_km = 0f, total_hr = 0f;
    TextView tot_rides;
    TextView tot_hours;
    TextView tot_km;

    //int gold_ride = 0,silver_ride = 0,bronze_ride = 0,gold_km,gold_hr = 0,silver_km = 0,silver_hr = 0,bronze_km = 0,bronze_hr = 0;

    //    ConstraintSet set = new ConstraintSet();
//    ConstraintLayout.LayoutParams lp;
    ImageView back_button;
    BarChart ride_chart, km_chart, hr_chart;
    BarData ride_barData, km_barData, hr_barData;
    BarDataSet ride_barDataSet, km_barDataSet, hr_barDataSet;
    ArrayList ride_barEntriesArrayList, km_barEntriesArrayList, hr_barEntriesArrayList;

    //    ConstraintLayout layout_golden_rides,layout_silver_rides,layout_bronze_rides,layout_golden_km,layout_silver_km,layout_bronze_km,layout_golden_hr,layout_silver_hr,layout_bronze_hr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_dashboard);
        // initializing variable for bar chart.
        ride_chart = findViewById(R.id.ride_chart);
        km_chart = findViewById(R.id.km_chart);
        hr_chart = findViewById(R.id.hr_chart);
        ride_barEntriesArrayList = new ArrayList<>();
        km_barEntriesArrayList = new ArrayList<>();
        hr_barEntriesArrayList = new ArrayList<>();
        tot_rides = findViewById(R.id.tot_ride);

        back_button = findViewById(R.id.back_button);
        tot_km = findViewById(R.id.tot_km);
        tot_hours = findViewById(R.id.tot_hr);

        if (Util_functions.ConnectivityCheck(GlobalDashboardActivity.this, GlobalDashboardActivity.this).booleanValue()) {
            ProgressDialog.setLottieProgressDialog(GlobalDashboardActivity.this, Constants.lottiePath);
            ProgressDialog.showProgress();
            getGlobalData();

        }

        back_button.setOnClickListener(v -> finish());
        Log.e("time zone", String.valueOf(TimeZone.getAvailableIDs()));


    }

    private void getHourChart(int max_value) {
        YAxis yAxis = hr_chart.getAxisLeft();
        yAxis.setLabelCount(10);
        yAxis.setTextSize(10);
        yAxis.setAxisMinimum(0);
        if (max_value < 10) {
            yAxis.setAxisMaximum(max_value + 5);
        } else {
            yAxis.setAxisMaximum(max_value + 10);
        }
        hr_chart.setDrawGridBackground(false);
        hr_chart.animateY(2000);
        hr_chart.setDragEnabled(false);
        hr_chart.setScaleEnabled(false);
        hr_chart.setPinchZoom(true);
        Bitmap[] starBitmap = new Bitmap[]{BitmapFactory.decodeResource(getResources(), R.drawable.gold_trophy_big),
                BitmapFactory.decodeResource(getResources(), R.drawable.silver_trophy_big),
                BitmapFactory.decodeResource(getResources(), R.drawable.bronze_trophy_big)};
        hr_chart.setRenderer(new ImageBarChartRenderer(hr_chart, hr_chart.getAnimator(), hr_chart.getViewPortHandler(), starBitmap));
        hr_chart.getDescription().setEnabled(false);
        hr_chart.getLegend().setEnabled(true);
        Legend l = hr_chart.getLegend();
        l.setForm(Legend.LegendForm.EMPTY);
        hr_chart.getAxisRight().setEnabled(false);
        hr_chart.getXAxis().setDrawGridLines(false);
        hr_chart.getAxisRight().setDrawGridLines(false);
        hr_chart.getAxisLeft().setDrawGridLines(true);
        hr_chart.setDrawGridBackground(false);
        hr_chart.getAxisLeft().setAxisLineColor(Color.BLACK);
        hr_chart.getXAxis().setAxisLineColor(Color.WHITE);
        hr_barDataSet = new BarDataSet(hr_barEntriesArrayList, "");
        hr_barData = new BarData(hr_barDataSet);
        hr_barData.setBarWidth(1f);
        hr_chart.setData(hr_barData);
        hr_barDataSet.setDrawValues(false);
        hr_barDataSet.setColors(ContextCompat.getColor(GlobalDashboardActivity.this, R.color.ride_green), ContextCompat.getColor(GlobalDashboardActivity.this, R.color.ride_amber), ContextCompat.getColor(GlobalDashboardActivity.this, R.color.ride_red));
        hr_barDataSet.setValueTextColor(Color.BLACK);
        hr_barDataSet.setValueTextSize(13f);
        hr_chart.getDescription().setEnabled(false);
        hr_chart.getData().setHighlightEnabled(false);
        XAxis xAxis = hr_chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setTextSize(8);
        //xAxis.setCenterAxisLabels(true);
        hr_chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getXAxisValues()));
    }

    private void getKmChart(int max_value) {
        //getBarEntries();
        YAxis yAxis = km_chart.getAxisLeft();
        yAxis.setLabelCount(10);
        yAxis.setTextSize(10);
        yAxis.setAxisMinimum(0);
        if (max_value < 10) {
            yAxis.setAxisMaximum(max_value + 5);
        } else {
            yAxis.setAxisMaximum(max_value + 10);
        }
        km_chart.setDrawGridBackground(false);
        km_chart.animateY(2000);
        km_chart.setDragEnabled(false);
        km_chart.setScaleEnabled(false);
        km_chart.setPinchZoom(true);
        Bitmap[] starBitmap = new Bitmap[]{BitmapFactory.decodeResource(getResources(), R.drawable.gold_trophy_big),
                BitmapFactory.decodeResource(getResources(), R.drawable.silver_trophy_big),
                BitmapFactory.decodeResource(getResources(), R.drawable.bronze_trophy_big)};
        km_chart.setRenderer(new ImageBarChartRenderer(km_chart, km_chart.getAnimator(), km_chart.getViewPortHandler(), starBitmap));
        km_chart.getDescription().setEnabled(false);
        km_chart.getLegend().setEnabled(true);
        Legend l = km_chart.getLegend();
        l.setForm(Legend.LegendForm.EMPTY);
        km_chart.getAxisRight().setEnabled(false);
        km_chart.getXAxis().setDrawGridLines(false);
        km_chart.getAxisRight().setDrawGridLines(false);
        km_chart.getAxisLeft().setDrawGridLines(true);
        km_chart.setDrawGridBackground(false);
        km_chart.getAxisLeft().setAxisLineColor(Color.BLACK);
        km_chart.getXAxis().setAxisLineColor(Color.WHITE);
        //km_chart.getXAxis().setSpaceMax(1f);
        km_barDataSet = new BarDataSet(km_barEntriesArrayList, "");
        km_barData = new BarData(km_barDataSet);
        km_barData.setBarWidth(1f);
        km_chart.setData(km_barData);
        km_barDataSet.setDrawValues(false);
        km_barDataSet.setColors(ContextCompat.getColor(GlobalDashboardActivity.this, R.color.ride_green), ContextCompat.getColor(GlobalDashboardActivity.this, R.color.ride_amber), ContextCompat.getColor(GlobalDashboardActivity.this, R.color.ride_red));
        km_barDataSet.setValueTextColor(Color.BLACK);
        km_barDataSet.setValueTextSize(13f);
        km_chart.getDescription().setEnabled(false);
        km_chart.getData().setHighlightEnabled(false);
        XAxis xAxis = km_chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setTextSize(8);
        //xAxis.setCenterAxisLabels(true);
        km_chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getXAxisValues()));
    }

    private void getRideChart(int max_value) {
        //getBarEntries();
        YAxis yAxis = ride_chart.getAxisLeft();
        yAxis.setLabelCount(10);
        yAxis.setTextSize(10);
        yAxis.setAxisMinimum(0);
        if (max_value < 10) {
            yAxis.setAxisMaximum(max_value + 5);
        } else {
            yAxis.setAxisMaximum(max_value + 10);
        }
        ride_chart.setDrawGridBackground(false);
        ride_chart.animateY(2000);
        ride_chart.setDragEnabled(false);
        ride_chart.setScaleEnabled(false);
        ride_chart.setPinchZoom(true);
        Bitmap[] starBitmap = new Bitmap[]{BitmapFactory.decodeResource(getResources(), R.drawable.gold_trophy_big),
                BitmapFactory.decodeResource(getResources(), R.drawable.silver_trophy_big),
                BitmapFactory.decodeResource(getResources(), R.drawable.bronze_trophy_big)};
        ride_chart.setRenderer(new ImageBarChartRenderer(ride_chart, ride_chart.getAnimator(), ride_chart.getViewPortHandler(), starBitmap));
        ride_chart.getDescription().setEnabled(false);
        ride_chart.getLegend().setEnabled(true);
        Legend l = ride_chart.getLegend();
        l.setForm(Legend.LegendForm.EMPTY);
        ride_chart.getAxisRight().setEnabled(false);
        ride_chart.getXAxis().setDrawGridLines(false);
        ride_chart.getAxisRight().setDrawGridLines(false);
        ride_chart.getAxisLeft().setDrawGridLines(true);
        ride_chart.setDrawGridBackground(false);
        ride_chart.getAxisLeft().setAxisLineColor(Color.BLACK);
        ride_chart.getXAxis().setAxisLineColor(Color.WHITE);
        //getBarEntries(0,0);
        ride_barDataSet = new BarDataSet(ride_barEntriesArrayList, "");
        ride_barData = new BarData(ride_barDataSet);
        ride_barData.setBarWidth(1f);
        ride_chart.setData(ride_barData);
        ride_barDataSet.setDrawValues(false);
        ride_barDataSet.setColors(ContextCompat.getColor(GlobalDashboardActivity.this, R.color.ride_green), ContextCompat.getColor(GlobalDashboardActivity.this, R.color.ride_amber), ContextCompat.getColor(GlobalDashboardActivity.this, R.color.ride_red));
        ride_barDataSet.setValueTextColor(Color.BLACK);
        ride_barDataSet.setValueTextSize(13f);
        ride_chart.getDescription().setEnabled(false);
        ride_chart.getData().setHighlightEnabled(false);
        XAxis xAxis = ride_chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setTextSize(8);
        //xAxis.setCenterAxisLabels(true);
        ride_chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getXAxisValues()));
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("Gold");
        xAxis.add("Silver");
        xAxis.add("Bronze");
        return xAxis;
    }

    private void getBarEntries(float x, float y) {
        // creating a new array list
        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.
        ride_barEntriesArrayList.add(new BarEntry(x, y));
    }

    private void getKmBarEntries(float x, float y) {
        // creating a new array list
        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.
        km_barEntriesArrayList.add(new BarEntry(x, y));
    }

    private void getHrBarEntries(float x, float y) {
        // creating a new array list
        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.
        hr_barEntriesArrayList.add(new BarEntry(x, y));
    }


    private String decodeBase64(String coded) {
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

    private void getGlobalData() {
        RestMethods restMethods;
        restMethods = RestClient.buildHTTPClient();
        restMethods.getDashboardResponse(Constants.referredBy, Preference_Details.getLoginSessID(), Preference_Details.getCompanyID(), Preference_Details.getUserID(), Constants.type_GPS_Device_GET, Constants.dashboard_type, 0, 100).enqueue(new Callback<List<DashboardResModel>>() {
            @Override
            public void onResponse(Call<List<DashboardResModel>> call, Response<List<DashboardResModel>> response) {
                try {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            gpsGlobalArrayList.clear();
                            gpsGlobalArrayList.addAll(response.body());
                            for (int i = 0; i < gpsGlobalArrayList.size(); i++) {
                                if (gpsGlobalArrayList.size() > 0) {
                                    try {
                                        total_rides = Float.parseFloat(gpsGlobalArrayList.get(i).getLatestDrivingScore().getYearlyBrozneRide()) + Float.parseFloat(gpsGlobalArrayList.get(i).getLatestDrivingScore().getYearlySilverRide()) + Float.parseFloat(gpsGlobalArrayList.get(i).getLatestDrivingScore().getYearlyGoldRide());
                                        tot_rides.setText(String.valueOf(Math.round(total_rides)));
                                        total_km = Float.parseFloat(gpsGlobalArrayList.get(i).getLatestDrivingScore().getYearlyBrozneKiloMeter()) + Float.parseFloat(gpsGlobalArrayList.get(i).getLatestDrivingScore().getYearlySilverKiloMeter()) + Float.parseFloat(gpsGlobalArrayList.get(i).getLatestDrivingScore().getYearlyGoldKiloMeter());
                                        tot_km.setText(String.valueOf(Math.round(total_km)));
                                        total_hr = Float.parseFloat(gpsGlobalArrayList.get(i).getLatestDrivingScore().getYearlyBrozneHour()) + Float.parseFloat(gpsGlobalArrayList.get(i).getLatestDrivingScore().getYearlySilverHour()) + Float.parseFloat(gpsGlobalArrayList.get(i).getLatestDrivingScore().getYearlyGoldHour());
                                        tot_hours.setText(String.valueOf(Math.round(total_hr)));

                                        getBarEntries(0, Math.round(Float.parseFloat(gpsGlobalArrayList.get(i).getLatestDrivingScore().getYearlyGoldRide())));
                                        getBarEntries(1, Math.round(Float.parseFloat(gpsGlobalArrayList.get(i).getLatestDrivingScore().getYearlySilverRide())));
                                        getBarEntries(2, Math.round(Float.parseFloat(gpsGlobalArrayList.get(i).getLatestDrivingScore().getYearlyBrozneRide())));
                                        getRideChart(Math.round(total_rides));

                                        getKmBarEntries(0, Math.round(Float.parseFloat(gpsGlobalArrayList.get(i).getLatestDrivingScore().getYearlyGoldKiloMeter())));
                                        getKmBarEntries(1, Math.round(Float.parseFloat(gpsGlobalArrayList.get(i).getLatestDrivingScore().getYearlySilverKiloMeter())));
                                        getKmBarEntries(2, Math.round(Float.parseFloat(gpsGlobalArrayList.get(i).getLatestDrivingScore().getYearlyBrozneKiloMeter())));
                                        getKmChart(Math.round(total_km));

                                        getHrBarEntries(0, Math.round(Float.parseFloat(gpsGlobalArrayList.get(i).getLatestDrivingScore().getYearlyGoldHour())));
                                        getHrBarEntries(1, Math.round(Float.parseFloat(gpsGlobalArrayList.get(i).getLatestDrivingScore().getYearlySilverHour())));
                                        getHrBarEntries(2, Math.round(Float.parseFloat(gpsGlobalArrayList.get(i).getLatestDrivingScore().getYearlyBrozneHour())));
                                        getHourChart(Math.round(total_hr));

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            ProgressDialog.dismissProgress();
                        } else if (response.code() == 412) {
                            try {
                                String Error = response.errorBody().string();
                                JSONObject obj = new JSONObject(Error);
                                JSONArray jsonArray1 = obj.getJSONArray("messages");
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    JSONObject object1 = jsonArray1.getJSONObject(0);
                                    Util_functions.ShowAlertError(GlobalDashboardActivity.this, object1.getString("message"), "OK", GlobalDashboardActivity.this);
                                }
                                //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.resp_empty),true);
                                ProgressDialog.dismissProgress();
                            } catch (Exception e) {
                                ProgressDialog.dismissProgress();
                                e.printStackTrace();
                            }

                        } else {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            String text = obj.getString("message");
                            Util_functions.ShowAlertError(GlobalDashboardActivity.this, text, "OK", GlobalDashboardActivity.this);
                            //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.resp_empty),true);
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
            public void onFailure(Call<List<DashboardResModel>> call, Throwable t) {
                if (t.toString().contains("Unable to resolve host")) {
                    Util_functions.ShowValidateAlertError(GlobalDashboardActivity.this, getString(R.string.check_internet), "OK");
                }
                Util_functions.ShowAlertError(GlobalDashboardActivity.this, t.getMessage(), "OK", GlobalDashboardActivity.this);
                t.printStackTrace();
                ProgressDialog.dismissProgress();
            }
        });
    }

    public class ImageBarChartRenderer extends BarChartRenderer {

        private Bitmap[] imageToRender;

        public ImageBarChartRenderer(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler, Bitmap[] imageToRender) {
            super(chart, animator, viewPortHandler);
            this.imageToRender = imageToRender;
        }

        @Override
        protected void drawDataSet(Canvas c, IBarDataSet dataSet, int index) {
            super.drawDataSet(c, dataSet, index);
            drawBarImages(c, dataSet, index);

        }

        protected void drawBarImages(Canvas c, IBarDataSet dataSet, int index) {
            BarBuffer buffer = mBarBuffers[index];

            float left; //avoid allocation inside loop
            float right;
            float top;
            float bottom;


            for (int j = 0; j < buffer.buffer.length * mAnimator.getPhaseX(); j += 4) {
                left = buffer.buffer[j];
                right = buffer.buffer[j + 2];
                top = buffer.buffer[j + 1];
                bottom = buffer.buffer[j + 3];

                float x = (left + right) / 2f;

                if (!mViewPortHandler.isInBoundsRight(x))
                    break;

                if (!mViewPortHandler.isInBoundsY(top)
                        || !mViewPortHandler.isInBoundsLeft(x))
                    continue;

                BarEntry entry = dataSet.getEntryForIndex(j / 4);
                float val = entry.getY();
                final Bitmap scaledBarImage = scaleBarImage(buffer, imageToRender[j / 4]);

                int starWidth = scaledBarImage.getWidth();
                int starOffset = starWidth / 2;

                int starHeight = scaledBarImage.getHeight();
                int starHeightOffset = starHeight / 2;

                drawImage(c, scaledBarImage, x - starOffset, top - 80);
            }
        }

        private Bitmap scaleBarImage(BarBuffer buffer, Bitmap image) {
            float firstLeft = buffer.buffer[0];
            float firstRight = buffer.buffer[2];
            int firstWidth = (int) Math.ceil(firstRight - firstLeft);
            return Bitmap.createScaledBitmap(image, firstWidth, firstWidth, false);
        }

        protected void drawImage(Canvas c, Bitmap image, float x, float y) {
            if (image != null) {
                c.drawBitmap(image, x, y, null);
            }
        }
    }

    @Override
    public void onPositiveButtonClicked(AlertDialog alertDialog) {

    }

    @Override
    public void onNegativeButtonClicked() {

    }
}