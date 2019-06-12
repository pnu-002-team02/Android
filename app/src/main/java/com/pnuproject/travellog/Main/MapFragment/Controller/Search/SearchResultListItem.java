package com.pnuproject.travellog.Main.MapFragment.Controller.Search;

public class SearchResultListItem {
    //커스텀리스트뷰
    //대중교통 경로 검색 결과
    private String place;   //장소명, 경로
    private String text2;   //상세주소
    private String time; //소요 시간
    private String x;
    private String y;

    public String getPlace(){
        return place;
    }

    public String getText2(){
        return text2;
    }

    public String getTime(){
        return time;
    }

    public String getX(){
        return x;
    }

    public String getY(){
        return y;
    }

    public void setPlace(String t){
        this.place = t;
    }

    public void setText2(String t){
        this.text2 = t;
    }

    public void setTime(String t){
        this.time = t;
    }

    public void setX(String t){
        this.x = t;
    }

    public void setY(String t){
        this.y = t;
    }
}
