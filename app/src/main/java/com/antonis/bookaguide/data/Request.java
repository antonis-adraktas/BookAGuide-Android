package com.antonis.bookaguide.data;

import androidx.annotation.NonNull;

public class Request {
    private String userEmail;
    private String date;
    private Routes route;
    private Guides guide;
    private Transport transport;

    public Request() {
    }

    public Request(String userEmail, String date, Routes route, Guides guide, Transport transport) {
        this.userEmail = userEmail;
        this.date = date;
        this.route = route;
        this.guide = guide;
        this.transport = transport;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getDate() {
        return date;
    }

    public Routes getRoute() {
        return route;
    }

    public Guides getGuide() {
        return guide;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRoute(Routes route) {
        this.route = route;
    }

    public void setGuide(Guides guide) {
        this.guide = guide;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    @NonNull
    @Override
    public String toString() {
        return "User email: "+userEmail+"\n"+"Date: "+date+"\n"
                +"Route: "+route.toString()
                +"Guide: "+guide.toString()
                +"Transport: "+transport.toString();
    }
}
