package com.pnuproject.travellog.Main.MapFragment.Controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.pnuproject.travellog.R;

import java.io.File;

public class CameraResult extends Activity {

    File imageFile;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cameraresult);


        Intent intent = getIntent();

        if(intent.getAction().equals("카메라 찍은 결과")){

            String path = intent.getExtras().getString("Path");

            imageFile = new File(path);
            Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());


            ImageView resultImage = (ImageView)findViewById(R.id.resultimage);
            resultImage.setImageBitmap(myBitmap);


        }
    }
}