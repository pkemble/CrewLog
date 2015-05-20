package com.kemblep.crewlog.obj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kemblep.crewlog.database.LogbookDbAdapter;

import java.util.ArrayList;

/**
 * Created by Pete on 5/3/2015.
 */
public class FlightLogEntry {
    private static final String TAG = FlightLogEntry.class.getName();

    public Integer Id;
    public Integer Date;
    public String TailNumber;
    public String FlightNumber;
    public String CrewMember;
    public ArrayList<Leg> Legs = new ArrayList<>();
    public long Duty;
    public long CrewMeals;
    public long Tips;
    public String Remarks;

    public FlightLogEntry(){
    }

    public enum Columns {
        ID,
        DUTY,
        DATE,
        TAILNUMBER,
        FLIGHTNUMBER,
        CREWNAME,
        CREWMEAL,
        TIPS,
        REMARKS
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        if(Date != null) {
            values.put(Columns.DATE.name(), Date);
        }
        if(TailNumber != null) {
            values.put(Columns.TAILNUMBER.name(), TailNumber);
        }
        if(FlightNumber != null) {
            values.put(Columns.FLIGHTNUMBER.name(), FlightNumber);
        }
        if(CrewMember != null) {
            values.put(Columns.CREWNAME.name(), CrewMember);
        }

        values.put(Columns.DUTY.name(), Duty);
        values.put(Columns.CREWMEAL.name(), CrewMeals);
        values.put(Columns.TIPS.name(), Tips);
        values.put(Columns.REMARKS.name(), Remarks);

        return values;
    }

    public long insertFlight(Context context) {
        try {

            //TODO handle multiple entries for the same date
            LogbookDbAdapter logbookDbAdapter = new LogbookDbAdapter(context);
            SQLiteDatabase logbook = logbookDbAdapter.getWritableDatabase();

            ContentValues logbookEntryValues;

            logbookEntryValues = this.getContentValues();

            long entryId = logbook.insert(logbookDbAdapter.LOGBOOK_TABLE_NAME, null, logbookEntryValues);

            for (int i = 0; i < this.Legs.size(); i++){
                //add the sequence to the leg
                Leg l = this.Legs.get(i);
                l.Sequence = entryId;
                Leg.insertLeg(l, context);
            }

            logbook.close();

            return entryId;

        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return -1;
    }

    public static FlightLogEntry getFlightLogEntry(Integer simpleDate, Context context){

        FlightLogEntry flightLogEntry = new FlightLogEntry();

        String[] dates = new String[] {simpleDate.toString()};

        LogbookDbAdapter logbookDbAdapter = new LogbookDbAdapter(context);
        SQLiteDatabase db = logbookDbAdapter.getReadableDatabase();

        Cursor l = db.query(logbookDbAdapter.LOGBOOK_TABLE_NAME, null, "DATE = ?", dates, null, null, null);
        //TODO handle counts > 1
        if(l.moveToFirst()){
            do {
                flightLogEntry.Id = l.getInt(l.getColumnIndexOrThrow(Columns.ID.name()));
                flightLogEntry.Duty = l.getLong(l.getColumnIndexOrThrow(Columns.DUTY.name()));
                flightLogEntry.Date = l.getInt(l.getColumnIndexOrThrow(Columns.DATE.name()));
                flightLogEntry.TailNumber = l.getString(l.getColumnIndexOrThrow(Columns.TAILNUMBER.name()));
                flightLogEntry.FlightNumber = l.getString(l.getColumnIndexOrThrow(Columns.FLIGHTNUMBER.name()));
                flightLogEntry.CrewMember = l.getString(l.getColumnIndexOrThrow(Columns.CREWNAME.name()));
                flightLogEntry.CrewMeals = l.getLong(l.getColumnIndexOrThrow(Columns.CREWMEAL.name()));
                flightLogEntry.Tips = l.getLong(l.getColumnIndexOrThrow(Columns.TIPS.name()));
            } while(l.moveToNext());

            if(l != null && !l.isClosed()){
                l.close();
            }

            //add the legs

            String[] seq = new String[] {(flightLogEntry.Id.toString())};
            Cursor f = db.query(logbookDbAdapter.FLIGHT_TABLE_NAME, null, Leg.Columns.SEQUENCE+"=?", seq, null, null, null);

            flightLogEntry.Legs.addAll(Leg.legFromCursor(f));

        } else if(l.getCount() == 0) {
            //it's an empty entry
            //l = db.query(logbookDbAdapter.FLIGHT_TABLE_NAME, )
        }

        return flightLogEntry;
    }
}
