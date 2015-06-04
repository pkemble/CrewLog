package com.kemblep.crewlog;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.kemblep.crewlog.obj.Flight;
import com.kemblep.crewlog.obj.JumpToEnd;
import com.kemblep.crewlog.obj.Util;


/**
 * Created by Pete on 5/5/2015.
 */
public class FlightEntryFragment extends Fragment {
    public FlightEntryFragment(){

    }

    private Context mContext;
    private Integer mId = null;
    private Long mSequence = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View vEntry = inflater.inflate(R.layout.fragment_flight, container, false);
        final Button btnSubmit = (Button) vEntry.findViewById(R.id.btn_submit_leg);

        mContext = vEntry.getContext();

        Bundle b = this.getArguments();
        if(b != null && b.keySet().size() > 0){
            //mId for existing legs
            mId = (Integer)b.get(Flight.Columns.ID.name());

            if(mId != null) {
                Flight flight = new Flight().getFlightFromDb(mId, mContext);
                if (flight != null) {
                    populateEntry(vEntry, flight);
                    btnSubmit.setText("UPDATE LEG");
                    mSequence = flight.Sequence;
                }
            } else {
                //mSequence for new legs
                mSequence = new Long(b.get(Flight.Columns.SEQUENCE.name()).toString());
            }
        }

        final EditText etBlockOut = (EditText) vEntry.findViewById(R.id.fl_blockout);
        final EditText etBlockIn = (EditText) vEntry.findViewById(R.id.fl_blockin);

        final EditText etDep = (EditText) vEntry.findViewById(R.id.fl_dep);
        final EditText etArr = (EditText) vEntry.findViewById(R.id.fl_arr);

        JumpToEnd jte = new JumpToEnd();

        etDep.setOnClickListener(jte);
        etArr.setOnClickListener(jte);

