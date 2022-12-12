package com.montbleu.OBD.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.montbleu.OBD.model.OBDModel;
import com.montbleu.roadsafety.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OBDAdapter extends RecyclerView.Adapter<OBDAdapter.MyViewHolder> {
private List<OBDModel> obdList;

    public OBDAdapter(Activity activity, List<OBDModel> obdList) {
        this.obdList = obdList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.obd_fun_list, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OBDModel obdModel = obdList.get(position);
        if (obdModel.getStatus().booleanValue()) {
            holder.img_obd.setImageResource(obdModel.getmImage());
            holder.obd_name.setText(obdModel.getName());
            holder.obd_value.setText(obdModel.getValue());
        } else {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }

    }
    @Override
    public int getItemCount() {
        return obdList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.obd_name)
        TextView obd_name;
        @BindView(R.id.img_obd)
        ImageView img_obd;
        @BindView(R.id.obd_num)
        TextView obd_num;
        @BindView(R.id.obd_value)
        TextView obd_value;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //  image = (ImageView)itemView.findViewById(R.id.image);
        }
    }

    }
