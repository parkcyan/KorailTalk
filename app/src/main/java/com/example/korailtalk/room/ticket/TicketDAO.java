package com.example.korailtalk.room.ticket;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface TicketDAO {

    @Query("select name from sqlite_master where name = 'Ticket'")
    String hasTable();

}
