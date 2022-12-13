package com.example.korailtalk.node;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NodeDAO {

    @Insert
    void insert(Node node);

    @Query("delete from node")
    void truncate();

    @Query("select * from node order by nodename")
    List<Node> getAllNodes();

    @Query("select * from node where mainnode = 1")
    List<Node> getMainNodes();

    @Query("select * from node where nodename like '%' || :str || '%' order by nodename")
    List<Node> searchNodes(String str);

    @Query("select count(*) from node where nodename >= :start and nodename < :end")
    int getNodeCountBetween(String start, String end);

    @Query("select name from sqlite_master where name = 'Node'")
    String hasTable();

    @Query("select nodeid from node where nodename = '서울'")
    String hasNode();

}
