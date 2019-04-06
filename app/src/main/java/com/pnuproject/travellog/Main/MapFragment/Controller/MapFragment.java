package com.pnuproject.travellog.Main.MapFragment.Controller;

/**
 * Created by s0woo on 2019-04-02.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.pnuproject.travellog.R;

import net.daum.mf.map.api.MapLayout;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class MapFragment extends Fragment
        implements MapView.OpenAPIKeyAuthenticationResultListener, MapView.MapViewEventListener {

    private MapView mMapView;

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

        ViewGroup mapViewContainer = (ViewGroup) getView().findViewById(R.id.map_view);
        mapViewContainer.addView(mapLayout);
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {
        //
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(35.231005,129.0800193), 2, true);
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
}
