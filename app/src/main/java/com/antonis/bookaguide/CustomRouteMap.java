package com.antonis.bookaguide;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class CustomRouteMap extends AppCompatActivity
        implements OnMapReadyCallback {

    // [START_EXCLUDE]
    // [START maps_marker_get_map_async]
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.map_layout);

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLngBounds atticaBounds = new LatLngBounds(
                new LatLng(37.882943, 23.657002), // SW bounds
                new LatLng(38.036468, 23.904720)  // NE bounds
        );
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(atticaBounds, 10));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(atticaBounds.getCenter(), 10));
        googleMap.setLatLngBoundsForCameraTarget(atticaBounds);
        googleMap.setMinZoomPreference(11);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                new AlertDialog.Builder(CustomRouteMap.this)
                        .setMessage(R.string.addMarkerQuestion)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                googleMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title("I add this place to my route."));
                            }
                        })
                        .setNegativeButton(android.R.string.cancel,null)
                        .setIcon(android.R.drawable.ic_dialog_map)
                        .show();
            }
        });



    }

}



