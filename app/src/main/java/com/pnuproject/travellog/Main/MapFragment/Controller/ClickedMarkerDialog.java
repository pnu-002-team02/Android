package com.pnuproject.travellog.Main.MapFragment.Controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pnuproject.travellog.R;

public class ClickedMarkerDialog extends Activity {

    String name;

    protected void onCreate(Bundle savedInsanceState) {
        super.onCreate(savedInsanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.clicked_marker_dialog);

        final TextView placeName = (TextView)findViewById(R.id.placeName);
        final TextView placeLocation = (TextView)findViewById(R.id.placeLocation);
        final Button btn_photo = (Button)findViewById(R.id.photo);

        System.out.println("방문 여부 확인 : " + getIntent().getIntExtra("visited", 1));
        //방문하지 않았을 때만 photo button 생성
        if(getIntent().getIntExtra("visited", 1) == 0) {
            btn_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "사진 찍을 수 있도록 연결", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            btn_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "이전에 촬영한 명소입니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        name = getIntent().getStringExtra("name");
        placeName.setText(name);
        placeLocation.setText("임시 주소");

    }

    //확인 버튼 클릭
    public void mOnClose(View v){
        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }


}
