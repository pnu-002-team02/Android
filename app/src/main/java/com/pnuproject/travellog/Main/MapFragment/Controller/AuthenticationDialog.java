package com.pnuproject.travellog.Main.MapFragment.Controller;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pnuproject.travellog.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AuthenticationDialog extends Activity {

    ImageView present_image;
    String product_Image_URL;
    Bitmap bitmap;
    String place_Name;
    Double place_latitude,place_longitude;

    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.present_image);
        present_image = (ImageView)findViewById(R.id.present_product_image);

        //해당명소 URL 넘겨받기
        product_Image_URL = getIntent().getStringExtra("productImage");
        place_latitude = getIntent().getDoubleExtra("latitude",0);
        place_longitude = getIntent().getDoubleExtra("longitude",0);
        place_Name = getIntent().getStringExtra("name");

        Toast.makeText(this, place_Name, Toast.LENGTH_SHORT).show();

        //해당 명소 사진 제시
        Thread mThread = new Thread(){
            @Override
            public void run(){
                try{
                    URL url = new URL(product_Image_URL);

                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                }catch(IOException ex){

                }
            }
        };
        mThread.start();

        try{
            mThread.join();
            present_image.setImageBitmap(bitmap);
        }catch(InterruptedException e){
            e.printStackTrace();
        }


        ImageButton start_camera = (ImageButton)findViewById(R.id.start_camera);
        start_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cameraIntent = new Intent(getApplicationContext(),CameraFunction.class);
                cameraIntent.putExtra("productImage",product_Image_URL);
                cameraIntent.putExtra("latitude",place_latitude);
                cameraIntent.putExtra("longitude",place_longitude);
                cameraIntent.putExtra("name",place_Name);
                startActivity(cameraIntent);
            }
        });

    }
}
