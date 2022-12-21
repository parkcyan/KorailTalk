package com.example.korailtalk.room.ticket;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Ticket {
    @PrimaryKey
    @NonNull
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
    int[] qtyArr;

    public Ticket(@NonNull String ticketingDate, String tsDate, String depNode, String arrNode,
                  String depTime, String arrTime, String trainInfo, String trainNum, String seat,
                  Boolean specialSeat, String ticketKind, String ticketNum, int[] qtyArr) {
        this.ticketingDate = ticketingDate;
        this.tsDate = tsDate;
        this.depNode = depNode;
        this.arrNode = arrNode;
        this.depTime = depTime;
        this.arrTime = arrTime;
        this.trainInfo = trainInfo;
        this.trainNum = trainNum;
        this.seat = seat;
        this.specialSeat = specialSeat;
        this.ticketKind = ticketKind;
        this.ticketNum = ticketNum;
        this.qtyArr = qtyArr;
    }

    @NonNull
    public String getTicketingDate() {
        return ticketingDate;
    }

    public String getTsDate() {
        return tsDate;
    }

    public String getDepNode() {
        return depNode;
    }

    public String getArrNode() {
        return arrNode;
    }

    public String getDepTime() {
        return depTime;
    }

    public String getArrTime() {
        return arrTime;
    }

    public String getTrainInfo() {
        return trainInfo;
    }

    public String getTrainNum() {
        return trainNum;
    }

    public String getSeat() {
        return seat;
    }

    public Boolean getSpecialSeat() {
        return specialSeat;
    }

    public String getTicketKind() {
        return ticketKind;
    }

    public String getTicketNum() {
        return ticketNum;
    }

    public int[] getQtyArr() {
        return qtyArr;
    }
}
