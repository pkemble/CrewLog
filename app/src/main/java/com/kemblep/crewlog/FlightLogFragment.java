package com.kemblep.crewlog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.kemblep.crewlog.database.LogbookDbAdapter;
import com.kemblep.crewlog.obj.FlightLogEntry;
import com.kemblep.crewlog.obj.Leg;
import com.kemblep.crewlog.obj.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Pete on 5/3/2015.
 */
public class FlightLogFragment extends Fragment {

    public FlightLogFragment() {
    }

    private final String TAG = FlightLogFragment.class.getName();
    private Date mDate;
    private SimpleDateFormat mSdf = new SimpleDateFormat("MM/dd/yyyy");
    private Context mContext;
    private FlightLogEntry mFlightLogEntry;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View vFlightLog = inflater.inflate(R.layout.fragment_flight_log, container, false);
        mContext = vFlightLog.getContext();
        LogbookDbAdapter logbookDbAdapter = new LogbookDbAdapter(vFlightLog.getContext());
        SQLiteDatabase logbook = logbookDbAdapter.getWritableDatabase();

        if(BuildConfig.DEBUG){
            setupDebug(vFlightLog, logbook, logbookDbAdapter);
        }

        if (mDate == null) {
            mDate = Calendar.getInstance().getTime();
        };

        if(mFlightLogEntry != null){
            return vFlightLog;
        }

        final EditText flDate = (EditText) vFlightLog.findViewById(R.id.fl_date);

        flDate.setText(mSdf.format(mDate));

        flDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(vFlightLog.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        mDate = newDate.getTime();
                        flDate.setText(mSdf.format(mDate));
                        getLegsForDate(vFlightLog, mDate);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        return getLegsForDate(vFlightLog, mDate);
    }

