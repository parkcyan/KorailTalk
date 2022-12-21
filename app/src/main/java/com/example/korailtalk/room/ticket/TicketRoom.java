package com.example.korailtalk.room.ticket;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.example.korailtalk.room.KtRoom;

public class TicketRoom {

    public static final int GET_TICKET_SUCCESS = 3;

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

    private boolean hasTable() {
        return ticketDAO.hasTable() != null;
    }

}
