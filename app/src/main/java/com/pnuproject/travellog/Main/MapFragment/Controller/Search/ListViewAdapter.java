package com.pnuproject.travellog.Main.MapFragment.Controller.Search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pnuproject.travellog.R;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter{

    public ArrayList<SearchResultListItem> searchResultListItemsList = new ArrayList<SearchResultListItem>();

    @Override
    public int getCount() {
        return searchResultListItemsList.size();
    }

    @Override
    public Object getItem(int i) {
        return searchResultListItemsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int position = i;
        final Context context = viewGroup.getContext();

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.search_result_listitem, viewGroup, false);
        }

        TextView place = (TextView) view.findViewById(R.id.place);
        TextView text2 = (TextView) view.findViewById(R.id.text2);
        TextView weather = (TextView) view.findViewById(R.id.weather);

        SearchResultListItem searchResultListItem = searchResultListItemsList.get(position);

        place.setText(searchResultListItem.getPlace());
        text2.setText(searchResultListItem.getText2());
        weather.setText(searchResultListItem.getWeather());

        return view;
    }

    public void addItem(String t1, String t2, String t3){
        SearchResultListItem item = new SearchResultListItem();
        item.setPlace(t1);
        item.setText2(t2);
        item.setWeather(t3);

        searchResultListItemsList.add(item);
    }

    public String getItemPlace(int i){
        SearchResultListItem item = searchResultListItemsList.get(i);
        return item.getPlace();
    }

    public String[] getItemInfo(int i){
        String[] info = new String[3];

        info[0] = searchResultListItemsList.get(i).getPlace();
        info[1] = searchResultListItemsList.get(i).getText2();
        info[2] = searchResultListItemsList.get(i).getWeather();

        return info;
    }

    public void clearItem(){
        searchResultListItemsList.clear();
    }
}
