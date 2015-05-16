package com.kemblep.crewlog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.kemblep.crewlog.obj.Leg;


/**
 * Created by Pete on 5/5/2015.
 */
public class LogEntryFragment extends Fragment {
    public LogEntryFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View vEntry = inflater.inflate(R.layout.fragment_log_entry, container, false);
        Bundle b = this.getArguments();
        if(b != null){
            Leg leg = Leg.getLeg(b.getInt("ID"));
            populateEntry(vEntry, leg);
        }
        return vEntry;
    }

    public void populateEntry(View flightLogEntryView, Leg leg){
        EditText flDeparture = (EditText) flightLogEntryView.findViewById(R.id.fl_dep);
        flDeparture.setText(leg.Departure);

        EditText flArrival = (EditText) flightLogEntryView.findViewById(R.id.fl_arr);
        flArrival.setText(leg.Arrival);
        //TODO and so on and so forth...
    }

}
