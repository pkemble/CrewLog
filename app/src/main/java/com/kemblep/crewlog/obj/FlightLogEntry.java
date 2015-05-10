package com.kemblep.crewlog.obj;

import android.content.ContentValues;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Pete on 5/3/2015.
 */
public class FlightLogEntry {
    public Integer Id;
    public Date Date;
    public String TailNumber;
    public String FlightNumber;
    public String CrewMember;
    public ArrayList<Leg> Legs = new ArrayList<>();
    public long Duty;
    public long CrewMeals;
    public Integer Tips;

    public FlightLogEntry(){
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        if(Date != null) {
            values.put("DATE", Date.toString());
        }
        if(TailNumber != null) {
            values.put("TAILNUMBER", TailNumber);
        }
        if(FlightNumber != null) {
            values.put("FLIGHTNUMBER", FlightNumber);
        }
        if(CrewMember != null) {
            values.put("CREWNAME", CrewMember);
        }

        values.put("DUTY", Duty);
        values.put("CREWMEAL", CrewMeals);

        if(Tips != null) {
            values.put("TIPS", Tips);
        }

        return values;
    }
}
