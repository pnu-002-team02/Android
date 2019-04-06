package com.pnuproject.travellog.Main.MapFragment.Controller;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pnuproject.travellog.R;

public class MapFragment extends Fragment {



    public MapFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        Button button =(Button) view.findViewById(R.id.firstButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               show();
            }
        });
        return view;
    }
    void show(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("인증여부 확인");
        builder.setMessage("인증하시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(),"예를 선택했습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),Dialog.class);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }



}
