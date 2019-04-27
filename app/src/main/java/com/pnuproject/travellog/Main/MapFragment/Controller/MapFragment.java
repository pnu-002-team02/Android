package com.pnuproject.travellog.Main.MapFragment.Controller;

/**
 * Created by s0woo on 2019-04-02.
 */

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pnuproject.travellog.R;
import com.pnuproject.travellog.etc.LocationClass;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapLayout;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapView;

import java.util.Map;

public class MapFragment extends Fragment
        implements MapView.OpenAPIKeyAuthenticationResultListener, MapView.MapViewEventListener, MapView.POIItemEventListener{

    private MapView mMapView;

    private MapPOIItem mDefaultMarker;
    private static final MapPoint DEFAULT_MARKER_POINT = MapPoint.mapPointWithGeoCoord(35.2295239,129.0881219);

    private EditText edit_search;
    private Button btn_search;

    private TextView gps;
    private LocationManager locationManager;
    private LocationClass locationClass;
    private LocationListener locationListener;

    public static String location;
    double latitude, lat;
    double longitude, lng;

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
        createDefaultMarker(mMapView);
        //createCustomMarker(mMapView);
        //createCustomBitmapMarker(mMapView);
        //showAll();

        edit_search = (EditText) view.findViewById(R.id.edit_search);
        btn_search = (Button) view.findViewById(R.id.btn_search);

//        locationClass = new LocationClass(getActivity());
//        locationClass.initLoc();
//        location = locationClass.getLoc();
//        System.out.println("check location : " + location);
//        locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);

        settingGPS();
        Location userLocation = getMyLocation();
        if(userLocation != null) {
            latitude = userLocation.getLatitude();
            longitude = userLocation.getLongitude();
            System.out.println("check location : " + latitude + " " + longitude);
        }

        gps = (TextView) getView().findViewById(R.id.gpsvalue);
        //gps.setText(location);
        gps.setText(latitude + " " + longitude);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = edit_search.getText().toString();

                if(str == null || str.length() == 0){
                    Toast.makeText(getContext(), "장소를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
                    //리스트뷰로 넘어감
                }
            }
        });

        gps = (TextView) getView().findViewById(R.id.gpsvalue);
        //gps.setText(location);
        gps.setText(latitude + " " + longitude);
    }

    public String findGPS(){
        String result = "";

        return result;
    }

    private Location getMyLocation() {
        Location currentLocation = null;
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
            getMyLocation();
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

            String locationProvider = LocationManager.GPS_PROVIDER;
            currentLocation = locationManager.getLastKnownLocation(locationProvider);

            if(currentLocation != null) {
                lng = currentLocation.getLongitude();
                lat = currentLocation.getLatitude();
                //System.out.println("in function : " + lng + " " + lat);
            }
        }
        return currentLocation;
    }

    private void settingGPS() {
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                // TODO 위도, 경도로 하고 싶은 것
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
    }


    // CalloutBalloonAdapter 인터페이스 구현
    class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {
        private final View mCalloutBalloon;

        public CustomCalloutBalloonAdapter() {
            mCalloutBalloon = getLayoutInflater().inflate(R.layout.custom_callout_balloon, null);
        }

        @Override
        public View getCalloutBalloon(MapPOIItem poiItem) {
            ((ImageView) mCalloutBalloon.findViewById(R.id.badge)).setImageResource(R.mipmap.ic_launcher);
            ((TextView) mCalloutBalloon.findViewById(R.id.title)).setText(poiItem.getItemName());
            ((TextView) mCalloutBalloon.findViewById(R.id.desc)).setText("Custom CalloutBalloon");
            return mCalloutBalloon;
        }

        @Override
        public View getPressedCalloutBalloon(MapPOIItem poiItem) {
            return null;
        }
    }

    private void createDefaultMarker(MapView mapView) {
        mDefaultMarker = new MapPOIItem();
        String name = "Default Marker";
        mDefaultMarker.setItemName(name);
        mDefaultMarker.setTag(0);
        mDefaultMarker.setMapPoint(DEFAULT_MARKER_POINT);
        mDefaultMarker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        mDefaultMarker.setSelectedMarkerType(MapPOIItem.MarkerType.BluePin);

        mapView.addPOIItem(mDefaultMarker);
        mapView.selectPOIItem(mDefaultMarker, false);
        mapView.setMapCenterPoint(DEFAULT_MARKER_POINT, false);
    }

//    private void showAll() {
//        int padding = 20;
//        float minZoomLevel = 7;
//        float maxZoomLevel = 10;
//        MapPointBounds bounds = new MapPointBounds(CUSTOM_MARKER_POINT, DEFAULT_MARKER_POINT);
//        mMapView.moveCamera(CameraUpdateFactory.newMapPointBounds(bounds, padding, minZoomLevel, maxZoomLevel));
//    }

    @Override
    public void onMapViewInitialized(MapView mapView) {
        //mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);

        settingGPS();
        Location userLocation = getMyLocation();
        if(userLocation != null) {
            latitude = userLocation.getLatitude();
            longitude = userLocation.getLongitude();
            //System.out.println("check location initialize : " + latitude + " " + longitude);
        }
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude,longitude), 2, true);
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

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
        System.out.println("marker clicked");
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}
