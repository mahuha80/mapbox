package com.example.mapbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Line;
import com.mapbox.mapboxsdk.plugins.annotation.LineManager;
import com.mapbox.mapboxsdk.plugins.annotation.LineOptions;
import com.mapbox.mapboxsdk.plugins.annotation.OnLineClickListener;
import com.mapbox.mapboxsdk.plugins.building.BuildingPlugin;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.mapboxsdk.style.layers.FillExtrusionLayer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionBase;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionHeight;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionOpacity;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, Style.OnStyleLoaded {
    private MapView mapView;
    private BuildingPlugin buildingPlugin;
    private MapboxMap mapboxMap;
    private Double latitude = 21.3011229;
    private Double longitude = -157.851376;
    private List<Point> routeCoordinates;

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
//        mapboxMap.setStyle(Style.MAPBOX_STREETS, this);
//        mapboxMap.setStyle(Style.OUTDOORS, new Style.OnStyleLoaded() {
//            @Override
//            public void onStyleLoaded(@NonNull Style style) {
//
//                initRouteCoordinates();
//                style.addSource(new GeoJsonSource("line-source",
//                        FeatureCollection.fromFeatures(new Feature[]{Feature.fromGeometry(
//                                LineString.fromLngLats(routeCoordinates)
//                        )})));
//                style.addLayer(new LineLayer("linelayer", "line-source").withProperties(
//                        PropertyFactory.lineDasharray(new Float[]{0.01f, 2f}),
//                        PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
//                        PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
//                        PropertyFactory.lineWidth(5f),
//                        PropertyFactory.lineColor(Color.parseColor("#e55e5e"))
//                ));
//            }
//        });
    }

    private void initRouteCoordinates() {
        routeCoordinates = new ArrayList<>();
        routeCoordinates.add(Point.fromLngLat(latitude, longitude));
        LatLng latLng = getNewLatLong(longitude, latitude);
        routeCoordinates.add(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()));
        LatLng latLng1 = getNewLatLong(latLng.getLatitude(), latLng.getLongitude());
        routeCoordinates.add(Point.fromLngLat(latLng1.getLongitude(), latLng1.getLatitude()));
        LatLng latLng2 = getNewLatLong(latLng1.getLatitude(), latLng1.getLongitude());
        routeCoordinates.add(Point.fromLngLat(latLng2.getLongitude(), latLng2.getLatitude()));
        LatLng latLng3 = getNewLatLong(latLng2.getLatitude(), latLng2.getLongitude());
        routeCoordinates.add(Point.fromLngLat(latLng3.getLongitude(), latLng3.getLatitude()));
        LatLng latLng4 = getNewLatLong(latLng3.getLatitude(), latLng3.getLongitude());
        routeCoordinates.add(Point.fromLngLat(latLng4.getLongitude(), latLng4.getLatitude()));
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
            LineManager lineManager = new LineManager(mapView, mapboxMap, style);

            //create new lat long
            List<LatLng> latLngs = new ArrayList<>();
            List<LatLng> latLngs1 = new ArrayList<>();
            //lat long 1
            LatLng latLng = getNewLatLong(latitude, longitude);
            //add to lat long
            latLngs.add(new LatLng(latitude, longitude));
            latLngs.add(latLng);

            //add to lat long 1
            latLngs1.add(latLng);
            latLngs1.add(new LatLng(latitude, longitude));

            //line option 1
            LineOptions lineOptions = new LineOptions()
                    .withLatLngs(latLngs)
                    .withLineColor("#ff0000")
                    .withLineWidth(30f);
            //line option 2
            LineOptions lineOptions1 = new LineOptions()
                    .withLatLngs(latLngs1)
                    .withLineColor("#000000")
                    .withLineWidth(30f);

            //options list
            List<LineOptions> optionsList = new ArrayList<>();
            //add to options list
            optionsList.add(lineOptions);
            optionsList.add(lineOptions1);

            //create
            lineManager.create(lineOptions);

            lineManager.addClickListener(new OnLineClickListener() {
                @Override
                public void onAnnotationClick(Line line) {
                    Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                }
            });
            LineManager lineManager1 = new LineManager(mapView, mapboxMap, style);
            lineManager1.create(lineOptions1);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }

    public LatLng getNewLatLong(double latitude, double longitude) {
        double earth = 6378.137;
        double pi = Math.PI;
        double m = (1 / ((2 * pi / 360) * earth)) / 1000;  //1 meter in degree
        double new_latitude = latitude + (10 * m);
        double new_longitude = longitude + (10 * m) / Math.cos(latitude * (pi / 180));
        LocationO locationO = new LocationO(new_latitude, new_longitude);
        LatLng latLng = new LatLng(locationO.getLatitude(), locationO.getLongtidute());
        return latLng;
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
