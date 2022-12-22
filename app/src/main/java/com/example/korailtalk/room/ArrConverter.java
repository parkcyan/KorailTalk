package com.example.korailtalk.room;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

public class ArrConverter {
    @TypeConverter
    public String arrToJson(int[] arr) {
        return new Gson().toJson(arr);
    }
    @TypeConverter
    public int[] jsonToArr(String json) {
        return new Gson().fromJson(json, int[].class);
    }
}
