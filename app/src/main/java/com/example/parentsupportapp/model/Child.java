package com.example.parentsupportapp.model;

public class Child {
    private String firstName;
    private int numberOfFlips;


    public Child () {

        this.firstName = null;
        numberOfFlips = 0;
    }

    public Child(String fName) {

        this.firstName = fName;
        numberOfFlips = 0;
    }

    public String getFirstName() {

        return firstName;
    }
    public int getNumberOfFlips() {
        return numberOfFlips;
    }

    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }
    public void setNumberOfFlips(int numberOfFlips) {
        this.numberOfFlips = numberOfFlips;
    }
}
