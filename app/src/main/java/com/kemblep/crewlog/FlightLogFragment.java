package com.kemblep.crewlog;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import com.kemblep.crewlog.database.Commands;
import com.kemblep.crewlog.database.LogbookDbAdapter;
import com.kemblep.crewlog.obj.FlightLogEntry;
import com.kemblep.crewlog.obj.Leg;
import com.kemblep.crewlog.obj.LegArrayAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Pete on 5/3/2015.
 */
public class FlightLogFragment extends Fragment {

    public FlightLogFragment() {
    }

    private Date mDate;
    private SimpleDateFormat mSdf = new SimpleDateFormat("MM/dd/yyyy");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View vFlightLog = inflater.inflate(R.layout.fragment_flight_log, container, false);

        LogbookDbAdapter logbookDbAdapter = new LogbookDbAdapter(vFlightLog.getContext());
        SQLiteDatabase logbook = logbookDbAdapter.getWritableDatabase();

        FlightLogEntry entry = new FlightLogEntry();

        if(BuildConfig.DEBUG){
            setupDebug(vFlightLog, logbook, logbookDbAdapter, entry);
        }

        mDate = Calendar.getInstance().getTime();

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
                    }
            },year, month, day);
                datePickerDialog.show();

            }
        });

        //TODO get today's date so we can pull all the legs from that date
        if(entry.Legs == null || entry.Legs.size() == 0){
            //for now we'll just return empty
            return vFlightLog;
        }

        LegArrayAdapter legArrayAdapter = new LegArrayAdapter(vFlightLog.getContext(),entry.Legs);

        ListView lvLegs = (ListView) vFlightLog.findViewById(R.id.fl_flights_listview);
        lvLegs.setAdapter(legArrayAdapter);

        LogEntryFragment logEntryFragment = new LogEntryFragment();
        getChildFragmentManager().beginTransaction().add(R.id.fl_entry_fragment, logEntryFragment).commit();

        return vFlightLog;
    }

    private void setupDebug(View vFlightLog, SQLiteDatabase logbook, LogbookDbAdapter logbookDbAdapter, FlightLogEntry entry) {
        Button btnDelete = (Button) vFlightLog.findViewById(R.id.btn_delete_logbook);
        btnDelete.setVisibility(View.VISIBLE);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLogbook(v);
            }
        });

        entry.Date = new Date();
        entry.TailNumber = "N123AF";
        entry.FlightNumber = "CNS976";
        entry.CrewMember = "Bob Joe";
        Leg leg = new Leg();
        leg.Approaches = 0;
        leg.Arrival = "KASH";
        leg.Departure = "KBED";
        leg.BlockIn = new Date();
        leg.BlockOut = new Date();
        leg.FlightTime = 1.3;
        leg.InstrumentTime = 0.0;
        leg.Night = 0.0;
        entry.Legs.add(leg);
        entry.Duty = 24;
        entry.CrewMeals = 0;
        entry.Tips = 2;

        Commands.insertFlight(entry, vFlightLog.getContext());

//        ContentValues values = new ContentValues();
//        values.put("DATE" ,entry.Date.toString());
//        values.put("TAILNUMBER", entry.TailNumber);
//        values.put("FLIGHTNUMBER", entry.FlightNumber);
//        values.put("CREWNAME", entry.CrewMember);
//        values.put("TYPE", leg.FlightType);
//        values.put("DEP", leg.Departure);
//        values.put("BLOCKOUT", leg.BlockOut.hashCode());
//        values.put("DEP", leg.Arrival);
//        values.put("BLOCKIN", leg.BlockIn.hashCode());
//        values.put("INST", leg.InstrumentTime);
//        values.put("APPR", leg.Approaches);
//        values.put("NIGHT", leg.Night);
//        values.put("CREWMEAL", entry.CrewMeals);
//        //TODO add tip amount
//
//        logbook.insert(logbookDbAdapter.TABLE_NAME, null, values);
    }

    protected void deleteLogbook(View v){
        LogbookDbAdapter logbookDbAdapter = new LogbookDbAdapter(v.getContext());
        SQLiteDatabase db = logbookDbAdapter.getWritableDatabase();
        logbookDbAdapter.deleteTable(db);
    }

}
