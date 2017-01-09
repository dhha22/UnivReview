package com.univreview.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DavidHa on 2017. 1. 8..
 */
public class TimeUtil {
    private Date now;
    private SimpleDateFormat timeStamp;

    public TimeUtil() {
        now = new Date();
        timeStamp = new SimpleDateFormat("yyyyMMddhhmmss");
    }

    public String getTimeStamp(){
        return timeStamp.format(now);
    }
}
