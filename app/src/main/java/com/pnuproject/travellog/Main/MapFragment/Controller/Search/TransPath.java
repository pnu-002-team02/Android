package com.pnuproject.travellog.Main.MapFragment.Controller.Search;

public class TransPath {
    private String path;
    private String traffic;
    private String time;

    TransPath(String path, String traffic, int time){
        this.path = path;
        this.traffic = traffic;
        this.time = transtime(time);
    }

    public String getPath(){
        return path;
    }

    public String getTraffic(){
        return traffic;
    }

    public String getTime(){
        return time;
    }

    public String transtime(int t){
        String result = "";
        if(t >= 60){
            int h = t / 60;
            int m = t % 60;
            result = h + "시간 " + m + "분";
        }
        else{
            result = t + "분";
        }

        return result;
    }
}
