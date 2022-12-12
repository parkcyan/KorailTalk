package com.example.korailtalk.node;

import android.content.Context;
import android.os.Handler;

import androidx.room.Room;

import com.example.korailtalk.KtData;
import com.example.korailtalk.node.Node;
import com.example.korailtalk.node.NodeDAO;
import com.example.korailtalk.node.NodeDB;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;

public class NodeRoom {

    private final int[] citycode = {11, 12, 21, 22, 23, 24, 25, 26, 31, 32, 33, 34, 35, 36, 37, 38};
    private final String[] mainnode = {"서울", "용산", "광명", "영등포", "수원", "평택", "천안아산", "천안",
            "오송", "조치원", "대전", "서대전", "김천구미", "구미", "동대구", "대구", "울산(통도사)", "포항", "경산", "밀양",
            "부산", "신경주", "구포", "창원중앙", "평창", "진부", "강릉", "익산", "전주", "광주송정", "목포", "순천",
            "청량리", "여수EXPO", "동해", "정동진", "안동", "서원주", "원주", "마산"};
    private final String[] ns = {"가", "나", "다", "마", "바", "사", "아", "자", "차", "타", "파", "하", "힣"};
    private final NodeDAO nodeDAO;
    private final NodeDB nodeDB;
    private final Handler handler;

    public NodeRoom(Context context, Handler handler) {
        nodeDB = Room.databaseBuilder(context, NodeDB.class, "KtRoom").build();
        nodeDAO = nodeDB.getNodeDAO();
        this.handler = handler;
    }

    public void checkNodeTable() {
        new Thread(() -> {
            if (!hasTable() || !hasNode()) getNodeFromApi();
            else {
                try {
                    Thread.sleep(1500);
                    handler.sendMessage(handler.obtainMessage(1, 1));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    public void getNodeFromApi() {
        new Thread(() -> {
            try {
                if (hasTable()) nodeDAO.truncate();
                for (int city : citycode) {
                    JSONArray nodeArray = KtData.api.getNode(city);
                    for (int i = 0; i < nodeArray.length(); i++) {
                        Node node = new Gson().fromJson(nodeArray.getJSONObject(i).toString(), Node.class);
                        node.mainnode = Arrays.asList(mainnode).contains(node.nodename);
                        nodeDB.getNodeDAO().insert(node);
                    }
                }
                handler.sendMessage(handler.obtainMessage(1, 1));
            } catch (Exception e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    public void getMainNodes() {
        new Thread(() -> handler.sendMessage(handler.obtainMessage(1, nodeDAO.getMainNodes()))).start();
    }

    public void getAllNodes() {
        new Thread(() -> {
            handler.sendMessage(handler.obtainMessage(2, nodeDAO.getAllNodes()));
        }).start();
        getNodeCountBetween();
    }

    public void getNodeCountBetween() {
        new Thread(() -> {
            ArrayList<Integer> arr = new ArrayList<>();
            int index = 0;
            arr.add(index);
            for (int i = 0; i < ns.length - 1; i++) {
                index += nodeDAO.getNodeCountBetween(ns[i], ns[i + 1]);
                arr.add(index);
            }
            handler.sendMessage(handler.obtainMessage(3, arr));
        }).start();
    }

    private boolean hasTable() {
        return nodeDAO.hasTable() != null;
    }

    private boolean hasNode() {
        return nodeDAO.hasNode() != null;
    }

}
