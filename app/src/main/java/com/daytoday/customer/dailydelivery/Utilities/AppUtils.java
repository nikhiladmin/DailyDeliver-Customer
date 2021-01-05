package com.daytoday.customer.dailydelivery.Utilities;

import com.daytoday.customer.dailydelivery.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppUtils {
    public static String getCurrentTimeStamp(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        return currentDateTime;
    }

    public static boolean isNumerical(String value) {
        return value.matches("-?[0-9]*");
    }

    public static int getResourceIdDates(String status) {
        switch (status){
            case Request.PENDING :
                return R.drawable.pending_color;
            case Request.ACCEPTED :
                return R.drawable.accepted_color;
            default :
                return R.drawable.canceled_color;
        }
    }
}
