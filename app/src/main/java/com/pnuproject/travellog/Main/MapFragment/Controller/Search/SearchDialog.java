package com.pnuproject.travellog.Main.MapFragment.Controller.Search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pnuproject.travellog.R;

public class SearchDialog extends DialogFragment {
    TextView place, address, weather;
    Button btn_find, btn_close;
    SearchClass searchClass;

    private Fragment fragment;

    public SearchDialog(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String xfrom, yfrom, xto, yto;

        place = (TextView) getView().findViewById(R.id.place);
        address = (TextView) getView().findViewById(R.id.address);
        weather = (TextView) getView().findViewById(R.id.weather);
        btn_find = (Button) getView().findViewById(R.id.btn_find);
        btn_close = (Button) getView().findViewById(R.id.btn_close);

        Bundle arguments = getArguments();
        final String[] user = arguments.getStringArray("user");
        final String[] info = arguments.getStringArray("search");

        System.out.println("user : " + user[0] + " " + user[1]);
        System.out.println("search : " + info[0] + " " + info[1] + " " + info[2] + " " + info[3] + " " + info[4]);

        place.setText(info[0]);
        address.setText(info[1]);
        weather.setText(info[2]);

        fragment = getActivity().getSupportFragmentManager().findFragmentByTag("tag");

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragment != null){
                    Toast.makeText(getContext(), "닫기", Toast.LENGTH_SHORT).show();
                    DialogFragment dialogFragment = (DialogFragment) fragment;
                    dialogFragment.dismiss();
                }
            }
        });

        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] p = new String[4];
                p[0] = user[0];
                p[1] = user[1];
                p[2] = info[3];
                p[3] = info[4];
                //길찾기 시작
                if(fragment != null){
                    Toast.makeText(getContext(), "길찾기를 시작합니다", Toast.LENGTH_SHORT).show();
                    DialogFragment dialogFragment = (DialogFragment) fragment;
                    SearchClass searchClass = new SearchClass();
                    searchClass.findPath(p, getContext());
                    dialogFragment.dismiss();
                }
            }
        });
    }
}
