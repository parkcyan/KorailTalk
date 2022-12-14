package com.example.korailtalk.node;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Node.class}, version = 1)
public abstract class NodeDB extends RoomDatabase {

    private static NodeDB INSTANCE;

    public abstract NodeDAO getNodeDAO();

    public static NodeDB getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = Room.databaseBuilder(context, NodeDB.class, "KtRoom").build();
        return INSTANCE;
    }

}
