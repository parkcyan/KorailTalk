package com.example.korailtalk.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class NodeDAO extends DBHelper {

    public NodeDAO(Context context) {
        super(context);
    }

    public void insert(ArrayList<NodeDTO> dtoArr) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        for (NodeDTO dto : dtoArr) {
            cv.put("nodeid", dto.getNodeid());
            cv.put("nodename", dto.getNodename());
            cv.put("citycode", dto.getCitycode());
            db.insert(TABLE_NODE, null, cv);
            cv.clear();
        }
    }

    public Cursor list() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + TABLE_NODE, null);
    }

    public void initNode() {
        Cursor cursor = list();
        if (cursor.getCount() == 0) {
            Thread thread = new Thread(() -> {
                try {
                    for (CityDTO cityDTO : MyDatabase.cityArr) {
                        insert(MyDatabase.api.getNode(cityDTO.getCitycode()));
                    }
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
            MyDatabase.nodeArr.add(new NodeDTO(cursor.getString(0),
                    cursor.getString(1), cursor.getInt(2)));
        }
    }

}
