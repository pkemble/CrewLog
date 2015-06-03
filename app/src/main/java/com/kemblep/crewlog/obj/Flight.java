package com.kemblep.crewlog.obj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.kemblep.crewlog.database.FlightDbAdapter;

import java.util.ArrayList;

/**
 * Created by Pete on 5/3/2015.
 */
public class Flight {
    public Integer Id;
    public long Sequence;
    public FlightTypes FlightType;
    public String Departure;
    public String BlockOut;
    public String Arrival;
    public String BlockIn;
    public String InstrumentTime;
    public String Approaches;
    public String Night;
    public boolean NightTakeoff;
    public boolean NightLanding;
    public String FlightTime;

    private final static String TAG = Flight.class.getName();

    public Flight(){}

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

    private ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        HobbsTime hobbsTime = new HobbsTime(BlockOut, BlockIn);
        FlightTime = hobbsTime.ConvertedTime;

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

    public Flight getFlightFromDb(int id, Context context){

        FlightDbAdapter fdb = new FlightDbAdapter(context);
        fdb.open();
        Cursor c = fdb.getFlightById(id);
        if(c == null){
            return null;
        }
        Flight f = flightsFromCursor(c).get(0);
        fdb.close();
        return f;
    }

    //TODO move this to another class?
    public static ArrayList<Flight> flightsFromCursor(Cursor c){
        ArrayList<Flight> flights = new ArrayList<>();
        if(c.moveToFirst()){
            do {
                Flight flight = new Flight();
                flight.Id = c.getInt(c.getColumnIndexOrThrow(Columns.ID.name()));
                flight.Sequence = c.getInt(c.getColumnIndexOrThrow(Columns.SEQUENCE.name()));
                flight.FlightType = Flight.FlightTypes.valueOf(c.getString(c.getColumnIndexOrThrow(Columns.FLIGHTTYPE.name())));
                flight.Departure = c.getString(c.getColumnIndexOrThrow(Columns.DEP.name()));
                flight.BlockOut = c.getString(c.getColumnIndexOrThrow(Columns.BLOCKOUT.name()));
                flight.Arrival = c.getString(c.getColumnIndexOrThrow(Columns.ARR.name()));
                flight.BlockIn = c.getString(c.getColumnIndexOrThrow(Columns.BLOCKIN.name()));
                flight.InstrumentTime = c.getString(c.getColumnIndexOrThrow(Columns.INST.name()));
                flight.Approaches = c.getString(c.getColumnIndexOrThrow(Columns.APPR.name()));
                flight.Night = c.getString(c.getColumnIndexOrThrow(Columns.NIGHT.name()));
                flight.NightTakeoff = c.getInt(c.getColumnIndexOrThrow(Columns.NIGHT_TO.name())) > 0;
                flight.NightLanding = c.getInt(c.getColumnIndexOrThrow(Columns.NIGHT_LNDG.name())) > 0;
                flight.FlightTime = c.getString(c.getColumnIndexOrThrow(Columns.FLIGHTTIME.name()));
                flights.add(flight);
            } while (c.moveToNext());

            if(c != null && !c.isClosed()){
                c.close();
            }
        }
        return flights;
    }

    public static long insertFlight(Flight flight, Context context){

        ContentValues flightValues = flight.getContentValues();

        FlightDbAdapter fdb = new FlightDbAdapter(context);
        fdb.open();
        long flightEntryId = fdb.insertFlight(flightValues);
        fdb.close();
        if(flightEntryId > -1){
            Log.d(TAG, "Inserted flight entry ID " + flightEntryId + " for sequence " + flight.Sequence);
        } else {
            Log.e(TAG, "Couldn't insert entry ID " + flightEntryId + " for sequence " + flight.Sequence);
        }
        return flightEntryId;
    }

    public static long updateFlight(Flight flight, Context context) {
        ContentValues flightValues = flight.getContentValues();

        FlightDbAdapter fdb = new FlightDbAdapter(context);
        fdb.open();
        long flightEntryId = fdb.updateFlight(flightValues, flight.Id);

        if(flightEntryId > -1){
            Log.d(TAG, "Updated flight entry ID " + flightEntryId + " for sequence " + flight.Sequence);
        } else {
            Log.e(TAG, "Couldn't update entry ID " + flightEntryId + " for sequence " + flight.Sequence);
        }
        return flightEntryId;
    }
}
