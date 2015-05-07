package com.kemble.crewlog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pete on 5/3/2015.
 */
public class LogbookOpenItemHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME = "logbook";
    private static final String LOGBOOK_TABLE_CREATE =
            "CREATE TABLE "+ TABLE_NAME + " (" +
                    "ID INTEGER PRIMARY KEY, " +
                    "DATE TIMESTAMP," +
                    "TAILNUMBER TEXT," +
                    "FLIGHTNUMBER TEXT," +
                    "CREWNAME TEXT," +
                    "TYPE INTEGER," +
                    "DEP TEXT," +
                    "BLOCKOUT TIMESTAMP," +
                    "ARR TEXT," +
                    "BLOCKIN TIMESTAMP," +
                    "INST INTEGER," +
                    "APPR INTEGER," +
                    "NIGHT BOOLEAN," +
                    "CREWMEAL INTEGER);";


    public LogbookOpenItemHelper(Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LOGBOOK_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void deleteTable(SQLiteDatabase db){
        db.execSQL("DROP TABLE logbook");
        onCreate(db);
    }
}
