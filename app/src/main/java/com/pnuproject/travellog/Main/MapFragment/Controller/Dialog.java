package com.pnuproject.travellog.Main.MapFragment.Controller;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import com.pnuproject.travellog.R;

import java.util.List;

public class Dialog extends Activity {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.present_image);


        Button btn = (Button)findViewById(R.id.ok_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isExistsCameraApplication()){

                    Intent CameraApp = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(CameraApp,10000);
                }
            }
        });

    }
    private boolean isExistsCameraApplication(){
        //안드로이드의 모든 어플리케이션을 받아온다.
        PackageManager packageManager = getPackageManager();

        //카메라 어플리케이션
        Intent CameraApp  = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //MediaStore.ACTION_IMAGE_CAPTURE의 intent를 처리할 수 있는 어플리케이션 정보를 가져옴.
        List<ResolveInfo> cameraApps = packageManager.queryIntentActivities(CameraApp,PackageManager.MATCH_DEFAULT_ONLY);

        return cameraApps.size() >0;
    }
}