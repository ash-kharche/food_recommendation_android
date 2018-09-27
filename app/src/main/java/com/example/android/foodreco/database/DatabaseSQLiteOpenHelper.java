package com.example.android.foodreco.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.foodreco.Constants;

public class DatabaseSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bookex.db";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseSQLiteOpenHelper sInstance;

    public DatabaseSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseSQLiteOpenHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseSQLiteOpenHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(Constants.TAG, "onCreate ::  version: " + DATABASE_VERSION);
        createOrderTable_v1(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(Constants.TAG, "onUpgrade ::  oldVersion: " + oldVersion + "   newVersion:  " + newVersion);
    }

    private void createOrderTable_v1(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DatabaseHelper.Tables.KEYS + "(" +
                DatabaseHelper.Keys._ID + " TEXT NOT NULL , " +
                DatabaseHelper.Keys.VALUE + " TEXT " +
                ");");
    }
}
