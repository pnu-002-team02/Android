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
import android.widget.Toast;

import com.pnuproject.travellog.Main.MapFragment.Model.MapMarkerRetrofitInterface;
import com.pnuproject.travellog.Main.MapFragment.Model.ResponseDataMarker;
import com.pnuproject.travellog.Main.MapFragment.Model.ResponseDataVisitedList;
import com.pnuproject.travellog.R;
import com.pnuproject.travellog.etc.RetrofitTask;
import com.pnuproject.travellog.etc.TLApp;
import org.json.JSONArray;
import java.net.ConnectException;
import java.net.UnknownHostException;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements RetrofitTask.RetrofitExecutionHandler {

    private WebView mWebView;
    private final static int RETROFIT_TASK_ERROR = 0x00;
    private final static int RETROFIT_TASK_GETITEMNUM = 0x01;
    private final static int RETROFIT_TASK_GETMYITEMNUM = 0x02;


    private RetrofitTask retrofitTask;
    private int allItemNum = 0;
    private int visitNum = 0;

    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public void onResume() {
        super.onResume();
        if (retrofitTask!=null && getUserVisibleHint()) {
            TLApp.UserInfo userinfo = TLApp.getUserInfo();
            if(userinfo != null ) {
                RetrofitTask.RetrofitRequestParam requestParam = new RetrofitTask.RetrofitRequestParam(RETROFIT_TASK_GETITEMNUM, null);
                retrofitTask.execute(requestParam);
            }else {
                allItemNum = 0;
                visitNum = 0;
                reloadData();
            }
        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (retrofitTask!=null && isVisibleToUser) {
            TLApp.UserInfo userinfo = TLApp.getUserInfo();
            if(userinfo != null ) {
                RetrofitTask.RetrofitRequestParam requestParam = new RetrofitTask.RetrofitRequestParam(RETROFIT_TASK_GETITEMNUM, null);
                retrofitTask.execute(requestParam);
            } else {
                allItemNum = 0;
                visitNum = 0;
                reloadData();
            }
        }
    }

    public void reloadData() {
        mWebView.addJavascriptInterface(new PassJsonArrayStr(), "android");
        mWebView.loadUrl("file:///android_asset/home.html");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        retrofitTask = new RetrofitTask(this, getResources().getString(R.string.server_address));
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

        return v;
    }

    class PassJsonArrayStr {
        @JavascriptInterface
        public String passData(){
            int visitPer = (int)(((double)visitNum)/allItemNum * 100);
            /**jsonarray를 string형식으로 넘겨 script에서 다시 jsonarray로 변환*/

             return String.format("[{\"region_name\":\"서울특별시\",\"percentage\":0},"
             +"{\"region_name\":\"부산광역시\",\"percentage\":%d},"
             +"{\"region_name\":\"대구광역시\",\"percentage\":0},"
             +"{\"region_name\":\"인천광역시\",\"percentage\":0},"
             +"{\"region_name\":\"광주광역시\",\"percentage\":0},"
             +"{\"region_name\":\"대전광역시\",\"percentage\":0},"
             +"{\"region_name\":\"울산광역시\",\"percentage\":0},"
             +"{\"region_name\":\"세종특별자치시\",\"percentage\":0},"
             +"{\"region_name\":\"경기도_수원시\",\"percentage\":0},"
             +"{\"region_name\":\"경기도_성남시\",\"percentage\":0},"
             +"{\"region_name\":\"경기도_의정부시\",\"percentage\":0},"
             +"{\"region_name\":\"경기도_안양시\",\"percentage\":0}]",visitPer);
        }
    }



    @Override
    public void onAfterAyncExcute(RetrofitTask.RetrofitResponseParam response2) {
        if (response2 == null || response2.getResponse() == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.errmsg_retrofit_unknown), Toast.LENGTH_SHORT).show();
            return;

        } else if (response2.getTaskNum() == RETROFIT_TASK_ERROR) {
            final String errMsg = (String) response2.getResponse();
            Toast.makeText(getContext(), errMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        int taskNum = response2.getTaskNum();
        Object responseData = response2.getResponse();

        switch (taskNum) {
            case RETROFIT_TASK_GETITEMNUM:
                final ResponseDataMarker res = (ResponseDataMarker) responseData;
                allItemNum = res.getProducts().size();
                RetrofitTask.RetrofitRequestParam requestParam = new RetrofitTask.RetrofitRequestParam(RETROFIT_TASK_GETMYITEMNUM,null);
                retrofitTask.execute(requestParam);
                break;
            case RETROFIT_TASK_GETMYITEMNUM:
                final ResponseDataVisitedList res2 = (ResponseDataVisitedList) responseData;
                visitNum = res2.getVisitlist().size();
                reloadData();
                break;
            default:
                break;
        }
    }

    @Override
    public Object onBeforeAyncExcute(Retrofit retrofit, RetrofitTask.RetrofitRequestParam paramRequest5) {
        Object response = null;
        int taskNum = paramRequest5.getTaskNum();
        MapMarkerRetrofitInterface markerRetrofit = retrofit.create(MapMarkerRetrofitInterface.class);

        try {
            switch (taskNum) {
                case RETROFIT_TASK_GETITEMNUM:
                    response = markerRetrofit.getMarker().execute().body();
                    break;
                case RETROFIT_TASK_GETMYITEMNUM:
                    response = markerRetrofit.getVisitedList(TLApp.getUserInfo().getUserID()).execute().body();
                    break;
                default:
                    break;
            }
        }
        catch (UnknownHostException ex) {
            paramRequest5.setTaskNum(RETROFIT_TASK_ERROR);
            response = new String(getResources().getString(R.string.errmsg_retrofitbefore_ownernetwork));
        }
        catch (ConnectException ex) {
            paramRequest5.setTaskNum(RETROFIT_TASK_ERROR);
            response = new String(getResources().getString(R.string.errmsg_retrofitbefore_servernetwork));
        }
        catch (Exception ex) {
            paramRequest5.setTaskNum(RETROFIT_TASK_ERROR);
            response = new String(getResources().getString(R.string.errmsg_retrofit_unknown));
        }
        return response;
    }
}
