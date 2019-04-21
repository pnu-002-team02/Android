package com.pnuproject.travellog.Main.MapFragment.Controller;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.pnuproject.travellog.Main.MapFragment.Controller.CameraResultActivity;
import com.pnuproject.travellog.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static android.Manifest.permission_group.CAMERA;
import static android.support.constraint.Constraints.TAG;

public class AndroidCamera extends Activity implements SurfaceHolder.Callback{

    final int MY_PERMISSION_REQUEST_CODE =100;
    int APIVersion = Build.VERSION.SDK_INT;

    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Button button;
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
        View viewControl = controlInflater.inflate(R.layout.control,null);
        ViewGroup.LayoutParams layoutparamsControl = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        this.addContentView(viewControl,layoutparamsControl);

        ImageButton buttonTakePicture = (ImageButton)findViewById(R.id.takepicture);
        buttonTakePicture.setOnClickListener(new ImageButton.OnClickListener(){

            @Override
            public void onClick(View v){
                if(!hasPermissions()){
                    requestNecessaryPermissions();
                }
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

                Toast.makeText(AndroidCamera.this,"Image Saved: "+ uriTarget.toString(),Toast.LENGTH_LONG).show();
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

    private boolean hasPermissions(){
        int res = 0;
        String[] permissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        for(String perms: permissions){
            res = checkCallingOrSelfPermission(perms);
            if(!(res == PackageManager.PERMISSION_GRANTED)){
                return false;
            }
        }
        return true;
    }
    private void requestNecessaryPermissions(){
        String[] permissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions, MY_PERMISSION_REQUEST_CODE);
        }
    }
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grandResults){
        boolean allowed =true;
        switch (requestCode){
            case MY_PERMISSION_REQUEST_CODE:
                for(int res : grandResults){
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                    Toast.makeText(this,"Camera Permission granted",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                Toast.makeText(this,"Camera Permission denied",Toast.LENGTH_SHORT).show();
                allowed = false;
                break;
        }
        if(allowed){
            doRestart(this);
        } else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                    Toast.makeText(AndroidCamera.this,"Camera Permissions denied",Toast.LENGTH_SHORT).show();
                }
                else if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Toast.makeText(AndroidCamera.this,"Storage Permissions denied",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public static void doRestart(Context c){
        try{
            if(c!=null){
                PackageManager pm = c.getPackageManager();

                if(pm!=null){
                    Intent mStartActivity = pm.getLaunchIntentForPackage(c.getPackageName());
                    if(mStartActivity!=null){
                        mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        int mPendingIntentId = 223344;
                        PendingIntent mPendingIntent = PendingIntent.getActivity(c,mPendingIntentId,mStartActivity,PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager mgr = (AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
                        mgr.set(AlarmManager.RTC,System.currentTimeMillis()+100,mPendingIntent);
                        System.exit(0);
                    }
                    else{
                        Log.e(TAG,"Wast not able to restart application,mStartActivity null");
                    }
                }
                else{
                    Log.e(TAG,"Was not able to restart application, PM null");
                }
            }
            else{
                Log.e(TAG,"Was not able to restart application,Context null");
            }
        }
        catch (Exception ex){
            Log.e(TAG,"Was not able to restart application");
        }
    }


}