package com.montbleu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.montbleu.model.rideListItem;
import com.montbleu.roadsafety.R;

import java.util.ArrayList;
import java.util.Random;

public class RideListAdapter extends RecyclerView.Adapter<RideListAdapter.ListHolder>{

    private ArrayList<rideListItem> rideList;
    private Context mContext;

    public RideListAdapter(ArrayList<rideListItem> rideList, Context mContext) {
        this.rideList = rideList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.rides_list_item, parent, false);
        return new ListHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, int position) {
        final rideListItem list = rideList.get(position);
        Random rnd = new Random();
        if(position==0){

        }else if(position==1){
            holder.tvRides.setTextColor(Color.parseColor("#09ABB6"));
            holder.tvRiskAlert.setTextColor(Color.parseColor("#09ABB6"));
            holder.tvScore.setTextColor(Color.parseColor("#09ABB6"));
        }else if(position==2){
            holder.tvRides.setTextColor(Color.parseColor("#433E96"));
            holder.tvRiskAlert.setTextColor(Color.parseColor("#433E96"));
            holder.tvScore.setTextColor(Color.parseColor("#433E96"));
        }else if(position==3){
            holder.tvRides.setTextColor(Color.parseColor("#E72862"));
            holder.tvRiskAlert.setTextColor(Color.parseColor("#E72862"));
            holder.tvScore.setTextColor(Color.parseColor("#E72862"));
        }else if(position==4){
            holder.tvRides.setTextColor(Color.parseColor("#813C95"));
            holder.tvRiskAlert.setTextColor(Color.parseColor("#813C95"));
            holder.tvScore.setTextColor(Color.parseColor("#813C95"));
        }else if(position==4){
            holder.tvRides.setTextColor(Color.parseColor("#7AA829"));
            holder.tvRiskAlert.setTextColor(Color.parseColor("#7AA829"));
            holder.tvScore.setTextColor(Color.parseColor("#7AA829"));
        }

        holder.tvRides.setText(list.getRides());
        holder.tvRiskAlert.setText(list.getRiskAlert());
        holder.tvScore.setText(list.getScore());
    }

    @Override
    public int getItemCount() {
        return rideList.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder {

        private TextView tvRides;
        private TextView tvRiskAlert;
        private TextView tvScore;

        public ListHolder(View itemView) {
            super(itemView);

            tvRides = itemView.findViewById(R.id.tvRides);
            tvRiskAlert = itemView.findViewById(R.id.tvRiskAlert);
            tvScore = itemView.findViewById(R.id.tvScore);
        }
    }
}
