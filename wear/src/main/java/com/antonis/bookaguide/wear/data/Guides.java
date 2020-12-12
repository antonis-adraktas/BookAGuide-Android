package com.antonis.bookaguide.wear.data;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Guides {
    private String name;
    private String spokenLanguages;
    private ArrayList<String> datesBooked;


    public Guides() {
    }

    public Guides(String name,String spokenLanguages) {
        this.name = name;
        this.spokenLanguages=spokenLanguages;
        datesBooked=new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getDatesBooked() {
        return datesBooked;
    }

    public String getSpokenLanguages() {
        return spokenLanguages;
    }

    public void addBookedDate(String s){
        datesBooked.add(s);
    }

    @NonNull
    @Override
    public String toString() {
        String dates="";
        if (datesBooked!=null){
            for (String s: datesBooked){
                dates=dates+s;
            }
        }else{
            dates="No bookings";
        }
        return "Name: "+name+"\n"+"Spoken languages: "+spokenLanguages
                +"\n"+"Dates booked: "+dates+"\n";
    }
}
