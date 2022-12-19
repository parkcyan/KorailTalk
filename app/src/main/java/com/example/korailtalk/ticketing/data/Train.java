package com.example.korailtalk.ticketing.data;

public class Train {

    private String depplacename, arrplacename, traingradename;
    private int adultcharge, trainno;
    private double depplandtime, arrplandtime;

    public Train(String depplacename, String arrplacename, String traingradename, int adultcharge, int trainno, double depplandtime, double arrplandtime) {
        this.depplacename = depplacename;
        this.arrplacename = arrplacename;
        this.traingradename = traingradename;
        this.adultcharge = adultcharge;
        this.trainno = trainno;
        this.depplandtime = depplandtime;
        this.arrplandtime = arrplandtime;
    }

    public String getDepplacename() {
        return depplacename;
    }

    public String getArrplacename() {
        return arrplacename;
    }

    public String getTraingradename() {
        return traingradename;
    }

    public int getAdultcharge() {
        return adultcharge;
    }

    public int getTrainno() {
        return trainno;
    }

    public double getDepplandtime() {
        return depplandtime;
    }

    public double getArrplandtime() {
        return arrplandtime;
    }

}
