package com.pnuproject.travellog.Main.MapFragment.Controller;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pnuproject.travellog.Main.MainActivity.Controller.MainActivity;
import com.pnuproject.travellog.Main.MapFragment.Controller.Search.SearchClass;
import com.pnuproject.travellog.Main.MapFragment.Controller.Search.SearchDialog;
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

public class ClickedMarkerDialog extends AppCompatDialog implements RetrofitTask.RetrofitExecutionHandler{

    String productName;
    String info;
    String gps;
    String productImage;
    AppCompatActivity parentActivity;
    private Bitmap bitmap;
    Thread mThread;
    MapFragment mapFragment;

    private RetrofitTask retrofitTask;

    private final static int RETROFIT_TASK_ERROR = 0x00;
    private final static int RETROFIT_TASK_MARKER = 0x01;

    TextView placeLocation;
    ImageView placePicture;
    //,String user0,String user1,String target0,String target1
    public ClickedMarkerDialog(final AppCompatActivity activity,final String name,final int isVisited,final double user0,final double user1,final double target0,final double target1,final MapFragment mapFragment) {
        super(activity);
        this.mapFragment = mapFragment;
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.clicked_marker_dialog);
        parentActivity = activity;
        retrofitTask = new RetrofitTask(this, getContext().getResources().getString(R.string.server_address));
        TextView placeName = (TextView)findViewById(R.id.placeName);
        placeLocation = (TextView)findViewById(R.id.placeLocation);
        placePicture = (ImageView)findViewById(R.id.viewPicture);
        Button btn_blogArticle = (Button)findViewById(R.id.btnBlogArticle_markerdlg);
        Button btn_photo = (Button)findViewById(R.id.photo);
        Button btn_searchTransport = (Button)findViewById(R.id.btnSearchTransport_markerdlg);

        placeName.setText(name);

        RequestDataClickedMarker dataClickedMarker = new RequestDataClickedMarker(name);
        RetrofitTask.RetrofitRequestParam requestParam = new RetrofitTask.RetrofitRequestParam(RETROFIT_TASK_MARKER, dataClickedMarker);
        retrofitTask.execute(requestParam);
        findViewById(R.id.btnClose_markerdlg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
        btn_searchTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] p = new String[4];
                p[0] = new Double(user1).toString();
                p[1] = new Double(user0).toString();
                p[2] = new Double(target1).toString();
                p[3] = new Double(target0).toString();
                final SearchClass searchClass = new SearchClass();
                searchClass.findPath(p, parentActivity);


                final ProgressDialog progressDialog = new ProgressDialog(parentActivity);
                progressDialog.setMessage("검색 중");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(searchClass.getf() == true){
                            searchClass.printPathTest();
                            mapFragment.showPathlist(name, searchClass);
                            cancel();
                            progressDialog.dismiss();
                        }
                    }
                },2000);
            }
        });
        btn_blogArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BlogArticleFragment blogArticleFragment = new BlogArticleFragment(name);
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.map_main_fragment, blogArticleFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                cancel();
            }
        });
        //System.out.println("방문 여부 확인 : " + getIntent().getIntExtra("visited", 1));
        //방문하지 않았을 때만 photo button 생성
        if(isVisited == 0 && TLApp.getUserInfo() != null) {
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
                        Toast.makeText(activity, "카메라 권한 및 쓰기 권한을 주지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            btn_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(activity, "이전에 촬영한 명소입니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        builder.setTitle("인증여부 확인");
        builder.setMessage("인증하시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(parentActivity,AuthenticationDialog.class);
                parentActivity.startActivity(intent);
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
            parentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(parentActivity, parentActivity.getResources().getString(R.string.errmsg_retrofit_unknown), Toast.LENGTH_SHORT).show();
                }
            });

            return;
        } else if( response.getTaskNum() == RETROFIT_TASK_ERROR) {
            final String errMsg = (String)response.getResponse();
            parentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(parentActivity, errMsg, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(parentActivity, res.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                        String fullMarkerInfo = res.getOneProduct().toString();
                        StringTokenizer st = new StringTokenizer(fullMarkerInfo, "$");

                        productName = st.nextToken();

                        info = st.nextToken().trim();

                        placeLocation.setText(info);
                        placeLocation.setMovementMethod(new ScrollingMovementMethod());

                        gps = st.nextToken();
                        productImage = st.nextToken();
                        Glide.with(parentActivity).load(productImage).into(placePicture);
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
            response = new String(parentActivity.getResources().getString(R.string.errmsg_retrofitbefore_ownernetwork));
        }
        catch (ConnectException ex) {
            paramRequest.setTaskNum(RETROFIT_TASK_ERROR);
            response = new String(parentActivity.getResources().getString(R.string.errmsg_retrofitbefore_servernetwork));
        }
        catch (Exception ex) {
            paramRequest.setTaskNum(RETROFIT_TASK_ERROR);
            response = new String(parentActivity.getResources().getString(R.string.errmsg_retrofit_unknown));
        }

        return response;
    }
}
