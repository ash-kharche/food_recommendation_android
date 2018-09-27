package com.example.android.foodreco.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * Created by Poonam on 7/10/15.
 */
public class DatabaseUtils {

    public static final String[] KEYS_PROJECTION = new String[]{
            DatabaseHelper.Keys._ID, //0
            DatabaseHelper.Keys.VALUE, //1
    };

    private static final String TAG = "DatabaseUtils";

    public static void insertOrUpdateKeys(Context context, String key, String value) {
        ContentValues values = new ContentValues(1);
        values.put(DatabaseHelper.Keys.VALUE, value);

        int u = context.getContentResolver().update(DatabaseProvider.URI_KEYS, values, DatabaseHelper.Keys._ID + " = ?", new String[]{key});
        //Log.i(TAG, "DatabaseUtils :: Update : KEYS table  " + key + "\"  ::  " + value + "  u  " + u);

        if (u <= 0) {
            values = new ContentValues(2);
            values.put(DatabaseHelper.Keys._ID, key);
            values.put(DatabaseHelper.Keys.VALUE, value);

            context.getContentResolver().insert(DatabaseProvider.URI_KEYS, values);
        }
    }

    public static String getKeyValue(Context context, String key) {
        String value = null;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(DatabaseProvider.URI_KEYS,
                    new String[]{DatabaseHelper.Keys.VALUE},
                    DatabaseHelper.Keys._ID + " = ?",
                    new String[]{key},
                    null);
            if (cursor != null && cursor.moveToNext()) {
                value = cursor.getString(0);
            }
        } catch (Exception e) {
            Log.e(TAG, "getKeys", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return value;
    }

    /*
    public static void insertOrder(Context context, String jsonData, boolean showNotification) {

        // make values to be inserted
        ContentValues values = new ContentValues();
        try {
            Gson gson = new Gson();
            Order order = gson.fromJson(jsonData, Order.class);
            values.put(DatabaseHelper.Orders._ID, order.getOrderId());
            values.put(DatabaseHelper.Orders.ACCEPTANCE_STATUS, Constants.OrderStatus.ORDER_STATUS_NEWS);
            values.put(DatabaseHelper.Orders.STATUS, order.getStatus());
            values.put(DatabaseHelper.Orders.JSON, jsonData);

            Uri uri = context.getContentResolver().insert(DatabaseProvider.URI_ORDERS, values);
            if (showNotification && uri != null && Integer.parseInt(uri.getLastPathSegment()) > 0) {
                //Utils.showNotification1(context);
                Utils.showNotification(context, "Deliver order: " + order.getOrderId());
            }

            Intent i = new Intent("order_inserted");
            context.sendBroadcast(i);

            String date = order.getCreatedAt();
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (!TextUtils.isEmpty(date)) {
                Date d = f.parse(date);
                long milliseconds = d.getTime();
                DatabaseUtils.insertOrUpdateKeys(context, DatabaseHelper.Keys.KEY_TIMESTAMP, milliseconds + "");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error insertOrder", e);
        } finally {
            values.clear();
        }
    }

    private static ArrayList<Order> getAllOrder(Context context, String where, String[] whereArgs, String orderClause) {
        if (TextUtils.isEmpty(orderClause)) {
            orderClause = DatabaseHelper.Orders._ID + " DESC, " + DatabaseHelper.Orders.ACCEPTANCE_STATUS + " ASC";
        }
        ArrayList<Order> ordersList = null;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(DatabaseProvider.URI_ORDERS,
                    ORDER_PROJECTION,
                    where, whereArgs,
                    orderClause);

            if (cursor != null && cursor.getCount() > 0) {
                ordersList = new ArrayList<>(cursor.getCount());
                Order order;
                Gson gson = new Gson();

                while (cursor.moveToNext()) {
                    order = gson.fromJson(cursor.getString(0), Order.class);
                    order.setJsonData(cursor.getString(0));
                    order.setAcceptanceStatus(cursor.getString(1));
                    order.setDeliveredTime(cursor.getString(2));
                    order.setRunId(cursor.getString(3));
                    order.setStatus(cursor.getString(4));

                    ordersList.add(order);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getAllOrder", e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return ordersList;
    }

    private static ArrayList<Integer> getAllOrderId(Context context, String where, String[] whereArgs, String orderClause) {
        if (TextUtils.isEmpty(orderClause)) {
            orderClause = DatabaseHelper.Orders._ID + " DESC, " + DatabaseHelper.Orders.ACCEPTANCE_STATUS + " ASC";
        }
        ArrayList<Integer> ordersList = null;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(DatabaseProvider.URI_ORDERS,
                    new String[] {DatabaseHelper.Orders._ID},
                    where, whereArgs,
                    orderClause);

            if (cursor != null && cursor.getCount() > 0) {
                ordersList = new ArrayList<>(cursor.getCount());

                while (cursor.moveToNext()) {
                    ordersList.add(cursor.getInt(0));
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getAllOrder", e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return ordersList;
    }

    public static ArrayList<Order> getNotificationOrder(Context context) {
        ArrayList<Order> ordersList = null;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(DatabaseProvider.URI_ORDERS,
                    NOTIFICATION_ORDER_PROJECTION,
                    DatabaseHelper.Orders.IS_SEEN + " = ? AND " +
                            DatabaseHelper.Orders.ACCEPTANCE_STATUS + " =? ",
                    new String[]{"0", Constants.OrderStatus.ORDER_STATUS_NEWS},
                    DatabaseHelper.Orders._ID + " DESC ");

            if (cursor != null && cursor.getCount() > 0) {
                ordersList = new ArrayList<>(cursor.getCount());
                Order order;
                Gson gson = new Gson();

                while (cursor.moveToNext()) {
                    order = new Order();
                    order.setOrderId(Integer.parseInt(cursor.getString(0)));
                    Location location = gson.fromJson(cursor.getString(1), Location.class);
                    order.setLocation(location);
                    ordersList.add(order);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getNotificationOrder", e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return ordersList;
    }

    public static int getAllAcceptedOrderBefore(Context context) {
        String runId = DatabaseUtils.getKeyValue(context, DatabaseHelper.Keys.KEY_RUN_ID);
        if (TextUtils.isEmpty(runId)) {
            runId = "0";
        }

        int count = 0;
        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(DatabaseProvider.URI_ORDERS,
                    new String[]{DatabaseHelper.Orders._ID},
                    "( " + DatabaseHelper.Orders.ACCEPTANCE_STATUS + " = ? OR " +
                            DatabaseHelper.Orders.ACCEPTANCE_STATUS + " = ? ) " +
                            " AND " + DatabaseHelper.Orders.RUN_ID + " = ? ",
                    new String[]{Constants.OrderStatus.ORDER_STATUS_ACCEPTEDS, Constants.OrderStatus.ORDER_STATUS_DELIVEREDS, runId},
                    null);

            if (cursor != null) {
                count = cursor.getCount();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getAllAcceptedOrderBeforeCount", e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return count;
    }

    public static ArrayList<Order> getAllAcceptedOrder(Context context) {
        String orderClause = DatabaseHelper.Orders.ACCEPTANCE_STATUS + " ASC, " + DatabaseHelper.Orders._ID + " DESC";

        String runId = DatabaseUtils.getKeyValue(context, DatabaseHelper.Keys.KEY_PAST_RUN_ID);
        if (TextUtils.isEmpty(runId)) {
            runId = "0";
        }
        return getAllOrder(context,
                "( " + DatabaseHelper.Orders.ACCEPTANCE_STATUS + " = ? OR " +
                        DatabaseHelper.Orders.ACCEPTANCE_STATUS + " = ? OR " +
                        DatabaseHelper.Orders.ACCEPTANCE_STATUS + " = ? ) " +
                        " AND " + DatabaseHelper.Orders.RUN_ID + " = ? ",
                new String[]{Constants.OrderStatus.ORDER_STATUS_ACCEPTEDS, Constants.OrderStatus.ORDER_STATUS_DELIVEREDS, Constants.OrderStatus.ORDER_STATUS_CANCELLEDS, runId}, orderClause);
    }

    public static ArrayList<Integer> getAllAcceptedOrderId(Context context) {
        String orderClause = DatabaseHelper.Orders.ACCEPTANCE_STATUS + " ASC, " + DatabaseHelper.Orders._ID + " DESC";

        String runId = DatabaseUtils.getKeyValue(context, DatabaseHelper.Keys.KEY_PAST_RUN_ID);
        if (TextUtils.isEmpty(runId)) {
            runId = "0";
        }
        return getAllOrderId(context,
                "( " + DatabaseHelper.Orders.ACCEPTANCE_STATUS + " = ? OR " +
                        DatabaseHelper.Orders.ACCEPTANCE_STATUS + " = ? OR " +
                        DatabaseHelper.Orders.ACCEPTANCE_STATUS + " = ? ) " +
                        " AND " + DatabaseHelper.Orders.RUN_ID + " = ? ",
                new String[]{Constants.OrderStatus.ORDER_STATUS_ACCEPTEDS, Constants.OrderStatus.ORDER_STATUS_DELIVEREDS, Constants.OrderStatus.ORDER_STATUS_CANCELLEDS, runId}, orderClause);
    }


    public static ArrayList<Order> getAllUpdatedOrder(Context context) {
        return getAllOrder(context,
                " ( " + DatabaseHelper.Orders.ACCEPTANCE_STATUS + " = ? OR " +
                        DatabaseHelper.Orders.ACCEPTANCE_STATUS + " = ? OR " +
                        DatabaseHelper.Orders.ACCEPTANCE_STATUS + " = ? OR " +
                        DatabaseHelper.Orders.ACCEPTANCE_STATUS + " = ? ) AND " +
                        DatabaseHelper.Orders.STATUS + " != ? ",

                new String[]{
                        Constants.OrderStatus.ORDER_STATUS_NEWS,
                        Constants.OrderStatus.ORDER_STATUS_ACCEPTEDS,
                        Constants.OrderStatus.ORDER_STATUS_ALREADY_ACCEPTEDS,
                        Constants.OrderStatus.ORDER_STATUS_CANCELLEDS,
                        Constants.OrderStatus.ORDER_DELIVERED}, null);
    }

    public static int getAllUpdatedOrderCount(Context context) {
        int count = 0;
        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(DatabaseProvider.URI_ORDERS,
                    new String[]{DatabaseHelper.Orders._ID},
                    " ( " + DatabaseHelper.Orders.ACCEPTANCE_STATUS + " = ? OR " +
                            DatabaseHelper.Orders.ACCEPTANCE_STATUS + " = ? OR " +
                            DatabaseHelper.Orders.ACCEPTANCE_STATUS + " = ? OR " +
                            DatabaseHelper.Orders.ACCEPTANCE_STATUS + " = ? ) AND " +
                            DatabaseHelper.Orders.STATUS + " != ? ",
                    new String[]{
                            Constants.OrderStatus.ORDER_STATUS_NEWS,
                            Constants.OrderStatus.ORDER_STATUS_ACCEPTEDS,
                            Constants.OrderStatus.ORDER_STATUS_ALREADY_ACCEPTEDS,
                            Constants.OrderStatus.ORDER_STATUS_CANCELLEDS,
                            Constants.OrderStatus.ORDER_DELIVERED},
                    null);

            if (cursor != null) {
                count = cursor.getCount();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getAllUpdatedOrderCount", e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return count;
    }

    public static int updateOrder(Context context, long orderId, String key, String value) {
        if (orderId > 0) {
            ContentValues values = new ContentValues(1);
            values.put(key, value);
            return context.getContentResolver().update(DatabaseProvider.URI_ORDERS,
                    values,
                    DatabaseHelper.Orders._ID + " = ?",
                    new String[]{String.valueOf(orderId)});
        } else {
            return 0;
        }
    }

    public static int updateOrder(Context context, long orderId, ContentValues values) {
        if (orderId > 0 && values != null) {
            try {
                return context.getContentResolver().update(DatabaseProvider.URI_ORDERS,
                        values,
                        DatabaseHelper.Orders._ID + " = ?",
                        new String[]{String.valueOf(orderId)});
            } finally {
                if (values != null) {
                    values.clear();
                }
            }
        } else {
            return 0;
        }
    }

    public static int updateOrder(Context context, String orderId, String key, String value) {
        if (!TextUtils.isEmpty(orderId)) {
            ContentValues values = new ContentValues(1);
            values.put(key, value);
            return context.getContentResolver().update(DatabaseProvider.URI_ORDERS,
                    values,
                    DatabaseHelper.Orders._ID + " = ?",
                    new String[]{orderId});
        } else {
            return 0;
        }
    }

    public static void updateOrderDeliveredTime(Context context, int orderId, String timeStamp) {
        if (orderId > 0) {
            ContentValues values = new ContentValues(1);
            values.put(DatabaseHelper.Orders.DELIVERED_TIME, timeStamp);
            context.getContentResolver().update(DatabaseProvider.URI_ORDERS,
                    values,
                    DatabaseHelper.Orders._ID + " = ?",
                    new String[]{String.valueOf(orderId)});
        }
    }

    private static int getOrderCount(Context context, String status) {
        int count = 0;
        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(DatabaseProvider.URI_ORDERS,
                    new String[]{DatabaseHelper.Orders._ID},
                    DatabaseHelper.Orders.ACCEPTANCE_STATUS + " = ?",
                    new String[]{status},
                    null);

            if (cursor != null) {
                count = cursor.getCount();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getUndeliveredOrderCount", e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return count;
    }

    public static int getUnAcceptedOrderCount(Context context) {
        return getOrderCount(context, Constants.OrderStatus.ORDER_STATUS_NEWS);
    }

    public static int getAcceptedOrderCount(Context context) {
        return getOrderCount(context, Constants.OrderStatus.ORDER_STATUS_ACCEPTEDS);
    }

    public static void deleteOrders(Context context, String status) {
        try {
            context.getContentResolver().delete(DatabaseProvider.URI_ORDERS, DatabaseHelper.Orders.ACCEPTANCE_STATUS + " =? ", new String[]{status});
        } catch (Exception e) {
            Log.e(TAG, "Error deleteDatabase : ", e);
        }
    }

    public static void deleteDatabase(Context context) {
        try {
            context.getContentResolver().delete(DatabaseProvider.URI_ORDERS, null, null);
            //context.getContentResolver().delete(DatabaseProvider.URI_KEYS, null, null);

            resetKeys(context);
        } catch (Exception e) {
            Log.e(TAG, "Error deleteDatabase : ", e);
        }
    }

    private static void resetKeys(Context context) {
        try {
            insertOrUpdateKeys(context, DatabaseHelper.Keys.KEY_RUN_ID, "0");
            insertOrUpdateKeys(context, DatabaseHelper.Keys.KEY_PAST_RUN_ID, "0");
            insertOrUpdateKeys(context, DatabaseHelper.Keys.KEY_DELIVERED_ORDER_COUNT, "0");
            insertOrUpdateKeys(context, DatabaseHelper.Keys.KEY_TIMESTAMP, null);
        } catch (Exception e) {
            Log.e(TAG, "Error resetKeys : ", e);
        }
    }

    public static void insertOrUpdateKeys(Context context, String key, String value) {
        ContentValues values = new ContentValues(1);
        values.put(DatabaseHelper.Keys.VALUE, value);

        int u = context.getContentResolver().update(DatabaseProvider.URI_KEYS, values, DatabaseHelper.Keys._ID + " = ?", new String[]{key});
        //Log.i(TAG, "DatabaseUtils :: Update : KEYS table  " + key + "\"  ::  " + value + "  u  " + u);

        if (u <= 0) {
            values = new ContentValues(2);
            values.put(DatabaseHelper.Keys._ID, key);
            values.put(DatabaseHelper.Keys.VALUE, value);

            context.getContentResolver().insert(DatabaseProvider.URI_KEYS, values);
        }
    }

    public static String getKeyValue(Context context, String key) {
        String value = null;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(DatabaseProvider.URI_KEYS,
                    new String[]{DatabaseHelper.Keys.VALUE},
                    DatabaseHelper.Keys._ID + " = ?",
                    new String[]{key},
                    null);
            if (cursor != null && cursor.moveToNext()) {
                value = cursor.getString(0);
            }
        } catch (Exception e) {
            Log.e(TAG, "getKeys", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return value;
    }

    public static void insertDriver(Context context, int driverId, int storeId, String storNo, String json) {
        insertOrUpdateKeys(context, DatabaseHelper.Keys.KEY_DRIVER_ID, driverId + "");
        insertOrUpdateKeys(context, DatabaseHelper.Keys.KEY_STORE_ID, storeId + "");
        insertOrUpdateKeys(context, DatabaseHelper.Keys.KEY_STORE_NUMBER, storNo);
        insertOrUpdateKeys(context, DatabaseHelper.Keys.KEY_DRIVER_JSON, json);
    }

    public static void deleteNotAcceptedOrders(Context context) {
        context.getContentResolver().delete(
                DatabaseProvider.URI_ORDERS, DatabaseHelper.Orders.ACCEPTANCE_STATUS + " != ?", new String[]{ Constants.OrderStatus.ORDER_STATUS_ACCEPTEDS });
    }
    */
}
