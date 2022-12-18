package com.example.korailtalk.node;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.example.korailtalk.Util;
import com.example.korailtalk.api.ApiExplorer;
import com.example.korailtalk.ticketing.data.NodeVO;
import com.example.korailtalk.ticketing.data.TrainVO;
import com.example.korailtalk.ticketing.data.TrainRvVO;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

public class NodeRoom {

    public static final int GET_NODE_SUCCESS = 1;
    public static final int SEARCH_SUCCESS = 1;
    public static final int GET_LIST_FOR_RV = 2;

    public static final int GET_TRAIN_SUCCESS = 1;
    public static final int GET_TRAIN_FAILED = -1;
    public static final ArrayList<NodeVO> nodeListForRv = new ArrayList<>();
    private final NodeDAO nodeDAO;
    private final NodeDB nodeDB;
    private final int[] citycode = {11, 12, 21, 22, 23, 24, 25, 26, 31, 32, 33, 34, 35, 36, 37, 38};
    private final String[] mainnode = {"서울", "용산", "광명", "영등포", "수원", "평택", "천안아산", "천안",
            "오송", "조치원", "대전", "서대전", "김천구미", "구미", "동대구", "대구", "울산(통도사)", "포항", "경산", "밀양",
            "부산", "신경주", "구포", "창원중앙", "평창", "진부", "강릉", "익산", "전주", "광주송정", "목포", "순천",
            "청량리", "여수EXPO", "동해", "정동진", "안동", "서원주", "원주", "마산"};
    private final String[] searchStr = {"가", "나", "다", "마", "바", "사", "아", "자", "차", "타", "파", "하"};
    private final String[] fl = {"가까운역", "최근검색구간", "주요역", "ㄱ", "ㄴ", "ㄷ", "ㅁ", "ㅂ",
            "ㅅ", "ㅇ", "ㅈ", "ㅊ", "ㅌ", "ㅍ", "ㅎ"};
    private final Handler handler;
    private final ApiExplorer apiExplorer = ApiExplorer.getInstance();

    public NodeRoom(Context context, Handler handler) {
        nodeDB = NodeDB.getInstance(context);
        nodeDAO = nodeDB.getNodeDAO();
        this.handler = handler;
    }

    public void checkNodeTable() {
        new Thread(() -> {
            if (!hasTable() || !hasNode()) getNodeFromApi();
            else {
                try {
                    Thread.sleep(500);
                    Log.d(TAG, "nodeRoom: GET_NODE_SUCCESS");
                    handler.sendMessage(handler.obtainMessage(GET_NODE_SUCCESS, 1));
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
                    JSONArray nodeArr = apiExplorer.getNode(city);
                    for (int i = 0; i < nodeArr.length(); i++) {
                        Node node = new Gson().fromJson(nodeArr.getJSONObject(i).toString(), Node.class);
                        node.mainnode = Arrays.asList(mainnode).contains(node.nodename);
                        nodeDB.getNodeDAO().insert(node);
                    }
                }
                handler.sendMessage(handler.obtainMessage(GET_NODE_SUCCESS, 1));
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    public void getNodeForRv() {
        new Thread(() -> {
            ArrayList<Node> mainNodeList = (ArrayList<Node>) nodeDAO.getMainNodes();
            ArrayList<Node> allNodeList = (ArrayList<Node>) nodeDAO.getAllNodes();
            ArrayList<Integer> indexList = new ArrayList<>();
            int index = 0;
            indexList.add(index);
            for (int i = 0; i < searchStr.length - 1; i++) {
                index += nodeDAO.getNodeCountBetween(searchStr[i], searchStr[i + 1]);
                indexList.add(index);
            }
            nodeListForRv.add(new NodeVO(fl[0], null, 1));
            nodeListForRv.add(new NodeVO(fl[1], null, 1));
            nodeListForRv.add(new NodeVO(fl[2], null, 1));
            for (int i = 0; i < mainNodeList.size(); i += 2) {
                if (i + 1 != mainNodeList.size()) {
                    nodeListForRv.add(new NodeVO(mainNodeList.get(i).nodename, mainNodeList.get(i + 1).nodename, 0));
                } else {
                    nodeListForRv.add(new NodeVO(mainNodeList.get(i).nodename, "", 0));
                }
            }
            int flIndex = 3;
            for (int i = 0; i < allNodeList.size(); ) {
                if (indexList.contains(i)) {
                    nodeListForRv.add(new NodeVO(fl[flIndex++], null, 1));
                }
                if (!indexList.contains(i + 1)) {
                    nodeListForRv.add(new NodeVO(allNodeList.get(i++).nodename, allNodeList.get(i++).nodename, 0));
                } else {
                    nodeListForRv.add(new NodeVO(allNodeList.get(i++).nodename, "", 0));
                }
            }
            Log.d(TAG, "nodeRoom: GET_LIST_FOR_RV");
            handler.sendMessage(handler.obtainMessage(GET_LIST_FOR_RV, nodeListForRv));
        }).start();
    }

    public void searchNodes(String str) {
        new Thread(() -> {
            if (str.equals("")) handler.obtainMessage(SEARCH_SUCCESS, null);
            else {
                ArrayList<Node> searchList = (ArrayList<Node>) nodeDAO.searchNodes(str);
                ArrayList<NodeVO> searchListForRv = new ArrayList<>();
                for (int i = 0; i < searchList.size(); i++) {
                    if (i + 1 != searchList.size()) {
                        searchListForRv.add(new NodeVO(searchList.get(i).nodename, searchList.get(i + 1).nodename, 2));
                        i++;
                    } else searchListForRv.add(new NodeVO(searchList.get(i).nodename, "", 2));
                }
                handler.sendMessage(handler.obtainMessage(SEARCH_SUCCESS, searchListForRv));
            }
        }).start();
    }

    public void getTrainFromApi(String depNode, String arrNode, Timestamp tsDate) {
        new Thread(() -> {
            String[] nodeidArr = new String[2];
            nodeidArr[0] = nodeDAO.getNodeid(depNode);
            nodeidArr[1] = nodeDAO.getNodeid(arrNode);
            try {
                JSONArray trainArr = apiExplorer.getTrain(nodeidArr[0], nodeidArr[1], Util.dateFormatInt(tsDate, "yyyyMMdd"), 1);
                ArrayList<TrainRvVO> trainList = new ArrayList<>();
                for (int i = 0; i < trainArr.length(); i++) {
                    TrainVO trainVO = new Gson().fromJson(trainArr.getJSONObject(i).toString(), TrainVO.class);
                    if (Util.getTimestmpFromDouble(trainVO.getDepplandtime()).getTime() > tsDate.getTime()) {
                        trainList.add(new TrainRvVO(trainVO));
                    }
                }
                handler.sendMessage(handler.obtainMessage(GET_TRAIN_SUCCESS, trainList));
            } catch (IOException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            } catch (JSONException e) {
                handler.sendMessage(handler.obtainMessage(GET_TRAIN_FAILED, null));
            }
        }).start();
    }

    private boolean hasTable() {
        return nodeDAO.hasTable() != null;
    }

    private boolean hasNode() {
        return nodeDAO.hasNode() != null;
    }

}
