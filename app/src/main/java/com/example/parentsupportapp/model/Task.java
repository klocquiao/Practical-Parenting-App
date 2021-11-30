package com.example.parentsupportapp.model;

import com.google.gson.Gson;

import java.util.List;

/**
 * The task class contains key data for each task created by the user. It holds
 * the name of the task, history of task, as well as a priority queue that keeps track of whose turn it is.
 */

public class Task {
    private String name;
    private PriorityQueue childQueue;
    private HistoryManager taskHistory;

    public Task (String taskName, List<Child> children) {
        this.name = taskName;
        this.childQueue = new PriorityQueue(children, PriorityQueue.EMPTY);
        this.taskHistory = new HistoryManager(children, HistoryManager.EMPTY);
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
        addHistoryEntry(firstChild);
        childQueue.queueRecentlyUsed(firstChild);
    }

    public void addHistoryEntry(Child child) {
        HistoryEntry newEntry = new HistoryEntry(child);
        taskHistory.addHistoryEntry(newEntry);
    }

    public String getSerializedTaskHistory() {
        Gson gson = new Gson();
        String jsonHistory = gson.toJson(taskHistory.getHistory());
        return jsonHistory;
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
