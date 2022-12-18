package com.example.korailtalk.ticketing.data;

import com.example.korailtalk.node.Train;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;

public class TrainRvVO {

    private String depplacename, arrplacename, traingradename, trainno, depplandtime, arrplandtime;
    private String adultcharge, adultscharge;

    public TrainRvVO(Train train) {
        DecimalFormat df = new DecimalFormat("000000");
        DecimalFormat chargeDf = new DecimalFormat("###,###원");
        depplacename = train.getDepplacename();
        arrplacename = train.getArrplacename();
        adultcharge = chargeDf.format(train.getAdultcharge());
        if (train.getTrainno() < 10) trainno = "00" + train.getTrainno();
        else if (train.getTrainno() < 100) trainno = "0" + train.getTrainno();
        else trainno = String.valueOf(train.getTrainno());
        BigDecimal dt = BigDecimal.valueOf(train.getDepplandtime());
        BigDecimal at = BigDecimal.valueOf(train.getArrplandtime());
        int dInt = dt.remainder(BigDecimal.valueOf(1000000), MathContext.DECIMAL128).intValue();
        int aInt = at.remainder(BigDecimal.valueOf(1000000), MathContext.DECIMAL128).intValue();
        StringBuilder dsb = new StringBuilder(df.format(dInt)).delete(4, 6);
        StringBuilder asb = new StringBuilder(df.format(aInt)).delete(4, 6);
        depplandtime = dsb.insert(2, ":").toString();
        arrplandtime = asb.insert(2, ":").toString();
        if (train.getTraingradename().contains("KTX-산천")) traingradename = "KTX-산천";
        else traingradename = train.getTraingradename();
        if (traingradename.contains("KTX")) adultscharge = chargeDf.format(train.getAdultcharge() * 1.4);
        if (adultcharge.equals("0원")) adultcharge = "매진";
        if (adultscharge != null && adultscharge.equals("0원")) adultscharge = "매진";
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

    public String getAdultcharge() {
        return adultcharge;
    }

    public String getTrainno() {
        return trainno;
    }

    public String getDepplandtime() {
        return depplandtime;
    }

    public String getArrplandtime() {
        return arrplandtime;
    }

    public String getAdultscharge() {
        return adultscharge;
    }
}
