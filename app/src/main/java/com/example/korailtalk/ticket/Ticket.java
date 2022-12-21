package com.example.korailtalk.ticket;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Ticket {
    @PrimaryKey @NonNull
    String ticketingDate; // 예매일자 Timestamp
    String tsDate; // 출발일자 Timestamp
    String depNode;
    String arrNode;
    String depTime;
    String arrTime;
    String trainInfo; // KTX 418
    String trainNum; // 5호차
    String seat; // 10A
    Boolean specialSeat; // 일반실, 특실
    String ticketKind; // 어른
    String ticketNum; // 82105-0731
    int qty; // 1매
}
