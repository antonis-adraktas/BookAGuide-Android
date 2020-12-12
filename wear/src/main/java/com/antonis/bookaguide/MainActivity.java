package com.antonis.bookaguide;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.wear.widget.SwipeDismissFrameLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;


public class MainActivity extends WearableActivity  implements OnMapReadyCallback {

    final int REQUEST_CODE = 123;
    LocationManager locationManager;
    private Location myLocation;
    LocationListener locationListener;

    private GoogleMap mMap;

    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        // Enables always on.
        setAmbientEnabled();

        setContentView(R.layout.activity_main);

        final SwipeDismissFrameLayout swipeDismissRootFrameLayout =
                (SwipeDismissFrameLayout) findViewById(R.id.swipe_dismiss_root_container);
        final FrameLayout mapFrameLayout = (FrameLayout) findViewById(R.id.map_container);

        // Enables the Swipe-To-Dismiss Gesture via the root layout (SwipeDismissFrameLayout).
        // Swipe-To-Dismiss is a standard pattern in Wear for closing an app and needs to be
        // manually enabled for any Google Maps Activity. For more information, review our docs:
        // https://developer.android.com/training/wearables/ui/exit.html
        swipeDismissRootFrameLayout.addCallback(new SwipeDismissFrameLayout.Callback() {
            @Override
            public void onDismissed(SwipeDismissFrameLayout layout) {
                // Hides view before exit to avoid stutter.
                layout.setVisibility(View.GONE);
                finish();
            }
        });

        // Adjusts margins to account for the system window insets when they become available.
        swipeDismissRootFrameLayout.setOnApplyWindowInsetsListener(
                new View.OnApplyWindowInsetsListener() {
                    @Override
                    public WindowInsets onApplyWindowInsets(View view, WindowInsets insets) {
                        insets = swipeDismissRootFrameLayout.onApplyWindowInsets(insets);

                        FrameLayout.LayoutParams params =
                                (FrameLayout.LayoutParams) mapFrameLayout.getLayoutParams();

                        // Sets Wearable insets to FrameLayout container holding map as margins
                        params.setMargins(
                                insets.getSystemWindowInsetLeft(),
                                insets.getSystemWindowInsetTop(),
                                insets.getSystemWindowInsetRight(),
                                insets.getSystemWindowInsetBottom());
                        mapFrameLayout.setLayoutParams(params);

                        return insets;
                    }
                });

        // Obtain the MapFragment and set the async listener to be notified when the map is ready.
        MapFragment mapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Map is ready to be used.
        mMap = googleMap;

        // Inform user how to close app (Swipe-To-Close).
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(getApplicationContext(), R.string.intro_text, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        LatLngBounds atticaBounds = new LatLngBounds(
                new LatLng(37.882943, 23.657002), // SW bounds
                new LatLng(38.036468, 23.904720)  // NE bounds
        );
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(atticaBounds, 10));

        enableMyLocation(googleMap);
        googleMap.setLatLngBoundsForCameraTarget(atticaBounds);
        googleMap.setMinZoomPreference(11);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(atticaBounds.getCenter(), 11));

        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Toast.makeText(MainActivity.this, "MyLocation button clicked", Toast.LENGTH_SHORT)
                        .show();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(myLocation.getLatitude(),
                                myLocation.getLongitude()), 14));
                return false;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}