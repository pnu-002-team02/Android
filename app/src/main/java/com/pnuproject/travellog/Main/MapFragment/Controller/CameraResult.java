package com.pnuproject.travellog.Main.MapFragment.Controller;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pnuproject.travellog.Main.MapFragment.Model.AddVisitedLocationInterface;
import com.pnuproject.travellog.Main.MapFragment.Model.MapMarkerRetrofitInterface;
import com.pnuproject.travellog.Main.MapFragment.Model.RequestDataClickedMarker;
import com.pnuproject.travellog.Main.MapFragment.Model.RequestVisitData;
import com.pnuproject.travellog.Main.MapFragment.Model.ResponseDataMarker;
import com.pnuproject.travellog.Main.MapFragment.Model.ResponseDataVisitedList;
import com.pnuproject.travellog.Main.MapFragment.Model.ResponseVisitData;
import com.pnuproject.travellog.Main.MapFragment.Model.ResultData;
import com.pnuproject.travellog.R;
import com.pnuproject.travellog.etc.GpsTracker;
import com.pnuproject.travellog.etc.RetrofitTask;
import com.pnuproject.travellog.etc.TLApp;

import net.daum.mf.map.api.MapPoint;

import org.opencv.android.Utils;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CameraResult extends Activity implements RetrofitTask.RetrofitExecutionHandler{

    File imageFile;
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;

    private Bitmap myBitmap;
    private ImageView resultImage;

    private final int RETROFIT_TASK_ERROR = 0x00;
    private final int RETROFIT_TASK_ADD = 0x01;
    private RetrofitTask retrofitTask;

    Double destination_latitude,destination_longitude;

    String image_URL ;
    String place_Name;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cameraresult);
        retrofitTask = new RetrofitTask(this, getResources().getString(R.string.server_address));


        ImageButton goAuthentication = (ImageButton)findViewById(R.id.goauthentication);
        ImageButton backButton = (ImageButton)findViewById(R.id.retrybutton);

        backButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        destination_latitude = getIntent().getDoubleExtra("latitude",0);
        destination_longitude = getIntent().getDoubleExtra("longitude",0);

        image_URL = getIntent().getStringExtra("productImage");
        if(intent.getAction().equals("카메라 찍은 결과")){

            String path = intent.getExtras().getString("Path");

            imageFile = new File(path);
            myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());


            resultImage = (ImageView)findViewById(R.id.resultimage);
            resultImage.setImageBitmap(myBitmap);


        }


        image_URL = getIntent().getStringExtra("productImage");

        goAuthentication.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v){


                new AsyncTaskLoadImage(resultImage).execute(image_URL);

            }


        });





    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    public double distance(Location A,Location B){
        double between_distance = A.distanceTo(B);
        between_distance = between_distance/1000;
        return between_distance;
    }




    public int compareFeature(Bitmap bitmap1,Bitmap bitmap2) {
        int retVal = 0;
        long startTime = System.currentTimeMillis();

        System.loadLibrary("opencv_java3");






        //Mat img1 = Imgcodecs.imread(filename1,Imgcodecs.CV_LOAD_IMAGE_COLOR);

        Mat img1 = new Mat();
        Utils.bitmapToMat(bitmap1,img1);
        Mat img2 = new Mat();
        Utils.bitmapToMat(bitmap2,img2);

        MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
        MatOfKeyPoint keypoints2 = new MatOfKeyPoint();

        Mat descriptors1 = new Mat();
        Mat descriptors2 = new Mat();

    // Definition of ORB key point detector and descriptor extractors
        FeatureDetector detector = FeatureDetector.create(FeatureDetector.ORB);
        DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.ORB);

    // Detect key points
        detector.detect(img1, keypoints1);
        detector.detect(img2, keypoints2);

    // Extract descriptors
        extractor.compute(img1, keypoints1, descriptors1);
        extractor.compute(img2, keypoints2, descriptors2);

    // Definition of descriptor matcher
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMINGLUT);
        MatOfDMatch matches = new MatOfDMatch();

        if (descriptors2.cols() == descriptors1.cols()) {
            matcher.match(descriptors1, descriptors2 ,matches);

    // Check matches of key points
            DMatch[] match = matches.toArray();

            //거리 최대는 100 최소는 0.
            double max_dist = 0; double min_dist = 100;

            for (int i = 0; i < descriptors1.rows(); i++) {
                double dist = match[i].distance;
                if( dist < min_dist ) min_dist = dist;
                if( dist > max_dist ) max_dist = dist;
            }
            // textView1.setText("max_dist=" + String.valueOf(max_dist) + ", min_dist=" + String.valueOf(min_dist));

// Extract good images (distances are under 10)
            for (int i = 0; i < descriptors1.rows(); i++) {
                if (match[i].distance <= 10) {
                    retVal++;
                }
            }
            //textView2.setText("matching count=" + String.valueOf(retVal));
        }

        long estimatedTime = System.currentTimeMillis() - startTime;
        //textView3.setText("estimatedTime=" + String.valueOf(estimatedTime)+ "ms");

        return retVal;
    }

    public class AsyncTaskLoadImage extends AsyncTask<String, String, Bitmap> {

        ProgressDialog asyncDialog = new ProgressDialog(
                CameraResult.this);

        private final static String TAG = "AsyncTaskLoadImage";
        private ImageView imageView;

        public AsyncTaskLoadImage(ImageView imageView) {
            this.imageView = imageView;
        }
        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("인증중입니다..");

            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                URL url = new URL(params[0]);
                bitmap = BitmapFactory.decodeStream((InputStream) url.getContent());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            try {
                int width, height;
                height = bitmap.getHeight();
                width = bitmap.getWidth();

                Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                if(myBitmap!=null){
                    int ret = compareFeature(bmpGrayscale,myBitmap);
                    //Toast.makeText(CameraResult.this, String.valueOf(ret), Toast.LENGTH_SHORT).show();

                    if(ret > 0){
                        Toast.makeText(CameraResult.this, "인증 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        place_Name = getIntent().getStringExtra("name");
                        RequestVisitData dataVisit = new RequestVisitData(TLApp.getUserInfo().getUserID(),place_Name);
                        RetrofitTask.RetrofitRequestParam requestParam = new RetrofitTask.RetrofitRequestParam(RETROFIT_TASK_ADD, dataVisit);
                        retrofitTask.execute(requestParam);


                    }
                    else{

                        GpsTracker gpsTracker = new GpsTracker(CameraResult.this);

                        double present_latitude = gpsTracker.getLatitude();
                        double present_longitude = gpsTracker.getLongitude();

                        Location present_Location = new Location("present Location");
                        Location destination = new Location("destination");

                        present_Location.setLatitude(present_latitude);
                        present_Location.setLongitude(present_longitude);

                        destination.setLatitude(destination_latitude);
                        destination.setLongitude(destination_longitude);

                        double between_distance = distance(present_Location,destination);

                        /////////////////////킬로미터 단위임.///////////////////////
                        if(between_distance <0.010){


                            Toast.makeText(CameraResult.this, "인증 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            place_Name = getIntent().getStringExtra("name");
                            Toast.makeText(CameraResult.this, place_Name, Toast.LENGTH_SHORT).show();
                            RequestVisitData dataVisit = new RequestVisitData(TLApp.getUserInfo().getUserID(),place_Name);
                            RetrofitTask.RetrofitRequestParam requestParam = new RetrofitTask.RetrofitRequestParam(RETROFIT_TASK_ADD, dataVisit);
                            retrofitTask.execute(requestParam);
                        }
                        else{
                            Toast.makeText(CameraResult.this, "인증 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                asyncDialog.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAfterAyncExcute(RetrofitTask.RetrofitResponseParam response) {
        if (response == null || response.getResponse() == null) {
            Toast.makeText(this, getResources().getString(R.string.errmsg_retrofit_unknown), Toast.LENGTH_SHORT).show();
            return;

        }

        int taskNum = response.getTaskNum();
        Object responseData = response.getResponse();

        switch (taskNum) {
            case RETROFIT_TASK_ADD: {
                final ResponseVisitData res = (ResponseVisitData) responseData;
                if (res.getSuccess() != 0) {
                    Toast.makeText(getBaseContext(), res.getMessage(), Toast.LENGTH_SHORT).show();
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
        AddVisitedLocationInterface addRetrofit = retrofit.create(AddVisitedLocationInterface.class);

        try {
            switch (taskNum) {
                case RETROFIT_TASK_ADD:
                    response = addRetrofit.putResultData((RequestVisitData)requestParam).execute().body();
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
            //System.out.println("에러 확인 함 : " + ex.toString());
            paramRequest.setTaskNum(RETROFIT_TASK_ERROR);
            response = new String(getResources().getString(R.string.errmsg_retrofit_unknown));
        }
        return response;
    }

}