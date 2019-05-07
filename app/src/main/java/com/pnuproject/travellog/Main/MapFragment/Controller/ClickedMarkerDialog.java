package com.pnuproject.travellog.Main.MapFragment.Controller;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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

                    boolean camera = ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED;

                    boolean write = ContextCompat.checkSelfPermission
                            (view.getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

                    if(camera && write){
                        //사진 찍는 인텐트 코드 넣기
                        authenticationMessageBox();
                    }
                    else{
                        Toast.makeText(ClickedMarkerDialog.this, "카메라 권한 및 쓰기 권한을 주지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(getApplicationContext(), "사진 찍을 수 있도록 연결", Toast.LENGTH_SHORT).show();
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

    void authenticationMessageBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("인증여부 확인");
        builder.setMessage("인증하시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(),AuthenticationDialog.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

}
