package com.example.korailtalk.node;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Node.class}, version = 1)
public abstract class NodeDB extends RoomDatabase {
    public abstract NodeDAO getNodeDAO();
}
