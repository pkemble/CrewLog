package com.kemblep.crewlog.obj;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Pete on 3/27/2015.
 */
public class HobbsTime {

    private static final String TAG = HobbsTime.class.getName();
    public Date StartDate;
    public Date EndDate;
    public float Time;
    public String ConvertedTime;
    private DecimalFormat mFinalFormat = new DecimalFormat("0.0");
    private DecimalFormat mPreciseFormat = new DecimalFormat("0.##");

    public HobbsTime(String start, String end){
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm", Locale.ENGLISH);
        try {
            StartDate = sdf.parse(start);
            EndDate = fixEndTime(StartDate, sdf.parse(end));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Time = convertTimesToHobbs(StartDate, EndDate);
        ConvertedTime = formatSimpleTime(Time);
    }

    private String formatSimpleTime(float flTime) {
        return mFinalFormat.format(flTime);
    }

    private float convertTimesToHobbs(Date start, Date end){

        double mills = (end.getTime() - start.getTime());
        float convertedMills = (float) (mills / 3600000);

        String diff = mPreciseFormat.format(convertedMills);

        Log.d(TAG, "decimal format 0.00:" + diff
                + "\n milliseconds: " + mills
                + "\n decimal format 0.00: " + mPreciseFormat.format(convertedMills));

        return convertedMills;
    }

    //if the end time is "before" the start time (i.e. went past midnight)
    //then fix the day to reflect this.
    private Date fixEndTime(Date start, Date end) {
        if(start.after(end)){
            Calendar c = Calendar.getInstance();
            c.setTime(end);
            c.add(Calendar.DATE, 1);
            end = c.getTime();
        }
        return end;
    }

    public String add(float addition){
        float total = Time + addition;
        return mFinalFormat.format(total);
    }
}