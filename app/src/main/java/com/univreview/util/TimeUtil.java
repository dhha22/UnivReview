package com.univreview.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DavidHa on 2017. 1. 8..
 */
public class TimeUtil {
    private Date now;
    private SimpleDateFormat timeStamp;
    private SimpleDateFormat basicFormat;

    public TimeUtil() {
        now = new Date();
        timeStamp = new SimpleDateFormat("yyyyMMddHHmmss");
        basicFormat = new SimpleDateFormat("yyyy.MM.dd");
    }

    public String getTimeStamp() {
        return timeStamp.format(now);
    }

    public String getPointFormat(String dateStr) {
        if (dateStr != null)
            return basicFormat.format(dateStr);
        return dateStr;
    }


}
