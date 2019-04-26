package com.done.recommendation.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recommendation.db";//Sqlite
    private static final int DATABASE_VERSION = 1;
    private static DatabaseSQLiteOpenHelper sInstance;

    public DatabaseSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseSQLiteOpenHelper getInstance(Context context) {
        if (sInstance == null) {
            synchronized (context) {
                sInstance = new DatabaseSQLiteOpenHelper(context.getApplicationContext());
            }
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        addTables_V1(db);
    }

    private void addTables_V1(SQLiteDatabase db) {
        try {
            executeQuery(db, CREATE_KEY);
            executeQuery(db, CREATE_COLLECTION);
            executeQuery(db, CREATE_PRODUCT);
            executeQuery(db, CREATE_TRENDING_PRODUCTS);
            executeQuery(db, CREATE_CART);
            Log.v("DB Created", "DB Created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v("DB Upgrade", "DB Upgrade --> oldVersion:" + oldVersion + "oldVersion:" + oldVersion);
        dropTables(db);
        addTables_V1(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTables(db);
    }

    private void dropTables(SQLiteDatabase sqLiteDatabase) {
        executeQuery(sqLiteDatabase, "DROP TABLE " + DatabaseHelper.Tables.KEYS);
        executeQuery(sqLiteDatabase, "DROP TABLE " + DatabaseHelper.Tables.COLLECTIONS);
        executeQuery(sqLiteDatabase, "DROP TABLE " + DatabaseHelper.Tables.PRODUCTS);
        executeQuery(sqLiteDatabase, "DROP TABLE " + DatabaseHelper.Tables.TRENDING_PRODUCTS);
        executeQuery(sqLiteDatabase, "DROP TABLE " + DatabaseHelper.Tables.CART);
        onCreate(sqLiteDatabase);
    }

    private void executeQuery(SQLiteDatabase sqLiteDatabase, String query) {
        if (sqLiteDatabase != null && query != null) {
            try {
                sqLiteDatabase.execSQL(query);
            } catch (Exception exception) {
                Log.e("SQLiteOpenHelper", "Error in executing query : " + query);
            }
        }
    }

    private final String CREATE_KEY = "CREATE TABLE IF NOT EXISTS " + DatabaseHelper.Tables.KEYS + "(" +
            DatabaseHelper.Keys._ID + " TEXT NOT NULL , " +
            DatabaseHelper.Keys.VALUE + " TEXT " +
            ");";

    private final String CREATE_COLLECTION = "CREATE TABLE IF NOT EXISTS " + DatabaseHelper.Tables.COLLECTIONS + " ( " +
            DatabaseHelper.Collection.COLLECTION_ID + " INTEGER PRIMARY KEY, " +
            DatabaseHelper.Collection.NAME + " TEXT  NOT NULL, " +
            DatabaseHelper.Collection.DESCRIPTION + " TEXT, " +
            DatabaseHelper.Collection.IMAGE_URL + " TEXT, " +
            "UNIQUE( " + DatabaseHelper.Collection.COLLECTION_ID + " ) ON CONFLICT IGNORE " +
            " );";

    private final String CREATE_PRODUCT = "CREATE TABLE IF NOT EXISTS " + DatabaseHelper.Tables.PRODUCTS + " ( " +
            DatabaseHelper.Product.PRODUCT_ID + " INTEGER PRIMARY KEY, " +
            DatabaseHelper.Product.NAME + " TEXT  NOT NULL, " +
            DatabaseHelper.Product.DESCRIPTION + " TEXT, " +
            DatabaseHelper.Product.IMAGE_URL + " TEXT, " +
            DatabaseHelper.Product.IS_VEG + "  INTEGER DEFAULT 1, " +
            DatabaseHelper.Product.COLLECTION_ID + " INTEGER DEFAULT 0, " +
            DatabaseHelper.Product.COLLECTION_NAME + " TEXT, " +
            DatabaseHelper.Product.PRICE + "  INTEGER DEFAULT 0, " +
            DatabaseHelper.Product.RATING + "  INTEGER DEFAULT 0, " +
            DatabaseHelper.Product.INGREDIENTS + " TEXT, " +
            DatabaseHelper.Product.INGREDIENTS_TEXT + " TEXT, " +
            DatabaseHelper.Product.FATS + "  INTEGER DEFAULT 0, " +
            DatabaseHelper.Product.PROTIENS + "  INTEGER DEFAULT 0, " +
            DatabaseHelper.Product.CARBS + "  INTEGER DEFAULT 0, " +
            "UNIQUE( " + DatabaseHelper.Product.PRODUCT_ID + " ) ON CONFLICT IGNORE " +
            " );";

    private final String CREATE_TRENDING_PRODUCTS = "CREATE TABLE IF NOT EXISTS " + DatabaseHelper.Tables.TRENDING_PRODUCTS + " ( " +
            DatabaseHelper.Product.PRODUCT_ID + " INTEGER PRIMARY KEY, " +
            DatabaseHelper.Product.NAME + " TEXT  NOT NULL, " +
            DatabaseHelper.Product.DESCRIPTION + " TEXT, " +
            DatabaseHelper.Product.IMAGE_URL + " TEXT, " +
            DatabaseHelper.Product.IS_VEG + "  INTEGER DEFAULT 1, " +
            DatabaseHelper.Product.COLLECTION_ID + " INTEGER DEFAULT 0, " +
            DatabaseHelper.Product.COLLECTION_NAME + " TEXT, " +
            DatabaseHelper.Product.PRICE + "  INTEGER DEFAULT 0, " +
            DatabaseHelper.Product.RATING + "  INTEGER DEFAULT 0, " +
            DatabaseHelper.Product.INGREDIENTS + " TEXT, " +
            DatabaseHelper.Product.INGREDIENTS_TEXT + " TEXT, " +
            DatabaseHelper.Product.FATS + "  INTEGER DEFAULT 0, " +
            DatabaseHelper.Product.PROTIENS + "  INTEGER DEFAULT 0, " +
            DatabaseHelper.Product.CARBS + "  INTEGER DEFAULT 0, " +
            "UNIQUE( " + DatabaseHelper.Product.PRODUCT_ID + " ) ON CONFLICT IGNORE " +
            " );";

    private final String CREATE_CART = "CREATE TABLE IF NOT EXISTS " + DatabaseHelper.Tables.CART + " ( " +
            DatabaseHelper.Cart.PRODUCT_ID + " INTEGER PRIMARY KEY, " +
            DatabaseHelper.Cart.PRODUCT_NAME + " TEXT  NOT NULL, " +
            DatabaseHelper.Cart.PRODUCT_IMAGE_URL + " TEXT, " +
            DatabaseHelper.Cart.COLLECTION_ID + " INETEGER, " +
            DatabaseHelper.Cart.COLLECTION_NAME + " TEXT, " +
            DatabaseHelper.Cart.PRICE + " TEXT, " +
            DatabaseHelper.Cart.QUANTITY + " INTEGER DEFAULT 0, " +
            DatabaseHelper.Product.INGREDIENTS + " TEXT, " +
            "UNIQUE( " + DatabaseHelper.Cart.PRODUCT_ID + " ) ON CONFLICT IGNORE " +
            " );";
}
