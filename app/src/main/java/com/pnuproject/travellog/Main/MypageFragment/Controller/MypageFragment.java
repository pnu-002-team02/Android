package com.pnuproject.travellog.Main.MypageFragment.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pnuproject.travellog.R;
import com.pnuproject.travellog.Signup.Controller.SignupActivity;

public class MypageFragment extends Fragment implements View.OnClickListener {
    private TextView tvBtnSignup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_mypage, container, false);

        tvBtnSignup = view.findViewById(R.id.tvSingup_mypage);
        tvBtnSignup.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSingup_mypage:
                final Intent intent = new Intent(this.getActivity(), SignupActivity.class);
                startActivity(intent);
                break;

        }
    }
}
