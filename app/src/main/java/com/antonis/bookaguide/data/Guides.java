package com.antonis.bookaguide.data;

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

}
