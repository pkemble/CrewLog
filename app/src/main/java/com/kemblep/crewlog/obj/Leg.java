package com.kemblep.crewlog.obj;

import android.content.ContentValues;

import java.util.Date;

/**
 * Created by Pete on 5/3/2015.
 */
public class Leg {
    public int Id;
    public long Sequence;
    public int FlightType; //0=pf 1=pm 2=sf 3=sm
    public String Departure;
    public Date BlockOut;
    public String Arrival;
    public Date BlockIn;
    public double InstrumentTime;
    public double Approaches;
    public double Night;
    public boolean NightTakeoff;
    public boolean NightLanding;
    public double FlightTime;

    public Leg dbToLeg(ContentValues values){
        return null;
    }

    public ContentValues getContentValues(long sequence) {
        ContentValues values = new ContentValues();

        values.put("SEQUENCE", sequence);
        values.put("TYPE", FlightType);
        if(Departure != null){
            values.put("DEP", Departure);
        }
        if(BlockOut != null){
            values.put("BLOCKOUT", BlockOut.hashCode());
        }
        if(Arrival != null){
            values.put("DEP", Arrival);
        }
        if(BlockIn != null){
            values.put("BLOCKIN", BlockIn.hashCode());
        }
        values.put("INST", InstrumentTime);
        values.put("APPR", Approaches);
        values.put("NIGHT", Night);
        values.put("NIGHT_TO", NightTakeoff);
        values.put("NIGHT_LNDG", NightLanding);
        values.put("FLIGHTTIME", FlightTime);

        return values;
    }

    public static Leg getLeg(int leg) {

        return null;
    }
}
