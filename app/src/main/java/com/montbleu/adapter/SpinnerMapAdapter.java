package com.montbleu.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.montbleu.model.ContinentResponse;
import com.montbleu.model.RowItem;
import com.montbleu.roadsafety.R;

import java.util.List;


public class SpinnerMapAdapter extends ArrayAdapter<ContinentResponse> {

    LayoutInflater flater;

    public SpinnerMapAdapter(Activity context, int resouceId, int textviewId, List<ContinentResponse> list) {

        super(context, resouceId, textviewId, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return rowview(convertView, position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return rowview(convertView, position);
    }

    private View rowview(View convertView, int position) {

        ContinentResponse rowItem = getItem(position);

        viewHolder holder;
        View rowview = convertView;
        if (rowview == null) {

            holder = new viewHolder();
            flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = flater.inflate(R.layout.listitems_layout_1, null, false);
            holder.txtTitle = (TextView) rowview.findViewById(R.id.text_spin_1);
            //holder.imageView = (ImageView) rowview.findViewById(R.id.icon);
            rowview.setTag(holder);
        } else {
            holder = (viewHolder) rowview.getTag();
        }

        if (!TextUtils.isEmpty(rowItem.getName())) {
            holder.txtTitle.setText(String.valueOf(rowItem.getName()));
        } else {
            holder.txtTitle.setText("");
        }


        return rowview;
    }

    private class viewHolder {
        TextView txtTitle;

    }
}