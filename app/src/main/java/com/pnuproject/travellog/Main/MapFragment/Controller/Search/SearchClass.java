package com.pnuproject.travellog.Main.MapFragment.Controller.Search;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.odsay.odsayandroidsdk.ODsayService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;

public class SearchClass {
    //장소 검색 - 카카오 REST API 로컬 검색 이용
    //길찾기 검색 - 오디세이 API 이용

    String place_user;
    String od_key =  "ckC8bK7AFt2P9CPFHNGWZpglYArGuTGpYaa/RtLLYPI";
    String rest_key = "cf326dcc411729ebc208b22c43b83e0c";

    public SearchClass() {
    }

    /*
      검색 버튼 클릭시 동작
      MapFragment
     */
    public String[] findPlace(String user){
        place_user = user;
        final StringBuilder sb = new StringBuilder();
        final String[] finalResult = new String[5];

        Thread thread = new Thread(){
            String urlhttp = "https://dapi.kakao.com/v2/local/search/keyword.json?query="+place_user
                    +"&apikey="+rest_key;

            public void run(){
                String data;
                try {
                    URL url = new URL(urlhttp);
                    HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
                    httpconn.setDefaultUseCaches(false);
                    httpconn.setRequestMethod("GET");
                    httpconn.setRequestProperty("Authorization", "KakaoAK "+rest_key);

                    BufferedReader br = new BufferedReader(new InputStreamReader(httpconn.getInputStream(), "UTF-8"));

                    while((data = br.readLine()) != null){
                        sb.append(data);
                    }
                    br.close();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String name = null;
                String add1 = null;
                String add2 = null;
                String x = null;
                String y = null;

                try {
                    JSONArray jarray = new JSONObject(sb.toString()).getJSONArray("documents");

                    for(int i = 0; i < jarray.length(); i++){
                        HashMap hashMap = new HashMap<>();
                        JSONObject jobject = jarray.getJSONObject(i);

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
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "결과 : " + sb.toString());

                for(int i = 0; i < 5; i++){
                    Log.i(TAG, "파싱 " + i + " : " + finalResult[i]);
                }

            }
        };
        thread.start();

        return finalResult;
    }
}
