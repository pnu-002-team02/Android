package com.pnuproject.travellog.Main.MapFragment.Controller;

/**
 * Created by s0woo on 2019-04-02.
 */

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pnuproject.travellog.Main.MapFragment.Controller.Search.ListViewAdapter;
import com.pnuproject.travellog.Main.MapFragment.Controller.Search.SearchClass;
import com.pnuproject.travellog.Main.MapFragment.Controller.Search.SearchDialog;
import com.pnuproject.travellog.Main.MapFragment.Controller.Search.TransPath;
import com.pnuproject.travellog.R;
import com.pnuproject.travellog.etc.GpsTracker;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapLayout;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

public class MapFragment extends Fragment
        implements MapView.OpenAPIKeyAuthenticationResultListener, MapView.MapViewEventListener, MapView.POIItemEventListener{

    private MapView mMapView;

    private MapPOIItem mVisitedMarker;
    private MapPOIItem mUnvisitedMarker;
    private static final MapPoint DEFAULT_MARKER_POINT = MapPoint.mapPointWithGeoCoord(35.2295239,129.0881219);
    private static final MapPoint DEFAULT_MARKER_POINT1 = MapPoint.mapPointWithGeoCoord(35.2336123,129.078816);

    private EditText edit_search;
    private ImageButton btn_x, btn_search;
    private ImageButton btn_gps;
    private Button btn_close;

    private TextView gps;
    private GpsTracker gpsTracker;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    double latitude;
    double longitude;

    private ListViewAdapter adapter;
    private ListView listViewPlace, listVIewPath;
    public SearchDialog dialog;
    public ProgressDialog pdialog;
    public Handler handler = new Handler();

    public MapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapLayout mapLayout = new MapLayout(getActivity());
        mMapView = mapLayout.getMapView();
        mMapView.setDaumMapApiKey(getString(R.string.kakao_map_key));
        mMapView.setOpenAPIKeyAuthenticationResultListener(this);
        mMapView.setMapViewEventListener(this);
        mMapView.setMapType(MapView.MapType.Standard);
        mMapView.setPOIItemEventListener(this);

        ViewGroup mapViewContainer = (ViewGroup) getView().findViewById(R.id.map_view);
        mapViewContainer.addView(mapLayout);

        mMapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
        createVisitedMarker(mMapView, "임시 방문 명소", DEFAULT_MARKER_POINT);
        createUnvisitedMarker(mMapView, "임시 미방문 명소", DEFAULT_MARKER_POINT1);
        //createCustomMarker(mMapView);
        //createCustomBitmapMarker(mMapView);
        //showAll();

        edit_search = (EditText) view.findViewById(R.id.edit_search);
        btn_search = (ImageButton) view.findViewById(R.id.btn_search);
        btn_gps = (ImageButton) view.findViewById(R.id.gps_tracker);
        btn_x = (ImageButton) view.findViewById(R.id.btn_x);
        btn_close = (Button) view.findViewById(R.id.btn_close);

        listViewPlace = (ListView) getView().findViewById(R.id.search_list);
        listVIewPath = (ListView) getView().findViewById(R.id.search_list);
        //gps = (TextView) getView().findViewById(R.id.gpsvalue);
        gpsTracker = new GpsTracker(getContext());

        //longitude : 위도(y / 0 ~ 180), latitude : 경도(x / 0 ~ 90)
        longitude = gpsTracker.getLongitude();
        latitude = gpsTracker.getLatitude();

        //gps.setText("위도 : " + longitude + " 경도 : " + latitude);

        edit_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_SEARCH:
                        searchEvent();
                        return true;
                    default:
                        return false;
                }
            }
        });

        btn_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_search.setText("");
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchEvent();
            }
        });
        
        btn_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude,longitude), 2,true);
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_close.setVisibility(View.INVISIBLE);
                listVIewPath.setVisibility(View.INVISIBLE);
                listViewPlace.setVisibility(View.INVISIBLE);
                edit_search.setText("");
            }
        });
    }

    public void searchEvent(){
        String str = edit_search.getText().toString();
        SearchClass searchClass = new SearchClass();
        ArrayList<String[]> result;

        if(str == null || str.length() == 0){
            Toast.makeText(getContext(), "장소를 입력하세요.", Toast.LENGTH_SHORT).show();
        }
        else{
            final String[] user = new String[2];
            user[0] = Double.toString(longitude);
            user[1] = Double.toString(latitude);

            searchClass.findPlace(str);
            if(searchClass.getIsnormal() == false){
                Toast.makeText(getContext(), "장소를 다시 입력해 주세요", Toast.LENGTH_SHORT).show();
                return;
            }
            result = searchClass.getPlaceResult();
            int arrsize = result.size();

            btn_close.setVisibility(View.VISIBLE);
            listViewPlace.setVisibility(View.VISIBLE);
            adapter = new ListViewAdapter();

            for(int i = 0; i < arrsize; i++){
                if(result.get(i)[2].length() != 0){
                    adapter.addItem(result.get(i)[0], result.get(i)[2], "", result.get(i)[3], result.get(i)[4]);
                }
                else{
                    adapter.addItem(result.get(i)[0], result.get(i)[1], "", result.get(i)[3], result.get(i)[4]);
                }
            }

            listViewPlace.setAdapter(adapter);
            listViewPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    //리스트뷰 아이템 클릭했을 때
                    btn_close.setVisibility(View.INVISIBLE);
                    listViewPlace.setVisibility(View.INVISIBLE);

                    Bundle bundle = new Bundle();
                    bundle.putStringArray("user", user);
                    bundle.putStringArray("search", adapter.getItemInfo(i));

                    dialog = new SearchDialog();
                    dialog.setArguments(bundle);

                    dialog.setDL(new SearchDialog.dialogListener() {
                        @Override
                        public void callBack(int toSearch) {
                            if(toSearch == 1){
                                String[] p = new String[4];
                                p[0] = user[0];
                                p[1] = user[1];
                                p[2] = adapter.getItemInfo(i)[3];
                                p[3] = adapter.getItemInfo(i)[4];

                                SearchClass searchClass = new SearchClass();
                                searchClass.findPath(p, getContext());

                                progressDialog(searchClass, i);
                            }
                            else{
                                Toast.makeText(getContext(), "경로 찾기에 실패했습니다", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dialog.show(getActivity().getSupportFragmentManager(), "tag");
                }
            });
        }
    }

    public void progressDialog(final SearchClass s, final int i){
        pdialog = new ProgressDialog(getContext());
        pdialog.setMessage("검색 중");
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.show();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(s.getf() == true){
                    s.printPathTest();
                    showPathlist(adapter.getItemInfo(i)[0], s);
                    pdialog.dismiss();
                }
            }
        },3000);
    }

    public void showPathlist(String s, SearchClass sc){
        System.out.println("리스트뷰");
        edit_search.setText("현재위치 -> "+ s);
        ListViewAdapter adapter = new ListViewAdapter();
        btn_close.setVisibility(View.VISIBLE);
        listVIewPath.setVisibility(View.VISIBLE);

        ArrayList<TransPath> path = sc.getPathResult();
        int size = path.size();

        for(int i = 0; i < size; i++){
            adapter.addItem(path.get(i).getTraffic(), path.get(i).getPath(), path.get(i).getTime());
        }

        listVIewPath.setAdapter(adapter);
        listViewPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                //리스트뷰 아이템 클릭했을 때
                btn_close.setVisibility(View.INVISIBLE);
                listViewPlace.setVisibility(View.INVISIBLE);
                edit_search.setText("");
            }
        });
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
        mapView.selectPOIItem(mVisitedMarker, false);
        //mapView.setMapCenterPoint(DEFAULT_MARKER_POINT, false);
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
        mapView.selectPOIItem(mUnvisitedMarker, false);
        //mapView.setMapCenterPoint(DEFAULT_MARKER_POINT, false);
    }

//    private void showAll() {
//        int padding = 20;
//        float minZoomLevel = 7;
//        float maxZoomLevel = 10;
//        MapPointBounds bounds = new MapPointBounds(CUSTOM_MARKER_POINT, DEFAULT_MARKER_POINT);
//        mMapView.moveCamera(CameraUpdateFactory.newMapPointBounds(bounds, padding, minZoomLevel, maxZoomLevel));
//    }

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
        //mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);

        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude,longitude), 2, true);
        addCurrentLocationCircle(latitude, longitude);
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {
//        if(i > 3) {
//            mapView.removeAllPOIItems();
//        } else {
//            createVisitedMarker(mMapView, "임시 방문 명소", DEFAULT_MARKER_POINT);
//            createUnvisitedMarker(mMapView, "임시 미방문 명소", DEFAULT_MARKER_POINT1);
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
        Intent intent = new Intent(getContext(), ClickedMarkerDialog.class);
        intent.putExtra("name",mapPOIItem.getItemName());
        intent.putExtra("visited", mapPOIItem.getTag());
        getContext().startActivity(intent);
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

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
