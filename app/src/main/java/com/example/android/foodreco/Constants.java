package com.example.android.foodreco;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Poonam on 11/05/15.
 */
public class Constants {

    public static final String TAG = "DriverApp";
    public static final int MARKET_PLACE = 6;
    public static final float MINIMUM_BATTERY = 20;

    public static final AtomicInteger NOTIFICATION_ID_ATOMIC = new AtomicInteger(0);
    public static final int NOTIFICATION_ID = 10001;
    public static final int INTERNET_NOTIFICATION_ID = 101;

    public interface DriverStatus {
        int CHECK_IN_STATE = 1;
        int CHECK_OUT_STATE = 2;
        int LOG_OUT_STATE = 3;

        String CHECK_IN_STATES = "1";
        String CHECK_OUT_STATES = "2";
        String LOG_OUT_STATES = "3";
    }

    public interface OrderStatus {
        int ORDER_STATUS_NEW = 0;
        int ORDER_STATUS_ACCEPTED = 1;
        int ORDER_STATUS_DELIVERED = 2;
        int ORDER_STATUS_CANCELLED = 3;
        int ORDER_STATUS_ALREADY_ACCEPTED = 4;

        String ORDER_STATUS_NEWS = "0";
        String ORDER_STATUS_ACCEPTEDS = "1";
        String ORDER_STATUS_DELIVEREDS = "2";
        String ORDER_STATUS_CANCELLEDS = "3";
        String ORDER_STATUS_ALREADY_ACCEPTEDS = "4";

        String ORDER_DELIVERED = "delivered";
    }
}
