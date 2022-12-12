package com.montbleu.roadsafety.activity;

import static com.montbleu.Utils.Constants.scoreListModelArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.anastr.speedviewlib.Speedometer;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;
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
import com.here.android.mpa.mapping.MapView;
import com.here.android.mpa.routing.RouteWaypoint;
import com.here.android.mpa.routing.RoutingError;
import com.montbleu.Retrofit.RestClient;
import com.montbleu.Retrofit.RestMethods;
import com.montbleu.Utils.Constants;
import com.montbleu.Utils.Preference_Details;
import com.montbleu.Utils.ProgressDialog;
import com.montbleu.Utils.Util_functions;
import com.montbleu.adapter.AlertAdapter;
import com.montbleu.adapter.RiskAlertListAdapter;
import com.montbleu.interfaces.OnDialogButtonClickListener;
import com.montbleu.model.AlertData;
import com.montbleu.model.AlertsModel;
import com.montbleu.model.GPSGETDeviceResp;
import com.montbleu.model.GPSGetAlertListResp;
import com.montbleu.model.Risk_alert_model;
import com.montbleu.roadsafety.BeautifulProgressDialog;
import com.montbleu.roadsafety.R;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.Unbinder;
import eu.amirs.JSON;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RidesDetailPageActivity extends AppCompatActivity implements View.OnClickListener,MediaPlayer.OnCompletionListener, OnDialogButtonClickListener {
    private Unbinder viewBinder;
    @BindView(R.id.alerts_recycler_View)
    RecyclerView recyclerViewAlerts;
    AlertAdapter alertsAdapter;
    List<AlertsModel> alertsArrayList = new ArrayList<>();
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    ImageView back_button,img_trophy,img_pdf;
    Speedometer speedView;
    LineChart lineChart;
    TextView current_date,txt_trophy;
    XAxis xAxis;
    ArrayList<Entry> lineEntries;
    ArrayList<Entry> lineEntries1;
    ArrayList<Entry> lineEntries2;
    TextView abt_antic_value,abt_self_conf_val,abt_skill_value,ride1_time,ride1_dist,ride1_min,text_content,night_percent,day_percent;
    DecimalFormat REAL_FORMATTER = new DecimalFormat("00.00");
    TextView driveScore;
    ImageView time1_img,dist1_img,min1_img;
    BeautifulProgressDialog progressDialog;
    RestMethods restMethods;
    ArrayList<GPSGETDeviceResp> gpsgetDeviceRespArrayList = new ArrayList<>();
    ArrayList<GPSGetAlertListResp.DeviceDataList> deviceDataListArrayList = new ArrayList<>();
    ArrayList<AlertData> alertDataArrayList = new ArrayList<>();
    ArrayList<GPSGETDeviceResp.DeviceDataList> graphArrayList = new ArrayList<>();
    LineDataSet lineDataSet,lineDataSet1,lineDataSet2;
    CardView export_pdf;
    String rideID = "",rideNum = "";

    TextToSpeech textToSpeech;
    double totdiff=0f;
    Double diffKm=0.0;
    Integer totKm=0;
    int date=0;
    int[] tracks = new int[3];
    NestedScrollView scroll_rides;
    int currentTrack = 0;
    TextView no_chart;
    private MediaPlayer mediaPlayer,ring = null;
    AnimatedPieView animatedPieView;
    AnimatedPieViewConfig config;
    TextView abt_alert;
    //map
    private Map map = null;
    private AndroidXMapFragment mapFragment = null;
    MapView mapView;
    private FTCRRouter.CancellableTask m_routeTask;
    //private GeoCoordinate[] m_fakeRoute;
    ArrayList<GeoCoordinate> m_fakeRoute = new ArrayList<>();
    private GeoCoordinate m_startPoint;
    private GeoCoordinate m_endPoint;
    private FTCRMapRoute m_mapRoute;
    private FTCRRouter m_router;
    List<RouteWaypoint> routePoints = new ArrayList<>();
    ImageView image1;
    TextView Ride_time_val,km_val,min_val;
    RecyclerView risk_list;
    TextView no_record;
    List<Risk_alert_model> risk_arrayList = new ArrayList<>();


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_rides_detail_page);
        initial();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAlertList();

    }

    private void initial() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
/*


         abt_alert = (TextView) findViewById(R.id.abt_alert);
        image1 = findViewById(R.id.image);
        Ride_time_val = findViewById(R.id.Ride_time_val);
        km_val = findViewById(R.id.km_val);
        min_val = findViewById(R.id.min_val);
        risk_list = findViewById(R.id.risk_list);
        no_record = findViewById(R.id.no_record);
        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        risk_list.setLayoutManager(RecyclerViewLayoutManager);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mapFragment = (AndroidXMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);



        export_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  Bitmap bitScroll = getBitmapFromView(scroll_rides, scroll_rides.getChildAt(0).getHeight(), scroll_rides.getChildAt(0).getWidth());
                // Bitmap bitScroll = getBitmapFromView(scroll_rides);

                ExportPDF(bitScroll);
            }
        });
        text_content=findViewById(R.id.text_content);
        rideID = getIntent().getStringExtra("RideID");
        rideNum = getIntent().getStringExtra("RideNum");
        lineChart = findViewById(R.id.graph);
        xAxis = lineChart.getXAxis();
        xAxis.setTextSize(15);
        //xAxis.setDrawAxisLine(true);
        // divided by digits
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // DecimalFormat form = new DecimalFormat("#.##");
        try {
            if(!scoreListModelArrayList.isEmpty()) {
                */
        /*

         */
