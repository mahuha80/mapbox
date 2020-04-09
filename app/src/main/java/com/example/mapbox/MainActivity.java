package com.example.mapbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.android.gestures.Utils;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
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
import com.mapbox.mapboxsdk.plugins.building.BuildingPlugin;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshot;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshotter;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.RasterLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.ImageSource;
import com.mapbox.mapboxsdk.utils.ColorUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.app.PendingIntent.getActivity;
import static com.mapbox.mapboxsdk.style.layers.Property.ICON_ROTATION_ALIGNMENT_VIEWPORT;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener, View.OnClickListener {
    private static final String ID_IMAGE_SOURCE = "animated_image_source";
    private static final String ID_IMAGE_LAYER = "animated_image_layer";
    private final Random random = new Random();
    private BuildingPlugin buildingPlugin;
    private MapView mapView;
    private MarkerView markerView;
    private MarkerViewManager markerViewManager;
    private MapboxMap mapboxMap;
    private NotificationManager notificationManager;
    private MapSnapshotter mapSnapshotter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, ("pk.eyJ1IjoidmluaG5ndXllbjAwMTEwMSIsImEiOiJjazhtdHR3amkwZm1wM21uc2ptdHVueDJ2In0.ihDe_tGKr2ui9tM7MBXJbw"));
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        MainActivity.this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                mapboxMap.addOnMapClickListener(MainActivity.this);
                markerViewManager = new MarkerViewManager(mapView, mapboxMap);
                buildingPlugin3d(style);
                addView();
                addImage(style);
            }
        });
    }


    private void addImage(Style style) {
        mapboxMap.addMarker(new MarkerOptions().position(new LatLng(20.996280, 105.826810))).setTitle("PiraGo");
        //add Image
        LatLngQuad quad = new LatLngQuad(new LatLng(25.7836, -80.11725),
                new LatLng(25.783548, -80.1397431334),
                new LatLng(25.7680, -80.13964),
                new LatLng(25.76795, -80.11725));
        style.addSource(new ImageSource(ID_IMAGE_SOURCE, quad, R.drawable.logo));
        style.addLayer(new RasterLayer(ID_IMAGE_LAYER, ID_IMAGE_SOURCE));
    }

    private void addView() {
        View customView = LayoutInflater.from(MainActivity.this).inflate(R.layout.marker_view_bubble, null);
        customView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Button btn = customView.findViewById(R.id.btn);
        btn.setText("PiraGo");
        btn.setTextColor(getColor(R.color.colorPrimaryDark));
        Button btn1 = customView.findViewById(R.id.btn1);
        btn1.setText("Image");
        btn1.setTextColor(getColor(R.color.colorPrimaryDark));
        markerView = new MarkerView(new LatLng(20.996280, 105.826810), customView);
        markerViewManager.addMarker(markerView);
        btn.setOnClickListener(this);
        btn1.setOnClickListener(this);
    }

    private void buildingPlugin3d(Style style) {
        //building plugin
        buildingPlugin = new BuildingPlugin(mapView, mapboxMap, style);
        buildingPlugin.setMinZoomLevel(15f);
        buildingPlugin.setVisibility(true);
    }

    private void startSnapShot(final LatLngBounds latLngBounds, final int height, final int width) {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                if (mapSnapshotter == null) {
                    MapSnapshotter.Options options =
                            new MapSnapshotter.Options(width, height)
                                    .withCameraPosition(mapboxMap.getCameraPosition())
                                    .withStyle(style.getUri())
                                    .withRegion(latLngBounds);

                    mapSnapshotter = new MapSnapshotter(MainActivity.this, options);
                } else {
                    mapSnapshotter.setSize(width, height);
                    mapSnapshotter.setRegion(latLngBounds);
                }
                mapSnapshotter.start(new MapSnapshotter.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(MapSnapshot snapshot) {
                        createNotification(snapshot.getBitmap());
                    }
                });
            }
        });
    }

    private void createNotification(Bitmap bitmap) {
        final int notifyId = 1002;
        String id = "channel_id";
        if (notificationManager == null) {
            notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null) {
                NotificationChannel notificationChannel = notificationManager.getNotificationChannel(id);
                if (notificationChannel == null) {
                    notificationChannel = new NotificationChannel(id, "channel_name", NotificationManager.IMPORTANCE_HIGH);
                    notificationChannel.setDescription("channel_description");
                    notificationManager.createNotificationChannel(notificationChannel);
                }
            }
        }
        Intent intent = new Intent(this, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, id)
                .setContentTitle("content")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(("title"))
                .setContentText(("description"))
                .setContentIntent(getActivity(this, 0, intent, 0))
                .setLargeIcon(bitmap);
        Notification notification = builder.build();
        notificationManager.notify(notifyId, notification);
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        startSnapShot(mapboxMap.getProjection().getVisibleRegion().latLngBounds, mapView.getMeasuredHeight(), mapView.getMeasuredWidth());
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(20.996280, 105.826810)).zoom(10).tilt(20).build();
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                break;
            case R.id.btn1:
                CameraPosition cameraPosition1 = new CameraPosition.Builder().target(new LatLng(25.783548, -80.1397431334)).zoom(10).tilt(20).build();
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition1));
                break;
        }
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