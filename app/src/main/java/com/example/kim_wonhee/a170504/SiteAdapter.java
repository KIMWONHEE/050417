package com.example.kim_wonhee.a170504;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kim_wonhee on 2017. 5. 10..
 */

public class SiteAdapter extends BaseAdapter {
    ArrayList<Site> data;
    Context c;

    TextView textView, textView2;

    public SiteAdapter(ArrayList<Site> data, Context c) {
        this.data = data;
        this.c = c;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(c);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_site, null);
        }

        textView = (TextView) convertView.findViewById(R.id.textView);
        textView2 = (TextView) convertView.findViewById(R.id.textView2);

        textView.setText("<" + data.get(position).getName() + ">");
        textView2.setText(data.get(position).getUrl());

        return convertView;
    }
}
