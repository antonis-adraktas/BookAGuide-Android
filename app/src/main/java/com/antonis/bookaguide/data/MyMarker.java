package com.antonis.bookaguide.data;

public class MyMarker {
    private LatLng latLng;
    private String title;

    public MyMarker() {
    }

    public MyMarker(LatLng latLng, String title) {
        this.latLng = latLng;
        this.title = title;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getTitle() {
        return title;
    }
}
