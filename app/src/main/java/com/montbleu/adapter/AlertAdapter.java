package com.montbleu.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.montbleu.Retrofit.RestClient;
import com.montbleu.Retrofit.RestMethods;
import com.montbleu.Utils.Constants;
import com.montbleu.Utils.Preference_Details;
import com.montbleu.Utils.ProgressDialog;
import com.montbleu.Utils.Util_functions;
import com.montbleu.model.AlertData;
import com.montbleu.model.GPSGetAlertListResp;
import com.montbleu.roadsafety.BeautifulProgressDialog;
import com.montbleu.roadsafety.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.amirs.JSON;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.MyviewHolder> {
    Context context;
    private List<AlertData> alertList;
    BeautifulProgressDialog progressDialog;
    RestMethods restMethods;
    ArrayList<GPSGetAlertListResp.DeviceDataList> gpsgetDeviceRespArrayList = new ArrayList<>();
    ArrayList<String> time = new ArrayList<>();
    String rideID = "";
    AlertData alertsModel;

    public AlertAdapter(List<AlertData> alertList, String rideID, Context context) {
        this.alertList = alertList;
        this.rideID = rideID;
        this.context = context;
    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.alerts_layout, parent, false);
        return new MyviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@Nonnull MyviewHolder holder, @SuppressLint("RecyclerView") int position) {
        alertsModel = alertList.get(position);
        holder.tvAlertName.setText(alertList.get(position).getAlertName());
        holder.tvAlertValue.setText(alertList.get(position).getAlertNoOfCount());
      //  holder.tvAlertValue.setText("100");
        Picasso.get()
                .load(alertList.get(position).getAlertTypeImg())
                .into(holder.imgAlertType);

        holder.card_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Util_functions.ShowGridDetails(v.getContext(),context.getString(R.string.gps_Yield),alertsModel.getAlertTime(),alertsModel.getAlertKM(),alertsModel.getAlertZipCode(),alertsModel.getAlertid());
                if (!alertList.get(position).getAlertNoOfCount().isEmpty()) {
                    if (alertList.get(position).getAlertNoOfCount().equals("0")){

                    }else {

                     //   getAlertList(position);
                        /*getLocalClickedAlert(position);*/
                    }
                } else {
                    Toast.makeText(context.getApplicationContext(),"No Data",Toast.LENGTH_SHORT ).show();
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return alertList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.alert_type)
        ImageView imgAlertType;

        @BindView(R.id.alert_value)
        TextView tvAlertValue;

        @BindView(R.id.alert_name)
        TextView tvAlertName;
        @BindView(R.id.constraintLayout7)
        ConstraintLayout card_detail;

        public MyviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //  image = (ImageView)itemView.findViewById(R.id.image);
        }
    }

}
