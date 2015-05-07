package com.kemble.crewlog.obj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kemble.crewlog.R;

import java.util.ArrayList;

/**
 * Created by Pete on 5/3/2015.
 */
public class LegArrayAdapter extends BaseAdapter {

    private ArrayList<Leg> mLegs;
    private Context mContext;

    public LegArrayAdapter(Context context, ArrayList<Leg> legs ){
        mContext = context;
        mLegs = legs;
    }

    @Override
    public int getCount() {
        return mLegs.size();
    }

    @Override
    public Object getItem(int position) {
        return mLegs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_flights, null);
        }

        Leg leg = mLegs.get(position);
        TextView tvLegTitle = (TextView) convertView.findViewById(R.id.fl_entry_title);
        if(tvLegTitle == null){
            return convertView;
        }

        String legTitle = "Leg " + position + 1;
        if(leg.Departure != null && leg.Arrival != null){
            legTitle += " " + leg.Departure + " -> " + leg.Arrival;
        }
        tvLegTitle.setText(legTitle);
        tvLegTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return convertView;
    }
}
