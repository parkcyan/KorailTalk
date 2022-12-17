package com.example.korailtalk.ticketing.data;

public class NodeVO {

    private String node1, node2;
    private int viewType;

    public NodeVO(String node1, String node2, int viewType) {
        this.node1 = node1;
        this.node2 = node2;
        this.viewType = viewType;
    }

    public String getNode1() {
        return node1;
    }

    public void setNode1(String node1) {
        this.node1 = node1;
    }

    public String getNode2() {
        return node2;
    }

    public void setNode2(String node2) {
        this.node2 = node2;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
