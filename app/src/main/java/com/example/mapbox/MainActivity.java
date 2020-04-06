package com.example.mapbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import com.mapbox.android.gestures.Utils;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngQuad;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Circle;
import com.mapbox.mapboxsdk.plugins.annotation.CircleManager;
import com.mapbox.mapboxsdk.plugins.annotation.CircleOptions;
import com.mapbox.mapboxsdk.plugins.annotation.OnCircleClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.OnCircleLongClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolLongClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.RasterLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.ImageSource;
import com.mapbox.mapboxsdk.utils.ColorUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mapbox.mapboxsdk.style.layers.Property.ICON_ROTATION_ALIGNMENT_VIEWPORT;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    MapView mapView;
    CircleManager circleManager;
    SymbolManager symbolManager;
    Symbol symbol;
    private static final String ID_IMAGE_SOURCE = "animated_image_source";
    private static final String ID_IMAGE_LAYER = "animated_image_layer";
    private final Random random = new Random();
    private static final String ID_ICON_AIRPORT = "airport";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, ("pk.eyJ1IjoidmluaG5ndXllbjAwMTEwMSIsImEiOiJjazhtdHR3amkwZm1wM21uc2ptdHVueDJ2In0.ihDe_tGKr2ui9tM7MBXJbw"));
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }

    private LatLng createRandomLatLng() {
        return new LatLng((random.nextDouble() * -180.0) + 90.0,
                (random.nextDouble() * -360.0) + 180.0);
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                LatLngQuad quad = new LatLngQuad(new LatLng(25.7836, -80.11725),
                        new LatLng(25.783548, -80.1397431334),
                        new LatLng(25.7680, -80.13964),
                        new LatLng(25.76795, -80.11725));
                style.addSource(new ImageSource(ID_IMAGE_SOURCE, quad, R.drawable.mac));
                style.addLayer(new RasterLayer(ID_IMAGE_LAYER, ID_IMAGE_SOURCE));
                CameraPosition position = new CameraPosition.Builder().target(new LatLng(25.76795, -80.11725)).zoom(10).tilt(20).build();
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1000);
            }
        });

    }

//    @Override
//    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
//        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
//            @Override
//            public void onStyleLoaded(@NonNull Style style) {
//                GeoJsonOptions geoJsonOptions = new GeoJsonOptions().withTolerance(0.4f);
//                symbolManager = new SymbolManager(mapView, mapboxMap, style, null, geoJsonOptions);
//                circleManager = new CircleManager(mapView, mapboxMap, style);
//                circleManager.addClickListener(new OnCircleClickListener() {
//                    @Override
//                    public void onAnnotationClick(Circle circle) {
//                        Toast.makeText(MainActivity.this, "here" + circle.getId(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//                circleManager.addLongClickListener(new OnCircleLongClickListener() {
//                    @Override
//                    public void onAnnotationLongClick(Circle circle) {
//                        Toast.makeText(MainActivity.this, circle.getId() + "", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                // create a fixed circle
//                CircleOptions circleOptions = new CircleOptions()
//                        .withLatLng(new LatLng(6.687337, 0.381457))
//                        .withCircleColor(ColorUtils.colorToRgbaString(Color.YELLOW))
//                        .withCircleRadius(12f)
//                        .withDraggable(true);
//                circleManager.create(circleOptions);
//                // random add circles across the globe
//                List<CircleOptions> circleOptionsList = new ArrayList<>();
//                for (int i = 0; i < 20; i++) {
//                    int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
//                    circleOptionsList.add(new CircleOptions()
//                            .withLatLng(createRandomLatLng())
//                            .withCircleColor(ColorUtils.colorToRgbaString(color))
//                            .withCircleRadius(8f)
//                            .withDraggable(true)
//                    );
//                }
//                circleManager.create(circleOptionsList);
//
//                SymbolOptions symbolOptions = new SymbolOptions()
//                        .withLatLng(new LatLng(6.687337, 0.381457))
//                        .withIconImage(ID_ICON_AIRPORT)
//                        .withIconSize(1.3f)
//                        .withSymbolSortKey(10.0f)
//                        .withDraggable(true);
//                symbol = symbolManager.create(symbolOptions);
//            }
//        });

}



