package com.example.parentsupportapp.model;

import androidx.annotation.NonNull;

/**
 * this child class models a child that has a first name
 * and can be used to configure
 */
public class Child {
    private String firstName;

    public Child(String fName) {
        this.firstName = fName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @NonNull
    @Override
    public String toString() {
        return firstName;
    }
}
