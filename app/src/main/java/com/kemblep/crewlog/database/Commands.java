package com.kemblep.crewlog.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kemblep.crewlog.obj.FlightLogEntry;

/**
 * Created by Pete on 5/7/2015.
 */
public class Commands {

    private final String INSERT_LOGBOOK_ENTRY = null;
    private final String INSERT_FLIGHT_ENTRY = null;
    private static final String TAG = Commands.class.getName();

    public static void insertFlight(FlightLogEntry logbookEntry, Context context) {
        try {
            LogbookOpenItemHelper logbookOpenItemHelper = new LogbookOpenItemHelper(context);
            SQLiteDatabase logbook = logbookOpenItemHelper.getWritableDatabase();

            ContentValues logbookEntryValues;
            
            logbookEntryValues = logbookEntry.getContentValues();

            long entryId = logbook.insert(logbookOpenItemHelper.LOGBOOK_TABLE_NAME, null, logbookEntryValues);

            for (int i = 0; i < logbookEntry.Legs.size(); i++){
                ContentValues flightEntryValues;
                flightEntryValues = logbookEntry.Legs.get(i).getContentValues(entryId);
                long flightEntryId = logbook.insert(logbookOpenItemHelper.FLIGHT_TABLE_NAME, null, flightEntryValues);
                Log.d(TAG, "Inserted flight entry ID " + flightEntryId + " for sequence " + logbookEntry.Legs.get(i).Sequence);
            }



        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
