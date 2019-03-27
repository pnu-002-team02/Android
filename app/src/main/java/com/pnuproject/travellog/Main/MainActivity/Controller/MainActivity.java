package com.pnuproject.travellog.Main.MainActivity.Controller;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pnuproject.travellog.Main.HomeFragment.Controller.HomeFragment;
import com.pnuproject.travellog.Main.MapFragment.Controller.MapFragment;
import com.pnuproject.travellog.Main.MypageFragment.Controller.MypageFragment;
import com.pnuproject.travellog.R;
import com.pnuproject.travellog.etc.SwipeViewPager;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    final public int MAX_TAB_PAGES = 3;
    Vector<Fragment> vFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vFragments = new Vector<Fragment>();

        final SwipeViewPager vpFrag = findViewById(R.id.vpFrag_main);

        MainPagerAdapter pagerAdapter = new MainPagerAdapter(vFragments,getSupportFragmentManager());
        if(savedInstanceState == null) {
            vFragments.add(new HomeFragment());
            vFragments.add(new MapFragment());
            vFragments.add(new MypageFragment());
        }
        LinearLayout homeTabView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.tab_main, null);
        LinearLayout mapTabView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.tab_main, null);
        LinearLayout mypageTabView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.tab_main, null);

        ImageView ivTab[] = new ImageView[3];
        TextView tvTab[] = new TextView[3];
        tvTab[0] = (TextView) homeTabView.findViewById(R.id.tvTabText_maintab);
        ivTab[0] = (ImageView) homeTabView.findViewById(R.id.ivTabImage_maintab);
        tvTab[1] = (TextView) mapTabView.findViewById(R.id.tvTabText_maintab);
        ivTab[1] = (ImageView) mapTabView.findViewById(R.id.ivTabImage_maintab);
        tvTab[2] = (TextView) mypageTabView.findViewById(R.id.tvTabText_maintab);
        ivTab[2] = (ImageView) mypageTabView.findViewById(R.id.ivTabImage_maintab);

        tvTab[0].setText("홈");
        ivTab[0].setImageResource(R.drawable.ic_home);
        tvTab[1].setText("지도");
        ivTab[1].setImageResource(R.drawable.ic_map);
        tvTab[2].setText("마이페이지");
        ivTab[2].setImageResource(R.drawable.ic_mypage);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout_main);
        tabLayout.addTab(tabLayout.newTab().setCustomView(homeTabView));
        tabLayout.addTab(tabLayout.newTab().setCustomView(mapTabView));
        tabLayout.addTab(tabLayout.newTab().setCustomView(mypageTabView));

        vpFrag.setOffscreenPageLimit(MAX_TAB_PAGES-1);
        vpFrag.setAdapter(pagerAdapter);
        vpFrag.setPagingEnabled(false);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabNum = tab.getPosition();
                vpFrag.setCurrentItem(tab.getPosition(),false);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

    }
}
