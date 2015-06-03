package com.kemblep.crewlog.obj;

import android.graphics.Color;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pete on 5/16/2015.
 */
public class Util {
    private static SimpleDateFormat mSdf = new SimpleDateFormat("MM/dd/yyyy");

    public static String CustomSimpleDate(Date date) {
        return mSdf.format(date);
    }

    public static void setText(TextView tv, String text){
        setText(tv, text, false);
    }

    //leave the fail functionality off for now, but maybe can be useful later.
    public static void setText(TextView tv, String text, boolean fail){
        if(fail){
            tv.setBackgroundColor(Color.RED);
            //tv.setTextSize(10);
        } else {
            tv.setBackgroundColor(Color.TRANSPARENT);
            //tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
        }
        tv.setText(text);
    }

    public static void resetEditText(EditText e){
        e.setBackgroundColor(Color.TRANSPARENT);
        e.setText(null);
    }

    public static boolean verifyTimes(EditText startTimeEntry, EditText endTimeEntry){
        //verify good dates, despite 2500 being interpreted as 0100
        String t1 = "0000", t2 = "0000";

        if(startTimeEntry.getText().length() > 0){
            t1 = startTimeEntry.getText().toString();
        }
        if(endTimeEntry.getText().length() > 0){
            t2 = endTimeEntry.getText().toString();
        }

        int t1h = Integer.parseInt(t1.substring(0, 2));
        int t1m = Integer.parseInt(t1.substring(2, 4));
        int t2h = Integer.parseInt(t2.substring(0, 2));
        int t2m = Integer.parseInt(t2.substring(2, 4));

        if(t1h > 23 | t1m > 59){
            startTimeEntry.setBackgroundColor(Color.RED);
            return false;
        } else {
            startTimeEntry.setBackgroundColor(Color.TRANSPARENT);
        }

        if(t2h > 23 | t2m > 59){
            endTimeEntry.setBackgroundColor(Color.RED);
            return false;
        } else {
            startTimeEntry.setBackgroundColor(Color.TRANSPARENT);
        }
        //everything is good
        return true;
    }

    public static void fixErrors(EditText e){
        Util.setText(e, e.getText().toString().substring(4));
        e.setBackgroundColor(Color.TRANSPARENT);
        e.setSelection(1);
    }

}
