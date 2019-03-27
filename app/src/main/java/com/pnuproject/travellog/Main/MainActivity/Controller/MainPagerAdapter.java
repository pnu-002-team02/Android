package com.pnuproject.travellog.Main.MainActivity.Controller;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Vector;

public class MainPagerAdapter extends FragmentPagerAdapter {
    private Vector<Fragment> vFragments;

    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(position);
    }

    public MainPagerAdapter(Vector<Fragment> vFragments, FragmentManager fm) {
        super(fm);
        this.vFragments = vFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return vFragments.get(position);
    }

    @Override
    public int getCount() {
        return vFragments.size();
    }
}