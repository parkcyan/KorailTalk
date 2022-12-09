package com.example.korailtalk.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class CityDAO extends DBHelper {

    public CityDAO(Context context) {
        super(context);
    }

    public void insert(ArrayList<CityDTO> dtoArr) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        for (CityDTO dto : dtoArr) {
            cv.put("citycode", dto.getCitycode());
            cv.put("cityname", dto.getCityname());
            db.insert(TABLE_CITY, null, cv);
            cv.clear();
        }
    }

    public Cursor list() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + TABLE_CITY, null);
    }

    public void initCity() {
        Cursor cursor = list();
        if (cursor.getCount() == 0) {
            Thread thread = new Thread(() -> {
                try {
                    insert(MyDatabase.api.getCitycode());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cursor = list();
        }

        while (cursor.moveToNext()) {
            MyDatabase.cityArr.add(new CityDTO(cursor.getInt(0), cursor.getString(1)));
        }
    }
}
