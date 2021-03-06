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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.antonis.bookaguide.data.MyMarker;
import com.antonis.bookaguide.data.Routes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

public class CustomRouteMap extends AppCompatActivity
        implements OnMapReadyCallback {
    final int REQUEST_CODE = 123;
    private int numMarker = 0;
    private ArrayList<Marker> markerList;
    private Button finishButton;
    private Button infoButton;
    LocationManager locationManager;
    private Location myLocation;
    LocationListener locationListener;
    public static Routes customRoute;
    private FirebaseAnalytics mFirebaseAnalytics;


    // [START_EXCLUDE]
    // [START maps_marker_get_map_async]
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.map_layout);
        mFirebaseAnalytics=FirebaseAnalytics.getInstance(this);

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        finishButton = findViewById(R.id.finish);
        infoButton = findViewById(R.id.info);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAction();
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }

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
                        Toast.makeText(CustomRouteMap.this,"myLocation is still null, couldn't retrieve last known location from GPS or Network provider",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
        }
    }





    @Override
    public void onMapReady(GoogleMap googleMap) {
        markerList = new ArrayList<>();
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                provideInfo(CustomRouteMap.this.getString(R.string.provideInfoMap),googleMap);
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "custom_route_info");
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

        locationListener= new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                myLocation=location;
            }
        };

        googleMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
                Toast.makeText(CustomRouteMap.this, "Current location:\n" + location, Toast.LENGTH_LONG)
                        .show();
            }
        });

        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
//                Toast.makeText(CustomRouteMap.this, "MyLocation button clicked", Toast.LENGTH_SHORT)
//                        .show();
                if (myLocation!=null){
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(myLocation.getLatitude(),
                                    myLocation.getLongitude()), 14));
                }else{
                    Toast.makeText(CustomRouteMap.this,"Cannot retrieve my location",Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });

        Log.d(MainActivity.LOGAPP,"My location is enabled: "+googleMap.isMyLocationEnabled());

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (numMarker<8) {
                    addMarkerWithDialog(CustomRouteMap.this.getString(R.string.addMarkerQuestion),googleMap,latLng);
                }else{
                    addLimitDialog(CustomRouteMap.this.getString(R.string.markerLimit),googleMap);
                }
            }
        });

    }

    private void addMarkerWithDialog(String message,GoogleMap map,LatLng latLng){
        new AlertDialog.Builder(CustomRouteMap.this)         //Alert dialog to add a marker
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Marker marker;
                        numMarker++;
                        marker = map.addMarker(new MarkerOptions()                   //marker is added to the map and to arraylist
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
    }

    private void addLimitDialog(String message,GoogleMap map){
        new AlertDialog.Builder(CustomRouteMap.this)             //can add up to 8 places
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {             //map is cleared from markers and user can start over
                        map.clear();
                        numMarker=0;
                        markerList.clear();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void finishAction(){
        if (!markerList.isEmpty()){
            Log.d(MainActivity.LOGAPP,"create custom route");
            customRoute=new Routes("Custom Route",false,new com.antonis.bookaguide.data.LatLng(markerList.get(0).getPosition().latitude,markerList.get(0).getPosition().longitude),
                    new com.antonis.bookaguide.data.LatLng(markerList.get(markerList.size()-1).getPosition().latitude,markerList.get(markerList.size()-1).getPosition().longitude),markerList.size());
            customRoute.setPointsToVisit(markerToMyMarker(markerList));
            Log.d(MainActivity.LOGAPP,"New custom route created "+customRoute.toString());
            MainActivity.setRoute(customRoute);
            Bundle params = new Bundle();
            params.putString("user_email_customMap", MainActivity.auth.getCurrentUser().getEmail());
            mFirebaseAnalytics.logEvent("custom_route_created", params);
        }
        Intent intent=new Intent(CustomRouteMap.this,MainActivity.class);
        startActivity(intent);
    }

    private ArrayList<MyMarker> markerToMyMarker(ArrayList<Marker> googleMarkerList){
        ArrayList<MyMarker> myMarkerList=new ArrayList<>();
        for (Marker marker: googleMarkerList){
            MyMarker myMarker=new MyMarker(new com.antonis.bookaguide.data.LatLng(marker.getPosition().latitude,marker.getPosition().longitude),marker.getTitle());
            myMarkerList.add(myMarker);
        }
        return myMarkerList;
    }

    private void provideInfo(String message,GoogleMap map){
        new AlertDialog.Builder(CustomRouteMap.this)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {             //map is cleared from markers and user can start over
                        map.clear();
                        numMarker=0;
                        markerList.clear();
                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "custom_route_cleared");
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (locationManager!=null) locationManager.removeUpdates(locationListener);
        Log.d(MainActivity.LOGAPP,"locationlistener remove updates when onPause");
    }
    public void onBackPressed() {
        Intent intent=new Intent(CustomRouteMap.this,MainActivity.class);
        finish();
        startActivity(intent);
    }


}



