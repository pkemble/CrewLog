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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.kemblep.crewlog.obj.EntryAdapter;
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
    private boolean mAdditionalEntry;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        setHasOptionsMenu(true);
        mFlightLogView = inflater.inflate(R.layout.fragment_logbook, container, false);

        mContext = mFlightLogView.getContext();

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
                        setupLogbookFragment();
                    }

                }, year, month, day);
                datePickerDialog.show();
            }
        });

        setupLogbookFragment();

        return mFlightLogView;
    }

    private void setupLogbookFragment(){
        mLogbookEntry = new LogbookEntry();

        Log.d(TAG, "Getting legs for " + mSdf.format(mDate));
        getLogbookEntry(mDate);

        //populate this view
        populateLogEntry();
    }

    private void getLogbookEntry(Date date){
        getLogbookEntry(date, null);
    }

    private void getLogbookEntry(Date date, String tailNumber) {
        //get an array of logbook entries for the date / tail
        final ArrayList<LogbookEntry> logEntries = LogbookEntry.getLogbookEntry(date, tailNumber, mContext);
        //check if there's more than one returned
        if(logEntries.size() > 1){
            //pop up a list dialog to pick the right one
            final AlertDialog.Builder bSingle = new AlertDialog.Builder(mContext);
            EntryAdapter entryAdapter = new EntryAdapter(mContext, logEntries);

            bSingle.setTitle(R.string.select_multiple_title);
            bSingle.setSingleChoiceItems(entryAdapter, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mLogbookEntry = logEntries.get(which);
                    dialog.dismiss();
                    populateLogEntry();
                }
            });

            AlertDialog dialog = bSingle.create();
            dialog.show();

        } else if(logEntries.size() == 1) {
            mLogbookEntry = logEntries.get(0);
        }
    }

    private long submitFlightLog(){
        return submitFlightLog(false);
    }

    private long submitFlightLog(boolean createAdditionalEntry) {
        getFormValues();

        long l;

        if(createAdditionalEntry){
            l = mLogbookEntry.insertLogbookEntry(mContext);
            populateLogEntry();
            return l;
        }
        ArrayList<LogbookEntry> dupes = LogbookEntry.getLogbookEntry(mDate, mLogbookEntry.TailNumber, mContext);
        if(dupes.size() > 0){
            l= mLogbookEntry.updateLogbookEntry(mContext);
        } else {
            l = mLogbookEntry.insertLogbookEntry(mContext);
        }
        populateLogEntry();
        return l;
    }

    private void deleteFlightLog() {
        String entryToDelete, crew = null;
        if(mLogbookEntry.CrewMember != null){
            crew = " with " + mLogbookEntry.CrewMember;
        }
        entryToDelete= mLogbookEntry.EntryDate + "\n" +
                mLogbookEntry.TailNumber + crew + "\n" +
                "; " + mLogbookEntry.Flights.size() + " flight(s)";

        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setCancelable(true)
                .setIcon(R.drawable.ic_report_problem_black_24dp)
                .setTitle("Confirm Deleting:")
                .setMessage(entryToDelete)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mLogbookEntry.deleteLogbookEntry(mContext);
                        dialog.dismiss();
                        setupLogbookFragment();
                    }

                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();



    }

    private void getFormValues(){
        View vFlightLogEntry = this.getView();
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
        mLogbookEntry.Expenses = Long.parseLong(flTips.getText().toString());
        mLogbookEntry.Remarks = flRemarks.getText().toString();
    }

    private void populateLogEntry() {
        EditText flTailNumber = (EditText) mFlightLogView.findViewById(R.id.fl_tail_number);
        EditText flFlightNumber = (EditText) mFlightLogView.findViewById(R.id.fl_flight_number);
        EditText flCrewMember = (EditText) mFlightLogView.findViewById(R.id.fl_crew_member);
        CheckBox cbSeat = (CheckBox) mFlightLogView.findViewById(R.id.cb_seat);
        EditText flCrewMeals = (EditText) mFlightLogView.findViewById(R.id.fl_crew_meals);
        EditText flTips = (EditText) mFlightLogView.findViewById(R.id.fl_tips);
        EditText flRemarks = (EditText) mFlightLogView.findViewById(R.id.fl_remarks);

        flTailNumber.setText("");
        flFlightNumber.setText("");
        flCrewMember.setText("");
        cbSeat.setChecked(false);
        flCrewMeals.setText("");
        flTailNumber.setText("");
        flRemarks.setText("");

        flTailNumber.setText(mLogbookEntry.TailNumber);
        flFlightNumber.setText(mLogbookEntry.FlightNumber);
        flCrewMember.setText(mLogbookEntry.CrewMember);
        cbSeat.setChecked(mLogbookEntry.Pic);
        flCrewMeals.setText(Long.toString(mLogbookEntry.CrewMeals)); //TODO test this
        flTips.setText(Long.toString(mLogbookEntry.Expenses));
        flRemarks.setText(mLogbookEntry.Remarks);

        setupSubmitUpdateButton();

        setupLegButtons();

        return;
    }

    private void setupLegButtons() {
        LinearLayout flLinearLayout = (LinearLayout) mFlightLogView.findViewById(R.id.fl_flight_list);

        flLinearLayout.removeAllViews();

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

        Button btnAddFlight = (Button) mFlightLogView.findViewById(R.id.btn_add_flight);
        btnAddFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                //TODO this can be done better
                //insert the logbook entry first, then go to the flight entry fragment
                if (mLogbookEntry.Id == null) {
                    mLogbookEntry.EntryDate = Util.CustomSimpleDate(mDate);
                    long newEntryId = submitFlightLog();
                    b.putInt("SEQUENCE", (int) newEntryId);
                } else {
                    //update the logbook entry
                    submitFlightLog(); //TODO seems a little auspicious that mLogbookEntry is being updated here
                    b.putInt("SEQUENCE", mLogbookEntry.Id);
                }
                createEntryFragment(b);
            }
        });
    }

    private void setupSubmitUpdateButton() {
        Button btnSubmit = (Button) mFlightLogView.findViewById(R.id.btn_update_flightlog);
        if(mLogbookEntry.Id != null){
            btnSubmit.setText("Update Logbook Entry");
        } else {
            btnSubmit.setText("Submit Logbook Entry");
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFlightLog();
            }
        });
    }

    private void createEntryFragment(Bundle b){
        FlightEntryFragment flightEntryFragment = new FlightEntryFragment();
        flightEntryFragment.setArguments(b);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fl_main, flightEntryFragment);
        ft.addToBackStack("Flight Entry");
        ft.commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_logbook_entry, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_add_entry:
                mLogbookEntry = new LogbookEntry();
                mDate = new Date();
                submitFlightLog(true);
                break;
            case R.id.action_delete_entry:
                deleteFlightLog();
        }

        return super.onOptionsItemSelected(item);
    }
}
