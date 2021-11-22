package com.example.parentsupportapp.model;

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
