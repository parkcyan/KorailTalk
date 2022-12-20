package com.example.korailtalk.ticket;

import android.content.Context;
import android.os.Handler;

public class TicketRoom {

    private final TicketDB ticketDB;
    private final TicketDAO ticketDAO;
    private final Handler handler;

    public TicketRoom(Context context, Handler handler) {
        ticketDB = TicketDB.getInstance(context);
        ticketDAO = ticketDB.getTicketDAO();
        this.handler = handler;
    }

}
