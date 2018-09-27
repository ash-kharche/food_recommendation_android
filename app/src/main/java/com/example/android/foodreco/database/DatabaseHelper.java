package com.example.android.foodreco.database;

public class DatabaseHelper {
    public interface Tables {
        String BOOKS = "books";      //version 1
        String KEYS = "keys";      //version 1
    }

    /**
     * Database version 1
     */
    public interface Keys {
        String _ID = "_id";
        String VALUE = "value";

        String BOOKS = "books";
        String CUSTOMER = "customer";
    }
}