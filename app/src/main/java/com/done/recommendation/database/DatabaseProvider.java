package com.done.recommendation.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class DatabaseProvider extends ContentProvider {

    public static final String AUTHORITY = "recommendation";

    private static final String PATH_KEYS = DatabaseHelper.Tables.KEYS;
    public static final Uri URI_KEYS = Uri.parse("content://" + AUTHORITY + "/" + PATH_KEYS);
    private static final int KEYS = 1000;
    private static final String KEYS_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + URI_KEYS;

    private static final String PATH_ORDERS = DatabaseHelper.Tables.ORDERS;
    public static final Uri URI_ORDERS = Uri.parse("content://" + AUTHORITY + "/" + PATH_ORDERS);
    private static final int ORDERS = 1001;
    private static final String ORDERS_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + URI_ORDERS;

    private static final String PATH_COLLECTIONS = DatabaseHelper.Tables.COLLECTIONS;
    public static final Uri URI_COLLECTIONS = Uri.parse("content://" + AUTHORITY + "/" + PATH_COLLECTIONS);
    private static final int COLLECTIONS = 1002;
    private static final String COLLECTIONS_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + URI_COLLECTIONS;

    private static final String PATH_PRODUCTS = DatabaseHelper.Tables.PRODUCTS;
    public static final Uri URI_PRODUCTS = Uri.parse("content://" + AUTHORITY + "/" + PATH_PRODUCTS);
    private static final int PRODUCTS = 1003;
    private static final String PRODUCTS_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + URI_PRODUCTS;

    private static final String PATH_CART = DatabaseHelper.Tables.CART;
    public static final Uri URI_CART = Uri.parse("content://" + AUTHORITY + "/" + PATH_CART);
    private static final int CART = 1004;
    private static final String CART_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + URI_CART;

    private static final String PATH_TRENDING_PRODUCTS = DatabaseHelper.Tables.TRENDING_PRODUCTS;
    public static final Uri URI_TRENDING_PRODUCTS = Uri.parse("content://" + AUTHORITY + "/" + PATH_TRENDING_PRODUCTS);
    private static final int TRENDING_PRODUCTS = 1005;
    private static final String TRENDING_PRODUCTS_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + URI_TRENDING_PRODUCTS;

    private static final UriMatcher matcher;

    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, PATH_ORDERS, ORDERS);
        matcher.addURI(AUTHORITY, PATH_KEYS, KEYS);
        matcher.addURI(AUTHORITY, PATH_COLLECTIONS, COLLECTIONS);
        matcher.addURI(AUTHORITY, PATH_PRODUCTS, PRODUCTS);
        matcher.addURI(AUTHORITY, PATH_CART, CART);
        matcher.addURI(AUTHORITY, PATH_TRENDING_PRODUCTS, TRENDING_PRODUCTS);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public int delete(Uri uri, String where, String[] selectionArgs) {
        final int match = matcher.match(uri);
        int rowsAffected;
        SQLiteDatabase sqlDB = DatabaseSQLiteOpenHelper.getInstance(getContext()).getWritableDatabase();
        switch (match) {

            case ORDERS:
                rowsAffected = sqlDB.delete(DatabaseHelper.Tables.ORDERS, where, selectionArgs);
                break;

            case COLLECTIONS:
                rowsAffected = sqlDB.delete(DatabaseHelper.Tables.COLLECTIONS, where, selectionArgs);
                break;

            case PRODUCTS:
                rowsAffected = sqlDB.delete(DatabaseHelper.Tables.PRODUCTS, where, selectionArgs);
                break;

            case CART:
                rowsAffected = sqlDB.delete(DatabaseHelper.Tables.CART, where, selectionArgs);
                break;

            case TRENDING_PRODUCTS:
                rowsAffected = sqlDB.delete(DatabaseHelper.Tables.TRENDING_PRODUCTS, where, selectionArgs);
                break;

            case KEYS:
                rowsAffected = sqlDB.delete(DatabaseHelper.Tables.KEYS, where, selectionArgs);
                break;

            default:
                throw new SQLException("Failed to delete row " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsAffected;
    }

    @Override
    public String getType(Uri uri) {
        final int match = matcher.match(uri);
        switch (match) {

            case ORDERS:
                return ORDERS_CONTENT_TYPE;

            case KEYS:
                return KEYS_CONTENT_TYPE;

            case PRODUCTS:
                return PRODUCTS_CONTENT_TYPE;

            case CART:
                return CART_CONTENT_TYPE;

            case COLLECTIONS:
                return COLLECTIONS_CONTENT_TYPE;

            case TRENDING_PRODUCTS:
                return TRENDING_PRODUCTS_CONTENT_TYPE;
        }
        return null;
    }

    private String getTable(Uri uri) {
        final int match = matcher.match(uri);
        String tableName = null;
        switch (match) {
            case ORDERS:
                tableName = DatabaseHelper.Tables.ORDERS;
                break;
            case KEYS:
                tableName = DatabaseHelper.Tables.KEYS;
                break;
            case COLLECTIONS:
                tableName = DatabaseHelper.Tables.COLLECTIONS;
                break;
            case PRODUCTS:
                tableName = DatabaseHelper.Tables.PRODUCTS;
                break;
            case CART:
                tableName = DatabaseHelper.Tables.CART;
                break;

            case TRENDING_PRODUCTS:
                tableName = DatabaseHelper.Tables.TRENDING_PRODUCTS;
                break;
        }
        if (tableName == null) {
            throw new SQLException("Invalid URI " + uri);
        }
        return tableName;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        SQLiteDatabase sqlDB = DatabaseSQLiteOpenHelper.getInstance(getContext()).getWritableDatabase();
        String tableName = getTable(uri);
        int count = 0;
        for (ContentValues initialValues : values) {
            long rowId = sqlDB.insertWithOnConflict(tableName, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
            if (rowId != -1) {
                count++;
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        SQLiteDatabase sqlDB = DatabaseSQLiteOpenHelper.getInstance(getContext()).getWritableDatabase();
        String tableName = getTable(uri);

        long rowId = sqlDB.insertWithOnConflict(tableName, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
        Uri incidentUri = ContentUris.withAppendedId(uri, rowId);
        getContext().getContentResolver().notifyChange(incidentUri, null);
        return incidentUri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final int match = matcher.match(uri);
        String tableName = null;
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (match) {

            case ORDERS:
                tableName = DatabaseHelper.Tables.ORDERS;
                break;
            case KEYS:
                tableName = DatabaseHelper.Tables.KEYS;
                break;
            case COLLECTIONS:
                tableName = DatabaseHelper.Tables.COLLECTIONS;
                break;
            case PRODUCTS:
                tableName = DatabaseHelper.Tables.PRODUCTS;
                break;
            case CART:
                tableName = DatabaseHelper.Tables.CART;
                break;
            case TRENDING_PRODUCTS:
                tableName = DatabaseHelper.Tables.TRENDING_PRODUCTS;
                break;
        }
        if (tableName == null) {
            throw new SQLException("Invalid URI " + uri);
        }
        qb.setTables(tableName);

        Cursor c = qb.query(DatabaseSQLiteOpenHelper.getInstance(getContext()).getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
        if (c != null) {
            c.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = matcher.match(uri);
        SQLiteDatabase sqlDB = DatabaseSQLiteOpenHelper.getInstance(getContext()).getWritableDatabase();
        int rowsAffected;
        switch (match) {

            case ORDERS:
                rowsAffected = sqlDB.update(DatabaseHelper.Tables.ORDERS, values, selection, selectionArgs);
                break;

            case KEYS:
                rowsAffected = sqlDB.update(DatabaseHelper.Tables.KEYS, values, selection, selectionArgs);
                break;

            case COLLECTIONS:
                rowsAffected = sqlDB.update(DatabaseHelper.Tables.COLLECTIONS, values, selection, selectionArgs);
                break;

            case PRODUCTS:
                rowsAffected = sqlDB.update(DatabaseHelper.Tables.PRODUCTS, values, selection, selectionArgs);
                break;

            case CART:
                rowsAffected = sqlDB.update(DatabaseHelper.Tables.CART, values, selection, selectionArgs);
                break;

            case TRENDING_PRODUCTS:
                rowsAffected = sqlDB.update(DatabaseHelper.Tables.TRENDING_PRODUCTS, values, selection, selectionArgs);
                break;

            default:
                throw new SQLException("Invalid URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsAffected;
    }
}