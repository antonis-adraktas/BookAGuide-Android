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
import android.os.Looper;
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
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Iterator;

public class RequestMap extends AppCompatActivity
        implements OnMapReadyCallback {
    final int REQUEST_CODE = 123;
    private Button infoButton;
    LocationManager locationManager;
    private Location myLocation;
    LocationListener locationListener;
    private Request myRequest;
    ArrayList<MyMarker> markerList;
    private FirebaseAnalytics mFirebaseAnalytics;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.my_map_layout);
        mFirebaseAnalytics=FirebaseAnalytics.getInstance(this);

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
        markerList=myRequest.getRoute().getPointsToVisit();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void enableMyLocation(GoogleMap map) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (map != null) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                myLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (myLocation==null){
                    myLocation=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (myLocation==null){
                        Toast.makeText(RequestMap.this,"myLocation is still null, couldn't retrieve last known location from GPS or Network provider",Toast.LENGTH_SHORT).show();
                    }
                }
                if (myLocation!=null){
                    locationListener= new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            myLocation=location;
                            showAlertWhenMarkerIsNear(location,markerList);
                        }
                    };
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,20,locationListener);
                }else{
                    Toast.makeText(RequestMap.this,"Location data is missing",Toast.LENGTH_SHORT).show();
                }

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
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "my_trip_map_info_button");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
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


        for (MyMarker marker: markerList){
            LatLng googleLatLng= new LatLng(marker.getLatLng().getLatitude(),marker.getLatLng().getLongitude());
            googleMap.addMarker(new MarkerOptions()
                    .position(googleLatLng))
                    .setTitle(marker.getTitle());

        }



//        showAlertWhenMarkerIsNear(myLocation,markerList);

        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (myLocation!=null){
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(myLocation.getLatitude(),
                                    myLocation.getLongitude()), 16));
                    showAlertWhenMarkerIsNear(myLocation,markerList);
                }else{
                    Toast.makeText(RequestMap.this,"Cannot retrieve my location",Toast.LENGTH_SHORT).show();
                }
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
//         Use iterator to avoid ConcurrentModificationException when removing items
//         Items are removed so that notification appears only once when distance criteria is met and not spamming the user while location changes
        Iterator<MyMarker> itr=myMarkerArrayList.iterator();
        while (itr.hasNext()) {
            MyMarker marker=itr.next();
            LatLng googleLatLng=new LatLng(marker.getLatLng().getLatitude(),marker.getLatLng().getLongitude());
            float distance[]={0};
            Location.distanceBetween(location.getLatitude(),location.getLongitude(),googleLatLng.latitude,googleLatLng.longitude,distance);
            Log.d(MainActivity.LOGAPP,"distance between my location and marker "+marker.getTitle()+" is "+String.valueOf(distance[0]));
            if (distance[0]<100){
                showAlertDialogWithAutoDismiss("Next stop: \n"+marker.getTitle());
                Bundle params = new Bundle();
                params.putString("user_email_alert_near", MainActivity.auth.getCurrentUser().getEmail());
                mFirebaseAnalytics.logEvent("marker_near_alert", params);
                itr.remove();
            }
        }
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
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()){
                    alertDialog.dismiss();
                }
            }
        }, 5000); //change 5000 with a specific time you want
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (locationManager!=null &&myLocation!=null) locationManager.removeUpdates(locationListener);
        Log.d(MainActivity.LOGAPP,"locationlistener remove updates when onPause");
    }


    public void onBackPressed() {
            Intent intent=new Intent(RequestMap.this,MyRequests.class);
            finish();
            startActivity(intent);
    }


}
