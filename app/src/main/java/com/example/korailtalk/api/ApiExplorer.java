package com.example.korailtalk.api;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ApiExplorer {

    private static ApiExplorer instance;
    private final String SERVICE_KEY = "bV8Qa13gBAIQTlWOVkdceGorgqXDUFoPycZ71CS/Ez5b7w/kN1w89tV2zMBWwU4FZfaCqjC15LXqszMNwumAzw==";
    private final String ENCODE = "UTF-8";
    private final String DATA_TYPE = "json";

    private ApiExplorer() {}

    public static ApiExplorer getInstance() {
        if (instance == null) instance = new ApiExplorer();
        return instance;
    }

    public JSONArray getNode(int city) throws IOException, JSONException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1613000/TrainInfoService/getCtyAcctoTrainSttnList");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", ENCODE) + "=" + SERVICE_KEY)
                .append("&" + URLEncoder.encode("numOfRows",ENCODE) + "=" + URLEncoder.encode("100", ENCODE))
                .append("&" + URLEncoder.encode("_type", ENCODE) + "=" + URLEncoder.encode(DATA_TYPE, ENCODE))
                .append("&" + URLEncoder.encode("cityCode", ENCODE) + "=" + URLEncoder.encode(Integer.toString(city), ENCODE)); /* 시/도 ID */
        JSONObject json = connect(urlBuilder)
                .getJSONObject("response")
                .getJSONObject("body")
                .getJSONObject("items");
        return json.getJSONArray("item");
    }

    private JSONObject connect(StringBuilder urlBuilder) throws IOException, JSONException {
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
