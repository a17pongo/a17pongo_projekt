package com.example.brom.listviewjsonapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MountainReaderDbHelper extends SQLiteOpenHelper {

    // TODO: You need to add member variables and methods to this helper class
    // See: https://developer.android.com/training/data-storage/sqlite.html#DbHelper

    MountainReaderDbHelper(Context context){
        super(context,"mountainsdb",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MountainReaderContract.SQL_STRING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL();
    }


}
