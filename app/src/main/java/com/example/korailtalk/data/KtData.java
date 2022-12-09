package com.example.korailtalk.data;

import com.example.korailtalk.api.ApiExplorer;
import com.example.korailtalk.sqlite.CityDTO;

import java.util.ArrayList;

public class KtData {

    public static ApiExplorer api = new ApiExplorer();
    public static ArrayList<CityDTO> cityArr = new ArrayList<>();
    public static ArrayList<NodeDTO> nodeArr;
    public static final int[] citycode = {11, 12, 21, 22, 23, 24, 25, 26, 31, 32, 33, 34, 35, 36, 37, 38};

    private KtData() {}

}
