package com.example.korailtalk.ticket;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.korailtalk.node.NodeDB;

@Database(entities = {Ticket.class}, version = 1)
public abstract class TicketDB extends RoomDatabase {

    private static TicketDB INSTANCE;

    public abstract TicketDAO getTicketDAO();

    public static TicketDB getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = Room.databaseBuilder(context, TicketDB.class, "KtRoom").build();
        return INSTANCE;
    }

}
