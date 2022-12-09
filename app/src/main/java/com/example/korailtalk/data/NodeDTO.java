package com.example.korailtalk.data;

public class NodeDTO {

    String nodeid, nodename;
    boolean mainnode;

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

    public boolean isMainnode() {
        return mainnode;
    }

    public void setMainnode(boolean mainnode) {
        this.mainnode = mainnode;
    }

}
