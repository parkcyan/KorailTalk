package com.example.korailtalk.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "korailtalk.db";
    public static final String TABLE_CITY = "city";
    public static final String TABLE_NODE = "node";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + TABLE_CITY +
                " (citycode number primary key not null," +
                " cityname varchar(25) not null)");
        db.execSQL("create table if not exists " + TABLE_NODE +
                " (nodeid varchar(25) primary key not null," +
                " nodename varchar(20) not null," +
                " citycode number not null," +
                " foreign key (citycode) references city (citycode))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
