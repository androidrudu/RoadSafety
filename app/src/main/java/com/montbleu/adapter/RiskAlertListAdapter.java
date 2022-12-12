package com.montbleu.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.here.android.mpa.common.Image;
import com.montbleu.Utils.Util_functions;
import com.montbleu.model.Risk_alert_model;
import com.montbleu.roadsafety.R;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RiskAlertListAdapter extends RecyclerView.Adapter<RiskAlertListAdapter.MyviewHolder>  {


    private List<Risk_alert_model> alertListValue;
    Context activity;
    public RiskAlertListAdapter(Activity activity, List<Risk_alert_model> alertListValue) {
        this.alertListValue = alertListValue;
        this.activity = activity;
    }
    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.risk_list_value_layout, parent, false);
        return new MyviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        DecimalFormat REAL_FORMATTER = new DecimalFormat("0.00");

        if (alertListValue.size()>0) {
            holder.tableLayout.setVisibility(View.VISIBLE);
            holder.no_record.setVisibility(View.GONE);
            Risk_alert_model alertValueModel = alertListValue.get(position);

            if (!alertValueModel.getPs_sl_val().isEmpty()) {
                holder.sl_ps.setText(alertValueModel.getPs_sl_val());
            } else {
                holder.sl_ps.setText("-");
            }

            if (!alertValueModel.getKm_distance().isEmpty()) {
                holder.tvRiskDist.setText(alertValueModel.getKm_distance());
            } else {
                holder.tvRiskDist.setText("-");
            }

            if (!alertValueModel.getTime_hh_mm().isEmpty()) {
                holder.tvRiskTime.setText(alertValueModel.getTime_hh_mm());
            } else {
                holder.tvRiskTime.setText("-");
            }


            if (!alertValueModel.getZipcode().isEmpty()) {
                holder.tvRiskZip.setText(alertValueModel.getZipcode());
            } else {
                holder.tvRiskZip.setText("-");
            }

            if (alertValueModel.getTime_hh_mm().equals("0")||alertValueModel.getTime_hh_mm().equals("")) {
                holder.tvRiskTime.setText("-");
            } else {
                try {
                    String  datetimeString = alertValueModel.getTime_hh_mm();
                    String timeString = datetimeString.substring(11,16);
                    String dateString1 = datetimeString.substring(0,11);
                    Log.d("data=e :",timeString+" "+dateString1+" ");
                    holder.tvRiskTime.setText(timeString);
                }catch(Exception e){
                    e.printStackTrace();
                }

            }



            if (!alertValueModel.getSpeed().isEmpty()) {
                holder.tvRiskSpeed.setText(String.valueOf(Math.round(Float.parseFloat(alertValueModel.getSpeed()))));
            } else {
                holder.tvRiskSpeed.setText("-");
            }

             if(!alertValueModel.getKm_distance().isEmpty()){
                 holder.tvRiskDist.setText(REAL_FORMATTER.format(Double.parseDouble(alertValueModel.getKm_distance())));
             } else {
                 holder.tvRiskSpeed.setText("-");

             }
            if (alertValueModel.getcategory().contains(activity.getString(R.string.gps_START_DATA))) {
                holder.categoryIcon.setImageResource(R.drawable.start_end_trip_icon);
                holder.alertIcon.setImageResource(R.drawable.img_start);
            } else if (alertValueModel.getcategory().contains(activity.getString(R.string.gps_ALERT_DATA))) {
                if (Double.valueOf(String.valueOf(alertValueModel.getRisk()))>90){
                    holder.categoryIcon.setImageResource(R.drawable.alert_trip_icon);
                    String imgName = Util_functions.getAlertImageString(Integer.parseInt(alertValueModel.getAlert_val()));
                    setAlertImageAlone(imgName, holder);
                }else {
                    holder.categoryIcon.setImageResource(R.drawable.alert_trip_amber_icon);
                    String imgName = Util_functions.getAlertImageString(Integer.parseInt(alertValueModel.getAlert_val()));
                    setAlertImageAlone(imgName, holder);
                }
            } else if (alertValueModel.getcategory().contains(activity.getString(R.string.gps_END_DATA))) {
                holder.categoryIcon.setImageResource(R.drawable.start_end_trip_icon);
                holder.alertIcon.setImageResource(R.drawable.img_finish);
            }
        } else {
            holder.tableLayout.setVisibility(View.GONE);
            holder.no_record.setVisibility(View.VISIBLE);
        }
    }

    private void setAlertImageAlone(String img, MyviewHolder holder){
        try {
            //Image image = new Image();
            String uri = "@drawable/"+img;
            int imageResource = activity.getResources().getIdentifier(uri, null, activity.getPackageName());
            holder.alertIcon.setImageResource(imageResource);
        } catch (Exception e) {
           e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return alertListValue.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.time_value)
        TextView tvRiskTime;

        @BindView(R.id.dist_km_val)
        TextView tvRiskDist;
        @BindView(R.id.zip_val)
        TextView tvRiskZip;

        @BindView(R.id.speed_val)
        TextView tvRiskSpeed;

        @BindView(R.id.category_icon)
        ImageView categoryIcon;

        @BindView(R.id.alert_icon)
        ImageView alertIcon;

        @BindView(R.id.no_record)
        TextView no_record;

        @BindView(R.id.sl_ps_val)
        TextView sl_ps;

        @BindView(R.id.linearLayout3)
        TableLayout tableLayout;




        public MyviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
