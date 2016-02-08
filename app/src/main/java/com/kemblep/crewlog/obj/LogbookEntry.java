package com.kemblep.crewlog.obj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.kemblep.crewlog.database.FlightDbAdapter;
import com.kemblep.crewlog.database.LogbookDbAdapter;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Pete on 5/3/2015.
 */
public class LogbookEntry {
    private static final String TAG = LogbookEntry.class.getName();

    public Integer Id;
    public String EntryDate;
    public String TailNumber;
    public String FlightNumber;
    public String CrewMember;
    public boolean Pic;
    public ArrayList<Flight> Flights = new ArrayList<>();
    public long Duty;
    public long CrewMeals;
    public long Expenses;
    public String Remarks;

    public LogbookEntry() {
    }

    public enum Columns {
        ID,
        DUTY,
        ENTRYDATE,
        TAILNUMBER,
        FLIGHTNUMBER,
        PIC,
        CREWMEMBER,
        CREWMEAL,
        EXPENSES,
        REMARKS;
    }
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        if(EntryDate != null) {
            values.put(Columns.ENTRYDATE.name(), EntryDate);
        }
        if(TailNumber != null) {
            values.put(Columns.TAILNUMBER.name(), TailNumber);
        }
        if(FlightNumber != null) {
            values.put(Columns.FLIGHTNUMBER.name(), FlightNumber);
        }
        if(CrewMember != null) { //TODO: left/right seat determination
            values.put(Columns.PIC.name(), CrewMember);
        }

        values.put(Columns.DUTY.name(), Duty);
        values.put(Columns.CREWMEAL.name(), CrewMeals);
        values.put(Columns.EXPENSES.name(), Expenses);
        values.put(Columns.REMARKS.name(), Remarks);

        return values;
    }

    public long insertLogbookEntry(Context context) {
        try {

            //TODO handle multiple entries for the same date and tailnumber
            LogbookDbAdapter lba = new LogbookDbAdapter(context);
            lba.open();
            long entryId = lba.insertLogbookEntry(this.getContentValues());

            for (int i = 0; i < this.Flights.size(); i++){
                //add the sequence to the leg
                Flight l = this.Flights.get(i);
                l.Sequence = entryId;
                Flight.insertFlight(l, context);
            }

            lba.close();

            return entryId;

        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return -1;
    }

    public long updateLogbookEntry(Context context) {
        try {

            //TODO handle multiple entries for the same date
            LogbookDbAdapter lba = new LogbookDbAdapter(context);
            lba.open();
            long entryId = lba.updateLogbookEntry(this.getContentValues());
            lba.close();
            return entryId;

        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return -1;
    }

    public void deleteLogbookEntry(Context context) {
        try {
            LogbookDbAdapter dba = new LogbookDbAdapter(context);
            dba.open();
            dba.deleteLogbookEntry(this.Id);

            //get them flights too
            for(Flight f : this.Flights){
                f.deleteFlight(context);
            }

            Log.d(TAG, "Deleted logbook entry ID " + this.Id);
        } catch (Exception e){
            Log.e(TAG, "Unable to delete entry ID " + this.Id);
            Log.e(TAG, e.getMessage());
        }

    }


    public static ArrayList<LogbookEntry> getLogbookEntry(Date date, String tailNumber, Context context){
        String simpleDate = Util.CustomSimpleDate(date);
        LogbookDbAdapter dba = new LogbookDbAdapter(context);
        dba.open();
        Cursor l = tailNumber != null ? dba.getLogbookEntry(simpleDate, tailNumber)
                : dba.getLogbookEntry(simpleDate);
        //TODO handle counts > 1
        if(l.getCount() > 1){
            Log.d(TAG, "Found " + l.getCount() + " Logbok entries for " + date);
        }

        ArrayList<LogbookEntry> entries = new ArrayList<>();

        if(l.moveToFirst()) {
            do {
                LogbookEntry cursorEntry = new LogbookEntry();
                cursorEntry.Id = l.getInt(l.getColumnIndexOrThrow(Columns.ID.name()));
                cursorEntry.Duty = l.getLong(l.getColumnIndexOrThrow(Columns.DUTY.name()));
                cursorEntry.EntryDate = l.getString(l.getColumnIndexOrThrow(Columns.ENTRYDATE.name()));
                cursorEntry.TailNumber = l.getString(l.getColumnIndexOrThrow(Columns.TAILNUMBER.name()));
                cursorEntry.FlightNumber = l.getString(l.getColumnIndexOrThrow(Columns.FLIGHTNUMBER.name()));
                cursorEntry.CrewMember = l.getString(l.getColumnIndexOrThrow(Columns.PIC.name())); //TODO: sort out pic/sic
                cursorEntry.CrewMeals = l.getLong(l.getColumnIndexOrThrow(Columns.CREWMEAL.name()));
                cursorEntry.Expenses = l.getLong(l.getColumnIndexOrThrow(Columns.EXPENSES.name()));

                //add the legs
                FlightDbAdapter flightDbAdapter = new FlightDbAdapter(context);
                flightDbAdapter.open();
                Cursor f = flightDbAdapter.getFlightsForSequence(cursorEntry.Id);
                cursorEntry.Flights.addAll(Flight.flightsFromCursor(f));
                flightDbAdapter.close();

                entries.add(cursorEntry);
            } while (l.moveToNext());

            if (l != null && !l.isClosed()) {
                l.close();
            }
        }
        return entries;
    }
}
