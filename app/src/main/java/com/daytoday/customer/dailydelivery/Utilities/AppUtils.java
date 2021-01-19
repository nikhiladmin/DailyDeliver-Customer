package com.daytoday.customer.dailydelivery.Utilities;

import android.util.Log;

import com.daytoday.customer.dailydelivery.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AppUtils {
    public static String getCurrentTimeStamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        return currentDateTime;
    }

    public static boolean isNumerical(String value) {
        return value.matches("-?[0-9]*");
    }

    public static int getResourceIdDates(String status) {
        switch (status) {
            case Request.PENDING:
                return R.drawable.pending_color;
            case Request.ACCEPTED:
                return R.drawable.accepted_color;
            default:
                return R.drawable.canceled_color;
        }
    }

    public static String getAgoTime(String convDate) {
        String timeAgo = "";
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(convDate);
            long different = System.currentTimeMillis() - date.getTime();
            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            if (elapsedDays > 0) {
                timeAgo = elapsedDays + " Days ";
            } else if (elapsedDays > 0 || elapsedHours > 0) {
                timeAgo = elapsedHours + " Hours ";
            } else if (elapsedHours > 0 || elapsedMinutes > 0) {
                if (elapsedMinutes > 2)
                    timeAgo = elapsedMinutes + " Minutes ";
                else
                    timeAgo = "Few Minutes";
            } else if (elapsedMinutes > 0 || elapsedSeconds > 0) {
                timeAgo = "Few seconds";
            }
            timeAgo += " ago";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeAgo;
    }
}
