package com.kemblep.crewlog.obj;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kemblep.crewlog.CrewLog;
import com.kemblep.crewlog.LogEntryFragment;
import com.kemblep.crewlog.R;

import java.util.ArrayList;

/**
 * Created by Pete on 5/3/2015.
 */
public class LegArrayAdapter extends BaseAdapter {

    private ArrayList<Leg> mLegs;
    private Context mContext;
    private FlightLogEntry mEntry;

    public LegArrayAdapter(Context context, FlightLogEntry entry){
        mContext = context;
        mEntry = entry;
        mLegs = entry.Legs;
    }

    @Override
    public int getCount() {
        return mLegs.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.listview_flights, null);
        }

        final Leg leg = mLegs.get(position);
        TextView tvLegTitle = (TextView) convertView.findViewById(R.id.fl_entry_title);
        if(tvLegTitle == null){
            return convertView;
        }

        String legTitle = "Leg " + (position + 1);
        if(leg.Departure != null && leg.Arrival != null){
            legTitle += " " + leg.Departure + " -> " + leg.Arrival;
        } else {
            //this is an empty leg - use it to add a new leg
            legTitle = mContext.getResources().getString(R.string.add_new_entry);
        }
        tvLegTitle.setText(legTitle);
        tvLegTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check for null id
                Bundle b = new Bundle();
                b.putInt(Leg.Columns.ID.name(), leg.Id);

                LogEntryFragment logEntryFragment = new LogEntryFragment();
                logEntryFragment.setArguments(b);

                FragmentManager fm = ((CrewLog)mContext).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fl_main, logEntryFragment);
                ft.addToBackStack("Leg Entry");
                ft.commit();

                return;
            }
        });

        return convertView;
    }
}
