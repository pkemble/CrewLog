package com.kemble.crewlog.obj;

import java.util.ArrayList;

/**
 * Created by Pete on 5/3/2015.
 */
public class FlightLogEntry {
    public java.util.Date Date;
    public String TailNumber;
    public String FlightNumber;
    public String CrewMember;
    public ArrayList<Leg> Legs = new ArrayList<>();
    public long CrewMeals;

    public FlightLogEntry(){
    }

}
