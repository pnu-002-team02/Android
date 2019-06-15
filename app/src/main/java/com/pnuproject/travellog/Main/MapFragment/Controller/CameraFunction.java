package com.pnuproject.travellog.Main.MapFragment.Controller;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.pnuproject.travellog.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import static android.content.ContentValues.TAG;

public class CameraFunction extends Activity implements SurfaceHolder.Callback {

    Camera camera;
    File imageFile;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;

    boolean previewing = false;

    LayoutInflater controlInflater = null;

    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFormat(PixelFormat.UNKNOWN);

        surfaceView = (SurfaceView)findViewById(R.id.surfaceview);
        surfaceHolder = surfaceView.getHolder();

        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        controlInflater = LayoutInflater.from(getBaseContext());
        View viewControl = controlInflater.inflate(R.layout.cameradesign,null);
        ViewGroup.LayoutParams layoutparamsControl = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        this.addContentView(viewControl,layoutparamsControl);

        ImageButton buttonTakePicture = (ImageButton)findViewById(R.id.takepicture);
        buttonTakePicture.setOnClickListener(new ImageButton.OnClickListener(){

            @Override
            public void onClick(View v){

                camera.takePicture(myShutterCallback,myPictureCallback_RAW,myPictureCallback_JPG);
            }
        });
    }
    Camera.ShutterCallback myShutterCallback =new Camera.ShutterCallback() {
        @Override
        public void onShutter() {

        }
    };
    Camera.PictureCallback myPictureCallback_RAW = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {

        }

    };
    Camera.PictureCallback myPictureCallback_JPG = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
// Bitmap bitmapPicture = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            Uri uriTarget = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,new ContentValues());

            OutputStream imageFileOS;

            try{
                imageFileOS = getContentResolver().openOutputStream(uriTarget);
                imageFileOS.write(bytes);
                imageFileOS.flush();
                imageFileOS.close();

                Cursor c = getContentResolver().query(Uri.parse(uriTarget.toString()), null, null, null, null);
                c.moveToNext();
                String absolutePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));

                imageFile = new File(absolutePath);
                if(imageFile.exists()){
                    Intent cameraReult = new Intent(getApplicationContext(),CameraResult.class);
                    cameraReult.setAction("카메라 찍은 결과");
                    cameraReult.putExtra("Path",absolutePath);
                    startActivity(cameraReult);
                }
                else{
                    //없을경우
                }



            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }

            camera.startPreview();
        }
    };


    @Override
    public void surfaceChanged(SurfaceHolder holder,int format,int width,int height){
        if(previewing){
            camera.stopPreview();
            previewing =false;
        }
        if(camera !=null){
            try{
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                previewing = true;
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder){
        camera = Camera.open();
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        camera.stopPreview();
        camera.release();
        camera = null;
        previewing = false;
    }


}
