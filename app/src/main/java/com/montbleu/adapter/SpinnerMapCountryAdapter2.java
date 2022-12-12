package com.montbleu.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.montbleu.model.ContinentResponse;
import com.montbleu.model.CountryResponse;
import com.montbleu.roadsafety.R;

import java.util.List;


public class SpinnerMapCountryAdapter2 extends ArrayAdapter<CountryResponse> {

    LayoutInflater flater;

    public SpinnerMapCountryAdapter2(Activity context, int resouceId, int textviewId, List<CountryResponse> list) {

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

        CountryResponse rowItem = getItem(position);

        viewHolder holder;
        View rowview = convertView;
        if (rowview == null) {
            holder = new viewHolder();
            flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = flater.inflate(R.layout.listitems_layout_1, null, false);
            holder.txtTitle = (TextView) rowview.findViewById(R.id.text_spin_1);
            rowview.setTag(holder);
        } else {
            holder = (viewHolder) rowview.getTag();
        }
        holder.txtTitle.setText(rowItem.getName());
        return rowview;
    }

    private class viewHolder {
        TextView txtTitle;
    }
}