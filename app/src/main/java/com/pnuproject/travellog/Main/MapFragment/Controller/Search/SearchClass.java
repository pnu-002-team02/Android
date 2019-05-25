package com.pnuproject.travellog.Main.MapFragment.Controller.Search;

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

    private ArrayList<String[]> place, path;
    private String s;
    private int arrsize;
    private boolean isnormal;

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
                Log.i(TAG, "결과 : " + sb.toString());
                s = sb.toString();
            }
        };
        thread.start();

        while(true){
            if(thread.getState() == Thread.State.TERMINATED){
                System.out.println("쓰레드 종료");
                System.out.println("s: " + s);
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
    public void findPath(String s[], Context context){
        String x1 = s[0];
        String y1 = s[1];
        String x2 = s[2];
        String y2 = s[3];

        initODsay(context);

        OnResultCallbackListener callbackListener = new OnResultCallbackListener() {
            @Override
            public void onSuccess(ODsayData oDsayData, API api) {
                if(api == API.SEARCH_PUB_TRANS_PATH) {
                    Log.i(TAG, "길찾기 api 호출 성공");
                    String json = oDsayData.getJson().toString();
                    //System.out.println("json : " + json);
                    parsePath(json);
                }
            }

            @Override
            public void onError(int i, String s, API api) {
                if(api == API.SEARCH_PUB_TRANS_PATH){
                    Log.e(TAG, "길찾기 api 호출 실패");
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
                //System.out.println("테스트 : " + jo.toString());

                //pathType 1 : 지하철 / 2 : 버스 / 3 : 버스+지하철
                String type = jo.getString("pathType");

                JSONObject info = jo.getJSONObject("info");
                String time = info.getString("totalTime");

                System.out.println("교통수단 : " + type + " / 소요 시간 : " + time + "분");

                JSONArray subpath = jo.getJSONArray("subPath");
                JSONObject jb = subpath.getJSONObject(1);

                /*
                JSONArray lane = jb.getJSONArray("lane");

                String name = "", busNo = "";
                if(type == "1"){
                    name = lane.getJSONObject(0).getString("name");
                }
                else if(type == "2"){
                    busNo = lane.getJSONObject(0).getString("busNo");
                }
                else if(type == "3"){
                    name = lane.getJSONObject(0).getString("name");
                    busNo = lane.getJSONObject(0).getString("busNo");
                }

                System.out.println("pathType : " + type + " / 시간 : " + time + " / 지하철 : " + name + " / 버스 : " + busNo);
                */

                String stoplist = jb.getString("passStopList");
                JSONArray stations = new JSONObject(stoplist).getJSONArray("stations");

                int size2 = stations.length();
                for(int j = 0; j < size2; j++){
                    System.out.println("경로 " + i + "의 "+j+"번째 : " + stations.getJSONObject(j));
                }
                System.out.println("\n");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initODsay(Context context){
        odsay = ODsayService.init(context, od_key);
        odsay.setReadTimeout(5000);
        odsay.setConnectionTimeout(5000);
    }

    public ArrayList<String[]> getPathResult(){
        return path;
    }
}
