package com.example.korailtalk;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ApiUnitTest {

    private final String SERVICE_KEY = "bV8Qa13gBAIQTlWOVkdceGorgqXDUFoPycZ71CS/Ez5b7w/kN1w89tV2zMBWwU4FZfaCqjC15LXqszMNwumAzw==";
    private final String ENCODE = "UTF-8";
    private final String DATA_TYPE = "json";

    @Test
    public void getCitycode() throws IOException, JSONException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1613000/TrainInfoService/getCtyCodeList");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", ENCODE) + "=" + SERVICE_KEY);
        urlBuilder.append("&" + URLEncoder.encode("_type", ENCODE) + "=" + URLEncoder.encode(DATA_TYPE, ENCODE));
        JSONObject json = connect(urlBuilder);
        System.out.println(json.toString());
    }

    @Test
    public void getStation() throws IOException, JSONException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1613000/TrainInfoService/getCtyAcctoTrainSttnList");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", ENCODE) + "=" + SERVICE_KEY)
                .append("&" + URLEncoder.encode("_type", ENCODE) + "=" + URLEncoder.encode(DATA_TYPE, ENCODE))
                .append("&" + URLEncoder.encode("cityCode", ENCODE) + "=" + URLEncoder.encode("11", ENCODE)); /* 시/도 ID */
        JSONObject json = connect(urlBuilder)
                .getJSONObject("response")
                .getJSONObject("body")
                .getJSONObject("items")
                .getJSONObject("item");
        JSONArray itemArr = json.getJSONArray("item");
        for (int i = 0; i < itemArr.length(); i++) {
            JSONObject j = itemArr.getJSONObject(i);
        }
    }

    @Test
    public JSONObject connect(StringBuilder urlBuilder) throws IOException, JSONException {
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        return new JSONObject(sb.toString());
    }
}