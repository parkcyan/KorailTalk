package com.example.korailtalk.data;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

public class NodeDAO {

    Context context;

    public NodeDAO(Context context) {
        this.context = context;
    }

    public void getNode() {
        try {
            for (int city : KtData.citycode) {
                Reader reader = new InputStreamReader(context.getResources().getAssets().open(city + ".json"));
                KtData.nodeArr = new Gson().fromJson(reader, new TypeToken<ArrayList<NodeDTO>>() {
                }.getType());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
