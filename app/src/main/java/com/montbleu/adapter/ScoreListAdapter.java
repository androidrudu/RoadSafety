package com.montbleu.adapter;

import static com.montbleu.Utils.Constants.scoreListModelArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.montbleu.Utils.Constants;
import com.montbleu.model.ScoreListModel;
import com.montbleu.roadsafety.R;
import com.montbleu.roadsafety.activity.LastDriveSummary;
import com.montbleu.roadsafety.activity.RidesDetailPageActivity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ScoreListAdapter extends RecyclerView.Adapter<ScoreListAdapter.ListHolder> implements Filterable {

    private ArrayList<ScoreListModel> scoreList;
    private Context mContext;
    private ArrayList<ScoreListModel> filtScoreList;


    public ScoreListAdapter(ArrayList<ScoreListModel> scoreList, Context mContext) {
        this.scoreList = scoreList;
        this.mContext = mContext;
        this.filtScoreList = new ArrayList<>(scoreList);

    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.score_list_layout, parent, false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreListAdapter.ListHolder holder, int position) {
        DecimalFormat REAL_FORMATTER = new DecimalFormat("00.00");
        ScoreListModel scoreModel = scoreList.get(position);

        if (scoreModel.getCheckDash().equals("1")) {
            holder.tv_ride.setText("Ride " + (position + 1));
        } else {
            holder.tv_ride.setText((scoreModel.getName()));
        }
        if (scoreModel.getDate_time().equals("0.0") || scoreModel.getDate_time().equals("") || scoreModel.getDate_time().contains("-1.0") || scoreModel.getDate_time().contains("-0.0")) {
            //  holder.tvRide1Time.setText("0");
            holder.ride_date.setText("0");
        } else {
            getDateCurrentTimeZone(holder, scoreModel, scoreModel.getDate_time());
        }
        if (scoreModel.getTotkms().equals("0.0") || scoreModel.getTotkms().equals("") || scoreModel.getTotkms().contains("-1.0") || scoreModel.getTotkms().contains("-0.0")) {
            holder.ride_dist.setText("00.00");
        } else {
            holder.ride_dist.setText(REAL_FORMATTER.format(Double.parseDouble(scoreModel.getTotkms())) + " KM");
        }

        if (scoreModel.getTotTime().equals("0.0") || scoreModel.getTotTime().equals("") || scoreModel.getTotTime().contains("-1.0") || scoreModel.getTotTime().contains("-0.0")) {
            holder.ride_min.setText("00.00");
        } else {
            holder.ride_min.setText(REAL_FORMATTER.format(Double.parseDouble(scoreModel.getTotTime())) + " Mins");
        }
        if (scoreModel.getDriveScore().equals("0.0") || scoreModel.getDriveScore().equals("") || scoreModel.getDriveScore().contains("-1.0") || scoreModel.getDriveScore().contains("-0.0")) {
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setStroke(1, Color.parseColor("#FC4300"));
            gradientDrawable.setCornerRadius(10);
            holder.ride_score_value.setText("0");
            holder.progressScore.setProgress(0);
        } else {
            if (Math.round(Float.parseFloat(scoreModel.getDriveScore())) <= 80) {
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setStroke(1, Color.parseColor("#FC4300"));
                gradientDrawable.setCornerRadius(10);

                holder.ride_score_value.setText(String.valueOf(Math.round(Float.parseFloat(scoreModel.getDriveScore()))));
                holder.ride_score_value.setText(String.valueOf(Math.round(Float.parseFloat(scoreModel.getDriveScore()))));
                holder.progressScore.setProgress(Math.round(Float.parseFloat(scoreModel.getDriveScore())));
            } else if (Math.round(Float.parseFloat(scoreModel.getDriveScore())) <= 90 && Math.round(Float.parseFloat(scoreModel.getDriveScore())) >= 81) {
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setStroke(1, Color.parseColor("#F6BAA4"));
                gradientDrawable.setCornerRadius(10);

                holder.ride_score_value.setText(String.valueOf(Math.round(Float.parseFloat(scoreModel.getDriveScore()))));
                holder.progressScore.setProgress(Math.round(Float.parseFloat(scoreModel.getDriveScore())));
            } else if (Math.round(Float.parseFloat(scoreModel.getDriveScore())) >= 91 && Math.round(Float.parseFloat(scoreModel.getDriveScore())) <= 100) {
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setStroke(1, Color.parseColor("#A9CB8B"));
                gradientDrawable.setCornerRadius(10);

                holder.ride_score_value.setText(String.valueOf(Math.round(Float.parseFloat(scoreModel.getDriveScore()))));
                holder.progressScore.setProgress(Math.round(Float.parseFloat(scoreModel.getDriveScore())));

            }

        }

        holder.scoreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScoreListModel scoreModel = scoreList.get(position);
                scoreListModelArrayList.clear();
                scoreListModelArrayList.add(scoreModel);
                Log.i("**", "onClick: " + scoreModel.getId());
                Log.i("**", "onClick: " + scoreModel.getDate_time());
                Intent intent = new Intent(mContext, LastDriveSummary.class);
                intent.putExtra("RideID", scoreModel.getId());
                intent.putExtra("RideNum", scoreModel.getName());
                mContext.startActivity(intent);

            }
        });

    }

    private void getDateCurrentTimeZone(ListHolder holder, ScoreListModel scoreModel, String timestamp) {

        try {

            SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date newDate = spf.parse(timestamp);
            spf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
            timestamp = spf.format(newDate);

            String timeString = timestamp.substring(11, 16);
            String dateString1 = timestamp.substring(0, 11);
            String t2 = timestamp.substring(20, 22);
            Log.d("data=e :", timeString + " " + dateString1 + " " + t2);

            holder.ride_date.setText(dateString1 + " | " + Html.fromHtml("<b>" + timeString + "</b>") + " " + t2.toUpperCase(Locale.getDefault()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<ScoreListModel> filteredlist = new ArrayList<>();
            filteredlist.clear();
            if (constraint == null || constraint.length() == 0) {
                filteredlist.addAll(filtScoreList);
            } else {
                String filterpattern = constraint.toString().toLowerCase().trim();
                for (ScoreListModel scoreListModel : filtScoreList) {
                    try {
                        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date newDate = spf.parse(scoreListModel.getDate_time());
                        spf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                        String timeString = spf.format(newDate);
                        timeString = timeString.substring(0, 11);

                        if (timeString.contains(filterpattern)) {
                            filteredlist.add(scoreListModel);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredlist;
            return results;


        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            scoreList.clear();
            scoreList.addAll((List) results.values);
            notifyDataSetChanged();
        }

    };


    public class ListHolder extends RecyclerView.ViewHolder {

        private TextView tv_ride;
        private TextView ride_date;
        private TextView ride_min;
        private TextView ride_dist;
        private TextView tvFrom;
        private TextView tvTo;
        private TextView ride_score_value;
        private ProgressBar progressScore;
        LinearLayout scoreLayout;


        public ListHolder(View itemView) {
            super(itemView);
            tv_ride = itemView.findViewById(R.id.textview_ride);
            ride_date = itemView.findViewById(R.id.ride1_date);
            ride_min = itemView.findViewById(R.id.ride1_min);
            ride_dist = itemView.findViewById(R.id.ride1_dist);
            tvFrom = itemView.findViewById(R.id.tvFrom);
            tvTo = itemView.findViewById(R.id.tvTo);
            ride_score_value = itemView.findViewById(R.id.ride_score_value);
            progressScore = itemView.findViewById(R.id.pb);
            scoreLayout = itemView.findViewById(R.id.scoreLayout);

        }
    }
}