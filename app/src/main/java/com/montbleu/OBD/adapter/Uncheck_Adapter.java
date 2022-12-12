package com.montbleu.OBD.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.montbleu.OBD.changeStatus;
import com.montbleu.OBD.model.OBDModel;
import com.montbleu.roadsafety.R;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Uncheck_Adapter extends RecyclerView.Adapter<Uncheck_Adapter.MyViewHolder> {
    /* private List<ObdCommand> obdLists;

      public Uncheck_Adapter(List<ObdCommand> alertList) {
          this.obdLists = alertList;
      }*/
    private List<OBDModel> obdLists;
    Activity activity;
    changeStatus changeStatus;

    public Uncheck_Adapter(Activity activity, List<OBDModel> alertList, changeStatus changeStatus) {
        this.activity = activity;
        this.obdLists = alertList;
        this.changeStatus = changeStatus;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.obd_uncheck, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //ObdCommand obd_unchecked = obdLists.get(position);
        OBDModel obd_unchecked = obdLists.get(position);
        if (obd_unchecked.status.booleanValue()) {
            holder.obd_check.setChecked(true);
        } else {
            holder.obd_check.setChecked(false);
        }
        holder.obd_name.setText(obd_unchecked.getName());
        holder.obd_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked =  holder.obd_check.isChecked();
                if(isChecked){
                    changeStatus.checkStatus(isChecked,position);
                }else{
                    changeStatus.checkStatus(isChecked,position);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return obdLists.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.obd_name1)
        TextView obd_name;
        @BindView(R.id.obd_check)
        CheckBox obd_check;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}