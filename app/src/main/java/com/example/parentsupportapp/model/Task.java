package com.example.parentsupportapp.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;

/**
 * The task class contains key data for each task created by the user. It holds
 * the name of the task, as well as a priority queue that keeps track of whose turn it is.
 */

public class Task {
    private String name;
    private PriorityQueue childQueue;

    public Task (String taskName, PriorityQueue childQueue) {
        this.name = taskName;
        this.childQueue = childQueue;
    }

    public PriorityQueue getPriorityQueue() {
        return childQueue;
    }

    public String getNextChildInQueueName() {
        return childQueue.getNextInQueue().getFirstName();
    }

    public String getNextChildInQueueImage() {
        return childQueue.getNextInQueue().getPortraitPath();
    }

    public void moveFirstChildToBack() {
        Child firstChild = childQueue.getNextInQueue();
        childQueue.queueRecentlyUsed(firstChild);
    }

    public String getTaskName() {
        return this.name;
    }

    public void setTaskName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
