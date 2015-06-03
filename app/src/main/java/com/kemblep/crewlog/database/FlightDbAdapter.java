package com.kemblep.crewlog.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kemblep.crewlog.obj.Flight;

/**
 * Created by Pete on 5/21/2015.
 */
public class FlightDbAdapter {

    private final Context mContext;
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;
    private String mTableName;

    public FlightDbAdapter(Context context){
        this.mContext = context;
    }

    public FlightDbAdapter open(){
        this.mDbHelper = new DbHelper(mContext);
        this.mDb = mDbHelper.getWritableDatabase();
        this.mTableName = mDbHelper.FLIGHT_TABLE_NAME;
        return this;
    }

    public long insertFlight(ContentValues values){
        return this.mDb.insert(this.mTableName, null, values);
    }

    public long updateFlight(ContentValues values, Integer id){
        String[] idString = new String[]{id.toString()};
        return this.mDb.update(this.mTableName, values, "ID=?", idString);
    }

    public Cursor getFlightById(Integer id){
        String[] idString = new String[]{ id.toString() };
        return this.mDb.query(this.mTableName, null, "ID=?", idString, null, null, null);
    }

    public Cursor getFlightsForSequence(Integer logbookId){
        String[] seq = new String[] {(logbookId.toString())};
        return this.mDb.query(this.mTableName, null, Flight.Columns.SEQUENCE+"=?", seq, null, null, null);
    }

    public void close(){
        this.mDb.close();
    }

}
