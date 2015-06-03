package com.kemblep.crewlog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.kemblep.crewlog.obj.Flight;
import com.kemblep.crewlog.obj.LogbookEntry;
import com.kemblep.crewlog.obj.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Pete on 5/20/2015.
 */
public class DbUtilities {
    private static final String TAG = DbUtilities.class.getName();
    private static Context mContext;
    private static DbHelper mAdapter;
    private static SQLiteDatabase mDb;

    public DbUtilities(Context context){
        mContext = context;
        mAdapter = new DbHelper(mContext);
        mDb = mAdapter.getWritableDatabase();
    }

    public void dropTables(){
        mDb.execSQL("DELETE FROM " + mAdapter.LOGBOOK_TABLE_NAME);
        mDb.execSQL("DELETE FROM " + mAdapter.FLIGHT_TABLE_NAME);
        mDb.execSQL("VACUUM");
        Log.d(TAG, "Dropped both tables");
    }

    public void backupDatabase() throws IOException {
        //Open your local db as the input stream
        String inFileName = "/data/data/com.kemblep.crewlog/databases/crewlog";
        File dbFile = new File(inFileName);
        FileInputStream fis = new FileInputStream(dbFile);

        String outFileName = Environment.getExternalStorageDirectory()+"/crewlog";
        //Open the empty db as the output stream
        OutputStream output = new FileOutputStream(outFileName);
        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer))>0){
            output.write(buffer, 0, length);
        }
        //Close the streams
        output.flush();
        output.close();
        fis.close();
    }

    public void populateLogbook(){
        LogbookEntry fakeEntry = new LogbookEntry(mContext);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, Calendar.MAY);
        c.set(Calendar.DAY_OF_MONTH, 16);
        c.set(Calendar.YEAR, 2015);
        Date d = c.getTime();


        fakeEntry.TailNumber = "N123AF";
        fakeEntry.EntryDate = Util.CustomSimpleDate(d);
        fakeEntry.FlightNumber = "CNS976";
        fakeEntry.CrewMember = "Bob Joe";

        Flight flight = new Flight();
        flight.Approaches = "0";
        flight.Arrival = "KASH";
        flight.Departure = "KBED";
        flight.BlockIn = "1400";
        flight.BlockOut = "1430";
        flight.FlightTime = "1.3";
        flight.FlightType = Flight.FlightTypes.PM;
        flight.InstrumentTime = "0.0";
        flight.Night = "0.0";

        Flight flight2 = new Flight();
        flight2.Approaches = "0";
        flight2.Arrival = "KBED";
        flight2.Departure = "KLWM";
        flight2.BlockIn = "1534";
        flight2.BlockOut = "1650";
        flight2.FlightTime = "1.3";
        flight2.FlightType = Flight.FlightTypes.PF;
        flight2.InstrumentTime = "0.0";
        flight2.Night = "0.3";
        flight2.NightLanding = true;

        fakeEntry.Flights.add(flight);
        fakeEntry.Flights.add(flight2);
        fakeEntry.Duty = 24;
        fakeEntry.CrewMeals = 0;
        fakeEntry.Tips = 2;

        fakeEntry.insertLogbookEntry();
    }
}
