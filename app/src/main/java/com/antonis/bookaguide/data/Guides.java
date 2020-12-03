package com.antonis.bookaguide.data;

import java.util.ArrayList;

public class Guides {
    private String name;
    private ArrayList<String> datesBooked;


    public Guides() {
    }

    public Guides(String name) {
        this.name = name;
        datesBooked=new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getDatesBooked() {
        return datesBooked;
    }

    public void addBookedDate(String s){
        datesBooked.add(s);
    }

}
