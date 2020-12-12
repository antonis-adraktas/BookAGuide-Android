package com.antonis.bookaguide.wear.data;

import androidx.annotation.NonNull;


import java.util.ArrayList;

public class Routes {
    private String name;
    private boolean onFoot;
    private LatLng startingPoint;
    private LatLng endPoint;
    private ArrayList<MyMarker> pointsToVisit;
    private int numPlaces;
    private ArrayList<String> placesToVisit;

    public Routes() {
    }

    public Routes(String name, boolean onFoot, LatLng startingPoint, LatLng endPoint, int numPlaces) {
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

    public ArrayList<MyMarker> getPointsToVisit() {
        return pointsToVisit;
    }

    public void setPointsToVisit(ArrayList<MyMarker> pointsToVisit) {
        this.pointsToVisit = pointsToVisit;
    }

    public void addPlace (String s){
        placesToVisit.add(s);
    }

    @NonNull
    @Override
    public String toString() {
        return "Name: "+name+"\n"+"onFoot: "+onFoot+"\n"+"Start point: "+startingPoint.toString()+"\n"
                +"end point: "+endPoint.toString()+"\n"+"Number of places to visit: "+String.valueOf(numPlaces)+"\n";
    }
}