        //Setup block time entries
        TextWatcher twBlockOut = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence end, int start, int before, int count) {
                if(end.length() >= 4 && !Util.verifyTimes(etBlockOut, etBlockIn)){
                    //nullify any further input
                    if(end.length() > 4){
                        Util.fixErrors(etBlockOut);
                    }
                    return;
                }

                //if the end time has been entered, focus on the start time
                //unless it's also already been entered
                if (end.toString().length() == 4 && etBlockOut.length() < 4){

                    Util.resetEditText(etBlockOut);
                    etBlockOut.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        View.OnClickListener timesClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                v.setBackgroundColor(Color.TRANSPARENT);
                EditText e = (EditText) getView().findViewById(v.getId());
                Util.resetEditText(e);
            }
        };

        TextWatcher twBlockIn = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence end, int start, int before, int count) {
                if(end.length() >= 4 && !Util.verifyTimes(etBlockOut, etBlockIn)){
                    //nullify any further input
                    if(end.length() > 4){
                        Util.fixErrors(etBlockIn);
                    }
                    return;
                }

                //if the end time has been entered, focus on the start time
                //unless it's also already been entered
                if (end.toString().length() == 4 && etBlockOut.length() < 4){

                    Util.resetEditText(etBlockOut);
                    etBlockOut.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };


        etBlockOut.setOnClickListener(timesClickListener);
        etBlockIn.setOnClickListener(timesClickListener);
        etBlockOut.addTextChangedListener(twBlockOut);
        etBlockIn.addTextChangedListener(twBlockIn);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitLeg(vEntry);
            }
        });


        return vEntry;
    }

    public void populateEntry(View flightLogEntryView, Flight flight){
        //RadioGroup flTypeGroup = (RadioGroup) flightLogEntryView.findViewById(R.id.fl_flight_type);
        RadioButton pf = (RadioButton) flightLogEntryView.findViewById(R.id.fl_pf);
        RadioButton pm = (RadioButton) flightLogEntryView.findViewById(R.id.fl_pm);
        RadioButton sf = (RadioButton) flightLogEntryView.findViewById(R.id.fl_sf);
        RadioButton sm = (RadioButton) flightLogEntryView.findViewById(R.id.fl_sm);

        switch (flight.FlightType){
            case PF:
                pf.setChecked(true);
                break;
            case PM:
                pm.setChecked(true);
                break;
            case SF:
                sf.setChecked(true);
                break;
            case SM:
                sm.setChecked(true);
                break;
        }

        EditText flDeparture = (EditText) flightLogEntryView.findViewById(R.id.fl_dep);
        flDeparture.setText(flight.Departure);

        EditText flBlockOut = (EditText) flightLogEntryView.findViewById(R.id.fl_blockout);
        flBlockOut.setText(flight.BlockOut);

        EditText flArrival = (EditText) flightLogEntryView.findViewById(R.id.fl_arr);
        flArrival.setText(flight.Arrival);

        EditText flBlockIn = (EditText) flightLogEntryView.findViewById(R.id.fl_blockin);
        flBlockIn.setText(flight.BlockIn);

        EditText flInst = (EditText) flightLogEntryView.findViewById(R.id.fl_inst);
        flInst.setText(flight.InstrumentTime);

        EditText flApproaches = (EditText) flightLogEntryView.findViewById(R.id.fl_approaches);
        flApproaches.setText(flight.Approaches);

        EditText flNight = (EditText) flightLogEntryView.findViewById(R.id.fl_night);
        flNight.setText(flight.Night);

        CheckBox flNightTo = (CheckBox) flightLogEntryView.findViewById(R.id.fl_night_to);
        flNightTo.setChecked(flight.NightTakeoff);

        CheckBox flNightLndg = (CheckBox) flightLogEntryView.findViewById(R.id.fl_night_landing);
        flNightLndg.setChecked(flight.NightLanding);
    }

    private void submitLeg(View flightLogEntryView){

        Flight flight = new Flight();

        if(mId != null){
            flight.Id = mId;
        }

        if(mSequence != null){
            flight.Sequence = mSequence;
        }

        RadioButton pf = (RadioButton) flightLogEntryView.findViewById(R.id.fl_pf);
        RadioButton pm = (RadioButton) flightLogEntryView.findViewById(R.id.fl_pm);
        RadioButton sf = (RadioButton) flightLogEntryView.findViewById(R.id.fl_sf);
        RadioButton sm = (RadioButton) flightLogEntryView.findViewById(R.id.fl_sm);
        //TODO have to implement the commented out stuff above
        if(pf.isChecked()){
            flight.FlightType = Flight.FlightTypes.PF;
        }
        if(pm.isChecked()){
            flight.FlightType = Flight.FlightTypes.PM;
        }
        if(sf.isChecked()){
            flight.FlightType = Flight.FlightTypes.SF;
        }
        if(sm.isChecked()){
            flight.FlightType = Flight.FlightTypes.SM;
        }

        EditText flDeparture = (EditText) flightLogEntryView.findViewById(R.id.fl_dep);
        flight.Departure = flDeparture.getText().toString();

        EditText flBlockOut = (EditText) flightLogEntryView.findViewById(R.id.fl_blockout);
        flight.BlockOut = flBlockOut.getText().toString();

        EditText flArrival = (EditText) flightLogEntryView.findViewById(R.id.fl_arr);
        flight.Arrival = flArrival.getText().toString();

        EditText flBlockIn = (EditText) flightLogEntryView.findViewById(R.id.fl_blockin);
        flight.BlockIn = flBlockIn.getText().toString();

        EditText flInst = (EditText) flightLogEntryView.findViewById(R.id.fl_inst);
        flight.InstrumentTime = flInst.getText().toString();

        EditText flApproaches = (EditText) flightLogEntryView.findViewById(R.id.fl_approaches);
        flight.Approaches = flApproaches.getText().toString();

        EditText flNight = (EditText) flightLogEntryView.findViewById(R.id.fl_night);
        flight.Night= flNight.getText().toString();

        CheckBox flNightTo = (CheckBox) flightLogEntryView.findViewById(R.id.fl_night_to);
        flight.NightTakeoff = flNightTo.isChecked();

        CheckBox flNightLndg = (CheckBox) flightLogEntryView.findViewById(R.id.fl_night_landing);
        flight.NightLanding = flNightLndg.isChecked();

        long l;
        if(flight.Id == null){
            l = flight.insertFlight(flight, flightLogEntryView.getContext());
        } else {
            l = flight.updateFlight(flight, flightLogEntryView.getContext());
        }
        if(l > -1){
            getFragmentManager().popBackStack();
        } else {
            Toast t = new Toast(mContext);
            t.setText("Had trouble updating / inserting this one...");
            t.show();
        }

    }
}
