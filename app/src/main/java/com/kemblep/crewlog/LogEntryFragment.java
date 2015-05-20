package com.kemblep.crewlog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.kemblep.crewlog.obj.Leg;


/**
 * Created by Pete on 5/5/2015.
 */
public class LogEntryFragment extends Fragment {
    public LogEntryFragment(){

    }

    private Context mContext;
    private Integer mId = null;
    private Integer mSequence = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View vEntry = inflater.inflate(R.layout.fragment_log_entry, container, false);
        EditText flDeparture = (EditText) vEntry.findViewById(R.id.fl_dep);

        mContext = vEntry.getContext();

        Bundle b = this.getArguments();
        if(b != null && b.keySet().size() > 0){
            //mId for existing legs
            mId = (Integer)b.get(Leg.Columns.ID.name());
            //mSequence for new legs
            mSequence = (Integer)b.get(Leg.Columns.SEQUENCE.name());
            if(mId != null) {
                Leg leg = Leg.getLegFromDb(mId, mContext);
                if (leg != null) {
                    populateEntry(vEntry, leg);
                }
            }
        }

        final Button btnSubmit = (Button) vEntry.findViewById(R.id.btn_submit_leg);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mId == null){
                    btnSubmit.setText("SUBMIT LEG");
                    //create leg
                } else {
                    btnSubmit.setText("UPDATE LEG");
                    //update leg
                }
                submitLeg(vEntry);
            }
        });


        return vEntry;
    }

    public void populateEntry(View flightLogEntryView, Leg leg){
        RadioGroup flTypeGroup = (RadioGroup) flightLogEntryView.findViewById(R.id.fl_flight_type);
        RadioButton pf = (RadioButton) flightLogEntryView.findViewById(R.id.fl_pf);
        RadioButton pm = (RadioButton) flightLogEntryView.findViewById(R.id.fl_pm);
        RadioButton sf = (RadioButton) flightLogEntryView.findViewById(R.id.fl_sf);
        RadioButton sm = (RadioButton) flightLogEntryView.findViewById(R.id.fl_sm);

        switch (leg.FlightType){
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
        flDeparture.setText(leg.Departure);

        EditText flBlockOut = (EditText) flightLogEntryView.findViewById(R.id.fl_blockout);
        flBlockOut.setText(leg.BlockOut);

        EditText flArrival = (EditText) flightLogEntryView.findViewById(R.id.fl_arr);
        flArrival.setText(leg.Arrival);

        EditText flBlockIn = (EditText) flightLogEntryView.findViewById(R.id.fl_blockin);
        flBlockIn.setText(leg.BlockIn);

        EditText flInst = (EditText) flightLogEntryView.findViewById(R.id.fl_inst);
        flInst.setText(Double.toString(leg.InstrumentTime));

        EditText flApproaches = (EditText) flightLogEntryView.findViewById(R.id.fl_approaches);
        flApproaches.setText(Double.toString(leg.Approaches));

        EditText flNight = (EditText) flightLogEntryView.findViewById(R.id.fl_night);
        flNight.setText(Double.toString(leg.Night));

        CheckBox flNightTo = (CheckBox) flightLogEntryView.findViewById(R.id.fl_night_to);
        flNightTo.setChecked(leg.NightTakeoff);

        CheckBox flNightLndg = (CheckBox) flightLogEntryView.findViewById(R.id.fl_night_landing);
        flNightLndg.setChecked(leg.NightLanding);
    }

    private void submitLeg(View flightLogEntryView){

        Leg leg = new Leg();

        if(mId != null){
            leg.Id = mId;
        }

        if(mSequence != null){
            //TODO this shouldn't be null?
            leg.Sequence = mSequence;
        }

        RadioGroup flTypeGroup = (RadioGroup) flightLogEntryView.findViewById(R.id.fl_flight_type);
//        RadioButton pf = (RadioButton) flightLogEntryView.findViewById(R.id.fl_pf);
//        RadioButton pm = (RadioButton) flightLogEntryView.findViewById(R.id.fl_pm);
//        RadioButton sf = (RadioButton) flightLogEntryView.findViewById(R.id.fl_sf);
//        RadioButton sm = (RadioButton) flightLogEntryView.findViewById(R.id.fl_sm);
        //TODO have to implement the commented out stuff above
        leg.FlightType = Leg.FlightTypes.valueOfInt(flTypeGroup.getCheckedRadioButtonId());

        EditText flDeparture = (EditText) flightLogEntryView.findViewById(R.id.fl_dep);
        leg.Departure = flDeparture.getText().toString();

        EditText flBlockOut = (EditText) flightLogEntryView.findViewById(R.id.fl_blockout);
        leg.BlockOut = flBlockOut.getText().toString();

        EditText flArrival = (EditText) flightLogEntryView.findViewById(R.id.fl_arr);
        leg.Arrival = flArrival.getText().toString();

        EditText flBlockIn = (EditText) flightLogEntryView.findViewById(R.id.fl_blockin);
        leg.BlockIn = flBlockIn.getText().toString();

        EditText flInst = (EditText) flightLogEntryView.findViewById(R.id.fl_inst);
        leg.InstrumentTime = Double.parseDouble(flInst.getText().toString());

        EditText flApproaches = (EditText) flightLogEntryView.findViewById(R.id.fl_approaches);
        leg.Approaches = Double.parseDouble(flApproaches.getText().toString());

        EditText flNight = (EditText) flightLogEntryView.findViewById(R.id.fl_night);
        leg.Night= Double.parseDouble(flNight.getText().toString());

        CheckBox flNightTo = (CheckBox) flightLogEntryView.findViewById(R.id.fl_night_to);
        leg.NightTakeoff = flNightTo.isChecked();

        CheckBox flNightLndg = (CheckBox) flightLogEntryView.findViewById(R.id.fl_night_landing);
        leg.NightLanding = flNightLndg.isChecked();

        Leg.insertLeg(leg, mContext);
    }
}
