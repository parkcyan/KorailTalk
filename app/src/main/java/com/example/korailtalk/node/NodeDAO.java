package com.example.korailtalk.node;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface NodeDAO {

    @Insert
    void insert(Node node);

    @Query("delete from node")
    void truncate();

    @Query("select * from node order by nodename")
    List<Node> getAllNodes();

    @Query("select name from sqlite_master where name = 'Node'")
    String hasTable();

    @Query("select nodeid from node where nodename = '서울'")
    String hasNode();


//    Context context;
//
//    public NodeDAO(Context context) {
//        this.context = context;
//    }
//
//    public void getNode() {
//        try {
//            for (int city : KtData.citycode) {
//                Reader reader = new InputStreamReader(context.getResources().getAssets().open(city + ".json"));
//                KtData.nodeArr = new Gson().fromJson(reader, new TypeToken<ArrayList<NodeDTO>>() {
//                }.getType());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
