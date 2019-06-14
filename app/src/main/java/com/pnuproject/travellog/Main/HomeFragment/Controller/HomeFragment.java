package com.pnuproject.travellog.Main.HomeFragment.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pnuproject.travellog.R;

import org.json.JSONArray;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public WebView mWebView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mWebView = (WebView) v.findViewById(R.id.webview);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        mWebView.zoomBy((float)1.3);

        mWebView.setWebViewClient(new WebViewClient());

        mWebView.addJavascriptInterface(new passJsonArrayStr(), "android");
        mWebView.loadUrl("file:///android_asset/home.html");

        return v;
    }

    class passJsonArrayStr {
        @JavascriptInterface
        public String passData(){
            JSONArray jsonArray = new JSONArray();
            String strJA = jsonArray.toString();

            /**jsonarray를 string형식으로 넘겨 script에서 다시 jsonarray로 변환
             String strJA ="[{\"region_name\":\"서울특별시\",\"percentage\":0.2},"
             +"{\"region_name\":\"부산광역시\",\"percentage\":0.4},"
             +"{\"region_name\":\"대구광역시\",\"percentage\":0.6},"
             +"{\"region_name\":\"인천광역시\",\"percentage\":0.8},"
             +"{\"region_name\":\"광주광역시\",\"percentage\":1},"
             +"{\"region_name\":\"대전광역시\",\"percentage\":0},"
             +"{\"region_name\":\"울산광역시\",\"percentage\":0.5},"
             +"{\"region_name\":\"세종특별자치시\",\"percentage\":0.1},"
             +"{\"region_name\":\"경기도_수원시\",\"percentage\":0.3},"
             +"{\"region_name\":\"경기도_성남시\",\"percentage\":0.9},"
             +"{\"region_name\":\"경기도_의정부시\",\"percentage\":0.9},"
             +"{\"region_name\":\"경기도_안양시\",\"percentage\":0.9}]";
             */

            return strJA;
        }
    }
}
