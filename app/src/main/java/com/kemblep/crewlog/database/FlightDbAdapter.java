package com.kemblep.crewlog.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kemblep.crewlog.obj.Flight;

/**
 * Created by Pete on 5/21/2015.
 */
public class FlightDbAdapter {

    private final Context mContext;
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;
    private String mTableName;

    private final static String TAG = FlightDbAdapter.class.getName();

    public FlightDbAdapter(Context context){
        this.mContext = context;
    }

    public FlightDbAdapter open(){
        if(this.mContext == null){
            Log.e(TAG, "Context is null - Instantiate Adapter class first.");
            return null;
        }
        this.mDbHelper = new DbHelper(mContext);
        this.mDb = mDbHelper.getWritableDatabase();
        this.mTableName = mDbHelper.FLIGHT_TABLE_NAME;
        return this;
    }

    public long insertFlight(ContentValues values){
        if(mDb == null){
            Log.e(TAG, "Database null; invoke .open() first.");
            return -1;
        }
        return this.mDb.insert(this.mTableName, null, values);
    }

    public long updateFlight(ContentValues values, Integer id){
        String[] idString = new String[]{id.toString()};
        long row = this.mDb.update(this.mTableName, values, "ID=?", idString);
        if(row > -1){
            Log.d(TAG, "Updated Flight entry with values: " + values.toString());
        }
        return row;
    }

    public void deleteFlightById(Integer id) {
        if(mDb == null){
            Log.e(TAG, "Database null; invoke .open() first.");
        }
        String[] idString = new String[] { id.toString() };
        this.mDb.delete(mTableName, "ID = ?", idString);
    }

    public void deleteFlightBySequence(Integer seq) {
        if(mDb == null){
            Log.e(TAG, "Database null; invoke .open() first.");
        }
        String[] seqString = new String[] { seq.toString() };
        this.mDb.delete(mTableName, "SEQUENCE = ?", seqString);
    }

    public Cursor getFlightById(Integer id){
        String[] idString = new String[]{ id.toString() };
        Cursor c = this.mDb.query(this.mTableName, null, "ID=?", idString, null, null, null);
        if(c != null){
            Log.d(TAG, "Retrieved (" + c.getCount() + ") flights for ID " + id.toString());
        }
        return c;
    }

    public Cursor getFlightsForSequence(Integer logbookId){
        String[] seq = new String[] {(logbookId.toString())};
        Cursor c =  this.mDb.query(this.mTableName, null, Flight.Columns.SEQUENCE+"=?", seq, null, null, null);
        if(c != null) {
            Log.d(TAG, "Retrieved (" + c.getCount() + ") flights for Logbook Entry # " + logbookId.toString());
        }
        return c;
    }

    public void close(){
        this.mDb.close();
    }
}
