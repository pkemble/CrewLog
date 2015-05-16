package com.kemblep.crewlog.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kemblep.crewlog.obj.FlightLogEntry;
import com.kemblep.crewlog.obj.Leg;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by Pete on 5/7/2015.
 */
public class Commands {

    private final String INSERT_LOGBOOK_ENTRY = null;
    private final String INSERT_FLIGHT_ENTRY = null;
    private static final String TAG = Commands.class.getName();

    public static void insertFlight(FlightLogEntry logbookEntry, Context context) {
        try {
            LogbookDbAdapter logbookDbAdapter = new LogbookDbAdapter(context);
            SQLiteDatabase logbook = logbookDbAdapter.getWritableDatabase();

            ContentValues logbookEntryValues;
            
            logbookEntryValues = logbookEntry.getContentValues();

            long entryId = logbook.insert(logbookDbAdapter.LOGBOOK_TABLE_NAME, null, logbookEntryValues);

            for (int i = 0; i < logbookEntry.Legs.size(); i++){
                ContentValues flightEntryValues;
                flightEntryValues = logbookEntry.Legs.get(i).getContentValues(entryId);
                long flightEntryId = logbook.insert(logbookDbAdapter.FLIGHT_TABLE_NAME, null, flightEntryValues);
                Log.d(TAG, "Inserted flight entry ID " + flightEntryId + " for sequence " + logbookEntry.Legs.get(i).Sequence);
            }



        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public FlightLogEntry getFlightLogEntry(int id){
        return null;
    }

    public ArrayList<Leg> getLegsFromSequence(int seq){
        return null;
    }

    public Leg getLeg(int id, Context context){
        String[] idString = new String[]{Integer.toString(id)};

        LogbookDbAdapter logbookDbAdapter = new LogbookDbAdapter(context);
        SQLiteDatabase db = logbookDbAdapter.getReadableDatabase();
        Cursor c = db.query(logbookDbAdapter.FLIGHT_TABLE_NAME, null, "ID = ", idString, null, null, null);
        if(c == null){
            return null;
        }

        Leg leg = new Leg();
        if(c.moveToFirst()){
            do {
                leg.Id = c.getInt(c.getColumnIndexOrThrow("ID"));
                leg.Sequence = c.getInt(c.getColumnIndexOrThrow("SEQUENCE"));
                leg.FlightType = c.getInt(c.getColumnIndexOrThrow("TYPE"));
                leg.Departure = c.getString(c.getColumnIndexOrThrow("STRING"));
                leg.BlockOut = Timestamp.valueOf(c.getString(c.getColumnIndexOrThrow("BLOCKOUT")));
                leg.Arrival = c.getString(c.getColumnIndexOrThrow("ARR"));
                leg.BlockIn = Timestamp.valueOf(c.getString(c.getColumnIndexOrThrow("BLOCKIN")));
                leg.InstrumentTime = c.getDouble(c.getColumnIndexOrThrow("INST"));
                leg.Approaches = c.getDouble(c.getColumnIndexOrThrow("APPR"));
                leg.Night = c.getDouble(c.getColumnIndexOrThrow("NIGHT"));
                leg.NightTakeoff = c.getInt(c.getColumnIndexOrThrow("NIGHT_TO")) > 0;
                leg.NightLanding = c.getInt(c.getColumnIndexOrThrow("NIGHT_LNDG")) > 0;
                leg.FlightTime = c.getDouble(c.getColumnIndexOrThrow("FLIGHTTIME"));
            } while (c.moveToNext());

            if(c != null && !c.isClosed()){
                c.close();
            }
        }
        return leg;
    }
}
