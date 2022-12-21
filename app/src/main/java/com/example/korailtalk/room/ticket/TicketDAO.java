package com.example.korailtalk.room.ticket;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TicketDAO {

    @Insert
    void insert(Ticket ticket);

    @Query("select * from ticket")
    List<Ticket> getTickets();

    @Query("select name from sqlite_master where name = 'Ticket'")
    String hasTable();

}
