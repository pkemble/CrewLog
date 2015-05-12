package com.kemblep.crewlog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Pete on 5/3/2015.
 */
public class LogbookOpenItemHelper extends SQLiteOpenHelper {

    private static final String TAG = LogbookOpenItemHelper.class.getName();
    private static final int DATABASE_VERSION = 2;
    public static final String LOGBOOK_TABLE_NAME = "logbook";
    public static final String FLIGHT_TABLE_NAME = "flights";
    private static final String LOGBOOK_TABLE_CREATE =
            "CREATE TABLE "+ LOGBOOK_TABLE_NAME + " (" +
                    "ID INTEGER PRIMARY KEY, " +
                    "DUTY INTEGER," +
                    "DATE TIMESTAMP," +
                    "TAILNUMBER TEXT," +
                    "FLIGHTNUMBER TEXT," +
                    "CREWNAME TEXT," +
                    "CREWMEAL INTEGER, " +
                    "TIPS INTEGER);";
    private static final String FLIGHT_TABLE_CREATE =
            "CREATE TABLE " + FLIGHT_TABLE_NAME + " (" +
                    "ID INTEGER PRIMARY KEY," +
                    "SEQUENCE INTEGER," +
                    "TYPE INTEGER," +
                    "DEP TEXT," +
                    "BLOCKOUT TIMESTAMP," +
                    "ARR TEXT," +
                    "BLOCKIN TIMESTAMP," +
                    "INST INTEGER," +
                    "APPR INTEGER," +
                    "NIGHT BOOLEAN, " +
                    "FLIGHTTIME INTEGER);";

    public LogbookOpenItemHelper(Context context) {
        super(context, LOGBOOK_TABLE_NAME, null, DATABASE_VERSION);
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

    public void deleteTable(SQLiteDatabase db){
        db.execSQL("DROP TABLE " + LOGBOOK_TABLE_NAME);
        db.execSQL("DROP TABLE " + FLIGHT_TABLE_NAME);
        Log.d(TAG, "Dropped both tables");
        onCreate(db);
    }
}
