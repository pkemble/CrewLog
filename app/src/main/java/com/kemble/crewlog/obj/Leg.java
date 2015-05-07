package com.kemble.crewlog.obj;

import android.content.ContentValues;

import java.util.Date;

/**
 * Created by Pete on 5/3/2015.
 */
public class Leg {
    public int FlightType; //0=pf 1=pm 2=sf 3=sm
    public String Departure;
    public Date BlockOut;
    public String Arrival;
    public Date BlockIn;
    public double InstrumentTime;
    public double Approaches;
    public boolean Night;
    public double FlightTime;

    public Leg dbToLeg(ContentValues values){
        return null;
    }

}
