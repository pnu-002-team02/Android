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
    TextView place, address;
    Button btn_find, btn_close;
    private Fragment fragment;

    private dialogListener dl;

    public interface dialogListener{
        public void callBack(int toSearch);
    }

    public void setDL(dialogListener dl){
        this.dl = dl;
    }

    public SearchDialog(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        place = (TextView) getView().findViewById(R.id.place);
        address = (TextView) getView().findViewById(R.id.address);
        btn_find = (Button) getView().findViewById(R.id.btn_find);
        btn_close = (Button) getView().findViewById(R.id.btn_close);

        Bundle arguments = getArguments();
        final String[] info = arguments.getStringArray("search");

        place.setText(info[0]);
        address.setText(info[1]);

        fragment = getActivity().getSupportFragmentManager().findFragmentByTag("tag");

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragment != null){
                    Toast.makeText(getContext(), "닫기", Toast.LENGTH_SHORT).show();
                    dl.callBack(0);
                    DialogFragment dialogFragment = (DialogFragment) fragment;
                    dialogFragment.dismiss();
                }
            }
        });

        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //길찾기 시작
                if(fragment != null){
                    dl.callBack(1);
                    DialogFragment dialogFragment = (DialogFragment) fragment;
                    dialogFragment.dismiss();
                }
            }
        });
    }
}