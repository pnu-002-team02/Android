package com.pnuproject.travellog.Main.MypageFragment.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pnuproject.travellog.Login.Controller.LoginActivity;
import com.pnuproject.travellog.R;

public class MypageFragment extends Fragment implements View.OnClickListener {
    private TextView tvBtnSignup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_mypage, container, false);

        tvBtnSignup = view.findViewById(R.id.tvLogin_mypage);
        tvBtnSignup.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvLogin_mypage:
                final Intent intent = new Intent(this.getActivity(), LoginActivity.class);
                startActivity(intent);
                break;

        }
    }
}
