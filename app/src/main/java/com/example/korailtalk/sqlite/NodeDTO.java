package com.example.korailtalk.sqlite;

public class NodeDTO {

    int citycode;
    String nodeid, nodename;

    public NodeDTO(String nodeid, String nodename, int citycode) {
        this.nodeid = nodeid;
        this.nodename = nodename;
        this.citycode = citycode;
    }

    public int getCitycode() {
        return citycode;
    }

    public void setCitycode(int citycode) {
        this.citycode = citycode;
    }

    public String getNodeid() {
        return nodeid;
    }

    public void setNodeid(String nodeid) {
        this.nodeid = nodeid;
    }

    public String getNodename() {
        return nodename;
    }

    public void setNodename(String nodename) {
        this.nodename = nodename;
    }
}
