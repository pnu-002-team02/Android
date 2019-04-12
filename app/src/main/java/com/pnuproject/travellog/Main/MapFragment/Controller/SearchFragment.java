package com.pnuproject.travellog.Main.MapFragment.Controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

        ListView listView = (ListView) view.findViewById(R.id.search_list);
        adapter = new ListViewAdapter();

        adapter.addItem("place1", "time1", "weather3");
        adapter.addItem("place2", "time2", "weather3");

        listView.setAdapter(adapter);
    }
}
