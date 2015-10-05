package com.kemblep.crewlog.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kemblep.crewlog.obj.LogbookEntry;

/**
 * Created by Pete on 5/21/2015.
 */
public class LogbookDbAdapter {

    private final Context mContext;
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;
    private String mTableName;

    private static final String TAG = LogbookDbAdapter.class.getName();

    public LogbookDbAdapter (Context context) {
        this.mContext = context;
    }
    public LogbookDbAdapter open() {
        if(this.mContext == null){
            Log.e(TAG, "Context is null - Instantiate Adapter class first.");
            return null;
        }
        this.mDbHelper = new DbHelper(mContext);
        this.mDb = mDbHelper.getWritableDatabase();
        this.mTableName = mDbHelper.LOGBOOK_TABLE_NAME;
        return this;
    }

    public long insertLogbookEntry(ContentValues values){
        if(mDb == null){
            Log.e(TAG, "Database null; invoke .open() first.");
            return -1;
        }
        long row = this.mDb.insert(mTableName, null, values);
        if(row > -1){
            Log.d(TAG, "Inserted Log Entry: " + values.toString());
        }
        return row;
    }

    public long updateLogbookEntry(ContentValues values) {
        if(mDb == null){
            Log.e(TAG, "Database null; invoke .open() first.");
            return -1;
        }
        String id = values.getAsString(LogbookEntry.Columns.ID.name());
        String[] idString = new String[] {id};
        long row = this.mDb.update(mTableName, values, "ID = ?", idString);
        if(row > 1){
            Log.d(TAG, "Updated Log Entry: " + values.toString());
        }
        return row;
    }
    public void deleteLogbookEntry(Integer id) {
        if(mDb == null){
            Log.e(TAG, "Database null; invoke .open() first.");
        }
        String[] idString = new String[] { id.toString() };
        this.mDb.delete(mTableName, "ID = ?", idString);
    }

    public Cursor getLogbookEntry(String date, String tailNumber){
        String[] clause = new String[] { date, tailNumber };
        Cursor c = this.mDb.query(this.mTableName, null, "ENTRYDATE = ? AND TAILNUMBER = ?", clause, null, null, null);
        if(c != null){
            Log.d(TAG, "Retrieved (" + c.getCount() + ") log entries for " + date + " on " + tailNumber);
        }
        return c;
    }

    public Cursor getLogbookEntry(String date){
        String[] clause = new String[] { date };
        Cursor c = this.mDb.query(this.mTableName, null, "ENTRYDATE = ?", clause, null, null, null);
        if(c != null){
            Log.d(TAG, "Retrieved (" + c.getCount() + ") log entries for " + date);
        }
        return c;
    }

    public void close(){
        this.mDb.close();
    }
}
