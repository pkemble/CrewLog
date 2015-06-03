package com.kemblep.crewlog.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Pete on 5/21/2015.
 */
public class LogbookDbAdapter {

    private final Context mContext;
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;
    private String mTableName;

    public LogbookDbAdapter (Context context) {
        this.mContext = context;
    }
    public LogbookDbAdapter open() {
        this.mDbHelper = new DbHelper(mContext);
        this.mDb = mDbHelper.getWritableDatabase();
        this.mTableName = mDbHelper.LOGBOOK_TABLE_NAME;
        return this;
    }

    public long insertLogbookEntry(ContentValues values){
        return this.mDb.insert(this.mTableName, null, values);
    }

    public Cursor getLogbookEntry(String date){
        String[] dateString = new String[] { date };
        return this.mDb.query(this.mTableName, null, "ENTRYDATE = ?", dateString, null, null, null);
    }

    public void close(){
        this.mDb.close();
    }

}
