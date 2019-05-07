package com.pnuproject.travellog.Main.MapFragment.Controller.Search;

public class SearchResultListItem {
    //커스텀리스트뷰
    //대중교통 경로 검색 결과
    private String place;
    private String text2;
    private String weather;

    public String getPlace(){
        return place;
    }

    public String getText2(){
        return text2;
    }

    public String getWeather(){
        return weather;
    }

    public void setPlace(String t){
        this.place = t;
    }

    public void setText2(String t){
        this.text2 = t;
    }

    public void setWeather(String t){
        this.weather = t;
    }
}
