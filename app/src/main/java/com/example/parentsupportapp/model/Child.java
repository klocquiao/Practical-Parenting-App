package com.example.parentsupportapp.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * this child class models a child that has a first name
 * and can be used to configure
 */
public class Child {
    private String firstName;
    private String portraitPath;

    public Child(String fName) {
        this.firstName = fName;
    }
    public Child(String fName, String port) {
        this.firstName = fName;
        this.portraitPath = port;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getPortraitPath() {
        return portraitPath;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setPortraitPath(String port) {
        this.portraitPath = port;
    }

    @NonNull
    @Override
    public String toString() {
        return firstName;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final Child other = (Child) obj;
        if (this.firstName.matches(other.getFirstName())) {
            return true;
        }
        else {
            return false;
        }
    }
}