    private View getLegsForDate(View vFlightLog, Date dateOfLegs){
        Integer simpleDate = Util.CustomIntDate(dateOfLegs);
        Log.d(TAG, "Getting legs for " + simpleDate);

        final FlightLogEntry entry = FlightLogEntry.getFlightLogEntry(simpleDate, vFlightLog.getContext());

        //setup submit/update button
        Button btnSubmit = (Button) vFlightLog.findViewById(R.id.btn_submit_flightlog);
        btnSubmit.setEnabled(false);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFlightLog(v);
            }
        });

        //populate this view
        populateLogEntry(vFlightLog, entry);

        LinearLayout flLinearLayout = (LinearLayout) vFlightLog.findViewById(R.id.fl_flight_list);
        for(int i = 0; i < entry.Legs.size(); i++){
            final Leg leg = entry.Legs.get(i);
            Button btnLeg = new Button(mContext);

            String legTitle = "Leg " + (i + 1);
            if(leg.Departure != null && leg.Arrival != null){
                legTitle += " " + leg.Departure + " -> " + leg.Arrival;
            }
            btnLeg.setText(legTitle);
            btnLeg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putInt(Leg.Columns.ID.name(), leg.Id);
                    createEntryFragment(b);

                    return;
                }
            });
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            flLinearLayout.addView(btnLeg, new ViewGroup.LayoutParams(lp));
        }

        //remove leg buttons
        if(entry.Legs == null || entry.Legs.size() == 0){
            flLinearLayout.removeAllViews();
        }

        Button btnAddFlight = (Button) vFlightLog.findViewById(R.id.btn_add_flight);
        btnAddFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(entry.Id == null){
                    entry.Date = Util.CustomIntDate(mDate);
                    long newEntryId = entry.insertFlight(mContext);
                    Bundle b = new Bundle();
                    b.putInt("SEQUENCE", (int) newEntryId);
                    createEntryFragment(b);
                }
            }
        });

        return vFlightLog;
    }

    private void submitFlightLog(View vFlightLogEntry) {
        EditText flTailNumber = (EditText) vFlightLogEntry.findViewById(R.id.fl_tail_number);
        EditText flFlightNumber = (EditText) vFlightLogEntry.findViewById(R.id.fl_flight_number);
        EditText flCrewMember = (EditText) vFlightLogEntry.findViewById(R.id.fl_crew_member);
        EditText flCrewMeals = (EditText) vFlightLogEntry.findViewById(R.id.fl_crew_meals);
        EditText flTips = (EditText) vFlightLogEntry.findViewById(R.id.fl_tips);
        EditText flRemarks = (EditText) vFlightLogEntry.findViewById(R.id.fl_remarks);

        FlightLogEntry entry = new FlightLogEntry();

        entry.Date = Util.CustomIntDate(mDate);
        entry.TailNumber = flTailNumber.getText().toString();
        entry.FlightNumber = flFlightNumber.getText().toString();
        entry.CrewMember = flCrewMember.getText().toString();
        entry.CrewMeals = Long.parseLong(flCrewMeals.getText().toString());
        entry.Tips = Long.parseLong(flTips.getText().toString());
        entry.Remarks = flRemarks.getText().toString();

        entry.insertFlight(mContext);

        Button btnAddLegs = (Button) vFlightLogEntry.findViewById(R.id.btn_add_flight);
        btnAddLegs.setEnabled(true);

        return;
    }

    private void populateLogEntry(View vFlightLog, FlightLogEntry entry) {
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
        LogEntryFragment logEntryFragment = new LogEntryFragment();
        logEntryFragment.setArguments(b);

        FragmentManager fm = ((CrewLog) mContext).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_main, logEntryFragment);
        ft.addToBackStack("Leg Entry");
        ft.commit();
    }

    private void setupDebug(final View vFlightLog, SQLiteDatabase logbook, LogbookDbAdapter logbookDbAdapter){
        Button btnDelete = (Button) vFlightLog.findViewById(R.id.btn_delete_logbook);
        btnDelete.setVisibility(View.VISIBLE);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLogbook(v);
            }
        });

        Button btnExport = (Button) vFlightLog.findViewById(R.id.btn_export_logbook);
        btnExport.setVisibility(View.VISIBLE);
        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    backupDatabase();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        deleteLogbook(vFlightLog);
        makeDummyLogbook(vFlightLog);

    }

    private void makeDummyLogbook(View vFlightLog){
        FlightLogEntry fakeEntry = new FlightLogEntry();

        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, Calendar.MAY);
        c.set(Calendar.DAY_OF_MONTH, 16);
        c.set(Calendar.YEAR, 2015);
        Date d = c.getTime();


        fakeEntry.TailNumber = "N123AF";
        fakeEntry.Date = Util.CustomIntDate(d);
        fakeEntry.FlightNumber = "CNS976";
        fakeEntry.CrewMember = "Bob Joe";

        Leg leg = new Leg();
        leg.Approaches = 0;
        leg.Arrival = "KASH";
        leg.Departure = "KBED";
        leg.BlockIn = "1400";
        leg.BlockOut = "1430";
        leg.FlightTime = 1.3;
        leg.FlightType = Leg.FlightTypes.PM;
        leg.InstrumentTime = 0.0;
        leg.Night = 0.0;

        Leg leg2 = new Leg();
        leg2.Approaches = 0;
        leg2.Arrival = "KBED";
        leg2.Departure = "KLWM";
        leg2.BlockIn = "1534";
        leg2.BlockOut = "1650";
        leg2.FlightTime = 1.3;
        leg2.FlightType = Leg.FlightTypes.PF;
        leg2.InstrumentTime = 0.0;
        leg2.Night = 0.3;
        leg2.NightLanding = true;

        fakeEntry.Legs.add(leg);
        fakeEntry.Legs.add(leg2);
        fakeEntry.Duty = 24;
        fakeEntry.CrewMeals = 0;
        fakeEntry.Tips = 2;

        fakeEntry.insertFlight(vFlightLog.getContext());
    }

    protected void deleteLogbook(View v){
        LogbookDbAdapter logbookDbAdapter = new LogbookDbAdapter(v.getContext());
        SQLiteDatabase db = logbookDbAdapter.getWritableDatabase();
        logbookDbAdapter.dropTables(db);
        makeDummyLogbook(v);
    }

    public static void backupDatabase() throws IOException {
        //Open your local db as the input stream
        String inFileName = "/data/data/com.kemblep.crewlog/databases/crewlog";
        File dbFile = new File(inFileName);
        FileInputStream fis = new FileInputStream(dbFile);

        String outFileName = Environment.getExternalStorageDirectory()+"/crewlog";
        //Open the empty db as the output stream
        OutputStream output = new FileOutputStream(outFileName);
        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer))>0){
            output.write(buffer, 0, length);
        }
        //Close the streams
        output.flush();
        output.close();
        fis.close();
    }
}
