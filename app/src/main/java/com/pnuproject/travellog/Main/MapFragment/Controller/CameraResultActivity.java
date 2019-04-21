package com.pnuproject.travellog.Main.MapFragment.Controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.ImageView;

import com.pnuproject.travellog.R;

public class CameraResultActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
        setContentView(R.layout.cameraresult);

        Intent intent = getIntent();
        String photoPath = intent.getStringExtra("strParamName");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        final Bitmap bmp  = BitmapFactory.decodeFile(photoPath,options);

        Matrix matrix =new Matrix();
        matrix.preRotate(90);
        Bitmap adjustedBitmap = Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),matrix,true);

        ImageView img = (ImageView)findViewById(R.id.imageView1);
        img.setImageBitmap(adjustedBitmap);
    }
}
