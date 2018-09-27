package com.example.android.foodreco.database;

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

    public static final String AUTHORITY = "bookex";

    private static final String PATH_KEYS = DatabaseHelper.Tables.KEYS;
    public static final Uri URI_KEYS = Uri.parse("content://" + AUTHORITY + "/" + PATH_KEYS);
    private static final int KEYS = 1001;
    private static final String KEYS_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + URI_KEYS;

    private static final UriMatcher matcher;

    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, PATH_KEYS, KEYS);
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
            case KEYS:
                return KEYS_CONTENT_TYPE;
        }
        return null;
    }

    private String getTable(Uri uri) {
        final int match = matcher.match(uri);
        String tableName = null;
        switch (match) {
            case KEYS:
                tableName = DatabaseHelper.Tables.KEYS;
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
            case KEYS:
                tableName = DatabaseHelper.Tables.KEYS;
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
            case KEYS:
                rowsAffected = sqlDB.update(DatabaseHelper.Tables.KEYS, values, selection, selectionArgs);
                break;

            default:
                throw new SQLException("Invalid URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsAffected;
    }
}