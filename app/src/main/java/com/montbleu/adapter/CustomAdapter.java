package com.montbleu.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.montbleu.roadsafety.R;

public class CustomAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] text;
    private Integer[] img;

    public CustomAdapter(Activity context, String[] text, Integer[] img) {
        super(context, R.layout.listview_profile, text);

        this.context = context;
        this.text = text;
        this.img = img;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowview = inflater.inflate(R.layout.listview_profile, null, true);
        View bottomLine = rowview.findViewById(R.id.bottomLine);
        TextView texts = rowview.findViewById(R.id.textview_txt);
        ImageView imagee = rowview.findViewById(R.id.imageview_img);
        texts.setText(text[position]);
        imagee.setImageResource(img[position]);
        if (text.length - 1 == position) {
            bottomLine.setBackgroundColor(context.getColor(R.color.color_dash_line));
        }
        return rowview;
    }
}
