package com.antonis.bookaguide.data;

import androidx.annotation.NonNull;

public class LatLng {
    private Double latitude;
    private Double longitude;

    public LatLng() {}

    public LatLng(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    @NonNull
    @Override
    public String toString() {
        return "latitude: "+latitude+", longitude: "+longitude;
    }
}
