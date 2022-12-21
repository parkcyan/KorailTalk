package com.example.korailtalk.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.korailtalk.room.node.Node;
import com.example.korailtalk.room.node.NodeDAO;
import com.example.korailtalk.room.ticket.QtyArrConverter;
import com.example.korailtalk.room.ticket.Ticket;
import com.example.korailtalk.room.ticket.TicketDAO;

@Database(entities = {Node.class, Ticket.class}, version = 1)
@TypeConverters(QtyArrConverter.class)
public abstract class KtRoom extends RoomDatabase {

    private static KtRoom INSTANCE;

    public abstract NodeDAO getNodeDAO();
    public abstract TicketDAO getTicketDAO();

    public static KtRoom getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = Room.databaseBuilder(context, KtRoom.class, "KtRoom").build();
        return INSTANCE;
    }

}