/*

                config=new AnimatedPieViewConfig();
                if (Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getUrbanPercent()))==0&&Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getRuralPercent()))==0&&Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getUrbanPercent()))==0){
                    animatedPieView.setVisibility(View.INVISIBLE);
                    no_chart.setVisibility(View.VISIBLE);
                } else {
                    animatedPieView.setVisibility(View.VISIBLE);
                    no_chart.setVisibility(View.INVISIBLE);
                }
                config.addData(new SimplePieInfo(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getHighwayPercent())),Color.parseColor("#A8A6A3"),String.valueOf(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getHighwayPercent()))+"%")));
                config.addData(new SimplePieInfo(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getUrbanPercent())),Color.parseColor("#6098C8"),String.valueOf(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getUrbanPercent()))+"%")));
                config.addData(new SimplePieInfo(Math.round(Float.parseFloat(scoreListMfgetEntriesodelArrayList.get(0).getRuralPercent())),Color.parseColor("#FD7C21"),String.valueOf(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getRuralPercent()))+"%")));
                config.duration(000);
                config.strokeMode(false);
                config.floatExpandAngle(0);
                config.drawText(true);
                config.textSize(35F);
                config.autoSize(true);
                config.animOnTouch(false);
                config.canTouch(false);
                config.floatShadowRadius(0);
                config.floatExpandSize(0);
                config.strokeWidth(20);
                config.textGravity(AnimatedPieViewConfig.ALIGN);
                animatedPieView.applyConfig(config);
                animatedPieView.start();

                day_percent.setText(String.valueOf(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDayPercentage())))+"%");
                night_percent.setText(String.valueOf(100-Math.round(Float.valueOf(scoreListModelArrayList.get(0).getDayPercentage())))+"%");

                if (!scoreListModelArrayList.get(0).getDate_time().isEmpty()) {
                    getDateCurrentTimeZone(scoreListModelArrayList.get(0).getDate_time());
                    Calendar c = Calendar.getInstance();
                    int date1 = c.get(Calendar.DATE);
                    if (date == date1) {

                    } else {

                    }
                } else {
                    Ride_time_val.setText("00:00");
                }


                if (scoreListModelArrayList.get(0).getTotkms().equals("0.0") || scoreListModelArrayList.get(0).getTotkms().equals("") || scoreListModelArrayList.get(0).getTotkms().contains("-1.0") || scoreListModelArrayList.get(0).getTotkms().contains("-0.0")) {
                    km_val.setText("00.00 KM");
                } else {
                    km_val.setText(REAL_FORMATTER.format(Double.parseDouble(scoreListModelArrayList.get(0).getTotkms())) + " " + "KM");
                    //    xAxis.setValueFormatter(new LargeValueFormatter());
                }

                if (scoreListModelArrayList.get(0).getTotTime().equals("0.0") || scoreListModelArrayList.get(0).getTotTime().equals("") || scoreListModelArrayList.get(0).getTotTime().contains("-1.0") || scoreListModelArrayList.get(0).getTotTime().contains("-0.0")) {
                    min_val.setText("00:00 Mins");
                } else {
                    min_val.setText(REAL_FORMATTER.format(Double.parseDouble(scoreListModelArrayList.get(0).getTotTime())) + " " + "Mins");
                }

                if (scoreListModelArrayList.get(0).getAnticipate().contains("-1.0") || scoreListModelArrayList.get(0).getAnticipate().equals("0.0") || scoreListModelArrayList.get(0).getAnticipate().isEmpty()) {
                    //     String FormattedText=form.format(scoreListModelArrayList.get(0).getAnticipate());
               //     abt_antic_value.setTextColor(getResources().getColor(R.color.ride_red));
                    abt_antic_value.setText("0");
                } else {
                    if (Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getAnticipate())) <= 50) {
               //         abt_antic_value.setTextColor(getResources().getColor(R.color.ride_red));
                        abt_antic_value.setText(String.valueOf(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getAnticipate()))));
                    } else if (Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getAnticipate())) <= 80 && Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getAnticipate())) >= 51) {
               //         abt_antic_value.setTextColor(getResources().getColor(R.color.ride_amber));
                        abt_antic_value.setText(String.valueOf(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getAnticipate()))));
                    } else if (Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getAnticipate())) >= 81 && Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getAnticipate())) <= 100) {
               //         abt_antic_value.setTextColor(getResources().getColor(R.color.ride_green));
                        abt_antic_value.setText(String.valueOf(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getAnticipate()))));
                    }
                }
                if (!scoreListModelArrayList.get(0).getDate_time().isEmpty()) {
                    getDateCurrentTimeZone(scoreListModelArrayList.get(0).getDate_time());
                    Calendar c = Calendar.getInstance();
                    int date1 = c.get(Calendar.DATE);
                    if (date == date1) {

                    } else {

                    }
                } else {
                    current_date.setText("0");
                }
                if (scoreListModelArrayList.get(0).getSelfConfident().contains("-1.0") || scoreListModelArrayList.get(0).getSelfConfident().equals("0.0") || scoreListModelArrayList.get(0).getSelfConfident().isEmpty()) {
                    //    String FormattedText=form.format(scoreListModelArrayList.get(0).getSelfConfident());
               //     abt_self_conf_val.setTextColor(getResources().getColor(R.color.ride_red));
                    abt_self_conf_val.setText("0");
                } else {
                    if (Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getSelfConfident())) <= 50) {
               //         abt_self_conf_val.setTextColor(getResources().getColor(R.color.ride_red));
                        abt_self_conf_val.setText(String.valueOf(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getSelfConfident()))));
                    } else if (Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getSelfConfident())) <= 80 && Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getAnticipate())) >= 51) {
               //         abt_self_conf_val.setTextColor(getResources().getColor(R.color.ride_amber));
                        abt_self_conf_val.setText(String.valueOf(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getSelfConfident()))));
                    } else if (Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getSelfConfident())) >= 81 && Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getAnticipate())) <= 100) {
               //         abt_self_conf_val.setTextColor(getResources().getColor(R.color.ride_green));
                        abt_self_conf_val.setText(String.valueOf(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getSelfConfident()))));
                    }
                }
                if (scoreListModelArrayList.get(0).getDrivingSkill().contains("-1.0") || scoreListModelArrayList.get(0).getDrivingSkill().equals("0.0") || scoreListModelArrayList.get(0).getDrivingSkill().isEmpty()) {
                    // String FormattedText=form.format(scoreListModelArrayList.get(0).getDrivingSkill());
               //     abt_skill_value.setTextColor(getResources().getColor(R.color.ride_red));
                    abt_skill_value.setText("0");
                } else {
                    if (Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDrivingSkill())) <= 50) {
               //         abt_skill_value.setTextColor(getResources().getColor(R.color.ride_red));
                        abt_skill_value.setText(String.valueOf(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDrivingSkill()))));
                    } else if (Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDrivingSkill())) <= 80 && Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDrivingSkill())) >= 51) {
               //         abt_skill_value.setTextColor(getResources().getColor(R.color.ride_amber));
                        abt_skill_value.setText(String.valueOf(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDrivingSkill()))));
                    } else if (Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDrivingSkill())) >= 81 && Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDrivingSkill())) <= 100) {
              //          abt_skill_value.setTextColor(getResources().getColor(R.color.ride_green));
                        abt_skill_value.setText(String.valueOf(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDrivingSkill()))));
                    }
                }
                if (scoreListModelArrayList.get(0).getTotkms().equals("0.0") || scoreListModelArrayList.get(0).getTotkms().equals("") || scoreListModelArrayList.get(0).getTotkms().contains("-1.0") || scoreListModelArrayList.get(0).getTotkms().contains("-0.0")) {
                    ride1_dist.setText("00.00 KM");
                } else {
                    ride1_dist.setText(REAL_FORMATTER.format(Double.parseDouble(scoreListModelArrayList.get(0).getTotkms())) + " " + "KM");
                    xAxis.setAxisMaximum(10000);
                    xAxis.setAxisMinimum(0);
                    lineChart.setVisibleXRange(0, Float.parseFloat(scoreListModelArrayList.get(0).getTotkms()));
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
                    //    xAxis.setValueFormatter(new LargeValueFormatter());
                }
                if (scoreListModelArrayList.get(0).getTotTime().equals("0.0") || scoreListModelArrayList.get(0).getTotTime().equals("") || scoreListModelArrayList.get(0).getTotTime().contains("-1.0") || scoreListModelArrayList.get(0).getTotTime().contains("-0.0")) {
                    ride1_min.setText("00:00 Mins");
                } else {
                    ride1_min.setText(REAL_FORMATTER.format(Double.parseDouble(scoreListModelArrayList.get(0).getTotTime())) + " " + "Mins");
                }


            }else{
                startActivity(new Intent(RidesDetailPageActivity.this,RoadStartPageActivity.class));
                finish();
            }


        }catch (Exception e){
            e.printStackTrace();
            //Toast.makeText(RidesDetailPageActivity.this,"Error Occur", Toast.LENGTH_SHORT).show();
        }
        back_button.setOnClickListener(this);
        Calendar c = Calendar.getInstance();
        String[]monthName={"Jan","Feb","Mar", "Apr", "May", "June", "July",
                "Aug", "Sep", "Oct", "Nov",
                "Dec"};
      */


        //getBitmapFromView(mapFragment.getView());

    }


    public Bitmap getBitmapFromView(View view) {
        view.measure(View.MeasureSpec.EXACTLY, View.MeasureSpec.EXACTLY);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);
        view.setDrawingCacheEnabled(false);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
    }

    private void ExportPDF(Bitmap bitmap) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        long timeInMili = calendar .getTimeInMillis();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh.mm aa");


        Log.e("ImageSave", "Saveimage");
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(scroll_rides.getChildAt(0).getWidth(),scroll_rides.getChildAt(0).getHeight(),1).create();
        PdfDocument.Page page = pdfDocument.startPage(myPageInfo);
        page.getCanvas().drawBitmap(bitmap,0,0, null);
        pdfDocument.finishPage(page);

        String pdfFile = Constants.DEMO_WORKING_PATHH+"/"+rideNum+" - "+format.format(date)+".pdf";
        File myPDFFile = new File(pdfFile);

        try {
            pdfDocument.writeTo(new FileOutputStream(myPDFFile));
            Util_functions.ShowValidateAlertError(RidesDetailPageActivity.this,"PDF Export Successfully\n\nGoto Documents and Access The File","OK");
        } catch (IOException e) {
            e.printStackTrace();
            Util_functions.ShowValidateAlertError(RidesDetailPageActivity.this,"Something Went Wrong","OK");
        }

        pdfDocument.close();

    }
    private Bitmap getBitmapFromView(View view, int height, int width) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        view.setDrawingCacheEnabled(false);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.BLACK);
        //img_pdf.setImageBitmap(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    private void createAlertList() {
        if(!scoreListModelArrayList.isEmpty()){
            if (scoreListModelArrayList.get(0).getDriveScore().contains("-1.0")||scoreListModelArrayList.get(0).getDriveScore().equals("0.0")||scoreListModelArrayList.get(0).getDriveScore().isEmpty()) {
                txt_trophy.setVisibility(View.INVISIBLE);
                img_trophy.setVisibility(View.INVISIBLE);
                driveScore.setText("0");
                speedView.setSpeedAt(0);
                text_content.setText("Your trip is too short to calculate the scores");
                if (Preference_Details.getVoice()==true){
//                    txt_to_speech("Score not available. Travel was too short");
                    ring= MediaPlayer.create(RidesDetailPageActivity.this
                            , R.raw.scorenotavailabe_travelwas_tooshort);
                    ring.start();
                }
            } else {

                if (Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDriveScore()))>=93 && Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDriveScore()))<=95){
                    txt_trophy.setVisibility(View.VISIBLE);
                    img_trophy.setVisibility(View.VISIBLE);
                    txt_trophy.setText("Bronze");
                    img_trophy.setBackground(getResources().getDrawable(R.drawable.bronze_trophy_big));
                }else if (Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDriveScore()))>=96 && Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDriveScore()))<=98){
                    txt_trophy.setVisibility(View.VISIBLE);
                    img_trophy.setVisibility(View.VISIBLE);
                    txt_trophy.setText("Silver");
                    img_trophy.setBackground(getResources().getDrawable(R.drawable.silver_trophy_big));
                }else if (Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDriveScore()))>=99 && Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDriveScore()))<=100){
                    txt_trophy.setVisibility(View.VISIBLE);
                    img_trophy.setVisibility(View.VISIBLE);
                    txt_trophy.setText("Gold");
                    img_trophy.setBackground(getResources().getDrawable(R.drawable.gold_trophy_big));
                }else {
                    txt_trophy.setVisibility(View.INVISIBLE);
                    img_trophy.setVisibility(View.INVISIBLE);
                }

                if(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDriveScore()))<95){
                    String score="score_"+Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDriveScore()));
                    if (Preference_Details.getVoice()==true) {
                        tracks[0] = R.raw.your_drivingscore_is;
                        tracks[1] = getApplicationContext().getResources().getIdentifier(score, "raw", getApplicationContext().getPackageName());
                        tracks[2] = R.raw.trytofollow_safealert;
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), tracks[currentTrack]);
                        mediaPlayer.setOnCompletionListener(RidesDetailPageActivity.this);
                        mediaPlayer.start();
                        //txt_to_speech("your driving score is" + String.valueOf(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDriveScore()))) + ". try to follow better road drive it safe alerts");
                    }
                }
                else if(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDriveScore()))>=95) {
                    String score="score_"+Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDriveScore()));
                    if (Preference_Details.getVoice()==true) {
                        tracks[0] = R.raw.your_drivingscore_is;
                        tracks[1] = getApplicationContext().getResources().getIdentifier(score, "raw", getApplicationContext().getPackageName());
                        tracks[2] = R.raw.excellent_driving;
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), tracks[currentTrack]);
                        mediaPlayer.setOnCompletionListener(RidesDetailPageActivity.this);
                        mediaPlayer.start();
                        //txt_to_speech("your driving score is" + String.valueOf(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDriveScore()))) + ". excellent driving");
                    }
                }
                if (Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDriveScore()))<=50){
                    driveScore.setText(String.valueOf(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDriveScore()))));
                    speedView.setSpeedAt(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDriveScore())));
                    text_content.setText("Your driving is risky. You have good driving practice and a normal self-confidence. You have probably taken some bad habits to urgently fix to lower your risk.");
                }
                else if (Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDriveScore()))<=80&&Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDriveScore()))>=51){
                    driveScore.setText(String.valueOf(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDriveScore()))));
                    speedView.setSpeedAt(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDriveScore())));
                    text_content.setText("You are surprised from time to time by the singularities of the road poorly anticipated. Yet you have good driving practice and a normal self-confidence. Learn how to focus your attention on key places that correspond to alerts and you will lower your risk.");
                }
                else if (Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDriveScore()))>=81&&Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDriveScore()))<=100){
                    driveScore.setText(String.valueOf(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDriveScore()))));
                    speedView.setSpeedAt(Math.round(Float.parseFloat(scoreListModelArrayList.get(0).getDriveScore())));
                    text_content.setText("You drive safely. You have excellent driving practice and you have a nature very cautious");
                }

            }

        }

        setupAlert();
        getAlertListCount();
        mapFragment.init(error -> {
            if (error == OnEngineInitListener.Error.NONE) {
                m_router = new FTCRRouter();
                map = mapFragment.getMap();
                map.setCenter(new GeoCoordinate(11.0168, 76.9558, 0.0), Map.Animation.NONE);
                map.setZoomLevel(11);

                //map.setMapScheme(Map.Scheme.HYBRID_DAY);
            }else {
                Toast.makeText(RidesDetailPageActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setupAlert() {
        alertDataArrayList.clear();
        alertDataArrayList.add(new AlertData("Animal Crossing","0", R.drawable.animal_cross,"2"));
        alertDataArrayList.add(new AlertData("Caution","0", R.drawable.caution,"3"));
        alertDataArrayList.add(new AlertData("Curves","0", R.drawable.curves,"5"));
        alertDataArrayList.add(new AlertData("Force Acceleration","0", R.drawable.force_acceleration,"32"));
        alertDataArrayList.add(new AlertData("Hill","0", R.drawable.hill,"6"));
        alertDataArrayList.add(new AlertData("Hill Downwards","0", R.drawable.hill_downwards,"7"));
        alertDataArrayList.add(new AlertData("Hill Upwards","0", R.drawable.hill_upwards,"8"));
        alertDataArrayList.add(new AlertData("Icy Conditions","0", R.drawable.icy,"22"));
        alertDataArrayList.add(new AlertData("Intersection","0", R.drawable.intersection,"10"));
        alertDataArrayList.add(new AlertData("Lane Merge","0", R.drawable.lane_merge,"11"));
        alertDataArrayList.add(new AlertData("Low Gear Area","0", R.drawable.low_gear,"12"));
        alertDataArrayList.add(new AlertData("Mobile Use","0", R.drawable.mobile_use,"29"));
        alertDataArrayList.add(new AlertData("Narrow Road","0", R.drawable.narrow_road,"13"));
        alertDataArrayList.add(new AlertData("No Overtaking","0", R.drawable.no_overtaking,"14"));
        alertDataArrayList.add(new AlertData("Over Speed","0", R.drawable.over_speed,"30"));
        alertDataArrayList.add(new AlertData("Pedestrian Crossing","0", R.drawable.pedestrian_crossing,"16"));
        alertDataArrayList.add(new AlertData("Priority","0", R.drawable.priority,"17"));
        alertDataArrayList.add(new AlertData("Railway Crossing","0", R.drawable.railway_cross,"19"));
        alertDataArrayList.add(new AlertData("Risk Of Grounding","0", R.drawable.risk_of_grounding,"20"));
        alertDataArrayList.add(new AlertData("School Zone","0", R.drawable.school_zone,"21"));
        alertDataArrayList.add(new AlertData("Slippery Roads","0", R.drawable.slippery_road,"22"));
        alertDataArrayList.add(new AlertData("Stop Sign","0", R.drawable.stop,"23"));
        alertDataArrayList.add(new AlertData("Sudden Braking","0", R.drawable.sudden_breaking,"31"));
        alertDataArrayList.add(new AlertData("Traffic Light","0", R.drawable.traffic_light,"24"));
        alertDataArrayList.add(new AlertData("Wind","0", R.drawable.wind,"22"));
        alertDataArrayList.add(new AlertData("Winding Road","0", R.drawable.winding_road,"27"));
        alertDataArrayList.add(new AlertData("Yield","0", R.drawable.yield,"28"));
        alertsAdapter = new AlertAdapter(alertDataArrayList,rideID,RidesDetailPageActivity.this);
        //  int spacingInPixels = 20;
        recyclerViewAlerts.setHasFixedSize(true);
        recyclerViewAlerts.setAdapter(alertsAdapter);
    }




    public void getDateCurrentTimeZone(String timestamp) {
        try{

            SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date newDate=spf.parse(timestamp);
            spf= new SimpleDateFormat("dd MMM yyyy hh:mm:ss a");
            timestamp = spf.format(newDate);

            // String  datetimeString = DateFormat.format("dd MMM yyyy hh:mm:ss a", new Date(timestamp)).toString();
            String timeString = timestamp.substring(12,17);
            String dateString1 = timestamp.substring(0,11);
            String t2 = timestamp.substring(20,23);
            Log.d("data=e :",timeString+" "+dateString1+" "+t2);
            current_date.setText(dateString1);
            Ride_time_val.setText(Html.fromHtml("<b>" + timeString + "</b>")+t2.toUpperCase(Locale.getDefault()));

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAlertListCount() {
        restMethods = RestClient.buildHTTPClient();
        gpsgetDeviceRespArrayList.clear();
        restMethods.GETAlertCountResponse(Constants.referredBy, Preference_Details.getLoginSessID(),Constants.companyID,Preference_Details.getUserID(),rideID,Constants.type_GPS_Device_GET,Constants.gps_ALERT_COUNT).enqueue(new Callback<List<GPSGetAlertListResp>>() {
            @Override
            public void onResponse(@NonNull Call<List<GPSGetAlertListResp>> call, @NonNull Response<List<GPSGetAlertListResp>> response) {
                Log.e("status", "" + response.body());
                try {
                    int recordsCount = 0;
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            //  deviceDataListArrayList.clear();
                            for(int i=0;i<response.body().size();i++){
                                for (int j=0;j<response.body().get(i).getDeviceDataList().size();j++){
                                    if (!response.body().get(i).getDeviceDataList().get(j).getSubCategory().isEmpty()) {
                                        for(int k=0;k<alertDataArrayList.size();k++){
                                            if (response.body().get(i).getDeviceDataList().get(j).getSubCategory().equals(Util_functions.getSubCategoryValueUpper(Integer.parseInt(alertDataArrayList.get(k).getAlertId())))) {
                                                alertDataArrayList.set(k,new AlertData(alertDataArrayList.get(k).getAlertName(),response.body().get(i).getDeviceDataList().get(k).getNoOfRecords(),alertDataArrayList.get(k).getAlertTypeImg(),alertDataArrayList.get(k).getAlertId()));
                                                recordsCount = recordsCount+Integer.parseInt(response.body().get(i).getDeviceDataList().get(j).getNoOfRecords());
                                            }
                                        }
                                        alertsAdapter.notifyDataSetChanged();
                                    }

                                }
                            }
                            if (recordsCount==0) {
                                abt_alert.setText(getString(R.string.label_alert)+" - "+recordsCount);
                            } else {
                                abt_alert.setText(getString(R.string.label_alert)+" - "+recordsCount);
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
                if (t.toString().contains("Unable to resolve host")){
                    Util_functions.ShowValidateAlertError(RidesDetailPageActivity.this,getString(R.string.check_internet),"OK");
                }
                Util_functions.ShowAlertError(RidesDetailPageActivity.this,t.getMessage(),"OK",RidesDetailPageActivity.this);
                t.printStackTrace();
                ProgressDialog.dismissProgress();
            }
        });
    }


    public void txt_to_speech(String str) {
//speeech for text
        textToSpeech = new TextToSpeech(getApplicationContext(), i -> {
            if (i == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.UK);
                textToSpeech.setPitch(0.7f);
                textToSpeech.setSpeechRate(1f);
                textToSpeech.speak(str, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });

    }

    private void getAlertList(){
        JSON generatedJsonObject = JSON.create(
                JSON.dic(
                        "deviceDataCategory",  getString(R.string.gps_START_DATA)+","+getString(R.string.gps_ALERT_DATA)+","+getString(R.string.gps_END_DATA)
                )
        );
        restMethods = RestClient.buildHTTPClient();
        restMethods.GETgraphList(Constants.referredBy, Preference_Details.getLoginSessID(),Constants.companyID,Preference_Details.getUserID(),rideID,Constants.type_GPS_Device_GET,Constants.gps_DEVICE_AND_DATA_LIST,generatedJsonObject.toString()).enqueue(new Callback<List<GPSGetAlertListResp>>() {
            @Override
            public void onResponse(@NonNull Call<List<GPSGetAlertListResp>> call, @NonNull Response<List<GPSGetAlertListResp>> response) {
                try {
                    if (response.code() == 200){
                        risk_arrayList.clear();
                        for(int i=0;i<response.body().size();i++){
                            for (int j=0;j<response.body().get(i).getDeviceDataList().size();j++) {
                                String ps_sl_val = "";
                                if(Double.parseDouble(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLatitude()) != 0.0 && Double.parseDouble(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLongitude())!= 0.0){
                                    routePoints.add(new RouteWaypoint(new GeoCoordinate(Double.parseDouble(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLatitude()), Double.parseDouble(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLongitude()))));
                                }
                                if (response.body().get(i).getDeviceDataList().get(j).getCategory().contains(getString(R.string.gps_START_DATA))){
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
                                            response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLongitude(),ps_sl_val,
                                            response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getRisk()));
                                    // String speed, String zipcode, String km_distance, String time_hh_mm, String alert_val, String category, String lat, String lng
                                }else if (response.body().get(i).getDeviceDataList().get(j).getCategory().contains(getString(R.string.gps_END_DATA))){
                                    setAlertImage("img_finish", Double.parseDouble(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLatitude()), Double.parseDouble(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLongitude()));
                                    m_endPoint = new GeoCoordinate(Double.parseDouble(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLatitude()), Double.parseDouble(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLongitude()));
                                    risk_arrayList.add(new Risk_alert_model(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getSpeed(),
                                            response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getZipCode(),
                                            response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getAlertKiloMeter(),
                                            response.body().get(i).getDeviceDataList().get(j).getCreatedAtToTimeZone(),
                                            response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getAlertValue(),
                                            response.body().get(i).getDeviceDataList().get(j).getCategory(),
                                            response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLatitude(),
                                            response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLongitude(),ps_sl_val,
                                            response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getRisk()));
                                }else {
                                    if(response.body().get(i).getDeviceDataList().get(j).getSubCategory().contains("SUDDEN_BRAKING") || response.body().get(i).getDeviceDataList().get(j).getSubCategory().contains("FORCE_ACCELERATION")){
                                        ps_sl_val = String.valueOf(Integer.parseInt(String.valueOf(Math.round(Float.parseFloat(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getPreviousSpeed())))));
                                    } else if(response.body().get(i).getDeviceDataList().get(j).getSubCategory().contains("OVER_SPEED") ){
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
                                            response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLongitude(),ps_sl_val,
                                            response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getRisk()));
                                }
                            }
                            calculateRoute();
                        }
                        //    Toast.makeText(getApplicationContext(), "Tested", Toast.LENGTH_SHORT).show();
                        if (risk_arrayList.size()>0) {
                            risk_list.setVisibility(View.VISIBLE);
                            no_record.setVisibility(View.GONE);
                            risk_list.setAdapter(new RiskAlertListAdapter(RidesDetailPageActivity.this,risk_arrayList));
                        } else {
                            risk_list.setVisibility(View.GONE);
                            no_record.setVisibility(View.VISIBLE);
                        }



                    }else if (response.code() == 412){
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }else {
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

    private void setAlertImage(String img,Double lat,Double longi){
        try {
            Image image = new Image();
            String uri = "@drawable/"+img;
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            image.setImageResource(imageResource);
            mapFragment.getPositionIndicator().setMarker(image);
            MapMarker customMarker = new MapMarker(new GeoCoordinate(lat, longi, 0.0), image);
            map.addMapObject(customMarker);
        } catch (Exception e) {
            com.here.odnp.util.Log.e("HERE", e.getMessage());
        }
    }

    private void getGraphList() {
        progressDialog = new BeautifulProgressDialog(RidesDetailPageActivity.this, BeautifulProgressDialog.withLottie, null);
        progressDialog.setLottieLocation(Constants.lottiePath);
        progressDialog.setLottieLoop(true);
        progressDialog.setLayoutColor(Color.TRANSPARENT);
        progressDialog.setLayoutElevation(0);
        progressDialog.setCancelable(true);
        progressDialog.show();
        JSON generatedJsonObject = JSON.create(
                JSON.dic(
                        "deviceDataCategory",  getString(R.string.gps_DISTANCE_DATA)
                )
        );
        restMethods = RestClient.buildHTTPClient();
        restMethods.GETgraphList(Constants.referredBy, Preference_Details.getLoginSessID(),Constants.companyID,Preference_Details.getUserID(),rideID,Constants.type_GPS_Device_GET,Constants.gps_DEVICE_AND_DATA_LIST,generatedJsonObject.toString()).enqueue(new Callback<List<GPSGetAlertListResp>>() {
            @Override
            public void onResponse(@NonNull Call<List<GPSGetAlertListResp>> call, @NonNull Response<List<GPSGetAlertListResp>> response) {
                Log.e("status", "" + response.body());
                try {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            graphArrayList.clear();
                            for(int i=0;i<response.body().size();i++){
                                for (int j=0;j<response.body().get(i).getDeviceDataList().size();j++){
                                    if (j == 0){
                                        getEntries(0.0f,0.0f,new Double(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLatitude()),new Double(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLongitude()));
                                    }else if (j == response.body().get(i).getDeviceDataList().size()-1){
                                        getEntries(Float.valueOf(scoreListModelArrayList.get(0).getTotkms()),0.0f,new Double(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLatitude()),new Double(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLongitude()));
                                    }else {
                                        getEntries(Float.parseFloat(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getKiloMeter()),Float.parseFloat(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getRisk()),new Double(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLatitude()),new Double(response.body().get(i).getDeviceDataList().get(j).getDeviceDataField().getLongitude()));
                                    }

                                }
                            }
                        }
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
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        ProgressDialog.dismissProgress();
                    }
                    catch (WindowManager.BadTokenException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<GPSGetAlertListResp>> call, @NonNull Throwable t) {
                if (t.toString().contains("Unable to resolve host")){
                    Util_functions.ShowValidateAlertError(RidesDetailPageActivity.this,getString(R.string.check_internet),"OK");
                }
                Util_functions.ShowAlertError(RidesDetailPageActivity.this,t.getMessage(),"OK",RidesDetailPageActivity.this);
                t.printStackTrace();
                ProgressDialog.dismissProgress();
            }
        });


    }

    private ArrayList<Entry> getEntries(Float x,float y,Double lat,Double longi) {
        if (x==0f &&y==0f) {
            if (Float.valueOf(scoreListModelArrayList.get(0).getTotkms())>0f){
                lineEntries.add(new Entry(0,0));
                Collections.sort(lineEntries, new EntryXComparator());
            }
        } else if (x.equals(Float.valueOf(Float.valueOf(scoreListModelArrayList.get(0).getTotkms())))){
            lineEntries.add(new Entry(x,0));
            Collections.sort(lineEntries, new EntryXComparator());
        }  else{
            float xx= x*10;
            double diffkk=diffKm*10;
            double diff=xx%diffkk;
            if (diff==0.0){
                DecimalFormat REAL_FORMATTER = new DecimalFormat("0.0");
                diffKm= Double.valueOf(REAL_FORMATTER.format(diffKm));
                //routePoints.add(new RouteWaypoint(new GeoCoordinate(lat,longi)));
                if (y == -1.0 || y == -100.0) {
                    y = 0;
                    lineEntries.add(new Entry(x, y));
                    Collections.sort(lineEntries, new EntryXComparator());
                } else {
                    lineEntries.add(new Entry(x, y));
                    Collections.sort(lineEntries, new EntryXComparator());
                }
            }else {
                if (totKm > 50) {
                    if (y != -1.0 && y != -100.0 && y != 0.0) {
                        if (y>50.0){
                            lineEntries.add(new Entry(x, y));
                            Collections.sort(lineEntries, new EntryXComparator());
                        }
                    }
                }else{
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
            lineEntries1.add(new Entry( Float.parseFloat(scoreListModelArrayList.get(0).getTotkms()), 90));
        }else{
            startActivity(new Intent(RidesDetailPageActivity.this,RoadStartPageActivity.class));
            finish();
        }
        return lineEntries1;
    }
    private ArrayList<Entry> getLineEntries2() {  //need to set co -pilot mode
        lineEntries2 = new ArrayList<Entry>();
        lineEntries2.add(new Entry(0, 50));    //value -70 - co -pilot mode need to change
        if (!scoreListModelArrayList.isEmpty()) {
            lineEntries2.add(new Entry( Float.parseFloat(scoreListModelArrayList.get(0).getTotkms()), 50));
        }else{
            startActivity(new Intent(RidesDetailPageActivity.this,RoadStartPageActivity.class));
            finish();
        }
        return lineEntries2;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button:
                finish();
                break;
        }

    }


    @Override
    public void onCompletion(MediaPlayer mp)
    {
        mp.release();
        if (currentTrack < tracks.length-1) {
            currentTrack++;
            mp = MediaPlayer.create(getApplicationContext(), tracks[currentTrack]);
            mp.setOnCompletionListener(this);
            mp.start();
        }
    }

    @Override
    public void onPositiveButtonClicked(AlertDialog alertDialog) {
        finish();
    }

    @Override
    public void onNegativeButtonClicked() {

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
                RouteWaypoint startPoint = new RouteWaypoint(m_startPoint);

                RouteWaypoint destination = new RouteWaypoint(m_endPoint);

                /* Initialize a RoutePlan */

                FTCRRoutePlan routePlan = new FTCRRoutePlan(routePoints, routeOptions);
    /*
      Set the name of the map overlay. It has to be the same that is used for uploading
      the custom roads to the fleet telematics server.
     */
                if (m_routeTask != null) {
                    m_routeTask.cancel();
                }

                m_routeTask = m_router.calculateRoute(routePlan, new FTCRRouter.Listener() {
                    @Override
                    public void onCalculateRouteFinished(@NonNull List<FTCRRoute> routeResults,
                                                         @NonNull FTCRRouter.ErrorResponse errorResponse) {
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
                                Toast.makeText(RidesDetailPageActivity.this,
                                        "Error:route results returned is not valid",
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(RidesDetailPageActivity.this,
                                    "Error:route calculation returned error code: "
                                            + errorResponse.getErrorCode()
                                            + ",\nmessage: " + errorResponse.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
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
}

