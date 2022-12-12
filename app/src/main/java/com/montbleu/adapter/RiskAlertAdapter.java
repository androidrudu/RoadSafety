package com.montbleu.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.montbleu.model.RiskAlertDataModel;
import com.montbleu.roadsafety.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RiskAlertAdapter extends RecyclerView.Adapter<RiskAlertHolder> {

    private Context mContext;
    private List<RiskAlertDataModel> mList;


    public RiskAlertAdapter(Context mContext, List<RiskAlertDataModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public RiskAlertHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new RiskAlertHolder(mView);
    }

    @Override
    public void onBindViewHolder(final RiskAlertHolder holder, int position) {

        Picasso.get()
                .load(mList.get(position).getImage())
                .into(holder.mImage);

//        holder.mImage.setImageResource(mList.get(position).getImage());
        holder.mTitle.setText(mList.get(position).getTitle());
        holder.mCount.setText(mList.get(position).getCount());

        for (int i = 0; i < holder.charts.length; i++) {

            LineData data = getData(mList.get(position).getGraph().size(), 100);
            setupChart(holder.charts[i], data);
        }
    }

    private LineData getData(int count, float range) {

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range) + 3;
            values.add(new Entry(i, val));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(values, "DataSet 1");

        set1.setLineWidth(1.75f);
        set1.setColor(R.color.graphgreen);
//        set1.setCircleColor(R.color.graphgreen);
        set1.setHighLightColor(R.color.graphgreen);
        set1.setDrawValues(false);

        // create a data object with the data sets
        return new LineData(set1);
    }

    @SuppressLint("ResourceAsColor")
    private void setupChart(LineChart chart, LineData data) {

        ((LineDataSet) data.getDataSetByIndex(0)).setCircleHoleColor(R.color.graphgreen);

        // no description text
        chart.getDescription().setEnabled(false);

        // chart.setDrawHorizontalGrid(false);
        //
        // enable / disable grid background
        chart.setDrawGridBackground(false);
//        chart.getRenderer().getGridPaint().setGridColor(Color.WHITE & 0x70FFFFFF);

        // enable touch gestures
        chart.setTouchEnabled(true);
        chart.setBorderColor(R.color.graphgreen);
        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

//        chart.setBackgroundColor(R.color.teal_200);

        // set custom chart offsets (automatic offset calculation is hereby disabled)
        chart.setViewPortOffsets(10, 0, 10, 0);

        // add data
        chart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setEnabled(false);

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisLeft().setSpaceTop(40);
        chart.getAxisLeft().setSpaceBottom(40);
        chart.getAxisRight().setEnabled(false);

        chart.getXAxis().setEnabled(false);

        // animate calls invalidate()...
        chart.animateX(2500);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}

class RiskAlertHolder extends RecyclerView.ViewHolder {

    ImageView mImage;
    TextView mTitle;
    TextView mCount;
    final LineChart[] charts = new LineChart[1];

    RiskAlertHolder(View itemView) {
        super(itemView);

        mImage = itemView.findViewById(R.id.ivImage);
        mTitle = itemView.findViewById(R.id.tvTitle);
        mCount = itemView.findViewById(R.id.tvCount);
        charts[0] = itemView.findViewById(R.id.chart1);
    }
}
