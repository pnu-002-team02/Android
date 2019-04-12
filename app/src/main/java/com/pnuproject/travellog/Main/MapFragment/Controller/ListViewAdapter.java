package com.pnuproject.travellog.Main.MapFragment.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
        TextView time = (TextView) view.findViewById(R.id.time);
        TextView weather = (TextView) view.findViewById(R.id.weather);

        SearchResultListItem searchResultListItem = searchResultListItemsList.get(position);

        place.setText(searchResultListItem.getPlace());
        time.setText(searchResultListItem.getTime());
        weather.getText(searchResultListItem.getWeather());

        return view;
    }

    public void addItem(String t1, String t2, String t3){
        SearchResultListItem item = new SearchResultListItem();
        item.setPlace(t1);
        item.setTime(t2);
        item.setWeather(t3);

        searchResultListItemsList.add(item);
    }

    public void clearItem(){
        searchResultListItemsList.clear();
    }
}
