package com.example.korailtalk.room.ticket;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.example.korailtalk.room.KtRoom;

import java.util.ArrayList;

public class TicketRoom {

    public static final int GET_TICKET_SUCCESS = 3;
    public static final int GET_LIST_TICKETS = 1;
    public static final int INSERT_TICKET_SUCCESS = 1;

    private final KtRoom room;
    private final TicketDAO ticketDAO;
    private final Handler handler;

    public TicketRoom(Context context, Handler handler) {
        room = KtRoom.getInstance(context);
        ticketDAO = room.getTicketDAO();
        this.handler = handler;
    }

    public void checkTicketTable() {
        new Thread(() -> {
            if (hasTable()) {
                try {
                    Thread.sleep(250);
                    Log.d(TAG, "ticketRoom: GET_TICKET_SUCCESS");
                    handler.sendMessage(handler.obtainMessage(GET_TICKET_SUCCESS, 1));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    public void addTicket(Ticket ticket) {
        new Thread(() -> {
           ticketDAO.insert(ticket);
           handler.sendMessage(handler.obtainMessage(INSERT_TICKET_SUCCESS, 1));
        }).start();
    }

    public void getTickets() {
        new Thread(() -> {
            ArrayList<Ticket> ticketList = (ArrayList<Ticket>) ticketDAO.getTickets();
            handler.sendMessage(handler.obtainMessage(GET_LIST_TICKETS, ticketList));
        }).start();
    }

    private boolean hasTable() {
        return ticketDAO.hasTable() != null;
    }

}
