package com.pnuproject.travellog.Main.MapFragment.Controller;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.pnuproject.travellog.R;

import java.util.List;

public class Dialog extends Activity {
//    final int MY_PERMISSION_REQUEST_CODE =100;
//    int APIVersion = Build.VERSION.SDK_INT;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.present_image);

        ImageButton btn = (ImageButton) findViewById(R.id.ok_button);
        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){


                Intent cameraIntent = new Intent(getApplicationContext(),AndroidCamera.class);
                startActivity(cameraIntent);
            }

        });

//        Button btn = (Button)findViewById(R.id.ok_button);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(isExistsCameraApplication()){
//
//                   // Intent CameraApp = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(CameraApp,10000);
//                }
//            }
//        });
//
//    }
//    private boolean isExistsCameraApplication(){
//        //안드로이드의 모든 어플리케이션을 받아온다.
//        PackageManager packageManager = getPackageManager();
//
//        //카메라 어플리케이션
//        Intent CameraApp  = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        //MediaStore.ACTION_IMAGE_CAPTURE의 intent를 처리할 수 있는 어플리케이션 정보를 가져옴.
//        List<ResolveInfo> cameraApps = packageManager.queryIntentActivities(CameraApp,PackageManager.MATCH_DEFAULT_ONLY);
//
//        return cameraApps.size() >0;
    }

//    private boolean checkCAMERAPermission(){
//        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
//        return result == PackageManager.PERMISSION_GRANTED;
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],@NonNull int[] grantResults){
//        switch (requestCode){
//            case MY_PERMISSION_REQUEST_CODE:
//                if(grantResults.length > 0){
//                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    if(cameraAccepted){
//
//                    }
//                }
//        }
//    }
}