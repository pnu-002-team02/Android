package com.pnuproject.travellog.Main.MapFragment.Controller;

public class SearchResultListItem {
    //커스텀리스트뷰
    //대중교통 경로 검색 결과
    private String place;
    private String time;
    private String weather;

    public String getPlace(){
        return place;
    }

    public String getTime(){
        return time;
    }

    public String getWeather(){
        return weather;
    }

    public void setPlace(String t){
        this.place = t;
    }

    public void setTime(String t){
        this.time = t;
    }

    public void setWeather(String t){
        this.weather = t;
    }
}
