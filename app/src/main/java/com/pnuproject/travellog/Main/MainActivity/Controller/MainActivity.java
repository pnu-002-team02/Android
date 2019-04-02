package com.pnuproject.travellog.Main.MainActivity.Controller;

import android.Manifest;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pnuproject.travellog.Main.HomeFragment.Controller.HomeFragment;
import com.pnuproject.travellog.Main.MapFragment.Controller.MapFragment;
import com.pnuproject.travellog.Main.MypageFragment.Controller.MypageFragment;
import com.pnuproject.travellog.R;
import com.pnuproject.travellog.etc.BackPressCloseHandler;
import com.pnuproject.travellog.etc.SwipeViewPager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    final public int MAX_TAB_PAGES = 3;
    Vector<Fragment> vFragments;

    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 카카오 key hash 획득
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        //뒤로가기 2번 누를 때 앱 종료
        backPressCloseHandler = new BackPressCloseHandler(this);

        //위치 permission 획득
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION  }, 1 );
        }


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


    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast toast=Toast.makeText(this,"현재 위치 기능 사용을 위한 권한 동의가 필요합니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            default:
                break;
        }
    }
}



