package com.antonis.bookaguide;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class CustomRouteMap extends AppCompatActivity
        implements OnMapReadyCallback {
    private int numMarker=0;
    private ArrayList<Marker> markerList;
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
        markerList=new ArrayList<>();
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
                if (numMarker<8) {
                    new AlertDialog.Builder(CustomRouteMap.this)         //Alert dialog to add a marker
                            .setMessage(R.string.addMarkerQuestion)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Marker marker;
                                    numMarker++;
                                    marker = googleMap.addMarker(new MarkerOptions()                   //marker is added to the map and to arraylist
                                            .position(latLng)
                                            .title("This is marker number " + numMarker + " in my route.")
                                            .draggable(true));
                                    markerList.add(marker);
                                    Log.d(MainActivity.LOGAPP, "A marker was added to the list. The position of the last marker added is "
                                            + markerList.get(markerList.size() - 1).getPosition().toString());
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .setIcon(android.R.drawable.ic_dialog_map)
                            .show();
                }else{
                    new AlertDialog.Builder(CustomRouteMap.this)             //can add up to 8 places
                            .setMessage(R.string.markerLimit)
                            .setPositiveButton(android.R.string.ok,null)
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {             //map is cleared from markers and user can start over
                                    googleMap.clear();
                                    numMarker=0;
                                    markerList.clear();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                }
            }
        });

    }

//    private void addMarkerDialog(String message){
//
//    }
//    private void mapOnClicklistenerAction implements DialogInterface.OnClickListener{
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//            Marker marker;
//    }

}



