package com.example.parentsupportapp.model;

public class Task {

    private String name;

    public Task (String name) {
        this.name = name;
    }

    public String getName () {
        return this.name;
    }

    public void setName (String name) {
        this.name = name;
    }

    @Override
    public String toString () {
        return this.name;
    }

}
