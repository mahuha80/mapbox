package com.example.mapbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.building.BuildingPlugin;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.mapboxsdk.style.layers.FillExtrusionLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.net.URI;
import java.net.URISyntaxException;

import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionBase;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionHeight;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionOpacity;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, Style.OnStyleLoaded {
    private MapView mapView;
    private BuildingPlugin buildingPlugin;
    private MapboxMap mapboxMap;

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
        mapboxMap.setStyle(Style.MAPBOX_STREETS, this);
        MarkerViewManager markerViewManager = new MarkerViewManager(mapView, mapboxMap);
        View view = LayoutInflater.from(this).inflate(R.layout.custom_view, null, false);
        MarkerView markerView = new MarkerView(new LatLng(21.3011229, -157.851376), view);
        LocationO locationO = getNewLatLong(21.3011229, -157.851376);
        MarkerView markerView1 = new MarkerView(new LatLng(locationO.getLatitude(), locationO.getLongtidute()), view);
        LocationO locationO1 = getNewLatLong(locationO.getLatitude(), locationO.getLongtidute());
        MarkerView markerView2 = new MarkerView(new LatLng(locationO1.getLatitude(), locationO1.getLongtidute()), view);
        markerViewManager.addMarker(markerView1);
    }

    @Override
    public void onStyleLoaded(@NonNull Style style) {
        //add building plugin 3d
        buildingPlugin = new BuildingPlugin(mapView, mapboxMap, style);
        buildingPlugin.setMinZoomLevel(15f);
        buildingPlugin.setVisibility(true);
        try {
            style.addSource(new GeoJsonSource("room-data", new URI("asset://indoor-3d-map.geojson")));
            style.addLayer(new FillExtrusionLayer(
                    "room-extrusion", "room-data").withProperties(
                    fillExtrusionColor(get("color")),
                    fillExtrusionHeight(get("height")),
                    fillExtrusionBase(get("base_height")),
                    fillExtrusionOpacity(0.5f)
            ));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }

    public LocationO getNewLatLong(double latitude, double longitude) {
        double earth = 6378.137;
        double pi = Math.PI;
        double m = (1 / ((2 * pi / 360) * earth)) / 1000;  //1 meter in degree
        double new_latitude = latitude + (10 * m);
        double new_longitude = longitude + (10 * m) / Math.cos(latitude * (pi / 180));
        LocationO locationO = new LocationO(new_latitude, new_longitude);
        return locationO;
    }

    // Add the mapView lifecycle to the activity's lifecycle methods
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