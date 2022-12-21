package com.example.korailtalk.ticketing.data;

import com.example.korailtalk.util.Util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;

public class TrainVO implements Serializable {

    private String depplacename, arrplacename, traingradename, trainno, depplandtime, arrplandtime;
    private String adultcharge, adultscharge;
    private int charge;

    public TrainVO(Train train) {
        depplacename = train.getDepplacename();
        arrplacename = train.getArrplacename();

        // 42,500원
        DecimalFormat chargeDf = new DecimalFormat("###,###원");
        adultcharge = chargeDf.format(train.getAdultcharge());
        charge = train.getAdultcharge();

        // 3 -> 003 13 -> 013
        if (train.getTrainno() < 10) trainno = "00" + train.getTrainno();
        else if (train.getTrainno() < 100) trainno = "0" + train.getTrainno();
        else trainno = String.valueOf(train.getTrainno());

        // 20221201061200 -> 06:12
        DecimalFormat df = new DecimalFormat("000000");
        BigDecimal dt = BigDecimal.valueOf(train.getDepplandtime());
        BigDecimal at = BigDecimal.valueOf(train.getArrplandtime());
        int dInt = dt.remainder(BigDecimal.valueOf(1000000), MathContext.DECIMAL128).intValue();
        int aInt = at.remainder(BigDecimal.valueOf(1000000), MathContext.DECIMAL128).intValue();
        StringBuilder dsb = new StringBuilder(df.format(dInt)).delete(4, 6);
        StringBuilder asb = new StringBuilder(df.format(aInt)).delete(4, 6);
        depplandtime = dsb.insert(2, ":").toString();
        arrplandtime = asb.insert(2, ":").toString();

        // KTX-산천 (A-type) -> KTX-산천
        if (train.getTraingradename().contains("KTX-산천")) traingradename = "KTX-산천";
        else traingradename = train.getTraingradename();

        // KTX만 특실 요금 활성화
        if (traingradename.contains("KTX")) adultscharge = chargeDf.format(Util.roundCharge(train.getAdultcharge() * 1.4));

        // api에서 불러온 정보에서 0원으로 가격이 안나올 경우 매진으로 처리
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

    public int getCharge() {
        return charge;
    }
}
