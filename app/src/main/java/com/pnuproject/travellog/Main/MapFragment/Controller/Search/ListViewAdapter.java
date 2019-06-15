package com.pnuproject.travellog.Main.MapFragment.Controller.Search;

import android.content.Context;
import android.util.Log;
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

        TextView text1 = (TextView) view.findViewById(R.id.text1);
        TextView text2 = (TextView) view.findViewById(R.id.text2);
        TextView time = (TextView) view.findViewById(R.id.time);

        SearchResultListItem searchResultListItem = searchResultListItemsList.get(position);

        text1.setText(searchResultListItem.getPlace());
        text2.setText(searchResultListItem.getText2());
        time.setText(searchResultListItem.getTime());

        Log.e("LIST", searchResultListItem.getText2());
        return view;
    }

    public void addItem(String t1, String t2, String t3, String t4, String t5){
        SearchResultListItem item = new SearchResultListItem();
        item.setPlace(t1);
        item.setText2(t2);
        item.setTime(t3);
        item.setX(t4);
        item.setY(t5);

        searchResultListItemsList.add(item);
    }

    public void addItem(String t1, String t2, String t3){
        SearchResultListItem item = new SearchResultListItem();
        item.setPlace(t1);
        item.setText2(t2);
        item.setTime(t3);

        searchResultListItemsList.add(item);
    }

    public String getItemPlace(int i){
        SearchResultListItem item = searchResultListItemsList.get(i);
        return item.getPlace();
    }

    public String[] getItemInfo(int i){
        String[] info = new String[5];

        info[0] = searchResultListItemsList.get(i).getPlace();
        info[1] = searchResultListItemsList.get(i).getText2();
        info[2] = searchResultListItemsList.get(i).getTime();
        info[3] = searchResultListItemsList.get(i).getX();
        info[4] = searchResultListItemsList.get(i).getY();

        return info;
    }

    public void clearItem(){
        searchResultListItemsList.clear();
    }
}
