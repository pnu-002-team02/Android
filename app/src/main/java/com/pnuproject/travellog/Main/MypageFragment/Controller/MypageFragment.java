package com.pnuproject.travellog.Main.MypageFragment.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pnuproject.travellog.Login.Controller.LoginActivity;
import com.pnuproject.travellog.R;
import com.pnuproject.travellog.etc.TLApp;

import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;

public class MypageFragment extends Fragment implements View.OnClickListener {
    private TextView tvBtnSignup;
    private TextView tvBtn_modifyUserInfo;
    private TextView tvName;
    private TextView tvBtn_logout;
    private LinearLayout layout_userinfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_mypage, container, false);

        tvName = view.findViewById(R.id.tvName_mypage);
        tvBtnSignup = view.findViewById(R.id.tvLogin_mypage);
        tvBtnSignup.setOnClickListener(this);
        layout_userinfo = view.findViewById(R.id.layout_userinfo);
        tvBtn_modifyUserInfo = view.findViewById(R.id.tvBtn_modifyUserInfo_mypage);
        tvBtn_logout = view.findViewById(R.id.tvBtn_logout_mypage);

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
            //로그인된경우
            layout_userinfo.setVisibility(View.VISIBLE);
            tvBtnSignup.setVisibility(View.GONE);
            tvName.setText(userinfo.getName());
            tvBtn_modifyUserInfo.setVisibility(View.VISIBLE);
            tvBtn_logout.setVisibility(View.VISIBLE);
        } else {
            layout_userinfo.setVisibility(View.GONE);
            tvBtnSignup.setVisibility(View.VISIBLE);
            tvBtn_modifyUserInfo.setVisibility(View.GONE);
            tvBtn_logout.setVisibility(View.GONE);
        }
    }
}
