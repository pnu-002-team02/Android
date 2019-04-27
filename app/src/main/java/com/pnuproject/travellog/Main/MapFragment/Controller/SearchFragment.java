package com.pnuproject.travellog.Main.MapFragment.Controller;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.pnuproject.travellog.R;

public class SearchFragment extends Fragment {

    private ListViewAdapter adapter;
    
    public SearchFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = (ListView) getView().findViewById(R.id.search_list_public);
        adapter = new ListViewAdapter();

        adapter.addItem("place1", "time1", "weather3");
        adapter.addItem("place2", "time2", "weather3");

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //리스트뷰 아이템 클릭했을 때
                Toast.makeText(getContext(), "아이템 " + i +" 터치", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
