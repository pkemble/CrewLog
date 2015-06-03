package com.kemblep.crewlog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.kemblep.crewlog.obj.Flight;
import com.kemblep.crewlog.obj.LogbookEntry;
import com.kemblep.crewlog.obj.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Pete on 5/3/2015.
 */
public class LogbookFragment extends Fragment {

    public LogbookFragment() {
    }

    private final String TAG = LogbookFragment.class.getName();
    private Date mDate;
    private SimpleDateFormat mSdf = new SimpleDateFormat("MM/dd/yyyy");
    private Context mContext;
    private LogbookEntry mLogbookEntry;
    private View mFlightLogView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mFlightLogView = inflater.inflate(R.layout.fragment_flight_log, container, false);

        mContext = mFlightLogView.getContext();

        mLogbookEntry = new LogbookEntry(mContext);

        //setup submit/update button
        Button btnSubmit = (Button) mFlightLogView.findViewById(R.id.btn_update_flightlog);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFlightLog(mFlightLogView);
            }
        });

        if (mDate == null) {
            mDate = Calendar.getInstance().getTime();
        };

        final EditText flDate = (EditText) mFlightLogView.findViewById(R.id.fl_date);

        flDate.setText(mSdf.format(mDate));

        flDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(mFlightLogView.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        mDate = newDate.getTime();
                        flDate.setText(mSdf.format(mDate));
                        getLegsForDate(mFlightLogView, mDate);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        return getLegsForDate(mFlightLogView, mDate);
    }

    private void getLogbookEntry(Date date) {
        //get an array (mostly just one) of logbook entries
        final ArrayList<LogbookEntry> logEntries = LogbookEntry.getLogbookEntry(date, mContext);
        //check if there's more than one returned
        if(logEntries.size() == 1){
            mLogbookEntry = logEntries.get(0);
        }
        //pop up a list dialog to pick the right one
        final AlertDialog.Builder bSingle = new AlertDialog.Builder(mContext);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.select_dialog_singlechoice);
        LogbookEntry selectedEntry;

        for(LogbookEntry e : logEntries){
            String lineItem = e.EntryDate;
            if(e.TailNumber != null){
                lineItem += " : " + e.TailNumber;
            }
            arrayAdapter.add(lineItem);
        }

        bSingle.setSingleChoiceItems(arrayAdapter, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mLogbookEntry = logEntries.get(which);
                dialog.dismiss();
                populateLogEntry(mFlightLogView, mLogbookEntry);
            }
        });
        AlertDialog dialog = bSingle.create();
        dialog.show();
    }

    private View getLegsForDate(final View vFlightLog, Date dateOfLegs){
        Log.d(TAG, "Getting legs for " + mSdf.format(dateOfLegs));
        getLogbookEntry(dateOfLegs);

        //populate this view
        populateLogEntry(vFlightLog, mLogbookEntry);

        //setup button
        Button btnSubmit = (Button) vFlightLog.findViewById(R.id.btn_update_flightlog);
        if(mLogbookEntry.Id != null){
            btnSubmit.setText("Update Logbook Entry");
        } else {
            btnSubmit.setText("Submit Logbook Entry");
        }

        LinearLayout flLinearLayout = (LinearLayout) vFlightLog.findViewById(R.id.fl_flight_list);

        for(int i = 0; i < mLogbookEntry.Flights.size(); i++){
            final Flight flight = mLogbookEntry.Flights.get(i);
            Button btnLeg = new Button(mContext);

            String legTitle = "Flight " + (i + 1);
            if(flight.Departure != null && flight.Arrival != null){
                legTitle += " " + flight.Departure + " -> " + flight.Arrival;
            }
            btnLeg.setText(legTitle);
            btnLeg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putInt(Flight.Columns.ID.name(), flight.Id);
                    createEntryFragment(b);

                    return;
                }
            });

            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            flLinearLayout.addView(btnLeg, new ViewGroup.LayoutParams(lp));
        }

        //remove leg buttons if current log has no legs
        if(mLogbookEntry.Flights.size() == 0){
            flLinearLayout.removeAllViews();
        }

        Button btnAddFlight = (Button) vFlightLog.findViewById(R.id.btn_add_flight);
        btnAddFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                //TODO this can be done better
                //insert the logbook entry first, then go to the flight entry fragment
                if (mLogbookEntry.Id == null) {
                    mLogbookEntry.EntryDate = Util.CustomSimpleDate(mDate);
                    long newEntryId = submitFlightLog(vFlightLog);
                    b.putInt("SEQUENCE", (int) newEntryId);
                } else {
                    b.putInt("SEQUENCE", mLogbookEntry.Id);
                }
                createEntryFragment(b);
            }
        });

        return vFlightLog;
    }

    private long submitFlightLog(View vFlightLogEntry) {
        EditText flTailNumber = (EditText) vFlightLogEntry.findViewById(R.id.fl_tail_number);
        EditText flFlightNumber = (EditText) vFlightLogEntry.findViewById(R.id.fl_flight_number);
        EditText flCrewMember = (EditText) vFlightLogEntry.findViewById(R.id.fl_crew_member);
        EditText flCrewMeals = (EditText) vFlightLogEntry.findViewById(R.id.fl_crew_meals);
        EditText flTips = (EditText) vFlightLogEntry.findViewById(R.id.fl_tips);
        EditText flRemarks = (EditText) vFlightLogEntry.findViewById(R.id.fl_remarks);

        mLogbookEntry.EntryDate = Util.CustomSimpleDate(mDate);
        mLogbookEntry.TailNumber = flTailNumber.getText().toString();
        mLogbookEntry.FlightNumber = flFlightNumber.getText().toString();
        mLogbookEntry.CrewMember = flCrewMember.getText().toString();
        mLogbookEntry.CrewMeals = Long.parseLong(flCrewMeals.getText().toString());
        mLogbookEntry.Tips = Long.parseLong(flTips.getText().toString());
        mLogbookEntry.Remarks = flRemarks.getText().toString();

        return mLogbookEntry.insertLogbookEntry();
    }

    private void populateLogEntry(View vFlightLog, LogbookEntry entry) {
        EditText flTailNumber = (EditText) vFlightLog.findViewById(R.id.fl_tail_number);
        EditText flFlightNumber = (EditText) vFlightLog.findViewById(R.id.fl_flight_number);
        EditText flCrewMember = (EditText) vFlightLog.findViewById(R.id.fl_crew_member);
        EditText flCrewMeals = (EditText) vFlightLog.findViewById(R.id.fl_crew_meals);
        EditText flTips = (EditText) vFlightLog.findViewById(R.id.fl_tips);
        EditText flRemarks = (EditText) vFlightLog.findViewById(R.id.fl_remarks);

        flTailNumber.setText(entry.TailNumber);
        flFlightNumber.setText(entry.FlightNumber);
        flCrewMember.setText(entry.CrewMember);
        flCrewMeals.setText(Long.toString(entry.CrewMeals)); //TODO test this
        flTips.setText(Long.toString(entry.Tips));
        flRemarks.setText(entry.Remarks);
        return;
    }

    private void createEntryFragment(Bundle b){
        FlightEntryFragment flightEntryFragment = new FlightEntryFragment();
        flightEntryFragment.setArguments(b);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fl_main, flightEntryFragment);
        ft.addToBackStack("Flight Entry");
        ft.commit();
    }
}
