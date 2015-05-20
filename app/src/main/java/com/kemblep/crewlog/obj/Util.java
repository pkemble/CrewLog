package com.kemblep.crewlog.obj;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pete on 5/16/2015.
 */
public class Util {
    public static Integer CustomIntDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
        return Integer.parseInt(sdf.format(date));
    }
}
