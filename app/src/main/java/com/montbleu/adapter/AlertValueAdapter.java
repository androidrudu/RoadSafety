package com.montbleu.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.montbleu.model.AlertValueModel;
import com.montbleu.roadsafety.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlertValueAdapter extends RecyclerView.Adapter<AlertValueAdapter.MyviewHolder> {
        private List<AlertValueModel> alertListValue;

    public AlertValueAdapter(List<AlertValueModel> alertListValue) {
        this.alertListValue = alertListValue;
    }
        @Override
        public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_value_layout, parent, false);
            return new MyviewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {

                AlertValueModel alertValueModel=alertListValue.get(position);
                holder.tvAlertTime.setText(alertValueModel.getTime());
                holder.tvAlertDist.setText(alertValueModel.getDist());
                holder.tvAlertZip.setText(alertValueModel.getZipcode());
                holder.tvAlertSpeed.setText(alertValueModel.getSpeed());
                if (alertValueModel.getValue().equals("29")) {
                    if (alertValueModel.getSpeedLimit().isEmpty()||alertValueModel.getSpeedLimit().equals("")||alertValueModel.getSpeedLimit().equals("0")){
                        holder.tvAlertSpeedLimit.setVisibility(View.VISIBLE);
                        holder.tvAlertSpeedLimit.setText("0.0.0");

                    }else {
                        holder.tvAlertSpeedLimit.setVisibility(View.VISIBLE);
                        holder.tvAlertSpeedLimit.setText(alertValueModel.getSpeedLimit());

                    }
                }else if (alertValueModel.getValue().equals("30")){
                    if (alertValueModel.getSpeedLimit().isEmpty()||alertValueModel.getSpeedLimit().equals("")||alertValueModel.getSpeedLimit().equals("0")){
                        holder.tvAlertSpeedLimit.setVisibility(View.GONE);
                    }else {
                        holder.tvAlertSpeedLimit.setVisibility(View.VISIBLE);
                        holder.tvAlertSpeedLimit.setText(alertValueModel.getSpeedLimit());
                    }
                }else if(alertValueModel.getValue().equals("31")){
                    if (alertValueModel.getSpeedLimit().isEmpty()||alertValueModel.getSpeedLimit().equals("")||alertValueModel.getSpeedLimit().equals("0")){
                        holder.tvAlertSpeedLimit.setVisibility(View.GONE);
                    }else {
                        holder.tvAlertSpeedLimit.setVisibility(View.VISIBLE);
                        holder.tvAlertSpeedLimit.setText(alertValueModel.getSpeedLimit());
                    }
                }else{
                    holder.tvAlertSpeedLimit.setVisibility(View.GONE);
                }


        }

        @Override
        public int getItemCount() {
            return alertListValue.size();
        }

        public class MyviewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.alert_time)
            TextView tvAlertTime;

            @BindView(R.id.alert_dist)
            TextView tvAlertDist;
            @BindView(R.id.alert_zip)
            TextView tvAlertZip;
            @BindView(R.id.alert_speed_limit)
            TextView tvAlertSpeedLimit;
            @BindView(R.id.alert_speed)
            TextView tvAlertSpeed;


            public MyviewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                //  image = (ImageView)itemView.findViewById(R.id.image);
            }
        }
    }


