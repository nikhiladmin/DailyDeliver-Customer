package com.daytoday.customer.dailydelivery.Utilities;

public class Request {
    public static final String PENDING = "Pending";
    public static final String ACCEPTED = "Accepted";
    public static final String REJECTED = "Rejected";

    public static final Integer PENDING_INT = 1;
    public static final Integer ACCEPTED_INT = 0;
    public static final Integer REJECTED_INT = -1;

    public static Integer getRespectiveIntegerValue(String notificationStatus) {
        switch (notificationStatus) {
            case PENDING : return PENDING_INT;
            case REJECTED : return REJECTED_INT;
            case ACCEPTED : return  ACCEPTED_INT;
        }
        return null;
    }
}
