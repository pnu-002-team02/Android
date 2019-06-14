package com.pnuproject.travellog.Main.MapFragment.Controller;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pnuproject.travellog.Main.MapFragment.Model.MapClickedMarkerRetrofitInterface;
import com.pnuproject.travellog.Main.MapFragment.Model.RequestDataClickedMarker;
import com.pnuproject.travellog.Main.MapFragment.Model.ResponseDataClickedMarker;
import com.pnuproject.travellog.R;
import com.pnuproject.travellog.etc.RetrofitTask;
import com.pnuproject.travellog.etc.TLApp;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import retrofit2.Retrofit;

public class ClickedMarkerDialog extends Activity implements RetrofitTask.RetrofitExecutionHandler{

    String name;
    String productName;
    String info;
    String gps;
    String productImage;

    private Bitmap bitmap;
    Thread mThread;

    private RetrofitTask retrofitTask;

    private final int RETROFIT_TASK_ERROR = 0x00;
    private final int RETROFIT_TASK_MARKER = 0x01;

    TextView placeLocation;
    ImageView placePicture;

    protected void onCreate(Bundle savedInsanceState) {
        super.onCreate(savedInsanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        retrofitTask = new RetrofitTask(this, getResources().getString(R.string.server_address));
        setContentView(R.layout.clicked_marker_dialog);

        TextView placeName = (TextView)findViewById(R.id.placeName);
        placeLocation = (TextView)findViewById(R.id.placeLocation);
        placePicture = (ImageView)findViewById(R.id.viewPicture);
        Button btn_photo = (Button)findViewById(R.id.photo);

        name = getIntent().getStringExtra("name");
        placeName.setText(name);

        RequestDataClickedMarker dataClickedMarker = new RequestDataClickedMarker(name);
        RetrofitTask.RetrofitRequestParam requestParam = new RetrofitTask.RetrofitRequestParam(RETROFIT_TASK_MARKER, dataClickedMarker);
        retrofitTask.execute(requestParam);

        mThread = new Thread() {
            @Override
            public void run() {
                try{
                    URL url = new URL(productImage);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    bitmap = BitmapFactory.decodeStream(bis);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        //System.out.println("방문 여부 확인 : " + getIntent().getIntExtra("visited", 1));
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

    @Override
    public void onAfterAyncExcute(RetrofitTask.RetrofitResponseParam response) {
        if (response == null || response.getResponse() == null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.errmsg_retrofit_unknown), Toast.LENGTH_SHORT).show();
                }
            });

            return;
        } else if( response.getTaskNum() == RETROFIT_TASK_ERROR) {
            final String errMsg = (String)response.getResponse();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getBaseContext(), errMsg, Toast.LENGTH_SHORT).show();
                }
            });

            return;
        }

        int taskNum = response.getTaskNum();
        Object responseData = response.getResponse();

        switch (taskNum) {
            case RETROFIT_TASK_MARKER: {
                final ResponseDataClickedMarker res = (ResponseDataClickedMarker) responseData;
                if (res.getSuccess() != 0) {
                    Toast.makeText(getBaseContext(), res.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                        String fullMarkerInfo = res.getOneProduct().toString();
                        StringTokenizer st = new StringTokenizer(fullMarkerInfo, "$");

                        productName = st.nextToken();

                        info = st.nextToken().trim();

                        placeLocation.setText(info);
                        placeLocation.setMovementMethod(new ScrollingMovementMethod());

                        //gps : '37.22222, 129.3333' 형식(string)
                        gps = st.nextToken();

                        productImage = st.nextToken();
                        mThread.start();

                        try{
                            mThread.join();
                            placePicture.setImageBitmap(bitmap);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                }
            }
            break;
            default:
                break;
        }
    }

    @Override
    public Object onBeforeAyncExcute(Retrofit retrofit, RetrofitTask.RetrofitRequestParam paramRequest) {
        Object response = null;
        int taskNum = paramRequest.getTaskNum();
        Object requestParam = paramRequest.getParamRequest();
        MapClickedMarkerRetrofitInterface clickedMarkerRetrofit = retrofit.create(MapClickedMarkerRetrofitInterface.class);

        try {
            switch (taskNum) {
                case RETROFIT_TASK_MARKER:
                    response = clickedMarkerRetrofit.getClickedMarker((RequestDataClickedMarker) requestParam).execute().body();
                    break;
                default:
                    break;
            }
        }
        catch (UnknownHostException ex) {
            paramRequest.setTaskNum(RETROFIT_TASK_ERROR);
            response = new String(getResources().getString(R.string.errmsg_retrofitbefore_ownernetwork));
        }
        catch (ConnectException ex) {
            paramRequest.setTaskNum(RETROFIT_TASK_ERROR);
            response = new String(getResources().getString(R.string.errmsg_retrofitbefore_servernetwork));
        }
        catch (Exception ex) {
            paramRequest.setTaskNum(RETROFIT_TASK_ERROR);
            response = new String(getResources().getString(R.string.errmsg_retrofit_unknown));
        }

        return response;
    }
}
