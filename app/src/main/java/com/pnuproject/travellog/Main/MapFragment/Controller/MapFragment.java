package com.pnuproject.travellog.Main.MapFragment.Controller;

/**
 * Created by s0woo on 2019-04-02.
 */

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pnuproject.travellog.Login.Controller.LoginActivity;
import com.pnuproject.travellog.Main.MapFragment.Controller.Search.ListViewAdapter;
import com.pnuproject.travellog.Main.MapFragment.Controller.Search.SearchClass;
import com.pnuproject.travellog.Main.MapFragment.Controller.Search.SearchDialog;
import com.pnuproject.travellog.Main.MapFragment.Model.MapMarkerRetrofitInterface;
import com.pnuproject.travellog.Main.MapFragment.Model.RequestDataMarker;
import com.pnuproject.travellog.Main.MapFragment.Model.ResponseDataMarker;
import com.pnuproject.travellog.R;
import com.pnuproject.travellog.etc.GpsTracker;
import com.pnuproject.travellog.etc.RetrofitTask;
import com.pnuproject.travellog.etc.TLApp;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapLayout;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import retrofit2.Retrofit;


public class MapFragment extends Fragment
        implements MapView.OpenAPIKeyAuthenticationResultListener, MapView.MapViewEventListener, MapView.POIItemEventListener, RetrofitTask.RetrofitExecutionHandler{

    private MapView mMapView;

    private MapPOIItem mVisitedMarker;
    private MapPOIItem mUnvisitedMarker;
    //private static final MapPoint DEFAULT_MARKER_POINT = MapPoint.mapPointWithGeoCoord(35.2295239,129.0881219);
    //private static final MapPoint DEFAULT_MARKER_POINT1 = MapPoint.mapPointWithGeoCoord(35.2336123,129.078816);

    String name;
    double latitude, longitude;
    String markerName;
    double markerLatitude, markerLongitude;

    private RetrofitTask retrofitTask;

    private final int RETROFIT_TASK_ERROR = 0x00;
    private final int RETROFIT_TASK_GET_MARKER = 0x01;

    private EditText edit_search;
    private ImageButton btn_search;
    private ImageButton btn_gps;

    private TextView gps;
    private GpsTracker gpsTracker;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    private ListViewAdapter adapter;
    private ListView listView;

    public MapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        retrofitTask = new RetrofitTask(this, getResources().getString(R.string.server_address));

        MapLayout mapLayout = new MapLayout(getActivity());
        mMapView = mapLayout.getMapView();
        mMapView.setDaumMapApiKey(getString(R.string.kakao_map_key));
        mMapView.setOpenAPIKeyAuthenticationResultListener(this);
        mMapView.setMapViewEventListener(this);
        mMapView.setMapType(MapView.MapType.Standard);
        mMapView.setPOIItemEventListener(this);

        ViewGroup mapViewContainer = (ViewGroup) getView().findViewById(R.id.map_view);
        mapViewContainer.addView(mapLayout);



        RequestDataMarker dataMarker = new RequestDataMarker();
        RetrofitTask.RetrofitRequestParam requestParam = new RetrofitTask.RetrofitRequestParam(RETROFIT_TASK_GET_MARKER, dataMarker);
        //RetrofitTask.RetrofitRequestParam requestParam = new RetrofitTask.RetrofitRequestParam(RETROFIT_TASK_GET_MARKER, "");
        retrofitTask.execute(requestParam);

        mMapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
        //createVisitedMarker(mMapView, "임시 방문 명소", DEFAULT_MARKER_POINT);
//        createUnvisitedMarker(mMapView, "임시 미방문 명소", DEFAULT_MARKER_POINT1);

//        for(int i=0 ; i<20; i++) {
//            double tempLon = 128.078816 + (i*0.1);
//            createUnvisitedMarker(mMapView, "임시 미방문 명소", MapPoint.mapPointWithGeoCoord(35.2036123+(i*0.01),tempLon));
//        }

        //createCustomMarker(mMapView);
        //createCustomBitmapMarker(mMapView);
        //showAll();


        LoginActivity login = (LoginActivity)getActivity().getApplicationContext();

        edit_search = (EditText) view.findViewById(R.id.edit_search);
        btn_search = (ImageButton) view.findViewById(R.id.btn_search);
        btn_gps = (ImageButton) view.findViewById(R.id.gps_tracker);

        listView = (ListView) getView().findViewById(R.id.search_list);
        gps = (TextView) getView().findViewById(R.id.gpsvalue);

        gpsTracker = new GpsTracker(getContext());
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
        mMapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude,longitude), 2, true);

        gps.setText(latitude + " " + longitude);
        final SearchClass searchClass = new SearchClass();

        /*
        * GPS로 받아온 위도, 경도 값을 실제 주소로 변환하는 작업 필요
        * geocoding 사용
        */

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = edit_search.getText().toString();

                if(str == null || str.length() == 0){
                    Toast.makeText(getContext(), "장소를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
                    searchClass.findPlace(str);
                    listView.setVisibility(View.VISIBLE);
                    adapter = new ListViewAdapter();

                    adapter.addItem("place1", "time/address1", "weather1");
                    adapter.addItem("place2", "time/address2", "weather2");
                    adapter.addItem("place3", "time/address3", "weather3");

                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //리스트뷰 아이템 클릭했을 때
                            Toast.makeText(getContext(), adapter.getItemPlace(i) + " 터치", Toast.LENGTH_SHORT).show();
                            edit_search.setText(null);
                            listView.setVisibility(View.INVISIBLE);
                            /*
                             * Dialog 형태로 띄워줌
                             */

                            Bundle bundle = new Bundle();
                            bundle.putStringArray("info", adapter.getItemInfo(i));

                            SearchDialog dialog = new SearchDialog();

                            dialog.setArguments(bundle);
                            dialog.show(getActivity().getSupportFragmentManager(), "tag");
                        }
                    });
                }
            }
        });
        
        btn_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude,longitude), 2,true);
            }
        });
    }


    @Override
    public void onAfterAyncExcute(RetrofitTask.RetrofitResponseParam response) {
        if (response == null || response.getResponse() == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.errmsg_retrofit_unknown), Toast.LENGTH_SHORT).show();
            //System.out.println("!!!!응답없음");
            return;

        } else if( response.getTaskNum() == RETROFIT_TASK_ERROR) {
            final String errMsg = (String)response.getResponse();
            Toast.makeText(getContext(), errMsg, Toast.LENGTH_SHORT).show();
            System.out.println("!!!!에러   " + response.getTaskNum() + "   " + errMsg);
            return;
        }

        int taskNum = response.getTaskNum();
        Object responseData = response.getResponse();

        switch (taskNum) {
            case RETROFIT_TASK_GET_MARKER: {
                final ResponseDataMarker res = (ResponseDataMarker) responseData;
                if (res.getSuccess() != 0) {
                    Toast.makeText(getContext(), res.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println("****전체 확인 : " + res.getProducts().toString());
                    String fullMarkerInfo = res.getProducts().toString();
                    StringTokenizer st = new StringTokenizer(fullMarkerInfo, "[$,]");
                    int i = 0;
                    while(st.hasMoreTokens()) {
                        String temp = st.nextToken();
                        //System.out.println(i + "번째 마커 확인 " + temp);
                        if (i % 3 == 0) {
                            markerName = temp.trim();
                        } else if (i % 3 == 1) {
                            markerLatitude = Double.parseDouble(temp);
                        } else if (i % 3 == 2) {
                            markerLongitude = Double.parseDouble(temp);
                            System.out.println("마커 확인 : " + markerName + " " + markerLatitude + " " + markerLongitude);
                            createUnvisitedMarker(mMapView, markerName, MapPoint.mapPointWithGeoCoord(markerLatitude, markerLongitude));
                        }
                        i++;
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
        MapMarkerRetrofitInterface markerRetrofit = retrofit.create(MapMarkerRetrofitInterface.class);

        try {
            switch (taskNum) {
                case RETROFIT_TASK_GET_MARKER:
                    //response = markerRetrofit.getMarker((RequestDataMarker) requestParam).execute().body();
                    response = markerRetrofit.getMarker().execute().body();
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
            System.out.println("에러 확인 함 : " + ex.toString());
            paramRequest.setTaskNum(RETROFIT_TASK_ERROR);
            response = new String(getResources().getString(R.string.errmsg_retrofit_unknown));
        }
        return response;
    }

    public String findGPS(){
        String result = "";

        return result;
    }
    /*
     검색 버튼 클릭 시 다음 REST API 로컬 검색을 이용해 장소 검색
     */
    public void searchPlace(String str){

    }

    // CalloutBalloonAdapter 인터페이스 구현
    class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {
        private final View mCalloutBalloon;

        public CustomCalloutBalloonAdapter() {
            mCalloutBalloon = getLayoutInflater().inflate(R.layout.custom_callout_balloon, null);
        }

        @Override
        public View getCalloutBalloon(MapPOIItem poiItem) {
                    //((ImageView) mCalloutBalloon.findViewById(R.id.badge)).setImageResource(R.mipmap.ic_launcher);
                    ((TextView) mCalloutBalloon.findViewById(R.id.title)).setText(poiItem.getItemName());
                    if(poiItem.getTag()==1) {
                        ((TextView) mCalloutBalloon.findViewById(R.id.desc)).setText("이전에 방문한 명소입니다.");
                    } else {
                        ((TextView) mCalloutBalloon.findViewById(R.id.desc)).setText("방문하지 않은 명소입니다.");
            }
            return mCalloutBalloon;
        }

        @Override
        public View getPressedCalloutBalloon(MapPOIItem poiItem) {
            return null;
        }
    }

    private void createVisitedMarker(MapView mapView, String placeName, MapPoint VISITED_MARKER_POINT) {
        mVisitedMarker = new MapPOIItem();
        String name = placeName;
        mVisitedMarker.setItemName(name);
        mVisitedMarker.setTag(1);
        mVisitedMarker.setMapPoint(VISITED_MARKER_POINT);
        mVisitedMarker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        mVisitedMarker.setSelectedMarkerType(MapPOIItem.MarkerType.BluePin);

        mapView.addPOIItem(mVisitedMarker);
        mapView.deselectPOIItem(mVisitedMarker);
    }

    private void createUnvisitedMarker(MapView mapView, String placeName, MapPoint UNVISITED_MARKER_POINT) {
        mUnvisitedMarker = new MapPOIItem();
        String name = placeName;
        mUnvisitedMarker.setItemName(name);
        mUnvisitedMarker.setTag(0);
        mUnvisitedMarker.setMapPoint(UNVISITED_MARKER_POINT);
        mUnvisitedMarker.setMarkerType(MapPOIItem.MarkerType.RedPin);
        mUnvisitedMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

        mapView.addPOIItem(mUnvisitedMarker);
        mapView.deselectPOIItem(mUnvisitedMarker);
    }

    private void addCurrentLocationCircle(double latitude, double longitude) {
        MapCircle circle1 = new MapCircle(
                MapPoint.mapPointWithGeoCoord(latitude, longitude), // center
                27, // radius
                Color.argb(150, 250, 100, 120), // strokeColor
                Color.argb(150, 250, 100, 120) // fillColor
        );
        circle1.setTag(1234);
        mMapView.addCircle(circle1);
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude,longitude), 2, true);
        addCurrentLocationCircle(latitude, longitude);
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {
//        if(i > 8) {
//            mapView.removeAllPOIItems();
//
//            System.out.println("zoom level changed : " + i);
//        } else {
//            createVisitedMarker(mMapView, "임시 방문 명소", DEFAULT_MARKER_POINT);
//            createUnvisitedMarker(mMapView, "임시 미방문 명소", DEFAULT_MARKER_POINT1);
//            System.out.println("zoom level changed : " + i);
//        }
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int i, String s) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
//        Intent intent = new Intent(getContext(), ClickedMarkerDialog.class);
//        intent.putExtra("name",mapPOIItem.getItemName());
//        intent.putExtra("visited", mapPOIItem.getTag());
//        getContext().startActivity(intent);
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
        Intent intent = new Intent(getContext(), ClickedMarkerDialog.class);
        intent.putExtra("name",mapPOIItem.getItemName());
        intent.putExtra("visited", mapPOIItem.getTag());
        getContext().startActivity(intent);
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {
        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {
            boolean check_result = true;

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if ( check_result ) {
                ;
            }
            else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[1])) {
                    Toast.makeText(getContext(), "퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }else {
                    Toast.makeText(getContext(), "퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    void checkRunTimePermission(){
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])) {
                Toast.makeText(getContext(), "위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }

        }

    }
}
