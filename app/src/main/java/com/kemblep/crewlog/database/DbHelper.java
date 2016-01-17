package com.kemblep.crewlog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kemblep.crewlog.obj.LogbookEntry;
import com.kemblep.crewlog.obj.Flight;

/**
 * Created by Pete on 5/3/2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String TAG = DbHelper.class.getName();
    public static final int DATABASE_VERSION = 2;
    public static String CREWLOG_DB_NAME = "crewlog";
    public static final String LOGBOOK_TABLE_NAME = "logbook";
    public static final String FLIGHT_TABLE_NAME = "Flights";
    private final String LOGBOOK_TABLE_CREATE =
            "CREATE TABLE "+ LOGBOOK_TABLE_NAME + " (" +
                    LogbookEntry.Columns.ID.name() + " INTEGER PRIMARY KEY, " +
                    LogbookEntry.Columns.DUTY.name() + " INTEGER," +
                    LogbookEntry.Columns.ENTRYDATE.name() + " TEXT," +
                    LogbookEntry.Columns.TAILNUMBER.name() + " TEXT," +
                    LogbookEntry.Columns.FLIGHTNUMBER.name() + " TEXT," +
                    LogbookEntry.Columns.PIC.name() + " TEXT," +
                    LogbookEntry.Columns.CREWMEAL.name() + " INTEGER, " +
                    LogbookEntry.Columns.EXPENSES.name() + " INTEGER, " +
                    LogbookEntry.Columns.REMARKS.name() + " TEXT);";

    private final String FLIGHT_TABLE_CREATE =
            "CREATE TABLE " + FLIGHT_TABLE_NAME + " (" +
                    Flight.Columns.ID.name() + " INTEGER PRIMARY KEY," +
                    Flight.Columns.SEQUENCE.name() + " INTEGER," +
                    Flight.Columns.FLIGHTTYPE.name() + " TEXT," +
                    Flight.Columns.DEP.name() + " TEXT," +
                    Flight.Columns.BLOCKOUT.name() + " TEXT," +
                    Flight.Columns.ARR.name() + " TEXT," +
                    Flight.Columns.BLOCKIN.name() + " TEXT," +
                    Flight.Columns.INST.name() + " INTEGER," +
                    Flight.Columns.APPR.name() + " INTEGER," +
                    Flight.Columns.NIGHT.name() + " INTEGER," +
                    Flight.Columns.NIGHT_TO.name() + " BOOLEAN," +
                    Flight.Columns.NIGHT_LNDG.name() + " BOOLEAN, " +
                    Flight.Columns.FLIGHTTIME.name() + " INTEGER);";

    public DbHelper(Context context) {
        super(context, CREWLOG_DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LOGBOOK_TABLE_CREATE);
        Log.d(TAG, "Created Logbook Table");
        db.execSQL(FLIGHT_TABLE_CREATE);
        Log.d(TAG, "Created Flights Table");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
