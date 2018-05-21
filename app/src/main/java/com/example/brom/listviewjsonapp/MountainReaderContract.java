package com.example.brom.listviewjsonapp;

import android.provider.BaseColumns;


public class MountainReaderContract {
    // This class should contain your database schema.
    // See: https://developer.android.com/training/data-storage/sqlite.html#DefineContract

    private MountainReaderContract(){}

    // Inner class that defines the Mountain table contents
    public static class MountainEntry implements BaseColumns {
        public static final String TABLE_NAME = "gamelist";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_COMPANY = "company";
        public static final String COLUMN_NAME_SIZE = "size";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_DESC = "desc";
    }

    public static final String SQL_STRING = "CREATE TABLE IF NOT EXISTS " +
            MountainEntry.TABLE_NAME + " (" +
            MountainEntry._ID + " INTEGER PRIMARY KEY," +
            MountainEntry.COLUMN_NAME_NAME + " TEXT NOT NULL UNIQUE," +
            MountainEntry.COLUMN_NAME_COMPANY + " TEXT," +
            MountainEntry.COLUMN_NAME_CATEGORY + " TEXT," +
            MountainEntry.COLUMN_NAME_SIZE + " INTEGER," +
            MountainEntry.COLUMN_NAME_DESC + " TEXT" +
            ")";
}