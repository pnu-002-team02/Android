package com.pnuproject.travellog.Main.MypageFragment.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pnuproject.travellog.Login.Controller.LoginActivity;
import com.pnuproject.travellog.Main.MapFragment.Model.MapMarkerRetrofitInterface;
import com.pnuproject.travellog.Main.MapFragment.Model.ResponseDataMarker;
import com.pnuproject.travellog.Main.MapFragment.Model.ResponseDataVisitedList;
import com.pnuproject.travellog.R;
import com.pnuproject.travellog.etc.RetrofitTask;
import com.pnuproject.travellog.etc.TLApp;

import java.net.ConnectException;
import java.net.UnknownHostException;

import retrofit2.Retrofit;

import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;

public class MypageFragment extends Fragment implements View.OnClickListener, RetrofitTask.RetrofitExecutionHandler {
    private TextView tvBtnSignup;
    private TextView tvBtn_modifyUserInfo;
    private TextView tvName,tvVisitPer,tvVisitNum,tvHealingDay;
    private TextView tvBtn_logout;
    private LinearLayout layout_userinfo;
    private RetrofitTask retrofitTask;

    final static private int RETROFIT_TASK_ERROR = 0x00;
    final static private int RETROFIT_TASK_GETITEMNUM = 0x01;
    final static private int RETROFIT_TASK_GETMYITEMNUM = 0x02;

    private int allItemNum = 0;
    private int visitNum = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_mypage, container, false);
        retrofitTask = new RetrofitTask(this, getResources().getString(R.string.server_address));

        tvName = view.findViewById(R.id.tvName_mypage);
        tvBtnSignup = view.findViewById(R.id.tvLogin_mypage);
        tvBtnSignup.setOnClickListener(this);
        layout_userinfo = view.findViewById(R.id.layout_userinfo);
        tvBtn_modifyUserInfo = view.findViewById(R.id.tvBtn_modifyUserInfo_mypage);
        tvBtn_logout = view.findViewById(R.id.tvBtn_logout_mypage);
        tvVisitNum = view.findViewById(R.id.tvVisitNum_mypage);
        tvVisitPer = view.findViewById(R.id.tvVisitPer_mypage);
        tvHealingDay = view.findViewById(R.id.tvHealingDay_mypage);
        tvBtn_logout.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvLogin_mypage:
                final Intent intent = new Intent(this.getActivity(), LoginActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                break;
            case R.id.tvBtn_logout_mypage:
                TLApp.setUserInfo(null);
                refreshMenu();
                Toast.makeText(getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            refreshMenu();
        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            refreshMenu();
        }
    }
    public void refreshMenu(){
        TLApp.UserInfo userinfo = TLApp.getUserInfo();
        if(userinfo != null ) {
            //로그인 된경우
            layout_userinfo.setVisibility(View.VISIBLE);
            tvBtnSignup.setVisibility(View.GONE);
            tvName.setText(userinfo.getName());
            tvBtn_modifyUserInfo.setVisibility(View.VISIBLE);
            tvBtn_logout.setVisibility(View.VISIBLE);
            RetrofitTask.RetrofitRequestParam requestParam = new RetrofitTask.RetrofitRequestParam(RETROFIT_TASK_GETITEMNUM, null);
            retrofitTask.execute(requestParam);
        } else {
            layout_userinfo.setVisibility(View.GONE);
            tvBtnSignup.setVisibility(View.VISIBLE);
            tvBtn_modifyUserInfo.setVisibility(View.GONE);
            tvBtn_logout.setVisibility(View.GONE);
            allItemNum=0;
            visitNum = 0;
        }
    }

    @Override
    public void onAfterAyncExcute(RetrofitTask.RetrofitResponseParam response) {
        if (response == null || response.getResponse() == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.errmsg_retrofit_unknown), Toast.LENGTH_SHORT).show();
            return;

        } else if (response.getTaskNum() == RETROFIT_TASK_ERROR) {
            final String errMsg = (String) response.getResponse();
            Toast.makeText(getContext(), errMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        int taskNum = response.getTaskNum();
        Object responseData = response.getResponse();

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
                    int visitPer = (int)(((double)visitNum)/allItemNum * 100);
                    tvVisitNum.setText(String.format("%d개",visitNum));
                    tvVisitPer.setText(String.format("%d%%",visitPer));
                    tvHealingDay.setText("0 일째");


                    break;
        }
    }

    @Override
    public Object onBeforeAyncExcute(Retrofit retrofit, RetrofitTask.RetrofitRequestParam paramRequest1) {
        Object response = null;
        int taskNum = paramRequest1.getTaskNum();
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
            paramRequest1.setTaskNum(RETROFIT_TASK_ERROR);
            response = new String(getResources().getString(R.string.errmsg_retrofitbefore_ownernetwork));
        }
        catch (ConnectException ex) {
            paramRequest1.setTaskNum(RETROFIT_TASK_ERROR);
            response = new String(getResources().getString(R.string.errmsg_retrofitbefore_servernetwork));
        }
        catch (Exception ex) {
            paramRequest1.setTaskNum(RETROFIT_TASK_ERROR);
            response = new String(getResources().getString(R.string.errmsg_retrofit_unknown));
        }
        return response;
    }
}
