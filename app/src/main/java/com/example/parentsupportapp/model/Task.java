package com.example.parentsupportapp.model;

public class Task {

    private String name;

    private PriorityQueue priorityQueue;

    public Task (String name) {
        this.name = name;
        this.priorityQueue = new PriorityQueue("");
    }

    public PriorityQueue getPriorityQueue() {
        return priorityQueue;
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
