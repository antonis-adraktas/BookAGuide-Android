package com.antonis.bookaguide.data;

import java.util.ArrayList;

public class Transport {
    private String name;
    private int capacity;
    private ArrayList<String> datesBooked;

    public Transport() {
    }

    public Transport(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        datesBooked=new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public ArrayList<String> getDatesBooked() {
        return datesBooked;
    }

    public void addBookedDate(String s){
        datesBooked.add(s);
    }
}
