package com.example.korailtalk.sqlite;

public class CityDTO {

    int citycode;
    String cityname;

    public CityDTO(int citycode, String cityname) {
        this.citycode = citycode;
        this.cityname = cityname;
    }

    public int getCitycode() {
        return citycode;
    }

    public void setCitycode(int citycode) {
        this.citycode = citycode;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

}
