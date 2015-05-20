package com.kemblep.crewlog.obj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kemblep.crewlog.database.LogbookDbAdapter;

import java.util.ArrayList;

/**
 * Created by Pete on 5/3/2015.
 */
public class Leg {
    public Integer Id;
    public long Sequence;
    public FlightTypes FlightType;
    public String Departure;
    public String BlockOut;
    public String Arrival;
    public String BlockIn;
    public double InstrumentTime;
    public double Approaches;
    public double Night;
    public boolean NightTakeoff;
    public boolean NightLanding;
    public double FlightTime;

    private final static String TAG = Leg.class.getName();

    public Leg() {
    }

    public enum Columns {
        ID,
        SEQUENCE,
        FLIGHTTYPE,
        DEP,
        BLOCKOUT,
        ARR,
        BLOCKIN,
        INST,
        APPR,
        NIGHT,
        NIGHT_TO,
        NIGHT_LNDG,
        FLIGHTTIME
    }

    public enum FlightTypes {
        PF,
        PM,
        SF,
        SM;

        public static FlightTypes valueOfInt(int i) {
            for(FlightTypes f : values()){
                if(f.equals(Integer.toString(i))){
                    return f;
                }
            }
            return null;
        }
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        values.put(Columns.SEQUENCE.name(), Sequence);
        values.put(Columns.FLIGHTTYPE.name(), String.valueOf(FlightType));
        if(Departure != null){
            values.put(Columns.DEP.name(), Departure);
        }
        if(BlockOut != null){
            values.put(Columns.BLOCKOUT.name(), BlockOut);
        }
        if(Arrival != null){
            values.put(Columns.ARR.name(), Arrival);
        }
        if(BlockIn != null){
            values.put(Columns.BLOCKIN.name(), BlockIn);
        }
        values.put(Columns.INST.name(), InstrumentTime);
        values.put(Columns.APPR.name(), Approaches);
        values.put(Columns.NIGHT.name(), Night);
        values.put(Columns.NIGHT_TO.name(), NightTakeoff);
        values.put(Columns.NIGHT_LNDG.name(), NightLanding);
        values.put(Columns.FLIGHTTIME.name(), FlightTime);

        return values;
    }

    public static Leg getLegFromDb(int id, Context context){
        String[] idString = new String[]{Integer.toString(id)};

        LogbookDbAdapter logbookDbAdapter = new LogbookDbAdapter(context);
        SQLiteDatabase db = logbookDbAdapter.getReadableDatabase();
        Cursor c = db.query(logbookDbAdapter.FLIGHT_TABLE_NAME, null, "ID=?", idString, null, null, null);
        if(c == null){
            return null;
        }

        return legFromCursor(c).get(0);
    }

    public static ArrayList<Leg> legFromCursor(Cursor c){
        ArrayList<Leg> legs = new ArrayList<>();
        if(c.moveToFirst()){
            do {
                Leg leg = new Leg();
                leg.Id = c.getInt(c.getColumnIndexOrThrow(Columns.ID.name()));
                leg.Sequence = c.getInt(c.getColumnIndexOrThrow(Columns.SEQUENCE.name()));
                leg.FlightType = Leg.FlightTypes.valueOf(c.getString(c.getColumnIndexOrThrow(Columns.FLIGHTTYPE.name())));
                leg.Departure = c.getString(c.getColumnIndexOrThrow(Columns.DEP.name()));
                leg.BlockOut = c.getString(c.getColumnIndexOrThrow(Columns.BLOCKOUT.name()));
                leg.Arrival = c.getString(c.getColumnIndexOrThrow(Columns.ARR.name()));
                leg.BlockIn = c.getString(c.getColumnIndexOrThrow(Columns.BLOCKIN.name()));
                leg.InstrumentTime = c.getDouble(c.getColumnIndexOrThrow(Columns.INST.name()));
                leg.Approaches = c.getDouble(c.getColumnIndexOrThrow(Columns.APPR.name()));
                leg.Night = c.getDouble(c.getColumnIndexOrThrow(Columns.NIGHT.name()));
                leg.NightTakeoff = c.getInt(c.getColumnIndexOrThrow(Columns.NIGHT_TO.name())) > 0;
                leg.NightLanding = c.getInt(c.getColumnIndexOrThrow(Columns.NIGHT_LNDG.name())) > 0;
                leg.FlightTime = c.getDouble(c.getColumnIndexOrThrow(Columns.FLIGHTTIME.name()));
                legs.add(leg);
            } while (c.moveToNext());

            if(c != null && !c.isClosed()){
                c.close();
            }
        }
        return legs;
    }

    public static void insertLeg(Leg leg, Context context){
        LogbookDbAdapter logbookDbAdapter = new LogbookDbAdapter(context);
        SQLiteDatabase db = logbookDbAdapter.getWritableDatabase();

        ContentValues legValues;
        legValues = leg.getContentValues();
        long flightEntryId = db.insert(logbookDbAdapter.FLIGHT_TABLE_NAME, null, legValues);
        Log.d(TAG, "Inserted flight entry ID " + flightEntryId + " for sequence " + leg.Sequence);
    }
}
