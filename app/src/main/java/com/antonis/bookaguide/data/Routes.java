package com.antonis.bookaguide.data;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

public class Routes {
    private String name;
    private boolean onFoot;
    private com.antonis.bookaguide.data.LatLng startingPoint;
    private com.antonis.bookaguide.data.LatLng endPoint;
    private ArrayList<Marker> pointsToVisit;
    private int numPlaces;
    private ArrayList<String> placesToVisit;

    public Routes() {
    }

    public Routes(String name, boolean onFoot, com.antonis.bookaguide.data.LatLng startingPoint, com.antonis.bookaguide.data.LatLng endPoint, int numPlaces) {
        this.name = name;
        this.onFoot = onFoot;
        this.startingPoint = startingPoint;
        this.endPoint = endPoint;
        this.numPlaces = numPlaces;
        pointsToVisit=new ArrayList<>();
        placesToVisit=new ArrayList<>();
    }



    public String getName() {
        return name;
    }

    public boolean isOnFoot() {
        return onFoot;
    }

    public LatLng getStartingPoint() {
        return startingPoint;
    }

    public LatLng getEndPoint() {
        return endPoint;
    }

    public int getNumPlaces() {
        return numPlaces;
    }

    public ArrayList<String> getPlacesToVisit() {
        return placesToVisit;
    }

    public ArrayList<Marker> getPointsToVisit() {
        return pointsToVisit;
    }

    public void setPointsToVisit(ArrayList<Marker> pointsToVisit) {
        this.pointsToVisit = pointsToVisit;
    }

    public void addPlace (String s){
        placesToVisit.add(s);
    }

    @NonNull
    @Override
    public String toString() {
        return "Name: "+name+"\n"+"onFoot: "+onFoot+"\n"+"Start point: "+startingPoint.toString()+"\n"
                +"end point: "+endPoint.toString();
    }
}
