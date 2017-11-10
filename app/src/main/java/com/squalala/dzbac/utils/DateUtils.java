package com.squalala.dzbac.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Back Packer
 * Date : 29/09/15
 */
public class DateUtils {

    public static long differenceDateMinuteFromNow(String dateStr1) {

        // Custom date format
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date d1 = null;
        try {
            d1 = format.parse(dateStr1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = new Date().getTime() - d1.getTime();

        return TimeUnit.MILLISECONDS.toMinutes(diff);
    }

    public static String getRelativeTime(Date date) {

        long now = System.currentTimeMillis();

        if (date != null) {
            String dateStr = (String) android.text.format
                    .DateUtils.getRelativeTimeSpanString(date.getTime(), now, android.text.format.DateUtils.FORMAT_ABBREV_ALL);

            if (dateStr != null)
                return dateStr;
        }

        return "none";
    }

    public static Date strToDate(String dateStr) {



        Date date = null;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {

            date = format.parse(dateStr);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
          //  cal.add(Calendar.HOUR, -1);

            return cal.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }


}
