package com.pnuproject.travellog.Main.MapFragment.Controller.Search;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.odsay.odsayandroidsdk.ODsayService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;

public class SearchClass {
    //카카오 REST API 로컬 검색을 이용

    String place_user;
    String od_key =  "ckC8bK7AFt2P9CPFHNGWZpglYArGuTGpYaa/RtLLYPI";
    String rest_key = "cf326dcc411729ebc208b22c43b83e0c";

    public SearchClass() {
    }

    /*
      검색 버튼 클릭시 동작
      MapFragment
     */
    public void findPlace(String user){
        place_user = user;

        Thread thread = new Thread(){
            String urlhttp = "https://dapi.kakao.com/v2/local/search/keyword.json?query="+place_user
                    +"&apikey="+rest_key;
            StringBuilder result = new StringBuilder();

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
                        result.append(data);
                    }
                    br.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.i(TAG, "결과 : " + result.toString());
                //return result.toString();
            }
        };
        thread.start();
    }

    /*
      길찾기 버튼 클릭시 동작
      SearchDialog

    public void findPath(String s1, String s2){
        place_user = s1;
        place_search = s2;
        ODsayService oDsayService = ODsayService.init(context, od_key);
    }
    */
}
