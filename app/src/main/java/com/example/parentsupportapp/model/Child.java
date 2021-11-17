package com.example.parentsupportapp.model;

import androidx.annotation.NonNull;

/**
 * this child class models a child that has a first name
 * and can be used to configure
 */
public class Child {
    private String firstName;
    private String portrait;

    public Child(String fName) {
        this.firstName = fName;
    }
    public Child(String fName, String port) {
        this.firstName = fName;
        this.portrait = port;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getPortrait() {
        return portrait;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setPortrait(String port) {
        this.portrait = port;
    }

    @NonNull
    @Override
    public String toString() {
        return firstName;
    }
}
