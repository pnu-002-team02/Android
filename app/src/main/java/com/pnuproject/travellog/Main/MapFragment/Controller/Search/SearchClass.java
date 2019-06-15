package com.pnuproject.travellog.Main.MapFragment.Controller.Search;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.odsay.odsayandroidsdk.API;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;
import com.pnuproject.travellog.Main.MapFragment.Controller.MapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;

public class SearchClass {
    private String user;
    private String od_key =  "ckC8bK7AFt2P9CPFHNGWZpglYArGuTGpYaa/RtLLYPI";
    private String rest_key = "cf326dcc411729ebc208b22c43b83e0c";

    private ArrayList<String[]> place;
    private ArrayList<TransPath> path;
    private String s;
    private int arrsize;
    private boolean isnormal,f=false;
    private String json;

    private ODsayService odsay;

    public SearchClass() {
    }

    public void init(){
        s = null;
        arrsize = 0;
        isnormal = false;
    }
    /*
     * 장소 검색
     * 카카오 REST API 사용
     * 검색 버튼 클릭시 동작
     * MapFragment
     */
    public void findPlace(String u) {
        init();
        user = u;
        final StringBuilder sb = new StringBuilder();

        Thread thread = new Thread(){
            String urlhttp = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + user
                    + "&apikey=" + rest_key;
            @Override
            public void run() {
                String data;
                try {
                    URL url = new URL(urlhttp);
                    HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
                    httpconn.setDefaultUseCaches(false);
                    httpconn.setRequestMethod("GET");
                    httpconn.setRequestProperty("Authorization", "KakaoAK " + rest_key);

                    BufferedReader br = new BufferedReader(new InputStreamReader(httpconn.getInputStream(), "UTF-8"));

                    while ((data = br.readLine()) != null) {
                        sb.append(data);
                    }
                    br.close();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Log.i(TAG, "결과 : " + sb.toString());
                s = sb.toString();
            }
        };
        thread.start();

        while(true){
            if(thread.getState() == Thread.State.TERMINATED){
                /*System.out.println("쓰레드 종료");
                System.out.println("s: " + s);*/
                parsePlace(s);
                break;
            }
        }
    }

    public void parsePlace(String s){
        String name = null;
        String add1 = null;
        String add2 = null;
        String x = null;
        String y = null;
        place = new ArrayList<String[]>();
        place.clear();

        try {
            JSONArray jarray = new JSONObject(s).getJSONArray("documents");

            for (int i = 0; i < 5; i++) {
                JSONObject jobject = jarray.getJSONObject(i);
                String[] finalResult = new String[5];

                name = jobject.getString("place_name");
                add1 = jobject.getString("address_name");
                add2 = jobject.getString("road_address_name");
                x = jobject.getString("x");
                y = jobject.getString("y");

                finalResult[0] = name;
                finalResult[1] = add1;
                finalResult[2] = add2;
                finalResult[3] = x;
                finalResult[4] = y;

                place.add(finalResult);
                arrsize = place.size();
            }
            isnormal = true;
        } catch (JSONException e) {
            isnormal = false;
            e.printStackTrace();
        }
    }

    public boolean getIsnormal(){
        return isnormal;
    }

    public ArrayList<String[]> getPlaceResult(){
        return place;
    }

    public int getSize(){
        return arrsize;
    }

    /*
     * 대중교통 길찾기
     * ODsay API 사용
     * 길찾기 버튼 클릭시 동작
     * SearchDialog
     */

    public boolean getf(){
        return f;
    }

    public void findPath(String[] s, Context context){
        final String x1 = s[0];
        final String y1 = s[1];
        final String x2 = s[2];
        final String y2 = s[3];

        path = new ArrayList<TransPath>();
        path.clear();
        f = false;

        initODsay(context);

        final OnResultCallbackListener callbackListener = new OnResultCallbackListener() {
            @Override
            public void onSuccess(ODsayData oDsayData, API api) {
                if(api == API.SEARCH_PUB_TRANS_PATH) {
                    json = oDsayData.getJson().toString();
                    parsePath(json);
                }
            }

            @Override
            public void onError(int i, String s, API api) {
                if(api == API.SEARCH_PUB_TRANS_PATH){
                    return;
                }
            }
        };

        odsay.requestSearchPubTransPath(x1, y1, x2, y2, "0","0","0", callbackListener);
    }

    public void parsePath(String s){
        try {
            JSONObject jobject = new JSONObject(s).getJSONObject("result");
            JSONArray jarray = jobject.getJSONArray("path");
            int size1 = jarray.length();

            for(int i = 0; i < size1; i++){
                JSONObject jo = jarray.getJSONObject(i);

                //pathType 1 : 지하철 / 2 : 버스 / 3 : 버스+지하철
                int type = jo.getInt("pathType");

                JSONObject info = jo.getJSONObject("info");
                int time = info.getInt("totalTime");

                JSONArray subpath = jo.getJSONArray("subPath");
                int sublength = subpath.length();

                String pathName = "", traffic = "";

                for(int j = 0; j < sublength; j++){
                    JSONObject path = subpath.getJSONObject(j);
                    //trafficTyep 1 : 지하철 / 2 : 버스 /  3 : 도보
                    int traffictype = path.getInt("trafficType");
                    if(traffictype == 3){
                    }
                    else {
                        JSONObject lane = path.getJSONArray("lane").getJSONObject(0);
                        if (traffictype == 1) {
                            String name = lane.getString("name");
                            traffic = traffic + "(지하철) " + name + " - ";
                        } else if (traffictype == 2) {
                            String busNo = lane.getString("busNo");
                            traffic = traffic + "(버스) " + busNo + "번 - ";
                        }

                        String startName = path.getString("startName");
                        String endName = path.getString("endName");
                        pathName = pathName + startName + " → " + endName + " ▶ ";
                    }
                }

                int lengt = traffic.length();
                int lengp = pathName.length();
                if(lengt != 0){
                    traffic = traffic.substring(0, lengt-3);
                }
                if(lengp != 0){
                    pathName = pathName.substring(0, lengp-3);
                }

                TransPath tp = new TransPath(pathName, traffic, time);
                path.add(tp);
            }
            f=true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initODsay(Context context){
        odsay = ODsayService.init(context, od_key);
        odsay.setReadTimeout(5000);
        odsay.setConnectionTimeout(5000);
    }

    public ArrayList<TransPath> getPathResult(){
        return path;
    }

    public void printPathTest(){
        System.out.println("경로 프린트");
        for(int i = 0; i < path.size(); i++){
            System.out.println(path.get(i).getPath());
        }
    }

}
