package com.example.korailtalk.api;

import android.util.Log;

import com.example.korailtalk.sqlite.CityDTO;
import com.example.korailtalk.data.NodeDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ApiExplorer {

    private static final String SERVICE_KEY = "bV8Qa13gBAIQTlWOVkdceGorgqXDUFoPycZ71CS/Ez5b7w/kN1w89tV2zMBWwU4FZfaCqjC15LXqszMNwumAzw==";
    private static final String ENCODE = "UTF-8";
    private static final String DATA_TYPE = "json";

    public ArrayList<CityDTO> getCitycode() throws IOException, JSONException {
        ArrayList<CityDTO> dtoArr = new ArrayList<>();
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1613000/TrainInfoService/getCtyCodeList");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", ENCODE) + "=" + SERVICE_KEY);
        urlBuilder.append("&" + URLEncoder.encode("_type", ENCODE) + "=" + URLEncoder.encode(DATA_TYPE, ENCODE));
        JSONObject json = connect(urlBuilder)
                .getJSONObject("response")
                .getJSONObject("body")
                .getJSONObject("items");
        JSONArray itemArr = json.getJSONArray("item");
        for (int i = 0; i < itemArr.length(); i++) {
            JSONObject j = itemArr.getJSONObject(i);
            dtoArr.add(new CityDTO(j.getInt("citycode"), j.getString("cityname")));
        }
        return dtoArr;
    }

    public ArrayList<NodeDTO> getNode(int code) throws IOException, JSONException {
        ArrayList<NodeDTO> dtoArr = new ArrayList<>();
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1613000/TrainInfoService/getCtyAcctoTrainSttnList");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", ENCODE) + "=" + SERVICE_KEY)
                .append("&" + URLEncoder.encode("_type", ENCODE) + "=" + URLEncoder.encode(DATA_TYPE, ENCODE))
                .append("&" + URLEncoder.encode("cityCode", ENCODE) + "=" + URLEncoder.encode(Integer.toString(code), ENCODE)); /* 시/도 ID */
        JSONObject json = connect(urlBuilder)
                .getJSONObject("response")
                .getJSONObject("body")
                .getJSONObject("items");
        JSONArray itemArr = json.getJSONArray("item");
        for (int i = 0; i < itemArr.length(); i++) {
            JSONObject j = itemArr.getJSONObject(i);
            //dtoArr.add(new NodeDTO(j.getString("nodeid"), j.getString("nodename"), code));
        }
        return dtoArr;
    }

    private static JSONObject connect(StringBuilder urlBuilder) throws IOException, JSONException {
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        Log.d("ApiExplorer", "Response code: " + conn.getResponseCode());
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
