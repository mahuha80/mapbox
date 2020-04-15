package com.example.mapbox;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.building.BuildingPlugin;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, Style.OnStyleLoaded {
    private MapView mapView;
    private BuildingPlugin buildingPlugin;
    private MapboxMap mapboxMap;
    private Double latitude = 21.3011229;
    private Double longitude = -157.851376;
    private List<Point> routeCoordinates;
    private List<Point> routeCoordinates1;
    private List<Point> routeCoordinates2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.ACCESS_TOKEN));
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        MainActivity.this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.OUTDOORS, this);
    }

    private void initRouteCoordinates() {
        routeCoordinates = new ArrayList<>();
        routeCoordinates1 = new ArrayList<>();
        routeCoordinates2 = new ArrayList<>();
        routeCoordinates.add(Point.fromLngLat(-118.39439114221236, 33.397676454651766));
        routeCoordinates.add(Point.fromLngLat(-118.39421054012909, 33.3976979945487));
        routeCoordinates1.add(Point.fromLngLat(-118.39438215905952,33.39766005386782));
        routeCoordinates1.add(Point.fromLngLat(-118.39420155697618,33.397681593922364));
        routeCoordinates2.add(Point.fromLngLat(-118.39437317590668,33.39764365309173));
        routeCoordinates2.add(Point.fromLngLat(-118.39419257382335,33.397665193304206));
    }


    @Override
    public void onStyleLoaded(@NonNull Style style) {
        initRouteCoordinates();
        style.addSource(new GeoJsonSource("line-source",
                FeatureCollection.fromFeatures(new Feature[]{Feature.fromGeometry(
                        LineString.fromLngLats(routeCoordinates)
                ), Feature.fromGeometry(
                        LineString.fromLngLats(routeCoordinates1)
                ), Feature.fromGeometry(
                        LineString.fromLngLats(routeCoordinates2)
                )})));
        style.addLayer(new LineLayer("linelayer", "line-source").withProperties(
                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                PropertyFactory.lineWidth(1f),
                PropertyFactory.lineColor(Color.parseColor("#e55e5e"))
        ));
    }

    public LatLng getNewLatLong(double latitude, double longitude) {
        double earth = 6378.137;
        double pi = Math.PI;
        double m = (1 / ((2 * pi / 360) * earth)) / 10000;  //1 meter in degree
        double new_latitude = latitude + (10 * m);
        double new_longitude = longitude + (10 * m) / Math.cos(latitude * (pi / 90));

        Log.e("TAG ",  "sss  " + new_latitude + "," + new_longitude);
        LatLng latLng = new LatLng(0, 0);

        Log.e("TAG ",  "sss  " + latLng.toString());
        return latLng;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

}
