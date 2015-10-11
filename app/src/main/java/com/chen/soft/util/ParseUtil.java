package com.chen.soft.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by chenchi_94 on 2015/10/11.
 */
public class ParseUtil {

    /**
     * 将由Mong DB读取的ISODATE转换为DATE格式
     *
     * @param isoDate
     * @return
     */
    public static Date parseISODate(String isoDate) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
        df.setTimeZone(tz);
        try {
            Date date = df.parse(isoDate);
            return date;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            Log.d("info", e.getMessage());
            return new Date();
        }
    }

    public static String ParseUrl(String args){
        return args.replace(" ", "%20");
    }
}
