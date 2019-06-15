package com.pnuproject.travellog.Main.MapFragment.Controller;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pnuproject.travellog.R;

import java.util.ArrayList;

public class BriefReviewFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView lvBriefReview;
    private BriefReviewLVAdapter breifReviewLVApdater;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_map, container, false);
        lvBriefReview = (ListView) view.findViewById(R.id.lvBriefReview_briefreview);
        lvBriefReview.setOnItemClickListener(this);
        lvBriefReview.setAdapter(breifReviewLVApdater);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l_position) {
        if (adapterView.getAdapter() == breifReviewLVApdater) {
            //breifReviewLVApdater.getItem(position);
        }
    }
}

class BriefReviewLVAdapter extends BaseAdapter {
    ArrayList<String> listItemStr;

    public BriefReviewLVAdapter() {
        listItemStr = new ArrayList<String>();
    }

    public void addItem(String str) {
        //listItemStr.add(str);
    }

    public void setItem(ArrayList<String> listItem) {
        //listItemStr = listItem;
    }

    public void removeItem(int index) {
        //listItemStr.remove(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lvitem_briefreview, parent, false);
        }
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle_briefreview);
        TextView tvContent = (TextView) convertView.findViewById(R.id.tvContent_briefreview);
        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage_briefreview);

        return convertView;
    }

    @Override
    public int getCount() {
        return listItemStr.size();
    }

    @Override
    public Object getItem(int position) {
        return listItemStr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
