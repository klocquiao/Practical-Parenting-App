package com.example.parentsupportapp.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.UUID;

/**
 * this child class models a child that has a first name
 * and can be used to configure
 */

public class Child {
    public static final String PNG = ".png";
    private String firstName;
    private String portraitPath;
    private String uniqueID;

    public Child(String fName) {
        this.firstName = fName;
        this.uniqueID = UUID.randomUUID().toString();
        this.portraitPath = this.uniqueID + PNG;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public String getPortraitPath() {
        return portraitPath;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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
        if (this.uniqueID.matches(other.getUniqueID())) {
            return true;
        }
        else {
            return false;
        }
    }
}
