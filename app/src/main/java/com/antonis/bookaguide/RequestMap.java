package com.antonis.bookaguide;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.antonis.bookaguide.data.MyMarker;
import com.antonis.bookaguide.data.Request;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class RequestMap extends AppCompatActivity
        implements OnMapReadyCallback {
    final int REQUEST_CODE = 123;
    private Button infoButton;
    LocationManager locationManager;
    private Location myLocation;
    LocationListener locationListener;
    private Request myRequest;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.my_map_layout);

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        infoButton = findViewById(R.id.requestInfo);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        myRequest=MyRequests.getRequestSelected();
    }

    private void enableMyLocation(GoogleMap map) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (map != null) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                myLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
        }
    }

    public void onMapReady(GoogleMap googleMap) {

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                provideInfo(RequestMap.this.getString(R.string.myRequestInfo));
            }
        });
        LatLngBounds atticaBounds = new LatLngBounds(
                new LatLng(37.882943, 23.657002), // SW bounds
                new LatLng(38.036468, 23.904720)  // NE bounds
        );
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(atticaBounds, 10));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(atticaBounds.getCenter(), 11));
        enableMyLocation(googleMap);
        googleMap.setLatLngBoundsForCameraTarget(atticaBounds);
        googleMap.setMinZoomPreference(11);

        ArrayList<MyMarker> markerList=myRequest.getRoute().getPointsToVisit();
        for (MyMarker marker: markerList){
            LatLng googleLatLng= new LatLng(marker.getLatLng().getLatitude(),marker.getLatLng().getLongitude());
            googleMap.addMarker(new MarkerOptions()
                    .position(googleLatLng))
                    .setTitle(marker.getTitle());

        }

        locationListener= new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                myLocation=location;
                showAlertWhenMarkerIsNear(location,markerList);
            }
        };

        showAlertWhenMarkerIsNear(myLocation,markerList);

        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Toast.makeText(RequestMap.this, "MyLocation button clicked", Toast.LENGTH_SHORT)
                        .show();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(myLocation.getLatitude(),
                                myLocation.getLongitude()), 14));
                return false;
            }
        });




    }

    private void provideInfo(String message) {
        new AlertDialog.Builder(RequestMap.this)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void showAlertWhenMarkerIsNear(Location location, ArrayList<MyMarker> myMarkerArrayList){
        for (MyMarker marker: myMarkerArrayList){
            LatLng googleLatLng=new LatLng(marker.getLatLng().getLatitude(),marker.getLatLng().getLongitude());
            float distance[]={0};
            Location.distanceBetween(location.getLatitude(),location.getLongitude(),googleLatLng.latitude,googleLatLng.longitude,distance);
            Log.d(MainActivity.LOGAPP,"distance between my location and marker "+marker.getTitle()+" is "+String.valueOf(distance[0]));
            if (distance[0]<100){
                showAlertDialogWithAutoDismiss("Next stop: \n"+marker.getTitle());
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (locationManager!=null) locationManager.removeUpdates(locationListener);
        Log.d(MainActivity.LOGAPP,"locationlistener remove updates when onPause");
    }
    public void showAlertDialogWithAutoDismiss(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RequestMap.this);
        builder.setTitle("Approaching landmark")
                .setMessage(message)
                .setCancelable(false).setCancelable(false)
                .setPositiveButton("SKIP", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //this for skip dialog
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()){
                    alertDialog.dismiss();
                }
            }
        }, 5000); //change 5000 with a specific time you want
    }

    public void onBackPressed() {
            Intent intent=new Intent(RequestMap.this,MyRequests.class);
            finish();
            startActivity(intent);
    }


}
