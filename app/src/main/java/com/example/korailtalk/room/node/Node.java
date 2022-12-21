package com.example.korailtalk.room.node;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Node {
    @PrimaryKey @NonNull
    public String nodeid;
    public String nodename;
    public boolean mainnode;
}
