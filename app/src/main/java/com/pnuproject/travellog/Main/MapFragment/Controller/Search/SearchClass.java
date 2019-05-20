package com.pnuproject.travellog.Main.MapFragment.Controller.Search;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.odsay.odsayandroidsdk.ODsayService;
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
    //장소 검색 - 카카오 REST API 로컬 검색 이용
    //길찾기 검색 - 오디세이 API 이용

    String place_user;
    String od_key =  "ckC8bK7AFt2P9CPFHNGWZpglYArGuTGpYaa/RtLLYPI";
    String rest_key = "cf326dcc411729ebc208b22c43b83e0c";

    ArrayList<String[]> tmp;
    String s;
    int arrsize;
    boolean isnormal;

    public SearchClass() {
    }

    public void init(){
        s = null;
        arrsize = 0;
        isnormal = false;
    }
    /*
      검색 버튼 클릭시 동작
      MapFragment
     */
    public void findPlace(String user) {
        init();
        place_user = user;
        final StringBuilder sb = new StringBuilder();

        Thread thread = new Thread(){
            String urlhttp = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + place_user
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
                parse(s);
                break;
            }
        }
    }

    public void parse(String s){
        String name = null;
        String add1 = null;
        String add2 = null;
        String x = null;
        String y = null;
        tmp = new ArrayList<String[]>();
        tmp.clear();

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

                tmp.add(finalResult);
                arrsize = tmp.size();
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

    public ArrayList<String[]> getResult(){
        //prints();
        return tmp;
    }

    /*
    public void prints(){
        for(int  i = 0; i < arrsize; i++){
            System.out.println(tmp.get(i)[0] + " " + tmp.get(i)[1]);
        }
    }
    */

    public int getSize(){
        return arrsize;
    }
}
